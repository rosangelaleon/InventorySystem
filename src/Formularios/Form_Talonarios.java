
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.AlphanumericFilter;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import javax.swing.text.AbstractDocument;
import javax.swing.*;
public class Form_Talonarios extends javax.swing.JInternalFrame implements InterfaceUsuario{
    private DBTableController tc;
    private DBTableController ts;
    private Map<String, String> mapData;
    private int idTalonarios;
    public Form_Talonarios() {
        initComponents();
        IdTalonario.setText("0");
        NumeroActual.setText("0");
        NumeroInicial.setText("0");
        NumeroFinal.setText("0");
        tc = new DBTableController();
        tc.iniciar("TALONARIOS");
        ts = new DBTableController();
        ts.iniciar("SUCURSALES");
        mapData = new HashMap<>();
        idTalonarios = -1;
        cargarSeriesComprobante();
        actualizarUltimoId();
        initializeTextFields();
        initializeComboBoxes();
    }

private void setMapData() {
    mapData.clear();

    mapData.put("id", IdTalonario.getText().trim());
    mapData.put("tipo_comprobante", String.valueOf(TipoComprobante.getSelectedIndex() - 1)); // Ajusta el índice para la base de datos (0 para Recibo, 1 para Factura)

    int tipoComprobante = TipoComprobante.getSelectedIndex() - 1; // Ajuste para 0-Recibo y 1-Factura
    if (tipoComprobante == 1|| tipoComprobante == 2) { // Si es Factura o nota de credito
        mapData.put("serie_comprobante", Serie.getSelectedItem().toString());
        mapData.put("numero_timbrado", NumeroTimbrado.getText().trim());
        mapData.put("fecha_inicio_timbrado", FechaInicioTimbrado.getDate() != null ?
            new SimpleDateFormat("yyyy-MM-dd").format(FechaInicioTimbrado.getDate()) : null);
        mapData.put("fecha_final_timbrado", FechaFinalTimbrado.getDate() != null ?
            new SimpleDateFormat("yyyy-MM-dd").format(FechaFinalTimbrado.getDate()) : null);
    } else { // Si es Recibo
        mapData.put("serie_comprobante", "");
        mapData.put("numero_timbrado", "");
    }

    mapData.put("numero_inicial", NumeroInicial.getText().trim());
    mapData.put("numero_final", NumeroFinal.getText().trim());
    mapData.put("numero_actual", NumeroActual.getText().trim());
    mapData.put("activo", Activo.isSelected() ? "1" : "0");
}


// Método resetData para limpiar todos los campos del formulario
private void resetData() {
    IdTalonario.setText("0");
    Serie.setSelectedItem("0-Seleccionar");
    TipoComprobante.setSelectedIndex(0);
    NumeroTimbrado.setText("");
    FechaInicioTimbrado.setDate(null);
    FechaFinalTimbrado.setDate(null);
    NumeroInicial.setText("0");
    NumeroFinal.setText("0");
    NumeroActual.setText("0");
    Activo.setSelected(true);
    mapData.clear();
}


  private void fillView(Map<String, String> data) {
    IdTalonario.setText(data.getOrDefault("id", ""));
    int tipoComprobante = Integer.parseInt(data.getOrDefault("tipo_comprobante", "0"));
    TipoComprobante.setSelectedIndex(tipoComprobante + 1); // Ajusta el índice para coincidir con 1-Recibo y 2-Factura
    Serie.setSelectedItem(data.getOrDefault("serie_comprobante", ""));
    NumeroTimbrado.setText(data.getOrDefault("numero_timbrado", ""));
    try {
        String fechaInicioStr = data.getOrDefault("fecha_inicio_timbrado", "");
        if (!fechaInicioStr.isEmpty()) {
            Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicioStr);
            FechaInicioTimbrado.setDate(fechaInicio);
        } else {
            FechaInicioTimbrado.setDate(null);
        }

        String fechaFinalStr = data.getOrDefault("fecha_final_timbrado", "");
        if (!fechaFinalStr.isEmpty()) {
            Date fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinalStr);
            FechaFinalTimbrado.setDate(fechaFinal);
        } else {
            FechaFinalTimbrado.setDate(null);
        }
    } catch (ParseException e) {
        e.printStackTrace();
    }

    NumeroInicial.setText(data.getOrDefault("numero_inicial", ""));
    NumeroFinal.setText(data.getOrDefault("numero_final", ""));
    NumeroActual.setText(data.getOrDefault("numero_actual", ""));
    Activo.setSelected("1".equals(data.getOrDefault("activo", "0")));
    tipoComprobanteChanged(null); // Actualiza la interfaz según el tipo de comprobante
}

