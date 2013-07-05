package com.swcm.scanandgo.rest;

import it.restrung.rest.marshalling.response.AbstractJSONResponse;

/**
 *  Esta clase alberga los campos "channel", "availability", "currency", "price".
 *  de los "inventories" que nos devuelve la API de GoogleShopping en la respuesta http. 
 *  Al extender a la clase AbstractJSONResponse (de RESTrung library) segun le llegan los campos 
 *  rellena esta clase. (Como hacer JSON.parse)
 *  
 *  @author Carlos Martin-Cleto y Antonio Prada
 */
public class Inventory extends AbstractJSONResponse {

	private static final long serialVersionUID = 6601423297854823082L;

	private String channel;
	private String availability;
	private String currency;
	private double price;

	public Inventory() {
		super();

	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
