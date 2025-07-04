package Modelo;

import java.util.HashMap;
import java.util.Map;

public class AjusteDetalle {
    private int id;
    private int ajuste_id;
    private String productodetalle_id;
    private int cantidad_actual; // Ahora es int
    private int cantidad_ajuste; // Ahora es int
    private String lote;
    private String lotevence;

    // Constructor
    public AjusteDetalle(int id, int ajuste_id, String productodetalle_id, int cantidad_actual, int cantidad_ajuste, String lote, String lotevence) {
        this.id = id;
        this.ajuste_id = ajuste_id;
        this.productodetalle_id = productodetalle_id;
        this.cantidad_actual = cantidad_actual;
        this.cantidad_ajuste = cantidad_ajuste;
        this.lote = lote;
        this.lotevence = lotevence;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAjuste_id() {
        return ajuste_id;
    }

    public String getProductodetalle_id() {
        return productodetalle_id;
    }

    public int getCantidad_actual() {
        return cantidad_actual;
    }

    public int getCantidad_ajuste() {
        return cantidad_ajuste;
    }

    public String getLote() {
        return lote;
    }

    public String getLotevence() {
        return lotevence;
    }

    // Método de acceso dinámico para Strings
    public String getString(String key) {
        switch (key) {
            case "productodetalle_id":
                return productodetalle_id;
            case "lote":
                return lote;
            case "lotevence":
                return lotevence;
            default:
                return null;
        }
    }

    // Método de acceso dinámico para enteros
    public int getInteger(String key) {
        switch (key) {
            case "cantidad_actual":
                return cantidad_actual;
            case "cantidad_ajuste":
                return cantidad_ajuste;
            case "ajuste_id":
                return ajuste_id;
            default:
                return 0;
        }
    }

    // Método para convertir el objeto en un mapa
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("ajuste_id", ajuste_id);
        map.put("productodetalle_id", productodetalle_id);
        map.put("cantidad_actual", cantidad_actual);
        map.put("cantidad_ajuste", cantidad_ajuste);
        map.put("lote", lote);
        map.put("lotevence", lotevence);
        return map;
    }
}
