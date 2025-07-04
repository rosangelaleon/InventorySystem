/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author gusta
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CobroDetalle {
    private int id;
    private int idCobro;
    private int idCuentaCobrar;
    private String moneda;
    private int nroCuota;
    private Date fechaVencimiento;
    private BigDecimal importe;

    public CobroDetalle(int id, int idCobro, int idCuentaCobrar, String moneda, int nroCuota, Date fechaVencimiento, BigDecimal importe) {
        this.id = id;
        this.idCobro = idCobro;
        this.idCuentaCobrar = idCuentaCobrar;
        this.moneda = moneda;
        this.nroCuota = nroCuota;
        this.fechaVencimiento = fechaVencimiento;
        this.importe = importe;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(int idCobro) {
        this.idCobro = idCobro;
    }

    public int getIdCuentaCobrar() {
        return idCuentaCobrar;
    }

    public void setIdCuentaCobrar(int idCuentaCobrar) {
        this.idCuentaCobrar = idCuentaCobrar;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getNroCuota() {
        return nroCuota;
    }

    public void setNroCuota(int nroCuota) {
        this.nroCuota = nroCuota;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    // Métodos adicionales para obtener valores específicos

    public String getString(String key) {
        switch (key) {
            case "moneda":
                return moneda;
            case "nroCuota":
                return String.valueOf(nroCuota);
            default:
                return null;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        switch (key) {
            case "importe":
                return importe;
            default:
                return null;
        }
    }

    public Date getDate(String key) {
        if ("fechaVencimiento".equals(key)) {
            return fechaVencimiento;
        }
        return null;
    }

    public int getInt(String key) {
        switch (key) {
            case "id":
                return id;
            case "idCobro":
                return idCobro;
            case "idCuentaCobrar":
                return idCuentaCobrar;
            case "nroCuota":
                return nroCuota;
            default:
                return -1; // Retorna -1 si no coincide ninguna clave
        }
    }

    // Método para convertir el objeto a un Map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("idCobro", idCobro);
        map.put("idCuentaCobrar", idCuentaCobrar);
        map.put("moneda", moneda);
        map.put("nroCuota", nroCuota);
        map.put("fechaVencimiento", fechaVencimiento);
        map.put("importe", importe);
        return map;
    }
    }