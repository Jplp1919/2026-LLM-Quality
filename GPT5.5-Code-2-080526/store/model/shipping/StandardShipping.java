package store.model.shipping;

public class StandardShipping implements ShippingRule {

    public String getName() {
        return "STANDARD";
    }

    public double calculateShipping() {
        return 12.0;
    }
}