package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.DetallesCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link DetallesCompra}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface DetallesCompraRepository extends JpaRepository<DetallesCompra, Long> {

}

