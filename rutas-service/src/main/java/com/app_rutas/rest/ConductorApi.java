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

import com.app_rutas.controller.dao.services.ConductorServices;
import com.app_rutas.controller.excepcion.ListEmptyException;

@Path("/conductor")
public class ConductorApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        // EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.listAll().toArray());
            if (ps.listAll().isEmpty()) {
                res.put("data", new Object[] {});
            }
            // ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de
            // conductors.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de conductors: " + e.getMessage());
            // ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getConductorById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        try {
            map.put("msg", "OK");
            map.put("data", ps.getConductorById(id));
            if (ps.getConductorById(id) == null) {
                map.put("msg", "ERROR");
                map.put("error", "No se encontro el conductor con id: " + id);
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
        ConductorServices ps = new ConductorServices();

        try {
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'nombre' es obligatorio.");
            }
            ps.getConductor().setNombre(map.get("nombre").toString());

            if (map.get("apellido") == null || map.get("apellido").toString().isEmpty()) {
                throw new IllegalArgumentException("El campo 'apellido' es obligatorio.");
            }
            ps.getConductor().setApellido(map.get("apellido").toString());

            if (map.get("tipoIdentificacion") != null) {
                ps.getConductor().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
            }
            if (map.get("identificacion") != null) {
                ps.getConductor().setIdentificacion(map.get("identificacion").toString());
            }
            if (map.get("fechaNacimiento") != null) {
                ps.getConductor().setFechaNacimiento(map.get("fechaNacimiento").toString());
            }
            if (map.get("direccion") != null) {
                ps.getConductor().setDireccion(map.get("direccion").toString());
            }
            if (map.get("telefono") != null) {
                ps.getConductor().setTelefono(map.get("telefono").toString());
            }
            if (map.get("email") != null) {
                ps.getConductor().setEmail(map.get("email").toString());
            }
            if (map.get("sexo") != null) {
                ps.getConductor().setSexo(ps.getSexo(map.get("sexo").toString()));
            }
            if (map.get("licenciaConducir") != null) {
                ps.getConductor().setLicenciaConducir(map.get("licenciaConducir").toString());
            }
            if (map.get("caducidadLicencia") != null) {
                ps.getConductor().setCaducidadLicencia(map.get("caducidadLicencia").toString());
            }
            if (map.get("salario") != null) {
                ps.getConductor().setSalario(Float.valueOf(map.get("salario").toString()));
            }
            if (map.get("turno") != null) {
                ps.getConductor().setTurno(ps.getTurno(map.get("turno").toString()));
            }
            if (map.get("estado") != null) {
                ps.getConductor().setEstado(ps.getEstado(map.get("estado").toString()));
            }
            //System.out.println("datos: " + ps.getConductor().getLicenciaConducir());
            ps.save();
            res.put("estado", "Ok");
            res.put("data", "Conductor guardado con exito.");
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
        ConductorServices ps = new ConductorServices();
        try {
            ps.getConductor().setId(id);
            ps.delete();
            res.put("estado", "Ok");
            res.put("data", "Conductor eliminado con exito.");

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
        ConductorServices ps = new ConductorServices();
        if (ps.getConductorById(Integer.valueOf(map.get("id").toString())) != null) {
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
                if (map.get("licenciaConducir") == null || map.get("licenciaConducir").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'licenciaConducir' es obligatorio.");
                }
                if (map.get("caducidadLicencia") == null || map.get("caducidadLicencia").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'caducidadLicencia' es obligatorio.");
                }
                if (map.get("salario") == null || map.get("salario").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'salario' es obligatorio.");
                }
                if (map.get("turno") == null || map.get("turno").toString().isEmpty()) {
                    throw new IllegalArgumentException("El campo 'turno' es obligatorio.");
                }
                if (map.get("estado") == null || map.get("estado").toString().isEmpty()) {
                    throw new IllegalArgumentException("Elsave campo 'estado' es obligatorio.");
                }
                //System.out.println("falta alguin dato");
                ps.setConductor(ps.getConductorById(Integer.valueOf(map.get("id").toString())));
                ps.getConductor().setNombre(map.get("nombre").toString());
                ps.getConductor().setApellido(map.get("apellido").toString());
                ps.getConductor().setTipoIdentificacion(ps.getTipo(map.get("tipoIdentificacion").toString()));
                ps.getConductor().setIdentificacion(map.get("identificacion").toString());
                ps.getConductor().setFechaNacimiento(map.get("fechaNacimiento").toString());
                ps.getConductor().setDireccion(map.get("direccion").toString());
                ps.getConductor().setTelefono(map.get("telefono").toString());
                ps.getConductor().setEmail(map.get("email").toString());
                ps.getConductor().setSexo(ps.getSexo(map.get("sexo").toString()));

                ps.update();
                res.put("estado", "Ok");
                res.put("data", "Conductor actualizado con exito.");
                return Response.ok(res).build();
            } catch (Exception e) {
                res.put("estado", "error");
                res.put("data", "Error interno del servidor: " + e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
            }
        } else {
            res.put("estado", "error");
            res.put("data", "No se encontro el conductor con id: " + map.get("id").toString());
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/ident/{identificacion}")
    public Response searchConductor(@PathParam("identificacion") String identificacion) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ps.obtenerConductorPor("identificacion", identificacion));
            if (ps.obtenerConductorPor(identificacion, ps) == null) {
                res.put("estado", "error");
                res.put("data", "No se encontro el conductor con identificacion: " + identificacion);
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
    public Response buscarConductors(@PathParam("atributo") String atributo, @PathParam("valor") String valor)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        try {
            res.put("estado", "Ok");
            res.put("data", ps.getConductorsBy(atributo, valor).toArray());
            if (ps.getConductorsBy(atributo, valor).isEmpty()) {
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
    public Response ordenarConductors(@PathParam("atributo") String atributo, @PathParam("orden") Integer orden)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ConductorServices ps = new ConductorServices();
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
        ConductorServices ps = new ConductorServices();
        map.put("msg", "OK");
        map.put("data", ps.getSexos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tipoidentificacion")
    public Response geTipos() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        map.put("msg", "OK");
        map.put("data", ps.getTipos());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/criterios")
    public Response getCriterios() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        map.put("msg", "OK");
        map.put("data", ps.getConductorAttributeLists());
        return Response.ok(map).build();
    }

    @Path("/turno")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType() {
        HashMap map = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        map.put("msg", "OK");
        map.put("data", ps.getTurnos());
        return Response.ok(map).build();
    }

    @Path("/estado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstadoC() {
        HashMap map = new HashMap<>();
        ConductorServices ps = new ConductorServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstados());
        return Response.ok(map).build();
    }
}