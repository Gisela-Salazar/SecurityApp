package Gise.presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Gise.dominio.User;
import Gise.persistencia.UserDAO;

/**
 * La clase LoginForm representa la ventana de inicio de sesión de la aplicación.
 * Extiende JDialog, lo que significa que es un cuadro de diálogo modal
 * que se utiliza para solicitar las credenciales del usuario (email y contraseña)
 * para acceder a la aplicación principal.
 */
public class LoginForm extends JDialog {

    // Componentes de la interfaz de usuario (UI)
    private JPanel mainPanel; // Panel principal que contendrá todos los demás componentes.
    private JTextField txtEmail; // Campo de texto para que el usuario ingrese su correo electrónico.
    private JPasswordField txtPassword; // Campo de contraseña para la entrada segura de la contraseña.
    private JButton btnLogin; // Botón para iniciar sesión.
    private JButton btnSalir; // Botón para salir de la aplicación.
    private JLabel lblTitulo; // Etiqueta para el título principal de la ventana.
    private JLabel lblSubtitulo; // Etiqueta para el subtítulo de la ventana (ej. "Iniciar Sesión").
    private JLabel lblEmail; // Etiqueta para el campo de correo electrónico.
    private JLabel lblPassword; // Etiqueta para el campo de contraseña.
    private JCheckBox chkMostrarPassword; // Checkbox para alternar la visibilidad de la contraseña.

    // Variables de control de la lógica de la aplicación
    private UserDAO userDAO; // Objeto Data Access Object para interactuar con la base de datos para usuarios.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.

