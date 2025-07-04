
package Controllers;

import com.toedter.calendar.IDateEditor;
import Controllers.DBConexion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JComboBox;
//import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;
public class Functions {
    private static ResultSet rs_suc;
    
    public static void e_estado(JComboBox<String> cb, String aTabla, String arg) {
        try {
            rs_suc = DBConexion.ejecuteSQL("SELECT * FROM " + aTabla + " WHERE " + arg + " ORDER BY 1");
            if (!rs_suc.first()) {
                return;
            }

            cb.setSelectedItem(rs_suc.getString(1) + "-" + rs_suc.getString(2));
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "No se pudo recuperar el registro. - ERROR: " + erro);
        }
    }
    //protected IDateEditor dateEditor;
    public static void E_estado (javax.swing.JComboBox cb, String aTabla, String arg){
       try{
            //javax.swing.JComboBox cb_carga;
            //cb_carga = cb;
            rs_suc = DBConexion.ejecuteSQL("SELECT * FROM "+aTabla+" WHERE " + arg+" ORDER BY 1");
            if (!rs_suc.first()){
                return;
            }

            cb.setSelectedItem(rs_suc.getString(1)+"-"+rs_suc.getString(2));
  
        }catch(SQLException erro){
            JOptionPane.showMessageDialog(null,  "No se pudo recuperar el registro. - ERROR: "+erro);
        }
    }
    
    public static String ExtraeCodigo(String args){
        String codigo="";
        String caracter;
        for(int i=0; i< args.length(); i++){
            caracter = args.substring(i, i+1);
            if(caracter.equals("-")){
               break;
            }else{
              codigo = codigo + caracter;
            }
        }
        if (codigo.length()<=0){
        codigo = "0";
        }
        return codigo;
    }
   
    public boolean fechaCorrecta(String fecha){ 
        
        if((fecha.substring(2,3)).compareTo("/")==0){ 
            int año = Integer.parseInt(fecha.substring(6)); 
            int mes = Integer.parseInt(fecha.substring(3,5)); 
            int dia = Integer.parseInt(fecha.substring(0,2)); 
            if (año > 1900) { 
                if (mes > 0 && mes < 13) { 
                    int tope; 
                    if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) { 
                        tope = 31; 
                    }else if (mes == 2){ 
                        if (año % 4 == 0) { 
                            tope = 29; //es bisiesto 
                        } else tope = 28;                         
                    } else tope = 30; 
                    if (dia > 0 && dia < tope + 1) { 
                        return true; 
                    }else{
                        JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
                        return false;
                    }
                }else{
                    JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
                    return false;
                }
            }else{
                JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
            return false;
        }
    }//fin fechacorrecta

    public static boolean hasColumn(ResultSet rs, String column){
        try{
            rs.findColumn(column);
            return true;
        } catch (SQLException sqlex){
            //JOptionPane.showMessageDialog(null,  "Fecha incorrecta ");
            return false;
        }
        //return false;
    } 
    
    public static String encryptMD5(String psw){
        String rtn="";
        String password = psw;
        String encryptedpassword = null;
        try{
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
  
            }
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        rtn = encryptedpassword;
        //System.out.println("Text password: "+password);
        //System.out.println("Encrypted password MD5: "+encryptedpassword);
        return rtn;
    }
   
    public static double sGetDecimalStringAnyLocaleAsDouble (String value) {        
        if (value == null) {
            //System.out.println("CORE - Null value!");
            return 0.0;
        }

        Locale theLocale = Locale.getDefault();
        NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
        Number theNumber;
        try {
            theNumber = numberFormat.parse(value);
            return theNumber.doubleValue();
        } catch (ParseException e) {
            // The string value might be either 99.99 or 99,99, depending on Locale.
            // We can deal with this safely, by forcing to be a point for the decimal separator, and then using Double.valueOf ...
            //http://stackoverflow.com/questions/4323599/best-way-to-parsedouble-with-comma-as-decimal-separator
            String valueWithDot = value.replaceAll(",",".");

            try {
              return Double.valueOf(valueWithDot);
            } catch (NumberFormatException e2)  {
                // This happens if we're trying (say) to parse a string that isn't a number, as though it were a number!
                // If this happens, it should only be due to application logic problems.
                // In this case, the safest thing to do is return 0, having first fired-off a log warning.
                System.out.println("CORE - Warning: Value is not a number" + value);
                return 0.0;
            }
        }
    }
    
    public static String decimalFormat(double numb){
        String rtn;
        DecimalFormat formatea = new DecimalFormat("#,###.##");
        rtn = formatea.format(numb);
        return rtn;
    }
     /**
     * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
     * 2004" if this is the selected date and locale is English.
     *
     * @param dfString
     *            The dateFormatString to set.
     */
//    public void setDateFormatString(String dfString){
//        dateEditor.setDateFormatString (dfString);    
//    }
    /**
     * Gets the date format string.
     *
     * @return Returns the dateFormatString.
     */
