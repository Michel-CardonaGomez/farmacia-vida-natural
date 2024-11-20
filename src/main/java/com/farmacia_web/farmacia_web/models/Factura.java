package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
/**
 * Modelo que representa una Factura en el sistema.
 * Incluye información sobre el tipo de factura, su serial único, ruta del archivo asociado
 * y datos de auditoría como la fecha de creación.
 */
@Entity
@Table(name = "facturas")
@Data
public class Factura {

    /**
     * Identificador único de la factura.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Serial único de la factura.
     * Este campo es obligatorio y debe ser único.
     */
    @Column(nullable = false, unique = true)
    private String serial;

    /**
     * Tipo de factura (por ejemplo, "venta" o "compra").
     * Este campo es obligatorio y tiene una longitud máxima de 45 caracteres.
     */
    @Column(length = 45, nullable = false)
    private String tipo;

    /**
     * Ruta del archivo asociado a la factura.
     * Este campo es opcional y tiene una longitud máxima de 100 caracteres.
     * Puede contener la ubicación donde se almacena un archivo PDF o similar relacionado con la factura.
     */
    @Column(name = "ruta_archivo", length = 100)
    private String rutaArchivo;

    /**
     * Fecha y hora en que se creó la factura.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;
}

