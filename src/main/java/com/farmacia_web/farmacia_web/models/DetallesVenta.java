package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Modelo que representa los detalles de una venta en el sistema.
 * Cada detalle de venta está asociado a un producto específico y a una venta,
 * e incluye información como el precio de venta, la cantidad y el subtotal.
 */
@Entity
@Table(name = "detalles_ventas")
@Data
public class DetallesVenta {

    /**
     * Identificador único del detalle de venta.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación con la venta a la que pertenece este detalle.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    /**
     * Relación con el producto vendido en este detalle de venta.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Precio unitario del producto al momento de la venta.
     * Este campo es obligatorio y se almacena con una precisión de hasta 12 dígitos
     * y 2 decimales.
     */
    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    /**
     * Cantidad del producto vendida en esta transacción.
     * Este campo es obligatorio.
     */
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Subtotal correspondiente al producto en este detalle de venta.
     * Calculado como la cantidad multiplicada por el precio unitario.
     * Este campo es obligatorio y se almacena con una precisión de hasta 12 dígitos
     * y 2 decimales.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
}


