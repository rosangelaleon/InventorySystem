
package Formularios;
import Controllers.DBConexion;
import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.CodigoBarraFilter;
import Filtros.NumeroALetras;
import Filtros.DecimalDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.RucDocumentFilter;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import Modelo.VentaDetalle;
import Modelo.cargaComboBox;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import javax.swing.JDesktopPane;

public class Form_NotaCredito extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario {
    private DBTableController tc;
    private DBTableController tt;
    private DBTableController tprecio;
    private DBTableController tprecios;
    private DBTableController tm;
    private DBTableController tmCoti;
    private DBTableController tcdet;
    private DBTableController tproductos;
    private DBTableController tproductosdet;
    private DBTableController tmClientes;
    private DBTableController tmMonedas;
    private DBTableController tmDepositos;
    private DBTableController tmCuotas;
    private ModeloTabla modelo;
    
    private ArrayList<Map<String, String>> columnData;
    private Map<String, String> talonarioData; // Datos del talonario
     private Map<String, String> clienteData;
    private Map<String, String> myData;
      private HashMap<String, String> myDet;
    private ArrayList<VentaDetalle> listaDetalles;
    private Component lastFocusedComponent;
    // Declaración de las variables totales
    private BigDecimal totalBruto = BigDecimal.ZERO;
    private BigDecimal totalNeto = BigDecimal.ZERO;
    private BigDecimal totalIva = BigDecimal.ZERO;
    private BigDecimal totalExenta = BigDecimal.ZERO;
    private boolean monedaListenerEnabled = true;
    private Map<String, BigDecimal> preciosOriginales = new HashMap<>(); // Para almacenar los precios originales
private String monedaIdInicial;
private String clienteIdActual; // Variable para almacenar el cliente actual
    public Form_NotaCredito() {
        initComponents();
        initializeTextFields();
        initializeTableListeners();
        IdNota.setText("0");
        RUCCliente.setText("0");
        RUCCliente.setText("0");
        Neto.setText("0");
        Exento.setText("0");
        Bruto.setText("0");
        Impuesto.setText("0");
        RUCLocal.setText("2435318-4");
        listaDetalles = new ArrayList<>();
        clienteData = new HashMap<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();
        talonarioData = new HashMap<>();
        tc = new DBTableController();
        tc.iniciar("VENTAS");
        
        tcdet = new DBTableController();
        tcdet.iniciar("VENTAS_DETALLE");
        tproductos = new DBTableController();
        tproductos .iniciar("Productos");
       tproductosdet= new DBTableController();
        tproductosdet .iniciar("Productos_Detalle");
        tmClientes = new DBTableController();
        tmClientes.iniciar("CLIENTES");
        tprecio= new DBTableController();
        tprecio.iniciar("PRECIOS_DETALLE");
        tprecios= new DBTableController();
        tprecios.iniciar("PRECIOS");
        tmMonedas = new DBTableController();
        tmMonedas.iniciar("MONEDAS");
        tmDepositos = new DBTableController();
        tmDepositos.iniciar("DEPOSITOS");
        tmCuotas = new DBTableController();
        tmCuotas.iniciar("CUOTAS");
         tmCoti = new DBTableController();
        tmCoti.iniciar("COTIZACIONES");
        tt = new DBTableController();
        tt.iniciar("TALONARIOS");
        cargaComboBox.pv_cargar(Deposito, "DEPOSITOS", "id, deposito", "id", "");
        cargaComboBox.pv_cargar(Moneda, "MONEDAS", "id, moneda", "id", "");

        cargarUltimoId();
        construirTabla();
        applyFiltersToColumns();
        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        
                cargarListenerModeloTabla() ;
             // Cargar detalles del talonario
        cargarDetallesTalonarioInicio();
         manejarCambioMoneda();
manejarCambioCliente() ;
    }
 

    private void cargarDetallesTalonarioInicio() {
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        // Inicializa whereClause correctamente
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("activo", "true");  // Filtrar talonarios activos
        whereClause.put("tipo_comprobante", "2");  // Filtrar tipo de comprobante = 2
        System.out.println("Ejecutando búsqueda de talonarios...");
        List<Map<String, String>> resultado = tt.searchListById(fields, whereClause);
        System.out.println("Resultado de la búsqueda: " + resultado);

        if (!resultado.isEmpty()) {
            Map<String, String> talonarioData = resultado.get(0);
            System.out.println("Datos del talonario recuperados: " + talonarioData);
            Timbrado.setText(talonarioData.get("numero_timbrado"));
            FechaInicioVigencia.setText(talonarioData.get("fecha_inicio_timbrado"));
            FechaFinVigencia.setText(talonarioData.get("fecha_final_timbrado"));
            NotaCredito.setText(talonarioData.get("serie_comprobante")+"-");

            System.out.println("Número de timbrado: " + talonarioData.get("numero_timbrado"));
            System.out.println("Fecha de inicio de vigencia: " + talonarioData.get("fecha_inicio_timbrado"));
            System.out.println("Fecha de fin de vigencia: " + talonarioData.get("fecha_final_timbrado"));
            System.out.println("Serie del comprobante: " + talonarioData.get("serie_comprobante"));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró información del talonario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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


    private void initializeTextFields() {
        applyNumericFilter(IdNota);
        addKeyListeners();
    }
    
private void addKeyListeners() {
    FocusListener focusTracker = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = e.getComponent();
        }
    };

    IdNota.addFocusListener(focusTracker);

    // FocusListener para las celdas de la columna "Cod Barra" en jtDetalle
    jtDetalle.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = jtDetalle;
        }
    });

    jtDetalle.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()) {
        {
            getComponent().addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    lastFocusedComponent = jtDetalle;
                }
            });
        }
    });

    jtDetalle.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F2) {
                int selectedRow = jtDetalle.getSelectedRow();
                int selectedColumn = jtDetalle.getSelectedColumn();
                if (selectedRow != -1 && selectedColumn == 1) { // Column 1 is "Cod Barra"
                    imFiltrar();
                }
            }
        }
    });
}


    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyRucFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new RucDocumentFilter());
    }


  private void construirTabla() {
    listaDetalles = consultarListaDetalles();
    ArrayList<String> titulosList = new ArrayList<>();
    titulosList.add("#Item");
    titulosList.add("Cod Barra");
    titulosList.add("Descripción");
    titulosList.add("Precio");
    titulosList.add("%IVA");
    titulosList.add("Base");
    titulosList.add("Impuesto");
    titulosList.add("Descuento");
    titulosList.add("Cantidad");
    titulosList.add("Total");
    titulosList.add("Id");

    String[] titulos = titulosList.toArray(new String[0]);

    Object[][] data = obtenerMatrizDatos(titulosList);
    construirTabla(titulos, data);
    //Limpiar tambien
     Neto.setText("0");
        Exento.setText("0");
        Bruto.setText("0");
        Impuesto.setText("0");
}

private ArrayList<VentaDetalle> consultarListaDetalles() {
    listaDetalles.add(new VentaDetalle(1, 0, "0", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, null));
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
         informacion[x][0] = (x + 1) + ""; 
        informacion[x][1] =  listaDetalles.get(x).getString("codigobarras") != null ? listaDetalles.get(x).getString("codigobarras") : "0";
        informacion[x][2] =  listaDetalles.get(x).getString("producto") != null ? listaDetalles.get(x).getString("producto") : "0";
        informacion[x][3] = listaDetalles.get(x).getBigDecimal("precio") + "";
        informacion[x][4] = listaDetalles.get(x).getInteger("impuesto") + "";
        informacion[x][5] = listaDetalles.get(x).getBigDecimal("base") + "";
        informacion[x][6] = listaDetalles.get(x).getBigDecimal("impuesto") + "";
        informacion[x][7] = listaDetalles.get(x).getBigDecimal("descuento") + "";
         informacion[x][8] = listaDetalles.get(x).getBigDecimal("cantidad") + "";
        informacion[x][9] = listaDetalles.get(x).getBigDecimal("total") + ""; 
        informacion[x][10] = listaDetalles.get(x).getInteger("id")+ "";
    }
    return informacion;
}

private void construirTabla(String[] titulos, Object[][] data) {
    ArrayList<Integer> noEditable = new ArrayList<>(List.of(0, 2, 3, 4, 5, 6,9));
    modelo = new ModeloTabla(data, titulos, noEditable);
    jtDetalle.setModel(modelo);

    jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));
    jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("texto"));
    jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(6).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(7).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(8).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(9).setCellRenderer(new GestionCeldas("numerico"));
    // jtDetalle.getColumnModel().getColumn(9).setCellRenderer(new GestionCeldas("numerico")); // No añadir el renderer para el ID

     // Ocultar la columna del ID
    jtDetalle.getColumnModel().getColumn(10).setMinWidth(0);
    jtDetalle.getColumnModel().getColumn(10).setMaxWidth(0);
    jtDetalle.getColumnModel().getColumn(10).setWidth(0);
    jtDetalle.getColumnModel().getColumn(10).setPreferredWidth(0);


    jtDetalle.getTableHeader().setReorderingAllowed(false);
    jtDetalle.setRowHeight(25);
    jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));

    jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(100);
    jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(200);
    jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(7).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(8).setPreferredWidth(150);
    jtDetalle.getColumnModel().getColumn(9).setPreferredWidth(150);

    JTableHeader jtableHeader = jtDetalle.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    jtDetalle.setTableHeader(jtableHeader);
}

