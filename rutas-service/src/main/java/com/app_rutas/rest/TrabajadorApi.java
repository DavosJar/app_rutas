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

import com.app_rutas.controller.dao.services.TrabajadorServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.excepcion.ValueAlreadyExistException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Trabajador;

@Path("/trabajador")
public class TrabajadorApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects(@HeaderParam("Authorization") String authHeader) {
        HashMap<String, Object> res = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();

        // Log para verificar el encabezado recibido
        System.out.println("Authorization Header: " + authHeader);

        // Verificar si el encabezado de autorización es nulo o no es "public"
        if (authHeader == null || !"public".equals(authHeader)) {
            System.out.println("Acceso no autorizado. Encabezado incorrecto o ausente.");
            res.put("status", "ERROR");
            res.put("msg", "Acceso no autorizado. Agrega el encabezado 'Authorization: public'.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        try {
            // Log antes de llamar al servicio
            System.out.println("Llamando a TrabajadorServices.listAll...");
            LinkedList<Trabajador> lista = ts.listAll();

            // Log para verificar si la lista obtenida es nula o vacía
            if (lista == null) {
                System.out.println("La lista de trabajadores es nula.");
            } else if (lista.isEmpty()) {
                System.out.println("La lista de trabajadores está vacía.");
            } else {
                System.out.println("Lista obtenida con " + lista.getSize() + " trabajadores.");
            }

            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista == null || lista.isEmpty() ? new Object[] {} : lista.toArray());
            return Response.ok(res).build();
        } catch (Exception e) {
            // Log del error capturado
            System.err.println("Error al obtener la lista de trabajadores: " + e.getMessage());
            e.printStackTrace();

            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de trabajadores: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getTrabajadorById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        try {
            map.put("msg", "OK");
            map.put("data", ts.getById(id));
            if (ts.getById(id) == null) {
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
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();

        try {

            ts.validateField("nombre", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            ts.validateField("apellido", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");

            if (map.get("tipoIdentificacion") != null) {
                ts.getTrabajador().setTipoIdentificacion(ts.getTipo(map.get("tipoIdentificacion").toString()));
            }
            ts.validateField("identificacion", map, "NOT_NULL", "IS_UNIQUE", "NUMERIC", "LENGTH=10");
            ts.validateField("fechaNacimiento", map, "DATE", "NOT_NULL", "MIN_DATE=1900-01-01", "MAX_DATE=2021-12-31");
            ts.validateField("direccion", map, "NOT_NULL", "ALPHANUMERIC", "MAX_LENGTH=50", "MIN_LENGTH=5");
            ts.validateField("telefono", map, "NOT_NULL", "NUMERIC", "LENGTH=10");
            ts.validateField("email", map, "NOT_NULL", "VALID_EMAIL", "IS_UNIQUE");
            if (map.get("sexo") != null) {
                ts.getTrabajador().setSexo(ts.getSexo(map.get("sexo").toString()));
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
        TrabajadorServices ts = new TrabajadorServices();
        try {
            ts.getTrabajador().setId(id);
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
        TrabajadorServices ts = new TrabajadorServices();

        try {
            if (map.get("id") == null) {
                throw new IllegalArgumentException("El id es obligatorio");
            }
            int id = (int) map.get("id");
            ts.setTrabajador(ts.getById(id));

            ts.validateField("id", map, "MIN_VALUE=1");
            if (map.get("nombre") != null && !map.get("nombre").equals(ts.getTrabajador().getNombre())) {
                ts.validateField("nombre", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            }
            if (map.get("apellido") != null && !map.get("apellido").equals(ts.getTrabajador().getApellido())) {
                ts.validateField("apellido", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            }
            if (map.get("tipoIdentificacion") != null
                    && !map.get("tipoIdentificacion").equals(ts.getTrabajador().getTipoIdentificacion())) {
                ts.getTrabajador().setTipoIdentificacion(ts.getTipo(map.get("tipoIdentificacion").toString()));
            }
            if (map.get("identificacion") != null) {
                String newIdentificacion = map.get("identificacion").toString();
                String currentIdentificacion = ts.getTrabajador().getIdentificacion();
                if (newIdentificacion.equalsIgnoreCase(currentIdentificacion)) {
                    ts.getTrabajador().setIdentificacion(currentIdentificacion);
                } else {
                    ts.validateField("identificacion", map, "NOT_NULL", "IS_UNIQUE", "NUMERIC", "LENGTH=10");
                }
            }
            if (map.get("fechaNacimiento") != null
                    && !map.get("fechaNacimiento").equals(ts.getTrabajador().getFechaNacimiento())) {
                ts.getTrabajador().setFechaNacimiento(map.get("fechaNacimiento").toString());
            }
            ts.validateField("direccion", map, "NOT_NULL", "ALPHANUMERIC", "MAX_LENGTH=50", "MIN_LENGTH=5");

            if (map.get("telefono") != null && !map.get("telefono").equals(ts.getTrabajador().getTelefono())) {
                ts.validateField("telefono", map, "NOT_NULL", "NUMERIC", "LENGTH=10");

            }
            if (map.get("email") != null) {
                String newEmail = map.get("email").toString();
                String currentEmail = ts.getTrabajador().getEmail();
                if (newEmail.equalsIgnoreCase(currentEmail)) {
                    ts.getTrabajador().setEmail(currentEmail);
                } else {
                    ts.validateField("email", map, "NOT_NULL", "VALID_EMAIL", "IS_UNIQUE");
                }
            }
            if (map.get("sexo") != null && !map.get("sexo").equals(ts.getTrabajador().getSexo())) {
                ts.getTrabajador().setSexo(ts.getSexo(map.get("sexo").toString()));
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
    public Response searchTrabajador(@PathParam("identificacion") String identificacion) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ts.obtenerTrabajadorPor("identificacion", identificacion));
            if (ts.obtenerTrabajadorPor("identificacion", identificacion) == null) {
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
    public Response buscarTrabajadores(@PathParam("atributo") String atributo, @PathParam("valor") String valor)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ts.getTrabajadoresBy(atributo, valor).toArray());
            if (ts.getTrabajadoresBy(atributo, valor).isEmpty()) {
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
    public Response ordenarTrabajadores(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sexo")
    public Response getSexo() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        map.put("msg", "OK");
        map.put("data", ts.getSexos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tipoidentificacion")
    public Response geTipos() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        map.put("msg", "OK");
        map.put("data", ts.getTipos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/criterios")
    public Response getCriterios() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        TrabajadorServices ts = new TrabajadorServices();
        map.put("msg", "OK");
        map.put("data", ts.getTrabajadorAttributeLists());
        return Response.ok(map).build();
    }
}