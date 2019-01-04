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

    public Funcion retornarFuncion() {
        return null;
    }

    public boolean existeFuncion(String key) {
        if (tabla.containsKey(key)) {
            return true;
        }
        return false;
    }

    public void mostrarTabla() {
        java.util.Enumeration claves = tabla.keys();
        while (claves.hasMoreElements()) {
            Object clave = claves.nextElement();
            Object valor = tabla.get(clave);
            System.out.println("Clave: " + clave.toString() + ", Funcion:  " + valor.toString());
        }
    }
}
