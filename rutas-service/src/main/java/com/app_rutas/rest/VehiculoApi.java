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
import com.app_rutas.controller.dao.services.VehiculoServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Vehiculo;

@Path("/vehiculo")
public class VehiculoApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap res = new HashMap<>();
        VehiculoServices ps = new VehiculoServices();
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
        VehiculoServices ps = new VehiculoServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstado());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        VehiculoServices ps = new VehiculoServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setVehiculo(ps.get(id));
            if (ps.getVehiculo() == null || ps.getVehiculo().getId() == null) {
                map.put("msg", "No existe generador con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getVehiculo());
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
        VehiculoServices ps = new VehiculoServices();

        try {
            if (map.get("placa") == null || map.get("placa").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'placa' es obligatorio.");
            }
            ps.getVehiculo().setPlaca(map.get("placa").toString());

            if (map.get("modelo") == null || map.get("modelo").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'modelo' es obligatorio.");
            }
            ps.getVehiculo().setModelo(map.get("modelo").toString());

            if (map.get("capacidad") != null) {
                ps.getVehiculo().setCapacidad(Integer.parseInt(map.get("capacidad").toString()));
            }
            if (map.get("marca") != null) {
                ps.getVehiculo().setMarca(map.get("marca").toString());
            }
            if(map.get("pesoMinimo") != null){
                ps.getVehiculo().setPesoMinimo(Float.parseFloat(map.get("pesoMinimo").toString()));
            }
            if(map.get("pesoMaximo") != null){
                ps.getVehiculo().setPesoMaximo(Float.parseFloat(map.get("pesoMaximo").toString()));
            }
            if(map.get("potencia") != null){
                ps.getVehiculo().setPotencia(Float.parseFloat(map.get("potencia").toString()));
            }
            if (map.get("estado") != null) {
                ps.getVehiculo().setEstado(ps.getEstadoEnum(map.get("estado").toString()));
            }
            if (map.get("tieneRestriccion") != null) {
                ps.getVehiculo().setTieneRestriccion(Boolean.parseBoolean(map.get("tieneRestriccion").toString()));
            } else {
                ps.getVehiculo().setTieneRestriccion(false);
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
        VehiculoServices ps = new VehiculoServices();
        try {
            ps.getVehiculo().setId(id);
            ps.delete();
            System.out.println("Orden de entrega eliminada" + id);
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Hasta aqui llega" + ps.getVehiculo().getId());
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
            VehiculoServices ps = new VehiculoServices();
            ps.setVehiculo(ps.get(Integer.parseInt(map.get("pepe").toString())));
            ps.getVehiculo().setPlaca(map.get("placa").toString());
            ps.getVehiculo().setModelo(map.get("modelo").toString());
            ps.getVehiculo().setCapacidad(Integer.parseInt(map.get("capacidad").toString()));
            ps.getVehiculo().setMarca(map.get("marca").toString());
            ps.getVehiculo().setPesoMinimo(Float.parseFloat(map.get("pesoMinimo").toString()));
            ps.getVehiculo().setPesoMaximo(Float.parseFloat(map.get("pesoMaximo").toString()));
            ps.getVehiculo().setPotencia(Float.parseFloat(map.get("potencia").toString()));
            ps.getVehiculo().setEstado(ps.getEstadoEnum(map.get("estado").toString()));
            if (map.get("tieneRestriccion") == null) {
                ps.getVehiculo().setTieneRestriccion(false);

            } else {
                ps.getVehiculo().setTieneRestriccion(Boolean.parseBoolean(map.get("tieneRestriccion").toString()));
            }
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
        VehiculoServices ps = new VehiculoServices();

        try {
            LinkedList<Vehiculo> results;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/{atributo}/{orden}")
    public Response ordenar(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ps = new VehiculoServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ps.order(atributo, orden).toArray());
            if (ps.order(atributo, orden).isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }
}