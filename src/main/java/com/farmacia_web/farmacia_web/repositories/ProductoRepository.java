package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Producto}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos según su estado.
     *
     * @param estado El estado de los productos (por ejemplo, "Disponible", "Descontinuado").
     * @return Una lista de productos con el estado especificado.
     */
    ArrayList<Producto> findByEstado(String estado);

    /**
     * Consulta nativa para obtener productos asociados a un proveedor específico.
     *
     * @param idProveedor El identificador único del proveedor.
     * @return Una lista de productos proporcionados por el proveedor indicado.
     */
    @Query(value = "SELECT * FROM productos p WHERE p.id_proveedor = :idProveedor", nativeQuery = true)
    ArrayList<Producto> findByProveedorId(@Param("idProveedor") Long idProveedor);

    /**
     * Consulta nativa para contar el número total de productos registrados en el sistema.
     *
     * @return El número total de productos como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT COUNT(*) FROM productos", nativeQuery = true)
    Long totalProductos();

    /**
     * Consulta nativa para calcular la cantidad total de existencias disponibles de todos los productos.
     *
     * @return La cantidad total de existencias como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(existencias) FROM productos", nativeQuery = true)
    Long existenciasActuales();
}

