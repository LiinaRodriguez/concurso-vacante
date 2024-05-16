function completeTask() {
    // Obtén todas las filas de la tabla
    var filas = document.querySelectorAll('#creditTable tbody tr');

    // Itera a través de las filas y busca la fila con estado "DRAFT"
    for (var i = 0; i < filas.length; i++) {
        var estado = filas[i].querySelector('td:last-child span').textContent.trim();
         if (estado === "DRAFT") {
            // Encuentra la fila con estado "DRAFT", obtén los datos y envíalos al controlador
            var taskId = filas[i].querySelector('td:first-child span').textContent.trim();
            // Verificar el valor de la penúltima columna (penúltimo td) en la última fila
            var lastColumnValue = filas[i].querySelector("td:nth-last-child(2) span").textContent;
            console.log(lastColumnValue);
            console.log("procesoId: " + taskId);

            // Crear un formulario oculto y agregar los datos a enviar
            var form = document.getElementById('routing');

            if (lastColumnValue == 0) {
                form.action = "/complete"
            } else {
                form.action = "/message-event"
            }

            // Después de obtener los valores
            document.getElementById('taskId').value = taskId;

            document.body.appendChild(form);

            // Enviar el formulario
            form.submit();

            // Marcar que se envió el formulario
            formularioEnviado = true;


            break;
        }
    }
    if(formularioEnviado){
        console.log("Se envió el formulario");
    }else{
        console.log("Intente nuevamente");
    }
}