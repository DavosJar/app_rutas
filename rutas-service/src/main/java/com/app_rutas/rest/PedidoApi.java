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

import com.app_rutas.controller.dao.services.ItinerarioServices;
import com.app_rutas.controller.dao.services.OrdenEntregaServices;
import com.app_rutas.controller.dao.services.PedidoServices;
import com.app_rutas.controller.dao.services.PuntoEntregaServices;
import com.app_rutas.controller.excepcion.ListEmptyException;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Pedido;
import com.google.gson.Gson;

@Path("/pedido")
public class PedidoApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap res = new HashMap<>();
        PedidoServices ps = new PedidoServices();
        try {
            res.put("status", "success");
            res.put("message", "Consulta realizada con exito.");
            res.put("data", ps.listShowAll());
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
        PedidoServices ps = new PedidoServices();
        map.put("msg", "OK");
        map.put("data", ps.getContenido());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getById(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        PedidoServices ps = new PedidoServices();
        try {
            if (id == null || id < 1) {
                map.put("msg", "ID invalido");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            }
            ps.setPedido(ps.get(id));
            if (ps.getPedido() == null || ps.getPedido().getId() == null) {
                map.put("msg", "No existe pedido con el ID proporcionado");
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", ps.getPedido());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener el pedido");
            map.put("error", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        HashMap res = new HashMap<>();
        Gson g = new Gson();

        try {
            if (map.get("punto-entrega") != null && map.get("orden-entrega") != null && map.get("itinerario") != null) {
                PuntoEntregaServices pes = new PuntoEntregaServices();
                pes.setPuntoEntrega(pes.get(Integer.parseInt(map.get("punto-entrega").toString())));
                //System.out.println("Punto de entrega: " + pes.getPuntoEntrega().getId());
                OrdenEntregaServices oes = new OrdenEntregaServices();
                oes.setOrdenEntrega(oes.get(Integer.parseInt(map.get("orden-entrega").toString())));
                ItinerarioServices is = new ItinerarioServices();
                is.setItinerario(is.get(Integer.parseInt(map.get("itinerario").toString())));
                //System.out.println("Orden de entrega: " + is.getItinerario().getId());
                if (pes.getPuntoEntrega().getId() != null && oes.getOrdenEntrega().getId() != null && is.getItinerario().getId() != null) {
                    PedidoServices ps = new PedidoServices();
                    if (map.get("contenido") == null || map.get("contenido").toString().isEmpty()) {
                        throw new IllegalArgumentException("El campo 'contenido' es obligatorio.");
                    }
                    ps.getPedido().setContenido(map.get("contenido").toString());

                    if (map.get("fechaRegistro") == null || map.get("fechaRegistro").toString().isEmpty()) {
                        throw new IllegalArgumentException("El campo 'fechaRegistro' es obligatorio.");
                    }
                    ps.getPedido().setFechaRegistro(map.get("fechaRegistro").toString());

                    if (map.get("pesoTotal") != null) {
                        ps.getPedido().setPesoTotal(Float.parseFloat(map.get("pesoTotal").toString()));
                    }
                    if (map.get("volumen") != null) {
                        ps.getPedido().setVolumenTotal(Double.parseDouble(map.get("volumen").toString()));
                    }
                    if (map.get("estado") != null) {
                        ps.getPedido().setEstado(ps.getContenidoEnum(map.get("estado").toString()));
                    }
                    if (map.get("requiereFrio") != null) {
                        ps.getPedido().setRequiereFrio(Boolean.parseBoolean(map.get("requiereFrio").toString()));
                    } else {
                        ps.getPedido().setRequiereFrio(false);
                    }
                    ps.getPedido().setIdPuntoEntrega(pes.getPuntoEntrega().getId());
                    ps.getPedido().setIdOrdenEntrega(oes.getOrdenEntrega().getId());
                    ps.getPedido().setIdItinerario(is.getItinerario().getId());
                    ps.save();
                    System.out.println("Pedido guardado" + pes + oes + is);
                    res.put("estado", "Ok");
                    res.put("data", "Pedido guardado con exito.");
                    return Response.ok(res).build();
                } else {
                    res.put("estado", "error");
                    res.put("data", "No existe punto de entrega o orden de entrega con el ID proporcionado");
                    return Response.status(Response.Status.NOT_FOUND).entity(res).build();
                }
            } else {
                res.put("estado", "error");
                res.put("data", "No se proporciono un punto de entrega o una orden de entrega");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }
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
        PedidoServices ps = new PedidoServices();
        try {
            ps.getPedido().setId(id);
            ps.delete();
            System.out.println("Orden de entrega eliminada" + id);
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Hasta aqui llega" + ps.getPedido().getId());
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
            PedidoServices ps = new PedidoServices();
            ps.setPedido(ps.get(Integer.parseInt(map.get("id").toString())));
            if (ps.getPedido().getId() == null) {
                res.put("msg", "Error");
                res.put("data", "El equipo no existe");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            } else {
                if (map.get("punto-entrega") != null && map.get("orden-entrega") != null || map.get("itinerario") != null) {
                    PuntoEntregaServices pes = new PuntoEntregaServices();
                    pes.setPuntoEntrega(pes.get(Integer.parseInt(map.get("punto-entrega").toString())));
                    OrdenEntregaServices oes = new OrdenEntregaServices();
                    oes.setOrdenEntrega(oes.get(Integer.parseInt(map.get("orden-entrega").toString())));
                    ItinerarioServices is = new ItinerarioServices();
                    is.setItinerario(is.get(Integer.parseInt(map.get("itinerario").toString())));
                    if (pes.getPuntoEntrega().getId() != null && oes.getOrdenEntrega().getId() != null) {
                        ps.getPedido().setContenido(map.get("contenido").toString());
                        ps.getPedido().setFechaRegistro(map.get("fechaRegistro").toString());
                        ps.getPedido().setPesoTotal(Float.parseFloat(map.get("pesoTotal").toString()));
                        ps.getPedido().setVolumenTotal(Double.parseDouble(map.get("volumen").toString()));
                        ps.getPedido().setEstado(ps.getContenidoEnum(map.get("estado").toString()));
                        ps.getPedido().setRequiereFrio(Boolean.parseBoolean(map.get("requiereFrio").toString()));
                        ps.getPedido().setIdPuntoEntrega(pes.getPuntoEntrega().getId());
                        ps.getPedido().setIdOrdenEntrega(oes.getOrdenEntrega().getId());
                        ps.getPedido().setIdItinerario(is.getItinerario().getId());
                        ps.update();
                        res.put("status", "success");
                        res.put("message", "Orden actualizada con exito.");
                        return Response.ok(res).build();
                    } else {
                        res.put("estado", "error");
                        res.put("data", "No existe punto de entrega o orden de entrega con el ID proporcionado");
                        return Response.status(Response.Status.NOT_FOUND).entity(res).build();
                    }
                } else {
                    res.put("estado", "error");
                    res.put("data", "No se proporciono un punto de entrega o una orden de entrega");
                    return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
                }
            }
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Ocurrió un error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{attribute}/{value}")
    public Response binarySearchLin(@PathParam("attribute") String attribute, @PathParam("value") String value) {
        HashMap<String, Object> map = new HashMap<>();
        PedidoServices ps = new PedidoServices();

        try {
            LinkedList<Pedido> results;
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
        PedidoServices ps = new PedidoServices();
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
