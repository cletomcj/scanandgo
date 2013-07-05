package com.swcm.scanandgo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.swcm.scanandgo.R;
import com.swcm.scanandgo.model.Product;


/**
 *  Usaremos el mismo Dialog para mostrar un producto, cuando terminamos de escanear
 *  y cuando una vez añadido a la base de datos, queremos volver a ver sus características.
 *  Ambos Dialog se diferencian únicamente en un botón, si acabamos de escanearlo, aparecerá
 *  el botón "Añadir" y si ya lo teníamos guardado, aparecerá el botón "Email". 
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class ProductInfoDialog extends SherlockDialogFragment {

	public static final String TAG = "ProductInfoDialog";
	private static final String ADDED = "added";

	private Product mProduct;

	public interface OnProductAddListener {
		public void onProductAdd(Product product);
	}

	private OnProductAddListener mListener;

	/**
	 *  Creamos este metodo estático, que será un método de clase, no de objeto, 
	 *  así pues podremos invocarlo sin necesidad de crear un objeto de la clase
	 *  ProductInfoDialog. Esta es la mejor manera de instanciar un nuevo Fragment.
	 *  
	 *  p: Producto que mostraremos en el dialog
	 *  isAdded: "false" si lo acabamos de escanear el producto, y será "true" si ya
	 *  lo tenemos guardado y estamos consultando sus características
	 */
	public static ProductInfoDialog newInstance(Product p, boolean isAdded) {
		ProductInfoDialog dialog = new ProductInfoDialog();
		Bundle args = new Bundle();
		//guardamos estos dos atributos en un Bundle que después será usado
		//en el onCreateDialog()
		args.putSerializable(Product.TAG, p);
		args.putBoolean(ADDED, isAdded);
		dialog.setArguments(args);
		return dialog;
	}
	
	/**
	 * Este método es llamado cuando se asocia el Dialog a la
	 * activity. 
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verificamos que la actividad implemente a la interfaz OnProductAddListener
		// instanciamos el mListener  
		try {
			mListener = (OnProductAddListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnProductAddListener");
		}
	}

	/**
	 * Metodo que es llamado después de onAttach()
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Obtenemos los atributos guardados en el Bundle
		mProduct = (Product) getArguments().getSerializable(Product.TAG);
		final boolean isAdded = getArguments().getBoolean(ADDED);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (mProduct != null) {
			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();
			//inflamos la vista y la rellenamos con los datos del producto
			View v = inflater.inflate(R.layout.dialog_product, null);
			((TextView) v.findViewById(R.id.textViewName)).setText(mProduct.getName());
			((TextView) v.findViewById(R.id.textViewDescription))
					.setText(mProduct.getDescription());
			((TextView) v.findViewById(R.id.textViewPrice)).setText(mProduct.getPrice() + "€");
			ImageView iv = (ImageView) v.findViewById(R.id.imageViewProduct);
			//utilizamos la libreria Universal Image Loader para descargar imagenes de internet
			imageLoader.displayImage(mProduct.getImageUrl(), iv);
			//Una vez inflado y rellenado el contenido del dialog, lo añadimos al builder del mismo 
			//y por ultimo le añadimos los botones
			builder.setView(v)
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							 ProductInfoDialog.this.getDialog().cancel();
						}
					}).setNeutralButton(R.string.visit, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if (!isAdded) {
								mListener.onProductAdd(mProduct);
							}
							//ACTION_VIEW muestra la informacion suministrada por la URI
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(mProduct.getUrl()));
							startActivity(i);
						}
					});
			if (!isAdded) {
				builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener.onProductAdd(mProduct);
					}
				});
			} else {
				builder.setPositiveButton(R.string.email, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/html");
						intent.putExtra(Intent.EXTRA_SUBJECT, "[scan&go] " + mProduct.getPrice()
								+ "€ " + mProduct.getName());
						intent.putExtra(Intent.EXTRA_TEXT,
								mProduct.getDescription() + " \n" + mProduct.getUrl());
						startActivity(Intent.createChooser(intent, "Email"));
					}
				});
			}
		}
		return builder.create();
	}
}