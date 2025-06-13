package Gise.presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Gise.dominio.User;

/**
 * La clase MainForm representa la ventana principal de la aplicación de escritorio.
 * Es la interfaz de usuario central desde la cual los usuarios pueden navegar a
 * diferentes módulos como la gestión de empleados o usuarios, y realizar acciones
 * relacionadas con su perfil, como cambiar la contraseña o cerrar sesión.
 * Extiende JFrame para crear la ventana principal de la aplicación.
 */
public class MainForm extends JFrame {

    // Atributo para almacenar el usuario autenticado en la sesión actual.
    private User userAutenticate;

    // Colores personalizados para el tema de la interfaz, asegurando una estética consistente.
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185); // Color azul oscuro, para elementos principales.
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219); // Color azul más claro, para elementos secundarios.
    private final Color COLOR_EXITO = new Color(39, 174, 96); // Color verde, para indicaciones de éxito.
    private final Color COLOR_PELIGRO = new Color(231, 76, 60); // Color rojo, para acciones de peligro o salida.
    private final Color COLOR_FONDO = new Color(236, 240, 241); // Color gris claro, para fondos generales.

    /**
     * Obtiene el objeto User que representa al usuario actualmente autenticado.
     * @return El objeto User autenticado.
     */
    public User getUserAutenticate() {
        return userAutenticate;
    }

    /**
     * Establece el objeto User que representa al usuario autenticado.
     * Este método se utiliza típicamente después de un inicio de sesión exitoso.
     * @param userAutenticate El objeto User a establecer como usuario autenticado.
     */
    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    /**
     * Constructor de la clase MainForm.
     * Inicializa la ventana principal de la aplicación, configurando su título,
     * operación de cierre, tamaño y apariencia inicial.
     */
    public MainForm(){
        setTitle("Sistema en java de escritorio"); // Establece el título de la ventana.

        // Cambio principal: usar DISPOSE_ON_CLOSE en lugar de EXIT_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Al cerrar esta ventana, solo se cierra esta ventana, no toda la aplicación.

        // Agregar WindowListener para manejar el evento de cierre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes agregar lógica adicional si es necesario
                // Por ejemplo, guardar datos, mostrar confirmación, etc.
                System.out.println("Cerrando MainForm - Regresando al menú principal");
                // La ventana se cerrará automáticamente debido a DISPOSE_ON_CLOSE
            }
        });

        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana al inicio.

        // Configura el color de fondo del panel de contenido del JFrame.
        getContentPane().setBackground(COLOR_FONDO);

        createMenu(); // Llama al método para crear y configurar la barra de menú.
        createWelcomePanel(); // Llama al método para crear y mostrar el panel de bienvenida.
    }

    /**
     * Crea y configura la barra de menú de la aplicación, incluyendo opciones
     * para el perfil del usuario (cambiar contraseña, cambiar de usuario, salir)
     * y módulos de mantenimiento (gestión de usuarios).
     */
    private void createMenu() {
        // Barra de menú principal con estilo personalizado.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE); // Fondo blanco para la barra de menú.
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARIO)); // Borde inferior de color primario.
        menuBar.setPreferredSize(new Dimension(0, 35)); // Altura preferida de la barra de menú.
        setJMenuBar(menuBar); // Asigna la barra de menú al JFrame.

        // Menú "Perfil" con estilo.
        JMenu menuPerfil = new JMenu("👤 Perfil"); // Crea el menú "Perfil" con un emoji.
        menuPerfil.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Fuente y estilo del texto del menú.
        menuPerfil.setForeground(COLOR_PRIMARIO); // Color del texto del menú.
        menuPerfil.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno del menú.
        menuBar.add(menuPerfil); // Añade el menú "Perfil" a la barra de menú.

        // Elemento de menú "Cambiar contraseña".
        JMenuItem itemChangePassword = new JMenuItem("🔐 Cambiar contraseña"); // Crea el item de menú.
        itemChangePassword.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fuente y estilo del texto.
        itemChangePassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno.
        addHoverEffect(itemChangePassword); // Añade un efecto de hover personalizado.
        menuPerfil.add(itemChangePassword); // Añade el item al menú Perfil.
        itemChangePassword.addActionListener(e -> { // Añade un ActionListener para abrir el formulario de cambio de contraseña.
            ChangePasswordForm changePassword = new ChangePasswordForm(this); // Instancia el formulario.
            changePassword.setVisible(true); // Hace visible el formulario.
        });

        // Elemento de menú "Cambiar de usuario" (volver a la pantalla de login).
        JMenuItem itemChangeUser = new JMenuItem("🔄 Cambiar de usuario"); // Crea el item de menú.
        itemChangeUser.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fuente y estilo del texto.
        itemChangeUser.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno.
        addHoverEffect(itemChangeUser); // Añade un efecto de hover.
        menuPerfil.add(itemChangeUser); // Añade el item al menú Perfil.
        itemChangeUser.addActionListener(e -> { // Añade un ActionListener para abrir el formulario de login.
            LoginForm loginForm = new LoginForm(this); // Instancia el formulario de login.
            loginForm.setVisible(true); // Hace visible el formulario de login.
        });

        menuPerfil.addSeparator(); // Añade un separador visual en el menú Perfil.

        // Elemento de menú "Salir" (cierra la aplicación).
        JMenuItem itemSalir = new JMenuItem("🚪 Salir"); // Crea el item de menú.
        itemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fuente y estilo del texto.
        itemSalir.setForeground(COLOR_PELIGRO); // Color del texto (rojo para salir).
        itemSalir.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno.
        addHoverEffect(itemSalir); // Añade un efecto de hover.
        menuPerfil.add(itemSalir); // Añade el item al menú Perfil.
        itemSalir.addActionListener(e -> System.exit(0)); // Añade un ActionListener para terminar la aplicación.

        // Menú "Mantenimientos" con estilo.
        JMenu menuMantenimiento = new JMenu("⚙️ Mantenimientos"); // Crea el menú "Mantenimientos" con un emoji.
        menuMantenimiento.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Fuente y estilo del texto.
        menuMantenimiento.setForeground(COLOR_SECUNDARIO); // Color del texto del menú.
        menuMantenimiento.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno.
        menuBar.add(menuMantenimiento); // Añade el menú "Mantenimientos" a la barra de menú.

        // Elemento de menú "Usuarios" (abre la gestión de usuarios).
        JMenuItem itemUsers = new JMenuItem("👥 Usuarios"); // Crea el item de menú.
        itemUsers.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fuente y estilo del texto.
        itemUsers.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Relleno interno.
        addHoverEffect(itemUsers); // Añade un efecto de hover.
        menuMantenimiento.add(itemUsers); // Añade el item al menú Mantenimientos.
        itemUsers.addActionListener(e -> { // Añade un ActionListener para abrir el formulario de lectura de usuarios.
            UserReadingForm userReadingForm = new UserReadingForm(this); // Instancia el formulario.
            userReadingForm.setVisible(true); // Hace visible el formulario.
        });

        // NOTA: Para abrir EmpleadoView, se añadiría un JMenuItem similar aquí:
        // JMenuItem itemEmployees = new JMenuItem("💼 Empleados");
        // itemEmployees.addActionListener(e -> {
        //     EmpleadoView empleadoView = new EmpleadoView();
        //     empleadoView.setVisible(true);
        // });
        // menuMantenimiento.add(itemEmployees);
    }

    /**
     * Añade un efecto visual de cambio de color al pasar el mouse por encima
     * de un JMenuItem (efecto hover).
     * @param item El JMenuItem al que se le aplicará el efecto hover.
     */
    private void addHoverEffect(JMenuItem item) {
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(230, 230, 230)); // Cambia el fondo a un gris claro.
                item.setOpaque(true); // Hace que el fondo sea visible.
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setOpaque(false); // Vuelve el fondo a transparente.
            }
        });
    }

    /**
     * Crea el panel de bienvenida que se muestra en el centro de la ventana principal.
     * Incluye un título, subtítulo, y un panel de estado en la parte inferior.
     */
    private void createWelcomePanel() {
        // Panel principal de bienvenida con un fondo de gradiente personalizado.
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Llama al método paintComponent de la superclase.
                Graphics2D g2d = (Graphics2D) g; // Convierte Graphics a Graphics2D para dibujar con gradientes.
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Mejora la calidad de renderizado.

                // Crea un gradiente de color para el fondo del panel.
                GradientPaint gradient = new GradientPaint(
                        0, 0, COLOR_FONDO, // Punto inicial (x,y) y color inicial.
                        0, getHeight(), Color.WHITE // Punto final (x,y) y color final (blanco).
                );
                g2d.setPaint(gradient); // Establece el gradiente como la pintura actual.
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Rellena el panel con el gradiente.
            }
        };
        welcomePanel.setLayout(new BorderLayout()); // Establece el layout a BorderLayout para organizar subpaneles.

        // Panel de contenido central que contiene el título y subtítulo de bienvenida.
        JPanel contentPanel = new JPanel(new GridBagLayout()); // Usa GridBagLayout para centrar elementos.
        contentPanel.setOpaque(false); // Hace el panel transparente para que se vea el gradiente de welcomePanel.
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para controlar las restricciones de GridBagLayout.

        // Título de bienvenida.
        JLabel titleLabel = new JLabel("Bienvenido al Sistema", JLabel.CENTER); // Etiqueta con texto centrado.
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Fuente y tamaño del título.
        titleLabel.setForeground(COLOR_PRIMARIO); // Color del texto.
        gbc.gridx = 0; gbc.gridy = 0; // Posición en la cuadrícula.
        gbc.insets = new Insets(50, 20, 20, 20); // Márgenes.
        contentPanel.add(titleLabel, gbc); // Añade el título al panel de contenido.

        // Subtítulo de la bienvenida.
        JLabel subtitleLabel = new JLabel("Sistema de Gestión en Java", JLabel.CENTER); // Etiqueta con texto centrado.
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Fuente y tamaño del subtítulo.
        subtitleLabel.setForeground(Color.GRAY); // Color del texto.
        gbc.gridy = 1; // Fila 1.
        gbc.insets = new Insets(0, 20, 50, 20); // Márgenes.
        contentPanel.add(subtitleLabel, gbc); // Añade el subtítulo al panel de contenido.

        welcomePanel.add(contentPanel, BorderLayout.CENTER); // Añade el panel de contenido al centro del welcomePanel.

        // Panel de estado en la parte inferior de la ventana principal.
        JPanel statusPanel = new JPanel(new BorderLayout()); // Usa BorderLayout para alinear texto a los lados.
        statusPanel.setBackground(COLOR_PRIMARIO); // Fondo de color primario.
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Relleno interno.
        statusPanel.setPreferredSize(new Dimension(0, 40)); // Altura preferida del panel de estado.

        JLabel statusLabel = new JLabel("Sistema iniciado correctamente"); // Etiqueta de mensaje de estado.
        statusLabel.setForeground(Color.WHITE); // Color del texto.
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Fuente y tamaño.

        JLabel versionLabel = new JLabel("Versión 1.0.0"); // Etiqueta de versión.
        versionLabel.setForeground(Color.WHITE); // Color del texto.
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Fuente y tamaño.

        statusPanel.add(statusLabel, BorderLayout.WEST); // Añade la etiqueta de estado a la izquierda.
        statusPanel.add(versionLabel, BorderLayout.EAST); // Añade la etiqueta de versión a la derecha.

        welcomePanel.add(statusPanel, BorderLayout.SOUTH); // Añade el panel de estado a la parte inferior del welcomePanel.

        add(welcomePanel, BorderLayout.CENTER); // Añade el welcomePanel (que contiene todo) al centro del JFrame.
    }
}