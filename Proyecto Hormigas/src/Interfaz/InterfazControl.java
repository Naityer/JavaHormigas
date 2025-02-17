/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.util.ArrayList;

/**
 *
 * @author tiand
 */
public class InterfazControl {

    private final InterfazHormigas interfazHormigas;
    private final StringBuilder sb_bucandoComida = new StringBuilder();
    private final StringBuilder sb_repeliendoInvasor = new StringBuilder();
    private final StringBuilder sb_almacenComida = new StringBuilder();
    private final StringBuilder sb_transportandoComida = new StringBuilder();
    private final StringBuilder sb_instruccion = new StringBuilder();
    private final StringBuilder sb_descansando = new StringBuilder();
    private final StringBuilder sb_zonaComer = new StringBuilder();
    private final StringBuilder sb_refugio = new StringBuilder();
    
    //CONTRUCTOR

    public InterfazControl(InterfazHormigas interfazHormigas) {
        this.interfazHormigas = interfazHormigas;
        interfazHormigas.setVisible(true);
    }
    
    //METODOS
    public synchronized void controladorJText1_BuscandoComida(ArrayList<String> registroRecolectar) {
        sb_bucandoComida.setLength(0);
        for (int i = 0; i < registroRecolectar.size(); i++) {
            sb_bucandoComida.append(registroRecolectar.get(i));
            sb_bucandoComida.append(", ");
        }
        //System.out.println("Buffer RecolectarComida " + sb_bucandoComida + " Size: " + registroRecolectar.size());
        interfazHormigas.getjTextField1().setText(sb_bucandoComida.toString());
    }
    
    public synchronized void controladorJText2_RepeliendoInvasor(ArrayList<String> registroDefender) {
        //sb_repeliendoInvasor.setLength(0);
        for (int i = 0; i < registroDefender.size(); i++) {
            sb_repeliendoInvasor.append(registroDefender.get(i));
            sb_repeliendoInvasor.append(", ");
        }
        //System.out.println("Buffer RepeliendoInvasor " + sb_repeliendoInvasor + " Size: " + registroDefender.size());
        interfazHormigas.getjTextField2().setText(sb_repeliendoInvasor.toString());
    }
    
    public synchronized void controladorJText3_AlmacenComida(ArrayList<String> registroAlmacen) {
        sb_almacenComida.setLength(0);
        for (int i = 0; i < registroAlmacen.size(); i++) {
            sb_almacenComida.append(registroAlmacen.get(i));
            sb_almacenComida.append(", ");
        }
        //System.out.println("Buffer AlmacenComida " + sb_almacenComida + " Size: " + registroAlmacen.size());
        interfazHormigas.getjTextField3().setText(sb_almacenComida.toString());
    }
    
    public synchronized void controladorJText4_transportarZonaComida(ArrayList<String> registroViajarZonaComer) {
        sb_transportandoComida.setLength(0);
        for (int i = 0; i < registroViajarZonaComer.size(); i++) {
            sb_transportandoComida.append(registroViajarZonaComer.get(i));
            sb_transportandoComida.append(", ");
        }
        //System.out.println("Buffer ZonaComer " + sb_transportandoComida + " Size: " + registroViajarZonaComer.size());
        interfazHormigas.getjTextField4().setText(sb_transportandoComida.toString());
    }
    
    public synchronized void controladorJText5_Instruccion(ArrayList<String> registroInstruccion) {
        sb_instruccion.setLength(0);
        for (int i = 0; i < registroInstruccion.size(); i++) {
            sb_instruccion.append(registroInstruccion.get(i));
            sb_instruccion.append(", ");
        }
        //System.out.println("Buffer Intruccion " + sb_instruccion + " Size: " + registroInstruccion.size());
        interfazHormigas.getjTextField5().setText(sb_instruccion.toString());
    }
    
    public synchronized void controladorJText6_Descansando(ArrayList<String> registroDescanso) {
        sb_descansando.setLength(0);
        for (int i = 0; i < registroDescanso.size(); i++) {
            sb_descansando.append(registroDescanso.get(i));
            sb_descansando.append(", ");
        }
        //System.out.println("Buffer Intruccion " + sb_descansando + " Size: " + registroDescanso.size());
        interfazHormigas.getjTextField6().setText(sb_descansando.toString());
    }
    
    public synchronized void controladorJText7_ComidaAlmacen(int almacenComida) {
        interfazHormigas.getjTextField7().removeAll();
        interfazHormigas.getjTextField7().setText(Integer.toString(almacenComida));
    }
    
    public synchronized void controladorJText8_ComidaDisponible(int comidaDisponible) {
        interfazHormigas.getjTextField8().removeAll();
        interfazHormigas.getjTextField8().setText(Integer.toString(comidaDisponible));
    }
    
    public synchronized void controladorJText9_ZonaComer(ArrayList<String> registroComida) {
        sb_zonaComer.setLength(0);
        for (int i = 0; i < registroComida.size(); i++) {
            sb_zonaComer.append(registroComida.get(i));
            sb_zonaComer.append(", ");
        }
        //System.out.println("Buffer Intruccion " + sb_zonaComer + " Size: " + registroComida.size());
        interfazHormigas.getjTextField9().setText(sb_zonaComer.toString());
    }
    
    public synchronized void controladorJText10_Refugio(ArrayList<String> registroRefugio) {
        //sb_refugio.setLength(0);
        for (int i = 0; i < registroRefugio.size(); i++) {
            sb_refugio.append(registroRefugio.get(i));
            sb_refugio.append(", ");
        }
        //System.out.println("Buffer Intruccion " + sb_refugio + " Size: " + registroRefugio.size());
        interfazHormigas.getjTextField10().setText(sb_refugio.toString());
    }

    public StringBuilder getSb_repeliendoInvasor() {
        return sb_repeliendoInvasor;
    }

    public StringBuilder getSb_refugio() {
        return sb_refugio;
    }
    
    
    
}
