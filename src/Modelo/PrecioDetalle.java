/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package Modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class PrecioDetalle {
    private int id;
    private String codigobarras;
    private BigDecimal precio;

    public PrecioDetalle(int id, String codigobarras, BigDecimal precio) {
        this.id = id;
        this.codigobarras = codigobarras;
        this.precio = precio;
    }

    // Getters y setters

    public String getString(String key) {
        switch (key) {
            case "codigobarras":
                return codigobarras;
            default:
                return null;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        switch (key) {
            case "precio":
                return precio;
            default:
                return null;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("codigobarras", codigobarras);
        map.put("precio", precio);
        return map;
    }
}
