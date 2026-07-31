package store.model.catalog;

public class Product {

    private String id;
    private String name;
    private double unitPrice;
    private Category category;
    private boolean active;

    public Product(String id, String name, double unitPrice, Category category) {
        if (id == null || name == null || category == null) {
            throw new IllegalArgumentException("Invalid product");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Negative price");
        }
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }
}