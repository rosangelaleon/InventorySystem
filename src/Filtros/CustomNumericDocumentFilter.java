package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CustomNumericDocumentFilter extends DocumentFilter {
    private String decimalRegex;

    public CustomNumericDocumentFilter(int decimalPlaces) {
        this.decimalRegex = "\\d*(\\.\\d{0," + decimalPlaces + "})?";
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        String text = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (text.matches(decimalRegex)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String docText = fb.getDocument().getText(0, fb.getDocument().getLength());
        docText = docText.substring(0, offset) + text + docText.substring(offset + length);
        if (docText.matches(decimalRegex)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        String docText = fb.getDocument().getText(0, fb.getDocument().getLength());
        docText = docText.substring(0, offset) + docText.substring(offset + length);
        if (docText.isEmpty() || docText.matches(decimalRegex)) {
            super.remove(fb, offset, length);
        }
    }
}