package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
/**
 * Modelo que representa una Marca en el sistema.
 * Incluye información básica como el nombre de la marca y datos de auditoría como la fecha de creación y actualización.
 */
@Data
@Entity
@Table(name = "marcas")
public class Marca {

    /**
     * Identificador único de la marca.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Nombre de la marca.
     * Este campo es opcional.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre")
    private String nombre;

    /**
     * Fecha y hora en que se creó la marca.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización de la marca.
     * Este valor se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;
}

