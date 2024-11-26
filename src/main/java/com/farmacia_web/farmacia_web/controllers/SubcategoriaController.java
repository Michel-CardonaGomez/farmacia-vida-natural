package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Subcategoria;
import com.farmacia_web.farmacia_web.services.CategoriaService;
import com.farmacia_web.farmacia_web.services.SubcategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar las operaciones relacionadas con las subcategorías.
 * Permite listar, crear, editar y eliminar subcategorías en el sistema.
 */
@Controller
@RequestMapping("/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaService subcategoriaService;

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Muestra una lista de todas las subcategorías junto con los formularios para crear o editar una subcategoría.
     *
     * @param model          El modelo que se utilizará para pasar datos a la vista.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de subcategorías y formularios.
     */
    @GetMapping
    public String listarSubcategorias(Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        model.addAttribute("subcategoria", new Subcategoria());
        model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
        return "entidades/subcategorias"; // Nombre de la vista para listar subcategorías y formularios
    }

    /**
     * Crea una nueva subcategoría y la guarda en la base de datos.
     *
     * @param subcategoria La nueva subcategoría a crear.
     * @param bindingResult  Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model          Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm             Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return Redirección a la lista de subcategorías después de crearla.
     */
    @PostMapping
    public String crearSubcategoria(@Valid @ModelAttribute("subcategoria") Subcategoria subcategoria, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("categorias", categoriaService.obtenerCategorias());
            model.addAttribute("subcategoria", subcategoria);
            model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados y corrija los errores.");
            return "entidades/subcategorias"; // Retorna a la vista del formulario
        }
        try {
            subcategoriaService.crearSubcategoria(subcategoria);
            rm.addFlashAttribute("message", "Subcategoría creada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/subcategorias";
    }

    /**
     * Muestra el formulario para editar una subcategoría existente.
     *
     * @param id             El ID de la subcategoría a editar.
     * @param model          El modelo que contiene la subcategoría a editar y la lista de subcategorías.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de subcategorías y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Subcategoria subcategoria = subcategoriaService.obtenerPorId(id);
        if (subcategoria != null) {
            model.addAttribute("subcategoria", subcategoria);
        } else {
            model.addAttribute("errorMessage", "Subcategoría no encontrada.");
        }
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
        return "entidades/subcategorias";
    }

    /**
     * Actualiza una subcategoría existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id           El ID de la subcategoría a actualizar.
     * @param subcategoria La subcategoría con los datos actualizados.
     * @param bindingResult  Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model          Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm             Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return Redirección a la lista de subcategorías después de actualizarla.
     */
    @PostMapping("/{id}")
    public String actualizarSubcategoria(@PathVariable Long id,@Valid @ModelAttribute("subcategoria") Subcategoria subcategoria, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("categorias", categoriaService.obtenerCategorias());
            model.addAttribute("subcategoria", subcategoria);
            model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados y corrija los errores.");
            return "entidades/subcategorias"; // Retorna a la vista del formulario
        }
        try {
            subcategoriaService.actualizarSubcategoriaPorId(subcategoria, id);
            rm.addFlashAttribute("message", "Subcategoría actualizada con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/subcategorias";
    }

    /**
     * Elimina una subcategoría específica de la base de datos.
     *
     * @param id El ID de la subcategoría a eliminar.
     * @param rm Objeto para agregar atributos flash a la redirección.
     * @return Redirección a la lista de subcategorías después de eliminarla.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarSubcategoria(@PathVariable Long id, RedirectAttributes rm) {
        try {
            subcategoriaService.eliminarSubcategoria(id);
            rm.addFlashAttribute("message", "Subcategoría eliminada con éxito.");
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/subcategorias"; // Redirige a la lista de subcategorías
    }
}

