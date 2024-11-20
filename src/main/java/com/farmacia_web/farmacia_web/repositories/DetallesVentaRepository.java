package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.DetallesVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link DetallesVenta}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface DetallesVentaRepository extends JpaRepository<DetallesVenta, Long> {

    /**
     * Consulta nativa para calcular la cantidad total de existencias vendidas.
     *
     * @return La cantidad total de productos vendidos como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(cantidad) FROM detalles_ventas", nativeQuery = true)
    Long existenciasvendidas();

    /**
     * Consulta nativa para obtener los 10 productos más vendidos.
     * Combina las tablas `detalles_ventas`, `productos` y `marcas` para calcular el total vendido por producto.
     *
     * @return Una lista de objetos donde cada elemento es un array de dos posiciones:
     *         [0] - Nombre completo del producto (String), incluyendo su marca y presentación.
     *         [1] - Cantidad total vendida (Long).
     */
    @Query(value = "SELECT \n" +
            "    CONCAT(p.nombre, ' ', m.nombre, ' ', p.presentacion) AS producto,\n" +
            "    SUM(dv.cantidad) AS total_vendido\n" +
            "FROM \n" +
            "    detalles_ventas dv\n" +
            "JOIN \n" +
            "    productos p ON dv.id_producto = p.id\n" +
            "JOIN \n" +
            "    marcas m ON p.id_marca = m.id\n" +
            "GROUP BY \n" +
            "    producto\n" +
            "ORDER BY \n" +
            "    total_vendido DESC\n" +
            "LIMIT 10;", nativeQuery = true)
    List<Object[]> topProductos();

    /**
     * Consulta nativa para obtener los 10 proveedores cuyos productos tienen mayor cantidad de ventas.
     * Combina las tablas `detalles_ventas`, `productos` y `proveedores` para calcular el total vendido por proveedor.
     *
     * @return Una lista de objetos donde cada elemento es un array de dos posiciones:
     *         [0] - Nombre del proveedor (String).
     *         [1] - Cantidad total vendida (Long).
     */
    @Query(value = "SELECT p.nombre, SUM(dv.cantidad) AS total_vendido\n" +
            "FROM detalles_ventas dv\n" +
            "JOIN productos pr ON dv.id_producto = pr.id\n" +
            "JOIN proveedores p ON pr.id_proveedor = p.id\n" +
            "GROUP BY p.nombre\n" +
            "ORDER BY total_vendido DESC\n" +
            "LIMIT 10;", nativeQuery = true)
    List<Object[]> topProveedores();
}

