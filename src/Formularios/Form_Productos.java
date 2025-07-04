
package Formularios;

import java.text.DecimalFormat;
import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.CodigoBarraFilter;
import Filtros.DecimalDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import Modelo.ProductoDetalle;
import Modelo.cargaComboBox;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.*;


public class Form_Productos extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario  {
private Map<String, String> myData;
    private HashMap<String, String> myDet;
    private DBTableController tc;
    private DBTableController tm;
    private DBTableController tcdet;
    
    ArrayList<ProductoDetalle> lista;// = new ArrayList<>();
    
    ArrayList<ProductoDetalle> listaDetalles;//lista que simula la información de la BD
    JComboBox jcbColor;
    JComboBox jcbTamano;
    JComboBox jcbDiseno;
    ModeloTabla modelo;//modelo definido en la clase ModeloTabla
    private int filasTabla;
    private int columnasTabla;
    public static int filaSeleccionada;
    
    private ArrayList<Map<String,String>> columnData, colDat;
    
    private DBTableModel tMProducto;
    Map<String, String> mapProductos;// = new HashMap<String, String>();
    
    private DBTableModel tmProductoDet;
    Map<String, String> mapProductoDet;
    
    
    public Form_Productos() {
        initComponents();
        IdProducto.setText("0");
        impuesto.setText("10");
      listaDetalles = new ArrayList<ProductoDetalle>();
        lista = new ArrayList<>();
        myData = new HashMap<String, String>();
        columnData = new ArrayList<Map<String,String>>();
        colDat = new ArrayList<Map<String,String>>();
        jcbColor = new JComboBox();
        jcbTamano = new JComboBox();
        jcbDiseno = new JComboBox();
         // COMBO BOX DESPLEGABLES DE LAS TABLAS//
        cargaComboBox.pv_cargar(categoria, "CATEGORIAS", "id, categoria", "id", "");
        cargaComboBox.pv_cargar(marca, "MARCAS", " id, marca", "id", "");   
        cargaComboBox.pv_cargar(jcbColor, "COLORES", "id, color", "id", "");
        cargaComboBox.pv_cargar(jcbTamano, "TAMANOS", "id, tamano", "id", "");
        cargaComboBox.pv_cargar(jcbDiseno, "DISENOS", "id, diseno", "id", "");
        

        tc = new DBTableController();
        tc.iniciar("PRODUCTOS");
        tcdet = new DBTableController();
        tcdet.iniciar("PRODUCTOS_DETALLE");
        tm = new DBTableController();
        tm.iniciar("MONEDAS");
        
    // Obtener el ID de la moneda "Guaraní"
    int idGuarani = obtenerIdGuarani();
    if (idGuarani != -1) {
        setMonedaPredeterminada("Guaraní", idGuarani);
    } else {
        JOptionPane.showMessageDialog(this, "No se encontró el ID de la moneda Guaraní.", "Error", JOptionPane.ERROR_MESSAGE);
    }
           //PARA EL DETALLE
        
        mapProductoDet = new HashMap<String, String>();
        tmProductoDet = new DBTableModel();
        tmProductoDet.iniciar("PRODUCTOS_DETALLE");
        //setLocationRelativeTo(null);
        cargarUltimoId();
        construirTabla();
        initializeTextFields();
        
        jtDetalle.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcbColor));
        jtDetalle.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jcbTamano));
        jtDetalle.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcbDiseno));
        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "selectNextColumnCell");
        applyNumericFilterToColumns(); 
    }
private void setMonedaPredeterminada(String nombreMoneda, int idMoneda) {
    Moneda.setText(nombreMoneda); // Muestra el nombre de la moneda
    // Guarda el ID de la moneda en el mapa de datos
    myData.put("moneda_id", String.valueOf(idMoneda));
}

private void applyNumericFilterToColumns() {
    int decimalesGuarani = obtenerDecimalesGuarani();

    // Aplicar filtro de código de barras a la columna 0
    JTextField barcodeField = new JTextField();
    ((AbstractDocument) barcodeField.getDocument()).setDocumentFilter(new CodigoBarraFilter());
    jtDetalle.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(barcodeField));

    for (int i = 4; i <= 6; i++) { // Asumiendo que UxB es la columna 4, Costo es la columna 5, y Stock es la columna 6
        JTextField textField = new JTextField();
        if (decimalesGuarani == 0) {
            // Filtro para valores enteros si la moneda es Guaraní y no tiene decimales
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        } else {
            // Filtro para valores decimales con el número de decimales especificado
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalesGuarani));
        }
        jtDetalle.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(textField));
    }
}


