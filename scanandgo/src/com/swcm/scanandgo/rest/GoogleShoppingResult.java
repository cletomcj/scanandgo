package com.swcm.scanandgo.rest;

import it.restrung.rest.marshalling.response.AbstractJSONResponse;
import java.util.ArrayList;

/**
 *  Esta clase alberga el campo "items" que nos devuelve la API de GoogleShopping 
 *  en la respuesta http. Al extender a la clase AbstractJSONResponse (de RESTrung library) 
 *  segun le llega el campo rellena esta clase. (Como hacer JSON.parse)
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class GoogleShoppingResult extends AbstractJSONResponse{

	
	private static final long serialVersionUID = -9201648767873922981L;
	
	private ArrayList<Item> items;

	public GoogleShoppingResult() {
        super();
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

}
