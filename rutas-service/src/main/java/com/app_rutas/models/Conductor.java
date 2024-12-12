package com.app_rutas.models;

import com.app_rutas.models.enums.ConductorTurnoEnum;

public class Conductor extends Persona {
    private String licenciaConducir;
    private Float salario;
    private ConductorTurnoEnum estado;

    public Conductor() {
        super();
    }
    public String getLicenciaConducir() {
        return this.licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }

    public Float getSalario() {
        return this.salario;
    }

    public void setSalario(Float salario) {
        this.salario = salario;
    }

    public ConductorTurnoEnum getEstado() {
        return this.estado;
    }

    public void setEstado(ConductorTurnoEnum estado) {
        this.estado = estado;
    }
}