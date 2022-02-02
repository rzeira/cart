package com.ee.cart.services;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ee.cart.persistence.domain.Product;
import com.ee.cart.persistence.domain.ShoppingCart;

@Service("cartService")
public class CartService {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger();

	public Product addProduct(String name, float price) {
		Product product = new Product(name, price);
		EntityRepository.addProduct(product);

		return product;
	}

	public void clearCart(int customerId) {
		ShoppingCart cart = EntityRepository.getShoppingCart(customerId);

		if (cart != null) {
			cart.clear();
		}
	}

	public ShoppingCart addLineItem(int customerId, int productId, int quantity) {
		ShoppingCart cart = getShoppingCart(customerId);
		cart.addLineItem(productId, quantity);

		return cart;
	}

	public ShoppingCart addLineItem(int customerId, String productName, int quantity) {
		ShoppingCart cart = getShoppingCart(customerId);
		cart.addLineItem(productName, quantity);

		return EntityRepository.getShoppingCart(customerId);
	}

	public ShoppingCart removeLineItem(int customerId, int productId) {
		ShoppingCart cart = getShoppingCart(customerId);
		cart.removeLineItem(productId);

		return EntityRepository.getShoppingCart(customerId);
	}

	public ShoppingCart removeLineItem(int customerId, String productName) {
		ShoppingCart cart = getShoppingCart(customerId);
		cart.removeLineItem(productName);

		return EntityRepository.getShoppingCart(customerId);
	}

	public ShoppingCart retrieveShoppingCart(int customerId) {
		return EntityRepository.getShoppingCart(customerId);
	}

	public Collection<Product> listProducts() {
		return EntityRepository.listProducts();
	}

	public void setTaxRate(float taxRatePercent) {
		ShoppingCart.setTaxRate(taxRatePercent);
	}

	public float getTaxRate() {
		return ShoppingCart.getTaxRate();
	}

	private ShoppingCart getShoppingCart(int customerId) {
		ShoppingCart cart = EntityRepository.getShoppingCart(customerId);

		if (cart == null) {
			cart = new ShoppingCart();
			EntityRepository.addShoppingCart(customerId, cart);
		}

		return cart;
	}
}
