package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Modelo que representa un Producto en el sistema.
 * Incluye información como código, nombre, descripción, precios, existencias,
 * y relaciones con subcategoría, proveedor y marca, además de datos de auditoría.
 */
@Data
@Entity
@Table(name = "productos")
public class Producto {

    /**
     * Identificador único del producto.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código único del producto.
     * Este campo es obligatorio, único, y tiene una longitud máxima de 55 caracteres.
     */
    @NotBlank(message = "El código no puede estar vacío.")
    @Pattern(regexp = "^[A-Za-z]{3}-\\d{3}$", message = "El código debe tener el formato 'AAA-123'.")
    @Column(name = "codigo", unique = true, nullable = false, length = 55)
    private String codigo;

    /**
     * Nombre del producto.
     * Este campo es obligatorio y tiene una longitud máxima de 255 caracteres.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    /**
     * Descripción del producto.
     * Este campo es obligatorio y tiene una longitud máxima de 255 caracteres.
     */
    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    /**
     * Precio de compra del producto.
     * Este campo es obligatorio.
     */
    @NotNull(message = "El precio de compra es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio de compra debe ser mayor que 0.")
    @Column(name = "precio_compra", nullable = false)
    private BigDecimal precioCompra;

    /**
     * Precio de venta del producto.
     * Este campo es obligatorio.
     */
    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio de venta debe ser mayor que 0.")
    @Column(name = "precio_venta", nullable = false)
    private BigDecimal precioVenta;

    /**
     * Número de existencias del producto en inventario.
     * Este campo es obligatorio.
     */
    @Column(name = "existencias", nullable = false)
    private int existencias;

    /**
     * Relación con la subcategoría a la que pertenece el producto.
     * Este campo es obligatorio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subcategoria", nullable = false)
    private Subcategoria subcategoria;

    /**
     * Relación con el proveedor del producto.
     * Este campo es opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = true)
    private Proveedor proveedor;

    /**
     * Relación con la marca del producto.
     * Este campo es obligatorio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    /**
     * Presentación del producto (por ejemplo, "500ml", "Caja de 12").
     * Este campo es obligatorio y tiene una longitud máxima de 100 caracteres.
     */
    @NotBlank(message = "La presentación no puede estar vacía.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    @Column(name = "presentacion", nullable = false, length = 100)
    private String presentacion;

    /**
     * Estado del producto (por ejemplo, "Disponible", "Descontinuado").
     * Este campo es obligatorio y tiene una longitud máxima de 100 caracteres.
     */
    @Column(name = "estado", nullable = false, length = 100)
    private String estado;

    /**
     * Porcentaje de IVA aplicado al producto.
     * Este campo es obligatorio.
     */
    @Column(name = "iva", nullable = false)
    private int iva;

    /**
     * Fecha y hora en que se creó el producto.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Fecha y hora de la última actualización del producto.
     * Este valor se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Timestamp actualizacion;
}

