package com.ee.cart.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ee.cart.persistence.domain.Product;
import com.ee.cart.persistence.domain.ShoppingCart;

public class EntityRepository {
	private static Map<Integer, Product> productsByIdMap = new HashMap<>();
	private static Map<String, Product> productsByNameMap = new HashMap<>();
	private static Map<Integer, ShoppingCart> cartsMap = new HashMap<>();

	public static void addProduct(Product product) {
		productsByIdMap.put(product.getId(), product);
		productsByNameMap.put(product.getName(), product);
	}

	public static Product retrieveProductById(int id) {
		return productsByIdMap.get(id);
	}

	public static Product retrieveProductByName(String name) {
		return productsByNameMap.get(name);
	}

	public static ShoppingCart getShoppingCart(int customerId) {
		return cartsMap.get(customerId);
	}

	public static void addShoppingCart(int customerId, ShoppingCart cart) {
		cartsMap.put(customerId, cart);
	}

	public static Collection<Product> listProducts() {
		return productsByNameMap.values();
	}
}
