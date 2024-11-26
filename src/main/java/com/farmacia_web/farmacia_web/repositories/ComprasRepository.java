package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Compra;
import com.farmacia_web.farmacia_web.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Compra}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD y consultas personalizadas.
 */
@Repository
public interface ComprasRepository extends JpaRepository<Compra, Long> {

    /**
     * Busca una compra asociada a una factura específica.
     *
     * @param factura La factura relacionada con la compra.
     * @return Un {@link Optional} que contiene la compra si se encuentra, o vacío en caso contrario.
     */
    Optional<Compra> findByFactura(Factura factura);

    /**
     * Consulta nativa para calcular el importe total pagado en todas las compras registradas.
     *
     * @return El importe total pagado como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT SUM(total) FROM compras", nativeQuery = true)
    Long importePagado();
}

