package Filtros;

import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditor extends AbstractCellEditor implements TableCellEditor {
    private JDateChooser dateChooser;
    private SimpleDateFormat dateFormat;

    public DateEditor() {
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Establece el formato de la fecha
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public Object getCellEditorValue() {
        Date date = dateChooser.getDate();
        // Devuelve la fecha como cadena formateada
        return date != null ? dateFormat.format(date) : "";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value != null) {
            if (value instanceof Date) {
                dateChooser.setDate((Date) value); // Si es un objeto Date, lo establece
            } else if (value instanceof String) {
                try {
                    // Intenta parsear la cadena a un objeto Date
                    dateChooser.setDate(dateFormat.parse((String) value));
                } catch (Exception e) {
                    dateChooser.setDate(null); // Si hay un error, establece null
                }
            }
        } else {
            dateChooser.setDate(null); // Si no hay valor, establece null
        }
        return dateChooser;
    }
}
