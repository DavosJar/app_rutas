package com.app_rutas.controller.dao.services;

import com.app_rutas.controller.dao.TrabajadorDao;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Trabajador;
import com.app_rutas.models.enums.Sexo;
import com.app_rutas.models.enums.TipoIdentificacion;

public class TrabajadorServices {

    @SuppressWarnings("FieldMayBeFinal")
    private TrabajadorDao obj;

    public TrabajadorServices() {
        obj = new TrabajadorDao();
    }

    public Trabajador getPersona() {
        return obj.getPersona();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Trabajador> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setPersona(Trabajador Pprsona) {
        obj.setPersona(Pprsona);
    }

    public Trabajador getPersonaById(Integer id) throws Exception {
        return obj.getPersonaById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Trabajador> getPersonasBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Trabajador> order(String atributo, Integer type) throws Exception {
        return obj.order(atributo, type);
    }

    public Trabajador obtenerPersonaPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public TipoIdentificacion getTipo(String tipo) {
        return obj.getTipo(tipo);
    }

    public TipoIdentificacion[] getTipos() {
        return obj.getTipos();
    }

    public Sexo getSexo(String sexo) {
        return obj.getSexo(sexo);
    }

    public Sexo[] getSexos() {
        return obj.getSexos();
    }

    public String[] getPersonaAttributeLists() {
        return obj.getPersonaAttributeLists();
    }

}