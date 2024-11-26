package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.Categoria;
import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador para gestionar las operaciones relacionadas con los categorías.
 * Permite listar, crear, editar y eliminar categorías en el sistema.
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Muestra una lista de todas las categorías junto con los formularios para crear o editar una categoría.
     *
     * @param model El modelo que se utilizará para pasar datos a la vista.
     * @return La vista que muestra la lista de categorías y formularios.
     */
    @GetMapping
    public String listarCategorias(Model model, Authentication authentication) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/categorias"; // Nombre de la vista para listar categorías y formularios
    }

    /**
     * Crea una nueva categoría y la guarda en la base de datos.
     *
     * @param categoria La nueva categoría a crear.
     * @param model El modelo para manejar mensajes de éxito o error.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de categorías después de crearla.
     */
    @PostMapping
    public String crearCategoria(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
       EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("categoria", categoria);
            model.addAttribute("categorias", categoriaService.obtenerCategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/categorias";
        }

        try {
            categoriaService.crearCategoria(categoria);
            rm.addFlashAttribute("message", "Categoría creada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }

    /**
     * Muestra el formulario para editar una categoría existente.
     *
     * @param id    El ID de la categoría a editar.
     * @param model El modelo que contiene la categoría a editar y la lista de categorías.
     * @return La vista que muestra la lista de categorías y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        if (categoria != null) {
            model.addAttribute("categoria", categoria);
        } else {
            model.addAttribute("errorMessage", "Categoría no encontrada.");
        }
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/categorias";
    }

    /**
     * Actualiza una categoría existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id       El ID de la categoría a actualizar.
     * @param categoria La categoría con los datos actualizados.
     * @param model    El modelo para manejar mensajes de éxito o error.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de categorías después de actualizarla.
     */
    @PostMapping("/{id}")
    public String actualizarCategoria(@PathVariable Long id,@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("categoria", categoria);
            model.addAttribute("categorias", categoriaService.obtenerCategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/categorias";
        }

        try {
            categoriaService.actualizarCategoriaPorId(categoria, id);
            rm.addFlashAttribute("message", "Categoría actualizada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al actualizar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }

    /**
     * Elimina una categoría específica de la base de datos.
     *
     * @param id El ID de la categoría a eliminar.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de categorías después de eliminarla.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes rm) {
        try{
            categoriaService.eliminarCategoria(id);
            rm.addFlashAttribute("message", "Categoría eliminada con éxito.");
        }catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al eliminar la categoria: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
}