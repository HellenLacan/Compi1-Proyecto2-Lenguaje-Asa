/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import fuentes.Nodo;

/**
 *
 * @author Hellen
 */
public class DeclaracionVar {

    public static void recorrerListaVars(Nodo tipo, Nodo nodo, Nodo exp, String tipoAmbito) {
        switch (nodo.getHijos().size()) {
            case 1:
                agregarVariables(tipo, nodo.getHijos().get(0), exp, tipoAmbito);
                break;
            case 2:
                recorrerListaVars(tipo, nodo.getHijos().get(0), exp, tipoAmbito);
                agregarVariables(tipo, nodo.getHijos().get(1), exp, tipoAmbito);
                break;
        }
    }

    public static void agregarVariables(Nodo t, Nodo id, Nodo nodo, String tipoAmbito) {
        TablaSimbolo ts = null;
        String tipo = t.getEtiqueta();
        String nombre = id.getEtiqueta();
        int linea = id.getFila();
        int columna = id.getColumna();
        String expresion = "";

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            if (EjecucionLenguajeAsa.pilaSimbolos.empty()) {
                TablaSimbolo tsMain = new TablaSimbolo("ambitoMain");
                ts = tsMain;
            } else if (EjecucionLenguajeAsa.pilaSimbolos.size() == 1) {
                ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
            }
        } else {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        }

        if (nodo.getHijos().get(0) != null) {
            //Si la variable viene con expresiones en el valor = 10+10; 

            if (nodo.getHijos().get(0).getHijos().size() > 0) {
                if (!ts.existeSimbolo(nombre)) {
                    expresion = EjecucionLenguajeAsa.evaluarExpresion(nodo.getHijos().get(0), tipoAmbito);
                    AsignacionVars.agregarAsignacionAVars(expresion, tipo, nombre, linea, columna, tipoAmbito);
                } else {
                    System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente"
                            + " linea:" + linea + " columna: " + columna);
                }
                //Si la variable viene solo con un terminal en la expresion en el valor = 10, valor = falso; 
            } else {
                if (!ts.existeSimbolo(nombre)) {
                    expresion = EjecucionLenguajeAsa.evaluarExpresion(nodo.getHijos().get(0), tipoAmbito);
                    AsignacionVars.agregarAsignacionAVars(expresion, tipo, nombre, linea, columna, tipoAmbito);
                } else {
                    System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente"
                            + " linea:" + linea + " columna: " + columna);
                }
            }
            //Si la variable viene solo con un terminal como  valor Entero = a;
        } else {
            if (!ts.existeSimbolo(nombre)) {
                Simbolo simb = new Simbolo(tipo, nombre, "", linea, columna, "");
                ts.insertar(nombre, simb);
            } else {
                System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente"
                        + " linea:" + linea + " columna: " + columna);
            }
        }

        //PILA DE SIMBOLOS
        if (EjecucionLenguajeAsa.pilaSimbolos.empty() && !tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            EjecucionLenguajeAsa.pilaSimbolos.push(ts);
        }
    }
}
