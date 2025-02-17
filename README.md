# Proyecto: Simulación de Hormiguero en Programación Concurrente y Distribuida

Este proyecto es una simulación de un hormiguero utilizando técnicas de programación concurrente y distribuida. Implementa sincronización entre hilos y comunicación cliente-servidor para modelar el comportamiento de una colonia de hormigas.

## Características Principales

- **Programación Concurrente:**
  - Se generan 10,000 hormigas divididas en obreras, soldados y crías.
  - Uso de hilos para simular el comportamiento de cada hormiga.
  - Sincronización con `synchronized`, `Semaphore`, `CountDownLatch`, `Lock` y `Condition`.
  
- **Estructura del Hormiguero:**
  - Diferentes zonas con acceso restringido por sincronización.
  - Soldados protegen contra insectos invasores.
  - Crías se refugian ante amenazas.
  
- **Programación Distribuida:**
  - Implementación de Cliente-Servidor mediante `Sockets`.
  - El cliente recibe información sobre el estado del hormiguero.
  - Puede generar eventos como la aparición de un insecto invasor.
  
- **Interfaz Gráfica:**
  - Implementada en Java Swing.
  - Permite visualizar el estado de la colonia en tiempo real.
  - Botones para pausar/reanudar y generar eventos.

## Tecnologías Utilizadas

- **Lenguaje:** Java
- **Frameworks/Librerías:** Java Swing, Java Concurrency Utilities
- **Herramientas de Sincronización:** `Semaphore`, `Lock`, `CountDownLatch`, `Condition`
- **Redes:** `Sockets` para comunicación Cliente-Servidor

## Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/usuario/SimulacionHormiguero.git
   ```
2. **Compilar el código:**
   ```bash
   javac -d bin src/**/*.java
   ```
3. **Ejecutar el servidor:**
   ```bash
   java -cp bin Servidor
   ```
4. **Ejecutar el cliente:**
   ```bash
   java -cp bin Cliente
   ```
5. **Ejecutar la interfaz gráfica:**
   ```bash
   java -cp bin InterfazHormigas
   ```

## Autores
- **Tian Duque Rey**

