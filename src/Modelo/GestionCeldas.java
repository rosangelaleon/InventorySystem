
package Modelo;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class GestionCeldas extends DefaultTableCellRenderer {

    private String tipo;

    // Constructor with the type of cell
    public GestionCeldas(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Customize cell based on the type
        if (tipo.equals("texto")) {
            setHorizontalAlignment(JLabel.LEFT);
        } else if (tipo.equals("numerico")) {
            setHorizontalAlignment(JLabel.RIGHT);
        } else if (tipo.equals("jComboBox")) {
            setHorizontalAlignment(JLabel.CENTER);
        }

        setText(value == null ? "" : value.toString());
        
        // Customizing the background color and foreground color
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this;
    }
}

