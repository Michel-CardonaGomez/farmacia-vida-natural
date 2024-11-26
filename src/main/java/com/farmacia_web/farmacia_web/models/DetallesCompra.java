package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
/**
 * Modelo que representa los detalles de una compra en el sistema.
 * Cada detalle de compra está asociado a un producto específico y a una compra,
 * e incluye información como el precio de compra, la cantidad y el subtotal.
 */
@Entity
@Table(name = "detalles_compras")
@Data
public class DetallesCompra {

    /**
     * Identificador único del detalle de compra.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación con la compra a la que pertenece este detalle.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    /**
     * Relación con el producto adquirido en este detalle de compra.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Precio unitario del producto al momento de la compra.
     * Este campo es obligatorio y se almacena con una precisión de hasta 12 dígitos
     * y 2 decimales.
     */
    @Column(name = "precio_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompra;

    /**
     * Cantidad del producto adquirida en esta compra.
     * Este campo es obligatorio.
     */
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Subtotal correspondiente al producto en este detalle de compra.
     * Calculado como la cantidad multiplicada por el precio unitario.
     * Este campo es obligatorio y se almacena con una precisión de hasta 12 dígitos
     * y 2 decimales.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
}

