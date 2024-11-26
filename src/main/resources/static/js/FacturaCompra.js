let totalFacturaCompra = 0;
let productoCountCompra = 0;

$(document).ready(function () {
    // Inicializar Select2 para permitir búsqueda
    $('#productoSelect').select2({
        placeholder: "Buscar producto",
        allowClear: true
    });
});

function agregarProductoCompra(button) {
    const productoDiv = button.closest('.producto-info');
    const id = productoDiv.querySelector('.producto-nombre').getAttribute('data-id');
    const nombre = productoDiv.querySelector('.producto-nombre').getAttribute('data-nombre');
    const codigo = productoDiv.querySelector('.producto-nombre').getAttribute('data-codigo');
    const iva = parseFloat(productoDiv.querySelector('.producto-nombre').getAttribute('data-iva'));
    const precio = parseFloat(productoDiv.querySelector('.producto-nombre').getAttribute('data-precio'));
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

    const facturaBodyCompra = document.getElementById('facturaBody');
    let productoExiste = false;

    // Buscar si el producto ya está en la factura de compra
    facturaBodyCompra.querySelectorAll('tr').forEach(row => {
        const productoIdInput = row.querySelector(`input[name*="producto"]`);
        if (productoIdInput && productoIdInput.value === id) {
            const cantidadTd = row.querySelector('td:nth-child(5)');
            const subtotalTd = row.querySelector('td:nth-child(6)');

            const cantidadActual = parseInt(cantidadTd.textContent);
            const nuevaCantidad = cantidadActual + cantidad;

            cantidadTd.textContent = nuevaCantidad;

            const nuevoSubtotal = Math.round(precio * nuevaCantidad);
            subtotalTd.textContent = nuevoSubtotal.toLocaleString('es-CO');
            row.dataset.subtotal = nuevoSubtotal; // Actualizar el subtotal en el atributo de la fila

            totalFacturaCompra += Math.round(precio * cantidad);
            productoExiste = true;
        }
    });

    if (productoExiste) {
        actualizarTotalFacturaCompra();
        cantidadInput.value = 1;
        return;
    }

    // Calcular subtotal para un producto nuevo
    const subtotal = Math.round(precio * cantidad);
    totalFacturaCompra += subtotal;

    // Crear una nueva fila en la tabla de factura de compra
    const row = document.createElement('tr');
    row.dataset.productoId = id; // Asignar el ID del producto como atributo
    row.dataset.subtotal = subtotal; // Guardar el subtotal como atributo

    row.innerHTML = `
        <td>
            <button onclick="eliminarProductoCompra(this, ${subtotal})" style="background: transparent; border: none;">
                <i class="fas fa-times" style="color: grey; background: transparent"></i>
            </button>${codigo}
        </td>
        <td>${nombre}</td>
        <td>${iva}%</td>
        <td>${precio.toLocaleString('es-CO')}</td>
        <td>${cantidad}</td>
        <td>${subtotal.toLocaleString('es-CO')}</td>
        <input type="hidden" name="detallesCompra[${productoCountCompra}].producto" value="${id}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].precioCompra" value="${precio}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].cantidad" value="${cantidad}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].subtotal" value="${subtotal}">
    `;

    facturaBodyCompra.appendChild(row);
    productoCountCompra++;

    actualizarTotalFacturaCompra();
    cantidadInput.value = 1;
}

function eliminarProductoCompra(button) {
    const row = button.closest('tr');
    const subtotal = parseFloat(row.dataset.subtotal); // Obtener el subtotal de la fila
    totalFacturaCompra -= subtotal; // Restar el subtotal del total general
    row.remove();

    // Reajustar el contador de productos y los índices de los input hidden
    let index = 0;
    const facturaBodyCompra = document.getElementById('facturaBody');
    facturaBodyCompra.querySelectorAll('tr').forEach(row => {
        const hiddenInputs = row.querySelectorAll('input[type="hidden"]');
        hiddenInputs.forEach(input => {
            const name = input.name;
            input.name = name.replace(/\[\d+\]/, `[${index}]`); // Reemplaza el índice con el nuevo valor
        });
        index++;
    });

    productoCountCompra = index; // Actualizar el contador de productos
    actualizarTotalFacturaCompra();
}


function actualizarTotalFacturaCompra() {
    const totalFacturaCompraFormateado = totalFacturaCompra.toLocaleString('es-CO');
    document.getElementById('totalFactura').textContent = totalFacturaCompraFormateado;
    document.getElementById('inputTotal').value = totalFacturaCompra;
}

function agregarProductoCompraTablet() {
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

    const facturaBodyCompra = document.getElementById('facturaBody');
    let productoExiste = false;

    // Buscar si el producto ya está en la factura de compra
    facturaBodyCompra.querySelectorAll('tr').forEach(row => {
        const productoIdInput = row.querySelector(`input[name*="producto"]`);
        if (productoIdInput && productoIdInput.value === id) {
            const cantidadTd = row.querySelector('td:nth-child(5)');
            const subtotalTd = row.querySelector('td:nth-child(6)');

            const cantidadActual = parseInt(cantidadTd.textContent);
            const nuevaCantidad = cantidadActual + cantidadTablet;

            cantidadTd.textContent = nuevaCantidad;

            const nuevoSubtotal = Math.round(precio * nuevaCantidad);
            subtotalTd.textContent = nuevoSubtotal.toLocaleString('es-CO');
            row.dataset.subtotal = nuevoSubtotal; // Actualizar el subtotal en el atributo de la fila

            totalFacturaCompra += Math.round(precio * cantidadTablet);
            productoExiste = true;
        }
    });

    if (productoExiste) {
        actualizarTotalFacturaCompra();
        cantidadInputTablet.value = 1;
        return;
    }

    // Calcular subtotal para un producto nuevo
    const subtotal = Math.round(precio * cantidadTablet);
    totalFacturaCompra += subtotal;

    // Crear una nueva fila en la tabla de factura de compra
    const row = document.createElement('tr');
    row.dataset.productoId = id; // Asignar el ID del producto como atributo
    row.dataset.subtotal = subtotal; // Guardar el subtotal como atributo

    row.innerHTML = `
        <td>
            <button onclick="eliminarProductoCompra(this, ${subtotal})" style="background: transparent; border: none;">
                <i class="fas fa-times" style="color: grey; background: transparent"></i>
            </button>${codigo}
        </td>
        <td>${nombre}</td>
        <td>${iva}%</td>
        <td>${precio.toLocaleString('es-CO')}</td>
        <td>${cantidadTablet}</td>
        <td>${subtotal.toLocaleString('es-CO')}</td>
        <input type="hidden" name="detallesCompra[${productoCountCompra}].producto" value="${id}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].precioCompra" value="${precio}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].cantidad" value="${cantidadTablet}">
        <input type="hidden" name="detallesCompra[${productoCountCompra}].subtotal" value="${subtotal}">
    `;

    facturaBodyCompra.appendChild(row);
    productoCountCompra++;

    actualizarTotalFacturaCompra();
    cantidadInputTablet.value = 1;
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