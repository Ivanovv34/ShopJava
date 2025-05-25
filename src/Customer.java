public class Customer
{
    private double balance;

    public Customer(double balance)
    {
        if (balance < 0)
        {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }

        this.balance = balance;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deduct(double amount) //checking if the customer has enough money
    {
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        if (amount <= balance)
        {
            balance -= amount;
        }
        else
        {
            throw new IllegalArgumentException("Not enough balance.");
        }
    }
}
