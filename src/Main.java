import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Store store = new Store("MyStore", 5, 20);
    private static final Map<Integer, Product> productCatalog = new HashMap<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Cashier> cashiers = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addProduct(true);
                case "2" -> addProduct(false);
                case "3" -> addCashier();
                case "4" -> simulateSale();
                case "5" -> viewReceipts();
                case "6" -> viewStats();
                case "7" -> loadReceiptFromFile();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== STORE MENU ===");
        System.out.println("1. Add Food Product");
        System.out.println("2. Add Non-Food Product");
        System.out.println("3. Add Cashier");
        System.out.println("4. Make a Sale");
        System.out.println("5. View All Receipts");
        System.out.println("6. View Sales & Profit Stats");
        System.out.println("7. Load Receipt from File");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addProduct(boolean isFood) {
        try {
            System.out.print("Enter product ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter delivery price: ");
            double price = Double.parseDouble(scanner.nextLine());
            LocalDate expiry;
            if (isFood)
            {
                System.out.print("Enter expiry date (YYYY-MM-DD): ");
                expiry = LocalDate.parse(scanner.nextLine());
            } else
            {
                expiry = LocalDate.now().plusYears(10); // default far-future expiry
            }

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            Product p = isFood
                    ? new FoodProduct(id, name, price, expiry)
                    : new NonFoodProduct(id, name, price, expiry);

            store.stockProduct(p, quantity);
            productCatalog.put(id, p);
            System.out.println("Product added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addCashier() {
        try {
            System.out.print("Enter cashier ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter salary: ");
            double salary = Double.parseDouble(scanner.nextLine());

            Cashier c = new Cashier(id, name, salary);
            store.addCashier(c);
            cashiers.add(c);
            System.out.println("Cashier added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void loadReceiptFromFile() {
        System.out.print("Enter filename (e.g., receipt_1.ser): ");
        String fileName = scanner.nextLine();
        Receipt loaded = Receipt.loadFromFile(fileName);
        if (loaded != null) {
            System.out.println("Receipt loaded successfully:");
            System.out.println(loaded);
        }
    }


    private static void simulateSale() {
        try {
            System.out.print("Enter customer balance: ");
            double balance = Double.parseDouble(scanner.nextLine());
            Customer customer = new Customer(balance);
            customers.add(customer);

            if (cashiers.isEmpty()) {
                System.out.println("No cashiers available.");
                return;
            }
            Cashier cashier = cashiers.get(0); // Simple: pick first

            Map<Product, Integer> cart = new HashMap<>();
            while (true) {
                System.out.print("Enter product ID to buy (or 0 to finish): ");
                int pid = Integer.parseInt(scanner.nextLine());
                if (pid == 0) break;
                Product p = productCatalog.get(pid);
                if (p == null) {
                    System.out.println("Product not found.");
                    continue;
                }
                System.out.print("Enter quantity: ");
                int qty = Integer.parseInt(scanner.nextLine());
                cart.put(p, qty);
            }

            Receipt receipt = store.sell(cashier, customer, cart, LocalDate.now());
            System.out.println(receipt);

            receipt.saveToFile();      // Saves as readable .txt file
            receipt.serialize();       // Saves as binary .ser file


        } catch (Exception e)
        {
            System.out.println("Sale failed: " + e.getMessage());
        }
    }

    private static void viewReceipts() {
        System.out.println("\n=== RECEIPTS ===");
        store.getReceipts().forEach(r -> {
            System.out.println(r);
            System.out.println("------------------------");
        });
    }

    private static void viewStats() {
        System.out.printf("Total receipts: %d\n", store.getReceiptCount());
        System.out.printf("Total revenue: %.2f\n", store.getTotalRevenue());
        System.out.printf("Profit: %.2f\n", store.getProfit());
    }
}
