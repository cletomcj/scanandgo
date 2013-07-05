package com.swcm.scanandgo;

import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.client.ContextAwareAPIDelegate;
import it.restrung.rest.client.RestClientFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.swcm.scanandgo.db.ProductsList;
import com.swcm.scanandgo.list.ProductsAdapter;
import com.swcm.scanandgo.model.Product;
import com.swcm.scanandgo.rest.GoogleShoppingResult;
import com.swcm.scanandgo.util.Log;
import com.swcm.scanandgo.view.ProductInfoDialog;
import com.swcm.scanandgo.view.ProductInfoDialog.OnProductAddListener;
import com.swcm.scanandgo.view.RemoveConfirmationDialog.OnProductDeleteListener;

/**
 * @author Carlos Martin-Cleto y Antonio Prada
 * @version 1.0
 */
public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener, OnProductAddListener, OnProductDeleteListener {

	private static final int ZBAR_SCANNER_REQUEST = 1;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private ProductsList mProductsList;
	private ProductsAdapter mAdapter;
	private SwipeListView swipeListView;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		Button b = (Button) findViewById(R.id.buttonScan);
		swipeListView = (SwipeListView) findViewById(R.id.listViewProducts);
		mProductsList = new ProductsList(mContext);
		// interconectamos la lista de productos con el swipeListView mediante
		// un ProductsAdapter
		mAdapter = new ProductsAdapter(mContext, mProductsList,
				getSupportFragmentManager(), swipeListView);
		swipeListView.setAdapter(mAdapter);
		b.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		mProductsList.close();
		super.onDestroy();
	}

	/**
	 * Al pulsar "Escanear" lanzamos la actividad "ZBarScannerActivity" con el
	 * metodo startActivityForResult(), el cual nos permite luego recoger el
	 * resultado, mediante el codigo ZBAR_SCANNER_REQUEST para distinguirlo del
	 * resto de Actividades que podamos estar esperando por un resultado
	 * 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonScan:
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
			break;
		}
	}

	/**
	 * En este metodo capturamos los intents devueltos por las actividades
	 * llamadas con el metodo startActivityForResult(). Distinguimos los intents
	 * de cada Activity haciendo un "switch" para cada requestCode.
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case ZBAR_SCANNER_REQUEST:
				// con getStringExtra() obtenemos la informacion extra que lleva
				// el Intent devuelto por la ZBarScannerActivity
				String code = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				if (code != null && !code.equals("")) {
					// Buscamos el codigo en GoogleShopping
					searchProductInfo(code);
				}
				break;
			// Actualizamos la base de datos con lo que hayamos editado en la
			// EditActivity
			case EditActivity.REQUEST_CODE:
				Product p = (Product) data.getSerializableExtra(Product.TAG);
				mProductsList.updateProduct(p);
				// refrescamos la SwipeListView,
				mAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	
	/**
	 * Este método se encarga de hacer la petición GET con el código de barras, 
	 * mostrar el progress dialog y cuando obtiene la información muestra el InfoDialog.
	 * @param code
	 */
	private void searchProductInfo(final String code) {
		// Mostramos un progress dialog hasta que encuentre los productos
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getText(R.string.please_wait));
		mProgressDialog.show();
		// Hacemos la peticion GET de forma asincrona y sencilla con la libreria
		// RESTrung y la API de googleShopping
		RestClientFactory.getClient().getAsync(
				new ContextAwareAPIDelegate<GoogleShoppingResult>(mContext,
						GoogleShoppingResult.class,
						RequestCache.LoadPolicy.NEVER) {
					// on response from server
					@Override
					public void onResults(GoogleShoppingResult response) {
						// creamos un Producto del paquete MODEL con los
						// atributos encontrados
						final Product p = new Product(code, response);
						//si el producto se ha creado con atributos a null es que no lo ha encontrado
						if (p.getBarcode() != null) {
							Runnable r = new Runnable() {

								@Override
								public void run() {
									mProgressDialog.dismiss();
									ProductInfoDialog dialog = ProductInfoDialog
											.newInstance(p, false);
									// una vez que ya hemos hecho
									// builder.create() del dialog...
									dialog.show(getSupportFragmentManager(),
											ProductInfoDialog.TAG);
								}
							};
							// tras encontrar el producto, esperamos 500ms a
							// cerrar el progress dialog pues se quedaba en blanco en la
							// transicion al ProductInfoDialog
							handler.postDelayed(r, 500);
						} else {
							mProgressDialog.dismiss();
							Toast.makeText(mContext, R.string.no_product, Toast.LENGTH_LONG).show();
						}

					}
					
					@Override
					public void onError(Throwable e) {
						mProgressDialog.dismiss();
						Toast.makeText(mContext, R.string.error_network,
								Toast.LENGTH_LONG).show();
						Log.e("error getting info " + e.getMessage());
					}
				},
				// request
				// con key = ... obtenemos el acceso a la API de googleShopping
				// donde esta %s sustituimos el parametro code
				"https://www.googleapis.com/shopping/search/v1/public/products"
						+ "?key=AIzaSyASEQZ5NOSO0FO_7g3g8mLO9PcynYyyIWg&country=US&q=%s&alt=json",
				code);

	}

	/**
	 * Este metodo es llamado desde el ProductInfoDialog, en el cual hemos
	 * asociado al boton "Añadir" un "OnProductAddListener" que ejecuta este
	 * método
	 * 
	 */
	@Override
	public void onProductAdd(Product product) {
		mProductsList.addProduct(product);
		// refrescamos la SwipeListView
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Este metodo es llamado desde el RemoveConfirmationDialog, en el cual
	 * hemos asociado al boton "Aceptar" un "OnProductDeleteListener" que
	 * ejecuta este método
	 */
	@Override
	public void onProductDelete(Product product) {
		mProductsList.deleteProduct(product);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Este metodo crea un Options Menu en la actionbar con un submenu de dos
	 * opciones Ayuda o Informacion.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		//utilizamos submenus para poder usar el método .setIcon() del actionBarSherlock
		SubMenu submenu = menu.addSubMenu(0, Menu.NONE, 1, R.string.menu_more)
				.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
		//siempre sea visible en la ActionBar
		submenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		submenu.add(0, 1, Menu.NONE, R.string.help);
		submenu.add(0, 2, Menu.NONE, R.string.about);
		submenu.add(0, 3, Menu.NONE, R.string.share);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Este metodo es llamado cuando seleccionamos un item del menu contextual
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			startActivity(new Intent(mContext, HelpActivity.class));
			return true;
		case 2:
			startActivity(new Intent(mContext, AboutActivity.class));
			return true;
		case 3:
		    Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");
		    intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_text));
		    startActivity(Intent.createChooser(intent, getText(R.string.share_chooser)));
		    return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
