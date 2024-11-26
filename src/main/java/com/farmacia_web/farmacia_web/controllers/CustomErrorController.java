package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model, Authentication authentication) {
        int statusCode = response.getStatus();
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);

        String errorMessage;
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            errorMessage = "Página no encontrada";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            errorMessage = "Error interno del servidor";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            errorMessage = "Acceso denegado";
        } else {
            errorMessage = "Error desconocido";
        }

        model.addAttribute("message", errorMessage);
        model.addAttribute("empleadoAuth", empleadoDetails);

        return "error";
    }
}