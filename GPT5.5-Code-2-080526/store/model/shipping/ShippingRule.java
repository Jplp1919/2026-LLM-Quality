package store.model.shipping;

public interface ShippingRule {

    String getName();

    double calculateShipping();
}