import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.io.*;

public class Receipt implements Serializable //tracks and formats all sale details into one object
{
    private static final long serialVersionUID = 1L;
    private static int counter = 1;

    private String storeName;
    private int serialNumber;
    private Cashier cashier;
    private LocalDateTime timestamp;
    private List<ReceiptItem> items;
    private double totalAmount;

    public Receipt(String storeName, Cashier cashier, List<ReceiptItem> items) {
        this.storeName = storeName;
        this.serialNumber = counter++;
        this.cashier = cashier;
        this.timestamp = LocalDateTime.now();
        this.items = items;
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        return items.stream()
                .mapToDouble(ReceiptItem::getTotalPrice)
                .sum();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("=".repeat(40)).append("\n");
        sb.append(String.format("        *** %s ***\n", storeName)); // Store name
        sb.append(String.format("RECEIPT #%d\n", serialNumber));
        sb.append("=".repeat(40)).append("\n");
        sb.append(String.format("Cashier: %s\n", cashier.getName()));
        sb.append("Date/Time: ").append(timestamp.format(formatter)).append("\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("%-20s %5s %10s\n", "Item", "Quantity", "Total"));
        sb.append("-".repeat(40)).append("\n");

        for (ReceiptItem item : items) {
            String name = item.getProduct().getName();
            int quantity = item.getQuantity();
            double price = item.getTotalPrice();

            sb.append(String.format("%-20s %5d %10.2f\n", name, quantity, price));

            if (item.getProduct() instanceof FoodProduct) {
                double fullPrice = item.getProduct().getDeliveryPrice() *
                        (1 + item.getProduct().getMarkupPercentage() / 100.0);

                if (Math.abs(fullPrice - item.getUnitPrice()) > 0.01) {
                    sb.append("  → Discount applied (expiry soon)\n");
                }
            }
        }
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("%-26s %10.2f\n", "TOTAL:", totalAmount));
        sb.append("=".repeat(40)).append("\n");

        return sb.toString();
    }


    public void saveToFile() {
        try {
            File folder = new File("receipts");
            folder.mkdirs(); // create the folder if it doesn't exist

            String fileName = "receipts/receipt_" + serialNumber + ".txt";
            try (PrintWriter out = new PrintWriter(fileName)) {
                out.println(this.toString());
            }

            System.out.println("Receipt saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to save receipt: " + e.getMessage());
        }
    }


    public void serialize() {
        try {
            File folder = new File("receipts");
            folder.mkdirs();

            String fileName = "receipts/receipt_" + serialNumber + ".ser";
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                out.writeObject(this);
            }
        } catch (IOException e) {
            System.out.println("Failed to serialize receipt: " + e.getMessage());
        }
    }
}
