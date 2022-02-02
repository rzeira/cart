package com.ee.cart.persistence.domain;

public class Product {
	private static int maxId = 0;

	private int id = 0;
	private String name = null;
	private float price = 0.0f;

	public Product(String name, float price) {
		this.id = ++maxId;
		this.name = name;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "{ id: " + id + ", name: " + name + ", price: " + price + " },";
	}
}
