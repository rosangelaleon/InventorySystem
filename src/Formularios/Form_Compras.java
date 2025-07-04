
package Formularios;
import Controllers.DBConexion;
import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.CodigoBarraFilter;
import Filtros.DateEditor;
import Filtros.DecimalDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.RucDocumentFilter;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import Modelo.CompraDetalle;
import Modelo.cargaComboBox;
import java.awt.Color;
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
import java.util.Locale;
import javax.swing.JDesktopPane;

public class Form_Compras extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario {
    private DBTableController tc;
    private DBTableController tt;
    private DBTableController tprecio;
    private DBTableController tprecios;
    private DBTableController tm;
    private DBTableController tmCoti;
    private DBTableController tcdet;
    private DBTableController tproductos;
    private DBTableController tproductosdet;
    private DBTableController tmProveedores;
    private DBTableController tmMonedas;
    private DBTableController tmDepositos;
    private DBTableController tmCuotas;
    private ModeloTabla modelo;
    private ArrayList<Map<String, String>> columnData;
     private Map<String, String> ProveedorData;
    private Map<String, String> myData;
      private HashMap<String, String> myDet;
    private ArrayList<CompraDetalle> listaDetalles;
    private Component lastFocusedComponent;
    // Declaración de las variables totales
    private BigDecimal totalBruto = BigDecimal.ZERO;
    private BigDecimal totalNeto = BigDecimal.ZERO;
    private BigDecimal totalIva = BigDecimal.ZERO;
    private BigDecimal totalExenta = BigDecimal.ZERO;
    private boolean monedaListenerEnabled = true;
    private Map<String, BigDecimal> preciosOriginales = new HashMap<>(); // Para almacenar los precios originales
private String monedaIdInicial;
private String proveedorIdActual; // Variable para almacenar el proveedor actual
      private boolean esBusquedaManual = true;
public Form_Compras() {
        initComponents();
        initializeTextFields();
        initializeTableListeners();
        IdCompras.setText("0");
        Factura.setText("0");
        IdProveedor.setText("0");
        RUCProveedores.setText("0");
        Neto.setText("0");
        Exento.setText("0");
        Bruto.setText("0");
        Impuesto.setText("0");
        listaDetalles = new ArrayList<>();
        ProveedorData = new HashMap<>();
        myData = new HashMap<>();
        columnData = new ArrayList<>();
        tc = new DBTableController();
        tc.iniciar("COMPRAS");
        
        tcdet = new DBTableController();
        tcdet.iniciar("COMPRAS_DETALLE");
        tproductos = new DBTableController();
        tproductos .iniciar("Productos");
       tproductosdet= new DBTableController();
        tproductosdet .iniciar("Productos_Detalle");
       tmProveedores = new DBTableController();
        tmProveedores.iniciar("Proveedores");
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
        cargaComboBox.pv_cargar(Deposito, "DEPOSITOS", "id, deposito", "id", "");
        cargaComboBox.pv_cargar(Moneda, "MONEDAS", "id, moneda", "id", "");
        cargaComboBox.pv_cargar(Cuota, "CUOTAS", "id, descripcion", "id", "");
        cargarUltimoId();
        construirTabla();
        applyFiltersToColumns();
        agregarTipoDocumento();
        jtDetalle.addMouseListener(this);
        jtDetalle.addKeyListener(this);
        jtDetalle.setOpaque(false);
        jtDetalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        
       cargarListenerModeloTabla() ;
     
         manejarCambioMoneda();
 
         Factura.addFocusListener(new FocusAdapter() {
    @Override
    public void focusLost(FocusEvent e) {
        String factura = Factura.getText().trim(); // Obtiene el texto del campo
        String regex = "\\d{3}-\\d{3}-\\d{7}"; // Formato: xxx-xxx-xxxxxxx

        if (!factura.matches(regex)) {
            JOptionPane.showMessageDialog(null, "El formato de la factura es inválido. Debe ser xxx-xxx-xxxxxxx.", 
                                          "Error de Validación", JOptionPane.ERROR_MESSAGE);
       Factura.setText("0");
        }
    }
});

    }
private void cargarCotizacion(String monedaId) {
    try {
        // Si la moneda es "Guaraní" (id = 1), no realizar ninguna acción
        if ("1".equals(monedaId)) {
            Cotizaciones.setText(""); // Limpiar el JLabel si es necesario
            return;
        }

        // Obtener la fecha actual
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Consultar cotización desde la base de datos
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("moneda_id", monedaId);
        whereClause.put("activo", "1"); // Filtrar solo cotizaciones activas

        List<Map<String, String>> resultado = tmCoti.searchListById(fields, whereClause);

        System.out.println("Resultado de la consulta: " + resultado);

        if (!resultado.isEmpty()) {
            // Obtener los datos de la cotización
            Map<String, String> cotizacionData = resultado.get(0);
            String cotizacionCompraStr = cotizacionData.get("compra");
            String cotizacionVentaStr = cotizacionData.get("venta");
            String fechaCotizacion = cotizacionData.get("fecha");

            // Convertir las cotizaciones a enteros
            int cotizacionCompra = (int) Double.parseDouble(cotizacionCompraStr); // Convertir a entero
            int cotizacionVenta = (int) Double.parseDouble(cotizacionVentaStr);   // Convertir a entero

            // Verificar si la fecha de la cotización coincide con la fecha actual
            if (!fechaHoy.equals(fechaCotizacion)) {
                JOptionPane.showMessageDialog(this,
                    "No existe una cotización para esta moneda en la fecha actual (" + fechaHoy + ").",
                    "Cotización no disponible", JOptionPane.ERROR_MESSAGE);
                Cotizaciones.setText(""); // Limpiar el JLabel
                return;
            }

            // Mostrar cotización en el JLabel si es válida y actualizada
            Cotizaciones.setText("Compra: " + cotizacionCompra + ", Venta: " + cotizacionVenta);
        } else {
            // Mostrar mensaje si no hay cotización para la moneda seleccionada
            JOptionPane.showMessageDialog(this,
                "No existe una cotización para la moneda seleccionada.",
                "Cotización no disponible", JOptionPane.ERROR_MESSAGE);
            Cotizaciones.setText(""); // Limpiar el JLabel
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error al cargar la cotización: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
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
        applyRucFilter(Factura);
        applyNumericFilter(IdCompras);
        applyRucFilter(RUCProveedores);
        applyNumericFilter(IdProveedor);
        addFocusListeners(); 
        addKeyListeners();
    }

    private void addFocusListeners() {
        Factura.addFocusListener(new DefaultFocusListener(Factura, true));
         IdCompras.addFocusListener(new DefaultFocusListener(IdCompras, true));
         IdProveedor.addFocusListener(new DefaultFocusListener( IdProveedor, true));
        RUCProveedores.addFocusListener(new DefaultFocusListener(RUCProveedores, true));
 
    }

    
private void addKeyListeners() {
    FocusListener focusTracker = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = e.getComponent();
        }
    };
    Factura.addFocusListener(focusTracker);
    IdCompras.addFocusListener(focusTracker);
    IdProveedor.addFocusListener(focusTracker);
    RUCProveedores.addFocusListener(focusTracker);

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
    titulosList.add("Lote");
    titulosList.add("Vencimiento");
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

private ArrayList<CompraDetalle> consultarListaDetalles() {
    listaDetalles.add(new CompraDetalle(1, 0, "0", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "0", null));
    return listaDetalles;
}

private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
    Object[][] informacion = new Object[listaDetalles.size()][titulosList.size()];
 // Inicializar toda la matriz con "0"
    for (int i = 0; i < informacion.length; i++) {
        for (int j = 0; j < informacion[i].length; j++) {
            informacion[i][j] = "0";
        }
    }

    for (int x = 0; x < informacion.length; x++) {
        informacion[x][0] = (x + 1); // #Item
        informacion[x][1] = listaDetalles.get(x).getString("codigobarras")!= null ? listaDetalles.get(x).getString("codigobarras") : "0";
        informacion[x][2] = listaDetalles.get(x).getString("producto")!= null ? listaDetalles.get(x).getString("producto") : "0";
        informacion[x][3] = listaDetalles.get(x).getBigDecimal("precio");
        informacion[x][4] = listaDetalles.get(x).getBigDecimal("impuesto");
        informacion[x][5] = listaDetalles.get(x).getBigDecimal("base");
        informacion[x][6] = listaDetalles.get(x).getBigDecimal("impuesto");
        informacion[x][7] = listaDetalles.get(x).getBigDecimal("descuento");
        informacion[x][8] = listaDetalles.get(x).getBigDecimal("cantidad");
        informacion[x][9] = listaDetalles.get(x).getBigDecimal("total");
        informacion[x][10] = listaDetalles.get(x).getString("lote");
        informacion[x][11] = listaDetalles.get(x).getDate("vencimiento")!= null ? listaDetalles.get(x).getString("vencimiento") : "0"; // Usar el objeto Date
        informacion[x][12] = listaDetalles.get(x).getId();
    }
    return informacion;
}

private void construirTabla(String[] titulos, Object[][] data) {
    // Lista de columnas no editables (ajustada para Compras)
    ArrayList<Integer> noEditable = new ArrayList<>(List.of(0, 2, 3, 4, 5, 6, 9, 12));
    modelo = new ModeloTabla(data, titulos, noEditable);
    jtDetalle.setModel(modelo);

    // Configurar renderizadores para las columnas
    jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("texto"));   // #Item
    jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("numerico")); // Cod Barra
    jtDetalle.getColumnModel().getColumn(2).setCellRenderer(new GestionCeldas("texto"));   // Descripción
    jtDetalle.getColumnModel().getColumn(3).setCellRenderer(new GestionCeldas("numerico")); // Precio
    jtDetalle.getColumnModel().getColumn(4).setCellRenderer(new GestionCeldas("numerico")); // %IVA
    jtDetalle.getColumnModel().getColumn(5).setCellRenderer(new GestionCeldas("numerico")); // Base
    jtDetalle.getColumnModel().getColumn(6).setCellRenderer(new GestionCeldas("numerico")); // Impuesto
    jtDetalle.getColumnModel().getColumn(7).setCellRenderer(new GestionCeldas("numerico")); // Descuento
    jtDetalle.getColumnModel().getColumn(8).setCellRenderer(new GestionCeldas("numerico")); // Cantidad
    jtDetalle.getColumnModel().getColumn(9).setCellRenderer(new GestionCeldas("numerico")); // Total
    jtDetalle.getColumnModel().getColumn(10).setCellRenderer(new GestionCeldas("texto"));   // Lote

    // Configurar editor para "Vencimiento" (columna 11)
    jtDetalle.getColumnModel().getColumn(11).setCellEditor(new DateEditor()); // Editable con JDateChooser

    // Ocultar la columna del ID (columna 12)
    jtDetalle.getColumnModel().getColumn(12).setMinWidth(0);
    jtDetalle.getColumnModel().getColumn(12).setMaxWidth(0);
    jtDetalle.getColumnModel().getColumn(12).setWidth(0);
    jtDetalle.getColumnModel().getColumn(12).setPreferredWidth(0);

    // Configuración visual de la tabla
    jtDetalle.getTableHeader().setReorderingAllowed(false); // Evitar reordenar columnas
    jtDetalle.setRowHeight(25); // Ajustar altura uniforme de las filas
    jtDetalle.setGridColor(new java.awt.Color(0, 0, 0)); // Color de las líneas separadoras

    // Ajustar ancho de columnas
    jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(100);  // #Item
    jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(150);  // Cod Barra
    jtDetalle.getColumnModel().getColumn(2).setPreferredWidth(250);  // Descripción
    jtDetalle.getColumnModel().getColumn(3).setPreferredWidth(150);  // Precio
    jtDetalle.getColumnModel().getColumn(4).setPreferredWidth(100);  // %IVA
    jtDetalle.getColumnModel().getColumn(5).setPreferredWidth(150);  // Base
    jtDetalle.getColumnModel().getColumn(6).setPreferredWidth(150);  // Impuesto
    jtDetalle.getColumnModel().getColumn(7).setPreferredWidth(150);  // Descuento
    jtDetalle.getColumnModel().getColumn(8).setPreferredWidth(150);  // Cantidad
    jtDetalle.getColumnModel().getColumn(9).setPreferredWidth(150);  // Total
    jtDetalle.getColumnModel().getColumn(10).setPreferredWidth(150); // Lote
    jtDetalle.getColumnModel().getColumn(11).setPreferredWidth(200); // Vencimiento

    // Personalizar encabezado de la tabla

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
if (productoDetalle == null || productoDetalle.isEmpty()) {
    if (esBusquedaManual) {
        JOptionPane.showMessageDialog(null, "El código de barras ingresado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    imDelFilas(); // Eliminar la fila errónea
    imInsFilas(); // Insertar una fila vacía
    return;
}


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
    where.put("activo", "1"); // Solo buscar productos activos

    Map<String, String> fields = new HashMap<>();
    fields.put("producto", "producto");
    fields.put("impuesto", "impuesto");

    // Buscar productos activos
    List<Map<String, String>> result = tproductos.searchListById(fields, where);

    if (!result.isEmpty()) {
        return result.get(0); // Retornar el primer producto activo encontrado
    }

    // Si no se encuentra un producto activo, buscar sin el filtro de activo
    where.remove("activo"); // Eliminar el filtro de activo
    List<Map<String, String>> todosProductos = tproductos.searchListById(fields, where);

    if (!todosProductos.isEmpty()) {
        // Si el producto existe pero está inactivo
        JOptionPane.showMessageDialog(null,
            "El producto con ID " + productoId + " existe pero está inactivo.",
            "Producto Inactivo", JOptionPane.WARNING_MESSAGE);
        imDelFilas();
        imInsFilas();
        return null; // Manejar según tu lógica (puedes retornar null o continuar)
    }

    // Si no se encuentra ningún producto
    JOptionPane.showMessageDialog(null,
        "El producto con ID " + productoId + " no existe.",
        "Producto No Encontrado", JOptionPane.ERROR_MESSAGE);
            imDelFilas();
        imInsFilas();
    return null;
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

// Método para manejar la selección de la moneda
private void manejarCambioMoneda() {
    Moneda.addItemListener(e -> {
        if (monedaListenerEnabled && e.getStateChange() == ItemEvent.SELECTED) {
            // Obtener la moneda seleccionada
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
            cargarCotizacion(selectedMonedaId);

            // Recalcular los valores en la tabla
            for (int row = 0; row < jtDetalle.getRowCount(); row++) {
                try {
                    String codBarra = jtDetalle.getValueAt(row, 1).toString();

                    // Guardar el precio original si no está almacenado ya
                    if (!preciosOriginales.containsKey(codBarra)) {
                        preciosOriginales.put(codBarra, getBigDecimalFromTable(jtDetalle, row, 3));
                    }

                    // Actualizar precio basado en la nueva cotización
                    actualizarPrecio(row);

                    // Calcular valores de la fila (subtotal, impuestos, etc.)
                    calcularValores(row);
                } catch (Exception ex) {
                    // Manejo de errores en caso de problemas con una fila específica
                    System.err.println("Error al actualizar la fila " + row + ": " + ex.getMessage());
                }
            }

            // Actualizar totales después de recalcular
            actualizarTotales();
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


 private void setData() {
    // Cabecera
    myData.put("id", IdCompras.getText());
    myData.put("proveedor_id", IdProveedor.getText());
    myData.put("factura", Factura.getText());

    // Verificar y formatear fechas
    Date fechaFacturaDate = FechaFactura.getDate();

    if (fechaFacturaDate != null) {
        myData.put("fechaFactura", new SimpleDateFormat("yyyy-MM-dd").format(fechaFacturaDate));
    } else {
        myData.put("fechaFactura", ""); // O un valor por defecto
    }

    myData.put("moneda_id", Functions.ExtraeCodigo(Moneda.getSelectedItem().toString()));
    myData.put("deposito_id", Functions.ExtraeCodigo(Deposito.getSelectedItem().toString()));
  // Manejar cuota_id para que sea "0" si el valor es "0-Seleccionar"
String cuotaId =  Functions.ExtraeCodigo(Cuota.getSelectedItem().toString());
System.out.println("id cuota: " + cuotaId);

// Manejar casos en los que cuotaId es nulo o igual a "0"
if (cuotaId == null || cuotaId.equals("0")) {
    myData.put("cuota_id", ""); // Asignar cadena vacía como valor predeterminado
} else {
    myData.put("cuota_id", cuotaId);
}
    myData.put("total_neto", Neto.getText().replace(".", "").replace(",", "."));
    myData.put("total_exento", Exento.getText().replace(".", "").replace(",", "."));
    myData.put("total_impuesto", Impuesto.getText().replace(".", "").replace(",", "."));
    myData.put("total_bruto", Bruto.getText().replace(".", "").replace(",", "."));

    String tipoDocumentoSeleccionado = (String) TipoDocumento.getSelectedItem();
    if ("1-Contado".equals(tipoDocumentoSeleccionado)) {
        myData.put("tipo_documento", "0");
    } else if ("2-Crédito".equals(tipoDocumentoSeleccionado)) {
        myData.put("tipo_documento", "1");
    } else {
        myData.put("tipo_documento", ""); // O manejarlo de otra manera si es necesario
    }

    // Obtener y almacenar el ID de la Compra
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int filasTabla = model.getRowCount();
    columnData.clear(); // Limpiar los datos anteriores

    int ultimoId = obtenerUltimoIdCompraDetalle(); // Obtener el último ID existente en la tabla DETALLE

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();

        // Obtener el ID actual de la fila si existe
        String idDetalle;
        Object idCell = model.getValueAt(i, 12); // Suponiendo que el ID está en la columna índice 10
        if (idCell == null || idCell.toString().trim().isEmpty() || idCell.toString().trim().equals("0")) {
            // Generar un nuevo ID si no existe
            idDetalle = String.valueOf(++ultimoId);
            model.setValueAt(idDetalle, i, 12); // Asignar el nuevo ID a la celda correspondiente
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
        String id = IdCompras.getText();
        myDet.put("id", idDetalle);
        myDet.put("compra_id", id);
        myDet.put("productodetalle_id", rowData.get("Cod Barra"));
        myDet.put("impuesto", rowData.get("%IVA"));
        myDet.put("producto", rowData.get("Descripción"));
        myDet.put("cantidad", rowData.get("Cantidad"));
        myDet.put("precio", rowData.get("Precio").replace(".", "").replace(",", "."));
        myDet.put("impuesto", rowData.get("Impuesto").replace(".", "").replace(",", "."));
        myDet.put("descuento", rowData.get("Descuento").replace(".", "").replace(",", "."));
        myDet.put("base", rowData.get("Base").replace(".", "").replace(",", "."));
        myDet.put("total", rowData.get("Total").replace(".", "").replace(",", "."));
         myDet.put("lote", rowData.get("Lote"));
         // Manejo del campo vencimiento
    String vencimiento = rowData.get("Vencimiento");
    if (vencimiento != null && !vencimiento.trim().isEmpty()) {
        try {
            // Formatear y validar el campo vencimiento
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // Validación estricta
            Date date = dateFormat.parse(vencimiento.trim());
            vencimiento = dateFormat.format(date); // Asegurar formato correcto
            myDet.put("vencimiento", vencimiento);
        } catch (ParseException e) {
            System.err.println("Error al parsear el vencimiento: " + vencimiento + " - " + e.getMessage());
            myDet.put("vencimiento", ""); // O un valor predeterminado
        }
    } else {
        myDet.put("vencimiento", ""); // Manejo para valores nulos o vacíos
    }
        this.columnData.add(this.myDet);
    }

    // Imprimir los valores de columnData para verificación
    System.out.println("Valores de columnData después de llenar:");
    for (Map<String, String> myRow : columnData) {
        System.out.println(myRow);
    }
}
private int obtenerUltimoIdCompraDetalle() {
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
    TipoDocumento.setSelectedIndex(0);
    Cuota.setSelectedIndex(0);
    Deposito.setSelectedIndex(0);
    Moneda.setSelectedIndex(0);
    Cotizaciones.setText("");
    this.myData = new HashMap<String, String>();
    this.myData.put("id", "0");
    this.myData.put("proveedor_id", "0");
    this.ProveedorData.put("proveedor", "");
    this.ProveedorData.put("tipodocumento", "");
    this.ProveedorData.put("nrodocumento", "0");
    this.ProveedorData.put("divisoria", "");
    this.ProveedorData.put("celular", "");
    this.ProveedorData.put("direccion", "");
    this.myData.put("factura", "0");
    this.myData.put("fechaFactura", "");
    this.myData.put("moneda_id", "0");
    this.myData.put("deposito_id", "0");
    this.myData.put("cuota_id", "0");
    this.myData.put("total_neto", "0");
    this.myData.put("total_exento", "0");
    this.myData.put("total_impuesto", "0");
    this.myData.put("total_bruto", "0");
    // Detalle
    this.myDet = new HashMap<String, String>();
    this.myDet.put("id", "0");
    this.myDet.put("compra_id", "0");
    this.myDet.put("productodetalle_id", "0");
    this.myDet.put("cantidad", "0");
    this.myDet.put("precio", "0");
    this.myDet.put("impuesto", "0");
    this.myDet.put("descuento", "0");
    this.myDet.put("base", "0");
    this.myDet.put("total", "0");
    this.myDet.put("vencimiento", "0");  // Vencimiento del lote
    this.myDet.put("lote", "0");  // Lote del producto

    this.columnData.add(this.myDet);
}
private void fillView(Map<String, String> data, List<Map<String, String>> colData, Map<String, String> proveedorData) {
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        switch (key) {
            case "id":
                IdCompras.setText(value);
                break;
            case "proveedor_id":
                IdProveedor.setText(value);
                Proveedores.setText(proveedorData.get("proveedor"));
                String tipoDocumento = proveedorData.get("tipodocumento");
                String documento = proveedorData.get("nrodocumento");
                String divisoria = proveedorData.get("divisoria");

                if ("RUC".equals(tipoDocumento) && divisoria != null) {
                    RUCProveedores.setText(documento + "-" + divisoria);
                } else {
                    RUCProveedores.setText(documento);
                }

                Direccion.setText(proveedorData.get("direccion"));
                Telefono.setText(proveedorData.get("celular"));
                break;
            case "fechaFactura":
                String strFecha = data.get("fechaFactura");
                if (strFecha != null && !strFecha.isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strFecha);
                        FechaFactura.setDate(date);
                    } catch (ParseException e) {
                        FechaFactura.setDate(null);
                        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    FechaFactura.setDate(null);
                }
                break;
            case "moneda_id":
                Functions.E_estado(Moneda, "MONEDAS", "id=" + value);
                actualizarDecimales(); // Llamar a actualizar decimales
                break;
            case "deposito_id":
                Functions.E_estado(Deposito, "DEPOSITOS", "id=" + value);
                break;
            case "cuota_id":
                
                if (value == null || value.trim().isEmpty()) {
        // Si no hay un valor válido para cuota_id, seleccionar "0-Seleccionar"
        Cuota.setSelectedIndex(0); // Esto asume que "0-Seleccionar" es la primera opción
        System.out.println("Cuota restablecida a '0-Seleccionar'");
    } else {
        // Usar Functions.E_estado para establecer el valor correspondiente
        Functions.E_estado(Cuota, "CUOTAS", "id=" + value);
        System.out.println("Valor de cuota_id recuperado y asignado: " + value);
    }
                break;
            case "tipo_documento":
                if ("0".equals(value)) {
                    TipoDocumento.setSelectedItem("1-Contado");
                } else if ("1".equals(value)) {
                    TipoDocumento.setSelectedItem("2-Crédito");
                } else {
                    TipoDocumento.setSelectedItem("0-Seleccionar");
                }
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
            case "factura":
                Factura.setText(value);
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
        String lote = myRow.get("lote");
        String vencimiento = myRow.get("vencimiento");
if (vencimiento != null && !vencimiento.isEmpty()) {
    try {
        // Define el formato esperado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Asegura que las fechas sean estrictamente válidas

        // Parsear y formatear la fecha para asegurar el formato correcto
        Date date = dateFormat.parse(vencimiento);
        vencimiento = dateFormat.format(date); // Reformatea al formato deseado si fuera necesario

        // Asignar la fecha al componente FechaFactura
        FechaFactura.setDate(date);
    } catch (ParseException e) {
        FechaFactura.setDate(null);
        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
    }
} else {
    FechaFactura.setDate(null);
}
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
            lote,
            vencimiento,
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


private Map<String, String> cargarMonedas() {
    Map<String, String> monedas = new HashMap<>();
    List<Map<String, String>> result = tmMonedas.searchListById(new HashMap<>(), new HashMap<>());

    for (Map<String, String> moneda : result) {
        monedas.put(moneda.get("id"), moneda.get("moneda"));
    }
    return monedas;
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

// Método para obtener los datos del proveedor
private Map<String, String> obtenerDatosProveedores(String ProveedorId) {
    Map<String, String> where = new HashMap<>();
    where.put("id", ProveedorId);

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    List<Map<String, String>> ProveedorList = this.tmProveedores.searchListById(fields, where);
    if (ProveedorList != null && !ProveedorList.isEmpty()) {
        return ProveedorList.get(0);
    } else {
        return new HashMap<>();
    }
}

private Map<String, String> buscarProveedorPorId(int ProveedorId) {
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(ProveedorId)); // Buscar por ID

    // Recuperar proveedores activos
    where.put("activo", "1"); // Solo proveedores activos

    Map<String, String> fields = new HashMap<>();
    fields.put("id", "id");
    fields.put("proveedor", "proveedor");
    fields.put("nrodocumento", "nrodocumento");
    fields.put("divisoria", "divisoria");
    fields.put("direccion", "direccion");
    fields.put("celular", "celular");
    fields.put("tipodocumento", "tipodocumento");

    // Buscar proveedores activos
    List<Map<String, String>> result = tmProveedores.searchListById(fields, where);

    if (!result.isEmpty()) {
        return result.get(0); // Retorna el proveedor activo
    }

    // Verificar si el proveedor existe pero no está activo
    where.remove("activo"); // Eliminar el filtro de activo
    List<Map<String, String>> todosProveedores = tmProveedores.searchListById(fields, where);

    if (!todosProveedores.isEmpty()) {
        JOptionPane.showMessageDialog(null,
            "El proveedor existe pero no está activo.",
            "Proveedor Inactivo", JOptionPane.WARNING_MESSAGE);
        return null; // Devuelve null o maneja el flujo como desees
    }

    // Si no existe ningún proveedor, preguntar si se desea registrar uno
    int option = JOptionPane.showConfirmDialog(null, "Proveedor no encontrado. ¿Desea registrarlo?", "Registrar Proveedor", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
        abrirFormularioProveedor();
    }
    return null;
}

private Map<String, String> buscarProveedorPorDocumento(String nroDocumento, String divisoria) {
    Map<String, String> where = new HashMap<>();
    where.put("nrodocumento", nroDocumento);
    if (divisoria != null) {
        where.put("divisoria", divisoria);
    }
    where.put("activo", "1"); // Solo buscar proveedores activos

    Map<String, String> fields = new HashMap<>();
    fields.put("id", "id");
    fields.put("proveedor", "proveedor");
    fields.put("nrodocumento", "nrodocumento");
    fields.put("divisoria", "divisoria");
    fields.put("direccion", "direccion");
    fields.put("celular", "celular");
    fields.put("tipodocumento", "tipodocumento");

    // Buscar proveedores activos
    List<Map<String, String>> result = tmProveedores.searchListById(fields, where);

    if (!result.isEmpty()) {
        return result.get(0); // Retorna el proveedor activo si se encuentra
    }

    // Verificar si el proveedor existe pero está inactivo
    where.remove("activo"); // Eliminar el filtro de activo
    List<Map<String, String>> todosProveedores = tmProveedores.searchListById(fields, where);

    if (!todosProveedores.isEmpty()) {
        JOptionPane.showMessageDialog(null,
            "El proveedor existe pero no está activo.",
            "Proveedor Inactivo", JOptionPane.WARNING_MESSAGE);
        return null; // O manejarlo según tu lógica
    }

    // Si no existe ningún proveedor, preguntar si desea registrarlo
    int option = JOptionPane.showConfirmDialog(null, "Proveedor no encontrado. ¿Desea registrarlo?", "Registrar Proveedor", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
        abrirFormularioProveedor();
    }
    return null; // No se encontró Proveedor y no se va a registrar uno nuevo
}

private void abrirFormularioProveedor() {
    try {
        // Crear una nueva instancia del formulario de Proveedor
        Form_Proveedores formProveedores = new Form_Proveedores();
        
        // Obtener el DesktopPane desde Form_Compras (JInternalFrame)
        JDesktopPane desktopPane = this.getDesktopPane();

        if (desktopPane != null) {
            // Añadir el formulario de Proveedor al DesktopPane
            desktopPane.add(formProveedores);

            // Ajustar la capa del Form_Proveedores para que esté encima
            desktopPane.setLayer(formProveedores, JDesktopPane.PALETTE_LAYER); // Coloca el formulario en una capa superior

            // Configurar las propiedades del formulario de Proveedor
            formProveedores.setVisible(true);
            formProveedores.setClosable(true);
            formProveedores.setIconifiable(true);
            formProveedores.setResizable(true);

            // Traer el formulario de proveedores al frente
            formProveedores.moveToFront();
            formProveedores.setSelected(true); // Selecciona el formulario para darle foco
            formProveedores.grabFocus(); // Forzar el foco en el formulario de proveedores

            // Manejar excepción de selección en caso de que ocurra
            if (!formProveedores.isSelected()) {
                formProveedores.setSelected(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el DesktopPane. El formulario de proveedores no se puede abrir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir el formulario de Proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

  private void agregarTipoDocumento() {
    TipoDocumento.removeAllItems(); 
    TipoDocumento.addItem("0-Seleccionar");
    TipoDocumento.addItem("1-Contado");
    TipoDocumento.addItem("2-Crédito");
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
        jtDetalle.getColumnModel().getColumn(10).setCellEditor(new DefaultCellEditor(decimalField1) {
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
        lbl_IdCompras = new javax.swing.JLabel();
        lbl_Proveedor = new javax.swing.JLabel();
        lbl_UltimoId = new javax.swing.JLabel();
        lbl_RUC = new javax.swing.JLabel();
        Ultimo = new javax.swing.JLabel();
        lbl_Condicion = new javax.swing.JLabel();
        Deposito = new javax.swing.JComboBox<>();
        TipoDocumento = new javax.swing.JComboBox<>();
        lbl_deposito = new javax.swing.JLabel();
        Moneda = new javax.swing.JComboBox<>();
        Cuota = new javax.swing.JComboBox<>();
        lbl_Moneda = new javax.swing.JLabel();
        lbl_cuotas = new javax.swing.JLabel();
        PanelTotales = new javax.swing.JPanel();
        lbl_Neto = new javax.swing.JLabel();
        lbl_Exento = new javax.swing.JLabel();
        lbl_Impuesto = new javax.swing.JLabel();
        lbl_Factura = new javax.swing.JLabel();
        Impuesto = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        Bruto = new javax.swing.JLabel();
        Exento = new javax.swing.JLabel();
        Neto = new javax.swing.JLabel();
        Detalles = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();
        lbl_direccion = new javax.swing.JLabel();
        RUCProveedores = new javax.swing.JTextField();
        Proveedores = new javax.swing.JLabel();
        IdProveedor = new javax.swing.JTextField();
        Telefono = new javax.swing.JLabel();
        lbl_FechaFactura = new javax.swing.JLabel();
        FechaFactura = new com.toedter.calendar.JDateChooser();
        lbl_FechaProceso = new javax.swing.JLabel();
        lbl_telefono = new javax.swing.JLabel();
        Direccion = new javax.swing.JLabel();
        IdCompras = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        Cotizaciones = new javax.swing.JLabel();
        lbl_Condicion1 = new javax.swing.JLabel();
        Factura = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Compras");
        setPreferredSize(new java.awt.Dimension(1005, 475));

        PanelPrincipal.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));
        PanelPrincipal.setMinimumSize(new java.awt.Dimension(785, 540));
        PanelPrincipal.setPreferredSize(new java.awt.Dimension(785, 540));
        PanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_IdCompras.setText("  N° Registro");
        PanelPrincipal.add(lbl_IdCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 20));

        lbl_Proveedor.setText("Proveedor");
        PanelPrincipal.add(lbl_Proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 20));

        lbl_UltimoId.setText(" Ultimo");
        PanelPrincipal.add(lbl_UltimoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 40, 20));

        lbl_RUC.setText("CI/RUC");
        PanelPrincipal.add(lbl_RUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 170, 50, 20));

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);
        PanelPrincipal.add(Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 150, 22));

        lbl_Condicion.setText("Cotización");
        PanelPrincipal.add(lbl_Condicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 60, 30));

        PanelPrincipal.add(Deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 140, -1));

        TipoDocumento.setToolTipText("");
        TipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoDocumentoActionPerformed(evt);
            }
        });
        PanelPrincipal.add(TipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, 140, -1));

        lbl_deposito.setText("Depósito");
        PanelPrincipal.add(lbl_deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, -1, -1));

        Moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonedaActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 150, 140, -1));

        Cuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CuotaActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 140, -1));

        lbl_Moneda.setText("  Moneda");
        PanelPrincipal.add(lbl_Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, -1, 20));

        lbl_cuotas.setText("     Cuotas ");
        PanelPrincipal.add(lbl_cuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, 60, -1));

        PanelTotales.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales"));
        PanelTotales.setPreferredSize(new java.awt.Dimension(200, 230));

        lbl_Neto.setText("Total Neto");

        lbl_Exento.setText("Total Exento");

        lbl_Impuesto.setText("Total Impuesto");

        lbl_Factura.setText("Total Factura");

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
                        .addComponent(lbl_Factura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelTotalesLayout.createSequentialGroup()
                        .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelTotalesLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_Neto, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_Exento, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18))
                            .addGroup(PanelTotalesLayout.createSequentialGroup()
                                .addComponent(lbl_Impuesto)
                                .addGap(14, 14, 14)))
                        .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Impuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Neto, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(Exento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTotalesLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lbl_Factura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelTotalesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        PanelPrincipal.add(PanelTotales, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 230, 230));

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
                "#Item", "Cod Barra", "Descripción", "Precio", "%IVA", "Base", "Descuento", "Cantidad", "Total", "Lote", "Vencimiento"
            }
        ));
        jtDetalle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));
        jtDetalle.setPreferredSize(new java.awt.Dimension(990, 100));
        jtDetalle.setRowSelectionAllowed(false);
        jtDetalle.setShowGrid(true);
        jtDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtDetalleKeyPressed(evt);
            }
        });
        Detalles.setViewportView(jtDetalle);

        PanelPrincipal.add(Detalles, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 980, 180));

        lbl_direccion.setText("Dirección");
        PanelPrincipal.add(lbl_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 60, 20));

        RUCProveedores.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                RUCProveedoresFocusLost(evt);
            }
        });
        RUCProveedores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RUCProveedoresKeyPressed(evt);
            }
        });
        PanelPrincipal.add(RUCProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 140, -1));

        Proveedores.setBackground(new java.awt.Color(204, 204, 255));
        Proveedores.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Proveedores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Proveedores.setOpaque(true);
        PanelPrincipal.add(Proveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 290, 22));

        IdProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdProveedorFocusLost(evt);
            }
        });
        IdProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdProveedorActionPerformed(evt);
            }
        });
        IdProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdProveedorKeyPressed(evt);
            }
        });
        PanelPrincipal.add(IdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 70, -1));

        Telefono.setBackground(new java.awt.Color(204, 204, 255));
        Telefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Telefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Telefono.setOpaque(true);
        PanelPrincipal.add(Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 150, 22));

        lbl_FechaFactura.setText("Fecha");
        PanelPrincipal.add(lbl_FechaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 70, -1, 20));
        PanelPrincipal.add(FechaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 150, -1));

        lbl_FechaProceso.setText("  Factura");
        PanelPrincipal.add(lbl_FechaProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 70, 50, 20));

        lbl_telefono.setText("Teléfono");
        PanelPrincipal.add(lbl_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 60, 20));

        Direccion.setBackground(new java.awt.Color(204, 204, 255));
        Direccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Direccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Direccion.setOpaque(true);
        PanelPrincipal.add(Direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 370, 22));

        IdCompras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdComprasFocusLost(evt);
            }
        });
        IdCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdComprasActionPerformed(evt);
            }
        });
        IdCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdComprasKeyPressed(evt);
            }
        });
        PanelPrincipal.add(IdCompras, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 140, -1));
        PanelPrincipal.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 246, 460, 10));
        PanelPrincipal.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 460, 10));

        Cotizaciones.setBackground(new java.awt.Color(204, 204, 255));
        Cotizaciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Cotizaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Cotizaciones.setOpaque(true);
        PanelPrincipal.add(Cotizaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 220, 22));

        lbl_Condicion1.setText("Tipo Documento");
        PanelPrincipal.add(lbl_Condicion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, -1, 20));

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
        PanelPrincipal.add(Factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 140, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 985, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 440, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdProveedorActionPerformed

    private void CuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CuotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CuotaActionPerformed

    private void IdComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdComprasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdComprasActionPerformed

    private void IdComprasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdComprasKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }  
    }//GEN-LAST:event_IdComprasKeyPressed

    private void IdProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdProveedorKeyPressed
                                      
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        try {
            int ProveedorId = Integer.parseInt(IdProveedor.getText());
            if (ProveedorId > 0) {
                Map<String, String> proveedorData = buscarProveedorPorId(ProveedorId);
                if (proveedorData != null) {
                    String tipoDocumento = proveedorData.get("tipodocumento"); // Asume que el campo 'tipodocumento' está disponible en los datos del Proveedor
                    String nroDocumento = proveedorData.get("nrodocumento");
                    String divisoria = proveedorData.get("divisoria");
                    
                    IdProveedor.setText(proveedorData.get("id"));
                    Proveedores.setText(proveedorData.get("proveedor"));
                    
                    if ("RUC".equals(tipoDocumento)) {
                        RUCProveedores.setText(nroDocumento + "-" + divisoria);
                    } else {
                        RUCProveedores.setText(nroDocumento);
                    }
                    
                    Direccion.setText(proveedorData.get("direccion"));
                    Telefono.setText(proveedorData.get("celular"));
                } else {
                    IdProveedor.setText("");
                    Proveedores.setText("");
                    RUCProveedores.setText("");
                    Direccion.setText("");
                    Telefono.setText("");
                }
            } else {
                Proveedores.setText("");
                RUCProveedores.setText("");
                Direccion.setText("");
                Telefono.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    }//GEN-LAST:event_IdProveedorKeyPressed

    private void RUCProveedoresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RUCProveedoresFocusLost
     
    }//GEN-LAST:event_RUCProveedoresFocusLost

    private void jtDetalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtDetalleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtDetalleKeyPressed

    private void IdComprasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdComprasFocusLost

    }//GEN-LAST:event_IdComprasFocusLost

    private void IdProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdProveedorFocusLost
  
    }//GEN-LAST:event_IdProveedorFocusLost

    private void RUCProveedoresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RUCProveedoresKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String rucInput = RUCProveedores.getText().trim();
        if (!rucInput.isEmpty()) {
            String[] parts = rucInput.split("-");
            String nroDocumento = parts[0];
            String divisoria = parts.length > 1 ? parts[1] : null;

            Map<String, String> proveedorData = buscarProveedorPorDocumento(nroDocumento, divisoria);
            if (proveedorData != null) {
                String tipoDocumento = proveedorData.get("tipodocumento");
                String documento = proveedorData.get("nrodocumento");
                String div = proveedorData.get("divisoria");

                IdProveedor.setText(proveedorData.get("id"));
               Proveedores.setText(proveedorData.get("proveedor"));

                if ("RUC".equals(tipoDocumento) && div != null) {
                    RUCProveedores.setText(documento + "-" + div);
                } else {
                    RUCProveedores.setText(documento);
                }

                Direccion.setText(proveedorData.get("direccion"));
                Telefono.setText(proveedorData.get("celular"));
            } else {
                IdProveedor.setText("");
                Proveedores.setText("");
                RUCProveedores.setText("");
                Direccion.setText("");
                Telefono.setText("");
            }
        } else {
            RUCProveedores.setText("");
            Direccion.setText("");
            Telefono.setText("");
        }
    }
    }//GEN-LAST:event_RUCProveedoresKeyPressed

    private void TipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TipoDocumentoActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Bruto;
    private javax.swing.JLabel Cotizaciones;
    private javax.swing.JComboBox<String> Cuota;
    private javax.swing.JComboBox<String> Deposito;
    private javax.swing.JScrollPane Detalles;
    private javax.swing.JLabel Direccion;
    private javax.swing.JLabel Exento;
    private javax.swing.JTextField Factura;
    private com.toedter.calendar.JDateChooser FechaFactura;
    private javax.swing.JTextField IdCompras;
    private javax.swing.JTextField IdProveedor;
    private javax.swing.JLabel Impuesto;
    private javax.swing.JComboBox<String> Moneda;
    private javax.swing.JLabel Neto;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelTotales;
    private javax.swing.JLabel Proveedores;
    private javax.swing.JTextField RUCProveedores;
    private javax.swing.JLabel Telefono;
    private javax.swing.JComboBox<String> TipoDocumento;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JLabel lbl_Condicion;
    private javax.swing.JLabel lbl_Condicion1;
    private javax.swing.JLabel lbl_Exento;
    private javax.swing.JLabel lbl_Factura;
    private javax.swing.JLabel lbl_FechaFactura;
    private javax.swing.JLabel lbl_FechaProceso;
    private javax.swing.JLabel lbl_IdCompras;
    private javax.swing.JLabel lbl_Impuesto;
    private javax.swing.JLabel lbl_Moneda;
    private javax.swing.JLabel lbl_Neto;
    private javax.swing.JLabel lbl_Proveedor;
    private javax.swing.JLabel lbl_RUC;
    private javax.swing.JLabel lbl_UltimoId;
    private javax.swing.JLabel lbl_cuotas;
    private javax.swing.JLabel lbl_deposito;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_telefono;
    // End of variables declaration//GEN-END:variables

  @Override