private int obtenerDecimalesGuarani() {
    int decimales = 0; // Asumimos que "Guaraní" no tiene decimales
    try {
        Map<String, String> where = new HashMap<>();
        where.put("moneda", "Guaraní");
        Map<String, String> fields = new HashMap<>();
        fields.put("decimales", "decimales");
        List<Map<String, String>> result = tm.searchListById(fields, where);
        if (!result.isEmpty()) {
            decimales = Integer.parseInt(result.get(0).get("decimales"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decimales;
}

    private void construirTabla() {
        listaDetalles = consultarListaDetalles();
        ArrayList<String> titulosList = new ArrayList<>();

     titulosList.add("Cod Barra");
    titulosList.add("Color");
    titulosList.add("Tamaño");
    titulosList.add("Diseño");
    titulosList.add("UxB");
    titulosList.add("Costo"); 
    titulosList.add("Stock"); 
     titulosList.add("Id");


        String titulos[] = new String[titulosList.size()];
        
        for (int i = 0; i < titulos.length; i++) {
            titulos[i] = titulosList.get(i);
        }

        Object[][] data = obtenerMatrizDatos(titulosList);
        construirTabla(titulos, data);
    }
    
    private ArrayList<ProductoDetalle> consultarListaDetalles() {
 this.lista.add(new ProductoDetalle(1,0, "0", 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        return lista;
    }
    
    private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
    String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

    for (int x = 0; x < informacion.length; x++) {
        informacion[x][0] = listaDetalles.get(x).getString("codigobarras");
        informacion[x][1] = listaDetalles.get(x).getInteger("color_id")+ "";
        informacion[x][2] = listaDetalles.get(x).getInteger("tamano_id")+ "";
        informacion[x][3] = listaDetalles.get(x).getInteger("diseno_id")+ "";
        informacion[x][4] = listaDetalles.get(x).getInteger("uxb")+ "";
        informacion[x][5] = listaDetalles.get(x).getBigDecimal("costo")+ "";
        informacion[x][6] = listaDetalles.get(x).getBigDecimal("stockminimo")+ "";
        informacion[x][7] = listaDetalles.get(x).getInteger("id")+ "";
    }
    return informacion;
}
 private void construirTabla(String[] titulos, Object[][] data) {
    ArrayList<Integer> noEditable = new ArrayList<Integer>();
    modelo = new ModeloTabla(data, titulos, noEditable);
    jtDetalle.setModel(modelo);

    filasTabla = jtDetalle.getRowCount();
    columnasTabla = jtDetalle.getColumnCount();

    // Asignar renderers y configurar columnas
    jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
    jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("jComboBox"));
    jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("jComboBox"));
    jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("jComboBox"));
    jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("jComboBox"));
    jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(6).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(7).setCellRenderer(new GestionCeldas("numerico"));

    // Ocultar la columna del ID
    jtDetalle.getColumnModel().getColumn(7).setMinWidth(0);
    jtDetalle.getColumnModel().getColumn(7).setMaxWidth(0);
    jtDetalle.getColumnModel().getColumn(7).setWidth(0);
    jtDetalle.getColumnModel().getColumn(7).setPreferredWidth(0);

    jtDetalle.getTableHeader().setReorderingAllowed(false);
    jtDetalle.setRowHeight(25);
    jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));

    // Ajustar el ancho de las columnas visibles
    jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(200); // cod_barra
    jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(300); // color
    jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(300); // tamaño
    jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(300); // diseño
    jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(150); // costo
    jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(150); // UxB
    jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(150); // stock min

    // Personalizar el encabezado
    JTableHeader jtableHeader = jtDetalle.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    jtDetalle.setTableHeader(jtableHeader);
}

