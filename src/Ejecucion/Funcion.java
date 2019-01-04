/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import fuentes.Nodo;
import java.util.ArrayList;

/**
 *
 * @author Hellen
 */
public class Funcion {
    
    String nombre;
    String tipo;
    int fila;
    int columna;
    private ArrayList<Parametro> parametros =  new ArrayList<>();
    Nodo cuerpo;
    String key;
    int noParametros;

    public Funcion(String nombre, String tipo, int fila, int columna, Nodo cuerpo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.cuerpo = cuerpo;
    }
    
    public String crearLlave(){
        return "";
    }
    
    public String existeParametro(String tipo, String nombre){
        return "";
    }
    
    public void agregarParametro(String tipo, String nombre, int fila, int columna){
        parametros.add(new Parametro(tipo, nombre, fila, columna));
    }
    
    public int getNoParametros() {
        return parametros.size();
    }
    
    
    
}
