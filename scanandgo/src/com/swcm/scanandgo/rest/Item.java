package com.swcm.scanandgo.rest;

import it.restrung.rest.marshalling.response.AbstractJSONResponse;

/**
 *  Esta clase alberga al campo "product" de los "items" que nos devuelve la API
 *  de GoogleShopping en la respuesta http. Al extender a la clase 
 *  AbstractJSONResponse (de RESTrung library) segun le llega el campo "product"
 *  rellena esta clase. (Como hacer JSON.parse)
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class Item extends AbstractJSONResponse {

	private static final long serialVersionUID = -5594946888152935913L;
	private Product product;

	public Item() {
		super();

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
