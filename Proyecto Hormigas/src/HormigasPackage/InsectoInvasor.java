/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HormigasPackage;

import java.util.Map;

/**
 *
 * @author tiand
 */
public class InsectoInvasor {
    
    
    private static int hormigasDetenidas = 0;
    private static int hormigasLiberadas = 0;
    private static boolean invasor = false;
    private final Map<String, Thread> soldadoHashMap;

    public InsectoInvasor(Map<String, Thread> soldadoHashMap) {
        this.soldadoHashMap = soldadoHashMap;
       
    }
    
    public synchronized void criasRefugio() {
        try {
            hormigasDetenidas++;
            //System.out.println("Cantidad Crias REFUGIO: " + hormigasDetenidas);
            while (invasor) {
                wait();
            }
            //historico.escribirControlInterrupt("CANTIDAD HILOS SUSPENDIDOS: " + hormigasDetenidas++);
            //historico.escribirControlInterrupt("SOY LIBRE COMO EL VIENTO: " + Thread.currentThread());
            System.out.println("INVASOR REPELIDO: " + Thread.currentThread());
            hormigasLiberadas++;
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread() + " ATAQUE INVASOR");
        } 
    }
    
//    public synchronized void esperarCyclicBarrier(String id_hormiga) {
//        try {
//            
//            // Esperar a que todos los soldados esten LISTOS
//            System.out.println(id_hormiga + " esperando en Cyclicbarrier TRANQUILAMENTE ");
//            
//            //RETIRAR HILO MAIN -1
//            barrier = new CyclicBarrier(soldadoHashMap.size() - 1);
//            barrier.await();
//            System.out.println("COMIENZA CYCLICBARRIER!!!!!");
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Hormiguero.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BrokenBarrierException ex) {
//        }
//    }
    
    public synchronized void invasorActivo() {
        invasor = true;
    }
    
    public synchronized void invasorRepelido() {
        invasor = false;
        System.out.println("CANTIDAD CRIAS EN REFUGIO: " + hormigasDetenidas);
        System.out.println("CANTIDAD CRIAS LIBERADOS DEL REFUGIO: " + hormigasLiberadas);
        notifyAll();
    }
    
}
