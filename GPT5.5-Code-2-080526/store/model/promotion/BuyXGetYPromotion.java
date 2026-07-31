package store.model.promotion;

import java.util.List;

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

    public String getName() {
        return name;
    }

    public double calculateDiscount(Order order) {
        List<OrderItem> items = order.getItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            OrderItem item = (OrderItem) items.get(i);

            if (item.getProduct().getId().equals(productId)) {
                int groupSize = buyQuantity + freeQuantity;
                int groups = item.getQuantity() / groupSize;

                return groups * freeQuantity * item.getUnitPrice();
            }
        }

        return 0;
    }
}