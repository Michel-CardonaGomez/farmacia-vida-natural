package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Factura;
import com.farmacia_web.farmacia_web.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Venta}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Busca una venta asociada a una factura específica.
     *
     * @param factura La factura relacionada con la venta.
     * @return Un {@link Optional} que contiene la venta si se encuentra, o vacío en caso contrario.
     */
    Optional<Venta> findByFactura(Factura factura);

    /**
     * Consulta nativa para contar el número total de ventas registradas en el sistema.
     *
     * @return El número total de ventas como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT COUNT(*) FROM ventas", nativeQuery = true)
    Long totalVentas();

    /**
     * Consulta nativa para calcular el importe total de todas las ventas realizadas.
     *
     * @return El importe total vendido como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(total) FROM ventas", nativeQuery = true)
    Long importeVendido();

    /**
     * Consulta nativa para calcular el beneficio neto de todas las ventas.
     * El beneficio neto considera el margen de ganancia menos los impuestos (IVA).
     *
     * @return El beneficio neto como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(d.cantidad * (p.precio_venta - p.precio_compra)) - " +
            "SUM(d.cantidad * p.precio_venta * (p.iva / 100)) AS beneficioNeto " +
            "FROM detalles_ventas d " +
            "JOIN productos p ON d.id_producto = p.id " +
            "JOIN ventas v ON d.id_venta = v.id", nativeQuery = true)
    Long obtenerBeneficioNeto();

    /**
     * Consulta nativa para calcular el beneficio bruto de todas las ventas.
     * El beneficio bruto considera únicamente el margen de ganancia sin deducir impuestos (IVA).
     *
     * @return El beneficio bruto como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(d.cantidad * (p.precio_venta - p.precio_compra)) AS beneficioBruto " +
            "FROM detalles_ventas d " +
            "JOIN productos p ON d.id_producto = p.id " +
            "JOIN ventas v ON d.id_venta = v.id", nativeQuery = true)
    Long obtenerBeneficioBruto();

    /**
     * Consulta nativa para calcular el total de IVA recaudado en las ventas.
     *
     * @return El total de IVA recaudado como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(d.cantidad * p.precio_venta * (p.iva / 100)) AS totalIva " +
            "FROM detalles_ventas d " +
            "JOIN productos p ON d.id_producto = p.id " +
            "JOIN ventas v ON d.id_venta = v.id", nativeQuery = true)
    Long obtenerTotalIva();

    /**
     * Consulta nativa para obtener los empleados con más ventas registradas.
     *
     * @return Una lista de objetos, donde cada objeto es un array con dos posiciones:
     *         [0] - Nombre del empleado (String).
     *         [1] - Número total de ventas realizadas (Long).
     */
    @Query(value = "SELECT e.nombre, COUNT(v.id) AS total_ventas " +
            "FROM empleados e " +
            "JOIN ventas v ON e.id = v.id_empleado " +
            "GROUP BY e.nombre " +
            "ORDER BY total_ventas DESC", nativeQuery = true)
    List<Object[]> findEmpleadosConMasVentas();

    /**
     * Consulta nativa para contar el número de transacciones por método de pago.
     *
     * @return Una lista de objetos, donde cada objeto es un array con dos posiciones:
     *         [0] - Método de pago (String).
     *         [1] - Número total de transacciones (Long).
     */
    @Query(value = "SELECT metodo_pago, COUNT(*) AS cantidad_transacciones " +
            "FROM ventas " +
            "GROUP BY metodo_pago", nativeQuery = true)
    List<Object[]> contarMetodosPago();
}

