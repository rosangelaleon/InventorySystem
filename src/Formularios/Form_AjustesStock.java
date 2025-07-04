package Formularios;
import Controllers.DBConexion;
import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.CodigoBarraFilter;
import Filtros.DecimalDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.RucDocumentFilter;
import Filtros.TextFilter;
import Modelo.AjusteDetalle;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import Modelo.VentaDetalle;
import Modelo.cargaComboBox;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class Form_AjustesStock extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario{

    private DefaultTableModel modelo;
    private DBTableController tc;
    private DBTableController tm;
    private DBTableController tstock;
    private DBTableController tproductos;
     private DBTableController tmDepositos;
     private DBTableController  tproductosdet;
    private DBTableController tcdet;
     private Map<String, String> myData;
     private Component lastFocusedComponent;
    private ModeloTabla tableModel;
    private ArrayList<Map<String, String>> columnData;
    private final Map<String, String> depositos;
       private ArrayList<AjusteDetalle> listaDetalles;
    ArrayList<AjusteDetalle> lista;
    AjusteDetalle ajustesdetalle;
    int idAjuste = 0, permisosBtn[];
    String itemDeposito = "";
    boolean showDate = false, view = false;
    String field;

    public Form_AjustesStock() {
        tc = new DBTableController();
        tc.iniciar("AJUSTES_STOCK");
        tcdet = new DBTableController();
        tcdet.iniciar("AJUSTES_STOCK_DETALLE");
        tproductos = new DBTableController();
        tproductos .iniciar("Productos");
        tmDepositos = new DBTableController();
        tmDepositos.iniciar("DEPOSITOS");
        tstock = new DBTableController();
        tstock.iniciar("STOCKS");
        tproductosdet = new DBTableController();
        tproductosdet.iniciar("PRODUCTOS_DETALLE");
        myData = new HashMap<>();
        depositos = new HashMap<>();
        tableModel = new ModeloTabla();
        listaDetalles = new ArrayList<>();
        lista = new ArrayList<>();

        initComponents();
        initializeTextFields();
        construirTabla();
        cargarUltimoId();
 cargaComboBox.pv_cargar(cboxdeposito, "DEPOSITOS", "id, deposito", "id", "");

    }
    
    private void addKeyListeners() {
    FocusListener focusTracker = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = e.getComponent();
        }
    };
    txtId.addFocusListener(focusTracker);

    // FocusListener para las celdas de la columna "Cod Barra" en jtDetalle
    jtdetalle.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = jtdetalle;
        }
    });

    jtdetalle.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()) {
        {
            getComponent().addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    lastFocusedComponent = jtdetalle;
                }
            });
        }
    });

    jtdetalle.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F2) {
                int selectedRow = jtdetalle.getSelectedRow();
                int selectedColumn = jtdetalle.getSelectedColumn();
                if (selectedRow != -1 && selectedColumn == 0) { // Column 0 is "Cod Barra"
                    imFiltrar();
                }
            }
        }
    });
}
    private void initializeTextFields() {
        applyNumericFilter(txtId);
        applyAlphaFilter(txtmotivo);
        addFocusListeners(); 
        addKeyListeners();
    }
 private void addFocusListeners() {
        txtId.addFocusListener(new DefaultFocusListener(txtId, true));
         txtmotivo.addFocusListener(new DefaultFocusListener(txtmotivo, false));
 
    }
    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }
    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

  private void cargarUltimoId() {
        try {
            int ultimoId = tc.getMaxId();
            Ultimo.setText(String.valueOf(ultimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        public void limpiarCelda(JTable tabla){
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
    }

           public void limpiarTabla(){
        this.columnData.clear();
        try {
            DefaultTableModel modelo = (DefaultTableModel)jtdetalle.getModel();
            int filas = jtdetalle.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }//fin limpiarTabla
private void construirTabla() {
    listaDetalles = consultarListaDetalles();
    ArrayList<String> titulosList = new ArrayList<>();
    titulosList.add("Cod Barra");
    titulosList.add("Descripción");
    titulosList.add("Cantidad Actual");
    titulosList.add("Cantidad Ajuste");
    titulosList.add("Id");

    String[] titulos = titulosList.toArray(new String[0]);

    Object[][] data = obtenerMatrizDatos(titulosList);
    construirTabla(titulos, data);

}

private ArrayList<AjusteDetalle> consultarListaDetalles() {
    listaDetalles.add(new AjusteDetalle(1, 0, "0", 0, 0, "0", null));
    return listaDetalles;
}

private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
    String informacion[][] = new String[listaDetalles.size()][titulosList.size()];
    // Inicializar toda la matriz con "0"
    for (int i = 0; i < informacion.length; i++) {
        for (int j = 0; j < informacion[i].length; j++) {
            informacion[i][j] = "0";
        }
    }

    for (int x = 0; x < informacion.length; x++) {
        informacion[x][0] =  listaDetalles.get(x).getString("codigobarras") != null ? listaDetalles.get(x).getString("codigobarras") : "0";
        informacion[x][1] =  listaDetalles.get(x).getString("producto") != null ? listaDetalles.get(x).getString("producto") : "0";
        informacion[x][2] = listaDetalles.get(x).getInteger("cantidad_actual") + "";
        informacion[x][3] = listaDetalles.get(x).getInteger("cantidad_ajuste") + "";
        informacion[x][4] = listaDetalles.get(x).getInteger("id")+ "";
    }
    return informacion;
}

private void construirTabla(String[] titulos, Object[][] data) {
    ArrayList<Integer> noEditable = new ArrayList<>(List.of(1, 2));
    modelo = new ModeloTabla(data, titulos, noEditable);
    jtdetalle.setModel(modelo);

     jtdetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("numerico"));
     jtdetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("texto"));
     jtdetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("numerico"));
     jtdetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));

          // Ocultar la columna del ID
    jtdetalle.getColumnModel().getColumn(4).setMinWidth(0);
    jtdetalle.getColumnModel().getColumn(4).setMaxWidth(0);
    jtdetalle.getColumnModel().getColumn(4).setWidth(0);
    jtdetalle.getColumnModel().getColumn(4).setPreferredWidth(0);


     
     jtdetalle.getTableHeader().setReorderingAllowed(false);
     jtdetalle.setRowHeight(25);
     jtdetalle.setGridColor(new java.awt.Color(0, 0, 0));

     jtdetalle.getColumnModel().getColumn(0).setPreferredWidth(100);
     jtdetalle.getColumnModel().getColumn(1).setPreferredWidth(150);
     jtdetalle.getColumnModel().getColumn(2).setPreferredWidth(200);
     jtdetalle.getColumnModel().getColumn(3).setPreferredWidth(150);


    JTableHeader jtableHeader = jtdetalle.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    jtdetalle.setTableHeader(jtableHeader);
}

