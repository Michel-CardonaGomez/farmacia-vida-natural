package com.farmacia_web.farmacia_web.models;

import lombok.Data;

/**
 * Data Transfer Object (DTO) que representa los datos de un empleado
 * utilizados para operaciones de actualización, como cambiar la contraseña o el número de teléfono.
 */
@Data
public class EmpleadoDTO {

    /**
     * Número de teléfono del empleado.
     * Este valor puede ser actualizado.
     */
    private Long telefono;

    /**
     * Contraseña actual del empleado.
     * Se utiliza para verificar la identidad del empleado antes de permitir cambios.
     */
    private String contrasenaActual;

    /**
     * Nueva contraseña que el empleado desea establecer.
     * Este campo se utiliza para actualizar la contraseña del empleado.
     */
    private String contrasenaNueva;

}

