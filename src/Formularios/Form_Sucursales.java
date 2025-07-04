
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

public class Form_Sucursales extends javax.swing.JInternalFrame implements InterfaceUsuario {
    private DBTableController tc;
    private Map<String, String> mapData;
    private int idSucursales;
    
    public Form_Sucursales() {
        initComponents();
        Idsucursal.setText("0"); 
        Telefono.setText("0"); 
        tc = new DBTableController();
        tc.iniciar("SUCURSALES");
        mapData = new HashMap<>();
        idSucursales = -1;
        initializeTextFields();
        actualizarUltimoId(); // Actualizar el JLabel con el último ID al abrir el formulario
    }


    private void setMapData() {
        mapData.clear();
        String id = Idsucursal.getText().trim();
        if (!id.isEmpty() && !id.equals("0")) {
            mapData.put("id", id);
        }

        String nombre = sucursal.getText().trim();
        if (!nombre.isEmpty()) {
            mapData.put("sucursal", nombre);
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
    }

    private void resetData() {
         mapData.clear();
        Idsucursal.setText("0");
        sucursal.setText("");
        textdireccion.setText("");
        Telefono.setText("0");
        Contacto.setText("");
        mapData.clear();
    }

private void fillView(Map<String, String> data) {
    Idsucursal.setText(data.getOrDefault("id", ""));
    sucursal.setText(data.getOrDefault("sucursal", ""));
    textdireccion.setText(data.getOrDefault("direccion", ""));
    Telefono.setText(data.getOrDefault("telefono", ""));
    Contacto.setText(data.getOrDefault("contacto", ""));
}


 private void initializeTextFields() {
        applyNumericFilter(Idsucursal);
        applyAlphaFilter(sucursal);
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
Idsucursal.addFocusListener(new DefaultFocusListener(Idsucursal, true)); // Aún rellenará con "0"
sucursal.addFocusListener(new DefaultFocusListener(sucursal, false)); // Aún rellenará con "0"
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
    Idsucursal.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                buscarYRellenarDatos();  // Llama al método que gestiona la búsqueda y el relleno.   
            }
        }
    });
}


    private int obtenerIdActual() {
        try {
            return Integer.parseInt(Idsucursal.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar el ID: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
   
private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()|| registro.equals("0") ) {
        JOptionPane.showMessageDialog(null, "No hay más registros en esta dirección.");
        return -1; // Indica que no hay más registros.
    } else {
        try {
             resetData();  
            // Actualizar el ID del actual para la navegación.
             idSucursales = Integer.parseInt(registro.getOrDefault("id", "-1"));

            // Actualizar la interfaz de usuario con los datos del registro.
            actualizarUIConDatos(registro);

            return 1; // Éxito
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Error en la conversión del ID.
        }
    }
}
private void actualizarUIConDatos(Map<String, String> datos) {
    Idsucursal.setText(datos.getOrDefault("id", ""));
    sucursal.setText(datos.getOrDefault("sucursal", ""));
    textdireccion.setText(datos.getOrDefault("direccion", "") );
    Contacto.setText(datos.getOrDefault("contacto", ""));
    Telefono.setText(datos.getOrDefault("telefono", ""));
  
}

private void buscarYRellenarDatos() {
    String id = Idsucursal.getText().trim();
    if (!id.isEmpty()) {
         resetData();
        try {
            int idSuc = Integer.parseInt(id);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("SUCURSALES", "id", idSuc  );
             idSucursales=idSuc  ;
            if (!registros.isEmpty()) {
                 
                Map<String, String> registro = registros.get(0);
                fillView(registro);
                System.out.println("Registro encontrado: " + registro);; 
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una sucursal con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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

    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Idsucursal = new javax.swing.JTextField();
        sucursal = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_sucursal = new javax.swing.JLabel();
        lbl_direccion = new javax.swing.JLabel();
        lblultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();
        textdireccion = new javax.swing.JTextField();
        lbl_contacto = new javax.swing.JLabel();
        Contacto = new javax.swing.JTextField();
        lbl_telefono = new javax.swing.JLabel();
        Telefono = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Sucursal");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_id.setText("Id");

        lbl_sucursal.setText("Sucursal");

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
                    .addComponent(lbl_sucursal, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_id, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Idsucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_contacto)
                    .addComponent(lbl_telefono, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblultimo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Contacto, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addComponent(Telefono))
                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Idsucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_id)
                        .addComponent(lblultimo)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_sucursal)
                    .addComponent(lbl_contacto)
                    .addComponent(Contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_direccion)
                    .addComponent(textdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_telefono)
                    .addComponent(Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Contacto;
    private javax.swing.JTextField Idsucursal;
    private javax.swing.JTextField Telefono;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_contacto;
    private javax.swing.JLabel lbl_direccion;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_sucursal;
    private javax.swing.JLabel lbl_telefono;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JTextField sucursal;
    private javax.swing.JTextField textdireccion;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String id = Idsucursal.getText().trim();
    String nombre = sucursal.getText().trim();
    String descripcion = textdireccion.getText().trim();
    String Contactosuc = Contacto.getText().trim();
    String Telefonosuc = Telefono.getText().trim();

    if (id.isEmpty() || Integer.parseInt(id) <= 0 || nombre.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Los campos Id y Sucursal son obligatorios y deben ser válidos.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Preparar los datos para la inserción o actualización
    setMapData();
    boolean isUpdate = SucursalExiste(id);
    int rowsAffected = 0;

    if (isUpdate) {
        Map<String, String> existingData = tc.searchById(mapData); // Asume que devuelve los datos actuales
        if (existingData != null && !hasChanges(existingData, mapData)) {
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
        JOptionPane.showMessageDialog(null, isUpdate ? "Sucursal actualizada correctamente." : "Sucursal creada correctamente.",
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No se pudo realizar la operación solicitada.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData();
    fillView(mapData);
    actualizarUltimoId();
    Idsucursal.requestFocusInWindow();

    return rowsAffected;
}

// Método para comprobar si hay cambios entre los datos existentes y los nuevos datos
private boolean hasChanges(Map<String, String> existingData, Map<String, String> newData) {
    for (String key : newData.keySet()) {
        String existingValue = existingData.get(key);
        String newValue = newData.get(key);

        if (existingValue == null) existingValue = "";
        if (newValue == null) newValue = "";

        if (!existingValue.equals(newValue)) {
            return true;
        }
    }
    return false;
}


/**
 * Comprueba si existe una sucursal con el ID especificado
 * @param id El ID de la sucursal a verificar
 * @return true si existe, false si no
 */
private boolean SucursalExiste(String id) {
    if (id == null || id.trim().isEmpty()) {
        System.out.println("El ID proporcionado es nulo o está vacío.");
        return false;
    }

    // Prepara el mapa con el ID para buscar en la base de datos
    Map<String, String> params = new HashMap<>();
    params.put("id", id.trim());  // Asegúrate de que el ID está correctamente ajustado (sin espacios innecesarios)
    System.out.println("Buscando Sucursal con ID: " + id);

    // Usa el método searchById para buscar en la tabla 
    Map<String, String> Data = tc.searchById(params);  // Asumiendo que tc es tu controlador de base de datos para la tabla 

    if (Data != null && !Data.isEmpty()) {
        System.out.println("Se encontró una sucursal con el ID: " + id + " -> " +Data);
        return true;
    } else {
        System.out.println("No se encontró ninguna sucursal con el ID: " + id);
        return false;
    }
}

@Override
public int imBorrar(String crud) {
    // Verificar que los campos no estén vacíos y que una sucursal esté seleccionada
    if (Idsucursal.getText().trim().isEmpty() || sucursal.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    // Obtener el ID del  actual
    int idActual = obtenerIdActual();
    if (idActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Confirmar la eliminación
    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta sucursal?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    // Crear la lista de registros para borrar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idActual));
    registrosParaBorrar.add(registro);

    // Realizar la eliminación
    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Sucursal eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar la sucursal.", "Error", JOptionPane.ERROR_MESSAGE);
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
         idSucursales = -1;
        return 0;
    }

@Override
public int imBuscar() {
    buscarYRellenarDatos();
    idSucursales = obtenerIdActual(); 
    return 0; // Retorna 0 para indicar que el formulario se cerró sin errores
}


    @Override
    public int imFiltrar() {
Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    // Lista de columnas para mostrar en el formulario de búsqueda 
    List<String> columnas = Arrays.asList("id", "sucursal");
    Form_Buscar buscador = new Form_Buscar(parentFrame, true, tc, "SUCURSALES", columnas);

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
    if ( idSucursales == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf( idSucursales), "NEXT");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imAnterior() {
    if ( idSucursales <= 1) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf( idSucursales), "PRIOR");
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
                job.setJobName("Sucursal");
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
    String nombre = datosSeleccionados.get("Descripcion"); // 'Descripcion' se usa como clave para el nombre 

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idsucursal = Integer.parseInt(idStr); // Convertir el ID a entero
            // Buscar detalles adicionales si es necesario
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("SUCURSALES", "id", idsucursal);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                // Suponiendo que 'registro' contenga claves adicionales si necesarias, ej.
                final String nombreSuc = registro.get("sucursal"); // Recuperar si está disponibl
                final String direccionSuc = registro.get("direccion"); // Recuperar si está disponible 
                final String telefonoD = registro.get("telefono");
                 final String contatoD = registro.get("contacto");
                // Actualizar la UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    Idsucursal.setText(idStr);
                    sucursal.setText(nombreSuc);
                    Telefono.setText(telefonoD);
                    Contacto.setText(contatoD);
                    textdireccion.setText(direccionSuc ); // Actualizar campo 
                     idSucursales = idsucursal; // Guardar el ID  actual
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una sucursal con el Código especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El Código debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Ingrese un Código para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}

}
