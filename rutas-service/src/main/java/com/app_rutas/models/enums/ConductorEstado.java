package com.app_rutas.models.enums;

public enum ConductorEstado {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO"),
    SUSPENDIDO("SUSPENDIDO"),
    BAJA("BAJA");

    private String estado;

    private ConductorEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return this.estado;
    }
}