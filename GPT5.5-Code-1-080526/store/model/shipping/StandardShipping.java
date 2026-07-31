package store.model.shipping;

public class StandardShipping implements ShippingRule {

    private double cost;

    public StandardShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShipping() {
        return cost;
    }

    public String getName() {
        return "STANDARD";
    }
}