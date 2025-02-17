/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HormigasPackage;

import Interfaz.InterfazHormigas;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 *
 * @author tiand
 */
public class Hormigas {
    
    private static boolean suspendido = false;
    private static int hormigasDetenidas = 0;
    private static int hormigasLiberadas = 0;
    private final Historico historico;
    private final InsectoInvasor insectoInvasor;

    public Hormigas(Historico historico, InsectoInvasor insectoInvasor) {
        this.historico = historico;
        this.insectoInvasor = insectoInvasor;
    }
    
    public class Obreras extends Thread {
    
        private final String id_obreras;
        private final String tareaInicial;
        private final Semaphore capacidadAlmacen;
        private final Semaphore tunelSalirLleno;
        private final Map<String, Thread> obrerasHashMap;
        private final Hormiguero hormiguero;
        

        public Obreras(String id_obreras, Semaphore capacidadAlmacen, Semaphore tunelSalirLleno, Map<String, Thread> obrerasHashMap, Hormiguero hormiguero) {
                this.id_obreras = id_obreras;
                this.capacidadAlmacen = capacidadAlmacen;
                this.tunelSalirLleno = tunelSalirLleno;
                this.tareaInicial = hormiguero.new ZonaExterior().entrarHormiguero(id_obreras);
                this.obrerasHashMap = obrerasHashMap;
                this.hormiguero = hormiguero;
        }

        @Override
        public void run() {
            // Clave - Valor "NOTA no ponerlo en el constructor porque sino tiene como valor MAIN"
            obrerasHashMap.put(id_obreras, Thread.currentThread());

            while(!Thread.interrupted()) {
                for (int i = 0; i < 10; i++) {
                    /*
                    ID Hormigas PARES repetiran iterativamente:
                            1.- intento acceso (1-2s)
                            2.- viajarZonaComer (1-3s)
                            3.- DepositarComida +5 elemntos de comida disponibles
                     */
                    if((Integer.parseInt(id_obreras.substring(id_obreras.length() - 4)) % 2) == 0) {
                        try {
                        // Obtener permiso en almacen - SEMAFORO 
                            capacidadAlmacen.acquire();
                            //System.out.println("La hormiga PAR " + id_obreras + " adquirio un permiso" + " === Permisos disponibles " + (capacidadAlmacen.availablePermits()));
                            hormiguero.new ZonaAlmacen().extraerComida(id_obreras);
                            //System.out.println("La hormiga " + id_obreras + " ha completado su tarea y libera el permiso");
                            capacidadAlmacen.release();
                        // Liberar permiso
                            hormiguero.viajarZonaComer(id_obreras);
                            hormiguero.new ZonaComer().depositarComida(id_obreras);
                        } catch (InterruptedException e) {
                            //MANJEO DE INTERRUPCIONES;
                            System.out.println("Interrupt PAUSAR - OBRERA PAR");
                            //historico.escribirControlInterrupt(historico.getEventosInterrupt("interruptOPAR"));
                            hilosSupendidos();
                        }

                    /*
                    ID Hormigas IMPARES repetiran iterativamente:
                            1.- intento acceso (1-2s)
                            2.- viajarZonaComer (1-3s)
                            3.- DepositarComida +5 elemntos de comida disponibles
                     */
                    } else{

                        try {
                            hormiguero.salirHormiguero(id_obreras);
                            hormiguero.new ZonaExterior().recolectarComida(id_obreras);
                            hormiguero.new ZonaExterior().entrarHormiguero(id_obreras);
                        // Obtener permiso en almacen - SEMAFORO    
                            capacidadAlmacen.acquire();
                            //System.out.println("La hormiga IMPAR " + id_obreras + " adquirio un permiso" + " === Permisos disponibles " + (capacidadAlmacen.availablePermits()));
                            hormiguero.new ZonaAlmacen().almacenarComida(id_obreras);
                            //System.out.println("La hormiga " + id_obreras + " ha completado su tarea y libera el permiso");
                            capacidadAlmacen.release();
                        //Liberar permiso
                        } catch (InterruptedException e) {
                            //MANJEO DE INTERRUPCIONES;
                            System.out.println("Interrupt PAUSAR - OBRERA IMPAR");
                            //historico.escribirControlInterrupt(historico.getEventosInterrupt("interruptOIMPAR"));
                            hilosSupendidos();
                    }
                }
                // Tareas comunes
                hormiguero.new ZonaComer().comer(3000, id_obreras);
                hormiguero.new ZonaDescanso().descansar(1000, id_obreras);

                }
                hilosSupendidos();
            }
        }
    }
    
// =========================================================================================================================
    
