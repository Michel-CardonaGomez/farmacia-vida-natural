package com.farmacia_web.farmacia_web.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Modelo que representa un Proveedor en el sistema.
 * Contiene información básica como nombre, email, teléfono, descripción, y ciudad,
 * además de datos de auditoría como la fecha de creación y actualización.
 */
@Data
@Entity
@Table(name = "proveedores")
public class Proveedor {

    /**
     * Identificador único del proveedor.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Nombre del proveedor.
     * Este campo es obligatorio.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Dirección de correo electrónico del proveedor.
     * Este campo es obligatorio.
     */
    @NotBlank(message = "El email no puede estar vacío.")
    @Pattern(
            regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "El correo electrónico debe tener un formato válido, como ejemplo@dominio.com."
    )
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Número de teléfono del proveedor.
     * Este campo es obligatorio.
     */
    @NotNull(message = "El teléfono no puede ser nulo.")
    @Digits(integer = 10, fraction = 0, message = "El teléfono debe tener hasta 10 dígitos.")
    @Column(name = "telefono", nullable = false)
    private Long telefono;

    /**
     * Descripción del proveedor.
     * Puede incluir información sobre los productos o servicios que ofrece.
     * Este campo es obligatorio.
     */
    @NotNull(message = "La descripción no puede estar vacía.")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    /**
     * Ciudad en la que opera el proveedor.
     * Este campo es obligatorio.
     */
    @NotNull(message = "La ciudad no puede estar vacía.")
    @Size(max = 255, message = "El nombre de la ciudad no puede tener más de 255 caracteres.")
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    /**
     * Fecha y hora en que se creó el proveedor.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización del proveedor.
     * Este valor se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;
}

