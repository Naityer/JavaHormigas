/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.util.Map;

/**
 *
 * @author tiand
 */
public class GeneradorSoldados extends Thread {
    
    private final Map<String, Thread> soldadoHashMap;

    public GeneradorSoldados(Map<String, Thread> soldadoHashMap) {
        
        this.soldadoHashMap = soldadoHashMap;
    }
    
    @Override
    public void run() {
        
        soldadoHashMap.put("GENERADOR SOLDADOS", Thread.currentThread());
    }
}
