package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AlphaNumericDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        if (isValidText(text)) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (isValidText(text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isValidText(String text) {
        // Permitir letras, números, espacios y algunos caracteres especiales
        return text.matches("[a-zA-Z0-9\\s!@#$%^&*()_+=\\-{}|:\"<>?,./]*");
    }
}
