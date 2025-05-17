import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt implements Serializable //tracks and formats all sale details into one object
{
    private static int counter = 1;

    private int serialNumber;
    private Cashier cashier;
    private LocalDateTime timestamp;
    private List<RecieptItem> items;
    private double totalAmount;

    public Receipt(Cashier cashier,List<RecieptItem> items)
    {
        this.serialNumber = counter++;
        this.cashier = cashier;
        this.timestamp = LocalDateTime.now();
        this.items = items;
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal()
    {
        return items.stream()
                .mapToDouble(RecieptItem::getTotalPrice)
                .sum();
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public List<RecieptItem> getItems()
    {
        return items;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt #").append(serialNumber).append("\n");
        sb.append("Cashier #").append(cashier.getName()).append("\n");
        sb.append("Date/Time: #").append(timestamp).append("\n");
        sb.append("Items:\n");
        for(RecieptItem item : items)
        {
            sb.append("  ").append(item).append("\n");
        }
        sb.append(String.format("Total: %.2f", totalAmount)).append("\n");
        return sb.toString();
    }
}
