
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Filtros.DescriptionFilter;
import java.awt.event.KeyAdapter;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
import Modelo.cargaComboBox;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.*;

public class Form_Depositos extends javax.swing.JInternalFrame implements InterfaceUsuario {
    private DBTableController tc;
    private Map<String, String> mapData;
    private int idDepositos;
    
    public Form_Depositos() {
        initComponents();
        Iddeposito.setText("0"); 
        Telefono.setText("0"); 
        tc = new DBTableController();
        tc.iniciar("DEPOSITOS");
        mapData = new HashMap<>();
        idDepositos = -1;
        initializeTextFields();
        actualizarUltimoId(); // Actualizar el JLabel con el último ID al abrir el formulario
        cargaComboBox.pv_cargar(Sucursal, "SUCURSALES", "id, sucursal", "id", "");
    }

  private void setMapData() {
        mapData.clear();
        String id = Iddeposito.getText().trim();
        if (!id.isEmpty() && !id.equals("0")) {
            mapData.put("id", id);
        }

        String nombre = deposito.getText().trim();
        if (!nombre.isEmpty()) {
            mapData.put("deposito", nombre);
        }

        String descripcion = textdireccion.getText().trim();
        // Only add description if it's not empty
        if (!descripcion.isEmpty()) {
            mapData.put("direccion", descripcion);  // Ensure correct handling
            
        }
                String ContactoSuc = Contacto.getText().trim();
        // Only add description if it's not empty
        if (!ContactoSuc.isEmpty()) {
            mapData.put("contacto", ContactoSuc);  // Ensure correct handling
            
        }
        String TelefonoSuc = Telefono.getText().trim();
        // Only add description if it's not empty
        if (!TelefonoSuc.isEmpty()) {
            mapData.put("telefono", TelefonoSuc);  // Ensure correct handling
            
        }
    String seleccionada = (String) Sucursal.getSelectedItem();
    if (seleccionada != null && !seleccionada.equals("0-Seleccionar")) {
        int sucursalId = Integer.parseInt(seleccionada.split("-")[0].trim());
        mapData.put("sucursal_id", String.valueOf(sucursalId));
    }
}
   
    private void resetData() {
         mapData.clear();
        Iddeposito.setText("0");
        deposito.setText("");
        textdireccion.setText("");
        Telefono.setText("0");
        Contacto.setText("");
        Sucursal.setSelectedIndex(0);
        mapData.clear();
    }

private void fillView(Map<String, String> data) {
    Iddeposito.setText(data.getOrDefault("id", ""));
    deposito.setText(data.getOrDefault("deposito", ""));
    textdireccion.setText(data.getOrDefault("direccion", ""));
    Telefono.setText(data.getOrDefault("telefono", ""));
    Contacto.setText(data.getOrDefault("contacto", ""));

    String sucursalId = data.getOrDefault("sucursal_id", "0");
    if (!sucursalId.equals("0")) {
        for (int i = 0; i < Sucursal.getItemCount(); i++) {
            String item = (String) Sucursal.getItemAt(i);
            if (item.startsWith(sucursalId + "-")) {
                Sucursal.setSelectedIndex(i);
                break;
            }
        }
    } else {
        Sucursal.setSelectedIndex(0);
    }
}



 private void initializeTextFields() {
        applyNumericFilter(Iddeposito);
        applyAlphaFilter(deposito);
         applyDescriptionFilter(Contacto);
         applyNumericFilter(Telefono);
        applyDescriptionFilter(textdireccion);
        addFocusListeners();
        addEnterKeyListenerToIdField();
    }

