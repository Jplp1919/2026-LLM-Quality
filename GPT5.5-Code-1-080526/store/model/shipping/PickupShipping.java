package store.model.shipping;

public class PickupShipping implements ShippingRule {

    public double calculateShipping() {
        return 0;
    }

    public String getName() {
        return "PICKUP";
    }
}