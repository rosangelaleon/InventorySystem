
package Formularios;

import Controllers.DBTableController;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Filtros.DescriptionFilter;
import Filtros.NumericDocumentFilter;
import Filtros.RucDocumentFilter;
import Filtros.TextFilter;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.swing.text.AbstractDocument;
import javax.swing.*;

public class Form_Proveedores extends javax.swing.JInternalFrame implements InterfaceUsuario{
    private DBTableController tc;
    private DBTableController td;
    private Map<String, String> mapData;
    private int idProveedores;
    
    public Form_Proveedores() {
        initComponents();
    IdProveedor.setText("0"); 
        tc = new DBTableController();
        tc.iniciar("PROVEEDORES");
        td = new DBTableController();
        td.iniciar("CIUDADES");
        mapData = new HashMap<>();
       idProveedores = -1;
       actualizarUltimoId();
       agregarTipoDocumento();
       cargarCiudades();
       initializeTextFields();
    }
private void setMapData() {
    mapData.clear();
    mapData.put("id", IdProveedor.getText().trim());
    mapData.put("proveedor", Proveedor.getText().trim());
    mapData.put("fecha", Optional.ofNullable(Fecha.getDate())
            .map(date -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
            .orElseThrow(() -> new RuntimeException("La fecha es obligatoria.")));

    String tipoDocumento = (String) tipodocumento.getSelectedItem();
    mapData.put("tipodocumento", tipoDocumento);
    mapData.put("nrodocumento", NroDocumento.getText().trim());

    // Calculate divisoria if tipoDocumento is RUC
    if ("RUC".equals(tipoDocumento)) {
        String nroDocumento = NroDocumento.getText().trim();
        if (!nroDocumento.isEmpty()) {
            int dv = Functions.createDV(nroDocumento, 11);
            divisoria.setText(String.valueOf(dv));
        } else {
            divisoria.setText("");
        }
        mapData.put("divisoria", divisoria.getText().trim());
    } else {
        mapData.put("divisoria", ""); // Clear divisoria if not RUC
    }
    
    mapData.put("celular", Celular.getText().trim());
    String direccion = Direccion.getText().trim();
    if (!direccion.isEmpty()) {
        mapData.put("direccion", direccion);
    }
    String ciudadSeleccionada = (String) Ciudad.getSelectedItem();
    int ciudadId = Integer.parseInt(ciudadSeleccionada.split(" - ")[0]);
    mapData.put("ciudad_id", String.valueOf(ciudadId));
    mapData.put("activo", Activo.isSelected() ? "1" : "0");
}

    private void resetData() { 
    IdProveedor.setText("0");
    Proveedor.setText("");
    NroDocumento.setText("");
    Celular.setText("");
    Direccion.setText("");
    divisoria.setText("");
    tipodocumento.setSelectedIndex(0);
    Ciudad.setSelectedIndex(0);
    Activo.setSelected(false);
    Fecha.setDate(null);  // Restablece la fecha al valor por defecto o nulo
    mapData.clear();
    }
 private void fillView(Map<String, String> data) {
    IdProveedor.setText(data.getOrDefault("id", ""));
    Proveedor.setText(data.getOrDefault("proveedor", ""));
    NroDocumento.setText(data.getOrDefault("nrodocumento", ""));
    Celular.setText(data.getOrDefault("celular", ""));
    Direccion.setText(data.getOrDefault("direccion", ""));
    Ciudad.setSelectedIndex(Integer.parseInt(data.getOrDefault("ciudad_id", "0")) - 0);
    Activo.setSelected("1".equals(data.getOrDefault("activo", "0")));
    
    String tipoDocumento = data.getOrDefault("tipodocumento", "");
    if ("CI".equals(tipoDocumento)) {
        tipodocumento.setSelectedItem("CI");
        divisoria.setVisible(false);
        divisoria.setText("");
    } else if ("RUC".equals(tipoDocumento)) {
        tipodocumento.setSelectedItem("RUC");
        divisoria.setVisible(true);
        divisoria.setText(data.getOrDefault("divisoria", ""));
    } else {
        tipodocumento.setSelectedItem("Seleccione tipo de documento");
        divisoria.setVisible(false);
        divisoria.setText("");
    }

    String strFecha = data.get("fecha");
    if (strFecha != null && !strFecha.isEmpty()) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strFecha);
            Fecha.setDate(date);
        } catch (ParseException e) {
            Fecha.setDate(null);
            JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        Fecha.setDate(null);
    }
}

