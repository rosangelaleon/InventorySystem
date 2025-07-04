package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {
        // Verificar si el texto a insertar contiene solo letras, espacios, la letra ñ y caracteres acentuados
        if (text.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+")) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        // Verificar si el texto a reemplazar contiene solo letras, espacios, la letra ñ y caracteres acentuados
        if (text.isEmpty() || text.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
