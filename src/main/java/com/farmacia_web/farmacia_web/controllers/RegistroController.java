package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.Empleado;
import com.farmacia_web.farmacia_web.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
/**
 * Controlador para manejar el registro de nuevos empleados.
 */
@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra el formulario de registro para empleados.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return La vista del formulario de registro.
     */
    @GetMapping
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "registro"; // La plantilla 'registro.html' debe ser creada en templates
    }

    /**
     * Maneja la lógica de registro para nuevos empleados.
     *
     * @param empleado   Objeto empleado que contiene los datos introducidos en el formulario.
     * @param contrasena Contraseña introducida por el empleado.
     * @param model      Modelo para manejar mensajes de éxito o error.
     * @return Redirección a la página de inicio de sesión o regreso al formulario con mensajes de error.
     */
    @PostMapping
    public String registrarEmpleado(@ModelAttribute("empleado") Empleado empleado,
                                    @RequestParam("contrasena") String contrasena, @RequestParam("confirmContrasena") String confirmContrasena, Model model) {
        // Buscar al empleado en la base de datos por correo electrónico
        Optional<Empleado> empleadoOptional = empleadoRepository.findByEmail(empleado.getEmail());

        if (!empleadoOptional.isPresent()) {
            // Si el correo no está registrado en la tabla empleados
            model.addAttribute("errorMessage", "Correo no registrado como empleado.");
            return "registro";
        }

        Empleado empleadoExistente = empleadoOptional.get();

        // Verificar si el empleado ya ha creado una cuenta
        if (empleadoExistente.getActivo()) {
            model.addAttribute("errorMessage", "Ya has creado tu cuenta.");
            return "registro";
        }
        // Validar la contraseña
        if (!contrasena.equals(confirmContrasena)) {
            model.addAttribute("errorMessage", "Las contraseñas deben coincidir.");
            return "registro";
        }

        // Validar la contraseña
        if (!validarContrasena(contrasena)) {
            model.addAttribute("errorMessage", "La contraseña debe tener al menos 8 caracteres y contener al menos un número.");
            return "registro";
        }

        // Actualizar los datos del empleado
        empleadoExistente.setContrasena(passwordEncoder.encode(contrasena)); // Encriptar la contraseña
        empleadoExistente.setActivo(true); // Cambiar el estado de 'activo' a true
        empleadoRepository.save(empleadoExistente); // Guardar los cambios en la base de datos

        return "redirect:/login?registroExitoso"; // Redirigir a la página de inicio de sesión
    }

    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad.
     *
     * @param contrasena La contraseña a validar.
     * @return {@code true} si la contraseña cumple con los requisitos; de lo contrario, {@code false}.
     */
    private boolean validarContrasena(String contrasena) {
        // Validar que la contraseña tenga al menos 8 caracteres y al menos un número
        return contrasena.length() >= 8 && contrasena.matches(".*\\d.*");
    }

}
