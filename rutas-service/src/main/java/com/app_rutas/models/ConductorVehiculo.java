package com.app_rutas.models;


public class ConductorVehiculo {
    private Integer id;
    private Integer idVehiculo;
    private Integer idConductor;
    private String fechaAsignacion;
    private String fechaDeBaja;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVehiculo() {
        return this.idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdConductor() {
        return this.idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public String getFechaAsignacion() {
        return this.fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getFechaDeBaja() {
        return this.fechaDeBaja;
    }

    public void setFechaDeBaja(String fechaDeBaja) {
        this.fechaDeBaja = fechaDeBaja;
    }
}