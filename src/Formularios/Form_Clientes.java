/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
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

public class Form_Clientes extends javax.swing.JInternalFrame implements InterfaceUsuario{
    private DBTableController tc;
    private DBTableController td;
    private DBTableController tp;
    private Map<String, String> mapData;
    private int idClientes;
    
    public Form_Clientes() {
        initComponents();
        tc = new DBTableController();
        tc.iniciar("CLIENTES");
        td = new DBTableController();
        td.iniciar("CIUDADES");
        tp = new DBTableController();
        tp.iniciar("PRECIOS");
        mapData = new HashMap<>();
       idClientes = -1;
       actualizarUltimoId();
       agregarTipoDocumento();
       cargarCiudades();
       cargarPrecios();
       initializeTextFields();
    }
    private void setMapData() {
        mapData.clear();
        mapData.put("id", IdCliente.getText().trim());
        mapData.put("cliente", cliente.getText().trim());
        mapData.put("apellido", Apellido.getText().trim());
        mapData.put("fecha", Optional.ofNullable(Fecha.getDate())
                .map(date -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
                .orElseThrow(() -> new RuntimeException("La fecha es obligatoria.")));

        String tipoDocumento = (String) tipodocumento.getSelectedItem();
        mapData.put("tipodocumento", tipoDocumento);
        mapData.put("nrodocumento", NroDocumento.getText().trim());
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
            mapData.put("divisoria", "");
        }
        mapData.put("celular", Celular.getText().trim());
        String direccion = Direccion.getText().trim();
        if (!direccion.isEmpty()) {
            mapData.put("direccion", direccion);
        }
        String ciudadSeleccionada = (String) Ciudad.getSelectedItem();
        int ciudadId = Integer.parseInt(ciudadSeleccionada.split(" - ")[0]);
        mapData.put("ciudad_id", String.valueOf(ciudadId));
        String precioSeleccionado = (String) Precio.getSelectedItem();
        int precioId = Integer.parseInt(precioSeleccionado.split(" - ")[0]);
        mapData.put("precio_id", String.valueOf(precioId));
        mapData.put("activo", Activo.isSelected() ? "1" : "0");
    }

    private void resetData() { 
        IdCliente.setText("0");
        cliente.setText("");
        Apellido.setText("");
        NroDocumento.setText("");
        Celular.setText("");
        Direccion.setText("");
        divisoria.setText("");
        tipodocumento.setSelectedIndex(0);
        Ciudad.setSelectedIndex(0);
        Precio.setSelectedIndex(0);
        Activo.setSelected(false);
        Fecha.setDate(null);
        mapData.clear();
    }

    private void fillView(Map<String, String> data) {
        IdCliente.setText(data.getOrDefault("id", ""));
        cliente.setText(data.getOrDefault("cliente", ""));
        Apellido.setText(data.getOrDefault("apellido", ""));
        NroDocumento.setText(data.getOrDefault("nrodocumento", ""));
        Celular.setText(data.getOrDefault("celular", ""));
        Direccion.setText(data.getOrDefault("direccion", ""));
       Ciudad.setSelectedIndex(Integer.parseInt(data.getOrDefault("ciudad_id", "0")) - 0);
        Precio.setSelectedIndex(Integer.parseInt(data.getOrDefault("precio_id", "0")) - 0);
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
        applyNumericFilter(IdCliente);
        applyAlphaFilter(cliente);
        applyAlphaFilter(Apellido);
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
        IdCliente.addFocusListener(new DefaultFocusListener(IdCliente, true));
        cliente.addFocusListener(new DefaultFocusListener(cliente, false));
        Apellido.addFocusListener(new DefaultFocusListener(Apellido, false));
        NroDocumento.addFocusListener(new DefaultFocusListener(NroDocumento, true));
        Celular.addFocusListener(new DefaultFocusListener(Celular, true));
        Direccion.addFocusListener(new DefaultFocusListener(Direccion, false));
    }

