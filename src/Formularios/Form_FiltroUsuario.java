
package Formularios;

import java.util.HashMap;
import javax.swing.JOptionPane;
import Controllers.DBConexion;
import Controllers.DBTableController;
import Filtros.DefaultFocusListener;
import Filtros.TextFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.*;


public class Form_FiltroUsuario extends javax.swing.JDialog {
private DBTableController tr;
    private DBTableController tu;
    private Map<String, Object> filtrosMap;
    private HashMap<String, Object> parametros;
    private Form_ReportesUsuarios formReportes; // Agregar esta línea

    public Form_FiltroUsuario(java.awt.Frame parent, boolean modal, Form_ReportesUsuarios formReportes) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        tr = new DBTableController();
        tr.iniciar("ROLES");
        tu = new DBTableController();
        tu.iniciar("USUARIOS");
        filtrosMap = new HashMap<>();  // Inicialización de mapData
        this.formReportes = formReportes; 
        cargarRoles();
        cargarIdUsuario();
        agregarOpcionesOrdenar();
        agregarActivo();
        this.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            imFiltrar();
        }
    }
});
this.setFocusable(true);
    }
         private void initializeTextFields() {
        applyAlphaFilter(Usuario);
        addFocusListeners();
    }
        private void addFocusListeners() {
         Usuario.addFocusListener(new DefaultFocusListener(Usuario,false));
    }
private void applyAlphaFilter(JTextField textField) {
        javax.swing.text.Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new TextFilter());
        }
    }

