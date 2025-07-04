/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;
import Controllers.DBTableController;
import Filtros.DefaultFocusListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.Map;
import Controllers.InterfaceUsuario;
import Filtros.NumericDocumentFilter;
import Filtros.TextFilter;
import java.awt.Frame;import java.awt.Graphics;
import java.awt.Graphics2D;
;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;


public class Form_Roles extends javax.swing.JInternalFrame implements InterfaceUsuario{
    private DBTableController tc;
    private Map<String, String> mapData;
    private int idRol;
    
    public Form_Roles() {
        initComponents();
        roles_id.setText("0"); 
        nombre_roles.setText(""); // Limpiar campo Nombre
        tc = new DBTableController();
        tc.iniciar("ROLES");
        mapData = new HashMap<>();
        idRol = -1;
        initializeTextFields();
        actualizarUltimoId(); // Actualizar el JLabel con el último ID al abrir el formulario

    }
     private void initializeTextFields() {
    applyNumericFilter(roles_id);
    applyAlphaFilter(nombre_roles);
    addFocusListeners();
    addEnterKeyListenerToIdField();
}



    private void addFocusListeners() {
        roles_id.addFocusListener(new DefaultFocusListener(roles_id,false));
        nombre_roles.addFocusListener(new DefaultFocusListener(nombre_roles,true));
    }
    
    private void addEnterKeyListenerToIdField() {
    roles_id.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int id = obtenerIdRolActual();
                if (id != -1) {
                    List<Map<String, String>> resultado = tc.buscarPorIdGenerico("ROLES", "id", id);
                    if (!resultado.isEmpty()) {
                        Map<String, String> datosRol = resultado.get(0); // Asumimos que la búsqueda por ID devuelve un único resultado
                        fillView(datosRol);
                        idRol=id;
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró el rol con ID: " + id);
                        resetData(); // Resetea los campos
                        fillView(mapData); // Actualiza la vista
                        
                    }
                }
            }
        }
    });
}


