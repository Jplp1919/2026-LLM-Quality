package store.model.supplier;

public class Supplier {

    private String id;
    private String name;

    public Supplier(String id, String name) {
        if (id == null || name == null) {
            throw new IllegalArgumentException("Invalid supplier");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}