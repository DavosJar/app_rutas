package com.app_rutas.rest;

import java.util.HashMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.app_rutas.controller.dao.services.OrdenEntregaServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.OrdenEntrega;

@Path("/orden-entrega")
public class OrdenEntregaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap res = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();
        try {
            res.put("status", "success");
            res.put("message", "Consulta realizada con exito.");
            res.put("data", ps.listAll().toArray());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/listType")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType() {
        HashMap map = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstado());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setOrdenEntrega(ps.get(id));
            if (ps.getOrdenEntrega() == null || ps.getOrdenEntrega().getId() == null) {
                map.put("msg", "No existe generador con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getOrdenEntrega());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el generador");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        HashMap res = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();

        try {
            if (map.get("ubicacion") == null || map.get("ubicacion").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'ubicacion' es obligatorio.");
            }
            ps.getOrdenEntrega().setUbicacionActual(map.get("ubicacion").toString());
            ps.getOrdenEntrega().setCodigoOrdenEntrega(ps.codigoU(map.get("ubicacion").toString()));

            if (map.get("fechaEntrega") == null || map.get("fechaEntrega").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'fechaEntrega' es obligatorio.");
            }
            ps.getOrdenEntrega().setFechaEntrega(map.get("fechaEntrega").toString());

            if (map.get("horaMaxima") != null) {
                ps.getOrdenEntrega().setHoraMaxima(map.get("horaMaxima").toString());
            }
            if (map.get("horaMinima") != null) {
                ps.getOrdenEntrega().setHoraMinima(map.get("horaMinima").toString());
            }
            if (map.get("estado") != null) {
                ps.getOrdenEntrega().setEstado(ps.getEstadoEnum(map.get("estado").toString()));
            }
            ps.save();
            res.put("estado", "Ok");
            res.put("data", "Registro guardado con exito.");
            return Response.ok(res).build();
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) {
        HashMap<String, Object> res = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();
        try {
            ps.getOrdenEntrega().setId(id);
            ps.delete();
            System.out.println("Orden de entrega eliminada" + id);
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Hasta aqui llega" + ps.getOrdenEntrega().getId());
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap map) {
        HashMap res = new HashMap<>();
        try {
            OrdenEntregaServices ps = new OrdenEntregaServices();
            ps.setOrdenEntrega(ps.get(Integer.parseInt(map.get("pepe").toString())));
            ps.getOrdenEntrega().setUbicacionActual(map.get("ubicacion").toString());
            ps.getOrdenEntrega().setFechaEntrega(map.get("fechaEntrega").toString());
            ps.getOrdenEntrega().setHoraMaxima(map.get("horaMaxima").toString());
            ps.getOrdenEntrega().setHoraMinima(map.get("horaMinima").toString());
            ps.getOrdenEntrega().setEstado(ps.getEstadoEnum(map.get("estado").toString()));
            ps.update();
            res.put("status", "success");
            res.put("message", "Orden actualizada con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{attribute}/{value}")
    public Response binarySearchLin(@PathParam("attribute") String attribute, @PathParam("value") String value) {
        HashMap<String, Object> map = new HashMap<>();
        OrdenEntregaServices ps = new OrdenEntregaServices();

        try {
            LinkedList<OrdenEntrega> results;
            try {
                results = ps.buscar(attribute, value);
            } catch (NumberFormatException e) {
                map.put("msg", "El valor proporcionado no es un numero valido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }

            if (results != null && !results.isEmpty()) {
                map.put("msg", "OK");
                map.put("data", results.toArray());
                return Response.ok(map).build();
            } else {
                map.put("msg", "No se encontraron ordenes de entrega con los valores proporcionados");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }

        } catch (Exception e) {
            map.put("msg", "Error en la busqueda");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }
}