/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import static Ejecucion.EjecucionLenguajeAsa.evaluarExpresion;
import fuentes.Nodo;

/**
 *
 * @author Hellen
 */
public class AsignacionVars {

    //Metodo cuando viene una asignacion a variables
    public static void asignacionAVariables(Nodo nodo, TablaSimbolo ts, String tipoAmbito) {
        String nombre = nodo.getHijos().get(0).getHijos().get(0).getEtiqueta();
        int linea = nodo.getHijos().get(0).getHijos().get(0).getFila();
        int columna = nodo.getHijos().get(0).getHijos().get(0).getColumna();
        String expresion = "";

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            if (EjecucionLenguajeAsa.pilaSimbolos.empty()) {
                TablaSimbolo tsMain = new TablaSimbolo();
                ts = tsMain;
            }
        }

        //Si la variable viene con expresiones en el valor = 10+10; 
        if (nodo.getHijos().get(1).getHijos().size() > 0) {
            //Si la variable no existe la agrega, sino error semantico por repetida
            if (ts.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito);
                actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito);
                //agregarAsignacionAVars(expresion, tipo, nombre, linea, columna);
            } else {
                System.out.println("Error semantico no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada "
                        + " linea:" + linea + " columna: " + columna);
            }
            //Si la variable viene solo con un terminal como  valor Entero = a;
        } else {
            if (ts.existeSimbolo(nombre)) {
                expresion = evaluarExpresion(nodo.getHijos().get(1), tipoAmbito);
                actualizarVariableGlobal(nombre, expresion, linea, columna, tipoAmbito);
            } else {
                System.out.println("Error semantico no se puede asignar en la variable \"" + nombre + "\" ya que no esta declarada "
                        + " linea:" + linea + " columna: " + columna);
            }
        }
    }

    //Este metodo asigna expresion a una declaracion de variable global
    public static void agregarAsignacionAVars(String expresion, String tipo, String nombre, int linea, int columna, String ambito) {
        TablaSimbolo ts = null;
        if (ambito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (ambito.equalsIgnoreCase("ambitoMain")) {
            if (EjecucionLenguajeAsa.pilaSimbolos.empty()) {
                TablaSimbolo tsMain = new TablaSimbolo();
                ts = tsMain;
            }else if(EjecucionLenguajeAsa.pilaSimbolos.size() == 1){
              ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
            }
        }

        if (!expresion.equals("error tipos Incompatibles") && !expresion.equals("null") && !expresion.equals("@error") && !expresion.equals("error")) {
            String[] num1 = expresion.toString().split("@");
            String valor = num1[0];
            String tipoValor = num1[1];
            if (tipoValor.equalsIgnoreCase(tipo)) {
                Simbolo simb = new Simbolo(tipo, nombre, valor, linea, columna, ambito);
                //Como key de una variable tomo el Tipo+id
                ts.insertar(nombre, simb);

            } else {
                if (verificarCasteo(tipo, tipoValor)) {
                    String nuevoValor = castearImplicitamente(tipo, tipoValor, valor);
                    Simbolo simb = new Simbolo(tipo, nombre, nuevoValor, linea, columna, "ambitoGlobal");
                    //Como key de una variable tomo el Tipo+id
                    ts.insertar(nombre, simb);
                    //ts.insertar(nombre, simb);
                    System.out.println("casteada, implicitamente el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + tipo + " linea:" + linea + " columna: " + columna);
                } else {
                    System.out.println("Error semantico no puede ser casteada, el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + tipo + " linea:" + linea + " columna: " + columna);
                }
            }
        } else {
            System.out.println("Error semantico al operar expresion" + " linea:" + linea + " columna: " + columna);
        }

        if (EjecucionLenguajeAsa.pilaSimbolos.empty() && !ambito.equalsIgnoreCase("ambitoGlobal")) {
            EjecucionLenguajeAsa.pilaSimbolos.push(ts);
        }

    }

    public static void actualizarVariableGlobal(String nombre, String expresion, int linea, int columna, String tipoAmbito) {
        String[] num1 = expresion.split("@");
        String valor = num1[0];
        String tipoValor = num1[1];
        TablaSimbolo ts = null;

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        } else if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            if (EjecucionLenguajeAsa.pilaSimbolos.empty()) {
                TablaSimbolo tsMain = new TablaSimbolo();
                ts = tsMain;
            }
        }

        Simbolo simb = ts.retornarSimbolo(nombre);

        if (!expresion.equalsIgnoreCase("error")) {
            if (tipoValor.equalsIgnoreCase(simb.getTipo())) {
                simb.setValor(valor);
                System.out.println("Variable -> " + simb.getNombre() + " actualizada");
            } else {
                if (AsignacionVars.verificarCasteo(simb.getTipo(), tipoValor)) {
                    String nuevoValor = AsignacionVars.castearImplicitamente(simb.getTipo(), tipoValor, valor);
                    simb.setValor(nuevoValor);
                    System.out.println("casteada, implicitamente el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + simb.getTipo() + " linea:" + linea + " columna: " + columna);
                } else {
                    System.out.println("Error semantico no puede ser casteada, el valor de la expresion es de tipo " + tipoValor
                            + " y la variable destino " + nombre + " es de tipo " + simb.getTipo() + " linea:" + linea + " columna: " + columna);
                }
            }
        } else {
            System.out.println("Error semantico al operar expresion" + " linea:" + linea + " columna: " + columna);
        }
    }

    public static boolean verificarCasteo(String varTipoDestino, String varTipoResult) {
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

    public static String castearImplicitamente(String varTipoDestino, String varTipoResult, String valor) {
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
                int num = n.intValue();
                return String.valueOf(num);
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
