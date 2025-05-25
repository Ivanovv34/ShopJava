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
    private List<Receipt> receipts = new ArrayList<>();

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

    // Add product to store inventory and accumulate delivery cost
    public void stockProduct(Product product, int quantity)
    {
        if (quantity <= 0)
        {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        inventory.put(product, inventory.getOrDefault(product, 0) + quantity);
        deliveryCosts += product.getDeliveryPrice() * quantity;
    }

    // Add cashier to the store and track salary costs
    public void addCashier(Cashier cashier)
    {
        cashiers.add(cashier);
        cashierCosts += cashier.getMonthlySalary();
    }

    // Process a sale: validate stock, apply pricing, generate receipt
    public Receipt sell(Cashier cashier, Customer customer, Map<Product, Integer> cart, LocalDate date)
            throws InsufficientStockException
    {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        double total = 0.0;

        // Check availability and validity for each product
        for (Map.Entry<Product, Integer> entry : cart.entrySet())
        {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int inStock = inventory.getOrDefault(product, 0);

            if (quantity <= 0)
            {
                throw new IllegalArgumentException("Requested quantity must be positive.");
            }

            if (inStock < quantity)
            {
                throw new InsufficientStockException(product.getName(), quantity, inStock);
            }

            if (product.getExpiryDate().isBefore(date))
            {
                throw new IllegalArgumentException("Product " + product.getName() + " is already expired");
            }

            double price = product.getSellingPrice(date, expiryThresholdDays, discountPercentage);
            receiptItems.add(new ReceiptItem(product, quantity, price));
            total += price * quantity;
        }

        // Validate customer's balance
        if (customer.getBalance() < total)
        {
            throw new IllegalArgumentException("Customer cannot afford the purchase");
        }

        // Deduct total from customer balance and update inventory
        customer.deduct(total);
        for (Map.Entry<Product, Integer> entry : cart.entrySet())
        {
            Product product = entry.getKey();
            inventory.put(product, inventory.get(product) - entry.getValue());
        }

        // Generate and save receipt
        Receipt receipt = new Receipt(this.name, cashier, receiptItems);
        receipts.add(receipt);
        //System.out.println(receipt);

        receipt.saveToFile();
        receipt.serialize();

        return receipt;
    }

    // Calculate total revenue from all sales
    public double getTotalRevenue()
    {
        return receipts.stream()
                .mapToDouble(Receipt::getTotalAmount)
                .sum();
    }

    // Calculate store profit = revenue - costs
    public double getProfit()
    {
        return getTotalRevenue() - (deliveryCosts + cashierCosts);
    }

    // Return total number of issued receipts
    public int getReceiptCount()
    {
        return receipts.size();
    }

    // Return all issued receipts
    public List<Receipt> getReceipts()
    {
        return receipts;
    }

    // Return all registered cashiers
    public List<Cashier> getCashiers()
    {
        return cashiers;
    }

    public void printInventory()
    {
        System.out.println("\n=== CURRENT INVENTORY ===");

        if (inventory.isEmpty())
        {
            System.out.println("No products in stock.");
        }
        else
        {
            for (Map.Entry<Product, Integer> entry : inventory.entrySet())
            {
                Product p = entry.getKey();
                int quantity = entry.getValue();
                System.out.printf("%-20s Qty: %d\n", p.getName(), quantity);
            }
        }
        System.out.println("==========================\n");
    }


    // Print summary of the day
    public void printSummary()
    {
        System.out.println("\n=== DAILY SUMMARY ===");
        System.out.printf("Total receipts issued: %d\n", getReceiptCount());
        System.out.printf("Total revenue: %.2f\n", getTotalRevenue());
        System.out.printf("Profit: %.2f\n", getProfit());

        System.out.println("Cashiers:");
        for (Cashier c : cashiers)
        {
            System.out.printf(" - %s (ID: %d, Salary: %.2f)\n", c.getName(), c.getId(), c.getMonthlySalary());
        }
        System.out.println("=======================\n");
    }

    // Optional: Get store name (used in Receipt formatting)
    public String getName()
    {
        return name;
    }
}
