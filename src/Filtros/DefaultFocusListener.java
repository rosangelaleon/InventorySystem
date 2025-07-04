package Filtros;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

public class DefaultFocusListener extends FocusAdapter {
    private JTextField textField;
    private boolean enforceNonEmpty; // Agregar un flag para controlar el comportamiento.

    public DefaultFocusListener(JTextField textField, boolean enforceNonEmpty) {
        if (textField == null) {
            throw new IllegalArgumentException("Text field cannot be null");
        }
        this.textField = textField;
        this.enforceNonEmpty = enforceNonEmpty;
        addMouseListenerToTextField();
    }

    private void addMouseListenerToTextField() {
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (textField != null) {
                    textField.selectAll();
                }
            }
        });
    }

    @Override
    public void focusGained(FocusEvent e) {
        textField.selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Solo establecer "0" si el flag está activo y el campo está vacío.
        if (enforceNonEmpty && textField.getText().isEmpty()) {
            textField.setText("0");
        }
    }
}