private void setData() {
    // Cabecera
    myData.put("id", IdProducto.getText());
    myData.put("producto", producto.getText());
    myData.put("marca_id", Functions.ExtraeCodigo(marca.getSelectedItem().toString()));
    myData.put("categoria_id", Functions.ExtraeCodigo(categoria.getSelectedItem().toString()));
    myData.put("impuesto", impuesto.getText());
    
    myData.put("servicio", servicio.isSelected() ? "1" : "0");
    myData.put("activo", activo.isSelected() ? "1" : "0");
    myData.put("perecedero", Perecedero.isSelected() ? "1" : "0");
    myData.put("pesable", Pesable.isSelected() ? "1" : "0");

    // Obtener y almacenar el ID de la moneda
    int idMoneda = obtenerIdGuarani();
    if (idMoneda != -1) {
        myData.put("moneda_id", String.valueOf(idMoneda));
    } else {
        JOptionPane.showMessageDialog(this, "No se encontró el ID de la moneda Guaraní.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Obtener el último ID existente en la tabla PRODUCTOS_DETALLE
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int filasTabla = model.getRowCount();
    columnData.clear(); // Limpiar los datos anteriores

    int ultimoId = obtenerUltimoIdProductoDetalle(); // Obtener el último ID existente en la tabla PRODUCTOS_DETALLE

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Obtener el ID actual de la fila si existe
        String idDetalle;
        Object idCell = model.getValueAt(i, 7); // Suponiendo que el ID está en la columna índice 7
        if (idCell == null || idCell.toString().trim().isEmpty() || idCell.toString().trim().equals("0")) {
            // Generar un nuevo ID si no existe
            idDetalle = String.valueOf(++ultimoId);
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

        // Verificar y convertir los valores antes de agregar a columnData
        String codigo_barra = rowData.get("Cod Barra");
        if (codigo_barra.equals("0")) {
            continue;
        }
        
        try {
            // Convertir y verificar valores
            Integer color = Integer.parseInt(Functions.ExtraeCodigo(rowData.get("Color")));
            Integer tamano = Integer.parseInt(Functions.ExtraeCodigo(rowData.get("Tamaño")));
            Integer diseno = Integer.parseInt(Functions.ExtraeCodigo(rowData.get("Diseño")));
            BigDecimal uxb = new BigDecimal(rowData.get("UxB"));
            BigDecimal costo = new BigDecimal(rowData.get("Costo"));
            BigDecimal stock = new BigDecimal(rowData.get("Stock"));

            // Crear el mapa de detalles
            myDet = new HashMap<>();
            String id = IdProducto.getText();
            myDet.put("id", idDetalle);
            myDet.put("cabecera_id", id);
            myDet.put("codigobarras", codigo_barra);
            myDet.put("color_id", color.toString());
            myDet.put("tamano_id", tamano.toString());
            myDet.put("diseno_id", diseno.toString());
            myDet.put("uxb", uxb.toString());
            myDet.put("costo", costo.toString());
            myDet.put("stockminimo", stock.toString());

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

private int obtenerIdGuarani() {
    int idGuarani = -1;
    try {
        // Suponiendo que tc es un controlador para la base de datos
        Map<String, String> where = new HashMap<>();
        where.put("moneda", "Guaraní");
        Map<String, String> fields = new HashMap<>();
        fields.put("id", "id");
        List<Map<String, String>> result = tm.searchListById(fields, where);
        if (!result.isEmpty()) {
            idGuarani = Integer.parseInt(result.get(0).get("id"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return idGuarani;
}

// Método para obtener el último ID existente en la tabla PRODUCTOS_DETALLE
private int obtenerUltimoIdProductoDetalle() {
    int ultimoId = 0;
        try {
             ultimoId = tcdet.getMaxId();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return ultimoId;
}


private void resetData() {        
    this.myData = new HashMap<String, String>();
    this.myData.put("id", "0");
    this.myData.put("producto", "");
    this.myData.put("marca_id", "0");
    this.myData.put("categoria_id", "0");
    this.myData.put("impuesto", "10");   
    this.myData.put("servicio", "0");
    this.myData.put("activo", "0");
    this.myData.put("perecedero", "0");
    this.myData.put("pesable", "0");
   // Restablecer la moneda a "Guaraní"
    Moneda.setText("Guaraní");
    int idGuarani = obtenerIdGuarani();
    if (idGuarani != -1) {
        this.myData.put("moneda_id", String.valueOf(idGuarani));
    }
    
    // Limpiar los comboboxes
    marca.setSelectedIndex(0);
    categoria.setSelectedIndex(0);

    // Detalle
    this.myDet = new HashMap<String, String>();
    this.myDet.put("id", "0");
    this.myDet.put("cabecera_id", "0");
    this.myDet.put("codigobarras", "0");
    this.myDet.put("color_id", "0");
    this.myDet.put("tamano_id", "0");
    this.myDet.put("diseno_id", "0");
    this.myDet.put("uxb", "0");
    this.myDet.put("costo", "0");
    this.myDet.put("stockminimo", "0");
      
    this.columnData.add(this.myDet);
            
    fillView(myData, columnData);
}

private void fillView(Map<String, String> data, List<Map<String, String>> colData) {
    // Obtener los decimales de la moneda
    int decimalesGuarani = obtenerDecimalesGuarani();
    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setMaximumFractionDigits(decimalesGuarani);
    decimalFormat.setMinimumFractionDigits(decimalesGuarani);

    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        switch (key) {
            case "id":
                IdProducto.setText(value);
                break;
            case "producto":
                producto.setText(value);
                break;
            case "marca_id":
                Functions.E_estado(marca, "MARCAS", "id=" + value);
                break;
            case "categoria_id":
                Functions.E_estado(categoria, "CATEGORIAS", "id=" + value);
                break;
            case "impuesto":
                impuesto.setText(value);
                break;
            case "servicio":
                servicio.setSelected(Integer.parseInt(value) != 0);
                break;
            case "activo":
                activo.setSelected(Integer.parseInt(value) != 0);
                break;
            case "perecedero":
                Perecedero.setSelected(Integer.parseInt(value) != 0);
                break;
            case "pesable":
                Pesable.setSelected(Integer.parseInt(value) != 0);
                break;
            case "moneda_id":
                Moneda.setText("Guaraní");
                break;
        }
    }

    // Detalle
    int row = 0;
    for (Map<String, String> myRow : colData) {
        JComboBox jcbColor = (JComboBox) jtDetalle.getCellEditor(row, 1).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 1);
        Functions.E_estado(jcbColor, "COLORES", "id=" + myRow.get("color_id"));

        JComboBox jcbTamano = (JComboBox) jtDetalle.getCellEditor(row, 2).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 2);
        Functions.E_estado(jcbTamano, "TAMANOS", "id=" + myRow.get("tamano_id"));

        JComboBox jcbDiseno = (JComboBox) jtDetalle.getCellEditor(row, 3).getTableCellEditorComponent(jtDetalle, "0-Seleccionar", true, row, 3);
        Functions.E_estado(jcbDiseno, "DISENOS", "id=" + myRow.get("diseno_id"));

        String costoFormateado = formatearNumero(myRow.get("costo"), decimalesGuarani);
        String uxbFormateado = formatearNumero(myRow.get("uxb"), decimalesGuarani);
        String stockFormateado = formatearNumero(myRow.get("stockminimo"), decimalesGuarani);

        this.modelo.addRow(new Object[]{
            myRow.get("codigobarras"),
            jcbColor.getSelectedItem(),
            jcbTamano.getSelectedItem(),
            jcbDiseno.getSelectedItem(),
            uxbFormateado,
            costoFormateado,
            stockFormateado,
            myRow.get("id")
        });

        this.jtDetalle.getSelectionModel().setSelectionInterval(row, 0);
        row++;
    }
}

private String formatearNumero(String valor, int decimales) {
    BigDecimal numero = new BigDecimal(valor);
    if (decimales == 0) {
        return numero.toBigInteger().toString();
    }
    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setMaximumFractionDigits(decimales);
    decimalFormat.setMinimumFractionDigits(decimales);
    return decimalFormat.format(numero);
}


     public void limpiarTabla(){
        this.columnData.clear();
        try {
            DefaultTableModel modelo = (DefaultTableModel)jtDetalle.getModel();
            int filas = jtDetalle.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }//fin limpiarTabla

    private void initializeTextFields() {
        applyNumericFilter(IdProducto);
        applyNumericFilter(impuesto);
        applyAlphaFilter(producto);
        addFocusListeners();
    }

    private void addFocusListeners() {
        IdProducto.addFocusListener(new DefaultFocusListener(IdProducto, true));
        producto.addFocusListener(new DefaultFocusListener(producto, false));
        impuesto.addFocusListener(new DefaultFocusListener(impuesto, true));
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondo = new javax.swing.JPanel();
        lbl_id = new javax.swing.JLabel();
        IdProducto = new javax.swing.JTextField();
        lbl_moneda = new javax.swing.JLabel();
        producto = new javax.swing.JTextField();
        lbl_Ultimo = new javax.swing.JLabel();
        Moneda = new javax.swing.JLabel();
        marca = new javax.swing.JComboBox<>();
        lbl_Marca = new javax.swing.JLabel();
        lbl_categoria = new javax.swing.JLabel();
        categoria = new javax.swing.JComboBox<>();
        servicio = new javax.swing.JCheckBox();
        activo = new javax.swing.JCheckBox();
        impuesto = new javax.swing.JTextField();
        lbl_impuesto = new javax.swing.JLabel();
        Perecedero = new javax.swing.JCheckBox();
        Pesable = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();
        Ultimo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Registrar Productos");
        setPreferredSize(new java.awt.Dimension(805, 390));

        Fondo.setFocusable(false);
        Fondo.setMinimumSize(new java.awt.Dimension(690, 340));
        Fondo.setPreferredSize(new java.awt.Dimension(800, 360));
        Fondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_id.setText("Id");
        Fondo.add(lbl_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 10, -1));

        IdProducto.setText("0");
        IdProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                IdProductoFocusGained(evt);
            }
        });
        IdProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdProductoActionPerformed(evt);
            }
        });
        IdProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdProductoKeyPressed(evt);
            }
        });
        Fondo.add(IdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, -1));

        lbl_moneda.setText("Moneda");
        Fondo.add(lbl_moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 50, 20));
        Fondo.add(producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 360, -1));

        lbl_Ultimo.setText("Último");
        Fondo.add(lbl_Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, -1, 20));

        Moneda.setBackground(new java.awt.Color(204, 204, 255));
        Moneda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Moneda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Moneda.setOpaque(true);
        Fondo.add(Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 140, 22));

        Fondo.add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 250, -1));

        lbl_Marca.setText("Marca");
        Fondo.add(lbl_Marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, -1, -1));

        lbl_categoria.setText("Categoría");
        Fondo.add(lbl_categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, -1, -1));

        Fondo.add(categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 70, 250, -1));

        servicio.setText("Servicio");
        Fondo.add(servicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, -1, -1));

        activo.setText("Activo");
        Fondo.add(activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, -1, -1));

        impuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                impuestoActionPerformed(evt);
            }
        });
        Fondo.add(impuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 70, -1));

        lbl_impuesto.setText("Impuesto");
        Fondo.add(lbl_impuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 60, 20));

        Perecedero.setText("Perecedero");
        Fondo.add(Perecedero, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 110, -1, -1));

        Pesable.setText("Pesable");
        Fondo.add(Pesable, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, -1, -1));

        jLabel2.setText("%");
        Fondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 20, 20));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));

        jtDetalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        jtDetalle.setRowSelectionAllowed(false);
        jtDetalle.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jtDetalle.setShowGrid(true);
        jScrollPane1.setViewportView(jtDetalle);

        Fondo.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 780, 220));

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);
        Fondo.add(Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 60, 22));

        jLabel3.setText("Producto");
        Fondo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 50, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdProductoActionPerformed

    private void IdProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdProductoKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }   
    }//GEN-LAST:event_IdProductoKeyPressed

    private void IdProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdProductoFocusGained
        IdProducto.selectAll(); 
    }//GEN-LAST:event_IdProductoFocusGained

    private void impuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_impuestoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JTextField IdProducto;
    private javax.swing.JLabel Moneda;
    private javax.swing.JCheckBox Perecedero;
    private javax.swing.JCheckBox Pesable;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JCheckBox activo;
    private javax.swing.JComboBox<String> categoria;
    private javax.swing.JTextField impuesto;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JLabel lbl_Marca;
    private javax.swing.JLabel lbl_Ultimo;
    private javax.swing.JLabel lbl_categoria;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_impuesto;
    private javax.swing.JLabel lbl_moneda;
    private javax.swing.JComboBox<String> marca;
    private javax.swing.JTextField producto;
    private javax.swing.JCheckBox servicio;
    // End of variables declaration//GEN-END:variables



