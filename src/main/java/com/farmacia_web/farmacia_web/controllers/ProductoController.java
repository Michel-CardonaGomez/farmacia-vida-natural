package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Producto;
import com.farmacia_web.farmacia_web.services.MarcaService;
import com.farmacia_web.farmacia_web.services.ProductoService;
import com.farmacia_web.farmacia_web.services.ProveedorService;
import com.farmacia_web.farmacia_web.services.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador para gestionar las operaciones relacionadas con los productos.
 * Permite listar, crear, editar y eliminar productos en el sistema.
 */
@Controller
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión de productos, incluyendo creación, edición y eliminación")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private SubcategoriaService subcategoriaService;

    /**
     * Muestra una lista de todos los productos junto con los formularios para crear o editar un producto.
     *
     * @param model          El modelo que se utilizará para pasar datos a la vista.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de productos y formularios.
     */
    @GetMapping
    public String listarProductos(Model model, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("productos", productoService.obtenerProductos());
        model.addAttribute("marcas", marcaService.obtenerMarcas());
        model.addAttribute("proveedores", proveedorService.obtenerProveedores());
        model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
        model.addAttribute("producto", new Producto());
        return "entidades/productos"; // Nombre de la vista para listar productos y formularios
    }

    /**
     * Crea un nuevo producto y lo guarda en la base de datos.
     *
     * @param producto El nuevo producto a crear.
     * @param model El modelo para manejar mensajes de éxito o error.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de productos después de crearla.
     */
    @PostMapping
    public String crearProducto(@ModelAttribute("producto") Producto producto, Model model, RedirectAttributes rm) {
        try {
            productoService.crearProducto(producto);
            rm.addFlashAttribute("message", "Producto creado con éxito.");
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Muestra el formulario para editar un producto existente.
     *
     * @param id    El ID del producto a editar.
     * @param model El modelo que contiene el producto a editar y las listas necesarias.
     * @param authentication Objeto que contiene la información del usuario autenticado.
     * @return La vista que muestra la lista de productos y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            model.addAttribute("producto", producto);
        } else {
            model.addAttribute("errorMessage", "Producto no encontrado.");
        }
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("marcas", marcaService.obtenerMarcas());
        model.addAttribute("proveedores", proveedorService.obtenerProveedores());
        model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
        model.addAttribute("productos", productoService.obtenerProductos());
        return "entidades/productos"; // Nombre de la vista para listar productos y formularios
    }

    /**
     * Actualiza un producto existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id    El ID del producto a actualizar.
     * @param producto El producto con los datos actualizados.
     * @param model El modelo para manejar mensajes de éxito o error.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de productos después de actualizarlo.
     */
    @PostMapping("/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute("producto") Producto producto, Model model, RedirectAttributes rm) {
        try {
            productoService.actualizarProducto(producto, id);
            rm.addFlashAttribute("message", "Producto actualizado con éxito.");

        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }

        return "redirect:/productos";
    }

    /**
     * Elimina un producto específico de la base de datos.
     *
     * @param id El ID del producto a eliminar.
     * @param rm objeto para agregar atributos flash a la redirección
     * @return Redirección a la lista de productos después de eliminarlo.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes rm) {
        try{
            productoService.eliminarProducto(id);
            rm.addFlashAttribute("message", "Producto eliminado con éxito.");
        }catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage",  e.getMessage());
        }
        return "redirect:/productos"; // Redirige a la lista de productos
    }
}
