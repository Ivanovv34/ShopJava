import java.time.LocalDate;

public abstract class Product
{
    protected  int id;
    protected String name;
    protected double deliveryPrice;
    protected LocalDate expiryDate;
    protected Category category;
    private double markupPercentage;

    public Product(int id, String name, double deliveryPrice, LocalDate expiryDate, Category category,double markupPercentage)
    {
        this.id = id;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.expiryDate = expiryDate;
        this.category = category;
        this.markupPercentage = markupPercentage;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getDeliveryPrice()
    {
        return deliveryPrice;
    }

    public LocalDate getExpiryDate()
    {
        return expiryDate;
    }

    public Category getCategory()
    {
        return category;
    }

    public double getMarkupPercentage()
    {
        return markupPercentage;
    }


    public abstract double getSellingPrice(LocalDate currentDate, int expiryThresholdDays, double discountPercent);

    @Override
    public String toString()
    {
        return String.format("%s (ID: %d), Category: %s, Delivery Price: %.2f, Expiry: %s",
                name, id, category, deliveryPrice, expiryDate);
    }
}
