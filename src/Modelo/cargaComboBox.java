
package Modelo;

import Controllers.DBConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class cargaComboBox {
    private static Connection conn = Controllers.DBConexion.con;

    public static void pv_cargar(JComboBox combo, String tabla, String campos, String codigo, String filtro) {
        combo.removeAllItems();
        combo.addItem("0-Seleccionar");

        String query = "SELECT " + campos + " FROM " + tabla;
        if (!filtro.isEmpty()) {
            query += " WHERE " + filtro;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            List<String> items = new ArrayList<>();
            while (rs.next()) {
                String item = rs.getString(codigo) + "-" + rs.getString(2);
                items.add(item);
            }

            for (String item : items) {
                combo.addItem(item);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el combo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
