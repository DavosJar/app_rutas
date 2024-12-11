package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.ConductorAsignadoDao;
import com.app_rutas.models.Conductor;
import com.app_rutas.models.ConductorAsignado;
import com.app_rutas.models.Vehiculo;
import com.app_rutas.models.enums.ConductorEstado;
import com.app_rutas.controller.tda.list.LinkedList;

public class ConductorAsignadoServices {
    private ConductorAsignadoDao obj;

    
    public Object[] listShowAll() throws Exception{
        if (!obj.getListAll().isEmpty()) {
            ConductorAsignado[] lista = (ConductorAsignado[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                Vehiculo o = new VehiculoServices().get(lista[i].getIdVehiculo());
                Conductor c = new ConductorServices().getConductorById(lista[i].getIdConductor());
                HashMap mapa = new HashMap();
                mapa.put("id", lista[i].getId());
                mapa.put("fechaAsignacion", lista[i].getFechaAsignacion());
                mapa.put("fechaDeBaja", lista[i].getFechaDeBaja());
                mapa.put("estado", lista[i].getEstado());
                mapa.put("vehiculo", o);
                mapa.put("conductor", c);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[] {};
    }

    public ConductorAsignadoServices() {
        this.obj = new ConductorAsignadoDao();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public ConductorAsignado getConductor() {
        return obj.getConductor();
    }

    public void setConductor(ConductorAsignado conductorAsignado) {
        obj.setConductor(conductorAsignado);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(ConductorAsignado conductorAsignado) throws Exception {
        obj.setConductor(conductorAsignado);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public ConductorAsignado get(Integer index) throws Exception {
        return obj.get(index);
    }

    public ConductorEstado[] getEstadoEnum() {
        return obj.getEstadoEnum();
    }

    public ConductorEstado getEstadoEnum(String estado){
        return obj.getEstadoEnum(estado);
    }

    public LinkedList<ConductorAsignado> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public ConductorAsignado buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<ConductorAsignado> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public ConductorAsignado getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }
}