    // Ensure correct filters and listeners are applied
    private void applyDescriptionFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DescriptionFilter());
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }

    private void addFocusListeners() {
Iddeposito.addFocusListener(new DefaultFocusListener(Iddeposito, true)); // Aún rellenará con "0"
deposito.addFocusListener(new DefaultFocusListener(deposito, false)); // Aún rellenará con "0"
Contacto.addFocusListener(new DefaultFocusListener(Contacto, false)); // Aún rellenará con "0"
Telefono.addFocusListener(new DefaultFocusListener(Telefono, true)); // No rellenará con "0"
textdireccion.addFocusListener(new DefaultFocusListener(textdireccion, false)); // No rellenará con "0"

    }

    private void actualizarUltimoId() {
        try {
            int ultimoId = tc.getMaxId();
            UltimoId.setText(String.valueOf(ultimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


private void addEnterKeyListenerToIdField() {
    Iddeposito.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                buscarYRellenarDatos();  // Llama al método que gestiona la búsqueda y el relleno.   
            }
        }
    });
}


    private int obtenerIdDepositoActual() {
        try {
            return Integer.parseInt(Iddeposito.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar el ID: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
   
private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay más registros en esta dirección.");
        resetData(); // Limpia los campos si no hay más registros
        return -1; // Indica que no hay más registros.
    } else {
        try {
              resetData();  
            // Actualizar el ID del depósito actual para la navegación.
            idDepositos = Integer.parseInt(registro.getOrDefault("id", "-1"));

            // Actualizar la interfaz de usuario con los datos del registro.
            actualizarUIConDatosDeposito(registro);

            return 1; // Éxito
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resetData(); // Limpia los campos en caso de error
            return -1; // Error en la conversión del ID.
        }
    }
}

private void actualizarUIConDatosDeposito(Map<String, String> datosDeposito) {
    Iddeposito.setText(datosDeposito.getOrDefault("id", ""));
    deposito.setText(datosDeposito.getOrDefault("deposito", ""));
    textdireccion.setText(datosDeposito.getOrDefault("direccion", ""));
    Contacto.setText(datosDeposito.getOrDefault("contacto", ""));
    Telefono.setText(datosDeposito.getOrDefault("telefono", ""));

    // Seleccionar la sucursal en el JComboBox
    int sucursalId = Integer.parseInt(datosDeposito.getOrDefault("sucursal_id", "0"));

    // Luego, selecciona la sucursal correspondiente
    for (int i = 0; i < Sucursal.getItemCount(); i++) {
        String item = (String) Sucursal.getItemAt(i);
        String[] itemData = item.split("-");
        int itemId = Integer.parseInt(itemData[0].trim());

        if (itemId == sucursalId) {
            Sucursal.setSelectedIndex(i);
            break;
        }
    }
}
private void buscarYRellenarDatos() {
    String id = Iddeposito.getText().trim();
    if (!id.isEmpty()) {
        resetData();
        try {
            int idDep = Integer.parseInt(id);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("DEPOSITOS", "id", idDep);
            idDepositos = idDep;
            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                // Llenar la vista con los datos obtenidos, manejando null para sucursal_id
                fillViewWithNullCheck(registro);
                System.out.println("Registro encontrado: " + registro);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un deposito con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            resetData();
        }
    } else {
        JOptionPane.showMessageDialog(this, "Ingrese un ID para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        resetData();
    }
}

private void fillViewWithNullCheck(Map<String, String> data) {
    Iddeposito.setText(data.getOrDefault("id", ""));
    deposito.setText(data.getOrDefault("deposito", ""));
    textdireccion.setText(data.getOrDefault("direccion", ""));
    Telefono.setText(data.getOrDefault("telefono", ""));
    Contacto.setText(data.getOrDefault("contacto", ""));

    String sucursalId = data.get("sucursal_id");
    if (sucursalId != null && !sucursalId.equals("0")) {
        for (int i = 0; i < Sucursal.getItemCount(); i++) {
            String item = (String) Sucursal.getItemAt(i);
            if (item.startsWith(sucursalId + "-")) {
                Sucursal.setSelectedIndex(i);
                break;
            }
        }
    } else {
        Sucursal.setSelectedIndex(0);
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

        jPanel1 = new javax.swing.JPanel();
        Iddeposito = new javax.swing.JTextField();
        deposito = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_deposito = new javax.swing.JLabel();
        lbl_direccion = new javax.swing.JLabel();
        lblultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();
        textdireccion = new javax.swing.JTextField();
        lbl_contacto = new javax.swing.JLabel();
        Contacto = new javax.swing.JTextField();
        lbl_telefono = new javax.swing.JLabel();
        Telefono = new javax.swing.JTextField();
        lbl_Sucursal = new javax.swing.JLabel();
        Sucursal = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Depósito");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_id.setText("Id");

        lbl_deposito.setText("Depósito");

        lbl_direccion.setBackground(new java.awt.Color(102, 102, 102));
        lbl_direccion.setForeground(new java.awt.Color(102, 102, 102));
        lbl_direccion.setText("Dirección");

        lblultimo.setText("Último");

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setOpaque(true);

        lbl_contacto.setBackground(new java.awt.Color(102, 102, 102));
        lbl_contacto.setForeground(new java.awt.Color(102, 102, 102));
        lbl_contacto.setText("Contacto");

        lbl_telefono.setBackground(new java.awt.Color(102, 102, 102));
        lbl_telefono.setForeground(new java.awt.Color(102, 102, 102));
        lbl_telefono.setText(" Teléfono");

        lbl_Sucursal.setText("Sucursal");

        Sucursal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_direccion)
                    .addComponent(lbl_deposito, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_id, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Iddeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblultimo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(deposito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_Sucursal))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_contacto)
                            .addComponent(lbl_telefono, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Contacto)
                    .addComponent(Telefono)
                    .addComponent(Sucursal, 0, 136, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Iddeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_id)
                                .addComponent(lblultimo)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_deposito)
                            .addComponent(lbl_contacto)
                            .addComponent(Contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_direccion)
                            .addComponent(textdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_telefono)
                            .addComponent(Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_Sucursal)
                        .addComponent(Sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Contacto;
    private javax.swing.JTextField Iddeposito;
    private javax.swing.JComboBox<String> Sucursal;
    private javax.swing.JTextField Telefono;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JTextField deposito;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_Sucursal;
    private javax.swing.JLabel lbl_contacto;
    private javax.swing.JLabel lbl_deposito;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_telefono;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JTextField textdireccion;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String id = Iddeposito.getText().trim();
    String nombre = deposito.getText().trim();
    String descripcion = textdireccion.getText().trim();
     String Contactosuc = Contacto.getText().trim();
      String Telefonosuc = Telefono.getText().trim();
      int sucursalIndex = Sucursal.getSelectedIndex();

    if (id.isEmpty() || Integer.parseInt(id) <= 0 || nombre.isEmpty() ) {
        JOptionPane.showMessageDialog(null, "Los campos Id y Sucursal son obligatorios y deben ser válidos.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
        
    }

    // Preparar los datos para la inserción o actualización
    setMapData();
    boolean isUpdate = DepositoExiste(id);
    int rowsAffected = 0;

    if (isUpdate) {
        Map<String, String> existingData = tc.searchById(mapData); // Asume que devuelve los datos actuales
        if (existingData != null && existingData.equals(mapData)) {
            JOptionPane.showMessageDialog(null, "No hay cambios para guardar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            resetData();  // Restablecer los campos
            fillView(mapData);
            actualizarUltimoId();  // Actualizar el último ID
            return 0;  // No se realizan cambios si los datos son iguales
        } else {
            ArrayList<Map<String, String>> updateList = new ArrayList<>();
            updateList.add(mapData);
            rowsAffected = tc.updateReg(updateList);
        }
    } else {
        rowsAffected = tc.createReg(mapData);
    }

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(null, isUpdate ? "Sucursal actualizado correctamente." : "Sucursal creado correctamente.",
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No se pudo realizar la operación solicitada.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData();
    fillView(mapData);
    actualizarUltimoId();
    Iddeposito.requestFocusInWindow();

    return rowsAffected;
}


/**
 * Comprueba si existe un deposito con el ID especificado
 * @param id El ID de la deposito a verificar
 * @return true si existe, false si no
 */
private boolean DepositoExiste(String id) {
    if (id == null || id.trim().isEmpty()) {
        System.out.println("El ID proporcionado es nulo o está vacío.");
        return false;
    }

    // Prepara el mapa con el ID para buscar en la base de datos
    Map<String, String> params = new HashMap<>();
    params.put("id", id.trim());  // Asegúrate de que el ID está correctamente ajustado (sin espacios innecesarios)
    System.out.println("Buscando Depósito con ID: " + id);

    // Usa el método searchById para buscar en la tabla 
    Map<String, String> DepositoData = tc.searchById(params);  // Asumiendo que tc es tu controlador de base de datos para la tabla 

    if (DepositoData != null && !DepositoData.isEmpty()) {
        System.out.println("Se encontró un depósito con el ID: " + id + " -> " +DepositoData);
        return true;
    } else {
        System.out.println("No se encontró ningun depósito con el ID: " + id);
        return false;
    }
}

@Override
public int imBorrar(String crud) {
    // Verificar que los campos no estén vacíos y que una sucursal esté seleccionada
    if (Iddeposito.getText().trim().isEmpty() || deposito.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    // Obtener el ID del depósito actual
    int idDepositoActual = obtenerIdDepositoActual();
    if (idDepositoActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Confirmar la eliminación
    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este depósito?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    // Crear la lista de registros para borrar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idDepositoActual));
    registrosParaBorrar.add(registro);

    // Realizar la eliminación
    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Depósito eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar el depósito.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Reiniciar los datos y actualizar la vista
    resetData();
    fillView(mapData);
    actualizarUltimoId();
    return resultado;
}


    @Override
  public int imNuevo() {
        resetData();
        fillView(mapData);
        actualizarUltimoId(); 
        idDepositos = -1;
        return 0;
    }

@Override
public int imBuscar() {
    buscarYRellenarDatos();
    return 0; // Retorna 0 para indicar que el formulario se cerró sin errores
}


    @Override
    public int imFiltrar() {
Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    // Lista de columnas para mostrar en el formulario de búsqueda 
    List<String> columnas = Arrays.asList("id", "deposito");
    Form_Buscar buscador = new Form_Buscar(parentFrame, true, tc, "DEPOSITOS", columnas);

    buscador.setOnItemSeleccionadoListener(this);
    // Muestra el formulario de búsqueda
    buscador.setVisible(true);

    // Este código no maneja el resultado de la búsqueda directamente
    // Se asume que la lógica de manejo del ítem seleccionado se procesa en el método onItemSeleccionado
   resetData();
    return 0; // Retorna 0 para indicar que el formulario se cerró sin errores
    }

@Override
public int imPrimero() {
    Map<String, String> registro = tc.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imSiguiente() {
    if (idDepositos == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idDepositos), "NEXT");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imAnterior() {
    if (idDepositos <= 1) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idDepositos), "PRIOR");
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
                job.setJobName("Depósito");
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
    System.out.println("Datos seleccionados originales: " + datosSeleccionados);

    // Extraer 'Codigo' y 'Descripcion' del mapa de datos seleccionados
    String idStr = datosSeleccionados.get("Codigo"); // 'Codigo' se usa como clave para el ID
    String nombreDeposito = datosSeleccionados.get("Descripcion"); // 'Descripcion' se usa como clave para el nombre 

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idDeposito = Integer.parseInt(idStr); // Convertir el ID a entero
            // Buscar detalles adicionales si es necesario
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("DEPOSITOS", "id", idDeposito);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                // Suponiendo que 'registro' contenga claves adicionales si necesarias, ej. 'descripcion'
                final String direccionDeposito = registro.getOrDefault("direccion", ""); // Recuperar si está disponible
                final String sucursal = registro.getOrDefault("sucursal_id", "");  
                final String telefonoD = registro.getOrDefault("telefono", "");
                final String contatoD = registro.getOrDefault("contacto", "");
                // Actualizar la UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    Iddeposito.setText(idStr);
                          idDepositos = idDeposito; // Guardar el ID actual
                    deposito.setText(nombreDeposito);
                    Telefono.setText(telefonoD);
                    Contacto.setText(contatoD);
                    textdireccion.setText(direccionDeposito); // Actualizar campo de descripción
                    if (!sucursal.isEmpty()) {
                        Sucursal.setSelectedIndex(Integer.parseInt(sucursal) - 1);
                    }
          
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un depósito con el Código especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData(); // Limpia los campos si no se encuentra un registro
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El Código debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            resetData(); // Limpia los campos en caso de error
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            resetData(); // Limpia los campos en caso de error
        }
    } else {
        JOptionPane.showMessageDialog(null, "Ingrese un Código para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        resetData(); // Limpia los campos si no se ingresa un ID
    }
}

}
