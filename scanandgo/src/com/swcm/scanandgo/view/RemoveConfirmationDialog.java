package com.swcm.scanandgo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.swcm.scanandgo.R;
import com.swcm.scanandgo.model.Product;

/**
 * Esta clase define el dialog que nos saldrá cuando intentemos borrar un 
 * producto de la base de datos
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 */
public class RemoveConfirmationDialog extends DialogFragment {

	public static final String TAG = "removeconfirmationdialog";
	private Product mProduct;
	
	public interface OnProductDeleteListener {
		public void onProductDelete(Product product);
	}
	
	private OnProductDeleteListener mListener;

	/**
	 *  Creamos este metodo estático, que será un método de clase, no de objeto, 
	 *  así pues podremos invocarlo sin necesidad de crear un objeto de la clase.
	 *  Esta es la mejor manera de instanciar un nuevo Fragment.
	 *  
	 *  p: Producto que eliminaremos de la bd
	 */
	public static RemoveConfirmationDialog newInstance(Product p) {
		RemoveConfirmationDialog dialog = new RemoveConfirmationDialog();
		Bundle args = new Bundle();
		args.putSerializable(Product.TAG, p);
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
		// Verificamos que la actividad implemente a la interfaz OnProductDeleteListener
		try {
			mListener = (OnProductDeleteListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDeleteListener");
		}
	}

	/**
	 * Metodo que es llamado después de onAttach()
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		mProduct = (Product) getArguments().getSerializable(Product.TAG);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getText(R.string.delete_confirmation) + " \"" + mProduct.getName() + "\"?" )
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener.onProductDelete(mProduct);
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}