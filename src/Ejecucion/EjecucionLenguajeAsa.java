/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import ControlDot.ControlDot;
import static ControlDot.generarGrafica.generarGrafica;
import static Ejecucion.AsignacionVars.actualizarVariableGlobal;
import EjecucionExpresiones.ExpresionAritmetica;
import Ejecucion.PalabraReservada.Reservada;
import fuentes.Nodo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * @author Hellen
 */
public class EjecucionLenguajeAsa {

    public static Stack<TablaSimbolo> pilaSimbolos = new Stack<>();
    public static Stack<TablaSimbolo> pilaSimbolosAux = new Stack<>();
    public static TablaSimbolo tsGlobal = new TablaSimbolo("ambitoGlobal");  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
    //public static TablaSimbolo tsMain = new TablaSimbolo("");  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
    public static TablaFunciones tsFunciones = new TablaFunciones(); //Tabla para almacenar funciones, metodos y principal
    public static TablaSimbolo tsVarsFuncion = new TablaSimbolo("FuncActual"); //Tabla para almacenar funciones, metodos y principal

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
                AsignacionVars.asignacionAVariables(nodo, "ambitoGlobal");
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

    public static String ejecutarMain() throws FileNotFoundException, UnsupportedEncodingException {
        String consola = "";

        if (tsFunciones.existeFuncion("vacio_principal")) {
            TablaSimbolo tsMain = new TablaSimbolo("ambitoMain");
            pilaSimbolos.push(tsMain);
            Funcion sentencias = tsFunciones.retornarFuncion("vacio_principal");
            consola = ejecutarSentenciasMain(sentencias.getCuerpo(), consola, "ambitoMain");
            tsFunciones = new TablaFunciones();
            System.out.println("Consolita");
            System.out.println(consola);
        } else {
            System.out.println("El metodo principal no esta declarado aun");
        }
        return consola;
    }