private void fillView(Map<String, String> data) {
    roles_id.setText(data.getOrDefault("id", ""));
    nombre_roles.setText(data.getOrDefault("rol", ""));
    // Asegúrate de actualizar otros campos del formulario aquí si son necesarios
}

    
    private void actualizarUltimoId() {
        try {
            int ultimoId = tc.getMaxId(); // devuelve el ID máximo actual de la tabla 'ROLES'
            UltimoId.setText(String.valueOf(ultimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID del rol.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void SetMapData() {
        mapData.put("id", roles_id.getText());
        mapData.put("rol", nombre_roles.getText());
    }

    private void resetData() {
        mapData.put("id", "0");
        mapData.put("rol", "");
    }

    // Implementación de imGuardar, imBorrar, etc.

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }


private int obtenerIdRolActual() {
    try {
        return Integer.parseInt(roles_id.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID inválido para el rol.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}

private int mostrarDialogoConfirmacion(String mensaje) {
    return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
}
private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
        return -1;
    } else {
        // Actualiza la interfaz de usuario con los datos obtenidos
        int idRol = Integer.parseInt(registro.get("id"));
        String nombreRol = registro.get("rol");
        actualizarUIConRegistro(idRol, nombreRol);
        return 1;
    }
}

private void actualizarUIConRegistro(int idRol, String nombreRol) {
    this.idRol = idRol;
    roles_id.setText(String.valueOf(idRol));
    nombre_roles.setText(nombreRol);
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
        lbl_roles = new javax.swing.JLabel();
        roles_id = new javax.swing.JTextField();
        lbl_nombre = new javax.swing.JLabel();
        nombre_roles = new javax.swing.JTextField();
        lbl_ultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Roles");

        jPanel1.setPreferredSize(new java.awt.Dimension(266, 140));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_roles.setText("Id");
        jPanel1.add(lbl_roles, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 23, -1, -1));

        roles_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roles_idKeyPressed(evt);
            }
        });
        jPanel1.add(roles_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(62, 20, 93, -1));

        lbl_nombre.setText("Nombre");
        jPanel1.add(lbl_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 62, -1, -1));
        jPanel1.add(nombre_roles, new org.netbeans.lib.awtextra.AbsoluteConstraints(62, 62, 194, -1));

        lbl_ultimo.setText("Último");
        jPanel1.add(lbl_ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(173, 23, -1, -1));

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setOpaque(true);
        jPanel1.add(UltimoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 20, 40, 22));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("Registrar Rol");

        pack();
    }// </editor-fold>//GEN-END:initComponents
 private void roles_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roles_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roles_idActionPerformed

    private void roles_idFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_roles_idFocusGained
   // Si se establece el texto aquí, podría sobrescribir el valor establecido programáticamente
    if (roles_id.getText().isEmpty()) {
        roles_id.setText("Ingrese ID del rol"); // Solo establecer texto si está vacío
    }
    }//GEN-LAST:event_roles_idFocusGained

    private void nombre_rolesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombre_rolesFocusGained
    if (nombre_roles.getText().isEmpty()) {
        nombre_roles.setText("Ingrese nombre del rol"); // Solo establecer texto si está vacío
    }
    }//GEN-LAST:event_nombre_rolesFocusGained

    private void roles_idKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roles_idKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_roles_idKeyPressed

    public static void focusGained(JTextField focus, String From_Buscar) {
    focus.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            focus.setText(From_Buscar);
            // Aquí podrías añadir cualquier otra lógica necesaria cuando el campo gana el foco
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel UltimoId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_nombre;
    private javax.swing.JLabel lbl_roles;
    private javax.swing.JLabel lbl_ultimo;
    private javax.swing.JTextField nombre_roles;
    private javax.swing.JTextField roles_id;
    // End of variables declaration//GEN-END:variables
@Override
public int imGuardar(String crud) {
    int rows = 0;
    String msg;

    // Establecer los datos del formulario en el mapa
    SetMapData();  // Asumiendo que mapData se define en algún lugar de tu clase

    // Comprobar si el campo de ID está vacío, lo que indicaría un nuevo registro
    String strId = roles_id.getText().trim();
    String nombreRol = nombre_roles.getText().trim();
    if (strId.isEmpty() || strId.equals("0") || nombreRol.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Los campos ID y Nombre del Rol son obligatorios y deben ser válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        resetData(); // Resetea los campos
        fillView(mapData); // Actualiza la vista
        actualizarUltimoId(); // Actualiza el último ID
        return -1; // Retorna -1 para indicar que hubo un error
    }

    int id = Integer.parseInt(strId);  // Convertir el ID a entero
    if (id <= 0) {
        JOptionPane.showMessageDialog(this, "El ID debe ser un número positivo mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
        resetData(); // Resetea los campos
        fillView(mapData); // Actualiza la vista
        actualizarUltimoId(); // Actualiza el último ID
        return -1; // Retorna -1 para indicar que hubo un error
    }

    // Verificar si el ID ya existe en la base de datos para prevenir la duplicación
    if (tc.existAny(mapData) > 0) {
        // Si existe, entonces se verifica si hay cambios antes de actualizar
        Map<String, String> existingData = tc.searchById(mapData);
        if (existingData != null && existingData.equals(mapData)) {
            JOptionPane.showMessageDialog(this, "No hay cambios para guardar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return 0; // Retorna 0 para indicar que no hubo cambios
            
        }

        // Si hay cambios, entonces se actualiza el registro
        ArrayList<Map<String, String>> listaParaActualizar = new ArrayList<>();
        listaParaActualizar.add(mapData);
        rows = tc.updateReg(listaParaActualizar);
        msg = (rows > 0) ? "El rol ha sido actualizado con éxito." : "No se pudo actualizar el rol.";
    } else {
        // Si no existe, se crea un nuevo registro
        rows = tc.createReg(mapData);
        msg = (rows > 0) ? "El rol ha sido creado con éxito." : "No se pudo crear el rol.";
    }

    resetData(); // Resetea los campos
    fillView(mapData); // Actualiza la vista
    actualizarUltimoId(); // Actualiza el último ID
    JOptionPane.showMessageDialog(this, msg, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
    return rows; // Devuelve el número de filas afectadas
}

@Override
public int imBorrar(String crud) {
    // Verificar que todos los campos estén llenos
    if (roles_id.getText().trim().isEmpty() || nombre_roles.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    int idRolActual = obtenerIdRolActual();
    if (idRolActual <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este rol?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0;
    }

    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registro = new HashMap<>();
    registro.put("id", String.valueOf(idRolActual));
    registrosParaBorrar.add(registro);

    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Rol eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar el rol,se utiliza en otra tabla.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData(); // Resetea los campos
    fillView(mapData); // Actualiza la vista
    actualizarUltimoId(); // Actualiza el último ID
    return resultado;
}

 @Override
public int imNuevo() {
// Resetear los datos
    resetData(); // Resetea a valores por defecto
    // Actualizar la interfaz de usuario con los datos reseteados
    fillView(mapData);
    actualizarUltimoId(); // Actualiza el último ID
    idRol = -1;

    return 0; // Indica éxito
}


   
    @Override
    public int imFiltrar() {

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        List<String> columnasParaRoles = Arrays.asList("id", "rol");
        Form_Buscar buscadorRoles = new Form_Buscar(parentFrame, true, tc, "roles", columnasParaRoles);

     buscadorRoles.setOnItemSeleccionadoListener(this);
    // Muestra el formulario de búsqueda
    buscadorRoles.setVisible(true);
    


    // Opcional: Manejo de lo que sucede después de cerrar el formulario de búsqueda
    // Suponiendo que tienes un método getSeleccionado() que devuelve el registro seleccionado
    return 0; // No se seleccionó nada o no se encontraron resultados
    }

@Override
public int imPrimero() {
Map<String, String> registro = tc.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}

@Override
public int imSiguiente() {
     // Si idRol es -1, significa que no hay registro actual seleccionado
    if (idRol == -1) {
          JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    Map<String, String> registro = tc.navegationReg(String.valueOf(idRol), "NEXT");
    return procesarRegistroNavegacion(registro);
}

@Override
public int imAnterior() {
if (idRol <= 1) {
        JOptionPane.showMessageDialog(null, "No hay más registros en esta dirección.");
        return -1;
    }
    
    if (idRol <= 1) return -1;
    Map<String, String> registro = tc.navegationReg(String.valueOf(idRol), "PRIOR");
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
                job.setJobName("Roles");
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
    public int imBuscar() {

                int id = obtenerIdRolActual();
                if (id != -1) {
                    List<Map<String, String>> resultado = tc.buscarPorIdGenerico("ROLES", "id", id);
                    if (!resultado.isEmpty()) {
                        Map<String, String> datosRol = resultado.get(0); // Asumimos que la búsqueda por ID devuelve un único resultado
                        fillView(datosRol);
                        idRol=id;
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró el rol con ID: " + id);
                        resetData(); // Resetea los campos
                        fillView(mapData); // Actualiza la vista
                        
                    }
                }
      return 0;
    }//fin imBuscar

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    System.out.println("Datos seleccionados originales: " + datosSeleccionados);

    // Normalizar las claves del mapa
    Map<String, String> normalizedData = new HashMap<>();
    datosSeleccionados.forEach((key, value) -> normalizedData.put(key.replaceAll("[^\\p{Alnum}\\p{Space}]+", ""), value));

    System.out.println("Datos normalizados: " + normalizedData);

    // Obtiene los valores de 'Codigo' y 'Descripcion'
    final String id = normalizedData.get("Codigo");
    final String nombre = normalizedData.get("Descripcion");

    System.out.println("ID: " + id + ", Nombre: " + nombre);

    SwingUtilities.invokeLater(() -> {
        roles_id.setText(id);
        nombre_roles.setText(nombre);
         idRol = Integer.parseInt(id); 
    });
}

}

