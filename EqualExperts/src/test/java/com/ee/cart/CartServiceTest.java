package com.ee.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ee.cart.persistence.domain.LineItem;
import com.ee.cart.persistence.domain.Product;
import com.ee.cart.persistence.domain.ShoppingCart;
import com.ee.cart.services.CartService;

@ExtendWith(SpringExtension.class)
@TestInstance(PER_CLASS)
@ContextConfiguration(classes = {CartService.class})
public class CartServiceTest {
	private static final int OUR_CUSTOMER_ID = 1;
	private static final String PRODUCT_DOVE_SOAP = "Dove Soap";
	private static final String PRODUCT_AXE_DEO = "Axe Deo";

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	@Qualifier(value = "cartService")
	private CartService cartService;

	@BeforeEach
	public void clear() throws Exception {
		cartService.clearCart(OUR_CUSTOMER_ID);
	}

	@BeforeAll
	public void initialize() throws Exception {
		logger.debug("In CartServiceTest.initialize()!");

		cartService.setTaxRate(12.5f);
		cartService.addProduct(PRODUCT_DOVE_SOAP, 39.99f);
		cartService.addProduct(PRODUCT_AXE_DEO, 99.99f);

		Collection<Product> products = cartService.listProducts();

		for (Product currProduct : products) {
			logger.debug(currProduct.toString());
		}
	}

	@AfterAll
	public void cleanup() throws Exception {
	}

	@Test
	public final void addSingleProduct() {
		ShoppingCart cart = cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 1);
		assertEquals(1, cart.getOrder().size(), 0);

		logger.debug("addSingleProduct:\n" + cart.toString());
	}

	@Test
	public final void addManyProducts() {
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 5);
		ShoppingCart cart = cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 3);
		List<LineItem> itemsList = cart.getOrder();
		assertEquals(1, itemsList.size(), 0);
		LineItem lineItem = itemsList.get(0);
		assertEquals(8, lineItem.getQuantity(), 0);
		assertEquals(319.92f, cart.getTotal(), 0);

		logger.debug("addManyProducts:\n" + cart.toString());
	}

	@Test
	public final void calculateTaxRateWithManyProducts() {
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 2);
		ShoppingCart cart = cartService.addLineItem(OUR_CUSTOMER_ID, "Axe Deo", 2);
		List<LineItem> itemsList = cart.getOrder();
		assertEquals(2, itemsList.size(), 0);
		LineItem lineItem = itemsList.get(0);
		assertTrue(PRODUCT_DOVE_SOAP.equals(lineItem.getProduct().getName()));
		assertEquals(2, lineItem.getQuantity(), 0);
		assertEquals(39.99f, lineItem.getProduct().getPrice(), 0);
		lineItem = itemsList.get(1);
		assertTrue(PRODUCT_AXE_DEO.equals(lineItem.getProduct().getName()));
		assertEquals(2, lineItem.getQuantity(), 0);
		assertEquals(99.99f, lineItem.getProduct().getPrice(), 0);

		assertEquals(35f, cart.getTax(), 0);
		assertEquals(314.96f, cart.getGrandTotal(), 0);

		logger.debug("calculateTaxRateWithManyProducts:\n" + cart.toString());
	}

	@Test
	public final void reduceQuantities1() {
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 9);
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_AXE_DEO, 7);
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, -3);
		ShoppingCart cart = cartService.addLineItem(OUR_CUSTOMER_ID, "Axe Deo", -2);
		List<LineItem> itemsList = cart.getOrder();
		assertEquals(2, itemsList.size(), 0);
		LineItem lineItem = itemsList.get(0);
		assertTrue(PRODUCT_DOVE_SOAP.equals(lineItem.getProduct().getName()));
		assertEquals(6, lineItem.getQuantity(), 0);
		assertEquals(239.94f, lineItem.getTotal(), 0);
		lineItem = itemsList.get(1);
		assertTrue(PRODUCT_AXE_DEO.equals(lineItem.getProduct().getName()));
		assertEquals(5, lineItem.getQuantity(), 0);
		assertEquals(499.95f, lineItem.getTotal(), 0);

		assertEquals(92.49f, cart.getTax(), 0);
		assertEquals(832.38f, cart.getGrandTotal(), 0);

		logger.debug("reduceQuantities1:\n" + cart.toString());
	}

	@Test
	public final void reduceQuantities2() {
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 2);
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_AXE_DEO, 7);
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, -3);
		ShoppingCart cart = cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_AXE_DEO, -2);
		List<LineItem> itemsList = cart.getOrder();
		assertEquals(1, itemsList.size(), 0);
		LineItem lineItem = itemsList.get(0);
		assertTrue(PRODUCT_AXE_DEO.equals(lineItem.getProduct().getName()));
		assertEquals(5, lineItem.getQuantity(), 0);
		assertEquals(499.95f, lineItem.getTotal(), 0);

		assertEquals(62.49f, cart.getTax(), 0);
		assertEquals(562.44f, cart.getGrandTotal(), 0);

		logger.debug("reduceQuantities2:\n" + cart.toString());
	}

	@Test
	public final void removeItemsFromCart() {
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP, 2);
		cartService.addLineItem(OUR_CUSTOMER_ID, PRODUCT_AXE_DEO, 2);
		ShoppingCart cart = cartService.removeLineItem(OUR_CUSTOMER_ID, PRODUCT_DOVE_SOAP);
		List<LineItem> itemsList = cart.getOrder();
		assertEquals(1, itemsList.size(), 0);
		LineItem lineItem = itemsList.get(0);
		assertTrue(PRODUCT_AXE_DEO.equals(lineItem.getProduct().getName()));
		assertEquals(2, lineItem.getQuantity(), 0);
		assertEquals(199.98f, lineItem.getTotal(), 0);

		assertEquals(25f, cart.getTax(), 0);
		assertEquals(224.98f, cart.getGrandTotal(), 0);

		logger.debug("calculateTaxRateWithManyProducts:\n" + cart.toString());
	}
}
