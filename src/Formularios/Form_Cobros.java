/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;


import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.AlphaNumericDocumentFilter;
import Filtros.CustomNumericDocumentFilter;
import Filtros.DecimalDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
import Modelo.ModeloTabla;
import Modelo.CobroDetalle;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.cargaComboBox;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;


/**
 *
 * @author Usuario
 */
public class Form_Cobros extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario  {
// Variables para manejar los datos y la lógica de cobros
private Map<String, String> myData = new HashMap<>();// Datos específicos del cobro principal
private HashMap<String, String> myDet; // Detalles específicos del cobro
private DBTableController tcCobros; // Controlador para la tabla COBROS
private DBTableController tcCobrosDetalle; // Controlador para la tabla COBROS_DETALLE
private ArrayList<CobroDetalle> listaDetalles; // Lista de detalles de cobro
private JComboBox<String> jcbMoneda = new JComboBox<>();
private ModeloTabla modelo; // Modelo para gestionar los datos en la tabla visual de detalles
private int filasTabla; // Cantidad de filas en la tabla de detalles
private int columnasTabla; // Cantidad de columnas en la tabla de detalles
public static int filaSeleccionada; // Fila seleccionada actualmente en la tabla de detalles
private ArrayList<Map<String, String>> columnData, colDat; // Datos de la tabla para renderizado y edición

// Modelos para manejar la lógica de cobros y detalles
private DBTableModel tMCobros; // Modelo para manejar la lógica de datos de la tabla de cabecera de cobros
private Map<String, String> mapCobros; // Mapeo de datos clave-valor de cobros
private DBTableModel tmCobrosDetalle; // Modelo para manejar la lógica de datos de la tabla de detalles de cobros
private Map<String, String> mapCobrosDetalle; // Mapeo de datos clave-valor de detalles de cobros

// Controladores auxiliares
private boolean isSavingOrUpdating = false; // Indica si se está guardando o actualizando datos
private boolean isUpdating = false; // Indica si se está en modo de actualización
private List<Object[]> cuentasCobrarSeleccionadas = new ArrayList<>(); // Lista de cuentas por cobrar seleccionadas para el cobro
// Modelos para manejar la lógica de CUENTASCOBRAR y VENTAS
private DBTableModel tmCuentasCobrar; // Modelo para manejar la lógica de datos de la tabla CUENTASCOBRAR
private Map<String, String> mapCuentasCobrar; // Mapeo de datos clave-valor de CUENTASCOBRAR

private boolean isHandlingSelection = false;
private DBTableController tcClientes;
private DBTableModel tmClientes; 
private Map<String, String> mapClientes;

private DBTableModel tmVentas; // Modelo para manejar la lógica de datos de la tabla VENTAS
private Map<String, String> mapVentas; // Mapeo de datos clave-valor de VENTAS

private DBTableModel tmMonedas; // Modelo para manejar la tabla de MONEDAS

private Component lastFocusedComponent;
private boolean isProcessingSelection = false;

public Form_Cobros() {
    System.out.println("Inicio del constructor Form_Cobros");
    try {
        initComponents(); // Initialize GUI components
        System.out.println("Componentes inicializados");
        // Initialize controllers and models
        inicializarControladoresYModelos();
        inicializarVariables(); // Inicializa variables y objetos como `myData`
        // Configure table model and UI
        configurarModeloTabla();
        construirTabla();
        myData = new HashMap<>(); // Inicialización explícita si no está declarada arriba
        // Initialize text fields with filters and validations

        // Load data into combo boxes and default values
        cargarDatosComboBoxMoneda();
        cargarUltimoId();
        resetCampos();
        System.out.println("Formulario inicializado correctamente");
        // Validate initial configurations
        if (!validarTalonario()) {
            mostrarMensajeError("El talonario no está configurado correctamente.");
        }
    } catch (Exception e) {
        mostrarMensajeError("Error al inicializar el formulario de cobros: " + e.getMessage());
        e.printStackTrace();
    }
}

private void inicializarVariables() {
    myData = new HashMap<>();
    jcbMoneda = new JComboBox<>();
    columnData = new ArrayList<>(); // Inicializa la lista
}
private void inicializarControladoresYModelos() {
    try {
        tcCobros = new DBTableController();
        tcCobrosDetalle = new DBTableController();
        tcCobros.iniciar("COBROS");
        tcCobrosDetalle.iniciar("COBROS_DETALLE");

        tmCuentasCobrar = new DBTableModel();
        tmCuentasCobrar.iniciar("CUENTAS_COBRAR");
        mapCuentasCobrar = new HashMap<>();

        tmVentas = new DBTableModel();
        tmVentas.iniciar("VENTAS");
        mapVentas = new HashMap<>();

        tMCobros = new DBTableModel();
        tMCobros.iniciar("COBROS");

        tmCobrosDetalle = new DBTableModel();
        tmCobrosDetalle.iniciar("COBROS_DETALLE");
        // Inicializa el modelo y asocia con la tabla "MONEDAS"
        tmMonedas = new DBTableModel();
        tmMonedas.iniciar("MONEDAS");
        tcClientes = new DBTableController();
        tcClientes.iniciar("clientes"); 

        tmClientes = new DBTableModel();
        tmClientes.iniciar("clientes");
        mapCobros = new HashMap<>();
        mapCobrosDetalle = new HashMap<>();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al inicializar controladores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}



private void mostrarMensajeError(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
}

private void cargarClientes(String clienteId) {
    try {
        // Define the query parameters
        Map<String, String> where = new HashMap<>();
        where.put("id", clienteId);

        // Specify the columns to retrieve
        Map<String, String> fields = Map.of(
            "id", "id",
            "nombre", "nombre",
            "apellido", "apellido",
            "tipodocumento", "tipodocumento",
            "nrodocumento", "nrodocumento"
        );

        // Fetch client data from the database
        List<Map<String, String>> clienteData = tmCuentasCobrar.readRegisterList(fields, where);

        // Check if data is found
        if (clienteData.isEmpty()) {
            mostrarMensaje("Cliente no encontrado. Verifique el ID ingresado.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Extract the first client record
        Map<String, String> cliente = clienteData.get(0);

        // Populate form fields with client data
        Cliente.setText(cliente.get("id"));
        txtNombreCliente.setText(cliente.get("nombre") + " " + cliente.get("apellido"));

        // Optionally display additional client details
        System.out.println("Cliente cargado: " + cliente);

    } catch (Exception e) {
        mostrarMensajeError("Error al cargar los datos del cliente: " + e.getMessage());
        e.printStackTrace();
    }
}
private void mostrarMensaje(String mensaje, int messageType) {
    JOptionPane.showMessageDialog(this, mensaje, "Información", messageType);
}
private void cargarDatosComboBoxMoneda() {
    try {
        // Inicializa el JComboBox si no está hecho
        if (jcbMoneda == null) {
            jcbMoneda = new JComboBox<>();
        }

        // Limpia los elementos existentes en el JComboBox
        jcbMoneda.removeAllItems();
        jcbMoneda.addItem("Seleccione una moneda");

        // Define las columnas que deseas recuperar
        Map<String, String> campos = Map.of(
            "id", "id",
            "moneda", "moneda"
        );

        // Realiza la consulta para obtener los datos de las monedas
        List<Map<String, String>> monedas = tmMonedas.readRegisterList(campos, new HashMap<>());

        // Verifica si se encontraron datos
        if (monedas == null || monedas.isEmpty()) {
            System.out.println("No se encontraron monedas en la base de datos.");
            return;
        }

        // Itera sobre los resultados y agrega al JComboBox
        for (Map<String, String> moneda : monedas) {
            String displayText = String.format("%s - %s", moneda.get("id"), moneda.get("moneda"));
            jcbMoneda.addItem(displayText);
        }

        System.out.println("Monedas cargadas correctamente en el JComboBox.");

    } catch (Exception e) {
        System.err.println("Error al cargar datos en el JComboBox de monedas: " + e.getMessage());
        e.printStackTrace();
    }
}





private void cargarNroRecibo() {
    try {
        // Retrieve the maximum receipt number from the database
        int ultimoNroRecibo = tcCobros.getMaxId();

        // Increment by 1 to determine the next receipt number
        int nuevoNroRecibo = ultimoNroRecibo + 1;

        // Populate the text field with the new receipt number
        txtId.setText(String.valueOf(nuevoNroRecibo));

        // Log the new receipt number for debugging purposes
        System.out.println("Nuevo número de recibo generado: " + nuevoNroRecibo);
    } catch (Exception e) {
        // Handle any errors during data retrieval or processing
        mostrarMensajeError("Error al cargar el número de recibo: " + e.getMessage());

        // Default to 1 if an error occurs
        txtId.setText("1");
    }
}

private void cargarUltimoId() {
    try {
        // Retrieve the maximum ID from the database
        int ultimoId = tcCobros.getMaxId();

        // If no records exist, set the ID to 0
        if (ultimoId <= 0) {
            ultimoId = 0;
        }

        // Populate the text field with the next ID
        txtId.setText(String.valueOf(ultimoId + 1));

        // Log the loaded ID for debugging purposes
        System.out.println("Último ID cargado: " + ultimoId);
    } catch (Exception e) {
        // Handle any errors during data retrieval
        mostrarMensajeError("Error al cargar el último ID: " + e.getMessage());

        // Default to 1 if an error occurs
        txtId.setText("1");
    }
}
private boolean validarCabecera() {
    try {
        // Validate ID field
        String idCobro = txtId.getText().trim();
        if (idCobro.isEmpty() || Integer.parseInt(idCobro) <= 0) {
            mostrarMensaje("El ID del cobro no puede estar vacío o ser menor o igual a 0.", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate Cliente field
        if (Cliente.getText().trim().isEmpty()) {
            mostrarMensaje("Debe seleccionar un cliente válido.", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate Fecha Cobro field
        if (txtFechaCobro.getDate() == null) {
            mostrarMensaje("Debe seleccionar una fecha válida para el cobro.", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Additional validations (optional)
        // Example: Check if the client exists in the database
        if (!validarCliente(Cliente.getText().trim())) {
            mostrarMensaje("El cliente especificado no existe en la base de datos.", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // All validations passed
        return true;
    } catch (NumberFormatException e) {
        mostrarMensaje("El ID del cobro debe ser un número válido.", JOptionPane.WARNING_MESSAGE);
        return false;
    } catch (Exception e) {
        mostrarMensajeError("Error al validar la cabecera: " + e.getMessage());
        return false;
    }
}
private boolean validarCliente(String clienteId) {
    // Verify if the client exists in the database
    Map<String, String> where = new HashMap<>();
    where.put("id", clienteId);

    Map<String, String> fields = Map.of(
        "id", "id"
    );

    List<Map<String, String>> clienteData = tmClientes.readRegisterList(fields, where);

    return !clienteData.isEmpty(); // Return true if client exists
}
private boolean validarTalonario() {
    try {
        // Retrieve the talonario ID from the relevant field
        String talonarioId = txtNombreCliente.getText().trim();

        // Validate if the talonario ID field is empty
        if (talonarioId.isEmpty()) {
            mostrarMensaje("Debe seleccionar un talonario válido.", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Ensure the ID is a valid number
        int talonarioIdNumeric = Integer.parseInt(talonarioId);

        // Query database for the talonario
        List<Map<String, String>> resultados = tcCobros.buscarPorIdGenerico("talonarios", "id", talonarioIdNumeric);

        // Validate if the talonario exists
        if (resultados.isEmpty()) {
            mostrarMensaje("El talonario no existe.", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check the type of the talonario
        String tipoTalonario = resultados.get(0).get("tipo");
        if (!"COBROS".equalsIgnoreCase(tipoTalonario)) {
            mostrarMensaje("El talonario seleccionado no es válido para cobros.", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Talonario is valid
        return true;
    } catch (NumberFormatException e) {
        // Handle invalid ID formats
        mostrarMensaje("El ID del talonario debe ser un número válido.", JOptionPane.ERROR_MESSAGE);
        return false;
    } catch (Exception e) {
        // Handle other errors
        mostrarMensajeError("Error al validar el talonario: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
private void configurarModeloTabla() {
    String[] columnNames = {"Nro Factura", "Cuota", "Vence", "Importe"};
    DefaultTableModel modeloTabla = new DefaultTableModel(new Object[][]{}, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // Solo la columna "Nro Factura" es editable
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 3: return BigDecimal.class; // Importe
                default: return String.class;
            }
        }
    };

    tablaDetalles.setModel(modeloTabla);
    cargarFacturasEnComboBox(Integer.parseInt(Cliente.getText())); // Cargar facturas rela
    tablaDetalles.setRowHeight(25);
}

private void construirTabla() {
    // Define los encabezados de columna
    String[] columnNames = {"Nro Factura", "Cuota", "Vence", "Importe", "Pagado"};

    // Crear el modelo de tabla
    DefaultTableModel modeloTabla = new DefaultTableModel(new Object[][]{}, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 4; // Solo "Nro Factura" y "Pagado" son editables
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 3: return BigDecimal.class; // Importe
                case 4: return BigDecimal.class; // Pagado
                default: return String.class;
            }
        }
    };

    // Configurar el modelo en la tabla
    tablaDetalles.setModel(modeloTabla);
    tablaDetalles.setRowHeight(25); // Altura de las filas
    tablaDetalles.setGridColor(new java.awt.Color(200, 200, 200));
    tablaDetalles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tablaDetalles.getTableHeader().setReorderingAllowed(false);

    // Ajustar el ancho de las columnas
    tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(100);
    tablaDetalles.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaDetalles.getColumnModel().getColumn(2).setPreferredWidth(100);
    tablaDetalles.getColumnModel().getColumn(3).setPreferredWidth(100);
    tablaDetalles.getColumnModel().getColumn(4).setPreferredWidth(100);

    // Configurar editores personalizados
    configurarEditorNroFactura();
    configurarEditorPagado();
    System.out.println("Tabla configurada correctamente con columna 'Pagado'.");
    configurarEditorImporte();
}
private void configurarEditorPagado() {
    JTextField textField = new JTextField();

    tablaDetalles.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Obtener el número de decimales según la moneda
            String nroFactura = (String) table.getValueAt(row, 0); // Obtén el número de factura
            String ventaId = getVentaIdFromNroFactura(nroFactura); // Convertir a ID de la venta
            int decimales = ventaId != null ? getDecimalPlacesFromVentaAndMoneda(ventaId) : 2;

            // Configurar filtro de decimales en el editor
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimales));

            // Establecer el valor actual en el editor
            textField.setText(value != null ? formatDecimal(new BigDecimal(value.toString()), decimales) : "");
            return textField;
        }

        @Override
        public boolean stopCellEditing() {
            String valor = textField.getText().trim();
            if (valor.isEmpty()) {
                valor = "0"; // Si está vacío, asignar un valor predeterminado
            }

            try {
                BigDecimal decimalValue = new BigDecimal(valor);
                textField.setText(formatDecimal(decimalValue, getDecimalPlacesFromVentaAndMoneda(getVentaIdFromNroFactura((String) tablaDetalles.getValueAt(tablaDetalles.getSelectedRow(), 0)))));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return super.stopCellEditing();
        }
    });
}

/**
 * Formatea un valor BigDecimal a un número de decimales específico.
 */
private String formatDecimal(BigDecimal value, int decimales) {
    return value.setScale(decimales, RoundingMode.HALF_UP).toPlainString();
}





/**
 * Recupera el número de decimales desde la tabla monedas para una venta específica.
 */
private int getDecimalPlacesFromVentaAndMoneda(String ventaId) {
    try {
        Map<String, String> condicionesVenta = Map.of("id", ventaId);
        List<Map<String, String>> ventas = tmVentas.readRegisterList(Map.of("moneda_id", "moneda_id"), condicionesVenta);

        if (ventas.isEmpty()) {
            return 2; // Valor predeterminado
        }

        String monedaId = ventas.get(0).get("moneda_id");
        Map<String, String> condicionesMoneda = Map.of("id", monedaId);
        List<Map<String, String>> monedas = tmMonedas.readRegisterList(Map.of("decimales", "decimales"), condicionesMoneda);

        return monedas.isEmpty() ? 2 : Integer.parseInt(monedas.get(0).get("decimales"));
    } catch (Exception e) {
        e.printStackTrace();
        return 2; // Predeterminado en caso de error
    }
}

private String getVentaIdFromNroFactura(String nroFactura) {
    if (nroFactura == null || nroFactura.trim().isEmpty()) {
        return null;
    }

    try {
        String[] partes = nroFactura.split("-");
        String serie = partes[0];
        String nroDocumento = partes[1];

        Map<String, String> condiciones = Map.of("serie", serie, "nro_documento", nroDocumento);
        List<Map<String, String>> ventas = tmVentas.readRegisterList(Map.of("id", "id"), condiciones);

        return ventas.isEmpty() ? null : ventas.get(0).get("id");
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


private int obtenerDecimalesPorVenta(String ventaId) {
    try {
        // Consultar la moneda asociada a la venta
        Map<String, String> condiciones = Map.of("id", ventaId);
        Map<String, String> columnas = Map.of("moneda", "moneda");
        List<Map<String, String>> resultados = tmVentas.readRegisterList(columnas, condiciones);

        if (!resultados.isEmpty()) {
            String moneda = resultados.get(0).get("moneda");
            return tcCobros.getDecimalPlacesForCurrency(moneda); // Obtener decimales por moneda
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return 2; // Valor predeterminado en caso de error
}

private void validarDatosTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();

    for (int i = 0; i < modelo.getRowCount(); i++) {
        Object pagadoObj = modelo.getValueAt(i, 4); // Columna "Pagado"
        Object ventaIdObj = modelo.getValueAt(i, 0); // Nro Factura

        if (pagadoObj == null || ventaIdObj == null) {
            throw new IllegalArgumentException("Faltan datos en la fila " + (i + 1) + ".");
        }

        try {
            BigDecimal pagado = new BigDecimal(pagadoObj.toString());
            int decimales = obtenerDecimalesPorVenta(ventaIdObj.toString());
            if (pagado.scale() > decimales) {
                throw new IllegalArgumentException("El valor en 'Pagado' excede los decimales permitidos en la fila " + (i + 1) + ".");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor en 'Pagado' no es válido en la fila " + (i + 1) + ".");
        }
    }

    System.out.println("Validación de datos completada.");
}



private void setImporteColumnEditor() {
    // Retrieve the allowed number of decimal places for the current currency
    int decimalPlaces = getDecimalPlaces();

    // Create a JTextField to serve as the editor for the "Importe" column
    JTextField textField = new JTextField();

    // Apply a custom document filter to restrict input to numeric values with the allowed decimals
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));

    // Add an input verifier to validate input when the user finishes editing
    textField.setInputVerifier(new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            try {
                // Attempt to parse the input as a BigDecimal
                new BigDecimal(tf.getText());
                return true;
            } catch (NumberFormatException e) {
                // Display an error message if the input is invalid
                JOptionPane.showMessageDialog(
                        null,
                        "El importe debe ser un valor numérico válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
    });

    // Configure the table column to use the JTextField as the editor
    tablaDetalles.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Set the current cell value in the editor
            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setText(value != null ? value.toString() : ""); // Handle null values
            editor.selectAll(); // Select all text for quick editing
            return editor;
        }

        @Override
        public boolean stopCellEditing() {
            String value = ((JTextField) getComponent()).getText();
            if (value.isEmpty()) {
                // Set default value to "0" if the cell is left empty
                value = "0";
                ((JTextField) getComponent()).setText(value);
            }
            return super.stopCellEditing();
        }
    });

    // Debugging: Log the decimal place configuration
    System.out.println("Editor configurado para la columna 'Importe' con " + decimalPlaces + " decimales.");
}

private void limpiarTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
    modelo.setRowCount(0); // Eliminar todas las filas
}

private void resetCampos() {
    txtId.setText("0");
    Cliente.setText("");
    txtNombreCliente.setText("");
    txtFechaCobro.setDate(new java.util.Date());
    limpiarTabla(); // Limpiar la tabla al reiniciar
    System.out.println("Campos reiniciados correctamente.");
}
private void resetearPagado() {
    DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
    int rowCount = modelo.getRowCount();
    
    // Recorrer todas las filas y reiniciar la columna "Pagado" (asumiendo que está en el índice 4)
    for (int i = 0; i < rowCount; i++) {
        modelo.setValueAt(null, i, 4); // Cambia 4 al índice real de la columna "Pagado"
    }
    
    System.out.println("Columna 'Pagado' reiniciada.");
}



private void actualizarDecimales() {
    // Get the number of decimal places allowed for the current currency
    int decimalPlaces = getDecimalPlaces();

    // Retrieve the table model
    DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();

    // Iterate through all rows in the table
    for (int i = 0; i < model.getRowCount(); i++) {
        Object importeObj = model.getValueAt(i, 4); // Column index for "Importe"

        if (importeObj != null) {
            String value = importeObj.toString().trim();
            try {
                // Parse the value as BigDecimal and adjust to the correct decimal places
                BigDecimal importeValor = new BigDecimal(value.isEmpty() ? "0" : value);
                BigDecimal scaledValue = importeValor.setScale(decimalPlaces, RoundingMode.HALF_UP);

                // Update the table with the adjusted value
                model.setValueAt(scaledValue.toString(), i, 4);
            } catch (NumberFormatException ex) {
                // Handle errors if the value cannot be parsed as a number
                System.err.println("Error al ajustar decimales para el importe en la fila " + i + ": " + ex.getMessage());
                model.setValueAt("0.00", i, 4); // Set default value if parsing fails
            }
        }
    }

    // Debugging: Log the update action
    System.out.println("Actualización de decimales completada con " + decimalPlaces + " lugares decimales.");
}

private int getDecimalPlaces() {
    // Check if a currency is selected in the JComboBox
    if (jcbMoneda.getSelectedItem() == null) {
        return 2; // Default to 2 decimal places if no currency is selected
    }

    // Extract the currency ID from the selected item
    String selectedCurrencyId = Functions.ExtraeCodigo(jcbMoneda.getSelectedItem().toString());

    try {
        // Retrieve the number of decimal places for the selected currency
        return tcCobros.getDecimalPlacesForCurrency(selectedCurrencyId);
    } catch (Exception e) {
        // Handle any errors and return the default value
        JOptionPane.showMessageDialog(
            this,
            "Error al obtener los decimales para la moneda seleccionada: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
        return 2; // Default value in case of an error
    }
}

private void setData() {
    // Prepare header data
    myData.put("id", txtId.getText().trim());
    myData.put("cliente_id", Cliente.getText().trim());
    myData.put("fecha_cobro", new SimpleDateFormat("yyyy-MM-dd").format(txtFechaCobro.getDate()));
    myData.put("anulado", jCheckBoxAnulado.isSelected() ? "1" : "0");
    myData.put("impreso", jCheckBoxAnulado1.isSelected() ? "1" : "0");

    // Retrieve the table model
    DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();
    int rowCount = model.getRowCount();
    columnData.clear(); // Clear any existing detail data

    // Process each row in the table
    for (int i = 0; i < rowCount; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Collect data for each column
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object value = model.getValueAt(i, j);
            rowData.put(model.getColumnName(j), value != null ? value.toString().trim() : ""); // Handle null values
        }

        // Validate the row before adding it to the details
        if (rowData.get("Nro Factura").isEmpty() || rowData.get("Nro Factura").equals("0")) {
            continue; // Skip rows without a valid "Nro Factura"
        }

        // Add additional data (like Cobro ID) for relational purposes
        rowData.put("cobro_id", txtId.getText().trim());

        // Assign a new ID to the row if it's missing
        if (!rowData.containsKey("id") || rowData.get("id").isEmpty()) {
            int newId = obtenerUltimoIdCobroDetalle() + 1;
            rowData.put("id", String.valueOf(newId));
        }

        // Add the row data to the detail list
        columnData.add(rowData);
    }

    // Debugging: Log the captured data
    System.out.println("Datos de la cabecera:");
    System.out.println(myData);

    System.out.println("Datos de los detalles:");
    for (Map<String, String> detail : columnData) {
        System.out.println(detail);
    }
}
private int obtenerUltimoIdCobroDetalle() {
    try {
        int ultimoId = tcCobrosDetalle.getMaxId(); // Usa el método en tu controlador
        return Math.max(ultimoId, 0); // Si `ultimoId` es negativo, devuelve 0
    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Error al obtener el último ID de cobro detalle: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
        return 0; // En caso de error, retorna 0
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblFechaOperacion = new javax.swing.JLabel();
        lblCliente = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jCheckBoxAnulado = new javax.swing.JCheckBox();
        txtFechaCobro = new com.toedter.calendar.JDateChooser();
        lblId1 = new javax.swing.JLabel();
        Cliente = new javax.swing.JTextField();
        jCheckBoxAnulado1 = new javax.swing.JCheckBox();
        txtNombreCliente = new javax.swing.JTextField();
        Ultimo = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Cobros");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeactivated(evt);
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        lblId.setText("Id");

        txtId.setText("0");
        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdFocusGained(evt);
            }
        });
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        lblFechaOperacion.setText("F. Operacion");

        lblCliente.setText("Cliente");

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nro Factura", "Cuota", "Vence", "Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDetalles.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tablaDetallesFocusGained(evt);
            }
        });
        tablaDetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaDetallesMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDetalles);

        jCheckBoxAnulado.setText("Anulado");
        jCheckBoxAnulado.setFocusable(false);
        jCheckBoxAnulado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckBoxAnuladoMousePressed(evt);
            }
        });

        lblId1.setText("Último");

        Cliente.setText("0");
        Cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ClienteFocusGained(evt);
            }
        });
        Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClienteActionPerformed(evt);
            }
        });

        jCheckBoxAnulado1.setText("Impreso");
        jCheckBoxAnulado1.setToolTipText("");
        jCheckBoxAnulado1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBoxAnulado1.setEnabled(false);
        jCheckBoxAnulado1.setFocusable(false);
        jCheckBoxAnulado1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCheckBoxAnulado1MousePressed(evt);
            }
        });

        txtNombreCliente.setBackground(new java.awt.Color(204, 204, 255));
        txtNombreCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNombreCliente.setOpaque(true);
        txtNombreCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreClienteFocusGained(evt);
            }
        });
        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setText("       20");
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxAnulado1)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxAnulado))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCliente, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblId, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblId1)
                                        .addGap(8, 8, 8)
                                        .addComponent(Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblFechaOperacion)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtFechaCobro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNombreCliente))))
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblId1)
                        .addComponent(lblFechaOperacion)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblId))
                    .addComponent(txtFechaCobro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCliente)
                    .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxAnulado1)
                    .addComponent(jCheckBoxAnulado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
       
    }//GEN-LAST:event_txtIdActionPerformed

    private void txtIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdFocusGained
      lastFocusedComponent = txtId; // Registra el componente activo
    }//GEN-LAST:event_txtIdFocusGained

    private void tablaDetallesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDetallesMousePressed
    
          
    }//GEN-LAST:event_tablaDetallesMousePressed

    private void dateFechaOperacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFechaOperacionActionPerformed
      
    }//GEN-LAST:event_dateFechaOperacionActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
       
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void tablaDetallesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tablaDetallesFocusGained
lastFocusedComponent = tablaDetalles; // Registra el componente activo
    }//GEN-LAST:event_tablaDetallesFocusGained

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed

    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
      
    }//GEN-LAST:event_formInternalFrameIconified

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
     
    }//GEN-LAST:event_formInternalFrameDeactivated

    private void jCheckBoxAnuladoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxAnuladoMousePressed
       
    }//GEN-LAST:event_jCheckBoxAnuladoMousePressed

    private void jCheckBoxAnulado1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxAnulado1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxAnulado1MousePressed

    private void txtNombreClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteFocusGained

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClienteActionPerformed

    private void ClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ClienteFocusGained
    lastFocusedComponent = Cliente; // Registrar el componente activo
    System.out.println("Cliente enfocado.");
    }//GEN-LAST:event_ClienteFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Cliente;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JCheckBox jCheckBoxAnulado;
    private javax.swing.JCheckBox jCheckBoxAnulado1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblFechaOperacion;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JTable tablaDetalles;
    private com.toedter.calendar.JDateChooser txtFechaCobro;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombreCliente;
    // End of variables declaration//GEN-END:variables

    

