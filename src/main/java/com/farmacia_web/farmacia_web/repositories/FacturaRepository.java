package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Factura}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Consulta nativa para obtener el número serial más alto de una factura
     * creada en una fecha específica y de un tipo determinado.
     *
     * @param fecha La fecha de creación de las facturas en formato "yyyy-MM-dd".
     * @param tipo  El tipo de factura (por ejemplo, "venta" o "compra").
     * @return El número serial más alto de las facturas que coinciden con los criterios, como una cadena.
     */
    @Query(value = "SELECT MAX(f.serial) FROM facturas f WHERE f.fecha_creacion LIKE CONCAT(:fecha, '%') AND f.tipo = :tipo", nativeQuery = true)
    String findUltimoNumeroFacturaPorFecha(@Param("fecha") String fecha, @Param("tipo") String tipo);
}

