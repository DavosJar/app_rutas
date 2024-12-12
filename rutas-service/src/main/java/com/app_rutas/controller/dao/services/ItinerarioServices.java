package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.ItinerarioDao;
import com.app_rutas.models.ConductorAsignado;
import com.app_rutas.models.Itinerario;
import com.app_rutas.models.enums.ItinerarioEstadoEnum;
import com.app_rutas.controller.tda.list.LinkedList;

public class ItinerarioServices {
    private ItinerarioDao obj;

    public Object[] listShowAll()throws Exception{
        if(!obj.getListAll().isEmpty()){
            Itinerario[] lista = (Itinerario[]) obj.getListAll().toArray();
            Object[] respuesta = new Object[lista.length];
            for (int i = 0; i < lista.length; i++) {
                ConductorAsignado c = new ConductorAsignadoServices().get(lista[i].getIdConductorAsignado());
                HashMap mapa = new HashMap();
                mapa.put("id", lista[i].getId());
                mapa.put("horaInicio", lista[i].getHoraIncio());
                mapa.put("duracionEstimada", lista[i].getDuracionEstimada());
                mapa.put("estado", lista[i].getEstado());
                mapa.put("idConductorAsignado", c);
                respuesta[i] = mapa;
            }
            return respuesta;
        }
        return new Object[]{};
    }

    public ItinerarioServices() {
        this.obj = new ItinerarioDao();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public Itinerario getItinerario() {
        return obj.getItinerario();
    }

    public void setItinerario(Itinerario itinerario) {
        obj.setItinerario(itinerario);
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean update(Itinerario itinerario) throws Exception {
        obj.setItinerario(itinerario);
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public Itinerario get(Integer index) throws Exception {
        return obj.get(index);
    }

    public ItinerarioEstadoEnum[] getEstado() {
        return obj.getEstado();
    }

    public ItinerarioEstadoEnum getEstadoEnum(String estado) {
        return obj.getEstadoEnum(estado);
    }

    public LinkedList<Itinerario> buscar(String attribute, Object value) throws Exception {
        return obj.buscar(attribute, value);
    }

    public Itinerario buscarPor(String attribute, Object value) throws Exception {
        return obj.buscarPor(attribute, value);
    }

    public String[] getOrdenAttributeLists() {
        return obj.getOrdenAttributeLists();
    }

    public LinkedList<Itinerario> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public Itinerario getById(Integer id) throws Exception {
        return obj.getById(id);
    }

    public String getByJson(Integer index) throws Exception {
        return obj.getByJson(index);
    }
}