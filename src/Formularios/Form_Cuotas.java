
package Formularios;

import Controllers.DBTableController;
import Controllers.DBTableModel;
import Controllers.InterfaceUsuario;
import Filtros.AlphaNumericDocumentFilter;
import Filtros.DefaultFocusListener;
import Filtros.DescriptionFilter;
import Filtros.NumericDocumentFilter;
import Filtros.NumericKeyListener;
import Filtros.TextFilter;
import Modelo.CuotaDetalle;
import Modelo.GestionCeldas;
import Modelo.GestionEncabezadoTabla;
import Modelo.ModeloTabla;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.PlainDocument;
import javax.swing.*;

public class Form_Cuotas extends javax.swing.JInternalFrame implements MouseListener, KeyListener, InterfaceUsuario {
private Map<String, String> myData;
private HashMap<String, String> myDet;
private DBTableController tc;
private DBTableController tcdet;

ArrayList<CuotaDetalle> lista; // = new ArrayList<>();

ArrayList<CuotaDetalle> listaDetalles; // lista que simula la información de la BD
ModeloTabla modelo; // modelo definido en la clase ModeloTabla
private int filasTabla;
private int columnasTabla;
public static int filaSeleccionada;

private ArrayList<Map<String,String>> columnData, colDat;

private DBTableModel tMCuota;
Map<String, String> mapCuotas; // = new HashMap<String, String>();

private DBTableModel tmCuotaDet;
Map<String, String> mapCuotaDet;
    private boolean isSavingOrUpdating = false;
private Map<String, List<Map<String, String>>> originalDataMap = new HashMap<>();
private boolean firstToggle = true; // Indica si es la primera vez que se selecciona "irregular"


public Form_Cuotas() {
    initComponents();
    IdCuota.setText("0");
    listaDetalles = new ArrayList<CuotaDetalle>();
    lista = new ArrayList<>();
    myData = new HashMap<String, String>();
    columnData = new ArrayList<Map<String, String>>();
    colDat = new ArrayList<Map<String, String>>();

    tc = new DBTableController();
    tc.iniciar("CUOTAS");
    tcdet = new DBTableController();
    tcdet.iniciar("CUOTAS_DETALLE");

    cargarUltimoId();
    construirTabla();
    initializeTextFields();

    jtDetalle.addMouseListener(this);
    jtDetalle.addKeyListener(this);

irregular.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (irregular.isSelected()) {
            jtDetalle.setVisible(true);
            jScrollPane2.setVisible(true); // Asegurarse de mostrar el JScrollPane que contiene la tabla
        } else {
            mostrarValoresPredeterminados();
            jtDetalle.setVisible(false);
            jScrollPane2.setVisible(false); // Asegurarse de ocultar el JScrollPane que contiene la tabla
        }
    }
});

    // Inicialmente, la tabla y el JScrollPane no deben ser visibles si "irregular" no está seleccionado
    jtDetalle.setVisible(false);
    jScrollPane2.setVisible(false);

    Cuotas.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }
        public void removeUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }
        public void insertUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }

        private void actualizarTablaCuotas() {
            String text = Cuotas.getText();
            if (!text.isEmpty()) {
                try {
                    int numeroCuotas = Integer.parseInt(text);
                    agregarCuotas(numeroCuotas);
                } catch (NumberFormatException ex) {
                    // Manejar error de formato de número
                }
            }
        }
    });
}

private void mostrarValoresPredeterminados() {
    String text = Cuotas.getText();
    if (!text.isEmpty()) {
        try {
            int numeroCuotas = Integer.parseInt(text);
            agregarCuotas(numeroCuotas);
        } catch (NumberFormatException ex) {
            // Manejar error de formato de número
        }
    }
}

// Método para agregar cuotas a la tabla
private void agregarCuotas(int numeroCuotas) {
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    model.setRowCount(0); // Limpiar la tabla antes de agregar nuevas cuotas

    for (int i = 1; i <= numeroCuotas; i++) {
        int dias = i * 30;
        model.addRow(new Object[]{i, dias});
    }
}


// Método para aplicar el filtro numérico al JTable
private void applyNumericFilterToTable() {
    JTextField numericField = new JTextField();
    ((AbstractDocument) numericField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    
    jtDetalle.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(numericField));
}