private void setMapData() {
    filtrosMap.clear();

    try {
        int id = obtenerIdUsuario();
        if (id != -1) {
            filtrosMap.put("id", id);
        }

        String usuario = Usuario.getText().trim();
        if (!usuario.isEmpty()) {
            filtrosMap.put("usuario", usuario);
        }

        int rolId = obtenerIdRolActual();
        if (rolId != -1) {
            filtrosMap.put("rol_id", rolId);
        }

        filtrosMap.put("activo", Activo.getSelectedItem().toString());
        filtrosMap.put("orden", Orden.getSelectedItem().toString());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al establecer datos de mapa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void resetData() {
    // Limpiar los campos del formulario y restablecer cualquier estado interno.
    Usuario.setText("");  // Limpia el campo de texto del usuario.
    if (Idusuario.getItemCount() > 0) {
        Idusuario.setSelectedIndex(0);  // Asumiendo que el índice 0 es "Seleccione un Id Usuario".
    }
    if (Rol.getItemCount() > 0) {
        Rol.setSelectedIndex(0);  // Asumiendo que el índice 0 es "Seleccione un Rol".
    }
    if (Activo.getItemCount() > 0) {
        Activo.setSelectedIndex(0);  // Asumiendo que el índice 0 es "Seleccione un Rol".
    }
    filtrosMap.clear();

    // Restablecer los ComboBox de ordenamiento si es necesario.
    if (Orden.getItemCount() > 0) {
        Orden.setSelectedIndex(0);  // Vuelve a la opción por defecto si es necesario.
    }


}
private void fillView() {
    Usuario.setText(getOrDefault("usuario", ""));
    updateComboBox(Idusuario, getOrDefault("id", null));
    updateComboBox(Rol, getOrDefault("rol_id", null));
    updateComboBox(Activo, getOrDefault("activo", null));
    updateComboBox(Orden, getOrDefault("orden", null));
}

private <T> T getOrDefault(String key, T defaultValue) {
    return (T) filtrosMap.getOrDefault(key, defaultValue);
}

private void updateComboBox(JComboBox comboBox, Object value) {
    if (value != null) {
        comboBox.setSelectedItem(value.toString());
    } else {
        comboBox.setSelectedIndex(0);
    }

}



private void cargarRoles() {
    Rol.removeAllItems(); // Limpia el JComboBox antes de cargar nuevos datos
    Rol.addItem("Seleccione un rol"); // Opción inicial

    try {
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", "");
        viewRegister.put("rol", "");

        ArrayList<Map<String, String>> resultados = tr.searchListById(viewRegister, new HashMap<>());
        resultados.sort(Comparator.comparing(a -> Integer.parseInt(a.get("id"))));

        for (Map<String, String> entry : resultados) {
            Rol.addItem(entry.get("id") + " - " + entry.get("rol"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar roles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private int obtenerIdRolActual() {
    String selectedItem = (String) Rol.getSelectedItem();
    if (selectedItem != null && !selectedItem.equals("Seleccione un rol")) {
        try {
            return Integer.parseInt(selectedItem.split(" - ")[0]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de rol no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    return -1; // Retorna -1 si no se selecciona un rol válido
}

private void seleccionarEnComboBoxId(int idUsuario) {
    for (int i = 0; i < Idusuario.getItemCount(); i++) {
        String id = Idusuario.getItemAt(i).toString();
        if (id.equals(String.valueOf(idUsuario))) {
            Idusuario.setSelectedIndex(i);
            break;
        }
    }
}


private void cargarIdUsuario() {
    Idusuario.removeAllItems(); // Limpia el JComboBox antes de cargar nuevos datos
    Idusuario.addItem("Seleccione un Id Usuario"); // Opción inicial

    try {
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", "");

        ArrayList<Map<String, String>> resultados = tu.searchListById(viewRegister, new HashMap<>());
        resultados.sort(Comparator.comparing(a -> Integer.parseInt(a.get("id"))));

        for (Map<String, String> usuario : resultados) {
            Idusuario.addItem(usuario.get("id"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar ID de usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void seleccionarRolEnComboBox(int idRol) {
    for (int i = 0; i < Rol.getItemCount(); i++) {
        String item = Rol.getItemAt(i).toString();
        if (item.startsWith(idRol + " - ")) {
            Rol.setSelectedIndex(i);
            break;
        }
}

}

public int imFiltrar() {
    try {
        // Inicializar el mapa de parámetros.
        parametros = new HashMap<>();

        // Establecer el ID de usuario, convirtiendo a Integer si es necesario.
        String idSeleccionado = (String) Idusuario.getSelectedItem();
        parametros.put("IdParam", idSeleccionado != null && !idSeleccionado.equals("Seleccione un Id Usuario") ? Integer.valueOf(idSeleccionado) : null);

        // Resto de los parámetros...
        // Asegúrate de que los nombres de los parámetros aquí coincidan con los de tu archivo .jrxml.
        String usuarioTexto = Usuario.getText().trim();
        parametros.put("UsuarioParam", !usuarioTexto.isEmpty() ? usuarioTexto : null);
        String activoSeleccionado = (String) Activo.getSelectedItem();
         if ("Seleccione una opción".equals(activoSeleccionado)) {
        // Si se selecciona "Seleccione una opción", mostrar todos los registros
        parametros.remove("ActivoParam"); // Eliminar el parámetro para mostrar todos los registros
        } else {
        // Si se selecciona "SI" o "NO", establecer el parámetro correspondiente
        parametros.put("ActivoParam", "NO".equals(activoSeleccionado) ? 0 : 1);
        }


        int rolId = obtenerIdRolActual();
        parametros.put("RolIdParam", rolId != -1 ? Integer.valueOf(rolId) : null);

        String ordenSeleccionado = (String) Orden.getSelectedItem();
        parametros.put("TipoOrdenParam", "Descendente".equals(ordenSeleccionado) ? "DESC" : "ASC");


        return 0;
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al establecer los parámetros para el informe: " + e.getMessage());
        return -1;
    }
}


private int obtenerIdUsuario() {
    String selectedItem = (String) Idusuario.getSelectedItem();
    if (selectedItem != null && !selectedItem.equals("Seleccione un Id Usuario")) {
        try {
            return Integer.parseInt(selectedItem);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al convertir el ID de usuario: " + selectedItem, "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return -1; // Retorna -1 si el ID no puede convertirse a entero
        }
    }
    return -1; // Retorna -1 si no se selecciona un ID de usuario válido o si se selecciona la opción por defecto
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
        lbl_usuario = new javax.swing.JLabel();
        Usuario = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        Idusuario = new javax.swing.JComboBox<>();
        lbl_Rol = new javax.swing.JLabel();
        Rol = new javax.swing.JComboBox<>();
        Ordenar = new javax.swing.JLabel();
        Orden = new javax.swing.JComboBox<>();
        Filtrar = new javax.swing.JLabel();
        jbAceptar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        Activo = new javax.swing.JComboBox<>();
        Lbl_Activo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Filtro Usuarios");
        setMaximumSize(new java.awt.Dimension(550, 300));
        setPreferredSize(new java.awt.Dimension(500, 285));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 245));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_usuario.setText("Usuario");
        jPanel1.add(lbl_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, -1));
        jPanel1.add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 170, -1));

        lbl_id.setText("Id");
        jPanel1.add(lbl_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        Idusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdusuarioActionPerformed(evt);
            }
        });
        jPanel1.add(Idusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 180, -1));

        lbl_Rol.setText("Rol");
        jPanel1.add(lbl_Rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, -1));

        Rol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RolActionPerformed(evt);
            }
        });
        jPanel1.add(Rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 170, -1));

        Ordenar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Ordenar.setText("Ordenar");
        jPanel1.add(Ordenar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        Orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrdenActionPerformed(evt);
            }
        });
        jPanel1.add(Orden, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 280, -1));

        Filtrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Filtrar.setText("Filtrar ");
        jPanel1.add(Filtrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jbAceptar.setText("Aceptar");
        jbAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAceptarActionPerformed(evt);
            }
        });
        jPanel1.add(jbAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, -1, -1));

        jbCancelar.setText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(jbCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, -1, -1));

        Activo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActivoActionPerformed(evt);
            }
        });
        jPanel1.add(Activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 180, -1));

        Lbl_Activo.setText("Activo");
        jPanel1.add(Lbl_Activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RolActionPerformed
        // Verifica si hay un rol seleccionado antes de proceder                                  
    int rolId = obtenerIdRolActual();
    if (rolId == -1 && !"Seleccione un rol".equals(Rol.getSelectedItem().toString())) {
        JOptionPane.showMessageDialog(this, "Seleccione un rol válido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_RolActionPerformed

    private void IdusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdusuarioActionPerformed
// Obtiene el ID de usuario seleccionado del JComboBox
    if (Idusuario.getSelectedItem() != null && !"Seleccione un Id Usuario".equals(Idusuario.getSelectedItem().toString())) {
      int idUsuario = obtenerIdUsuario(); // Cambio aquí
        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        resetData();  // Llama a resetData si no hay selección o si es la opción por defecto
    }
    }//GEN-LAST:event_IdusuarioActionPerformed

    private void agregarOpcionesOrdenar() {
        Orden.addItem("Seleccione el tipo de Orden");
        Orden.addItem("Ascendente");
        Orden.addItem("Descendente");
    }
        private void agregarActivo() {
        Activo.addItem("Seleccione una opción");
        Activo.addItem("SI");
        Activo.addItem("NO");
    }


    
    private void OrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrdenActionPerformed
        String ordenSeleccionado = (String) Orden.getSelectedItem();
        if ("Ascendente".equals(ordenSeleccionado)) {
            // Código para ordenar de manera ascendente
        } else if ("Descendente".equals(ordenSeleccionado)) {
            // Código para ordenar de manera descendente
        }
    }//GEN-LAST:event_OrdenActionPerformed

    private void jbAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAceptarActionPerformed
    if (imFiltrar() == 0) { // Solo procede si imFiltrar fue exitoso
        formReportes.actualizarYGenerarReporte(this.parametros); // Usa la referencia para actualizar y generar el reporte
        
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo establecer los filtros para el informe.");
    }
    this.dispose(); // Cierra el formulario de filtro
    }//GEN-LAST:event_jbAceptarActionPerformed

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
         resetData();  // Limpia todos los campos y restablece el estado de los controles
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void ActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActivoActionPerformed
        String activoSeleccionado = (String) Activo.getSelectedItem();
        if ("SI".equals(activoSeleccionado)) {
            // Código para ordenar de manera ascendente
        } else if ("NO".equals(activoSeleccionado)) {
            // Código para ordenar de manera descendente
        }        // TODO add your handling code here:
    }//GEN-LAST:event_ActivoActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    /* Establece la apariencia visual Nimbus */
    //<editor-fold defaultstate="collapsed" desc=" Código de configuración de apariencia (opcional) ">
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(Form_FiltroUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Crea y muestra el diálogo */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Suponiendo que no estamos configurando funcionalidad de filtrado aquí
            // porque no tiene sentido abrir solo un formulario de filtro en main.
            // Típicamente, abrirías una ventana principal de la aplicación.
            JFrame frame = new JFrame("Ventana Principal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Si necesitas abrir el filtro directamente para pruebas:
            Form_FiltroUsuario dialog = new Form_FiltroUsuario(frame, true, null);  // Pasando null si no se necesita un callback para pruebas
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Activo;
    private javax.swing.JLabel Filtrar;
    private javax.swing.JComboBox<String> Idusuario;
    private javax.swing.JLabel Lbl_Activo;
    private javax.swing.JComboBox<String> Orden;
    private javax.swing.JLabel Ordenar;
    private javax.swing.JComboBox<String> Rol;
    private javax.swing.JTextField Usuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbAceptar;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JLabel lbl_Rol;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_usuario;
    // End of variables declaration//GEN-END:variables

}
