package Gise.presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Gise.dominio.Employee;
import Gise.dominio.Position;
import Gise.persistencia.EmployeeDAO;
import Gise.persistencia.PositionDAO;

public class EmpleadoView extends JFrame {

    // Componentes de la interfaz
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Tabla y modelo
    private JTable tblEmpleados;
    private DefaultTableModel modeloTabla;

    // Formulario de datos
    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<Position> cmbCargo;
    private JTextField txtFechaContratacion;
    private JTextField txtSalario;
    private JButton btnGuardar;
    private JButton btnCancelar;

    // DAOs
    private EmployeeDAO empleadoDAO;
    private PositionDAO cargoDAO;

    // Variables de control
    private boolean modoEdicion = false;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private JPanel pnlBusqueda;
    private JLabel lblBuscar;
    private JPanel pnlTabla;
    private JScrollPane scrollTabla;
    private JPanel pnlBotonesTabla;
    private JPanel pnlFormulario;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblCargo;
    private JLabel lblFecha;
    private JButton btnCalendario;
    private JLabel lblSalario;
    private JPanel pnlBotones;

    public EmpleadoView() {
        empleadoDAO = new EmployeeDAO();
        cargoDAO = new PositionDAO();
        initComponents();
        configurarTabla();
        cargarCargos();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Empleados");
        // Cambio principal: usar DISPOSE_ON_CLOSE en lugar de EXIT_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Agregar WindowListener para manejar el cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // La ventana se cerrará automáticamente con DISPOSE_ON_CLOSE
                // pero podemos agregar lógica adicional aquí si es necesario
                System.out.println("Cerrando ventana de Gestión de Empleados - Regresando al menú principal");
            }
        });

        // Panel superior - Búsqueda
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        JLabel lblBuscar = new JLabel("Buscar por nombre:");
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");

        pnlBusqueda.add(lblBuscar);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnLimpiar);

        // Panel central - Tabla
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Lista de Empleados"));

        tblEmpleados = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tblEmpleados);
        scrollTabla.setPreferredSize(new Dimension(700, 300));

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
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        pnlFormulario.setPreferredSize(new Dimension(350, 400));

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

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNombre = new JLabel("Nombre:");
        pnlFormulario.add(lblNombre, gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        pnlFormulario.add(txtNombre, gbc);

        // Cargo
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCargo = new JLabel("Cargo:");
        pnlFormulario.add(lblCargo, gbc);

        gbc.gridx = 1;
        cmbCargo = new JComboBox<>();
        cmbCargo.setPreferredSize(new Dimension(200, 25));
        pnlFormulario.add(cmbCargo, gbc);

        // Fecha de contratación
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblFecha = new JLabel("Fecha Contratación:");
        pnlFormulario.add(lblFecha, gbc);

        gbc.gridx = 1;
        txtFechaContratacion = new JTextField(15);
        txtFechaContratacion.setToolTipText("Formato: dd/MM/yyyy");
        pnlFormulario.add(txtFechaContratacion, gbc);

        // Salario
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblSalario = new JLabel("Salario:");
        pnlFormulario.add(lblSalario, gbc);

        gbc.gridx = 1;
        txtSalario = new JTextField(15);
        pnlFormulario.add(txtSalario, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel pnlBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);
        pnlFormulario.add(pnlBotones, gbc);

        return pnlFormulario;
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Nombre", "Cargo", "Fecha Contratación", "Salario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados.setModel(modeloTabla);
        tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar ancho de columnas
        tblEmpleados.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblEmpleados.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblEmpleados.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblEmpleados.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblEmpleados.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void configurarEventos() {
        // Evento de búsqueda
        btnBuscar.addActionListener(e -> buscarEmpleados());
        txtBuscar.addActionListener(e -> buscarEmpleados());

        // Evento limpiar búsqueda
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatos();
        });

        // Eventos de botones CRUD
        btnNuevo.addActionListener(e -> nuevoEmpleado());
        btnEditar.addActionListener(e -> editarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());

        // Eventos del formulario
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnCancelar.addActionListener(e -> cancelarOperacion());

        // Evento de selección en tabla
        tblEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void cargarCargos() {
        try {
            ArrayList<Position> cargos = cargoDAO.getAll();
            cmbCargo.removeAllItems();
            cmbCargo.addItem(null); // Opción vacía
            for (Position cargo : cargos) {
                cmbCargo.addItem(cargo);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los cargos: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            // Como no hay un método getAll en EmployeeDAO, usamos search con cadena vacía
            ArrayList<Employee> empleados = empleadoDAO.search("");
            actualizarTabla(empleados);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void buscarEmpleados() {
        String termino = txtBuscar.getText().trim();

        try {
            ArrayList<Employee> empleados = empleadoDAO.search(termino);
            actualizarTabla(empleados);
        } catch (SQLException e) {
            mostrarError("Error al buscar: " + e.getMessage());
        }
    }

    private void actualizarTabla(ArrayList<Employee> empleados) {
        modeloTabla.setRowCount(0);
        for (Employee empleado : empleados) {
            // Obtener nombre del cargo
            String nombreCargo = "";
            try {
                Position cargo = cargoDAO.getById(empleado.getPositionId());
                if (cargo != null) {
                    nombreCargo = cargo.getTitle();
                }
            } catch (SQLException e) {
                nombreCargo = "Error al cargar";
            }

            Object[] fila = {
                    empleado.getId(),
                    empleado.getName(),
                    nombreCargo,
                    formatoFecha.format(empleado.getHireDate()),
                    empleado.getFormattedSalary()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void nuevoEmpleado() {
        modoEdicion = false;
        limpiarFormulario();
        habilitarFormulario(true);
        txtNombre.requestFocus();
    }

    private void editarEmpleado() {
        int filaSeleccionada = tblEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un empleado para editar.");
            return;
        }

        modoEdicion = true;
        habilitarFormulario(true);
        txtNombre.requestFocus();
    }

    private void eliminarEmpleado() {
        int filaSeleccionada = tblEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un empleado para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este empleado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                Employee empleado = new Employee();
                empleado.setId(id);

                if (empleadoDAO.delete(empleado)) {
                    mostrarInformacion("Empleado eliminado exitosamente.");
                    cargarDatos();
                    limpiarFormulario();
                } else {
                    mostrarError("No se pudo eliminar el empleado.");
                }
            } catch (SQLException e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void guardarEmpleado() {
        if (!validarDatos()) {
            return;
        }

        try {
            Employee empleado = new Employee();
            empleado.setName(txtNombre.getText().trim());

            Position cargoSeleccionado = (Position) cmbCargo.getSelectedItem();
            empleado.setPositionId(cargoSeleccionado.getId());

            Date fechaContratacion = formatoFecha.parse(txtFechaContratacion.getText().trim());
            empleado.setHireDate(fechaContratacion);

            double salario = Double.parseDouble(txtSalario.getText().trim());
            empleado.setSalary(salario);

            if (modoEdicion) {
                empleado.setId(Integer.parseInt(txtId.getText()));
                if (empleadoDAO.update(empleado)) {
                    mostrarInformacion("Empleado actualizado exitosamente.");
                } else {
                    mostrarError("No se pudo actualizar el empleado.");
                }
            } else {
                Employee empleadoCreado = empleadoDAO.create(empleado);
                if (empleadoCreado != null) {
                    mostrarInformacion("Empleado creado exitosamente.");
                } else {
                    mostrarError("No se pudo crear el empleado.");
                }
            }

            cargarDatos();
            cancelarOperacion();

        } catch (SQLException e) {
            mostrarError("Error al guardar: " + e.getMessage());
        } catch (ParseException e) {
            mostrarError("Formato de fecha inválido. Use dd/MM/yyyy");
        } catch (NumberFormatException e) {
            mostrarError("El salario debe ser un número válido.");
        }
    }

    private void cancelarOperacion() {
        habilitarFormulario(false);
        limpiarFormulario();
        modoEdicion = false;
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tblEmpleados.getSelectedRow();
        if (filaSeleccionada != -1) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                Employee empleado = empleadoDAO.getById(id);

                if (empleado != null) {
                    txtId.setText(String.valueOf(empleado.getId()));
                    txtNombre.setText(empleado.getName());
                    txtFechaContratacion.setText(formatoFecha.format(empleado.getHireDate()));
                    txtSalario.setText(String.valueOf(empleado.getSalary()));

                    // Seleccionar el cargo en el combo
                    for (int i = 0; i < cmbCargo.getItemCount(); i++) {
                        Position cargo = cmbCargo.getItemAt(i);
                        if (cargo != null && cargo.getId() == empleado.getPositionId()) {
                            cmbCargo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                mostrarError("Error al cargar detalles: " + e.getMessage());
            }
        }
    }

    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (cmbCargo.getSelectedItem() == null) {
            mostrarAdvertencia("Debe seleccionar un cargo.");
            cmbCargo.requestFocus();
            return false;
        }

        if (txtFechaContratacion.getText().trim().isEmpty()) {
            mostrarAdvertencia("La fecha de contratación es obligatoria.");
            txtFechaContratacion.requestFocus();
            return false;
        }

        try {
            formatoFecha.parse(txtFechaContratacion.getText().trim());
        } catch (ParseException e) {
            mostrarAdvertencia("Formato de fecha inválido. Use dd/MM/yyyy");
            txtFechaContratacion.requestFocus();
            return false;
        }

        if (txtSalario.getText().trim().isEmpty()) {
            mostrarAdvertencia("El salario es obligatorio.");
            txtSalario.requestFocus();
            return false;
        }

        try {
            double salario = Double.parseDouble(txtSalario.getText().trim());
            if (salario <= 0) {
                mostrarAdvertencia("El salario debe ser mayor que cero.");
                txtSalario.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El salario debe ser un número válido.");
            txtSalario.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        cmbCargo.setSelectedIndex(0);
        txtFechaContratacion.setText("");
        txtSalario.setText("");
    }

    private void habilitarFormulario(boolean habilitar) {
        txtNombre.setEnabled(habilitar);
        cmbCargo.setEnabled(habilitar);
        txtFechaContratacion.setEnabled(habilitar);
        txtSalario.setEnabled(habilitar);
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
            new EmpleadoView().setVisible(true);
        });
    }
}