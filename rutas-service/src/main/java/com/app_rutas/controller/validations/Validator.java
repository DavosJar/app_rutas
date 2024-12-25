package com.app_rutas.controller.validations;

import java.util.regex.Pattern;

public class Validator {
    /*
     * @param value: Valor a validar.
     * 
     * @param types: Tipos de validación a aplicar.
     * 
     * @return Boolean: Retorna verdadero si el valor cumple con las validaciones.
     * 
     * @throws IllegalArgumentException: Lanza una excepción si el valor no cumple
     * con las validaciones.
     * 
     * @throws NumberFormatException: Lanza una excepción si el valor no es un
     * número.
     *
     * Instruccinoes:
     * Par el método validateStringField, se debe validar que el valor no sea nulo y
     * que los tipos no sean nulos.
     * Los tipos de validación son los siguientes:
     * IS_NUMERIC: El valor debe contener solo números.
     * IS_ALPHABETIC: El valor debe contener solo letras y espacios.
     * IS_ALPHANUMERIC: El valor debe contener solo letras, números y espacios.
     * IS_VALID_EMAIL: El valor debe ser un correo electrónico válido.
     * MAX_LENGTH: El valor no debe exceder el número de caracteres especificado.
     * ejem: "MAX_LENGTH=10"
     * MIN_LENGTH: El valor debe tener al menos el número de caracteres
     * especificado. ejem: "MIN_LENGTH=5"
     * EXACT_LENGTH: El valor debe tener exactamente el número de caracteres
     * especificado. ejem: "EXACT_LENGTH=8"
     *
     */
    public static Boolean validateStringField(Object value, String... types) {
        System.out.println("Iniciando validación de campo String: " + value.toString());
        if (types == null || types.length == 0) {
            throw new IllegalArgumentException("Debe especificar al menos un tipo de validación.");
        }
        System.out.println("Iniciando validación de campo String: " + value.toString());
        String stringValue = value == null ? "" : value.toString().trim();

        for (String typeWithParams : types) {
            String[] typeAndParam = parseTypeWithParams(typeWithParams);
            String typeName = typeAndParam[0];
            int param = Integer.parseInt(typeAndParam[1]);

            switch (typeName.toUpperCase()) {
                case "NOT_NULL":
                    if (stringValue.isEmpty()) {
                        throw new IllegalArgumentException("El campo es obligatorio y no puede estar vacío.");
                    }
                    break;

                case "NUMERIC":
                    validatePattern(stringValue, "\\d+", "El valor debe contener solo números.");
                    break;

                case "ALPHABETIC":
                    validatePattern(stringValue, "[A-Za-zÁÉÍÓÚáéíóúÜüÑñ\\s]+",
                            "El valor debe contener solo letras y espacios.");
                    break;

                case "ALPHANUMERIC":
                    validatePattern(stringValue, "[A-Za-z0-9ÁÉÍÓÚáéíóúÜüÑñ\\s]+",
                            "El valor debe contener solo letras, números y espacios.");
                    break;

                case "VALID_EMAIL":
                    validatePattern(stringValue, "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                            "El valor debe ser un correo electrónico válido.");
                    break;

                case "MAX_LENGTH":
                    validateMaxLength(stringValue, param);
                    break;

                case "MIN_LENGTH":
                    validateMinLength(stringValue, param);
                    break;

                case "LENGTH":
                    validateExactLength(stringValue, param);
                    break;

                default:
                    throw new IllegalArgumentException("Validación no implementada para: " + typeName);
            }
        }
        System.out.println("Validación exitosa para el campo String: " + value);

        return true;
    }

    /*
     * Parsea el tipo de validación y su parámetro si lo tiene.
     * 
     * @param typeWithParams: Tipo de validación con su parámetro si lo tiene.
     * 
     * @return String[]: Arreglo con el tipo de validación y su parámetro.
     *
     * Instrucciones:
     * Escribe el tipo de validadion y su parámetro si lo tiene. ejem:
     * "MAX_VALUE=1000"
     */
    private static String[] parseTypeWithParams(String typeWithParams) {
        if (typeWithParams == null || typeWithParams.isEmpty()) {
            throw new IllegalArgumentException("El tipo de validación no puede estar vacío.");
        }

        String type = typeWithParams;
        String param = "-1";

        int separatorIndex = typeWithParams.lastIndexOf('=');
        if (separatorIndex != -1) {
            String potentialParam = typeWithParams.substring(separatorIndex + 1);

            if (potentialParam.matches("\\d+") || potentialParam.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                type = typeWithParams.substring(0, separatorIndex);
                param = potentialParam;
            } else {
                throw new IllegalArgumentException("Formato de parámetro no válido: " + potentialParam);
            }
        }

        return new String[] { type, param };
    }

    private static void validatePattern(String value, String regex, String errorMessage) {
        if (!Pattern.matches(regex, value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void validateMaxLength(String value, int param) {
        if (param != -1 && value.length() > param) {
            throw new IllegalArgumentException("El valor no debe exceder " + param + " caracteres.");
        }
    }

    private static void validateMinLength(String value, int param) {
        if (param != -1 && value.length() < param) {
            throw new IllegalArgumentException("El valor debe tener al menos " + param + " caracteres.");
        }
    }

    private static void validateExactLength(String value, int param) {
        if (param != -1 && value.length() != param) {
            throw new IllegalArgumentException("El valor debe tener exactamente " + param + " caracteres.");
        }
    }

    public static Boolean validNumberField(Object value, String... types) {
        if (types == null || types.length == 0) {
            throw new IllegalArgumentException("Debe especificar al menos un tipo de validación.");
        }

        if (value == null) {
            for (String typeWithParams : types) {
                String[] typeAndParam = parseTypeWithParams(typeWithParams);
                String type = typeAndParam[0];

                if ("NOT_NULL".equalsIgnoreCase(type)) {
                    throw new IllegalArgumentException("El campo es obligatorio y no puede estar vacío.");
                }
            }
            return false;
        }

        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("El valor debe ser un número válido.");
        }

        Number numValue = (Number) value;
        double absValue = Math.abs(numValue.doubleValue());

        for (String typeWithParams : types) {
            String[] typeAndParam = parseTypeWithParams(typeWithParams);
            String type = typeAndParam[0];
            String param = typeAndParam[1];

            switch (type) {

                case "MIN_VALUE":
                    if (numValue.doubleValue() < 0) {
                        throw new IllegalArgumentException("El valor no puede ser negativo.");
                    }
                    break;

                case "MAX_VALUE":
                    validDataMaxValue(numValue, param, absValue);
                    break;

                case "RANGE":
                    validDateRange(param, absValue);
                    break;

                default:
                    throw new IllegalArgumentException("Validación no implementada para: " + type);
            }
        }

        return true;
    }

    private static void validDataMaxValue(Number value, String params, double absValue) {
        if (!params.isEmpty()) {
            try {
                double maxValue = Double.parseDouble(params);
                if (absValue > maxValue) {
                    throw new IllegalArgumentException("El valor no puede ser mayor a " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Formato inválido en MAX_VALUE: " + params);
            }
        } else if (absValue > 999999999) {
            throw new IllegalArgumentException("El valor no puede ser mayor a 999999999.");
        }
    }

    private static void validDateRange(String params, double absValue) {
        if (!params.isEmpty()) {
            try {
                String[] range = params.split(",");
                if (range.length != 2) {
                    throw new IllegalArgumentException("Formato inválido en RANGE: " + params);
                }
                double minValue = Double.parseDouble(range[0].trim());
                double maxValue = Double.parseDouble(range[1].trim());
                if (absValue < minValue || absValue > maxValue) {
                    throw new IllegalArgumentException(
                            "El valor debe estar entre " + minValue + " y " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Formato inválido en RANGE: " + params);
            }
        } else {
            throw new IllegalArgumentException("RANGE requiere parámetros.");
        }
    }

}
