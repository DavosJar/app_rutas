package com.app_rutas.models.enums;

public enum ConductorTurnoEnum {
    MATUTINO("MATUTINO"),
    VESPERTINO("VESPERTINO"),
    NOCTURNO("NOCTURNO"),
    MIXTO("MIXTO");

    private String estado;

    private ConductorTurnoEnum(String estado) {
        this.estado = estado;
    }

    public String getTurno() {
        return this.estado;
    }
}