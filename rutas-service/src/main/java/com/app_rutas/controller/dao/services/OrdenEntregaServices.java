package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.OrdenEntregaDao;
import com.app_rutas.models.Itinerario;
import com.app_rutas.models.OrdenEntrega;
import com.app_rutas.models.Pedido;
import com.app_rutas.models.enums.EstadoEnum;
import com.app_rutas.controller.tda.list.LinkedList;

public class OrdenEntregaServices {
    private OrdenEntregaDao obj;

    public Object[] listShowAll() throws Exception {
        if (!obj.getListAll().isEmpty()) {
            OrdenEntrega[] lista = (OrdenEntrega[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                Pedido p = new PedidoServices().get(lista[i].getIdPedido());
                HashMap<String, Object> mapa = new HashMap<>();
                mapa.put("id", lista[i].getId());
                mapa.put("fechaProgramada", lista[i].getFechaProgramada());
                mapa.put("receptor", lista[i].getReceptor());
                mapa.put("observaciones", lista[i].getObservaciones());
                mapa.put("estado", lista[i].getEstado());
                mapa.put("pedido", p);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[] {};
    }

    public OrdenEntregaServices() {
        this.obj = new OrdenEntregaDao();
    }

    public LinkedList<OrdenEntrega> listAll() throws Exception {
        return obj.getListAll();
    }

    public OrdenEntrega getOrdenEntrega() {
        return obj.getOrdenEntrega();
    }

    public void setOrdenEntrega(OrdenEntrega ordenEntrega) {
        obj.setOrdenEntrega(ordenEntrega);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(OrdenEntrega ordenEntrega) throws Exception {
        obj.setOrdenEntrega(ordenEntrega);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public OrdenEntrega get(Integer index) throws Exception {
        return obj.get(index);
    }

    public EstadoEnum[] getEstado() {
        return obj.getEstado();
    }

    public EstadoEnum getEstadoEnum(String estado) {
        return obj.getEstadoEnum(estado);
    }

    public LinkedList<OrdenEntrega> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public OrdenEntrega buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<OrdenEntrega> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public OrdenEntrega getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }

    public String codigoU(String input) {
        return obj.codigoU(input);
    }

    public OrdenEntrega generarOrdenEntrega(Pedido pedido, Integer idItinerario) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no existe");
        }

        if (idItinerario == null) {
            throw new Exception("El idItinerario no puede ser nulo");
        }

        Itinerario itinerario = new ItinerarioServices().getById(idItinerario);

        if (itinerario == null) {
            throw new Exception("No se encontró un itinerario con el ID: " + idItinerario);
        }

        obj.setOrdenEntrega(new OrdenEntrega());
        obj.getOrdenEntrega().setIdPedido(pedido.getId());
        obj.getOrdenEntrega().setIdItinerario(idItinerario);
        obj.getOrdenEntrega().setFechaProgramada(itinerario.getFechaProgramada());
        obj.getOrdenEntrega().setEstado(EstadoEnum.PENDIENTE);

        return obj.getOrdenEntrega();
    }
}
