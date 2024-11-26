package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

/**
 * Modelo que representa una Subcategoría en el sistema.
 * Está relacionada con una Categoría y contiene información básica como su nombre,
 * así como datos de auditoría como la fecha de creación y actualización.
 */
@Data
@Entity
@Table(name = "subcategorias")
public class Subcategoria {

    /**
     * Identificador único de la subcategoría.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nombre de la subcategoría.
     * Este campo es opcional.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre")
    private String nombre;

    /**
     * Fecha y hora en que se creó la subcategoría.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización de la subcategoría.
     * Este valor se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;

    /**
     * Relación con la Categoría a la que pertenece esta subcategoría.
     * Este campo es obligatorio y establece una llave foránea hacia la entidad {@link Categoria}.
     */
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}

