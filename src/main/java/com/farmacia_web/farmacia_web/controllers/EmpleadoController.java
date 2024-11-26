package com.farmacia_web.farmacia_web.controllers;


import com.farmacia_web.farmacia_web.models.Empleado;
import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.services.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar las operaciones relacionadas con los empleados.
 * Este controlador permite listar, crear, editar y eliminar empleados en el sistema.
 */
@Controller
@RequestMapping("/admin/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    /**
     * Muestra una lista de todos los empleados junto con los formularios para crear o editar empleados.
     *
     * @param model           El modelo utilizado para pasar datos a la vista.
     * @param authentication  La autenticación del usuario actual.
     * @return La vista que muestra la lista de empleados y formularios.
     */
    @GetMapping
    public String listarEmpleados(Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("empleados", empleadoService.obtenerEmpleados());
        return "entidades/empleados"; // Nombre de la vista para listar empleados y formularios
    }

    /**
     * Crea un nuevo empleado y lo guarda en la base de datos.
     *
     * @param empleado         El objeto empleado que contiene los datos del nuevo empleado.
     * @param bindingResult    Los resultados de la validación de datos.
     * @param model            El modelo utilizado para manejar mensajes de error o éxito.
     * @param rm               Los atributos de redirección para mensajes flash.
     * @param authentication   La autenticación del usuario actual.
     * @return Redirección a la lista de empleados después de crear un nuevo empleado.
     */
    @PostMapping
    public String crearEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("empleado", empleado);
            model.addAttribute("empleados", empleadoService.obtenerEmpleados());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/empleados";
        } else {
            try {
                empleadoService.crearEmpleado(empleado);
                rm.addFlashAttribute("message", "Empleado creado con éxito.");
            } catch (Exception e) {
                rm.addFlashAttribute("errorMessage", "Error al guardar el empleado: " + e.getMessage());
            }
            return "redirect:/admin/empleados";
        }
    }

    /**
     * Muestra el formulario para editar un empleado existente.
     *
     * @param id               El ID del empleado a editar.
     * @param model            El modelo utilizado para pasar datos a la vista.
     * @param authentication   La autenticación del usuario actual.
     * @return La vista que muestra la lista de empleados y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        Empleado empleado = empleadoService.obtenerPorId(id);
        if (empleado != null) {
            model.addAttribute("empleado", empleado);
        } else {
            model.addAttribute("errorMessage", "Empleado no encontrado.");
        }
        model.addAttribute("empleados", empleadoService.obtenerEmpleados());
        return "entidades/empleados"; // Nombre de la vista para listar empleados y formularios
    }

    /**
     * Actualiza un empleado existente en la base de datos con los datos proporcionados.
     *
     * @param id               El ID del empleado a actualizar.
     * @param empleado         El objeto empleado con los datos actualizados.
     * @param bindingResult    Los resultados de la validación de datos.
     * @param model            El modelo utilizado para manejar mensajes de error o éxito.
     * @param rm               Los atributos de redirección para mensajes flash.
     * @param authentication   La autenticación del usuario actual.
     * @return Redirección a la lista de empleados después de actualizar un empleado.
     */
    @PostMapping("/{id}")
    public String actualizarEmpleado(@PathVariable Long id, @Valid @ModelAttribute("empleado") Empleado empleado, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("empleado", empleado);
            model.addAttribute("empleados", empleadoService.obtenerEmpleados());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/empleados";
        }

        try {
            empleadoService.actualizarEmpleadoPorId(empleado, id);
            rm.addFlashAttribute("message", "Empleado actualizado con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al actualizar el empleado: " + e.getMessage());
        }
        return "redirect:/admin/empleados";
    }

    /**
     * Elimina un empleado específico de la base de datos.
     *
     * @param id El ID del empleado a eliminar.
     * @param rm Los atributos de redirección para mensajes flash.
     * @return Redirección a la lista de empleados después de eliminar un empleado.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes rm) {
        try {
            empleadoService.eliminarEmpleado(id);
            rm.addFlashAttribute("message", "Empleado eliminado exitosamente.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al eliminar el empleado: " + e.getMessage());
        }
        return "redirect:/admin/empleados";
    }
}
