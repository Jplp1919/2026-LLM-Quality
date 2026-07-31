package store.model.shipping;

public class ExpressShipping implements ShippingRule {

    private double cost;

    public ExpressShipping(double cost) {
        this.cost = cost;
    }

    public double calculateShipping() {
        return cost;
    }

    public String getName() {
        return "EXPRESS";
    }
}