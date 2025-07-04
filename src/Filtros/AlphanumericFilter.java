package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AlphanumericFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {
        // Verificar si el texto a insertar contiene solo letras (sin ñ/Ñ) y números
        if (text.matches("[a-zA-Z0-9]+")) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        // Verificar si el texto a reemplazar contiene solo letras (sin ñ/Ñ) y números
        if (text.isEmpty() || text.matches("[a-zA-Z0-9]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
