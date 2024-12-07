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
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.listAll().toArray());
            if (ps.listAll().isEmpty()) {
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
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'nombre' es obligatorio.");
            }
            ps.getPersona().setNombre(map.get("nombre").toString());

            if (map.get("apellido") == null || map.get("apellido").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'apellido' es obligatorio.");
            }
            ps.getPersona().setApellido(map.get("apellido").toString());

            if (map.get("tipoIdentificacion") != null) {
                ps.getPersona().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
            }
            if (map.get("identificacion") != null) {
                ps.getPersona().setIdentificacion(map.get("identificacion").toString());
            }
            if (map.get("fechaNacimiento") != null) {
                ps.getPersona().setFechaNacimiento(map.get("fechaNacimiento").toString());
            }
            if (map.get("direccion") != null) {
                ps.getPersona().setDireccion(map.get("direccion").toString());
            }
            if (map.get("telefono") != null) {
                ps.getPersona().setTelefono(map.get("telefono").toString());
            }
            if (map.get("email") != null) {
                ps.getPersona().setEmail(map.get("email").toString());
            }
            if (map.get("sexo") != null) {
                ps.getPersona().setSexo(ps.getSexo(map.get("sexo").toString()));
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
    public Response update(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        PersonaServices ps = new PersonaServices();
        if (ps.getPersonaById(Integer.valueOf(map.get("id").toString())) != null) {
            try {
                if (map.get("id") == null || map.get("id").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'id' es obligatorio.");
                }
                if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'nombre' es obligatorio.");
                }
                if (map.get("apellido") == null || map.get("apellido").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'apellido' es obligatorio.");
                }
                if (map.get("tipoIdentificacion") == null || map.get("tipoIdentificacion").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'tipoIdentificacion' es obligatorio.");
                }
                if (map.get("identificacion") == null || map.get("identificacion").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'identificacion' es obligatorio.");
                }
                if (map.get("fechaNacimiento") == null || map.get("fechaNacimiento").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'fechaNacimiento' es obligatorio.");
                }
                if (map.get("direccion") == null || map.get("direccion").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'direccion' es obligatorio.");
                }
                if (map.get("telefono") == null || map.get("telefono").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'telefono' es obligatorio.");
                }
                if (map.get("email") == null || map.get("email").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'email' es obligatorio.");
                }
                if (map.get("sexo") == null || map.get("sexo").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'sexo' es obligatorio.");
                }
                System.out.println("falta alguin dato");
                ps.setPersona(ps.getPersonaById(Integer.valueOf(map.get("id").toString())));
                ps.getPersona().setNombre(map.get("nombre").toString());
                ps.getPersona().setApellido(map.get("apellido").toString());
                ps.getPersona().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
                ps.getPersona().setIdentificacion(map.get("identificacion").toString());
                ps.getPersona().setFechaNacimiento(map.get("fechaNacimiento").toString());
                ps.getPersona().setDireccion(map.get("direccion").toString());
                ps.getPersona().setTelefono(map.get("telefono").toString());
                ps.getPersona().setEmail(map.get("email").toString());
                ps.getPersona().setSexo(ps.getSexo(map.get("sexo").toString()));

                ps.update();
                res.put("estado", "Ok");
                res.put("data", "Registro actualizado con exito.");
                return Response.ok(res).build();
            } catch (Exception e) {
                res.put("estado", "error");
                res.put("data", "Error interno del servidor: " + e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
            }
        } else {
            res.put("estado", "error");
            res.put("data", "No se encontro el persona con id: " + map.get("id").toString());
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
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
            res.put("estado", "Ok");
            res.put("data", ps.getPersonasBy(atributo, valor).toArray());
            if (ps.getPersonasBy(atributo, valor).isEmpty()) {
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