private void initializeComboBoxes() {
    TipoComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0-Seleccionar", "1-Recibo", "2-Factura","3-Nota de Crédito" }));
    TipoComprobante.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            tipoComprobanteChanged(evt);
        }
    });
}

    private void initializeTextFields() {
        applyNumericFilter(IdTalonario);
        applyAlphaFilter(NumeroTimbrado);
        applyNumericFilter(NumeroInicial);
        applyNumericFilter(NumeroFinal);
        applyNumericFilter(NumeroActual);
        addFocusListeners();
        addEnterKeyListenerToIdField();
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new AlphanumericFilter());
    }

    private void addFocusListeners() {
        IdTalonario.addFocusListener(new DefaultFocusListener(IdTalonario, true));
        NumeroTimbrado.addFocusListener(new DefaultFocusListener(NumeroTimbrado, false));
        NumeroInicial.addFocusListener(new DefaultFocusListener(NumeroInicial, true));
        NumeroFinal.addFocusListener(new DefaultFocusListener(NumeroFinal, true));
        NumeroActual.addFocusListener(new DefaultFocusListener(NumeroActual, true));
    }

    private void addEnterKeyListenerToIdField() {
        IdTalonario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarTalonarioPorId();
                }
            }
        });
    }

    private void buscarTalonarioPorId() {
        String id = IdTalonario.getText().trim();
        if (!id.isEmpty()) {
            resetData();
            try {
                int idNum = Integer.parseInt(id);
                idTalonarios = idNum;
                List<Map<String, String>> resultados = tc.buscarPorIdGenerico("TALONARIOS", "id", idNum);
                if (resultados != null && !resultados.isEmpty()) {
                    Map<String, String> resultado = resultados.get(0);
                    fillViewLlenar(resultado);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el talonario con ID: " + id);
                    resetData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                resetData();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un ID para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            resetData();
        }
    }

    private int obtenerIdTalonarioActual() {
        try {
            return Integer.parseInt(IdTalonario.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void actualizarUltimoId() {
        try {
            int UltimoId = tc.getMaxId();
            ultimoid.setText(String.valueOf(UltimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private int procesarRegistroNavegacion(Map<String, String> registro) {
        if (registro == null || registro.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros siguientes disponibles.");
            return -1; // Indica que no hay más registros.
        } else {
            try {
                // Actualizar el ID del talonario actual para la navegación.
                idTalonarios = Integer.parseInt(registro.getOrDefault("id", "-1"));

                // Actualizar la interfaz de usuario con los datos del registro.
                fillView(registro);

                return 1; // Éxito
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1; // Error en la conversión del ID.
            }
        }
    }
    private boolean TalonarioExiste(String id) {
    Map<String, String> params = new HashMap<>();
    params.put("id", id);
    return tc.existAny(params) > 0;
}
private void cargarSeriesComprobante() {
    Serie.removeAllItems();
    Serie.addItem("0-Seleccionar");

    try {
        // Configurar parámetros de búsqueda para sucursales
        Map<String, String> whereSucursales = new HashMap<>();
        Map<String, String> viewSucursales = new HashMap<>();
        viewSucursales.put("id", "");
        viewSucursales.put("sucursal", "");

        // Obtener resultados de búsqueda
        ArrayList<Map<String, String>> sucursales = ts.searchListById(viewSucursales, whereSucursales);
        // Ordenar resultados por ID
        sucursales.sort(new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                int id1 = Integer.parseInt(o1.get("id"));
                int id2 = Integer.parseInt(o2.get("id"));
                return Integer.compare(id1, id2);
            }
        });

        // Combinar sucursales en el ComboBox
        for (Map<String, String> sucursal : sucursales) {
            String sucursalId = String.format("%03d", Integer.parseInt(sucursal.get("id")));
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar las series de comprobante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void tipoComprobanteChanged(java.awt.event.ActionEvent evt) {
    int selectedIndex = TipoComprobante.getSelectedIndex();
    if (selectedIndex == 1) { // 1 es Recibo
        NumeroTimbrado.setEnabled(false);
        FechaInicioTimbrado.setEnabled(false);
        FechaFinalTimbrado.setEnabled(false);
        NumeroTimbrado.setText("");
        FechaInicioTimbrado.setDate(null);
        FechaFinalTimbrado.setDate(null);
        Serie.setEnabled(false);
        Serie.setSelectedIndex(0); // Opcional: restablecer a la opción predeterminada
    } else if (selectedIndex == 2 || selectedIndex == 3) { // 2 es Factura
        NumeroTimbrado.setEnabled(true);
        FechaInicioTimbrado.setEnabled(true);
        FechaFinalTimbrado.setEnabled(true);
        Serie.setEnabled(true);
    } else { // 0-Seleccionar u otro valor no válido
        NumeroTimbrado.setEnabled(false);
        FechaInicioTimbrado.setEnabled(false);
        FechaFinalTimbrado.setEnabled(false);
        NumeroTimbrado.setText("");
        FechaInicioTimbrado.setDate(null);
        FechaFinalTimbrado.setDate(null);
        Serie.setEnabled(true); // Suponiendo que 0-Seleccionar también permite editar la serie
    }
}

private void fillViewLlenar(Map<String, String> data) {
    IdTalonario.setText(data.getOrDefault("id", ""));
    int tipoComprobante = Integer.parseInt(data.getOrDefault("tipo_comprobante", "0"));
    TipoComprobante.setSelectedIndex(tipoComprobante + 1); // Ajusta el índice para coincidir con 1-Recibo y 2-Factura
    Serie.setSelectedItem(data.getOrDefault("serie_comprobante", ""));
    NumeroTimbrado.setText(data.getOrDefault("numero_timbrado", ""));
    try {
        String fechaInicioStr = data.get("fecha_inicio_timbrado");
        if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
            Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicioStr);
            FechaInicioTimbrado.setDate(fechaInicio);
        } else {
            FechaInicioTimbrado.setDate(null);
        }

        String fechaFinalStr = data.get("fecha_final_timbrado");
        if (fechaFinalStr != null && !fechaFinalStr.isEmpty()) {
            Date fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinalStr);
            FechaFinalTimbrado.setDate(fechaFinal);
        } else {
            FechaFinalTimbrado.setDate(null);
        }
    } catch (ParseException e) {
        e.printStackTrace();
    }

    NumeroInicial.setText(data.getOrDefault("numero_inicial", ""));
    NumeroFinal.setText(data.getOrDefault("numero_final", ""));
    NumeroActual.setText(data.getOrDefault("numero_actual", ""));
    Activo.setSelected("1".equals(data.getOrDefault("activo", "0")));
    tipoComprobanteChanged(null); // Actualiza la interfaz según el tipo de comprobante
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        NumeroActual = new javax.swing.JTextField();
        lbl_fechainicio = new javax.swing.JLabel();
        lbl_serie = new javax.swing.JLabel();
        lbl_numeroactual = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_fechafinal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NumeroInicial = new javax.swing.JTextField();
        NumeroFinal = new javax.swing.JTextField();
        lbl_numerotimbrado = new javax.swing.JLabel();
        NumeroTimbrado = new javax.swing.JTextField();
        Activo = new javax.swing.JCheckBox();
        FechaInicioTimbrado = new com.toedter.calendar.JDateChooser();
        FechaFinalTimbrado = new com.toedter.calendar.JDateChooser();
        IdTalonario = new javax.swing.JTextField();
        lbl_idTalonario = new javax.swing.JLabel();
        ultimoid = new javax.swing.JLabel();
        lbl_ultimo = new javax.swing.JLabel();
        Serie = new javax.swing.JComboBox<>();
        TipoComprobante = new javax.swing.JComboBox<>();
        lbl_comprobante = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Talonarios");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(NumeroActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 130, -1));

        lbl_fechainicio.setText("Fecha Inicio");
        jPanel1.add(lbl_fechainicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        lbl_serie.setText("Serie Comprobante");
        jPanel1.add(lbl_serie, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, 20));

        lbl_numeroactual.setText("Número Actual");
        jPanel1.add(lbl_numeroactual, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, -1, -1));

        jLabel1.setText("Número Inicial");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, 40));

        lbl_fechafinal.setText("Fecha Final");
        jPanel1.add(lbl_fechafinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, -1, -1));

        jLabel2.setText("Número Final");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, 20));
        jPanel1.add(NumeroInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 130, -1));
        jPanel1.add(NumeroFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 130, -1));

        lbl_numerotimbrado.setText("Número Timbrado");
        jPanel1.add(lbl_numerotimbrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, 40));
        jPanel1.add(NumeroTimbrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, -1));

        Activo.setSelected(true);
        Activo.setText("Activo");
        jPanel1.add(Activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, -1, -1));
        jPanel1.add(FechaInicioTimbrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 130, -1));
        jPanel1.add(FechaFinalTimbrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 130, -1));
        jPanel1.add(IdTalonario, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 80, -1));

        lbl_idTalonario.setText("Id");
        jPanel1.add(lbl_idTalonario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 10, 20));

        ultimoid.setBackground(new java.awt.Color(204, 204, 255));
        ultimoid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ultimoid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ultimoid.setOpaque(true);
        jPanel1.add(ultimoid, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 80, 23));

        lbl_ultimo.setText("Último");
        jPanel1.add(lbl_ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 40, -1));

        Serie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(Serie, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 130, -1));

        TipoComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(TipoComprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 130, -1));

        lbl_comprobante.setText("Tipo Comprobante");
        jPanel1.add(lbl_comprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private com.toedter.calendar.JDateChooser FechaFinalTimbrado;
    private com.toedter.calendar.JDateChooser FechaInicioTimbrado;
    private javax.swing.JTextField IdTalonario;
    private javax.swing.JTextField NumeroActual;
    private javax.swing.JTextField NumeroFinal;
    private javax.swing.JTextField NumeroInicial;
    private javax.swing.JTextField NumeroTimbrado;
    private javax.swing.JComboBox<String> Serie;
    private javax.swing.JComboBox<String> TipoComprobante;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_comprobante;
    private javax.swing.JLabel lbl_fechafinal;
    private javax.swing.JLabel lbl_fechainicio;
    private javax.swing.JLabel lbl_idTalonario;
    private javax.swing.JLabel lbl_numeroactual;
    private javax.swing.JLabel lbl_numerotimbrado;
    private javax.swing.JLabel lbl_serie;
    private javax.swing.JLabel lbl_ultimo;
    private javax.swing.JLabel ultimoid;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    int rowsAffected = 0;

    try {
        setMapData(); // Actualiza los datos de la vista

        // Validación general
        if (mapData.get("id").isEmpty() || mapData.get("numero_inicial").isEmpty() ||
            mapData.get("numero_final").isEmpty() || mapData.get("numero_actual").isEmpty() || 
            mapData.get("tipo_comprobante").isEmpty() || mapData.get("id").equals("0") ||
            mapData.get("numero_inicial").equals("0") || mapData.get("numero_final").equals("0") || 
            mapData.get("numero_actual").equals("0")) {

            JOptionPane.showMessageDialog(this, "Los campos son obligatorios y no deben ser cero.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }

        // Obtener el tipo de comprobante
        int tipoComprobante = Integer.parseInt(mapData.get("tipo_comprobante"));

        // Validación específica para Factura
        if (tipoComprobante == 1 || tipoComprobante==2) { // Factura
            if (mapData.get("serie_comprobante").isEmpty() || mapData.get("numero_timbrado").isEmpty() ||
                mapData.get("fecha_inicio_timbrado") == null || mapData.get("fecha_final_timbrado") == null ||
                mapData.get("serie_comprobante").equals("0") || mapData.get("numero_timbrado").equals("0")) {

                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios para Factura o Nota de credito y no deben ser cero.",
                                              "Error", JOptionPane.ERROR_MESSAGE);
                imNuevo();
                return -1;
            }
        } else { // Si es Recibo, asignar valores vacíos a los campos de fecha y número de timbrado
            mapData.put("serie_comprobante", "");
            mapData.put("numero_timbrado", "");
        }

        // Convertir mapData a una ArrayList para la compatibilidad del método
        ArrayList<Map<String, String>> alMapData = new ArrayList<>();
        alMapData.add(mapData);

        try {
            if (TalonarioExiste(mapData.get("id"))) {
                rowsAffected = tc.updateReg(alMapData); // Actualiza con un solo registro
            } else {
                rowsAffected = tc.createReg(mapData); // Crea con un solo registro
            }

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.",
                                              "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } 
        } catch (Exception e) {
            if (e instanceof SQLException) {
                // Mostrar mensaje de error en ventana emergente
                JOptionPane.showMessageDialog(this, "Error SQL: " + e.getMessage(),
                                              "Error SQL", JOptionPane.ERROR_MESSAGE);
            } else {
                // Manejo genérico de excepciones
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            imNuevo();
            return -1;
        }
    } catch (Exception e) {
        // Capturar cualquier otra excepción
        String errorMessage = e.getMessage();
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage,
                                      "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    imNuevo();
    return rowsAffected;
}

@Override
public int imBorrar(String crud) {
    // Verificar que el campo ID esté lleno
    if (IdTalonario.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un talonario antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        imNuevo();
        return -1;
    }

    int idTalonarioActual = obtenerIdTalonarioActual(); // Método para obtener el ID actual del campo de texto
    if (idTalonarioActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este talonario?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    // Preparar el registro a eliminar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idTalonarioActual));
    registrosParaBorrar.add(registro);

    // Intentar eliminar el registro
    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Talonario eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
         imNuevo();
        return resultado;
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar el talonario.", "Error", JOptionPane.ERROR_MESSAGE);
          imNuevo();
        return -1;
    }
}


    @Override
    public int imNuevo() {
        actualizarUltimoId();
        idTalonarios = -1;
        fillView(mapData);
        resetData();
        return 0;
    }

    @Override
    public int imBuscar() {
       buscarTalonarioPorId();
        return 0;
    }

    @Override
    public int imFiltrar() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        List<String> columnasParaTalonarios = Arrays.asList("id", "serie_comprobante");
        Form_Buscar buscadorTalonarios = new Form_Buscar(parentFrame, true, tc, "TALONARIOS", columnasParaTalonarios);
        buscadorTalonarios.setOnItemSeleccionadoListener(this);
        buscadorTalonarios.setVisible(true);
        return 0;
    }

    @Override
    public int imPrimero() {
       Map<String, String> registro = tc.navegationReg(null, "FIRST");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imSiguiente() {
 if (idTalonarios == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idTalonarios), "NEXT");
        return procesarRegistroNavegacion(registro);
    }
@Override
    public int imAnterior() {
        if (idTalonarios <= 1) {
            JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idTalonarios), "PRIOR");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imUltimo() {
        Map<String, String> registro = tc.navegationReg(null, "LAST");
        return procesarRegistroNavegacion(registro);
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
                job.setJobName("Talonarios");
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

    String idStr = datosSeleccionados.get("Codigo"); // Asegúrate de que 'Codigo' sea la clave correcta.
    String serieComprobante = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idTalonario = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("TALONARIOS", "id", idTalonario);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                final String tipoComprobanteStr = registro.get("tipo_comprobante");
                final int tipoComprobante = tipoComprobanteStr != null ? Integer.parseInt(tipoComprobanteStr) : -1;
                final String numeroTimbrado = registro.get("numero_timbrado");
                final String numeroInicial = registro.get("numero_inicial");
                final String numeroFinal = registro.get("numero_final");
                final String numeroActual = registro.get("numero_actual");
                final String activoStr = registro.get("activo");
                final boolean activo = "1".equals(activoStr);
                final String strFechaInicio = registro.get("fecha_inicio_timbrado");
                final String strFechaFinal = registro.get("fecha_final_timbrado");
                idTalonarios = idTalonario;
                SwingUtilities.invokeLater(() -> {
                    IdTalonario.setText(idStr);
                    TipoComprobante.setSelectedIndex(tipoComprobante + 1); // Ajustar índice para coincidir con el combo box
                    Serie.setSelectedItem(serieComprobante != null ? serieComprobante : "0-Seleccionar");
                    NumeroTimbrado.setText(numeroTimbrado != null ? numeroTimbrado : "");
                    NumeroInicial.setText(numeroInicial != null ? numeroInicial : "0");
                    NumeroFinal.setText(numeroFinal != null ? numeroFinal : "0");
                    NumeroActual.setText(numeroActual != null ? numeroActual : "0");
                    Activo.setSelected(activo);

                    try {
                        if (strFechaInicio != null && !strFechaInicio.equals("null") && !strFechaInicio.isEmpty()) {
                            Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(strFechaInicio);
                            FechaInicioTimbrado.setDate(fechaInicio);
                        } else {
                            FechaInicioTimbrado.setDate(null);
                        }

                        if (strFechaFinal != null && !strFechaFinal.equals("null") && !strFechaFinal.isEmpty()) {
                            Date fechaFinal = new SimpleDateFormat("yyyy-MM-dd").parse(strFechaFinal);
                            FechaFinalTimbrado.setDate(fechaFinal);
                        } else {
                            FechaFinalTimbrado.setDate(null);
                        }
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                    }
                    tipoComprobanteChanged(null); // Actualiza la interfaz según el tipo de comprobante
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un talonario con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                imNuevo();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de talonario inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
