/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;

/**
 *
 * @author Delia Silva
 */
import javax.swing.text.*;

// Clase que extiende DocumentFilter para permitir solo dígitos
public class NumericDocumentFilter extends DocumentFilter {

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
        if (newText.matches("\\d*")) { // Solo permitir dígitos
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