    // Colores personalizados para el tema de la interfaz
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185); // Color azul oscuro para elementos principales.
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219); // Color azul más claro para elementos secundarios.
    private final Color COLOR_EXITO = new Color(39, 174, 96); // Color verde para indicar éxito.
    private final Color COLOR_PELIGRO = new Color(231, 76, 60); // Color rojo para indicar peligro o acción de salir.
    private final Color COLOR_FONDO = new Color(236, 240, 241); // Color gris claro para el fondo.
    private final Color COLOR_TEXTO = new Color(44, 62, 80); // Color gris oscuro para el texto.

    /**
     * Constructor de la clase LoginForm.
     * Inicializa la ventana de inicio de sesión.
     * @param mainForm La referencia a la ventana principal de la aplicación.
     */
    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm; // Asigna la referencia a la ventana principal.
        userDAO = new UserDAO(); // Instancia el UserDAO para las operaciones de base de datos.

        initializeComponents(); // Llama al método para inicializar todos los componentes de la UI.
        setupLayout(); // Llama al método para configurar la disposición de los componentes.
        setupEventHandlers(); // Llama al método para configurar los manejadores de eventos.

        setModal(true); // Hace que el diálogo sea modal, bloqueando la interacción con otras ventanas hasta que se cierre.
        setTitle("Iniciar Sesión - Control de empleados"); // Establece el título de la ventana.
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impide que la ventana se cierre directamente al hacer clic en la "X".
        setResizable(false); // Impide que el usuario redimensione la ventana.
        pack(); // Ajusta el tamaño de la ventana para que contenga todos sus componentes.
        setLocationRelativeTo(mainForm); // Centra la ventana de login con respecto a la ventana principal.
    }

    /**
     * Inicializa y configura todos los componentes visuales de la interfaz de usuario del formulario de login.
     */
    private void initializeComponents() {
        // Panel principal con un fondo de gradiente personalizado.
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Llama al método paintComponent de la superclase.
                Graphics2D g2d = (Graphics2D) g; // Convierte el objeto Graphics a Graphics2D para capacidades avanzadas.
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Mejora la calidad de renderizado.

                // Crea un objeto GradientPaint para definir el gradiente de color.
                GradientPaint gradient = new GradientPaint(
                        0, 0, COLOR_FONDO, // Punto inicial (x,y) y color inicial.
                        0, getHeight(), Color.WHITE // Punto final (x,y) y color final.
                );
                g2d.setPaint(gradient); // Establece el gradiente como la pintura actual.
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Rellena el panel con el gradiente.
            }
        };

        // Configuración del título principal de la aplicación.
        lblTitulo = new JLabel("Control de empleados", JLabel.CENTER); // Crea una etiqueta con texto centrado.
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Establece la fuente, estilo y tamaño.
        lblTitulo.setForeground(COLOR_PRIMARIO); // Establece el color del texto.

        // Configuración del subtítulo del formulario de login.
        lblSubtitulo = new JLabel("Iniciar Sesión", JLabel.CENTER); // Crea una etiqueta para el subtítulo.
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Establece la fuente y tamaño.
        lblSubtitulo.setForeground(COLOR_TEXTO); // Establece el color del texto.

        // Configuración de la etiqueta para el campo de correo electrónico.
        lblEmail = new JLabel("📧 Correo Electrónico:"); // Etiqueta con un emoji y texto.
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Establece la fuente y estilo.
        lblEmail.setForeground(COLOR_TEXTO); // Establece el color del texto.

        // Configuración de la etiqueta para el campo de contraseña.
        lblPassword = new JLabel("🔒 Contraseña:"); // Etiqueta con un emoji y texto.
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Establece la fuente y estilo.
        lblPassword.setForeground(COLOR_TEXTO); // Establece el color del texto.

        // Configuración del campo de texto para el correo electrónico.
        txtEmail = new JTextField(20); // Crea un campo de texto con un ancho preferido de 20 columnas.
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Establece la fuente y tamaño.
        txtEmail.setBorder(BorderFactory.createCompoundBorder( // Establece un borde compuesto.
                BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1), // Borde de línea de un pixel con color secundario.
                BorderFactory.createEmptyBorder(8, 10, 8, 10) // Relleno interno del campo de texto.
        ));
        txtEmail.setToolTipText("Ingrese su correo electrónico"); // Muestra un tooltip al pasar el mouse.

        // Configuración del campo de contraseña.
        txtPassword = new JPasswordField(20); // Crea un campo de contraseña.
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Establece la fuente y tamaño.
        txtPassword.setBorder(BorderFactory.createCompoundBorder( // Establece un borde compuesto.
                BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1), // Borde de línea.
                BorderFactory.createEmptyBorder(8, 10, 8, 10) // Relleno interno.
        ));
        txtPassword.setToolTipText("Ingrese su contraseña"); // Muestra un tooltip.

        // Configuración del checkbox para mostrar/ocultar la contraseña.
        chkMostrarPassword = new JCheckBox("Mostrar contraseña"); // Crea un checkbox.
        chkMostrarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Establece la fuente y tamaño.
        chkMostrarPassword.setOpaque(false); // Hace que el checkbox sea transparente para ver el gradiente del fondo.
        chkMostrarPassword.setForeground(COLOR_TEXTO); // Establece el color del texto.

        // Configuración de los botones, utilizando un método auxiliar para el estilo.
        btnLogin = createStyledButton("🔑 INICIAR SESIÓN", COLOR_EXITO); // Crea el botón de login con estilo y color de éxito.
        btnLogin.setPreferredSize(new Dimension(280, 45)); // Establece el tamaño preferido del botón.

        btnSalir = createStyledButton("🚪 SALIR", COLOR_PELIGRO); // Crea el botón de salir con estilo y color de peligro.
        btnSalir.setPreferredSize(new Dimension(120, 40)); // Establece el tamaño preferido del botón.
    }

    /**
     * Crea un botón JButton con un estilo personalizado, incluyendo gradiente de fondo,
     * colores de texto, fuente, y efectos hover.
     * @param text El texto a mostrar en el botón.
     * @param color El color base para el gradiente y los efectos del botón.
     * @return Un objeto JButton con el estilo aplicado.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g; // Convierte Graphics a Graphics2D.
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Habilita el antialiasing para suavizar los bordes.

                // Crea un gradiente para el fondo del botón.
                GradientPaint gradient = new GradientPaint(
                        0, 0, color, // Punto inicial (x,y) y color inicial.
                        0, getHeight(), color.darker() // Punto final (x,y) y color final (más oscuro).
                );
                g2d.setPaint(gradient); // Establece el gradiente como la pintura actual.
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); // Rellena el botón con el gradiente y esquinas redondeadas.

                super.paintComponent(g); // Llama al método paintComponent de la superclase para dibujar el texto y otros elementos.
            }
        };

        button.setForeground(Color.WHITE); // Establece el color del texto del botón a blanco.
        button.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Establece la fuente, estilo y tamaño del texto del botón.
        button.setFocusPainted(false); // Deshabilita el pintado del borde de enfoque.
        button.setBorderPainted(false); // Deshabilita el pintado del borde del botón.
        button.setContentAreaFilled(false); // Permite que el método paintComponent dibuje el fondo.
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor a una mano cuando está sobre el botón.

        // Añade un Listener para los efectos hover (cuando el mouse entra y sale del botón).
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter()); // Cambia el color de fondo a uno más brillante al entrar el mouse.
                button.repaint(); // Vuelve a pintar el botón para aplicar el cambio de color.
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color); // Restaura el color de fondo original al salir el mouse.
                button.repaint(); // Vuelve a pintar el botón.
            }
        });

        return button; // Retorna el botón estilizado.
    }

    /**
     * Configura la disposición de los componentes dentro del `mainPanel`
     * utilizando un `GridBagLayout` para un control preciso de la posición.
     */
    private void setupLayout() {
        mainPanel.setLayout(new GridBagLayout()); // Establece el layout del panel principal a GridBagLayout.
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40)); // Añade un borde vacío para padding.
        mainPanel.setPreferredSize(new Dimension(420, 400)); // Establece el tamaño preferido del panel principal.

        GridBagConstraints gbc = new GridBagConstraints(); // Crea un objeto GridBagConstraints para controlar la disposición.
        gbc.insets = new Insets(10, 10, 10, 10); // Establece los márgenes externos para todos los componentes por defecto.

        // Configuración para el título principal.
        gbc.gridx = 0; gbc.gridy = 0; // Posición en la cuadrícula (columna 0, fila 0).
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.anchor = GridBagConstraints.CENTER; // Centra el componente en su celda.
        mainPanel.add(lblTitulo, gbc); // Añade el título al panel con las restricciones definidas.

        // Configuración para el subtítulo.
        gbc.gridy = 1; // Fila 1.
        gbc.insets = new Insets(0, 10, 20, 10); // Márgenes específicos para el subtítulo.
        mainPanel.add(lblSubtitulo, gbc); // Añade el subtítulo.

        // Configuración para la etiqueta de correo electrónico.
        gbc.gridy = 2; // Fila 2.
        gbc.gridwidth = 1; // Ocupa 1 columna.
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda.
        gbc.insets = new Insets(10, 10, 5, 10); // Márgenes específicos.
        mainPanel.add(lblEmail, gbc); // Añade la etiqueta de email.

        // Configuración para el campo de texto de correo electrónico.
        gbc.gridy = 3; // Fila 3.
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que el componente se expanda horizontalmente.
        gbc.insets = new Insets(0, 10, 10, 10); // Márgenes específicos.
        mainPanel.add(txtEmail, gbc); // Añade el campo de email.

        // Configuración para la etiqueta de contraseña.
        gbc.gridy = 4; // Fila 4.
        gbc.gridwidth = 1; // Ocupa 1 columna.
        gbc.fill = GridBagConstraints.NONE; // No se expande.
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda.
        gbc.insets = new Insets(10, 10, 5, 10); // Márgenes específicos.
        mainPanel.add(lblPassword, gbc); // Añade la etiqueta de contraseña.

        // Configuración para el campo de contraseña.
        gbc.gridy = 5; // Fila 5.
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Se expande horizontalmente.
        gbc.insets = new Insets(0, 10, 5, 10); // Márgenes específicos.
        mainPanel.add(txtPassword, gbc); // Añade el campo de contraseña.

        // Configuración para el checkbox de mostrar contraseña.
        gbc.gridy = 6; // Fila 6.
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.fill = GridBagConstraints.NONE; // No se expande.
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda.
        gbc.insets = new Insets(0, 10, 20, 10); // Márgenes específicos.
        mainPanel.add(chkMostrarPassword, gbc); // Añade el checkbox.

        // Panel para los botones (permite un layout vertical para los botones).
        JPanel buttonPanel = new JPanel(); // Crea un nuevo panel para organizar los botones.
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Establece un BoxLayout vertical.
        buttonPanel.setOpaque(false); // Hace que el panel sea transparente para ver el gradiente del fondo.

        // Centra el botón de login y lo añade al panel de botones.
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // Alinea el botón al centro en el eje X.
        buttonPanel.add(btnLogin); // Añade el botón de login.

        // Espacio vertical entre los botones.
        buttonPanel.add(Box.createVerticalStrut(10)); // Añade un espacio vertical de 10 píxeles.

        // Centra el botón de salir y lo añade al panel de botones.
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT); // Alinea el botón al centro en el eje X.
        buttonPanel.add(btnSalir); // Añade el botón de salir.

        // Configuración para el panel de botones dentro del mainPanel.
        gbc.gridy = 7; // Fila 7.
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Se expande horizontalmente.
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes específicos.
        mainPanel.add(buttonPanel, gbc); // Añade el panel de botones.

        setContentPane(mainPanel); // Establece el mainPanel como el panel de contenido de la ventana.
    }

    /**
     * Configura los manejadores de eventos (listeners) para los componentes de la interfaz.
     * Esto incluye acciones para botones, checkbox y eventos de teclado.
     */
    private void setupEventHandlers() {
        // Manejador de eventos para el botón "Salir".
        btnSalir.addActionListener(e -> { // Añade un ActionListener al botón Salir.
            int confirmacion = JOptionPane.showConfirmDialog( // Muestra un cuadro de diálogo de confirmación.
                    this, // Componente padre para el diálogo.
                    "¿Está seguro que desea salir del sistema?", // Mensaje del diálogo.
                    "Confirmar Salida", // Título del diálogo.
                    JOptionPane.YES_NO_OPTION, // Opciones (Sí/No).
                    JOptionPane.QUESTION_MESSAGE // Tipo de mensaje (icono de pregunta).
            );

            if (confirmacion == JOptionPane.YES_OPTION) { // Si el usuario selecciona "Sí".
                System.exit(0); // Termina la aplicación.
            }
        });

        // Manejador de eventos para el botón "Login".
        btnLogin.addActionListener(e -> login()); // Al hacer clic, llama al método login().

        // Manejador de eventos para el checkbox "Mostrar contraseña".
        chkMostrarPassword.addActionListener(e -> { // Añade un ActionListener al checkbox.
            if (chkMostrarPassword.isSelected()) { // Si el checkbox está seleccionado.
                txtPassword.setEchoChar((char) 0); // Muestra los caracteres de la contraseña (deshabilita el echo char).
            } else { // Si el checkbox no está seleccionado.
                txtPassword.setEchoChar('•'); // Oculta los caracteres de la contraseña con un punto.
            }
        });

        // KeyListener para permitir iniciar sesión presionando Enter en los campos de texto.
        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) { // Se llama cuando se presiona una tecla.
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Si la tecla presionada es Enter.
                    login(); // Llama al método login().
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {} // No implementado.

            @Override
            public void keyReleased(KeyEvent e) {} // No implementado.
        };

        txtEmail.addKeyListener(enterKeyListener); // Añade el KeyListener al campo de email.
        txtPassword.addKeyListener(enterKeyListener); // Añade el KeyListener al campo de contraseña.

        // Manejador de eventos para el cierre de la ventana (hacer clic en la "X" del marco).
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { // Se llama cuando el usuario intenta cerrar la ventana.
                int confirmacion = JOptionPane.showConfirmDialog(
                        LoginForm.this, // Referencia al propio LoginForm.
                        "¿Está seguro que desea salir del sistema?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0); // Termina la aplicación si se confirma la salida.
                }
            }
        });

        // Establece el foco inicial en el campo de correo electrónico después de que la interfaz esté completamente visible.
        SwingUtilities.invokeLater(() -> txtEmail.requestFocus());
    }

    /**
     * Realiza la lógica de autenticación del usuario.
     * Valida los campos, deshabilita el botón de login, intenta autenticar al usuario
     * en un hilo de fondo (usando SwingWorker) y maneja el resultado de la autenticación.
     */
    private void login() {
        // Validación de campos vacíos.
        if (txtEmail.getText().trim().isEmpty()) { // Comprueba si el campo de email está vacío o solo contiene espacios.
            showErrorMessage("Por favor, ingrese su correo electrónico"); // Muestra un mensaje de error.
            txtEmail.requestFocus(); // Establece el foco de nuevo en el campo de email.
            return; // Sale del método.
        }

        if (txtPassword.getPassword().length == 0) { // Comprueba si el campo de contraseña está vacío.
            showErrorMessage("Por favor, ingrese su contraseña"); // Muestra un mensaje de error.
            txtPassword.requestFocus(); // Establece el foco de nuevo en el campo de contraseña.
            return; // Sale del método.
        }

        // Deshabilita el botón de login y cambia su texto durante el proceso de autenticación.
        btnLogin.setEnabled(false); // Deshabilita el botón para evitar múltiples clics.
        btnLogin.setText("🔄 VERIFICANDO..."); // Cambia el texto del botón para indicar que está procesando.

        // Usa SwingWorker para realizar la operación de autenticación en un hilo de fondo.
        // Esto evita que la interfaz de usuario se congele (bloquee).
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                User user = new User(); // Crea un nuevo objeto User para la autenticación.
                user.setEmail(txtEmail.getText().trim()); // Establece el email del usuario.
                user.setPasswordHash(new String(txtPassword.getPassword())); // Establece la contraseña (convertida a String).

                return userDAO.authenticate(user); // Llama al método de autenticación del UserDAO.
            }

            @Override
            protected void done() {
                try {
                    User userAut = get(); // Obtiene el resultado del hilo de fondo (el usuario autenticado o null).

                    // Verifica el resultado de la autenticación.
                    if (userAut != null && userAut.getId() > 0 &&
                            userAut.getEmail().equals(txtEmail.getText().trim())) {
                        // Autenticación exitosa.
                        mainForm.setUserAutenticate(userAut); // Establece el usuario autenticado en el MainForm.
                        showSuccessMessage("¡Bienvenido " + userAut.getName() + "!"); // Muestra un mensaje de bienvenida.

                        // Cierra la ventana de login después de un breve retraso.
                        Timer timer = new Timer(1000, e -> dispose()); // Crea un temporizador que se dispara una vez.
                        timer.setRepeats(false); // Asegura que el temporizador solo se ejecute una vez.
                        timer.start(); // Inicia el temporizador.

                    } else {
                        // Autenticación fallida.
                        showErrorMessage("Correo electrónico o contraseña incorrectos"); // Muestra un mensaje de error.
                        txtPassword.setText(""); // Limpia el campo de contraseña.
                        txtEmail.requestFocus(); // Establece el foco en el campo de email.
                    }

                } catch (Exception ex) {
                    // Captura cualquier excepción que ocurra durante la autenticación.
                    showErrorMessage("Error de conexión: " + ex.getMessage()); // Muestra un mensaje de error de conexión.
                } finally {
                    // Siempre se ejecuta, independientemente de si hubo éxito o error.
                    btnLogin.setEnabled(true); // Vuelve a habilitar el botón de login.
                    btnLogin.setText("🔑 INICIAR SESIÓN"); // Restaura el texto original del botón.
                }
            }
        };

        worker.execute(); // Ejecuta el SwingWorker, iniciando el proceso de autenticación en un hilo separado.
    }

    /**
     * Muestra un cuadro de diálogo de mensaje de error.
     * @param message El mensaje de error a mostrar.
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                this, // Componente padre para el diálogo.
                message, // El mensaje a mostrar.
                "Error de Autenticación", // Título del diálogo.
                JOptionPane.ERROR_MESSAGE // Tipo de mensaje (icono de error).
        );
    }

    /**
     * Muestra un cuadro de diálogo de mensaje de éxito/información.
     * @param message El mensaje de éxito a mostrar.
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                this, // Componente padre para el diálogo.
                message, // El mensaje a mostrar.
                "Acceso Concedido", // Título del diálogo.
                JOptionPane.INFORMATION_MESSAGE // Tipo de mensaje (icono de información).
        );
    }
}
