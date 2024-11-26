package com.farmacia_web.farmacia_web.repositories;

import com.farmacia_web.farmacia_web.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones relacionadas con la entidad {@link Empleado}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y consultas personalizadas.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /**
     * Consulta nativa para listar todos los empleados con sus principales atributos.
     * Devuelve un listado de empleados, incluyendo información como identificador, identificación,
     * nombre, teléfono, email, rol, fechas de creación y actualización, y estado.
     *
     * @return Una lista de mapas donde cada mapa representa un empleado con sus atributos clave-valor.
     */
    @Query(value = "SELECT id, identificacion, nombre, telefono, email, rol, fecha_creacion, fecha_actualizacion, activo FROM empleados", nativeQuery = true)
    List<Map<String, Object>> listarEmpleados();

    /**
     * Busca un empleado por su dirección de correo electrónico.
     *
     * @param email El correo electrónico del empleado.
     * @return Un {@link Optional} que contiene el empleado si se encuentra, o vacío en caso contrario.
     */
    Optional<Empleado> findByEmail(String email);
}

