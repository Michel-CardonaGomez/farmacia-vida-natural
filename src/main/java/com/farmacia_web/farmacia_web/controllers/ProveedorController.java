package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Proveedor;
import com.farmacia_web.farmacia_web.services.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
     * Controlador para crear un nuevo proveedor y guardarlo en la base de datos.
     *
     * @param proveedor      El objeto Proveedor que contiene los datos del nuevo proveedor a crear.
     * @param bindingResult  Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model          Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm             Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return               Redirección a la lista de proveedores después de crear el nuevo proveedor
     *                       o al formulario de creación en caso de errores.
     * @throws RuntimeException Si ocurre un error inesperado durante la creación del proveedor.
     */
    @PostMapping
    public String crearProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                                 BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {

        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("proveedores", proveedorService.obtenerProveedores());
            model.addAttribute("errorMessage", "Verifique los datos ingresados y corrija los errores.");
            return "entidades/proveedores"; // Retorna a la vista del formulario
        }
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
     * Controlador para actualizar un proveedor existente en la base de datos con los datos proporcionados.
     *
     * @param id              El ID del proveedor que se desea actualizar.
     * @param proveedor       El objeto Proveedor que contiene los datos actualizados.
     * @param bindingResult   Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model           Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm              Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return                Redirección a la lista de proveedores después de actualizar el proveedor
     *                        o al formulario de edición en caso de errores.
     * @throws RuntimeException Si ocurre un error inesperado durante la actualización del proveedor.
     */
    @PostMapping("/{id}")
    public String actualizarProveedor(@PathVariable Long id,@Valid @ModelAttribute("proveedor") Proveedor proveedor, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("proveedores", proveedorService.obtenerProveedores());
            model.addAttribute("errorMessage", "Verifique los datos ingresados y corrija los errores.");
            return "entidades/proveedores"; // Retorna a la vista del formulario
        }
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
