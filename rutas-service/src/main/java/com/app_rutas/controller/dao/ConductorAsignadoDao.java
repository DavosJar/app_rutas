package com.app_rutas.controller.dao;

import com.app_rutas.controller.dao.implement.AdapterDao;
import com.app_rutas.controller.dao.implement.Contador;
import com.app_rutas.controller.tda.list.LinkedList;
import com.app_rutas.models.ConductorAsignado;
import com.app_rutas.models.enums.ConductorEstado;
import com.google.gson.Gson;

import java.lang.reflect.Method;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources" })
public class ConductorAsignadoDao extends AdapterDao<ConductorAsignado> {
    private ConductorAsignado conductorAsignado;
    private LinkedList<ConductorAsignado> listAll;

    public ConductorAsignadoDao() {
        super(ConductorAsignado.class);
    }

    public ConductorAsignado getConductor() {
        if (this.conductorAsignado == null) {
            this.conductorAsignado = new ConductorAsignado();
        }
        return this.conductorAsignado;
    }

    public void setConductor(ConductorAsignado conductorAsignado) {
        this.conductorAsignado = conductorAsignado;
    }

    public LinkedList<ConductorAsignado> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = Contador.obtenerValorActual(ConductorAsignado.class);
        try {
            this.conductorAsignado.setId(id);
            this.persist(this.conductorAsignado);
            Contador.actualizarContador(ConductorAsignado.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el conductorAsignado: " + e.getMessage());
        }
    }

    public Boolean update() throws Exception {
        if (this.conductorAsignado == null || this.conductorAsignado.getId() == null) {
            throw new Exception("No se ha seleccionado un conductorAsignado para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getByIndex("id", this.conductorAsignado.getId());
        if (index == -1) {
            throw new Exception("ConductorAsignado no encontrado.");
        }
        try {
            this.merge(this.conductorAsignado, index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar el conductorAsignado: " + e.getMessage());
        }
    }

    public Boolean delete() throws Exception {
        if (this.conductorAsignado == null || this.conductorAsignado.getId() == null) {
            throw new Exception("No se ha seleccionado un conductorAsignado para eliminar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getByIndex("id", this.conductorAsignado.getId());
        if (index == -1) {
            throw new Exception("ConductorAsignado no encontrado.");
        }
        try {
            this.delete(index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar el conductorAsignado: " + e.getMessage());
        }
    }

    private LinkedList<ConductorAsignado> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<ConductorAsignado> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<ConductorAsignado> ordenes = new LinkedList<>();
        if (!lista.isEmpty()) {
            ConductorAsignado[] aux = lista.toArray();
            Integer low = 0;
            Integer high = aux.length - 1;
            Integer mid;
            Integer index = -1;
            String searchValue = value.toString().toLowerCase();
            while (low <= high) {
                mid = (low + high) / 2;

                String midValue = obtenerAttributeValue(aux[mid], attribute).toString().toLowerCase();
                //System.out.println("Comparando: " + midValue + " con " + searchValue);

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
                return ordenes;
            }

            Integer i = index;
            while (i < aux.length
                    && obtenerAttributeValue(aux[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
                ordenes.add(aux[i]);
                i++;
            }
        }
        return ordenes;
    }

    public LinkedList<ConductorAsignado> buscar(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public ConductorAsignado buscarPor(String attribute, Object value) throws Exception {
        LinkedList<ConductorAsignado> lista = listAll();
        ConductorAsignado p = null;

        if (!lista.isEmpty()) {
            ConductorAsignado[] ordenes = lista.toArray();
            for (int i = 0; i < ordenes.length; i++) {
                if (obtenerAttributeValue(ordenes[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = ordenes[i];
                    break;
                }
            }
        }
        return p;
    }

    private Integer getByIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            ConductorAsignado[] ordenes = this.listAll.toArray();
            for (int i = 0; i < ordenes.length; i++) {
                if (obtenerAttributeValue(ordenes[i], attribute).toString().toLowerCase()
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

    public String[] getOrdenAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : ConductorAsignado.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public LinkedList<ConductorAsignado> order(String attribute, Integer type) throws Exception {
        LinkedList<ConductorAsignado> lista = listAll();
        return lista.isEmpty() ? lista : lista.mergeSort(attribute, type);
    }

    public String toJson() throws Exception {
        Gson g = new Gson();
        return g.toJson(this.conductorAsignado);
    }

    public ConductorAsignado getById(Integer id) throws Exception {
        return get(id);
    }

    public String getByJasonByIndex(Integer index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(index));
    }

    public ConductorEstado getEstadoEnum(String estado) {
        return ConductorEstado.valueOf(estado);
    }

    public ConductorEstado[] getEstadoEnum() {
        return ConductorEstado.values();
    }

    public String getByJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }
}