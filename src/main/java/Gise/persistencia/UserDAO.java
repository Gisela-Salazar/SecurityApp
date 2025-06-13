package Gise.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Gise.dominio.User;
import Gise.utils.PasswordHasher;

public class UserDAO {
    // Manejo de conexión con la base de datos
    private ConnectionManager conn;
    private PreparedStatement ps; // Para ejecutar consultas SQL parametrizadas
    private ResultSet rs; // Para almacenar resultados de consultas SQL

    // Constructor: inicializa la conexión a la base de datos
    public UserDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo usuario en la base de datos y retorna el usuario creado.
     */
    public User create(User user) throws SQLException {
        User res = null;
        try {
            // Preparamos la consulta de inserción, permitiendo obtener el ID generado
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO Users (name, passwordHash, email, status) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash())); // Se cifra la contraseña
            ps.setString(3, user.getEmail());
            ps.setByte(4, user.getStatus());

            int affectedRows = ps.executeUpdate(); // Ejecutamos la consulta

            // Si la inserción fue exitosa, obtenemos el ID generado
            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Actualiza la información de un usuario en la base de datos.
     */
    public boolean update(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Users SET name = ?, email = ?, status = ? WHERE id = ?"
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setByte(3, user.getStatus());
            ps.setInt(4, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Elimina un usuario de la base de datos por su ID.
     */
    public boolean delete(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM Users WHERE id = ?");
            ps.setInt(1, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Busca usuarios en la base de datos cuyo nombre contenga la cadena ingresada.
     */
    public ArrayList<User> search(String name) throws SQLException {
        ArrayList<User> records = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE name LIKE ?");
            ps.setString(1, "%" + name + "%"); // Permite buscar coincidencias parciales en nombres

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
                records.add(user);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public User getById(int id) throws SQLException {
        User user = null;
        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE id = ?");
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un usuario por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return user;
    }

    /**
     * Autentica un usuario verificando su email y contraseña cifrada.
     */
    public User authenticate(User user) throws SQLException {
        User userAuthenticated = null;
        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status FROM Users WHERE email = ? AND passwordHash = ? AND status = 1");
            ps.setString(1, user.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));

            rs = ps.executeQuery();

            if (rs.next()) {
                userAuthenticated = new User();
                userAuthenticated.setId(rs.getInt(1));
                userAuthenticated.setName(rs.getString(2));
                userAuthenticated.setEmail(rs.getString(3));
                userAuthenticated.setStatus(rs.getByte(4));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al autenticar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return userAuthenticated;
    }

    /**
     * Actualiza la contraseña de un usuario cifrándola antes de almacenarla.
     */
    public boolean updatePassword(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("UPDATE Users SET passwordHash = ? WHERE id = ?");
            ps.setString(1, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setInt(2, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el password del usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }
}
