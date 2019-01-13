/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import fuentes.Nodo;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/**
 *
 * @author Hellen
 */
public class DeclaracionVar {

    public static void recorrerListaVars(Nodo tipo, Nodo nodo, Nodo exp, String tipoAmbito) throws FileNotFoundException, UnsupportedEncodingException {
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

    public static void agregarVariables(Nodo t, Nodo id, Nodo nodo, String tipoAmbito) throws FileNotFoundException, UnsupportedEncodingException {
        TablaSimbolo ts = null;
        String tipo = t.getEtiqueta();
        String nombre = id.getEtiqueta();
        int linea = id.getFila();
        int columna = id.getColumna();
        String expresion = "";
        String funcion = "";
        String sentencia = "";

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        } else {
            String[] ambito = tipoAmbito.split("@");
            if (tipoAmbito.contains("@")) {
                funcion = ambito[0];
                sentencia = ambito[1];
            }
            if (tipoAmbito.equalsIgnoreCase(EjecucionLenguajeAsa.pilaSimbolos.peek().ambito)) {
                ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
            } else {
                TablaSimbolo tsTemp = new TablaSimbolo(tipoAmbito);
                //Verifico si la sentencia que viene pertenece a la misma funcion,
                if (EjecucionLenguajeAsa.pilaSimbolos.peek().ambito.equalsIgnoreCase(funcion)) {

                    Hashtable<?, ?> tabla = EjecucionLenguajeAsa.pilaSimbolos.peek().tabla;

                    Enumeration e = tabla.keys();
                    Object clave;
                    Simbolo valor;
                    while (e.hasMoreElements()) {
                        clave = e.nextElement();
                        valor = (Simbolo) tabla.get(clave);
                        String tipo1 = valor.getTipo();
                        String nombre1 = valor.getNombre();
                        String valor1 = valor.getValor();
                        int linea1 = valor.getLinea();
                        int columna1 = valor.getColumna();

                        Simbolo newVar = new Simbolo(tipo1, nombre1, valor1, linea1, columna1, "");
                        tsTemp.insertar(valor.getNombre(), newVar);
                    }

                }
                EjecucionLenguajeAsa.pilaSimbolos.push(tsTemp);
                ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
            }
            //ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        }

        //Si la variable viene con expresion
        if (nodo.getHijos().get(0) != null) {
            if (!ts.existeSimbolo(nombre)) {
                expresion = EjecucionLenguajeAsa.evaluarExpresion(nodo.getHijos().get(0), tipoAmbito);
                AsignacionVars.agregarAsignacionAVars(expresion, tipo, nombre, linea, columna, tipoAmbito);
            } else {

                System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente"
                        + " linea:" + linea + " columna: " + columna);
            }

            //Si la variable viene sin expresion
        } else {
            if (!ts.existeSimbolo(nombre)) {
                Simbolo simb = new Simbolo(tipo, nombre, "", linea, columna, "");

                if (tipoAmbito.equalsIgnoreCase("ambitoGlobal") || tipoAmbito.equalsIgnoreCase("ambitoMain")) {
                    ts.insertar(nombre, simb);
                    //Insertando la variable en el ambito actual de la funcion
                } else if (!EjecucionLenguajeAsa.tsVarsFuncion.existeSimbolo(nombre)) {
                    ts.insertar(nombre, simb);
                    EjecucionLenguajeAsa.tsVarsFuncion.insertar(nombre, simb);
                } else {
                    System.out.println("Variable ya declarada en esta funcion");
                }

                //EjecucionLenguajeAsa.pilaAuxVarsFuncion.push(ts);
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
