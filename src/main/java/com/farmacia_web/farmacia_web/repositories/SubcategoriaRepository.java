package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Subcategoria}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos.
 */
@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {
}

