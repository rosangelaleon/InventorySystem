package Modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CompraDetalle {
    private int id;
    private int compra_id;
    private String productodetalle_id;
    private BigDecimal cantidad;
    private BigDecimal precio;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private BigDecimal base;
    private BigDecimal total;
    private String lote;
    private Date vencimiento; // Cambiado de String a Date

    public CompraDetalle(int id, int compra_id, String productodetalle_id, BigDecimal cantidad, BigDecimal precio, BigDecimal impuesto, BigDecimal descuento, BigDecimal base, BigDecimal total, String lote, Date vencimiento) {
        this.id = id;
        this.compra_id = compra_id;
        this.productodetalle_id = productodetalle_id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.base = base;
        this.total = total;
        this.lote = lote;
        this.vencimiento = vencimiento;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getCompra_id() {
        return compra_id;
    }

    public String getProductodetalle_id() {
        return productodetalle_id;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getBase() {
        return base;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getLote() {
        return lote;
    }

    public Date getVencimiento() { // Devuelve Date
        return vencimiento;
    }

    public String getString(String key) {
        switch (key) {
            case "productodetalle_id":
                return productodetalle_id;
            case "lote":
                return lote;
            default:
                return null;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        switch (key) {
            case "cantidad":
                return cantidad;
            case "precio":
                return precio;
            case "impuesto":
                return impuesto;
            case "descuento":
                return descuento;
            case "base":
                return base;
            case "total":
                return total;
            default:
                return BigDecimal.ZERO;
        }
    }

    public Date getDate(String key) { // Nuevo método para obtener fechas
        if ("vencimiento".equals(key)) {
            return vencimiento;
        }
        return null;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("compra_id", compra_id);
        map.put("productodetalle_id", productodetalle_id);
        map.put("cantidad", cantidad);
        map.put("precio", precio);
        map.put("impuesto", impuesto);
        map.put("descuento", descuento);
        map.put("base", base);
        map.put("total", total);
        map.put("lote", lote);
        map.put("vencimiento", vencimiento); // Almacena Date directamente
        return map;
    }
}
