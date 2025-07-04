package Filtros;

import javax.swing.text.*;

public class RucDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);

        if (sb.toString().matches("^[0-9-]*$")) {
            super.insertString(fb, offset, string, attr);
        }
    }

@Override
public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
    StringBuilder sb = new StringBuilder();
    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
    if (text != null) {
        sb.replace(offset, offset + length, text);
    }

    if (sb.toString().matches("^[0-9-]*$")) {
        super.replace(fb, offset, length, text, attrs);
    }
}

    
    
}


