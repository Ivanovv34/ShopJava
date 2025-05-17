public class Customer
{
    private String name;
    private double balance;

    public Customer(String name, double balance)
    {
        this.name = name;
        this.balance = balance;
    }

    public String getName()
    {
        return name;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deduct(double amount) //checking if the customer has enough money
    {
        if(amount <= balance)
        {
            balance -= amount;
        }
        else
        {
            throw new IllegalArgumentException("Not enough balance");
        }
    }

    @Override
    public String toString()
    {
        return name + " (Balance: " + String.format("%.2f", balance) + ")";
    }
}
