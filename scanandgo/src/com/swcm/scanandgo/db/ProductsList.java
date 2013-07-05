package com.swcm.scanandgo.db;

import java.util.ArrayList;

import android.content.Context;

import com.swcm.scanandgo.model.Product;

/**
 *  Clase creada para simplificar el acceso a la base de datos.
 *  Contiene los prodcutos de la base de datos en un arrayList
 *  y cuyos metodos implementan la interfaz CRUD
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class ProductsList {
	
	private DatabaseAdapter databaseAdapter;
	private ArrayList<Product> products;
	
	public ProductsList(Context context) {
		databaseAdapter  = new DatabaseAdapter(context);
		databaseAdapter.open(); //se crea la base de datos vac’a
	}
	
	
	public void close() {
		databaseAdapter.close();
	}
	
	public void addProduct(Product p) {
		long id = databaseAdapter.insertProduct(p);
		if (id >= 0) {
			p.setId(id);
			products.add(p);
		}
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	public void deleteProduct(Product p) {
		products.remove(p);
		databaseAdapter.deleteProduct(p);
	}
	
	public void updateProduct(Product p) {
		products.set(products.indexOf(p), p);
		databaseAdapter.updateProduct(p);
	}

}
