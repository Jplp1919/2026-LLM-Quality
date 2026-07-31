package store.model.payment;

public class Payment {

    private String id;
    private double amount;

    public Payment(String id, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid payment");
        }

        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}