package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
/**
 * Modelo que representa un Empleado en el sistema.
 * Contiene información personal, credenciales, rol, estado y datos de auditoría.
 */
@Data
@Entity
@Table(name = "empleados")
public class Empleado {

        /**
         * Identificador único del empleado.
         * Generado automáticamente por la base de datos.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        /**
         * Número de identificación único del empleado.
         * Este campo es obligatorio y debe ser único.
         */
        @NotNull(message = "La identificación no puede estar vacío.")
        @Min(value = 1, message = "La cédula debe ser un número positivo")
        @Column(name = "identificacion", nullable = false, unique = true)
        private Long identificacion;

        /**
         * Nombre completo del empleado.
         * Este campo es obligatorio.
         */
        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
        @Column(name = "nombre", nullable = false)
        private String nombre;

        /**
         * Número de teléfono del empleado.
         * Este campo es obligatorio.
         */
        @NotNull(message = "El telefono no puede estar vacío.")
        @Digits(integer = 10, fraction = 0, message = "El teléfono debe tener hasta 10 dígitos.")
        @Column(name = "telefono", nullable = false)
        private Long telefono;

        /**
         * Rol asignado al empleado dentro del sistema (por ejemplo, "Administrador", "Vendedor").
         * Este campo es obligatorio.
         */
        @NotBlank(message = "El rol no puede estar vacío.")
        @Size(max = 255, message = "El rol no puede tener más de 255 caracteres.")
        @Column(name = "rol", nullable = false)
        private String rol;

        /**
         * Dirección de correo electrónico del empleado.
         * Este campo es obligatorio y debe ser único.
         */
        @NotBlank(message = "El email no puede estar vacío.")
        @Pattern(
                regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "El correo electrónico debe tener un formato válido, como ejemplo@dominio.com."
        )
        @Column(name = "email", unique = true, nullable = false)
        private String email;

        /**
         * Contraseña encriptada del empleado para autenticación.
         * Este campo es opcional.
         */
        @Column(name = "contraseña")
        private String contrasena;

        /**
         * Fecha y hora en que se registró el empleado.
         * Este valor se genera automáticamente al insertar el registro y no se puede actualizar.
         */
        @CreationTimestamp
        @Column(name = "fecha_creacion", updatable = false)
        private Timestamp creacion;

        /**
         * Fecha y hora de la última actualización de los datos del empleado.
         * Este valor se actualiza automáticamente cada vez que se modifica el registro.
         */
        @UpdateTimestamp
        @Column(name = "fecha_actualizacion")
        private Timestamp actualizacion;

        /**
         * Estado del empleado, indica si está activo o inactivo en el sistema, con logueo tradicional.
         * Por ejemplo, `true` para activo, `false` para inactivo.
         */
        @Column(name = "activo")
        private Boolean activo;

}
