public class Cashier {
    private int id;
    private String name;
    private double monthlySalary;

    public Cashier(int id, String name, double monthlySalary)
    {
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