private void procesarSeleccion(Map<String, String> datosSeleccionados, int row) {
    String codigobarras = datosSeleccionados.get("Codigo"); // Revisa la clave correcta
    String producto = datosSeleccionados.get("Descripcion"); // Revisa la clave correcta

    // Depuración: Imprimir los valores obtenidos
    System.out.println("Procesando selección - Codigobarras: " + codigobarras + ", Producto: " + producto);

    // Verificar si los valores son válidos
    if (codigobarras != null && producto != null && !codigobarras.equals("N/A") && !producto.equals("N/A")) {
        DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();

        // Verificar si el producto ya está en la tabla
        boolean exists = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String existingCodigobarras = model.getValueAt(i, 1).toString();
            if (existingCodigobarras.equals(codigobarras)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            try {
                // Obtener los datos del producto
                Map<String, String> productoDetalle = obtenerDetalleProducto(codigobarras);
                if (productoDetalle == null) {
                    JOptionPane.showMessageDialog(null, "No se encontró el detalle del producto con código de barras: " + codigobarras, "Error", JOptionPane.ERROR_MESSAGE);
                     limpiarTabla();
                    imInsFilas();
                    return;
                }

                String productoId = productoDetalle.get("cabecera_id");
                Map<String, String> datosProducto = obtenerDatosProducto(productoId);
                if (datosProducto == null) {
                    JOptionPane.showMessageDialog(null, "No se encontró el producto con ID: " + productoId, "Error", JOptionPane.ERROR_MESSAGE);
                      imDelFilas();
                    imInsFilas();
                    return;
                }

                BigDecimal precio = obtenerPrecioProducto(codigobarras);
                if (precio.compareTo(BigDecimal.ZERO) == 0) {
                    JOptionPane.showMessageDialog(null, "Debe añadir precio al producto con código de barras: " + codigobarras, "Error", JOptionPane.ERROR_MESSAGE);
                    imDelFilas();
                    imInsFilas();
                    return;
                }

                // Detener la edición de la celda de "Cod Barra"
                if (jtDetalle.isEditing()) {
                    jtDetalle.getCellEditor().stopCellEditing();
                }

                // Llenar los campos de la fila seleccionada
                jtDetalle.setValueAt(codigobarras, row, 1); // Código de Barras
                jtDetalle.setValueAt(producto, row, 2); // Descripción
                int decimales = getDecimalPlaces();
                String formato = "%." + decimales + "f";
                jtDetalle.setValueAt(String.format(formato, precio), row, 3); // Precio

                BigDecimal impuesto = new BigDecimal(datosProducto.get("impuesto"));
                jtDetalle.setValueAt(impuesto, row, 4); // %IVA

                // Calcular otros valores si es necesario
                calcularValores(row);

                // Depuración: Imprimir el estado final de la fila
                System.out.println("Fila " + row + " después de la selección:");
                for (int i = 0; i < jtDetalle.getColumnCount(); i++) {
                    System.out.print(jtDetalle.getValueAt(row, i) + " ");
                }
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al llenar los campos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El producto ya ha sido seleccionado.", "Producto duplicado", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        System.out.println("Selección de producto inválida: Codigobarras: " + codigobarras + ", Producto: " + producto);
        JOptionPane.showMessageDialog(this, "Selección de producto inválida.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private int obtenerClientePorVenta(int idVenta) {
    Map<String, String> whereClause = new HashMap<>();
    whereClause.put("id", String.valueOf(idVenta));

    Map<String, String> fields = new HashMap<>();
    fields.put("cliente_id", "cliente_id");

    List<Map<String, String>> result = tc.searchListById(fields, whereClause);

    if (!result.isEmpty()) {
        return Integer.parseInt(result.get(0).get("cliente_id"));
    } else {
        JOptionPane.showMessageDialog(this, "No se encontró cliente asociado a la venta.", "Error", JOptionPane.ERROR_MESSAGE);
        return 0;
    }
}

private BigDecimal obtenerCotizacion(String monedaId) {
    try {
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("moneda_id", monedaId);
        whereClause.put("activo", "1");

        Map<String, String> fields = new HashMap<>();
        fields.put("compra", "compra");
        fields.put("venta", "venta");

        List<Map<String, String>> result = tmCoti.searchListById(fields, whereClause);
        if (!result.isEmpty()) {
            Map<String, String> cotizacion = result.get(0);
            return new BigDecimal(cotizacion.get("venta")); // Usar "venta" para la tasa de conversión
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al obtener la tasa de cambio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return BigDecimal.ONE; // Valor por defecto si no se encuentra la tasa
}

private BigDecimal convertirPrecio(BigDecimal precio, String monedaIdProducto, String monedaIdSeleccionada) {
    if (monedaIdProducto.equals(monedaIdSeleccionada)) {
        return precio; // No se necesita conversión si las monedas son las mismas
    }

    BigDecimal tasaProductoAGuarani = obtenerCotizacion(monedaIdProducto);
    BigDecimal tasaSeleccionadaAGuarani = obtenerCotizacion(monedaIdSeleccionada);

    // Convertir de moneda del producto a Guaraníes
    BigDecimal precioEnGuaranies = precio.multiply(tasaProductoAGuarani);

    // Convertir de Guaraníes a la moneda seleccionada
    return precioEnGuaranies.divide(tasaSeleccionadaAGuarani, 2, RoundingMode.HALF_UP);
}

private BigDecimal obtenerPrecioProducto(String codBarra) {
    String selectedCurrencyId = Functions.ExtraeCodigo(Moneda.getSelectedItem().toString());

    // Obtener el precio_id desde precios_detalle utilizando el productoDetalle_id (codBarra)
    Map<String, String> wherePrecioDetalle = new HashMap<>();
    wherePrecioDetalle.put("productoDetalle_id", codBarra);

    Map<String, String> fieldsPrecioDetalle = new HashMap<>();
    fieldsPrecioDetalle.put("precio_id", "precio_id");
    fieldsPrecioDetalle.put("precio", "precio");

    List<Map<String, String>> resultPrecioDetalle = tprecio.searchListById(fieldsPrecioDetalle, wherePrecioDetalle);
    if (resultPrecioDetalle.isEmpty()) {
        return BigDecimal.ZERO;
    }

    String precioId = resultPrecioDetalle.get(0).get("precio_id");
    BigDecimal precio = new BigDecimal(resultPrecioDetalle.get(0).get("precio"));

    // Obtener el moneda_id desde precios utilizando el precio_id
    Map<String, String> wherePrecios = new HashMap<>();
    wherePrecios.put("id", precioId);

    Map<String, String> fieldsPrecios = new HashMap<>();
    fieldsPrecios.put("moneda_id", "moneda_id");

    List<Map<String, String>> resultPrecios = tprecios.searchListById(fieldsPrecios, wherePrecios);
    if (resultPrecios.isEmpty()) {
        return BigDecimal.ZERO;
    }

    String monedaId = resultPrecios.get(0).get("moneda_id");

    // Convertir el precio si la moneda no coincide
    return convertirPrecio(precio, monedaId, selectedCurrencyId);
}

private void llenarCampos(int row, String codBarra) {
    if (Moneda.getSelectedItem() == null || Moneda.getSelectedItem().toString().equals("0-Seleccionar")) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione una moneda antes de proceder.", "Error", JOptionPane.ERROR_MESSAGE);
        limpiarTabla();
        imInsFilas();
        return;
    }

    try {
        for (int i = 0; i < jtDetalle.getRowCount(); i++) {
            if (i != row) { 
                String existingCodBarra = jtDetalle.getValueAt(i, 1).toString();
                if (existingCodBarra.equals(codBarra)) {
                    JOptionPane.showMessageDialog(null, "El producto ya ha sido seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                    imDelFilas();
                    imInsFilas();
                    return;
                }
            }
        }

        Map<String, String> productoDetalle = obtenerDetalleProducto(codBarra);
        if (productoDetalle != null) {
            String productoId = productoDetalle.get("cabecera_id");
            Map<String, String> datosProducto = obtenerDatosProducto(productoId);
            BigDecimal precio = obtenerPrecioProducto(codBarra);

            if (precio.compareTo(BigDecimal.ZERO) == 0) {
                JOptionPane.showMessageDialog(null, "Debe añadir precio al producto con código de barras: " + codBarra, "Error", JOptionPane.ERROR_MESSAGE);
                limpiarTabla();
                imInsFilas();
                return;
            }

            if (datosProducto != null) {
                jtDetalle.setValueAt(datosProducto.get("producto"), row, 2); 
                int decimales = getDecimalPlaces();
                String formato = "%." + decimales + "f";
                jtDetalle.setValueAt(String.format(formato, precio), row, 3); 

                BigDecimal impuesto = new BigDecimal(datosProducto.get("impuesto"));
                jtDetalle.setValueAt(datosProducto.get("impuesto"), row, 4); 
            }

            calcularValores(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al llenar los campos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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

// Método para obtener datos del producto desde la tabla Productos
private Map<String, String> obtenerDatosProducto(String productoId) {
    Map<String, String> where = new HashMap<>();
    where.put("id", productoId);

    Map<String, String> fields = new HashMap<>();
    fields.put("producto", "producto");
    fields.put("impuesto", "impuesto");

    List<Map<String, String>> result = tproductos.searchListById(fields, where);
    if (!result.isEmpty()) {
        return result.get(0);
    } else {
        return null;
    }
}

// Método para obtener el número de decimales basado en la moneda seleccionada
private int getDecimalPlaces() {
    String selectedCurrencyId = Functions.ExtraeCodigo(Moneda.getSelectedItem().toString());
    return tc.getDecimalPlacesForCurrency(selectedCurrencyId);
}


private void actualizarPrecio(int row) {
    String codigoBarra = jtDetalle.getValueAt(row, 1).toString();
    BigDecimal precio = obtenerPrecioProducto(codigoBarra); // Obtener el precio usando el código de barra
    if (precio != null) {
        int decimales = getDecimalPlaces();
        String formato = "%." + decimales + "f";
        precio = precio.setScale(decimales, RoundingMode.HALF_UP); // Asegúrate de que el precio tenga los decimales correctos
        jtDetalle.setValueAt(precio.toString(), row, 3); // Establecer el precio con el formato adecuado
        System.out.println("Actualizando precio para la fila " + row + ": " + precio);
    }
}


// Método para cargar el listener del modelo de la tabla
private void cargarListenerModeloTabla() {
    jtDetalle.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 3 || e.getColumn() == 7 || e.getColumn() == 8) {
            calcularValores(e.getFirstRow());
        }
    });
}
// Método para obtener la moneda_id del precio asociado a un cliente
private String obtenerMonedaCliente(String clienteId) {
    Map<String, String> whereCliente = new HashMap<>();
    whereCliente.put("id", clienteId);

    Map<String, String> fieldsCliente = new HashMap<>();
    fieldsCliente.put("precio_id", "precio_id");

    List<Map<String, String>> resultCliente = tmClientes.searchListById(fieldsCliente, whereCliente);
    if (resultCliente.isEmpty()) {
        return null;
    }

    String precioId = resultCliente.get(0).get("precio_id");

    // Obtener la moneda_id del precio asociado
    Map<String, String> wherePrecio = new HashMap<>();
    wherePrecio.put("id", precioId);

    Map<String, String> fieldsPrecio = new HashMap<>();
    fieldsPrecio.put("moneda_id", "moneda_id");

    List<Map<String, String>> resultPrecio = tprecios.searchListById(fieldsPrecio, wherePrecio);
    if (resultPrecio.isEmpty()) {
        return null;
    }

    return resultPrecio.get(0).get("moneda_id");
}
// Método para manejar la selección del cliente
private void manejarCambioCliente() {
    RUCCliente.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            // Restablecer la moneda al valor predefinido cuando se cambia el cliente
            monedaListenerEnabled = false; // Desactivar temporalmente el listener
            Moneda.setSelectedIndex(0);
            limpiarTabla();
            imInsFilas();
             actualizarTotales();
            monedaListenerEnabled = true; // Reactivar el listener
        }
    });
}

// Método para manejar la selección de la moneda
private void manejarCambioMoneda() {
    Moneda.addItemListener(e -> {
        if (monedaListenerEnabled && e.getStateChange() == ItemEvent.SELECTED) {
            // Verificar que IdClientes y RUCClientes no estén vacíos ni sean cero
            if (RUCCliente.getText().trim().isEmpty() || RUCCliente.getText().trim().equals("0") || 
                RUCCliente.getText().trim().isEmpty() || RUCCliente.getText().trim().equals("0")) {
                
                monedaListenerEnabled = false; // Desactivar temporalmente el listener
                JOptionPane.showMessageDialog(this, "Por favor, ingrese los datos de Cliente y RUC antes de seleccionar una moneda.", "Error", JOptionPane.ERROR_MESSAGE);
                Moneda.setSelectedIndex(0); // Reiniciar la selección de la moneda
                monedaListenerEnabled = true; // Reactivar el listener
                return;
            }

            String selectedMoneda = Moneda.getSelectedItem().toString();
            String selectedMonedaId = Functions.ExtraeCodigo(selectedMoneda);


            // Ignorar la verificación si el valor seleccionado es "0-Seleccionar"
            if (selectedMonedaId.equals("0")) {
                this.limpiarTabla();
                this.imInsFilas();
                actualizarTotales();
                return;
            }
    // Cargar cotización para la moneda seleccionada
            String clienteId = RUCCliente.getText().trim();

            // Verificar que la moneda seleccionada corresponde a la moneda del precio asociado al cliente
            String clienteMonedaId = obtenerMonedaCliente(clienteId);
            if (clienteMonedaId == null || !selectedMonedaId.equals(clienteMonedaId)) {
                monedaListenerEnabled = false; // Desactivar temporalmente el listener
                JOptionPane.showMessageDialog(this, "La moneda seleccionada no corresponde a la moneda del precio asociado al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                Moneda.setSelectedIndex(0); // Reiniciar la selección de la moneda
                monedaListenerEnabled = true; // Reactivar el listener
                this.limpiarTabla();
                this.imInsFilas();
                actualizarTotales();
                return;
            }

            // Si la moneda seleccionada es válida y corresponde a la del cliente, actualizar decimales y precios

            actualizarDecimales();
            for (int row = 0; row < jtDetalle.getRowCount(); row++) {
                String codBarra = jtDetalle.getValueAt(row, 1).toString();

                // Guardar el precio original si no está almacenado ya
                if (!preciosOriginales.containsKey(codBarra)) {
                    preciosOriginales.put(codBarra, getBigDecimalFromTable(jtDetalle, row, 3));
                }

                actualizarPrecio(row);
                calcularValores(row);
            }
        }
    });
}


private void actualizarDecimales() {
    String selectedCurrency = Moneda.getSelectedItem().toString();
    if ("0-Seleccionar".equals(selectedCurrency)) {
        System.out.println("Moneda no seleccionada, manteniendo decimales como están.");
        return;
    }
    int decimalPlaces = getDecimalPlaces(); // Obtener el número de decimales
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(decimalPlaces);
    df.setMinimumFractionDigits(decimalPlaces);
    df.setGroupingUsed(false);

    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        for (int col = 3; col <= 9; col++) {
            if (col == 4 || col == 8) {
                continue; // Saltar las columnas 4 (IVA) y 8 (Cantidad)
            }
            Object valueObj = model.getValueAt(i, col);
            if (valueObj != null) {
                String value = valueObj.toString().trim();
                try {
                    BigDecimal valor = new BigDecimal(value.replace(",", "."));
                    BigDecimal scaledValue = valor.setScale(decimalPlaces, RoundingMode.HALF_UP);
                    model.setValueAt(df.format(scaledValue), i, col);
                } catch (NumberFormatException ex) {
                    System.err.println("actualizarDecimales - Error al formatear el valor: " + value);
                }
            }
        }
    }

    // Recalcular valores en la tabla después de formatear los decimales
    for (int row = 0; row < jtDetalle.getRowCount(); row++) {
        calcularValores(row);
    }
}
private String obtenerSeriePorTalonarioId(String talonarioId) {
    String serie = null;
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("serie_comprobante", "serie_comprobante");

        // Inicializa whereClause correctamente
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("id", talonarioId);

        System.out.println("Ejecutando búsqueda de serie por talonarioId...");
        List<Map<String, String>> resultado = tt.searchListById(fields, whereClause);
        System.out.println("Resultado de la búsqueda: " + resultado);

        if (!resultado.isEmpty()) {
            Map<String, String> talonarioData = resultado.get(0);
            serie = talonarioData.get("serie_comprobante");
            System.out.println("Serie del comprobante recuperada: " + serie);
        } else {
            System.out.println("No se encontró la serie para el talonarioId: " + talonarioId);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar la serie del talonario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return serie;
}

 private void setData() {
    // Cabecera
    myData.put("id", IdNota.getText());
    myData.put("cliente_id", RUCCliente.getText());

    // Verificar y formatear fechas
    Date fechaProcesoDate = Fecha.getDate();

    if (fechaProcesoDate != null) {
        myData.put("fechaProceso", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaProcesoDate));
    } else {
        myData.put("fechaProceso", ""); // O un valor por defecto
    }
    myData.put("moneda_id", Functions.ExtraeCodigo(Moneda.getSelectedItem().toString()));
    myData.put("deposito_id", Functions.ExtraeCodigo(Deposito.getSelectedItem().toString()));
    myData.put("total_neto", Neto.getText().replace(".", "").replace(",", "."));
    myData.put("total_exento", Exento.getText().replace(".", "").replace(",", "."));
    myData.put("total_impuesto", Impuesto.getText().replace(".", "").replace(",", "."));
    myData.put("total_bruto", Bruto.getText().replace(".", "").replace(",", "."));
    // Obtener la serie del talonario
    String talonarioId = obtenerTalonarioActivo();
    myData.put("talonario_id", talonarioId);
    String serie = obtenerSeriePorTalonarioId(talonarioId);
    myData.put("serie", serie);

    // Obtener y almacenar el ID de la venta
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int filasTabla = model.getRowCount();
    columnData.clear(); // Limpiar los datos anteriores

    int ultimoId = obtenerUltimoIdVentaDetalle(); // Obtener el último ID existente en la tabla DETALLE

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Obtener el ID actual de la fila si existe
        String idDetalle;
        Object idCell = model.getValueAt(i, 10); // Suponiendo que el ID está en la columna índice 10
        if (idCell == null || idCell.toString().trim().isEmpty() || idCell.toString().trim().equals("0")) {
            // Generar un nuevo ID si no existe
            idDetalle = String.valueOf(++ultimoId);
            model.setValueAt(idDetalle, i, 10); // Asignar el nuevo ID a la celda correspondiente
            System.out.println("Generando nuevo idDetalle: " + idDetalle);
        } else {
            // Usar el ID existente
            idDetalle = idCell.toString().trim();
            System.out.println("Usando idDetalle existente: " + idDetalle);
        }

        rowData.put("id", idDetalle); // Agregar el ID a los datos de la fila

        // Obtener los demás valores de la fila y agregarlos a rowData
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object value = model.getValueAt(i, j);
            rowData.put(model.getColumnName(j), value != null ? value.toString().trim() : ""); // Manejar valores nulos con cadena vacía
        }

        // Crear el mapa de detalles
        myDet = new HashMap<>();
        String id = IdNota.getText();
        myDet.put("id", idDetalle);
        myDet.put("venta_id", id);
        myDet.put("productodetalle_id", rowData.get("Cod Barra"));
        myDet.put("impuesto", rowData.get("%IVA"));
        myDet.put("producto", rowData.get("Descripción"));
        myDet.put("cantidad", rowData.get("Cantidad"));
        myDet.put("precio", rowData.get("Precio").replace(".", "").replace(",", "."));
        myDet.put("impuesto", rowData.get("Impuesto").replace(".", "").replace(",", "."));
        myDet.put("descuento", rowData.get("Descuento").replace(".", "").replace(",", "."));
        myDet.put("base", rowData.get("Base").replace(".", "").replace(",", "."));
        myDet.put("total", rowData.get("Total").replace(".", "").replace(",", "."));

        this.columnData.add(this.myDet);
    }

    // Imprimir los valores de columnData para verificación
    System.out.println("Valores de columnData después de llenar:");
    for (Map<String, String> myRow : columnData) {
        System.out.println(myRow);
    }
}
private int obtenerUltimoIdVentaDetalle() {
    int ultimoId = 0;
    try {
        ultimoId = tcdet.getMaxId();
        System.out.println("Último ID obtenido: " + ultimoId);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Error al obtener el último ID: " + e.getMessage());
    }
    return ultimoId;
}
private void resetData() {
    Deposito.setSelectedIndex(0);
    Moneda.setSelectedIndex(0);
    this.myData = new HashMap<String, String>();
    this.myData.put("id", "0");
    this.myData.put("cliente_id", "0");
    this.clienteData.put("cliente", "");
    this.clienteData.put("apellido", "");
    this.clienteData.put("tipodocumento", "");
    this.clienteData.put("nrodocumento", "0");
    this.clienteData.put("divisoria", "");
     this.clienteData.put("celular", "");
      this.clienteData.put("direccion", "");
    this.myData.put("nro_documento", "");
    this.myData.put("fechaFactura", "");
    this.myData.put("fechaProceso", "");
    this.myData.put("moneda_id", "0");
    this.myData.put("deposito_id", "0");
    this.myData.put("cuota_id", "0");
    this.myData.put("total_neto", "0");
    this.myData.put("total_exento", "0");
    this.myData.put("total_impuesto", "0");
    this.myData.put("total_bruto", "0");
    this.myData.put("impreso", "0");
    this.myData.put("anulado", "0");
    // Detalle
    this.myDet = new HashMap<String, String>();
    this.myDet.put("id", "0");
    this.myDet.put("venta_id", "0");
    this.myDet.put("productodetalle_id", "0");
    this.myDet.put("cantidad", "0");
    this.myDet.put("precio", "0");
    this.myDet.put("impuesto", "0");
    this.myDet.put("descuento", "0");
    this.myDet.put("base", "0");
    this.myDet.put("total", "0");

    this.columnData.add(this.myDet);
}
private void fillView(Map<String, String> data, List<Map<String, String>> colData, Map<String, String> clienteData) {
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        switch (key) {
            case "id":
                IdNota.setText(value);
                break;
            case "cliente_id":
                RUCCliente.setText(value);
                Clientes.setText(clienteData.get("cliente") + " " + clienteData.get("apellido"));
                String tipoDocumento = clienteData.get("tipodocumento");
                String documento = clienteData.get("nrodocumento");
                String divisoria = clienteData.get("divisoria");

                if ("RUC".equals(tipoDocumento) && divisoria != null) {
                    RUCCliente.setText(documento + "-" + divisoria);
                } else {
                    RUCCliente.setText(documento);
                }

                Direccion.setText(clienteData.get("direccion"));
                RUCCliente.setText(clienteData.get("celular"));
                break;
            case "fechaProceso":
                String strFechaP = data.get("fechaProceso");
                if (strFechaP != null && !strFechaP.isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strFechaP);
                        Fecha.setDate(date);
                    } catch (ParseException e) {
                        Fecha.setDate(null);
                        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Fecha.setDate(null);
                }
                break;
            case "moneda_id":
                Functions.E_estado(Moneda, "MONEDAS", "id=" + value);
                actualizarDecimales(); // Llamar a actualizar decimales
                break;
            case "deposito_id":
                Functions.E_estado(Deposito, "DEPOSITOS", "id=" + value);
                break;
  case "total_neto":
                Neto.setText(value);
                break;
            case "total_exento":
                Exento.setText(value);
                break;
            case "total_impuesto":
                Impuesto.setText(value);
                break;
            case "total_bruto":
                Bruto.setText(value);
                break;
            case "talonario_id":
                cargarDetallesTalonario(value);
                break; 
            case "serie":
                         NotaCredito.setText(value + "-" + formatearNumeroFactura(data.get("nro_documento")));
                break;
        }
    }

    // Detalle
    int row = 0;
    for (Map<String, String> myRow : colData) {
        String codBarras = myRow.get("productodetalle_id");
        String descripcion = myRow.get("producto");
        BigDecimal precio = new BigDecimal(myRow.get("precio"));
        BigDecimal iva = new BigDecimal(myRow.get("iva"));
        BigDecimal base = new BigDecimal(myRow.get("base")); 
        BigDecimal impuestoDetalle = new BigDecimal(myRow.get("impuesto"));
        BigDecimal descuento = new BigDecimal(myRow.get("descuento"));
        BigDecimal cantidad = new BigDecimal(myRow.get("cantidad"));
        BigDecimal total = new BigDecimal(myRow.get("total"));
        String id = myRow.get("id");

        // Agregar fila a la tabla con formateo adecuado
        this.modelo.addRow(new Object[]{
            row + 1,  // Contador de filas
            codBarras,
            descripcion,
            precio,
            iva,
            base,
            impuestoDetalle,
            descuento,
            cantidad,
            total,
            id
        });

        this.jtDetalle.getSelectionModel().setSelectionInterval(row, 0);
        row++;
    }

    // Aplicar filtros de decimales y recalcular valores
    actualizarDecimales();
}

