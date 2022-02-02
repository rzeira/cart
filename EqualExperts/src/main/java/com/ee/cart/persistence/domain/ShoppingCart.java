package com.ee.cart.persistence.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ee.cart.services.EntityRepository;
import com.ee.cart.shared.Utils;

public class ShoppingCart {
	private static float taxRate = 0;

	private int customerId = 0;
	private Map<Product, LineItem> order = new LinkedHashMap<>();
	private float total = 0.0f;
	private float tax = 0.0f;

	public void clear() {
		order.clear();
		total = 0.0f;
		tax = 0.0f;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public List<LineItem> getOrder() {
		List<LineItem> itemsList = new ArrayList<>();

		for (Product currProduct: order.keySet()) {
			itemsList.add(order.get(currProduct));
		}

		return itemsList;
	}

	public void addLineItem(int productId, int quantity) {
		addLineItem(EntityRepository.retrieveProductById(productId), quantity);
	}

	public void addLineItem(String productName, int quantity) {
		addLineItem(EntityRepository.retrieveProductByName(productName), quantity);
	}

	public void removeLineItem(int productId) {
		removeLineItem(EntityRepository.retrieveProductById(productId));
	}

	public void removeLineItem(String productName) {
		removeLineItem(EntityRepository.retrieveProductByName(productName));
	}

	public float getTotal() {
		return total;
	}

	public float getTax() {
		return tax;
	}

	public float getGrandTotal() {
		return total + tax;
	}

	public int getNumLineItems() {
		return order.size();
	}

	public static void setTaxRate(float taxRatePercent) {
		ShoppingCart.taxRate = taxRatePercent / 100;
	}

	public static float getTaxRate() {
		return ShoppingCart.taxRate * 100;
	}

	private void addLineItem(Product product, int quantity) {
		LineItem lineItem = order.get(product);

		if (lineItem == null) {
			lineItem = new LineItem(product, quantity);
			order.put(product, lineItem);
		} else {
			total = Utils.round(total - lineItem.getTotal());
			lineItem.adjustQuantity(quantity);
		}

		if (lineItem.getQuantity() <= 0) {
			order.remove(product);
		} else {
			total = Utils.round(total + lineItem.getTotal());
			tax = Utils.round(total * taxRate);
		}
	}

	private void removeLineItem(Product product) {
		LineItem lineItem = order.remove(product);

		if (lineItem != null) {
			total = Utils.round(total - lineItem.getTotal());
			tax = Utils.round(total * taxRate);
		}
	}

	@Override
	public String toString() {
		StringBuilder line =
			new StringBuilder("{ customerId: " + customerId + ", order: [\n");

		for (Product currProduct: order.keySet()) {
			line.append(order.get(currProduct) + "\n");
		}

		line.append(
			"],\ntotal: " + total + ", tax: " + tax + ", grandTotal: " + getGrandTotal() + "},");

		return line.toString();
	}
}
