
package Controllers;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DBTableModel {
    //Nombre de la tabla cuyo modelo se representa en esta instancia
    private String tableName; 
    //Nombre de todas las columnas de la tabla
    private ArrayList<String> columnNames;
    //Tipo de cada columna de la tabla
    private ArrayList<String> columnTypes;
    //Claves primarias de la tabla
    private ArrayList<String> tableKeys;
    //private HashMap<String, String> Data;
    List<Object> Data; //= new ArrayList <Object>();
    
    /**
     * Constructor que recibe el nombre de la tabla a la que representará la clase
     * @param tableName 
     */
    public void iniciar(String tableName){
        this.tableName = tableName;
        this.columnNames = DBConexion.getColumnNames(tableName);
        this.columnTypes = DBConexion.getColumnTypes(tableName);
        this.tableKeys = DBConexion.getKeyColumns(tableName, "PRI");
        this.Data = new ArrayList <Object>();
    }//fin constructor
    
   private String createPKKeyValueList(Map<String, String> whereClause, String separator){
    String rtn = "";
    int row = 0; // Iniciar el contador de filas en 0
    for (Map.Entry<String, String> entry : whereClause.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (this.tableKeys.contains(key)) {
            if (row > 0) {
                rtn += " " + separator + " "; // Añadir el separador si no es el primer elemento
            }
            rtn += matchKeyValue(key, "=", value);
            row++; // Incrementar el contador después de añadir cada clave-valor
        }
    }
    System.out.println("Consulta generada: " + rtn);
    return rtn;
}
    
    public int existAnybyField(Map<String, String> viewRegister){
        int rtn = 0;
        String sql;
        sql = "";
        sql += "SELECT * FROM "+this.tableName; // + " WHERE ";     
        sql += this.createWhereClause(viewRegister);
        ResultSet rs = DBConexion.ejecuteSQL(sql); //Retorna canitdad de filas afectadas
        try {
            if(rs.next()){
                System.out.println("Resultado"+rs);
                rtn = 1;
            }else{
                System.out.println("No pasó un wevo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn; 
    }//fin saveRegister
    
    /**
     * Consuta si existe algún registro en la bd con los datos de primary key sea simpel o compuesta
     * @param viewRegister campos de la tabla, al menos las claves primarias
     * @return 1 si existe algún registro y 0 si no existe
     */
    public int existAny(Map<String, String> viewRegister){
        int rtn = 0;
        String sql;
        sql = "";
        sql += "SELECT * FROM "+this.tableName + " WHERE ";   
        sql += this.createPKKeyValueList(viewRegister,"AND");
        ResultSet rs = DBConexion.ejecuteSQL(sql); //Retorna canitdad de filas afectadas
        try {
            if(rs.next()){
                System.out.println("Resultado existAny"+rs);
                rtn = 1;
            }else{
                System.out.println("No pasó un wevo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn; 
    }//fin saveRegister
    /**
     * Método que construye y ejecuta la sentencia sql de INSERT.
     * @param viewRegister Map de pares campo-valor enviado desde la vista
     * @return rtn int de filas afectadas, 1 si hubo éxito.
     */
    public int saveRegister(Map<String, String> viewRegister){
        int rtn = 0;
        String sql;
        sql = "";
        sql += "INSERT INTO "+this.tableName+"(" ;
        ArrayList<String> keys = new ArrayList(viewRegister.keySet());
        sql += this.createKeyListString(keys);
        sql += ") VALUES (";
        sql += this.createValueListString(viewRegister, ",");        
        sql += ")";
        System.out.println("Resultado fsql saveRegister"+sql);
        try {
        rtn = DBConexion.ejecuteUPD(sql); //Retorna cantidad de filas afectadas
        System.out.println("Resultado filas afectadas saveRegister"+rtn);
    } catch (SQLException e) {
        // Mostrar mensaje de error en ventana emergente
        JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
        System.out.println("Resultado filas afectadas saveRegister"+rtn);
        return rtn; 
    }//fin saveRegister
    
    public List<Object> getData(){
        return this.Data;
    }
    public void setData(List<Object> data){
        this.Data = data;
    }
    /**
     * Prepara y ejecuta sentencia sql para actualizar (UPDATE) los datos en la tabla
     * @param viewRegister Map que contiene el par campo-valor de los campos que no son clave primaria
     * @param conditions Map que contiene el par campo-valor de los campos clave primaria de la tabla
     * @return rtn int cantidad de filas afectadas
     */
    public int updateRegister(Map<String, String> keys){
        int rtn = 0;
        String sql;
        sql = "";
        //Primero v
        sql="UPDATE " + this.tableName + " SET ";
        ArrayList<ArrayList<String>> aList; 
        aList = new ArrayList<ArrayList<String>>();    //Crear lista
        ArrayList<String> aRow;                        //Declara cada registro de la lista que serán 3 [key, comparation, value]
        
        for(String key : this.tableKeys){              //Recorre todos los primary key
            if(keys.containsKey(key)){
                String v = keys.get(key);              //Recupera el valor correspondiente al key, sino existe devuelve una cadena vacía ""
                //System.out.println("V "+v);
                if(!v.equals("")){                     //Si no está vacía entonces
                    aRow = new ArrayList<String>();    //se crea una nueva instancia de array con elementos tipo String
                    aRow.add(key);                     //donde se ingrsa como elemento 0 el id
                    aRow.add("=");                     //segundo elemento el comparador   
                    aRow.add(keys.get(key));           //tercer elemento el valor
                    aList.add(aRow);                   //y se almacena en el array de array
                    keys.remove(key);                  //Quita primary key del Map para no formar parte del SET en la sentencia
                }
            } 
        }
        //System.out.println("= = = = = = = = nuevo keys antes "+keys);
        keys = this.justTableFields(keys, false);   //dejar sólo los keys que se corresponden con los campos de la tabla
        System.out.println("= = = =  = = = = = = =nuevo keys despues "+keys);
        sql += createKeyValueList(keys, ",");      //Crea String tipo (key=value, key=value)
        sql += createWhereClause3(aList);          //Se pasa el array al método para que cree la clausula WHERE
        System.out.println("SQL Model 105  : "+sql);
   try {
        rtn = DBConexion.ejecuteUPD(sql); //Retorna cantidad de filas afectadas
        System.out.println("Resultado filas afectadas saveRegister"+rtn);
    } catch (SQLException e) {
        // Mostrar mensaje de error en ventana emergente
        JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
        return rtn;                                //retornamos la cantidad de filas afectadas
    }//fin updateRegister
    
    /**
     * Método que prepara y ejectua sentencia sql para borrar (DELETE) registro.
     * @param id el código del restro a ser eliminado pasado desde la vista.
     * @return rtn int cantidad de las filas afectadas.
     */
    public int deleteRegister(Map<String, String> keys){
        int rtn = 0;
        //Primero v
        String sql = "";
        sql="DELETE FROM "+this.tableName;
        ArrayList<ArrayList<String>> aList; 
        aList = new ArrayList<ArrayList<String>>(); //Crear lista
        ArrayList<String> aRow; //Declara cada registro de la lista que serán 3 [key, comparation, value]
        
        for(String key : this.tableKeys){          //Recorre todos los primary key
            String v = keys.getOrDefault(key, ""); //Recupera el valor correspondiente al key, sino existe devuelve una cadena vacía ""
            if(!v.equals("")){                     //Si no está vacía entonces
                aRow = new ArrayList<String>();    //se crea una nueva instancia de array con elementos tipo String
                aRow.add(key);                     //donde se ingrsa como elemento 0 el id
                aRow.add("=");                     //segundo elemento el comparador   
                aRow.add(keys.get(key));           //tercer elemento el valor
                aList.add(aRow);                   //y se almacena en el array de array
            }
        }
        sql += createWhereClause3(aList);          //Se pasa el array al método para que cree la clausula WHERE
        //System.out.println("SQL  : "+sql);
        try {
        rtn = DBConexion.ejecuteUPD(sql); //Retorna cantidad de filas afectadas
        System.out.println("Resultado filas afectadas saveRegister"+rtn);
    } catch (SQLException e) {
        // Mostrar mensaje de error en ventana emergente
   
    }         //se ejecuta la consulta update contra la bd
        return rtn;                                //retornamos la cantidad de filas afectadas
    }//fin deleteRegister
    
    public ArrayList<Map<String, String>> readRegister(String group, Map<String, String> fielsToSelect, Map<String, String> conditions, ArrayList<String> orderBy, String order, int limit){
        ArrayList<Map<String, String>> rtn = null;
        rtn = new ArrayList<Map<String, String>>();
        ResultSet rs;
        //Sólo necesitamos la lista de campos a poner en el select
        ArrayList fields = new ArrayList(fielsToSelect.keySet());
        String sql;
        sql = createReadSQL(fields, conditions);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            while(rs.next()){ 
                Map<String, String> mapColumns = new HashMap<String, String>();
                for (int i = 1; i <= colCount; i++) {
                  mapColumns.put(metaData.getColumnLabel(i), rs.getString(i));
                }
                rtn.add(mapColumns);
            }   
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn;
    }
    
    public ArrayList<Map<String, String>> readRegisterList(Map<String, String> fielsToSelect, Map<String, String> conditions){
        ArrayList<Map<String, String>> rtn = null;
        rtn = new ArrayList<Map<String, String>>();
        
        ResultSet rs;
        //Sólo necesitamos la lista de campos a poner en el select
        //System.out.println("fieldselect "+fielsToSelect);
        //System.out.println("conditions "+conditions);
        ArrayList fields = new ArrayList(fielsToSelect.keySet());
        String sql;
        sql = createReadSQL(fields, conditions);
        //System.out.println(" tableModel 175 sql "+sql);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            while(rs.next()){ 
                HashMap<String, String> mapColumns = new HashMap<String, String>();
                for (int i = 1; i <= colCount; i++) {
                    System.out.println(metaData.getColumnLabel(i));
                    System.out.println(rs.getString(i));
                    mapColumns.put(metaData.getColumnLabel(i), rs.getString(i));
                }
                rtn.add(mapColumns);
            }   
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn;
    }
    
    public Map<String, String> readRegisterById(Map<String, String> viewRegister, Map<String, String> conditions){
        Map<String, String> rtn = new HashMap<String, String>();
        ResultSet rs;
        int rows = 0;
        ArrayList<String> fields = new ArrayList(viewRegister.keySet());
        String sql="";
        sql = createReadSQL(fields, conditions);
        //System.out.println("M readRegisterById => "+sql);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            while(rs.next()){ 
                for (int i = 1; i <= colCount; i++) {
                    String rsIndx = metaData.getColumnLabel(i);
                    if(Functions.hasColumn(rs, rsIndx)){
                        rtn.put(rsIndx, rs.getString(rsIndx));
                    }
                }
                rows++;
            }   
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("M readRegisterById rtn => "+rtn.toString());
        if (rows == 0){
            rtn.clear();
        }
        return rtn;
    }// fin readRegisterById
    
    public Map<String, String> readNavetionReg (String id, String goTo){
        int rows = 0;
        Map<String, String> rtn = new HashMap<String, String>();
        ResultSet rs;
        String sql;
        int row=1;
        sql = createNavegateSQL(id, goTo);
        //System.out.println("model 233 readNav "+sql);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            //System.out.println("model 236  ");
            ResultSetMetaData metaData = rs.getMetaData();
            //System.out.println("model 238  ");
            int colCount = metaData.getColumnCount();
            //System.out.println("model 240 colCount "+colCount);
            while(rs.next()){
                for (int col = 1; col <= colCount; col++) {
                    //System.out.println("column : "+metaData.getColumnName(col)+ " value : "+rs.getString(metaData.getColumnName(col)));
                    rtn.put( metaData.getColumnName(col), rs.getString(metaData.getColumnName(col)));
                }
                rows++;
            } 
            //System.out.println("tm 244 filas "+rows);
            if(rows == 0){
                //System.out.println("tm 246 no va ser que entre aquí ");
                rtn.clear();
            }
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn;
    }
    
    public int getMaxId(){
        int rtn = 0;
        ResultSet rs;
        String sql;
        sql = "SELECT MAX("+this.tableKeys.get(0)+") as id FROM "+this.tableName;
        //System.out.println("sql "+sql);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            //System.out.println("rs size "+colCount);
            if(rs.next()){ 
                rtn = rs.getInt("id");
                //System.out.println("while ");
                //System.out.println("rtn "+rtn);
            } else{
                rtn = 0;
            }
                
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn;
    }
    
    public String getMaxIdString(){
        String rtn = new String();
        ResultSet rs;
        String sql;
        sql = "SELECT MAX("+this.tableKeys.get(0)+") as id FROM "+this.tableName;
        //System.out.println("sql "+sql);
        try {
            rs = DBConexion.ejecuteSQL(sql); //Esto devuelve un ResultSet
            
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            //System.out.println("rs size "+colCount);
            if(rs.next()){ 
                rtn = rs.getString("id");
                //System.out.println("while ");
                //System.out.println("rtn "+rtn);
            } else{
                rtn = "Vacio";
            }
                
        } //fin deleteRegister
        catch (SQLException ex) {
            Logger.getLogger(DBTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtn;
    }
    
    
    /*=======================MÉTODOS PARA CREAR LISTAS=========================*/
    public String createKeyListString(ArrayList<String> columnNames){
        String rtn;
        int rows, row;
        rows = columnNames.size();
        row = 1;
        rtn="";
        // Declaramos el Iterador y citamos los campos solicitados como Elementos del ArrayList
        Iterator<String> arrayIterator = columnNames.iterator();
        while(arrayIterator.hasNext()){
            String elemento = arrayIterator.next();
            rtn += elemento;
            if(row < rows){
                rtn += ", ";
            }//fin if
            row++;
        }//fin while
        return rtn;
    }//fin createKeyListString
    
    public String createValueListString(Map<String, String> whereClause, String separator){
        String rtn="";
        int rows, row;
        rows = whereClause.size();
        if(rows > 0){
            row = 1;
            for (Map.Entry<String, String> entry : whereClause.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                rtn += matchValueType(key, value);
                if(row < rows){
                    rtn += " "+separator+" ";
                }//fin if
                row++;
            }
        }
        return rtn;
    }//fin createKeyListString
    
    private String createKeyValueSeparatedBy(Map<String, String> whereClause, String separator){
        String rtn="";
        int rows, row;
        rows = whereClause.size();
        if(rows > 0){
            row = 1;
            for (Map.Entry<String, String> entry : whereClause.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                rtn += matchKeyValue(key, "=", value);
                if(row < rows){
                    rtn += " "+separator+" ";
                }//fin if
                row++;
            }
        }
        return rtn;
    }//fin createWhereClause
        
    private String createUpdateSQL(Map<String, String> viewRegister, Map<String, String> whereClause){
        String sql="";
        int rows, row;
        rows = columnNames.size();
        row = 1;  
        sql = "UPDATE "+this.tableName+" SET ";
        sql += createKeyValueList(viewRegister, ",");
        sql += createWhereClause(whereClause);
        return sql;
    }//fin createUpdatetSQL
    
    
    private String createReadSQL(ArrayList<String> columnNames, Map<String, String> whereClause){
        String sql;
        int rows, row;
        rows = columnNames.size();
        row = 1;
        sql="SELECT ";
        sql += createSelectList(columnNames);     
        sql += " FROM "+this.tableName; 
        sql += createWhereClause(whereClause);
        return sql;
    }//fin createDeletetSQL
    
    
    private String createNavegateSQL(String currentId, String position){
        String sql;
        int rows, row;
        rows = columnNames.size();
        row = 1;
        sql="SELECT * ";
        sql += " FROM "+this.tableName;          
        //Contrucción de la cláusula WHERE
        if(position.equalsIgnoreCase("NEXT")){
            // Here aList is an ArrayList of ArrayLists
            ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>(3);
            ArrayList<String> aRow;
            aRow = new ArrayList<String>();
            aRow.add(this.tableKeys.get(0));
            aRow.add(">");
            aRow.add(currentId);
            aList.add(aRow);
            sql += createWhereClause3(aList);
            sql += " ORDER BY "+this.tableKeys.get(0)+" ASC";
        }
        if(position.equalsIgnoreCase("PRIOR")){
           // Here aList is an ArrayList of ArrayLists
            ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>(3);
            ArrayList<String> aRow;
            aRow = new ArrayList<String>();
            aRow.add(this.tableKeys.get(0));
            aRow.add("<");
            aRow.add(currentId);
            aList.add(aRow);
            sql += createWhereClause3(aList);
            sql += " ORDER BY "+this.tableKeys.get(0)+" DESC";
        }
        if(position.equalsIgnoreCase("FIRST")){
            sql += " ORDER BY "+this.tableKeys.get(0)+" ASC";
        }
        if(position.equalsIgnoreCase("LAST")){
            sql += " ORDER BY "+this.tableKeys.get(0)+" DESC";
        }
        sql += " LIMIT 1 ";
        //System.out.println("SQL = "+sql);
        return sql;
    }//fin createDeletetSQL
    
    private String matchKeyValue(String key, String match, String value){
        match = match.trim().equals("") ? " = " : " " + match.trim() + " ";
        int index;
        String rtn, fieldType;
        rtn = "";
        fieldType = "";
        if(this.columnNames.contains(key)){
            index = this.columnNames.indexOf(key);
            fieldType = this.columnTypes.get(index);
            System.out.println("fieltype "+fieldType);
            System.out.println("match "+match);
             System.out.println("key "+key);
             System.out.println("value "+value);
            switch (fieldType) {
                case "int":
                    value = (value.equals(""))? "0": value;
                    rtn = key + match + value;
                    break;
                case "bigint":
                    value = (value.equals(""))? "0": value;
                    rtn = key + match + value;
                    break;
                case "integer":
                    value = (value.equals(""))? "0": value;
                    rtn = key + match + value;
                    break;
                case "decimal":
                    value = (value.equals(""))? "0": value;
                    rtn = key + match + value;
                    break;
                case "double precision":
                    value = (value.equals(""))? "0": value;
                    rtn = key + match + value;
                    break;
                case "date":
                    rtn = key + match + "'"+ value+"'";
                    break;
                case "varchar":
                    rtn = key + match + "'"+ value+"'";
                    break;
                case "timestamp":
                    rtn = key + match + "'"+ value+"'";
                    break;
                case "datetime":
                    rtn = key + match + "'"+ value+"'";
                    break;
                case "character varying":
                    rtn = key + match + "'"+ value+"'";
                    break;
            }//end swich
        }//end if
        System.out.println("El retorn  "+rtn);
        return rtn;
    }//end matchKeyValue
    
 
    
    private String createWhereClause(Map<String, String> whereClause){
        String rtn="";
        int rows, row;
        rows = whereClause.size();
        if(rows > 0){
            row = 1;
            rtn += " WHERE ";
            for (Map.Entry<String, String> entry : whereClause.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("M createWhereClause key: "+key+" value: "+value);
                rtn += matchKeyValue(key, "=", value);
                if(row < rows){
                    rtn += " AND ";
                }//fin if
                row++;
            }
        }
        return rtn;
    }//fin createWhereClause
 
    private String createKeyValueList(Map<String, String> whereClause, String separator){
        String rtn="";
        boolean contain = false;
        int rows, row, listRow;
        rows = whereClause.size();
        row = 1;
        Iterator<String> arrayIterator = this.tableKeys.iterator();
        for (Map.Entry<String, String> entry : whereClause.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(!this.tableKeys.contains(key)){
               rtn += matchKeyValue(key, "=", value);
                if(row < rows){
                    rtn += " "+separator+" ";
                }//fin if
            }
            row++;
        }  
        //System.out.println("C createSetFieldsValues  return "+rtn);
        return rtn;
    }//fin createWhereClause
        
    private String createWhereClause3(ArrayList<ArrayList<String>> whereClause){
        String rtn="";
        int rows, row;
        rows = whereClause.size();
        if(rows > 0){
            row = 1;
            rtn += " WHERE ";
            for (int i = 0; i < whereClause.size(); i++) {
                //rtn += whereClause.get(i).get(0)+" "+whereClause.get(i).get(1)+" "+whereClause.get(i).get(2) ;
                rtn += matchKeyValue( whereClause.get(i).get(0), whereClause.get(i).get(1), whereClause.get(i).get(2));
                if(row < rows){
                    rtn += " AND ";
                }//fin if
                row++;
            }
        }
        return rtn;
    }//fin createWhereClause3
    

    private String createSelectList(ArrayList<String> columnNames){
        String rtn;
        int rows, row;
        rows = columnNames.size();
        row = 1;
        rtn="";
        // Declaramos el Iterador y citamos los campos solicitados como Elementos del ArrayList
        Iterator<String> arrayIterator = columnNames.iterator();
        while(arrayIterator.hasNext()){
            String elemento = arrayIterator.next();
            rtn += elemento; 
            if(row < rows){
                rtn += ", ";
            }//fin if
            row++;
        }//fin while
        return rtn;
    }//fin createSelectList
    
    public String matchValueType(String key, String value){
        int index;
        String rtn, fieldType;
        rtn = "";
        fieldType = "";
        if(this.columnNames.contains(key)){
            index = this.columnNames.indexOf(key);
            fieldType = this.columnTypes.get(index);
            //System.out.println("clave "+key+" valor "+value+" tipo "+fieldType);
            switch (fieldType) {
                case "int":
                    rtn = (value.equals(""))? "0": value;
                    break;
                case "bigint":
                    rtn = (value.equals(""))? "0": value;
                    break;
                case "integer":
                    rtn = (value.equals(""))? "0": value;
                    break;
                case "decimal":
                    rtn = (value.equals(""))? "0": value;
                    break;
                case "double precision":
                    rtn = (value.equals(""))? "0": value;
                    break;
                case "date":
                    rtn = "'"+ value+"'";
                    break;
                case "varchar":
                    rtn = "'"+ value+"'";
                    break;
                case "timestamp":
                    rtn = "'"+ value+"'";
                    break;
                case "datetime":
                    rtn = "'"+ value+"'";
                    break;
                case "character varying":
                    rtn = "'"+ value+"'";
                    break;
            }//end swich
        }//end if
        return rtn;
    }//end matchKeyValue
    
    /***
     * Método verica que solo queden aquellas claves del Map que se corresponden con los campos
     * de la tabla de la bd. Puede pedirse sólo las prímary key
     * @param myMap Map que se envía desde la vista, puede tener claves extras 
     * @return rtn Map de claves que son iguales a los campos de la tabla. 
     * Puede que no sea la totalidad de los capos.
     */
    public Map<String, String> justTableFields(Map<String, String> myMap, boolean justPrimayKeys){
        Map<String, String> rtn;
        rtn = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(this.columnNames.contains(key)){
                if(justPrimayKeys && this.tableKeys.contains(key)){                 
                    rtn.put(key, value);
                    continue;
                }else{
                    rtn.put(key, value);
                }
            }else{
                continue;
            } 
        }  
        return rtn;
    }
}
