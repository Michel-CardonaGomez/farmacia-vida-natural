package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Marca;
import com.farmacia_web.farmacia_web.services.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar las operaciones relacionadas con las marcas.
 * Permite listar, crear, editar y eliminar marcas en el sistema.
 */
@Controller
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    /**
     * Muestra una lista de todas las marcas junto con los formularios para crear o editar una marca.
     *
     * @param model El modelo que se utilizará para pasar datos a la vista.
     * @return La vista que muestra la lista de marcas y formularios.
     */
    @GetMapping
    public String listarMarcas(Model model, Authentication authentication) {
        model.addAttribute("marca", new Marca());
        model.addAttribute("marcas", marcaService.obtenerMarcas());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/marcas"; // Nombre de la vista para listar marcas y formularios
    }

    /**
     * Crea una nueva marca y la guarda en la base de datos.
     *
     * @param marca El objeto que representa la nueva marca a crear.
     * @param bindingResult Resultado de la validación del formulario de la marca.
     * @param model El modelo utilizado para pasar datos entre el controlador y la vista.
     * @param rm Atributos para mensajes flash que se mostrarán después de la operación.
     * @param authentication La autenticación del empleado actual, utilizada para obtener detalles del usuario autenticado.
     * @return Redirección a la lista de marcas después de crearla, o la vista del formulario si hay errores de validación.
     */
    @PostMapping
    public String crearMarca(@Valid @ModelAttribute("marca") Marca marca, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("marca", marca);
            model.addAttribute("marcas", marcaService.obtenerMarcas());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/marcas";
        }

        try {
            marcaService.crearMarca(marca);
            rm.addFlashAttribute("message", "Marca creada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al guardar la marca: " + e.getMessage());
        }
        return "redirect:/marcas";
    }

    /**
     * Muestra el formulario para editar una marca existente.
     *
     * @param id    El ID de la marca a editar.
     * @param model El modelo que contiene la marca a editar y la lista de marcas.
     * @return La vista que muestra la lista de marcas y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Marca marca = marcaService.obtenerPorId(id);
        if (marca != null) {
            model.addAttribute("marca", marca);
        } else {
            model.addAttribute("errorMessage", "Marca no encontrada.");
        }
        model.addAttribute("marcas", marcaService.obtenerMarcas());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/marcas";
    }

    /**
     * Actualiza una marca existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id El ID de la marca a actualizar.
     * @param marca El objeto que contiene los datos actualizados de la marca.
     * @param bindingResult Resultado de la validación del formulario de la marca.
     * @param model El modelo utilizado para pasar datos entre el controlador y la vista.
     * @param rm Atributos para mensajes flash que se mostrarán después de la operación.
     * @param authentication La autenticación del empleado actual, utilizada para obtener detalles del usuario autenticado.
     * @return Redirección a la lista de marcas después de actualizarla, o la vista del formulario si hay errores de validación.
     */
    @PostMapping("/{id}")
    public String actualizarMarca(@PathVariable Long id, @Valid @ModelAttribute("marca") Marca marca, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("marca", marca);
            model.addAttribute("marcas", marcaService.obtenerMarcas());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/marcas";
        }
        try {
            marcaService.actualizarMarcaPorId(marca, id);
            rm.addFlashAttribute("message", "Marca actualizada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al actualizar la marca: " + e.getMessage());
        }
        return "redirect:/marcas";
    }

    /**
     * Elimina una marca específica de la base de datos.
     *
     * @param id El ID de la marca a eliminar.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de marcas después de eliminarla.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id, RedirectAttributes rm) {
        try{
            marcaService.eliminarMarca(id);
            rm.addFlashAttribute("message", "Marca eliminada.");
        }catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al eliminar la marca: " + e.getMessage());
        }
        return "redirect:/marcas";
    }
}



