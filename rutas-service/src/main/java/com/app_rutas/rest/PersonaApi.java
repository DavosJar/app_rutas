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

import com.app_rutas.controller.dao.services.PersonaServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.excepcion.ValueAlreadyExistException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Persona;

@Path("/persona")
public class PersonaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        // EventoCrudServices ev = new EventoCrudServices();
        try {
            LinkedList<Persona> lista = ps.listAll();
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            // ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de personas.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de personas: " + e.getMessage());
            // ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getPersonaById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        try {
            map.put("msg", "OK");
            map.put("data", ps.getPersonaById(id));
            if (ps.getPersonaById(id) == null) {
                map.put("msg", "ERROR");
                map.put("error", "No se encontro el persona con id: " + id);
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
        PersonaServices ps = new PersonaServices();

        try {
            System.out.println("Mapa recibido: " + map);

            ps.validateField("nombre", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            ps.validateField("apellido", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");

            if (map.get("tipoIdentificacion") != null) {
                ps.getPersona().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
            }
            ps.validateField("identificacion", map, "NOT_NULL", "IS_UNIQUE", "NUMERIC", "LENGTH=10");
            if (map.get("fechaNacimiento") != null) {
                ps.getPersona().setFechaNacimiento(map.get("fechaNacimiento").toString());
            }
            ps.validateField("direccion", map, "NOT_NULL", "ALPHANUMERIC", "MAX_LENGTH=50", "MIN_LENGTH=5");
            ps.validateField("telefono", map, "NOT_NULL", "NUMERIC", "LENGTH=10");
            ps.validateField("email", map, "NOT_NULL", "VALID_EMAIL", "IS_UNIQUE");
            if (map.get("sexo") != null) {
                ps.getPersona().setSexo(ps.getSexo(map.get("sexo").toString()));
            }
            ps.save();

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
        PersonaServices ps = new PersonaServices();
        try {
            ps.getPersona().setId(id);
            ps.delete();
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
        PersonaServices ps = new PersonaServices();

        try {
            ps.validateField("id", map, "MIN_VALUE=1");
            if (ps.getPersonaById(Integer.valueOf(map.get("id").toString())) == null) {
                throw new IllegalArgumentException("No se encontró la persona con ID: " + map.get("id").toString());
            }
            ps.validateField("id", map, "MIN_VALUE=1");
            if (map.get("nombre") != null && !map.get("nombre").equals(ps.getPersona().getNombre())) {
                ps.validateField("nombre", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            }
            if (map.get("apellido") != null && !map.get("apellido").equals(ps.getPersona().getApellido())) {
                ps.validateField("apellido", map, "NOT_NULL", "ALPHABETIC", "MAX_LENGTH=25", "MIN_LENGTH=3");
            }
            if (map.get("tipoIdentificacion") != null
                    && !map.get("tipoIdentificacion").equals(ps.getPersona().getTipoIdentificacion())) {
                ps.getPersona().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
            }
            if (map.get("identificacion") != null) {
                String newIdentificacion = map.get("identificacion").toString();
                String currentIdentificacion = ps.getPersona().getIdentificacion();

                if (currentIdentificacion == null || !newIdentificacion.equals(currentIdentificacion)) {
                    ps.validateField("identificacion", map, "NOT_NULL", "IS_UNIQUE", "NUMERIC", "LENGTH=10");
                }
            }
            if (map.get("fechaNacimiento") != null
                    && !map.get("fechaNacimiento").equals(ps.getPersona().getFechaNacimiento())) {
                ps.getPersona().setFechaNacimiento(map.get("fechaNacimiento").toString());
            }
            ps.validateField("direccion", map, "NOT_NULL", "ALPHANUMERIC", "MAX_LENGTH=50", "MIN_LENGTH=5");

            if (map.get("telefono") != null && !map.get("telefono").equals(ps.getPersona().getTelefono())) {
                ps.validateField("telefono", map, "NOT_NULL", "NUMERIC", "LENGTH=10");

            }
            if (map.get("email") != null) {
                String newEmail = map.get("email").toString();
                String currentEmail = ps.getPersona().getEmail();
                if (currentEmail == null || !newEmail.equals(currentEmail)) {
                    ps.validateField("email", map, "NOT_NULL", "VALID_EMAIL", "IS_UNIQUE");
                } else {
                    ps.getPersona().setEmail(currentEmail);
                }
            }
            if (map.get("sexo") != null && !map.get("sexo").equals(ps.getPersona().getSexo())) {
                ps.getPersona().setSexo(ps.getSexo(map.get("sexo").toString()));
            }

            ps.update();
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
    public Response searchPersona(@PathParam("identificacion") String identificacion) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ps.obtenerPersonaPor("identificacion", identificacion));
            if (ps.obtenerPersonaPor(identificacion, ps) == null) {
                res.put("estado", "error");
                res.put("data", "No se encontro el persona con identificacion: " + identificacion);
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
    public Response buscarPersonas(@PathParam("atributo") String atributo, @PathParam("valor") String valor)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        try {
            LinkedList<Persona> lista = ps.getPersonasBy(atributo, valor);
            res.put("estado", "Ok");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
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
    public Response ordenarPersonas(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        try {
            LinkedList<Persona> lista = ps.order(atributo, orden);
            res.put("estado", "Ok");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
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
        PersonaServices ps = new PersonaServices();
        map.put("msg", "OK");
        map.put("data", ps.getSexos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tipoidentificacion")
    public Response geTipos() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        map.put("msg", "OK");
        map.put("data", ps.getTipos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/criterios")
    public Response getCriterios() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        map.put("msg", "OK");
        map.put("data", ps.getPersonaAttributeLists());
        return Response.ok(map).build();
    }
}
