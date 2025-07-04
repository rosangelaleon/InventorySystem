
package Formularios;

import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Modelo.cargaComboBox;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import javax.swing.*;

public class Form_CierreCaja extends javax.swing.JInternalFrame implements InterfaceUsuario{
    
    private final Map<String, String> myData, cajas, datosCaja;
    String field, itemCaja = "", idApertura = "0";
    
    String sqlAperturaCaja = "SELECT fechaApertura, fechaCierre, montoInicial, montoFinal, montoRetirado, idCaja, usuario FROM aperturacajas, usuarios, cajas WHERE aperturacajas.idUsuario = usuarios.id AND aperturacajas.idCaja = cajas.id AND aperturacajas.id = ";
    String sqlArqueoCaja = "SELECT * FROM arqueocajas, aperturacajas, cajas, usuarios WHERE arqueocajas.idApertura = aperturacajas.id AND aperturacajas.idCaja = cajas.id AND aperturacajas.idUsuario = usuarios.id AND arqueocajas.id = ";
    boolean isImprimir = true, view = false, showDate = false;


    public Form_CierreCaja() {
        initComponents();
        agregarOpcionesOrdenar() ;
         cargaComboBox.pv_cargar(cboxCajas, "CAJAS", "id,nombre", "id", "");
        myData = new HashMap<>();
        cajas = new HashMap<>();
        datosCaja = new HashMap<>();
        PlainDocument docId = (PlainDocument) txtId.getDocument();

        txtTotal.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }
        });

        txtMontoFinal.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldStateDiferencia();
            }
        });

        txtMontoRetirado.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldStateQuedaCaja();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldStateQuedaCaja();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldStateQuedaCaja();
            }
        });
        addFocusListeners() ;

    }

       private void agregarOpcionesOrdenar() {
        cboxCajas.addItem("0-Seleccionar");
        cboxCajas.addItem("1-Caja Principal");
        cboxCajas.addItem("2-Caja Secundaria");
        cboxCajas.addItem("2-Caja Prueba");
        
    }

            private void addFocusListeners() {
        txtId.addFocusListener(new DefaultFocusListener(txtId, true));
 
    }
    private void updateFieldStateDiferencia() {
        String total = txtTotal.getText().replace(".", "").replace(",", ".");
        String montoFinal = txtMontoFinal.getText().replace(".", "").replace(",", ".");
        String montoRetirado = txtMontoRetirado.getText().replace(".", "").replace(",", ".");
        if (montoFinal.equals("")) {
            txtDiferencia.setText(total);
            txtQuedaCaja.setText("0");
        } else if (total.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?") && montoFinal.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {
            double diferencia = Double.parseDouble(total) - Double.parseDouble(montoFinal);
            
            if (montoRetirado.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {
                double queda = Double.parseDouble(montoFinal) - Double.parseDouble(montoRetirado);
                
            } else {
                if (montoFinal.equals("")) {
                    txtQuedaCaja.setText("0");
                } else {
              
                }
            }
        }
    }

    private void updateFieldStateQuedaCaja() {
        String quedaCaja = txtQuedaCaja.getText().replace(".", "").replace(",", ".");
        String montoRetirado = txtMontoRetirado.getText().replace(".", "").replace(",", ".");
        if (montoRetirado.equals("")) {
            if (txtMontoFinal.getText().equals("")) {
                txtQuedaCaja.setText("0");
            } else {
                txtQuedaCaja.setText(txtMontoFinal.getText());
            }
        } else if (montoRetirado.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?") && quedaCaja.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {
            double queda = Double.parseDouble(quedaCaja) - Double.parseDouble(montoRetirado);
          
        }
    }


    private boolean setData() {

        myData.put("totalVentas", txtTotalVentas.getText().replace(".", "").replace(",", "."));
        myData.put("totalCompras", txtTotalCompras.getText().replace(".", "").replace(",", "."));
        myData.put("totalCobros", txtTotalCobros.getText().replace(".", "").replace(",", "."));
        myData.put("totalPagos", txtTotalPagos.getText().replace(".", "").replace(",", "."));
        myData.put("totalArqueo", txtTotal.getText().replace(".", "").replace(",", "."));

        String montoRetirado = txtMontoRetirado.getText().replace(".", "").replace(",", ".");

        myData.put("quedaCaja", txtQuedaCaja.getText().replace(".", "").replace(",", "."));

        return true;
    }

    private void resetData() {
        myData.put("id", "0");
        myData.put("idCaja", "0");
        myData.put("fechaApertura", "");
        myData.put("fechaCierre", "");
        myData.put("usuario", "");
        myData.put("saldoActual", "0");
        myData.put("totalVentas", "0");
        myData.put("totalCompras", "0");
        myData.put("totalCobros", "0");
        myData.put("totalPagos", "0");
        myData.put("totalArqueo", "0");
        myData.put("montoFinal", "0");
        myData.put("montoRetirado", "0");
        myData.put("quedaCaja", "0");
    }

    private void fillView(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    txtId.setText(value);
                    break;
                case "idApertura":
                    this.idApertura = value;
                    break;
                case "idCaja":
 
                    view = true;
                    for (Map.Entry<String, String> valor : cajas.entrySet()) {
                        if (valor.getKey().equals(value)) {
                            cboxCajas.setSelectedItem(valor.getValue());
                            itemCaja = cboxCajas.getSelectedItem().toString();
                        }
                    }
                    cboxCajas.setEnabled(false);
                    cboxCajas.setFocusable(false);
                    break;
                case "fechaApertura":
                  
                    break;
                case "fechaCierre":
                   
                    break;
                case "montoInicial":
                    
                    break;
                case "totalVentas":
                
                    break;
                case "totalCompras":
                    
                    break;
                case "totalCobros":
                   
                    break;
                case "totalPagos":
                   
                    break;
                case "totalArqueo":
         
                    break;
                case "montoFinal":
                  
                    break;
                case "montoRetirado":
                    
                    break;
            }
        }

    }

    private void resetCampos() {
        txtId.setText("0");
        cboxCajas.setSelectedIndex(-1);
        cboxCajas.setEnabled(true);
        cboxCajas.setFocusable(true);

        txtTotalVentas.setText("0");
        txtTotalCompras.setText("0");
        txtTotalPagos.setText("0");
        txtTotalCobros.setText("0");
        txtTotal.setText("0");
        txtMontoFinal.setText("0");
        txtDiferencia.setText("0");
        txtMontoRetirado.setText("0");
        txtQuedaCaja.setText("0");
        view = false;

    }

    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblFechaApertura = new javax.swing.JLabel();
        lblFechaCierre = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblMontoInicial = new javax.swing.JLabel();
        lblTotalVentas = new javax.swing.JLabel();
        lblPagos = new javax.swing.JLabel();
        lblCobros = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        txtTotalCobros = new javax.swing.JTextField();
        txtTotalPagos = new javax.swing.JTextField();
        txtTotalVentas = new javax.swing.JTextField();
        txtMontoInicial = new javax.swing.JTextField();
        lblTotalCompras = new javax.swing.JLabel();
        txtTotalCompras = new javax.swing.JTextField();
        lblCaja = new javax.swing.JLabel();
        cboxCajas = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        lblId1 = new javax.swing.JLabel();
        Ultimo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblDiferencia = new javax.swing.JLabel();
        lblMontoFinal = new javax.swing.JLabel();
        txtMontoFinal = new javax.swing.JFormattedTextField();
        txtDiferencia = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblMontoRetirado = new javax.swing.JLabel();
        txtMontoRetirado = new javax.swing.JFormattedTextField();
        lblQuedaCaja = new javax.swing.JLabel();
        txtQuedaCaja = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cierre Caja");
        setPreferredSize(new java.awt.Dimension(604, 370));
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

        lblId.setText("Id");

        lblFechaApertura.setText("Fecha Apertura");

        lblFechaCierre.setText("Fecha Cierre");

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Arqueo"));

        lblMontoInicial.setText("+ Monto Inicial");

        lblTotalVentas.setText("+ Total Ventas");

        lblPagos.setText("- Total Pagos");

        lblCobros.setText("+ Total Cobros");

        lblTotal.setText("Total:");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(204, 204, 255));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setText("0");
        txtTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotal.setFocusable(false);
        txtTotal.setOpaque(true);

        txtTotalCobros.setEditable(false);
        txtTotalCobros.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalCobros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCobros.setText("0");
        txtTotalCobros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCobros.setFocusable(false);
        txtTotalCobros.setOpaque(true);

        txtTotalPagos.setEditable(false);
        txtTotalPagos.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalPagos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalPagos.setText("0");
        txtTotalPagos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalPagos.setFocusable(false);
        txtTotalPagos.setOpaque(true);

        txtTotalVentas.setEditable(false);
        txtTotalVentas.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalVentas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVentas.setText("0");
        txtTotalVentas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalVentas.setFocusable(false);

        txtMontoInicial.setEditable(false);
        txtMontoInicial.setBackground(new java.awt.Color(204, 204, 255));
        txtMontoInicial.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMontoInicial.setText("0");
        txtMontoInicial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtMontoInicial.setFocusable(false);
        txtMontoInicial.setOpaque(true);

        lblTotalCompras.setText("- Total Compras");

        txtTotalCompras.setEditable(false);
        txtTotalCompras.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalCompras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCompras.setText("0");
        txtTotalCompras.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalCompras.setFocusable(false);
        txtTotalCompras.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblTotalCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMontoInicial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotalVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPagos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCobros, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator2)
                    .addComponent(txtTotalCompras)
                    .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(txtMontoInicial)
                    .addComponent(txtTotalVentas)
                    .addComponent(txtTotalPagos)
                    .addComponent(txtTotalCobros, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMontoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMontoInicial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalCobros, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCobros))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPagos)
                    .addComponent(txtTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalCompras)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtTotalCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal))
                .addContainerGap())
        );

        lblCaja.setText("Caja");

        cboxCajas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboxCajasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboxCajasFocusLost(evt);
            }
        });
        cboxCajas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxCajasActionPerformed(evt);
            }
        });

        lblId1.setText("Último");

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setText("3");
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setOpaque(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cierre"));

        lblDiferencia.setText("Diferencia:");

        lblMontoFinal.setText("Monto en caja:");

        txtMontoFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.##"))));
        txtMontoFinal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMontoFinal.setText("0");
        txtMontoFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMontoFinalFocusGained(evt);
            }
        });
        txtMontoFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoFinalActionPerformed(evt);
            }
        });
        txtMontoFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoFinalKeyReleased(evt);
            }
        });

        txtDiferencia.setEditable(false);
        txtDiferencia.setBackground(new java.awt.Color(204, 204, 255));
        txtDiferencia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiferencia.setText("0");
        txtDiferencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtDiferencia.setFocusable(false);
        txtDiferencia.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblMontoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMontoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMontoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMontoFinal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDiferencia)
                    .addComponent(txtDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Retiro"));

        lblMontoRetirado.setText("Monto a retirar:");

        txtMontoRetirado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.##"))));
        txtMontoRetirado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMontoRetirado.setText("0");
        txtMontoRetirado.setToolTipText("");
        txtMontoRetirado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMontoRetiradoFocusGained(evt);
            }
        });
        txtMontoRetirado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoRetiradoActionPerformed(evt);
            }
        });
        txtMontoRetirado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoRetiradoKeyReleased(evt);
            }
        });

        lblQuedaCaja.setText("Queda en caja:");

        txtQuedaCaja.setEditable(false);
        txtQuedaCaja.setBackground(new java.awt.Color(204, 204, 255));
        txtQuedaCaja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQuedaCaja.setText("0");
        txtQuedaCaja.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtQuedaCaja.setFocusable(false);
        txtQuedaCaja.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblMontoRetirado, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addGap(29, 29, 29))
                    .addComponent(lblQuedaCaja))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtMontoRetirado, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtQuedaCaja, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMontoRetirado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMontoRetirado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQuedaCaja)
                    .addComponent(txtQuedaCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblId)
                            .addComponent(lblCaja))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblId1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Ultimo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(cboxCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaCierre)
                            .addComponent(lblFechaApertura))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblId)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblId1)
                        .addComponent(Ultimo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFechaApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFechaCierre, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCaja)
                            .addComponent(cboxCajas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdFocusGained
       
        txtId.selectAll();
        if (!txtId.getText().equals("")) {
            int id = Integer.parseInt(txtId.getText());
            if (id > 0 && !showDate) {
              
            }
            field = "txtId";
        }
    }//GEN-LAST:event_txtIdFocusGained

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
       
        if (!itemCaja.equals("")) {
            cboxCajas.setSelectedItem(itemCaja);
        }
      
    }//GEN-LAST:event_formInternalFrameActivated

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        txtId.selectAll();
        if (!txtId.getText().equals("")) {
            int id = Integer.parseInt(txtId.getText());
            if (id > 0) {
            
            }
        }
    }//GEN-LAST:event_txtIdActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed

    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
  
    }//GEN-LAST:event_formInternalFrameIconified

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
     
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void cboxCajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxCajasActionPerformed
     
    }//GEN-LAST:event_cboxCajasActionPerformed

    private void jXDatePickerFechaAperturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerFechaAperturaActionPerformed
        showDate = true;
    }//GEN-LAST:event_jXDatePickerFechaAperturaActionPerformed

    private void jXDatePickerFechaCierreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerFechaCierreActionPerformed
        showDate = true;
    }//GEN-LAST:event_jXDatePickerFechaCierreActionPerformed

    private void cboxCajasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxCajasFocusGained
       
    }//GEN-LAST:event_cboxCajasFocusGained

    private void cboxCajasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxCajasFocusLost

    }//GEN-LAST:event_cboxCajasFocusLost

    private void txtMontoRetiradoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoRetiradoKeyReleased
        String monto = txtMontoRetirado.getText().replace(".", "").replace(",", ".");
        if (monto.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {
        }
    }//GEN-LAST:event_txtMontoRetiradoKeyReleased

    private void txtMontoRetiradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoRetiradoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoRetiradoActionPerformed

    private void txtMontoRetiradoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMontoRetiradoFocusGained

        txtMontoRetirado.selectAll();
    }//GEN-LAST:event_txtMontoRetiradoFocusGained

    private void txtMontoFinalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoFinalKeyReleased
        String monto = txtMontoFinal.getText().replace(".", "").replace(",", ".");
        if (monto.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {

        }
    }//GEN-LAST:event_txtMontoFinalKeyReleased

    private void txtMontoFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoFinalActionPerformed

    private void txtMontoFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMontoFinalFocusGained

        txtMontoFinal.selectAll();
    }//GEN-LAST:event_txtMontoFinalFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Ultimo;
    private javax.swing.JComboBox<String> cboxCajas;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblCaja;
    private javax.swing.JLabel lblCobros;
    private javax.swing.JLabel lblDiferencia;
    private javax.swing.JLabel lblFechaApertura;
    private javax.swing.JLabel lblFechaCierre;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JLabel lblMontoFinal;
    private javax.swing.JLabel lblMontoInicial;
    private javax.swing.JLabel lblMontoRetirado;
    private javax.swing.JLabel lblPagos;
    private javax.swing.JLabel lblQuedaCaja;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalCompras;
    private javax.swing.JLabel lblTotalVentas;
    private javax.swing.JTextField txtDiferencia;
    private javax.swing.JTextField txtId;
    private javax.swing.JFormattedTextField txtMontoFinal;
    private javax.swing.JTextField txtMontoInicial;
    private javax.swing.JFormattedTextField txtMontoRetirado;
    private javax.swing.JTextField txtQuedaCaja;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalCobros;
    private javax.swing.JTextField txtTotalCompras;
    private javax.swing.JTextField txtTotalPagos;
    private javax.swing.JTextField txtTotalVentas;
    // End of variables declaration//GEN-END:variables
 

    @Override
    public int imGuardar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imBorrar(String crud) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imFiltrar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imInsFilas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imDelFilas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imNuevo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imBuscar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imPrimero() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imSiguiente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imAnterior() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imUltimo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int imImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
