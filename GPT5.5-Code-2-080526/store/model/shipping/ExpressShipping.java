package store.model.shipping;

public class ExpressShipping implements ShippingRule {

    public String getName() {
        return "EXPRESS";
    }

    public double calculateShipping() {
        return 25.0;
    }
}