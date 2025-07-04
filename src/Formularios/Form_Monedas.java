/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.*;

public class Form_Monedas extends javax.swing.JInternalFrame implements InterfaceUsuario {

    /**
     * Creates new form Form_Monedas
     */
    private DBTableController tc;           // Controlador de la base de datos para operaciones CRUD.
    private Map<String, String> mapData;    // Mapa para almacenar datos del formulario que se enviarán a la base de datos.
    private int idMoneda;                   // ID actual de la moneda en uso o edición.

    public Form_Monedas() {
    initComponents();
    moneda_id.setText("0"); 
    tc = new DBTableController();
    tc.iniciar("MONEDAS");
    mapData = new HashMap<>();
    idMoneda = -1;
    initializeTextFields();
    actualizarUltimoId();
    
        SwingUtilities.invokeLater(() -> {
        try {
            this.setSelected(true);
            this.setVisible(true);
            this.requestFocusInWindow();
            this.toFront();
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    });
    }

    private void initializeTextFields() {
    // Apply numeric filters to fields that should only accept numbers
    applyNumericFilter(moneda_id);
    applyNumericFilter(moneda_decimales);

    // Apply alpha filters to fields that should accept alphabetical input
    applyAlphaFilter(moneda);
    applyAlphaFilter(moneda_Abreviatura);

    // Add other initialization as needed
    addFocusListeners();
    addEnterKeyListenerToIdField();
}
private void addFocusListeners() {
    moneda_id.addFocusListener(new DefaultFocusListener(moneda_id,true));
    moneda.addFocusListener(new DefaultFocusListener(moneda,false));
    moneda_Abreviatura.addFocusListener(new DefaultFocusListener(moneda_Abreviatura,false));
    moneda_decimales.addFocusListener(new DefaultFocusListener(moneda_decimales,true));
}
    
private void addEnterKeyListenerToIdField() {
    moneda_id.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int id = obtenerIdMonedaActual();  // This method should return the current ID from moneda_id field
                if (id != -1) {
                    List<Map<String, String>> resultado = tc.buscarPorIdGenerico("MONEDAS", "id", id);
                    idMoneda=id;
                    if (!resultado.isEmpty()) {
                        Map<String, String> datosMoneda = resultado.get(0); // Assuming the search by ID returns a single result
                        fillView(datosMoneda);  // This method should update the UI with the data of the moneda
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró la moneda con ID: " + id);
                        resetData(); // Resets the fields
                        fillView(mapData); // Updates the view with the initial or default data
                    }
                }
            }
        }
    });
}

    
    
