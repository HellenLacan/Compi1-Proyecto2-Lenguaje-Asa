/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import EjecucionExpresiones.ExpresionAritmetica;
import Ejecucion.PalabraReservada.Reservada;
import fuentes.Nodo;
import java.util.Stack;

/**
 *
 * @author Hellen
 */
public class EjecucionLenguajeAsa {

    Stack<TablaSimbolo> pilaSimbolos = new Stack<TablaSimbolo>();
    public static TablaSimbolo tsGlobal = new TablaSimbolo();  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
    public static TablaFunciones tsFunciones = new TablaFunciones(); //Tabla para almacenar funciones, metodos y principal
    public Nodo AST;

    public EjecucionLenguajeAsa() {
    }

    public void almacenarVariablesGlobales(Nodo nodo) {
        switch (nodo.getEtiqueta()) {
            case "CUERPO_PRINCIPAL":
                for (Nodo item : nodo.getHijos()) {
                    almacenarVariablesGlobales(item);
                }
                break;
            case "DECLARACION_VARIABLES":
                agregarVariablesGlobales(nodo);
                break;
            case "ASIGNACIONES":
                asignacionAVariablesGlobales(nodo);
                break;
            default:
        }
    }

    public void agregarVariablesGlobales(Nodo nodo) {
        String tipo = nodo.getHijos().get(0).getEtiqueta();
        String nombre = nodo.getHijos().get(1).getHijos().get(0).getEtiqueta();
        int linea = nodo.getHijos().get(1).getHijos().get(0).getFila();
        int columna = nodo.getHijos().get(1).getHijos().get(0).getColumna();
        String expresion = "";

        //Si la variable viene declara sin valor -> Entero a;
        if (nodo.getHijos().size() == 2) {
            if (!tsGlobal.existeSimbolo(nombre)) {
                Simbolo simb = new Simbolo(tipo, nombre, "", linea, columna, "");
                tsGlobal.insertar(nombre, simb);
            } else {
                System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente");
            }
        } else {
            //Si la variable viene con expresiones en el valor = 10+10; 
            if (nodo.getHijos().get(2).getHijos().size() > 0) {
                //Si la variable no existe la agrega, sino error semantico por repetida
                if (!tsGlobal.existeSimbolo(nombre)) {
                    expresion = evaluarExpresion(nodo.getHijos().get(2));
                    agregarAsignacionAVars(expresion, tipo, nombre, linea, columna);
                } else {
                    System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente");
                }
                //Si la variable viene solo con un terminal como  valor Entero = a;
            } else {
                if (!tsGlobal.existeSimbolo(nombre)) {
                    expresion = evaluarExpresion(nodo.getHijos().get(2));
                    agregarAsignacionAVars(expresion, tipo, nombre, linea, columna);
                } else {
                    System.out.println("Error semantico variable\" " + nombre + " \" ya fue declarada anteriormente");
                }
            }

        }

    }

