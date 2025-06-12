package Gise.persistencia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection; // Importa la clase Connection del paquete java.sql, que
import java.sql.SQLException; // Importa la clase SQLException del paquete java.sql,

import static org.junit.jupiter.api.Assertions.*; // Importa todos los métodos estáticos de Assertions para aserciones de prueba.

class ConnectionManagerTest {

    ConnectionManager connectionManager; // 5 usages

    @BeforeEach
    void setUp() throws SQLException {
        // Se ejecuta antes de cada método de prueba.
        // Inicializa el ConnectionManager.
        connectionManager = ConnectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Se ejecuta después de cada método de prueba.
        // Cierra la conexión y limpia los recursos.
        if (connectionManager != null) {
            connectionManager.disconnect();
            connectionManager = null; // Para asegurar que no se use accidentalmente
        }
    }

    @Test
    void connect() throws SQLException {
        // Intenta establecer una conexión a la base de datos utilizando el método connect() de ConnectionManager.
        Connection conn = connectionManager.connect();

        // Realiza una aserción para verificar que la conexión establecida no sea nula.
        // Si conn es nulo, la prueba fallará con el mensaje "La conexion no debe ser nula".
        assertNotNull(conn, "La conexion no debe ser nula");

        // Realiza una aserción para verificar que la conexión establecida esté abierta.
        // El método isClosed() devuelve true si la conexión está cerrada, por lo que assertFalse espera que devuelva false.
        // Si la conexión está cerrada, la prueba fallará con el mensaje "La conexión debe esta abierta".
        assertFalse(conn.isClosed(), "La conexión debe esta abierta");

        if (conn != null) {
            conn.close(); // Cierra la conexión después de la prueba.
        }
    }
}

