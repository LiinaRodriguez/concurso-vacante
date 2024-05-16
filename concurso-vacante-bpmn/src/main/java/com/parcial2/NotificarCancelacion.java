package com.parcial2;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class NotificarCancelacion implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String ruta = "src/main/resources/Notificaciones";
        String nombre = (String) delegateExecution.getVariable("fullName");
        String cargo = (String) delegateExecution.getVariable("role");
        String archivo = nombre + ".txt";

        String rutaCompleta = ruta + File.separator + archivo;
        File file = new File(rutaCompleta);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("Estimado " + nombre + ",\n");
            bw.write("Se canceló su solicitud al puesto de " + cargo
                    + " porque la ventana de tiempo para la respuesta a la oferta se cerro.\n");

            bw.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo " + e.getMessage());
        }
    }
}
