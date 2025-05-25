import java.io.Serializable;

public class Cashier implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private double monthlySalary;

    public Cashier(int id, String name, double monthlySalary)
    {
        if (monthlySalary <= 0)
        {
            throw new IllegalArgumentException("Monthly salary must be positive.");
        }

        this.id = id;
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getMonthlySalary()
    {
        return monthlySalary;
    }

    @Override
    public String toString()
    {
        return String.format("Cashier #%d: %s, Salary: %.2f", id, name, monthlySalary);
    }
}
//Needed for tracking who sells the product