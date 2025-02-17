/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HormigasPackage;

/**
 *
 * @author tiand
 */
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Historico {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock interrupt = new ReentrantLock();
    private final Lock write = lock.writeLock();
    private final File archivo = new File("evolucionColonia.txt");
    private final File controlInterrupt = new File("controlInterrupt.txt");
    private final LocalDateTime now = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
    private final String formattedDateTime = now.format(formatter);
    
    private final Map<String, String> eventos = new HashMap<>() {{
        put("recolectarComida", " se encuentra recolectando 5 elementos de comida");
        put("almacenarComida", " almacena +5 de comida");
        put("consumirComida", " transportando 5 elementos de comida a la zona para comer...");
        put("depositarComida", " + 5 elementos de comida disponibles");
        put("entrarHormiguero", " esta entrando al hormiguero");
        put("comer", " consume -1 unidad de comida");
        put("descansar", " descansando profundamente... ");
        put("hacerInstruccion", " esta siendo instruida");
        put("viajarZonaComer", " esta tranportando +5 unidades de comida al comedor");
        put("comerCrias", " una cria tarda entre 3-5 (s) en comer ");
        put("defenderHormiguero", " se encuentra defendiendo el hormiguero valientemente");
        put("salirHormiguero", " esta saliendo del hormiguero");
        put("refugio", " se encuentra en el REFUGIO");
    }};
    
    private final Map<String, String> eventosInterrupt = new HashMap<>() {{
        put("interruptM", " Interrupcion - MAIN");
        put("interruptS", " Interrupcion SOLDADO");
        put("interruptOComun", " Interrupcion OBRERAS");
        put("interruptOPAR", " Interrupcion OBRERAS");
        put("interruptOIMPAR", " Interrupcion IMPAR");
        put("interuptC", " Interrupcion CRIAS");
        put("jButton1_pausar", " jButton1_pausar: ");
        put("jButton3_invasor", " jButton3_invasor: ");
        put("Ientrar", " Interrupt PAUSAR - ENTRAR HORMIGUERO ");
        put("Idescanso", " Interrupt PAUSAR - ZONA DESCANSO ");
        put("IInstruccion", " Interrupt PAUSAR - ZONA INSTRUCCION ");
        put("comer", " Interrupt PAUSAR - ZONA COMER");
        put("salir", " Interrupt PAUSAR - SALIR HOMRIGUERO");
        put("invasor", " Interrupt PAUSAR - DEFENDER INVASOR");
        put("refugio", " Interrupt PAUSAR - REFUGIO");
        
    }};
    
    public void escribirControlInterrupt(String tarea) {
        interrupt.lock();
        try {
            FileWriter escritor = new FileWriter(controlInterrupt, true);
            BufferedWriter bufferEscritor = new BufferedWriter(escritor);
            bufferEscritor.write(tarea);
            bufferEscritor.newLine();
            bufferEscritor.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            interrupt.unlock();
        }
    }

    public void escribirTarea(String id_hormiga, String tarea) throws InterruptedException {
        read.lock();
        try {
            FileWriter escritor = new FileWriter(archivo, true);
            BufferedWriter bufferEscritor = new BufferedWriter(escritor);
            if(tarea.contains("Registro")) {
                bufferEscritor.write(tarea + " ==> " + formattedDateTime);
            } else {
                bufferEscritor.write("La hormiga " + id_hormiga + tarea + " ==> " + formattedDateTime);
            }
            bufferEscritor.newLine();
            bufferEscritor.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            read.unlock();
        }
    }

//    public void leerTarea() {
//        write.lock();
//        try {
//            FileReader lector = new FileReader(archivo);
//            BufferedReader bufferLector = new BufferedReader(lector);
//            String linea;
//            while ((linea = bufferLector.readLine()) != null) {
//                System.out.println(linea);
//            }
//            bufferLector.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            write.unlock();
//        }
//    }

    public String getEventos(String evento) {
        return eventos.get(evento);
    }

    public String getEventosInterrupt(String evento) {
        return eventosInterrupt.get(evento);
    }
    
    

}

