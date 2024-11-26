
let totalFactura = 0;
let productoCount = 0;

$(document).ready(function() {
    // Inicializar Select2 para permitir búsqueda
    $('#productoSelect').select2({
        placeholder: "Buscar producto",
        allowClear: true
    });
});


function agregarProducto(button) {
    const productoDiv = button.closest('.producto-info');
    const id = productoDiv.querySelector('.producto-nombre').getAttribute('data-id');
    const nombre = productoDiv.querySelector('.producto-nombre').getAttribute('data-nombre');
    const codigo = productoDiv.querySelector('.producto-nombre').getAttribute('data-codigo');
    const iva = parseFloat(productoDiv.querySelector('.producto-nombre').getAttribute('data-iva'));
    const precio = parseFloat(productoDiv.querySelector('.producto-nombre').getAttribute('data-precio'));
    const stock = parseInt(productoDiv.querySelector('.producto-nombre').getAttribute('data-stock'));
    const cantidadInput = productoDiv.querySelector('.cantidad-input');
    const cantidad = parseInt(cantidadInput.value);
    const inputTotal = document.getElementById('inputTotal');

    if (isNaN(cantidad) || cantidad <= 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Seleccione una cantidad válida para continuar',
        });
        return;
    }

    if (cantidad > stock) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No hay suficiente stock disponible',
        });
        return;
    }

    const facturaBody = document.getElementById('facturaBody');
    let productoExiste = false;

    // Buscar si el producto ya está en la factura
    facturaBody.querySelectorAll('tr').forEach(row => {
        const productoId = row.dataset.productoId;
        if (productoId === id) {
            const cantidadTd = row.querySelector('td:nth-child(5)');
            const subtotalTd = row.querySelector('td:nth-child(6)');

            const cantidadActual = parseInt(cantidadTd.textContent);
            const nuevaCantidad = cantidadActual + cantidad;

            if (nuevaCantidad > stock) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'No hay suficiente stock para esa cantidad',
                });
                productoExiste = true; // Evita continuar si excede el stock
                return;
            }

            cantidadTd.textContent = nuevaCantidad;

            const nuevoSubtotal = Math.round(precio * nuevaCantidad);
            subtotalTd.textContent = nuevoSubtotal.toLocaleString('es-CO');
            row.dataset.subtotal = nuevoSubtotal; // Actualizar el subtotal en el atributo de la fila

            totalFactura += Math.round(precio * cantidad);
            productoExiste = true;
        }
    });

    if (productoExiste) {
        actualizarTotalFactura();
        cantidadInput.value = 1;
        return;
    }

    // Calcular subtotal
    const subtotal = Math.round(precio * cantidad);
    totalFactura += subtotal;

    const row = document.createElement('tr');
    row.dataset.productoId = id; // Asignar el ID del producto como atributo
    row.dataset.subtotal = subtotal; // Guardar el subtotal como atributo

    row.innerHTML = `
        <td><button onclick="eliminarProducto(this, ${subtotal})" style="background: transparent; border: none;"><i class="fas fa-times" style="color: grey; background: transparent"></i></button>${codigo}</td>
        <td>${nombre}</td>
        <td>${iva}%</td>
        <td>${precio.toLocaleString('es-CO')}</td>
        <td>${cantidad}</td>
        <td>${subtotal.toLocaleString('es-CO')}</td>
    `;

    facturaBody.appendChild(row);
    productoCount++;

    actualizarTotalFactura();
    cantidadInput.value = 1;
}

function eliminarProducto(button) {
    const row = button.closest('tr');
    const subtotal = parseFloat(row.dataset.subtotal); // Obtener el subtotal de la fila
    totalFactura -= subtotal; // Restar el subtotal del total general
    row.remove();

    actualizarTotalFactura();
}

function actualizarTotalFactura() {
    const totalFacturaFormateado = totalFactura.toLocaleString('es-CO');
    document.getElementById('totalFactura').textContent = totalFacturaFormateado;
    document.getElementById('inputTotal').value = totalFactura;
}

