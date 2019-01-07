/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import EjecucionExpresiones.ExpresionAritmetica;
import Ejecucion.PalabraReservada.Reservada;
import fuentes.Nodo;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * @author Hellen
 */
public class EjecucionLenguajeAsa {

    public static Stack<TablaSimbolo> pilaSimbolos = new Stack<>();
    public static Stack<TablaSimbolo> pilaSimbolosAux = new Stack<>();
    public static TablaSimbolo tsGlobal = new TablaSimbolo();  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
    public static TablaSimbolo tsMain = new TablaSimbolo();  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
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
                if (nodo.getHijos().get(1).getEtiqueta().equalsIgnoreCase("LISTA_VARIABLES")) {
                    Nodo tipo = nodo.getHijos().get(0);
                    Nodo exp = nodo.getHijos().get(2);
                    DeclaracionVar.recorrerListaVars(tipo, nodo.getHijos().get(1), exp, "ambitoGlobal");
                }
                break;
            case "ASIGNACIONES":
                AsignacionVars.asignacionAVariables(nodo, tsGlobal, "ambitoGlobal");
                break;
            default:
        }
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

    public static String ejecutarMain() {
        String consola = "";

        if (tsFunciones.existeFuncion("vacio_principal")) {
            Funcion sentencias = tsFunciones.retornarFuncion("vacio_principal");
            consola = ejecutarSentenciasMain(sentencias.getCuerpo(), consola, "ambitoMain");
            System.out.println("Consolita");
            System.out.println(consola);
        } else {
            System.out.println("El metodo principal no esta declarado aun");
        }
        return consola;
    }

    public static String ejecutarSentenciasMain(Nodo nodo, String consola, String ambito) {
        switch (nodo.getEtiqueta()) {
            case "LISTA_SENTENCIAS":

                switch (nodo.getHijos().size()) {
                    case 1:
                        consola += ejecutarSentenciasMain(nodo.getHijos().get(0), consola, ambito);
                        return consola;
                    case 2:
                        consola += ejecutarSentenciasMain(nodo.getHijos().get(0), consola, ambito);
                        consola += ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito);
                        return consola;
                }
                break;
            case "DECLARACION_VARIABLES":
                Nodo tipo = nodo.getHijos().get(0);
                Nodo exp = nodo.getHijos().get(2);
                DeclaracionVar.recorrerListaVars(tipo, nodo.getHijos().get(1), exp, ambito);
                break;
            case "MOSTRAR":
                String imprimir = evaluarExpresion(nodo.getHijos().get(0), ambito);
                String[] num1 = imprimir.split("@");
                String val = num1[0];
                consola = "> " + val + "\n";
                return consola;
        }
        return consola;
    }

    //Tipo es para ver si esta con globales o locales
    public static String evaluarExpresion(Nodo n, String tipoAmbito) {
        TablaSimbolo ts = null;
        String n1 = "";
        String signo = "";
        String n3 = "";

        if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
            ts = EjecucionLenguajeAsa.tsGlobal;
        }

        if (n.getTipo().equalsIgnoreCase("NoTerm")) {
            switch (n.getEtiqueta()) {
                case "EXPR_ARIT":
                    switch (n.getHijos().size()) {
                        case 3:

                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito);

                            if (!n1.equalsIgnoreCase("error") && !n3.equalsIgnoreCase("error")) {
                                String[] num1 = n1.split("@");
                                String n1Val = num1[0];
                                String n1Tipo = num1[1];

                                String[] num2 = n3.split("@");
                                String n2Val = num2[0];
                                String n2Tipo = num2[1];

                                if (n1Tipo.equalsIgnoreCase("identificador") || n2Tipo.equalsIgnoreCase("identificador")) {
                                    String val;
                                    String tipo;
                                    if (ts.existeSimbolo(n1Val)) {
                                        val = ts.retornarSimbolo(n1Val).getValor();
                                        tipo = ts.retornarSimbolo(n1Val).getTipo();
                                        n1Val = val;
                                        n1Tipo = tipo;
                                    } else {
                                        System.out.println("Bitacora La variable " + n1Val + " no esta declarada"
                                                + "linea: " + n.getHijos().get(0).getFila() + " columna:" + n.getHijos().get(0).getColumna());
                                        return "error";
                                    }

                                    if (ts.existeSimbolo(n2Val)) {
                                        val = ts.retornarSimbolo(n2Val).getValor();
                                        tipo = ts.retornarSimbolo(n2Val).getTipo();
                                        n2Val = val;
                                        n2Tipo = tipo;
                                    } else {
                                        System.out.println("Bitacora La variable " + n2Val + " no esta declarada"
                                                + "linea: " + n.getHijos().get(2).getFila() + " columna:" + n.getHijos().get(2).getColumna());
                                        return "error";
                                    }
                                }

                                Object resultado = realizarOperacionAritmeticas(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                                return resultado.toString();
                            } else {
                                return "error";
                            }

                    }
                    break;
                case "NUM_NEG":
                    n1 += evaluarExpresion(n.getHijos().get(1), tipoAmbito);
                    String[] num = n1.split("@");
                    String val = num[0];
                    String tipo = num[1];
                    if (tipo.equalsIgnoreCase("Decimal") || tipo.equalsIgnoreCase("Entero")) {
                        String numerin = val.replace(",", ".");
                        Double numCast = Double.parseDouble(numerin);
                        Double res = numCast * -1;
                        String r = String.valueOf(res);
                        return String.valueOf(r.replace(",", ".") + "@decimal");
                    } else {
                        return "error";
                    }

                case "EXPR_LOGICA":
                    switch (n.getHijos().size()) {
                        case 1:
                            break;
                        case 2:

                            break;
                        case 3:
                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito);

                            String[] num1 = n1.split("@");
                            String n1Val = num1[0];
                            String n1Tipo = num1[1];

                            String[] num2 = n3.split("@");
                            String n2Val = num2[0];
                            String n2Tipo = num2[1];

                            String resultado = "";
                            if (n1Tipo.equalsIgnoreCase("booleano") && n2Tipo.equalsIgnoreCase("booleano")) {
                                resultado = operarExprLogica(n1Val, n2Val, signo);
                                return resultado + "@booleano";
                            } else {
                                return resultado + "@error";
                            }
                        //Object resultado = realizarOperacionAritmeticas(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                    }
                    break;
                case "EXPR_REL":

                    switch (n.getHijos().size()) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:

                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito);

                            String[] num1 = n1.split("@");
                            String n1Val = num1[0];
                            String n1Tipo = num1[1];

                            String[] num2 = n3.split("@");
                            String n2Val = num2[0];
                            String n2Tipo = num2[1];
                            if (verificarExprLogicaValida(n1Tipo, n2Tipo)) {
                                String res = evaluarExpresionRelacional(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                                return res + "@booleano";
                            } else {
                                System.out.println("15@ERROR");
                            }
//                            Object resultado = realizarOperacionAritmeticas(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                            break;
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (n.getTipoVar().equalsIgnoreCase("Identificador")) {
                String id = n.getEtiqueta();
                if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
                    if (ts.existeSimbolo(id)) {
                        String val = ts.retornarSimbolo(id).getValor();
                        String tipo = ts.retornarSimbolo(id).getTipo();
                        String term = val + "@" + tipo;
                        return term;
                    } else {
                        System.out.println("Bitacora La variable " + id + " no esta declarada"
                                + "linea: " + n.getFila() + " columna: " + n.getColumna());

                        return "error";
                    }
                } else {

                    if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
                        if (!pilaSimbolos.empty()) {
                            boolean cond = true;
                            if (pilaSimbolos.size() == 1) {
                                ts = pilaSimbolos.peek();
                            }

                            if (ts.existeSimbolo(id)) {
                                String val = ts.retornarSimbolo(id).getValor();
                                String tipo = ts.retornarSimbolo(id).getTipo();
                                String term = val + "@" + tipo;
                                return term;
                            } else if (tsGlobal.existeSimbolo(id)) {
                                ts = tsGlobal;
                                String val = ts.retornarSimbolo(id).getValor();
                                String tipo = ts.retornarSimbolo(id).getTipo();
                                String term = val + "@" + tipo;
                                return term;
                            } else {
                                System.out.println("Bitacora La variable " + id + " no esta declarada"
                                        + "linea: " + n.getFila() + " columna: " + n.getColumna());

                                return "error";
                            }

                        }
                    }

//                    if (pilaSimbolos.empty()) {
//                        //Me toca buscar primero en metodo main xD
//                        if (tsGlobal.existeSimbolo(id)) {
//                            String val = tsGlobal.retornarSimbolo(id).getValor();
//                            String tipo = tsGlobal.retornarSimbolo(id).getTipo();
//                            String term = val + "@" + tipo;
//                            return term;
//                        } else {
//                            System.out.println("Bitacora La variable " + id + " no esta declarada"
//                                    + "linea: " + n.getFila() + " columna: " + n.getColumna());
//
//                            return "error";
//                        }
//                    } else {
//
//                    }
                }
            } else {
                String term = n.getEtiqueta() + "@" + n.getTipoVar();
                return term;
            }

        }
        return null;
    }

    private static String operarExprLogica(String val1, String val2, String op) {
        Boolean a = false;
        Boolean b = false;

        if (val1.equalsIgnoreCase("verdadero")) {
            a = true;
        }

        if (val2.equalsIgnoreCase("verdadero")) {
            b = true;
        }

        if (op.equalsIgnoreCase("&&")) {
            if (a && b) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase("||") || (op.equalsIgnoreCase("|"))) {
            if (a || b) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase("!")) {
            if (!a) {
                return "verdadero";
            } else {
                return "falso";
            }
        }
        return "error";
    }

    //primreo evaluo y luego opero
    private static String evaluarExpresionRelacional(String op, String tipo1, String val1, String tipo2, String val2) {
        String resultado = "";
        if (tipo1.equalsIgnoreCase("Decimal") || tipo1.equalsIgnoreCase("Entero")
                && tipo2.equalsIgnoreCase("Decimal") || tipo2.equalsIgnoreCase("Entero")) {

            resultado = operarExpresionRelacional(op, val1, val2);

            return resultado;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Texto")) {
            int ascii1 = 0;
            int ascii2 = 0;
            for (int x = 0; x < val1.length(); x++) {
                ascii1 += val1.codePointAt(x);
            }

            for (int x = 0; x < val2.length(); x++) {
                ascii2 += val2.codePointAt(x);
            }
            //System.out.println("guardar en bitacora cual es mayor que la otra");
            resultado = operarExpresionRelacional(op, Integer.toString(ascii1), Integer.toString(ascii2));

            return resultado;
        }
        return "";
    }

    private static String operarExpresionRelacional(String op, String val1, String val2) {
        if (op.equalsIgnoreCase("==")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (Objects.equals(num1, num2)) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase("!=")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (num1 != num2) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase("<")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (num1 < num2) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase(">")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (num1 > num2) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase("<=")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (num1 <= num2) {
                return "verdadero";
            } else {
                return "falso";
            }
        } else if (op.equalsIgnoreCase(">=")) {
            Double num1 = Double.parseDouble(val1);
            Double num2 = Double.parseDouble(val2);

            if (num1 >= num2) {
                return "verdadero";
            } else {
                return "falso";
            }
        }
        return "error";
    }

    public static boolean verificarExprLogicaValida(String tipo1, String tipo2) {
        if (tipo1.equalsIgnoreCase("Decimal") || tipo1.equalsIgnoreCase("Entero")
                && tipo2.equalsIgnoreCase("Decimal") || tipo2.equalsIgnoreCase("Entero")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Texto")) {
            return true;
        }
        return false;
    }

    public static Object realizarOperacionAritmeticas(String tipoOp, String tipo1, String num1, String tipo2, String num2) {
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
            System.out.println("Error semantico Metodo principal ya fue declarado anteriormente"
                    + " fila: " + fila + " columna: " + columna);
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