public boolean validarMovimientosPosteriores(String fecha, List<String> codigosBarras) {
    String sqlCompras = "SELECT COUNT(*) as movimientos FROM COMPRAS " +
            "INNER JOIN COMPRAS_DETALLE ON COMPRAS.id = COMPRAS_DETALLE.compra_id " +
            "WHERE COMPRAS.fechaFactura > ? AND COMPRAS_DETALLE.productodetalle_id IN (" +
            codigosBarras.stream().map(cb -> "?").collect(Collectors.joining(", ")) + ")";
    
    String sqlVentas = "SELECT COUNT(*) as movimientos FROM VENTAS " +
            "INNER JOIN VENTAS_DETALLE ON VENTAS.id = VENTAS_DETALLE.venta_id " +
            "WHERE VENTAS.fechaProceso > ? AND VENTAS_DETALLE.productodetalle_id IN (" +
            codigosBarras.stream().map(cb -> "?").collect(Collectors.joining(", ")) + ")";
    
    try (PreparedStatement stmtCompras = DBConexion.con.prepareStatement(sqlCompras);
         PreparedStatement stmtVentas = DBConexion.con.prepareStatement(sqlVentas)) {
        
        // Configurar parámetros para COMPRAS
        stmtCompras.setString(1, fecha);
        for (int i = 0; i < codigosBarras.size(); i++) {
            stmtCompras.setString(i + 2, codigosBarras.get(i));
        }
        
        // Configurar parámetros para VENTAS
        stmtVentas.setString(1, fecha);
        for (int i = 0; i < codigosBarras.size(); i++) {
            stmtVentas.setString(i + 2, codigosBarras.get(i));
        }
        
        // Ejecutar consultas y sumar los movimientos
        int movimientos = 0;
        try (ResultSet rsCompras = stmtCompras.executeQuery();
             ResultSet rsVentas = stmtVentas.executeQuery()) {
            if (rsCompras.next()) {
                movimientos += rsCompras.getInt("movimientos");
            }
            if (rsVentas.next()) {
                movimientos += rsVentas.getInt("movimientos");
            }
        }
        
        return movimientos == 0; // Retorna true si no hay movimientos posteriores
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Considerar movimientos no válidos en caso de error
    }
}
private void setData() {
    // Cabecera de AJUSTES_STOCK
    myData.put("id", txtId.getText());

    // Verificar y formatear fecha del ajuste
    java.util.Date fechaAjusteDate = Fecha.getDate();
    if (fechaAjusteDate != null) {
        myData.put("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaAjusteDate));
    } else {
        myData.put("fecha", ""); // O un valor por defecto
    }

    // Obtener datos de la cabecera
    myData.put("deposito_id", Functions.ExtraeCodigo(cboxdeposito.getSelectedItem().toString()));
    myData.put("aprobado", aprobado.isSelected() ? "1" : "0");
    myData.put("contabilizado", contabilizado.isSelected() ? "1" : "0");

    // Detalle de AJUSTES_STOCK_DETALLE
    DefaultTableModel model = (DefaultTableModel) jtdetalle.getModel();
    int filasTabla = model.getRowCount();
    columnData.clear(); // Limpiar los datos anteriores

    int ultimoId = obtenerUltimoIdAjusteDetalle(); // Obtener el último ID existente en la tabla DETALLE

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Obtener el ID actual de la fila si existe
        String idDetalle;
        Object idCell = model.getValueAt(i, 4); // Suponiendo que el ID está en la columna índice 4
        if (idCell == null || idCell.toString().trim().isEmpty() || "0".equals(idCell.toString().trim())) {
            // Generar un nuevo ID si no existe
            idDetalle = String.valueOf(++ultimoId);
            model.setValueAt(idDetalle, i, 4); // Asignar el nuevo ID a la celda correspondiente
        } else {
            // Usar el ID existente
            idDetalle = idCell.toString().trim();
        }

        rowData.put("id", idDetalle); // Agregar el ID a los datos de la fila

        // Obtener los demás valores de la fila y agregarlos a rowData
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object value = model.getValueAt(i, j);
            rowData.put(model.getColumnName(j), value != null ? value.toString().trim() : ""); // Manejar valores nulos con cadena vacía
        }

        // Crear el mapa de detalles para AJUSTES_STOCK_DETALLE
        Map<String, String> myDet = new HashMap<>();
        String ajusteId = txtId.getText(); // Asumiendo que ajuste_id es el ID del ajuste
        myDet.put("id", idDetalle);
        myDet.put("ajuste_id", ajusteId);
        myDet.put("productoDetalle_id", rowData.get("Cod Barra"));
        myDet.put("cantidad_actual", rowData.get("Cantidad Actual"));
        myDet.put("cantidad_ajuste", rowData.get("Cantidad Ajuste"));
        columnData.add(myDet);
    }

    // Imprimir los valores de columnData para verificación
    System.out.println("Valores de columnData después de llenar:");
    for (Map<String, String> myRow : columnData) {
        System.out.println(myRow);
    }
}

