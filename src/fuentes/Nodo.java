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
    private String Etiqueta;
    private ArrayList<Nodo> hijos =  new ArrayList<>();
    private String valor;
    private int idNodo;
    private String Separador;
    
    public void addHijo(Nodo hijo){
        hijos.add(hijo);
    }

    public String getEtiqueta() {
        return Etiqueta;
    }

    public void setEtiqueta(String Etiqueta) {
        this.Etiqueta = Etiqueta;
    }

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(int idNodo) {
        this.idNodo = idNodo;
    }

    public String getSeparador() {
        return Separador;
    }

    public void setSeparador(String Separador) {
        this.Separador = Separador;
    }

}