    public void asignacionAVariablesGlobales(Nodo nodo) {
        String nombre = nodo.getHijos().get(0).getHijos().get(0).getEtiqueta();
        int linea = nodo.getHijos().get(0).getHijos().get(0).getFila();
        int columna = nodo.getHijos().get(0).getHijos().get(0).getColumna();
        String expresion = "";

        //Si la variable viene con expresiones en el valor = 10+10; 
        if (nodo.getHijos().get(1).getHijos().size() > 0) {
            //Si la variable no existe la agrega, sino error semantico por repetida
            if (tsGlobal.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1));
                actualizarVariableGlobal(nombre, expresion);
                //agregarAsignacionAVars(expresion, tipo, nombre, linea, columna);
            } else {
                System.out.println("Error semantico no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada ");
            }
            //Si la variable viene solo con un terminal como  valor Entero = a;
        } else {
            if (tsGlobal.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1));
                actualizarVariableGlobal(nombre, expresion);
            } else {
                System.out.println("Error semantico no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada ");
            }
        }
    }

    //Este metodo asigna expresion a una declaracion de variable global
    public void agregarAsignacionAVars(String expresion, String tipo, String nombre, int linea, int columna) {
        if (!expresion.equals("error tipos Incompatibles") && !expresion.equals("null") && !expresion.equals("error")) {
            String[] num1 = expresion.toString().split("@");
            String valor = num1[0];
            String tipoValor = num1[1];

            if (tipoValor.equalsIgnoreCase(tipo)) {
                Simbolo simb = new Simbolo(tipo, nombre, valor, linea, columna, "ambitoGlobal");
                //Como key de una variable tomo el Tipo+id
                tsGlobal.insertar(nombre, simb);
            } else {
                System.out.println("Error semantico, el valor de la expresion es de tipo " + tipoValor
                        + " y la variable " + nombre + " es de tipo " + tipo);
            }

        }
    }

    public void actualizarVariableGlobal(String nombre, String expresion) {
        String[] num1 = expresion.toString().split("@");
        String valor = num1[0];
        String tipoValor = num1[1];

        Simbolo simb = tsGlobal.retornarSimbolo(nombre);

        if (tipoValor.equalsIgnoreCase(simb.getTipo())) {
            simb.setValor(valor);
            System.out.println("Variable -> " + simb.getNombre() + " actualizada");
        } else {
            if (tipoValor.equalsIgnoreCase("Entero") && simb.getTipo().equalsIgnoreCase("Decimal")) {
                simb.setValor(valor);
                System.out.println("Variable -> " + simb.getNombre() + " actualizada");
            } else {
                System.out.println("Error semantico, no se puede asignar ya que " + simb.getNombre() + " es de tipo " + simb.getTipo()
                        + " y esta intentando asignarle un tipo " + tipoValor);
            }

        }
    }

    public String evaluarExpresion(Nodo n) {
        String n1 = "";
        String signo = "";
        String n3 = "";
        if (n.getTipo().equalsIgnoreCase("NoTerm")) {
            switch (n.getEtiqueta()) {
                case "EXPR_ARIT":
                    switch (n.getHijos().size()) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:

                            n1 += evaluarExpresion(n.getHijos().get(0));
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2));

                            String[] num1 = n1.split("@");
                            String n1Val = num1[0];
                            String n1Tipo = num1[1];

                            String[] num2 = n3.split("@");
                            String n2Val = num2[0];
                            String n2Tipo = num2[1];

                            Object resultado = realizarOperacionAritmeticas(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                            //System.out.println(n1Val + signo + n2Val + "= " + resultado);
                            return resultado.toString();

                    }
                    break;

                case "EXPR_LOGICA":
                    switch (n.getHijos().size()) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            n1 += evaluarExpresion(n.getHijos().get(0));
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2));
                            System.out.println(n1 + signo + n3);
                            break;
                    }
                    break;
                case "EXPR_REL":

                    switch (n.getHijos().size()) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:

                            n1 += evaluarExpresion(n.getHijos().get(0));
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2));
                            System.out.println(n1 + signo + n3);
                            break;
                    }
                    break;
                default:
                    break;
            }
        } else {
            String term = n.getEtiqueta() + "@" + n.getTipoVar();
            return term;
        }

        return null;
    }

    public Object realizarOperacionAritmeticas(String tipoOp, String tipo1, String num1, String tipo2, String num2) {
        ExpresionAritmetica exp = new ExpresionAritmetica();
        if (tipoOp.equalsIgnoreCase("+")) {
            return exp.sumar(tipo1, num1, tipo2, num2);
        } else if (tipoOp.equalsIgnoreCase("-")) {
            return exp.restar(tipo1, num1, tipo2, num2);
        } else if (tipoOp.equalsIgnoreCase("*")) {
            return exp.multiplicar(tipo1, num1, tipo2, num2);
        } else if (tipoOp.equalsIgnoreCase("%")) {
            return exp.modular(tipo1, num1, tipo2, num2);
        } else if (tipoOp.equalsIgnoreCase("^")) {
            return exp.potencia(tipo1, num1, tipo2, num2);
        } else if (tipoOp.equalsIgnoreCase("/")) {
            return exp.dividir(tipo1, num1, tipo2, num2);
        }
        return null;
    }

    public void almacenarFunciones(Nodo nodo) {
        switch (nodo.getEtiqueta()) {
            case "CUERPO_PRINCIPAL":
                for (Nodo item : nodo.getHijos()) {
                    almacenarFunciones(item);
                }
                break;
            case "FUNCION":
                agregarFunciones(nodo);
                break;
            case "METODO":
                agregarFunciones(nodo);
                break;
            case "METODO_PRINCIPAL":
                agregarFunciones(nodo);
                break;
            default:
        }
    }

    public void agregarFunciones(Nodo nodo) {
        String tipo = nodo.getHijos().get(0).getEtiqueta();
        String id = nodo.getHijos().get(1).getEtiqueta();
        int fila = nodo.getHijos().get(1).getFila();
        int columna = nodo.getHijos().get(1).getColumna();
        String parametros = "";
        String key = "";
        Nodo cuerpoFuncion;

        //Si tiene mas de 3 hijos es una funcion o metodo
        if (nodo.getHijos().size() > 3 && nodo.getHijos().get(2) != null) {
            parametros = obtenerParametros(nodo.getHijos().get(2));
        }

        key = tipo.toLowerCase() + "_" + id.toLowerCase() + "_" + parametros;

        if (id.equalsIgnoreCase("Principal")) {
            agregarMetodoPrincipal(nodo);
        } else {
            if (!IdesPalabraReservada(id) && nodo.getHijos().size() > 3) {
                if (!tsFunciones.existeFuncion(key)) {
                    cuerpoFuncion = nodo.getHijos().get(3);
                    Funcion miFuncion = new Funcion(id, tipo, fila, columna, cuerpoFuncion);
                    agregarParametros(nodo.getHijos().get(2), miFuncion);
                    tsFunciones.insertar(key, miFuncion);
                } else {
                    System.out.println("Error semantico Funcion " + tipo + " " + id + " ya fue declara anteriormente");
                }
            } else {
                System.out.println("Error semantico Funcion no puede ser llamada como una palabra reservada");
            }
        }
    }

    public void agregarMetodoPrincipal(Nodo nodo) {
        String tipo = nodo.getHijos().get(0).getEtiqueta();
        String id = nodo.getHijos().get(1).getEtiqueta();
        String key = tipo.toLowerCase() + "_" + id.toLowerCase();
        int fila = nodo.getHijos().get(1).getFila();
        int columna = nodo.getHijos().get(1).getColumna();
        Nodo cuerpoFuncion = nodo.getHijos().get(2);

        //Verificando si el id es una palabra reservada
        if (!tsFunciones.existeFuncion(key)) {
            Funcion miFuncion = new Funcion(id, tipo, fila, columna, cuerpoFuncion);
            agregarParametros(nodo.getHijos().get(2), miFuncion);
            tsFunciones.insertar(key, miFuncion);
        } else {
            System.out.println("Error semantico Metodo principal ya fue declarado anteriormente");
        }
    }

    public String obtenerParametros(Nodo nodo) {
        String parametros = "";
        switch (nodo.getEtiqueta()) {
            case "LISTA_PARAMETROS":
                switch (nodo.getHijos().size()) {
                    case 2:
                        String tipo = nodo.getHijos().get(0).getEtiqueta();
                        parametros += tipo.toLowerCase();
                        break;
                    case 3:
                        parametros += obtenerParametros(nodo.getHijos().get(0));
                        tipo = nodo.getHijos().get(1).getEtiqueta();
                        parametros += tipo.toLowerCase();
                        break;
                }
                break;
            default:
        }
        return parametros;
    }

    public void agregarParametros(Nodo nodo, Funcion funcion) {
        switch (nodo.getEtiqueta()) {
            case "LISTA_PARAMETROS":
                switch (nodo.getHijos().size()) {
                    case 2:
                        String tipo = nodo.getHijos().get(0).getEtiqueta();
                        String id = nodo.getHijos().get(1).getEtiqueta();
                        int fila = nodo.getHijos().get(1).getFila();
                        int columna = nodo.getHijos().get(1).getColumna();
                        funcion.agregarParametro(tipo, id, fila, columna);
                        break;
                    case 3:
                        agregarParametros(nodo.getHijos().get(0), funcion);
                        tipo = nodo.getHijos().get(1).getEtiqueta();
                        id = nodo.getHijos().get(2).getEtiqueta();
                        fila = nodo.getHijos().get(2).getFila();
                        columna = nodo.getHijos().get(2).getColumna();
                        funcion.agregarParametro(tipo, id, fila, columna);
                        break;
                }
                break;
            default:
        }
    }

    public Boolean IdesPalabraReservada(String id) {
        for (Reservada item : PalabraReservada.Reservada.values()) {
            if (item.toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
}
