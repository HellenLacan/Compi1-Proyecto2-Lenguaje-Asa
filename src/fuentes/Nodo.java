/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuentes;

import java.util.ArrayList;

/**
 *
 * @author Hellen
 */
public class Nodo {

    private String etiqueta;
    private ArrayList<Nodo> hijos =  new ArrayList<>();
    private String valor;
    private int fila;
    private int columna;
    private String tipo;
    
    public Nodo(String etiqueta){
        this.etiqueta = etiqueta;
        this.tipo = "NoTerm";
    }
    
    public Nodo(String etiqueta, int fila, int columna){
        this.etiqueta = etiqueta;
        this.fila = fila;
        this.columna = columna;
        this.tipo = "Term";
    }
    
    public void addHijo(Nodo hijo){
        hijos.add(hijo);
    }

        public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }
}