@Override
public void mouseClicked(MouseEvent e) {
    try {
        // Obtener la fila y columna donde ocurrió el clic
        int fila = tablaDetalles.rowAtPoint(e.getPoint());
        int columna = tablaDetalles.columnAtPoint(e.getPoint());

        // Verificar si el clic fue sobre una fila válida
        if (fila >= 0) {
            // Seleccionar la fila clicada
            tablaDetalles.setRowSelectionInterval(fila, fila);

            // Si la columna corresponde a "Importe" o "Moneda", activar la edición
            if (columna == 4 || columna == 1) { // Ajustar índices según las columnas de tu tabla
                tablaDetalles.editCellAt(fila, columna);
                Component editor = tablaDetalles.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus(); // Poner el foco en el componente editor
                }
            }
        } else {
            // Si el clic no fue sobre una fila, limpiar la selección de la tabla
            tablaDetalles.clearSelection();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
            this,
            "Error al procesar el clic en la tabla: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

@Override
public void mousePressed(MouseEvent e) {
    int row = tablaDetalles.rowAtPoint(e.getPoint());
    if (row < 0) {
        tablaDetalles.clearSelection(); // Limpia la selección si la fila es inválida
        return;
    }

    tablaDetalles.setRowSelectionInterval(row, row); // Establece la fila seleccionada
}

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
@Override
public void keyPressed(KeyEvent e) {
   
}



private boolean isRowInvalid(Map<String, String> row) {
    for (Map.Entry<String, String> entry : row.entrySet()) {
        String columna = entry.getKey();
        String valor = entry.getValue();

        // Excepciones para ciertas columnas que pueden ser opcionales
        if ("Descuento".equalsIgnoreCase(columna)) {
            continue; // Saltar validación para esta columna
        }

        // Verificar si el valor es nulo, vacío o igual a "0"
        if (valor == null || valor.trim().isEmpty() || "0".equals(valor)) {
            return true; // La fila es inválida si se encuentra un valor no válido
        }
    }
    return false; // Si todos los valores son válidos, la fila es válida
}

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imGuardar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imBorrar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imBuscar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

private void cargarDatosCliente(String clienteId) {
    try {
        // Validar entrada
        if (clienteId == null || clienteId.trim().isEmpty()) {
            mostrarMensajeError("El ID del cliente no puede estar vacío.");
            return;
        }

        // Definir las condiciones para buscar en la base de datos
        Map<String, String> condiciones = Map.of("id", clienteId);
        Map<String, String> campos = new HashMap<>();
        campos.put("id", "id");
        campos.put("nrodocumento", "nrodocumento");
        campos.put("tipodocumento", "tipodocumento");
        campos.put("cliente", "cliente");
        campos.put("apellido", "apellido");
        campos.put("celular", "celular");
        campos.put("direccion", "direccion");
        campos.put("correo", "correo");
        campos.put("ciudad_id", "ciudad_id");
        campos.put("precio_id", "precio_id");
        campos.put("fecha", "fecha");
        campos.put("activo", "activo");


        // Consultar la información del cliente
        List<Map<String, String>> resultados = tmClientes.readRegisterList(campos, condiciones);

        if (resultados.isEmpty()) {
            mostrarMensajeError("No se encontró información para el cliente con ID: " + clienteId);
            return;
        }

        // Recuperar el primer resultado
        Map<String, String> cliente = resultados.get(0);

        // Actualizar los campos del formulario
        txtNombreCliente.setText(cliente.get("cliente") + " " + cliente.get("apellido"));
        Cliente.setText(cliente.get("id"));

        // Opcional: Mostrar más información en consola para depuración
        System.out.println("Datos completos del cliente cargados: " + cliente);

    } catch (Exception e) {
        mostrarMensajeError("Error al cargar información del cliente: " + e.getMessage());
        e.printStackTrace();
    }
}


private void cargarDetallesDeCobroDesdeVenta(String ventaId) {
    try {
        // Verificar si hay un cliente seleccionado
        String clienteId = Cliente.getText().trim();
        if (clienteId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente antes de cargar los detalles de la venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Definir las condiciones para buscar las cuotas asociadas a la venta y cliente
        Map<String, String> condiciones = Map.of(
            "venta_id", ventaId,
            "cliente_id", clienteId
        );

        // Definir los campos que se desean obtener de las cuentas por cobrar
        Map<String, String> campos = Map.of(
            "id", "id",
            "cuota", "cuota",
            "fecha_vencimiento", "fecha_vencimiento",
            "importe", "importe",
            "moneda", "moneda"
        );

        // Consultar los detalles de cobro (cuotas) relacionados con la venta seleccionada
        List<Map<String, String>> detallesCuentas = tmCuentasCobrar.readRegisterList(campos, condiciones);

        if (detallesCuentas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron detalles de cobro asociados a la venta seleccionada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtener el modelo de la tabla para actualizar los datos
        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();

        // Agregar cada detalle de cobro a la tabla visual
        for (Map<String, String> detalle : detallesCuentas) {
            modelo.addRow(new Object[]{
                ventaId, // Número de factura o venta
                detalle.get("moneda"), // Moneda de la cuota
                detalle.get("cuota"), // Número de cuota
                detalle.get("fecha_vencimiento"), // Fecha de vencimiento de la cuota
                detalle.get("importe") // Importe de la cuota
            });
        }

        // Mostrar mensaje de éxito al cargar los detalles
        JOptionPane.showMessageDialog(this, "Detalles de la venta cargados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        // Manejar errores y mostrar mensajes al usuario
        JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}




@Override
public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    
    // Limpiar la tabla y restablecer campos
    limpiarTabla();
    resetCampos();

    if (lastFocusedComponent == Cliente) {
        // Configurar columnas para el buscador
        List<String> columnasParaClientes = Arrays.asList("id", "cliente");
        Form_Buscar buscadorClientes = new Form_Buscar(parentFrame, true, tcClientes, "clientes", columnasParaClientes);

        buscadorClientes.setOnItemSeleccionadoListener(this);
        buscadorClientes.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un campo válido para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return 0;
}

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    if (lastFocusedComponent == Cliente) {
        String idStr = datosSeleccionados.get("Codigo"); // ID del cliente
        String nombre = datosSeleccionados.get("Descripcion"); // Nombre del cliente

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idCliente = Integer.parseInt(idStr);

                // Verificar si el cliente tiene ventas
                if (clienteTieneVentas(idCliente)) {
                    // Verificar si las ventas tienen cuentas por cobrar
                    if (ventasTienenCuentasCobrar(idCliente)) {
                        // Recuperar datos del cliente
                        Map<String, String> clienteData = buscarClientePorId(idCliente);
                        if (clienteData != null) {
                            // Rellenar datos del cliente en el formulario
                            String apellido = clienteData.get("apellido");
                            Cliente.setText(idStr);
                            txtNombreCliente.setText(nombre + " " + apellido);

                            // Llamar al método para cargar las facturas en el JComboBox
                            cargarFacturasEnComboBox(idCliente);

                            // Agregar fila vacía en la tabla
                            agregarFilaVaciaEnTabla();
                                
                            // Log para depuración
                            System.out.println("Cliente cargado con éxito y fila agregada a la tabla.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No se encontraron datos para el cliente seleccionado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El cliente no tiene cuentas por cobrar asociadas a sus ventas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "El cliente no tiene ventas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del cliente debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID del cliente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



private void cargarFacturasEnComboBox(int clienteId) {
    try {
        Map<String, String> campos = Map.of(
            "serie", "serie",
            "nro_documento", "nro_documento",
            "id", "id"
        );
        Map<String, String> condiciones = Map.of("cliente_id", String.valueOf(clienteId));

        // Obtener las facturas del cliente
        List<Map<String, String>> facturas = tmVentas.readRegisterList(campos, condiciones);

        // Crear JComboBox
        JComboBox<String> comboBoxFacturas = new JComboBox<>();
        for (Map<String, String> factura : facturas) {
            String numeroFactura = factura.get("serie") + "-" + String.format("%07d", Integer.parseInt(factura.get("nro_documento")));
            comboBoxFacturas.addItem(numeroFactura);
            comboBoxFacturas.putClientProperty(numeroFactura, factura.get("id")); // Mapear número de factura al venta_id
        }

        // Configurar JComboBox como editor de la celda
        DefaultCellEditor comboBoxEditor = new DefaultCellEditor(comboBoxFacturas);
        tablaDetalles.getColumnModel().getColumn(0).setCellEditor(comboBoxEditor);

        // Manejar selección del JComboBox
        comboBoxFacturas.addActionListener(e -> {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String facturaSeleccionada = (String) source.getSelectedItem();
            String ventaId = (String) comboBoxFacturas.getClientProperty(facturaSeleccionada);

            if (ventaId != null) {
                cargarCuotasPorFactura(ventaId, facturaSeleccionada); // Pasar ventaId y número de factura
            }
        });

        System.out.println("Facturas cargadas en el JComboBox para el cliente ID: " + clienteId);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar facturas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}






private void cargarCuotasPorFactura(String ventaId, String numeroFactura) {
    try {
        // Obtener las cuotas relacionadas con la factura
        Map<String, String> condiciones = Map.of("venta_id", ventaId);
        List<Map<String, String>> cuotas = tmCuentasCobrar.readRegisterList(Map.of(
            "cuota", "cuota",
            "vencimiento", "fecha_vencimiento",
            "importe", "importe"
        ), condiciones);

        if (cuotas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron cuotas para esta factura.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtener los decimales desde la moneda de la venta
        int decimales = getDecimalPlacesFromVentaAndMoneda(ventaId);

        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
        modelo.setRowCount(0); // Limpiar filas anteriores

        // Agregar las cuotas a la tabla ajustando el formato del importe
        for (Map<String, String> cuota : cuotas) {
            BigDecimal importe = new BigDecimal(cuota.get("importe"));
            modelo.addRow(new Object[]{
                numeroFactura, // Número de factura
                cuota.get("cuota"), // Número de cuota
                cuota.get("vencimiento"), // Fecha de vencimiento
                importe.setScale(decimales, RoundingMode.HALF_UP) // Ajustar decimales
            });
        }

        System.out.println("Cuotas cargadas correctamente.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar cuotas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}








private Map<String, String> buscarClientePorId(int clienteId) {
    try {
        Map<String, String> where = new HashMap<>();
        where.put("id", String.valueOf(clienteId));

        // Especificar columnas a recuperar
        Map<String, String> fields = Map.of(
            "id", "id",
            "cliente", "cliente",
            "apellido", "apellido"
        );

        // Consultar base de datos
        List<Map<String, String>> result = tmClientes.readRegisterList(fields, where);
        return result.isEmpty() ? null : result.get(0); // Retorna el primer resultado o null
    } catch (Exception e) {
        e.printStackTrace();
        return null; // Si ocurre un error, retorna null
    }
}


private boolean clienteTieneVentas(int clienteId) {
    try {
        // Condiciones para buscar ventas del cliente
        Map<String, String> condiciones = Map.of("cliente_id", String.valueOf(clienteId));

        // Consultar si existen registros en la tabla de ventas
        List<Map<String, String>> ventas = tmVentas.readRegisterList(Map.of("id", "id"), condiciones);
        return !ventas.isEmpty(); // Retorna true si hay ventas
    } catch (Exception e) {
        e.printStackTrace();
        return false; // En caso de error, retorna false
    }
}
private boolean ventasTienenCuentasCobrar(int clienteId) {
    try {
        // Buscar todas las ventas del cliente
        Map<String, String> condicionesVentas = Map.of("cliente_id", String.valueOf(clienteId));
        List<Map<String, String>> ventas = tmVentas.readRegisterList(Map.of("id", "id"), condicionesVentas);

        if (ventas.isEmpty()) {
            return false; // Si no hay ventas, no hay cuentas por cobrar
        }

        // Verificar si al menos una venta tiene cuentas por cobrar
        for (Map<String, String> venta : ventas) {
            String ventaId = venta.get("id");

            // Consultar cuentas por cobrar asociadas a la venta
            Map<String, String> condicionesCuentas = Map.of("venta_id", ventaId);
            List<Map<String, String>> cuentasCobrar = tmCuentasCobrar.readRegisterList(Map.of("id", "id"), condicionesCuentas);

            if (!cuentasCobrar.isEmpty()) {
                return true; // Si encuentra una cuenta por cobrar, retorna true
            }
        }
        return false; // Si ninguna venta tiene cuentas por cobrar, retorna false
    } catch (Exception e) {
        e.printStackTrace();
        return false; // En caso de error, retorna false
    }
}
private void agregarFilaVaciaEnTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();

    // Verificar si ya existe una fila vacía al final
    int rowCount = modelo.getRowCount();
    if (rowCount > 0) {
        Object ultimaFila = modelo.getValueAt(rowCount - 1, 0);
        if (ultimaFila == null || ultimaFila.toString().trim().isEmpty()) {
            return; // No agregar si ya existe una fila vacía
        }
    }

    modelo.addRow(new Object[]{null, null, null, null, null}); // Incluye la columna "Pagado"
    System.out.println("Fila vacía agregada a la tabla.");
}





private void configurarEditorNroFactura() {
    JComboBox<String> comboBoxFacturas = new JComboBox<>();
    comboBoxFacturas.addItem("Seleccione una factura");

    // Agregar facturas al JComboBox
    List<Map<String, String>> facturas = obtenerFacturasPorCliente(Integer.parseInt(Cliente.getText()));
    for (Map<String, String> factura : facturas) {
        String nroFactura = factura.get("serie") + "-" + String.format("%07d", Integer.parseInt(factura.get("nro_documento")));
        comboBoxFacturas.addItem(nroFactura);
    }

    // Configurar el editor para la columna "Nro Factura"
    tablaDetalles.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBoxFacturas));

    // Listener para manejar cambios en el JComboBox
    comboBoxFacturas.addActionListener(e -> {
        String facturaSeleccionada = (String) comboBoxFacturas.getSelectedItem();
        if (facturaSeleccionada != null && !facturaSeleccionada.equals("Seleccione una factura")) {
            completarDatosDesdeFactura(facturaSeleccionada);
        }
    });

    System.out.println("Editor del JComboBox para 'Nro Factura' configurado correctamente.");
}


private void completarDatosDesdeFactura(String facturaSeleccionada) {
    try {
        // Obtener datos de la factura
        String[] partesFactura = facturaSeleccionada.split("-");
        String serie = partesFactura[0];
        String nroDocumento = partesFactura[1].replaceFirst("^0+(?!$)", ""); // Quitar ceros iniciales

        Map<String, String> condiciones = Map.of(
            "serie", serie,
            "nro_documento", nroDocumento
        );

        // Consultar la tabla de cuentas por cobrar
        List<Map<String, String>> cuotas = obtenerCuotasPorFactura(condiciones);

        if (cuotas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La factura seleccionada no tiene cuotas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Agregar filas a la tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
        modelo.setRowCount(0); // Limpiar filas existentes
        for (Map<String, String> cuota : cuotas) {
            modelo.addRow(new Object[]{
                facturaSeleccionada,
                cuota.get("cuota"),
                cuota.get("fecha_vencimiento"),
                cuota.get("importe")
            });
        }

        System.out.println("Cuotas cargadas en la tabla.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al completar datos de la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
private List<Map<String, String>> obtenerCuotasPorFactura(Map<String, String> condicionesFactura) {
    List<Map<String, String>> cuotas = new ArrayList<>();

    try {
        // Especificar los campos que queremos recuperar
        Map<String, String> campos = Map.of(
            "cuota", "cuota",
            "fecha_vencimiento", "fecha_vencimiento",
            "importe", "importe"
        );

        // Consultar la tabla CUENTAS_COBRAR usando las condiciones de la factura
        cuotas = tmCuentasCobrar.readRegisterList(campos, condicionesFactura);

        // Verificar si se encontraron cuotas
        if (cuotas.isEmpty()) {
            System.out.println("No se encontraron cuotas para la factura especificada.");
        } else {
            System.out.println("Cuotas encontradas: " + cuotas.size());
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Error al obtener cuotas para la factura: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
    }

    return cuotas;
}

private List<Map<String, String>> obtenerFacturasPorCliente(int clienteId) {
    try {
        // Definir condiciones para buscar facturas
        Map<String, String> condiciones = Map.of("cliente_id", String.valueOf(clienteId));

        // Especificar las columnas necesarias
        Map<String, String> columnas = Map.of(
            "id", "id",
            "serie", "serie",
            "nro_documento", "nro_documento"
        );

        // Consultar las facturas desde la tabla "ventas"
        return tmVentas.readRegisterList(columnas, condiciones);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener facturas del cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return new ArrayList<>();
    }
}
private void completarDatosDesdeFactura(String nroFactura, int clienteId) {
    try {
        // Dividir el NroFactura en serie y nro_documento
        String[] partes = nroFactura.split("-");
        if (partes.length != 2) {
            JOptionPane.showMessageDialog(this, "Formato de NroFactura inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String serie = partes[0];
        String nroDocumento = partes[1];

        // Buscar la venta asociada
        Map<String, String> condicionesVenta = Map.of(
            "serie", serie,
            "nro_documento", nroDocumento,
            "cliente_id", String.valueOf(clienteId)
        );

        Map<String, String> columnasVenta = Map.of(
            "id", "id",
            "fechaFactura", "fechaFactura"
        );

        List<Map<String, String>> ventas = tmVentas.readRegisterList(columnasVenta, condicionesVenta);

        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró la venta asociada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtener el ID de la venta
        String ventaId = ventas.get(0).get("id");

        // Verificar si la venta tiene cuentas por cobrar
        completarDesdeCuentasCobrar(ventaId);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al completar datos desde la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
private void completarDesdeCuentasCobrar(String ventaId) {
    try {
        // Buscar las cuentas por cobrar asociadas a la venta
        Map<String, String> condiciones = Map.of("venta_id", ventaId);

        Map<String, String> columnas = Map.of(
            "cuota", "cuota",
            "fecha_vencimiento", "fecha_vencimiento",
            "importe", "importe"
        );

        List<Map<String, String>> cuentas = tmCuentasCobrar.readRegisterList(columnas, condiciones);

        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La venta no tiene cuentas por cobrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Agregar las cuentas por cobrar a la tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
        for (Map<String, String> cuenta : cuentas) {
            modelo.addRow(new Object[]{
                "",  // NroFactura ya se seleccionó, no es necesario agregar
                cuenta.get("cuota"),
                cuenta.get("fecha_vencimiento"),
                cuenta.get("importe")
            });
        }

        System.out.println("Datos completados desde cuentas por cobrar.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al completar datos desde cuentas por cobrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

private void configurarColumnaImporte() {
    // Configurar renderizador para mostrar el importe con los decimales adecuados
    tablaDetalles.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
        JLabel label = new JLabel();
        label.setOpaque(true);

        // Aplica formato de decimales basado en la moneda
        if (value instanceof BigDecimal) {
            BigDecimal importe = (BigDecimal) value;
            int decimales = getDecimalPlaces();
            label.setText(formatDecimal(importe, decimales));
        } else if (value != null) {
            label.setText(value.toString());
        }

        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        } else {
            label.setBackground(table.getBackground());
            label.setForeground(table.getForeground());
        }

        return label;
    });

    // Configurar editor para restringir los decimales del importe
    JTextField textField = new JTextField();
    tablaDetalles.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int decimales = getDecimalPlaces();
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimales));
            textField.setText(value != null ? value.toString() : "");
            return textField;
        }

        @Override
        public boolean stopCellEditing() {
            String text = textField.getText().trim();
            try {
                BigDecimal decimalValue = new BigDecimal(text);
                int decimales = getDecimalPlaces();
                BigDecimal scaledValue = decimalValue.setScale(decimales, RoundingMode.HALF_UP);
                textField.setText(scaledValue.toPlainString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return super.stopCellEditing();
        }
    });
}
private void configurarEditorImporte() {
    JTextField textField = new JTextField();

    tablaDetalles.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public boolean stopCellEditing() {
            int row = tablaDetalles.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(
                    null,
                    "No se ha seleccionado ninguna fila válida.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return false; // Cancelar la edición si no hay una fila válida seleccionada
            }

            String value = textField.getText().trim();
            if (value.isEmpty()) {
                value = "0"; // Asignar valor predeterminado si está vacío
                textField.setText(value);
            }

            try {
                BigDecimal decimalValue = new BigDecimal(value);
                String ventaId = obtenerVentaIdDesdeTabla(row); // Usa el método para obtener `ventaId`
                int decimales = getDecimalPlacesFromVentaAndMoneda(ventaId);
                textField.setText(formatDecimal(decimalValue, decimales));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Ingrese un valor válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            return super.stopCellEditing();
        }
    });
}


private String obtenerVentaIdDesdeTabla(int row) {
    Object nroFactura = tablaDetalles.getValueAt(row, 0);
    if (nroFactura == null) {
        return null;
    }

    String numeroFactura = nroFactura.toString();
    String[] partesFactura = numeroFactura.split("-");
    if (partesFactura.length < 2) {
        return null;
    }

    String serie = partesFactura[0];
    String nroDocumento = partesFactura[1].replaceFirst("^0+(?!$)", ""); // Quita ceros iniciales

    try {
        Map<String, String> condiciones = Map.of("serie", serie, "nro_documento", nroDocumento);
        List<Map<String, String>> ventas = tmVentas.readRegisterList(Map.of("id", "id"), condiciones);
        return ventas.isEmpty() ? null : ventas.get(0).get("id");
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}




    @Override
    public int imPrimero() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imSiguiente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imAnterior() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imUltimo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imInsFilas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imDelFilas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }















    
}
