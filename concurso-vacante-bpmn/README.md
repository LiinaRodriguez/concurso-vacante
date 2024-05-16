# Proyecto Concurso-vacante

## Descripción

Este proyecto implementa un sistema de gestión de solicitudes de empleo utilizando Camunda, automatizando el proceso de concurso para una vacante. El proceso comienza cuando un candidato interesado, decide aplicar a una vacante de empleo. El sistema se encarga de verificar la disponibillidad del puesto,permitir agendar la entrevista con el candidato, realizar entrevistas y tomar decisiones sobre la contratación. 

---

## Requisitos Previos

Es necesario tener instalado lo siguiente:

- **[Java JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)**
   
- **Maven** 
   
- **Git**

- **InteliJ** 

- **[npm](https://nodejs.org/en/blog/release/v16.16.0)**
---

## Configuración Inicial

### Clonar el Repositorio

Para copiar el repositorio mediante comandos, sigue estos pasos:

```bash
git clone https://github.com/LiinaRodriguez/concurso-vacante.git
cd concurso-vacante
```
Para configura el worker, sigue estos pasos:
- Entrar a la carpeta `worker` dentro del proyecto:
  ```bash
  cd src/worker
  ```
- Instalar la libreria de javascript para Camunda
   ```bash
   npm install camunda-external-task-client-js
   ```
Instalar Dependencias y Construir el Proyecto
Una vez obtenido el código fuente, diríjase al directorio del proyecto y  abralo desde el IDE.

Ejecución del Proyecto
Para iniciar el proyecto, ejecute el programa en archivo main:

Ejecutar el worker
 ```bash
   cd src/worker
   node external-task-worker.js
   ```
Verificación de la Instalación
Para asegurarte de que el proyecto se está ejecutando correctamente, acceda al dashboard de Camunda :

Acceder al Dashboard de Camunda

Al Camunda dashboard se puede acceder mediante:
```
http://localhost:8080/
```
Utilizando las siguientes credenciales para iniciar sesión:

- **Username:** `demo`
- **Password:** `demo`

Una vez iniciada la sesión en el dashboard de Camunda, dirígete al Tasklist para comenzar un nuevo proceso de "Proceso Vacante" y llenar los formularios correctamente:

- En el menú lateral izquierdo, selecciona "Tasklist".
- Haz clic en "Start process".
- En la ventana emergente, elige el proceso "Proceso Vacante" de la lista de procesos disponibles.
- Completa los formularios proporcionados con la información requerida. Asegúrate de llenar todos los campos correctamente, incluyendo datos personales del candidato,  experiencia laboral, educación, etc.
- Haz clic en "Start" para iniciar el proceso.
Ahora podrás ver tu tarea en la lista de tareas pendientes en el Tasklist.
