/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import static Ejecucion.EjecucionLenguajeAsa.evaluarExpresion;
import static Ejecucion.EjecucionLenguajeAsa.listaErrores;
import fuentes.Nodo;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Hellen
 */
public class AsignacionVars {

    //Metodo cuando viene una asignacion a variablesowe
    public static void asignacionAVariables(Nodo nodo, String tipoAmbito, String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException {
        TablaSimbolo ts = null;
        String nombre = nodo.getHijos().get(0).getHijos().get(0).getEtiqueta();
        int linea = nodo.getHijos().get(0).getHijos().get(0).getFila();
        int columna = nodo.getHijos().get(0).getHijos().get(0).getColumna();
        String expresion = "";

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        } else {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        }

        //Si la variable viene con expresiones en el valor = 10+10; 
        if (nodo.getHijos().get(1).getHijos().size() > 0) {
            //Si la variable no existe la agrega, sino error semantico por repetida
            if (ts.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito, nombreArchivo);
                actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito, nombreArchivo);
                //agregarAsignacionAVars(expresion, tipo, nombre, linea, columna);
            } else {
                ts = EjecucionLenguajeAsa.tsVarsFuncion;
                if (ts.existeSimbolo(nombre)) {
                    expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito, nombreArchivo);
                    actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito, nombreArchivo);
                } else {
                    ts = EjecucionLenguajeAsa.tsGlobal;
                    if (ts.existeSimbolo(nombre)) {
                        expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito, nombreArchivo);
                        actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito, nombreArchivo);
                    } else {
                        EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada", nombreArchivo, linea, columna));

                    }
                }

            }
            //Si la variable viene solo con un terminal como  valor Entero = a;
        } else {
            if (ts.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito, nombreArchivo);
                actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito, nombreArchivo);
            } else {
                EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada", nombreArchivo, linea, columna));
            }
        }
    }

    //Este metodo asigna expresion a una declaracion de variable global
    public static void agregarAsignacionAVars(String expresion, String tipo, String nombre, int linea, int columna, String ambito, String nombreArchivo) {
        TablaSimbolo ts = null;
        if (ambito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (ambito.equalsIgnoreCase("ambitoMain")) {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        } else {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
        }

        if (!expresion.equals("error tipos Incompatibles") && !expresion.equals("null") && !expresion.equals("@error") && !expresion.equals("error")) {
            String[] num1 = expresion.toString().split("@");
            String valor = num1[0];
            String tipoValor = num1[1];
            if (tipoValor.equalsIgnoreCase(tipo)) {
                Simbolo simb = new Simbolo(tipo, nombre, valor, linea, columna, ambito);
                //Como key de una variable tomo el Tipo+id

                if (ambito.equalsIgnoreCase("ambitoGlobal") || ambito.equalsIgnoreCase("ambitoMain")) {
                    ts.insertar(nombre, simb);
                } else if (!EjecucionLenguajeAsa.tsVarsFuncion.existeSimbolo(nombre)) {
                    ts.insertar(nombre, simb);
                    EjecucionLenguajeAsa.tsVarsFuncion.insertar(nombre, simb);
                } else {
                    EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "Variable " + nombre + " ya esta declarada en esta funcion", nombreArchivo, linea, columna));
                }

            } else {
                if (verificarCasteo(tipo, tipoValor, nombreArchivo)) {
                    String nuevoValor = castearImplicitamente(tipo, tipoValor, valor, nombreArchivo);
                    Simbolo simb = new Simbolo(tipo, nombre, nuevoValor, linea, columna, "ambitoGlobal");
                    //Como key de una variable tomo el Tipo+id
                    ts.insertar(nombre, simb);
                    System.out.println("casteada, implicitamente el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + tipo + " linea:" + linea + " columna: " + columna);
                } else {
                    EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "No puede ser casteada, el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + tipo, nombreArchivo, linea, columna));
                }
            }
        } else {
            EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "Error semantico al operar expresion", nombreArchivo, linea, columna));
        }

        if (EjecucionLenguajeAsa.pilaSimbolos.empty() && !ambito.equalsIgnoreCase("ambitoGlobal")) {
            EjecucionLenguajeAsa.pilaSimbolos.push(ts);
        }

    }

    public static void actualizarVariableGlobal(String nombre, String expresion, int linea, int columna, String tipoAmbito, String nombreArchivo) {
        String[] num1 = expresion.split("@");
        String valor = num1[0];
        String tipoValor = num1[1];
        TablaSimbolo ts = null;
        Simbolo simb = null;

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
            simb = ts.retornarSimbolo(nombre);

        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
            simb = ts.retornarSimbolo(nombre);
            //Si no esta en el main, esta en la global
            if (ts.existeSimbolo(nombre)) {
                simb = ts.retornarSimbolo(nombre);

            } else {
                ts = EjecucionLenguajeAsa.tsGlobal;
                if (ts.existeSimbolo(nombre)) {
                    simb = ts.retornarSimbolo(nombre);
                }
            }
        } else {
            ts = EjecucionLenguajeAsa.pilaSimbolos.peek();

            if (ts.existeSimbolo(nombre)) {
                simb = ts.retornarSimbolo(nombre);

                //Si estoy en un sub ambito el main lo agrego a la lista actual de variables
                if (EjecucionLenguajeAsa.tsVarsFuncion.tabla.size() != 0) {
                    if (!EjecucionLenguajeAsa.tsVarsFuncion.existeSimbolo(simb.getNombre())) {
                        EjecucionLenguajeAsa.tsVarsFuncion.insertar(nombre, simb);
                    }
                } else {
                    EjecucionLenguajeAsa.tsVarsFuncion.insertar(nombre, simb);
                }

            } else {
                ts = EjecucionLenguajeAsa.tsVarsFuncion;
                if (ts.existeSimbolo(nombre)) {
                    simb = ts.retornarSimbolo(nombre);
                }
            }

        }

        if (!expresion.equalsIgnoreCase("error")) {
            if (tipoValor.equalsIgnoreCase(simb.getTipo())) {
                simb.setValor(valor);
                if (!tipoAmbito.equalsIgnoreCase("ambitoGlobal") && !tipoAmbito.equalsIgnoreCase("ambitoMain")) {
                    Simbolo varModify = EjecucionLenguajeAsa.tsVarsFuncion.retornarSimbolo(nombre);
                    varModify.setValor(valor);
                }
                //System.out.println(" Bitacora Variable -> " + simb.getNombre() + " actualizada");
            } else {
                if (AsignacionVars.verificarCasteo(simb.getTipo(), tipoValor, nombreArchivo)) {
                    String nuevoValor = AsignacionVars.castearImplicitamente(simb.getTipo(), tipoValor, valor, nombreArchivo);
                    simb.setValor(nuevoValor);
                    System.out.println("casteada, implicitamente el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + simb.getTipo() + " linea:" + linea + " columna: " + columna);
                } else {
                    EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "Error semantico no puede ser casteada, el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + simb.getTipo() + nombre + "\" ya que no esta declarada", nombreArchivo, linea, columna));
                }
            }
        } else {
            EjecucionLenguajeAsa.listaErrores.add(new Error("Semantico", "Error semantico al operar expresion", nombreArchivo, linea, columna));
        }
    }

    public static boolean verificarCasteo(String varTipoDestino, String varTipoResult, String nombreArchivo) {
        if (varTipoDestino.equalsIgnoreCase("Texto")
                && (varTipoResult.equalsIgnoreCase("Decimal") || (varTipoResult.equalsIgnoreCase("Booleano")) || (varTipoResult.equalsIgnoreCase("Entero")))) {
            return true;
        } else if (varTipoDestino.equalsIgnoreCase("Decimal")
                && (varTipoResult.equalsIgnoreCase("Booleano") || (varTipoResult.equalsIgnoreCase("Entero")))) {
            return true;
        } else if (varTipoDestino.equalsIgnoreCase("Entero")
                && (varTipoResult.equalsIgnoreCase("Decimal") || (varTipoResult.equalsIgnoreCase("Booleano")))) {
            return true;
        }
        return false;
    }

    public static String castearImplicitamente(String varTipoDestino, String varTipoResult, String valor, String nombreArchivo) {
        if (varTipoDestino.equalsIgnoreCase("Texto")) {
            if ((varTipoResult.equalsIgnoreCase("Decimal") || varTipoResult.equalsIgnoreCase("Entero"))) {
                return valor;
            } else if (varTipoResult.equalsIgnoreCase("Booleano")) {
                if (valor.equalsIgnoreCase("falso")) {
                    return "0";
                } else {
                    return "1";
                }
            }
        } else if (varTipoDestino.equalsIgnoreCase("Decimal")) {
            if (varTipoResult.equalsIgnoreCase("Entero")) {
                int n = Integer.parseInt(valor);
                double num = (double) n; // d = 3.0
                return String.valueOf(num);
            } else if (varTipoResult.equalsIgnoreCase("Booleano")) {
                if (valor.equalsIgnoreCase("falso")) {
                    return "0";
                } else {
                    return "1";
                }
            }
        } else if (varTipoDestino.equalsIgnoreCase("Entero")) {
            if (varTipoResult.equalsIgnoreCase("Decimal")) {
                Double n = Double.parseDouble(valor.replace(",", "."));

                //int num = n.intValue();
                return String.valueOf(Math.round(n));
            } else if (varTipoResult.equalsIgnoreCase("Booleano")) {
                if (valor.equalsIgnoreCase("falso")) {
                    return "0";
                } else {
                    return "1";
                }
            }
        }
        return "";
    }
}
