package com.swcm.scanandgo.rest;

import it.restrung.rest.marshalling.response.AbstractJSONResponse;

import java.util.ArrayList;

/**
 *  Esta clase alberga los campos "title", "description", "link", "images", "inventories"
 *  de un "porduct" que nos devuelve la API de GoogleShopping en la respuesta http. 
 *  Al extender a la clase AbstractJSONResponse (de RESTrung library) segun le llegan los campos 
 *  rellena esta clase. (Como hacer JSON.parse)
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class Product extends AbstractJSONResponse {
	private static final long serialVersionUID = 6288383807423849045L;
	private String title;
	private String description;
	private String link;
	private ArrayList<Image> images;
	private ArrayList<Inventory> inventories;

	public Product() {
		super();

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}

	public ArrayList<Inventory> getInventories() {
		return inventories;
	}

	public void setInventories(ArrayList<Inventory> inventories) {
		this.inventories = inventories;
	}

}
