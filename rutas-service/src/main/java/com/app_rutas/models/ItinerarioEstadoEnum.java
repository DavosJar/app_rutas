package com.app_rutas.models;

public enum ItinerarioEstadoEnum {
    PENDINENTE("PENDIENTE"),
    EN_PROGRESO("EN PROGRESO"),
    COMPLETADO("COMPLETADO"),
    CANCELADO("CANCELADO"),
    RE_PROGRAMADO("RE PROGRAMADO");

    private String estado;

    private ItinerarioEstadoEnum(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
