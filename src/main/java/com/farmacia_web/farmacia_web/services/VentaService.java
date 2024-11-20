package com.farmacia_web.farmacia_web.services;

import com.farmacia_web.farmacia_web.models.DetallesVenta;
import com.farmacia_web.farmacia_web.models.Factura;
import com.farmacia_web.farmacia_web.models.Venta;
import com.farmacia_web.farmacia_web.repositories.DetallesVentaRepository;
import com.farmacia_web.farmacia_web.repositories.FacturaRepository;
import com.farmacia_web.farmacia_web.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar operaciones CRUD de la entidad Venta.
 */
@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetallesVentaRepository detallesVentaRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Guarda una venta en la base de datos.
     *
     * @param venta Objeto de tipo Venta a guardar
     * @return La venta guardada
     */
    public Venta guardarVenta(Venta venta) {
        try {
            return ventaRepository.save(venta);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al guardar la venta
            throw new RuntimeException("Error al guardar la venta: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda un detalle de venta en la base de datos.
     *
     * @param detalle Objeto de tipo DetallesVenta a guardar
     */
    public void guardarDetalleVenta(DetallesVenta detalle) {
        try {
            detallesVentaRepository.save(detalle);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al guardar el detalle de la venta
            throw new RuntimeException("Error al guardar el detalle de la venta: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda una factura asociada a una venta en la base de datos.
     *
     * @param factura Objeto de tipo Factura a guardar
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
     * Actualiza una venta existente en la base de datos.
     *
     * @param venta Objeto de tipo Venta con los datos actualizados
     */
    public void actualizarVenta(Venta venta) {
        try {
            ventaRepository.save(venta);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al actualizar la venta
            throw new RuntimeException("Error al actualizar la venta: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza una factura existente en la base de datos.
     *
     * @param factura Objeto de tipo Factura con los datos actualizados
     */
    public void actualizarFactura(Factura factura) {
        try {
            facturaRepository.save(factura);
        } catch (Exception e) {
            // Manejo de excepción en caso de error al actualizar la factura
            throw new RuntimeException("Error al actualizar la factura: " + e.getMessage(), e);
        }
    }
}

