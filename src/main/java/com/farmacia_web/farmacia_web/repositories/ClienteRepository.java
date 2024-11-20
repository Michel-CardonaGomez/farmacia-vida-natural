package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Cliente}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD y consultas personalizadas.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su número de identificación.
     *
     * @param identificacion El número de identificación del cliente.
     * @return Un {@link Optional} que contiene el cliente si se encuentra, o vacío en caso contrario.
     */
    Optional<Cliente> findByIdentificacion(Long identificacion);

    /**
     * Consulta nativa para contar el número total de clientes registrados en el sistema.
     *
     * @return La cantidad total de clientes como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT COUNT(*) FROM clientes", nativeQuery = true)
    Long totalClientes();
}