private void initializeTextFields() {
        applyNumericFilter(IdProveedor);
        applyAlphaFilter(Proveedor);
        applyRucFilter(NroDocumento);
        applyNumericFilter(Celular);
        applyDescriptionFilter(Direccion);
        applyDescriptionFilter(divisoria);
        addFocusListeners();
        addEnterKeyListenerToIdField();
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }
    private void applyDescriptionFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DescriptionFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }

    private void applyRucFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new RucDocumentFilter());
    }

    private void addFocusListeners() {
        IdProveedor.addFocusListener(new DefaultFocusListener(IdProveedor, true));
        Proveedor.addFocusListener(new DefaultFocusListener(Proveedor, false));
        divisoria.addFocusListener(new DefaultFocusListener(divisoria, false));
        NroDocumento.addFocusListener(new DefaultFocusListener(NroDocumento, true));
        Celular.addFocusListener(new DefaultFocusListener(Celular, true));
        Direccion.addFocusListener(new DefaultFocusListener(Direccion, false));
    }
        
    private boolean proveedorExiste(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return tc.existAny(params) > 0;
    }


  private void agregarTipoDocumento() {
    tipodocumento.removeAllItems(); 
    tipodocumento.addItem("Seleccione tipo de documento");
    tipodocumento.addItem("CI");
    tipodocumento.addItem("RUC");
}


    private void actualizarUltimoId() {
        try {
            int UltimoId = tc.getMaxId();
            ultimoId.setText(String.valueOf(UltimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEnterKeyListenerToIdField() {
        IdProveedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarProveedorPorId();
                }
            }
        });
    }

   private void buscarProveedorPorId() {
    String id = IdProveedor.getText().trim();
    if (!id.isEmpty()) {
        resetData();
        try {
            int idNum = Integer.parseInt(id);
             idProveedores = idNum;
            List<Map<String, String>> resultados = tc.buscarPorIdGenerico("PROVEEDORES", "id", idNum);
            if (resultados != null && !resultados.isEmpty()) {
                Map<String, String> resultado = resultados.get(0);
                System.out.println("Datos del proveedor encontrado: " + resultado);
                fillView(resultado);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el proveedor con ID: " + id);
                resetData();
                fillView(mapData);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
        }
    } else {
        JOptionPane.showMessageDialog(null, "Ingrese un ID para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}


    private int obtenerIdProveedorActual() {
        try {
            return Integer.parseInt(IdProveedor.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cargarCiudades() {
    Ciudad.removeAllItems();
    Ciudad.addItem("Seleccione una ciudad"); // Añade la opción inicial que actúa como prompt

    try {
        Map<String, String> where = new HashMap<>();
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", "");  // Los campos se dejan vacíos para traer todos los registros
        viewRegister.put("ciudad", "");

        ArrayList<Map<String, String>> resultados = td.searchListById(viewRegister, where);
        resultados.sort(new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                int id1 = Integer.parseInt(o1.get("id"));
                int id2 = Integer.parseInt(o2.get("id"));
                return Integer.compare(id1, id2);
            }
        });

        for (Map<String, String> ciudad : resultados) {
            String id = ciudad.get("id");
            String nombreCiudad = ciudad.get("ciudad");
            Ciudad.addItem(id + " - " + nombreCiudad); // Añade cada ciudad al JComboBox con su ID y nombre
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar ciudades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private int obtenerIdCiudadActual() {
        String selectedItem = (String) Ciudad.getSelectedItem();
        if (selectedItem != null && !selectedItem.equals("Seleccione una ciudad")) {
            try {
                return Integer.parseInt(selectedItem.split(" - ")[0]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay registros siguientes disponibles.");
        return -1; // Indica que no hay más registros.
    } else {
        try {
            // Actualizar el ID del proveedor actual para la navegación.
            idProveedores = Integer.parseInt(registro.getOrDefault("id", "-1"));

            // Actualizar la interfaz de usuario con los datos del registro.
            actualizarUIConDatosProveedor(registro);

            return 1; // Éxito
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Error en la conversión del ID.
        }
    }
}
private void actualizarUIConDatosProveedor(Map<String, String> datosProveedor) {
    IdProveedor.setText(datosProveedor.getOrDefault("id", ""));
    Proveedor.setText(datosProveedor.getOrDefault("proveedor", ""));
    NroDocumento.setText(datosProveedor.getOrDefault("nrodocumento", ""));
    Celular.setText(datosProveedor.getOrDefault("celular", ""));
    Direccion.setText(datosProveedor.getOrDefault("direccion", ""));
    Ciudad.setSelectedIndex(Integer.parseInt(datosProveedor.getOrDefault("ciudad_id", "0")) - 0);
    Activo.setSelected("1".equals(datosProveedor.getOrDefault("activo", "0")));

    // Manejo del tipo de documento
    String tipoDocumento = datosProveedor.getOrDefault("tipodocumento", "");
    if ("CI".equals(tipoDocumento)) {
        tipodocumento.setSelectedItem("CI");
        divisoria.setVisible(false);
        divisoria.setText("");
    } else if ("RUC".equals(tipoDocumento)) {
        tipodocumento.setSelectedItem("RUC");
        divisoria.setVisible(true);
        divisoria.setText(datosProveedor.getOrDefault("divisoria", ""));
    } else {
        tipodocumento.setSelectedItem("Seleccione tipo de documento");
        divisoria.setVisible(false);
        divisoria.setText("");
    }

    // Parsear y establecer la fecha si existe
    String fechaStr = datosProveedor.get("fecha");
    if (fechaStr != null && !fechaStr.isEmpty()) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fechaStr);
            Fecha.setDate(date);
        } catch (ParseException e) {
            Fecha.setDate(null);
            JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        Fecha.setDate(null);
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

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        lbl_id = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        lblProveedor = new javax.swing.JLabel();
        lblnrodocumento = new javax.swing.JLabel();
        lbl_celular = new javax.swing.JLabel();
        IdProveedor = new javax.swing.JTextField();
        Proveedor = new javax.swing.JTextField();
        NroDocumento = new javax.swing.JTextField();
        Celular = new javax.swing.JTextField();
        lbl_direccion = new javax.swing.JLabel();
        Direccion = new javax.swing.JTextField();
        lbl_ciudad = new javax.swing.JLabel();
        Ciudad = new javax.swing.JComboBox<>();
        Activo = new javax.swing.JCheckBox();
        lblultimo = new javax.swing.JLabel();
        ultimoId = new javax.swing.JLabel();
        lbl_tipo = new javax.swing.JLabel();
        tipodocumento = new javax.swing.JComboBox<>();
        Fecha = new com.toedter.calendar.JDateChooser();
        divisoria = new javax.swing.JTextField();

        jToggleButton1.setText("jToggleButton1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Proveedores");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(680, 270));

        jPanel1.setPreferredSize(new java.awt.Dimension(655, 300));

        lbl_id.setText("Id");

        lbl_fecha.setText("Fecha");

        lblProveedor.setText("Nombre y Apellido");

        lblnrodocumento.setBackground(new java.awt.Color(102, 102, 102));
        lblnrodocumento.setForeground(new java.awt.Color(102, 102, 102));
        lblnrodocumento.setText("Nro de Documento");

        lbl_celular.setForeground(new java.awt.Color(102, 102, 102));
        lbl_celular.setText(" Celular");

        NroDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NroDocumentoActionPerformed(evt);
            }
        });

        lbl_direccion.setForeground(new java.awt.Color(102, 102, 102));
        lbl_direccion.setText("Dirección");

        lbl_ciudad.setText("Ciudad");

        Ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CiudadActionPerformed(evt);
            }
        });

        Activo.setText("Activo");

        lblultimo.setText("Último");

        ultimoId.setBackground(new java.awt.Color(204, 204, 255));
        ultimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ultimoId.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        ultimoId.setOpaque(true);

        lbl_tipo.setForeground(new java.awt.Color(102, 102, 102));
        lbl_tipo.setText("Tipo de Documento");

        tipodocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipodocumentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_id)
                                .addGap(8, 8, 8)
                                .addComponent(IdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lblultimo)
                                .addGap(2, 2, 2)
                                .addComponent(ultimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_fecha)
                                .addGap(9, 9, 9)
                                .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lblProveedor)
                                .addGap(8, 8, 8)
                                .addComponent(Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lbl_ciudad)
                                        .addGap(12, 12, 12)
                                        .addComponent(Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(82, 82, 82)
                                        .addComponent(lbl_celular))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lbl_tipo)
                                        .addGap(9, 9, 9)
                                        .addComponent(tipodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblnrodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(lbl_direccion)
                                .addGap(10, 10, 10)
                                .addComponent(Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Activo)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(NroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(divisoria, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Celular, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_id)
                            .addComponent(lblultimo)
                            .addComponent(lbl_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ultimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblProveedor))
                    .addComponent(Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lbl_tipo))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tipodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblnrodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(divisoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Celular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Activo)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NroDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NroDocumentoActionPerformed
  String tipoDocumentoSeleccionado = (String) tipodocumento.getSelectedItem();
    if ("RUC".equals(tipoDocumentoSeleccionado)) {
        String ruc = NroDocumento.getText().trim();
        if (!ruc.isEmpty() && divisoria.isVisible()) {
            int dv = Functions.createDV(ruc, 11);
            divisoria.setText(String.valueOf(dv));
        } else {
            divisoria.setText("");
        }
    } else {
        divisoria.setText("");
    }
    }//GEN-LAST:event_NroDocumentoActionPerformed

    private void CiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CiudadActionPerformed
  String ciudadSeleccionada = (String) Ciudad.getSelectedItem();
    if (!"Seleccione una ciudad".equals(ciudadSeleccionada)) {
        int ciudadId = Integer.parseInt(ciudadSeleccionada.split(" - ")[0]);
        if (ciudadId > 0) {
            // La ciudad seleccionada es válida, puedes proceder con tu lógica aquí
            System.out.println("Ciudad seleccionada válida: " + ciudadSeleccionada);
        } else {
            // La ciudad seleccionada no es válida, muestra un mensaje de error si es necesario
            JOptionPane.showMessageDialog(this, "Seleccione una ciudad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData(); // Restablece los datos del formulario
            fillView(mapData); // Rellena el formulario con los datos actuales
            actualizarUltimoId(); // Actualiza el último ID mostrado
        }
    } else {
        // La opción "Seleccione una ciudad" fue seleccionada, puedes manejarlo aquí si es necesario
        System.out.println("Seleccione una ciudad");
    }
    }//GEN-LAST:event_CiudadActionPerformed

    private void tipodocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipodocumentoActionPerformed
 String tipoDocumentoSeleccionado = (String) tipodocumento.getSelectedItem();
    if (!"Seleccione tipo de documento".equals(tipoDocumentoSeleccionado)) {
        mapData.put("tipodocumento", tipoDocumentoSeleccionado);
        if ("RUC".equals(tipoDocumentoSeleccionado)) {
            divisoria.setVisible(true);
            divisoria.setText(""); // Clear the field when RUC is selected
        } else {
            divisoria.setVisible(false);
            divisoria.setText(""); // Clear the field when not RUC
        }
    } else {
        mapData.put("tipodocumento", "");
        divisoria.setVisible(false);
        divisoria.setText(""); // Clear the field when no valid type is selected
    }
    }//GEN-LAST:event_tipodocumentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JTextField Celular;
    private javax.swing.JComboBox<String> Ciudad;
    private javax.swing.JTextField Direccion;
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JTextField IdProveedor;
    private javax.swing.JTextField NroDocumento;
    private javax.swing.JTextField Proveedor;
    private javax.swing.JTextField divisoria;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lbl_celular;
    private javax.swing.JLabel lbl_ciudad;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_tipo;
    private javax.swing.JLabel lblnrodocumento;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JComboBox<String> tipodocumento;
    private javax.swing.JLabel ultimoId;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String idProveedor = IdProveedor.getText().trim();
    String proveedor = Proveedor.getText().trim();
    Date fechaDate = Fecha.getDate();  // Obtiene la fecha del JDateChooser
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String fecha = (fechaDate != null) ? sdf.format(fechaDate) : "";  // Formatea la fecha

    String nroDocumento = NroDocumento.getText().trim();
    String celular = Celular.getText().trim();
    String direccion = Direccion.getText().trim();
    int ciudadIndex = Ciudad.getSelectedIndex(); // Ajustar según cómo se gestionen las ciudades
    String activo = Activo.isSelected() ? "1" : "0";
    String tipoDocumento = (String) tipodocumento.getSelectedItem();
    String divisoriaValue = divisoria.getText().trim();

    // Verificar campos obligatorios
    if (idProveedor.isEmpty() || proveedor.isEmpty() || fecha.isEmpty() || ciudadIndex == 0) {
        JOptionPane.showMessageDialog(this, "Los campos ID, Proveedor, Fecha y Ciudad son obligatorios.",
                                      "Error", JOptionPane.ERROR_MESSAGE);
        resetData();
        System.out.println("Contenido de mapData: " + mapData);
        fillView(mapData);
        actualizarUltimoId();
        IdProveedor.requestFocusInWindow();
        return -1;
    }

    // Preparar los datos para la inserción o actualización
    mapData.clear();
    mapData.put("id", idProveedor);
    mapData.put("proveedor", proveedor);
    mapData.put("fecha", fecha); // Asumir que la fecha ya está en un formato adecuado
    mapData.put("celular", celular);
    mapData.put("direccion", direccion);
    mapData.put("ciudad_id", String.valueOf(ciudadIndex));
    mapData.put("activo", activo);

    // Inclusión condicional de nroDocumento, tipodocumento y divisoria
    if (!nroDocumento.isEmpty()) {
        mapData.put("nrodocumento", nroDocumento);
        if (!"Seleccione tipo de documento".equals(tipoDocumento)) {
            mapData.put("tipodocumento", tipoDocumento);
            if ("RUC".equals(tipoDocumento) && !divisoriaValue.isEmpty()) {
                mapData.put("divisoria", divisoriaValue);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de documento válido.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
            System.out.println("Contenido de mapData: " + mapData);
            fillView(mapData);
            actualizarUltimoId();
            IdProveedor.requestFocusInWindow();
            return -1;
        }
    } else if (!"Seleccione tipo de documento".equals(tipoDocumento)) {
        JOptionPane.showMessageDialog(this, "Ingrese el número de documento correspondiente al tipo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        resetData();
        System.out.println("Contenido de mapData: " + mapData);
        fillView(mapData);
        actualizarUltimoId();
        IdProveedor.requestFocusInWindow();
        return -1;
    }

    boolean isUpdate = proveedorExiste(idProveedor);
    int rowsAffected = 0;

    if (isUpdate) {
        // Buscar los datos actuales del proveedor para comparar
        Map<String, String> existingData = tc.searchById(mapData);
        if (existingData != null && existingData.equals(mapData)) {
            JOptionPane.showMessageDialog(this, "No hay cambios para guardar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return 0;  // No se realizan cambios si los datos son iguales
        }

        // Preparar la lista de datos para actualizar
        ArrayList<Map<String, String>> updateList = new ArrayList<>();
        updateList.add(mapData);
        rowsAffected = tc.updateReg(updateList); // Actualizar el proveedor en la base de datos
    } else {
        // Crear un nuevo proveedor
        rowsAffected = tc.createReg(mapData);
    }

    // Mostrar mensaje de éxito o error según el resultado de la operación
    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(this, isUpdate ? "Proveedor actualizado correctamente." : "Proveedor registrado correctamente.",
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo realizar la operación solicitada.",
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }
    resetData(); // Resetear los campos
    System.out.println("Contenido de mapData: " + mapData);
    fillView(mapData);
    actualizarUltimoId();
    IdProveedor.requestFocusInWindow();
    return rowsAffected;
}

@Override
public int imBorrar(String crud) {
    // Verificar que el campo ID esté lleno
    if (IdProveedor.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un proveedor antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    int idProveedorActual = obtenerIdProveedorActual(); // Método para obtener el ID actual del campo de texto
    if (idProveedorActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    // Preparar el registro a eliminar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idProveedorActual));
    registrosParaBorrar.add(registro);

    // Intentar eliminar el registro
    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Proveedor eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        resetData(); // Resetea los campos
        fillView(mapData); // Actualiza la vista con los datos por defecto
        actualizarUltimoId(); // Actualiza el último ID mostrado
        return resultado;
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}

@Override
public int imNuevo() {
    resetData();
    System.out.println("Contenido de mapData: " + mapData);
    fillView(mapData);
    actualizarUltimoId();
    idProveedores = -1;
    return 0;
}

@Override
public int imBuscar() {
     buscarProveedorPorId();
    return 0;
}

@Override
public int imPrimero() {
    Map<String, String> registro = tc.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}
@Override
public int imSiguiente() {
    if (idProveedores == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idProveedores), "NEXT");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imAnterior() {
    if (idProveedores <= 1) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idProveedores), "PRIOR");
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
                job.setJobName("Proveedores");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
}

@Override
public int imInsFilas() {
    // Funcionalidad para insertar filas si es necesario
    JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
    return -1;
}

@Override
public int imDelFilas() {
    // Funcionalidad para eliminar filas si es necesario
    JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
    return -1;
}
@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    System.out.println("Datos seleccionados recibidos: " + datosSeleccionados);

    String idStr = datosSeleccionados.get("Codigo"); // Asegúrate de que 'id' sea la clave correcta.
    String proveedor = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idProveedor = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("PROVEEDORES", "id", idProveedor);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                final String ciudadId = registro.get("ciudad_id");
                final String activoStr = registro.get("activo");
                final boolean activo = "1".equals(activoStr);
                final String nroDocumento = registro.get("nrodocumento");
                final String celular = registro.get("celular");
                final String Divisoria = registro.get("divisoria");
                final String direccion = registro.get("direccion");
                final String tipoDocumento = registro.get("tipodocumento");
                final String strFecha= registro.get("fecha");
                idProveedores = idProveedor;
                SwingUtilities.invokeLater(() -> {
                    IdProveedor.setText(idStr);
                    Proveedor.setText(proveedor);
                    NroDocumento.setText(nroDocumento);
                    Celular.setText(celular);
                    Direccion.setText(direccion);
                    Ciudad.setSelectedIndex(Integer.parseInt(ciudadId) - 1);
                    Activo.setSelected(activo);

                    if (strFecha != null && !strFecha.isEmpty()) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strFecha);
                            Fecha.setDate(date);
                        } catch (ParseException e) {
                            JOptionPane.showMessageDialog(this, "Error al parsear la fecha: " + e.getMessage(), "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        Fecha.setDate(null);
                    }

                    // Manejo del tipo de documento
                    if ("CI".equals(tipoDocumento)) {
                        tipodocumento.setSelectedItem("CI");
                        divisoria.setVisible(false);
                        divisoria.setText("");
                    } else if ("RUC".equals(tipoDocumento)) {
                        tipodocumento.setSelectedItem("RUC");
                        divisoria.setVisible(true);
                        divisoria.setText(Divisoria);
                    } else {
                        tipodocumento.setSelectedItem("Seleccione tipo de documento");
                        divisoria.setVisible(false);
                        divisoria.setText("");
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un proveedor con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de proveedor inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    @Override
    public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaProveedores = Arrays.asList("id", "proveedor");
    Form_Buscar buscadorProveedores = new Form_Buscar(parentFrame, true, tc, "PROVEEDORES", columnasParaProveedores);
    buscadorProveedores.setOnItemSeleccionadoListener(this);
    buscadorProveedores.setVisible(true);
    return 0;
    }
}
