
package Modelo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ModeloTabla extends DefaultTableModel {
    String[] titulos;
    Object[][] datos;
    ArrayList<Integer> noEditable;

    public ModeloTabla(Object[][] datos, String[] titulos, ArrayList<Integer> noEditable) {
        super();
        this.titulos = titulos;
        this.datos = datos;
        this.noEditable = noEditable;
        setDataVector(datos, titulos);
    }

    public ModeloTabla() {
        // Constructor vacío
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return !noEditable.contains(column);
    }

    @Override
    public void addRow(Object[] rowData) {
        addRow(convertToVector(rowData));
    }

    @Override
    public void removeRow(int row) {
        dataVector.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }
}