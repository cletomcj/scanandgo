package com.swcm.scanandgo.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.swcm.scanandgo.model.Product;
import com.swcm.scanandgo.util.Log;

/**
 * @authors Carlos Martin-Cleto y Antonio Prada
 * 
 *  Esta clase se encarga de de la creación y los accesos a la base de datos
 *  Consiste en una base de datos con una única tabla, cada fila será un producto. 
 *  Dentro de cada producto tenemos 7 columnas.
 */
public class DatabaseAdapter {

	private static final String DATABASE_NAME = "scanandgo";
	private static final int DATABASE_VERSION = 3;
	private static final String TABLE_PRODUCTS = "products";

	// Columnas de cada objeto
	public static final String PRODUCT_ID = "_id";
	public static final String PRODUCT_BARCODE = "barcode";
	public static final String PRODUCT_NAME = "name";
	public static final String PRODUCT_DESCRIPTION = "description";
	public static final String PRODUCT_IMAGEURL = "image_url";
	public static final String PRODUCT_PRICE = "price";
	public static final String PRODUCT_URL = "url";

	//Sentencia SQL para crear la bd
	//autoincrement adjudica un _ID aleatorio al primer producto y va incrementando el _ID
	//por cada producto añadido, así evitamos especificar el _ID al insertar cada uno
	private static final String DATABASE_CREATE = "create table " + TABLE_PRODUCTS + " " + "("
			+ PRODUCT_ID + " integer primary key autoincrement, " + PRODUCT_BARCODE + " text, "
			+ PRODUCT_NAME + " text, " + PRODUCT_DESCRIPTION + " text, " + PRODUCT_URL + " text, "
			+ PRODUCT_PRICE + " real, " + PRODUCT_IMAGEURL + " text " + ");" 
			;

	private final Context mContext;
	//utilizaremos un DatabaseHelper para crear la bd y actualizar sus versiones
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDataBase;

	public DatabaseAdapter(Context ctx) {
		mContext = ctx;
		mDBHelper = new DatabaseHelper(mContext);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		//Se crea la bd, este método es llamado cuando se utiliza el método 
		//getWritableDataBase del mDBHelper
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		//En el caso de que la bd haya cambiado de version se vuelve a crear de nuevo
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Upgrading database from version " + oldVersion + " to " + newVersion);
			Log.w("Which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
			onCreate(db);
		}
	}

	// ---opens the database---
	public DatabaseAdapter open() throws SQLException {
		mDataBase = mDBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		mDBHelper.close();
	}

	public boolean isClosed() {
		return !mDataBase.isOpen();
	}

	/**
	 * Este método añade un producto a la base de datos y devuelve el ID
	 * asignado a ese producto en la base de datos
	 * @param p
	 * @return
	 */
	public long insertProduct(Product p) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PRODUCT_BARCODE, p.getBarcode());
		initialValues.put(PRODUCT_DESCRIPTION, p.getDescription());
		initialValues.put(PRODUCT_NAME, p.getName());
		initialValues.put(PRODUCT_IMAGEURL, p.getImageUrl());
		initialValues.put(PRODUCT_PRICE, p.getPrice());
		initialValues.put(PRODUCT_URL, p.getUrl());
		return mDataBase.insert(TABLE_PRODUCTS, null, initialValues);
	}

	public ArrayList<Product> getAllProducts() {
		ArrayList<Product> products = new ArrayList<Product>();
		//todos los nulls indican que incluimos en el cursor todos los elementos de la tabla
		Cursor cursor = mDataBase.query(true, TABLE_PRODUCTS, null, null, null, null, null, null,
				null);
		//recorremos con el cursor toda la bd, fila por fila, sacando los parametros de cada producto
		if (cursor.moveToFirst()) {
			do {
				Product p = new Product();
				p.setId(cursor.getLong(cursor.getColumnIndex(PRODUCT_ID)));
				p.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
				p.setBarcode(cursor.getString(cursor.getColumnIndex(PRODUCT_BARCODE)));
				p.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
				p.setImageUrl(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGEURL)));
				p.setPrice(cursor.getDouble(cursor.getColumnIndex(PRODUCT_PRICE)));
				p.setUrl(cursor.getString(cursor.getColumnIndex(PRODUCT_URL)));
				products.add(p);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return products;
	}

	public boolean deleteProduct(Product p) {
		return mDataBase.delete(TABLE_PRODUCTS, PRODUCT_ID + "=" + p.getId(), null) > 0;
	}

	public boolean updateProduct(Product p) {
		ContentValues args = new ContentValues();
		args.put(PRODUCT_BARCODE, p.getBarcode());
		args.put(PRODUCT_DESCRIPTION, p.getDescription());
		args.put(PRODUCT_IMAGEURL, p.getImageUrl());
		args.put(PRODUCT_NAME, p.getName());
		args.put(PRODUCT_PRICE, p.getPrice());
		args.put(PRODUCT_URL, p.getUrl());
		return mDataBase.update(TABLE_PRODUCTS, args, PRODUCT_ID + "=" + p.getId(), null) > 0;
	}

}