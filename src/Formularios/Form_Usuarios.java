/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;
import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import javax.swing.*;
import Filtros.NumericDocumentFilter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.AbstractDocument;
import org.mindrot.jbcrypt.BCrypt;


public class Form_Usuarios extends javax.swing.JInternalFrame implements InterfaceUsuario {

       private DBTableController tu;
    private DBTableController tr;
    private DBTableController tm;
    private Map<String, String> mapData;
    private int idUsuarioActual = -1;
    
    public Form_Usuarios() {
        initComponents();
        txtId_usuario.setText("0"); 
        mapData = new HashMap<>();  // Inicialización de mapData
        tu = new DBTableController();
        tu.iniciar("USUARIOS");
        tr = new DBTableController();
        tr.iniciar("ROLES");
        initializeTextFields();
         cargarUltimoId(); // Llamar al método que actualiza el JLabel con el último ID al abrir el formulario
         cargarRoles();
    }
    private void initializeTextFields() {
        applyNumericFilter(txtId_usuario);
        applyAlphaFilter(txtUsuario);
        addFocusListeners();
        addEnterKeyListenerToIdField();
    }

    private void addFocusListeners() {
        txtId_usuario.addFocusListener(new DefaultFocusListener(txtId_usuario,true));
        txtUsuario.addFocusListener(new DefaultFocusListener(txtUsuario,false));
         txtContrasena.addFocusListener(new DefaultFocusListener(txtContrasena,false));
        // Agregar focus listeners para otros campos si es necesario
    }

    private void applyNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    private void applyAlphaFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new TextFilter());
    }
    
