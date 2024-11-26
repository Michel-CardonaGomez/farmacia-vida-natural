package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.Cliente;
import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controlador para gestionar las operaciones relacionadas con los clientes.
 * Permite listar, crear, editar y eliminar clientes en el sistema.
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Muestra una lista de todos los clientes junto con los formularios para crear o editar un cliente.
     *
     * @param model El modelo que se utilizará para pasar datos a la vista.
     * @return La vista que muestra la lista de clientes y formularios.
     */
    @GetMapping
    public String listarClientes(Model model, Authentication authentication) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clienteService.obtenerClientes());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/clientes"; // Nombre de la vista para listar clientes y formularios
    }

    /**
     * Crea un nuevo cliente y lo guarda en la base de datos.
     *
     * @param cliente El nuevo cliente a crear.
     * @param bindingResult Resultado de la validación del formulario.
     * @param model El modelo para manejar datos de la vista.
     * @param rm Atributos para mensajes flash.
     * @param authentication La autenticación del empleado actual.
     * @return Redirección a la lista de clientes después de crearlo, o la vista de formulario si hay errores.
     */
    @PostMapping
    public String crearCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("cliente", cliente);
            model.addAttribute("clientes", clienteService.obtenerClientes());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/clientes";
        }

        try {
            clienteService.crearCliente(cliente);
            rm.addFlashAttribute("message", "Cliente creado con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al guardar el cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    /**
     * Muestra el formulario para editar un cliente existente.
     *
     * @param id    El ID del cliente a editar.
     * @param model El modelo que contiene el cliente a editar y la lista de clientes.
     * @return La vista que muestra la lista de clientes y el formulario de edición.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication authentication) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
        } else {
            model.addAttribute("errorMessage", "Cliente no encontrado.");
        }
        model.addAttribute("clientes", clienteService.obtenerClientes());
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        return "entidades/clientes";
    }

    /**
     * Actualiza un cliente existente en la base de datos con los nuevos datos proporcionados.
     *
     * @param id El ID del cliente a actualizar.
     * @param cliente El cliente con los datos actualizados.
     * @param bindingResult Resultado de la validación del formulario.
     * @param model El modelo para manejar datos de la vista.
     * @param rm Atributos para mensajes flash.
     * @param authentication La autenticación del empleado actual.
     * @return Redirección a la lista de clientes después de actualizarlo, o la vista de formulario si hay errores.
     */
    @PostMapping("/{id}")
    public String actualizarCliente(@PathVariable Long id, @Valid @ModelAttribute("cliente") Cliente cliente, BindingResult bindingResult, Model model, RedirectAttributes rm, Authentication authentication) {
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleadoAuth", empleadoDetails);
            model.addAttribute("cliente", cliente);
            model.addAttribute("clientes", clienteService.obtenerClientes());
            model.addAttribute("errorMessage", "Verifique los datos ingresados");
            return "entidades/clientes";
        }

        try {
            clienteService.actualizarClientePorId(cliente, id);
            rm.addFlashAttribute("message", "Cliente actualizado con éxito.");
        } catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al actualizar el cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    /**
     * Elimina un cliente específico de la base de datos.
     *
     * @param id El ID del cliente a eliminar.
     * @return Redirección a la lista de clientes después de eliminarlo.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes rm) {
        try {
            clienteService.eliminarCliente(id);
            return "redirect:/clientes";
        }catch (Exception e) {
            rm.addFlashAttribute("errorMessage", "Error al eliminar el cliente: " + e.getMessage());
            return "redirect:/clientes";
        }

    }
}

