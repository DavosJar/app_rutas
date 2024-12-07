package com.app_rutas.controller.dao;

import com.app_rutas.controller.dao.implement.AdapterDao;
import com.app_rutas.controller.dao.implement.Contador;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.Persona;
import com.app_rutas.models.Sexo;
import com.app_rutas.models.TipoIdentificacion;
import com.google.gson.Gson;
import java.lang.reflect.Method;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources" })
public class PersonaDao extends AdapterDao<Persona> {
    private Persona persona;
    private LinkedList<Persona> listAll;

    public PersonaDao() {
        super(Persona.class);
    }

    public Persona getPersona() {
        if (this.persona == null) {
            this.persona = new Persona();
        }
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LinkedList<Persona> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = Contador.obtenerValorActual(Persona.class);
        try {
            this.persona.setId(id);
            this.persist(this.persona);
            Contador.actualizarContador(Persona.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el persona: " + e.getMessage());
        }
    }

    public Boolean update() throws Exception {
        if (this.persona == null || this.persona.getId() == null) {
            throw new Exception("No se ha seleccionado un persona para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getPersonaIndex("id", this.persona.getId());
        if (index == -1) {
            throw new Exception("Persona no encontrado.");
        }
        try {
            this.merge(this.persona, index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar el persona: " + e.getMessage());
        }
    }

    public Boolean delete() throws Exception {
        if (this.persona == null || this.persona.getId() == null) {
            throw new Exception("No se ha seleccionado un persona para eliminar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getPersonaIndex("id", this.persona.getId());
        if (index == -1) {
            throw new Exception("Persona no encontrado.");
        }
        try {
            this.delete(index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar el persona: " + e.getMessage());
        }
    }

    private LinkedList<Persona> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<Persona> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Persona> personas = new LinkedList<>();
        if (!lista.isEmpty()) {
            Persona[] aux = lista.toArray();
            Integer low = 0;
            Integer high = aux.length - 1;
            Integer mid;
            Integer index = -1;
            String searchValue = value.toString().toLowerCase();
            while (low <= high) {
                mid = (low + high) / 2;

                String midValue = obtenerAttributeValue(aux[mid], attribute).toString().toLowerCase();
                System.out.println("Comparando: " + midValue + " con " + searchValue);

                if (midValue.startsWith(searchValue)) {
                    if (mid == 0 || !obtenerAttributeValue(aux[mid - 1], attribute).toString().toLowerCase()
                            .startsWith(searchValue)) {
                        index = mid;
                        break;
                    } else {
                        high = mid - 1;
                    }
                } else if (midValue.compareToIgnoreCase(searchValue) < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            if (index.equals(-1)) {
                return personas;
            }

            Integer i = index;
            while (i < aux.length
                    && obtenerAttributeValue(aux[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
                personas.add(aux[i]);
                System.out.println("Agregando: " + aux[i].getNombre());
                i++;
            }
        }
        return personas;
    }

    public LinkedList<Persona> buscar(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public Persona buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Persona> lista = listAll();
        Persona p = null;

        if (!lista.isEmpty()) {
            Persona[] personas = lista.toArray();
            for (int i = 0; i < personas.length; i++) {
                if (obtenerAttributeValue(personas[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = personas[i];
                    break;
                }
            }
        }
        return p;
    }

    private Integer getPersonaIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Persona[] personas = this.listAll.toArray();
            for (int i = 0; i < personas.length; i++) {
                if (obtenerAttributeValue(personas[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        String normalizedAttribute = "get" + attribute.substring(0, 1).toUpperCase()
                + attribute.substring(1).toLowerCase();
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(normalizedAttribute) && method.getParameterCount() == 0) {
                return method.invoke(object);
            }
        }

        throw new NoSuchMethodException("No se encontor el atributo: " + attribute);
    }

    public String[] getPersonaAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Persona.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Persona> order(String attribute, Integer type) throws Exception {
        LinkedList<Persona> lista = listAll();
        return lista.isEmpty() ? lista : lista.mergeSort(attribute, type);
    }

    public String toJson() throws Exception {
        Gson g = new Gson();
        return g.toJson(this.persona);
    }

    public Persona getPersonaById(Integer id) throws Exception {
        return get(id);
    }

    public String getPersonaJasonByIndex(Integer index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(index));
    }

    public TipoIdentificacion getTipo(String tipo) {
        return TipoIdentificacion.valueOf(tipo);
    }

    public TipoIdentificacion[] getTipos() {
        return TipoIdentificacion.values();
    }

    public Sexo getSexo(String sexo) {
        return Sexo.valueOf(sexo);
    }

    public Sexo[] getSexos() {
        return Sexo.values();
    }

    public String getPersonaJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }

}