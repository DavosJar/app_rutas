package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.PedidoDao;
import com.app_rutas.models.Itinerario;
import com.app_rutas.models.OrdenEntrega;
import com.app_rutas.models.Pedido;
import com.app_rutas.models.PuntoEntrega;
import com.app_rutas.models.enums.ContenidoEnum;
import com.app_rutas.controller.tda.list.LinkedList;

public class PedidoServices {
    private PedidoDao obj;

    public Object[] listShowAll() throws Exception{
        if (!obj.getListAll().isEmpty()) {
            Pedido[] lista = (Pedido[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                OrdenEntrega o = new OrdenEntregaServices().get(lista[i].getIdOrdenEntrega());
                PuntoEntrega p = new PuntoEntregaServices().get(lista[i].getIdPuntoEntrega());
                Itinerario it = new ItinerarioServices().get(lista[i].getIdItinerario());
                HashMap mapa = new HashMap();
                mapa.put("id", lista[i].getId());
                mapa.put("contenido", lista[i].getContenido());
                mapa.put("fechaRegistro", lista[i].getFechaRegistro());
                mapa.put("pesoTotal", lista[i].getPesoTotal());
                mapa.put("volumen", lista[i].getVolumenTotal());
                mapa.put("estado", lista[i].getEstado());
                mapa.put("requiereFrio", lista[i].getRequiereFrio());
                mapa.put("orden-entrega", o);
                mapa.put("punto-entrega", p);
                mapa.put("itinerario", it);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[] {};
    }

    public PedidoServices() {
        this.obj = new PedidoDao();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public Pedido getPedido() {
        return obj.getPedido();
    }

    public void setPedido(Pedido pedido) {
        obj.setPedido(pedido);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(Pedido pedido) throws Exception {
        obj.setPedido(pedido);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public Pedido get(Integer index) throws Exception {
        return obj.get(index);
    }

    public ContenidoEnum[] getContenido() {
        return obj.getContenido();
    }

    public ContenidoEnum getContenidoEnum(String estado){
        return obj.getContenidoEnum(estado);
    }

    public LinkedList<Pedido> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public Pedido buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<Pedido> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public Pedido getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }
}