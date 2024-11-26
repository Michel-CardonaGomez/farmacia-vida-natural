package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.Empleado;
import com.farmacia_web.farmacia_web.models.EmpleadoDTO;
import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.repositories.EmpleadoRepository;
import com.farmacia_web.farmacia_web.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar las operaciones relacionadas con el perfil del empleado autenticado.
 * Permite visualizar y editar los datos personales del empleado.
 */
@Controller
@RequestMapping("/perfil")
public class perfilController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    /**
     * Muestra la página de perfil del empleado autenticado.
     *
     * @param model          Modelo para pasar datos a la vista.
     * @param authentication Objeto de autenticación que contiene los detalles del empleado autenticado.
     * @return El nombre de la vista "entidades/perfil".
     */
    @GetMapping
    public String perfil(Model model, Authentication authentication) {
        EmpleadoDetails empleadoAuth = (EmpleadoDetails) authentication.getPrincipal();
        Empleado empleado = empleadoRepository.findByEmail(empleadoAuth.getUsername()).orElse(null);
        model.addAttribute("empleado", empleado);
        model.addAttribute("empleadoAuth", empleadoAuth);
        model.addAttribute("empleadoDto", new EmpleadoDTO());
        return "entidades/perfil";
    }

    /**
     * Permite editar los datos del empleado autenticado, incluyendo su contraseña y teléfono.
     *
     * @param id             ID del empleado a editar.
     * @param empleadoDTO    DTO que contiene los datos actualizados del empleado.
     * @param authentication Objeto de autenticación que contiene los detalles del empleado autenticado.
     * @param rm             Objeto para enviar atributos flash entre redirecciones.
     * @return Redirección a la página de perfil después de la edición.
     */
    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @ModelAttribute("empleadoDto") EmpleadoDTO empleadoDTO,
                         Authentication authentication, RedirectAttributes rm) {
        EmpleadoDetails empleadoAuth = (EmpleadoDetails) authentication.getPrincipal();
        Empleado empleado = empleadoRepository.findByEmail(empleadoAuth.getUsername()).orElse(null);

        // Verificar si la contraseña actual es correcta
        if (!passwordEncoder.matches(empleadoDTO.getContrasenaActual(), empleadoAuth.getPassword())) {
            rm.addFlashAttribute("errorMessage", "La contraseña actual es incorrecta");
        } else {
            try {
                // Actualizar datos del empleado
                empleado.setTelefono(empleadoDTO.getTelefono());
                empleado.setContrasena(passwordEncoder.encode(empleadoDTO.getContrasenaNueva()));
                empleadoService.actualizarEmpleadoPorId(empleado, id);
                rm.addFlashAttribute("message", "El empleado se ha modificado correctamente");
            } catch (RuntimeException e) {
                rm.addFlashAttribute("errorMessage", "Error al modificar los datos del empleado: " + e.getMessage());
            }
        }
        return "redirect:/perfil";
    }
}
