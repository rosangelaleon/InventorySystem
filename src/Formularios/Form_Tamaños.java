/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import Filtros.DescriptionFilter;
import java.awt.event.KeyAdapter;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
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

public class Form_Tamaños extends javax.swing.JInternalFrame implements InterfaceUsuario {
    private DBTableController tc;
    private Map<String, String> mapData;
    private int idTamanos ;
    
    public Form_Tamaños() {
        initComponents();
        Idtamano.setText("0"); 
      
        tc = new DBTableController();
        tc.iniciar("TAMANOS");
        mapData = new HashMap<>();
        idTamanos = -1;
        initializeTextFields();
        actualizarUltimoId(); // Actualizar el JLabel con el último ID al abrir el formulario
    }


    private void setMapData() {
        mapData.clear();
        String id = Idtamano.getText().trim();
        if (!id.isEmpty() && !id.equals("0")) {
            mapData.put("id", id);
        }

        String nombre = tamano.getText().trim();
        if (!nombre.isEmpty()) {
            mapData.put("tamano", nombre);
        }

        String descripcion = textdescripcion.getText().trim();
        // Only add description if it's not empty
        if (!descripcion.isEmpty()) {
            mapData.put("descripcion", descripcion);  // Ensure correct handling
            System.out.println("Descripcion setmapdata: " + descripcion );
            
        }
    }

    private void resetData() {
                mapData.clear();
        Idtamano.setText("0");
        tamano.setText("");
        textdescripcion.setText("");
       mapData.clear();
    }

private void fillView(Map<String, String> data) {
    Idtamano.setText(data.getOrDefault("id", ""));
    tamano.setText(data.getOrDefault("tamano", ""));
    textdescripcion.setText(data.getOrDefault("descripcion", ""));
}


 private void initializeTextFields() {
        applyNumericFilter(Idtamano);
        applyAlphaFilter(tamano);
        applyDescriptionFilter(textdescripcion);
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
Idtamano.addFocusListener(new DefaultFocusListener(Idtamano, true)); // Aún rellenará con "0"
tamano.addFocusListener(new DefaultFocusListener(tamano, true)); // Aún rellenará con "0"
textdescripcion.addFocusListener(new DefaultFocusListener(textdescripcion, false)); // No rellenará con "0"

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
    Idtamano.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                buscarYRellenarDatos();  // Llama al método que gestiona la búsqueda y el relleno.   
            }
        }
    });
}


    private int obtenerIdActual() {
        try {
            return Integer.parseInt(Idtamano.getText().trim());
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
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    } else {
        resetData();  
        // Actualiza la interfaz de usuario con los datos obtenidos
        int Id = Integer.parseInt(registro.get("id"));
        String nombre = registro.get("tamano");
        String descripcion = registro.get("descripcion");
        actualizarUIConRegistro(Id, nombre,descripcion);
        return 1;
    }
}

