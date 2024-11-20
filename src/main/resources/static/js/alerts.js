window.onload = function() {
    verificarContenido(); // Ejecutar la verificación cuando la página se cargue
};

function verificarContenido() {
    // Intenta obtener el mensaje de error y éxito
    const errorMessageElement = document.getElementById("errorMessage");
    const successMessageElement = document.getElementById("message");

    if (errorMessageElement) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: errorMessageElement.textContent.trim(),
        });
    } else if (successMessageElement) {
        Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: successMessageElement.textContent.trim(),
        });
    }
}