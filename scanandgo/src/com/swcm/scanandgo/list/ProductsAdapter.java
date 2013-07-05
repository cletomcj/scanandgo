package com.swcm.scanandgo.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.swcm.scanandgo.EditActivity;
import com.swcm.scanandgo.R;
import com.swcm.scanandgo.db.ProductsList;
import com.swcm.scanandgo.model.Product;
import com.swcm.scanandgo.view.ProductInfoDialog;
import com.swcm.scanandgo.view.RemoveConfirmationDialog;

/**
 *  Esta clase define al Adapter que "interconecta" la swipeListView de la MainActivity
 *  con la lista de productos alamacenados en la db
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class ProductsAdapter extends BaseAdapter {

	private Context mContext;
	private ProductsList mProductsList;
	private LayoutInflater mInflater;
	private FragmentManager mFragmentManager;
	private SwipeListView mSwipeListView;
	
	public ProductsAdapter(Context context, ProductsList productsList, FragmentManager fragmentManager, SwipeListView swipeListView) {
		super();
		mContext = context;
		mProductsList = productsList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFragmentManager = fragmentManager;
		mSwipeListView = swipeListView;
	}

	/**
	 * Obtiene el numero de productos a representar por el Adapter 
	 */
	@Override
	public int getCount() {
		if(mProductsList.getProducts() == null){
			return 0;
		}
		return mProductsList.getProducts().size();
	}

	/**
	 * Devuelve el producto que está en esa posicion del Adapter
	 */
	@Override
	public Object getItem(int index) {
		return mProductsList.getProducts().get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	/**
	 *  En este método definimos cómo se van a "inflar" los productos en el SwipeListView.
	 *  Será llamado internamente por el SwipeListView para ver cómo representa 
	 *  cada Item (producto) individualmente.
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = mInflater.inflate(R.layout.product_row, null);

		final Product p = (Product) getItem(position);
		// R.id.parent: Vista superficial del Product_row (es clickable)
		RelativeLayout l = (RelativeLayout) v.findViewById(R.id.parent);
		l.setOnClickListener(new OnClickListener() {
			//Al hacer click sobre un item de la lista salta el ProductInfoDialog
			@Override
			public void onClick(View v) {
				ProductInfoDialog dialog = ProductInfoDialog.newInstance(p, true);
				dialog.show(mFragmentManager, ProductInfoDialog.TAG);
			}
		});
		//Añadimos el nombre del producto al textViewName
		TextView tn = (TextView) v.findViewById(R.id.textViewName);
		if (tn != null) {
			tn.setText(p.getName());
		}
		//Añadimos el precio del producto al textViewPrice
		TextView tp = (TextView) v.findViewById(R.id.textViewPrice);
		if (tp != null) {
			tp.setText(p.getPrice() + "€");
		}
		Button be = (Button) v.findViewById(R.id.buttonEdit);
		Button br = (Button) v.findViewById(R.id.buttonRemove);
		if (br != null) {
			br.setOnClickListener( new OnClickListener() {
				//Boton de borrar 
				@Override
				public void onClick(View v) {
					//cerramos el efecto del swipe antes de que salga el dialog
					mSwipeListView.closeAnimate(position);
					RemoveConfirmationDialog dialog = RemoveConfirmationDialog.newInstance(p);
					dialog.show(mFragmentManager, RemoveConfirmationDialog.TAG);
				}
			});
		}
		if (be != null) {
			be.setOnClickListener( new OnClickListener() {
				//Boton de editar
				@Override
				public void onClick(View v) {
					//cerramos el efecto del swipe entes de que salga el dialog
					mSwipeListView.closeAnimate(position);
					Intent i = new Intent(mContext, EditActivity.class);
					i.putExtra(Product.TAG, p);
					((Activity) mContext).startActivityForResult(i, EditActivity.REQUEST_CODE);
				}
			});
		}
		return v;
	}


}