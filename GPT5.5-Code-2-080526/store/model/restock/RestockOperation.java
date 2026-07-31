package store.model.restock;

import store.model.product.Product;
import store.model.supplier.Supplier;

public class RestockOperation {

    private String id;
    private Product product;
    private Supplier supplier;
    private int quantity;
    private double unitCost;

    public RestockOperation(String id, Product product, Supplier supplier,
            int quantity, double unitCost) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (unitCost < 0) {
            throw new IllegalArgumentException("Negative cost");
        }

        this.id = id;
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getTotalCost() {
        return quantity * unitCost;
    }
}