private void construirTabla() {
    ArrayList<String> titulosList = new ArrayList<>();
    titulosList.add("Nro de Cuota");
    titulosList.add("Días");

    String titulos[] = new String[titulosList.size()];
    for (int i = 0; i < titulos.length; i++) {
        titulos[i] = titulosList.get(i);
    }

    Object[][] data = new Object[0][titulosList.size()]; // No agregar filas iniciales
    construirTabla(titulos, data);
}



private ArrayList<CuotaDetalle> consultarListaDetalles() {
    lista.add(new CuotaDetalle(0, 0, 0, 0));
    return lista;
}

private Object[][] obtenerMatrizDatos(ArrayList<String> titulosList) {
    String informacion[][] = new String[listaDetalles.size()][titulosList.size()];

    for (int x = 0; x < informacion.length; x++) {
        informacion[x][0] = String.valueOf(listaDetalles.get(x).getCuota());
        informacion[x][1] = String.valueOf(listaDetalles.get(x).getDias());
    }
    return informacion;
}

private void construirTabla(String[] titulos, Object[][] data) {
    DefaultTableModel model = new DefaultTableModel(data, titulos) {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Solo permitir la edición de la columna "Días" (índice 1)
            return column == 1;
        }
    };
    jtDetalle.setModel(model);

    filasTabla = jtDetalle.getRowCount();
    columnasTabla = jtDetalle.getColumnCount();

    // Asignar renderers y configurar columnas
    jtDetalle.getColumnModel().getColumn(0).setCellRenderer(new GestionCeldas("numerico"));
    jtDetalle.getColumnModel().getColumn(1).setCellRenderer(new GestionCeldas("numerico"));

    // Aplicar el filtro numérico a la columna "Días"
    applyNumericFilterToTable();

    jtDetalle.getTableHeader().setReorderingAllowed(false);
    jtDetalle.setRowHeight(25);
    jtDetalle.setGridColor(new java.awt.Color(0, 0, 0));

    // Ajustar el ancho de las columnas visibles
    jtDetalle.getColumnModel().getColumn(0).setPreferredWidth(200); // Nro de Cuota
    jtDetalle.getColumnModel().getColumn(1).setPreferredWidth(200); // Días

    // Personalizar el encabezado
    JTableHeader jtableHeader = jtDetalle.getTableHeader();
    jtableHeader.setDefaultRenderer(new GestionEncabezadoTabla());
    jtDetalle.setTableHeader(jtableHeader);
}

private void setData() {
    // Cabecera
    myData.put("id", IdCuota.getText());
    myData.put("descripcion", Descripcion.getText() == null ? "" : Descripcion.getText());
    myData.put("cuotas", Cuotas.getText() == null ? "" : Cuotas.getText());
    myData.put("irregular", irregular.isSelected() ? "1" : "0");
    myData.put("activo", activo.isSelected() ? "1" : "0");

    // Obtener el último ID existente en la tabla CUOTAS_DETALLE
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int filasTabla = model.getRowCount();
    int columnasTabla = model.getColumnCount();
    columnData.clear(); // Limpiar los datos anteriores

    for (int i = 0; i < filasTabla; i++) {
        Map<String, String> rowData = new HashMap<>();
        // Obtener el ID actual de la fila si existe
        String idDetalle = null;
        if (columnasTabla > 2) { // Asegurarse de que hay al menos 3 columnas
            Object idCell = model.getValueAt(i, 2); // Suponiendo que el ID está en la columna índice 2
            if (idCell == null || idCell.toString().trim().isEmpty()) {
                // Generar un nuevo ID si no existe
                int ultimoId = obtenerUltimoIdCuotaDetalle(); // Método para obtener el último ID
                idDetalle = String.valueOf(ultimoId + 1);
            } else {
                // Usar el ID existente
                idDetalle = idCell.toString().trim();
            }
        }

        if (idDetalle != null) {
            rowData.put("id", idDetalle); // Agregar el ID a los datos de la fila
        }

        // Obtener los demás valores de la fila y agregarlos a rowData
        if (columnasTabla > 0) {
            rowData.put("cuota", model.getValueAt(i, 0).toString());
        }
        if (columnasTabla > 1) {
            rowData.put("dias", model.getValueAt(i, 1).toString());
        }

        this.columnData.add(rowData);
    }

    // Imprimir los valores de columnData para verificación
    System.out.println("Valores de columnData después de llenar:");
    for (Map<String, String> myRow : columnData) {
        System.out.println(myRow);
    }
}



