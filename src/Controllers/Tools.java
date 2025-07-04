/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Controllers.DBConexion;
import static Controllers.DBConexion.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class Tools {

   // Método en la clase DBConexion para obtener los permisos de un rol
public static Set<String> obtenerPermisosDelRol(int rolId) {
    // Se crea un conjunto para almacenar los ID de los menús permitidos
    Set<String> permisos = new HashSet<>();
    
    // Se define la consulta SQL para obtener los permisos
    String sql = "SELECT menu_id FROM PERMISOS WHERE rol_id = ?";
    
    // Se utiliza un try-with-resources para asegurar que los recursos se cierren después de su uso
    try (PreparedStatement pstm = con.prepareStatement(sql)) {
        // Se establece el ID del rol en la consulta
        pstm.setInt(1, rolId);
        
        // Se ejecuta la consulta
        try (ResultSet resultset = pstm.executeQuery()) {
            // Se recorren los resultados y se agregan al conjunto
            while (resultset.next()) {
                permisos.add(resultset.getString("menu_id"));
            }
        }
    } catch (SQLException e) {
        // Manejo de la excepción
        System.out.println("Error al obtener permisos: " + e.getMessage());
        JOptionPane.showMessageDialog(null, "Error al obtener permisos del rol: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Se devuelve el conjunto con los permisos
    return permisos;
}
}