
package Modelo;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class GestionEncabezadoTabla extends DefaultTableCellRenderer {

    public GestionEncabezadoTabla() {
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }
        setText(value == null ? "" : value.toString());
        return this;
    }
}
