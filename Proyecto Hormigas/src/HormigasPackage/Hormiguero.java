/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HormigasPackage;

/**
 *
 * @author tiand
*/

import Interfaz.InterfazHormigas;
import Interfaz.InterfazControl;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hormiguero {

    private final Hormigas hormigas;
    private final InsectoInvasor insectoInvasor;
    
    private final Historico historicoEventos = new Historico();
    
    private final Map<String, Thread> soldadoHashMap;
    private final Map<String, Thread> criasHashMap;
    private final Semaphore tunelSalirLleno;
    
    private static int hormigasInteriorColonia = 0;
    private static int hormigasExteriorColonia = 0;
    
    private final ArrayList<String> registroAlmacen = new ArrayList<>();
    private final ArrayList<String> registroComida = new ArrayList<>();
    private final ArrayList<String> registroInstruccion = new ArrayList<>();
    private final ArrayList<String> registroDescanso = new ArrayList<>();
    private final ArrayList<String> registroRecolectar = new ArrayList<>();
    private final ArrayList<String> registroDefender = new ArrayList<>(); 
    private final ArrayList<String> registroRefugio = new ArrayList<>();
    private final ArrayList<String> registroViajarZonaComer = new ArrayList<>();
    private final InterfazControl interfazControl;
    private final InterfazHormigas interfazHormigas;

    public Hormiguero(InterfazControl interfazControl, Hormigas hormigas, InterfazHormigas interfazHormigas, Map<String, Thread> soldadoHashMap, Map<String, Thread> criasHashMap, Semaphore tunelSalirLleno, InsectoInvasor insectoInvasor) {
        this.interfazControl = interfazControl;
        this.hormigas = hormigas;
        this.interfazHormigas = interfazHormigas;
        this.soldadoHashMap = soldadoHashMap;
        this.criasHashMap = criasHashMap;
        this.tunelSalirLleno = tunelSalirLleno;
        this.insectoInvasor = insectoInvasor;
    }
    
    public void salirHormiguero(String id_hormiga) {
        
        try {
            tunelSalirLleno.acquire();
            // System.out.println("La hormiga " + id_hormiga + " SALE del hormiguero..." + " === Permisos salida " + (tunelSalirLleno.availablePermits()));
            historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("salirHormiguero"));
            Thread.sleep(100);
            hormigasExteriorColonia++;
            hormigasInteriorColonia--;
        } catch (InterruptedException e) {
            //MANJEO DE INTERRUPCIONES
            if(interfazHormigas.isJBottom1_pausar()) {
                    System.out.println("Interrupt PAUSAR - SALIR HOMRIGUERO");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("salir"));
                    hormigas.hilosSupendidos();
            } else if (interfazHormigas.isJBottom3_invasor()) {
                System.out.println("Interrupt INVASOR - SALIR HOMRIGUERO");
                
                if(id_hormiga.contains("S")) {
                    salirHormiguero(id_hormiga);
                    new ZonaExterior().defenderInvasor(id_hormiga);
                } else {
                     new ZonaRefugio().refugio(id_hormiga);
                }

            } else {
                System.out.println(e);
            }
        } finally {
            tunelSalirLleno.release();
        }
    }
    
    public synchronized void viajarZonaComer(String id_hormiga) throws InterruptedException {
            try {
                registroViajarZonaComer.add(id_hormiga);
                interfazControl.controladorJText4_transportarZonaComida(registroViajarZonaComer);
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("viajarZonaComer"));
                Thread.sleep((long) (1000 + Math.random()* 2000));
            } finally {
                registroViajarZonaComer.remove(id_hormiga);
            }
    }

    // ZONAS DEL HORMIGUERO INTERNO
    public class ZonaAlmacen extends Hormiguero {
        
        private static int almacenComida;
        private final Lock controlAlmacen = new ReentrantLock();
        private final Condition vacio = controlAlmacen.newCondition();
        
        public ZonaAlmacen() {
            super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
        }
        
        public void almacenarComida(String id_hormiga) throws InterruptedException{
            // Control permisos - HORMIGAS ZONA ALMACEN
            
            //Comienzo SC
            controlAlmacen.lock();
            try {  
                registroAlmacen.add(id_hormiga);
                interfazControl.controladorJText3_AlmacenComida(registroAlmacen);
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("almacenarComida"));
                historicoEventos.escribirTarea(id_hormiga, "Registro => numHormigas en el almacen: " + registroAlmacen.size() + " almacenComida = " + almacenComida);
                almacenComida =+ 5;
                Thread.sleep((long) (200 + Math.random()* 200));
                almacenComida += 5;
                interfazControl.controladorJText7_ComidaAlmacen(almacenComida);
                vacio.signalAll();
            } finally {
                registroAlmacen.remove(id_hormiga);
                //System.out.println("Registro almacen size: "+ registroAlmacen.size());
                controlAlmacen.unlock();
            }

        }

        public void extraerComida(String id_hormiga) throws InterruptedException {
            // Control permisos - HORMIGAS ZONA ALMACEN
            // Comienzo SC
            controlAlmacen.lock();
            try {
                registroAlmacen.add(id_hormiga);
                interfazControl.controladorJText3_AlmacenComida(registroAlmacen);
                while (almacenComida <= 0) {
                    vacio.await();
                }
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("consumirComida"));
                historicoEventos.escribirTarea(id_hormiga, "Registro => numHormigas en el almacen: " + registroAlmacen.size() + " almacenComida = " + almacenComida);
                // Tiempo de acceso requerido para acceder al almacen
                Thread.sleep((long) (1000 + Math.random()* 1000));
                almacenComida -= 5;
            } finally {
                registroAlmacen.remove(id_hormiga);
                controlAlmacen.unlock();
            }
        }
    }
