import java.io.Serializable;

public class ReceiptItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Product product;
    private int quantity;
    private double unitPrice;

    public ReceiptItem(Product product, int quantity, double unitPrice)
    {
        if (quantity <= 0)
        {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        if (unitPrice < 0)
        {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Product getProduct()
    {
        return product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public  double getTotalPrice()
    {
        return unitPrice * quantity;
    }

    @Override
    public String toString()
    {
        return String.format("%s x%d @ %.2f = %.2f",
                product.getName(), quantity, unitPrice, getTotalPrice());
    }
}
