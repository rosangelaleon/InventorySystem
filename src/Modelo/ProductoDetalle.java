package Modelo;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductoDetalle {
    private int id; // Add an id field
    private int cabecera_id;
    private String codigobarras;
    private int color_id;
    private int tamano_id;
    private int diseno_id;
    private int moneda_id;
    private BigDecimal costo;
    private BigDecimal uxb;
    private BigDecimal stockminimo;

    public ProductoDetalle(int id, int cabecera_id, String codigobarras, int color_id, int tamano_id, int diseno_id, int moneda_id, BigDecimal costo, BigDecimal uxb, BigDecimal stockminimo) {
        this.id = id;
        this.cabecera_id = cabecera_id;
        this.codigobarras = codigobarras;
        this.color_id = color_id;
        this.tamano_id = tamano_id;
        this.diseno_id = diseno_id;
        this.moneda_id = moneda_id;
        this.costo = costo;
        this.uxb = uxb;
        this.stockminimo = stockminimo;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getCabecera_id() {
        return cabecera_id;
    }

    public String getCodigobarras() {
        return codigobarras;
    }

    public int getColor_id() {
        return color_id;
    }

    public int getTamano_id() {
        return tamano_id;
    }

    public int getDiseno_id() {
        return diseno_id;
    }

    public int getMoneda_id() {
        return moneda_id;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public BigDecimal getUxb() {
        return uxb;
    }

    public BigDecimal getStockminimo() {
        return stockminimo;
    }

    public String getString(String key) {
        switch (key) {
            case "codigobarras":
                return codigobarras;
            default:
                return null;
        }
    }

    public int getInteger(String key) {
        switch (key) {
            case "color_id":
                return color_id;
            case "tamano_id":
                return tamano_id;
            case "diseno_id":
                return diseno_id;
            case "moneda_id":
                return moneda_id;
            default:
                return 0;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        switch (key) {
            case "uxb":
                return uxb;
            case "costo":
                return costo;
            case "stockminimo":
                return stockminimo;
            default:
                return BigDecimal.ZERO;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id); // Add id to the map
        map.put("cabecera_id", cabecera_id);
        map.put("codigobarras", codigobarras);
        map.put("color_id", color_id);
        map.put("tamano_id", tamano_id);
        map.put("diseno_id", diseno_id);
        map.put("moneda_id", moneda_id);
        map.put("costo", costo);
        map.put("uxb", uxb);
        map.put("stockminimo", stockminimo);
        return map;
    }
}