// =========================================================================================================================
    
    public class ZonaInstruccion extends Hormiguero {

        public ZonaInstruccion() {
            super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
        }
        
        public synchronized void hacerInstruccion(String id_hormiga) {
            try {
                registroInstruccion.add(id_hormiga);
                interfazControl.controladorJText5_Instruccion(registroInstruccion);
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("hacerInstruccion"));
                Thread.sleep((long) (2000 + Math.random()* 6000));
            } catch (InterruptedException e){
                //MANJEO DE INTERRUPCIONES
                if(interfazHormigas.isJBottom1_pausar()) {
                    System.out.println("Interrupt PAUSAR - ZONA INSTRUCCION");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("IInstruccion"));
                    hormigas.hilosSupendidos();
                } else if (interfazHormigas.isJBottom3_invasor()) {
                    System.out.println("Interrupt INVASOR- ZONA INSTRUCCION");
                    //SOLO PUEDEN SER HORMIGAS SOLDADO
                    salirHormiguero(id_hormiga);
                    new ZonaExterior().defenderInvasor(id_hormiga);
                    
                } else {
                    System.out.println(e);
                }
            } finally {
                registroInstruccion.remove(id_hormiga);
            }
        }
    }

// =========================================================================================================================
        
    public class ZonaDescanso extends Hormiguero {

        public ZonaDescanso() {
            super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
        }
        
        public synchronized void descansar(int taskTimeMiliseconds, String id_hormiga) {
            try {
                registroDescanso.add(id_hormiga);
                interfazControl.controladorJText6_Descansando(registroDescanso);
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("descansar"));
                Thread.sleep((taskTimeMiliseconds));
            } catch (InterruptedException e) {
                //MANJEO DE INTERRUPCIONES;
                if(interfazHormigas.isJBottom1_pausar()) {
                    System.out.println("Interrupt PAUSAR - ZONA DESCANSO");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("Idescanso"));
                    hormigas.hilosSupendidos();
                } else if (interfazHormigas.isJBottom3_invasor()) {
                    System.out.println("Interrupt INVASOR - ZONA DESCANSO");
                    
                    System.out.println(id_hormiga);
                    if(id_hormiga.contains("S")) {
                        salirHormiguero(id_hormiga);
                        new ZonaExterior().defenderInvasor(id_hormiga);
                    } else {
                         new ZonaRefugio().refugio(id_hormiga);
                    }

                } else {
                    System.out.println(e);
                }
            } finally {
                registroDescanso.remove(id_hormiga);
            }
        }
    }

