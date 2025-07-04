/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;

/**
 *
 * @author Delia Silva
 */
import javax.swing.*;
import javax.swing.text.*;

public class Numericos {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creamos un JTextField
        JTextField textField = new JTextField(10);

        // Aplicamos el DocumentFilter creado anteriormente al JTextField
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        // Añadimos el JTextField a la ventana
        frame.getContentPane().add(textField);

        frame.pack();
        frame.setLocationRelativeTo(null); // Centramos la ventana en la pantalla
        frame.setVisible(true);
    }
}
