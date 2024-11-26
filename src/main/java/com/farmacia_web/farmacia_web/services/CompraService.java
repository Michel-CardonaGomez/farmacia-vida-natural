package com.farmacia_web.farmacia_web.services;

import com.farmacia_web.farmacia_web.models.*;
import com.farmacia_web.farmacia_web.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar operaciones CRUD de la entidad Compra.
 */
@Service
public class CompraService {

    @Autowired
    private ComprasRepository compraRepository;

    @Autowired
    private DetallesCompraRepository detallesCompraRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    /**
     * Guarda una compra en la base de datos.
     * <p>
     * Este método guarda la información de una compra, incluyendo los detalles y la factura asociada.
     *
     * @param compra Objeto de tipo Compra a guardar.
     * @return La compra guardada.
     * @throws RuntimeException Si ocurre un error al guardar la compra.
     */
    public Compra guardarCompra(Compra compra) {
        try {
            return compraRepository.save(compra);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al guardar la compra
            throw new RuntimeException("Error al guardar la compra: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda un detalle de compra en la base de datos.
     * <p>
     * Este método guarda un detalle de la compra relacionado con una compra específica.
     *
     * @param detalle Objeto de tipo DetallesCompra a guardar.
     * @throws RuntimeException Si ocurre un error al guardar el detalle de la compra.
     */
    public void guardarDetalleCompra(DetallesCompra detalle) {
        try {
            detallesCompraRepository.save(detalle);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al guardar el detalle de la compra
            throw new RuntimeException("Error al guardar el detalle de la compra: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda una factura asociada a una compra en la base de datos.
     * <p>
     * Este método guarda la factura que se genera para una compra específica.
     *
     * @param factura Objeto de tipo Factura a guardar.
     * @throws RuntimeException Si ocurre un error al guardar la factura.
     */
    public void guardarFactura(Factura factura) {
        try {
            facturaRepository.save(factura);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al guardar la factura
            throw new RuntimeException("Error al guardar la factura: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza una compra existente en la base de datos.
     * <p>
     * Este método actualiza los datos de una compra previamente guardada.
     *
     * @param compra Objeto de tipo Compra con los datos actualizados.
     * @throws RuntimeException Si ocurre un error al actualizar la compra.
     */
    public void actualizarCompra(Compra compra) {
        try {
            compraRepository.save(compra);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al actualizar la compra
            throw new RuntimeException("Error al actualizar la compra: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza una factura existente en la base de datos.
     * <p>
     * Este método actualiza los datos de una factura previamente guardada.
     *
     * @param factura Objeto de tipo Factura con los datos actualizados.
     * @throws RuntimeException Si ocurre un error al actualizar la factura.
     */
    public void actualizarFactura(Factura factura) {
        try {
            facturaRepository.save(factura);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al actualizar la factura
            throw new RuntimeException("Error al actualizar la factura: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el proveedor asociado a una compra por su ID.
     * <p>
     * Este método consulta la base de datos para obtener el proveedor de una compra dada
     * su ID.
     *
     * @param idProveedor El ID del proveedor que se desea consultar.
     * @return El proveedor asociado a la compra.
     * @throws RuntimeException Si el proveedor no existe o ocurre un error.
     */
    public Proveedor obtenerProveedorPorId(Long idProveedor) {
        try {
            return proveedorRepository.findById(idProveedor).orElseThrow(() ->
                    new RuntimeException("Proveedor no encontrado con ID: " + idProveedor));
        } catch (Exception e) {
            // Manejo de excepción en caso de error al obtener el proveedor
            throw new RuntimeException("Error al obtener el proveedor: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el empleado que está realizando la compra por su correo.
     *
     * Este método consulta la base de datos para obtener el empleado asociado con el correo
     * electrónico proporcionado.
     *
     * @param email El correo electrónico del empleado.
     * @return El empleado que realiza la compra.
     * @throws RuntimeException Si el empleado no existe o ocurre un error.
     */
    public Empleado obtenerEmpleadoPorEmail(String email) {
        try {
            return empleadoRepository.findByEmail(email).orElseThrow(() ->
                    new RuntimeException("Empleado no encontrado con correo: " + email));
        } catch (Exception e) {
            // Manejo de excepción en caso de error al obtener el empleado
            throw new RuntimeException("Error al obtener el empleado: " + e.getMessage(), e);
        }
    }
}
