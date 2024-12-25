package com.app_rutas.controller.dao.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.app_rutas.controller.excepcion.ValueAlreadyExistException;
import com.app_rutas.controller.validations.Validator;

public class FieldValidator {

    private static String SERVICE_URL = "com.app_rutas.controller.dao.services.";

    /*
     * @param target: Objeto que invoca el método.
     * 
     * @param map: Mapa con los campos y valores a validar.
     * 
     * @param field: Campo a validar.
     * 
     * @param validations: Validaciones a aplicar.
     * 
     * @throws Exception: Lanza una excepción si el campo no existe en la clase.
     * Funcionalidad:
     * El metodo validateAndSet se encarga de validar los campos de un objeto y
     * asignarles un valor.
     * El metodo recibe un objeto, un mapa con los campos y valores a validar, el
     * campo a validar y las validaciones a aplicar.
     * El metodo valida si el campo es nulo, si es obligatorio y si es unico.
     * Si el campo es un String, se valida que cumpla con las validaciones
     * especificadas.
     */
    public static void validateAndSet(Object target, HashMap<String, Object> map, String field, String... validations)
            throws Exception {
        Object value = map.get(field);

        if (value == null) {
            for (String validation : validations) {
                if ("NOT_NULL".equals(validation)) {
                    throw new IllegalArgumentException("El campo '" + field + "' es obligatorio.");
                }
            }
        }

        /*
         * La verificacion de unicidad tiene un origen distinto a las validaciones
         * estandar por tanto debe ejecutarse
         * en un bloque de código separado.
         */
        for (String validation : validations) {
            if ("IS_UNIQUE".equals(validation)) {
                if (!checkUniqueness(target, field, value)) {
                    throw new ValueAlreadyExistException(
                            "El valor del campo '" + field + "' (" + value + ") ya existe.");
                }
                // validateStringField((String) value, validation), valida Strings, sean
                // numeros, letras, etc
            } else if (value instanceof String) {
                Validator.validateStringField((String) value, validation);
            } else if (value instanceof Number) {
                // validateNumberField((Number) value, validation), valida numeros
                Validator.validNumberField((Number) value, validation);
            }
        }

        try {
            // Asignacion del valor al campo
            Field fieldObj = target.getClass().getDeclaredField(field);
            fieldObj.setAccessible(true);
            fieldObj.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldException("El campo '" + field + "' no existe en la clase.");
        }
    }
    /*
     * La verificacion de unicida se encuantra en una funcin dentro de cada service
     * checkUniqueness construye la clase service correspondiente a partir del
     * objeto recibido al
     * llamarla en el servicse ejemplo Persona + Service para el objeto
     * PersonaService
     * El metodo isUnique se encarga de verificar si el valor ya existe en los
     * archivos
     * 
     * @param target: Objeto que invoca el método.
     * 
     * @param field: Campo a validar.
     * 
     * @param value: Valor a validar.
     * 
     */

    private static Boolean checkUniqueness(Object target, String field, Object value) throws Exception {
        try {
            // Construccion del nombre correcto del service, toda Clase tiene su
            // ClaseServices
            String targetClassName = target.getClass().getSimpleName();
            String serviceClassName = targetClassName + "Services";
            Class<?> serviceClass = Class.forName(SERVICE_URL + serviceClassName);

            Object service = serviceClass.getDeclaredConstructor().newInstance();
            Method isUniqueMethod = service.getClass().getMethod("isUnique", String.class, Object.class);
            // Invocacion del metodo isUnique, debe estar en presente en cada service donde
            // se requiera verificar unicidad
            boolean isUnique = (boolean) isUniqueMethod.invoke(service, field, value);

            return isUnique;

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ValueAlreadyExistException) {
                e.printStackTrace();
                return false;
            }
            System.out.println("Error inesperado en isUnique: " + cause.getMessage());
            throw new Exception("Error al invocar el método isUnique: " + cause.getMessage());
        } catch (Exception e) {
            System.out.println("Error al invocar el método isUnique: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
}