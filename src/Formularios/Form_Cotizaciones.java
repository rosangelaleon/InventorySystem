
package Formularios;

import Controllers.DBTableController;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Filtros.DecimalDocumentFilter;
import Filtros.NumericDocumentFilter;
import Modelo.cargaComboBox;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.*;


public class Form_Cotizaciones extends javax.swing.JInternalFrame implements InterfaceUsuario {
  private DBTableController tc;
    private Map<String, String> myData;
    public Form_Cotizaciones() {
        initComponents();
        IdCotizacion.setText("0");
        Compra.setText("0");
        Venta.setText("0");
         initialize();
    }

        private void initialize() {
        tc = new DBTableController();
        tc.iniciar("COTIZACIONES");
        myData = new HashMap<>();
        initializeTextFields();
        cargarUltimoId();
        cargarMonedas();
               
    }
    private void cargarMonedas() {
        List<Map<String, String>> monedas = tc.buscarPorConsultaGenerica("MONEDAS", "id, moneda", "cotizacion = 1 AND activo = 1");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("0-Seleccionar"); // Opción por defecto
        for (Map<String, String> moneda : monedas) {
            String id = moneda.get("id");
            String nombreMoneda = moneda.get("moneda");
            model.addElement(id + "-" + nombreMoneda);
        }
        Moneda.setModel(model);
    }
        
    private void initializeTextFields() {
        applyNumericFilter(IdCotizacion);
        applyNumericFilter(Compra);
        applyNumericFilter(Venta);
        addFocusListeners();
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }


    private void addFocusListeners() {
        IdCotizacion.addFocusListener(new DefaultFocusListener(IdCotizacion, true));
        Compra.addFocusListener(new DefaultFocusListener(Compra, true));
        Venta.addFocusListener(new DefaultFocusListener(Venta, true));
    }

