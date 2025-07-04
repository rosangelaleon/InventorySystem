package Modelo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VentaDetalle {
    private int id;
    private int venta_id;
    private String productodetalle_id;
    private BigDecimal cantidad;
    private BigDecimal precio;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private BigDecimal base;
    private BigDecimal total;
    private int lote_id;
    private String vencimiento;

    public VentaDetalle(int id, int venta_id, String productodetalle_id, BigDecimal cantidad, BigDecimal precio, BigDecimal impuesto, BigDecimal descuento, BigDecimal base, BigDecimal total, int lote_id, String vencimiento) {
        this.id = id;
        this.venta_id = venta_id;
        this.productodetalle_id = productodetalle_id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.base = base;
        this.total = total;
        this.lote_id = lote_id;
        this.vencimiento = vencimiento;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getVenta_id() {
        return venta_id;
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

    public int getLote_id() {
        return lote_id;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public String getString(String key) {
        switch (key) {
            case "productodetalle_id":
                return productodetalle_id;
            default:
                return null;
        }
    }

    public int getInteger(String key) {
        switch (key) {
            case "lote_id":
                return lote_id;
            default:
                return 0;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("venta_id", venta_id);
        map.put("productodetalle_id", productodetalle_id);
        map.put("cantidad", cantidad);
        map.put("precio", precio);
        map.put("impuesto", impuesto);
        map.put("descuento", descuento);
        map.put("base", base);
        map.put("total", total);
        map.put("lote_id", lote_id);
        map.put("vencimiento", vencimiento);
        return map;
    }
}
