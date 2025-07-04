/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Formularios;

import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import Filtros.DefaultFocusListener;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;


public class Form_BuscarTabla extends javax.swing.JDialog {

    /**
     * Creates new form Form_Buscar
     */
     private InterfaceUsuario listener; 
   // Listener para comunicarse con el formulario llamador
     private JTable jTable;
    private JScrollPane jScrollPane;
     private DBTableController controlador; // Aquí agregamos el controlador
    private String nombreTabla;
    private List<String> camposDeBusqueda; // Lista de nombres de columnas a mostrar en la búsqueda.
    private Map<String, String> registroSeleccionado = new HashMap<>();
    private List<String> columnasDeBusqueda;
    private InterfaceUsuario seleccionadoListener;

    public void setOnItemSeleccionadoListener2(InterfaceUsuario listener) {
    this.seleccionadoListener = listener;
    }

    public Form_BuscarTabla(Frame parent, boolean modal, DBTableController controlador, String nombreTabla, List<String> columnasDeBusqueda) {
        super(parent, modal);
        this.controlador = controlador;
        this.nombreTabla = nombreTabla;
        this.columnasDeBusqueda = columnasDeBusqueda; // Establece las columnas de búsqueda dinámicamente
        initComponents();
        postInitComponents();
        buscar.addFocusListener(new DefaultFocusListener(buscar,false));
        this.setLocationRelativeTo(null);   
    }

private void postInitComponents() {
    // Configuración del modelo de la tabla con columnas dinámicas
    configurarModeloTabla();

    // Añadir un DocumentListener al campo de texto buscar
    buscar.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void changedUpdate(DocumentEvent e) {
            filtrar();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filtrar();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            filtrar();
        }
    });

    tabla.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // Doble clic para la selección
                int fila = tabla.getSelectedRow();
                if (fila != -1) { // Comprueba que una fila está seleccionada
                    seleccionarItem(fila);
                }
            }
        }
    });

    // Añadir el WindowListener para manejar el cierre de la ventana
    this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            SwingUtilities.invokeLater(() -> buscar.setText(""));
        }
    });
}




private void configurarModeloTabla() {
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Hacer que todas las celdas no sean editables
            return false;
        }
    };

    // Configurar columnas iniciales si conoces los nombres de las columnas por adelantado
    // Si los nombres de las columnas son dinámicos, puedes configurarlos al realizar la búsqueda
    modelo.setColumnIdentifiers(new String[]{"Codigo", "Descripcion"});
    tabla.setModel(modelo);
}

private void filtrar() {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);

    String textoBusqueda = buscar.getText().trim();
    if (textoBusqueda.isEmpty()) {
        return;
    }

    List<Map<String, String>> resultadosDetalle = new ArrayList<>();
    List<Map<String, String>> resultadosProductos = new ArrayList<>();
    Set<String> resultadosUnicos = new HashSet<>();

    try {
        // Buscar en productos_detalle por codigobarras
        resultadosDetalle = controlador.buscarPorColumna("productos_detalle", Arrays.asList("codigobarras", "cabecera_id"), textoBusqueda);
        System.out.println("Resultados de productos_detalle: " + resultadosDetalle);

        // Buscar en productos por producto
        resultadosProductos = controlador.buscarPorColumna("productos", Arrays.asList("producto", "id"), textoBusqueda);
        System.out.println("Resultados de productos: " + resultadosProductos);

        // Agregar resultados de búsqueda inversa para codigobarras
        for (Map<String, String> detalle : resultadosDetalle) {
            String cabeceraId = detalle.get("cabecera_id");
            List<Map<String, String>> productosRelacionados = controlador.buscarPorColumna("productos", Arrays.asList("producto", "id"), cabeceraId);
            resultadosProductos.addAll(productosRelacionados);
        }

        // Agregar resultados de búsqueda inversa para productos
        for (Map<String, String> producto : resultadosProductos) {
            String productoId = producto.get("id");
            List<Map<String, String>> detallesRelacionados = controlador.buscarPorColumna("productos_detalle", Arrays.asList("codigobarras", "cabecera_id"), productoId);
            resultadosDetalle.addAll(detallesRelacionados);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al buscar en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mapear los productos por su ID
    Map<String, String> productosMap = new HashMap<>();
    for (Map<String, String> producto : resultadosProductos) {
        productosMap.put(producto.get("id"), producto.get("producto"));
    }

    // Combinar los resultados basados en la relación entre cabecera_id y id, evitando duplicados
    for (Map<String, String> detalle : resultadosDetalle) {
        String cabeceraId = detalle.get("cabecera_id");
        String codigobarras = detalle.get("codigobarras");
        String producto = productosMap.getOrDefault(cabeceraId, "N/A");

        if (!producto.equals("N/A")) {
            String resultadoUnico = codigobarras + "_" + producto;
            if (!resultadosUnicos.contains(resultadoUnico)) {
                resultadosUnicos.add(resultadoUnico);
                modelo.addRow(new Object[]{codigobarras, producto});
            }
        }
    }

    if (modelo.getRowCount() == 0) {
        if (this.isVisible()) {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados para su búsqueda.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> buscar.setText(""));
        }
    }
}


public void configurarBusqueda(String tabla, List<String> columnasDeBusqueda) {
    this.nombreTabla = tabla;
    this.columnasDeBusqueda = columnasDeBusqueda;
    System.out.println("Datos columnas : " + columnasDeBusqueda);
}





private void seleccionarItem(int fila) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    Map<String, String> datosSeleccionados = new HashMap<>();
    
    for (int i = 0; i < modelo.getColumnCount(); i++) {
        String nombreColumna = modelo.getColumnName(i);
        Object valorColumna = modelo.getValueAt(fila, i);
        datosSeleccionados.put(nombreColumna, valorColumna != null ? valorColumna.toString() : "N/A");
    }
     System.out.println("Datos seleccionados: " + datosSeleccionados);
    if (seleccionadoListener != null) {
        seleccionadoListener.onItemSeleccionado(datosSeleccionados);
    }

    // Cierra la ventana de búsqueda
    this.dispose();
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
        jLabel1 = new javax.swing.JLabel();
        buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Buscar");

        buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Código", "Descripción"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed

    }//GEN-LAST:event_buscarActionPerformed

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