        public class Soldado extends Thread {

        private final String id_soldado;
        private final String tareaSoldado;
        private final Semaphore tunelSalirLleno;
        private final Map<String, Thread> soldadoHashMap;
        private final Hormiguero hormiguero;

        public Soldado(String id_soldado, Semaphore tunelSalirLleno, Map<String, Thread> soldadoHashMap, Hormiguero hormiguero) {
            this.id_soldado = id_soldado;
            this.tunelSalirLleno = tunelSalirLleno;
            this.tareaSoldado = hormiguero.new ZonaExterior().entrarHormiguero(id_soldado);
            this.soldadoHashMap = soldadoHashMap;
            this.hormiguero = hormiguero;
        }

        @Override
        public void run() {
            soldadoHashMap.put(id_soldado, Thread.currentThread());
            while(!Thread.interrupted()) {
                for (int i = 0; i < 6; i++) {
                    hormiguero.new ZonaInstruccion().hacerInstruccion(id_soldado);
                    hormiguero.new ZonaDescanso().descansar(2000, id_soldado);
                }
                hormiguero.new ZonaComer().comer(3000, id_soldado);
                }
            }

        }  
    
// =========================================================================================================================
    
    public class Crias extends Thread {

        private final String id_crias;
        private final String tareaCrias;
        private final Map<String, Thread> criasHashMap;
        private final Hormiguero hormiguero;
        private final InterfazHormigas interfazHormigas;

        public Crias(String id_crias, Map<String, Thread> criasHashMap, Hormiguero hormiguero, InterfazHormigas interfazHormigas) {
            this.id_crias = id_crias;
            this.tareaCrias = hormiguero.new ZonaExterior().entrarHormiguero(id_crias);
            this.criasHashMap = criasHashMap;
            this.hormiguero = hormiguero;
            this.interfazHormigas =  interfazHormigas;
        }

        @Override
        public void run() {

            criasHashMap.put(id_crias, Thread.currentThread());
            while (interfazHormigas.isJBottom3_invasor()) {
                hormiguero.new ZonaRefugio().refugio(id_crias);
            }

            while(!Thread.interrupted()) {
                hormiguero.new ZonaComer().comer(3000, id_crias); 
                hormiguero.new ZonaDescanso().descansar(4000, id_crias);
            }

        }
    }
    
    
// =========================================================================================================================    
    
    public synchronized void hilosSupendidos() {
        try {
            //HORMIGAS DETENIDAS
            //System.out.println("Hormigas detenidas " + hormigasDetenidas++);
            
            //Control .txt de la ejecucion de las interrupciones
            //historico.escribirControlInterrupt("CANTIDAD HILOS SUSPENDIDOS: " + hormigasDetenidas++);
            //historico.escribirControlInterrupt("HILO: " + Thread.currentThread() + " isInterrupted: " + Thread.currentThread().isInterrupted());
            //historico.escribirControlInterrupt("VALOR SUSPENDIDO = " + suspendido);
            
            System.out.println("VALOR SUSPENDIDO = " + suspendido);
            hormigasDetenidas++;
            while (suspendido) {
                wait();
            }
            //historico.escribirControlInterrupt("CANTIDAD HILOS SUSPENDIDOS: " + hormigasDetenidas++);
            //historico.escribirControlInterrupt("SOY LIBRE COMO EL VIENTO: " + Thread.currentThread());
            System.out.println("SOY LIBRE COMO EL VIENTO: " + Thread.currentThread());
            hormigasLiberadas++;
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread() + " detenido");
        } 
            
    }

    public void detener() {
        suspendido = true;
    }

    public synchronized void reanudar() {
       suspendido = false;
       System.out.println("CANTIDAD HILOS SUSPENDIDOS: " + hormigasDetenidas);
       System.out.println("CANTIDAD HILOS LIBERADOS: " + hormigasLiberadas);
       notifyAll();
    }

    public static boolean isSuspendido() {
        return suspendido;
    }

}
