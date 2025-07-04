/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
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
import Filtros.NumericKeyListener;
import Filtros.TextFilter;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import Modelo.PrecioDetalle;
import Modelo.cargaComboBox;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.PlainDocument;
import javax.swing.*;


public class Form_Lista_Precios extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario  {
 private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private DBTableController tc;
    private DBTableController tcdet;
    private ArrayList<PrecioDetalle> listaDetalles;
    private JComboBox<String> jcbMoneda;
    private ModeloTabla modelo;
    private int filasTabla;
    private int columnasTabla;
    public static int filaSeleccionada;
    private ArrayList<Map<String, String>> columnData, colDat;
    private DBTableModel tMPrecios;
    private Map<String, String> mapPrecios;
    private DBTableModel tmPrecioDet;
    private List<Object[]> productosSeleccionados = new ArrayList<>();
    private Map<String, String> mapPrecioDet;
    private DecimalDocumentFilter decimalDocumentFilter;
    private boolean isSavingOrUpdating = false;

private boolean isUpdating = false;
private DBTableController tcProductos; // Controlador para la tabla productos
private DBTableController tcProductosDetalle; // Controlador para la tabla productos



public Form_Lista_Precios() {
    initComponents();
    tcdet = new DBTableController(); // Inicialización de tcdet

    id_precio.setText("0");

    listaDetalles = new ArrayList<PrecioDetalle>();
    myData = new HashMap<String, String>();
    columnData = new ArrayList<Map<String, String>>();
    colDat = new ArrayList<Map<String, String>>();
    jcbMoneda = new JComboBox<>();

    // Inicialización de tc
    tc = new DBTableController();
    tc.iniciar("PRECIOS");

    tcdet = new DBTableController();
    tcdet.iniciar("PRECIOS_DETALLE");

    // Inicialización de tcProductos
    tcProductos = new DBTableController();
    tcProductos.iniciar("productos");
    
        // Inicialización de tcProductos
    tcProductosDetalle = new DBTableController();
    tcProductosDetalle.iniciar("productos_detalle");

    mapPrecios = new HashMap<String, String>();
    tmPrecioDet = new DBTableModel();
    tmPrecioDet.iniciar("PRECIOS_DETALLE");

    cargarUltimoId();
    construirTabla();
    initializeTextFields();

    cargaComboBox.pv_cargar(Moneda, "MONEDAS", "id, moneda", "id", "");

    if (Detalle_precio.getColumnModel().getColumnCount() > 3) {
        Detalle_precio.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(Moneda));
    }
    Detalle_precio.addMouseListener(this);
    Detalle_precio.addKeyListener(this);
    Detalle_precio.setOpaque(false);
    Detalle_precio.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");

    Detalle_precio.getModel().addTableModelListener(e -> {
        if (e.getType() == TableModelEvent.UPDATE && !isUpdating) {
            isUpdating = true;
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 2) { // Columna de precio
                Object data = Detalle_precio.getValueAt(row, column);
                if (data != null) {
                    String value = data.toString().trim();
                    if (value.isEmpty() || value.matches("\\d+(\\.\\d{0,2})?")) { // Check if value is empty or a number with up to 2 decimal places
                        int decimalPlaces = getDecimalPlaces();
                        BigDecimal precioValor = new BigDecimal(value.isEmpty() ? "0" : value);
                        BigDecimal scaledValue = precioValor.setScale(decimalPlaces, RoundingMode.HALF_UP);
                        if (!scaledValue.toString().equals(value)) {
                            Detalle_precio.setValueAt(scaledValue.toString(), row, column);
                        }
                    }
                }
            }
            isUpdating = false;
        }
    });
    Moneda.addActionListener(e -> {
        actualizarDecimales();
        setPriceColumnEditor(); // Reapply editor settings for price column
    });

    setPriceColumnEditor(); // Set the editor initially

    // Añadir DocumentListener para buscar
    Buscar_jtable.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filterTable();
        }
    });
}

// Método para filtrar la tabla
private void filterTable() {
    DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    Detalle_precio.setRowSorter(sorter);

    String text = Buscar_jtable.getText();
    if (text.trim().length() == 0) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
    }
}

private void setPriceColumnEditor() {
    int decimalPlaces = getDecimalPlaces();
    JTextField textField = new JTextField();
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new CustomNumericDocumentFilter(decimalPlaces));
    textField.setInputVerifier(new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            try {
                new BigDecimal(tf.getText());
                return true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio debe ser un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    });
    Detalle_precio.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setText(value != null ? value.toString() : "");
            editor.selectAll();
            return editor;
        }

        @Override
        public boolean stopCellEditing() {
            String value = ((JTextField) getComponent()).getText();
            if (value.isEmpty()) {
                value = "0";
                ((JTextField) getComponent()).setText(value);
            }
            return super.stopCellEditing();
        }
    });
}



