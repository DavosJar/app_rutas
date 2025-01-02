package com.app_rutas.rest;

import com.app_rutas.controller.dao.services.CuentaServices;
import com.app_rutas.models.Cuenta;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;

import com.app_rutas.controller.dao.services.cuentaUtils.CuentaUtils;

@Path("")
public class AuthApi {
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(HashMap<String, String> loginData) {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            if (username == null || password == null) {
                res.put("estado", "error");
                res.put("data", "El nombre de usuario y la contraseña son requeridos.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

            if (cs.obtenerCuentaPor("username", username) == null) {
                res.put("estado", "error");
                res.put("data", "Credenciales incorrectas.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

            if (!CuentaUtils.validatePassword(password, cs.obtenerCuentaPor("username", username).getPassword(),
                    cs.obtenerCuentaPor("username", username).getSalt())) {
                res.put("estado", "error");
                res.put("data", "Credenciales incorrectas.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

            res.put("estado", "ok");
            res.put("data", "Autenticación exitosa.");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}
