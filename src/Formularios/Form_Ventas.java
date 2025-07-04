
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
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
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

public class Form_Ventas extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario {
    private DBTableController tc;
    private DBTableController tt;
    private DBTableController tprecio;
     private DBTableController tstocks;
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
    private boolean esBusquedaManual = true;

    // Declaración de las variables totales
    private BigDecimal totalBruto = BigDecimal.ZERO;
    private BigDecimal totalNeto = BigDecimal.ZERO;
    private BigDecimal totalIva = BigDecimal.ZERO;
    private BigDecimal totalExenta = BigDecimal.ZERO;
    private boolean monedaListenerEnabled = true;
    private Map<String, BigDecimal> preciosOriginales = new HashMap<>(); // Para almacenar los precios originales
private String monedaIdInicial;
private String clienteIdActual; // Variable para almacenar el cliente actual
    public Form_Ventas() {
        initComponents();
        initializeTextFields();
        initializeTableListeners();
        IdVentas.setText("0");
        IdClientes.setText("0");
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
        tstocks = new DBTableController();
        tstocks .iniciar("STOCKS");
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
        // Desactiva el checkbox para evitar que el usuario lo modifique
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
             // Cargar detalles del talonario
        cargarDetallesTalonarioInicio();
         manejarCambioMoneda();
manejarCambioCliente() ;
        configurarListenerCantidad() ;
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
               
                Moneda.setSelectedItem("0-Seleccionar");
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
            Moneda.setSelectedItem("0-Seleccionar");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error al cargar la cotización: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void cargarDetallesTalonarioInicio() {
    try {
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        // Inicializa whereClause correctamente
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("activo", "true");  // Filtrar talonarios activos
        whereClause.put("tipo_comprobante", "1");  // Filtrar tipo de comprobante = 1 (Factura)

        System.out.println("Ejecutando búsqueda de talonarios...");
        List<Map<String, String>> resultado = tt.searchListById(fields, whereClause);
        System.out.println("Resultado de la búsqueda: " + resultado);

        if (!resultado.isEmpty()) {
            Map<String, String> talonarioData = resultado.get(0);
            System.out.println("Datos del talonario recuperados: " + talonarioData);
            Timbrado.setText(talonarioData.get("numero_timbrado"));
            FechaInicioVigencia.setText(talonarioData.get("fecha_inicio_timbrado"));
            FechaFinVigencia.setText(talonarioData.get("fecha_final_timbrado"));
            Factura.setText(talonarioData.get("serie_comprobante")+"-");

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
        applyNumericFilter(IdVentas);
        applyRucFilter(RUCCliente);
        applyNumericFilter(IdClientes);
        addFocusListeners(); 
        addKeyListeners();
    }

    private void addFocusListeners() {
        IdVentas.addFocusListener(new DefaultFocusListener(IdVentas, true));
         IdClientes.addFocusListener(new DefaultFocusListener( IdClientes, true));
        RUCCliente.addFocusListener(new DefaultFocusListener(RUCCliente, true));
 
    }

    
private void addKeyListeners() {
    FocusListener focusTracker = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            lastFocusedComponent = e.getComponent();
        }
    };

    IdVentas.addFocusListener(focusTracker);
    IdClientes.addFocusListener(focusTracker);
    RUCCliente.addFocusListener(focusTracker);

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
                if (productoDetalle == null || productoDetalle.isEmpty()) {
    JOptionPane.showMessageDialog(null, "El código de barras ingresado no existe:", "Error", JOptionPane.ERROR_MESSAGE);
    imDelFilas(); // Eliminar la fila errónea
    imInsFilas(); // Insertar una fila vacía
    return;
}

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

private BigDecimal obtenerStockActual(String codBarra, int depositoId) {
    // Crear el mapa para la condición WHERE
    Map<String, String> whereStocks = new HashMap<>();
    whereStocks.put("producto_detalle", codBarra);
    whereStocks.put("deposito_id", String.valueOf(depositoId));

    // Especificar las columnas a recuperar
    Map<String, String> fieldsStocks = new HashMap<>();
    fieldsStocks.put("stockActual", "stockActual");

    // Buscar en la tabla STOCKS
    List<Map<String, String>> resultStocks = tstocks.searchListById(fieldsStocks, whereStocks);
    
    if (resultStocks.isEmpty()) {
        return BigDecimal.ZERO; // Retorna 0 si no encuentra el registro
    }

    // Obtener el valor de stockActual
    return new BigDecimal(resultStocks.get(0).get("stockActual"));
}
private void configurarListenerCantidad() {
    jtDetalle.getModel().addTableModelListener(e -> {
        int row = e.getFirstRow();
        int column = e.getColumn();

        // Verificar si la columna editada es la de cantidad (índice correspondiente)
        if (column == 8) { // Cambia este índice al índice real de tu columna "Cantidad"
            validarStockCantidad(row);
        }
    });
}
private void validarStockCantidad(int row) {
    try {
        // Obtener los valores necesarios
        String codBarra = jtDetalle.getValueAt(row, 1).toString(); // Código de barras
        String depositoSeleccionadoStr = Functions.ExtraeCodigo(Deposito.getSelectedItem().toString());
        int depositoSeleccionado = Integer.parseInt(depositoSeleccionadoStr);

        // Obtener el stock actual desde la base de datos
        BigDecimal stockActual = obtenerStockActual(codBarra, depositoSeleccionado);

        // Obtener la cantidad ingresada por el usuario
        BigDecimal cantidadIngresada = new BigDecimal(jtDetalle.getValueAt(row, 8).toString());

        // Validar si la cantidad ingresada es mayor al stock
        if (cantidadIngresada.compareTo(stockActual) > 0) {
            JOptionPane.showMessageDialog(null,
                "Stock insuficiente en el depósito seleccionado. Stock disponible: " + stockActual,
                "Error", JOptionPane.ERROR_MESSAGE);

            // Restablecer el valor anterior
            jtDetalle.setValueAt(stockActual.toString(), row, 8);
        limpiarTabla();
        imInsFilas();
        return;
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null,
            "El formato del valor ingresado no es válido. Por favor, ingrese un número.",
            "Error", JOptionPane.ERROR_MESSAGE);
        jtDetalle.setValueAt("0", row, 8); // Restablecer a 0 si hay un error
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Error al validar el stock: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

private void llenarCampos(int row, String codBarra) {
    if (Moneda.getSelectedItem() == null || Moneda.getSelectedItem().toString().equals("0-Seleccionar")) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione una moneda antes de proceder.", "Error", JOptionPane.ERROR_MESSAGE);
        limpiarTabla();
        imInsFilas();
        return;
    }
    if (Deposito.getSelectedItem() == null || Deposito.getSelectedItem().toString().equals("0-Seleccionar")) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un depósito antes de proceder.", "Error", JOptionPane.ERROR_MESSAGE);
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
    IdClientes.addFocusListener(new FocusAdapter() {
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
            if (IdClientes.getText().trim().isEmpty() || IdClientes.getText().trim().equals("0") || 
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
            cargarCotizacion(selectedMonedaId);
            String clienteId = IdClientes.getText().trim();

            // Verificar que la moneda seleccionada corresponde a la moneda del precio asociado al cliente
            String clienteMonedaId = obtenerMonedaCliente(clienteId);
            if (clienteMonedaId == null || !selectedMonedaId.equals(clienteMonedaId)) {
                monedaListenerEnabled = false; // Desactivar temporalmente el listener
                JOptionPane.showMessageDialog(this, "La moneda seleccionada no corresponde a la moneda del precio asociado al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                Moneda.setSelectedIndex(0); // Reiniciar la selección de la moneda
                 Cotizaciones.setText(""); // Limpiar el JLabel si es necesario
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
    myData.put("id", IdVentas.getText());
    myData.put("cliente_id", IdClientes.getText());

    // Verificar y formatear fechas
    Date fechaFacturaDate = FechaFactura.getDate();
    Date fechaProcesoDate = FechaProceso.getDate();

    if (fechaFacturaDate != null) {
        myData.put("fechaFactura", new SimpleDateFormat("yyyy-MM-dd").format(fechaFacturaDate));
    } else {
        myData.put("fechaFactura", ""); // O un valor por defecto
    }

    if (fechaProcesoDate != null) {
        myData.put("fechaProceso", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaProcesoDate));
    } else {
        myData.put("fechaProceso", ""); // O un valor por defecto
    }
    myData.put("moneda_id", Functions.ExtraeCodigo(Moneda.getSelectedItem().toString()));
    myData.put("deposito_id", Functions.ExtraeCodigo(Deposito.getSelectedItem().toString()));
  // Manejar cuota_id para que sea "0" si el valor es "0-Seleccionar"
    String cuotaId = Functions.ExtraeCodigo(Cuota.getSelectedItem().toString());
if (cuotaId == null || cuotaId.equals("0")) {
    myData.put("cuota_id", ""); // Asignar cadena vacía como valor predeterminado
} else {
    myData.put("cuota_id", cuotaId);
}
    myData.put("total_neto", Neto.getText().replace(".", "").replace(",", "."));
    myData.put("total_exento", Exento.getText().replace(".", "").replace(",", "."));
    myData.put("total_impuesto", Impuesto.getText().replace(".", "").replace(",", "."));
    myData.put("total_bruto", Bruto.getText().replace(".", "").replace(",", "."));
    myData.put("impreso", Impreso.isSelected() ? "1" : "0");
    myData.put("anulado", Anulado.isSelected() ? "1" : "0");


    // Obtener la serie del talonario
    String talonarioId = obtenerTalonarioActivo();
    myData.put("talonario_id", talonarioId);
    String serie = obtenerSeriePorTalonarioId(talonarioId);
    myData.put("serie", serie);

    String tipoDocumentoSeleccionado = (String) TipoDocumento.getSelectedItem();
    if ("1-Contado".equals(tipoDocumentoSeleccionado)) {
        myData.put("tipo_documento", "0");
    } else if ("2-Crédito".equals(tipoDocumentoSeleccionado)) {
        myData.put("tipo_documento", "1");
    } else {
        myData.put("tipo_documento", ""); // O manejarlo de otra manera si es necesario
    }

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
        String id = IdVentas.getText();
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
    TipoDocumento.setSelectedIndex(0);
    Cuota.setSelectedIndex(0);
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
                IdVentas.setText(value);
                break;
            case "cliente_id":
                IdClientes.setText(value);
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
                Telefono.setText(clienteData.get("celular"));
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
            case "fechaProceso":
                String strFechaP = data.get("fechaProceso");
                if (strFechaP != null && !strFechaP.isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strFechaP);
                        FechaProceso.setDate(date);
                    } catch (ParseException e) {
                        FechaProceso.setDate(null);
                        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    FechaProceso.setDate(null);
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
            case "impreso":
                Impreso.setSelected(Integer.parseInt(value) != 0);
                break;
            case "anulado":
                Anulado.setSelected(Integer.parseInt(value) != 0);
                break;
            case "talonario_id":
                cargarDetallesTalonario(value);
                break; 
            case "serie":
                         Factura.setText(value + "-" + formatearNumeroFactura(data.get("nro_documento")));
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
        whereClause.put("tipo_comprobante", "1");  // Filtrar tipo de comprobante = 1 (Factura)

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
            Factura.setText(talonarioData.get("serie_comprobante"));
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

// Método para obtener los datos del cliente
private Map<String, String> obtenerDatosCliente(String clienteId) {
    Map<String, String> where = new HashMap<>();
    where.put("id", clienteId);

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    List<Map<String, String>> clienteList = this.tmClientes.searchListById(fields, where);
    if (clienteList != null && !clienteList.isEmpty()) {
        return clienteList.get(0);
    } else {
        return new HashMap<>();
    }
}

  private Map<String, String> buscarClientePorId(int clienteId) {
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(clienteId)); // Buscar por ID
    where.put("activo", "1"); // Solo buscar clientes activos

    // Definir qué campos necesitamos recuperar de la base de datos
    Map<String, String> fields = new HashMap<>();
    fields.put("id", "id");
    fields.put("cliente", "cliente");
    fields.put("apellido", "apellido");
    fields.put("nrodocumento", "nrodocumento");
    fields.put("divisoria", "divisoria");
    fields.put("direccion", "direccion");
    fields.put("celular", "celular");
    fields.put("tipodocumento", "tipodocumento");

    // Hacer la búsqueda
    List<Map<String, String>> result = tmClientes.searchListById(fields, where);

    if (!result.isEmpty()) {
        return result.get(0); // Si se encuentra un cliente activo, lo devolvemos
    }

    // Verificar si el cliente existe pero está inactivo
    where.remove("activo"); // Eliminar el filtro de activo
    List<Map<String, String>> todosClientes = tmClientes.searchListById(fields, where);

    if (!todosClientes.isEmpty()) {
        JOptionPane.showMessageDialog(null,
            "El cliente existe pero no está activo.",
            "Cliente Inactivo", JOptionPane.WARNING_MESSAGE);
        return null; // Manejar el cliente inactivo según la lógica
    }

    // Si no existe ningún cliente, preguntar si desea registrarlo
    int option = JOptionPane.showConfirmDialog(null, "Cliente no encontrado o inactivo. ¿Desea registrarlo?", "Registrar Cliente", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
        abrirFormularioClientes();
    }
    return null; // No se encontró cliente y no se va a registrar uno nuevo
}
private Map<String, String> buscarClientePorDocumento(String nroDocumento, String divisoria) {
    Map<String, String> where = new HashMap<>();
    where.put("nrodocumento", nroDocumento);
    if (divisoria != null) {
        where.put("divisoria", divisoria);
    }
    where.put("activo", "1"); // Solo buscar clientes activos

    Map<String, String> fields = new HashMap<>();
    fields.put("id", "id");
    fields.put("cliente", "cliente");
    fields.put("apellido", "apellido");
    fields.put("nrodocumento", "nrodocumento");
    fields.put("divisoria", "divisoria");
    fields.put("direccion", "direccion");
    fields.put("celular", "celular");
    fields.put("tipodocumento", "tipodocumento");

    // Buscar clientes activos
    List<Map<String, String>> result = tmClientes.searchListById(fields, where);

    if (!result.isEmpty()) {
        return result.get(0); // Si se encuentra un cliente activo, lo devolvemos
    }

    // Verificar si el cliente existe pero está inactivo
    where.remove("activo"); // Eliminar el filtro de activo
    List<Map<String, String>> todosClientes = tmClientes.searchListById(fields, where);

    if (!todosClientes.isEmpty()) {
        JOptionPane.showMessageDialog(null,
            "El cliente existe pero no está activo.",
            "Cliente Inactivo", JOptionPane.WARNING_MESSAGE);
        return null; // Manejar el cliente inactivo según la lógica
    }

    // Si no existe ningún cliente, preguntar si desea registrarlo
    int option = JOptionPane.showConfirmDialog(null, "Cliente no encontrado o inactivo. ¿Desea registrarlo?", "Registrar Cliente", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
        abrirFormularioClientes();
    }
    return null; // No se encontró cliente y no se va a registrar uno nuevo
}

private void abrirFormularioClientes() {
    try {
        // Crear una nueva instancia del formulario de clientes
        Form_Clientes formClientes = new Form_Clientes();
        
        // Obtener el DesktopPane desde Form_Ventas (JInternalFrame)
        JDesktopPane desktopPane = this.getDesktopPane();

        if (desktopPane != null) {
            // Añadir el formulario de clientes al DesktopPane
            desktopPane.add(formClientes);

            // Ajustar la capa del Form_Clientes para que esté encima
            desktopPane.setLayer(formClientes, JDesktopPane.PALETTE_LAYER); // Coloca el formulario en una capa superior

            // Configurar las propiedades del formulario de clientes
            formClientes.setVisible(true);
            formClientes.setClosable(true);
            formClientes.setIconifiable(true);
            formClientes.setResizable(true);

            // Traer el formulario de clientes al frente
            formClientes.moveToFront();
            formClientes.setSelected(true); // Selecciona el formulario para darle foco
            formClientes.grabFocus(); // Forzar el foco en el formulario de clientes

            // Manejar excepción de selección en caso de que ocurra
            if (!formClientes.isSelected()) {
                formClientes.setSelected(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el DesktopPane. El formulario de clientes no se puede abrir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir el formulario de clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

  private void agregarTipoDocumento() {
    TipoDocumento.removeAllItems(); 
    TipoDocumento.addItem("0-Seleccionar");
    TipoDocumento.addItem("1-Contado");
    TipoDocumento.addItem("2-Crédito");
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
        PanelFactura = new javax.swing.JPanel();
        Factura = new javax.swing.JLabel();
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
        Impreso = new javax.swing.JCheckBox();
        Anulado = new javax.swing.JCheckBox();
        lbl_direccion = new javax.swing.JLabel();
        RUCCliente = new javax.swing.JTextField();
        Clientes = new javax.swing.JLabel();
        IdClientes = new javax.swing.JTextField();
        Telefono = new javax.swing.JLabel();
        lbl_FechaFactura = new javax.swing.JLabel();
        FechaFactura = new com.toedter.calendar.JDateChooser();
        FechaProceso = new com.toedter.calendar.JDateChooser();
        lbl_FechaProceso = new javax.swing.JLabel();
        lbl_telefono = new javax.swing.JLabel();
        Direccion = new javax.swing.JLabel();
        IdVentas = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        Cotizaciones = new javax.swing.JLabel();
        lbl_Condicion1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Ventas");
        setPreferredSize(new java.awt.Dimension(985, 590));

        PanelPrincipal.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));
        PanelPrincipal.setMinimumSize(new java.awt.Dimension(975, 540));
        PanelPrincipal.setPreferredSize(new java.awt.Dimension(975, 540));
        PanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFactura.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles Factura"));

        Factura.setBackground(new java.awt.Color(204, 204, 255));
        Factura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Factura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Factura.setOpaque(true);

        lbl_fechafin.setText("   Fecha Fin Vigencia");

        FechaInicioVigencia.setBackground(new java.awt.Color(204, 204, 255));
        FechaInicioVigencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FechaInicioVigencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FechaInicioVigencia.setOpaque(true);

        jLabel8.setText("Factura N°");

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

        javax.swing.GroupLayout PanelFacturaLayout = new javax.swing.GroupLayout(PanelFactura);
        PanelFactura.setLayout(PanelFacturaLayout);
        PanelFacturaLayout.setHorizontalGroup(
            PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFacturaLayout.createSequentialGroup()
                .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFacturaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(PanelFacturaLayout.createSequentialGroup()
                        .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_rucLocal)
                            .addComponent(lbl_fechafin)
                            .addComponent(lbl_timbrado)
                            .addComponent(lbl_fechainicio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FechaInicioVigencia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Timbrado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FechaFinVigencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RUCLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(PanelFacturaLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelFacturaLayout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(Factura, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        PanelFacturaLayout.setVerticalGroup(
            PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Timbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_timbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FechaInicioVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fechainicio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FechaFinVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fechafin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_rucLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RUCLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Factura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        PanelPrincipal.add(PanelFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 240, 230));

        lbl_IdVentas.setText("  N° Registro");
        PanelPrincipal.add(lbl_IdVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 20));

        lbl_Cliente.setText("Cliente");
        PanelPrincipal.add(lbl_Cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, 20));

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
        PanelPrincipal.add(lbl_Condicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 260, -1, 20));

        PanelPrincipal.add(Deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 240, -1));

        TipoDocumento.setToolTipText("");
        TipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoDocumentoActionPerformed(evt);
            }
        });
        PanelPrincipal.add(TipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 260, 180, -1));

        lbl_deposito.setText("Depósito");
        PanelPrincipal.add(lbl_deposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        Moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonedaActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 240, -1));

        Cuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CuotaActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 180, -1));

        lbl_Moneda.setText("  Moneda");
        PanelPrincipal.add(lbl_Moneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 20));

        lbl_cuotas.setText("Cuotas ");
        PanelPrincipal.add(lbl_cuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, -1, -1));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
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
                .addGap(17, 17, 17)
                .addGroup(PanelTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bruto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Factura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        PanelPrincipal.add(Detalles, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 950, 200));

        Impreso.setText("Impreso");
        Impreso.setToolTipText("");
        Impreso.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Impreso.setEnabled(false);
        Impreso.setOpaque(true);
        Impreso.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ImpresoItemStateChanged(evt);
            }
        });
        Impreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImpresoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ImpresoMousePressed(evt);
            }
        });
        Impreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImpresoActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Impreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, 70, -1));

        Anulado.setText("Anulado");
        Anulado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnuladoActionPerformed(evt);
            }
        });
        PanelPrincipal.add(Anulado, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 300, -1, -1));

        lbl_direccion.setText("Dirección");
        PanelPrincipal.add(lbl_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 60, 20));

        RUCCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                RUCClienteFocusLost(evt);
            }
        });
        RUCCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RUCClienteKeyPressed(evt);
            }
        });
        PanelPrincipal.add(RUCCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 150, -1));

        Clientes.setBackground(new java.awt.Color(204, 204, 255));
        Clientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Clientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Clientes.setOpaque(true);
        PanelPrincipal.add(Clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 300, 22));

        IdClientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdClientesFocusLost(evt);
            }
        });
        IdClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdClientesActionPerformed(evt);
            }
        });
        IdClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdClientesKeyPressed(evt);
            }
        });
        PanelPrincipal.add(IdClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 70, -1));

        Telefono.setBackground(new java.awt.Color(204, 204, 255));
        Telefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Telefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Telefono.setOpaque(true);
        PanelPrincipal.add(Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 150, 22));

        lbl_FechaFactura.setText("Fecha Factura");
        PanelPrincipal.add(lbl_FechaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, 80, 20));
        PanelPrincipal.add(FechaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 150, -1));
        PanelPrincipal.add(FechaProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 140, -1));

        lbl_FechaProceso.setText("Fecha Proceso");
        PanelPrincipal.add(lbl_FechaProceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 70, -1, 20));

        lbl_telefono.setText("Teléfono");
        PanelPrincipal.add(lbl_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 50, 20));

        Direccion.setBackground(new java.awt.Color(204, 204, 255));
        Direccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Direccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Direccion.setOpaque(true);
        PanelPrincipal.add(Direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 380, 22));

        IdVentas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdVentasFocusLost(evt);
            }
        });
        IdVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdVentasActionPerformed(evt);
            }
        });
        IdVentas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdVentasKeyPressed(evt);
            }
        });
        PanelPrincipal.add(IdVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 140, -1));
        PanelPrincipal.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 246, 460, 10));
        PanelPrincipal.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 460, 10));

        Cotizaciones.setBackground(new java.awt.Color(204, 204, 255));
        Cotizaciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Cotizaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Cotizaciones.setOpaque(true);
        PanelPrincipal.add(Cotizaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 260, 190, 22));

        lbl_Condicion1.setText("Tipo Documento");
        PanelPrincipal.add(lbl_Condicion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, -1, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AnuladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnuladoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnuladoActionPerformed

    private void ImpresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImpresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ImpresoActionPerformed

    private void IdClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdClientesActionPerformed

    private void CuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CuotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CuotaActionPerformed

    private void IdVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdVentasActionPerformed

    private void IdVentasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdVentasKeyPressed
              if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }  
    }//GEN-LAST:event_IdVentasKeyPressed

    private void IdClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdClientesKeyPressed
                                      
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        try {
            int clienteId = Integer.parseInt(IdClientes.getText());
            if (clienteId > 0) {
                Map<String, String> clienteData = buscarClientePorId(clienteId);
                if (clienteData != null) {
                    String tipoDocumento = clienteData.get("tipodocumento"); // Asume que el campo 'tipodocumento' está disponible en los datos del cliente
                    String nroDocumento = clienteData.get("nrodocumento");
                    String divisoria = clienteData.get("divisoria");
                    
                    IdClientes.setText(clienteData.get("id"));
                    Clientes.setText(clienteData.get("cliente") + " " + clienteData.get("apellido"));
                    
                    if ("RUC".equals(tipoDocumento)) {
                        RUCCliente.setText(nroDocumento + "-" + divisoria);
                    } else {
                        RUCCliente.setText(nroDocumento);
                    }
                    
                    Direccion.setText(clienteData.get("direccion"));
                    Telefono.setText(clienteData.get("celular"));
                } else {
                    IdClientes.setText("");
                    Clientes.setText("");
                    RUCCliente.setText("");
                    Direccion.setText("");
                    Telefono.setText("");
                }
            } else {
                RUCCliente.setText("");
                Direccion.setText("");
                Telefono.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    }//GEN-LAST:event_IdClientesKeyPressed

    private void RUCClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RUCClienteFocusLost
     
    }//GEN-LAST:event_RUCClienteFocusLost

    private void jtDetalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtDetalleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtDetalleKeyPressed

    private void IdVentasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdVentasFocusLost

    }//GEN-LAST:event_IdVentasFocusLost

    private void IdClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdClientesFocusLost
  
    }//GEN-LAST:event_IdClientesFocusLost

    private void RUCClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RUCClienteKeyPressed

    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String rucInput = RUCCliente.getText().trim();
        if (!rucInput.isEmpty()) {
            String[] parts = rucInput.split("-");
            String nroDocumento = parts[0];
            String divisoria = parts.length > 1 ? parts[1] : null;

            Map<String, String> clienteData = buscarClientePorDocumento(nroDocumento, divisoria);
            if (clienteData != null) {
                String tipoDocumento = clienteData.get("tipodocumento");
                String documento = clienteData.get("nrodocumento");
                String div = clienteData.get("divisoria");

                IdClientes.setText(clienteData.get("id"));
                Clientes.setText(clienteData.get("cliente") + " " + clienteData.get("apellido"));

                if ("RUC".equals(tipoDocumento) && div != null) {
                    RUCCliente.setText(documento + "-" + div);
                } else {
                    RUCCliente.setText(documento);
                }

                Direccion.setText(clienteData.get("direccion"));
                Telefono.setText(clienteData.get("celular"));
            } else {
                IdClientes.setText("");
                Clientes.setText("");
                RUCCliente.setText("");
                Direccion.setText("");
                Telefono.setText("");
            }
        } else {
            RUCCliente.setText("");
            Direccion.setText("");
            Telefono.setText("");
        }
    }
    }//GEN-LAST:event_RUCClienteKeyPressed

    private void TipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TipoDocumentoActionPerformed

    private void MonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MonedaActionPerformed
                                                                          
    }//GEN-LAST:event_MonedaActionPerformed

    private void ImpresoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ImpresoItemStateChanged

    }//GEN-LAST:event_ImpresoItemStateChanged

    private void ImpresoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImpresoMouseClicked
        // Evita que el evento cambie el estado
    }//GEN-LAST:event_ImpresoMouseClicked

    private void ImpresoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImpresoMousePressed
  // Evita que el evento cambie el estado        // TODO add your handling code here:
    }//GEN-LAST:event_ImpresoMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Anulado;
    private javax.swing.JLabel Bruto;
    private javax.swing.JLabel Clientes;
    private javax.swing.JLabel Cotizaciones;
    private javax.swing.JComboBox<String> Cuota;
    private javax.swing.JComboBox<String> Deposito;
    private javax.swing.JScrollPane Detalles;
    private javax.swing.JLabel Direccion;
    private javax.swing.JLabel Exento;
    private javax.swing.JLabel Factura;
    private com.toedter.calendar.JDateChooser FechaFactura;
    private javax.swing.JLabel FechaFinVigencia;
    private javax.swing.JLabel FechaInicioVigencia;
    private com.toedter.calendar.JDateChooser FechaProceso;
    private javax.swing.JTextField IdClientes;
    private javax.swing.JTextField IdVentas;
    private javax.swing.JCheckBox Impreso;
    private javax.swing.JLabel Impuesto;
    private javax.swing.JComboBox<String> Moneda;
    private javax.swing.JLabel Neto;
    private javax.swing.JPanel PanelFactura;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelTotales;
    private javax.swing.JTextField RUCCliente;
    private javax.swing.JLabel RUCLocal;
    private javax.swing.JLabel Telefono;
    private javax.swing.JLabel Timbrado;
    private javax.swing.JComboBox<String> TipoDocumento;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JLabel lbl_Cliente;
    private javax.swing.JLabel lbl_Condicion;
    private javax.swing.JLabel lbl_Condicion1;
    private javax.swing.JLabel lbl_Exento;
    private javax.swing.JLabel lbl_Factura;
    private javax.swing.JLabel lbl_FechaFactura;
    private javax.swing.JLabel lbl_FechaProceso;
    private javax.swing.JLabel lbl_IdVentas;
    private javax.swing.JLabel lbl_Impuesto;
    private javax.swing.JLabel lbl_Moneda;
    private javax.swing.JLabel lbl_Neto;
    private javax.swing.JLabel lbl_RUC;
    private javax.swing.JLabel lbl_UltimoId;
    private javax.swing.JLabel lbl_cuotas;
    private javax.swing.JLabel lbl_deposito;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_fechafin;
    private javax.swing.JLabel lbl_fechainicio;
    private javax.swing.JLabel lbl_rucLocal;
    private javax.swing.JLabel lbl_telefono;
    private javax.swing.JLabel lbl_timbrado;
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
                imNuevo();
                return -1;
            }
        }
    }
    
    // Verificar valores de cabecera en myData
    List<String> columnasObligatoriasCabecera = Arrays.asList("id","cliente_id", "talonario_id", "total_neto", "total_bruto", "total_impuesto", "moneda_id", "deposito_id", "serie", "fechaProceso", "fechaFactura");
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
    // Si llega aquí, todas las verificaciones están completas, proceder al guardado
    if (!guardarCabecera(Integer.parseInt(myData.get("id")))) {
        return -1;
    }
    
    guardarDetalle(Integer.parseInt(myData.get("id")));
    
    // Solo preguntar si no está impreso
    if (!myData.get("impreso").equals("1")) {
        int option = JOptionPane.showConfirmDialog(this, "¿Desea imprimir la factura?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            int idFactura = Integer.parseInt(myData.get("id"));
            BigDecimal totalBruto = new BigDecimal(myData.get("total_bruto"));
            String totalLetras = NumeroALetras.convertir(totalBruto);
            totalLetras = NumeroALetras.capitalizarPrimeraLetra(totalLetras);
            imprimirFactura(idFactura, totalLetras);
            // Actualizar el estado de impresión
            actualizarEstadoImpreso(Integer.parseInt(myData.get("id")));
        }
    } else {
        imNuevo();
        return -1;
    }
    
    imNuevo();
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

