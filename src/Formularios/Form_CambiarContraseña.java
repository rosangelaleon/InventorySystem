
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;

public class Form_CambiarContraseña extends javax.swing.JInternalFrame implements InterfaceUsuario {

   private DBTableController tc;
   private Map<String, String> mapData;
   private static int usuarioId; 
    public Form_CambiarContraseña(int usuarioId) {
        initComponents();
        this.usuarioId = usuarioId; // Configurar el ID de usuario
        tc = new DBTableController();
        tc.iniciar("USUARIOS");
        mapData = new HashMap<>();
        
    }
    
private boolean esContrasenaFuerte(String contrasena) {
    // Mínimo ocho caracteres, al menos una letra mayúscula, una letra minúscula, un número y un carácter especial
    return contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
}


private void addFocusListeners() {
        ContrasenaActual.addFocusListener(new DefaultFocusListener(ContrasenaActual,false));
        ContrasenaNueva.addFocusListener(new DefaultFocusListener(ContrasenaNueva,false));
        ContrasenaRepetir.addFocusListener(new DefaultFocusListener(ContrasenaRepetir,false));
    }
      
    private void SetMapData() {
        mapData.put("id", String.valueOf(usuarioId)); // ID de usuario se configura al abrir el formulario
        mapData.put("contraseña_actual", new String(ContrasenaActual.getPassword()));
        mapData.put("contraseña_nueva", new String(ContrasenaNueva.getPassword()));
    }

    private void resetData() {
        mapData.clear(); // Limpia el mapa por completo
        ContrasenaActual.setText("");
        ContrasenaNueva.setText("");
        ContrasenaRepetir.setText("");
    }

    private void fillView(Map<String, String> data) {
        ContrasenaActual.setText(data.getOrDefault("contraseña_actual", ""));
        ContrasenaNueva.setText(data.getOrDefault("contraseña_nueva", ""));
        ContrasenaRepetir.setText(data.getOrDefault("contraseña_nueva", ""));
    }
    
   public boolean validarContraseña(String contraseñaIngresada, String hashAlmacenado) {
    if (contraseñaIngresada == null || hashAlmacenado == null) {
        return false;
    }
    try {
        return BCrypt.checkpw(contraseñaIngresada, hashAlmacenado);
    } catch (IllegalArgumentException e) {
        System.err.println("Error de argumento ilegal: " + e.getMessage());
        return false;
    }
}
   
   private String obtenerHashContraseñaActual(int userId) {
    // Supongamos que el controlador tiene un método que permite buscar un registro específico por ID
    Map<String, String> searchCriteria = new HashMap<>();
    searchCriteria.put("id", String.valueOf(userId));
    searchCriteria.put("contraseña", "contraseña");
    
    // Asumimos que 'contraseña' es el nombre de la columna en la base de datos que contiene el hash de la contraseña
    Map<String, String> resultado = tc.searchById(searchCriteria);
    
    if (resultado != null && resultado.containsKey("contraseña")) {
        return resultado.get("contraseña");
    } else {
        // Maneja el caso en que no se encuentra el registro o la conexión a la base de datos falló
        System.err.println("No se pudo recuperar el hash de la contraseña para el usuario con ID: " + userId);
        return null;
    }
}

    private boolean actualizarContraseñaUsuario(String contraseñaHashed) {
        Map<String, String> datosActualizacion = new HashMap<>();
        ArrayList<Map<String, String>> listaParaActualizar = new ArrayList<>();
        datosActualizacion.put("id", Integer.toString(this.usuarioId));
        datosActualizacion.put("contraseña", contraseñaHashed);
        listaParaActualizar.add(datosActualizacion); 
        return tc.updateReg(listaParaActualizar) > 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Lbl_ContActual = new javax.swing.JLabel();
        ContrasenaActual = new javax.swing.JPasswordField();
        lbl_ContRepetir = new javax.swing.JLabel();
        lbl_ContNueva = new javax.swing.JLabel();
        ContrasenaNueva = new javax.swing.JPasswordField();
        ContrasenaRepetir = new javax.swing.JPasswordField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cambiar Contraseña");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Lbl_ContActual.setText("Contraseña Actual");

        lbl_ContRepetir.setText("Repetir Contraseña Nueva");

        lbl_ContNueva.setText("Nueva Contraseña");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_ContNueva)
                            .addComponent(Lbl_ContActual))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ContrasenaNueva)
                            .addComponent(ContrasenaActual)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_ContRepetir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ContrasenaRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Lbl_ContActual)
                    .addComponent(ContrasenaActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ContrasenaNueva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ContNueva))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ContRepetir)
                    .addComponent(ContrasenaRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField ContrasenaActual;
    private javax.swing.JPasswordField ContrasenaNueva;
    private javax.swing.JPasswordField ContrasenaRepetir;
    private javax.swing.JLabel Lbl_ContActual;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_ContNueva;
    private javax.swing.JLabel lbl_ContRepetir;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    String contraseñaActual = new String(ContrasenaActual.getPassword()).trim();
    String nuevaContraseña = new String(ContrasenaNueva.getPassword()).trim();
    String repetirContraseña = new String(ContrasenaRepetir.getPassword()).trim();

    if (contraseñaActual.isEmpty() || nuevaContraseña.isEmpty() || repetirContraseña.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    if (!nuevaContraseña.equals(repetirContraseña)) {
        JOptionPane.showMessageDialog(this, "Las  contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    if (!esContrasenaFuerte(nuevaContraseña)) {
        JOptionPane.showMessageDialog(this, "La nueva contraseña debe tener al menos 8 caracteres y contener una mezcla de mayúsculas, minúsculas, números y símbolos especiales.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificación de la contraseña actual con la base de datos
    String hashAlmacenado = obtenerHashContraseñaActual(usuarioId);
    if (hashAlmacenado == null || !BCrypt.checkpw(contraseñaActual, hashAlmacenado)) {
        JOptionPane.showMessageDialog(this, "La contraseña actual no es correcta.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    if (BCrypt.checkpw(nuevaContraseña, hashAlmacenado)) {
        JOptionPane.showMessageDialog(this, "La nueva contraseña no puede ser igual a la actual.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    String contraseñaHashed = BCrypt.hashpw(nuevaContraseña, BCrypt.gensalt());
    if (actualizarContraseñaUsuario(contraseñaHashed)) {
        JOptionPane.showMessageDialog(this, "Contraseña actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        resetData();
        return 1;
    } else {
        JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}


    @Override
    public int imBorrar(String crud) {
    JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return 0;
    }

    @Override
    public int imNuevo() {
    // Resetear los datos
    resetData(); // Resetea a valores por defecto
    // Actualizar la interfaz de usuario con los datos reseteados
    fillView(mapData);

    return 0; // Indica éxito  
    }

    @Override
    public int imBuscar() {
          JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
     return -1;
    }

    @Override
    public int imFiltrar() {
       JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return -1;
    }

    @Override
    public int imPrimero() {
       JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return -1;
    }

    @Override
    public int imSiguiente() {
        JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return -1;
    }

    @Override
    public int imAnterior() {
       JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return -1;
    }

    @Override
    public int imUltimo() {
      JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
       return -1;
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
                job.setJobName("Contraseña");
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
