package com.app_rutas.models;

import com.app_rutas.models.enums.VehiculoEstadoEnum;

public class Vehiculo {
    private Integer id;
    private String marca;
    private String modelo;
    private String placa;
    private Integer capacidad;
    private Float potencia;
    private Float pesoMinimo;
    private Float pesoMaximo;
    private Boolean refrigerado;
    private VehiculoEstadoEnum estado;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return this.placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getCapacidad() {
        return this.capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Float getPotencia() {
        return this.potencia;
    }

    public void setPotencia(Float potencia) {
        this.potencia = potencia;
    }

    public Float getPesoMinimo() {
        return this.pesoMinimo;
    }

    public void setPesoMinimo(Float pesoMinimo) {
        this.pesoMinimo = pesoMinimo;
    }

    public Float getPesoMaximo() {
        return this.pesoMaximo;
    }

    public void setPesoMaximo(Float pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }

    public Boolean getRefrigerado() {
        return this.refrigerado;
    }

    public void setRefrigerado(Boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    public VehiculoEstadoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(VehiculoEstadoEnum estado) {
        this.estado = estado;
    }

}