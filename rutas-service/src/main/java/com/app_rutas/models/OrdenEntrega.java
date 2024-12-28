package com.app_rutas.models;

import com.app_rutas.models.enums.EstadoEnum;

public class OrdenEntrega {
    private Integer id;
    private Integer idPedido;
    private String fechaProgramada;
    private String horaProgramada;
    private String receptor;
    private String observaciones;
    private EstadoEnum estado;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getIdPedido() {
        return this.idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getFechaProgramada() {
        return this.fechaProgramada;
    }

    public void setFechaProgramada(String fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getHoraProgramada() {
        return this.horaProgramada;
    }

    public void setHoraProgramada(String horaProgramada) {
        this.horaProgramada = horaProgramada;
    }

    public String getReceptor() {
        return this.receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }
}
