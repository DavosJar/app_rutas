package com.app_rutas.models;

import com.app_rutas.models.enums.ConductorTurnoEnum;
import com.app_rutas.models.enums.EstadoConductor;

public class Conductor extends Trabajador {
    private String licenciaConducir;
    private String caducidadLicencia;
    private Float salario;
    private ConductorTurnoEnum turno;
    private EstadoConductor estado;

    public Conductor() {
        super();
    }
    public String getLicenciaConducir() {
        return this.licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }
    public String getCaducidadLicencia() {
        return this.caducidadLicencia;
    }

    public void setCaducidadLicencia(String caducidadLicencia) {
        this.caducidadLicencia = caducidadLicencia;
    }

    public Float getSalario() {
        return this.salario;
    }

    public void setSalario(Float salario) {
        this.salario = salario;
    }

    public ConductorTurnoEnum getTurno() {
        return this.turno;
    }

    public void setTurno(ConductorTurnoEnum turno) {
        this.turno = turno;
    }

    public EstadoConductor getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoConductor estado) {
        this.estado = estado;
    }
}