// Método para obtener los decimales según la moneda seleccionada
private int getDecimalPlaces() {
    String selectedCurrencyId = Functions.ExtraeCodigo(Moneda.getSelectedItem().toString());
    return tc.getDecimalPlacesForCurrency(selectedCurrencyId);
}

// Método para actualizar los decimales de los precios en la tabla
private void actualizarDecimales() {
    int decimalPlaces = getDecimalPlaces();
    DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        Object precioObj = model.getValueAt(i, 2);
        if (precioObj != null) {
            String value = precioObj.toString().trim();
            try {
                BigDecimal precioValor = new BigDecimal(value.isEmpty() ? "0" : value);
                BigDecimal scaledValue = precioValor.setScale(decimalPlaces, RoundingMode.HALF_UP);
                model.setValueAt(scaledValue.toString(), i, 2);
            } catch (NumberFormatException ex) {
                // Ignorar errores de formato de número
            }
        }
    }
}

private void applyNumericFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
}

private void applyAlphaFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
}





private void configurarModeloTabla() {
    String[] columnNames = {"Cod Barra", "Descripción", "Precio", "ID"};

    DefaultTableModel modeloTabla = new DefaultTableModel(new Object[][]{}, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2; // Solo permite la edición en la columna "Precio"
        }
    };

    Detalle_precio.setModel(modeloTabla);

    // Ocultar la columna de ID
    Detalle_precio.getColumnModel().getColumn(3).setMinWidth(0);
    Detalle_precio.getColumnModel().getColumn(3).setMaxWidth(0);
    Detalle_precio.getColumnModel().getColumn(3).setWidth(0);

    Detalle_precio.setRowHeight(25);
    Detalle_precio.setGridColor(new java.awt.Color(0, 0, 0));
    Detalle_precio.getTableHeader().setReorderingAllowed(false);

    JTableHeader jtableHeader = Detalle_precio.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    Detalle_precio.setTableHeader(jtableHeader);
}






private void aplicarFiltroDecimal() {
    int decimalPlaces = getDecimalPlaces();
    JTextField textField = new JTextField();
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));
    Detalle_precio.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(textField) {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setText(value != null ? value.toString() : "");
            editor.selectAll();
            return editor;
        }
    });
}


    private void actualizarDocumentFilter(JTextField textField) {
        int decimalPlaces = getDecimalPlaces();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));
    }



private void construirTabla() {
    ArrayList<String> titulosList = new ArrayList<>();
    titulosList.add("Cod Barra");
    titulosList.add("Descripción");
    titulosList.add("Precio");

    String[] titulos = titulosList.toArray(new String[0]);
    Object[][] data = obtenerMatrizDatos(titulosList);

    DefaultTableModel modeloTabla = new DefaultTableModel(data, titulos) {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Solo permite la edición de la columna de precio
            return column == 2;
        }
    };

    Detalle_precio.setModel(modeloTabla);
    Detalle_precio.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
    Detalle_precio.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
    Detalle_precio.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));

    Detalle_precio.getTableHeader().setReorderingAllowed(false);
    Detalle_precio.setRowHeight(25);
    Detalle_precio.setGridColor(new java.awt.Color(0, 0, 0));
    Detalle_precio.getColumnModel().getColumn(0).setPreferredWidth(200);
    Detalle_precio.getColumnModel().getColumn(1).setPreferredWidth(300);
    Detalle_precio.getColumnModel().getColumn(2).setPreferredWidth(150);

    JTableHeader jtableHeader = Detalle_precio.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    Detalle_precio.setTableHeader(jtableHeader);
}


    
private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
    String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

    for (int x = 0; x < informacion.length; x++) {
        informacion[x][0] = listaDetalles.get(x).getString("codigobarras");
        informacion[x][1] = listaDetalles.get(x).getString("producto");
        informacion[x][2] = listaDetalles.get(x).getBigDecimal("precio").toString();
    }
    return informacion;
}

