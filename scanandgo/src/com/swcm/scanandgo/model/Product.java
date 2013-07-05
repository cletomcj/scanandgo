package com.swcm.scanandgo.model;

import java.io.Serializable;

import com.swcm.scanandgo.rest.GoogleShoppingResult;
import com.swcm.scanandgo.rest.Inventory;
import com.swcm.scanandgo.rest.Item;

/**
 * Esta clase simplifica a la clase Product del modelo REST, quedándonos sólo
 * con los mejores atributos de cada producto obteniendo dichos atributos del
 * resultado de la busqueda en GoogleShopping ("GoogleShoppingResult")
 * 
 * @author Carlos Martin-Cleto y Antonio Prada
 */
public class Product implements Serializable {

	private static final long serialVersionUID = -4868251503075445750L;

	public static final String TAG = "product";

	private long id;
	private String barcode;
	private String name;
	private String description;
	private String imageUrl;
	private double price;
	private String url;

	public Product() {
	}

	/**
	 * Construimos el producto con con el nombre y descripcion mas larga que
	 * encuentrre y también con el precio más bajo. Para ello recorremos todos
	 * los items devueltos en GoogleShoppingResult result
	 */
	public Product(String code, GoogleShoppingResult result) {
		//si no encuentra items la busqueda, el producto se crea con los atributos a null
		if (result != null && result.getItems() != null) {
			barcode = code;
			price = Double.MAX_VALUE;
			url = "";
			for (Item item : result.getItems()) {
				if (item.getProduct() != null) {
					com.swcm.scanandgo.rest.Product p = item.getProduct();
					// Utilizamos el nombre de mayor longitud de entre los
					// encontrados
					if (name == null && p.getTitle() != null) {
						if (name == null
								|| name.length() < p.getTitle().length()) {
							name = p.getTitle();
						}
					}
					// Utilizamos la descripción de mayor longitud
					if (p.getDescription() != null) {
						if (description == null
								|| description.length() < p.getDescription()
										.length()) {
							description = p.getDescription();
						}
					}
					// Utilizamos la primera imagen que encuentre
					if (imageUrl == null && p.getImages() != null
							&& p.getImages().get(0) != null) {
						imageUrl = p.getImages().get(0).getLink();
					}
					if (p.getInventories() != null) {
						// Utilizamos el precio más bajo que encuentre y nos
						// quedamos
						// con la web donde se vende a ese precio
						for (Inventory inventory : p.getInventories()) {
							if (inventory.getPrice() < price) {
								price = inventory.getPrice();
								url = p.getLink();
							}
						}
					}
				}
			}
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object o) {
		Product p = (Product) o;
		return p.getId() == id;
	}
}
