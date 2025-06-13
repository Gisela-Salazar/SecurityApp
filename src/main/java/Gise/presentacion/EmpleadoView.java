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

/**
 * La clase EmpleadoView representa la ventana principal para la gestión de empleados.
 * Permite a los usuarios buscar, crear, editar y eliminar registros de empleados,
 * así como visualizar la lista de empleados en una tabla.
 * Extiende JFrame para crear una ventana de aplicación.
 */
public class EmpleadoView extends JFrame {

    // Componentes de la interfaz de usuario (UI) para la sección de búsqueda y tabla
    private JTextField txtBuscar; // Campo de texto para ingresar el término de búsqueda de empleados.
    private JButton btnBuscar; // Botón para iniciar la búsqueda.
    private JButton btnNuevo; // Botón para iniciar la creación de un nuevo empleado.
    private JButton btnEditar; // Botón para iniciar la edición de un empleado seleccionado.
    private JButton btnEliminar; // Botón para eliminar un empleado seleccionado.
    private JButton btnLimpiar; // Botón para limpiar el campo de búsqueda y recargar todos los datos.

    // Tabla y su modelo para mostrar los datos de los empleados
    private JTable tblEmpleados; // La tabla donde se muestran los empleados.
    private DefaultTableModel modeloTabla; // El modelo de datos para la tabla, gestiona filas y columnas.

    // Componentes de la UI para el formulario de datos del empleado
    private JTextField txtId; // Campo de texto para mostrar el ID del empleado (generalmente deshabilitado para edición manual).
    private JTextField txtNombre; // Campo de texto para el nombre del empleado.
    private JComboBox<Position> cmbCargo; // ComboBox para seleccionar el cargo del empleado.
    private JTextField txtFechaContratacion; // Campo de texto para la fecha de contratación.
    private JTextField txtSalario; // Campo de texto para el salario del empleado.
    private JButton btnGuardar; // Botón para guardar un nuevo empleado o los cambios de uno existente.
    private JButton btnCancelar; // Botón para cancelar la operación actual en el formulario.

    // Objetos Data Access Object (DAOs) para interactuar con la base de datos
    private EmployeeDAO empleadoDAO; // DAO para operaciones CRUD sobre la entidad Employee.
    private PositionDAO cargoDAO; // DAO para operaciones de recuperación sobre la entidad Position.

