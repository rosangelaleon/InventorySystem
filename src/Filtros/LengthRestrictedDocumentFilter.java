/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.Toolkit;

public class LengthRestrictedDocumentFilter extends DocumentFilter {
    private final int maxLength;

    public LengthRestrictedDocumentFilter(int maxLength) {
        if (maxLength < 0) {
            throw new IllegalArgumentException("Maximum length must be non-negative");
        }
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument().getLength() + string.length()) <= maxLength) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if ((fb.getDocument().getLength() + text.length() - length) <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
