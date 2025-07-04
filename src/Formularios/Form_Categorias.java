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

public class Form_Categorias extends javax.swing.JInternalFrame implements InterfaceUsuario {
    private DBTableController tc;
    private Map<String, String> mapData;
    private int idCategoria;
    
    public Form_Categorias() {
        initComponents();
        IdCategoria.setText("0"); 
        tc = new DBTableController();
        tc.iniciar("CATEGORIAS");
        mapData = new HashMap<>();
      idCategoria = -1;
        initializeTextFields();
        actualizarUltimoId(); // Actualizar el JLabel con el último ID al abrir el formulario
    }
     private void setMapData() {
        mapData.clear();
        String id = IdCategoria.getText().trim();
        if (!id.isEmpty() && !id.equals("0")) {
            mapData.put("id", id);
        }

        String nombreCategoria = categoria.getText().trim();
        if (!nombreCategoria.isEmpty()) {
            mapData.put("categoria", nombreCategoria);
        }

        String descripcion = textdescripcion.getText().trim();
        if (!descripcion.isEmpty()) {
            mapData.put("descripcion", descripcion);
            System.out.println("Descripcion setMapData: " + descripcion);
        }

        mapData.put("activo", Activo.isSelected() ? "1" : "0");
    }

    private void resetData() {
        mapData.clear();
        IdCategoria.setText("0");
        categoria.setText("");
        textdescripcion.setText("");
        Activo.setSelected(false);
        mapData.clear();
    }

    private void fillView(Map<String, String> data) {
        System.out.println("Map data received in fillView: " + data);
        IdCategoria.setText(data.getOrDefault("id", ""));
        categoria.setText(data.getOrDefault("categoria", ""));
        textdescripcion.setText(data.getOrDefault("descripcion", ""));
        Activo.setSelected("1".equals(data.getOrDefault("activo", "0")));
    }

    private void initializeTextFields() {
        applyNumericFilter(IdCategoria);
        applyAlphaFilter(categoria);
        applyDescriptionFilter(textdescripcion);
        addFocusListeners();
        addEnterKeyListenerToIdField();
    }

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
        IdCategoria.addFocusListener(new DefaultFocusListener(IdCategoria, true)); // Aún rellenará con "0"
        categoria.addFocusListener(new DefaultFocusListener(categoria, false)); // Aún rellenará con "0"
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
        IdCategoria.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarYRellenarDatos();  // Llama al método que gestiona la búsqueda y el relleno.
                }
            }
        });
    }

    private int obtenerIdCategoriaActual() {
        try {
            return Integer.parseInt(IdCategoria.getText().trim());
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
        resetData();  // Limpiar todos los campos antes de actualizar con nuevos datos

        int idCategoria = Integer.parseInt(registro.get("id"));
        String nombreCat = registro.get("categoria");
        String descripcion = registro.get("descripcion");
        String activo = registro.get("activo");
        actualizarUIConRegistro(idCategoria, nombreCat, descripcion, activo);
        return 1;
    }
}


    private void actualizarUIConRegistro(int idcategoria, String nombreCat, String descripcionCat, String activo) {
        this.idCategoria = idcategoria;
        IdCategoria.setText(String.valueOf(idcategoria));
        categoria.setText(nombreCat);
        textdescripcion.setText(descripcionCat);
        Activo.setSelected("1".equals(activo));
    }

    private void buscarYRellenarDatos() {
        String id = IdCategoria.getText().trim();
        if (!id.isEmpty()) {
            resetData();
            try {
                int idcategoria = Integer.parseInt(id);
                List<Map<String, String>> registros = tc.buscarPorIdGenerico("CATEGORIAS", "id", idcategoria);
                idCategoria = idcategoria;
                if (!registros.isEmpty()) {
                    Map<String, String> registro = registros.get(0);
                    fillView(registro);
                    System.out.println("Registro encontrado: " + registro);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró una categoría con el ID especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
        IdCategoria = new javax.swing.JTextField();
        categoria = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_categoria = new javax.swing.JLabel();
        lbl_descripcion = new javax.swing.JLabel();
        lblultimo = new javax.swing.JLabel();
        UltimoId = new javax.swing.JLabel();
        textdescripcion = new javax.swing.JTextField();
        Activo = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Categorías");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_id.setText("Id");

        lbl_categoria.setText("Categoría");

        lbl_descripcion.setBackground(new java.awt.Color(102, 102, 102));
        lbl_descripcion.setForeground(new java.awt.Color(102, 102, 102));
        lbl_descripcion.setText("Descripción");

        lblultimo.setText("Último");

        UltimoId.setBackground(new java.awt.Color(204, 204, 255));
        UltimoId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UltimoId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UltimoId.setOpaque(true);

        Activo.setText("Activo");

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
                    .addComponent(lbl_categoria, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_id, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoria)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(IdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(lblultimo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(textdescripcion))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Activo)
                .addGap(101, 101, 101))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UltimoId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(IdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_id)
                        .addComponent(lblultimo)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_categoria))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_descripcion)
                    .addComponent(textdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Activo)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Activo;
    private javax.swing.JTextField IdCategoria;
    private javax.swing.JLabel UltimoId;
    private javax.swing.JTextField categoria;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_categoria;
    private javax.swing.JLabel lbl_descripcion;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lblultimo;
    private javax.swing.JTextField textdescripcion;
    // End of variables declaration//GEN-END:variables
