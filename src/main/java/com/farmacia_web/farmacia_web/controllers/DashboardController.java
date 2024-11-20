package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.EmpleadoDetails;
import com.farmacia_web.farmacia_web.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que gestiona la visualización del panel de control administrativo.
 * Proporciona estadísticas generales y datos gráficos sobre clientes, proveedores, productos, ventas y compras.
 */
@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetallesVentaRepository detallesVentaRepository;

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Muestra el panel de control administrativo con estadísticas y datos gráficos.
     *
     * @param model          Modelo para pasar datos a la vista.
     * @param authentication Objeto de autenticación que contiene los detalles del empleado autenticado.
     * @return El nombre de la vista que representa el panel de control.
     */
    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        // Obtener detalles del empleado autenticado
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);

        // Agregar estadísticas generales al modelo
        model.addAttribute("totalClientes", clienteRepository.totalClientes() != null ? clienteRepository.totalClientes() : 0);
        model.addAttribute("totalProveedores", proveedorRepository.totalProveedores() != null ? proveedorRepository.totalProveedores() : 0);
        model.addAttribute("totalProductos", productoRepository.totalProductos() != null ? productoRepository.totalProductos() : 0);
        model.addAttribute("totalVentas", ventaRepository.totalVentas() != null ? ventaRepository.totalVentas() : 0);
        model.addAttribute("existenciasVendidas", detallesVentaRepository.existenciasvendidas() != null ? detallesVentaRepository.existenciasvendidas() : 0);
        model.addAttribute("existenciasActuales", productoRepository.existenciasActuales() != null ? productoRepository.existenciasActuales() : 0);
        model.addAttribute("existenciasTotales",
                (productoRepository.existenciasActuales() != null ? productoRepository.existenciasActuales() : 0) +
                        (detallesVentaRepository.existenciasvendidas() != null ? detallesVentaRepository.existenciasvendidas() : 0)
        );
        model.addAttribute("importeVendido", ventaRepository.importeVendido() != null ? ventaRepository.importeVendido() : 0);
        model.addAttribute("importePagado", comprasRepository.importePagado() != null ? comprasRepository.importePagado() : 0);
        model.addAttribute("beneficioBruto", ventaRepository.obtenerBeneficioBruto() != null ? ventaRepository.obtenerBeneficioBruto() : 0);
        model.addAttribute("impuestos", ventaRepository.obtenerTotalIva() != null ? ventaRepository.obtenerTotalIva() : 0);
        model.addAttribute("beneficioNeto", ventaRepository.obtenerBeneficioNeto() != null ? ventaRepository.obtenerBeneficioNeto() : 0);


        // Datos para el gráfico 1: Cantidad vendida por categoría
        List<Object[]> chart1 = categoriaRepository.cantidadVendidaPorCategoria();
        List<String> categorias = new ArrayList<>();
        List<Long> cantidades = new ArrayList<>();
        for (Object[] fila : chart1) {
            categorias.add((String) fila[0]);
            cantidades.add(((BigDecimal) fila[1]).longValue());
        }

        // Datos para el gráfico 2: Empleados con más ventas
        List<Object[]> chart2 = ventaRepository.findEmpleadosConMasVentas();
        List<String> empleado = new ArrayList<>();
        List<Long> numeroVentas = new ArrayList<>();
        for (Object[] fila : chart2) {
            empleado.add((String) fila[0]);
            numeroVentas.add(((Long) fila[1]));
        }

        // Datos para el gráfico 3: Productos más vendidos
        List<Object[]> chart3 = detallesVentaRepository.topProductos();
        List<String> productos = new ArrayList<>();
        List<Long> cantidadVentas = new ArrayList<>();
        for (Object[] fila : chart3) {
            productos.add((String) fila[0]);
            cantidadVentas.add(((BigDecimal) fila[1]).longValue());
        }

        // Datos para el gráfico 4: Métodos de pago más utilizados
        List<Object[]> chart4 = ventaRepository.contarMetodosPago();
        List<String> metodosPago = new ArrayList<>();
        List<Long> cantidadPagos = new ArrayList<>();
        for (Object[] fila : chart4) {
            metodosPago.add((String) fila[0]);
            cantidadPagos.add(((Long) fila[1]));
        }

        // Datos para el gráfico 5: Proveedores con más ventas
        List<Object[]> chart5 = detallesVentaRepository.topProveedores();
        List<String> proveedores = new ArrayList<>();
        List<Long> numVentas = new ArrayList<>();
        for (Object[] fila : chart5) {
            proveedores.add((String) fila[0]);
            numVentas.add(((BigDecimal) fila[1]).longValue());
        }

        // Agregar datos gráficos al modelo
        model.addAttribute("categorias", categorias);
        model.addAttribute("cantidades", cantidades);
        model.addAttribute("empleados", empleado);
        model.addAttribute("numeroVentas", numeroVentas);
        model.addAttribute("productos", productos);
        model.addAttribute("cantidadVentas", cantidadVentas);
        model.addAttribute("metodosPago", metodosPago);
        model.addAttribute("cantidadPagos", cantidadPagos);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("numVentas", numVentas);

        return "entidades/dashboard";
    }
}



