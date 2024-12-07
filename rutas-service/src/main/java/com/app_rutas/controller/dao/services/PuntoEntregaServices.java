package com.app_rutas.controller.dao.services;

import com.app_rutas.controller.dao.PuntoEntregaDao;
import com.app_rutas.models.PuntoEntrega;

import com.app_rutas.controller.tda.list.LinkedList;

public class PuntoEntregaServices {
    private PuntoEntregaDao obj;

    public PuntoEntregaServices() {
        this.obj = new PuntoEntregaDao();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public PuntoEntrega getPuntoEntrega() {
        return obj.getPuntoEntrega();
    }

    public void setPuntoEntrega(PuntoEntrega puntoEntrega) {
        obj.setPuntoEntrega(puntoEntrega);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(PuntoEntrega puntoEntrega) throws Exception {
        obj.setPuntoEntrega(puntoEntrega);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public PuntoEntrega get(Integer index) throws Exception {
        return obj.get(index);
    }

    public LinkedList<PuntoEntrega> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public PuntoEntrega buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<PuntoEntrega> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public PuntoEntrega getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }
}