private int obtenerUltimoIdCuotaDetalle() {
    int ultimoId = 0;
    try {
        ultimoId = tcdet.getMaxId();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return ultimoId;
}

private void resetData() {        
    this.myData = new HashMap<String, String>();
    this.myData.put("id", "0");
    this.myData.put("descripcion", "");
    this.myData.put("cuotas", "0");
    this.myData.put("irregular", "0");
    this.myData.put("activo", "0");

    // Limpiar los comboboxes (si tuvieras alguno para cuotas)
    // Ejemplo: combobox.setSelectedIndex(0);

    // Detalle
    this.myDet = new HashMap<String, String>();
    this.myDet.put("id", "0");
    this.myDet.put("cuota_id", "0");
    this.myDet.put("cuota", "0");
    this.myDet.put("dias", "0");

    this.columnData.add(this.myDet);
            
    fillView(myData, columnData);
}
private void fillView(Map<String, String> data, List<Map<String, String>> colData) {
    // Actualizar los campos de entrada de la cabecera
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (value == null) {
            value = ""; // Manejar valores nulos
        }
        switch (key) {
            case "id":
                IdCuota.setText(value);
                break;
            case "descripcion":
                Descripcion.setText(value);
                break;
            case "cuotas":
                Cuotas.setText(value);
                break;
            case "irregular":
                irregular.setSelected(Integer.parseInt(value) != 0);
                break;
            case "activo":
                activo.setSelected(Integer.parseInt(value) != 0);
                break;
        }
    }

    // Mostrar u ocultar la tabla según la selección de "irregular"
    boolean esIrregular = irregular.isSelected();
    jtDetalle.setVisible(esIrregular);
    jScrollPane2.setVisible(esIrregular);

    // Limpiar la tabla de detalles antes de llenarla
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    model.setRowCount(0); // Limpiar filas existentes

    // Llenar la tabla de detalles con los datos proporcionados
    for (Map<String, String> myRow : colData) {
        model.addRow(new Object[]{
            myRow.get("cuota"),
            myRow.get("dias")
        });
    }
}

private void cargarDetallesCuota(String cuotaId) {
    Map<String, String> where = new HashMap<>();
    where.put("cuota_id", cuotaId);

    Map<String, String> fieldsToSelect = new HashMap<>();
    fieldsToSelect.put("*", "*");
    List<Map<String, String>> resultList = this.tcdet.searchListById(fieldsToSelect, where);

    System.out.println("Datos de cuotas_detalle para cuota_id " + cuotaId + ": " + resultList);

    this.columnData = new ArrayList<>(resultList);
}


public void limpiarTabla() {
    this.columnData.clear();
    try {
        DefaultTableModel modelo = (DefaultTableModel) jtDetalle.getModel();
        int filas = jtDetalle.getRowCount();
        for (int i = 0; filas > i; i++) {
            modelo.removeRow(0);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
    }
}

private void initializeTextFields() {
    applyNumericFilter(IdCuota);
    applyNumericFilter(Cuotas);
    applyAlphaNumericFilter(Descripcion); // Cambia este método para aplicar el filtro alfanumérico
    addFocusListeners();

    Cuotas.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }
        public void removeUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }
        public void insertUpdate(DocumentEvent e) {
            actualizarTablaCuotas();
        }

        private void actualizarTablaCuotas() {
            String text = Cuotas.getText();
            if (!text.isEmpty()) {
                try {
                    int numeroCuotas = Integer.parseInt(text);
                    agregarCuotas(numeroCuotas);
                } catch (NumberFormatException ex) {
                    // Manejar error de formato de número
                }
            }
        }
    });
}



private void addFocusListeners() {
    IdCuota.addFocusListener(new DefaultFocusListener(IdCuota, true));
    Descripcion.addFocusListener(new DefaultFocusListener(Descripcion, false));
    Cuotas.addFocusListener(new DefaultFocusListener(Cuotas, true));
}

private void applyNumericFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
}

