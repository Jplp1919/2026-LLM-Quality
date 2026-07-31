package com.store.order;

import com.store.catalog.CatalogService;
import com.store.catalog.Product;
import com.store.exception.StoreBusinessException;
import java.util.Map;

public class CartService {

    public Cart createCart(String customerId) {
        return new Cart(customerId);
    }

    public void addProduct(Cart cart, String productId, int quantity, CatalogService catalogService) {
        if (quantity <= 0) {
            throw new StoreBusinessException("Cart item quantities must be positive.");
        }
        Product product = catalogService.getProduct(productId);
        if (product == null) {
            throw new StoreBusinessException("Product not found.");
        }
        if (!product.isActive()) {
            throw new StoreBusinessException("Inactive products cannot be added to carts.");
        }

        Map<String, CartItem> items = cart.getItems();
        if (items.containsKey(productId)) {
            CartItem item = items.get(productId);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(productId, new CartItem(productId, quantity));
        }
    }

    public void updateQuantity(Cart cart, String productId, int quantity) {
        if (quantity <= 0) {
            throw new StoreBusinessException("Cart item quantities must be positive.");
        }
        Map<String, CartItem> items = cart.getItems();
        if (!items.containsKey(productId)) {
            throw new StoreBusinessException("Product not in cart.");
        }
        items.get(productId).setQuantity(quantity);
    }

    public void removeProduct(Cart cart, String productId) {
        cart.getItems().remove(productId);
    }

    public double calculateSubtotalPreview(Cart cart, CatalogService catalogService) {
        double subtotal = 0.0;
        for (CartItem item : cart.getItems().values()) {
            Product product = catalogService.getProduct(item.getProductId());
            subtotal += product.getUnitPrice() * item.getQuantity();
        }
        return subtotal;
    }
}