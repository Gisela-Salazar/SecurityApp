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

    // Componentes de la interfaz
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblEmail;
    private JLabel lblPassword;
    private JCheckBox chkMostrarPassword;

    // Variables de control
    private UserDAO userDAO;
    private MainForm mainForm;

    // Colores del tema
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_EXITO = new Color(39, 174, 96);
    private final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_TEXTO = new Color(44, 62, 80);

    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setModal(true);
        setTitle("Iniciar Sesión -  Control de empleados");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(mainForm);
    }

    private void initializeComponents() {
        // Panel principal con gradiente
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Crear gradiente
                GradientPaint gradient = new GradientPaint(
                        0, 0, COLOR_FONDO,
                        0, getHeight(), Color.WHITE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Título principal
        lblTitulo = new JLabel("Control de empleados", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        // Subtítulo
        lblSubtitulo = new JLabel("Iniciar Sesión", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(COLOR_TEXTO);

        // Etiquetas
        lblEmail = new JLabel("📧 Correo Electrónico:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(COLOR_TEXTO);

        lblPassword = new JLabel("🔒 Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(COLOR_TEXTO);

        // Campos de texto
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtEmail.setToolTipText("Ingrese su correo electrónico");

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtPassword.setToolTipText("Ingrese su contraseña");

        // Checkbox para mostrar contraseña
        chkMostrarPassword = new JCheckBox("Mostrar contraseña");
        chkMostrarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkMostrarPassword.setOpaque(false);
        chkMostrarPassword.setForeground(COLOR_TEXTO);

        // Botones - Botón de login más ancho
        btnLogin = createStyledButton("🔑 INICIAR SESIÓN", COLOR_EXITO);
        btnLogin.setPreferredSize(new Dimension(280, 45)); // Aumentado de 200x40 a 280x45

        btnSalir = createStyledButton("🚪 SALIR", COLOR_PELIGRO);
        btnSalir.setPreferredSize(new Dimension(120, 40));
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                        0, 0, color,
                        0, getHeight(), color.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Aumentado el tamaño de fuente
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
                button.repaint();
            }
        });

        return button;
    }

    private void setupLayout() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setPreferredSize(new Dimension(420, 400)); // Aumentado el ancho para acomodar el botón más grande

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblTitulo, gbc);

        // Subtítulo
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(lblSubtitulo, gbc);

        // Email label
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(lblEmail, gbc);

        // Email field
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(txtEmail, gbc);

        // Password label
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(lblPassword, gbc);

        // Password field
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 5, 10);
        mainPanel.add(txtPassword, gbc);

        // Checkbox mostrar contraseña
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(chkMostrarPassword, gbc);

        // Panel de botones con layout vertical para el botón principal
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Centrar el botón de login
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(btnLogin);

        // Espacio entre botones
        buttonPanel.add(Box.createVerticalStrut(10));

        // Centrar el botón de salir
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(btnSalir);

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);

        setContentPane(mainPanel);
    }

    private void setupEventHandlers() {
        // Botón Salir
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea salir del sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Botón Login
        btnLogin.addActionListener(e -> login());

        // Checkbox mostrar contraseña
        chkMostrarPassword.addActionListener(e -> {
            if (chkMostrarPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        // Enter en los campos de texto
        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        txtEmail.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);

        // Manejar cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        LoginForm.this,
                        "¿Está seguro que desea salir del sistema?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Focus inicial en el campo email
        SwingUtilities.invokeLater(() -> txtEmail.requestFocus());
    }

    private void login() {
        // Validar campos vacíos
        if (txtEmail.getText().trim().isEmpty()) {
            showErrorMessage("Por favor, ingrese su correo electrónico");
            txtEmail.requestFocus();
            return;
        }

        if (txtPassword.getPassword().length == 0) {
            showErrorMessage("Por favor, ingrese su contraseña");
            txtPassword.requestFocus();
            return;
        }

        // Deshabilitar botón durante el proceso
        btnLogin.setEnabled(false);
        btnLogin.setText("🔄 VERIFICANDO...");

        // Usar SwingWorker para no bloquear la interfaz
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                User user = new User();
                user.setEmail(txtEmail.getText().trim());
                user.setPasswordHash(new String(txtPassword.getPassword()));

                return userDAO.authenticate(user);
            }

            @Override
            protected void done() {
                try {
                    User userAut = get();

                    // Verificar autenticación
                    if (userAut != null && userAut.getId() > 0 &&
                            userAut.getEmail().equals(txtEmail.getText().trim())) {

                        // Autenticación exitosa
                        mainForm.setUserAutenticate(userAut);
                        showSuccessMessage("¡Bienvenido " + userAut.getName() + "!");

                        // Cerrar ventana de login después de un breve delay
                        Timer timer = new Timer(1000, e -> dispose());
                        timer.setRepeats(false);
                        timer.start();

                    } else {
                        // Autenticación fallida
                        showErrorMessage("Correo electrónico o contraseña incorrectos");
                        txtPassword.setText("");
                        txtEmail.requestFocus();
                    }

                } catch (Exception ex) {
                    showErrorMessage("Error de conexión: " + ex.getMessage());
                } finally {
                    // Rehabilitar botón
                    btnLogin.setEnabled(true);
                    btnLogin.setText("🔑 INICIAR SESIÓN");
                }
            }
        };

        worker.execute();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error de Autenticación",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Acceso Concedido",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}