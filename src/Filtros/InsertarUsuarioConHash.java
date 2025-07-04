/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;


import Controllers.DBConexion;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertarUsuarioConHash {
    public static void main(String[] args) {
        String nombreUsuario = "Rosangela";
        String contraseñaOriginal = "Ini12345_";
        int rolId = 3; // Asegúrate de que este ID exista en la tabla de roles
        int activo = 1; // Estado activo

        // Generar hash de la contraseña
        String hashContraseña = BCrypt.hashpw(contraseñaOriginal, BCrypt.gensalt(12));

        // Sentencia SQL para insertar usuario
        String sql = "INSERT INTO USUARIOS (id, rol_id, usuario, contraseña, activo) VALUES (?, ?, ?, ?, ?)";

        try {
            if (DBConexion.Conectar()) {
                try (PreparedStatement pstmt = DBConexion.con.prepareStatement(sql)) {
                    // Establecer los parámetros para la inserción
                    pstmt.setInt(1, 1); // Asume que el id es generado automáticamente o es el siguiente en la secuencia
                    pstmt.setInt(2, rolId);
                    pstmt.setString(3, nombreUsuario);
                    pstmt.setString(4, hashContraseña);
                    pstmt.setInt(5, activo);

                    // Ejecutar la inserción
                    int resultado = pstmt.executeUpdate();
                    if (resultado > 0) {
                        System.out.println("Usuario insertado correctamente");
                    } else {
                        System.out.println("No se pudo insertar el usuario");
                    }
                } catch (SQLException e) {
                    System.err.println("Error al insertar el usuario: " + e.getMessage());
                } finally {
                    DBConexion.desconecta();
                }
            } else {
                System.out.println("No se pudo establecer la conexión con la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
