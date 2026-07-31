package store.model.shipping;

public interface ShippingRule {

    double calculateShipping();

    String getName();
}