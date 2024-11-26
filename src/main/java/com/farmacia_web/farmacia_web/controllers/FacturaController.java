package com.farmacia_web.farmacia_web.controllers;

import com.farmacia_web.farmacia_web.models.*;
import com.farmacia_web.farmacia_web.repositories.*;
import com.farmacia_web.farmacia_web.services.*;
import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controlador que gestiona las operaciones relacionadas con las facturas.
 * Permite manejar funcionalidades de ventas, compras, y generación de reportes de facturación.
 */
@Controller
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas de ventas y compras")
public class FacturaController {

    @Value("${facturas.ventas.dir}")
    private String ventasDir;

    @Value("${facturas.compras.dir}")
    private String comprasDir;

    @Autowired
    ProductoService productoService;

    @Autowired
    VentaService ventaService;

    @Autowired
    EmpleadoRepository empleadoRepository;

    Map<String, Integer> contadorPorFecha = new HashMap<>();

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private ComprasRepository comprasRepository;
    @Autowired
    private DetallesCompraRepository detallesCompraRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private CompraService compraService;
    @Autowired
    private ProveedorService proveedorService;

    /**
     * Controlador para mostrar el formulario de ventas.
     *
     * @param model            Objeto del modelo para pasar datos a la vista.
     * @param authentication   Objeto de autenticación que contiene los detalles del empleado autenticado.
     * @param identificacion   Identificación del cliente (puede ser null).
     * @return Nombre de la vista que representa el formulario de ventas.
     */
    @GetMapping("/ventas")
    @Operation(summary = "Mostrar formulario de ventas", description = "Muestra el formulario para gestionar ventas, incluyendo productos y clientes.")
    public String mostrarFormVentas(Model model, Authentication authentication, @RequestParam(value = "identificacion", required = false) Long identificacion) {
        if (identificacion == null) {
            model.addAttribute("cliente", new Cliente());
        } else {
            Cliente cliente = clienteRepository.findByIdentificacion(identificacion).orElseThrow();
            model.addAttribute("cliente", cliente);
        }
        List<Producto> productos = productoService.obtenerProductos();
        int totalProductos = productos.size();
        int filas = (totalProductos + 3) / 4; // Calcular filas redondeando hacia arriba
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("productos", productoService.obtenerProductos());
        model.addAttribute("filas", filas);
        model.addAttribute("venta", new Venta());
        return "entidades/facturasVentas";
    }



    /**
     * Controlador para mostrar el formulario de compras.
     *
     * @param model            Objeto del modelo para pasar datos a la vista.
     * @param authentication   Objeto de autenticación que contiene los detalles del empleado autenticado.
     * @param id               ID del proveedor (puede ser null).
     * @return Nombre de la vista que representa el formulario de compras.
     */

    @GetMapping("/compras")
    @Operation(summary = "Mostrar formulario de compras", description = "Muestra el formulario para gestionar compras, incluyendo productos y proveedores.")
    public String mostrarFormCompras(Model model, Authentication authentication, @RequestParam(value = "idProveedor", required = false) Long id) {
        List<Producto> productos = new ArrayList<>();

        if (id == null) {
            model.addAttribute("proveedor", new Proveedor());
        } else {
            Proveedor proveedor = proveedorRepository.findById(id).orElseThrow();
            model.addAttribute("proveedor", proveedor);
            productos = productoRepository.findByProveedorId(id);
        }

        int totalProductos = productos.size();
        int filas = (totalProductos + 3) / 4; // Calcular filas redondeando hacia arriba
        EmpleadoDetails empleadoDetails = (EmpleadoDetails) authentication.getPrincipal();
        model.addAttribute("empleadoAuth", empleadoDetails);
        model.addAttribute("productos", productos);
        model.addAttribute("filas", filas);
        model.addAttribute("venta", new Venta());
        return "entidades/facturasCompras";
    }

