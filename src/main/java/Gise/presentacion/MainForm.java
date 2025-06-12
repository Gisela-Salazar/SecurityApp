package Gise.presentacion;

import javax.swing.*;
import java.awt.*;
import Gise.dominio.User;

public class MainForm extends JFrame {

    private User userAutenticate;

    // Colores para el tema
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_EXITO = new Color(39, 174, 96);
    private final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private final Color COLOR_FONDO = new Color(236, 240, 241);

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm(){
        setTitle("Sistema en java de escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Configurar colores de fondo
        getContentPane().setBackground(COLOR_FONDO);

        createMenu();
        createWelcomePanel();
    }

    private void createMenu() {
        // Barra de menú con estilo
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARIO));
        menuBar.setPreferredSize(new Dimension(0, 35));
        setJMenuBar(menuBar);

        // Menú Perfil con estilo
        JMenu menuPerfil = new JMenu("👤 Perfil");
        menuPerfil.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuPerfil.setForeground(COLOR_PRIMARIO);
        menuPerfil.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuPerfil);

        // Elemento Cambiar contraseña
        JMenuItem itemChangePassword = new JMenuItem("🔐 Cambiar contraseña");
        itemChangePassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemChangePassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addHoverEffect(itemChangePassword);
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            ChangePasswordForm changePassword = new ChangePasswordForm(this);
            changePassword.setVisible(true);
        });

        // Elemento Cambiar de usuario
        JMenuItem itemChangeUser = new JMenuItem("🔄 Cambiar de usuario");
        itemChangeUser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemChangeUser.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addHoverEffect(itemChangeUser);
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        // Separador
        menuPerfil.addSeparator();

        // Elemento Salir
        JMenuItem itemSalir = new JMenuItem("🚪 Salir");
        itemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemSalir.setForeground(COLOR_PELIGRO);
        itemSalir.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addHoverEffect(itemSalir);
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> System.exit(0));

        // Menú Mantenimientos con estilo
        JMenu menuMantenimiento = new JMenu("⚙️ Mantenimientos");
        menuMantenimiento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuMantenimiento.setForeground(COLOR_SECUNDARIO);
        menuMantenimiento.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(menuMantenimiento);

        // Elemento Usuarios
        JMenuItem itemUsers = new JMenuItem("👥 Usuarios");
        itemUsers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemUsers.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addHoverEffect(itemUsers);
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
        });
    }

    private void addHoverEffect(JMenuItem item) {
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(230, 230, 230));
                item.setOpaque(true);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setOpaque(false);
            }
        });
    }

    private void createWelcomePanel() {
        // Panel principal con gradiente
        JPanel welcomePanel = new JPanel() {
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
        welcomePanel.setLayout(new BorderLayout());

        // Panel de contenido central
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Título de bienvenida
        JLabel titleLabel = new JLabel("Bienvenido al Sistema", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(50, 20, 20, 20);
        contentPanel.add(titleLabel, gbc);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema de Gestión en Java", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 50, 20);
        contentPanel.add(subtitleLabel, gbc);

        welcomePanel.add(contentPanel, BorderLayout.CENTER);

        // Panel de estado en la parte inferior
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(COLOR_PRIMARIO);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statusPanel.setPreferredSize(new Dimension(0, 40));

        JLabel statusLabel = new JLabel("Sistema iniciado correctamente");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel versionLabel = new JLabel("Versión 1.0.0");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);

        welcomePanel.add(statusPanel, BorderLayout.SOUTH);

        add(welcomePanel, BorderLayout.CENTER);
    }
}