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
public class TablaFunciones {

    Hashtable<String, Funcion> tabla = new Hashtable<String, Funcion>();

    public TablaFunciones() {
    }

    public void insertar(String key, Funcion funcion) {
        tabla.put(key, funcion);
    }

    public Funcion retornarFuncion(String key) {
        return tabla.get(key);
    }

    public boolean existeFuncion(String key) {
        if (tabla.containsKey(key)) {
            return true;
        }
        return false;
    }

    public void mostrarTS() {
        Enumeration e = tabla.keys();
        Object clave;
        Funcion valor;
        while (e.hasMoreElements()) {
            clave = e.nextElement();
            valor = tabla.get(clave);
            System.out.println("Tipo: " + valor.getTipo() + " id: " + valor.getNombre()
                    + " Fila: " + valor.getFila() + " Columna: " + valor.getColumna() + " No param: " + valor.getNoParametros());
        }
    }
}
