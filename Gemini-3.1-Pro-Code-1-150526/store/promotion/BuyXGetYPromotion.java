package store.promotion;

import store.model.Order;
import store.model.OrderItem;
import store.model.Product;

public class BuyXGetYPromotion implements Promotion {
    private Product targetProduct;
    private int buyX;
    private int getY;

    public BuyXGetYPromotion(Product targetProduct, int buyX, int getY) {
        this.targetProduct = targetProduct;
        this.buyX = buyX;
        this.getY = getY;
    }

    public double apply(Order order) {
        int targetQuantity = 0;
        double unitPrice = 0.0;

        for (OrderItem item : order.getItems()) {
            if (item.getProduct().getId().equals(targetProduct.getId())) {
                targetQuantity += item.getQuantity();
                unitPrice = item.getUnitPrice();
            }
        }

        if (targetQuantity == 0) {
            return 0.0;
        }

        int groupSize = buyX + getY;
        int freeGroups = targetQuantity / groupSize;
        int freeItems = freeGroups * getY;

        return freeItems * unitPrice;
    }
}