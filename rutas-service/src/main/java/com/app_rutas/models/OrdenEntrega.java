package com.app_rutas.models;

public class OrdenEntrega {
        private Integer id;
    public String codigoOrdenEntrega;
    private String horaMinima;
    private String horaMaxima;
    private String fechaEntrega;
    private String ubicacionActual;
    private EstadoEnum estado;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoOrdenEntrega() {
        return this.codigoOrdenEntrega;
    }

    public void setCodigoOrdenEntrega(String codigoOrdenEntrega) {
        this.codigoOrdenEntrega = codigoOrdenEntrega;
    }

    public String getHoraMinima() {
        return this.horaMinima;
    }

    public void setHoraMinima(String horaMinima) {
        this.horaMinima = horaMinima;
    }

    public String getHoraMaxima() {
        return this.horaMaxima;
    }

    public void setHoraMaxima(String horaMaxima) {
        this.horaMaxima = horaMaxima;
    }

    public String getFechaEntrega() {
        return this.fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUbicacionActual() {
        return this.ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public EstadoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }
}