private int obtenerIdMonedaActual() {
    try {
        // Attempt to parse the ID from the moneda_id text field.
        return Integer.parseInt(moneda_id.getText().trim());
    } catch (NumberFormatException e) {
        // Show an error dialog if the text in moneda_id isn't a valid integer.
        JOptionPane.showMessageDialog(this, "ID inválido para la moneda.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;  // Return -1 to indicate the failure in parsing the ID.
    }
}
private void SetMapData() {
    // It's important to trim the inputs to remove any leading or trailing whitespace.
    mapData.put("id", moneda_id.getText().trim());
    mapData.put("moneda", moneda.getText().trim());
    mapData.put("abreviatura", moneda_Abreviatura.getText().trim());
    mapData.put("decimales", moneda_decimales.getText().trim());
    mapData.put("activo", String.valueOf(Activo.isSelected()));
    mapData.put("cotizacion", String.valueOf(Cotizacion.isSelected()));
}

 private void resetData() {
    mapData.put("id", "0");
    mapData.put("moneda", "");
    mapData.put("abreviatura", "");
    mapData.put("decimales", "0");  
    Activo.setSelected(false);
    Cotizacion.setSelected(false);
}

private void fillView(Map<String, String> data) {
    // Set the text of each field using data from the map, with default values if no data exists.
    moneda_id.setText(data.getOrDefault("id", ""));
    moneda.setText(data.getOrDefault("moneda", ""));  // Assuming the key is "moneda" for the currency name
    moneda_Abreviatura.setText(data.getOrDefault("abreviatura", ""));
    moneda_decimales.setText(data.getOrDefault("decimales", ""));
    Activo.setSelected("1".equals(data.getOrDefault("activo", "0")));
    Cotizacion.setSelected("1".equals(data.getOrDefault("cotizacion", "0")));
}

private void actualizarUltimoId() {
    try {
        // Call the getMaxId method which should be contextually set to fetch from the 'MONEDAS' table
        int ultimoId = tc.getMaxId(); 
        // Update the UI component that is supposed to display the last ID. Make sure 'ultima_moneda' is the correct JLabel.
        ultima_moneda.setText(String.valueOf(ultimoId));
    } catch (Exception e) {
        // Handle any exceptions by showing an error message dialog.
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID de la moneda. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private int mostrarDialogoConfirmacion(String mensaje) {
    return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
}

private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;  // No records to process
    } else {
        resetData();  
        // Extracting data from the map and updating the UI accordingly
        int newIdMoneda = Integer.parseInt(registro.get("id"));
        String nombreMoneda = registro.get("moneda");
        String abreviaturaMoneda = registro.get("abreviatura");
        String decimalesMoneda = registro.get("decimales");

        actualizarUIConRegistro(newIdMoneda, nombreMoneda, abreviaturaMoneda, decimalesMoneda);
        idMoneda = newIdMoneda; // Update the global idMoneda to reflect the current record's ID
        return 1;  // Indicate successful processing
    }
}


private void actualizarUIConRegistro(int idMoneda, String nombreMoneda, String abreviaturaMoneda, String decimalesMoneda) {
    // Update the internal state with the new currency ID
    this.idMoneda = idMoneda;

    // Update the form's text fields with new data
    moneda_id.setText(String.valueOf(idMoneda));
    moneda.setText(nombreMoneda);
    moneda_Abreviatura.setText(abreviaturaMoneda);
    moneda_decimales.setText(decimalesMoneda);
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
        UltimoId = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        moneda_id = new javax.swing.JTextField();
        moneda = new javax.swing.JTextField();
        moneda_Abreviatura = new javax.swing.JTextField();
        moneda_decimales = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ultima_moneda = new javax.swing.JLabel();
        Activo = new javax.swing.JCheckBox();
        Cotizacion = new javax.swing.JCheckBox();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        UltimoId.setBackground(new java.awt.Color(153, 153, 153));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setOpaque(true);

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Moneda");
        setVisible(true);

        jLabel1.setText("Id");

        jLabel2.setText("Moneda");

        jLabel3.setText("Abreviatura");

        jLabel4.setText("Decimales");

        moneda_id.setText("0");
        moneda_id.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                moneda_idFocusGained(evt);
            }
        });
        moneda_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moneda_idActionPerformed(evt);
            }
        });

        moneda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monedaFocusGained(evt);
            }
        });

        moneda_Abreviatura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                moneda_AbreviaturaFocusGained(evt);
            }
        });

        moneda_decimales.setText("0");
        moneda_decimales.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                moneda_decimalesFocusGained(evt);
            }
        });
        moneda_decimales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moneda_decimalesActionPerformed(evt);
            }
        });

        jLabel6.setText("Ultimo");

        ultima_moneda.setBackground(new java.awt.Color(204, 204, 255));
        ultima_moneda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ultima_moneda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ultima_moneda.setOpaque(true);

        Activo.setText("Activo");

        Cotizacion.setText("Req. Cotización");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(moneda)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(moneda_id, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel6))
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ultima_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(moneda_Abreviatura)
                            .addComponent(moneda_decimales)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Activo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Cotizacion)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(moneda_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6))
                    .addComponent(ultima_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(moneda_Abreviatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(moneda_decimales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Activo)
                    .addComponent(Cotizacion))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moneda_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_idActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_moneda_idActionPerformed

    private void moneda_decimalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_decimalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moneda_decimalesActionPerformed

    private void moneda_idFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_moneda_idFocusGained
    // Verifica si el campo está vacío antes de establecer un texto predeterminado.
    if (moneda_id.getText().isEmpty()) {
        moneda_id.setText("Ingrese ID de la moneda"); // Solo establece texto si está vacío.
    }
    }//GEN-LAST:event_moneda_idFocusGained

    private void monedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monedaFocusGained
 
    }//GEN-LAST:event_monedaFocusGained

    private void moneda_AbreviaturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_moneda_AbreviaturaFocusGained

    }//GEN-LAST:event_moneda_AbreviaturaFocusGained

    private void moneda_decimalesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_moneda_decimalesFocusGained
           // Verifica si el campo está vacío antes de establecer un texto predeterminado.
    if (moneda_decimales.getText().isEmpty()) {
        moneda_decimales.setText("Ingrese cantidad de decimales"); // Solo establece texto si está vacío.
    }
    }//GEN-LAST:event_moneda_decimalesFocusGained

