package Controllers;

import Modelo.ProductoDetalle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DBTableController {
    // Claves primarias de la tabla
    private ArrayList<String> tableKeys;
    private Map<String, String> tableMap;
    // El modelo para nuestra tabla
    DBTableModel tm;
    
    public void iniciar(String table) {
        this.tableKeys = new ArrayList<String>();
        this.tableKeys = DBConexion.getKeyColumns(table, "PRI");     // Se recuperan todos los campos primarios  
        tm = new DBTableModel(); // Instanciamos el modelo de la tabla
        tm.iniciar(table);        // Especificamos la tabla
    } // fin init
    
    public int getMaxId() {
        return this.tm.getMaxId();
    }
    
    public Map<String, String> getTableMap() {
        return this.tableMap;
    }
    
    /**
     * Prepara y ejecuta sentencia para insertar un registro. Utiliza el método de saveRegister de tableModel
     * @param viewRegister Map<String, String> para clave valor de datos de la vista
     * @return rtn int valor según la ejecución haya sido exitoso el id o no 0.
     */
    public int createReg(Map<String, String> viewRegister) {
        // Recibe todos los campos de la vista viewRegister
        // Limpiar el Map dejando sólo los campos de la tabla  
        int rtn;
        rtn = 0;
        int size;
        tableMap = this.tm.justTableFields(viewRegister, false);

        // Primero hay que decidir si se crea o se actualiza si es que ya existe
        if (this.tm.existAny(viewRegister) > 0) {
            System.out.println("Ya existe, se supone debe actualizar");
            rtn = this.tm.updateRegister(tableMap);
        } else {
            // Enviar el nuevo Map para ser procesado
            size = this.tableKeys.size(); // Para saber cuántas claves tiene la tabla        
            if (size == 0) { // Si el detalle no tiene clave alguna
                System.out.println("La tabla no tiene clave primaria asignada");
                rtn = this.tm.saveRegister(tableMap);
            }

            if (size >= 2) { // Cuando es detalle por lo general tiene clave compuesta
                System.out.println("La tabla tiene clave primaria compuesta");
                rtn = this.tm.saveRegister(tableMap);
            }

            if (size == 1) {
                System.out.println("La tabla es de clave primaria única");
                String idname = this.tableKeys.get(0);
                String providedId = viewRegister.get(idname); // Obtener el ID proporcionado
                if (providedId == null || providedId.isEmpty()) {
                    // Si no se proporcionó ID, obtener el máximo ID y sumar uno
                    int id = this.tm.getMaxId(); // Para los detalles hay que ver cuál es el que se recupera
                    id = id + 1;
                    viewRegister.put(idname, String.valueOf(id));
                    tableMap.put(idname, String.valueOf(id));
                } else {
                    tableMap.put(idname, providedId); // Usar el ID proporcionado
                }
                rtn = this.tm.saveRegister(tableMap);
                if (rtn > 0 && (providedId == null || providedId.isEmpty())) {
                    rtn = Integer.parseInt(tableMap.get(idname));
                }
            }
        }
        System.out.println("CreateReg:" + rtn); 
        return rtn;
    } // en createReg
    
    public int createRegString(Map<String, String> viewRegister) {
        // Recibe todos los campos de la vista viewRegister
        // Limpiar el Map dejando sólo los campos de la tabla       
        tableMap = this.tm.justTableFields(viewRegister, false);
        // Enviar el nuevo Map para ser procesado
        int rtn;
        String id = new String();
        rtn = 0;   
        int size;
        size = this.tableKeys.size();
        if (size == 0 || size >= 2) { // Si es un detalle por lo general ya se le pasará
            System.out.println("La tabla no tiene clave primaria asignada o es de clave compuesta");
            rtn = this.tm.saveRegister(tableMap);
        }
        if (size == 1) {
            // System.out.println("La tabla es de clave primaria única");
            // String idname = this.tableKeys.get(0);
            // id = this.tm.getMaxIdString(); // Para los detalles hay ver cual es el que se recupera
            // id = id + 1;
            // System.out.println("el ID = " + id);
            // viewRegister.put(idname, id + "");
            // tableMap.put(idname, id + "");
            rtn = this.tm.saveRegister(tableMap);
        }

        return rtn;
    } // en createReg
    
    /**
     * Método que permite la búsqueda de un registro por el id. Recurre al método interno createIdVal
     * y a readRegisterById de la clase tableModel.
     * @param viewRegister Map par de campos y valores que se pasa desde la vista
     * @return rtn Map con el registro recuperado
     */
    public Map<String, String> searchById(Map<String, String> viewRegister) {
        Map<String, String> rtn;
        Map<String, String> where;
        rtn = new HashMap<String, String>();
        where = this.createIdVal(viewRegister);
        rtn = this.tm.readRegisterById(viewRegister, where);
        // Registro de la consulta y resultados para depuración
        System.out.println("Resultado de searchById: " + rtn);
        return rtn;
    } // fin searchById
    
    public ArrayList<Map<String, String>> searchListById(Map<String, String> viewRegister, Map<String, String> where) {
        ArrayList<Map<String, String>> rtn;
        rtn = new ArrayList<Map<String, String>>();
        System.out.println("tc 132 viewreg " + viewRegister);
        System.out.println("tc 133 where " + where);
        rtn = this.tm.readRegisterList(viewRegister, where);
        return rtn;
    } // fin searchById
    
    /**
     * Méttodo que prepara la navegación entre registros.
     * @param id String en el que se pasa el número de registro actual
     * @param goTo String en que se dice a qué posición se desea mover (FIRST, NEXT, PRIOR, LAST)
     * @return rtn Map que contiene el registro recuperado
     */
    public Map<String, String> navegationReg(String id, String goTo) {
        Map<String, String> rtn;
        System.out.println("tc 100 id " + id + " goto " + goTo);
        rtn = new HashMap<String, String>();
        rtn = this.tm.readNavetionReg(id, goTo);
        System.out.println("tc 103 ");
        return rtn;
    } // fin searchById
    
    /**
     * Método que prepara para la eliminación de registro. Usa el método deleteRegister de la clase tableModel
     * @param id Sring el código actual en la vista
     * @return rtn int devuelve las filas afectadas
     */
    public int deleteReg(ArrayList<Map<String, String>> registers) {
        int rows = 0;
        for (Map<String, String> myRow : registers) { // En el mejor de los casos será un solo registro
            rows += this.tm.deleteRegister(myRow); // Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows; // Retorna la suma de todas las filas eliminadas
    } // fin deleteReg
    
    /**
     * Método que prepara la actualiación de un registro. Invoca métodos propios de la clase createIdVal, createSetFieldsValues; así como
     * el método updateRegister de la clase tableModel
     * @param viewRegister Map con los datos de la vista.
     * @return rtn int cantidad de filas afectadas.
     */
    public int updateReg(ArrayList<Map<String, String>> registers) {
        int rows = 0;
        for (Map<String, String> myRow : registers) { // En el mejor de los casos será un solo registro
            rows += this.tm.updateRegister(myRow); // Hay que pasar un Map, devuelve cantidad de filas afectadas
        }
        return rows;
    } // end updateReg
    
    /**
     * Método que construye un Map con sólo los campos de clave primaria con sus respectivos valores.
     * @param viewRegister Map con los pares campo-valor de claves primarias
     * @return rtn Map de las claves de tabla con sus valores si los tiene
     */
    public Map<String, String> createIdVal(Map<String, String> viewRegister) {
        Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        Iterator<String> arrayIterator = this.tableKeys.iterator();
        while (arrayIterator.hasNext()) {
            String elemento = arrayIterator.next();
            if (viewRegister.containsKey(elemento)) {
                rtn.put(elemento, viewRegister.get(elemento));
            }
        } // fin while    
        return rtn;
    } // createIdVal
    
    /**
     * Método que construye un Map de pares campos-valores que no sean clave primaria en la tabla.
     * @param viewRegister Map de pareas campos-valores enviados desde la vista
     * @return rtn Map de pares campos-valores que no sean clave primaria en la tabla
     */
    public Map<String, String> createSetFieldsValues(Map<String, String> viewRegister) {
        Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : viewRegister.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!this.tableKeys.contains(key)) {
                rtn.put(key, value);
            }
        }
        // System.out.println("C createSetFieldsValues RETURN " + rtn.toString());
        return rtn;
    } // createSetFieldsValues

    public int existAny(Map<String, String> viewRegister) {
        return tm.existAny(viewRegister);
    }

    public List<Map<String, String>> buscarPorColumna(String nombreTabla, List<String> columnas, String textoBusqueda) {
        List<Map<String, String>> resultados = new ArrayList<>();
        String consultaSQL = "SELECT " + String.join(", ", columnas) + " FROM " + nombreTabla + 
                             " WHERE " + columnas.stream().map(col -> col + " LIKE ?").collect(Collectors.joining(" OR "));
        
        try (PreparedStatement pstmt = DBConexion.con.prepareStatement(consultaSQL)) {
            for (int i = 1; i <= columnas.size(); i++) {
                pstmt.setString(i, "%" + textoBusqueda + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    for (int i = 1; i <= columnas.size(); i++) {
                        fila.put(columnas.get(i - 1), rs.getString(i));
                    }
                    resultados.add(fila);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Asegúrate de manejar la excepción adecuadamente
        }

        return resultados;
    }

    public List<Map<String, String>> buscarPorIdGenerico(String nombreTabla, String columnaId, int id) {
        List<Map<String, String>> resultados = new ArrayList<>();
        String consultaSQL = "SELECT * FROM " + nombreTabla + " WHERE " + columnaId + " = ?";

        try (PreparedStatement pstmt = DBConexion.con.prepareStatement(consultaSQL)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.put(metaData.getColumnName(i), rs.getString(i));
                    }
                    resultados.add(fila);
                }
            }
        } catch (SQLException e) {
            // Manejar adecuadamente la excepción SQL
            e.printStackTrace();
        }

        return resultados;
    }
 public List<Map<String, String>> buscarRegistros(String tabla, String columnaFiltro, String valorFiltro) {
    List<Map<String, String>> resultados = new ArrayList<>();
    String sql = "SELECT * FROM " + tabla + " WHERE " + columnaFiltro + " = ?";
    try (PreparedStatement stmt = DBConexion.con.prepareStatement(sql)) {
        stmt.setString(1, valorFiltro);
        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    fila.put(metaData.getColumnName(i), rs.getString(i));
                }
                resultados.add(fila);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return resultados;
}
public Map<String, String> obtenerUltimaFila(String tabla, String columnaOrden) {
    String sql = "SELECT * FROM " + tabla + " ORDER BY " + columnaOrden + " DESC LIMIT 1";
    Map<String, String> ultimaFila = new HashMap<>();
    try (PreparedStatement stmt = DBConexion.con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                ultimaFila.put(metaData.getColumnName(i), rs.getString(i));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return ultimaFila;
}

    public List<Map<String, String>> buscarPorConsultaGenerica(String tabla, String campos, String condicion) {
        List<Map<String, String>> resultados = new ArrayList<>();
        String sql = "SELECT " + campos + " FROM " + tabla + " WHERE " + condicion;

        try (PreparedStatement pstmt = DBConexion.con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    fila.put(metaData.getColumnName(i), rs.getString(i));
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }
    
public int getDecimalPlacesForCurrency(String currencyId) {
    String query = "SELECT decimales FROM MONEDAS WHERE id = ?";
    try (PreparedStatement statement = DBConexion.con.prepareStatement(query)) {
        statement.setString(1, currencyId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("decimales");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 2; // Valor por defecto en caso de no encontrar la moneda
}
}