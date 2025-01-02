package com.app_rutas.models.enums;

public enum Rol {
    ADMINISTRADOR("ADMINISTRADOR"),
    CONDUCTOR("CONDUCTOR"),
    GESTOR_PEDIDOS("GESTOR_PEDIDOS"),
    OPERADOR_FLOTA("OPERADOR_FLOTA");

    private String rol;

    private Rol(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }
}
