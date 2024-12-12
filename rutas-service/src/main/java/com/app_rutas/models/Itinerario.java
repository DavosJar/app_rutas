package com.app_rutas.models;

import com.app_rutas.models.enums.ItinerarioEstadoEnum;

public class Itinerario {
    private Integer id;
    private Integer idConductorAsignado;
    private String horaIncio;
    private String duracionEstimada;
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

    public String getHoraIncio() {
        return this.horaIncio;
    }

    public void setHoraIncio(String horaIncio) {
        this.horaIncio = horaIncio;
    }

    public String getDuracionEstimada() {
        return this.duracionEstimada;
    }

    public void setDuracionEstimada(String duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public ItinerarioEstadoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(ItinerarioEstadoEnum estado) {
        this.estado = estado;
    }
}