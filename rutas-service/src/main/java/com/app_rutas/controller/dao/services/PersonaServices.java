package com.app_rutas.controller.dao.services;

import com.app_rutas.controller.dao.PersonaDao;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Persona;
import com.app_rutas.models.Sexo;
import com.app_rutas.models.TipoIdentificacion;

public class PersonaServices {

    @SuppressWarnings("FieldMayBeFinal")
    private PersonaDao obj;

    public PersonaServices() {
        obj = new PersonaDao();
    }

    public Persona getPersona() {
        return obj.getPersona();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Persona> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setPersona(Persona Pprsona) {
        obj.setPersona(Pprsona);
    }

    public Persona getPersonaById(Integer id) throws Exception {
        return obj.getPersonaById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Persona> getPersonasBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Persona> order(String atributo, Integer type) throws Exception {
        return obj.order(atributo, type);
    }

    public Persona obtenerPersonaPor(String atributo, Object valor) throws Exception {
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