package Gise.dominio;

import java.util.Date;

public class Employee {
    // Atributos privados para encapsular los datos del empleado
    private int id; // Identificador único del empleado
    private String name; // Nombre del empleado
    private int positionId; // ID de la posición/cargo del empleado
    private Date hireDate; // Fecha de contrataciónthanysalazar4@thhbxqkjwsiubebhw
    private double salary; // Salario del empleado

    // Constructor vacío: útil cuando se necesita crear un objeto sin inicializar atributos
    public Employee() {
    }

    // Constructor con parámetros: permite crear un objeto 'Employee' con valores específicos
    public Employee(int id, String name, int positionId, Date hireDate, double salary) {
        this.id = id;
        this.name = name;
        this.positionId = positionId;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    // Métodos getters y setters para acceder y modificar los atributos privados

    // Obtiene el ID del empleado
    public int getId() {
        return id;
    }

    // Establece un nuevo ID para el empleado
    public void setId(int id) {
        this.id = id;
    }

    // Obtiene el nombre del empleado
    public String getName() {
        return name;
    }

    // Establece un nuevo nombre para el empleado
    public void setName(String name) {
        this.name = name;
    }

    // Obtiene el ID de la posición del empleado
    public int getPositionId() {
        return positionId;
    }

    // Establece un nuevo ID de posición para el empleado
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    // Obtiene la fecha de contratación del empleado
    public Date getHireDate() {
        return hireDate;
    }

    // Establece una nueva fecha de contratación para el empleado
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    // Obtiene el salario del empleado
    public double getSalary() {
        return salary;
    }

    // Establece un nuevo salario para el empleado
    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Devuelve el salario formateado como una cadena con dos decimales y símbolo de dólar
    public String getFormattedSalary() {
        return String.format("$%.2f", salary);
    }
}
