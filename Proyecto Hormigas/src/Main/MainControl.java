/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;



import Servidor.Servidor;
import Interfaz.InterfazControl;
import HormigasPackage.Historico;
import HormigasPackage.Hormigas;
import HormigasPackage.Hormiguero;
import HormigasPackage.InsectoInvasor;
import Interfaz.InterfazHormigas;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 *
 * @author tiand
 */
public class MainControl {
    
    public static void main(String[] args) {
        
        Historico historico = new Historico();
        
        Semaphore capacidadAlmacen = new Semaphore(10);
        Semaphore tunelSalirLleno = new Semaphore(2);
        
        Map<String, Thread> obrerasHashMap = new HashMap<>();
        Map<String, Thread> soldadoHashMap = new HashMap<>();
        Map<String, Thread> criasHashMap = new HashMap<>();
        
        InsectoInvasor insectoInvasor = new InsectoInvasor(soldadoHashMap);
        Hormigas hormigas = new Hormigas(historico,insectoInvasor);
        
        InterfazHormigas interfazHormigas = new InterfazHormigas(obrerasHashMap, soldadoHashMap, criasHashMap, hormigas, historico, insectoInvasor);
        InterfazControl interfazControl = new InterfazControl(interfazHormigas);
        Hormiguero hormiguero = new Hormiguero(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
       
        /*Si añadimos Thread_MAIN en el hashmap de INTERRUPCIONES
            - Causa problemas la interrupcion de hilo MAIN, por tanto implementaremos un hilo encargado de inicializar los hilos. Al que podemos manejar
              y realizar operaciones de PARAR Y REANUDAR.
        */
        
        GeneradorHormigas generadorHormigas = new GeneradorHormigas(hormigas, hormiguero, interfazHormigas, obrerasHashMap, soldadoHashMap, criasHashMap, tunelSalirLleno, capacidadAlmacen);
        generadorHormigas.start();
        
        GeneradorSoldados generadorThreadMainSoldados = new GeneradorSoldados(soldadoHashMap);
        generadorThreadMainSoldados.start();
        
        Servidor servidor = new Servidor(hormiguero, interfazHormigas);
        servidor.start();
        
        
    }
}