    private boolean clienteExiste(String id) {
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
        IdCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarClientePorId();
                }
            }
        });
    }

    private void buscarClientePorId() {
        String id = IdCliente.getText().trim();
        if (!id.isEmpty()) {
            resetData();
            try {
                int idNum = Integer.parseInt(id);
                idClientes = idNum;
                List<Map<String, String>> resultados = tc.buscarPorIdGenerico("CLIENTES", "id", idNum);
                if (resultados != null && !resultados.isEmpty()) {
                    Map<String, String> resultado = resultados.get(0);
                    System.out.println("Datos del cliente encontrado: " + resultado);
                    fillView(resultado);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el cliente con ID: " + id);
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

    private int obtenerIdClienteActual() {
        try {
            return Integer.parseInt(IdCliente.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cargarCiudades() {
        Ciudad.removeAllItems();
        Ciudad.addItem("Seleccione una ciudad");

        try {
            Map<String, String> where = new HashMap<>();
            Map<String, String> viewRegister = new HashMap<>();
            viewRegister.put("id", "");
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
                Ciudad.addItem(id + " - " + nombreCiudad);
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

    private void cargarPrecios() {
        Precio.removeAllItems();
        Precio.addItem("Seleccione un precio");

        try {
            Map<String, String> where = new HashMap<>();
            Map<String, String> viewRegister = new HashMap<>();
            viewRegister.put("id", "");
            viewRegister.put("listaprecio", "");

            ArrayList<Map<String, String>> resultados = tp.searchListById(viewRegister, where);
            resultados.sort(new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    int id1 = Integer.parseInt(o1.get("id"));
                    int id2 = Integer.parseInt(o2.get("id"));
                    return Integer.compare(id1, id2);
                }
            });

            for (Map<String, String> precio : resultados) {
                String id = precio.get("id");
                String nombrePrecio = precio.get("listaprecio");
                Precio.addItem(id + " - " + nombrePrecio);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar precios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerIdPrecioActual() {
        String selectedItem = (String) Precio.getSelectedItem();
        if (selectedItem != null && !selectedItem.equals("Seleccione un precio")) {
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
            return -1;
        } else {
            try {
                idClientes = Integer.parseInt(registro.getOrDefault("id", "-1"));
                actualizarUIConDatosCliente(registro);
                return 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    private void actualizarUIConDatosCliente(Map<String, String> datosCliente) {
        IdCliente.setText(datosCliente.getOrDefault("id", ""));
        cliente.setText(datosCliente.getOrDefault("cliente", ""));
        Apellido.setText(datosCliente.getOrDefault("apellido", ""));
        NroDocumento.setText(datosCliente.getOrDefault("nrodocumento", ""));
        Celular.setText(datosCliente.getOrDefault("celular", ""));
        Direccion.setText(datosCliente.getOrDefault("direccion", ""));
        Ciudad.setSelectedIndex(Integer.parseInt(datosCliente.getOrDefault("ciudad_id", "0")) - 0);
        Precio.setSelectedIndex(Integer.parseInt(datosCliente.getOrDefault("precio_id", "0")) - 0);
        Activo.setSelected("1".equals(datosCliente.getOrDefault("activo", "0")));

        String tipoDocumento = datosCliente.getOrDefault("tipodocumento", "");
        if ("CI".equals(tipoDocumento)) {
            tipodocumento.setSelectedItem("CI");
            divisoria.setVisible(false);
            divisoria.setText("");
        } else if ("RUC".equals(tipoDocumento)) {
            tipodocumento.setSelectedItem("RUC");
            divisoria.setVisible(true);
            divisoria.setText(datosCliente.getOrDefault("divisoria", ""));
        } else {
            tipodocumento.setSelectedItem("Seleccione tipo de documento");
            divisoria.setVisible(false);
            divisoria.setText("");
        }

        String fechaStr = datosCliente.get("fecha");
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
        lbNombre = new javax.swing.JLabel();
        lblnrodocumento = new javax.swing.JLabel();
        lbl_celular = new javax.swing.JLabel();
        IdCliente = new javax.swing.JTextField();
        cliente = new javax.swing.JTextField();
        NroDocumento = new javax.swing.JTextField();
        Celular = new javax.swing.JTextField();
        lbl_direccion = new javax.swing.JLabel();
        Direccion = new javax.swing.JTextField();
        lbl_correo = new javax.swing.JLabel();
        Ciudad = new javax.swing.JComboBox<>();
        Activo = new javax.swing.JCheckBox();
        lblultimo = new javax.swing.JLabel();
        ultimoId = new javax.swing.JLabel();
        lbl_tipo = new javax.swing.JLabel();
        tipodocumento = new javax.swing.JComboBox<>();
        Fecha = new com.toedter.calendar.JDateChooser();
        divisoria = new javax.swing.JTextField();
        Apellido = new javax.swing.JTextField();
        lbl_apellido = new javax.swing.JLabel();
        Correo = new javax.swing.JTextField();
        lbl_ciudad = new javax.swing.JLabel();
        Precio = new javax.swing.JComboBox<>();
        lbl_precio = new javax.swing.JLabel();

        jToggleButton1.setText("jToggleButton1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Clientes");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(690, 305));

        jPanel1.setPreferredSize(new java.awt.Dimension(655, 305));

        lbl_id.setText("Id");

        lbl_fecha.setText("Fecha");

        lbNombre.setText("Nombre/s");

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

        lbl_correo.setForeground(new java.awt.Color(102, 102, 102));
        lbl_correo.setText("Correo");

        Ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CiudadActionPerformed(evt);
            }
        });

        Activo.setText("Activo");

        lblultimo.setText("Último");

        ultimoId.setBackground(new java.awt.Color(204, 204, 255));
        ultimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ultimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ultimoId.setOpaque(true);

        lbl_tipo.setForeground(new java.awt.Color(102, 102, 102));
        lbl_tipo.setText("Tipo de Documento");

        tipodocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipodocumentoActionPerformed(evt);
            }
        });

        lbl_apellido.setText("Apellido/s");

        lbl_ciudad.setText("Ciudad");

        Precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecioActionPerformed(evt);
            }
        });

        lbl_precio.setForeground(new java.awt.Color(102, 102, 102));
        lbl_precio.setText("Precio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_tipo)
                                .addGap(9, 9, 9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addComponent(lbl_apellido))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Correo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tipodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_celular)
                                    .addComponent(lblnrodocumento)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(lbl_precio)
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_ciudad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Precio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ciudad, 0, 210, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_direccion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_id)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblultimo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ultimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_fecha))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_correo)
                                .addGap(344, 344, 344)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(NroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(divisoria, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Activo)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Celular, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(Direccion)))))
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ultimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_id)
                                        .addComponent(IdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblultimo)))
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_fecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Fecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_apellido)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbNombre)
                            .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Celular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_ciudad))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Activo)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_precio)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
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
                System.out.println("Ciudad seleccionada válida: " + ciudadSeleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una ciudad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                resetData();
                fillView(mapData);
                actualizarUltimoId();
            }
        } else {
            System.out.println("Seleccione una ciudad");
        }
    }//GEN-LAST:event_CiudadActionPerformed

    private void tipodocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipodocumentoActionPerformed
 String tipoDocumentoSeleccionado = (String) tipodocumento.getSelectedItem();
        if (!"Seleccione tipo de documento".equals(tipoDocumentoSeleccionado)) {
            mapData.put("tipodocumento", tipoDocumentoSeleccionado);
            if ("RUC".equals(tipoDocumentoSeleccionado)) {
                divisoria.setVisible(true);
                divisoria.setText("");
            } else {
                divisoria.setVisible(false);
                divisoria.setText("");
            }
        } else {
            mapData.put("tipodocumento", "");
            divisoria.setVisible(false);
            divisoria.setText("");
        }
    }//GEN-LAST:event_tipodocumentoActionPerformed

    private void PrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecioActionPerformed
    String precioSeleccionado = (String) Precio.getSelectedItem();
        if (!"Seleccione un precio".equals(precioSeleccionado)) {
            int precioId = Integer.parseInt(precioSeleccionado.split(" - ")[0]);
            if (precioId > 0) {
                System.out.println("Precio seleccionado válido: " + precioSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                resetData();
                fillView(mapData);
                actualizarUltimoId();
            }
        } else {
            System.out.println("Seleccione un precio");
        }
    }//GEN-LAST:event_PrecioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JTextField Apellido;
    private javax.swing.JTextField Celular;
    private javax.swing.JComboBox<String> Ciudad;
    private javax.swing.JTextField Correo;
    private javax.swing.JTextField Direccion;
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JTextField IdCliente;
    private javax.swing.JTextField NroDocumento;
    private javax.swing.JComboBox<String> Precio;
    private javax.swing.JTextField cliente;
    private javax.swing.JTextField divisoria;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lbNombre;
    private javax.swing.JLabel lbl_apellido;
    private javax.swing.JLabel lbl_celular;
    private javax.swing.JLabel lbl_ciudad;
    private javax.swing.JLabel lbl_correo;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_precio;
    private javax.swing.JLabel lbl_tipo;
    private javax.swing.JLabel lblnrodocumento;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JComboBox<String> tipodocumento;
    private javax.swing.JLabel ultimoId;
    // End of variables declaration//GEN-END:variables
 @Override
    public int imGuardar(String crud) {
        String idCliente = IdCliente.getText().trim();
        String cliente = this.cliente.getText().trim();
        String apellido = Apellido.getText().trim();
        Date fechaDate = Fecha.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = (fechaDate != null) ? sdf.format(fechaDate) : "";

        String nroDocumento = NroDocumento.getText().trim();
        String celular = Celular.getText().trim();
        String direccion = Direccion.getText().trim();
        int ciudadIndex = Ciudad.getSelectedIndex();
        int precioIndex = Precio.getSelectedIndex();
        String activo = Activo.isSelected() ? "1" : "0";
        String tipoDocumento = (String) tipodocumento.getSelectedItem();
        String divisoriaValue = divisoria.getText().trim();

        if (idCliente.isEmpty() || cliente.isEmpty() || apellido.isEmpty() || fecha.isEmpty() || ciudadIndex == 0 || precioIndex == 0) {
            JOptionPane.showMessageDialog(this, "Los campos ID, Cliente, Apellido, Fecha, Ciudad y Precio son obligatorios.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
            fillView(mapData);
            actualizarUltimoId();
            IdCliente.requestFocusInWindow();
            return -1;
        }

        mapData.clear();
        mapData.put("id", idCliente);
        mapData.put("cliente", cliente);
        mapData.put("apellido", apellido);
        mapData.put("fecha", fecha);
        mapData.put("celular", celular);
        mapData.put("direccion", direccion);
        mapData.put("ciudad_id", String.valueOf(ciudadIndex));
        mapData.put("precio_id", String.valueOf(precioIndex));
        mapData.put("activo", activo);

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
                fillView(mapData);
                actualizarUltimoId();
                IdCliente.requestFocusInWindow();
                return -1;
            }
        } else if (!"Seleccione tipo de documento".equals(tipoDocumento)) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de documento correspondiente al tipo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
            fillView(mapData);
            actualizarUltimoId();
            IdCliente.requestFocusInWindow();
            return -1;
        }

        boolean isUpdate = clienteExiste(idCliente);
        int rowsAffected = 0;

        if (isUpdate) {
            Map<String, String> existingData = tc.searchById(mapData);
            if (existingData != null && existingData.equals(mapData)) {
                JOptionPane.showMessageDialog(this, "No hay cambios para guardar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }

            ArrayList<Map<String, String>> updateList = new ArrayList<>();
            updateList.add(mapData);
            rowsAffected = tc.updateReg(updateList);
        } else {
            rowsAffected = tc.createReg(mapData);
        }

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, isUpdate ? "Cliente actualizado correctamente." : "Cliente registrado correctamente.",
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo realizar la operación solicitada.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
        resetData();
        fillView(mapData);
        actualizarUltimoId();
        IdCliente.requestFocusInWindow();
        return rowsAffected;
    }

    @Override
    public int imBorrar(String crud) {
        if (IdCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        int idClienteActual = obtenerIdClienteActual();
        if (idClienteActual <= 0) {
            JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return 0;
        }

        ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
        Map<String, String> registro = new HashMap<>();
        registro.put("id", String.valueOf(idClienteActual));
        registrosParaBorrar.add(registro);

        int resultado = tc.deleteReg(registrosParaBorrar);
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            resetData();
            fillView(mapData);
            actualizarUltimoId();
            return resultado;
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    @Override
    public int imNuevo() {
        resetData();
        fillView(mapData);
        actualizarUltimoId();
        idClientes = -1;
        return 0;
    }

    @Override
    public int imBuscar() {
        buscarClientePorId();
        return 0;
    }

    @Override
    public int imPrimero() {
        Map<String, String> registro = tc.navegationReg(null, "FIRST");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imSiguiente() {
        if (idClientes == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idClientes), "NEXT");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imAnterior() {
        if (idClientes <= 1) {
            JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idClientes), "PRIOR");
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
                job.setJobName("Clientes");
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
    String Cliente = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idCliente = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("CLIENTES", "id", idCliente);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                final String ciudadId = registro.get("ciudad_id");
                final String precioId = registro.get("precio_id");
                final String activoStr = registro.get("activo");
                final boolean activo = "1".equals(activoStr);
                final String nroDocumento = registro.get("nrodocumento");
                final String celular = registro.get("celular");
                final String Divisoria = registro.get("divisoria");
                final String direccion = registro.get("direccion");
                final String tipoDocumento = registro.get("tipodocumento");
                final String strFecha = registro.get("fecha");
                idClientes = idCliente;

                SwingUtilities.invokeLater(() -> {
                    IdCliente.setText(idStr);
                    cliente.setText(Cliente);
                    NroDocumento.setText(nroDocumento);
                    Celular.setText(celular);
                    Direccion.setText(direccion);

                    // Seleccionar la ciudad en el JComboBox
                    for (int i = 0; i < Ciudad.getItemCount(); i++) {
                        if (Ciudad.getItemAt(i).startsWith(ciudadId + " -")) {
                            Ciudad.setSelectedIndex(i);
                            break;
                        }
                    }

                    // Seleccionar el precio en el JComboBox
                    for (int i = 0; i < Precio.getItemCount(); i++) {
                        if (Precio.getItemAt(i).startsWith(precioId + " -")) {
                            Precio.setSelectedIndex(i);
                            break;
                        }
                    }

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
                JOptionPane.showMessageDialog(null, "No se encontró un cliente con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de cliente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    @Override
    public int imFiltrar() {

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        List<String> columnasParaClientes = Arrays.asList("id", "cliente");
        Form_Buscar buscadorClientes = new Form_Buscar(parentFrame, true, tc, "CLIENTES", columnasParaClientes);
        buscadorClientes.setOnItemSeleccionadoListener(this);
        buscadorClientes.setVisible(true);
        return 0;
    }
   
}