private void construirTabla(String[] titulos, Object[][] data) {
    ArrayList<Integer> noEditable = new ArrayList<Integer>();
    modelo = new ModeloTabla(data, titulos, noEditable);
    // Se asigna el modelo a la tabla
    Detalle_precio.setModel(modelo);

    filasTabla = Detalle_precio.getRowCount();
    columnasTabla = Detalle_precio.getColumnCount();

    // Se asigna el tipo de dato que tendrán las celdas de cada columna definida respectivamente para validar su personalización
    Detalle_precio.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
    Detalle_precio.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
    Detalle_precio.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));

    Detalle_precio.getTableHeader().setReorderingAllowed(false);
    Detalle_precio.setRowHeight(25); // Tamaño de las celdas
    Detalle_precio.setGridColor(new java.awt.Color(0, 0, 0)); 

    // Se define el tamaño de largo para cada columna y su contenido
    Detalle_precio.getColumnModel().getColumn(0).setPreferredWidth(200); // Cod Barra
    Detalle_precio.getColumnModel().getColumn(1).setPreferredWidth(300); // Descripción
    Detalle_precio.getColumnModel().getColumn(2).setPreferredWidth(150); // Precio

    // Personaliza el encabezado
    JTableHeader jtableHeader = Detalle_precio.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    Detalle_precio.setTableHeader(jtableHeader);
}
private void setData() {
    // Cabecera
    myData.put("id", id_precio.getText());
    myData.put("listaprecio", listaprecio.getText());
    myData.put("moneda_id", Functions.ExtraeCodigo(Moneda.getSelectedItem().toString()));
    myData.put("activo", Activo.isSelected() ? "1" : "0");

    DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();
    int filasTabla = model.getRowCount();
    columnData.clear(); // Limpiar los datos anteriores

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Obtener los demás valores de la fila y agregarlos a rowData
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object value = model.getValueAt(i, j);
            rowData.put(model.getColumnName(j), value != null ? value.toString().trim() : ""); // Manejar valores nulos con cadena vacía
        }

        // Verificar y convertir los valores antes de agregar a columnData
        String codigo_barra = rowData.get("Cod Barra");
        if (codigo_barra.equals("0")) {
            continue;
        }

        try {
            // Convertir y verificar valores
            BigDecimal precio = new BigDecimal(rowData.get("Precio"));

            // Crear el mapa de detalles
            myDet = new HashMap<>();
            myDet.put("precio_id", id_precio.getText());
            myDet.put("productoDetalle_id", codigo_barra);
            myDet.put("precio", precio.toString());

            // Agregar id solo si no existe
            if (!rowData.containsKey("id")) {
                int ultimoId = obtenerUltimoIdPrecioDetalle(); // Método para obtener el último ID
                String idDetalle = String.valueOf(ultimoId + 1);
                myDet.put("id", idDetalle);
            } else {
                myDet.put("id", rowData.get("id"));
            }

            this.columnData.add(this.myDet);
        } catch (NumberFormatException e) {
            // Manejar errores de conversión
            System.err.println("Error al convertir valores: " + e.getMessage());
        }
    }

    // Imprimir los valores de columnData para verificación
    System.out.println("Valores de columnData después de llenar:");
    for (Map<String, String> myRow : columnData) {
        System.out.println(myRow);
    }
}



