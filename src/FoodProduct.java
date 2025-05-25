import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

public class FoodProduct extends Product implements Serializable
{
    private static final long serialVersionUID = 1L;

    public FoodProduct() //only used for serialization
    {
        super(0, "DefaultFood", 0.0, LocalDate.now().plusDays(1), Category.FOOD, 25.0);
    }

    public FoodProduct(int id, String name, double deliveryPrice, LocalDate expiryDate, double markupPercentage)
    {
        super(id, name, deliveryPrice, expiryDate, Category.FOOD, markupPercentage);

        if (deliveryPrice <= 0)
        {
            throw new IllegalArgumentException("Delivery price must be positive.");
        }

        if (markupPercentage < 0)
        {
            throw new IllegalArgumentException("Markup percentage cannot be negative.");
        }
    }

    @Override
    public double getSellingPrice(LocalDate currentDate, int expiryThresholdDays, double discountPercent)
    {
        long daysLeft = ChronoUnit.DAYS.between(currentDate, expiryDate);
        double basePrice = deliveryPrice * (1 + getMarkupPercentage() / 100.0);

        if (daysLeft <= expiryThresholdDays && daysLeft > 0)
        {
            basePrice *= (1 - discountPercent / 100.0);
        }

        return basePrice;
    }

}
