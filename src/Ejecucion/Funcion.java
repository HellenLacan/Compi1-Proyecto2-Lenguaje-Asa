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
    
    private String nombre;
    private String tipo;
    private int fila;
    private int columna;
    private ArrayList<Parametro> parametros =  new ArrayList<>();
    private Nodo cuerpo;
    private String key;
    private String valorRetorno;
    int noParametros;

    public Funcion(String nombre, String tipo, int fila, int columna, Nodo cuerpo, String valorRet) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.cuerpo = cuerpo;
        this.valorRetorno = valorRet;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Nodo getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(Nodo cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<Parametro> parametros) {
        this.parametros = parametros;
    }

    public String getValorRetorno() {
        return valorRetorno;
    }

    public void setValorRetorno(String valorRetorno) {
        this.valorRetorno = valorRetorno;
    }
    
    
    
}
