package Gise.dominio;

import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private int positionId;
    private Date hireDate;
    private double salary;

    public Employee() {
    }

    public Employee(int id, String name, int positionId, Date hireDate, double salary) {
        this.id = id;
        this.name = name;
        this.positionId = positionId;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getFormattedSalary() {
        return String.format("$%.2f", salary);
    }
}

