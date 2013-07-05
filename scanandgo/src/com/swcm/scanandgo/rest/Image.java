package com.swcm.scanandgo.rest;

import it.restrung.rest.marshalling.response.AbstractJSONResponse;

/**
 *  Esta clase alberga los campos "link" y "status" de las "images" que nos devuelve 
 *  la API de GoogleShopping en la respuesta http. Al extender a la clase AbstractJSONResponse
 *  (de RESTrung library) segun le llegan los campos rellena esta clase. (Como hacer JSON.parse)
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class Image extends AbstractJSONResponse {

	private static final long serialVersionUID = 5177150968564953310L;
	
	private String link;
	private String status;
	
	public Image() {}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
