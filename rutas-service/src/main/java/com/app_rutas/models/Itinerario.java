package com.app_rutas.models;

public class Itinerario {
    private Integer id;
    private String horaIncio;
    private String duracionEstimada;
    private ItinerarioEstadoEnum estado;
    private String conductorResponsable;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getConductorResponsable() {
        return this.conductorResponsable;
    }

    public void setConductorResponsable(String conductorResponsable) {
        this.conductorResponsable = conductorResponsable;
    }    
}