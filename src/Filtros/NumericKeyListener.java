package Filtros;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class NumericKeyListener extends KeyAdapter {
    private int decimalPlaces;

    public NumericKeyListener(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        JTextField textField = (JTextField) e.getSource();
        String text = textField.getText();

        // Allow only digits, decimal point, backspace, and delete
        if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
            e.consume();
            return;
        }

        // Ensure only one decimal point is allowed
        if (c == '.' && text.contains(".")) {
            e.consume();
            return;
        }

        // Ensure the correct number of decimal places
        if (text.contains(".")) {
            int index = text.indexOf('.');
            if (text.substring(index + 1).length() >= decimalPlaces && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                e.consume();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        JTextField textField = (JTextField) e.getSource();
        String text = textField.getText();

        // Ensure only valid numeric characters are present
        if (!text.matches("\\d*(\\.\\d{0," + decimalPlaces + "})?")) {
            textField.setText(text.replaceAll("[^\\d.]", ""));
        }

        // If text is empty, reset to "0"
        if (text.isEmpty() || text.equals("0")) {
            textField.setText("0");
            textField.selectAll(); // Ensure the text is selected so it can be easily replaced
        }
    }
}
