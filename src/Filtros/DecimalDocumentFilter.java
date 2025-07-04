package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

public class DecimalDocumentFilter extends DocumentFilter {
    private final int decimalPlaces;
    private final Pattern pattern;

    public DecimalDocumentFilter(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        if (decimalPlaces == 0) {
            this.pattern = Pattern.compile("-?\\d*"); // Solo permite dígitos enteros
        } else {
            this.pattern = Pattern.compile("-?\\d*(\\.\\d{0," + decimalPlaces + "})?");
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String newText = getNewText(fb, offset, 0, string);
        if (pattern.matcher(newText).matches()) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String newText = getNewText(fb, offset, length, text);
        if (pattern.matcher(newText).matches()) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        String newText = getNewText(fb, offset, length, "");
        if (pattern.matcher(newText).matches() || newText.isEmpty()) {
            super.remove(fb, offset, length);
        }
    }

    private String getNewText(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        return currentText.substring(0, offset) + text + currentText.substring(offset + length);
    }
}
