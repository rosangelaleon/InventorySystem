/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DescriptionFilter extends DocumentFilter {
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {
        if (text == null) return;
        if (text.matches("[\\p{L}\\p{N}\\p{Punct}\\s]+")) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        if (text.isEmpty() || text.matches("[\\p{L}\\p{N}\\p{Punct}\\s]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