private void applyAlphaNumericFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new AlphaNumericDocumentFilter());
}
private void cargarUltimoId() {
    try {
        int ultimoId = tc.getMaxId();
        Ultimo.setText(String.valueOf(ultimoId));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo cargar el último ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void limpiarCelda(JTable tabla) {
    try {
        tabla.setValueAt("", tabla.getSelectedRow(), tabla.getSelectedColumn());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "No se pudo limpiar la celda.", "Error", JOptionPane.ERROR_MESSAGE);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        IdCuota = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_Ultimo = new javax.swing.JLabel();
        Ultimo = new javax.swing.JLabel();
        irregular = new javax.swing.JCheckBox();
        lbl_descripcion = new javax.swing.JLabel();
        Descripcion = new javax.swing.JTextField();
        lbl_Cuotas = new javax.swing.JLabel();
        Cuotas = new javax.swing.JTextField();
        activo = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtDetalle = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setIconifiable(true);
        setTitle("Cuotas");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        IdCuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IdCuotaKeyPressed(evt);
            }
        });
        jPanel1.add(IdCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 60, -1));

        lbl_id.setText("Id");
        jPanel1.add(lbl_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        lbl_Ultimo.setText("Último");
        jPanel1.add(lbl_Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        Ultimo.setBackground(new java.awt.Color(204, 204, 255));
        Ultimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ultimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ultimo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Ultimo.setOpaque(true);
        jPanel1.add(Ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 50, 20));

        irregular.setText("Irregular");
        jPanel1.add(irregular, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, -1));

        lbl_descripcion.setText("Descripción");
        jPanel1.add(lbl_descripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        jPanel1.add(Descripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 180, -1));

        lbl_Cuotas.setText("  Cuotas");
        jPanel1.add(lbl_Cuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 50, 20));
        jPanel1.add(Cuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 80, -1));

        activo.setText("Activo");
        jPanel1.add(activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, -1, -1));

        jtDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nro de Cuota", "Días"
            }
        ));
        jScrollPane2.setViewportView(jtDetalle);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 390, 140));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("Cuotas");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdCuotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IdCuotaKeyPressed
              if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.imBuscar();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_IdCuotaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Cuotas;
    private javax.swing.JTextField Descripcion;
    private javax.swing.JTextField IdCuota;
    private javax.swing.JLabel Ultimo;
    private javax.swing.JCheckBox activo;
    private javax.swing.JCheckBox irregular;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jtDetalle;
    private javax.swing.JLabel lbl_Cuotas;
    private javax.swing.JLabel lbl_Ultimo;
    private javax.swing.JLabel lbl_descripcion;
    private javax.swing.JLabel lbl_id;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = jtDetalle.rowAtPoint(e.getPoint());
        int columna = jtDetalle.columnAtPoint(e.getPoint());
        if (columna == 0 || columna == 5 || columna == 6 || columna == 7) {
            jtDetalle.setColumnSelectionInterval(columna, columna);
            jtDetalle.editCellAt(fila, columna);
        }
    }

     @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
public void mouseEntered(MouseEvent e) {
    // Capturo fila o columna dependiendo de mi necesidad
    // OBS: Aquí debemos llamar a un método que controle que los campos de la cabecera estén completos
    int fila = jtDetalle.rowAtPoint(e.getPoint());
    int columna = jtDetalle.columnAtPoint(e.getPoint());

    /* Uso la columna para validar si corresponde a la columna de perfil garantizando
     * que solo se produzca algo si selecciono una fila de esa columna
     */
    if (columna == 0) { // 0 corresponde a Nro de Cuota
        // Sabiendo que corresponde a la columna de perfil, envío la posición de la fila seleccionada
        // validarSeleccionMouse(fila);
    } else if (columna == 2) { // Se valida que sea la columna del otro evento 2 que corresponde a días
        // JOptionPane.showMessageDialog(null, "Evento del otro icono");
    }
}

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
@Override
public void keyPressed(KeyEvent e) {
    int row = jtDetalle.getSelectedRow();
    int rows = jtDetalle.getRowCount();
    int col = jtDetalle.getSelectedColumn();

    int key = e.getKeyChar();

    boolean numeros = key >= 48 && key <= 57;
    boolean erraser = key == 8;

    if (!numeros && !erraser && key != 10 && key != 9) {
        e.consume(); // Consume el evento si no es un número, tecla de borrado, Enter o Tab
    } else {
        if (numeros) {
            if (jtDetalle.getModel().isCellEditable(row, col)) {
                this.limpiarCelda(jtDetalle);
            }
        }
    }

    if (key == 10 || key == 9 || (key >= 37 && key <= 40)) { // 10 es Enter, 9 es Tab
        if (jtDetalle.isEditing()) {
            jtDetalle.getCellEditor().stopCellEditing();
        }

        if (col == 0) {
            return;
        }

        if (col == 1 && key == 10 && (row == (rows - 1))) { // Si está en la última columna y presiona Enter, inserta una nueva fila
            Map<String, String> rowData = new HashMap<>();
            rowData.put("cuota", this.jtDetalle.getValueAt(row, 0).toString());
            rowData.put("dias", this.jtDetalle.getValueAt(row, 1).toString());

            if (isRowInvalid(rowData)) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los detalles de la cuota correctamente antes de añadir una nueva fila.", "¡A T E N C I O N!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.imInsFilas();
            }
        }
    }
}

