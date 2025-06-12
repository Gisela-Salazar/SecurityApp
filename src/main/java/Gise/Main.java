package Gise;

import Gise.presentacion.LoginForm;
import Gise.presentacion.MainForm;
import Gise.presentacion.EmpleadoView;
import Gise.presentacion.CargoView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    // Colores para el tema de la aplicación
    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private static final Color COLOR_FONDO = new Color(236, 240, 241);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);

    public static void main(String[] args) {
        // Configurar propiedades del sistema para mejor apariencia
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            // Crear ventana principal con menú mejorado
            JFrame menuPrincipal = crearMenuPrincipal();
            menuPrincipal.setVisible(true);

            // Mostrar login
            LoginForm loginForm = new LoginForm(new MainForm());
            loginForm.setVisible(true);
        });
    }

    private static void setupUIDefaults() {
        // Configurar fuentes por defecto
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 14);

        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("TitledBorder.font", titleFont);

        // Configurar colores
        UIManager.put("Button.background", new Color(240, 240, 240));
        UIManager.put("Panel.background", new Color(250, 250, 250));
        UIManager.put("Table.selectionBackground", new Color(184, 207, 229));
        UIManager.put("Table.gridColor", new Color(220, 220, 220));
    }

    private static JFrame crearMenuPrincipal() {
        // Crear ventana principal
        JFrame frame = new JFrame("Sistema de gestión de empleados");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        // Configurar apariencia por defecto
        setupUIDefaults();

        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel() {
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
        panelPrincipal.setLayout(new BorderLayout());

        // Título principal
        JLabel lblTitulo = new JLabel("GESTION DE EMPLEADOS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Gestión Integral de Recursos Humanos", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Panel de encabezado
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botón Empleados
        JButton btnEmpleados = crearBotonMenu("👥 EMPLEADOS",
                "Gestionar información de empleados",
                COLOR_PRIMARIO);
        btnEmpleados.addActionListener(e -> {
            EmpleadoView empleadoView = new EmpleadoView();
            empleadoView.setVisible(true);
        });

        // Botón Cargos
        JButton btnCargos = crearBotonMenu("🏢 CARGOS",
                "Administrar cargos y posiciones",
                COLOR_SECUNDARIO);
        btnCargos.addActionListener(e -> {
            CargoView cargoView = new CargoView();
            cargoView.setVisible(true);
        });

        // Botón Usuarios
        JButton btnUsuarios = crearBotonMenu("👤 USUARIOS",
                "Gestionar usuarios del sistema",
                new Color(39, 174, 96));
        btnUsuarios.addActionListener(e -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });

        // Botón Salir
        JButton btnSalir = crearBotonMenu("🚪 SALIR",
                "Cerrar la aplicación",
                new Color(231, 76, 60));
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    frame,
                    "¿Está seguro que desea salir del sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Agregar botones al panel
        panelBotones.add(btnEmpleados);
        panelBotones.add(btnCargos);
        panelBotones.add(btnUsuarios);
        panelBotones.add(btnSalir);

        // Panel footer
        JPanel panelFooter = new JPanel(new FlowLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblVersion = new JLabel("Versión 1.0.0 - © 2025 Sistema GISE");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(Color.GRAY);
        panelFooter.add(lblVersion);

        // Agregar componentes al panel principal
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelFooter, BorderLayout.SOUTH);

        // Agregar panel principal al frame
        frame.add(panelPrincipal);

        return frame;
    }

    private static JButton crearBotonMenu(String texto, String descripcion, Color color) {
        JButton button = new JButton() {
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
            }
        };

        // Configurar texto
        String[] lineas = texto.split(" ", 2);
        String html = "<html><div style='text-align: center;'>" +
                "<div style='font-size: 24px; margin-bottom: 5px;'>" + lineas[0] + "</div>" +
                "<div style='font-size: 14px; font-weight: bold;'>" + (lineas.length > 1 ? lineas[1] : "") + "</div>" +
                "<div style='font-size: 10px; color: #ecf0f1; margin-top: 5px;'>" + descripcion + "</div>" +
                "</div></html>";

        button.setText(html);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(250, 120));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(descripcion);

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
}