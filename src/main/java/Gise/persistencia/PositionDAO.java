package Gise.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Gise.dominio.Position;

public class PositionDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public PositionDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo cargo en la base de datos.
     */
    public Position create(Position position) throws SQLException {
        Position res = null;
        try{
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "Positions (title, description)" +
                            "VALUES (?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, position.getTitle());
            ps.setString(2, position.getDescription());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating position failed, no ID obtained.");
                }
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al crear el cargo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Actualiza la información de un cargo existente en la base de datos.
     */
    public boolean update(Position position) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "UPDATE Positions " +
                            "SET title = ?, description = ? " +
                            "WHERE id = ?"
            );

            ps.setString(1, position.getTitle());
            ps.setString(2, position.getDescription());
            ps.setInt(3, position.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al modificar el cargo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    /**
     * Elimina un cargo de la base de datos basándose en su ID.
     */
    public boolean delete(Position position) throws SQLException{
        boolean res = false;
        try{
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Positions WHERE id = ?"
            );
            ps.setInt(1, position.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al eliminar el cargo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    /**
     * Busca cargos en la base de datos cuyo título contenga la cadena de búsqueda proporcionada.
     */
    public ArrayList<Position> search(String title) throws SQLException{
        ArrayList<Position> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, title, description " +
                    "FROM Positions " +
                    "WHERE title LIKE ?");

            ps.setString(1, "%" + title + "%");

            rs = ps.executeQuery();

            while (rs.next()){
                Position position = new Position();
                position.setId(rs.getInt(1));
                position.setTitle(rs.getString(2));
                position.setDescription(rs.getString(3));
                records.add(position);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al buscar cargos: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    /**
     * Obtiene un cargo de la base de datos basado en su ID.
     */
    public Position getById(int id) throws SQLException{
        Position position = new Position();

        try {
            ps = conn.connect().prepareStatement("SELECT id, title, description " +
                    "FROM Positions " +
                    "WHERE id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                position.setId(rs.getInt(1));
                position.setTitle(rs.getString(2));
                position.setDescription(rs.getString(3));
            } else {
                position = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al obtener un cargo por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return position;
    }

    /**
     * Obtiene todos los cargos disponibles en la base de datos.
     */
    public ArrayList<Position> getAll() throws SQLException{
        ArrayList<Position> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, title, description " +
                    "FROM Positions " +
                    "ORDER BY title");

            rs = ps.executeQuery();

            while (rs.next()){
                Position position = new Position();
                position.setId(rs.getInt(1));
                position.setTitle(rs.getString(2));
                position.setDescription(rs.getString(3));
                records.add(position);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex){
            throw new SQLException("Error al obtener todos los cargos: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }
}
