package com.farmacia_web.farmacia_web.models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente que actúa como un contenedor para manejar una lista de detalles de compras.
 * Este wrapper es útil para gestionar múltiples objetos `DetallesCompra` en operaciones
 * como formularios o transferencias de datos en el sistema.
 */
@Component
@Data
public class DetallesComprasListWrapper {

    /**
     * Lista de detalles de compras.
     * Permite agrupar múltiples objetos `DetallesCompra` para su manejo conjunto.
     */
    private List<DetallesCompra> detallesCompra = new ArrayList<>();
}