private void aplicarFiltroDecimalAColumna(int columnIndex, int decimalPlaces) {
    JTextField decimalField = new JTextField();
    ((AbstractDocument) decimalField.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));
    decimalField.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            decimalField.selectAll();
        }
    });
    jtDetalle.getColumnModel().getColumn(columnIndex).setCellEditor(new DefaultCellEditor(decimalField) {
        @Override
        public boolean stopCellEditing() {
            if (decimalField.getText().trim().isEmpty()) {
                decimalField.setText("0");
            }
            return super.stopCellEditing();
        }
    });
}


private void cargarDetallesTalonario(String talonarioId) {
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        // Inicializa whereClause correctamente
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("id", talonarioId);  // Filtrar por id del talonario
        whereClause.put("activo", "true");  // Filtrar talonarios activos
        whereClause.put("tipo_comprobante", "2");  // Filtrar tipo de comprobante = 1 (Factura)

        List<Map<String, String>> resultado = tt.searchListById(fields, whereClause);

        if (!resultado.isEmpty()) {
            Map<String, String> talonarioData = resultado.get(0);
            Timbrado.setText(talonarioData.get("numero_timbrado"));
            FechaInicioVigencia.setText(talonarioData.get("fecha_inicio_timbrado"));
            FechaFinVigencia.setText(talonarioData.get("fecha_final_timbrado"));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró información del talonario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private String formatearNumeroFactura(String numero) {
    return String.format("%07d", Integer.parseInt(numero)); // Ajusta el formato del número de la factura según sea necesario
}
private String obtenerTalonarioActivo() {
    String talonarioId = "";
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("id", "id");

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("activo", "true");
        whereClause.put("tipo_comprobante", "1");

        List<Map<String, String>> resultado = tt.searchListById(fields, whereClause);
            System.out.println("Resultado de la búsqueda de talonarios: " + resultado);
        if (!resultado.isEmpty()) {
            talonarioId = resultado.get(0).get("id");
            Timbrado.setText(talonarioData.get("numero_timbrado"));
            FechaInicioVigencia.setText(talonarioData.get("fecha_inicio_timbrado"));
            FechaFinVigencia.setText(talonarioData.get("fecha_final_timbrado"));
            NotaCredito.setText(talonarioData.get("serie_comprobante"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al obtener el talonario activo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return talonarioId;
}

private Map<String, String> cargarMonedas() {
    Map<String, String> monedas = new HashMap<>();
    List<Map<String, String>> result = tmMonedas.searchListById(new HashMap<>(), new HashMap<>());

    for (Map<String, String> moneda : result) {
        monedas.put(moneda.get("id"), moneda.get("moneda"));
    }
    return monedas;
}

private int obtenerProximoNroDocumento(String ventaId) {
    int nroDocumento = 0;
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("nro_documento", "nro_documento");

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("id", ventaId);

        List<Map<String, String>> resultado = tc.searchListById(fields, whereClause);
        if (!resultado.isEmpty()) {
            Map<String, String> ventaData = resultado.get(0);
            nroDocumento = Integer.parseInt(ventaData.get("nro_documento"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al obtener el número de documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return nroDocumento;
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


private Map<String, String> buscarClientePorVenta(int idVenta) {
    // Verificar si la venta existe y obtener el idCliente asociado
    Map<String, String> whereVenta = new HashMap<>();
    whereVenta.put("id", String.valueOf(idVenta)); // Filtrar por idVenta

    Map<String, String> fieldsVenta = new HashMap<>();
    fieldsVenta.put("cliente_id", "cliente_id"); // Obtener el idCliente

    List<Map<String, String>> resultadoVenta = tc.searchListById(fieldsVenta, whereVenta);

    if (resultadoVenta.isEmpty()) {
        JOptionPane.showMessageDialog(null, "La venta no existe. Verifique el número de venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return null;
    }

    // Obtener el idCliente de la venta
    String idCliente = resultadoVenta.get(0).get("cliente_id");

    // Buscar el cliente en la tabla de clientes
    Map<String, String> whereCliente = new HashMap<>();
    whereCliente.put("id", idCliente); // Buscar por el idCliente recuperado

    Map<String, String> fieldsCliente = new HashMap<>();
    fieldsCliente.put("id", "id");
    fieldsCliente.put("cliente", "cliente");
    fieldsCliente.put("apellido", "apellido");
    fieldsCliente.put("nrodocumento", "nrodocumento");
    fieldsCliente.put("divisoria", "divisoria");
    fieldsCliente.put("direccion", "direccion");
    fieldsCliente.put("celular", "celular");
    fieldsCliente.put("tipodocumento", "tipodocumento");

    List<Map<String, String>> resultadoCliente = tmClientes.searchListById(fieldsCliente, whereCliente);

    if (resultadoCliente.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No se encontró el cliente asociado a la venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return null;
    }

    // Devolver los datos del cliente
    return resultadoCliente.get(0);
}

private List<Map<String, String>> consultarDetallesPorVenta(int idVenta) {
    // Configurar los criterios de búsqueda en la tabla VENTAS_DETALLE
    Map<String, String> whereClause = new HashMap<>();
    whereClause.put("venta_id", String.valueOf(idVenta)); // Filtrar por idVenta

    // Seleccionar los campos necesarios de la tabla VENTAS_DETALLE
    Map<String, String> fields = new HashMap<>();
    fields.put("productodetalle_id", "Cod Barra");
    fields.put("producto", "Descripción");
    fields.put("precio", "Precio");
    fields.put("impuesto", "%IVA");
    fields.put("base", "Base");
    fields.put("impuesto_total", "Impuesto");
    fields.put("descuento", "Descuento");
    fields.put("cantidad", "Cantidad");
    fields.put("total", "Total");

    // Consultar en la base de datos
    List<Map<String, String>> detalles = tcdet.searchListById(fields, whereClause);

    if (detalles.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para la venta indicada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    return detalles;
}

private Map<String, String> obtenerDatosVenta(int idVenta) {
    Map<String, String> whereClause = new HashMap<>();
    whereClause.put("id", String.valueOf(idVenta)); // Filtrar por idVenta

    // Campos necesarios de la tabla VENTAS
    Map<String, String> fields = new HashMap<>();
    fields.put("moneda_id", "moneda_id");
    fields.put("deposito_id", "deposito_id");

    // Consultar en la base de datos
    List<Map<String, String>> result = tc.searchListById(fields, whereClause);

    if (!result.isEmpty()) {
        return result.get(0); // Retornar el primer resultado
    } else {
        JOptionPane.showMessageDialog(this, "No se encontraron datos de la venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return null;
    }
}

private void rellenarTablaConDetalles(int idVenta) {
    // Obtener los detalles de la venta
    List<Map<String, String>> detallesVenta = consultarDetallesPorVenta(idVenta);

    if (detallesVenta == null || detallesVenta.isEmpty()) {
        limpiarTabla(); // Limpiar la tabla si no hay datos
        return;
    }

    // Limpiar la tabla antes de llenarla con los nuevos datos
    limpiarTabla();

    // Modelo de la tabla
    DefaultTableModel modelo = (DefaultTableModel) jtDetalle.getModel();

    // Llenar las filas con los detalles de la venta
    int item = 1; // Contador para el #Item
    for (Map<String, String> detalle : detallesVenta) {
        Object[] fila = new Object[]{
            item++, // Incrementar el índice del item
            detalle.get("Cod Barra"), // Código de barras
            detalle.get("Descripción"), // Descripción del producto
            detalle.get("Precio"), // Precio
            detalle.get("%IVA"), // Porcentaje de IVA
            detalle.get("Base"), // Base imponible
            detalle.get("Impuesto"), // Total del impuesto
            detalle.get("Descuento"), // Descuento
            detalle.get("Cantidad"), // Cantidad
            detalle.get("Total") // Total de la fila
        };

        // Añadir la fila al modelo de la tabla
        modelo.addRow(fila);
    }

    // Actualizar los totales
    actualizarTotales();
}

private int obtenerVentaPorFactura(String serie, String nroDocumento) {
    Map<String, String> whereClause = new HashMap<>();
    whereClause.put("serie", serie);
    whereClause.put("nro_documento", nroDocumento);

    Map<String, String> fields = new HashMap<>();
    fields.put("id", "id"); // Obtener el id de la venta

    List<Map<String, String>> result = tc.searchListById(fields, whereClause);

    if (!result.isEmpty()) {
        return Integer.parseInt(result.get(0).get("id"));
    } else {
        JOptionPane.showMessageDialog(this, "Factura no encontrada. Verifique la serie y número de documento.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return 0;
    }
}

private void validarVencimientoTimbrado(String fechaFinalTimbrado) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse(fechaFinalTimbrado);
        Date fechaActual = new Date();
        
        if (fechaVencimiento.before(fechaActual)) {
            JOptionPane.showMessageDialog(this, "El talonario está vencido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    } catch (ParseException e) {
        e.printStackTrace();
    }
}

private void rellenarCabecera(String serie, String nroDocumento) {
    try {
        // Obtener idVenta basado en la factura
        int idVenta = obtenerVentaPorFactura(serie, nroDocumento);

        if (idVenta == 0) {
            imNuevo(); // Si no hay venta, reiniciar
            return;
        }

        // Obtener datos de la cabecera de la venta, incluyendo cliente, moneda y depósito
        Map<String, String> datosVenta = obtenerDatosVenta(idVenta);

        if (datosVenta == null || datosVenta.isEmpty()) {
            imNuevo(); // Si no se encuentran datos, reiniciar
            return;
        }

        // Rellenar datos del cliente
        Map<String, String> datosCliente = buscarClientePorVenta(idVenta);
        if (datosCliente != null) {
            Clientes.setText(datosCliente.get("cliente") + " " + datosCliente.get("apellido"));
            RUCCliente.setText(datosCliente.get("nrodocumento") + "-" + datosCliente.get("divisoria"));
            Direccion.setText(datosCliente.get("direccion"));
            Telefono.setText(datosCliente.get("celular"));
        } else {
            imNuevo(); // Si no hay cliente, reiniciar
            return;
        }

        // Rellenar moneda
        String monedaId = datosVenta.get("moneda_id");
        if (monedaId != null) {
            Functions.E_estado(Moneda, "MONEDAS", "id=" + monedaId);
        }

        // Rellenar depósito
        String depositoId = datosVenta.get("deposito_id");
        if (depositoId != null) {
            Functions.E_estado(Deposito, "DEPOSITOS", "id=" + depositoId);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al rellenar los datos de la cabecera: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

   
private void applyFiltersToColumns() {
    int decimalPlaces = getDecimalPlaces();

    // Columna 7: Decimal (Descuento)
    JTextField decimalField1 = new JTextField();
    ((AbstractDocument) decimalField1.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));
    decimalField1.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            decimalField1.selectAll();
        }
    });
    jtDetalle.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(decimalField1) {
        @Override
        public boolean stopCellEditing() {
            if (decimalField1.getText().trim().isEmpty()) {
                decimalField1.setText("0");
            }
            return super.stopCellEditing();
        }
    });

    // Columna 8: Otro campo con filtro decimal (ejemplo)
    JTextField decimalField2 = new JTextField();
    ((AbstractDocument) decimalField2.getDocument()).setDocumentFilter(new DecimalDocumentFilter(decimalPlaces));
    decimalField2.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            decimalField2.selectAll();
        }
    });
    jtDetalle.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(decimalField2) {
        @Override
        public boolean stopCellEditing() {
            if (decimalField2.getText().trim().isEmpty()) {
                decimalField2.setText("0");
            }
            return super.stopCellEditing();
        }
    });

    // Agregar el KeyListener al Cod Barra
    JTextField textField = new JTextField();
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new CodigoBarraFilter());
    textField.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            textField.selectAll();
        }
    });
    DefaultCellEditor editor = new DefaultCellEditor(textField);
    jtDetalle.getColumnModel().getColumn(1).setCellEditor(editor);
    jtDetalle.getColumnModel().getColumn(1).getCellEditor().addCellEditorListener(new CellEditorListener() {
        @Override
        public void editingStopped(ChangeEvent e) {
            int row = jtDetalle.getSelectedRow();
            String codBarra = (String) jtDetalle.getValueAt(row, 1);
            if (codBarra != null && !codBarra.trim().isEmpty()) {
                llenarCampos(row, codBarra);
            }
        }

        @Override
        public void editingCanceled(ChangeEvent e) {}
    });
}

private void calcularValores(int row) {
    try {
        int decimales = getDecimalPlaces();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimales);
        df.setMinimumFractionDigits(decimales);
        df.setGroupingUsed(false);

        BigDecimal precio = getBigDecimalFromTable(jtDetalle, row, 3); // Columna de precio
        BigDecimal cantidad = getBigDecimalFromTable(jtDetalle, row, 8); // Columna de cantidad
        BigDecimal descuento = getBigDecimalFromTable(jtDetalle, row, 7); // Columna de descuento
        BigDecimal iva = getBigDecimalFromTable(jtDetalle, row, 4); // Columna de IVA

        if (precio == null) precio = BigDecimal.ZERO;
        if (cantidad == null) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            cantidad = BigDecimal.ONE; // Valor predeterminado
        }
        if (descuento == null) descuento = BigDecimal.ZERO;

        // Validación de descuento mayor que precio
        if (descuento.compareTo(precio) > 0) {
            JOptionPane.showMessageDialog(null, "El descuento no puede ser mayor que el precio.", "Error", JOptionPane.ERROR_MESSAGE);
            jtDetalle.setValueAt("0", row, 7);
            descuento = BigDecimal.ZERO;
        }

        // Calculo precio con descuento
        BigDecimal precioConDescuento = precio.subtract(descuento);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal impuesto = BigDecimal.ZERO;
        BigDecimal neto = BigDecimal.ZERO;

        // Calcular valores solo para la fila actual
        BigDecimal valor = precioConDescuento.multiply(cantidad);

        if ("10".equals(iva.toPlainString())) {
            // IVA del 10%
            neto = valor.divide(BigDecimal.valueOf(1.1), decimales, RoundingMode.HALF_UP);
            impuesto = valor.subtract(neto);
        } else if ("5".equals(iva.toPlainString())) {
            // IVA del 5%
            neto = valor.divide(BigDecimal.valueOf(1.05), decimales, RoundingMode.HALF_UP);
            impuesto = valor.subtract(neto);
        }

        // Calculo total 
        total = valor;

        // Actualizar valores en la tabla
        jtDetalle.setValueAt(df.format(neto.setScale(decimales, RoundingMode.HALF_UP)), row, 5); // Columna de base
        jtDetalle.setValueAt(df.format(impuesto.setScale(decimales, RoundingMode.HALF_UP)), row, 6); // Columna de impuesto
        jtDetalle.setValueAt(df.format(total.setScale(decimales, RoundingMode.HALF_UP)), row, 9); // Columna de total

        // Actualizar los totales
        actualizarTotales();

    } catch (NumberFormatException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error en el formato de número: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void actualizarTotales() {
    int decimales = getDecimalPlaces();
    String formato = "%." + decimales + "f";

    BigDecimal totalExento = BigDecimal.ZERO;
    BigDecimal totalNeto = BigDecimal.ZERO;
    BigDecimal totalImpuesto = BigDecimal.ZERO;
    BigDecimal totalBruto = BigDecimal.ZERO;

    for (int row = 0; row < jtDetalle.getRowCount(); row++) {
        BigDecimal base = getBigDecimalFromTable(jtDetalle, row, 5); // Columna de base
        BigDecimal impuesto = getBigDecimalFromTable(jtDetalle, row, 6); // Columna de impuesto
        BigDecimal total = getBigDecimalFromTable(jtDetalle, row, 9); // Columna de total
        BigDecimal cantidad = getBigDecimalFromTable(jtDetalle, row, 8); // Columna de cantidad
        BigDecimal descuento = getBigDecimalFromTable(jtDetalle, row, 7); // Columna de descuento

        if (base == null) base = BigDecimal.ZERO;
        if (impuesto == null) impuesto = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;
        if (cantidad == null) cantidad = BigDecimal.ONE;
        if (descuento == null) descuento = BigDecimal.ZERO;

        // Totales generales
        totalBruto = totalBruto.add(total);
        totalNeto = totalNeto.add(base.subtract(descuento.multiply(cantidad)));
        totalImpuesto = totalImpuesto.add(impuesto);
   // Si el IVA es cero, se considera exento
        BigDecimal iva = getBigDecimalFromTable(jtDetalle, row, 4); // Columna de IVA
        if (iva == null || iva.compareTo(BigDecimal.ZERO) == 0) {
            totalExento = totalExento.add(base);
        }

    }

    // Actualizar en los campos de la interfaz
    Exento.setText(String.format(formato, totalExento));
    Neto.setText(String.format(formato, totalNeto));
    Impuesto.setText(String.format(formato, totalImpuesto));
    Bruto.setText(String.format(formato, totalBruto));
}


private BigDecimal getBigDecimalFromTable(JTable table, int row, int column) {
    Object value = table.getValueAt(row, column);
    if (value == null || value.toString().isEmpty()) {
        return BigDecimal.ZERO;
    }
    try {
        String valueStr = value.toString().replace(",", ".").trim();
        return new BigDecimal(valueStr);
    } catch (NumberFormatException e) {
        e.printStackTrace();
        return BigDecimal.ZERO;
    }
}



private void initializeTableListeners() {
    jtDetalle.getModel().addTableModelListener(e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 7 || col == 8) {
                Object value = jtDetalle.getValueAt(row, col);
                if (value == null || value.toString().trim().isEmpty()) {
                    if (col == 7) { // Descuento
                        jtDetalle.setValueAt("0", row, col);
                    } else if (col == 8) { // Cantidad
                        jtDetalle.setValueAt("1", row, col);
                    }
                }
                calcularValores(row); // recalcular los valores si se actualiza una celda relevante
            }
        }
    });
}


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelPrincipal = new javax.swing.JPanel();
        PanelNota = new javax.swing.JPanel();
        NotaCredito = new javax.swing.JLabel();
        lbl_fechafin = new javax.swing.JLabel();
        FechaInicioVigencia = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        RUCLocal = new javax.swing.JLabel();
        lbl_rucLocal = new javax.swing.JLabel();
        Timbrado = new javax.swing.JLabel();
        lbl_fechainicio = new javax.swing.JLabel();
        FechaFinVigencia = new javax.swing.JLabel();
        lbl_timbrado = new javax.swing.JLabel();
        lbl_IdVentas = new javax.swing.JLabel();
        lbl_Cliente = new javax.swing.JLabel();
        lbl_UltimoId = new javax.swing.JLabel();
        lbl_RUC = new javax.swing.JLabel();
        Ultimo = new javax.swing.JLabel();
        Deposito = new javax.swing.JComboBox<>();
        lbl_deposito = new javax.swing.JLabel();
        Moneda = new javax.swing.JComboBox<>();
        lbl_Moneda = new javax.swing.JLabel();
        PanelTotales = new javax.swing.JPanel();
        lbl_Neto = new javax.swing.JLabel();
        lbl_Exento = new javax.swing.JLabel();
        lbl_Impuesto = new javax.swing.JLabel();
        lbl_nota = new javax.swing.JLabel();
        Impuesto = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        Bruto = new javax.swing.JLabel();
        Exento = new javax.swing.JLabel();
        Neto = new javax.swing.JLabel();
        Detalles = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();
        lbl_direccion = new javax.swing.JLabel();
        Clientes = new javax.swing.JLabel();
        RUCCliente = new javax.swing.JLabel();
        Fecha = new com.toedter.calendar.JDateChooser();
        lbl_FechaProceso = new javax.swing.JLabel();
        lbl_telefono = new javax.swing.JLabel();
        Direccion = new javax.swing.JLabel();
        IdNota = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        Telefono = new javax.swing.JLabel();
        Telefono2 = new javax.swing.JLabel();
        IdClientes1 = new javax.swing.JLabel();
        Factura = new javax.swing.JTextField();
        lbl_IdVentas1 = new javax.swing.JLabel();
        lbl_deposito1 = new javax.swing.JLabel();
        Factura1 = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Nota de Credito");
        setPreferredSize(new java.awt.Dimension(985, 570));

        PanelPrincipal.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));
        PanelPrincipal.setMinimumSize(new java.awt.Dimension(975, 540));
        PanelPrincipal.setPreferredSize(new java.awt.Dimension(975, 520));
        PanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelNota.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles Nota de Crédito"));

        NotaCredito.setBackground(new java.awt.Color(204, 204, 255));
        NotaCredito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NotaCredito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        NotaCredito.setOpaque(true);

        lbl_fechafin.setText("   Fecha Fin Vigencia");

        FechaInicioVigencia.setBackground(new java.awt.Color(204, 204, 255));
        FechaInicioVigencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FechaInicioVigencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FechaInicioVigencia.setOpaque(true);

        jLabel8.setText("Nota de Crédito N°");

        RUCLocal.setBackground(new java.awt.Color(204, 204, 255));
        RUCLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RUCLocal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RUCLocal.setOpaque(true);

        lbl_rucLocal.setText("RUC");

        Timbrado.setBackground(new java.awt.Color(204, 204, 255));
        Timbrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Timbrado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Timbrado.setOpaque(true);

        lbl_fechainicio.setText("   Fecha Inicio Vigencia");

        FechaFinVigencia.setBackground(new java.awt.Color(204, 204, 255));
        FechaFinVigencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FechaFinVigencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FechaFinVigencia.setOpaque(true);

        lbl_timbrado.setText("Timbrado N°");

        javax.swing.GroupLayout PanelNotaLayout = new javax.swing.GroupLayout(PanelNota);
        PanelNota.setLayout(PanelNotaLayout);
        PanelNotaLayout.setHorizontalGroup(
            PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelNotaLayout.createSequentialGroup()
                .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelNotaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(PanelNotaLayout.createSequentialGroup()
                        .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_rucLocal)
                            .addComponent(lbl_fechafin)
                            .addComponent(lbl_timbrado)
                            .addComponent(lbl_fechainicio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FechaInicioVigencia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Timbrado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FechaFinVigencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RUCLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(PanelNotaLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(NotaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 32, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(PanelNotaLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelNotaLayout.setVerticalGroup(
            PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNotaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Timbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_timbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FechaInicioVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fechainicio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FechaFinVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fechafin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelNotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_rucLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RUCLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NotaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        PanelPrincipal.add(PanelNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 240, 230));

        lbl_IdVentas.setText("  N° Factura");
        PanelPrincipal.add(lbl_IdVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        lbl_Cliente.setText("Cliente");
        PanelPrincipal.add(lbl_Cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, 20));

        lbl_UltimoId.setText(" Ultimo");
        PanelPrincipal.add(lbl_UltimoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 40, 20));

        lbl_RUC.setText("CI/RUC");
        PanelPrincipal.add(lbl_RUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 170, 50, -1));

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);
        PanelPrincipal.add(Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 150, 22));

        PanelPrincipal.add(Deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 260, 170, -1));

        lbl_deposito.setText("Motivo");
        PanelPrincipal.add(lbl_deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 40, -1));

        Moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonedaActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 260, 180, -1));

        lbl_Moneda.setText("  Moneda");
        PanelPrincipal.add(lbl_Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 260, -1, 20));

        PanelTotales.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales"));
        PanelTotales.setPreferredSize(new java.awt.Dimension(200, 230));

        lbl_Neto.setText("Total Neto");

        lbl_Exento.setText("Total Exento");

        lbl_Impuesto.setText("Total Impuesto");

        lbl_nota.setText("Total Bruto");

        Impuesto.setBackground(new java.awt.Color(204, 204, 255));
        Impuesto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Impuesto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Impuesto.setOpaque(true);

        Bruto.setBackground(new java.awt.Color(204, 204, 255));
        Bruto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Bruto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Bruto.setOpaque(true);

        Exento.setBackground(new java.awt.Color(204, 204, 255));
        Exento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Exento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Exento.setOpaque(true);

        Neto.setBackground(new java.awt.Color(204, 204, 255));
        Neto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Neto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Neto.setOpaque(true);

        javax.swing.GroupLayout PanelTotalesLayout = new javax.swing.GroupLayout(PanelTotales);
        PanelTotales.setLayout(PanelTotalesLayout);
        PanelTotalesLayout.setHorizontalGroup(
            PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelTotalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(PanelTotalesLayout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PanelTotalesLayout.createSequentialGroup()
                                .addComponent(lbl_nota)
                                .addGap(18, 18, 18)
                                .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addGroup(PanelTotalesLayout.createSequentialGroup()
                                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_Neto, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_Exento, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_Impuesto, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Impuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Neto, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(Exento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(15, 15, 15))
        );
        PanelTotalesLayout.setVerticalGroup(
            PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTotalesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Neto)
                    .addComponent(Neto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Exento)
                    .addComponent(Exento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Impuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Impuesto))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nota, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        PanelPrincipal.add(PanelTotales, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 230, 230));

        Detalles.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles"));

        jtDetalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#Item", "Cod Barra", "Descripción", "Precio", "%IVA", "Base", "Descuento", "Cantidad", "Total", "Title 10", "Title 11"
            }
        ));
        jtDetalle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        jtDetalle.setRowSelectionAllowed(false);
        jtDetalle.setShowGrid(true);
        jtDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtDetalleKeyPressed(evt);
            }
        });
        Detalles.setViewportView(jtDetalle);

        PanelPrincipal.add(Detalles, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 950, 200));

        lbl_direccion.setText("Dirección");
        PanelPrincipal.add(lbl_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 60, -1));

        Clientes.setBackground(new java.awt.Color(204, 204, 255));
        Clientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Clientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Clientes.setOpaque(true);
        PanelPrincipal.add(Clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 300, 22));

        RUCCliente.setBackground(new java.awt.Color(204, 204, 255));
        RUCCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RUCCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RUCCliente.setOpaque(true);
        PanelPrincipal.add(RUCCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 130, 22));
        PanelPrincipal.add(Fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 150, -1));

        lbl_FechaProceso.setText("Fecha ");
        PanelPrincipal.add(lbl_FechaProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, 20));

        lbl_telefono.setText("Teléfono");
        PanelPrincipal.add(lbl_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 50, -1));

        Direccion.setBackground(new java.awt.Color(204, 204, 255));
        Direccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Direccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Direccion.setOpaque(true);
        PanelPrincipal.add(Direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 380, 22));

        IdNota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdNotaFocusLost(evt);
            }
        });
        IdNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdNotaActionPerformed(evt);
            }
        });
        IdNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdNotaKeyPressed(evt);
            }
        });
        PanelPrincipal.add(IdNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 140, -1));
        PanelPrincipal.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 246, 460, 10));
        PanelPrincipal.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 460, 10));

        Telefono.setBackground(new java.awt.Color(204, 204, 255));
        Telefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Telefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Telefono.setOpaque(true);
        PanelPrincipal.add(Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 180, 22));

        Telefono2.setBackground(new java.awt.Color(204, 204, 255));
        Telefono2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Telefono2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Telefono2.setOpaque(true);
        PanelPrincipal.add(Telefono2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 180, 22));

        IdClientes1.setBackground(new java.awt.Color(204, 204, 255));
        IdClientes1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IdClientes1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        IdClientes1.setOpaque(true);
        PanelPrincipal.add(IdClientes1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 60, 22));

        Factura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                FacturaFocusLost(evt);
            }
        });
        Factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FacturaActionPerformed(evt);
            }
        });
        Factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FacturaKeyPressed(evt);
            }
        });
        PanelPrincipal.add(Factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 380, -1));

        lbl_IdVentas1.setText("  N° Registro");
        PanelPrincipal.add(lbl_IdVentas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 20));

        lbl_deposito1.setText("Depósito");
        PanelPrincipal.add(lbl_deposito1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 260, -1, 20));

        Factura1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                Factura1FocusLost(evt);
            }
        });
        Factura1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Factura1ActionPerformed(evt);
            }
        });
        Factura1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Factura1KeyPressed(evt);
            }
        });
        PanelPrincipal.add(Factura1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 140, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdNotaActionPerformed

    private void IdNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdNotaKeyPressed
              if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }  
    }//GEN-LAST:event_IdNotaKeyPressed

    private void jtDetalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtDetalleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtDetalleKeyPressed

    private void IdNotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdNotaFocusLost

    }//GEN-LAST:event_IdNotaFocusLost

    private void MonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MonedaActionPerformed
                                                                          
    }//GEN-LAST:event_MonedaActionPerformed

    private void FacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_FacturaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_FacturaFocusLost

    private void FacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FacturaActionPerformed

    private void FacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FacturaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_FacturaKeyPressed

    private void Factura1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Factura1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_Factura1FocusLost

    private void Factura1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Factura1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Factura1ActionPerformed

    private void Factura1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Factura1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String factura = Factura.getText().trim();

        // Validar el formato de la factura ingresada
        if (!factura.matches("^\\d{3}-\\d{4}-\\d{7}$")) {
            JOptionPane.showMessageDialog(this, "El formato de la factura debe ser: XXX-XXXX-0000000.\nEjemplo: 333-1332-0000001", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dividir la factura en partes: serie y número de documento
        String[] partesFactura = factura.split("-");
        if (partesFactura.length != 3) {
            JOptionPane.showMessageDialog(this, "Ingrese la factura en el formato correcto: XXX-XXXX-0000000.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener serie y número de documento
        String serie = partesFactura[0] + "-" + partesFactura[1];
        String nroDocumento = partesFactura[2];

        try {
            // Buscar la venta y rellenar datos
            int idVenta = obtenerVentaPorFactura(serie, nroDocumento);
            if (idVenta > 0) {
                rellenarCabecera(serie, nroDocumento); // Rellena la cabecera
                rellenarTablaConDetalles(idVenta); // Rellena la tabla de detalles
            } else {
                 imNuevo();
                limpiarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_Factura1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Bruto;
    private javax.swing.JLabel Clientes;
    private javax.swing.JComboBox<String> Deposito;
    private javax.swing.JScrollPane Detalles;
    private javax.swing.JLabel Direccion;
    private javax.swing.JLabel Exento;
    private javax.swing.JTextField Factura;
    private javax.swing.JTextField Factura1;
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JLabel FechaFinVigencia;
    private javax.swing.JLabel FechaInicioVigencia;
    private javax.swing.JLabel IdClientes1;
    private javax.swing.JTextField IdNota;
    private javax.swing.JLabel Impuesto;
    private javax.swing.JComboBox<String> Moneda;
    private javax.swing.JLabel Neto;
    private javax.swing.JLabel NotaCredito;
    private javax.swing.JPanel PanelNota;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelTotales;
    private javax.swing.JLabel RUCCliente;
    private javax.swing.JLabel RUCLocal;
    private javax.swing.JLabel Telefono;
    private javax.swing.JLabel Telefono2;
    private javax.swing.JLabel Timbrado;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JLabel lbl_Cliente;
    private javax.swing.JLabel lbl_Exento;
    private javax.swing.JLabel lbl_FechaProceso;
    private javax.swing.JLabel lbl_IdVentas;
    private javax.swing.JLabel lbl_IdVentas1;
    private javax.swing.JLabel lbl_Impuesto;
    private javax.swing.JLabel lbl_Moneda;
    private javax.swing.JLabel lbl_Neto;
    private javax.swing.JLabel lbl_RUC;
    private javax.swing.JLabel lbl_UltimoId;
    private javax.swing.JLabel lbl_deposito;
    private javax.swing.JLabel lbl_deposito1;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_fechafin;
    private javax.swing.JLabel lbl_fechainicio;
    private javax.swing.JLabel lbl_nota;
    private javax.swing.JLabel lbl_rucLocal;
    private javax.swing.JLabel lbl_telefono;
    private javax.swing.JLabel lbl_timbrado;
    // End of variables declaration//GEN-END:variables

  @Override
public int imGuardar(String crud) {
    System.out.println("Iniciando guardado de la cabecera y detalles...");
   
    setData();
 System.out.println("Datos de la cabecera (myData): " + myData);
    // Lista de columnas obligatorias en el detalle
    List<String> columnasObligatoriasDetalle = Arrays.asList("Cod Barra", "Cantidad", "Precio", "Impuesto", "Base", "Total");

    // Validar que todos los campos obligatorios en cada fila de jtDetalle están llenos
    for (int i = 0; i < jtDetalle.getRowCount(); i++) {
        for (String columna : columnasObligatoriasDetalle) {
            int colIndex = getColumnIndexByName(jtDetalle, columna);
            if (colIndex == -1) {
                System.err.println("Columna obligatoria '" + columna + "' no encontrada en jtDetalle.");
                continue; // Saltar esta columna si el índice no es válido
            }
            
            Object valor = jtDetalle.getValueAt(i, colIndex);
            if (valor == null || valor.toString().isEmpty() || valor.equals("0")) {
                JOptionPane.showMessageDialog(this, "Todos los detalles deben tener campos obligatorios válidos (no vacíos y no ceros) en la fila " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                resetData();
                return -1;
            }
        }
    }

    // Verificar valores de cabecera en myData
    List<String> columnasObligatoriasCabecera = Arrays.asList("cliente_id", "talonario_id", "total_neto", "total_bruto", "total_impuesto", "moneda_id", "deposito_id", "serie", "fechaProceso", "fechaFactura");
    for (String columna : columnasObligatoriasCabecera) {
        String valor = myData.get(columna);
        if (valor == null || valor.isEmpty() || valor.equals("0")) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos: " + columna, "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
            return -1;
        }
    }

    // Si llega aquí, todas las verificaciones están completas, proceder al guardado
    if (!guardarCabecera(Integer.parseInt(myData.get("id")))) {
        JOptionPane.showMessageDialog(this, "Error al guardar la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
    guardarDetalle(Integer.parseInt(myData.get("id")));

   int option = JOptionPane.showConfirmDialog(this, "¿Desea imprimir la factura?", "Confirmación", JOptionPane.YES_NO_OPTION);
if (option == JOptionPane.YES_OPTION) {
    if (myData.get("impreso").equals("1")) {
        JOptionPane.showMessageDialog(this, "Esta venta ya fue impresa y no puede imprimirse nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1; // Cancelar el proceso de impresión
    }
    int idFactura = Integer.parseInt(myData.get("id"));
    BigDecimal totalBruto = new BigDecimal(myData.get("total_bruto"));
    String totalLetras = NumeroALetras.convertir(totalBruto);
    totalLetras = NumeroALetras.capitalizarPrimeraLetra(totalLetras);

    imprimirFactura(idFactura, totalLetras);

    // Actualizar el estado de impresión
    actualizarEstadoImpreso(Integer.parseInt(myData.get("id")));
}
    resetData();
    return 0;
}

private void actualizarEstadoImpreso(int idVenta) {
    Map<String, String> updateData = new HashMap<>();
    updateData.put("id", String.valueOf(idVenta));
    updateData.put("impreso", "1");

    ArrayList<Map<String, String>> dataToUpdate = new ArrayList<>();
    dataToUpdate.add(updateData);

    try {
        int rowsAffected = tc.updateReg(dataToUpdate);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar el estado de impresión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Método para obtener el índice de la columna por nombre en jtDetalle
private int getColumnIndexByName(JTable table, String columnName) {
    for (int i = 0; i < table.getColumnCount(); i++) {
        if (table.getColumnName(i).equals(columnName)) {
            return i;
        }
    }
    System.err.println("Columna no encontrada: " + columnName);
    return -1;
}

// Método para guardar la cabecera de la venta
private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    if (!myData.containsKey("cuota_id") || myData.get("cuota_id").isEmpty()) {
        myData.remove("cuota_id");
    }
    alCabecera.add(myData);

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));
    if (tc.searchListById(fields, where).isEmpty()) {
        int rows = tc.createReg(myData);
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar crear el registro de la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar el registro de la cabecera: " + idCabecera, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    return true;
}

private void guardarDetalle(int idCabecera) {
    try {
        // Consulta para obtener los detalles existentes
        Map<String, String> where = new HashMap<>();
        where.put("venta_id", String.valueOf(idCabecera));

        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        // Obtener detalles existentes
        List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);
        Map<String, Map<String, String>> detallesExistentesMap = new HashMap<>();
        for (Map<String, String> detalle : detallesExistentes) {
            detallesExistentesMap.put(detalle.get("id"), detalle);
        }

        // Procesar detalles nuevos o actualizados
        for (Map<String, String> myRow : columnData) {
            myRow.put("venta_id", String.valueOf(idCabecera));
            String detalleId = myRow.get("id");

            if (!detallesExistentesMap.containsKey(detalleId)) {
                // Crear nuevo detalle
                System.out.println("Insertando nuevo detalle: " + myRow);
                tcdet.createReg(myRow);
            } else {
                // Actualizar detalle existente
                detallesExistentesMap.remove(detalleId);
                ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
                alDetalle.add(myRow);
                System.out.println("Actualizando detalle existente: " + alDetalle);
                tcdet.updateReg(alDetalle);
            }
        }

        // Eliminar detalles sobrantes
        for (Map.Entry<String, Map<String, String>> entry : detallesExistentesMap.entrySet()) {
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(entry.getValue());
            System.out.println("Eliminando detalle: " + entry.getValue());
            tcdet.deleteReg(alDetalle);
        }
        cargarDetallesTalonarioInicio();
        JOptionPane.showMessageDialog(this, "Registro de detalles guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        cargarDetallesTalonarioInicio();
        JOptionPane.showMessageDialog(this, "Error al guardar los detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Método para imprimir la factura
public void imprimirFactura(int idFactura, String totalLetras) {
    System.out.println("ID Factura para imprimir: " + idFactura);
    String reportPath = "C:\\Users\\Delia Silva\\Desktop\\Octavo Semestre\\Proyecto II\\InventorySystem\\Reportes\\factura.jrxml";
    try {
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id_factura", idFactura);
        parameters.put("Total_Letras", totalLetras);
        System.out.println("Parámetros enviados al reporte: " + parameters);


        Connection conexion = DBConexion.con;

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);

        JasperViewer.viewReport(jasperPrint, false);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al imprimir la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


@Override
public int imBorrar(String crud) {
    if (IdNota.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro para borrar.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int idVenta = Integer.parseInt(IdNota.getText());
    if (!confirmarBorrado()) {
        return -1;
    }

    try {
        Map<String, String> where = new HashMap<>();
        where.put("id", String.valueOf(idVenta));
        ArrayList<Map<String, String>> whereList = new ArrayList<>();
        whereList.add(where);
        tc.deleteReg(whereList);

        Map<String, String> whereDetalle = new HashMap<>();
        whereDetalle.put("venta_id", String.valueOf(idVenta));
        ArrayList<Map<String, String>> whereDetalleList = new ArrayList<>();
        whereDetalleList.add(whereDetalle);
        tcdet.deleteReg(whereDetalleList);

        JOptionPane.showMessageDialog(this, "Registro borrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return 0;
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al borrar el registro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}

private boolean confirmarBorrado() {
    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea borrar este registro?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
    return confirm == JOptionPane.YES_OPTION;
}

    
@Override
public int imNuevo() {
    // Reiniciar datos
    this.resetData();
    this.limpiarTabla();
    this.imInsFilas();

    // Cargar último ID de venta
    cargarUltimoId();
    this.fillView(myData, columnData, clienteData);
     cargarDetallesTalonarioInicio();
    return 0;
}


// Método para obtener el último ID de venta desde la base de datos
private int obtenerUltimoIdVenta() {
    int ultimoId = 0;
    try {
        ultimoId = tc.getMaxId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoId;
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

    // Realiza la búsqueda de la cabecera de la Nota de Crédito
    Map<String, String> resultadoCabecera = this.tc.searchById(myData);
    System.out.println("Nota de Crédito imBuscar " + resultadoCabecera);

    // Limpia la tabla de la vista
    this.limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        System.out.println("No hay registros que mostrar");
        cargarDetallesTalonarioInicio();
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualiza myData con los resultados de la búsqueda de la cabecera
    this.myData = resultadoCabecera;

    // Obtener serie del talonario y número de documento
    String idTalonario = myData.get("talonario_id");
    String serieComprobante = null;
    if (idTalonario != null && !idTalonario.isEmpty()) {
        Map<String, String> whereTalonario = new HashMap<>();
        whereTalonario.put("id", idTalonario);

        Map<String, String> fieldsTalonario = new HashMap<>();
        fieldsTalonario.put("serie_comprobante", "serie_comprobante");

        List<Map<String, String>> resultTalonario = tt.searchListById(fieldsTalonario, whereTalonario);
        if (!resultTalonario.isEmpty()) {
            serieComprobante = resultTalonario.get(0).get("serie_comprobante");
            myData.put("serie", serieComprobante);
        }
    }
    String idNotaCredito = myData.get("id");
    String nroDocumento = myData.get("nro_documento");
    if (nroDocumento == null || nroDocumento.isEmpty()) {
        nroDocumento = String.valueOf(obtenerProximoNroDocumento(idNotaCredito));
        myData.put("nro_documento", nroDocumento);
    }

    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("notacredito_id", this.myData.get("id"));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para el producto especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return -1; // Indicador de que no se encontraron detalles
    }

    // Convierte el resultado a ArrayList y actualiza columnData
    this.columnData = new ArrayList<>(detalles);

    // Obtener datos del cliente
    int clienteId = Integer.parseInt(myData.get("cliente_id"));
    clienteData = buscarClientePorVenta(clienteId);
    if (clienteData == null) {
        JOptionPane.showMessageDialog(this, "No se encontraron datos del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de error
    }

    // Obtener datos de productos para el IVA y producto
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
                fieldsProducto.put("impuesto", "impuesto");

                List<Map<String, String>> resultProducto = tproductos.searchListById(fieldsProducto, whereProducto);
                if (!resultProducto.isEmpty()) {
                    Map<String, String> producto = resultProducto.get(0);
                    detalle.put("iva", producto.get("impuesto"));
                    detalle.put("producto", producto.get("producto"));
                }
            }
        }
    }

    // Llena la vista con los datos recuperados
    this.fillView(myData, columnData, clienteData);

    return 0; // Indicador de que la búsqueda fue exitosa
}

@Override
public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

    if (lastFocusedComponent == IdNota) {
        List<String> columnasParaVentas = Arrays.asList("id", "factura");
        Form_Buscar buscadorVentas = new Form_Buscar(parentFrame, true, tc, "NOTAS_CREDITOS", columnasParaVentas);
        buscadorVentas.setOnItemSeleccionadoListener(this);
        buscadorVentas.setVisible(true);
    } else if (lastFocusedComponent == RUCCliente) {
        List<String> columnasParaClientes = Arrays.asList("id", "cliente");
        Form_Buscar buscadorClientes = new Form_Buscar(parentFrame, true, tmClientes, "clientes", columnasParaClientes);
        buscadorClientes.setOnItemSeleccionadoListener(this);
        buscadorClientes.setVisible(true);
    } else if (lastFocusedComponent == jtDetalle) {
        int selectedRow = jtDetalle.getSelectedRow();
        int selectedColumn = jtDetalle.getSelectedColumn();
        if (selectedRow != -1 && selectedColumn == 1) { // Verificar si la columna es la de Cod Barra
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
    this.myData = this.tc.navegationReg(IdNota.getText(), "FIRST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    monedaListenerEnabled = false; 
    this.fillView(myData, columnData, clienteData);
    imBuscar();
    return 0;
    
}

@Override
public int imSiguiente() {
    if (IdNota.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdNota.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData, clienteData);
    imBuscar();
    return 0;
}

@Override
public int imAnterior() {
    if (IdNota.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdNota.getText(), "PRIOR");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros en esta dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData, clienteData);
    imBuscar();
    return 0;
}

@Override
public int imUltimo() {
    this.myData = this.tc.navegationReg(IdNota.getText(), "LAST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
     monedaListenerEnabled = false; 
    this.fillView(myData, columnData, clienteData);
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
                job.setJobName("Ventas");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }
@Override
public int imInsFilas() {
    int currentRow = jtDetalle.getSelectedRow();

    if (currentRow == -1) {
        int lastRow = jtDetalle.getRowCount() - 1;
        if (lastRow >= 0) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("Cod Barra", jtDetalle.getValueAt(lastRow, 1) != null ? jtDetalle.getValueAt(lastRow, 1).toString() : "");
            rowData.put("Descripción", jtDetalle.getValueAt(lastRow, 2) != null ? jtDetalle.getValueAt(lastRow, 2).toString() : "");
            rowData.put("Cantidad", jtDetalle.getValueAt(lastRow, 8) != null ? jtDetalle.getValueAt(lastRow, 8).toString() : "");
            rowData.put("Precio", jtDetalle.getValueAt(lastRow, 3) != null ? jtDetalle.getValueAt(lastRow, 3).toString() : "");
            rowData.put("%IVA", jtDetalle.getValueAt(lastRow, 4) != null ? jtDetalle.getValueAt(lastRow, 4).toString() : "");
            rowData.put("Descuento", jtDetalle.getValueAt(lastRow, 7) != null ? jtDetalle.getValueAt(lastRow, 7).toString() : "");
            rowData.put("Base", jtDetalle.getValueAt(lastRow, 5) != null ? jtDetalle.getValueAt(lastRow, 5).toString() : "");
            rowData.put("Total", jtDetalle.getValueAt(lastRow, 9) != null ? jtDetalle.getValueAt(lastRow, 9).toString() : "");
            rowData.put("id", jtDetalle.getValueAt(lastRow, 10) != null ? jtDetalle.getValueAt(lastRow, 10).toString() : "");

            if (isRowInvalid(rowData)) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.", "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return -1;
            }
        }
        int nextItemNumber = jtDetalle.getRowCount() + 1;
        modelo.addRow(new Object[]{nextItemNumber, "0", "0", "0", "0", "0", "0", "0", "0", "0"});
        currentRow = jtDetalle.getRowCount() - 1;
        jtDetalle.setRowSelectionInterval(currentRow, currentRow);
        return 0;
    }

    Map<String, String> rowData = new HashMap<>();
    rowData.put("Cod Barra", jtDetalle.getValueAt(currentRow, 1) != null ? jtDetalle.getValueAt(currentRow, 1).toString() : "");
    rowData.put("Cantidad", jtDetalle.getValueAt(currentRow, 8) != null ? jtDetalle.getValueAt(currentRow, 8).toString() : "");
    rowData.put("Precio", jtDetalle.getValueAt(currentRow, 3) != null ? jtDetalle.getValueAt(currentRow, 3).toString() : "");
    rowData.put("%IVA", jtDetalle.getValueAt(currentRow, 4) != null ? jtDetalle.getValueAt(currentRow, 4).toString() : "");
    rowData.put("Descuento", jtDetalle.getValueAt(currentRow, 7) != null ? jtDetalle.getValueAt(currentRow, 7).toString() : "");
    rowData.put("Base", jtDetalle.getValueAt(currentRow, 5) != null ? jtDetalle.getValueAt(currentRow, 5).toString() : "");
    rowData.put("Total", jtDetalle.getValueAt(currentRow, 9) != null ? jtDetalle.getValueAt(currentRow, 9).toString() : "");
    rowData.put("id", jtDetalle.getValueAt(currentRow, 10) != null ? jtDetalle.getValueAt(currentRow, 10).toString() : "");

if (isRowInvalid(rowData)) {
        String msg = "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.";
        System.out.println(msg);
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
    } else {
        int nextItemNumber = jtDetalle.getRowCount() + 1;
        modelo.addRow(new Object[]{nextItemNumber, "0", "0", "0", "0", "0", "0", "0", "0", "0"});

        this.jtDetalle.requestFocus();

        int toRow = this.jtDetalle.getRowCount() - 1;
        this.jtDetalle.changeSelection(toRow, 0, false, false);
    }
    return 0;
}


private boolean isRowInvalid(Map<String, String> row) {
    for (Map.Entry<String, String> entry : row.entrySet()) {
        String column = entry.getKey();
        String value = entry.getValue();
        if ("Descuento".equals(column)) {
            continue; // Descuento puede ser cero
        }
        if (value == null || value.isEmpty() || "0".equals(value)) {
            return true; // Si cualquier otra columna está vacía o es cero, la fila es inválida
        }
    }
    return false; // La fila es válida
}
    @Override
    public int imDelFilas() {
             int selectedRow = jtDetalle.getSelectedRow();

        if (selectedRow != -1) {
            ((DefaultTableModel) this.jtDetalle.getModel()).removeRow(selectedRow);

            int rowCount = jtDetalle.getRowCount();
            if (rowCount > 0) {
                jtDetalle.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna fila seleccionada para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        return 0;
    }

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    if (lastFocusedComponent == IdNota) {
        String idStr = datosSeleccionados.get("Codigo");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idNotaCredito = Integer.parseInt(idStr);
                List<Map<String, String>> registrosCabecera = tc.buscarPorIdGenerico("NOTAS_DE_CREDITO", "id", idNotaCredito);

                if (!registrosCabecera.isEmpty()) {
                    Map<String, String> registroCabecera = registrosCabecera.get(0);

                    int talonarioId = Integer.parseInt(registroCabecera.get("talonario_id"));
                    int clienteId = Integer.parseInt(registroCabecera.get("cliente_id"));
                    List<Map<String, String>> registrosDetalle = tc.buscarPorIdGenerico("NOTAS_DE_CREDITO_DETALLE", "notacredito_id", idNotaCredito);
                    List<Map<String, String>> registrosCliente = tmClientes.buscarPorIdGenerico("CLIENTES", "id", clienteId);
                    List<Map<String, String>> registrosTalonario = tt.buscarPorIdGenerico("TALONARIOS", "id", talonarioId);

                    if (!registrosCliente.isEmpty() && !registrosTalonario.isEmpty()) {
                        Map<String, String> registroCliente = registrosCliente.get(0);
                        Map<String, String> registroTalonario = registrosTalonario.get(0);

                        SwingUtilities.invokeLater(() -> {
                            // Llenar la cabecera
                            IdNota.setText(idStr);
                            RUCCliente.setText(registroCabecera.get("cliente_id"));
                            RUCCliente.setText(registroCliente.get("nrodocumento"));
                            Clientes.setText(registroCliente.get("cliente") + " " + registroCliente.get("apellido"));
                            RUCCliente.setText(registroCliente.get("celular"));
                            Direccion.setText(registroCliente.get("direccion"));

                            // Formatear y llenar el número de factura
                            String serie = registroCabecera.get("serie");
                            String nroDocumento = formatearNumeroFactura(registroCabecera.get("nro_documento"));
                            NotaCredito.setText(serie + "-" + nroDocumento);

                            // Llenar datos del talonario
                            Timbrado.setText(registroTalonario.get("numero_timbrado"));
                            FechaInicioVigencia.setText(registroTalonario.get("fecha_inicio_timbrado"));
                            FechaFinVigencia.setText(registroTalonario.get("fecha_final_timbrado"));
                            
                            // Llenar moneda y depósito
                            String monedaId = registroCabecera.get("moneda_id");
                            String depositoId = registroCabecera.get("deposito_id");
                            Functions.E_estado(Deposito, "DEPOSITOS", "id=" + depositoId);
                            Functions.E_estado(Moneda, "MONEDAS", "id=" + monedaId);

                            try {
                                Fecha.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(registroCabecera.get("fechaProceso")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Llenar la tabla de detalles
                            DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
                            model.setRowCount(0); // Limpiar tabla

                            int itemCount = 1;
                            for (Map<String, String> detalle : registrosDetalle) {
                                String codBarras = detalle.get("productodetalle_id");
                                BigDecimal precio = new BigDecimal(detalle.get("precio"));
                                BigDecimal base = new BigDecimal(detalle.get("base"));
                                BigDecimal impuesto = new BigDecimal(detalle.get("impuesto"));
                                BigDecimal descuento = new BigDecimal(detalle.get("descuento"));
                                BigDecimal cantidad = new BigDecimal(detalle.get("cantidad"));
                                BigDecimal total = new BigDecimal(detalle.get("total"));

                                // Obtener detalles adicionales del producto desde la tabla PRODUCTOS
                                Map<String, String> whereProductoDetalle = new HashMap<>();
                                whereProductoDetalle.put("codigobarras", codBarras);

                                Map<String, String> fieldsProductoDetalle = new HashMap<>();
                                fieldsProductoDetalle.put("cabecera_id", "cabecera_id");

                                List<Map<String, String>> resultProductoDetalle = tproductosdet.searchListById(fieldsProductoDetalle, whereProductoDetalle);
                                if (!resultProductoDetalle.isEmpty()) {
                                    String productoId = resultProductoDetalle.get(0).get("cabecera_id");

                                    Map<String, String> whereProducto = new HashMap<>();
                                    whereProducto.put("id", productoId);

                                    Map<String, String> fieldsProducto = new HashMap<>();
                                    fieldsProducto.put("producto", "producto");
                                    fieldsProducto.put("impuesto", "impuesto");

                                    List<Map<String, String>> resultProducto = tproductos.searchListById(fieldsProducto, whereProducto);
                                    if (!resultProducto.isEmpty()) {
                                        Map<String, String> producto = resultProducto.get(0);
                                        String descripcion = producto.get("producto");
                                        BigDecimal iva = new BigDecimal(producto.get("impuesto"));

                                        model.addRow(new Object[]{
                                            itemCount++,
                                            codBarras,
                                            descripcion,
                                            precio,
                                            iva,
                                            base,
                                            impuesto,
                                            descuento,
                                            cantidad,
                                            total
                                        });
                                        actualizarDecimales();
                                    }
                                }
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontraron datos del cliente o talonario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró una Nota de Crédito con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    resetData();
                    limpiarTabla();
                    imInsFilas();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID de Nota de Crédito inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


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
  // Implementa la funcionalidad si es necesaria
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    // Implementa la funcionalidad si es necesaria
    }

@Override
public void mouseEntered(MouseEvent e) {
    // No hacer nada por ahora
}

    @Override
    public void mouseExited(MouseEvent e) {
       // Implementa la funcionalidad si es necesaria
    }

    @Override
    public void keyTyped(KeyEvent e) {
         // Implementa la funcionalidad si es necesaria
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
    } else {
        if (numeros) {
            if (jtDetalle.getModel().isCellEditable(row, col)) {
                this.limpiarCelda(jtDetalle);
            }
        }
    }

    if (key == 10 || key == 9 || (key >= 37 && key <= 40)) {
        if (jtDetalle.isEditing()) {
            jtDetalle.getCellEditor().stopCellEditing();
        }

        if (col == 0) {
            return;
        }

        if (col == 8 && key == 10 && (row == (rows - 1))) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("Cod. Barra", this.jtDetalle.getValueAt(row, 1).toString());
            rowData.put("Cantidad", this.jtDetalle.getValueAt(row, 7).toString());
            rowData.put("Precio", this.jtDetalle.getValueAt(row, 3).toString());
            rowData.put("%IVA", this.jtDetalle.getValueAt(row, 4).toString());
            rowData.put("Descuento", this.jtDetalle.getValueAt(row, 6).toString());
            rowData.put("Base", this.jtDetalle.getValueAt(row, 5).toString());
            rowData.put("Total", this.jtDetalle.getValueAt(row, 8).toString());
            rowData.put("Lote", this.jtDetalle.getValueAt(row, 9).toString());
            rowData.put("Vencimiento", this.jtDetalle.getValueAt(row, 10).toString());

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
       
    }


}
