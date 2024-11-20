const filterDayButton = document.getElementById('filter-day');
const filterWeekButton = document.getElementById('filter-week');
const filterMonthButton = document.getElementById('filter-month');

// Agregar eventos a los botones de filtro
filterDayButton.addEventListener('click', () => filterTableFecha('day'));
filterWeekButton.addEventListener('click', () => filterTableFecha('week'));
filterMonthButton.addEventListener('click', () => filterTableFecha('month'));

// Función para filtrar por fecha (día, semana, mes)
function filterTableFecha(filterType) {
    const today = new Date();
    const rows = document.querySelectorAll('#data-table tbody tr'); // Seleccionar todas las filas de la tabla
    rows.forEach(row => {
        const fechaCell = row.querySelector('.fecha-creacion'); // Obtener la celda de la fecha
        const fechaTexto = fechaCell.textContent.trim();
        const fechaCreacion = new Date(fechaTexto);

        // Comprobar si la fecha es válida
        if (isNaN(fechaCreacion.getTime())) {  // Cambiar aquí
            row.style.display = 'none';
            return;
        }

        let mostrar = false;
        if (filterType === 'day') {
            // Mostrar solo si es el mismo día
            mostrar = (fechaCreacion.getDate() === today.getDate()) &&
                (fechaCreacion.getMonth() === today.getMonth()) &&
                (fechaCreacion.getFullYear() === today.getFullYear());
        } else if (filterType === 'week') {
            const startOfWeek = new Date(today.setDate(today.getDate() - today.getDay() + 1)); // Lunes de esta semana
            const endOfWeek = new Date(startOfWeek);
            endOfWeek.setDate(startOfWeek.getDate() + 6); // Domingo de esta semana

            mostrar = fechaCreacion >= startOfWeek && fechaCreacion <= endOfWeek;
        } else if (filterType === 'month') {
            // Mostrar solo si está en el mismo mes
            mostrar = (fechaCreacion.getMonth() === today.getMonth()) &&
                (fechaCreacion.getFullYear() === today.getFullYear());
        }

        // Mostrar u ocultar la fila según el filtro de fecha
        row.style.display = mostrar ? '' : 'none';
    });
}