public static void focusGained(JTextField focus, String From_Buscar) {
    focus.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            // Se establece el texto solo si el campo está vacío.
            if (focus.getText().isEmpty()) {
                focus.setText(From_Buscar);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            // Si el texto del campo es igual al placeholder, se limpia al perder el foco.
            if (focus.getText().equals(From_Buscar)) {
                focus.setText("");
            }
        }
    });
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JCheckBox Cotizacion;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField moneda;
    private javax.swing.JTextField moneda_Abreviatura;
    private javax.swing.JTextField moneda_decimales;
    private javax.swing.JTextField moneda_id;
    private javax.swing.JLabel ultima_moneda;
    // End of variables declaration//GEN-END:variables

 


@Override
public int imGuardar(String crud) {
    int rows = 0;
    String msg = "No se especificó la operación"; // Mensaje por defecto en caso de que no entre en ninguna condición

    SetMapData();

    // Validación inicial para garantizar que los campos necesarios están llenos
    if (moneda_id.getText().trim().isEmpty() || moneda.getText().trim().isEmpty() ||
        moneda_Abreviatura.getText().trim().isEmpty() || moneda_decimales.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos deben estar llenos y los decimales no deben ser cero.", "Error", JOptionPane.ERROR_MESSAGE);
        resetData();  
        actualizarUltimoId();
        return -1;  // Retorno anticipado por error de validación
    }

    // Verificar si el registro ya existe y si es necesario actualizar
    if (tc.existAny(mapData) > 0) {
        Map<String, String> existingData = tc.searchById(mapData);
        if (existingData != null && existingData.equals(mapData)) {
            msg = "No hay cambios para guardar.";
            JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
            resetData();  // Restablecer los campos
            actualizarUltimoId();  // Actualizar el último ID
            return 0;
        } else {
            ArrayList<Map<String, String>> listaParaActualizar = new ArrayList<>();
            listaParaActualizar.add(mapData);
            rows = tc.updateReg(listaParaActualizar);
            msg = (rows > 0) ? "La moneda ha sido actualizada con éxito." : "No se pudo actualizar la moneda.";
        }
    } else {
        rows = tc.createReg(mapData);
        msg = (rows > 0) ? "La moneda ha sido creada con éxito." : "No se pudo crear la moneda.";
    }

    resetData();  // Restablecer los campos siempre al final del método
    fillView(mapData);
    actualizarUltimoId();  // Actualizar el último ID siempre al final del método

    JOptionPane.showMessageDialog(this, msg, (rows > 0) ? "Éxito" : "Error", JOptionPane.INFORMATION_MESSAGE);
    return rows;
}



   public int imBorrar(String crud) {
    // Verify that all required fields are filled
    if (moneda_id.getText().trim().isEmpty() || moneda.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    int idMonedaActual = obtenerIdMonedaActual(); // Get the current ID from the form
    if (idMonedaActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta moneda?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idMonedaActual));
    registrosParaBorrar.add(registro);

    int resultado = tc.deleteReg(registrosParaBorrar); // Delete the record using the controller
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Moneda eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar la moneda.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData(); // Reset the fields
    fillView(mapData); // Update the view to reflect any changes
    actualizarUltimoId(); // Refresh the last ID displayed
    return resultado;
}


    @Override
