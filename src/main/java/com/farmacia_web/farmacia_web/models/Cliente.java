package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
/**
 * Modelo que representa un Cliente en el sistema.
 * Contiene información básica del cliente, como identificación, nombre, email y teléfono,
 * además de datos de auditoría como fecha de creación y actualización.
 */
@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    /**
     * Identificador único del cliente.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Número de identificación del cliente.
     * Este campo es obligatorio.
     * La cédula debe ser un número positivo.
     */
    @NotNull(message = "La identificación no puede estar vacía.")
    @Min(value = 1, message = "La cédula debe ser un número positivo")
    @Column(name = "identificacion", nullable = false)
    private Long identificacion;

    /**
     * Nombre completo del cliente.
     * Este campo es obligatorio.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Correo electrónico del cliente.
     * Este campo es opcional.
     * Se valida que el correo tenga un formato válido.
     */
    @NotBlank(message = "El email no puede estar vacío.")
    @Pattern(
            regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "El correo electrónico debe tener un formato válido."
    )
    @Column(name = "email")
    private String email;

    /**
     * Número de teléfono del cliente.
     * Este campo es opcional.
     * Se valida que el teléfono solo contenga números y caracteres especiales como +, -, (, ).
     */
    @NotBlank(message = "El telefono no puede estar vacío.")
    @Digits(integer = 10, fraction = 0, message = "El teléfono debe tener hasta 10 dígitos.")
    @Column(name = "telefono")
    private String telefono;

    /**
     * Fecha y hora en que se creó el cliente.
     * Este valor se genera automáticamente al insertar el registro.
     * Este campo no es actualizable.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización de los datos del cliente.
     * Este valor se actualiza automáticamente al modificar el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;
}
