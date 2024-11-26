package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Proveedor}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Busca un proveedor por su nombre.
     *
     * @param nombre El nombre del proveedor.
     * @return Un {@link Optional} que contiene el proveedor si se encuentra, o vacío en caso contrario.
     */
    Optional<Proveedor> findByNombre(String nombre);

    /**
     * Consulta nativa para contar el número total de proveedores registrados en el sistema.
     *
     * @return El número total de proveedores como un valor de tipo {@link Long}.
     */
    @Query(value = "SELECT COUNT(*) FROM proveedores", nativeQuery = true)
    Long totalProveedores();
}