private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();

    // Validar cuota según tipo_documento
    if ("1".equals(myData.get("tipo_documento"))) { // Crédito
        if (!myData.containsKey("cuota_id") || myData.get("cuota_id").isEmpty() || "0".equals(myData.get("cuota_id"))) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cuota para el tipo de documento crédito.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // No continuar si no hay cuota válida
        }
    } else if ("0".equals(myData.get("tipo_documento"))) { // Contado
        // Remover cuota si está presente
        if (myData.containsKey("cuota_id")) {
            myData.remove("cuota_id");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Tipo de documento desconocido. Verifique los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
        return false; // No continuar si el tipo de documento no es válido
    }

    alCabecera.add(myData);

    // Respaldar el ID
    String idBackup = myData.get("id");

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));

    if (tc.searchListById(fields, where).isEmpty()) {
        // Crear registro si no existe
        int rows = tc.createReg(myData);
        if (rows < 1) {
            imNuevo();
            return false;
        }
    } else {
        // Actualizar registro si ya existe
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            imNuevo();
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
        JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        cargarDetallesTalonarioInicio();

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
    setData(); // Actualiza los datos desde la vista

    // Verificar que el campo ID de la cabecera no esté vacío o inválido
    if (myData.get("id") == null || myData.get("id").isEmpty() || myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro válido para borrar.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
    if ( myData.get("impreso").equals("1") ) {
        JOptionPane.showMessageDialog(this, "No se puede eliminar un registro impreso", "Error", JOptionPane.ERROR_MESSAGE);
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
    whereDetalle.put("venta_id", myData.get("id")); // Asegúrate de que este sea el campo correcto que identifica los detalles de esta venta específica

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

    // Cargar último ID de venta
    this.fillView(myData, columnData, clienteData);
     cargarDetallesTalonarioInicio();
      cargarUltimoId();
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
        imNuevo();
        return -1; // Indicador de error
    }
   
    // Realiza la búsqueda de la cabecera
    Map<String, String> resultadoCabecera = this.tc.searchById(myData);
    System.out.println("ventas imBuscar " + resultadoCabecera);

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
    String id1= myData.get("id");
    // Obtener el número de documento
    String nroDocumento = myData.get("nro_documento");
    if (nroDocumento == null || nroDocumento.isEmpty()) {
        nroDocumento = String.valueOf(obtenerProximoNroDocumento(id1));
        myData.put("nro_documento", nroDocumento);
    }

    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("venta_id", this.myData.get("id"));

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
    clienteData = buscarClientePorId(Integer.parseInt(myData.get("cliente_id")));
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
esBusquedaManual = false; // Indicar que no es una búsqueda manual
    if (lastFocusedComponent == IdVentas) {
        List<String> columnasParaVentas = Arrays.asList("id", "fechaProceso");
        Form_Buscar buscadorVentas = new Form_Buscar(parentFrame, true, tc, "ventas", columnasParaVentas);
        buscadorVentas.setOnItemSeleccionadoListener(this);
        buscadorVentas.setVisible(true);
    } else if (lastFocusedComponent == IdClientes) {
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
    this.myData = this.tc.navegationReg(IdVentas.getText(), "FIRST");
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
    if (IdVentas.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdVentas.getText(), "NEXT");
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
    if (IdVentas.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdVentas.getText(), "PRIOR");
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
    this.myData = this.tc.navegationReg(IdVentas.getText(), "LAST");
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
            rowData.put("Id", jtDetalle.getValueAt(lastRow, 10) != null ? jtDetalle.getValueAt(lastRow, 10).toString() : "");

            System.out.println("Datos de la última fila antes de validar: " + rowData);

            if (isRowInvalid(rowData)) {
                System.out.println("Fila inválida. Datos de la fila fallida: " + rowData);
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

    System.out.println("Datos de la fila seleccionada antes de validar: " + rowData);

    if (isRowInvalid(rowData)) {
        System.out.println("Fila inválida. Datos de la fila fallida: " + rowData);
        JOptionPane.showMessageDialog(this, "Debe ingresar los detalles del producto correctamente antes de añadir una nueva fila.", "ATENCIÓN...!", JOptionPane.OK_OPTION);
        return -1;
    } else {
        System.out.println("Fila válida. Procediendo a agregar una nueva fila.");
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

        // Imprimir cada columna y su valor para depuración
        System.out.println("Validando columna: " + column + " con valor: " + value);

        if ("Descuento".equals(column)) {
            // Descuento puede ser cero o vacío, lo ignoramos
            continue;
        }

        // Verificar si el valor es nulo, vacío o "0"
        if (value == null || value.trim().isEmpty() || "0".equals(value.trim())) {
            System.out.println("Fila inválida: " + column + " está vacía o tiene valor '0'.");
            return true; // La fila es inválida
        }
    }

    System.out.println("Fila válida: Todos los valores son correctos.");
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
    if (lastFocusedComponent == IdVentas) {
        String idStr = datosSeleccionados.get("Codigo");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idVenta = Integer.parseInt(idStr);
                List<Map<String, String>> registrosCabecera = tc.buscarPorIdGenerico("VENTAS", "id", idVenta);

                if (!registrosCabecera.isEmpty()) {
                    Map<String, String> registroCabecera = registrosCabecera.get(0);

                    int talonarioId = Integer.parseInt(registroCabecera.get("talonario_id"));
                    int clienteId = Integer.parseInt(registroCabecera.get("cliente_id"));
                    List<Map<String, String>> registrosDetalle = tc.buscarPorIdGenerico("VENTAS_DETALLE", "venta_id", idVenta);
                    List<Map<String, String>> registrosCliente = tmClientes.buscarPorIdGenerico("CLIENTES", "id", clienteId);
                    List<Map<String, String>> registrosTalonario = tt.buscarPorIdGenerico("TALONARIOS", "id", talonarioId);

                    if (!registrosCliente.isEmpty() && !registrosTalonario.isEmpty()) {
                        Map<String, String> registroCliente = registrosCliente.get(0);
                        Map<String, String> registroTalonario = registrosTalonario.get(0);

                        SwingUtilities.invokeLater(() -> {
                            // Llenar la cabecera
                            IdVentas.setText(idStr);
                            IdClientes.setText(registroCabecera.get("cliente_id"));
                            RUCCliente.setText(registroCliente.get("nrodocumento"));
                            Clientes.setText(registroCliente.get("cliente") + " " + registroCliente.get("apellido"));
                            Telefono.setText(registroCliente.get("celular"));
                            Direccion.setText(registroCliente.get("direccion"));

                            // Formatear y llenar el número de factura
                            String serie = registroCabecera.get("serie");
                            String nroDocumento = formatearNumeroFactura(registroCabecera.get("nro_documento"));
                            Factura.setText(serie + "-" + nroDocumento);

                            // Llenar datos del talonario
                            Timbrado.setText(registroTalonario.get("numero_timbrado"));
                            FechaInicioVigencia.setText(registroTalonario.get("fecha_inicio_timbrado"));
                            FechaFinVigencia.setText(registroTalonario.get("fecha_final_timbrado"));
                            
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
                        
                            Impreso.setSelected(Integer.parseInt(registroCabecera.get("impreso")) != 0);
                            Anulado.setSelected(Integer.parseInt(registroCabecera.get("anulado"))!= 0);

                            try {
                                FechaFactura.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(registroCabecera.get("fechaFactura")));
                                FechaProceso.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(registroCabecera.get("fechaProceso")));
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
                    JOptionPane.showMessageDialog(null, "No se encontró una venta con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ID de venta inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else if (lastFocusedComponent == IdClientes) {
        String idStr = datosSeleccionados.get("Codigo"); 
        String nombre = datosSeleccionados.get("Descripcion"); 

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idCliente = Integer.parseInt(idStr);
                Map<String, String> clienteData = buscarClientePorId(idCliente);

                if (clienteData != null) {
                    String apellido = clienteData.get("apellido");
                    String ruc = clienteData.get("nrodocumento");
                    String divisoria = clienteData.get("divisoria");
                    String direccion = clienteData.get("direccion");
                    String telefono = clienteData.get("celular");
                    String tipoDocumento = clienteData.get("tipodocumento");

                    IdClientes.setText(idStr);
                    Clientes.setText(nombre + " " + apellido);

                    if ("RUC".equals(tipoDocumento)) {
                        RUCCliente.setText(ruc + "-" + divisoria);
                    } else {
                        RUCCliente.setText(ruc);
                    }

                    Direccion.setText(direccion);
                    Telefono.setText(telefono);
                } else {
                    JOptionPane.showMessageDialog(this, "ID de cliente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID de cliente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
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
