package Gise.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Gise.dominio.Employee;
import Gise.dominio.Position;
import Gise.persistencia.EmployeeDAO;
import Gise.persistencia.PositionDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase EmployeeDAO
 */
class EmployeeDAOTest {
    private EmployeeDAO employeeDAO;
    private PositionDAO positionDAO;

    @BeforeEach
    void setUp() {
        // Inicializar los DAOs antes de cada prueba
        employeeDAO = new EmployeeDAO();
        positionDAO = new PositionDAO();
    }

    private Employee create(Employee employee) throws SQLException {
        // Crear un empleado en la base de datos
        Employee res = employeeDAO.create(employee);

        // Verificar que la creación fue exitosa
        assertNotNull(res, "El empleado creado no debería ser nulo.");
        assertEquals(employee.getName(), res.getName(), "El nombre del empleado creado debe ser igual al original.");
        assertEquals(employee.getPositionId(), res.getPositionId(), "El ID de posición del empleado creado debe ser igual al original.");
        assertTrue(res.getId() > 0, "El ID del empleado creado debería ser mayor que 0.");

        return res;
    }

    private void update(Employee employee) throws SQLException {
        // Modificar los atributos del empleado
        employee.setName(employee.getName() + "_actualizado");
        employee.setSalary(employee.getSalary() + 1000);

        // Actualizar el empleado en la base de datos
        boolean res = employeeDAO.update(employee);

        // Verificar que la actualización fue exitosa
        assertTrue(res, "La actualización del empleado debería ser exitosa.");

        // Verificar que los cambios se persistieron correctamente
        getById(employee);
    }

    private void getById(Employee employee) throws SQLException {
        // Obtener el empleado por su ID
        Employee res = employeeDAO.getById(employee.getId());

        // Verificar que el empleado obtenido coincide con el esperado
        assertNotNull(res, "El empleado obtenido por ID no debería ser nulo.");
        assertEquals(employee.getId(), res.getId(), "El ID del empleado obtenido debe ser igual al original.");
        assertEquals(employee.getName(), res.getName(), "El nombre del empleado obtenido debe ser igual al esperado.");
        assertEquals(employee.getPositionId(), res.getPositionId(), "El ID de posición del empleado obtenido debe ser igual al esperado.");
    }

    private void search(Employee employee) throws SQLException {
        // Buscar empleados por nombre
        ArrayList<Employee> employees = employeeDAO.search(employee.getName());
        boolean find = false;

        // Verificar que se encontró al menos un empleado con el nombre buscado
        for (Employee employeeItem : employees) {
            if (employeeItem.getName().contains(employee.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }

        assertTrue(find, "El nombre buscado no fue encontrado: " + employee.getName());
    }

    private void delete(Employee employee) throws SQLException {
        // Eliminar el empleado de la base de datos
        boolean res = employeeDAO.delete(employee);

        // Verificar que la eliminación fue exitosa
        assertTrue(res, "La eliminación del empleado debería ser exitosa.");

        // Verificar que el empleado ya no existe en la base de datos
        Employee res2 = employeeDAO.getById(employee.getId());
        assertNull(res2, "El empleado debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testEmployeeDAO() throws SQLException {
        // Primero necesitamos un cargo para asignar al empleado
        Position position = new Position();
        position.setTitle("Desarrollador Test");
        position.setDescription("Cargo para pruebas unitarias");

        // Crear el cargo en la base de datos
        Position createdPosition = positionDAO.create(position);
        assertNotNull(createdPosition, "El cargo creado no debería ser nulo.");

        // Generar datos aleatorios para el empleado
        Random random = new Random();
        int num = random.nextInt(1000) + 1;

        // Crear un nuevo empleado con datos de prueba
        Employee employee = new Employee();
        employee.setName("Empleado Test " + num);
        employee.setPositionId(createdPosition.getId());
        employee.setHireDate(new Date());
        employee.setSalary(50000 + num);

        // Ejecutar las pruebas CRUD
        Employee testEmployee = create(employee);
        update(testEmployee);
        search(testEmployee);
        delete(testEmployee);

        // Limpiar: eliminar el cargo creado para la prueba
        positionDAO.delete(createdPosition);
    }

    @Test
    void createEmployee() throws SQLException {
        // Obtener un cargo existente para asignar al empleado
        ArrayList<Position> positions = positionDAO.getAll();
        Position position;

        if (positions.isEmpty()) {
            // Si no hay cargos, crear uno
            position = new Position();
            position.setTitle("Desarrollador");
            position.setDescription("Desarrollador de software");
            position = positionDAO.create(position);
        } else {
            // Usar el primer cargo disponible
            position = positions.get(0);
        }

        // Crear un nuevo empleado
        Employee employee = new Employee();
        employee.setName("Juan Pérez");
        employee.setPositionId(position.getId());
        employee.setHireDate(new Date());
        employee.setSalary(60000);

        // Crear el empleado en la base de datos
        Employee res = employeeDAO.create(employee);

        // Verificar que la creación fue exitosa
        assertNotNull(res, "El empleado creado no debería ser nulo.");
        assertTrue(res.getId() > 0, "El ID del empleado creado debería ser mayor que 0.");
    }

    @Test
    void testGetByPosition() throws SQLException {
        // Obtener un cargo existente
        ArrayList<Position> positions = positionDAO.getAll();

        if (!positions.isEmpty()) {
            Position position = positions.get(0);

            // Obtener empleados por posición
            ArrayList<Employee> employees = employeeDAO.getByPosition(position.getId());

            // Verificar que la lista no es nula
            assertNotNull(employees, "La lista de empleados no debería ser nula.");

            // Imprimir información de los empleados (útil para depuración)
            System.out.println("Número de empleados encontrados para la posición " +
                    position.getTitle() + ": " + employees.size());

            // Verificar que todos los empleados tienen la posición correcta
            for (Employee employee : employees) {
                assertEquals(position.getId(), employee.getPositionId(),
                        "Todos los empleados deberían tener el ID de posición correcto.");
                System.out.println("ID: " + employee.getId() + ", Nombre: " + employee.getName());
            }
        } else {
            System.out.println("No hay posiciones disponibles para probar getByPosition()");
        }
    }
}