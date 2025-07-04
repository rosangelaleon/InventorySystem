
package Formularios;
import static Controllers.DBConexion.statement;
import Controllers.DBTableController;
import Controllers.InterfaceUsuario;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Form_Permisos extends javax.swing.JInternalFrame implements InterfaceUsuario{
    private DBTableController tc;
    private DBTableController tr;
    private DBTableController tm;
    private Map<String, String> mapData;
    private Map<String, String> menuNombres;

    private int idRolSeleccionado;

    public Form_Permisos() {
        initComponents();
        roles.addItem("Seleccione un rol");
        tc = new DBTableController();
        tc.iniciar("PERMISOS");
        tr = new DBTableController();
        tr.iniciar("ROLES");
        tm = new DBTableController();
        tm.iniciar("MENUS");
        mapData = new HashMap<>();
        idRolSeleccionado = -1;
        menuNombres = cargarNombresMenus();
        cargarRoles();
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        roles = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Permisos = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Registrar Permisos");

        jLabel1.setText("Rol");

        roles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rolesActionPerformed(evt);
            }
        });

        Permisos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Seleccionar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Permisos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(roles, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rolesActionPerformed
int rolId = getSelectedRolId(); // Obtiene el ID del rol seleccionado
    if (rolId != -1) {
        idRolSeleccionado = rolId; 
        cargarPermisos(rolId); // Carga los permisos asociados con el rol seleccionado
    } else {
        // Limpia la tabla de permisos si no se seleccionó un rol válido
        DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
        model.setRowCount(0);
    }
    }//GEN-LAST:event_rolesActionPerformed
   
  private void SetMapData() {
    int rolId = getSelectedRolId();
    if (rolId != -1) {
        // Restablecer mapData para asegurarse de que solo contiene datos relevantes
        mapData.clear();
        mapData.put("rol_id", " "+String.valueOf(rolId)+" ");

        DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String menuId = (String) model.getValueAt(i, 0);
            boolean seleccionado = (Boolean) model.getValueAt(i, 2);

            // Usar solo menuId como clave
            mapData.put(menuId, " "+String.valueOf(seleccionado)+" ");
        }
    }
}


private void resetData() {
    mapData.clear();
    roles.setSelectedIndex(0); // Esto restablece el JComboBox a la posición inicial
    DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
    model.setRowCount(0); // Esto limpia la tabla Permisos
}

private void fillView(Map<String, String> data) {
    String rolId = data.getOrDefault("rol_id", "-1");
    seleccionarRolEnComboBox(Integer.parseInt(rolId));

    // Llenar la tabla Permisos basándose en los datos del mapa
    DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
    model.setRowCount(0); // Limpia la tabla antes de llenarla
    for (Map.Entry<String, String> entry : data.entrySet()) {
        if (entry.getKey().startsWith("menu_id")) {
            String menuId = entry.getKey().substring(8);
            boolean seleccionado = Boolean.parseBoolean(entry.getValue());
            // Aquí deberás obtener el nombre del menú basado en el menuId
            String menuNombre = obtenerNombreMenu(menuId);
            model.addRow(new Object[]{menuId, menuNombre, seleccionado});
        }
    }
}

private int getSelectedRolId() {
    String selectedRol = (String) roles.getSelectedItem();
    // Verificar si el elemento seleccionado es "Seleccione un rol"
    if (selectedRol == null || selectedRol.equals("Seleccione un rol")) {
        return -1;
    }
    try {
        String[] parts = selectedRol.split(" - ");
        if (parts.length > 1) {
            return Integer.parseInt(parts[0]);
        } else {
            return -1;
        }
    } catch (NumberFormatException e) {
        return -1;
    }
}

