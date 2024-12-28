package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.ConductorVehiculoDao;
import com.app_rutas.models.Conductor;
import com.app_rutas.models.ConductorVehiculo;
import com.app_rutas.models.Vehiculo;
import com.app_rutas.controller.tda.list.LinkedList;

public class ConductorVehiculoServices {
    private ConductorVehiculoDao obj;

    
    public Object[] listShowAll() throws Exception{
        if (!obj.getListAll().isEmpty()) {
            ConductorVehiculo[] lista = (ConductorVehiculo[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                Vehiculo o = new VehiculoServices().get(lista[i].getIdVehiculo());
                Conductor c = new ConductorServices().getConductorById(lista[i].getIdConductor());
                HashMap mapa = new HashMap();
                mapa.put("id", lista[i].getId());
                mapa.put("fechaAsignacion", lista[i].getFechaAsignacion());
                mapa.put("fechaDeBaja", lista[i].getFechaDeBaja());
                mapa.put("vehiculo", o);
                mapa.put("conductor", c);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[] {};
    }

    public ConductorVehiculoServices() {
        this.obj = new ConductorVehiculoDao();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public ConductorVehiculo getConductor() {
        return obj.getConductor();
    }

    public void setConductor(ConductorVehiculo conductorAsignado) {
        obj.setConductor(conductorAsignado);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(ConductorVehiculo conductorAsignado) throws Exception {
        obj.setConductor(conductorAsignado);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public ConductorVehiculo get(Integer index) throws Exception {
        return obj.get(index);
    }

    public LinkedList<ConductorVehiculo> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public ConductorVehiculo buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<ConductorVehiculo> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public ConductorVehiculo getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }
}