@Override
public void mouseClicked(MouseEvent e) {
    int fila = jtDetalle.rowAtPoint(e.getPoint());
    int columna = jtDetalle.columnAtPoint(e.getPoint());
    if (columna == 0 || columna == 5 || columna == 6 || columna == 7) {
        jtDetalle.setColumnSelectionInterval(columna, columna);
        jtDetalle.editCellAt(fila, columna);
    }
}

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       //capturo fila o columna dependiendo de mi necesidad
        //OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
        int fila = jtDetalle.rowAtPoint(e.getPoint());
        int columna = jtDetalle.columnAtPoint(e.getPoint());

        /*uso la columna para valiar si corresponde a la columna de perfil garantizando
         * que solo se produzca algo si selecciono una fila de esa columna
         */
        if (columna == 0) { //0 corresponde a cod barra
            //sabiendo que corresponde a la columna de perfil, envio la posicion de la fila seleccionada
           // validarSeleccionMouse(fila);
        }else if (columna == 2){//se valida que sea la columna del otro evento 2 que corresponde a precio
            //JOptionPane.showMessageDialog(null, "Evento del otro icono");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

@Override
public void keyPressed(KeyEvent e) {
    int row = jtDetalle.getSelectedRow();
    int rows = jtDetalle.getRowCount();
    int col = jtDetalle.getSelectedColumn();

    int key = e.getKeyChar();

    boolean numeros = key >= 48 && key <= 57;
    boolean decimalPoint = key == 46;
    boolean erraser = key == 8;

    if (!numeros && !decimalPoint && !erraser && key != 10) {
        //e.consume();
    } else {
        if (numeros) {
            if (jtDetalle.getModel().isCellEditable(row, col)) {
                this.limpiarCelda(jtDetalle);
            }
        }
    }

    if (key == 10 || key == 9 || (key >= 37 && key <= 40)) { // 10 es enter
        if (jtDetalle.isEditing()) {
            jtDetalle.getCellEditor().stopCellEditing();
        }

        if (col == 0) {
            return;
        }

        if (col == 6 && key == 10 && (row == (rows - 1))) { // Si está en la última columna y presiona enter, inserta una nueva fila
            Map<String, String> rowData = new HashMap<>();
            rowData.put("codigobarras", this.jtDetalle.getValueAt(row, 0).toString());
            rowData.put("color", this.jtDetalle.getValueAt(row, 1).toString());
            rowData.put("tamano", this.jtDetalle.getValueAt(row, 2).toString());
            rowData.put("diseno", this.jtDetalle.getValueAt(row, 3).toString());
            rowData.put("uxb", this.jtDetalle.getValueAt(row, 4).toString());
            rowData.put("costo", this.jtDetalle.getValueAt(row, 5).toString());
            rowData.put("stockminimo", this.jtDetalle.getValueAt(row, 6).toString());

            if (isRowInvalid(rowData)) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.imInsFilas();
            }
        }
    }
}




    @Override
    public void keyReleased(KeyEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
@Override
public int imGuardar(String crud) {
    setData(); // Actualiza los datos de la vista

    boolean validacionFallo = false;

    // Verificar que las columnas obligatorias de la cabecera no estén vacías
    List<String> columnasObligatorias = Arrays.asList("producto", "marca_id", "categoria_id", "impuesto", "moneda_id");
    for (String columna : columnasObligatorias) {
        if (myData.get(columna).isEmpty() || marca.getSelectedIndex() == 0 || categoria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }

    // Verificar campo obligatorio ID para la cabecera
    int idCabecera = Integer.parseInt(myData.get("id"));
    if (idCabecera == 0 || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID de la cabecera no puede quedar vacío o ser cero.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que exista al menos un detalle
    if (columnData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe haber al menos un detalle.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que todos los detalles tengan campos obligatorios válidos (no vacíos ni cero)
    List<String> columnasObli = Arrays.asList("codigobarras", "stockminimo", "costo", "uxb", "color_id", "tamano_id", "diseno_id");
    boolean detalleInvalido = false;
    for (Map<String, String> myRow : columnData) {
        for (String columna : columnasObli) {
            String valor = myRow.get(columna);
            if (valor == null || valor.isEmpty() || (columna.endsWith("_id") && (valor.equals("0") || valor.startsWith("0-")))) {
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

    // Verificar si hay detalles con valores predefinidos de cero, excluyendo la columna de ID
    detalleInvalido = false;
    for (int rowIndex = 0; rowIndex < jtDetalle.getRowCount(); rowIndex++) {
        for (int columnIndex = 0; columnIndex < jtDetalle.getColumnCount(); columnIndex++) {
            // Suponiendo que la columna de ID es la columna índice 7
            if (columnIndex == 7) {
                continue; // Saltar la columna de ID
            }
            Object valor = jtDetalle.getValueAt(rowIndex, columnIndex);
            if (valor == null || valor.toString().isEmpty() || valor.toString().equals("0")) {
                detalleInvalido = true;
                break;
            }
        }
        if (detalleInvalido) {
            break; // Rompemos el bucle externo si se encuentra un detalle inválido
        }
    }

    if (detalleInvalido) {
        JOptionPane.showMessageDialog(this, "Todos los detalles deben tener campos obligatorios válidos (no vacíos y no ceros).", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que no haya códigos de barras duplicados en los detalles
    Set<String> codigosBarras = new HashSet<>();
    for (Map<String, String> myRow : columnData) {
        String codigoBarras = myRow.get("codigobarras");
        if (codigosBarras.contains(codigoBarras)) {
            JOptionPane.showMessageDialog(this, "El código de barras " + codigoBarras + " está duplicado en los detalles.", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
        codigosBarras.add(codigoBarras);
    }

    // Guardar la cabecera
    if (!guardarCabecera(idCabecera)) {
        System.out.println("Fallo al guardar la cabecera con ID: " + idCabecera);
        imNuevo();
        return -1; // Fallo al guardar la cabecera
    } else {
        System.out.println("Cabecera guardada exitosamente con ID: " + idCabecera);
    }

    // Ahora, procesamos el detalle
    Map<String, String> where = new HashMap<>();
    where.put("cabecera_id", String.valueOf(idCabecera));
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);

    // Crear un mapa de detalles existentes para facilitar la actualización
    Map<String, Map<String, String>> detallesExistentesMap = new HashMap<>();
    for (Map<String, String> detalle : detallesExistentes) {
        detallesExistentesMap.put(detalle.get("id"), detalle); // Usar ID del detalle para mapear detalles
    }

    for (Map<String, String> myRow : columnData) {
        myRow.put("cabecera_id", String.valueOf(idCabecera)); // Asignar el id de la cabecera correcto
        String detalleId = myRow.get("id");

        if (!detallesExistentesMap.containsKey(detalleId)) {
            // Si el detalle no existe, lo creamos
            tcdet.createReg(myRow);
            System.out.println("Detalle creado: " + myRow);
        } else {
            // Si el detalle existe, lo actualizamos
            detallesExistentesMap.remove(detalleId);
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(myRow);
            tcdet.updateReg(alDetalle);
            System.out.println("Detalle actualizado: " + myRow);
        }
    }

    // Eliminar detalles que ya no están presentes en columnData
    for (Map.Entry<String, Map<String, String>> entry : detallesExistentesMap.entrySet()) {
        ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
        alDetalle.add(entry.getValue());
        tcdet.deleteReg(alDetalle);
        System.out.println("Detalle eliminado: " + entry.getValue());
    }

    JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    imNuevo();
    return 0;
}

// Método para guardar la cabecera y devolver el éxito o fallo
private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    alCabecera.add(myData);
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));
    if (tc.searchListById(fields, where).isEmpty()) {
        // Si no existe, creamos un nuevo registro
        int rows = tc.createReg(myData);
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar crear el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        // Si existe, actualizamos el registro
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar el registro: " + idCabecera, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    return true;
}


@Override
public int imBorrar(String crud) {
    setData(); // Actualiza los datos de la vista

    // Verificar que las columnas obligatorias de la cabecera no estén vacías
    List<String> columnasObligatoriasCabecera = Arrays.asList("producto", "marca_id", "categoria_id", "impuesto");
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
        JOptionPane.showMessageDialog(this, "No se puede eliminar un producto sin detalles.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
    
// Verificar que todos los detalles tengan campos obligatorios válidos (no vacíos ni cero)
List<String> columnasObli = Arrays.asList("codigobarras", "stockminimo", "costo", "uxb", "color_id", "tamano_id", "diseno_id");
boolean detalleInvalido = false;
for (Map<String, String> myRow : columnData) {
    for (String columna : columnasObli) {
        String valor = myRow.get(columna);
        if (valor == null || valor.isEmpty() || valor.equals("0")||(columna.endsWith("_id") && (valor.equals("0") || valor.startsWith("0-")))) {
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
        JOptionPane.showMessageDialog(this, "El producto no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
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
    where.put("cabecera_id", myData.get("id"));
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
            String msg = "EL REGISTRO: " + IdProducto.getText() + " SE HA ELIMINADO CORRECTAMENTE";
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
    public int imNuevo() {
              this.resetData();
        this.limpiarTabla();
        this.imInsFilas();
        this.fillView(myData, columnData);
        cargarUltimoId();
        return 0;
    }

    @Override
public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaProductos = Arrays.asList("id", "producto");
    Form_Buscar buscadorProductos = new Form_Buscar(parentFrame, true, tc, "productos", columnasParaProductos);

    buscadorProductos.setOnItemSeleccionadoListener(this);
    buscadorProductos.setVisible(true);

    return 0;
}

@Override
public int imBuscar() {
    this.setData(); // Toma los datos de la vista

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
    System.out.println("PRODUCTOS imBuscar " + resultadoCabecera);

    // Limpia la tabla de la vista
    this.limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        System.out.println("No hay registros que mostrar");
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualiza myData con los resultados de la búsqueda de la cabecera
    this.myData = resultadoCabecera;

    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("cabecera_id", this.myData.get("id"));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el producto especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
public int imPrimero() {
    this.myData = this.tc.navegationReg(IdProducto.getText(), "FIRST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    this.fillView(this.myData, columnData);
    imBuscar();
    return 0;
}

@Override
public int imSiguiente() {
    if (IdProducto.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdProducto.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    
    this.limpiarTabla();
    this.fillView(this.myData, columnData);
    cargarDetallesProducto(Integer.parseInt(this.myData.get("id"))); // Cargar los detalles correspondientes
    return 0;
}

@Override
public int imAnterior() {
    this.myData = this.tc.navegationReg(IdProducto.getText(), "PRIOR");
    if (IdProducto.equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    else if(this.myData.isEmpty()){
            JOptionPane.showMessageDialog(this, "No hay registros en esta dirección.");
        return -1;
    }
    this.limpiarTabla();
    this.fillView(this.myData, columnData);
    imBuscar();
    return 0;
}

@Override
public int imUltimo() {
    this.myData = this.tc.navegationReg(IdProducto.getText(), "LAST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    this.fillView(this.myData, columnData);
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
                job.setJobName("Productos");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

@Override
public int imDelFilas() {
    int selectedRow = jtDetalle.getSelectedRow();

    // Verificar si hay una fila seleccionada
    if (selectedRow != -1) {
        ((DefaultTableModel) this.jtDetalle.getModel()).removeRow(selectedRow);

        // Seleccionar la última fila restante, si hay filas restantes
        int rowCount = jtDetalle.getRowCount();
        if (rowCount > 0) {
            jtDetalle.setRowSelectionInterval(rowCount - 1, rowCount - 1);
        }
    } else {
        // Manejar el caso donde no hay una fila seleccionada (opcional)
        JOptionPane.showMessageDialog(this, "No hay ninguna fila seleccionada para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    return 0;
}
@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    String idStr = datosSeleccionados.get("Codigo");
    String nombre = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idProducto = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("PRODUCTOS", "id", idProducto);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                SwingUtilities.invokeLater(() -> {
                    IdProducto.setText(idStr);
                    producto.setText(nombre);
                    impuesto.setText(registro.get("impuesto"));

                    Perecedero.setSelected(Integer.parseInt(registro.get("perecedero")) != 0);
                    Pesable.setSelected(Integer.parseInt(registro.get("pesable")) != 0);
                    servicio.setSelected(Integer.parseInt(registro.get("servicio")) != 0);
                    activo.setSelected(Integer.parseInt(registro.get("activo")) != 0);

                    // Asignar marca y categoría
                    String marcaId = registro.get("marca_id");
                    String categoriaId = registro.get("categoria_id");
                    Functions.E_estado(marca, "MARCAS", "id=" + marcaId);
                    Functions.E_estado(categoria, "CATEGORIAS", "id=" + categoriaId);
                });

                // Cargar detalles del producto
                cargarDetallesProducto(idProducto);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
        JOptionPane.showMessageDialog(null, "ID de producto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void cargarDetallesProducto(int idProducto) {
    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("cabecera_id", String.valueOf(idProducto));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el producto especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        limpiarTabla();
        imInsFilas(); // Añadir una fila vacía
    } else {
        columnData.clear();
        limpiarTabla();
        columnData.addAll(detalles);
        fillView(myData, columnData);
    }
}
@Override
public int imInsFilas() {
    int currentRow = jtDetalle.getSelectedRow();

    if (currentRow == -1) {
        // No hay fila seleccionada, verificar la última fila antes de agregar una nueva
        int lastRow = jtDetalle.getRowCount() - 1;
        if (lastRow >= 0) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("codigobarras", this.jtDetalle.getValueAt(lastRow, 0).toString());
            rowData.put("color", this.jtDetalle.getValueAt(lastRow, 1).toString());
            rowData.put("tamano", this.jtDetalle.getValueAt(lastRow, 2).toString());
            rowData.put("diseno", this.jtDetalle.getValueAt(lastRow, 3).toString());
            rowData.put("uxb", this.jtDetalle.getValueAt(lastRow, 4).toString());
            rowData.put("costo", this.jtDetalle.getValueAt(lastRow, 5).toString());
            rowData.put("stockminimo", this.jtDetalle.getValueAt(lastRow, 6).toString());

            if (isRowInvalid(rowData)) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.", "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return -1;
            }
        }
        modelo.addRow(new Object[]{"0", "0", "0", "0", "0", "0", "0"});
        currentRow = jtDetalle.getRowCount() - 1; // Seleccionar la nueva fila
        jtDetalle.setRowSelectionInterval(currentRow, currentRow);
        return 0; // Se ha añadido una nueva fila
    }

    // Obtener los valores de la fila actual
    Map<String, String> rowData = new HashMap<>();
    rowData.put("codigobarras", this.jtDetalle.getValueAt(currentRow, 0).toString());
    rowData.put("color", this.jtDetalle.getValueAt(currentRow, 1).toString());
    rowData.put("tamano", this.jtDetalle.getValueAt(currentRow, 2).toString());
    rowData.put("diseno", this.jtDetalle.getValueAt(currentRow, 3).toString());
    rowData.put("uxb", this.jtDetalle.getValueAt(currentRow, 4).toString());
    rowData.put("costo", this.jtDetalle.getValueAt(currentRow, 5).toString());
    rowData.put("stockminimo", this.jtDetalle.getValueAt(currentRow, 6).toString());

    // Verificar si la fila actual es válida
    if (isRowInvalid(rowData)) {
        // Si cualquier campo obligatorio es "0", vacío o null, mostrar un mensaje de advertencia
        String msg = "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.";
        System.out.println(msg);
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
    } else {
        // Si los detalles son válidos, agregar una nueva fila
        modelo.addRow(new Object[]{"0", "0", "0", "0", "0", "0", "0", "0"});

        // Devuelve el foco a la tabla
        this.jtDetalle.requestFocus();

        // Selecciona la última fila de la tabla y la primera columna de esa fila
        int toRow = this.jtDetalle.getRowCount() - 1;
        this.jtDetalle.changeSelection(toRow, 0, false, false);
    }
    return 0;
}


// Método para verificar si una fila es inválida
private boolean isRowInvalid(Map<String, String> row) {
    return row.values().stream().allMatch(value -> value.equals("0") || value.isEmpty());
}
}