private int obtenerUltimoIdPrecioDetalle() {
    int ultimoId = 0;
    try {
        ultimoId = tcdet.getMaxId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoId;
}


@Override
public int imGuardar(String crud) {
    isSavingOrUpdating = true;
    setData(); // Configurar los datos desde el formulario

    // Validación de campos obligatorios de la cabecera
    List<String> columnasObligatorias = Arrays.asList("listaprecio", "moneda_id");
    for (String columna : columnasObligatorias) {
        if (!myData.containsKey(columna) || myData.get(columna) == null || myData.get(columna).isEmpty() || Moneda.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            
            return -1;
        }
    }

    int idCabecera = Integer.parseInt(myData.getOrDefault("id", "0"));
    if (idCabecera == 0 || myData.get("id") == null || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID de la cabecera no puede quedar vacío o ser cero.", "Error", JOptionPane.ERROR_MESSAGE);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    }

    if (columnData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe haber al menos un detalle.", "Error", JOptionPane.ERROR_MESSAGE);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    }

    // Validación de campos obligatorios de los detalles
    List<String> columnasObli = Arrays.asList("productoDetalle_id", "precio");
    boolean detalleInvalido = false;
    for (Map<String, String> myRow : columnData) {
        for (String columna : columnasObli) {
            String valor = myRow.get(columna);
            if (valor == null || valor.isEmpty() || (columna.equals("precio") && new BigDecimal(valor).compareTo(BigDecimal.ZERO) == 0)) {
                detalleInvalido = true;
                break;
            }
        }
        if (detalleInvalido) {
            JOptionPane.showMessageDialog(this, "Todos los detalles deben tener campos obligatorios válidos (no vacíos y no ceros).", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            return -1;
        }
    }

    // Validación de códigos de barras duplicados en la misma lista de precios
    Set<String> codigosBarras = new HashSet<>();
    for (Map<String, String> myRow : columnData) {
        String codigoBarras = myRow.get("productoDetalle_id");
        if (codigosBarras.contains(codigoBarras)) {
            JOptionPane.showMessageDialog(this, "El código de barras " + codigoBarras + " está duplicado en los detalles.", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            return -1;
        }
        codigosBarras.add(codigoBarras);
    }

    if (!guardarCabecera(idCabecera)) {
        System.out.println("Fallo al guardar la cabecera con ID: " + idCabecera);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    } else {
        System.out.println("Cabecera guardada exitosamente con ID: " + idCabecera);
    }

    // Guardar detalles
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", String.valueOf(idCabecera));
    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);

    Map<String, Map<String, String>> detallesExistentesMap = new HashMap<>();
    for (Map<String, String> detalle : detallesExistentes) {
        detallesExistentesMap.put(detalle.get("productoDetalle_id"), detalle);
    }

    for (Map<String, String> myRow : columnData) {
        myRow.put("precio_id", String.valueOf(idCabecera));
        String productoDetalleId = myRow.get("productoDetalle_id");

        if (detallesExistentesMap.containsKey(productoDetalleId)) {
            // Actualizar el detalle existente
            Map<String, String> existingDetail = detallesExistentesMap.get(productoDetalleId);
            String detalleId = existingDetail.get("id");
            myRow.put("id", detalleId);
            detallesExistentesMap.remove(productoDetalleId);
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(myRow);
            tcdet.updateReg(alDetalle);
            System.out.println("Detalle actualizado: " + myRow);
        } else {
            // Insertar un nuevo detalle
            int newDetalleId = obtenerUltimoIdPrecioDetalle() + 1;
            myRow.put("id", String.valueOf(newDetalleId));
            tcdet.createReg(myRow);
            System.out.println("Detalle creado: " + myRow);
        }
    }

    // Eliminar detalles que ya no existen
    for (Map.Entry<String, Map<String, String>> entry : detallesExistentesMap.entrySet()) {
        ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
        alDetalle.add(entry.getValue());
        tcdet.deleteReg(alDetalle);
        System.out.println("Detalle eliminado: " + entry.getValue());
    }

    JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    isSavingOrUpdating = false;
    imNuevo();
    return 0;
}

private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    alCabecera.add(processFields(myData));
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));

    if (tc.searchListById(fields, where).isEmpty()) {
        int rows = tc.createReg(myData);
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar crear el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar el registro: " + idCabecera, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    return true;
}



private Map<String, String> processFields(Map<String, String> data) {
    Map<String, String> processedData = new HashMap<>();
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if ("listaprecio".equals(key)) {
            // Eliminar comillas dobles innecesarias antes de agregar las nuevas comillas dobles
            value = value.replaceAll("^\"|\"$", "");
            processedData.put(key, "\"" + value.replace("\"", "\\\"") + "\""); // Usar comillas dobles para listaprecio
        } else if ("activo".equals(key) || "moneda_id".equals(key) || "id".equals(key)) {
            processedData.put(key, value); // Tratar campos numéricos correctamente
        } else {
            processedData.put(key, "'" + value.replace("'", "\\'") + "'"); // Escapar valores de texto correctamente
        }
    }
    return processedData;
}


// Implementación del método imNuevo para evitar UnsupportedOperationException
@Override
public int imNuevo() {
    resetData();  // Limpiar los datos del formulario
    limpiarTabla();  // Limpiar la tabla de detalles
    cargarUltimoId();  // Cargar el último ID disponible
    return 0;
}



@Override
public int imBorrar(String crud) {
    setData(); // Actualiza los datos de la vista

    // Verificar que las columnas obligatorias de la cabecera no estén vacías
    List<String> columnasObligatoriasCabecera = Arrays.asList("listaprecio", "moneda_id");
    for (String columna : columnasObligatoriasCabecera) {
        if (myData.get(columna).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las columnas obligatorias de la cabecera no pueden quedar vacías.", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }

    // Verificar campo obligatorio ID para la cabecera
    if (myData.get("id").equals("0") || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID de la cabecera no puede quedar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que haya al menos un detalle
    if (columnData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se puede eliminar un registro sin detalles.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
    
    // Verificar que todos los detalles tengan campos obligatorios válidos (no vacíos ni cero)
    List<String> columnasObli = Arrays.asList("productoDetalle_id", "precio");
    boolean detalleInvalido = false;
    for (Map<String, String> myRow : columnData) {
        for (String columna : columnasObli) {
            String valor = myRow.get(columna);
            if (valor == null || valor.isEmpty() || valor.equals("0")) {
                detalleInvalido = true;
                break;
            }
        }
        if (detalleInvalido) {
            JOptionPane.showMessageDialog(this, "Todos los detalles deben tener campos obligatorios válidos (no vacíos y no ceros).", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }
    
    // Primero, verificamos si el ID ya existe en la base de datos
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", myData.get("id"));
    
    List<Map<String, String>> existingHeaders = tc.searchListById(fields, where);
    if (existingHeaders.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El registro no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar si la cabecera coincide con los datos de la base de datos
    Map<String, String> existingHeader = existingHeaders.get(0);
    for (String columna : columnasObligatoriasCabecera) {
        if (!myData.get(columna).equals(existingHeader.get(columna))) {
            JOptionPane.showMessageDialog(this, "La cabecera no coincide con los datos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Verificar si existen los detalles correspondientes a la cabecera
    where.clear();
    where.put("precio_id", myData.get("id"));
    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);
    if (detallesExistentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No existen detalles asociados a esta cabecera en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificar que los detalles coincidan con los datos de la base de datos
    for (Map<String, String> myRow : columnData) {
        boolean foundMatchingDetail = false;
        for (Map<String, String> detalleExistente : detallesExistentes) {
            boolean detallesCoinciden = true;
            for (String columna : columnasObli) {
                if (!myRow.get(columna).equals(detalleExistente.get(columna))) {
                    detallesCoinciden = false;
                    break;
                }
            }
            if (detallesCoinciden) {
                foundMatchingDetail = true;
                break;
            }
        }
        if (!foundMatchingDetail) {
            JOptionPane.showMessageDialog(this, "Los detalles no coinciden con los datos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Iniciar transacción (simulada)
    boolean cabeceraEliminada = false;
    boolean detallesEliminados = false;

    try {
        // Eliminar los detalles
        int affectedDetails = 0;
        for (Map<String, String> detalle : detallesExistentes) {
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(detalle);
            affectedDetails += tcdet.deleteReg(alDetalle);
        }

        if (affectedDetails == detallesExistentes.size()) {
            detallesEliminados = true;
        } else {
            throw new Exception("Error al eliminar los detalles del registro.");
        }

        // Eliminar la cabecera
        ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
        alCabecera.add(myData);
        int rowsAffected = tc.deleteReg(alCabecera);
        if (rowsAffected > 0) {
            cabeceraEliminada = true;
        } else {
            throw new Exception("Error al eliminar la cabecera del registro.");
        }

        // Confirmar la transacción (simulada)
        if (cabeceraEliminada && detallesEliminados) {
            String msg = "EL REGISTRO: " + id_precio.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            imNuevo();
            return 0;
        } else {
            throw new Exception("Error durante la eliminación del registro.");
        }
    } catch (Exception e) {
        // Revertir la transacción (simulada)
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
}



@Override
public int imBuscar() {
    this.setData(); // Recopila los datos de la vista

    // Verificar si el ID es 0
    if (myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "El ID 0 no es válido. Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de error
    }

    // Realiza la búsqueda de la cabecera
    Map<String, String> resultadoCabecera = this.tc.searchById(myData);
    System.out.println("PRECIOS imBuscar " + resultadoCabecera);

    // Limpia la tabla de la vista
    this.limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        System.out.println("No hay registros que mostrar");
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualiza myData con los resultados de la búsqueda de la cabecera
    this.myData = resultadoCabecera;

    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", this.myData.get("id"));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el precio especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de que no se encontraron detalles
    }

    // Convierte el resultado a ArrayList y actualiza columnData
    this.columnData = new ArrayList<>(detalles);

    // Llena la vista con los datos recuperados
    this.fillView(myData, columnData);

    return 0; // Indicador de que la búsqueda fue exitosa
}



    @Override
    public int imFiltrar() {
         Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaPrecios = Arrays.asList("id", "listaprecio");
    Form_Buscar buscadorPrecios = new Form_Buscar(parentFrame, true, tc, "PRECIOS", columnasParaPrecios);

    buscadorPrecios.setOnItemSeleccionadoListener(this);
    buscadorPrecios.setVisible(true);

    return 0;
    }

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    String idStr = datosSeleccionados.get("Codigo");
    String descripcion = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idPrecio = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("PRECIOS", "id", idPrecio);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                SwingUtilities.invokeLater(() -> {
                    id_precio.setText(idStr);
                    listaprecio.setText(descripcion);

                    // Seleccionar el ítem correcto en el ComboBox Moneda
                    String monedaId = registro.get("moneda_id");
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) Moneda.getModel();
                    for (int i = 0; i < model.getSize(); i++) {
                        String item = model.getElementAt(i);
                        if (Functions.ExtraeCodigo(item).equals(monedaId)) {
                            Moneda.setSelectedIndex(i);
                            break;
                        }
                    }

                    Activo.setSelected(Integer.parseInt(registro.get("activo")) != 0);
                });

                // Cargar detalles del precio
                cargarDetallesPrecio(idPrecio);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un registro con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
                limpiarTabla();
                imInsFilas(); // Añadir una fila vacía
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de registro inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void cargarDetallesPrecio(int idPrecio) {
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", String.valueOf(idPrecio));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el precio especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return;
    }

    // Convierte el resultado a ArrayList y actualiza columnData
    this.columnData = new ArrayList<>(detalles);

    // Llena la vista con los datos recuperados
    this.fillView(myData, columnData);
}


@Override
public int imPrimero() {
    this.myData = this.tc.navegationReg("id", "FIRST");

    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    String precioId = this.myData.get("id");
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", precioId);

    // Asegurarse de seleccionar todas las columnas
    Map<String, String> fieldsToSelect = new HashMap<>();
    fieldsToSelect.put("*", "*"); // Seleccionar todas las columnas
    List<Map<String, String>> resultList = this.tcdet.searchListById(fieldsToSelect, where);

    // Añadir mensajes de depuración
    System.out.println("Datos de precios_detalle: " + resultList);

    // Realizar el casting de List a ArrayList
    if (resultList instanceof ArrayList) {
        this.columnData = (ArrayList<Map<String, String>>) resultList;
    } else {
        this.columnData = new ArrayList<>(resultList);
    }

    this.limpiarTabla();
    this.fillView(this.myData, this.columnData);
    return 0;
}



@Override
public int imSiguiente() {
    if (id_precio.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    // Obtener el siguiente registro basado en el ID actual
    this.myData = this.tc.navegationReg(id_precio.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    // Obtener el ID del registro de precios actual
    String precioId = this.myData.get("id");
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", precioId);

    // Asegurarse de seleccionar todas las columnas
    Map<String, String> fieldsToSelect = new HashMap<>();
    fieldsToSelect.put("*", "*"); // Seleccionar todas las columnas
    List<Map<String, String>> resultList = this.tcdet.searchListById(fieldsToSelect, where);

    // Añadir mensajes de depuración
    System.out.println("Datos de precios_detalle: " + resultList);

    // Realizar el casting de List a ArrayList
    if (resultList instanceof ArrayList) {
        this.columnData = (ArrayList<Map<String, String>>) resultList;
    } else {
        this.columnData = new ArrayList<>(resultList);
    }

    // Limpiar la tabla y llenar la vista con los nuevos datos
    this.limpiarTabla();
    this.fillView(this.myData, this.columnData);
    return 0;
}


@Override
public int imAnterior() {
    if (id_precio.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    // Obtener el registro anterior basado en el ID actual
    this.myData = this.tc.navegationReg(id_precio.getText(), "PREVIOUS");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    // Obtener el ID del registro de precios actual
    String precioId = this.myData.get("id");
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", precioId);

    // Asegurarse de seleccionar todas las columnas
    Map<String, String> fieldsToSelect = new HashMap<>();
    fieldsToSelect.put("*", "*"); // Seleccionar todas las columnas
    List<Map<String, String>> resultList = this.tcdet.searchListById(fieldsToSelect, where);

    // Añadir mensajes de depuración
    System.out.println("Datos de precios_detalle: " + resultList);

    // Realizar el casting de List a ArrayList
    if (resultList instanceof ArrayList) {
        this.columnData = (ArrayList<Map<String, String>>) resultList;
    } else {
        this.columnData = new ArrayList<>(resultList);
    }

    // Limpiar la tabla y llenar la vista con los nuevos datos
    this.limpiarTabla();
    this.fillView(this.myData, this.columnData);
    return 0;
}

@Override
public int imUltimo() {
    // Obtener el último registro
    this.myData = this.tc.navegationReg("id", "LAST");

    // Verificar si se encontraron registros
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    // Obtener el ID del registro de precios actual
    String precioId = this.myData.get("id");
    Map<String, String> where = new HashMap<>();
    where.put("precio_id", precioId);

    // Asegurarse de seleccionar todas las columnas
    Map<String, String> fieldsToSelect = new HashMap<>();
    fieldsToSelect.put("*", "*"); // Seleccionar todas las columnas
    List<Map<String, String>> resultList = this.tcdet.searchListById(fieldsToSelect, where);

    // Añadir mensajes de depuración
    System.out.println("Datos de precios_detalle: " + resultList);

    // Realizar el casting de List a ArrayList
    if (resultList instanceof ArrayList) {
        this.columnData = (ArrayList<Map<String, String>>) resultList;
    } else {
        this.columnData = new ArrayList<>(resultList);
    }

    // Limpiar la tabla y llenar la vista con los nuevos datos
    this.limpiarTabla();
    this.fillView(this.myData, this.columnData);
    return 0;
}

  @Override
    public int imImprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(pageFormat.getImageableWidth() / getWidth(), pageFormat.getImageableHeight() / getHeight());
                paint(g2d);
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.setJobName("Precios");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

@Override
public int imInsFilas() {
     if (isSavingOrUpdating) {
        return 0;
    }
    DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        Object precioObj = model.getValueAt(i, 2);
        if (precioObj == null) {
            JOptionPane.showMessageDialog(this, "Por favor, cambie el precio de los productos antes de insertar una nueva fila.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
        String precioStr = precioObj.toString().trim();
        if (precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, cambie el precio de los productos antes de insertar una nueva fila.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
        try {
            BigDecimal precio = new BigDecimal(precioStr.replace(",", ""));
            if (precio.compareTo(BigDecimal.ZERO) == 0) {
                JOptionPane.showMessageDialog(this, "Por favor, cambie el precio de los productos antes de insertar una nueva fila.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return 0;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
    }

    // Abrir la ventana de Form_BuscarTabla cuando se necesite añadir una fila nueva
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaBusqueda = Arrays.asList("codigobarras", "producto");
    Form_BuscarTabla buscadorProductos = new Form_BuscarTabla(parentFrame, true, tc, "productos_detalle", columnasParaBusqueda);
    buscadorProductos.setOnItemSeleccionadoListener2(new InterfaceUsuario() {
        @Override
        public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
            procesarSeleccion(datosSeleccionados);
        }

        @Override
        public int imGuardar(String crud) { return 0; }

        @Override
        public int imBorrar(String crud) { return 0; }

        @Override
        public int imNuevo() { return 0; }

        @Override
        public int imBuscar() { return 0; }

        @Override
        public int imFiltrar() { return 0; }

        @Override
        public int imPrimero() { return 0; }

        @Override
        public int imSiguiente() { return 0; }

        @Override
        public int imAnterior() { return 0; }

        @Override
        public int imUltimo() { return 0; }

        @Override
        public int imImprimir() { return 0; }

        @Override
        public int imInsFilas() { return 0; }

        @Override
        public int imDelFilas() { return 0;}
    });
    buscadorProductos.setVisible(true);
    return 0;
}





private void procesarSeleccion(Map<String, String> datosSeleccionados) {
    String codigobarras = datosSeleccionados.getOrDefault("Codigo", "N/A");
    String producto = datosSeleccionados.getOrDefault("Descripcion", "N/A");

    if (!"N/A".equals(codigobarras) || !"N/A".equals(producto)) {
        DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();

        // Verificar si el producto ya está en la tabla
        boolean exists = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingCodigobarras = model.getValueAt(i, 0).toString();
            if (existingCodigobarras.equals(codigobarras)) {
                exists = true;
                break;
            }
        }

        // Si no existe, agregar a la tabla
        if (!exists) {
            int decimalPlaces = getDecimalPlaces();
            String precioInicial = String.format("%." + decimalPlaces + "f", 0.0);
            model.addRow(new Object[]{codigobarras, producto, precioInicial});
        } else {
            JOptionPane.showMessageDialog(this, "El producto ya ha sido seleccionado.", "Producto duplicado", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selección de producto inválida.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



@Override
public int imDelFilas() {
    int selectedRow = Detalle_precio.getSelectedRow(); // Obtén la fila seleccionada
    if (selectedRow != -1) { // Verifica que haya una fila seleccionada
        DefaultTableModel model = (DefaultTableModel) Detalle_precio.getModel();
        model.removeRow(selectedRow); // Elimina la fila del modelo de la tabla
        JOptionPane.showMessageDialog(this, "Fila eliminada con éxito.", "Eliminación de fila", JOptionPane.INFORMATION_MESSAGE);
        return 1; // Devuelve 1 indicando que la fila fue eliminada
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return 0; // Devuelve 0 indicando que no se eliminó ninguna fila
    }
}


private void resetData() {
    // Restablecer datos de la cabecera
    this.myData = new HashMap<>();
    this.myData.put("id", "0");
    this.myData.put("listaprecio", "");
    this.myData.put("moneda_id", "0");
    this.myData.put("activo", "0");

    // Restablecer los campos de entrada de la cabecera
    id_precio.setText("0");
    listaprecio.setText("");
    Moneda.setSelectedIndex(0); // Selecciona el primer elemento del JComboBox Moneda
    Activo.setSelected(false); // Desactiva el JCheckBox Activo

    // Restablecer datos del detalle
    this.myDet = new HashMap<>();
    this.myDet.put("precio_id", "0");
    this.myDet.put("productoDetalle_id", "0");
    this.myDet.put("precio", "0");

    // Limpiar datos de la tabla de detalles
    this.columnData.clear();
    this.columnData.add(this.myDet);

    // Actualizar la vista con los datos restablecidos
    fillView(myData, columnData);
}


public void fillView(Map<String, String> data, List<Map<String, String>> colData) {
    // Actualizar los campos de entrada de la cabecera
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue().replaceAll("^\"|\"$", ""); // Eliminar comillas dobles al inicio y al final
        switch (key) {
            case "id":
                id_precio.setText(value);
                break;
            case "listaprecio":
                listaprecio.setText(value);
                break;
            case "moneda_id":
                Functions.E_estado(Moneda, "MONEDAS", "id=" + value);
                break;
            case "activo":
                Activo.setSelected(Integer.parseInt(value) != 0);
                break;
        }
    }

    // Limpiar la tabla de detalles antes de llenarla
    limpiarTabla();

    // Obtener el número de decimales según la moneda seleccionada
    int decimalPlaces = getDecimalPlaces();

    // Llenar la tabla de detalles con los datos proporcionados
    DefaultTableModel modelo = (DefaultTableModel) Detalle_precio.getModel();

    for (Map<String, String> myRow : colData) {
        Object[] rowData = new Object[4]; // 4 columnas incluyendo la columna oculta de ID

        String codigobarras = myRow.get("productoDetalle_id");
        String precio = myRow.get("precio");
        String idDetalle = myRow.get("id");

        // Ajustar el precio según los decimales de la moneda
        BigDecimal precioValor = new BigDecimal(precio);
        precio = precioValor.setScale(decimalPlaces, RoundingMode.HALF_UP).toString();

        String cabeceraId = obtenerCabeceraId(codigobarras);
        if (!"N/A".equals(cabeceraId)) {
            String descripcion = obtenerDescripcionProducto(cabeceraId);
            rowData[0] = codigobarras;
            rowData[1] = descripcion;
            rowData[2] = precio;
            rowData[3] = idDetalle; // Añadir el ID del detalle en una columna oculta

            modelo.addRow(rowData);
        }
    }
}


private String obtenerCabeceraId(String codigobarras) {
    if (codigobarras == null || codigobarras.isEmpty()) {
        return "N/A";
    }

    Map<String, String> viewRegister = new HashMap<>();
    viewRegister.put("cabecera_id", "cabecera_id");

    Map<String, String> where = new HashMap<>();
    where.put("codigobarras", codigobarras);

    List<Map<String, String>> result = tcProductosDetalle.searchListById(viewRegister, where);  // Usando el controlador para productos_detalle

    if (!result.isEmpty() && result.get(0).containsKey("cabecera_id")) {
        return result.get(0).get("cabecera_id");
    } else {
        return "N/A";
    }
}

private String obtenerDescripcionProducto(String cabeceraId) {
    if (cabeceraId == null || cabeceraId.isEmpty() || "N/A".equals(cabeceraId)) {
        return "N/A";
    }

    Map<String, String> viewRegister = new HashMap<>();
    viewRegister.put("producto", "producto");

    Map<String, String> where = new HashMap<>();
    where.put("id", cabeceraId);

    List<Map<String, String>> result = tcProductos.searchListById(viewRegister, where);  // Usando el controlador para productos

    if (!result.isEmpty() && result.get(0).containsKey("producto")) {
        return result.get(0).get("producto");
    } else {
        return "N/A";
    }
}




private void limpiarTabla() {
    DefaultTableModel modelo = (DefaultTableModel) Detalle_precio.getModel();
    modelo.setRowCount(0);  // Esto limpiará todas las filas de la tabla
}

private void initializeTextFields() {
        applyAlphaNumericFilter(listaprecio); // Aplica el nuevo filtro alfanumérico
        applyNumericFilter(id_precio); // Aplica el filtro numérico al campo id_precio
        addFocusListeners(); // Agrega los listeners de foco
    }

    // Método para aplicar el filtro alfanumérico
    private void applyAlphaNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new AlphaNumericDocumentFilter());
    }

private void addFocusListeners() {
    id_precio.addFocusListener(new DefaultFocusListener(id_precio, true));
    listaprecio.addFocusListener(new DefaultFocusListener(listaprecio, false));
    
}

private void cargarUltimoId() {
    try {
        int ultimoId = tc.getMaxId();
        UltimoId.setText(String.valueOf(ultimoId));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

   public void limpiarCelda(JTable tabla){
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
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
        id_precio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        listaprecio = new javax.swing.JTextField();
        Activo = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        Moneda = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Detalle_precio = new javax.swing.JTable();
        UltimoId = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Buscar_jtable = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);

        jLabel1.setText("Id");

        id_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id_precioKeyPressed(evt);
            }
        });

        jLabel2.setText("Lista");

        Activo.setText("Activo");

        jLabel4.setText("Moneda");

        Moneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonedaActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        Detalle_precio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Descripción", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Detalle_precio);

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setOpaque(true);

        jLabel5.setText("Ultimo");

        jLabel6.setText("Buscar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(listaprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(id_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(220, 220, 220)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(147, 147, 147)
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(Buscar_jtable, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Activo)
                .addGap(81, 81, 81))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(id_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(listaprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(Moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Activo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Buscar_jtable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MonedaActionPerformed
    Moneda.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            actualizarDecimales();
            setPriceColumnEditor(); // Reapply editor settings for price column
        }
    });
    }//GEN-LAST:event_MonedaActionPerformed

    private void id_precioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_precioKeyPressed
             if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }  
    }//GEN-LAST:event_id_precioKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JTextField Buscar_jtable;
    private javax.swing.JTable Detalle_precio;
    private javax.swing.JComboBox<String> Moneda;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JTextField id_precio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField listaprecio;
    // End of variables declaration//GEN-END:variables
 @Override
    public void mouseClicked(MouseEvent e) {
        int fila = Detalle_precio.rowAtPoint(e.getPoint());
        int columna = Detalle_precio.columnAtPoint(e.getPoint());

        if (columna == 2) {
            Detalle_precio.setColumnSelectionInterval(columna, columna);
            Detalle_precio.editCellAt(fila, columna);
            Component editor = Detalle_precio.getEditorComponent();
            if (editor instanceof JTextField) {
                JTextField textField = (JTextField) editor;
                SwingUtilities.invokeLater(textField::selectAll);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int row = Detalle_precio.getSelectedRow(); // Obtiene la fila seleccionada
        int col = Detalle_precio.getSelectedColumn(); // Obtiene la columna seleccionada

        if (col == 2) { // Columna de precio
            if (Detalle_precio.isEditing()) { // Si la tabla está en modo de edición
                JTextField editor = (JTextField) Detalle_precio.getEditorComponent();
                editor.setText(editor.getText()); // Actualiza el contenido del editor
                editor.selectAll(); // Selecciona todo el texto al editar
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }



}

