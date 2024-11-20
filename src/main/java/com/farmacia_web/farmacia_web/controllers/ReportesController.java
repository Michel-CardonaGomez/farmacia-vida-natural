package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.repositories.ComprasRepository;
import com.farmacia_web.farmacia_web.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Controlador que permite visualizar el historial de compras y ventas.
 * Está diseñado para ser utilizado por los administradores del sistema.
 */
@Controller
@RequestMapping("/admin/reportes")
public class ReportesController {

@Autowired
private VentaRepository ventaRepository;
    @Autowired
    private ComprasRepository comprasRepository;

    /**
     * Muestra la página de reportes que incluye los historiales de ventas y compras.
     *
     * @param model          Modelo para pasar datos a la vista.
     * @param authentication Objeto que contiene los detalles del empleado autenticado.
     * @return El nombre de la vista que muestra los reportes de ventas y compras.
     */
    @GetMapping
    public String mostrarHistoriales(Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("ventas", ventaRepository.findAll());
        model.addAttribute("compras", comprasRepository.findAll());
        return "entidades/reportes";
    }
}
