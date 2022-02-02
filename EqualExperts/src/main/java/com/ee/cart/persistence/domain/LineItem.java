package com.ee.cart.persistence.domain;

import com.ee.cart.shared.Utils;

public class LineItem {
	private Product product = null;
	private int quantity = 0;
	private float total = 0.0f;

	public LineItem() {
	}

	public LineItem(Product product, int quantity) {
		setProduct(product);
		setQuantity(quantity);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		total = Utils.round(product.getPrice() * this.quantity);
	}

	public void adjustQuantity(int quantity) {
		setQuantity(this.quantity + quantity);
	}

	public float getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return
			"{ product: " + product.toString() +
			", quantity: " + quantity + ", total: " + total + " },";
	}
}
