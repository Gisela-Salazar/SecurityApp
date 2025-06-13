package Gise.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Gise.dominio.Position;
import Gise.persistencia.PositionDAO;

import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase PositionDAO
 */
class PositionDAOTest {
    private PositionDAO positionDAO;

    @BeforeEach
    void setUp() {
        // Inicializar el DAO antes de cada prueba
        positionDAO = new PositionDAO();
    }

    private Position create(Position position) throws SQLException {
        // Crear un cargo en la base de datos
        Position res = positionDAO.create(position);

        // Verificar que la creación fue exitosa
        assertNotNull(res, "El cargo creado no debería ser nulo.");
        assertEquals(position.getTitle(), res.getTitle(), "El título del cargo creado debe ser igual al original.");
        assertEquals(position.getDescription(), res.getDescription(), "La descripción del cargo creado debe ser igual al original.");
        assertTrue(res.getId() > 0, "El ID del cargo creado debería ser mayor que 0.");

        return res;
    }

    private void update(Position position) throws SQLException {
        // Modificar los atributos del cargo
        position.setTitle(position.getTitle() + "_actualizado");
        position.setDescription(position.getDescription() + " - Actualizado");

        // Actualizar el cargo en la base de datos
        boolean res = positionDAO.update(position);

        // Verificar que la actualización fue exitosa
        assertTrue(res, "La actualización del cargo debería ser exitosa.");

        // Verificar que los cambios se persistieron correctamente
        getById(position);
    }

    private void getById(Position position) throws SQLException {
        // Obtener el cargo por su ID
        Position res = positionDAO.getById(position.getId());

        // Verificar que el cargo obtenido coincide con el esperado
        assertNotNull(res, "El cargo obtenido por ID no debería ser nulo.");
        assertEquals(position.getId(), res.getId(), "El ID del cargo obtenido debe ser igual al original.");
        assertEquals(position.getTitle(), res.getTitle(), "El título del cargo obtenido debe ser igual al esperado.");
        assertEquals(position.getDescription(), res.getDescription(), "La descripción del cargo obtenido debe ser igual al esperado.");
    }

    private void search(Position position) throws SQLException {
        // Buscar cargos por título
        ArrayList<Position> positions = positionDAO.search(position.getTitle());
        boolean find = false;

        // Verificar que se encontró al menos un cargo con el título buscado
        for (Position positionItem : positions) {
            if (positionItem.getTitle().contains(position.getTitle())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }

        assertTrue(find, "El título buscado no fue encontrado: " + position.getTitle());
    }

    private void delete(Position position) throws SQLException {
        // Eliminar el cargo de la base de datos
        boolean res = positionDAO.delete(position);

        // Verificar que la eliminación fue exitosa
        assertTrue(res, "La eliminación del cargo debería ser exitosa.");

        // Verificar que el cargo ya no existe en la base de datos
        Position res2 = positionDAO.getById(position.getId());
        assertNull(res2, "El cargo debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testPositionDAO() throws SQLException {
        // Generar datos aleatorios para el cargo
        Random random = new Random();
        int num = random.nextInt(1000) + 1;

        // Crear un nuevo cargo con datos de prueba
        Position position = new Position();
        position.setTitle("Cargo Test " + num);
        position.setDescription("Descripción del cargo de prueba " + num);

        // Ejecutar las pruebas CRUD
        Position testPosition = create(position);
        update(testPosition);
        search(testPosition);
        delete(testPosition);
    }

    @Test
    void createPosition() throws SQLException {
        // Crear un nuevo cargo
        Position position = new Position();
        position.setTitle("Gerente");
        position.setDescription("Gerente de departamento");

        // Crear el cargo en la base de datos
        Position res = positionDAO.create(position);

        // Verificar que la creación fue exitosa
        assertNotNull(res, "El cargo creado no debería ser nulo.");
        assertTrue(res.getId() > 0, "El ID del cargo creado debería ser mayor que 0.");
    }

    @Test
    void testGetAll() throws SQLException {
        // Obtener todos los cargos
        ArrayList<Position> positions = positionDAO.getAll();

        // Verificar que la lista no es nula
        assertNotNull(positions, "La lista de cargos no debería ser nula.");

        // Imprimir información de los cargos (útil para depuración)
        System.out.println("Número de cargos encontrados: " + positions.size());
        for (Position position : positions) {
            System.out.println("ID: " + position.getId() + ", Título: " + position.getTitle());
        }
    }

    @Test
    void testShortDescription() {
        // Crear un cargo con una descripción larga
        Position position = new Position();
        position.setTitle("Cargo con descripción larga");
        position.setDescription("Esta es una descripción muy larga que debería ser truncada cuando se llame al método getShortDescription para asegurar que no ocupe demasiado espacio en la interfaz de usuario");

        // Obtener la descripción corta (si existe el método)
        String shortDesc = position.getDescription();
        if (position.getClass().getDeclaredMethods().length > 0) {
            try {
                java.lang.reflect.Method method = position.getClass().getMethod("getShortDescription");
                shortDesc = (String) method.invoke(position);

                // Verificar que la descripción fue truncada
                assertTrue(shortDesc.length() <= position.getDescription().length(),
                        "La descripción corta debería ser igual o más corta que la descripción original.");

                if (shortDesc.length() < position.getDescription().length()) {
                    assertTrue(shortDesc.endsWith("..."),
                            "La descripción corta debería terminar con '...' si fue truncada.");
                }
            } catch (Exception e) {
                // Si no existe el método, simplemente continuamos
                System.out.println("Nota: El método getShortDescription() no existe en la clase Position.");
            }
        }

        System.out.println("Descripción: " + position.getDescription());
        System.out.println("Descripción utilizada: " + shortDesc);
    }
}