// Método auxiliar para verificar si la fila es válida
private boolean isRowInvalid(Map<String, String> rowData) {
    return rowData.get("cuota").isEmpty() || rowData.get("dias").isEmpty();
}

   @Override
   public void keyReleased(KeyEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
@Override
public int imGuardar(String crud) {
    isSavingOrUpdating = true;
    setData(); // Configurar los datos desde el formulario

    // Validación de campos obligatorios de la cabecera
    List<String> columnasObligatorias = Arrays.asList("descripcion", "cuotas", "irregular", "activo");
    for (String columna : columnasObligatorias) {
        if (!myData.containsKey(columna) || myData.get(columna) == null || myData.get(columna).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos obligatorios de la cabecera no pueden quedar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            return -1;
        }
    }

    int idCabecera = Integer.parseInt(myData.getOrDefault("id", "0"));
    if (idCabecera == 0 || myData.get("id") == null || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID de la cabecera no puede quedar vacío o ser cero.", "Error", JOptionPane.ERROR_MESSAGE);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    }

    if (columnData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe haber al menos un detalle de cuota.", "Error", JOptionPane.ERROR_MESSAGE);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    }

    // Validación de campos obligatorios de los detalles
    List<String> columnasObli = Arrays.asList("cuota", "dias");
    boolean detalleInvalido = false;
    for (Map<String, String> myRow : columnData) {
        for (String columna : columnasObli) {
            String valor = myRow.get(columna);
            if (valor == null || valor.isEmpty() || Integer.parseInt(valor) == 0) {
                detalleInvalido = true;
                break;
            }
        }
        if (detalleInvalido) {
            JOptionPane.showMessageDialog(this, "Todos los detalles de cuotas deben tener campos obligatorios válidos (no vacíos y no ceros).", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            return -1;
        }
    }

    // Validación de números de cuota duplicados
    Set<String> numerosCuota = new HashSet<>();
    for (Map<String, String> myRow : columnData) {
        String numeroCuota = myRow.get("cuota");
        if (numerosCuota.contains(numeroCuota)) {
            JOptionPane.showMessageDialog(this, "El número de cuota " + numeroCuota + " está duplicado en los detalles.", "Error", JOptionPane.ERROR_MESSAGE);
            isSavingOrUpdating = false;
            imNuevo();
            return -1;
        }
        numerosCuota.add(numeroCuota);
    }

    if (!guardarCabecera(idCabecera)) {
        System.out.println("Fallo al guardar la cabecera de cuotas con ID: " + idCabecera);
        isSavingOrUpdating = false;
        imNuevo();
        return -1;
    } else {
        System.out.println("Cabecera de cuotas guardada exitosamente con ID: " + idCabecera);
    }

    // Guardar detalles
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("cuota_id", String.valueOf(idCabecera));
    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);

    Map<String, Map<String, String>> detallesExistentesMap = new HashMap<>();
    for (Map<String, String> detalle : detallesExistentes) {
        detallesExistentesMap.put(detalle.get("cuota"), detalle);
    }

    for (Map<String, String> myRow : columnData) {
        myRow.put("cuota_id", String.valueOf(idCabecera));
        String numeroCuota = myRow.get("cuota");

        if (detallesExistentesMap.containsKey(numeroCuota)) {
            // Actualizar el detalle existente
            Map<String, String> existingDetail = detallesExistentesMap.get(numeroCuota);
            String detalleId = existingDetail.get("id");
            myRow.put("id", detalleId);
            detallesExistentesMap.remove(numeroCuota);
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(myRow);
            tcdet.updateReg(alDetalle);
            System.out.println("Detalle de cuota actualizado: " + myRow);
        } else {
            // Insertar un nuevo detalle
            int newDetalleId = obtenerUltimoIdCuotaDetalle() + 1;
            myRow.put("id", String.valueOf(newDetalleId));
            tcdet.createReg(myRow);
            System.out.println("Detalle de cuota creado: " + myRow);
        }
    }

    // Eliminar detalles que ya no existen
    for (Map.Entry<String, Map<String, String>> entry : detallesExistentesMap.entrySet()) {
        ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
        alDetalle.add(entry.getValue());
        tcdet.deleteReg(alDetalle);
        System.out.println("Detalle de cuota eliminado: " + entry.getValue());
    }

    JOptionPane.showMessageDialog(this, "Registro de cuotas guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    isSavingOrUpdating = false;
    imNuevo();
    return 0;
}


private boolean guardarCabecera(int idCabecera) {
    ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
    alCabecera.add(processFields(myData)); // Procesa los campos antes de guardarlos

    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", String.valueOf(idCabecera));

    if (tc.searchListById(fields, where).isEmpty()) {
        // Crear nuevo registro si no existe
        int rows = tc.createReg(processFields(myData)); 
        if (rows < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar crear el registro de cuotas.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        // Actualizar registro existente
        int rowsAffected = tc.updateReg(alCabecera);
        if (rowsAffected < 1) {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar el registro de cuotas: " + idCabecera, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    return true;
}


private Map<String, String> processFields(Map<String, String> data) {
    Map<String, String> processedData = new HashMap<>();
    for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        switch (key) {
            case "id":
            case "cuotas":
                // Campos numéricos, mantener el valor tal cual
                processedData.put(key, value);
                break;
            case "irregular":
            case "activo":
                // Campos booleanos, convertir a 1 o 0
                processedData.put(key, "1".equals(value) ? "1" : "0");
                break;
            case "descripcion":
            default:
                // Para descripción y cualquier otro campo de texto, 
                // solo escapar comillas simples sin añadir comillas adicionales
                processedData.put(key, value != null ? value.replace("'", "''") : null);
                break;
        }
    }
    return processedData;
}





 @Override
public int imBorrar(String crud) {
    setData(); // Actualiza los datos de la vista

    // Verificar que las columnas obligatorias de la cabecera no estén vacías
    List<String> columnasObligatoriasCabecera = Arrays.asList("descripcion", "cuotas", "irregular", "activo");
    for (String columna : columnasObligatoriasCabecera) {
        if (myData.get(columna).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las columnas obligatorias de la cabecera no pueden quedar vacías.", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }

    // Verificar campo obligatorio ID para la cabecera
    if (myData.get("id").equals("0") || myData.get("id").isEmpty()) {
        JOptionPane.showMessageDialog(this, "El campo ID de la cabecera no puede quedar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar que haya al menos un detalle
    if (columnData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se puede eliminar una cuota sin detalles.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
    
    // Verificar que todos los detalles tengan campos obligatorios válidos (no vacíos ni cero)
    List<String> columnasObli = Arrays.asList("cuota", "dias");
    boolean detalleInvalido = false;
    for (Map<String, String> myRow : columnData) {
        for (String columna : columnasObli) {
            String valor = myRow.get(columna);
            if (valor == null || valor.isEmpty() || valor.equals("0")) {
                detalleInvalido = true;
                break;
            }
        }
        if (detalleInvalido) {
            JOptionPane.showMessageDialog(this, "Todos los detalles deben tener campos obligatorios válidos (no vacíos y no ceros).", "Error", JOptionPane.ERROR_MESSAGE);
            imNuevo();
            return -1;
        }
    }
    
    // Primero, verificamos si el ID ya existe en la base de datos
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");
    Map<String, String> where = new HashMap<>();
    where.put("id", myData.get("id"));
    
    List<Map<String, String>> existingHeaders = tc.searchListById(fields, where);
    if (existingHeaders.isEmpty()) {
        JOptionPane.showMessageDialog(this, "La cuota no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }

    // Verificar si la cabecera coincide con los datos de la base de datos
    Map<String, String> existingHeader = existingHeaders.get(0);
    for (String columna : columnasObligatoriasCabecera) {
        if (!myData.get(columna).equals(existingHeader.get(columna))) {
            JOptionPane.showMessageDialog(this, "Los datos no coincide con los datos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Verificar si existen los detalles correspondientes a la cabecera
    where.clear();
    where.put("cuota_id", myData.get("id"));
    List<Map<String, String>> detallesExistentes = tcdet.searchListById(fields, where);
    if (detallesExistentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No existen detalles asociados a esta cabecera en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }

    // Verificar que los detalles coincidan con los datos de la base de datos
    for (Map<String, String> myRow : columnData) {
        boolean foundMatchingDetail = false;
        for (Map<String, String> detalleExistente : detallesExistentes) {
            boolean detallesCoinciden = true;
            for (String columna : columnasObli) {
                if (!myRow.get(columna).equals(detalleExistente.get(columna))) {
                    detallesCoinciden = false;
                    break;
                }
            }
            if (detallesCoinciden) {
                foundMatchingDetail = true;
                break;
            }
        }
        if (!foundMatchingDetail) {
            JOptionPane.showMessageDialog(this, "Los detalles no coinciden con los datos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Iniciar transacción (simulada)
    boolean cabeceraEliminada = false;
    boolean detallesEliminados = false;

    try {
        // Eliminar los detalles
        int affectedDetails = 0;
        for (Map<String, String> detalle : detallesExistentes) {
            ArrayList<Map<String, String>> alDetalle = new ArrayList<>();
            alDetalle.add(detalle);
            affectedDetails += tcdet.deleteReg(alDetalle);
        }

        if (affectedDetails == detallesExistentes.size()) {
            detallesEliminados = true;
        } else {
            throw new Exception("Error al eliminar los detalles del registro.");
        }

        // Eliminar la cabecera
        ArrayList<Map<String, String>> alCabecera = new ArrayList<>();
        alCabecera.add(myData);
        int rowsAffected = tc.deleteReg(alCabecera);
        if (rowsAffected > 0) {
            cabeceraEliminada = true;
        } else {
            throw new Exception("Error al eliminar la cabecera del registro.");
        }

        // Confirmar la transacción (simulada)
        if (cabeceraEliminada && detallesEliminados) {
            String msg = "EL REGISTRO: " + IdCuota.getText() + " SE HA ELIMINADO CORRECTAMENTE";
            JOptionPane.showMessageDialog(this, msg, "ATENCIÓN...!", JOptionPane.OK_OPTION);
            imNuevo();
            return 0;
        } else {
            throw new Exception("Error durante la eliminación del registro.");
        }
    } catch (Exception e) {
        // Revertir la transacción (simulada)
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        imNuevo();
        return -1;
    }
}


   @Override
    public int imNuevo() {
        this.resetData();
        this.limpiarTabla();
        this.fillView(myData, columnData);
        cargarUltimoId();
        return 0;
    }

@Override
public int imBuscar() {
    this.setData(); // Recopila los datos de la vista

    // Verificar si el ID es 0
    if (myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "El ID 0 no es válido. Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de error
    }

    // Realiza la búsqueda de la cabecera
    Map<String, String> resultadoCabecera = this.tc.searchById(myData);
    System.out.println("CUOTAS imBuscar " + resultadoCabecera);

    // Limpia la tabla de la vista
    this.limpiarTabla();

    if (resultadoCabecera == null || resultadoCabecera.isEmpty()) {
        System.out.println("No hay registros que mostrar");
        JOptionPane.showMessageDialog(this, "No se encontraron registros con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de que no se encontraron registros
    }

    // Actualiza myData con los resultados de la búsqueda de la cabecera
    this.myData = resultadoCabecera;

    // Prepara los criterios de búsqueda para los detalles
    Map<String, String> where = new HashMap<>();
    where.put("cuota_id", this.myData.get("id"));

    // Define los campos a recuperar
    Map<String, String> fields = new HashMap<>();
    fields.put("*", "*");

    // Realiza la búsqueda de los detalles
    List<Map<String, String>> detalles = this.tcdet.searchListById(fields, where);
    System.out.println("Detalles encontrados: " + detalles);

    if (detalles == null || detalles.isEmpty()) {
        System.out.println("No se encontraron registros de detalles.");
        JOptionPane.showMessageDialog(this, "No se encontraron detalles para la cuota especificada.", "Información", JOptionPane.INFORMATION_MESSAGE);
        this.resetData();
        this.limpiarTabla();
        this.imInsFilas(); // Añadir una fila vacía
        return -1; // Indicador de que no se encontraron detalles
    }

    // Convierte el resultado a ArrayList y actualiza columnData
    this.columnData = new ArrayList<>(detalles);

    // Llena la vista con los datos recuperados
    this.fillView(myData, columnData);

    return 0; // Indicador de que la búsqueda fue exitosa
}

@Override
public int imFiltrar() {
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaCuotas = Arrays.asList("id", "descripcion");
    Form_Buscar buscadorCuotas = new Form_Buscar(parentFrame, true, tc, "CUOTAS", columnasParaCuotas);

    buscadorCuotas.setOnItemSeleccionadoListener(this);
    buscadorCuotas.setVisible(true);

    return 0;
}


@Override
public int imPrimero() {
    this.myData = this.tc.navegationReg("id", "FIRST");

    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    cargarDetallesCuota(this.myData.get("id"));
    this.fillView(this.myData, this.columnData);
    return 0;
}

@Override
public int imSiguiente() {
    if (IdCuota.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdCuota.getText(), "NEXT");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    cargarDetallesCuota(this.myData.get("id"));
    this.fillView(this.myData, this.columnData);
    return 0;
}

@Override
public int imAnterior() {
    if (IdCuota.getText().equals("0")) {
        JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    this.myData = this.tc.navegationReg(IdCuota.getText(), "PRIOR");
    if (this.myData.isEmpty() || this.myData.get("id").equals("0")) {
        JOptionPane.showMessageDialog(this, "No hay más registros en esa dirección.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    cargarDetallesCuota(this.myData.get("id"));
    this.fillView(this.myData, this.columnData);
    return 0;
}

@Override
public int imUltimo() {
    this.myData = this.tc.navegationReg("id", "LAST");

    if (this.myData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay registros para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return -1;
    }

    cargarDetallesCuota(this.myData.get("id"));
    this.fillView(this.myData, this.columnData);
    return 0;
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
                job.setJobName("Cuotas");
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al imprimir", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

    @Override
public int imInsFilas() {
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int lastRow = jtDetalle.getRowCount();
    
    if (lastRow > 0) {
        // Verifica si la última fila contiene datos válidos
        Object lastCuotaValue = jtDetalle.getValueAt(lastRow - 1, 0);
        Object lastDiasValue = jtDetalle.getValueAt(lastRow - 1, 1);

        if (lastCuotaValue != null && lastCuotaValue.toString().equals("0") && 
            lastDiasValue != null && lastDiasValue.toString().equals("0")) {
            // Si la última fila tiene valores "0", simplemente selecciona esa fila en lugar de agregar una nueva
            jtDetalle.setRowSelectionInterval(lastRow - 1, lastRow - 1);
            return 0;
        }
    }

    int newCuota = lastRow + 1;
    int newDias = newCuota * 30;

    model.addRow(new Object[]{newCuota, newDias});

    jtDetalle.setRowSelectionInterval(lastRow, lastRow); // Seleccionar la nueva fila

    // Actualizar el valor del campo Cuotas
    Cuotas.setText(String.valueOf(newCuota));

    return 0;
}




@Override
public int imDelFilas() {
    DefaultTableModel model = (DefaultTableModel) jtDetalle.getModel();
    int rowCount = model.getRowCount();
    
    if (rowCount > 0) {
        // Eliminar la última fila
        model.removeRow(rowCount - 1);

        // Actualizar el campo Cuotas restando 1
        int currentCuotas = Integer.parseInt(Cuotas.getText());
        if (currentCuotas > 0) {
            Cuotas.setText(String.valueOf(currentCuotas - 1));
        }
    } else {
        JOptionPane.showMessageDialog(this, "No hay filas para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    return 0;
}


@Override
public void onItemSeleccionado(Map<String, String> datosSeleccionados) {
    String idStr = datosSeleccionados.get("Codigo");
    String descripcion1 = datosSeleccionados.get("Descripcion");

    if (idStr != null && !idStr.isEmpty()) {
        try {
            int idCuota = Integer.parseInt(idStr);
            List<Map<String, String>> registros = tc.buscarPorIdGenerico("CUOTAS", "id", idCuota);

            if (!registros.isEmpty()) {
                Map<String, String> registro = registros.get(0);

                SwingUtilities.invokeLater(() -> {
                    IdCuota.setText(idStr);
                    Descripcion.setText(descripcion1);

                    irregular.setSelected(Integer.parseInt(registro.get("irregular")) != 0);
                    activo.setSelected(Integer.parseInt(registro.get("activo")) != 0);
                    Cuotas.setText(registro.get("cuotas"));

                    // Cargar detalles de la cuota
                    cargarDetallesCuota(idStr);

                    // Actualizar la vista con los datos cargados
                    fillView(myData, columnData);
                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un registro con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                resetData();
                limpiarTabla();
                imInsFilas(); // Añadir una fila vacía
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "ID de registro inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


}