    /**
     * Controlador para crear una nueva venta y generar su factura.
     *
     * @param detallesVentasListWrapper Objeto que encapsula los detalles de las ventas.
     * @param model                     Modelo para pasar datos a la vista.
     * @param principal                 Objeto que representa al usuario autenticado.
     * @param total                     Total de la venta (puede ser null).
     * @param metodoPago                Método de pago seleccionado.
     * @param idCliente                 ID del cliente asociado (puede ser null).
     * @param rm                        Objeto para enviar atributos flash.
     * @return Redirección a la vista de facturas de ventas.
     */
    @PostMapping("/nueva-venta")
    @Operation(summary = "Registrar nueva venta", description = "Registra una nueva venta y genera su respectiva factura.")
    @Transactional
    public String nuevaVenta(@ModelAttribute DetallesVentasListWrapper detallesVentasListWrapper,
                             Model model,
                             Principal principal,
                             @RequestParam(value = "total", required = false) BigDecimal total,
                             @RequestParam("metodoPago") String metodoPago,
                             @RequestParam(value = "cliente", required = false) Long idCliente, RedirectAttributes rm) {
        if (total == null || total.equals(BigDecimal.ZERO)) {
            rm.addFlashAttribute("errorMessage", "Seleccione productos para realizar la venta");
            return "redirect:/facturas/ventas";
        }
        try {
            // Paso 1: Crear y guardar la factura
            Factura factura = new Factura();
            factura.setSerial(generarNumeroFactura("venta"));
            factura.setTipo("venta");
            ventaService.guardarFactura(factura);

            // Paso 2: Crear y guardar la venta
            Venta venta = new Venta();
            Empleado empleado = empleadoRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            venta.setEmpleado(empleado);
            venta.setTotal(total);
            venta.setMetodoPago(metodoPago);
            venta.setFactura(factura);

            if (idCliente != null) {
                Cliente cliente = clienteService.obtenerPorId(idCliente);
                venta.setCliente(cliente);
            }

            Venta ventaGuardada = ventaService.guardarVenta(venta);

            // Paso 3: Asignar y guardar los detalles de la venta
            List<DetallesVenta> detallesVenta = detallesVentasListWrapper.getDetallesVenta();
            for (DetallesVenta detalle : detallesVenta) {
                detalle.setVenta(ventaGuardada);
                ventaGuardada.getDetallesVenta().add(detalle);
                ventaService.guardarDetalleVenta(detalle);


            }

            // Paso 4: Generar archivo de factura y actualizar la factura
            String rutaArchivo = generarFacturaVenta(factura);
            factura.setRutaArchivo(rutaArchivo);
            ventaService.actualizarFactura(factura);
            ventaGuardada.setFactura(factura);
            ventaService.actualizarVenta(ventaGuardada);

            // Paso 5: Redireccionar con mensaje de éxito
            rm.addFlashAttribute("message", "Venta y factura creadas exitosamente");
            rm.addFlashAttribute("serialFactura", factura.getSerial());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage",  e.getMessage());
            return "redirect:/facturas/ventas"; // Mantener la redirección para el flujo en caso de error
        }
        return "redirect:/facturas/ventas";
    }

