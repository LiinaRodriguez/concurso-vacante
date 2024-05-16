package com.parcial2;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

public class RevisarVacante implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String cargo = (String) execution.getVariable("role");
        System.out.println(cargo);
        boolean disponible = verificarDisponibilidadCargo(cargo);
        System.out.println(disponible);
        execution.setVariable("CargoDisponible", disponible);
    }

    private boolean verificarDisponibilidadCargo(String cargo) {
        boolean disponible = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("vacantes.txt").getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase(cargo)) {
                    disponible = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return disponible;
    }
}

