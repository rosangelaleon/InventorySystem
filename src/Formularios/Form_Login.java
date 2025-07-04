
package Formularios;
import Controllers.DBTableController;
import Controllers.DBConexion;
import Controllers.DBTableModel;
import Filtros.DefaultFocusListener;
import Filtros.TextFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
public class Form_Login extends javax.swing.JFrame {

// Variables de clase para manejar los intentos de inicio de sesión
   private int intentosLogin = 0;
    private static final int MAX_INTENTOS = 2;
    public Form_Login() {
        initComponents();
        applyAlphaFilter(Usuario);
                // Agregar el listener a tus campos de texto
        Usuario.addFocusListener(new DefaultFocusListener(Usuario,false));
        Contraseña.addFocusListener(new DefaultFocusListener(Contraseña,false));
        this.setLocationRelativeTo(null);
        
    // Listener para la tecla Enter
    KeyListener enterKey = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btn_ingresarActionPerformed(null);
            }
        }
    };

    Usuario.addKeyListener(enterKey);
    Contraseña.addKeyListener(enterKey);
    }

   private void limpiarCampos() {
    Usuario.setText(""); // Limpia el campo de texto del usuario
    Contraseña.setText(""); // Limpia el campo de contraseña
}
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Label_Usuario = new javax.swing.JLabel();
        Label_Contraseña = new javax.swing.JLabel();
        btn_ingresar = new javax.swing.JButton();
        Usuario = new javax.swing.JTextField();
        nombre_Sistema = new javax.swing.JLabel();
        Logo = new javax.swing.JLabel();
        Contraseña = new javax.swing.JPasswordField();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inicio de Sesión");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMaximumSize(new java.awt.Dimension(500, 500));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 550));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Label_Usuario.setBackground(new java.awt.Color(255, 255, 255));
        Label_Usuario.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        Label_Usuario.setText("Usuario:");
        jPanel1.add(Label_Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 80, -1));

        Label_Contraseña.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        Label_Contraseña.setText("Contraseña:");
        jPanel1.add(Label_Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 110, -1));

        btn_ingresar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_ingresar.setText("Iniciar Sesión");
        btn_ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ingresarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_ingresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 390, 151, 42));

        Usuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Usuario.setMinimumSize(new java.awt.Dimension(70, 30));
        Usuario.setPreferredSize(new java.awt.Dimension(74, 30));
        Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 180, -1));

        nombre_Sistema.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        nombre_Sistema.setText("INVENTORY SYSTEM");
        jPanel1.add(nombre_Sistema, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, -1, -1));

        Logo.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/LOGOTIPO_LOGIN.png"))); // NOI18N
        jPanel1.add(Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        Contraseña.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        Contraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContraseñaActionPerformed(evt);
            }
        });
        jPanel1.add(Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 180, 30));

        fondo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FONDO_LOGO.png"))); // NOI18N
        jPanel1.add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        private void applyAlphaFilter(JTextField textField) {
        javax.swing.text.Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new TextFilter());
        }
    }
    private void UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsuarioActionPerformed


    private void btn_ingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ingresarActionPerformed
                                          
    String usuario = Usuario.getText();
    String contraseña = new String(Contraseña.getPassword());

    if (usuario.isEmpty() || contraseña.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Los campos no pueden quedar vacíos.");
        limpiarCampos();
        return;
    }

    int userId = verificarCredenciales(usuario, contraseña);
    if (userId > 0) {  // Check if a valid ID was returned
        String rol = obtenerRolUsuario(usuario);
        intentosLogin = 0;
        Form_MenuPrincipal menuPrincipal = new Form_MenuPrincipal(userId, usuario, rol);
        menuPrincipal.setVisible(true);
        this.dispose();
    } else if (intentosLogin >= MAX_INTENTOS) {
        JOptionPane.showMessageDialog(this, "Se han superado el número de intentos de inicio de sesión. Inténtelo más tarde.");
        this.dispose();
    } else {
        intentosLogin++;
        if (intentosLogin <= MAX_INTENTOS) {
            limpiarCampos();
        }
    }

    }//GEN-LAST:event_btn_ingresarActionPerformed

private int verificarCredenciales(String usuario, String contraseña) {
    try {
        if (!DBConexion.Conectar()) {
            JOptionPane.showMessageDialog(this, "No se pudo establecer la conexión con la base de datos.");
            return -1;
        }

        DBTableModel mUser = new DBTableModel();
        mUser.iniciar("usuarios");
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("usuario", usuario);

        Map<String, String> fieldsToRetrieve = new HashMap<>();
        fieldsToRetrieve.put("id", "");
        fieldsToRetrieve.put("contraseña", "");
        fieldsToRetrieve.put("activo", ""); // Agregar el campo "activo"

        Map<String, String> userData = mUser.readRegisterById(fieldsToRetrieve, searchCriteria);

        if (!userData.isEmpty() && BCrypt.checkpw(contraseña, userData.get("contraseña"))) {
            // Verificar si el usuario está activo
            if (userData.get("activo").equals("1")) {
                return Integer.parseInt(userData.get("id"));
            } else {
                JOptionPane.showMessageDialog(this, "Su cuenta de usuario está desactivada. Contacte al administrador.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
        return -1;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        return -1;
    }
}

private String obtenerRolUsuario(String usuario) {
    DBTableController dbTableController = new DBTableController();
    dbTableController.iniciar("usuarios"); // Iniciar con la tabla de usuarios.
    
    // Realizar una consulta SQL de unión para obtener el rol del usuario.
    String sql = "SELECT r.rol FROM USUARIOS u JOIN ROLES r ON u.rol_id = r.id WHERE u.usuario = '" + usuario + "'";
    ResultSet resultSet = DBConexion.ejecuteSQL(sql);

    try {
        if (resultSet != null && resultSet.next()) {
            return resultSet.getString("rol");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    private void ContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ContraseñaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form_Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField Contraseña;
    private javax.swing.JLabel Label_Contraseña;
    private javax.swing.JLabel Label_Usuario;
    private javax.swing.JLabel Logo;
    private javax.swing.JTextField Usuario;
    private javax.swing.JButton btn_ingresar;
    private javax.swing.JLabel fondo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nombre_Sistema;
    // End of variables declaration//GEN-END:variables
}
