/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import HormigasPackage.Hormigas;
import HormigasPackage.Hormiguero;
import Interfaz.InterfazHormigas;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 *
 * @author tiand
 */
public class GeneradorHormigas extends Thread{
    
    private final Hormigas hormigas;
    private final Hormiguero hormiguero;
    private final InterfazHormigas interfazHormigas;
    private static boolean init = true;
    
    private final Map<String, Thread> obrerasHashMap;
    private final Map<String, Thread> soldadoHashMap;
    private final Map<String, Thread> criasHashMap;
    
    private final Semaphore tunelSalirLleno;
    private final Semaphore capacidadAlmacen;
    
    private static int id_obreras = 0, id_soldado = 0, id_crias = 0;

    public GeneradorHormigas(Hormigas hormigas, Hormiguero hormiguero, InterfazHormigas interfazHormigas, Map<String, Thread> obrerasHashMap, Map<String, Thread> soldadoHashMap, Map<String, Thread> criasHashMap, Semaphore tunelSalirLleno, Semaphore capacidadAlmacen) {
        this.hormigas = hormigas;
        this.hormiguero = hormiguero;
        this.interfazHormigas = interfazHormigas;
        this.obrerasHashMap = obrerasHashMap;
        this.soldadoHashMap = soldadoHashMap;
        this.criasHashMap = criasHashMap;
        this.tunelSalirLleno = tunelSalirLleno;
        this.capacidadAlmacen = capacidadAlmacen;
    }
    
    @Override
    public void run() {
        
        if(init) {
            obrerasHashMap.put("GENERADOR HORMIGAS", Thread.currentThread());
            init = false;
        }
        //criasHashMap.put("GENERADOR HORMIGAS", Thread.currentThread());
        
        while (!Thread.interrupted()) {
            try {
                for (int i = 0; i < 2000; i++) { //2000
                    Thread.sleep((long) (800 + Math.random()*2700));
                    for (int j = 0; j < 3; j++) { 
                        hormigas.new Obreras(String.format("HO%04d", id_obreras), capacidadAlmacen, tunelSalirLleno, obrerasHashMap, hormiguero).start();
                        id_obreras++;
                    }
                    // CANTIDAD HORMIGAS - OBRERAS
                    //System.out.println("id_obreras " + id_obreras);
                    
                    hormigas.new Soldado(String.format("HS%04d", id_soldado), tunelSalirLleno, soldadoHashMap, hormiguero).start();    
                    id_soldado++;

                    // CANTIDAD HORMIGAS - CRIAS
                    hormigas.new Crias(String.format("HC%04d", id_crias), criasHashMap, hormiguero, interfazHormigas).start();
                    id_crias++;
                    //System.out.println("id_crias " + id_crias);

                    //TOTAL HORMIGAS EN EJECUCION
                    //System.out.println("TOTAL HORMGIAS = " + (id_obreras + id_soldado + id_crias));
                }
            } catch (InterruptedException e) {
                //MANJEO DE INTERRUPCIONES
                if(interfazHormigas.isJBottom1_pausar()) {
                    System.out.println("Interrupt - GENERADOR HORMIGAS");
                    hormigas.hilosSupendidos();
                } else if (interfazHormigas.isJBottom3_invasor()) {
                    System.out.println("Interrupt - INVASOR");
                    for(String key: soldadoHashMap.keySet()) {
                            hormiguero.salirHormiguero(key);
                            hormiguero.new ZonaExterior().defenderInvasor(key);
                        }

                    for(String key: criasHashMap.keySet()) {
                        hormiguero.new ZonaRefugio().refugio(key);
                    }
                } else {
                    System.out.println(e);
                }
            }
        }
    }
}
