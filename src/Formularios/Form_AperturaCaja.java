
package Formularios;

import Controllers.DBConexion;
import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.Functions;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Modelo.cargaComboBox;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import javax.swing.*;

public class Form_AperturaCaja extends javax.swing.JInternalFrame implements InterfaceUsuario {

    private final Map<String, String> myData, cajas;


    /**
     * Creates new form AperturaCajaView
     *
     * @param permisos
     */
    public Form_AperturaCaja() {
        initComponents();
                cargaComboBox.pv_cargar(cboxCajas, "CAJAS", "id,nombre", "id", "");
        myData = new HashMap<>();
        cajas = new HashMap<>();
        PlainDocument doc = (PlainDocument) txtId.getDocument();
        txtMontoActual.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldState();
            }
        });

        txtMonto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldState();
            }
        });
    }

    private void updateFieldState() {
        String montoActual = txtMontoActual.getText().replace(".", "").replace(",", ".");
        String monto = txtMonto.getText().replace(".", "").replace(",", ".");
        if (monto.equals("")) {
            txtSaldoFinal.setText(montoActual);
        } else if (montoActual.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?") && monto.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {
            double saldoActual = Double.parseDouble(montoActual) + Double.parseDouble(monto);
         //   txtSaldoFinal.setText((Utils.decimalFormat(saldoActual)));
        }
    }
        private void addFocusListeners() {
        txtId.addFocusListener(new DefaultFocusListener(txtId, true));
 
    }

    private void cargarCboxCajas(String where) {
        cajas.clear();
        cboxCajas.removeAllItems();
     
        cboxCajas.setSelectedIndex(-1);
        if (cajas.isEmpty()) {
            cboxCajas.removeAllItems();

        }
    }

    private boolean setData() {
       String isValid = txtId.getText();
        if (isValid== null) {
            myData.put("id", txtId.getText());
        } else {
            txtId.requestFocus();
            return false;
        }



        String montoApertura = txtMonto.getText().replace(".", "").replace(",", ".");
     
        int saldoFinal = Integer.parseInt(txtSaldoFinal.getText().replace(".", "").replace(",", "."));
        myData.put("montoInicial", saldoFinal + "");

        return true;
    }

    private void resetData() {
        myData.put("id", "0");
        myData.put("idCaja", "0");
        myData.put("fechaApertura", "");
        myData.put("montoApertura", "0");
        myData.put("montoInicial", "0");
    }

    private void fillView(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "id":
                    txtId.setText(value);
                    break;
                case "idCaja":
                    cargarCboxCajas("");
                    for (Map.Entry<String, String> valor : cajas.entrySet()) {
                        if (valor.getKey().equals(value)) {
                            cboxCajas.setSelectedItem(valor.getValue());
                        }
                    }
                    break;
                case "fechaApertura":
                    break;
                case "montoApertura":
                    break;
            }
        }
    }

    private void resetCampos() {
        txtId.setText("0");
        cargarCboxCajas("estado = 1");
        cboxCajas.setSelectedIndex(-1);
        txtMontoActual.setText("0");
        txtMonto.setText("0");
        txtSaldoFinal.setText("0");
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
        txtId = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        lblMontoApertura = new javax.swing.JLabel();
        lblCaja = new javax.swing.JLabel();
        cboxCajas = new javax.swing.JComboBox<>();
        lblSaldoActual = new javax.swing.JLabel();
        txtMontoActual = new javax.swing.JTextField();
        lblSaldoFinal = new javax.swing.JLabel();
        txtSaldoFinal = new javax.swing.JTextField();
        txtMonto = new javax.swing.JFormattedTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        lblId1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Apertura Caja");
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

        lblFecha.setText("Fecha");

        lblMontoApertura.setText("Monto Inicio");

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

        lblSaldoActual.setText("Monto Sistema");

        txtMontoActual.setEditable(false);
        txtMontoActual.setBackground(new java.awt.Color(204, 204, 255));
        txtMontoActual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMontoActual.setText("0");
        txtMontoActual.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtMontoActual.setFocusable(false);
        txtMontoActual.setOpaque(true);

        lblSaldoFinal.setText("Monto Final");

        txtSaldoFinal.setEditable(false);
        txtSaldoFinal.setBackground(new java.awt.Color(204, 204, 255));
        txtSaldoFinal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSaldoFinal.setText("0");
        txtSaldoFinal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtSaldoFinal.setFocusable(false);
        txtSaldoFinal.setOpaque(true);

        txtMonto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.##"))));
        txtMonto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMonto.setText("0");
        txtMonto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMontoFocusGained(evt);
            }
        });
        txtMonto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoKeyReleased(evt);
            }
        });

        lblId1.setText("Último");

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("3");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setOpaque(true);

        jCheckBox1.setText("Cerrar");
        jCheckBox1.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCaja)
                            .addComponent(lblFecha))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblId)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblId1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboxCajas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblSaldoFinal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSaldoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblSaldoActual)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMontoActual, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblMontoApertura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addGap(219, 219, 219))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblId)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSaldoActual)
                        .addComponent(txtMontoActual, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblId1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCaja)
                            .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMontoApertura))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboxCajas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFecha)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSaldoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSaldoFinal)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdFocusGained

    }//GEN-LAST:event_txtIdFocusGained

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
      
    }//GEN-LAST:event_formInternalFrameActivated

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed

    }//GEN-LAST:event_txtIdActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed

    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
  
    }//GEN-LAST:event_formInternalFrameIconified

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified

    }//GEN-LAST:event_formInternalFrameDeiconified

    private void cboxCajasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxCajasFocusGained
    
    }//GEN-LAST:event_cboxCajasFocusGained

    private void cboxCajasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboxCajasFocusLost

    }//GEN-LAST:event_cboxCajasFocusLost

    private void cboxCajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxCajasActionPerformed

    }//GEN-LAST:event_cboxCajasActionPerformed

    private void txtMontoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoKeyReleased
        String monto = txtMonto.getText().replace(".", "").replace(",", ".");
        if (monto.matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?")) {

        }
    }//GEN-LAST:event_txtMontoKeyReleased

    private void txtMontoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMontoFocusGained

        txtMonto.selectAll();
    }//GEN-LAST:event_txtMontoFocusGained

    private void jXDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerActionPerformed

    }//GEN-LAST:event_jXDatePickerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboxCajas;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCaja;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JLabel lblMontoApertura;
    private javax.swing.JLabel lblSaldoActual;
    private javax.swing.JLabel lblSaldoFinal;
    private javax.swing.JTextField txtId;
    private javax.swing.JFormattedTextField txtMonto;
    private javax.swing.JTextField txtMontoActual;
    private javax.swing.JTextField txtSaldoFinal;
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
