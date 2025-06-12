package Gise.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Gise.dominio.Employee;

public class EmployeeDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public EmployeeDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     */
    public Employee create(Employee employee) throws SQLException {
        Employee res = null;
        try{
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "Employees (name, positionId, hireDate, salary)" +
                            "VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getPositionId());
            ps.setDate(3, new java.sql.Date(employee.getHireDate().getTime()));
            ps.setDouble(4, employee.getSalary());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al crear el empleado: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Actualiza la información de un empleado existente en la base de datos.
     */
    public boolean update(Employee employee) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "UPDATE Employees " +
                            "SET name = ?, positionId = ?, hireDate = ?, salary = ? " +
                            "WHERE id = ?"
            );

            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getPositionId());
            ps.setDate(3, new java.sql.Date(employee.getHireDate().getTime()));
            ps.setDouble(4, employee.getSalary());
            ps.setInt(5, employee.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al modificar el empleado: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    /**
     * Elimina un empleado de la base de datos basándose en su ID.
     */
    public boolean delete(Employee employee) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Employees WHERE id = ?"
            );
            ps.setInt(1, employee.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al eliminar el empleado: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    /**
     * Busca empleados en la base de datos cuyo nombre contenga la cadena de búsqueda proporcionada.
     */
    public ArrayList<Employee> search(String name) throws SQLException{
        ArrayList<Employee> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, positionId, hireDate, salary " +
                    "FROM Employees " +
                    "WHERE name LIKE ?");

            ps.setString(1, "%" + name + "%");

            rs = ps.executeQuery();

            while (rs.next()){
                Employee employee = new Employee();
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setPositionId(rs.getInt(3));
                employee.setHireDate(rs.getDate(4));
                employee.setSalary(rs.getDouble(5));
                records.add(employee);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al buscar empleados: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    /**
     * Obtiene un empleado de la base de datos basado en su ID.
     */
    public Employee getById(int id) throws SQLException{
        Employee employee = new Employee();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, positionId, hireDate, salary " +
                    "FROM Employees " +
                    "WHERE id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setPositionId(rs.getInt(3));
                employee.setHireDate(rs.getDate(4));
                employee.setSalary(rs.getDouble(5));
            } else {
                employee = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al obtener un empleado por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return employee;
    }

    /**
     * Obtiene todos los empleados que pertenecen a un cargo específico.
     */
    public ArrayList<Employee> getByPosition(int positionId) throws SQLException{
        ArrayList<Employee> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, name, positionId, hireDate, salary " +
                    "FROM Employees " +
                    "WHERE positionId = ?");

            ps.setInt(1, positionId);

            rs = ps.executeQuery();

            while (rs.next()){
                Employee employee = new Employee();
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setPositionId(rs.getInt(3));
                employee.setHireDate(rs.getDate(4));
                employee.setSalary(rs.getDouble(5));
                records.add(employee);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al buscar empleados por cargo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }
}