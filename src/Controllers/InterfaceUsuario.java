/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Controllers;

import java.util.HashMap;
import java.util.Map;

public interface InterfaceUsuario {
// Método para crear un registro (CRUD: Create)
    
    // Método para guardar un registro (CRUD: Create) 
    int imGuardar(String crud);

    // Método para borrar un registro (CRUD: Delete)
    int imBorrar(String crud);

    // Método para preparar la creación de un nuevo registro
    int imNuevo();

    // Método para buscar un registro (CRUD: Read)
      int imBuscar();


 
   

    // Método para filtrar registros según ciertos criterios
    int imFiltrar();

    // Método para navegar al primer registro
    int imPrimero();

    // Método para navegar al siguiente registro
    int imSiguiente();

    // Método para navegar al registro anterior
    int imAnterior();

    // Método para navegar al último registro
    int imUltimo();

    // Método para imprimir el registro actual o un conjunto de registros
    int imImprimir();

    // Método para insertar filas en una tabla o estructura similar
    int imInsFilas();

    // Método para eliminar filas de una tabla o estructura similar
    int imDelFilas();

    public void onItemSeleccionado(Map<String, String> datosSeleccionados);
    
}