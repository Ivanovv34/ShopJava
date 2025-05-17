import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NonFoodProduct extends Product
{
    private static final double MARKUP_PERCENTAGE = 40.0;

    public NonFoodProduct(int id, String name, double deliveryPrice, LocalDate expiryDate)
    {
        super(id, name, deliveryPrice, expiryDate, Category.NON_FOOD);
    }

    @Override
    public double getSellingPrice(LocalDate currentDate, int expiryThresholdDays, double discountPercent)
    {
        long daysLeft = ChronoUnit.DAYS.between(currentDate, expiryDate);
        double basePrice = deliveryPrice * (1 + MARKUP_PERCENTAGE / 100);

        if(daysLeft <= expiryThresholdDays && daysLeft > 0)
        {
            basePrice *= (1 - discountPercent / 100);
        }
        return basePrice;
    }
}