public int imNuevo() {
    // Resetear los datos: Clears all form fields or resets them to default values.
    resetData();

    // Actualizar la interfaz de usuario con los datos reseteados: Updates the UI to reflect the cleared or default state data.
    fillView(mapData);

    // Actualiza el último ID: Refreshes the display of the last ID, ensuring the UI is up-to-date.
    actualizarUltimoId();
 
    idMoneda = -1;
    // Indica éxito: Indicates that the method has successfully completed.
    return 0;
}

   @Override
public int imBuscar() {
 int id = obtenerIdMonedaActual();  // This method should return the current ID from moneda_id field
                if (id != -1) {
                    List<Map<String, String>> resultado = tc.buscarPorIdGenerico("MONEDAS", "id", id);
                    idMoneda=id;
                    if (!resultado.isEmpty()) {
                        Map<String, String> datosMoneda = resultado.get(0); // Assuming the search by ID returns a single result
                        fillView(datosMoneda);  // This method should update the UI with the data of the moneda
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró la moneda con ID: " + id);
                        resetData(); // Resets the fields
                        fillView(mapData); // Updates the view with the initial or default data
                    }
                }
    return 0; 
}

    @Override
    public int imFiltrar() {

    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

    List<String> columnasParaMonedas = Arrays.asList("id", "moneda", "abreviatura", "decimales");

    Form_Buscar buscadorMonedas = new Form_Buscar(parentFrame, true, tc, "monedas", columnasParaMonedas);

    buscadorMonedas.setOnItemSeleccionadoListener(this);

    // Display the search form
    buscadorMonedas.setVisible(true);

    return 0; 
    }

@Override
public int imPrimero() {
    Map<String, String> registro = tc.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}

@Override
public int imSiguiente() {
    // Check if no current record has been selected or the ID is not set
    if (idMoneda == -1) {
        JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún registro inicial.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1; // Return -1 to indicate failure to navigate
    }
    // Request the next record from the current moneda ID
    Map<String, String> registro = tc.navegationReg(String.valueOf(idMoneda), "NEXT");
    // Process the retrieved record and update the UI accordingly
    return procesarRegistroNavegacion(registro);
}


@Override
public int imAnterior() {
    // Check if the current record's ID is valid and more than 1 to ensure there's a possible previous record.
    if (idMoneda <= 1) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1; // Return -1 indicating no previous records are available.
    }
    
    // Fetch the previous record based on the current record's ID.
    Map<String, String> registro = tc.navegationReg(String.valueOf(idMoneda), "PRIOR");
    // Process the fetched record and update the UI accordingly.
    return procesarRegistroNavegacion(registro);
}


@Override
public int imUltimo() {
    // Request the last record from the data controller
    Map<String, String> registro = tc.navegationReg(null, "LAST");
    // Process the record and update the UI
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
                job.setJobName("Monedas");
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

public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    System.out.println("Datos seleccionados originales: " + datosSeleccionados);

    String id = datosSeleccionados.get("Codigo");
    String nombreMoneda = datosSeleccionados.get("Descripcion");

    // Realiza una consulta a la base de datos para obtener todos los detalles de la moneda seleccionada por ID
    if (id != null && !id.isEmpty()) {
        try {
            List<Map<String, String>> resultado = tc.buscarPorIdGenerico("MONEDAS", "id", Integer.parseInt(id));
            if (!resultado.isEmpty()) {
                Map<String, String> datosMoneda = resultado.get(0);
                final String abreviatura = datosMoneda.get("abreviatura");
                final String decimales = datosMoneda.get("decimales");
                final String activoStr = datosMoneda.get("activo");
                final boolean activo = "1".equals(activoStr);
                final String cotizacionStr = datosMoneda.get("cotizacion");
                final boolean cotizacion = "1".equals(activoStr);
                
                SwingUtilities.invokeLater(() -> {
                    moneda_id.setText(id);
                    moneda.setText(nombreMoneda);
                    moneda_Abreviatura.setText(abreviatura != null ? abreviatura : "");
                    moneda_decimales.setText(decimales != null ? decimales : "0");
                    idMoneda = Integer.parseInt(id);
                });
            } else {
                JOptionPane.showMessageDialog(null, "Detalles adicionales no encontrados.", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de moneda inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


   private void applyNumericFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }
}
