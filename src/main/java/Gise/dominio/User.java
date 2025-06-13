package Gise.dominio;

public class User {
    // Atributos privados para mantener la encapsulación de los datos del usuario
    private int id; // Identificador único del usuario
    private String name; // Nombre del usuario
    private String passwordHash; // Hash de la contraseña (para mayor seguridad)
    private String email; // Correo electrónico del usuario
    private byte status; // Estado del usuario (activo/inactivo)

    // Constructor vacío: permite crear un objeto sin inicializar atributos
    public User() {
    }

    // Constructor con parámetros: inicializa un objeto 'User' con valores específicos
    public User(int id, String name, String passwordHash, String email, byte status) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.status = status;
    }

    // Métodos getters y setters para acceder y modificar los atributos privados

    // Obtiene el ID del usuario
    public int getId() {
        return id;
    }

    // Establece un nuevo ID para el usuario
    public void setId(int id) {
        this.id = id;
    }

    // Obtiene el nombre del usuario
    public String getName() {
        return name;
    }

    // Establece un nuevo nombre para el usuario
    public void setName(String name) {
        this.name = name;
    }

    // Obtiene el hash de la contraseña
    public String getPasswordHash() {
        return passwordHash;
    }

    // Establece un nuevo hash de contraseña
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Obtiene el correo electrónico del usuario
    public String getEmail() {
        return email;
    }

    // Establece un nuevo correo electrónico para el usuario
    public void setEmail(String email) {
        this.email = email;
    }

    // Obtiene el estado del usuario (1 = Activo, 2 = Inactivo)
    public byte getStatus() {
        return status;
    }

    // Establece un nuevo estado para el usuario
    public void setStatus(byte status) {
        this.status = status;
    }

    // Método que devuelve una representación del estado del usuario como texto
    public String getStrEstatus() {
        String str = "";
        switch (status) {
            case 1:
                str = "ACTIVO"; // Usuario activo
                break;
            case 2:
                str = "INACTIVO"; // Usuario inactivo
                break;
            default:
                str = "DESCONOCIDO"; // Estado desconocido
        }
        return str;
    }
}
