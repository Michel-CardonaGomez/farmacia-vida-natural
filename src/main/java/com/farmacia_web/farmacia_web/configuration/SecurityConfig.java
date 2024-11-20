package com.farmacia_web.farmacia_web.configuration;

import com.farmacia_web.farmacia_web.models.Empleado;
import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.repositories.EmpleadoRepository;
import com.farmacia_web.farmacia_web.services.EmpleadoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Configuración de seguridad para la aplicación.
 * Proporciona autenticación, autorización, y soporte para inicio de sesión con OAuth2.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    EmpleadoDetailsService empleadoDetailsService;

    @Autowired
    EmpleadoRepository empleadoRepository;

    /**
     * Configura el codificador de contraseñas utilizando BCrypt.
     *
     * @return Un bean de tipo {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el gestor de autenticación.
     *
     * @param authenticationConfiguration Configuración de autenticación proporcionada por Spring Security.
     * @return Un bean de tipo {@link AuthenticationManager}.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura el proveedor de autenticación utilizando detalles de usuario personalizados
     * y codificación de contraseñas.
     *
     * @return Un bean de tipo {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(empleadoDetailsService);
        return provider;
    }

    /**
     * Configura el servicio de usuario para autenticación con OAuth2.
     * Verifica si el email del usuario autenticado está registrado en la base de datos.
     *
     * @return Un bean de tipo {@link OAuth2UserService}.
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            // Cargar el usuario desde el proveedor de OAuth2
            OAuth2User oauth2User = delegate.loadUser(request);

            // Obtener el email del usuario autenticado
            String email = oauth2User.getAttribute("email");

            // Verificar si el email está registrado en la base de datos
            Empleado empleado = empleadoRepository.findByEmail(email)
                    .orElseThrow(() -> new OAuth2AuthenticationException("Usuario no autorizado"));

            // Crear y retornar el objeto EmpleadoDetails con los detalles del empleado
            return new EmpleadoDetails(empleado, oauth2User.getAttributes());
        };
    }

    /**
     * Configura el filtro de seguridad para manejar las solicitudes HTTP.
     * Define las reglas de autorización, login, logout, y soporte para OAuth2.
     *
     * @param http El objeto {@link HttpSecurity} para configurar la seguridad HTTP.
     * @return El filtro de seguridad configurado como un bean de tipo {@link SecurityFilterChain}.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/registro", "/login", "/oauth2/**").permitAll() // Permitir acceso a las páginas de registro y login
                .requestMatchers("/css/**", "/js/**", "/imagenes/**", "/archivos/**", "/static/archivos**").permitAll() // Permitir acceso a los recursos estáticos
                .requestMatchers("/admin/**").hasAnyRole("ADMINISTRADOR")
                .requestMatchers("/**").hasAnyRole("ADMINISTRADOR", "EMPLEADO")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("contrasena")
                .defaultSuccessUrl("/", true) // Redirigir tras login exitoso
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Cerrar sesión
                .logoutSuccessUrl("/login?logout") // Redirigir a la página de inicio después de cerrar sesión
                .invalidateHttpSession(true) // Invalidar la sesión
                .deleteCookies("JSESSIONID") // Eliminar las cookies
                .permitAll()
                .and()
                .oauth2Login() // Habilitar login con OAuth2
                .failureUrl("/login?error=true")  // Redirigir también para el login con OAuth2
                .loginPage("/login") // La página de login personalizada también aplica para OAuth2
                .defaultSuccessUrl("/", true) // Redirigir tras un login exitoso con OAuth2
                .userInfoEndpoint()
                .userService(oauth2UserService());

        return http.build();
    }
}
