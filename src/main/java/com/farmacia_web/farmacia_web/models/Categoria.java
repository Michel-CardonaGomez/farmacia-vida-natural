package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
/**
 * Modelo que representa una Categoría en el sistema.
 * Cada categoría puede tener múltiples subcategorías asociadas.
 * También incluye información de auditoría como la fecha de creación y actualización.
 */
@Data
@Entity
@Table(name = "categorias")
public class Categoria {

    /**
     * Identificador único de la categoría.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Nombre de la categoría.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre")
    private String nombre;

    /**
     * Relación uno a muchos con Subcategoría.
     * Cada categoría puede tener múltiples subcategorías asociadas.
     * Las operaciones en cascada se aplican para persistencia y eliminación.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subcategoria> subcategorias;

    /**
     * Fecha y hora en que se creó la categoría.
     * Se genera automáticamente al momento de insertar un registro.
     * Este campo no es actualizable.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización de la categoría.
     * Se actualiza automáticamente cada vez que se modifica un registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;
}