private void addEnterKeyListenerToIdField() {
    txtId_usuario.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                buscarYRellenarDatos();  // Directamente llamar al método que gestiona la búsqueda y el relleno
            }
        }
    });
}

    
    private void cargarUltimoId() {
        try {
            int ultimoId = tu.getMaxId(); // Suponemos que getMaxId() devuelve el último ID utilizado en la tabla 'usuarios'
            UltimoId.setText(String.valueOf(ultimoId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void setMapData() {
    mapData.clear();
    String id = txtId_usuario.getText().trim();
    // Asegúrate de que el ID no sea vacío y no sea "0".
    if (!id.isEmpty() && !id.equals("0")) {
        mapData.put("id", id);
    }

    String usuario = txtUsuario.getText().trim();
    // Asegúrate de que el campo de usuario no esté vacío.
    if (!usuario.isEmpty()) {
        mapData.put("usuario", usuario);
    }

    int rolId = obtenerIdRolActual();
    // Solo añadir el rol si es válido (no -1).
    if (rolId != -1) {
        mapData.put("rol_id", String.valueOf(rolId));
    }

    mapData.put("activo", String.valueOf(Activo.isSelected()));

    // Agregar la contraseña solo si es un nuevo usuario y la contraseña es válida.
    if (!usuarioExiste(id) && rolId != -1 && !id.equals("0")) {
        String contrasena = new String(txtContrasena.getPassword()).trim();
        if (!contrasena.isEmpty() && esContrasenaFuerte(contrasena)) {
            mapData.put("contraseña", BCrypt.hashpw(contrasena, BCrypt.gensalt()));
        }
    }
}


 private void resetData() {
    // Limpiar los campos del formulario y restablecer cualquier estado interno.
    txtId_usuario.setText("0");  // Asegurarse de que el campo ID también se limpia.
    txtUsuario.setText("");
    txtContrasena.setText("");
    ConfirmarContraseña.setText("");
    rol.setSelectedIndex(0);  // Asumiendo que el índice 0 es "Seleccione un Rol".
    Activo.setSelected(false);

    // Limpiar cualquier mapa de datos si es utilizado para retener datos del formulario.
    mapData.clear();
}

 private void fillView() {
    // Aplicar los valores de mapData a los componentes de la UI.
    txtId_usuario.setText(mapData.getOrDefault("id", ""));
    txtUsuario.setText(mapData.getOrDefault("usuario", ""));
    txtContrasena.setText("");  // La contraseña generalmente no se muestra por razones de seguridad.
    // Asegurarse de seleccionar el rol correcto en el combo box utilizando el ID del rol.
    int idRol = Integer.parseInt(mapData.getOrDefault("rol_id", "-1"));
    seleccionarRolEnComboBox(idRol);
    
    Activo.setSelected(Boolean.parseBoolean(mapData.getOrDefault("activo", "false")));

    // Si necesitas manejar otros campos, asegúrate de actualizarlos aquí.
}

private int obtenerIdRolActual() {
    String selectedItem = (String) rol.getSelectedItem();
    if (selectedItem != null && !selectedItem.equals("Seleccione un rol")) {
        return Integer.parseInt(selectedItem.split(" - ")[0]);
    }
    return -1; // Retorna -1 si no se selecciona un rol válido
}

private void cargarRoles() {
    rol.removeAllItems(); // Limpia el JComboBox antes de cargar nuevos datos
    rol.addItem("Seleccione un rol"); // Opción inicial

    try {
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", "");  // No necesitas filtrar por un id específico
        viewRegister.put("rol", "");

        ArrayList<Map<String, String>> resultados = tr.searchListById(viewRegister, new HashMap<>());
        resultados.sort(Comparator.comparing(a -> Integer.parseInt(a.get("id"))));

        for (Map<String, String> entry : resultados) {
            rol.addItem(entry.get("id") + " - " + entry.get("rol"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar roles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void buscarYRellenarDatos() {
    String id = txtId_usuario.getText().trim();
    System.out.println("Id para buscar: " + id);
    if (id.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese un ID para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    try {
        int idUsuario = Integer.parseInt(id);
        List<Map<String, String>> registros = tu.buscarPorIdGenerico("USUARIOS", "id", idUsuario);
        
        System.out.println("Registros obtenidos: " + registros);
        idUsuarioActual=idUsuario;
        if (!registros.isEmpty()) {
            Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado
            actualizarUIConDatosUsuario(registro);  // Método para actualizar los campos directamente
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un usuario con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            resetData();
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void actualizarUIConDatosUsuario(Map<String, String> datosUsuario) {
    txtId_usuario.setText(datosUsuario.getOrDefault("id", ""));
    txtUsuario.setText(datosUsuario.getOrDefault("usuario", ""));
    txtContrasena.setText("");  // Por razones de seguridad, no mostramos la contraseña.
    ConfirmarContraseña.setText("");  // Igualmente para confirmar contraseña.
    int rolId = -1;
    try {
        rolId = Integer.parseInt(datosUsuario.get("rol_id"));
    } catch (NumberFormatException ex) {
        rolId = -1;  // Si no se puede convertir, mantenemos -1 como valor no válido.
    }
    seleccionarRolEnComboBox(rolId);  // Actualizar el combo box con el rol.
    Activo.setSelected("1".equals(datosUsuario.get("activo")));  // Actualizar el estado activo.
}


private boolean usuarioExiste(String id) {
    if (id == null || id.trim().isEmpty()) {
        System.out.println("El ID proporcionado es nulo o está vacío.");
        return false;
    }

    Map<String, String> params = new HashMap<>();
    params.put("id", id.trim());
    System.out.println("Buscando usuario con ID: " + id);

    Map<String, String> userData = tu.searchById(params);

    if (userData != null && !userData.isEmpty()) {
        System.out.println("Se encontró un usuario con el ID: " + id + " -> " + userData);
        return true;
    } else {
        System.out.println("No se encontró ningún usuario con el ID: " + id);
        return false;
    }
}


private boolean esContrasenaFuerte(String contrasena) {
    // Mínimo ocho caracteres, al menos una letra mayúscula, una letra minúscula, un número y un carácter especial
    return contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
}
   



    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        lbl_idusuario = new javax.swing.JLabel();
        txtId_usuario = new javax.swing.JTextField();
        lbl_usuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lbl_contrasena = new javax.swing.JLabel();
        lbl_rol = new javax.swing.JLabel();
        rol = new javax.swing.JComboBox<>();
        Activo = new javax.swing.JCheckBox();
        txtContrasena = new javax.swing.JPasswordField();
        ConfirmarContraseña = new javax.swing.JPasswordField();
        lbl_ConfirmarContraseña = new javax.swing.JLabel();
        lbl_Ultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Usuarios");

        jPanel1.setPreferredSize(new java.awt.Dimension(407, 285));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_idusuario.setText("Id");
        jPanel1.add(lbl_idusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));
        jPanel1.add(txtId_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 72, -1));

        lbl_usuario.setText("Usuario");
        jPanel1.add(lbl_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));
        jPanel1.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 250, -1));

        lbl_contrasena.setText(" Contraseña");
        jPanel1.add(lbl_contrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 70, -1));

        lbl_rol.setText("Rol");
        jPanel1.add(lbl_rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, -1, -1));

        rol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rolActionPerformed(evt);
            }
        });
        jPanel1.add(rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 250, -1));

        Activo.setText("Activo");
        Activo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActivoActionPerformed(evt);
            }
        });
        jPanel1.add(Activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, -1, -1));
        jPanel1.add(txtContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 250, -1));
        jPanel1.add(ConfirmarContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 250, -1));

        lbl_ConfirmarContraseña.setText("Confirmar Contraseña");
        jPanel1.add(lbl_ConfirmarContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        lbl_Ultimo.setText("Último");
        jPanel1.add(lbl_Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setAlignmentX(0.5F);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        UltimoId.setOpaque(true);
        jPanel1.add(UltimoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 60, 22));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ActivoActionPerformed

    private void rolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rolActionPerformed
        int rolId = obtenerIdRolActual();
        if (rolId == -1 && !"Seleccione un rol".equals(rol.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Seleccione un rol válido.", "Error", JOptionPane.ERROR_MESSAGE);
             resetData();
                fillView();
                cargarUltimoId();

        }
    }//GEN-LAST:event_rolActionPerformed

    private Integer safeParseInt(String str) {
        try {
            return str != null && !str.isEmpty() ? Integer.parseInt(str) : null;
        } catch (NumberFormatException e) {
            System.err.println("No se pudo convertir a número: " + e.getMessage());
            return null;
        }
    }
private int procesarRegistroNavegacion(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay registros siguientes disponibles.");
        return -1; // Indica que no hay más registros.
    } else {
        try {
            // Actualizar el ID del usuario actual para la navegación.
            idUsuarioActual = Integer.parseInt(registro.get("id"));

            // Actualizar la interfaz de usuario con los datos del registro.
            actualizarUIConDatosUsuario(registro);

            return 1; // Éxito
        } catch (NumberFormatException e) {
            return -1; // Error en la conversión del ID.
        }
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JPasswordField ConfirmarContraseña;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JLabel lbl_ConfirmarContraseña;
    private javax.swing.JLabel lbl_Ultimo;
    private javax.swing.JLabel lbl_contrasena;
    private javax.swing.JLabel lbl_idusuario;
    private javax.swing.JLabel lbl_rol;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JComboBox<String> rol;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtId_usuario;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String id = txtId_usuario.getText().trim();
    String usuario = txtUsuario.getText().trim();
    int rolId = obtenerIdRolActual();
    String contrasena = new String(txtContrasena.getPassword()).trim();
    String confirmarContrasena = new String(ConfirmarContraseña.getPassword()).trim();

    // Validar que los campos obligatorios no estén vacíos
    if (id.isEmpty() || Integer.parseInt(id) <= 0 || usuario.isEmpty() || rolId == -1) {
        JOptionPane.showMessageDialog(null, "Los campos ID, Usuario y Rol son obligatorios y deben ser válidos.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificar si es necesario actualizar la contraseña
    boolean actualizarContrasena = !contrasena.isEmpty() && esContrasenaFuerte(contrasena) && contrasena.equals(confirmarContrasena);

    // Preparar los datos para la inserción o actualización
    setMapData();
    boolean isUpdate = usuarioExiste(id);
    int rowsAffected = 0;

    if (isUpdate) {
        if (actualizarContrasena) {
            mapData.put("contrasena", BCrypt.hashpw(contrasena, BCrypt.gensalt())); // Encriptar nueva contraseña
        }
        ArrayList<Map<String, String>> updateList = new ArrayList<>();
        updateList.add(mapData);
        rowsAffected = tu.updateReg(updateList);
    } else {
        // Si es un nuevo usuario, asegurar que la contraseña esté presente y sea válida
        if (!actualizarContrasena) {
            JOptionPane.showMessageDialog(null, "Para nuevos usuarios, la contraseña no puede estar vacía y debe cumplir los requisitos de seguridad.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        mapData.put("contrasena", BCrypt.hashpw(contrasena, BCrypt.gensalt())); // Encriptar nueva contraseña
        rowsAffected = tu.createReg(mapData);
    }

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(null, isUpdate ? "Usuario actualizado correctamente." : "Usuario creado correctamente.",
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No se pudo realizar la operación solicitada.", 
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }

    resetData();
    fillView();
    cargarUltimoId();

    return rowsAffected;
}


@Override
public int imBorrar(String crud) {
    // Verificar que el campo ID esté lleno
    String strId = txtId_usuario.getText().trim();
    if (strId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de usuario válido para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int idUsuario = Integer.parseInt(strId);
    if (idUsuario <= 0) {
        JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0; // Acción cancelada por el usuario
    }

    // Crear la lista de mapas con el registro a borrar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    Map<String, String> registroParaBorrar = new HashMap<>();
    registroParaBorrar.put("id", String.valueOf(idUsuario));
    registrosParaBorrar.add(registroParaBorrar);

    int rowsAffected = 0;
    try {
        rowsAffected = tu.deleteReg(registrosParaBorrar);
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    resetData(); // Resetea los campos después de borrar
    fillView(); // Actualiza la vista para reflejar el estado inicial del formulario
    cargarUltimoId(); // Actualiza el último ID mostrado

    return rowsAffected;
}



// Método para seleccionar el rol en el JComboBox por ID
private void seleccionarRolEnComboBox(int idRol) {
    for (int i = 0; i < rol.getItemCount(); i++) {
        if (rol.getItemAt(i).toString().startsWith(idRol + " - ")) {
            rol.setSelectedIndex(i);
            break;
        }
    }
}


    @Override
    public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    // Solo incluir 'id' y 'usuario' como columnas de búsqueda
    List<String> columnasParaUsuarios = Arrays.asList( "id","usuario");
    Form_Buscar buscadorUsuarios = new Form_Buscar(parentFrame, true, tu, "usuarios", columnasParaUsuarios);

    buscadorUsuarios.setOnItemSeleccionadoListener(this);
    buscadorUsuarios.setVisible(true);

    return 0; // Retorno estándar si no hay selección o no se encontraron resultados
    }

    
@Override
public int imPrimero() {
    // Obtener el primer registro de usuarios.
    Map<String, String> registro = tu.navegationReg(null, "FIRST");
    return procesarRegistroNavegacion(registro);
}

@Override
public int imSiguiente() {
    // Verificar si hay un usuario actualmente seleccionado.
 if (idUsuarioActual  == -1) {
          JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
        return -1;
    }
    // Obtener el siguiente usuario basado en el ID del usuario actual.
    Map<String, String> registro = tu.navegationReg(String.valueOf(idUsuarioActual), "NEXT");
    
    return procesarRegistroNavegacion(registro);
}

@Override
public int imAnterior() {
   // Verificar si hay un usuario actualmente seleccionado y si no es el primero.
    if (idUsuarioActual <= 1) {
        JOptionPane.showMessageDialog(null, "No hay registros anteriores disponibles.");
        return -1;
    }
    // Obtener el usuario anterior basado en el ID del usuario actual.
    Map<String, String> registro = tu.navegationReg(String.valueOf(idUsuarioActual), "PRIOR");
    return procesarRegistroNavegacion(registro);
}

@Override
public int imUltimo() {
    // Obtener el último usuario registrado.
    Map<String, String> registro = tu.navegationReg(null, "LAST");
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
                job.setJobName("Usuarios");
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
    public int imNuevo() {
       // Resetear los datos
    resetData(); // Resetea a valores por defecto
    // Actualizar la interfaz de usuario con los datos reseteados
    fillView();
    cargarUltimoId(); // Actualiza el último 
    idUsuarioActual = -1;
    
    return 0;

    }

@Override
public int imBuscar() {  
    buscarYRellenarDatos(); 

    return 0; // Retorno estándar si no hay selección o no se encontraron resultados
}

@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    System.out.println("Datos seleccionados recibidos: " + datosSeleccionados);

    String idStr = datosSeleccionados.get("Codigo"); // Asegúrate de que 'Codigo' sea la clave correcta.
    String nombre = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idUsuario = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tu.buscarPorIdGenerico("USUARIOS", "id", idUsuario);

            System.out.println("Registros obtenidos: " + registros);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0); // Asumimos que hay al menos un resultado

                // Asumiendo que registro contenga todas las claves como 'rol_id', 'activo', etc.
                final String rolId = registro.get("rol_id");
                final String activoStr = registro.get("activo");
                final boolean activo = "1".equals(activoStr);

                SwingUtilities.invokeLater(() -> {
                    txtId_usuario.setText(idStr);
                    txtUsuario.setText(nombre);
                    seleccionarRolEnComboBox(safeParseInt(rolId));
                    Activo.setSelected(activo);
                    idUsuarioActual=  Integer.parseInt(idStr); 
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un usuario con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de usuario inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void seleccionarRolEnComboBox(Integer idRol) {
    if (idRol != null) {
        for (int i = 0; i < rol.getItemCount(); i++) {
            if (rol.getItemAt(i).toString().startsWith(idRol + " - ")) {
                rol.setSelectedIndex(i);
                break;
            }
        }
    } else {
        rol.setSelectedIndex(0);  // Asumiendo que el índice 0 es "Seleccione un Rol"
    }
}


}


