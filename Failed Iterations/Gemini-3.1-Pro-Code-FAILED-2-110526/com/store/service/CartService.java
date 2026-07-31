package com.store.service;

import com.store.domain.Cart;
import com.store.domain.CartItem;
import com.store.domain.Customer;
import com.store.domain.Product;
import com.store.exception.StoreBusinessException;

public class CartService {

public Cart createCart(Customer customer) {
    return new Cart(customer);
}

public void addProductToCart(Cart cart, Product product, int quantity) {
    if (!product.isActive()) {
        throw new StoreBusinessException("Inactive products cannot be added to carts");
    }
    for (CartItem item : cart.getItems()) {
        if (item.getProduct().getId().equals(product.getId())) {
            item.setQuantity(item.getQuantity() + quantity);
            return;
        }
    }
    cart.getItems().add(new CartItem(product, quantity));
}

public void updateCartItemQuantity(Cart cart, String productId, int newQuantity) {
    for (CartItem item : cart.getItems()) {
        if (item.getProduct().getId().equals(productId)) {
            item.setQuantity(newQuantity);
            return;
        }
    }
    throw new StoreBusinessException("Product not found in cart");
}

public void removeProductFromCart(Cart cart, String productId) {
    CartItem toRemove = null;
    for (CartItem item : cart.getItems()) {
        if (item.getProduct().getId().equals(productId)) {
            toRemove = item;
            break;
        }
    }
    if (toRemove != null) {
        cart.getItems().remove(toRemove);
    }
}
}