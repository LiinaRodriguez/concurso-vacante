package com.parcial2;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class EnviarBienvenida implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String ruta = "src/main/resources/Bienvenidos";
        String nombre = (String) delegateExecution.getVariable("fullName");
        String cargo = (String) delegateExecution.getVariable("role");
        Integer salarioPropuesto = (Integer) delegateExecution.getVariable("salarioPropuesto");
        String archivo = nombre + ".txt";

        String rutaCompleta = ruta + File.separator + archivo;
        File file = new File(rutaCompleta);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("Bienvenido " + nombre + ", a la empresa X.\n");
            bw.write("Gracias por aplicar al cargo de " + cargo + ".\n");
            bw.write("Tu salario será: $" + salarioPropuesto + ".\n");

            bw.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo " + e.getMessage());
        }
    }
}