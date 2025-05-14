import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FoodProduct extends Product
{
    private static final double MARKUP_PERCENTAGE = 25.0;

    public FoodProduct(int id, String name, double deliveryPrice, LocalDate expiryDate)
    {
        super(id, name, deliveryPrice, expiryDate, Category.FOOD);
    }

    @Override
    public double getSellingPrice(LocalDate currentDate, int expiryThresholdDays, double discountPercent)
    {
        long daysLeft = ChronoUnit.DAYS.between(currentDate, expiryDate);
        double basePrice = deliveryPrice * (1 + MARKUP_PERCENTAGE / 100.0);

        if (daysLeft <= expiryThresholdDays && daysLeft > 0)
        {
            basePrice *= (1 - discountPercent / 100.0);
        }

        return basePrice;
    }
}
