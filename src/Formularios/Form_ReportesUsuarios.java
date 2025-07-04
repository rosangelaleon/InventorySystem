/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;
import Formularios.Form_FiltroUsuario; 
import Controllers.InterfaceUsuario;
import java.sql.Connection;
import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Map;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRException;
import javax.swing.*;

public class Form_ReportesUsuarios extends javax.swing.JInternalFrame implements InterfaceUsuario {
   private Connection conexion;
    private String reportePath;
    private String titulo;
    private HashMap<String, Object> parametros;

    public Form_ReportesUsuarios(String reportePath, String titulo, Connection conexion, HashMap<String, Object> parametros) {
        initComponents();
        this.conexion = conexion;
        this.reportePath = reportePath;
        this.titulo = titulo;
        this.parametros = parametros;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpReporte = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Reportes de Usuarios");
        setMaximumSize(new java.awt.Dimension(1200, 500));
        setMinimumSize(new java.awt.Dimension(600, 400));
        setPreferredSize(new java.awt.Dimension(770, 450));
        setRequestFocusEnabled(false);

        jpReporte.setMaximumSize(new java.awt.Dimension(1200, 500));
        jpReporte.setMinimumSize(new java.awt.Dimension(800, 500));
        jpReporte.setPreferredSize(new java.awt.Dimension(800, 500));
        jpReporte.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 765, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void actualizarYGenerarReporte(HashMap<String, Object> nuevosParametros) {
    boolean sinFiltros = nuevosParametros.isEmpty() || nuevosParametros.values().stream().allMatch(Objects::isNull);
    
    // If no filters are applied, set the parameters to null to retrieve all records
    if (sinFiltros) {
        this.parametros.clear(); // Clear any existing parameters
    } else {
        this.parametros = nuevosParametros; // Update the parameters
    }
    
    generarReporte(); // Call the method to generate the report
}

public void generarReporte() {
    try {        
        JasperReport reporte = JasperCompileManager.compileReport(new File(reportePath).getAbsolutePath());
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);
        
        JRViewer viewer = new JRViewer(print);
        jpReporte.removeAll();
        jpReporte.add(viewer, BorderLayout.CENTER);
        jpReporte.revalidate();
        jpReporte.repaint();

        // Ajusta el tamaño del JInternalFrame al contenido del JRViewer
        
        this.setTitle(titulo);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.pack(); // Asegúrate de llamar a pack() después de añadir todos los componentes

    } catch (JRException e) {
        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpReporte;
    // End of variables declaration//GEN-END:variables

    @Override
    public int imGuardar(String crud) {
        JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
        return -1; 
    }

    @Override
    public int imBorrar(String crud) {
       JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
        return -1;
    }

    @Override
    public int imNuevo() {
               JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
        return -1;
    }

    @Override
    public int imBuscar() {
        JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
        return -1;
    }

    @Override
    public int imFiltrar() {
    Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
    Form_FiltroUsuario dialogoFiltro = new Form_FiltroUsuario(frame, true, this);
    dialogoFiltro.setVisible(true);
    return 0;
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
       JOptionPane.showMessageDialog(this, "Esta acción no se puede realizar");
        return -1;
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
