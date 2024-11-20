package com.farmacia_web.farmacia_web.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador principal de la aplicación que maneja la redirección inicial y la vista de inicio de sesión.
 */
@Controller
public class HomeController {

    /**
     * Redirige a la página principal de ventas.
     *
     * @return Redirección a la URL "/facturas/ventas".
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/facturas/ventas";
    }

    /**
     * Maneja las solicitudes de inicio de sesión.
     * Si hay un error en el inicio de sesión, muestra un mensaje de error.
     *
     * @param error   Parámetro opcional que indica si hubo un error de inicio de sesión.
     * @param session Objeto HttpSession para gestionar los atributos de sesión.
     * @param model   Modelo para pasar datos a la vista.
     * @return El nombre de la vista "login".
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        HttpSession session, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email o contraseña inválidas");
            session.removeAttribute("errorMessage"); // Limpiar el mensaje después de mostrarlo
        }
        return "login";
    }

}
