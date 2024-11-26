package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Categoria}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD y consultas personalizadas.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Consulta nativa para obtener la cantidad total vendida por cada categoría.
     * Combina las tablas `detalles_ventas`, `productos`, `subcategorias` y `categorias`
     * para calcular la suma de cantidades vendidas agrupadas por categoría.
     *
     * @return Una lista de objetos donde cada elemento es un array de dos posiciones:
     *         [0] - Nombre de la categoría (String).
     *         [1] - Cantidad total vendida (BigDecimal o Long, dependiendo del resultado).
     */
    @Query(value = "SELECT c.nombre, SUM(d.cantidad) AS cantidad_vendida " +
            "FROM detalles_ventas d " +
            "JOIN productos p ON d.id_producto = p.id " +
            "JOIN subcategorias s ON p.id_subcategoria = s.id " +
            "JOIN categorias c ON s.id_categoria = c.id " +
            "GROUP BY c.nombre",
            nativeQuery = true)
    List<Object[]> cantidadVendidaPorCategoria();
}

