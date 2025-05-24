import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

    public class StoreTest {

        private Store store;
        private Cashier cashier;
        private Product freshMilk;
        private Product expiredJuice;
        private Product shampoo;

        @BeforeEach
        public void setup() {
            store = new Store("TestStore", 3, 20); // 3 days threshold, 20% discount
            cashier = new Cashier(1, "Hris", 1000);
            store.addCashier(cashier);

            freshMilk = new FoodProduct(101, "Milk", 2.0, LocalDate.now().plusDays(2), 25.0);
            expiredJuice = new FoodProduct(102, "Juice", 3.0, LocalDate.now().minusDays(1), 25.0);
            shampoo = new NonFoodProduct(201, "Shampoo", 4.0, LocalDate.now().plusDays(10), 40.0);

            store.stockProduct(freshMilk, 10);
            store.stockProduct(expiredJuice, 5);
            store.stockProduct(shampoo, 8);
        }

        @Test
        public void testSuccessfulSale() throws Exception {
            Customer customer = new Customer(100);
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(shampoo, 2);

            Receipt receipt = store.sell(cashier, customer, cart, LocalDate.now());
            assertEquals(2, receipt.getItems().get(0).getQuantity());
            assertEquals(1, store.getReceiptCount());
        }

        @Test
        public void testInsufficientStockException() {
            Customer customer = new Customer(100);
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(shampoo, 20); // only 8 in stock

            assertThrows(InsufficientStockException.class, () -> {
                store.sell(cashier, customer, cart, LocalDate.now());
            });
        }

        @Test
        public void testExpiredProductNotSold() {
            Customer customer = new Customer(100);
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(expiredJuice, 1);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                store.sell(cashier, customer, cart, LocalDate.now());
            });

            assertTrue(exception.getMessage().contains("expired"));
        }

        @Test
        public void testDiscountAppliedNearExpiry() throws Exception {
            Customer customer = new Customer(100);
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(freshMilk, 1); // expires in 2 days, threshold = 3 days

            Receipt receipt = store.sell(cashier, customer, cart, LocalDate.now());
            double expectedPrice = 2.0 * 1.25 * 0.8; // markup then 20% discount

            assertEquals(expectedPrice, receipt.getItems().get(0).getUnitPrice(), 0.001);
        }

        @Test
        public void testNoDiscountWhenExpiryIsFar() throws Exception {
            Customer customer = new Customer(100);
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(shampoo, 1); // expires in 10 days, no discount

            Receipt receipt = store.sell(cashier, customer, cart, LocalDate.now());
            double expectedPrice = 4.0 * 1.40; // 40% markup

            assertEquals(expectedPrice, receipt.getItems().get(0).getUnitPrice(), 0.001);
        }

        @Test
        public void testCustomerCannotAffordPurchase() {
            Customer customer = new Customer(1); // not enough for anything
            Map<Product, Integer> cart = new HashMap<>();
            cart.put(shampoo, 1);

            assertThrows(IllegalArgumentException.class, () -> {
                store.sell(cashier, customer, cart, LocalDate.now());
            });
        }
    }
