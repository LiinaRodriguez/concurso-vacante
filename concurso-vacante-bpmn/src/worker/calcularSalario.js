import { Client, Variables, logger } from "camunda-external-task-client-js"

const config = {
    baseUrl: "http://localhost:9000/engine-rest",
    use: logger
}

const client = new Client(config);

client.subscribe("calcularSalario", async function ({ task, taskService }) {
    const salario = task.variables.get("salario");
    const mult_estudios = task.variables.get("mult_estudio");
    console.log("Salario: " + salario);
    const salarioPropuesto = salario  * mult_estudios;
    // console.log(salario);
    console.log("Se ha calculado el salario de: " + salarioPropuesto);
    
    const processVariables = new Variables();
    processVariables.set("salarioPropuesto", salarioPropuesto);

await taskService.complete(task, processVariables);
   
});
