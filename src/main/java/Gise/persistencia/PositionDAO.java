package Gise.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Gise.dominio.Position;

public class PositionDAO {
    // Manejador de conexión a la base de datos
    private ConnectionManager conn;

    // Constructor: inicializa la conexión con la base de datos
    public PositionDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo cargo en la base de datos y retorna el objeto creado.
     */
    public Position create(Position position) throws SQLException {
        Position res = null;
        String sql = "INSERT INTO Positions (title, description) VALUES (?, ?)";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, position.getTitle());
            ps.setString(2, position.getDescription());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGenerado = generatedKeys.getInt(1);
                        res = getById(idGenerado);
                    } else {
                        throw new SQLException("Creating position failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el cargo: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }

    /**
     * Obtiene un cargo por su ID.
     */
    public Position getById(int id) throws SQLException {
        Position position = null;
        String sql = "SELECT id, title, description FROM Positions WHERE id = ?";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    position = new Position();
                    position.setId(rs.getInt(1));
                    position.setTitle(rs.getString(2));
                    position.setDescription(rs.getString(3));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un cargo por id: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return position;
    }

    /**
     * Actualiza la información de un cargo en la base de datos.
     */
    public boolean update(Position position) throws SQLException {
        boolean res = false;
        String sql = "UPDATE Positions SET title = ?, description = ? WHERE id = ?";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setString(1, position.getTitle());
            ps.setString(2, position.getDescription());
            ps.setInt(3, position.getId());

            res = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el cargo: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }

    /**
     * Elimina un cargo de la base de datos por su ID.
     */
    public boolean delete(Position position) throws SQLException {
        boolean res = false;
        String sql = "DELETE FROM Positions WHERE id = ?";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setInt(1, position.getId());
            res = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el cargo: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }

    /**
     * Obtiene todos los cargos disponibles en la base de datos.
     */
    public ArrayList<Position> getAll() throws SQLException {
        ArrayList<Position> records = new ArrayList<>();
        String sql = "SELECT id, title, description FROM Positions ORDER BY title";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Position position = new Position();
                position.setId(rs.getInt(1));
                position.setTitle(rs.getString(2));
                position.setDescription(rs.getString(3));
                records.add(position);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los cargos: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return records;
    }

    /**
     * Busca cargos cuyo título contenga la cadena ingresada.
     */
    public ArrayList<Position> search(String title) throws SQLException {
        ArrayList<Position> records = new ArrayList<>();
        String sql = "SELECT id, title, description FROM Positions WHERE title LIKE ?";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Position position = new Position();
                    position.setId(rs.getInt(1));
                    position.setTitle(rs.getString(2));
                    position.setDescription(rs.getString(3));
                    records.add(position);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar cargos: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return records;
    }
}
