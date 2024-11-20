package com.farmacia_web.farmacia_web.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/archivos")
public class ArchivosController {

    private final String basePath = "C:/Users/USUARIO/proyectos/java/farmacia-web/archivos/";

    @GetMapping("/{tipo}/{nombreArchivo}")
    public ResponseEntity<Resource> servirArchivo(
            @PathVariable String tipo,
            @PathVariable String nombreArchivo) {
        try {
            // Construir la ruta completa del archivo
            Path archivoPath = Paths.get(basePath, tipo, nombreArchivo);
            Resource resource = new UrlResource(archivoPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Configurar encabezados HTTP para servir el PDF
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf") // Tipo de contenido
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"") // Abrir en el navegador
                        .body(resource);
            } else {
                throw new RuntimeException("Archivo no encontrado: " + nombreArchivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al servir el archivo: " + nombreArchivo, e);
        }
    }
}