    public static String ejecutarSentenciasMain(Nodo nodo, String consola, String ambito) throws FileNotFoundException, UnsupportedEncodingException {
        switch (nodo.getEtiqueta()) {
            case "LISTA_SENTENCIAS":
                switch (nodo.getHijos().size()) {
                    case 1:
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(0), consola, ambito);
                        return consola;
                    case 2:
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(0), consola, ambito);
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito);
                        return consola;
                }
                break;

            case "DECLARACION_VARIABLES":
                Nodo tipo = nodo.getHijos().get(0);
                Nodo exp = nodo.getHijos().get(2);
                DeclaracionVar.recorrerListaVars(tipo, nodo.getHijos().get(1), exp, ambito);

                return consola;

            case "ASIGNACIONES":
                AsignacionVars.asignacionAVariables(nodo, ambito);
                return consola;

            case "MOSTRAR":
                String imprimir = "";
                imprimir = evaluarExpresion(nodo.getHijos().get(0), ambito);
                String[] num1 = imprimir.split("@");
                String val = num1[0];
                consola += "> " + val + "\n";
                return consola;

            case "LLAMADA_FUNCION":
                String key = "";
                String paramsEnviados = "";
                Funcion miFuncion = null;
                String paramsEnviadosOrigin = "";

                if (nodo.getHijos().get(2).getHijos().size() != 0) {
                    paramsEnviados = obtenerVarsParam(nodo.getHijos().get(2));
                    String[] params = paramsEnviados.split(",");

                    //Obtengo el tipo de las variables que van como parametros al llamar una funcion
                    List<Simbolo> vars = new ArrayList<Simbolo>();
                    for (String item : params) {
                        vars.add(obtenerTipo(ambito, item));
                    }

                    //Generando la llave de los parametros
                    for (Simbolo item : vars) {
                        paramsEnviadosOrigin += item.getTipo().toLowerCase();
                    }

                    key = nodo.getHijos().get(0).getEtiqueta() + "_" + paramsEnviadosOrigin;

                    if (tsFunciones.existeFuncion(key)) {
                        miFuncion = tsFunciones.retornarFuncion(key);

                        TablaSimbolo tsFuncion = new TablaSimbolo(nodo.getHijos().get(0).getEtiqueta(), "");
                        int i = 0;
                        for (Simbolo item : vars) {
                            //Simbolo simb = new Simbolo(tipo, nombre, valor, linea, columna, ambito);
                            Parametro paramRecibido = miFuncion.getParametros().get(i);
                            item.setNombre(paramRecibido.getNombre());
                            tsFuncion.insertar(item.getNombre(), item);
                            i++;
                        }

                        //La cima de la pila sera ahora mi funcion actual
                        pilaSimbolos.push(tsFuncion);
                        consola = ejecutarSentenciasMain(miFuncion.getCuerpo(), consola, nodo.getHijos().get(0).getEtiqueta());
                        //Quito el ambito de la funcion actual, quedandome solo el main
                        pilaSimbolos.pop();
                        return consola;
                    } else {
                        System.out.println("Funcion o parametros invalidos");
                        return consola;

                    }
                } else {
                    key = nodo.getHijos().get(0).getEtiqueta() + "_" + paramsEnviadosOrigin;

                    if (tsFunciones.existeFuncion(key)) {
                        miFuncion = tsFunciones.retornarFuncion(key);
                        //La cima de la pila sera ahora mi funcion actual
                        TablaSimbolo tsFuncion = new TablaSimbolo(nodo.getHijos().get(0).getEtiqueta(), "");
                        pilaSimbolos.push(tsFuncion);
                        consola = ejecutarSentenciasMain(miFuncion.getCuerpo(), consola, nodo.getHijos().get(0).getEtiqueta());
                        //Quito el ambito de la funcion actual, quedandome solo el main
                        if (pilaSimbolos.peek().ambito.equalsIgnoreCase(nodo.getHijos().get(0).getEtiqueta())) {
                            pilaSimbolos.pop();
                        }
                        tsVarsFuncion = new TablaSimbolo("");
                        return consola;
                    } else {
                        return consola;
                    }
                }

            case "ES_VERDADERO":
                //ambito
                String condicion = evaluarExpresion(nodo.getHijos().get(0), "esVerdadero");
                String[] condi = condicion.split("@");
                String condEsVerdadera = condi[0];
                String tipoCondIf = condi[1];
                if (tipoCondIf.equalsIgnoreCase("booleano")) {
                    if (condEsVerdadera.equalsIgnoreCase("verdadero") || condEsVerdadera.equalsIgnoreCase("1")) {
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito + "@" + "if");
                    } else {
                        //Si la condicion es falsa veo si el if contiene un ELSE
                        if (nodo.getHijos().size() == 3) {
                            consola = ejecutarSentenciasMain(nodo.getHijos().get(2).getHijos().get(0), consola, ambito + "@" + "if");

                        } else {
                            System.out.println("no tiene else");
                        }
                    }
                    if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "if")) {
                        pilaSimbolos.pop();
                    }
                } else {
                    System.out.println("Condicion del if invalida en fila: " + nodo.getFila());
                }
                //Limpio este ambito en la pila actuaol
                return consola;

            case "MIENTRAS_QUE":
                //ambito
                String condicionMientrasQ = evaluarExpresion(nodo.getHijos().get(0), "mientrasQue");
                String[] condMientras = condicionMientrasQ.split("@");
                String condBoolMQ = condMientras[0];
                String tipoCondMientras = condMientras[1];

                if (tipoCondMientras.equalsIgnoreCase("booleano")) {
                    if (condBoolMQ.equalsIgnoreCase("verdadero") || condBoolMQ.equalsIgnoreCase("1")) {
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito + "@" + "while");

                        //iteracion del while
                        while (true) {
                            condicionMientrasQ = evaluarExpresion(nodo.getHijos().get(0), "mientrasQue");
                            String[] condMientras2 = condicionMientrasQ.split("@");
                            condBoolMQ = condMientras2[0];
                            tipoCondMientras = condMientras2[1];

                            if (condBoolMQ.equalsIgnoreCase("verdadero") || condBoolMQ.equalsIgnoreCase("1")) {
                                consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito + "@" + "while");
                            } else {
                                break;
                            }
                        }
                    }

                    if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "while")) {
                        //Limpio este ambito en la pila actual
                        pilaSimbolos.pop();
                    }
                } else {
                    System.out.println("Condicion del while invalida en fila:");
                }
                return consola;

            case "HASTA_QUE":
                //ambito
                String condicionHastaQue = evaluarExpresion(nodo.getHijos().get(0), "hastaQue");
                String[] condHastaQue = condicionHastaQue.split("@");
                String condBoolHQ = condHastaQue[0];
                String tipoCondHQ = condHastaQue[1];

                if (tipoCondHQ.equalsIgnoreCase("booleano")) {
                    if (condBoolHQ.equalsIgnoreCase("falso") || condBoolHQ.equalsIgnoreCase("0")) {
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito + "@" + "whileNeg");

                        //iteracion del while
                        while (true) {
                            condBoolHQ = evaluarExpresion(nodo.getHijos().get(0), "hastaQue");
                            String[] condHastaQue2 = condBoolHQ.split("@");
                            condBoolHQ = condHastaQue2[0];
                            tipoCondHQ = condHastaQue2[1];

                            if (condBoolHQ.equalsIgnoreCase("falso") || condBoolHQ.equalsIgnoreCase("0")) {
                                consola = ejecutarSentenciasMain(nodo.getHijos().get(1), consola, ambito + "@" + "whileNeg");
                            } else {
                                break;
                            }
                        }
                    }

                    if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "whileNeg")) {
                        //Limpio este ambito en la pila actual
                        pilaSimbolos.pop();
                    }
                } else {
                    System.out.println("Condicion del Hasta que no es de tipo boolean:");
                }
                return consola;

            case "PARA":
                String tipoP = nodo.getHijos().get(0).getHijos().get(0).getEtiqueta();
                String idP = nodo.getHijos().get(0).getHijos().get(1).getEtiqueta();
                int lineaP = nodo.getHijos().get(0).getHijos().get(1).getFila();
                int colP = nodo.getHijos().get(0).getHijos().get(1).getColumna();
                String valIniP = evaluarExpresion(nodo.getHijos().get(0).getHijos().get(2), ambito);
                String inc_Dec = nodo.getHijos().get(2).getHijos().get(1).getEtiqueta();

                String valP = "";
                String tipoValP = "";
                String[] condP = valIniP.split("@");
                if (valIniP.contains("@")) {
                    valP = condP[0];
                    tipoValP = condP[1];
                }

                Simbolo simbP = new Simbolo(tipoP, idP, valP, lineaP, colP, ambito + "@para");
                TablaSimbolo tsPara = new TablaSimbolo(ambito + "@" + "para");
                tsPara.insertar(idP, simbP);
                pilaSimbolos.push(tsPara);
                tsVarsFuncion.insertar(idP, simbP);

                String condFinP = evaluarExpresion(nodo.getHijos().get(1).getHijos().get(0), ambito);

                String[] condPara = condFinP.split("@");
                String condBoolP = condPara[0];
                String tipoCondP = condPara[1];

                if (tipoCondP.equalsIgnoreCase("booleano")) {
                    if (condBoolP.equalsIgnoreCase("verdadero") || condBoolP.equalsIgnoreCase("1")) {
                        consola = ejecutarSentenciasMain(nodo.getHijos().get(3), consola, ambito + "@" + "para");

                        //Aqui realizo la iteracion del for
                        while (true) {
                            condFinP = evaluarExpresion(nodo.getHijos().get(1).getHijos().get(0), ambito);
                            String[] condPara2 = condFinP.split("@");
                            condBoolP = condPara2[0];
                            tipoCondP = condPara2[1];

                            Simbolo s = tsVarsFuncion.retornarSimbolo(idP);

                            if (condBoolP.equalsIgnoreCase("verdadero") || condBoolP.equalsIgnoreCase("1")) {
                                //Aqui realizo el incremento o decremento
                                if (inc_Dec.equalsIgnoreCase("++")) {
                                    if (s.getTipo().equalsIgnoreCase("entero")) {
                                        Integer n = Integer.parseInt(s.getValor()) + 1;
                                        s.setValor(String.valueOf(n));
                                    } else {
                                        double n = Double.parseDouble(s.getValor()) + 1;
                                        s.setValor(String.valueOf(n));
                                    }

                                } else {
                                    if (s.getTipo().equalsIgnoreCase("entero")) {
                                        Integer n = Integer.parseInt(s.getValor()) + 1;
                                        s.setValor(String.valueOf(n));
                                    } else {
                                        double n = Double.parseDouble(s.getValor()) + 1;
                                        s.setValor(String.valueOf(n));
                                    }
                                }

                                consola = ejecutarSentenciasMain(nodo.getHijos().get(3), consola, ambito + "@" + "para");

                            } else {
                                break;
                            }
                        }
                    }

                    if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "para")) {
                        //Limpio este ambito en la pila actual
                        pilaSimbolos.pop();
                    }
                } else {
                    System.out.println("Condicion del para no es boolean en fila:");
                }

                return consola;

            case "DIBUJAR_EXP":
                dibujarEXP(nodo.getHijos().get(0));
                return consola;

            case "DIBUJAR_AST":
                Enumeration e = tsFunciones.tabla.keys();
                Object clave;
                Funcion valor;
                while (e.hasMoreElements()) {
                    clave = e.nextElement();
                    valor = tsFunciones.tabla.get(clave);
                    System.out.println("Tipo: " + valor.getTipo() + " id: " + valor.getNombre()
                            + " Fila: " + valor.getFila() + " Columna: " + valor.getColumna() + " No param: " + valor.getNoParametros());
                    if (valor.getNombre().equalsIgnoreCase(nodo.getHijos().get(0).getEtiqueta())) {
                        dibujarAST(valor.getCuerpo());
                        return consola;
                    }
                }
                System.out.println("No se puede generar el ast de la funcion, ya que no existe");
                return consola;
        }
        return "";
    }

    private static void dibujarAST(Nodo raiz) throws FileNotFoundException, UnsupportedEncodingException {
        String archivoTxT = "C:\\Users\\Hellen\\Desktop" + "\\DibujarAST.txt";
        File file1 = new File(archivoTxT);

        PrintWriter writer = new PrintWriter(file1, "UTF-8");

        writer.println(ControlDot.getDotDibujarAST(raiz));
        writer.close();
        generarGrafica(archivoTxT, "DibujarAST");
    }

    public static void dibujarEXP(Nodo AST) throws FileNotFoundException, UnsupportedEncodingException {
        String archivoTxT = "C:\\Users\\Hellen\\Desktop" + "\\DibujarEXP.txt";
        File file1 = new File(archivoTxT);

        PrintWriter writer = new PrintWriter(file1, "UTF-8");

        writer.println(ControlDot.getDotDibujarEXP(AST));
        writer.close();
        generarGrafica(archivoTxT, "DibujarEXP");
    }

    public static Simbolo obtenerTipo(String tipoAmbito, String id) {
        TablaSimbolo ts = null;
        Simbolo var = null;
        if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
            if (EjecucionLenguajeAsa.pilaSimbolos.empty()) {
                ts = tsGlobal;
                if (ts.existeSimbolo(id)) {
                    var = ts.retornarSimbolo(id);
                } else {
                    System.out.println("Error semantico no existe var");
                }
            } else {
                if (EjecucionLenguajeAsa.pilaSimbolos.size() > 0) {
                    ts = EjecucionLenguajeAsa.pilaSimbolos.peek();
                    if (ts.existeSimbolo(id)) {
                        var = ts.retornarSimbolo(id);
                    } else {
                        System.out.println("Error semantico no existe var");
                    }
                } else {

                }
            }
        }
        return var;
    }

    public static String obtenerVarsParam(Nodo nodo) {
        String parametros = "";
        switch (nodo.getEtiqueta()) {
            case "LISTA_PARAM":
                switch (nodo.getHijos().size()) {
                    case 1:
                        String id = nodo.getHijos().get(0).getEtiqueta();
                        return id;
                    case 2:
                        parametros += obtenerVarsParam(nodo.getHijos().get(0));
                        id = nodo.getHijos().get(1).getEtiqueta();
                        parametros += "," + id.toLowerCase();
                        break;
                }
                break;
            default:
        }
        return parametros;
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
                                return "@error";
                            }
