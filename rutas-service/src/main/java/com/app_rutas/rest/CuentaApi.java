package com.app_rutas.rest;

import java.util.HashMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

//librerias priyecto
import com.app_rutas.controller.dao.services.CuentaServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.excepcion.ValueAlreadyExistException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Cuenta;
import com.app_rutas.controller.dao.services.cuentaUtils.CuentaUtils;
//librerias hash

@Path("/cuenta")
public class CuentaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        // EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Cuenta> lista = cs.listAll();
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            // ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de
            // cuentas.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de trabajadors: " + e.getMessage());
            // ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getCuentaById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            map.put("msg", "OK");
            map.put("data", cs.getById(id));
            if (cs.getById(id) == null) {
                map.put("msg", "ERROR");
                map.put("error", "No se encontro el trabajador con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "ERROR");
            map.put("error", "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map, @HeaderParam("Authorization") String authHeader)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        Integer idTrabajador = Integer.parseInt(map.get("idTrabajador").toString());
        if (authHeader == null || !"public".equals(authHeader)) {
            res.put("status", "ERROR");
            res.put("msg", "Acceso no autorizado. Agrega el encabezado 'Authorization: public'.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }
        if (idTrabajador != null && cs.exist(idTrabajador) == true) {

            try {

                cs.validateField("username", map, "NOT_NULL", "ALPHANUMERIC", "MIN_LENGTH=5", "MAX_LENGTH=25",
                        "IS_UNIQUE");
                cs.validateField("altEmail", map, "NOT_NULL", "VALID_EMAIL", "IS_UNIQUE");
                cs.validateField("idTrabajador", map, "NOT_NULL", "MIN_VALUE=1", "IS_UNIQUE");
                cs.validateField("password", map, "PASSWORD");
                cs.getCuenta().setRol(cs.getRol(map.get("rol").toString()));

                if (!map.get("password").toString().equals(map.get("confirmPassword"))) {
                    throw new Exception("Las contrase;as no coinciden");
                }
                String password = map.get("password").toString();
                String salt = CuentaUtils.generarSalt(); // Método para generar salt
                String hashedPassword = CuentaUtils.hashPassword(password, salt);
                cs.getCuenta().setPassword(hashedPassword);
                cs.getCuenta().setSalt(salt);
                cs.save();

                res.put("estado", "Ok");
                res.put("data", "Registro guardado con éxito.");
                return Response.ok(res).build();

            } catch (ValueAlreadyExistException e) {
                res.put("estado", "error");
                res.put("data", e.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

            } catch (IllegalArgumentException e) {
                res.put("estado", "error");
                res.put("data", "Error de validación: " + e.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

            } catch (Exception e) {
                res.put("estado", "error");
                res.put("data", "Error interno del servidor: " + e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
            }
        } else {
            res.put("estado", "error");
            res.put("data", "El idTrabajador es obligatorio");
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            cs.getCuenta().setId(id);
            cs.delete();
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");

            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();

        try {
            cs.update();
            res.put("estado", "Ok");
            res.put("data", "Registro actualizado con éxito.");
            return Response.ok(res).build();
        } catch (ValueAlreadyExistException e) {
            res.put("estado", "error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } catch (IllegalArgumentException e) {
            res.put("estado", "error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/ident/{identificacion}")
    public Response searchCuenta(@PathParam("identificacion") String identificacion) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            res.put("estado", "Ok");
            res.put("data", cs.obtenerCuentaPor("identificacion", identificacion));
            if (cs.obtenerCuentaPor("identificacion", identificacion) == null) {
                res.put("estado", "error");
                res.put("data", "No se encontro el trabajador con identificacion: " + identificacion);
                return Response.status(Response.Status.NOT_FOUND).entity(res).build();
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/{atributo}/{valor}")
    public Response buscarCuentaes(@PathParam("atributo") String atributo, @PathParam("valor") String valor)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            res.put("estado", "Ok");
            res.put("data", cs.getCuentaesBy(atributo, valor).toArray());
            if (cs.getCuentaesBy(atributo, valor).isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{atributo}/{orden}")
    public Response ordenarCuentaes(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        try {
            res.put("estado", "Ok");
            res.put("data", cs.order(atributo, orden).toArray());
            if (cs.order(atributo, orden).isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rol-list")
    public Response getRolList() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        map.put("msg", "OK");
        map.put("data", cs.getRolList());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/criterios")
    public Response getCriterios() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        CuentaServices cs = new CuentaServices();
        map.put("msg", "OK");
        map.put("data", cs.getCuentaAttributeLists());
        return Response.ok(map).build();
    }
}