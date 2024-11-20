package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * Modelo que representa una Venta en el sistema.
 * Incluye información sobre el cliente, empleado, factura asociada,
 * total de la venta, método de pago, y detalles de los productos vendidos.
 */
@Entity
@Table(name = "ventas")
@Data
public class Venta {

    /**
     * Identificador único de la venta.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación con el cliente que realizó la compra.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    /**
     * Relación con el empleado que gestionó la venta.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    /**
     * Relación con la factura asociada a la venta.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    /**
     * Total de la venta.
     * Representado como un valor decimal con una precisión de hasta 12 dígitos y 2 decimales.
     * Este campo es obligatorio.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    /**
     * Fecha y hora en que se creó la venta.
     * Este valor se genera automáticamente al insertar el registro y no puede ser actualizado.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Timestamp creacion;

    /**
     * Método de pago utilizado para esta venta (por ejemplo, "efectivo", "tarjeta").
     * Este campo es obligatorio.
     */
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    /**
     * Lista de detalles de la venta, que representan los productos vendidos y sus cantidades.
     * La relación es uno a muchos, con cascada y eliminación huérfana activadas.
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallesVenta> detallesVenta = new ArrayList<>();
}
