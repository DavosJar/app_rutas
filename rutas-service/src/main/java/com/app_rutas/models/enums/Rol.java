package com.app_rutas.models.enums;

public enum Rol {
    ADMINISTRADOR("ADMINISTRADOR"),
    CONDUCTOR("CONDUCTOR"),
    USUARIO("USUARIO");

    private String rol;

    private Rol(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }
}
