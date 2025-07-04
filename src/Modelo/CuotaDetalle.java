/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.HashMap;
import java.util.Map;

public class CuotaDetalle {
    private int id;
    private int cuota_id;
    private int cuota;
    private int dias;

    public CuotaDetalle(int id, int cuota_id, int cuota, int dias) {
        this.id = id;
        this.cuota_id = cuota_id;
        this.cuota = cuota;
        this.dias = dias;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public int getCuota_id() {
        return cuota_id;
    }

    public int getCuota() {
        return cuota;
    }

    public int getDias() {
        return dias;
    }

    public String getString(String key) {
        return String.valueOf(getInteger(key));
    }

    public int getInteger(String key) {
        switch (key) {
            case "id":
                return id;
            case "cuota_id":
                return cuota_id;
            case "cuota":
                return cuota;
            case "dias":
                return dias;
            default:
                return 0;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("cuota_id", cuota_id);
        map.put("cuota", cuota);
        map.put("dias", dias);
        return map;
    }
}