    // Variables de control de la lógica de la vista
    private boolean modoEdicion = false; // Bandera que indica si el formulario está en modo de edición (true) o creación (false).
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); // Formateador para convertir fechas a y desde String.

    // Paneles auxiliares para organizar la interfaz (declarados como atributos de instancia)
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
    private JLabel lblSalario;
    private JPanel pnlBotones;

    /**
     * Constructor de la clase EmpleadoView.
     * Inicializa los DAOs, los componentes de la interfaz, configura la tabla
     * y carga los datos iniciales de cargos y empleados.
     */
    public EmpleadoView() {
        empleadoDAO = new EmployeeDAO(); // Instancia el DAO para empleados.
        cargoDAO = new PositionDAO(); // Instancia el DAO para cargos.
        initComponents(); // Llama al método para inicializar todos los componentes de la UI.
        configurarTabla(); // Llama al método para establecer el modelo y apariencia de la tabla.
        cargarCargos(); // Llama al método para poblar el ComboBox de cargos.
        cargarDatos(); // Llama al método para cargar los datos de empleados en la tabla.
    }

    /**
     * Inicializa y organiza todos los componentes visuales de la ventana `EmpleadoView`.
     */
    private void initComponents() {
        setTitle("Gestión de Empleados"); // Establece el título de la ventana.
        // Cambio principal: usar DISPOSE_ON_CLOSE en lugar de EXIT_ON_CLOSE.
        // Esto permite que solo se cierre esta ventana al hacer clic en la "X",
        // sin terminar toda la aplicación, lo que es útil si hay una ventana principal (MainForm).
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); // Establece el layout principal del JFrame a BorderLayout.

        // Agrega un WindowListener para manejar eventos de la ventana, como el cierre.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aunque DISPOSE_ON_CLOSE maneja el cierre, esta es una buena práctica
                // para añadir lógica adicional antes de que la ventana se destruya.
                System.out.println("Cerrando ventana de Gestión de Empleados - Regresando al menú principal");
            }
        });

        // --- Panel superior - Búsqueda ---
        pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Crea un panel con FlowLayout alineado a la izquierda.
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda")); // Añade un borde con título.

        lblBuscar = new JLabel("Buscar por nombre:"); // Etiqueta para el campo de búsqueda.
        txtBuscar = new JTextField(20); // Campo de texto para la búsqueda.
        btnBuscar = new JButton("Buscar"); // Botón para ejecutar la búsqueda.
        btnLimpiar = new JButton("Limpiar"); // Botón para limpiar la búsqueda.

        pnlBusqueda.add(lblBuscar); // Añade la etiqueta al panel de búsqueda.
        pnlBusqueda.add(txtBuscar); // Añade el campo de texto al panel de búsqueda.
        pnlBusqueda.add(btnBuscar); // Añade el botón de búsqueda al panel.
        pnlBusqueda.add(btnLimpiar); // Añade el botón de limpiar al panel.

        // --- Panel central - Tabla ---
        pnlTabla = new JPanel(new BorderLayout()); // Crea un panel para la tabla con BorderLayout.
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Lista de Empleados")); // Añade un borde con título.

        tblEmpleados = new JTable(); // Instancia la JTable.
        scrollTabla = new JScrollPane(tblEmpleados); // Envuelve la tabla en un JScrollPane para scroll.
        scrollTabla.setPreferredSize(new Dimension(700, 300)); // Establece el tamaño preferido del scroll pane.

        // Panel de botones debajo de la tabla (CRUD básico)
        pnlBotonesTabla = new JPanel(new FlowLayout()); // Crea un panel para los botones de la tabla con FlowLayout.
        btnNuevo = new JButton("Nuevo"); // Botón para añadir un nuevo empleado.
        btnEditar = new JButton("Editar"); // Botón para editar un empleado existente.
        btnEliminar = new JButton("Eliminar"); // Botón para eliminar un empleado.

        pnlBotonesTabla.add(btnNuevo); // Añade el botón Nuevo al panel de botones de la tabla.
        pnlBotonesTabla.add(btnEditar); // Añade el botón Editar al panel.
        pnlBotonesTabla.add(btnEliminar); // Añade el botón Eliminar al panel.

        pnlTabla.add(scrollTabla, BorderLayout.CENTER); // Añade el scroll pane con la tabla al centro del panel de tabla.
        pnlTabla.add(pnlBotonesTabla, BorderLayout.SOUTH); // Añade el panel de botones al sur del panel de tabla.

        // --- Panel derecho - Formulario de datos ---
        pnlFormulario = crearPanelFormulario(); // Llama a un método auxiliar para crear el panel del formulario.

        // --- Agregar todos los paneles al frame principal ---
        add(pnlBusqueda, BorderLayout.NORTH); // Añade el panel de búsqueda en la parte superior.
        add(pnlTabla, BorderLayout.CENTER); // Añade el panel de tabla en el centro.
        add(pnlFormulario, BorderLayout.EAST); // Añade el panel de formulario en la parte derecha.

        // Configura los manejadores de eventos para los botones y campos.
        configurarEventos();

        pack(); // Ajusta el tamaño de la ventana para que se adapte a sus componentes.
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
    }

    /**
     * Crea y configura el panel que contiene el formulario para ingresar/editar
     * los datos de un empleado. Utiliza GridBagLayout para una disposición flexible.
     * @return El JPanel configurado para el formulario de empleado.
     */
    private JPanel crearPanelFormulario() {
        JPanel pnlFormulario = new JPanel(new GridBagLayout()); // Crea un panel con GridBagLayout.
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Empleado")); // Añade un borde con título.
        pnlFormulario.setPreferredSize(new Dimension(350, 400)); // Establece el tamaño preferido del panel.

        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para controlar las restricciones de GridBagLayout.
        gbc.insets = new Insets(5, 5, 5, 5); // Establece los márgenes internos para los componentes.
        gbc.anchor = GridBagConstraints.WEST; // Alinea los componentes a la izquierda dentro de su celda.

        // Componente: ID del Empleado (generalmente oculto o solo lectura)
        gbc.gridx = 0; gbc.gridy = 0; // Posición en la cuadrícula (columna 0, fila 0).
        lblId = new JLabel("ID:"); // Etiqueta para el ID.
        pnlFormulario.add(lblId, gbc); // Añade la etiqueta.

        gbc.gridx = 1; // Columna 1.
        txtId = new JTextField(15); // Campo de texto para el ID.
        txtId.setEnabled(false); // Deshabilita el campo para evitar edición manual.
        pnlFormulario.add(txtId, gbc); // Añade el campo.

        // Componente: Nombre del Empleado
        gbc.gridx = 0; gbc.gridy = 1; // Columna 0, fila 1.
        lblNombre = new JLabel("Nombre:"); // Etiqueta para el nombre.
        pnlFormulario.add(lblNombre, gbc); // Añade la etiqueta.

        gbc.gridx = 1; // Columna 1.
        txtNombre = new JTextField(15); // Campo de texto para el nombre.
        pnlFormulario.add(txtNombre, gbc); // Añade el campo.

        // Componente: Cargo del Empleado
        gbc.gridx = 0; gbc.gridy = 2; // Columna 0, fila 2.
        lblCargo = new JLabel("Cargo:"); // Etiqueta para el cargo.
        pnlFormulario.add(lblCargo, gbc); // Añade la etiqueta.

        gbc.gridx = 1; // Columna 1.
        cmbCargo = new JComboBox<>(); // ComboBox para seleccionar el cargo.
        cmbCargo.setPreferredSize(new Dimension(200, 25)); // Establece el tamaño preferido del ComboBox.
        pnlFormulario.add(cmbCargo, gbc); // Añade el ComboBox.

        // Componente: Fecha de Contratación
        gbc.gridx = 0; gbc.gridy = 3; // Columna 0, fila 3.
        lblFecha = new JLabel("Fecha Contratación:"); // Etiqueta para la fecha.
        pnlFormulario.add(lblFecha, gbc); // Añade la etiqueta.

        gbc.gridx = 1; // Columna 1.
        txtFechaContratacion = new JTextField(15); // Campo de texto para la fecha.
        txtFechaContratacion.setToolTipText("Formato: dd/MM/yyyy"); // Muestra un tooltip con el formato esperado.
        pnlFormulario.add(txtFechaContratacion, gbc); // Añade el campo.

        // Componente: Salario del Empleado
        gbc.gridx = 0; gbc.gridy = 4; // Columna 0, fila 4.
        lblSalario = new JLabel("Salario:"); // Etiqueta para el salario.
        pnlFormulario.add(lblSalario, gbc); // Añade la etiqueta.

        gbc.gridx = 1; // Columna 1.
        txtSalario = new JTextField(15); // Campo de texto para el salario.
        pnlFormulario.add(txtSalario, gbc); // Añade el campo.

        // Botones del formulario (Guardar y Cancelar)
        gbc.gridx = 0; gbc.gridy = 5; // Columna 0, fila 5.
        gbc.gridwidth = 2; // Ocupa 2 columnas.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que el panel de botones se expanda horizontalmente.

        pnlBotones = new JPanel(new FlowLayout()); // Crea un panel para los botones de guardar/cancelar.
        btnGuardar = new JButton("Guardar"); // Botón para guardar datos.
        btnCancelar = new JButton("Cancelar"); // Botón para cancelar la edición/creación.

        pnlBotones.add(btnGuardar); // Añade el botón Guardar.
        pnlBotones.add(btnCancelar); // Añade el botón Cancelar.
        pnlFormulario.add(pnlBotones, gbc); // Añade el panel de botones al formulario.

        return pnlFormulario; // Retorna el panel del formulario.
    }

    /**
     * Configura el modelo de la tabla de empleados, define las columnas
     * y las propiedades de selección.
     */
    private void configurarTabla() {
        String[] columnas = {"ID", "Nombre", "Cargo", "Fecha Contratación", "Salario"}; // Nombres de las columnas.
        modeloTabla = new DefaultTableModel(columnas, 0) { // Crea un nuevo DefaultTableModel.
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que todas las celdas de la tabla no sean editables.
            }
        };
        tblEmpleados.setModel(modeloTabla); // Asigna el modelo a la tabla.
        tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite seleccionar solo una fila a la vez.

        // Configura el ancho preferido para cada columna para mejorar la presentación.
        tblEmpleados.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tblEmpleados.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tblEmpleados.getColumnModel().getColumn(2).setPreferredWidth(150); // Cargo
        tblEmpleados.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha Contratación
        tblEmpleados.getColumnModel().getColumn(4).setPreferredWidth(100); // Salario
    }

    /**
     * Configura los manejadores de eventos (ActionListeners y ListSelectionListeners)
     * para todos los botones y componentes interactivos de la interfaz.
     */
    private void configurarEventos() {
        // Evento para el botón de búsqueda y el campo de texto de búsqueda (al presionar Enter).
        btnBuscar.addActionListener(e -> buscarEmpleados()); // Al hacer clic en Buscar, llama a buscarEmpleados().
        txtBuscar.addActionListener(e -> buscarEmpleados()); // Al presionar Enter en txtBuscar, llama a buscarEmpleados().

        // Evento para el botón de limpiar búsqueda.
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText(""); // Limpia el campo de texto de búsqueda.
            cargarDatos(); // Recarga todos los datos de empleados en la tabla.
        });

        // Eventos de los botones CRUD (Crear, Editar, Eliminar).
        btnNuevo.addActionListener(e -> nuevoEmpleado()); // Llama a nuevoEmpleado() al hacer clic.
        btnEditar.addActionListener(e -> editarEmpleado()); // Llama a editarEmpleado() al hacer clic.
        btnEliminar.addActionListener(e -> eliminarEmpleado()); // Llama a eliminarEmpleado() al hacer clic.

        // Eventos de los botones del formulario (Guardar, Cancelar).
        btnGuardar.addActionListener(e -> guardarEmpleado()); // Llama a guardarEmpleado() al hacer clic.
        btnCancelar.addActionListener(e -> cancelarOperacion()); // Llama a cancelarOperacion() al hacer clic.

        // Evento de selección de fila en la tabla.
        tblEmpleados.getSelectionModel().addListSelectionListener(e -> {
            // Solo procesa el evento cuando la selección ha finalizado (para evitar múltiples eventos).
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados(); // Llama a cargarDatosSeleccionados() cuando se selecciona una fila.
            }
        });
    }

    /**
     * Carga todos los cargos (posiciones) disponibles desde la base de datos
     * y los añade al ComboBox de cargos.
     */
    private void cargarCargos() {
        try {
            ArrayList<Position> cargos = cargoDAO.getAll(); // Obtiene todos los cargos desde el DAO.
            cmbCargo.removeAllItems(); // Elimina todos los elementos actuales del ComboBox.
            cmbCargo.addItem(null); // Añade una opción vacía o nula al inicio.
            for (Position cargo : cargos) { // Itera sobre la lista de cargos.
                cmbCargo.addItem(cargo); // Añade cada cargo al ComboBox.
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los cargos: " + e.getMessage()); // Muestra un mensaje de error si falla la carga.
        }
    }

    /**
     * Carga todos los empleados desde la base de datos y los muestra en la tabla.
     * Utiliza el método `search` del DAO con una cadena vacía para obtener todos los registros.
     */
    private void cargarDatos() {
        try {
            // Como no hay un método getAll explícito en EmployeeDAO, se usa search con cadena vacía
            // para simular la obtención de todos los empleados.
            ArrayList<Employee> empleados = empleadoDAO.search("");
            actualizarTabla(empleados); // Llama a actualizarTabla para poblar la JTable.
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage()); // Muestra un error si la carga falla.
        }
    }

    /**
     * Realiza una búsqueda de empleados en la base de datos basándose en el texto
     * ingresado en el campo `txtBuscar` y actualiza la tabla con los resultados.
     */
    private void buscarEmpleados() {
        String termino = txtBuscar.getText().trim(); // Obtiene el texto de búsqueda y elimina espacios en blanco.

        try {
            ArrayList<Employee> empleados = empleadoDAO.search(termino); // Realiza la búsqueda a través del DAO.
            actualizarTabla(empleados); // Actualiza la tabla con los empleados encontrados.
        } catch (SQLException e) {
            mostrarError("Error al buscar: " + e.getMessage()); // Muestra un error si la búsqueda falla.
        }
    }

    /**
     * Actualiza el `modeloTabla` con la lista de empleados proporcionada,
     * limpiando la tabla existente y añadiendo nuevas filas.
     * @param empleados La lista de objetos Employee a mostrar en la tabla.
     */
    private void actualizarTabla(ArrayList<Employee> empleados) {
        modeloTabla.setRowCount(0); // Elimina todas las filas existentes en la tabla.
        for (Employee empleado : empleados) { // Itera sobre cada empleado en la lista.
            // Obtener el nombre del cargo para mostrarlo en la tabla (ya que la tabla muestra nombres, no IDs).
            String nombreCargo = "";
            try {
                Position cargo = cargoDAO.getById(empleado.getPositionId()); // Busca el objeto Position por su ID.
                if (cargo != null) {
                    nombreCargo = cargo.getTitle(); // Obtiene el título del cargo.
                }
            } catch (SQLException e) {
                nombreCargo = "Error al cargar"; // Si hay un error al cargar el cargo, muestra un mensaje.
            }

            // Crea una fila de datos para la tabla.
            Object[] fila = {
                    empleado.getId(), // ID del empleado.
                    empleado.getName(), // Nombre del empleado.
                    nombreCargo, // Nombre del cargo.
                    formatoFecha.format(empleado.getHireDate()), // Fecha de contratación formateada.
                    empleado.getFormattedSalary() // Salario formateado (con símbolo de moneda).
            };
            modeloTabla.addRow(fila); // Añade la fila al modelo de la tabla.
        }
    }

    /**
     * Prepara el formulario para la entrada de un nuevo empleado:
     * establece el `modoEdicion` a falso, limpia el formulario y lo habilita.
     */
    private void nuevoEmpleado() {
        modoEdicion = false; // Se establece en modo de creación.
        limpiarFormulario(); // Limpia todos los campos del formulario.
        habilitarFormulario(true); // Habilita los campos de entrada del formulario.
        txtNombre.requestFocus(); // Establece el foco en el campo de nombre.
    }

    /**
     * Prepara el formulario para editar un empleado existente:
     * verifica si hay una fila seleccionada, establece el `modoEdicion` a true,
     * y habilita el formulario.
     */
    private void editarEmpleado() {
        int filaSeleccionada = tblEmpleados.getSelectedRow(); // Obtiene el índice de la fila seleccionada.
        if (filaSeleccionada == -1) { // Si no hay ninguna fila seleccionada.
            mostrarAdvertencia("Seleccione un empleado para editar."); // Muestra un mensaje de advertencia.
            return; // Sale del método.
        }

        modoEdicion = true; // Se establece en modo de edición.
        habilitarFormulario(true); // Habilita los campos del formulario.
        txtNombre.requestFocus(); // Establece el foco en el campo de nombre.
    }

    /**
     * Elimina un empleado de la base de datos después de la confirmación del usuario.
     * Requiere que haya una fila seleccionada en la tabla.
     */
    private void eliminarEmpleado() {
        int filaSeleccionada = tblEmpleados.getSelectedRow(); // Obtiene el índice de la fila seleccionada.
        if (filaSeleccionada == -1) { // Si no hay fila seleccionada.
            mostrarAdvertencia("Seleccione un empleado para eliminar."); // Muestra advertencia.
            return; // Sale del método.
        }

        int confirmacion = JOptionPane.showConfirmDialog( // Muestra un diálogo de confirmación.
                this,
                "¿Está seguro de eliminar este empleado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) { // Si el usuario confirma la eliminación.
            try {
                int id = Integer.parseInt(txtId.getText()); // Obtiene el ID del empleado desde el campo de texto.
                Employee empleado = new Employee(); // Crea un objeto Employee.
                empleado.setId(id); // Establece el ID al objeto.

                if (empleadoDAO.delete(empleado)) { // Intenta eliminar el empleado a través del DAO.
                    mostrarInformacion("Empleado eliminado exitosamente."); // Muestra mensaje de éxito.
                    cargarDatos(); // Recarga los datos en la tabla.
                    limpiarFormulario(); // Limpia el formulario.
                } else {
                    mostrarError("No se pudo eliminar el empleado."); // Muestra error si no se eliminó.
                }
            } catch (SQLException e) {
                mostrarError("Error al eliminar: " + e.getMessage()); // Muestra error de base de datos.
            }
        }
    }

    /**
     * Guarda los datos del empleado del formulario en la base de datos.
     * Si está en `modoEdicion`, actualiza un empleado existente; de lo contrario, crea uno nuevo.
     * Realiza validaciones de datos antes de intentar guardar.
     */
    private void guardarEmpleado() {
        if (!validarDatos()) { // Llama a validarDatos() para comprobar si los campos son válidos.
            return; // Si los datos no son válidos, sale del método.
        }

        try {
            Employee empleado = new Employee(); // Crea un nuevo objeto Employee.
            empleado.setName(txtNombre.getText().trim()); // Establece el nombre.

            Position cargoSeleccionado = (Position) cmbCargo.getSelectedItem(); // Obtiene el cargo seleccionado del ComboBox.
            empleado.setPositionId(cargoSeleccionado.getId()); // Establece el ID del cargo.

            Date fechaContratacion = formatoFecha.parse(txtFechaContratacion.getText().trim()); // Parsea la fecha.
            empleado.setHireDate(fechaContratacion); // Establece la fecha de contratación.

            double salario = Double.parseDouble(txtSalario.getText().trim()); // Parsea el salario a double.
            empleado.setSalary(salario); // Establece el salario.

            if (modoEdicion) { // Si está en modo edición.
                empleado.setId(Integer.parseInt(txtId.getText())); // Obtiene el ID del empleado a actualizar.
                if (empleadoDAO.update(empleado)) { // Intenta actualizar el empleado.
                    mostrarInformacion("Empleado actualizado exitosamente."); // Mensaje de éxito.
                } else {
                    mostrarError("No se pudo actualizar el empleado."); // Mensaje de error.
                }
            } else { // Si está en modo creación.
                Employee empleadoCreado = empleadoDAO.create(empleado); // Intenta crear un nuevo empleado.
                if (empleadoCreado != null) { // Si la creación fue exitosa (se obtuvo un objeto).
                    mostrarInformacion("Empleado creado exitosamente."); // Mensaje de éxito.
                } else {
                    mostrarError("No se pudo crear el empleado."); // Mensaje de error.
                }
            }

            cargarDatos(); // Recarga los datos en la tabla para reflejar los cambios.
            cancelarOperacion(); // Restablece el formulario.

        } catch (SQLException e) {
            mostrarError("Error al guardar: " + e.getMessage()); // Error de base de datos.
        } catch (ParseException e) {
            mostrarError("Formato de fecha inválido. Use dd/MM/yyyy"); // Error de formato de fecha.
        } catch (NumberFormatException e) {
            mostrarError("El salario debe ser un número válido."); // Error de formato de número para salario.
        }
    }

    /**
     * Cancela la operación actual en el formulario:
     * deshabilita los campos, limpia el formulario y sale del modo edición.
     */
    private void cancelarOperacion() {
        habilitarFormulario(false); // Deshabilita los campos del formulario.
        limpiarFormulario(); // Limpia los campos.
        modoEdicion = false; // Restablece el modo a creación.
    }

    /**
     * Carga los datos del empleado seleccionado en la tabla en el formulario
     * para su visualización o edición.
     */
    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tblEmpleados.getSelectedRow(); // Obtiene el índice de la fila seleccionada.
        if (filaSeleccionada != -1) { // Si hay una fila seleccionada.
            try {
                // Obtiene el ID del empleado desde la primera columna de la fila seleccionada.
                int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                Employee empleado = empleadoDAO.getById(id); // Busca el empleado en la base de datos por su ID.

                if (empleado != null) { // Si el empleado fue encontrado.
                    txtId.setText(String.valueOf(empleado.getId())); // Muestra el ID.
                    txtNombre.setText(empleado.getName()); // Muestra el nombre.
                    txtFechaContratacion.setText(formatoFecha.format(empleado.getHireDate())); // Muestra la fecha formateada.
                    txtSalario.setText(String.valueOf(empleado.getSalary())); // Muestra el salario.

                    // Selecciona el cargo correspondiente en el ComboBox.
                    for (int i = 0; i < cmbCargo.getItemCount(); i++) {
                        Position cargo = cmbCargo.getItemAt(i); // Obtiene cada ítem del ComboBox.
                        // Comprueba si el ítem no es nulo y su ID coincide con el ID de posición del empleado.
                        if (cargo != null && cargo.getId() == empleado.getPositionId()) {
                            cmbCargo.setSelectedIndex(i); // Selecciona ese ítem.
                            break; // Sale del bucle una vez encontrado.
                        }
                    }
                }
            } catch (SQLException e) {
                mostrarError("Error al cargar detalles: " + e.getMessage()); // Error de base de datos.
            }
        }
    }

    /**
     * Valida los datos ingresados en el formulario antes de intentar guardar.
     * Muestra mensajes de advertencia si algún campo es inválido o está vacío.
     * @return true si todos los datos son válidos, false en caso contrario.
     */
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) { // Valida que el nombre no esté vacío.
            mostrarAdvertencia("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (cmbCargo.getSelectedItem() == null) { // Valida que se haya seleccionado un cargo.
            mostrarAdvertencia("Debe seleccionar un cargo.");
            cmbCargo.requestFocus();
            return false;
        }

        if (txtFechaContratacion.getText().trim().isEmpty()) { // Valida que la fecha no esté vacía.
            mostrarAdvertencia("La fecha de contratación es obligatoria.");
            txtFechaContratacion.requestFocus();
            return false;
        }

        try {
            formatoFecha.parse(txtFechaContratacion.getText().trim()); // Intenta parsear la fecha para validar el formato.
        } catch (ParseException e) {
            mostrarAdvertencia("Formato de fecha inválido. Use dd/MM/yyyy");
            txtFechaContratacion.requestFocus();
            return false;
        }

        if (txtSalario.getText().trim().isEmpty()) { // Valida que el salario no esté vacío.
            mostrarAdvertencia("El salario es obligatorio.");
            txtSalario.requestFocus();
            return false;
        }

        try {
            double salario = Double.parseDouble(txtSalario.getText().trim()); // Intenta parsear el salario a double.
            if (salario <= 0) { // Valida que el salario sea mayor que cero.
                mostrarAdvertencia("El salario debe ser mayor que cero.");
                txtSalario.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) { // Captura si el salario no es un número válido.
            mostrarAdvertencia("El salario debe ser un número válido.");
            txtSalario.requestFocus();
            return false;
        }

        return true; // Todos los datos son válidos.
    }

    /**
     * Limpia todos los campos de entrada del formulario.
     */
    private void limpiarFormulario() {
        txtId.setText(""); // Limpia el campo ID.
        txtNombre.setText(""); // Limpia el campo Nombre.
        cmbCargo.setSelectedIndex(0); // Selecciona el primer ítem (generalmente nulo) en el ComboBox.
        txtFechaContratacion.setText(""); // Limpia el campo Fecha Contratación.
        txtSalario.setText(""); // Limpia el campo Salario.
    }

    /**
     * Habilita o deshabilita los campos de entrada y los botones de acción del formulario.
     * @param habilitar un booleano que indica si los componentes deben ser habilitados (true) o deshabilitados (false).
     */
    private void habilitarFormulario(boolean habilitar) {
        txtNombre.setEnabled(habilitar); // Habilita/deshabilita el campo Nombre.
        cmbCargo.setEnabled(habilitar); // Habilita/deshabilita el ComboBox Cargo.
        txtFechaContratacion.setEnabled(habilitar); // Habilita/deshabilita el campo Fecha Contratación.
        txtSalario.setEnabled(habilitar); // Habilita/deshabilita el campo Salario.
        btnGuardar.setEnabled(habilitar); // Habilita/deshabilita el botón Guardar.
        btnCancelar.setEnabled(habilitar); // Habilita/deshabilita el botón Cancelar.
    }

    // --- Métodos de Mensajes (Utilidades para mostrar JOptionPanes) ---

    /**
     * Muestra un cuadro de diálogo de mensaje de error.
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un cuadro de diálogo de mensaje de advertencia.
     * @param mensaje El mensaje de advertencia a mostrar.
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra un cuadro de diálogo de mensaje de información.
     * @param mensaje El mensaje de información a mostrar.
     */
    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método principal (main) para ejecutar la aplicación de gestión de empleados.
     * Crea una instancia de EmpleadoView y la hace visible en el Event Dispatch Thread (EDT).
     * @param args Argumentos de la línea de comandos (no se usan en este caso).
     */
    public static void main(String[] args) {
        // Asegura que la creación y actualización de la UI se realicen en el Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            new EmpleadoView().setVisible(true); // Crea y muestra la ventana EmpleadoView.
        });
    }
}