private int obtenerUltimoIdAjusteDetalle() {
    int ultimoId = 0;
    try {
        ultimoId = tcdet.getMaxId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoId;
}


private int obtenerUltimoIdVentaDetalle() {
    int ultimoId = 0;
    try {
        ultimoId = tcdet.getMaxId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoId;
}

    private void resetData() {
        myData.put("id", "0");
        myData.put("motivo", "");
        myData.put("fecha", "0");
        myData.put("deposito_id", "0");
        myData.put("aprobado", "0");
        myData.put("contabilizado", "0");
        DefaultTableModel dm = (DefaultTableModel) jtdetalle.getModel();
        int rows = dm.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        String newRow[] = {"0", "0", "0", "0"};
        tableModel.addRow(newRow);
        listaDetalles.clear();
    }

   private void fillView(Map<String, String> data, ArrayList<Map<String, String>> ajustesdetalle) {
    // Cargar datos en los campos principales
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        switch (key) {
            case "id":
                txtId.setText(value);
                break;
            case "motivo":
                txtmotivo.setText(value);
                break;
            case "deposito_id":
                Functions.E_estado(cboxdeposito, "DEPOSITOS", "id=" + value);
                break;
            case "fecha":
                String strFecha = data.get("fecha");
                if (strFecha != null && !strFecha.isEmpty()) {
                    try {
                        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strFecha);
                        Fecha.setDate(date);
                    } catch (ParseException e) {
                        Fecha.setDate(null);
                        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Fecha.setDate(null);
                }
                break;
            case "aprobado":
                aprobado.setSelected(value.equals("1"));
                break;
            case "contabilizado":
                contabilizado.setSelected(value.equals("1"));
                break;
        }
    }

    // Limpiar tabla antes de agregar nuevos detalles
    DefaultTableModel tableModel = (DefaultTableModel) jtdetalle.getModel();
    tableModel.setRowCount(0); // Elimina todas las filas existentes

    // Agregar detalles a la tabla
    int row = 0;
    for (Map<String, String> detalle : ajustesdetalle) {
        String codBarra = detalle.get("productodetalle_id");
        String descripcion = detalle.get("producto");
        String cantActual = detalle.get("cantidad_actual");
        String cantAjuste = detalle.get("cantidad_ajuste");

        // Agregar fila formateada
        tableModel.addRow(new Object[]{
            codBarra,
            descripcion,
            cantActual,
            cantAjuste
        });

        // Seleccionar la fila recién agregada
        jtdetalle.getSelectionModel().setSelectionInterval(row, 0);
        row++;
    }

    // Actualizar vista
    jtdetalle.repaint();
}


    public boolean validarCabecera() {
        if (txtId.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor complete el id del Ajuste Stock.", "¡ATENCION!", JOptionPane.WARNING_MESSAGE);
            txtId.requestFocus();
            return false;
        }


        if (cboxdeposito.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un Déposito", "¡ATENCION!", JOptionPane.WARNING_MESSAGE);
            cboxdeposito.requestFocus();
            return false;
        }

        if (Fecha == null) {
            JOptionPane.showMessageDialog(this, "Debe completar la fecha", "¡ATENCION!", JOptionPane.WARNING_MESSAGE);
            txtId.requestFocus();
            return false;
        }

        return true;
    }
// Método para obtener los detalles del producto desde la tabla Productos_Detalle
private Map<String, String> obtenerDetalleProducto(String codBarra) {
    Map<String, String> where = new HashMap<>();
    where.put("codigobarras", codBarra);

    Map<String, String> fields = new HashMap<>();
    fields.put("cabecera_id", "cabecera_id");

    List<Map<String, String>> result = tproductosdet.searchListById(fields, where);
    if (!result.isEmpty()) {
        return result.get(0);
    } else {
        return null;
    }
}
 private int obtenerStockProducto(String codBarra) {
    // Configurar los filtros para buscar en la tabla de stock utilizando el código de barras
    Map<String, String> whereStockDetalle = new HashMap<>();
    whereStockDetalle.put("producto_detalle", codBarra);

    // Definir los campos que queremos recuperar de la tabla 'STOCKS'
    Map<String, String> fieldsStockDetalle = new HashMap<>();
    fieldsStockDetalle.put("stockActual", "stockActual");

    // Consultar la tabla 'STOCKS' con las condiciones establecidas
    List<Map<String, String>> resultStockDetalle = tstock.searchListById(fieldsStockDetalle, whereStockDetalle);

    // Verificar si se encontró el registro correspondiente
    if (resultStockDetalle.isEmpty()) {
        return 0; // Retorna 0 si no hay datos de stock para el código de barras
    }

    // Obtener el primer resultado (único esperado para un código de barras)
    Map<String, String> stockData = resultStockDetalle.get(0);

    // Extraer el valor del stock como cadena y convertirlo a entero
    String stockStr = stockData.get("stockActual");
    return Integer.parseInt(stockStr);
}
private void procesarSeleccion(Map<String, String> datosSeleccionados, int row) {
    // Obtener los datos básicos necesarios
    String codigobarras = datosSeleccionados.get("Codigo");
    String descripcion = datosSeleccionados.get("Descripcion");

    // Depuración: Imprimir los valores obtenidos
    System.out.println("Procesando selección - Codigobarras: " + codigobarras + ", Producto: " + descripcion);

    // Validar datos seleccionados
    if (codigobarras == null || descripcion == null || 
        codigobarras.isEmpty() || descripcion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Datos incompletos o inválidos para el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Obtener detalles adicionales del producto
        Map<String, String> productoDetalle = obtenerDetalleProducto(codigobarras);
        if (productoDetalle == null || productoDetalle.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró el detalle del producto con código de barras: " + codigobarras, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el stock del producto
        int stock = obtenerStockProducto(codigobarras);
        if (stock <= 0) {
            JOptionPane.showMessageDialog(null, "El producto seleccionado no tiene stock disponible.", "Sin stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) jtdetalle.getModel();

        // Verificar si el código de barras ya existe en la tabla
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingCodigobarras = model.getValueAt(i, 0).toString();
            if (existingCodigobarras.equals(codigobarras)) {
                JOptionPane.showMessageDialog(null, "El producto ya existe en la tabla.", "Producto duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Detener la edición de la celda si está activa
        if (jtdetalle.isEditing()) {
            jtdetalle.getCellEditor().stopCellEditing();
        }

        // Llenar los campos de la fila seleccionada
        model.setValueAt(codigobarras, row, 0); // Código de barras
        model.setValueAt(descripcion, row, 1);   // Descripción
        model.setValueAt(stock, row, 2);         // Stock


        // Depuración: Imprimir el estado de la fila después de la selección
        System.out.println("Fila " + row + " después de la selección:");
        for (int i = 0; i < jtdetalle.getColumnCount(); i++) {
            System.out.print(jtdetalle.getValueAt(row, i) + " ");
        }
        System.out.println();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al procesar la selección: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel2 = new javax.swing.JPanel();
        jSpinField1 = new com.toedter.components.JSpinField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtdetalle = new javax.swing.JTable();
        lblId = new javax.swing.JLabel();
        lblMotivo = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblDeposito = new javax.swing.JLabel();
        aprobado = new javax.swing.JCheckBox();
        contabilizado = new javax.swing.JCheckBox();
        cboxdeposito = new javax.swing.JComboBox<>();
        txtId = new javax.swing.JTextField();
        lblId1 = new javax.swing.JLabel();
        Fecha = new com.toedter.calendar.JDateChooser();
        txtmotivo = new javax.swing.JTextField();
        Ultimo = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setTitle("Ajuste Stock");
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

        jtdetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cod Barra", "Descripcion", "Cant. Actual", "Cant. Ajustes"
            }
        ));
        jtdetalle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtdetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtdetalleFocusGained(evt);
            }
        });
        jtdetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtdetalleMousePressed(evt);
            }
        });
        jtdetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtdetalleKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtdetalle);

        lblId.setText("Último");

        lblMotivo.setText("Motivo");

        lblFecha.setText("Fecha");

        lblDeposito.setText("Deposito");

        aprobado.setText("Aprobado");
        aprobado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aprobadoFocusGained(evt);
            }
        });

        contabilizado.setText("Contabilizado");
        contabilizado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contabilizadoFocusGained(evt);
            }
        });

        cboxdeposito.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboxdepositoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboxdepositoFocusLost(evt);
            }
        });
        cboxdeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxdepositoActionPerformed(evt);
            }
        });

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

        lblId1.setText("Id");

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblDeposito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboxdeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(lblId1)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtId)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblId)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblFecha)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(aprobado)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(contabilizado)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(lblMotivo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtmotivo))))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblId1)
                        .addComponent(lblId)
                        .addComponent(lblFecha)))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboxdeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aprobado)
                    .addComponent(contabilizado))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMotivo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(txtmotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated

    }//GEN-LAST:event_formInternalFrameActivated

    private void jtdetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtdetalleFocusGained

    }//GEN-LAST:event_jtdetalleFocusGained

    private void dateFechajustestockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFechajustestockActionPerformed

    }//GEN-LAST:event_dateFechajustestockActionPerformed

    private void cboxdepositoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxdepositoFocusGained

    }//GEN-LAST:event_cboxdepositoFocusGained

    private void cboxdepositoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxdepositoFocusLost

    }//GEN-LAST:event_cboxdepositoFocusLost

    private void aprobadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aprobadoFocusGained

    }//GEN-LAST:event_aprobadoFocusGained

    private void contabilizadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contabilizadoFocusGained

    }//GEN-LAST:event_contabilizadoFocusGained

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed

    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified

    }//GEN-LAST:event_formInternalFrameDeiconified

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified

    }//GEN-LAST:event_formInternalFrameIconified

    private void jtdetalleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtdetalleKeyReleased

    }//GEN-LAST:event_jtdetalleKeyReleased

    private void jtdetalleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtdetalleMousePressed
        if (evt.getClickCount() == 1) {
            this.validarCabecera();
        }

        if (evt.getClickCount() == 2) {
            Component aComp = jtdetalle.getEditorComponent();
            if (aComp instanceof JTextComponent) {
                ((JTextComponent) aComp).selectAll();
            }
        }
    }//GEN-LAST:event_jtdetalleMousePressed

    private void txtIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdFocusGained

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void cboxdepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxdepositoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxdepositoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JCheckBox aprobado;
    private javax.swing.JComboBox<String> cboxdeposito;
    private javax.swing.JCheckBox contabilizado;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.components.JSpinField jSpinField1;
    private javax.swing.JTable jtdetalle;
    private javax.swing.JLabel lblDeposito;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JLabel lblMotivo;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtmotivo;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    setData(); // Configurar datos de la cabecera y detalle

    // Validar movimientos posteriores
    List<String> codigosBarras = columnData.stream()
            .map(row -> row.get("productodetalle_id"))
            .collect(Collectors.toList());
    if (!validarMovimientosPosteriores(myData.get("fecha"), codigosBarras)) {
        JOptionPane.showMessageDialog(null, "Existen movimientos posteriores para los productos ajustados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    // Continuar con el guardado
    if (!guardarCabecera(Integer.parseInt(myData.get("id")))) {
        JOptionPane.showMessageDialog(null, "Error al guardar la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
    guardarDetalle(Integer.parseInt(myData.get("id")));
    JOptionPane.showMessageDialog(null, "Ajuste de stock guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    return 0;
}

private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    alCabecera.add(myData);

    // Respaldar el ID
    String idBackup = myData.get("id");

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));

    // Decidir entre crear o actualizar
    if (tc.searchListById(fields, where).isEmpty()) {
        int rows = tc.createReg(myData);
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al crear la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Restaurar el ID si fue eliminado
    if (!myData.containsKey("id")) {
        myData.put("id", idBackup);
    }
    return true;
}


private void guardarDetalle(int idCabecera) {
    try {
        System.out.println("GuardarDetalle - ID recibido: " + idCabecera);

        // Obtener detalles existentes
        Map<String, String> where = new HashMap<>();
        where.put("ajuste_id", String.valueOf(idCabecera));
        List<Map<String, String>> detallesExistentes = tcdet.searchListById(Map.of("*", "*"), where);
        Map<String, Map<String, String>> detallesExistentesMap = new HashMap<>();
        for (Map<String, String> detalle : detallesExistentes) {
            detallesExistentesMap.put(detalle.get("id"), detalle);
        }

        // Procesar detalles nuevos o actualizados
        for (Map<String, String> myRow : columnData) {
            myRow.put("ajuste_id", String.valueOf(idCabecera));
            String detalleId = myRow.get("id");

            if (!detallesExistentesMap.containsKey(detalleId)) {
                tcdet.createReg(myRow);
            } else {
                detallesExistentesMap.remove(detalleId);
                tcdet.updateReg(new ArrayList<>(List.of(myRow)));
            }
        }

        // Eliminar detalles sobrantes
        for (Map.Entry<String, Map<String, String>> entry : detallesExistentesMap.entrySet()) {
            tcdet.deleteReg(new ArrayList<>(List.of(entry.getValue())));
        }

        JOptionPane.showMessageDialog(this, "Detalles guardados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al guardar los detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
@Override
public int imBorrar(String crud) {
    setData(); // Actualiza los datos desde la vista

    // Verificar que el campo ID de la cabecera no esté vacío o inválido
    if (myData.get("id") == null || myData.get("id").isEmpty() || myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro válido para borrar.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que la cabecera existe en la base de datos
    Map<String, String> whereCabecera = new HashMap<>();
    whereCabecera.put("id", myData.get("id"));

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    List<Map<String, String>> cabeceraExistente = tc.searchListById(fields, whereCabecera);
    if (cabeceraExistente.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El ajuste seleccionado no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que hay detalles asociados a esta cabecera
    Map<String, String> whereDetalle = new HashMap<>();
    whereDetalle.put("ajuste_id", myData.get("id"));

    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, whereDetalle);
    if (detallesExistentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No existen detalles asociados a este ajuste.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    } else {
        System.out.println("Detalles encontrados para eliminar:");
        for (Map<String, String> detalle : detallesExistentes) {
            System.out.println("Detalle a eliminar: " + detalle);
        }
    }

    // Confirmar eliminación
    if (!confirmarBorrado()) {
        return -1;
    }

    // Verificar que no existen movimientos posteriores relacionados con los productos afectados
    List<String> codigosBarras = detallesExistentes.stream()
            .map(detalle -> detalle.get("productodetalle_id"))
            .collect(Collectors.toList());
    if (validarMovimientosPosteriores(myData.get("fecha"), codigosBarras)) {
        JOptionPane.showMessageDialog(this, "No se puede borrar el ajuste. Existen movimientos posteriores para los productos ajustados.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Eliminar detalles y cabecera
    try {
        boolean detallesEliminados = true;
        boolean cabeceraEliminada = false;

        // **Eliminar detalles relacionados**
        if (!detallesExistentes.isEmpty()) {
            for (Map<String, String> detalle : detallesExistentes) {
                ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
                alDetalle.add(detalle);
                int rowsDeleted = tcdet.deleteReg(alDetalle);
                if (rowsDeleted > 0) {
                    System.out.println("Detalle eliminado correctamente: " + detalle);
                } else {
                    System.err.println("Error al eliminar el detalle: " + detalle);
                    detallesEliminados = false;
                }
            }
        }

        // **Eliminar cabecera**
        ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
        alCabecera.add(myData);
        System.out.println("Cabecera a eliminar: " + myData);
        int rowsAffected = tc.deleteReg(alCabecera);
        if (rowsAffected > 0) {
            cabeceraEliminada = true;
            System.out.println("Cabecera eliminada correctamente: " + myData);
        } else {
            System.err.println("Error al eliminar la cabecera: " + myData);
        }

        // Confirmar eliminación
        if (cabeceraEliminada && detallesEliminados) {
            JOptionPane.showMessageDialog(this, "Ajuste eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            imNuevo(); // Reinicia la vista
            return 0;
        } else {
            throw new Exception("Error durante la eliminación.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al borrar el ajuste: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
}

private boolean confirmarBorrado() {
    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea borrar este ajuste?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
    return confirm == JOptionPane.YES_OPTION;
}


    @Override
    public int imNuevo() {
    resetData();
    cargarUltimoId();
    return 0;
    }

  @Override
public int imBuscar() {
    setData(); // Actualiza los datos desde la vista

    System.out.println("Datos después de setData: " + myData);

    // Verificar si el ID es válido
    if (myData.get("id") == null || myData.get("id").isEmpty() || myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "El ID 0 no es válido. Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        resetData();
        limpiarTabla();
        imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de error
    }

    System.out.println("ImBuscar - ID antes de búsqueda: " + myData.get("id"));

    // Realizar la búsqueda de la cabecera
    Map<String, String> resultadoCabecera = tc.searchById(myData);
    System.out.println("Resultado de cabecera encontrado: " + resultadoCabecera);

    // Limpiar la tabla
    limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualizar myData con los resultados de la cabecera
     this.myData = resultadoCabecera;
    System.out.println("Datos de cabecera actualizados: " + myData);

    // Preparar los criterios de búsqueda para los detalles
    Map<String, String> whereDetalle = new HashMap<>();
    whereDetalle.put("ajuste_id", myData.get("id"));

    // Definir los campos a recuperar
    Map<String, String> fieldsDetalle = new HashMap<>();
    fieldsDetalle.put("*", "*");

    // Realizar la búsqueda de los detalles
    List<Map<String, String>> detalles = tcdet.searchListById(fieldsDetalle, whereDetalle);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el ajuste especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return -1; // Indicador de que no se encontraron detalles
    }

    // Convertir los detalles a columnData
    columnData = new ArrayList<>(detalles);

    // Enriquecer los detalles con información adicional (como producto y stock actual)
    for (Map<String, String> detalle : columnData) {
        String codBarra = detalle.get("productodetalle_id");

        if (codBarra != null && !codBarra.isEmpty()) {
            // Obtener detalles del producto
            Map<String, String> whereProductoDetalle = new HashMap<>();
            whereProductoDetalle.put("codigobarras", codBarra);

            Map<String, String> fieldsProductoDetalle = new HashMap<>();
            fieldsProductoDetalle.put("cabecera_id", "cabecera_id");

            List<Map<String, String>> resultProductoDetalle = tproductosdet.searchListById(fieldsProductoDetalle, whereProductoDetalle);
            if (!resultProductoDetalle.isEmpty()) {
                String productoId = resultProductoDetalle.get(0).get("cabecera_id");

                Map<String, String> whereProducto = new HashMap<>();
                whereProducto.put("id", productoId);

                Map<String, String> fieldsProducto = new HashMap<>();
                fieldsProducto.put("producto", "producto");

                List<Map<String, String>> resultProducto = tproductos.searchListById(fieldsProducto, whereProducto);
                if (!resultProducto.isEmpty()) {
                    Map<String, String> producto = resultProducto.get(0);
                    detalle.put("producto", producto.get("producto"));
                }
            }
        }
    }

    // Llenar la vista con los datos recuperados
    fillView(myData, columnData);

    return 0; // Indicador de que la búsqueda fue exitosa
}


    @Override
    public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

    if (lastFocusedComponent == txtId) {
        List<String> columnasParaCompra = Arrays.asList("id", "motivo");
        Form_Buscar buscadorCompra = new Form_Buscar(parentFrame, true, tc, "AJUSTES_STOCK", columnasParaCompra);
        buscadorCompra.setOnItemSeleccionadoListener(this);
        buscadorCompra.setVisible(true);
    } else if (lastFocusedComponent == jtdetalle) {
        int selectedRow = jtdetalle.getSelectedRow();
        int selectedColumn = jtdetalle.getSelectedColumn();
        if (selectedRow != -1 && selectedColumn == 0) { // Verificar si la columna es la de Cod Barra
            List<String> columnasParaProductos = Arrays.asList("codigobarras", "producto");
            Form_BuscarTabla buscadorProductos = new Form_BuscarTabla(parentFrame, true, tproductosdet, "productos_detalle", columnasParaProductos);
            buscadorProductos.setOnItemSeleccionadoListener2(new InterfaceUsuario() {
                @Override
                public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
                    procesarSeleccion(datosSeleccionados, selectedRow);
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
                public int imDelFilas() { return 0; }
            });
            buscadorProductos.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila de la tabla de detalles y asegúrese de que la columna 'Cod Barra' esté seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "No se ha seleccionado un campo válido para la búsqueda.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    return 0;
    }

 @Override
public int imPrimero() {
    this.myData = this.tc.navegationReg(txtId.getText(), "FIRST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    this.fillView(myData, columnData);
    imBuscar();
    return 0;
    
}

@Override
public int imSiguiente() {
    if (txtId.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(txtId.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData);
    imBuscar();
    return 0;
}

@Override
public int imAnterior() {
    if (txtId.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(txtId.getText(), "PRIOR");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros en esta dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData);
    imBuscar();
    return 0;
}

@Override
public int imUltimo() {
    this.myData = this.tc.navegationReg(txtId.getText(), "LAST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    this.fillView(myData, columnData);
    imBuscar();
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
                job.setJobName("Ajustes Stock");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

@Override
public int imInsFilas() {
    int currentRow = jtdetalle.getSelectedRow();

    if (currentRow == -1) {
        // No hay fila seleccionada, verificar la última fila antes de agregar una nueva
        int lastRow = jtdetalle.getRowCount() - 1;
        if (lastRow >= 0) {
            // Obtener los datos de la última fila
            Map<String, String> rowData = new HashMap<>();
            rowData.put("Cod Barra", jtdetalle.getValueAt(lastRow, 0).toString());
            rowData.put("Descripcion", jtdetalle.getValueAt(lastRow, 1).toString());
            rowData.put("Cant. Actual", jtdetalle.getValueAt(lastRow, 2).toString());
            rowData.put("Cant. Ajuste", jtdetalle.getValueAt(lastRow, 3).toString());

            if (isRowInvalid(rowData)) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles correctamente antes de añadir una nueva fila.", "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return -1;
            }
        }

        // Agregar una nueva fila
        modelo.addRow(new Object[]{"0", "0", "0", "0"});
        currentRow = jtdetalle.getRowCount() - 1; // Seleccionar la nueva fila
        jtdetalle.setRowSelectionInterval(currentRow, currentRow);
        return 0; // Se ha añadido una nueva fila
    }

    // Obtener los valores de la fila actual
    Map<String, String> rowData = new HashMap<>();
    rowData.put("Cod Barra", jtdetalle.getValueAt(currentRow, 0).toString());
    rowData.put("Descripcion", jtdetalle.getValueAt(currentRow, 1).toString());
    rowData.put("Cant. Actual", jtdetalle.getValueAt(currentRow, 2).toString());
    rowData.put("Cant. Ajuste", jtdetalle.getValueAt(currentRow, 3).toString());

    // Verificar si la fila actual es válida
    if (isRowInvalid(rowData)) {
        String msg = "Debe ingresar los detalles correctamente antes de añadir una nueva fila.";
        System.out.println(msg);
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
    } else {
        // Si los detalles son válidos, agregar una nueva fila
        modelo.addRow(new Object[]{"0", "0", "0", "0"});

        // Devuelve el foco a la tabla
        this.jtdetalle.requestFocus();

        // Selecciona la última fila de la tabla y la primera columna de esa fila
        int toRow = this.jtdetalle.getRowCount() - 1;
        this.jtdetalle.changeSelection(toRow, 0, false, false);
    }
    return 0;
}

// Método para verificar si una fila es inválida
private boolean isRowInvalid(Map<String, String> row) {
    return row.values().stream().allMatch(value -> value.equals("0") || value.isEmpty());
}


    @Override
    public int imDelFilas() {
               int selectedRow = jtdetalle.getSelectedRow();

        if (selectedRow != -1) {
            ((DefaultTableModel) this.jtdetalle.getModel()).removeRow(selectedRow);

            int rowCount = jtdetalle.getRowCount();
            if (rowCount > 0) {
                jtdetalle.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna fila seleccionada para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        return 0;
    }

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    if (lastFocusedComponent == txtId) {
        String idStr = datosSeleccionados.get("Codigo");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idAjuste = Integer.parseInt(idStr);
                List<Map<String, String>> registrosCabecera = tc.buscarPorIdGenerico("AJUSTES_STOCK", "id", idAjuste);

                if (!registrosCabecera.isEmpty()) {
                    Map<String, String> registroCabecera = registrosCabecera.get(0);

                    int depositoId = Integer.parseInt(registroCabecera.get("deposito_id"));
                    List<Map<String, String>> registrosDetalle = tc.buscarPorIdGenerico("AJUSTES_STOCK_DETALLE", "ajuste_id", idAjuste);

                    SwingUtilities.invokeLater(() -> {
                        // Llenar la cabecera
                        txtId.setText(idStr);
                        Fecha.setDate(java.sql.Date.valueOf(registroCabecera.get("fecha")));
                        aprobado.setSelected("1".equals(registroCabecera.get("aprobado")));
                        contabilizado.setSelected("1".equals(registroCabecera.get("contabilizado")));
                        Functions.E_estado(cboxdeposito, "DEPOSITOS", "id=" + depositoId);

                        // Llenar la tabla de detalles
                        DefaultTableModel model = (DefaultTableModel) jtdetalle.getModel();
                        model.setRowCount(0); // Limpiar tabla

                        for (Map<String, String> detalle : registrosDetalle) {
                            String codBarras = detalle.get("productoDetalle_id");
                            int cantActual = Integer.parseInt(detalle.get("cantidad_actual"));
                            int cantAjuste = Integer.parseInt(detalle.get("cantidad_ajuste"));

                            // Obtener descripción del producto
                            Map<String, String> whereProducto = new HashMap<>();
                            whereProducto.put("codigobarras", codBarras);

                            Map<String, String> fieldsProducto = new HashMap<>();
                            fieldsProducto.put("producto", "producto");

                            List<Map<String, String>> resultProducto = tproductosdet.searchListById(fieldsProducto, whereProducto);
                            String descripcion = resultProducto.isEmpty() ? "Desconocido" : resultProducto.get(0).get("producto");

                            model.addRow(new Object[]{codBarras, descripcion, cantActual, cantAjuste});
                        }
                    });

                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró un Ajuste con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    resetData();
                    limpiarTabla();
                    imInsFilas();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID de Ajuste inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    @Override
    public void mouseClicked(MouseEvent e) {
            int fila = jtdetalle.rowAtPoint(e.getPoint());
    int columna = jtdetalle.columnAtPoint(e.getPoint());
    if (columna == 0 || columna == 3) {
        jtdetalle.setColumnSelectionInterval(columna, columna);
        jtdetalle.editCellAt(fila, columna);
    }
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
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }


}