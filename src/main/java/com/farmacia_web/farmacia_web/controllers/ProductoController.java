package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.models.Producto;
import com.farmacia_web.farmacia_web.services.MarcaService;
import com.farmacia_web.farmacia_web.services.ProductoService;
import com.farmacia_web.farmacia_web.services.ProveedorService;
import com.farmacia_web.farmacia_web.services.SubcategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.BindingResult;
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
     * Controlador para crear un nuevo producto y guardarlo en la base de datos.
     *
     * @param producto        El objeto Producto que contiene los datos del nuevo producto a crear.
     * @param bindingResult   Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model           Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm              Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return                Redirección a la lista de productos después de crear el nuevo producto
     *                        o al formulario de creación en caso de errores.
     * @throws RuntimeException Si ocurre un error inesperado durante la creación del producto.
     */
    @PostMapping
    public String crearProducto(@Valid @ModelAttribute("producto") Producto producto, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("producto", producto);
            model.addAttribute("productos", productoService.obtenerProductos());
            model.addAttribute("marcas", marcaService.obtenerMarcas());
            model.addAttribute("proveedores", proveedorService.obtenerProveedores());
            model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/productos";
        }
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
     * Controlador para actualizar un producto existente en la base de datos con los datos proporcionados.
     *
     * @param id              El ID del producto que se desea actualizar.
     * @param producto        El objeto Producto que contiene los datos actualizados.
     * @param bindingResult   Objeto que captura los resultados de la validación de los datos ingresados.
     * @param model           Modelo utilizado para pasar datos a la vista en caso de errores.
     * @param rm              Objeto RedirectAttributes para agregar mensajes flash después de redirigir.
     * @param authentication  Objeto Authentication que contiene información del usuario autenticado.
     * @return                Redirección a la lista de productos después de actualizar el producto
     *                        o el formulario de edición en caso de errores.
     * @throws RuntimeException Si ocurre un error inesperado durante la actualización del producto.
     */
    @PostMapping("/{id}")
    public String actualizarProducto(@PathVariable Long id, @Valid @ModelAttribute("producto") Producto producto, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("producto", producto);
            model.addAttribute("productos", productoService.obtenerProductos());
            model.addAttribute("proveedores", proveedorService.obtenerProveedores());
            model.addAttribute("subcategorias", subcategoriaService.obtenerSubcategorias());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/productos";
        }

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