private Map<String, String> cargarNombresMenus() {
    Map<String, String> nombres = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            // Extrae los números del id y compáralos numéricamente
            int numericId1 = Integer.parseInt(o1.replaceAll("\\D+", ""));
            int numericId2 = Integer.parseInt(o2.replaceAll("\\D+", ""));
            return Integer.compare(numericId1, numericId2);
        }
    });

    try {
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", "");
        viewRegister.put("nombre", "");
        ArrayList<Map<String, String>> resultados = tm.searchListById(viewRegister, new HashMap<>());
        for (Map<String, String> resultado : resultados) {
            nombres.put(resultado.get("id"), resultado.get("nombre"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar nombres de menús: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return nombres;
}


private void cargarPermisos(int rolId) {
    try {
        DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
        model.setRowCount(0);

        // Itera sobre cada entrada en el mapa de nombres de menús
        for (Map.Entry<String, String> menu : menuNombres.entrySet()) {
            String menuId = menu.getKey();  // ID del menú
            String menuNombre = menu.getValue();  // Nombre del menú

            boolean seleccionado = determinarSiPermisoSeleccionado(menuId, rolId);  // Determina si el permiso está seleccionado

            // Añade la fila al modelo de la tabla
            model.addRow(new Object[]{menuId, menuNombre, seleccionado});
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar permisos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}



public String obtenerNombreMenu(String menuId) {
    try {
        // Obtener todos los nombres de menú disponibles
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", ""); // Obtener todos los nombres de menú
        viewRegister.put("nombre", ""); // Obtener todos los nombres de menú
        ArrayList<Map<String, String>> nombresMenus = tm.searchListById(viewRegister, new HashMap<>());

        // Buscar el nombre del menú basado en el ID
        for (Map<String, String> nombreMenu : nombresMenus) {
            if (nombreMenu.get("id").equals(menuId)) {
                return nombreMenu.get("nombre");
            }
        }

        // Si no se encuentra el nombre del menú, devolver una cadena vacía
        return "";
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener el nombre del menú: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return "";
    }
}


private boolean determinarSiPermisoSeleccionado(String menuId, int rolId) {
    try {
        Map<String, String> criterios = new HashMap<>();
        criterios.put("rol_id", String.valueOf(rolId));
        criterios.put("menu_id", menuId);

        Map<String, String> resultado = tc.searchById(criterios);
        return !resultado.isEmpty();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al determinar el estado del permiso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

private void cargarRoles() {
    roles.removeAllItems(); // Limpia el JComboBox antes de cargar nuevos datos
    roles.addItem("Seleccione un rol"); // Opción inicial

    try {
        // Crea un mapa vacío para los criterios de búsqueda ya que queremos todos los roles
        Map<String, String> where = new HashMap<>();
        Map<String, String> viewRegister = new HashMap<>();
        viewRegister.put("id", ""); // Asigna valores vacíos o criterios genéricos
        viewRegister.put("rol", "");

        // Llama a searchListById con los criterios vacíos
        ArrayList<Map<String, String>> resultados = tr.searchListById(viewRegister, where);

        // Ordena los resultados basándose en el valor numérico de 'id'
        resultados.sort(new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                int id1 = Integer.parseInt(o1.get("id"));
                int id2 = Integer.parseInt(o2.get("id"));
                return Integer.compare(id1, id2);
            }
        });

        // Itera sobre los resultados ya ordenados para añadirlos al JComboBox
        for (Map<String, String> rol : resultados) {
            String id = rol.get("id");
            String nombreRol = rol.get("rol");
            roles.addItem(id + " - " + nombreRol);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar roles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Permisos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> roles;
    // End of variables declaration//GEN-END:variables

@Override
public int imGuardar(String crud) {
    int rolId = getSelectedRolId();
    if (rolId == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un rol antes de guardar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    SetMapData(); // Actualiza mapData con los datos de la interfaz de usuario
    ArrayList<Map<String, String>> registrosParaCrear = new ArrayList<>();
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) Permisos.getModel();

    int registrosCreados = 0, registrosBorrados = 0, registrosActualizados = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        String menuId = (String) model.getValueAt(i, 0);
        boolean seleccionado = (Boolean) model.getValueAt(i, 2);

        if (menuId != null && !menuId.trim().isEmpty()) {
            boolean existe = determinarSiPermisoSeleccionado(menuId, rolId);

            Map<String, String> registro = new HashMap<>();
            registro.put("rol_id", String.valueOf(rolId));
            registro.put("menu_id", menuId.trim());

            if (seleccionado && !existe) {
                registrosParaCrear.add(registro);
            } else if (!seleccionado && existe) {
                registrosParaBorrar.add(registro);
            }
        }
    }

    try {

        // Crear nuevos permisos
        for (Map<String, String> registro : registrosParaCrear) {
            if (tc.existAny(registro) == 0) {
     
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO Permisos (rol_id, menu_id) VALUES (");
            queryBuilder.append(registro.get("rol_id")).append(", '").append(registro.get("menu_id")).append("')");

            int rowsAffected = statement.executeUpdate(queryBuilder.toString());

            // Verificar si se insertó correctamente
            if (rowsAffected > 0) {
                registrosCreados++;
            }
        }
        }


        // Borrar permisos deseleccionados
        if (!registrosParaBorrar.isEmpty()) {
            registrosBorrados = tc.deleteReg(registrosParaBorrar);
        }

        // Recargar la vista de permisos
        cargarPermisos(rolId);

        // Mensajes de retroalimentación
        if (registrosCreados > 0) {
            JOptionPane.showMessageDialog(this, "Se crearon " + registrosCreados + " permisos.", "Permisos Creados", JOptionPane.INFORMATION_MESSAGE);
        }
        if (registrosBorrados > 0) {
            JOptionPane.showMessageDialog(this, "Se actualizaron " + registrosBorrados + " permisos.", "Permisos Borrados", JOptionPane.INFORMATION_MESSAGE);
        }
        if (registrosCreados == 0 && registrosBorrados == 0) {
            JOptionPane.showMessageDialog(this, "No hay cambios para guardar.", "Sin Cambios", JOptionPane.INFORMATION_MESSAGE);
        }

        return registrosCreados + registrosBorrados;
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar permisos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}


@Override
public int imBorrar(String crud) {
    int rolId = getSelectedRolId();
    if (rolId == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un rol antes de borrar los permisos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar todos los permisos asociados a este rol?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion != JOptionPane.YES_OPTION) {
        return 0; // Acción cancelada por el usuario
    }

    // Crear una lista de registros a borrar
    ArrayList<Map<String, String>> registrosParaBorrar = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) Permisos.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        String menuId = (String) model.getValueAt(i, 0);
        Map<String, String> registroParaBorrar = new HashMap<>();
        registroParaBorrar.put("rol_id", String.valueOf(rolId));
        registroParaBorrar.put("menu_id", menuId);
        registrosParaBorrar.add(registroParaBorrar);
    }

    // Utilizar el método deleteReg para borrar los registros
    int resultado = tc.deleteReg(registrosParaBorrar);
    if (resultado > 0) {
        JOptionPane.showMessageDialog(this, "Permisos eliminados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarPermisos(rolId); // Recargar los permisos para actualizar la tabla
    } else {
        JOptionPane.showMessageDialog(this, "No se pudieron eliminar los permisos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return resultado; // Devuelve la cantidad de registros eliminados
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
    Map<String, String> registro = tr.navegationReg(null, "FIRST");
    return procesarRegistroNavegacionParaPermisos(registro);
}

@Override
public int imSiguiente() {
    int rolActualId = getSelectedRolId();
    if (rolActualId == -1) {
        return -1;
    }
    Map<String, String> registro = tr.navegationReg(String.valueOf(rolActualId), "NEXT");
    return procesarRegistroNavegacionParaPermisos(registro);
}

@Override
public int imAnterior() {
int rolActualId = getSelectedRolId();
    
    // Verificar si el rol actual es el primero.
    if (rolActualId <= 1) {
        JOptionPane.showMessageDialog(null, "No hay registros anteriores disponibles.");
        return -1;
    }
    
    // Obtener el rol anterior basado en el ID del rol actual.
    Map<String, String> registro = tr.navegationReg(String.valueOf(rolActualId), "PRIOR");

    return procesarRegistroNavegacionParaPermisos(registro);
}

@Override
public int imUltimo() {
    Map<String, String> registro = tr.navegationReg(null, "LAST");
    return procesarRegistroNavegacionParaPermisos(registro);
}

private int procesarRegistroNavegacionParaPermisos(Map<String, String> registro) {
    if (registro == null || registro.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay más roles en esta dirección.");
        return -1;
    } else {
        // Aquí debes extraer el id del rol y cargar los permisos correspondientes
        int idRol = Integer.parseInt(registro.get("id"));
        seleccionarRolEnComboBox(idRol);
        cargarPermisos(idRol);
        return 1;
    }
}

private void seleccionarRolEnComboBox(int idRol) {
    for (int i = 0; i < roles.getItemCount(); i++) {
        String item = roles.getItemAt(i).toString();
        // Suponiendo que tus elementos en el JComboBox son cadenas de la forma "id - nombre"
        if (item.startsWith(idRol + " - ")) {
            roles.setSelectedIndex(i);
            break;
        }
    }
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
                job.setJobName("Permisos");
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