@Override
    public int imGuardar(String crud) {
        String id = IdCategoria.getText().trim();
        String nombrecat = categoria.getText().trim();
        String descripcion = textdescripcion.getText().trim();

        if (id.isEmpty() || Integer.parseInt(id) <= 0 || nombrecat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los campos ID y Categoría son obligatorios y deben ser válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        setMapData();
        boolean isUpdate = categoriaExiste(id);
        int rowsAffected = 0;

        if (isUpdate) {
            Map<String, String> existingData = tc.searchById(mapData); // Asume que devuelve los datos actuales
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
            JOptionPane.showMessageDialog(null, isUpdate ? "Categoría actualizada correctamente." : "Categoría creada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar la operación solicitada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        resetData();
        fillView(mapData);
        actualizarUltimoId();
        IdCategoria.requestFocusInWindow();

        return rowsAffected;
    }

    private boolean categoriaExiste(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("El ID proporcionado es nulo o está vacío.");
            return false;
        }

        Map<String, String> params = new HashMap<>();
        params.put("id", id.trim());  // Asegúrate de que el ID está correctamente ajustado (sin espacios innecesarios)

        Map<String, String> CategoriaData = tc.searchById(params);

        if (CategoriaData != null && !CategoriaData.isEmpty()) {
            System.out.println("Se encontró una categoría con el ID: " + id + " -> " + CategoriaData);
            return true;
        } else {
            System.out.println("No se encontró ninguna categoría con el ID: " + id);
            return false;
        }
    }

    @Override
    public int imBorrar(String crud) {
        if (IdCategoria.getText().trim().isEmpty() || categoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de eliminar.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        int idCatActual = obtenerIdCategoriaActual();
        if (idCatActual <= 0) {
            JOptionPane.showMessageDialog(this, "ID inválido o no seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return 0;
        }

        ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
        Map<String, String> registro = new HashMap<>();
        registro.put("id", String.valueOf(idCatActual));
        registrosParaBorrar.add(registro);

        int resultado = tc.deleteReg(registrosParaBorrar);
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Categoría eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
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
          idCategoria = -1;
        return 0;
    }

@Override
public int imBuscar() {
     buscarYRellenarDatos();
    return 0;
}


    @Override
    public int imFiltrar() {
   resetData(); // Limpiar todos los campos antes de la búsqueda
    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
    List<String> columnasParaCategoria = Arrays.asList("id", "categoria");
    Form_Buscar buscadorCategoria = new Form_Buscar(parentFrame, true, tc, "CATEGORIAS", columnasParaCategoria);

    buscadorCategoria.setOnItemSeleccionadoListener(this);
    buscadorCategoria.setVisible(true);

    return 0;
    }

    @Override
    public int imPrimero() {
        Map<String, String> registro = tc.navegationReg(null, "FIRST");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imSiguiente() {
        if (idCategoria == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idCategoria), "NEXT");
        return procesarRegistroNavegacion(registro);
    }

    @Override
    public int imAnterior() {
        if (idCategoria <= 1) {
            JOptionPane.showMessageDialog(this, "No hay más registros en esta dirección.");
            return -1;
        }
        Map<String, String> registro = tc.navegationReg(String.valueOf(idCategoria), "PRIOR");
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
                job.setJobName("Categorias");
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

        String idStr = datosSeleccionados.get("Codigo");
        String nombreCategoria = datosSeleccionados.get("Descripcion");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idCat = Integer.parseInt(idStr);
                List<Map<String, String>> registros = tc.buscarPorIdGenerico("CATEGORIAS", "id", idCat);

                System.out.println("Registros obtenidos: " + registros);

                if (!registros.isEmpty()) {
                    Map<String, String> registro = registros.get(0);
                    final String descripcionCategoria = registro.get("descripcion");
                    final String activo = registro.get("activo");

                    SwingUtilities.invokeLater(() -> {
                        IdCategoria.setText(idStr);
                        idCategoria = idCat;
                        categoria.setText(nombreCategoria);
                        textdescripcion.setText(descripcionCategoria);
                        Activo.setSelected("1".equals(activo));
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró una categoría con el Código especificado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
