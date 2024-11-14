import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private double amount;
    private String description;
    private String category;
    private String date;
    private String type;

    public Transaction(int id, double amount, String description, String category, String date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s - ₹%.2f (%s) on %s", description, Math.abs(amount), category, date);
    }
}