public int imGuardar(String crud) {
    System.out.println("Iniciando guardado de la cabecera y detalles...");
   
    setData();
 System.out.println("Datos de la cabecera (myData): " + myData);
 
    // Validar tipo_documento y cuota_id
    String tipoDocumento = myData.get("tipo_documento");
    String cuotaId = myData.get("cuota_id");

    if ("1".equals(tipoDocumento)) { // Si es crédito
        if (cuotaId == null || cuotaId.isEmpty() || "0-Seleccionar".equals(cuotaId)) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cuota si el documento es de tipo CRÉDITO.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    } else if ("0".equals(tipoDocumento)) { // Si es contado
        if (cuotaId != null && !cuotaId.isEmpty() && !"0-Seleccionar".equals(cuotaId)) {
            JOptionPane.showMessageDialog(this, "No puede seleccionar cuotas si el documento es de tipo CONTADO.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        // Eliminar cuota_id si está presente
        myData.remove("cuota_id");
    }
    // Lista de columnas obligatorias en el detalle
    List<String> columnasObligatoriasDetalle = Arrays.asList("Cod Barra", "Cantidad", "Precio", "Impuesto", "Base", "Total","Lote","Vencimiento");

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
                imNuevo();
                return -1;
            }
        }
    }

    // Verificar valores de cabecera en myData
    List<String> columnasObligatoriasCabecera = Arrays.asList("id","proveedor_id", "total_neto", "total_bruto", "total_impuesto", "moneda_id", "deposito_id", "factura", "fechaFactura");
    for (String columna : columnasObligatoriasCabecera) {
        String valor = myData.get(columna);
        if (valor == null || valor.isEmpty() || valor.equals("0")) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos: " + columna, "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }
        String valort = myData.get("tipo_documento");
        if (valort == null || valort.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos: " + valort, "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
        
System.out.println("Antes de guardarCabecera - ID en myData: " + myData.get("id"));
    // Si llega aquí, todas las verificaciones están completas, proceder al guardado
    if (!guardarCabecera(Integer.parseInt(myData.get("id")))) {
        JOptionPane.showMessageDialog(this, "Error al guardar la cabecera.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
    System.out.println("Después de guardarCabecera - ID en myData: " + myData.get("id"));
   System.out.println("Datos antes de guardarDetalle:");
System.out.println("myData: " + myData);
System.out.println("columnData: " + columnData);
    guardarDetalle(Integer.parseInt(myData.get("id")));

    imNuevo();
    return 0;
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

private boolean guardarCabecera(int idCabecera) {
    if (!myData.containsKey("cuota_id") || myData.get("cuota_id").isEmpty()|| Cuota.equals("0-Seleccionar")) {
        myData.remove("cuota_id");
    }
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    alCabecera.add(myData);

    // Respaldar el ID
    String idBackup = myData.get("id");

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

    // Reestablecer el ID si fue eliminado
    if (!myData.containsKey("id")) {
        myData.put("id", idBackup);
    }
    return true;
}

private void guardarDetalle(int idCabecera) {
    try {
        // Consulta para obtener los detalles existentes
        System.out.println("GuardarDetalle - ID recibido: " + idCabecera);
        Map<String, String> where = new HashMap<>();
        where.put("compra_id", String.valueOf(idCabecera));

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
            myRow.put("compra_id", String.valueOf(idCabecera));
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
        JOptionPane.showMessageDialog(this, "Registro de detalles guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        JOptionPane.showMessageDialog(this, "La cabecera seleccionada no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que hay detalles asociados a esta cabecera
    Map<String, String> whereDetalle = new HashMap<>();
    whereDetalle.put("compra_id", myData.get("id")); // Asegúrate de que este sea el campo correcto que identifica los detalles de esta venta específica

    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, whereDetalle);
    if (detallesExistentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No existen detalles asociados a esta cabecera.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    // Eliminar detalles y cabecera
    try {
        boolean detallesEliminados = true;
        boolean cabeceraEliminada = false;

        // **Eliminar detalles relacionados**
        if (!detallesExistentes.isEmpty()) {
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>(detallesExistentes);
            int rowsDeleted = tcdet.deleteReg(alDetalle);
            if (rowsDeleted == detallesExistentes.size()) {
                System.out.println("Detalles eliminados correctamente.");
            } else {
                System.err.println("Error al eliminar algunos detalles.");
                detallesEliminados = false;
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
        if (cabeceraEliminada && (detallesExistentes.isEmpty() || detallesEliminados)) {
            JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            imNuevo(); // Reinicia la vista
            return 0;
        } else {
            throw new Exception("Error durante la eliminación.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al borrar el registro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
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
    this.fillView(myData, columnData, ProveedorData);
     cargarUltimoId();
    return 0;
}


// Método para obtener el último ID de Compra desde la base de datos
private int obtenerUltimoIdCompra() {
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
System.out.println("Datos después de setData: " + myData);

    // Verificar si el ID es 0
    if (myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "El ID 0 no es válido. Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
        return -1; // Indicador de error
    }
  
     System.out.println("ImBuscar - IdCompras antes de búsqueda: " + IdCompras.getText());
   
   
    // Realiza la búsqueda de la cabecera
    Map<String, String> resultadoCabecera = this.tc.searchById(myData);
    System.out.println("Compras imBuscar " + resultadoCabecera);

    // Limpia la tabla de la vista
    this.limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        System.out.println("No hay registros que mostrar");
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        imNuevo();
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualiza myData con los resultados de la búsqueda de la cabecera
    this.myData = resultadoCabecera;
System.out.println("ImBuscar - myData actualizado con cabecera: " + this.myData);

  
    String id1= myData.get("id");
    // Obtener la factura
    String nroDocumento = myData.get("factura");
    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("compra_id", this.myData.get("id"));

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

    // Obtener datos del proveedor
    ProveedorData = buscarProveedorPorId(Integer.parseInt(myData.get("proveedor_id")));
    if (ProveedorData == null) {
        JOptionPane.showMessageDialog(this, "No se encontraron datos del proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
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
    this.fillView(myData, columnData, ProveedorData);

    return 0; // Indicador de que la búsqueda fue exitosa
}
@Override
public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    esBusquedaManual = false; // Indicar que no es una búsqueda manual
    if (lastFocusedComponent == IdCompras) {
        List<String> columnasParaCompra = Arrays.asList("id", "factura");
        Form_Buscar buscadorCompra = new Form_Buscar(parentFrame, true, tc, "Compras", columnasParaCompra);
        buscadorCompra.setOnItemSeleccionadoListener(this);
        buscadorCompra.setVisible(true);
    } else if (lastFocusedComponent == IdProveedor) {
        List<String> columnasParaProveedor = Arrays.asList("id", "proveedor");
        Form_Buscar buscadorProveedor = new Form_Buscar(parentFrame, true, tmProveedores, "Proveedores", columnasParaProveedor);
        buscadorProveedor.setOnItemSeleccionadoListener(this);
        buscadorProveedor.setVisible(true);
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
                    esBusquedaManual = true; // Indicar que no es una búsqueda manual
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
    this.myData = this.tc.navegationReg(IdCompras.getText(), "FIRST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
    monedaListenerEnabled = false; 
    this.fillView(myData, columnData, ProveedorData);
    imBuscar();
    return 0;
    
}

@Override
public int imSiguiente() {
    if (IdCompras.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdCompras.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData, ProveedorData);
    imBuscar();
    return 0;
}

@Override
public int imAnterior() {
    if (IdCompras.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdCompras.getText(), "PRIOR");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros en esta dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.limpiarTabla();
    this.fillView(myData, columnData, ProveedorData);
    imBuscar();
    return 0;
}

@Override
public int imUltimo() {
    this.myData = this.tc.navegationReg(IdCompras.getText(), "LAST");
    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }
    this.limpiarTabla();
     monedaListenerEnabled = false; 
    this.fillView(myData, columnData, ProveedorData);
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
                job.setJobName("Compras");
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
            rowData.put("lote", jtDetalle.getValueAt(lastRow, 10) != null ? jtDetalle.getValueAt(lastRow, 10).toString() : "");

            Object vencimientoCell = jtDetalle.getValueAt(lastRow, 11);
            System.out.println("Valor original en la celda de vencimiento: " + vencimientoCell);

            if (vencimientoCell != null && vencimientoCell instanceof Date) {
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format((Date) vencimientoCell);
                rowData.put("vencimiento", formattedDate);
            } else if (vencimientoCell != null) {
                rowData.put("vencimiento", vencimientoCell.toString().trim());
            } else {
                rowData.put("vencimiento", ""); // Valor por defecto si no hay fecha
            }

            rowData.put("id", jtDetalle.getValueAt(lastRow, 12) != null ? jtDetalle.getValueAt(lastRow, 12).toString() : "");

            System.out.println("Datos de la fila antes de validar: " + rowData);

            if (isRowInvalid(rowData)) {
                System.out.println("La fila es inválida por algún campo vacío o incorrecto.");
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.", "ATENCIÓN...!", JOptionPane.OK_OPTION);
                return -1;
            }
        }
        int nextItemNumber = jtDetalle.getRowCount() + 1;
        modelo.addRow(new Object[]{nextItemNumber, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
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
    rowData.put("lote", jtDetalle.getValueAt(currentRow, 10) != null ? jtDetalle.getValueAt(currentRow, 10).toString() : "");

    Object vencimientoCell = jtDetalle.getValueAt(currentRow, 11);
    System.out.println("Valor original en la celda de vencimiento: " + vencimientoCell);

    if (vencimientoCell != null && vencimientoCell instanceof Date) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format((Date) vencimientoCell);
        rowData.put("vencimiento", formattedDate);
    } else if (vencimientoCell != null) {
        rowData.put("vencimiento", vencimientoCell.toString().trim());
    } else {
        rowData.put("vencimiento", ""); // Valor por defecto si no hay fecha
    }

    rowData.put("id", jtDetalle.getValueAt(currentRow, 12) != null ? jtDetalle.getValueAt(currentRow, 12).toString() : "");

    System.out.println("Datos de la fila actual antes de validar: " + rowData);

    if (isRowInvalid(rowData)) {
        String msg = "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.";
        System.out.println("Validación fallida: " + msg);
        JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
        return -1;
    } else {
        int nextItemNumber = jtDetalle.getRowCount() + 1;
        modelo.addRow(new Object[]{nextItemNumber, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});

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
    if (lastFocusedComponent == IdCompras) {
        String idStr = datosSeleccionados.get("Codigo");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idCompra = Integer.parseInt(idStr);
                List<Map<String, String>> registrosCabecera = tc.buscarPorIdGenerico("Compras", "id", idCompra);

                if (!registrosCabecera.isEmpty()) {
                    Map<String, String> registroCabecera = registrosCabecera.get(0);

                    int ProveedorId = Integer.parseInt(registroCabecera.get("proveedor_id"));
                    List<Map<String, String>> registrosDetalle = tc.buscarPorIdGenerico("COMPRAS_DETALLE", "compra_id", idCompra);
                    List<Map<String, String>> registrosProveedor = tmProveedores.buscarPorIdGenerico("Proveedores", "id", ProveedorId);


                    if (!registrosProveedor.isEmpty()) {
                        Map<String, String> registroProveedor = registrosProveedor.get(0);

                        SwingUtilities.invokeLater(() -> {
                            // Llenar la cabecera
                            IdCompras.setText(idStr);
                            IdProveedor.setText(registroCabecera.get("proveedor_id"));
                            RUCProveedores.setText(registroProveedor.get("nrodocumento"));
                            Proveedores.setText(registroProveedor.get("proveedor"));
                            Telefono.setText(registroProveedor.get("celular"));
                            Direccion.setText(registroProveedor.get("direccion"));

                            // Formatear y llenar el número de factura
                            String factura = registroCabecera.get("factura");
                            Factura.setText(factura);

                            String monedaId = registroCabecera.get("moneda_id");
                            String DepositoId = registroCabecera.get("deposito_id");
                            String cuotaId = registroCabecera.get("cuota_id");
                            if ( cuotaId !=null ) {
                                Functions.E_estado(Cuota, "CUOTAS", "id=" + cuotaId);
                               }
                            String value =registroCabecera.get("tipo_documento");
                            if ("0".equals(value)) {
                              TipoDocumento.setSelectedItem("1-Contado");
                            } else if ("1".equals(value)) {
                                 TipoDocumento.setSelectedItem("2-Crédito");
                            } else {
                                  TipoDocumento.setSelectedItem("0-Seleccionar");
                            }
                             Functions.E_estado(Deposito, "DEPOSITOS", "id=" + DepositoId);
                             Functions.E_estado(Moneda, "MONEDAS", "id=" + monedaId);

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
                        JOptionPane.showMessageDialog(null, "No se encontraron datos del proveedor", "Información", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró una Compra con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                       imNuevo();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "ID de Compra inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else if (lastFocusedComponent == IdProveedor) {
        String idStr = datosSeleccionados.get("Codigo"); 
        String nombre = datosSeleccionados.get("Descripcion"); 

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idProveedor = Integer.parseInt(idStr);
                Map<String, String> proveedorData = buscarProveedorPorId(idProveedor );

                if (proveedorData != null) {
                    String ruc = proveedorData.get("nrodocumento");
                    String divisoria = proveedorData.get("divisoria");
                    String direccion = proveedorData.get("direccion");
                    String telefono = proveedorData.get("celular");
                    String tipoDocumento = proveedorData.get("tipodocumento");

                    IdProveedor.setText(idStr);
                    Proveedores.setText(nombre);

                    if ("RUC".equals(tipoDocumento)) {
                        RUCProveedores.setText(ruc + "-" + divisoria);
                    } else {
                        RUCProveedores.setText(ruc);
                    }

                    Direccion.setText(direccion);
                    Telefono.setText(telefono);
                } else {
                    JOptionPane.showMessageDialog(this, "ID del proveedor inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID del proveedor inválido.", "Error", JOptionPane.ERROR_MESSAGE);
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
            rowData.put("Lote", this.jtDetalle.getValueAt(row, 10).toString());
           
    Object vencimientoValue = this.jtDetalle.getValueAt(row, 11);
    if (vencimientoValue instanceof Date) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format((Date) vencimientoValue);
        rowData.put("Vencimiento", formattedDate);
    } else if (vencimientoValue != null) {
        rowData.put("Vencimiento", vencimientoValue.toString());
    } else {
        rowData.put("Vencimiento", "");
    }


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
