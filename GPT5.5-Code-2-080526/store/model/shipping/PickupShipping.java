package store.model.shipping;

public class PickupShipping implements ShippingRule {

    public String getName() {
        return "PICKUP";
    }

    public double calculateShipping() {
        return 0.0;
    }
}