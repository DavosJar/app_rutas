package com.app_rutas.models;

import com.app_rutas.models.enums.ItinerarioEstadoEnum;

public class Itinerario {
    private Integer id;
    private Integer idConductorAsignado;
    private String fechaGeneracion;
    private String detallesEntrega;
    private ItinerarioEstadoEnum estado;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdConductorAsignado() {
        return this.idConductorAsignado;
    }

    public void setIdConductorAsignado(Integer idConductorAsignado) {
        this.idConductorAsignado = idConductorAsignado;
    }
    public String getFechaGeneracion() {
        return this.fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getDetallesEntrega() {
        return this.detallesEntrega;
    }

    public void setDetallesEntrega(String detallesEntrega) {
        this.detallesEntrega = detallesEntrega;
    }

    public ItinerarioEstadoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(ItinerarioEstadoEnum estado) {
        this.estado = estado;
    }
}