//                            Object resultado = realizarOperacionAritmeticas(signo, n1Tipo, n1Val, n2Tipo, n2Val);
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (n.getTipoVar().equalsIgnoreCase("Identificador")) {
                String id = n.getEtiqueta();

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

                    } else {
                        ts = tsGlobal;
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
                    }
                } else if (tipoAmbito.equalsIgnoreCase("ambitoGlobal")) {
                    ts = tsGlobal;
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
                    //Si no esta en la cima, busca en las variables globales
                    if (!pilaSimbolos.peek().tabla.isEmpty()) {
                        ts = pilaSimbolos.peek();
                    } else {
                        ts = tsVarsFuncion;
                    }
                    if (ts.existeSimbolo(id)) {
                        String val = ts.retornarSimbolo(id).getValor();
                        String tipo = ts.retornarSimbolo(id).getTipo();
                        String term = val + "@" + tipo;
                        return term;
                    } else {
                        ts = tsGlobal;
                        if (ts.existeSimbolo(id)) {
                            String val = ts.retornarSimbolo(id).getValor();
                            String tipo = ts.retornarSimbolo(id).getTipo();
                            String term = val + "@" + tipo;
                            //Si la variable no la encuentra en el ambito actual y si en la global, agrego esta al ambito actual
                            Simbolo addVarFunctionActual = ts.retornarSimbolo(id);
                            tsVarsFuncion.insertar(id, addVarFunctionActual);
                            return term;
                        } else {
                            System.out.println("Bitacora La variable " + id + " no esta declarada"
                                    + "linea: " + n.getFila() + " columna: " + n.getColumna());

                            return "error";
                        }

                    }
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

        key = id.toLowerCase() + "_" + parametros;

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

    public static String obtenerParametros(Nodo nodo) {
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