    private void cargarUltimoId() {
        try {
            int ultimoId = tc.getMaxId();
            UltimoId.setText(String.valueOf(ultimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setData() {
        myData.put("id", IdCotizacion.getText());
        myData.put("moneda_id", Functions.ExtraeCodigo(Moneda.getSelectedItem().toString()));
        Date fechaProceso = Fecha.getDate();

    if ( fechaProceso != null) {
        myData.put("fecha", new SimpleDateFormat("yyyy-MM-dd").format(fechaProceso));
    } else {
        myData.put("fecha", ""); // O un valor por defecto
    }
        myData.put("compra", Compra.getText());
        myData.put("venta", Venta.getText());
        myData.put("activo", Activo.isSelected() ? "1" : "0");

    }

    private void resetData() {
        myData.clear();
        IdCotizacion.setText("0");
        Moneda.setSelectedIndex(0);
        Fecha.setDate(null);
        Compra.setText("0");
        Venta.setText("0");
        Activo.setSelected(false);
    }
    private void fillView() {
        IdCotizacion.setText(myData.get("id"));
        Functions.E_estado(Moneda, "MONEDAS", "id=" + myData.get("moneda_id"));

        String strFecha = myData.get("fecha");
        if (strFecha != null && !strFecha.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strFecha);
                Fecha.setDate(date);
            } catch (ParseException e) {
                Fecha.setDate(null);
                JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Fecha.setDate(null);
        }

        Compra.setText(formatAsInteger(myData.get("compra")));
        Venta.setText(formatAsInteger(myData.get("venta")));
        Activo.setSelected(myData.get("activo").equals("1"));
    }

    private String formatAsInteger(String value) {
        BigDecimal decimalValue = new BigDecimal(value);
        return decimalValue.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        IdCotizacion = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();
        lbl_moneda = new javax.swing.JLabel();
        lbl_compra = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        Fecha = new com.toedter.calendar.JDateChooser();
        Moneda = new javax.swing.JComboBox<>();
        Compra = new javax.swing.JTextField();
        lbl_venta = new javax.swing.JLabel();
        Venta = new javax.swing.JTextField();
        Activo = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Cotizaciones");

        IdCotizacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdCotizacionKeyPressed(evt);
            }
        });

        lbl_id.setText("Id");

        jLabel2.setText("Ultimo");

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        UltimoId.setOpaque(true);

        lbl_moneda.setText("Moneda");

        lbl_compra.setText("Compra");

        lbl_fecha.setText("Fecha");

        Moneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbl_venta.setText("Venta");

        Activo.setText("Activo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_moneda)
                    .addComponent(lbl_id))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(IdCotizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UltimoId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_fecha)
                    .addComponent(lbl_compra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Compra, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_venta))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Venta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Activo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(IdCotizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_id)
                        .addComponent(jLabel2)
                        .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_fecha)
                    .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Activo))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_moneda)
                    .addComponent(Moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_compra)
                    .addComponent(Compra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_venta)
                    .addComponent(Venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdCotizacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdCotizacionKeyPressed
     if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }   
    }//GEN-LAST:event_IdCotizacionKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JTextField Compra;
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JTextField IdCotizacion;
    private javax.swing.JComboBox<String> Moneda;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JTextField Venta;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_compra;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_moneda;
    private javax.swing.JLabel lbl_venta;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    setData();

    // Verificar campos obligatorios
    if (myData.get("id").equals("0") || myData.get("id").isEmpty() ||
        myData.get("moneda_id").isEmpty() ||
        myData.get("fecha").isEmpty() ||
        myData.get("compra").isEmpty() ||
        myData.get("venta").isEmpty()) {

        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificar que los valores de compra y venta sean positivos
    BigDecimal compraValue = new BigDecimal(myData.get("compra"));
    BigDecimal ventaValue = new BigDecimal(myData.get("venta"));
    if (compraValue.compareTo(BigDecimal.ZERO) <= 0 || ventaValue.compareTo(BigDecimal.ZERO) <= 0) {
        JOptionPane.showMessageDialog(this, "Los valores de compra y venta deben ser mayores que cero.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificar que la tasa de compra no sea mayor o igual que la tasa de venta
    if (compraValue.compareTo(ventaValue) >= 0) {
        JOptionPane.showMessageDialog(this, "La tasa de compra no puede ser mayor o igual que la tasa de venta.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que la fecha no sea futura y no sea pasada
    LocalDate fechaCotizacion = LocalDate.parse(myData.get("fecha"));
    LocalDate currentDate = LocalDate.now();
    if (fechaCotizacion.isAfter(currentDate)) {
        JOptionPane.showMessageDialog(this, "La fecha de cotización no puede ser futura.", "Error", JOptionPane.ERROR_MESSAGE);
       imNuevo();
        return -1;
    } else if (fechaCotizacion.isBefore(currentDate)) {
        JOptionPane.showMessageDialog(this, "La fecha de cotización no puede ser pasada.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar si se está creando un nuevo registro o actualizando uno existente
    boolean isCreating = myData.get("id").equals("0");

    if (isCreating) {
        // Verificar que no existan cotizaciones duplicadas para la misma moneda en la misma fecha solo si es una creación
        Map<String, String> where = new HashMap<>();
        where.put("moneda_id", myData.get("moneda_id"));
        where.put("fecha", myData.get("fecha"));
        
        Map<String, String> fields = new HashMap<>();
        fields.put("*", "*");

        List<Map<String, String>> existingRecords = tc.searchListById(fields, where);
        if (!existingRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ya existe una cotización para esta moneda en la fecha especificada.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    int idCotizacion = Integer.parseInt(myData.get("id"));
    if (!guardarCabecera(idCotizacion)) {
        return -1;
    }
    JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    resetData();
    cargarUltimoId();
    return 0;
}


private boolean guardarCabecera(int idCotizacion) {
    ArrayList<Map<String, String>> alData = new ArrayList<>();
    alData.add(myData);

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCotizacion));

    if (tc.searchListById(fields, where).isEmpty()) {
        int rows = tc.createReg(myData);
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar crear el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        int rowsAffected = tc.updateReg(alData);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    return true;
}
@Override
public int imBorrar(String crud) {
    setData();

    // Verificar campo ID obligatorio
    if (myData.get("id").equals("0") || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID no puede quedar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        ArrayList<Map<String, String>> alData = new ArrayList<>();
        alData.add(myData);

        if (!tc.searchById(myData).isEmpty()) {
            int rowsAffected = tc.deleteReg(alData);
            if (rowsAffected < 1) {
                JOptionPane.showMessageDialog(this, "Error al intentar eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        }
        JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        resetData();
        cargarUltimoId();
    } else {
        JOptionPane.showMessageDialog(this, "Eliminación cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    return 0;
}

    @Override
    public int imNuevo() {
        resetData();
        cargarUltimoId();
        return 0;
    }

    @Override
    public int imFiltrar() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        List<String> columnasParaCotizaciones = Arrays.asList("id", "fecha");
        Form_Buscar buscadorCotizaciones = new Form_Buscar(parentFrame, true, tc, "cotizaciones", columnasParaCotizaciones);

        buscadorCotizaciones.setOnItemSeleccionadoListener(this);
        buscadorCotizaciones.setVisible(true);

        return 0;
    }
    @Override
    public int imBuscar() {
        setData();

        // Verificar campo ID obligatorio
        if (myData.get("id").equals("0")) {
            JOptionPane.showMessageDialog(this, "El ID 0 no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        myData = tc.searchById(myData);
        if (myData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        fillView();
        return 0;
    }

    @Override
    public int imPrimero() {
        myData = tc.navegationReg(IdCotizacion.getText(), "FIRST");
        if (myData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        fillView();
        return 0;
    }

    @Override
    public int imSiguiente() {
        myData = tc.navegationReg(IdCotizacion.getText(), "NEXT");
        if (myData.isEmpty() || myData.get("id").equals("0")) {
            JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        fillView();
        return 0;
    }

    @Override
    public int imAnterior() {
        myData = tc.navegationReg(IdCotizacion.getText(), "PRIOR");
        if (myData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        fillView();
        return 0;
    }

    @Override
    public int imUltimo() {
        myData = tc.navegationReg(IdCotizacion.getText(), "LAST");
        if (myData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return -1;
        }
        fillView();
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
                job.setJobName("Cotizaciones");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

    @Override
    public int imInsFilas() {
             JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
     return -1;
    }

    @Override
    public int imDelFilas() {
    
    JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
     return -1;
    }

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    System.out.println("Datos seleccionados recibidos: " + datosSeleccionados);

    String idStr = datosSeleccionados.get("Codigo");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idCotizacion = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("COTIZACIONES", "id", idCotizacion);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                final String monedaId = registro.get("moneda_id");
                final String strFecha = registro.get("fecha");
                final String compra = registro.get("compra");
                final String venta = registro.get("venta");
                final String activoStr = registro.get("activo");
                final boolean activo = "1".equals(activoStr);

                System.out.println("Valores recuperados - Moneda: " + monedaId + ", Fecha: " + strFecha + ", Compra: " + compra + ", Venta: " + venta);

                SwingUtilities.invokeLater(() -> {
                    try {
                        IdCotizacion.setText(idStr);
                        // Ajustar los filtros de decimales después de cargar los datos
                        // adjustDecimalFilters(); // Eliminar si ya no se necesita

                        // Formatear y mostrar los valores de compra y venta como enteros
                        Compra.setText(formatAsInteger(compra));
                        Venta.setText(formatAsInteger(venta));

                        Activo.setSelected(activo);
                        Functions.E_estado(Moneda, "MONEDAS", "id=" + monedaId);

                        // Manejar la fecha
                        if (strFecha != null && !strFecha.isEmpty()) {
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strFecha);
                                Fecha.setDate(date);
                            } catch (ParseException e) {
                                JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            Fecha.setDate(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error al actualizar la vista: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una cotización con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de cotización inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
