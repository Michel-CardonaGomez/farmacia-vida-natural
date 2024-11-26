package com.farmacia_web.farmacia_web.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa una Compra en el sistema.
 * Contiene información relacionada con el proveedor, empleado, factura, detalles de la compra,
 * total de la compra, método de pago y datos de auditoría.
 */
@Entity
@Table(name = "compras")
@Data
public class Compra {

    /**
     * Identificador único de la compra.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación con el proveedor que suministró los productos en esta compra.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    /**
     * Relación con el empleado que registró esta compra.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    /**
     * Total monetario de la compra.
     * Representado como un valor decimal.
     */
    private BigDecimal total;

    /**
     * Relación con la factura asociada a esta compra.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    /**
     * Fecha y hora en que se registró la compra.
     * Este valor se genera automáticamente al insertar el registro.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private Timestamp creacion;

    /**
     * Método de pago utilizado en esta compra (por ejemplo, "efectivo", "tarjeta").
     * Este campo es obligatorio.
     */
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    /**
     * Lista de detalles de la compra.
     * Representa los productos incluidos en esta compra y sus cantidades.
     * La relación es uno a muchos con cascada y eliminación huérfana activadas.
     */
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallesCompra> detallesCompra = new ArrayList<>();
}

