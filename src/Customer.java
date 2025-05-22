public class Customer
{
    private double balance;

    public Customer(double balance)
    {
        this.balance = balance;
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
}