//    public String getDateFormatString(){
//        return dateEditor.getDateFormatString();
//    }
    /**
     * Returns the date. If the JDateChooser is started with a null date and no
     * date was set by the user, null is returned.
     *
     * @return the current date
     */
//    public Date getDate(){
//        return dateEditor.getDate ();
//    }
    
    public static int validarPermiso(int idrol, String menu, String opcion){
        ResultSet sql;
        int valor = 0; 
            try{
                    sql = DBConexion.ejecuteSQL("SELECT "+opcion 
                    + " FROM sys_permisos WHERE rolid = "+idrol 
                    + " AND menuid = '"+menu+"'");
                    if (!sql.next()){
                            valor = 0;
                    } else{
                    valor = sql.getInt(opcion);

                    }
                    System.out.println("SQL FUNCION VALIDARPERMISO;" +sql);
            }catch(Exception error)
        {
        
    }
        return valor;
    
    }
    
    
    /**
     * 
     * @param origen int Id Moneda Origen
     * @param destino int Id Moneda Destino
     * @param fecha bigint/long fecha de la contización
     * @param importe double en la moneda origen
     * @param defecto cotización por defecto
     * @return double convertido a moneda destino y redondeado
     */
    public static double cambiarCotizacion(int origen, int destino, long fecha, double importe, double defecto){
        String sql;
        double total, cotizacion;
        BigDecimal rtn;
        int operador, decimales;
        
        operador = 0;
        decimales = 0;
        total = 0.0;
        sql = "";
        cotizacion = 0.0;
       
        if(origen == 0 || destino == 0){
            //Mensaje de no se encuentra Moenda origen o destino
            return 0.0;
        }
        if(importe == 0 && defecto > 0){
            importe = defecto;
        }
        if(origen == destino){
            //Las monedas origen y destino son iguales
            return importe;
        }
        
        try{
            sql = "SELECT cotizacion, operacion, decimales "
                    + "FROM SYS_COTIZACIONES c, SYS_MONEDAS m "
                    + "WHERE c.monedadestid = m.id "
                    + " AND monedaorigid = " + origen
                    + " AND monedadestid = "+destino
                    + " AND FROM_UNIXTIME(fecha, '%d/%m/%Y') = FROM_UNIXTIME("+fecha+", '%d/%m/%Y')";

            System.out.println("sql = "+sql);
            rs_suc = DBConexion.ejecuteSQL(sql);
            if (!rs_suc.first()){
                return 0.0;
            }
            operador = rs_suc.getInt("operacion"); //0 = multiplicar y 1 = Dividir
            cotizacion = rs_suc.getDouble("cotizacion");
            if(defecto > 0){
                cotizacion = defecto;
            }
            decimales = rs_suc.getInt("decimales");
            //Si rtn = 0 returnar algo...
        }catch(SQLException erro){
            JOptionPane.showMessageDialog(null,  "No se pudo recuperar el registro. - ERROR: "+erro);
        }
        //
        if(operador == 0){                //Multiplica
           total = cotizacion * importe;
           rtn = new BigDecimal(total).setScale(decimales, RoundingMode.HALF_UP); 
        }else{                            //Divide
           total = importe/cotizacion ;
           rtn = new BigDecimal(total).setScale(decimales, RoundingMode.HALF_UP);
        }
        System.out.println("de: "+origen+" a: "+destino+" oper "+operador+" cotiz: "+cotizacion+" rtn: "+total+" dec: "+decimales);
        return rtn.doubleValue();
    }
    
//    public static void addMessage(HttpServletRequest request, String msg) {
//        if (request.getAttribute("listaResultados") == null) {
//            ArrayList<String> lista = new ArrayList<String>();
//            lista.add(msg);
//            request.setAttribute("listaResultados", lista);
//        } else {
//            ArrayList<String> lista = (ArrayList<String>) request.getAttribute("listaResultados");
//            lista.add(msg);
//            request.setAttribute("listaResultados", lista);
//        }
//    }
    public static int createDV(String p_numero, int p_basemax) {
    int v_total, v_resto, k, v_numero_aux, v_digit;
    String v_numero_al = "";

    for (int i = 0; i < p_numero.length(); i++) {
        char c = p_numero.charAt(i);
        if (Character.isDigit(c)) {
            v_numero_al += c;
        } else {
            v_numero_al += (int) c;
        }
    }

    k = 2;
    v_total = 0;

    for (int i = v_numero_al.length() - 1; i >= 0; i--) {
        k = k > p_basemax ? 2 : k;
        v_numero_aux = v_numero_al.charAt(i) - 48;
        v_total += v_numero_aux * k++;
    }

    v_resto = v_total % 11;
    v_digit = v_resto > 1 ? 11 - v_resto : 0;
    return v_digit;
}
//Fin createDV
}
