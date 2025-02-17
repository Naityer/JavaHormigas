/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import HormigasPackage.Hormiguero;
import Interfaz.InterfazHormigas;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiand
 */
public class Servidor extends Thread {
    
    private ServerSocket servidor;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Socket conexion;
    private static int numConexiones = 0;
    private final Map<String, String> mensajes = new HashMap<>();
    private final Hormiguero hormiguero;
    private final InterfazHormigas interfazHormigas;
    private String insectoInvasor;

    public Servidor(Hormiguero hormiguero, InterfazHormigas interfazHormigas) {
        this.hormiguero = hormiguero;
        this.interfazHormigas = interfazHormigas;
    }
    
    @Override
    public void run() {
        try { 
            iniciarServidor();
            while (numConexiones > 0) {
                Thread.sleep(1000);
                enviarMensajes();
                //recibirMensajes();
            }
            
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } 
    }
    public void iniciarServidor() {
        try {
                servidor = new ServerSocket(5000);   //Creamos el socket para conectarnos al puerto 5000 del servidor
                System.out.println("Servidor Arrancado....");
                conexion = servidor.accept();     // Esperamos una conexión
                numConexiones++;
                System.out.println("Conexión nº " + numConexiones + " desde: " + conexion.getInetAddress().getHostName());
                entrada = new DataInputStream(conexion.getInputStream());  // Abrimos los canales de E/S
                salida = new DataOutputStream(conexion.getOutputStream());
                String mensaje = entrada.readUTF();
                System.out.println(mensaje + " ha entrado al servidor");
        } catch(IOException ex) {
        }
    }
    
    public void enviarMensajes() {
        
        mensajes.put("homigasIntruccion", String.valueOf(hormiguero.getRegistroInstruccion().size()));
        mensajes.put("hormigasInvasion", String.valueOf(hormiguero.getRegistroDefender().size()));
        mensajes.put("hormigasZonaComer", String.valueOf(hormiguero.getRegistroComida().size()));
        mensajes.put("hormigasRefugio", String.valueOf(hormiguero.getRegistroRefugio().size()));
        mensajes.put("hormigasExteriorColonia", String.valueOf(Math.abs(hormiguero.getHormigasExteriorColonia())));
        mensajes.put("hormigasInteriorColonia", String.valueOf(Math.abs(hormiguero.getHormigasInteriorColonia())));
        
        
        try{
            
            for(String key: mensajes.keySet()) {
                //MENSAJES ENVIADOS
                salida.writeUTF(key + " : " + mensajes.get(key));
                System.out.println("MENSAJE: " + key + " = " + mensajes.get(key));
            }
        } catch (IOException e) {
            
        }
    }
    
    
//    public void recibirMensajes() {
//        try {  
//            entrada = new DataInputStream(conexion.getInputStream());
//            String mensaje = entrada.readUTF();
//            System.out.println(mensaje);
//        } catch (IOException ex) {
//            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//           
//    }

    public int getNumConexiones() {
        return numConexiones;
    }
    
    
}
