package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Proveedor;
import com.farmacia_web.farmacia_web.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Controlador para gestionar las operaciones relacionadas con los proveedores.
 * Permite listar, crear, editar y eliminar proveedores en el sistema.
 */
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Muestra una lista de todos los proveedores junto con los formularios para crear o editar un proveedor.
     *
     * @param model          El modelo que se utilizará para pasar datos a la vista.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de proveedores y formularios.
     */
    @GetMapping
    public String listarProveedores(Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("proveedores", proveedorService.obtenerProveedores());
        return "entidades/proveedores"; // Nombre de la vista para listar proveedores y formularios
    }

    /**
     * Crea un nuevo proveedor y lo guarda en la base de datos.
     *
     * @param proveedor El nuevo proveedor a crear.
     * @param model     El modelo para manejar mensajes de éxito o error.
     * @param rm        Objeto para agregar atributos flash a la redirección.
     * @return Redirección a la lista de proveedores después de crearlo.
     */
    @PostMapping
    public String crearProveedor(@ModelAttribute("proveedor") Proveedor proveedor, Model model, RedirectAttributes rm) {
        try {
            proveedorService.crearProveedor(proveedor);
            rm.addFlashAttribute("message", "Proveedor creado con éxito.");
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/proveedores";
    }

    /**
     * Muestra el formulario para editar un proveedor existente.
     *
     * @param id             El ID del proveedor a editar.
     * @param model          El modelo que contiene el proveedor a editar y la lista de proveedores.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de proveedores y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Proveedor proveedor = proveedorService.obtenerPorId(id);
        if (proveedor != null) {
            model.addAttribute("proveedor", proveedor);
        } else {
            model.addAttribute("errorMessage", "Proveedor no encontrado.");
        }
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("proveedores", proveedorService.obtenerProveedores());
        return "entidades/proveedores";
    }

    /**
     * Actualiza un proveedor existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id        El ID del proveedor a actualizar.
     * @param proveedor El proveedor con los datos actualizados.
     * @param model     El modelo para manejar mensajes de éxito o error.
     * @param rm        Objeto para agregar atributos flash a la redirección.
     * @return Redirección a la lista de proveedores después de actualizarlo.
     */
    @PostMapping("/{id}")
    public String actualizarProveedor(@PathVariable Long id, @ModelAttribute("proveedor") Proveedor proveedor, Model model, RedirectAttributes rm) {
        try {
            proveedorService.actualizarProveedorPorId(proveedor, id);
            rm.addFlashAttribute("message", "Proveedor actualizado con éxito.");
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/proveedores";
    }

    /**
     * Elimina un proveedor específico de la base de datos.
     *
     * @param id El ID del proveedor a eliminar.
     * @param rm Objeto para agregar atributos flash a la redirección.
     * @return Redirección a la lista de proveedores después de eliminarlo.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes rm) {
        try {
            proveedorService.eliminarProveedor(id);
            rm.addFlashAttribute("message", "Proveedor eliminado con éxito.");
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/proveedores";
    }
}
