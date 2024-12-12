package com.app_rutas.models;
import com.app_rutas.models.enums.ContenidoEnum;
public class Pedido {
    private Integer id;
    private Integer idCliente;
    private String contenido;
    private String fechaRegistro;
    private Boolean requiereFrio;
    private Double volumenTotal;
    private Float pesoTotal;
    private ContenidoEnum estado;
    private Integer idOrdenEntrega;
    private Integer idPuntoEntrega;
    private Integer idItinerario;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getContenido() {
        return this.contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getRequiereFrio() {
        return this.requiereFrio;
    }

    public void setRequiereFrio(Boolean requiereFrio) {
        this.requiereFrio = requiereFrio;
    }

    public Double getVolumenTotal() {
        return this.volumenTotal;
    }

    public void setVolumenTotal(Double volumenTotal) {
        this.volumenTotal = volumenTotal;
    }

    public Float getPesoTotal() {
        return this.pesoTotal;
    }

    public void setPesoTotal(Float pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public ContenidoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(ContenidoEnum estado) {
        this.estado = estado;
    }

    public Integer getIdOrdenEntrega() {
        return this.idOrdenEntrega;
    }

    public void setIdOrdenEntrega(Integer idOrdenEntrega) {
        this.idOrdenEntrega = idOrdenEntrega;
    }

    public Integer getIdPuntoEntrega() {
        return this.idPuntoEntrega;
    }

    public void setIdPuntoEntrega(Integer idPuntoEntrega) {
        this.idPuntoEntrega = idPuntoEntrega;
    }

    public Integer getIdItinerario() {
        return this.idItinerario;
    }

    public void setIdItinerario(Integer idItinerario) {
        this.idItinerario = idItinerario;
    }
}
