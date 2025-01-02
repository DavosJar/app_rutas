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

import com.app_rutas.controller.dao.services.VehiculoServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.excepcion.ValueAlreadyExistException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Vehiculo;

@Path("/vehiculo")
public class VehiculoApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        // EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Vehiculo> lista = ts.listAll();
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            // ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de
            // vehiculos.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de vehiculos: " + e.getMessage());
            // ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getVehiculoById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        try {
            map.put("msg", "OK");
            map.put("data", ts.getById(id));
            if (ts.getById(id) == null) {
                map.put("msg", "ERROR");
                map.put("error", "No se encontro el vehiculo con id: " + id);
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
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();

        try {

            ts.validateField("marca", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            ts.validateField("modelo", map, "NOT_NULL", "MAX_LENGTH=25", "MIN_LENGTH=1");
            ts.validateField("placa", map, "NOT_NULL", "IS_UNIQUE", "ALPHANUMERIC", "MIN_LENGTH=6", "MAX_LENGTH=8");
            ts.validateField("capacidad", map, "NOT_NULL", "MIN_VALUE=0");
            ts.validateField("potencia", map, "NOT_NULL", "MIN_VALUE=0");
            ts.validateField("pesoTara", map, "NOT_NULL", "MIN_VALUE=0");
            ts.validateField("pesoMaximo", map, "NOT_NULL", "MIN_VALUE=0");
            ts.getVehiculo().setRefrigerado(Boolean.parseBoolean(map.get("refrigerado").toString()));
            if (map.get("estado") != null) {
                ts.getVehiculo().setEstado(ts.getEstado(map.get("estado").toString()));
            }
            ts.save();

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
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        try {
            ts.getVehiculo().setId(id);
            ts.delete();
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
        VehiculoServices ts = new VehiculoServices();

        try {
            if (map.get("id") == null) {
                throw new IllegalArgumentException("El id es obligatorio");
            }
            int id = (int) map.get("id");
            ts.setVehiculo(ts.getById(id));
            System.out.println("Vehiculo obtenido " + ts.getById(id));

            ts.validateField("id", map, "MIN_VALUE=1");
            if (map.get("marca") != null && !map.get("marca").equals(ts.getVehiculo().getMarca())) {
                ts.validateField("marca", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            }
            if (map.get("modelo") != null && !map.get("modelo").equals(ts.getVehiculo().getModelo())) {
                ts.validateField("modelo", map, "NOT_NULL", "MIN_LENGTH=1", "MAX_LENGTH=25");
            }

            if (map.get("placa") != null) {
                String newPlaca = map.get("placa").toString();
                String currentPlaca = ts.getVehiculo().getPlaca();
                if (!newPlaca.equalsIgnoreCase(currentPlaca)) {
                    ts.validateField("placa", map, "NOT_NULL", "IS_UNIQUE", "ALPHANUMERIC", "MIN_LENGTH=6",
                            "MAX_LENGTH=8");
                }
            }
            if (map.get("capacidad") != null
                    && !map.get("capacidad").equals(ts.getVehiculo().getCapacidad())) {
                ts.validateField("capacidad", map, "NOT_NULL", "MIN_VALUE=0");
            }
            if (map.get("potencia") != null
                    && !map.get("potencia").equals(ts.getVehiculo().getPotencia())) {
                ts.validateField("potencia", map, "NOT_NULL", "MIN_VALUE=0");
            }
            if (map.get("pesoTara") != null
                    && !map.get("pesoTara").equals(ts.getVehiculo().getPesoTara())) {
                ts.validateField("pesoTara", map, "NOT_NULL", "MIN_VALUE=0");
            }
            if (map.get("pesoMaximo") != null
                    && !map.get("pesoMaximo").equals(ts.getVehiculo().getPesoMaximo())) {
                ts.validateField("pesoMaximo", map, "NOT_NULL", "MIN_VALUE=0");
            }
            if (map.get("refrigerado") != null && !map.get("refrigerado").equals(ts.getVehiculo().getRefrigerado())) {
                ts.getVehiculo().setRefrigerado(Boolean.parseBoolean(map.get("refrigerado").toString()));
            }
            if (map.get("estado") != null && !map.get("estado").equals(ts.getVehiculo().getEstado().toString())) {
                ts.getVehiculo().setEstado(ts.getEstado(map.get("estado").toString()));
            }
            ts.update();
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
    public Response searchVehiculo(@PathParam("identificacion") String identificacion) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ts.buscarPor("identificacion", identificacion));
            if (ts.buscarPor("identificacion", identificacion) == null) {
                res.put("estado", "error");
                res.put("data", "No se encontro el vehiculo con identificacion: " + identificacion);
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
    public Response buscarConductores(@PathParam("atributo") String atributo, @PathParam("valor") String valor)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ts.buscar(atributo, valor).toArray());
            if (ts.buscar(atributo, valor).isEmpty()) {
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
    public Response ordenarConductores(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        VehiculoServices ts = new VehiculoServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ts.order(atributo, orden).toArray());
            if (ts.order(atributo, orden).isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/listType")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType() {
        HashMap<String, Object> map = new HashMap<>();
        VehiculoServices ps = new VehiculoServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstados());
        return Response.ok(map).build();
    }
}
