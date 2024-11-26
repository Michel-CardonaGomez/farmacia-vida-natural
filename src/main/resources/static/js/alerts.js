window.onload = function () {
    verificarContenido(); // Ejecutar la verificación cuando la página se cargue
};

function verificarContenido() {
    // Obtener los elementos de error, éxito y serialFactura
    const errorMessageElement = document.getElementById("errorMessage");
    const successMessageElement = document.getElementById("message");
    const serialFacturaElement = document.getElementById("serialFactura");

    // Verificar si serialFactura existe y obtener su contenido
    const serialFactura = serialFacturaElement ? serialFacturaElement.textContent.trim() : null;
    let facturaUrl = null;

    // Generar URL según el prefijo del serialFactura
    if (serialFactura) {
        if (serialFactura.startsWith("C")) {
            facturaUrl = `/archivos/facturasCompras/${serialFactura}.pdf`; // Ruta para compras
        } else if (serialFactura.startsWith("V")) {
            facturaUrl = `/archivos/facturasVentas/${serialFactura}.pdf`; // Ruta para ventas
        }
    }

    // Mostrar mensaje de error si existe
    if (errorMessageElement && errorMessageElement.textContent.trim() !== "") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: errorMessageElement.textContent.trim(),
        });
        return; // Salir para no mostrar otros mensajes
    }

    // Mostrar mensaje de éxito si existe
    if (successMessageElement && successMessageElement.textContent.trim() !== "") {
        if (serialFactura && facturaUrl) {
            // Caso específico para serialFactura
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: successMessageElement.textContent.trim(),
                showCancelButton: true,
                cancelButtonText: 'OK',
                confirmButtonText: 'Visualizar Factura',
                preConfirm: () => {
                    window.open(facturaUrl, '_blank');
                },
            });
        } else {
            // Mensaje de éxito genérico
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: successMessageElement.textContent.trim(),
            });
        }
    }
}
