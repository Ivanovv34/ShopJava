public class InsufficientStockException extends Exception
{
    public InsufficientStockException(String productName, int requested, int available)
    {
        super("Not enough stock for '" + productName + "'. Requested: " + requested + ", Available: " + available);
    }
}
