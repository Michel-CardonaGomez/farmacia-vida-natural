package com.farmacia_web.farmacia_web.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Clase que implementa las interfaces {@link UserDetails} y {@link OAuth2User}.
 * Proporciona los detalles de autenticación y autorización de un empleado en el sistema.
 * Esta clase actúa como un adaptador entre la entidad {@link Empleado} y los requisitos de Spring Security.
 */
public class EmpleadoDetails implements UserDetails, OAuth2User {

    /**
     * Instancia de la entidad {@link Empleado} que contiene los datos del empleado autenticado.
     */
    private Empleado empleado;

    /**
     * Mapa de atributos adicionales (opcional) para autenticación OAuth2.
     */
    private Map<String, Object> attributes;

    /**
     * Constructor para empleados autenticados sin atributos OAuth2.
     *
     * @param empleado La entidad {@link Empleado} que representa al empleado autenticado.
     */
    public EmpleadoDetails(Empleado empleado) {
        this.empleado = empleado;
        this.attributes = null; // Sin atributos OAuth2
    }

    /**
     * Constructor para empleados autenticados con atributos OAuth2.
     *
     * @param empleado   La entidad {@link Empleado} que representa al empleado autenticado.
     * @param attributes Mapa de atributos adicionales relacionados con OAuth2.
     */
    public EmpleadoDetails(Empleado empleado, Map<String, Object> attributes) {
        this.empleado = empleado;
        this.attributes = attributes;
    }

    /**
     * Devuelve el nombre del empleado autenticado.
     *
     * @return El nombre del empleado.
     */
    public String getName() {
        return empleado.getNombre();
    }

    /**
     * Devuelve el rol del empleado autenticado.
     *
     * @return El rol del empleado.
     */
    public String getRol() {
        return empleado.getRol();
    }

    /**
     * Devuelve las autoridades del empleado autenticado.
     * Cada empleado tiene un rol asignado que se mapea como una autoridad.
     *
     * @return Una colección de {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + empleado.getRol().toUpperCase();
        return List.of(new SimpleGrantedAuthority(role));
    }

    /**
     * Devuelve la contraseña del empleado para autenticación.
     *
     * @return La contraseña del empleado.
     */
    @Override
    public String getPassword() {
        return empleado.getContrasena();
    }

    /**
     * Devuelve los atributos adicionales del usuario autenticado en caso de OAuth2.
     *
     * @return Un mapa de atributos o {@code null} si no se utiliza OAuth2.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * Devuelve el nombre de usuario del empleado.
     * En este caso, el nombre de usuario es el correo electrónico.
     *
     * @return El correo electrónico del empleado.
     */
    @Override
    public String getUsername() {
        return empleado.getEmail();
    }

    /**
     * Indica si la cuenta del empleado no ha expirado.
     *
     * @return {@code true}, ya que no se implementa lógica de expiración de cuenta.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del empleado no está bloqueada.
     *
     * @return {@code true}, ya que no se implementa lógica de bloqueo de cuenta.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del empleado no han expirado.
     *
     * @return {@code true}, ya que no se implementa lógica de expiración de credenciales.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el empleado está habilitado para autenticarse.
     * Solo los empleados con el estado "activo" pueden iniciar sesión.
     *
     * @return {@code true} si el empleado está activo, de lo contrario {@code false}.
     */
    @Override
    public boolean isEnabled() {
        return empleado.getActivo();
    }
}

