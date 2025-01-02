package com.app_rutas.controller.dao.services;

import java.util.HashMap;

import com.app_rutas.controller.dao.TrabajadorDao;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Trabajador;
import com.app_rutas.models.enums.Sexo;
import com.app_rutas.models.enums.TipoIdentificacion;

public class TrabajadorServices {

    private TrabajadorDao obj;

    public TrabajadorServices() {
        obj = new TrabajadorDao();
    }

    public Trabajador getTrabajador() {
        return obj.getTrabajador();
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

    public void setTrabajador(Trabajador trabajador) {
        obj.setTrabajador(trabajador);
    }

    public Trabajador getById(Integer id) throws Exception {
        return obj.getTrabajadorById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Trabajador> getTrabajadoresBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Trabajador> order(String atributo, Integer type) throws Exception {
        return obj.order(atributo, type);
    }

    public Trabajador obtenerTrabajadorPor(String atributo, Object valor) throws Exception {
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

    public String[] getTrabajadorAttributeLists() {
        return obj.getTrabajadorAttributeLists();
    }

    public Boolean isUnique(String campo, Object value) throws Exception {
        Boolean unicTrabajador;
        Boolean unicConductor;
        unicTrabajador = obj.isUnique(campo, value);
        ConductorServices cs = new ConductorServices();
        unicConductor = cs.isUnique(campo, value);
        return unicTrabajador && unicConductor;
    }

    public void validateField(String field, HashMap<String, Object> map, String... validations) throws Exception {
        Trabajador persona = this.getTrabajador();
        FieldValidator.validateAndSet(persona, map, field, validations);
    }
}