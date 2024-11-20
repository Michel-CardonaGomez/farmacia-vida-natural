package com.farmacia_web.farmacia_web.models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente que actúa como un contenedor para manejar una lista de detalles de ventas.
 * Este wrapper es útil para gestionar múltiples objetos `DetallesVenta` en operaciones
 * como formularios o transferencias de datos en el sistema.
 */
@Component
@Data
public class DetallesVentasListWrapper {

    /**
     * Lista de detalles de ventas.
     * Permite agrupar múltiples objetos `DetallesVenta` para su manejo conjunto.
     * Se inicializa como una lista vacía para evitar problemas de `null`.
     */
    private List<DetallesVenta> detallesVenta = new ArrayList<>();
}