    /**
     * Controlador para registrar una nueva compra y generar su factura.
     *
     * @param detallesComprasListWrapper Objeto que encapsula los detalles de las compras.
     * @param model                      Modelo para pasar datos a la vista.
     * @param principal                  Objeto que representa al usuario autenticado.
     * @param total                      Total de la compra.
     * @param metodoPago                 Método de pago seleccionado.
     * @param idProveedor                ID del proveedor asociado (puede ser null).
     * @param rm                         Objeto para enviar atributos flash.
     * @return Redirección a la vista de facturas de compras.
     */
    @PostMapping("/nueva-compra")
    @Operation(summary = "Registrar nueva compra", description = "Registra una nueva compra y genera su respectiva factura.")
    @Transactional
    public String nuevaCompra(@ModelAttribute DetallesComprasListWrapper detallesComprasListWrapper,
                              Model model, Principal principal,
                              @RequestParam(value = "total", required = false) BigDecimal total,
                              @RequestParam("metodoPago") String metodoPago,
                              @RequestParam(value = "idProveedor", required = false) Long idProveedor, RedirectAttributes rm) {
        if (total == null || total.equals(BigDecimal.ZERO)) {
            rm.addFlashAttribute("errorMessage", "Seleccione productos para realizar la compra");
            return "redirect:/facturas/compras";
        }
        try {
            // Paso 1: Obtener detalles de compra desde el formulario
            List<DetallesCompra> detallesCompra = detallesComprasListWrapper.getDetallesCompra();


            // Paso 2: Crear la compra
            Compra compra = new Compra();
            Empleado empleado = compraService.obtenerEmpleadoPorEmail(principal.getName());

            // Paso 3: Crear la factura asociada
            Factura factura = new Factura();
            factura.setSerial(generarNumeroFactura("compra"));
            factura.setTipo("compra");

            // Paso 4: Asociar la factura a la compra
            compra.setFactura(factura);
            compraService.guardarFactura(factura);

            // Paso 5: Asignar los detalles de la compra y guardar
            compra.setProveedor(compraService.obtenerProveedorPorId(idProveedor));
            compra.setMetodoPago(metodoPago);
            compra.setEmpleado(empleado);
            compra.setTotal(total);

            // Guardar la compra para obtener su ID
            Compra compraGuardada = compraService.guardarCompra(compra);

            // Paso 6: Guardar cada detalle de la compra
            for (DetallesCompra detalle : detallesCompra) {
                // Asociar el detalle con la compra guardada
                detalle.setCompra(compraGuardada);
                compra.getDetallesCompra().add(detalle);

                // Guardar el detalle de la compra
                compraService.guardarDetalleCompra(detalle);
            }


            // Paso 7: Generar y almacenar el archivo de la factura
            factura.setRutaArchivo(generarFacturaCompra(compraGuardada.getFactura()));
            compraService.actualizarFactura(factura);

            // Paso 8: Redirigir a la vista de compras (o facturas)
            rm.addFlashAttribute("message", "Compra y factura creadas exitosamente.");
            rm.addFlashAttribute("serialFactura", factura.getSerial());
        } catch (RuntimeException e) {
            rm.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/facturas/compras";
    }
    /**
     * Genera un número único para la factura basado en la fecha y un contador del número de facturas realizado para ese día.
     *
     * @param tipo Tipo de factura ("venta" o "compra").
     * @return Número de factura generado en el formato específico.
     */
    public String generarNumeroFactura(String tipo) {
        // Obtener la fecha actual en formato yyyy-MM-dd para la búsqueda y ddMMyyyy para la generación del número de factura
        SimpleDateFormat formatoFechaBuscar = new SimpleDateFormat("yyyy-MM-dd");
        String fechaBuscar = formatoFechaBuscar.format(new Date());
        System.out.println(fechaBuscar);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");
        String fecha = formatoFecha.format(new Date());

        // Buscar el último número de factura del día en la base de datos
        String ultimoNumeroFactura = facturaRepository.findUltimoNumeroFacturaPorFecha(fechaBuscar, tipo);

        // Si hay facturas generadas hoy, extraer los últimos 4 dígitos del número; si no, comenzar desde 1
        int contador;
        if (ultimoNumeroFactura != null) {
            // Extraer los últimos 4 caracteres del número de factura
            String contadorStr = ultimoNumeroFactura.substring(ultimoNumeroFactura.length() - 3);
            contador = Integer.parseInt(contadorStr) + 1; // Incrementar el contador
        } else {
            contador = 1; // Iniciar desde 1 si no hay facturas hoy
        }

        // Definir el prefijo (C para compra, V para venta)
        String prefijo = tipo.equals("venta") ? "V" : "C";

        // Generar el número de factura con formato que incluya la fecha y un contador de 4 dígitos
        return prefijo + fecha + String.format("%03d", contador);
    }


    /**
     * Genera un archivo PDF para la factura de una venta.
     *
     * @param factura Objeto Factura que contiene los datos necesarios para la generación del archivo.
     * @return Ruta relativa del archivo PDF generado.
     * @throws IOException Si ocurre un error al acceder o escribir el archivo.
     */
   public String generarFacturaVenta (Factura factura) throws IOException {
            Venta venta = ventaRepository.findByFactura(factura).orElseThrow();

       try {
           // Crear directorio si no existe
           File directorio = new File(ventasDir);
           if (!directorio.exists()) {
               directorio.mkdirs();
           }

           // Ruta del archivo
           String rutaArchivo = Paths.get(ventasDir, factura.getSerial() + ".pdf").toString();
               PdfWriter writer = new PdfWriter(new FileOutputStream(rutaArchivo));
               PdfDocument pdf = new PdfDocument(writer);
               pdf.addNewPage();

               PdfFont poppinsFont = PdfFontFactory.createFont("src/main/resources/static/fonts/Poppins-Regular.ttf", true);

               // Crear el documento
               Document document = new Document(pdf);

               Color verdeOscuro = new DeviceRgb(34, 139, 34);
               Color verdeClaro = new DeviceRgb(144, 238, 144);

               // Encabezado con logo
               String logoPath = "src/main/resources/static/imagenes/logo_farmacia.png"; // Ruta del logo de la farmacia
               Image logo = new Image(ImageDataFactory.create(logoPath));
               logo.setWidth(60);

               // Formateo de la fecha
               SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
               String fechaFormateada = dateFormat.format(venta.getCreacion());

               // Título de la factura
               Paragraph header = new Paragraph("Factura de Venta")
                       .setFont(poppinsFont)
                       .setFontSize(16)
                       .setBold()
                       .setTextAlignment(TextAlignment.CENTER)
                       .setFontColor(verdeOscuro);

               // Información de la farmacia y cliente
               Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
               infoTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
               infoTable.addCell(new Cell().add(new Paragraph("Farmacia Vida Natural").setFont(poppinsFont))
                       .setTextAlignment(TextAlignment.LEFT)
                       .setBorder(Border.NO_BORDER));

               infoTable.addCell(new Cell().add(new Paragraph(fechaFormateada))
                       .setBold().setFontColor(verdeOscuro)
                       .setTextAlignment(TextAlignment.LEFT)
                       .setBorder(Border.NO_BORDER)
                       .setFont(poppinsFont)
                       .setMarginBottom(20));

               infoTable.addCell(new Cell().add(new Paragraph("Factura N°: " + factura.getSerial()))
                       .setBold().setFontColor(verdeOscuro)
                       .setTextAlignment(TextAlignment.RIGHT)
                       .setBorder(Border.NO_BORDER)
                       .setFont(poppinsFont)
                       .setMarginBottom(20));

               document.add(header);

               if (venta.getCliente() != null) {
                   Cell clienteCell = new Cell(1,2)
                           .add(new Paragraph("Cliente: " + venta.getCliente().getNombre() + "     identificacion: " + venta.getCliente().getIdentificacion())
                                   .setFontColor(new DeviceRgb(128, 128, 128))
                                   .setFontSize(10)
                                   .setFont(poppinsFont))
                           .setTextAlignment(TextAlignment.LEFT)
                           .setMarginBottom(20)
                           .setBorder(Border.NO_BORDER); // Sin bordes

                   infoTable.addCell(clienteCell);
               }

               document.add(infoTable);



               DecimalFormatSymbols symbols = new DecimalFormatSymbols();
               symbols.setGroupingSeparator('.');
               symbols.setDecimalSeparator(',');
               DecimalFormat df = new DecimalFormat("#,##0", symbols);

               // Tabla de productos
               Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2, 2,2})).useAllAvailableWidth().setFont(poppinsFont).setFontSize(10).setTextAlignment(TextAlignment.CENTER);
               table.addHeaderCell(new Cell().add(new Paragraph("Código")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
               table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
               table.addHeaderCell(new Cell().add(new Paragraph("Iva")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
               table.addHeaderCell(new Cell().add(new Paragraph("Precio unitario")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
               table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
               table.addHeaderCell(new Cell().add(new Paragraph("Subtotal")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));

               // Agregar celdas de detalles sin bordes
               for (DetallesVenta detalle : venta.getDetallesVenta()) {
                   table.addCell(new Cell().add(new Paragraph(String.valueOf(detalle.getProducto().getCodigo())).setFontSize(10)).setBorder(Border.NO_BORDER));
                   table.addCell(new Cell().add(new Paragraph(detalle.getProducto().getNombre() + " " + detalle.getProducto().getMarca().getNombre() + " " + detalle.getProducto().getPresentacion()).setFontSize(10)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
                   table.addCell(new Cell().add(new Paragraph(String.valueOf(detalle.getProducto().getIva() + "%")).setFontSize(10)).setBorder(Border.NO_BORDER));
                   table.addCell(new Cell().add(new Paragraph("$ " + df.format(detalle.getPrecioVenta())).setFontSize(10)).setFontColor(verdeOscuro).setBorder(Border.NO_BORDER));
                   table.addCell(new Cell().add(new Paragraph(detalle.getCantidad().toString()).setFontSize(10)).setFontColor(verdeOscuro).setBorder(Border.NO_BORDER));
                   table.addCell(new Cell().add(new Paragraph("$ " + df.format(detalle.getSubtotal())).setFontSize(10)).setFontColor(verdeOscuro).setBorder(Border.NO_BORDER));
               }

               // Agregar fila de total sin bordes
               table.addCell(new Cell(1, 5).add(new Paragraph("Total:").setFontSize(10)).setFontColor(verdeOscuro).setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
               table.addCell(new Cell().add(new Paragraph(df.format(venta.getTotal())).setFontSize(10)).setFontColor(verdeOscuro).setBold().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

               // Agregar fila de método de pago sin bordes
               table.addCell(new Cell(1, 5).add(new Paragraph("Método de Pago").setFontSize(10)).setFontColor(verdeOscuro).setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
               table.addCell(new Cell().add(new Paragraph(venta.getMetodoPago()).setFontSize(10)).setFontColor(verdeOscuro).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

               // Añadir la tabla al documento
               document.add(table);

               // Pie de página
               Paragraph footer = new Paragraph("Gracias por su compra en Farmacia Vida Natural")
                       .setTextAlignment(TextAlignment.CENTER)
                       .setFontColor(new DeviceRgb(128, 128, 128))
                       .setFontSize(10)
                       .setFont(poppinsFont);

               Paragraph empleado = new Paragraph("Usted fue atendido por: " + venta.getEmpleado().getNombre())
                        .setTextAlignment(TextAlignment.CENTER)
                       .setFontColor(new DeviceRgb(128, 128, 128))
                       .setFontSize(10)
                       .setFont(poppinsFont);

               document.add(empleado);
               document.add(footer);
               document.close();


               return "/archivos/facturasVentas/" + factura.getSerial() + ".pdf";
           } catch (Exception e) {
               throw new RuntimeException(e);
           }

        }

    /**
     * Genera un archivo PDF para la factura de una compra.
     *
     * @param factura Objeto Factura que contiene los datos necesarios para la generación del archivo.
     * @return Ruta relativa del archivo PDF generado.
     * @throws IOException Si ocurre un error al acceder o escribir el archivo.
     */
    public String generarFacturaCompra (Factura factura) throws IOException {
        Compra compra = comprasRepository.findByFactura(factura).orElseThrow();

        try {
            // Crear el directorio si no existe
            File directorio = new File(comprasDir);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Crear la ruta del archivo
            String rutaArchivo = Paths.get(comprasDir, factura.getSerial() + ".pdf").toString();
            PdfWriter writer = new PdfWriter(new FileOutputStream(rutaArchivo));
            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();

            PdfFont poppinsFont = PdfFontFactory.createFont("src/main/resources/static/fonts/Poppins-Regular.ttf", true);

            // Crear el documento
            Document document = new Document(pdf);

            Color verdeOscuro = new DeviceRgb(34, 139, 34);
            Color verdeClaro = new DeviceRgb(144, 238, 144);

            // Encabezado con logo
            String logoPath = "src/main/resources/static/imagenes/logo_farmacia.png"; // Ruta del logo de la farmacia
            Image logo = new Image(ImageDataFactory.create(logoPath));
            logo.setWidth(60);

            // Formateo de la fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            String fechaFormateada = dateFormat.format(compra.getCreacion());

            // Título de la factura
            Paragraph header = new Paragraph("Factura de Compra")
                    .setFont(poppinsFont)
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(verdeOscuro);

            // Información de la farmacia y cliente
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
            infoTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph("Farmacia Vida Natural").setFont(poppinsFont))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER));

            infoTable.addCell(new Cell().add(new Paragraph(fechaFormateada))
                    .setBold().setFontColor(verdeOscuro)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER)
                    .setFont(poppinsFont)
                    .setMarginBottom(20));

            infoTable.addCell(new Cell().add(new Paragraph("Factura N°: " + factura.getSerial()))
                    .setBold().setFontColor(verdeOscuro)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER)
                    .setFont(poppinsFont)
                    .setMarginBottom(20));

            document.add(header);


                Cell proveedorCell = new Cell(1,2)
                        .add(new Paragraph("Proveedor: " + compra.getProveedor().getNombre() +"     Contacto: " + compra.getProveedor().getEmail())
                                .setFontColor(new DeviceRgb(128, 128, 128))
                                .setFontSize(10)
                                .setFont(poppinsFont))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setMarginBottom(20)
                        .setBorder(Border.NO_BORDER); // Sin bordes

                infoTable.addCell(proveedorCell);


            document.add(infoTable);



            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("#,##0", symbols);

            // Tabla de productos
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2, 2,2})).useAllAvailableWidth().setFont(poppinsFont).setFontSize(10).setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(new Cell().add(new Paragraph("Código")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
            table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
            table.addHeaderCell(new Cell().add(new Paragraph("Iva")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
            table.addHeaderCell(new Cell().add(new Paragraph("Precio unitario")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
            table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));
            table.addHeaderCell(new Cell().add(new Paragraph("Subtotal")).setBackgroundColor(verdeClaro).setBold().setBorder(Border.NO_BORDER));

// Agregar celdas de detalles sin bordes
            for (DetallesCompra detalle : compra.getDetallesCompra()) {
                table.addCell(new Cell().add(new Paragraph(detalle.getProducto().getCodigo()))
                        .setFontSize(10)
                        .setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(detalle.getProducto().getNombre() + " "
                                + detalle.getProducto().getMarca().getNombre() + " "
                                + detalle.getProducto().getPresentacion()))
                        .setFontSize(10)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT));

                table.addCell(new Cell().add(new Paragraph(String.valueOf(detalle.getProducto().getIva() + "%")))
                        .setFontSize(10)
                        .setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph("$ " + df.format(detalle.getPrecioCompra())))
                        .setFontSize(10)
                        .setFontColor(verdeOscuro)
                        .setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(detalle.getCantidad().toString()))
                        .setFontSize(10)
                        .setFontColor(verdeOscuro)
                        .setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph("$ " + df.format(detalle.getSubtotal())))
                        .setFontSize(10)
                        .setFontColor(verdeOscuro)
                        .setBorder(Border.NO_BORDER));
            }

// Agregar fila de total sin bordes
            table.addCell(new Cell(1, 5).add(new Paragraph("Total:"))
                    .setFontSize(10)
                    .setFontColor(verdeOscuro)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph(df.format(compra.getTotal())))
                    .setFontSize(10)
                    .setFontColor(verdeOscuro)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER));

// Agregar fila de método de pago sin bordes
            table.addCell(new Cell(1, 5).add(new Paragraph("Método de Pago"))
                    .setFontSize(10)
                    .setFontColor(verdeOscuro)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph(compra.getMetodoPago()))
                    .setFontSize(10)
                    .setFontColor(verdeOscuro)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER));

            // Añadir la tabla al documento
            document.add(table);




            // Pie de página
            Paragraph footer = new Paragraph("registro de compra realizada por Farmacia Vida Natural")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(128, 128, 128))
                    .setFontSize(10)
                    .setFont(poppinsFont);

            Paragraph empleado = new Paragraph("Bajo la responsabilidad de: " + compra.getEmpleado().getNombre())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(128, 128, 128))
                    .setFontSize(10)
                    .setFont(poppinsFont);

            document.add(footer);
            document.add(empleado);
            document.close();

            return "/archivos/facturasCompras/" + factura.getSerial() + ".pdf";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}


