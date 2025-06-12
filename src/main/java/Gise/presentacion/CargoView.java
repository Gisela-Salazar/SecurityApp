package Gise.presentacion;

import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import Gise.dominio.Position;
import Gise.persistencia.PositionDAO;

public class CargoView extends JFrame {

    // Componentes de la interfaz
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Tabla y modelo
    private JTable tblCargos;
    private DefaultTableModel modeloTabla;

    // Formulario de datos
    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JButton btnGuardar;
    private JButton btnCancelar;

    // DAO
    private PositionDAO cargoDAO;

    // Variables de control
    private boolean modoEdicion = false;
    private JPanel mainpanel;
    private JPanel pnlBusqueda;
    private JLabel lblBuscar;
    private JPanel pnlTabla;
    private JPanel pnlFormulario;
    private JScrollPane scrollTabla;
    private JPanel pnlBotonesTabla;
    private JLabel lblId;
    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JScrollPane scrollDesc;
    private JPanel pnlBotones;

    public CargoView() {
        cargoDAO = new PositionDAO();
        initComponents();
        configurarTabla();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Cargos");
        // Cambio principal: usar DISPOSE_ON_CLOSE en lugar de EXIT_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Agregar WindowListener para manejar el cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // La ventana se cerrará automáticamente con DISPOSE_ON_CLOSE
                // pero podemos agregar lógica adicional aquí si es necesario
                System.out.println("Cerrando ventana de Gestión de Cargos - Regresando al menú principal");
            }
        });

        // Panel superior - Búsqueda
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        JLabel lblBuscar = new JLabel("Buscar por título:");
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");

        pnlBusqueda.add(lblBuscar);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnLimpiar);

        // Panel central - Tabla
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Lista de Cargos"));

        tblCargos = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tblCargos);
        scrollTabla.setPreferredSize(new Dimension(600, 300));

        // Panel de botones de tabla
        JPanel pnlBotonesTabla = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        pnlBotonesTabla.add(btnNuevo);
        pnlBotonesTabla.add(btnEditar);
        pnlBotonesTabla.add(btnEliminar);

        pnlTabla.add(scrollTabla, BorderLayout.CENTER);
        pnlTabla.add(pnlBotonesTabla, BorderLayout.SOUTH);

        // Panel derecho - Formulario
        JPanel pnlFormulario = crearPanelFormulario();

        // Agregar paneles al frame
        add(pnlBusqueda, BorderLayout.NORTH);
        add(pnlTabla, BorderLayout.CENTER);
        add(pnlFormulario, BorderLayout.EAST);

        // Configurar eventos
        configurarEventos();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelFormulario() {
        JPanel pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cargo"));
        pnlFormulario.setPreferredSize(new Dimension(300, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ID (oculto)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("ID:");
        pnlFormulario.add(lblId, gbc);

        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEnabled(false);
        pnlFormulario.add(txtId, gbc);

        // Título
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTitulo = new JLabel("Título:");
        pnlFormulario.add(lblTitulo, gbc);

        gbc.gridx = 1;
        txtTitulo = new JTextField(15);
        pnlFormulario.add(txtTitulo, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDescripcion = new JLabel("Descripción:");
        pnlFormulario.add(lblDescripcion, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        txtDescripcion = new JTextArea(5, 15);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        pnlFormulario.add(scrollDesc, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        JPanel pnlBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);
        pnlFormulario.add(pnlBotones, gbc);

        return pnlFormulario;
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Título", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCargos.setModel(modeloTabla);
        tblCargos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar ancho de columnas
        tblCargos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblCargos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblCargos.getColumnModel().getColumn(2).setPreferredWidth(300);
    }

    private void configurarEventos() {
        // Evento de búsqueda
        btnBuscar.addActionListener(e -> buscarCargos());
        txtBuscar.addActionListener(e -> buscarCargos());

        // Evento limpiar búsqueda
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatos();
        });

        // Eventos de botones CRUD
        btnNuevo.addActionListener(e -> nuevoCargo());
        btnEditar.addActionListener(e -> editarCargo());
        btnEliminar.addActionListener(e -> eliminarCargo());

        // Eventos del formulario
        btnGuardar.addActionListener(e -> guardarCargo());
        btnCancelar.addActionListener(e -> cancelarOperacion());

        // Evento de selección en tabla
        tblCargos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void cargarDatos() {
        try {
            ArrayList<Position> cargos = cargoDAO.getAll();
            actualizarTabla(cargos);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void buscarCargos() {
        String termino = txtBuscar.getText().trim();
        if (termino.isEmpty()) {
            cargarDatos();
            return;
        }

        try {
            ArrayList<Position> cargos = cargoDAO.search(termino);
            actualizarTabla(cargos);
        } catch (SQLException e) {
            mostrarError("Error al buscar: " + e.getMessage());
        }
    }

    private void actualizarTabla(ArrayList<Position> cargos) {
        modeloTabla.setRowCount(0);
        for (Position cargo : cargos) {
            Object[] fila = {
                    cargo.getId(),
                    cargo.getTitle(),
                    cargo.getDescription()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void nuevoCargo() {
        modoEdicion = false;
        limpiarFormulario();
        habilitarFormulario(true);
        txtTitulo.requestFocus();
    }

    private void editarCargo() {
        int filaSeleccionada = tblCargos.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un cargo para editar.");
            return;
        }

        modoEdicion = true;
        habilitarFormulario(true);
        txtTitulo.requestFocus();
    }

    private void eliminarCargo() {
        int filaSeleccionada = tblCargos.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un cargo para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este cargo?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                Position cargo = new Position();
                cargo.setId(id);

                if (cargoDAO.delete(cargo)) {
                    mostrarInformacion("Cargo eliminado exitosamente.");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarError("No se pudo eliminar el cargo.");
                }
            } catch (SQLException e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void guardarCargo() {
        if (!validarDatos()) {
            return;
        }

        try {
            Position cargo = new Position();
            cargo.setTitle(txtTitulo.getText().trim());
            cargo.setDescription(txtDescripcion.getText().trim());

            if (modoEdicion) {
                cargo.setId(Integer.parseInt(txtId.getText()));
                if (cargoDAO.update(cargo)) {
                    mostrarInformacion("Cargo actualizado exitosamente.");
                } else {
                    mostrarError("No se pudo actualizar el cargo.");
                }
            } else {
                Position cargoCreado = cargoDAO.create(cargo);
                if (cargoCreado != null) {
                    mostrarInformacion("Cargo creado exitosamente.");
                } else {
                    mostrarError("No se pudo crear el cargo.");
                }
            }

            cargarDatos();
            cancelarOperacion();

        } catch (SQLException e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    private void cancelarOperacion() {
        habilitarFormulario(false);
        limpiarFormulario();
        modoEdicion = false;
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tblCargos.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtTitulo.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());

            // Cargar descripción completa
            try {
                int id = Integer.parseInt(txtId.getText());
                Position cargo = cargoDAO.getById(id);
                if (cargo != null) {
                    txtDescripcion.setText(cargo.getDescription());
                }
            } catch (SQLException e) {
                mostrarError("Error al cargar detalles: " + e.getMessage());
            }
        }
    }

    private boolean validarDatos() {
        if (txtTitulo.getText().trim().isEmpty()) {
            mostrarAdvertencia("El título es obligatorio.");
            txtTitulo.requestFocus();
            return false;
        }

        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAdvertencia("La descripción es obligatoria.");
            txtDescripcion.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
    }

    private void habilitarFormulario(boolean habilitar) {
        txtTitulo.setEnabled(habilitar);
        txtDescripcion.setEnabled(habilitar);
        btnGuardar.setEnabled(habilitar);
        btnCancelar.setEnabled(habilitar);
    }

    // Métodos de mensajes
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CargoView().setVisible(true);
        });
    }
}