import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NonFoodProduct extends Product
{

    public NonFoodProduct(int id, String name, double deliveryPrice, LocalDate expiryDate, double markupPercentage)
    {
        super(id, name, deliveryPrice, expiryDate, Category.NON_FOOD, markupPercentage);
    }

    @Override
    public double getSellingPrice(LocalDate currentDate, int expiryThresholdDays, double discountPercent) {
        long daysLeft = ChronoUnit.DAYS.between(currentDate, expiryDate);
        double basePrice = deliveryPrice * (1 + getMarkupPercentage() / 100.0);

        if (daysLeft <= expiryThresholdDays && daysLeft > 0) {
            basePrice *= (1 - discountPercent / 100.0);
        }

        return basePrice;
    }
}
