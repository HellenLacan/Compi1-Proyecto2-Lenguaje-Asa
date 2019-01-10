/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/**
 *
 * @author Hellen
 */
public class TablaSimbolo {

    public String ambito;
    public String tipo;
    Hashtable<String, Simbolo> tabla = new Hashtable<String, Simbolo>();

    /*Esta la usare para cuando vengan declaraciones dentro de una funcion, 
      esta sera mi pila de la sentencia en la funcion actual*/
    public static Stack<TablaSimbolo> pilaInterna = new Stack<>();

    public TablaSimbolo(String ambito) {
        this.ambito = ambito;
    }

    public TablaSimbolo( TablaSimbolo t) {
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

    public  TablaSimbolo(  Hashtable<String, Simbolo> t) {
         Hashtable<String, Simbolo> tabla = new Hashtable<String, Simbolo>(t);
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
