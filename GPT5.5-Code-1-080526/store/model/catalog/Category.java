package store.model.catalog;

public class Category {

    private String id;
    private String name;

    public Category(String id, String name) {
        if (id == null || name == null) {
            throw new IllegalArgumentException("Invalid category");
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