// =========================================================================================================================
    public class ZonaComer extends Hormiguero{

        private static int comidaDisponible = 0;
        private final Lock control = new ReentrantLock();
        private final Condition vacio = control.newCondition();
        private final Condition lleno = control.newCondition();

        public ZonaComer() {
            super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
        }
        
        public synchronized void depositarComida(String id_hormiga) throws InterruptedException {
            control.lock();
            registroComida.add(id_hormiga);
            interfazControl.controladorJText9_ZonaComer(registroComida);
            try {
                //Poner MAIXMO aunque no especifique para evitar DEADLOCK, mejora rendimiento y reparto de recursos
                while(comidaDisponible == 10) {
                    lleno.await();
                }
                historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("depositarComida"));
                Thread.sleep((long) (1000 + Math.random()* 1000));
                comidaDisponible += 5;
                interfazControl.controladorJText8_ComidaDisponible(comidaDisponible);
                vacio.signal();
            } finally {
                registroComida.remove(id_hormiga);
                control.unlock();
            }
        }
        
        public synchronized void comer(int taskTimeMiliseconds, String id_hormiga) {
            control.lock();
            registroComida.add(id_hormiga);
            interfazControl.controladorJText9_ZonaComer(registroComida);
            try {
                while(comidaDisponible <= 0) {
                    historicoEventos.escribirTarea(id_hormiga, " se ha quedado sin comer");
                    vacio.await();
                }
                if(id_hormiga.contains("C")) {
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("comerCrias"));
                    Thread.sleep((long) (taskTimeMiliseconds + Math.random()* 2000));
                } else {
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("comer"));
                    Thread.sleep((taskTimeMiliseconds));
                }
                lleno.signal();
            } catch (InterruptedException e) {
                //MANEJO DE EXCEPCIONES
                
                if(interfazHormigas.isJBottom1_pausar()) {
                    System.out.println("Interrupt PAUSAR - ZONA COMER");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("comer"));
                    hormigas.hilosSupendidos();
                    
                } else if (interfazHormigas.isJBottom3_invasor()) {
                    System.out.println("Interrupt INVASOR - ZONA COMER");
                    
                    System.out.println(id_hormiga);
                    if(id_hormiga.contains("S")) {
                        
                        System.out.println("Soy un soldado? " + id_hormiga);
                        salirHormiguero(id_hormiga);
                        new ZonaExterior().defenderInvasor(id_hormiga);
                    } else {
                         new ZonaRefugio().refugio(id_hormiga);
                    }
                    
                } else {
                    System.out.println(e);
                }
            } finally {
                comidaDisponible--;
                registroComida.remove(id_hormiga);
                control.unlock();
            }
        }
    }
// =========================================================================================================================
    
        public class ZonaRefugio extends Hormiguero {

            public ZonaRefugio() {
                super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
            }

            public synchronized void refugio(String id_hormiga) {
                try {
                    
                    registroRefugio.add(id_hormiga);
                    interfazControl.controladorJText10_Refugio(registroRefugio);
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("refugio"));
                    insectoInvasor.criasRefugio();
                } catch (InterruptedException e) {
                    //MANJEO DE INTERRUPCIONES --> La interrupcion solo puede ser de PAUSAR
                    System.out.println("Interrupt PAUSAR - REFUGIO");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("refugio"));
                    hormigas.hilosSupendidos();
                } finally {
                    registroRefugio.remove(id_hormiga);
                    interfazControl.getSb_refugio().setLength(0);
                    interfazControl.controladorJText10_Refugio(registroRefugio);
                }
            }
        }    