private void actualizarUIConRegistro(int id, String nombre,String descripcion) {
    this.idTamanos  = id;
    Idtamano.setText(String.valueOf(id));
    tamano.setText(nombre);
        textdescripcion.setText(descripcion!= null ? descripcion : "");
}
private void buscarYRellenarDatos() {
    String id = Idtamano.getText().trim();
    if (!id.isEmpty()) {
         resetData();
        try {
            int idT= Integer.parseInt(id);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("TAMANOS", "id", idT);
            idTamanos=idT;
            if (!registros.isEmpty()) {
                 
                Map<String, String> registro = registros.get(0);
                fillView(registro);
                System.out.println("Registro encontrado: " + registro); 
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un tamaño con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
        Idtamano = new javax.swing.JTextField();
        tamano = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_tamano = new javax.swing.JLabel();
        lbl_descripcion = new javax.swing.JLabel();
        lblultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();
        textdescripcion = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Tamaños");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_id.setText("Id");

        lbl_tamano.setText("Tamaño");

        lbl_descripcion.setForeground(new java.awt.Color(102, 102, 102));
        lbl_descripcion.setText("Descripción");

        lblultimo.setText("Último");

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setOpaque(true);

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
                    .addComponent(lbl_descripcion)
                    .addComponent(lbl_tamano, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_id, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tamano)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Idtamano, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(lblultimo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textdescripcion))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Idtamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_id)
                    .addComponent(lblultimo)
                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tamano))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_descripcion)
                    .addComponent(textdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Idtamano;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_descripcion;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_tamano;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JTextField tamano;
    private javax.swing.JTextField textdescripcion;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String id = Idtamano.getText().trim();
    String nombre = tamano.getText().trim();
    String descripcion = textdescripcion.getText().trim();

    if (id.isEmpty() || Integer.parseInt(id) <= 0 || nombre.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Los campos ID y tamaño son obligatorios y deben ser válidos.",
                                      "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Preparar los datos para la inserción o actualización
    setMapData();
    boolean isUpdate = Existe(id);
    int rowsAffected = 0;

    if (isUpdate) {
        Map<String, String> existingData = tc.searchById(mapData);  // Asumimos que este método devuelve los datos actuales
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
        JOptionPane.showMessageDialog(null, isUpdate ? "Tamaño actualizado correctamente." : "Tamaño creado correctamente.",
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No se pudo realizar la operación solicitada.",
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData();
    fillView(mapData);
    actualizarUltimoId();
    Idtamano.requestFocusInWindow();

    return rowsAffected;
}

private boolean Existe(String id) {
    if (id == null || id.trim().isEmpty()) {
        System.out.println("El ID proporcionado es nulo o está vacío.");
        return false;
    }

    // Prepara el mapa con el ID para buscar en la base de datos
    Map<String, String> params = new HashMap<>();
    params.put("id", id.trim());  // Asegúrate de que el ID está correctamente ajustado (sin espacios innecesarios)
    System.out.println("Buscando tamaño con ID: " + id);

    // Usa el método searchById para buscar 
    Map<String, String> Data = tc.searchById(params);  // Asumiendo que tc es tu controlador de base de datos para la tabla

    if (Data != null && !Data.isEmpty()) {
        return true;
    } else {
        System.out.println("No se encontró ningun tamaño con el ID: " + id);
        return false;
    }
}


    @Override
    public int imBorrar(String crud) {
     if (Idtamano.getText().trim().isEmpty() || tamano.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        int idActual = obtenerIdActual();
        if (idActual <= 0) {
            JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta tamaño?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return 0;
        }

        ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
        Map<String, String> registro = new HashMap<>();
        registro.put("id", String.valueOf(idActual));
        registrosParaBorrar.add(registro);

        int resultado = tc.deleteReg(registrosParaBorrar);
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Tamaño eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el tamaño.", "Error", JOptionPane.ERROR_MESSAGE);
        }

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
         idTamanos = -1;
        return 0;
    }

@Override
public int imBuscar() {
    buscarYRellenarDatos(); 
    return 0; // Retorna 0 para indicar que el formulario se cerró sin errores
}


    @Override
    public int imFiltrar() {
  resetData(); 
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    // Lista de columnas para mostrar en el formulario de búsqueda 
    List<String> columnas= Arrays.asList("id", "tamano");
    Form_Buscar buscador = new Form_Buscar(parentFrame, true, tc, "TAMANOS", columnas);

    buscador.setOnItemSeleccionadoListener(this);
    // Muestra el formulario de búsqueda
    buscador.setVisible(true);

    // Este código no maneja el resultado de la búsqueda directamente
    // Se asume que la lógica de manejo del ítem seleccionado se procesa en el método onItemSeleccionado
    return 0; // Retorna 0 para indicar que el formulario se cerró sin errores
    }

@Override
public int imPrimero() {
    Map<String, String> registro = tc.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imSiguiente() {
    if (idTamanos  == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idTamanos ), "NEXT");
    return procesarRegistroNavegacion(registro);
}


@Override
public int imAnterior() {
    if (idTamanos  <= 1) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idTamanos ), "PRIOR");
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
                job.setJobName("Tamaños");
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
            int id = Integer.parseInt(idStr); // Convertir el ID a entero
            // Buscar detalles adicionales si es necesario
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("TAMANOS", "id",  id );

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                // Suponiendo que 'registro' contenga claves adicionales si necesarias, ej. 'descripcion'
                final String descripcion = registro.get("descripcion"); // Recuperar la descripción si está disponible

                // Actualizar la UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    Idtamano.setText(idStr);
                    tamano.setText(nombre);
                    textdescripcion.setText(descripcion); // Actualizar campo de descripción
                    idTamanos  = id; // Guardar el ID de la  actual
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un tamaño con el Código especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
