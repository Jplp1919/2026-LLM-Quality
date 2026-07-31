package store.model.promotion;

import java.util.Iterator;

import store.model.order.Order;
import store.model.order.OrderItem;

public class BuyXGetYPromotion implements Promotion {

    private String name;
    private String productId;
    private int buyQuantity;
    private int freeQuantity;

    public BuyXGetYPromotion(String name, String productId, int buyQuantity, int freeQuantity) {
        this.name = name;
        this.productId = productId;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
    }

    public double calculateDiscount(Order order) {
        Iterator iterator = order.getItems().iterator();

        while (iterator.hasNext()) {
            OrderItem item = (OrderItem) iterator.next();

            if (item.getProduct().getId().equals(productId)) {
                int groups = item.getQuantity() / (buyQuantity + freeQuantity);
                return groups * freeQuantity * item.getUnitPrice();
            }
        }

        return 0;
    }

    public String getName() {
        return name;
    }
}