// =========================================================================================================================
        public class ZonaExterior extends Hormiguero{
            
            int numHormigasDefensoras;
            private CountDownLatch latch;
            
            public ZonaExterior() {
                super(interfazControl, hormigas, interfazHormigas, soldadoHashMap, criasHashMap, tunelSalirLleno, insectoInvasor);
            }
            
            public synchronized String entrarHormiguero(String id_hormiga) {
                try {
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("entrarHormiguero"));
                    //System.out.println("La hormiga " + id_hormiga + " ENTRANDO al hormiguero...");
                    Thread.sleep(100);
                    hormigasInteriorColonia++;
                    hormigasExteriorColonia--;
                } catch (InterruptedException e) {
                    //MANJEO DE INTERRUPCIONES
                    if(interfazHormigas.isJBottom1_pausar()) {
                        System.out.println("Interrupt PAUSAR - ENTRAR HORMIGUERO");
                        historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("Ientrar"));
                        hormigas.hilosSupendidos();
                    } else if (interfazHormigas.isJBottom3_invasor()) {
                        System.out.println("Interrupt INVASOR - ENTRAR HORMIGUERO");
                        
                        if(id_hormiga.contains("S")) {
                            salirHormiguero(id_hormiga);
                            new ZonaExterior().defenderInvasor(id_hormiga);
                        } else {
                             new ZonaRefugio().refugio(id_hormiga);
                        }
                        
                    } else {
                        System.out.println(e);
                    }
                }
                return null;
            }

            public synchronized void recolectarComida(String id_hormiga) throws InterruptedException {
                try {
                    registroRecolectar.add(id_hormiga);
                    interfazControl.controladorJText1_BuscandoComida(registroRecolectar);
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("recolectarComida"));
                    Thread.sleep(4000);
                } finally {
                    registroRecolectar.remove(id_hormiga);
                }

            }

            public synchronized void defenderInvasor(String id_hormiga) {
                
                //System.out.println(id_hormiga + " se va a esperar en CyclicBarrier");
                
                try {
                    registroDefender.add(id_hormiga);
                    System.out.println(registroDefender.size());
                    interfazControl.controladorJText2_RepeliendoInvasor(registroDefender);
                    historicoEventos.escribirTarea(id_hormiga, historicoEventos.getEventos("defenderHormiguero"));
                    System.out.println("Hormiga " + id_hormiga + " defendiendo el hormiguero");
                    
                    // COMIENZO COUNTDOWN - LACH: Esperar a que todos los soldados esten LISTOS
                    if(numHormigasDefensoras == 0) {
                        //RETIRAR HILO MAIN -1
                        latch = new CountDownLatch((soldadoHashMap.size() - 1));
                        for (int i = 0; i < (soldadoHashMap.size() - 1); i++) {
                            latch.countDown();
                            numHormigasDefensoras++;
                            System.out.println(id_hormiga + " esperando en COUNTDOWN - LACH " + numHormigasDefensoras + "/" + (soldadoHashMap.size() - 1));
                            historicoEventos.escribirTarea(id_hormiga, " esperando en COUNTDOWN - LACH " + numHormigasDefensoras + "/" + (soldadoHashMap.size() - 1));
                            
                        }
                    }
                    latch.await();
                    // FIN COUNTDOWN - LACH
                    System.out.println("COMIENZA COUNTDOWN - LACH!!!!!");
                    historicoEventos.escribirTarea("COUNTDOWN - LACH", "COMIENZA COUNTDOWN - LACH!!!!!");
                    
                    Thread.sleep(20000);
                    InterfazHormigas.setJBottom3_invasor(false);
                    System.out.println("INVASOR REPELIDO ");
                } catch (InterruptedException e) {
                    //MANJEO DE INTERRUPCIONES --> La interrupcion solo puede ser de PAUSAR
                    System.out.println("Interrupt PAUSAR - DEFENDER INVASOR");
                    historicoEventos.escribirControlInterrupt(historicoEventos.getEventosInterrupt("invasor"));
                    hormigas.hilosSupendidos();
                } finally {
                    registroDefender.remove(id_hormiga);
                    interfazControl.getSb_repeliendoInvasor().setLength(0);
                    interfazControl.controladorJText2_RepeliendoInvasor(registroDefender);
                    insectoInvasor.invasorRepelido();
                    entrarHormiguero(id_hormiga);
                }
            }
        }

    public ArrayList<String> getRegistroComida() {
        return registroComida;
    }

    public ArrayList<String> getRegistroInstruccion() {
        return registroInstruccion;
    }

    public ArrayList<String> getRegistroDefender() {
        return registroDefender;
    }

    public ArrayList<String> getRegistroRefugio() {
        return registroRefugio;
    }

    public int getHormigasInteriorColonia() {
        return hormigasInteriorColonia;
    }

    public int getHormigasExteriorColonia() {
        return hormigasExteriorColonia;
    }
    
}


