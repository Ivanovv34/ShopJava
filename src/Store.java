import javax.naming.InsufficientResourcesException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store
{
    private String name;
    private Map<Product, Integer> inventory = new HashMap<>();
    private List<Cashier> cashiers = new ArrayList<>();
    private List<Receipt> receipts  = new ArrayList<>();

    private double deliveryCosts = 0.0;
    private double cashierCosts = 0.0;

    private int expiryThresholdDays;
    private double discountPercentage;

    public Store(String name, int expiryThresholdDays, double discountPercentage)
    {
        this.name = name;
        this.expiryThresholdDays = expiryThresholdDays;
        this.discountPercentage = discountPercentage;
    }

    public void stockProduct(Product product, int quantity)
    {
        inventory.put(product, inventory.getOrDefault(product, 0) + quantity);
        deliveryCosts += product.getDeliveryPrice() * quantity;
    }

    public void addCashier(Cashier cashier)
    {
        cashiers.add(cashier);
        cashierCosts += cashier.getMonthlySalary();
    }

    public Receipt sell(Cashier cashier, Customer customer, Map<Product, Integer> productToBuy, LocalDate date)
            throws InsufficientStockException
    {
        List<RecieptItem> receiptItems  = new ArrayList<>();
        double total = 0.0;

        for(Map.Entry<Product, Integer> entry : productToBuy.entrySet())
        {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            int inStock = inventory.getOrDefault(product, 0);

            if(inStock < quantity)
            {
                throw new InsufficientStockException(product.getName(),quantity, inStock);
            }

            if(product.getExpiryDate().isBefore(date))
            {
                throw new IllegalArgumentException("Product " + product.getName() + " is expired");
            }

            double price = product.getSellingPrice(date, expiryThresholdDays, discountPercentage);
            receiptItems.add(new RecieptItem(product, quantity, price));
            total += price * quantity;
        }

        if(customer.getBalance() < total)
        {
            throw new IllegalArgumentException("Customer cannot afford the purchase");
        }

        customer.deduct(total);
        for(Map.Entry<Product, Integer> entry : productToBuy.entrySet())
        {
            Product product = entry.getKey();
            inventory.put(product, inventory.get(product) - entry.getValue());
        }

        Receipt receipt = new Receipt(cashier,receiptItems);
        receipts.add(receipt);
        System.out.println(receipt);

        return receipt;

    }

    public double getTotalRevenue()
    {
        return receipts.stream()
                .mapToDouble(Receipt::getTotalAmount)
                .sum();
    }

    public double getProfit()
    {
        return getTotalRevenue() - (deliveryCosts + cashierCosts);
    }

    public int getReceiptCount()
    {
        return receipts.size();
    }
}
