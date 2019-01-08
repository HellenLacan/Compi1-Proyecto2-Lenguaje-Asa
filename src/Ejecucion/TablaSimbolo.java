/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Hellen
 */
public class TablaSimbolo {

    public String ambito;
    public String tipo;
    Hashtable<String, Simbolo> tabla = new Hashtable<String, Simbolo>();

    public TablaSimbolo(String ambito) {
        this.ambito = ambito;
    }

    public TablaSimbolo(String ambito, String tipo) {
        this.ambito = ambito;
    }

    public void insertar(String nombre, Simbolo simbolo) {
        //Ya que el resultado de las operaciones devuelve 0,0
        if (simbolo.getTipo().equalsIgnoreCase("Decimal")) {
            if (simbolo.getValor() != null) {
                simbolo.setValor(simbolo.getValor().replace(",", "."));
            }
        }
        tabla.put(nombre, simbolo);
    }

    public Simbolo retornarSimbolo(String key) {
        return tabla.get(key);
    }

    public Boolean existeSimbolo(String key) {
        if (tabla.containsKey(key)) {
            return true;
        }
        return false;
    }

    public void mostrarTS() {
        Enumeration e = tabla.keys();
        Object clave;
        Simbolo valor;
        while (e.hasMoreElements()) {
            clave = e.nextElement();
            valor = tabla.get(clave);
            System.out.println("Tipo: " + valor.getTipo() + " id: " + valor.getNombre() + " valor: " + valor.getValor()
                    + " Fila: " + valor.getLinea() + " Columna: " + valor.getColumna());
        }
    }

}