function agregarProductoTablet() {
    // Obtener el select y el producto seleccionado
    const productoSelect = document.getElementById('productoSelect');
    const selectedOption = productoSelect.options[productoSelect.selectedIndex];

    if (!selectedOption || selectedOption.value === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Seleccione un producto para continuar',
        });
        return;
    }

    // Obtener la información del producto seleccionado
    const id = selectedOption.getAttribute('data-id');
    const nombre = selectedOption.getAttribute('data-nombre');
    const codigo = selectedOption.getAttribute('data-codigo');
    const stock = selectedOption.getAttribute('data-stock');
    const iva = parseFloat(selectedOption.getAttribute('data-iva'));
    const precio = parseFloat(selectedOption.getAttribute('data-precio'));
    const cantidadInputTablet = document.getElementById('cantidadTablet');
    const cantidadTablet = parseInt(cantidadInputTablet.value);
    const inputTotal = document.getElementById('inputTotal');

    if (isNaN(cantidadTablet) || cantidadTablet <= 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Seleccione una cantidad válida para continuar',
        });
        return;
    }

    if (cantidadTablet > stock) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No hay suficiente stock disponible',
        });
        return;
    }

    const facturaBody = document.getElementById('facturaBody');
    let productoExiste = false;

    // Buscar si el producto ya está en la factura
    facturaBody.querySelectorAll('tr').forEach(row => {
        const productoId = row.dataset.productoId;
        if (productoId == id) {
            const cantidadTd = row.querySelector('td:nth-child(5)');
            const subtotalTd = row.querySelector('td:nth-child(6)');

            const cantidadActual = parseInt(cantidadTd.textContent);
            const nuevaCantidad = cantidadActual + cantidadTablet;

            if (nuevaCantidad > stock) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'No hay suficiente stock disponible para esa cantidad',
                });
                productoExiste = true;
                return;
            }

            cantidadTd.textContent = nuevaCantidad;

            const nuevoSubtotal = Math.round(precio * nuevaCantidad);
            subtotalTd.textContent = nuevoSubtotal.toLocaleString('es-CO');
            row.dataset.subtotal = nuevoSubtotal; // Actualizar el subtotal en el atributo de la fila

            totalFactura += Math.round(precio * cantidadTablet);
            productoExiste = true;
        }
    });

    if (productoExiste) {
        actualizarTotalFactura();
        cantidadInputTablet.value = 1;
        return;
    }

    // Calcular subtotal para un producto nuevo
    const subtotal = Math.round(precio * cantidadTablet);
    totalFactura += subtotal;

    // Crear una nueva fila en la tabla de factura
    const row = document.createElement('tr');
    row.dataset.productoId = id; // Asignar el ID del producto como atributo
    row.dataset.subtotal = subtotal; // Guardar el subtotal como atributo

    row.innerHTML = `
        <td>
            <button onclick="eliminarProducto(this, ${subtotal})" style="background: transparent; border: none;">
                <i class="fas fa-times" style="color: grey; background: transparent"></i>
            </button>${codigo}
        </td>
        <td>${nombre}</td>
        <td>${iva}%</td>
        <td>${precio.toLocaleString('es-CO')}</td>
        <td>${cantidadTablet}</td>
        <td>${subtotal.toLocaleString('es-CO')}</td>
    `;

    facturaBody.appendChild(row);
    productoCount++;

    actualizarTotalFactura();
    cantidadInputTablet.value = 1;
}

document.getElementById('formFactura').addEventListener('submit', function (event) {
    event.preventDefault(); // Evitar el envío automático inicial del formulario
    generarInputsOcultos(this); // Pasar el formulario a la función
});

function generarInputsOcultos(form) {
    const facturaBody = document.getElementById('facturaBody');
    const inputsOcultosContainer = document.getElementById('inputsOcultos'); // Contenedor para los inputs

    // Eliminar inputs ocultos existentes en el contenedor
    inputsOcultosContainer.innerHTML = '';

    // Crear nuevos inputs ocultos basados en las filas de la tabla
    let productoCount = 0;
    facturaBody.querySelectorAll('tr').forEach(row => {
        const id = row.dataset.productoId;
        const subtotal = parseFloat(row.dataset.subtotal);
        const cantidad = row.querySelector('td:nth-child(5)').textContent;
        const precio = parseFloat(row.querySelector('td:nth-child(4)').textContent.replace(/\./g, '').replace(',', '.'));

        inputsOcultosContainer.insertAdjacentHTML('beforeend', `
            <input type="hidden" name="detallesVenta[${productoCount}].producto" value="${id}">
            <input type="hidden" name="detallesVenta[${productoCount}].precioVenta" value="${precio}">
            <input type="hidden" name="detallesVenta[${productoCount}].cantidad" value="${cantidad}">
            <input type="hidden" name="detallesVenta[${productoCount}].subtotal" value="${subtotal}">
        `);
        productoCount++;
    });

    // Enviar el formulario automáticamente después de generar los inputs
    form.submit();
}


// Función para remover tildes
function removerTildes(str) {
    const mapaTildes = {á: 'a', é: 'e', í: 'i', ó: 'o', ú: 'u', Á: 'a', É: 'e', Í: 'i', Ó: 'o', Ú: 'u'};
    return str.replace(/[áéíóúÁÉÍÓÚ]/g, match => mapaTildes[match] || match);
}

// Función para realizar la búsqueda
function buscarProductos() {
    const query = removerTildes(document.getElementById('search-input').value.toLowerCase());  // Convertir la búsqueda a minúsculas y remover tildes
    const productos = document.querySelectorAll('.container-producto');
    let encontrados = 0;

    productos.forEach(producto => {
        const nombreProducto = removerTildes(producto.querySelector('.producto-nombre').textContent.toLowerCase());

        if (nombreProducto.startsWith(query)) {
            producto.style.display = 'block';  // Mostrar producto
            encontrados++;
        } else {
            producto.style.display = 'none';  // Ocultar producto que no coincida
        }
    });

    // Si no hay productos encontrados, mostrar un mensaje
    if (encontrados === 0) {
        const noResultados = document.getElementById('no-resultados');
        if (!noResultados) {
            const mensaje = document.createElement('p');
            mensaje.id = 'no-resultados';
            mensaje.textContent = 'No se encontraron productos que coincidan.';
            document.querySelector('.productos-container').appendChild(mensaje);
        }
    } else {
        const noResultados = document.getElementById('no-resultados');
        if (noResultados) {
            noResultados.remove();
        }
    }
}

// Evento para realizar la búsqueda mientras escribimos
document.getElementById('search-input').addEventListener('input', buscarProductos);
