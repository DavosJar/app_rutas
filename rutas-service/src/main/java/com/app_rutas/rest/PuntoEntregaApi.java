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

import com.app_rutas.controller.dao.services.PuntoEntregaServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.PuntoEntrega;

@Path("/punto-entrega")
public class PuntoEntregaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap res = new HashMap<>();
        PuntoEntregaServices ps = new PuntoEntregaServices();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        PuntoEntregaServices ps = new PuntoEntregaServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setPuntoEntrega(ps.get(id));
            if (ps.getPuntoEntrega() == null || ps.getPuntoEntrega().getId() == null) {
                map.put("msg", "No existe punto de entrega con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getPuntoEntrega());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el punto de entrega");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        HashMap res = new HashMap<>();
        PuntoEntregaServices ps = new PuntoEntregaServices();

        try {
            if (map.get("ciudad") == null || map.get("ciudad").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'ciudad' es obligatorio.");
            }
            ps.getPuntoEntrega().setCiudad(map.get("ciudad").toString());

            if (map.get("direccion") == null || map.get("direccion").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'direccion' es obligatorio.");
            }
            ps.getPuntoEntrega().setDireccion(map.get("direccion").toString());

            if (map.get("nombre") != null) {
                ps.getPuntoEntrega().setNombre(map.get("nombre").toString());
            }
            if (map.get("latitud") != null) {
                ps.getPuntoEntrega().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            }
            if (map.get("longitud") != null) {
                ps.getPuntoEntrega().setLongitud(Double.parseDouble(map.get("longitud").toString()));
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
        PuntoEntregaServices ps = new PuntoEntregaServices();
        try {
            ps.getPuntoEntrega().setId(id);
            ps.delete();
            System.out.println("Orden de entrega eliminada" + id);
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Hasta aqui llega" + ps.getPuntoEntrega().getId());
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
            PuntoEntregaServices ps = new PuntoEntregaServices();
            ps.setPuntoEntrega(ps.get(Integer.parseInt(map.get("tilin").toString())));
            ps.getPuntoEntrega().setCiudad(map.get("ciudad").toString());
            ps.getPuntoEntrega().setDireccion(map.get("direccion").toString());
            ps.getPuntoEntrega().setNombre(map.get("nombre").toString());
            ps.getPuntoEntrega().setLatitud(Double.parseDouble(map.get("latitud").toString()));
            ps.getPuntoEntrega().setLongitud(Double.parseDouble(map.get("longitud").toString()));
            ps.update();
            res.put("status", "success");
            res.put("message", "Punto de entrega actualizado con exito.");
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
        PuntoEntregaServices ps = new PuntoEntregaServices();

        try {
            LinkedList<PuntoEntrega> results;
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