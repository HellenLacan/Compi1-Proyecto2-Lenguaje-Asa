/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import ControlDot.ControlDot;
import static ControlDot.generarGrafica.generarGrafica;
import static Ejecucion.AsignacionVars.actualizarVariableGlobal;
import static Ejecucion.AsignacionVars.castearImplicitamente;
import static Ejecucion.AsignacionVars.verificarCasteo;
import EjecucionExpresiones.ExpresionAritmetica;
import Ejecucion.PalabraReservada.Reservada;
import Form.VentanaPrincipal;
import fuentes.Nodo;
import fuentes.parser;
import fuentes.scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static String consola = "";
    public static ArrayList<Object> valRetorno = new ArrayList<Object>();
    public static ArrayList<Nodo> cuerpoCase = new ArrayList<Nodo>();
    public static String ruta = "";
    public static ArrayList<String> imagen = new ArrayList<String>();
    public static ArrayList<String> imports = new ArrayList<String>();
    public static ArrayList<Error> listaErrores = new ArrayList<Error>();

    public Nodo AST;

    public EjecucionLenguajeAsa() {
    }

    public void almacenarImportsYRutas(Nodo nodo, String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException, Exception {
        switch (nodo.getEtiqueta()) {
            case "CUERPO_PRINCIPAL":
                for (Nodo item : nodo.getHijos()) {
                    almacenarImportsYRutas(item, nombreArchivo);
                }
                break;

            case "DEFINIR":
                File af = new File(nodo.getHijos().get(1).getEtiqueta());
                if (af.exists()) {
                    ruta = af.toString();
                } else {
                    listaErrores.add(new Error("General", "Archivo " + af + ".asa no existe", nombreArchivo, nodo.getFila(), nodo.getColumna()));
                    ruta = "C:\\Users\\Hellen\\Desktop\\ruta";
                }
                break;

            case "IMPORTAR":
                File files = new File("C:\\Users\\Hellen\\Desktop\\dot");
                String nombre = nodo.getHijos().get(1).getEtiqueta() + ".asa";
                Object rutaAbsoluta = buscarArchivo(nombre, files);
                if (rutaAbsoluta != null) {
                    analizarImport((String) rutaAbsoluta, nombre);
                } else {
                    listaErrores.add(new Error("General", "Archivo " + nombre + " no existe", nombreArchivo, nodo.getHijos().get(0).getFila(), nodo.getHijos().get(0).getColumna()));
                }
                break;
            default:
        }
    }

    public void analizarImport(String ruta, String nombreArchivo) throws Exception {
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
            System.out.println("Inicia analisis...");
            if (!"".equals(contenido)) {
                scanner scan = new scanner(new BufferedReader(new StringReader(contenido)));
                parser parser = new parser(scan);
                parser.parse();
                Nodo AST = parser.padre;

                String archivoTxT = "C:\\Users\\Hellen\\Desktop" + "\\GrafoApila.txt";
                File file1 = new File(archivoTxT);

                PrintWriter writer = new PrintWriter(file1, "UTF-8");

                writer.println(ControlDot.getDot(AST));
                writer.close();
                generarGrafica(archivoTxT, "AST", "C:\\Users\\Hellen\\Desktop");
                System.out.println("Finaliza analisis...");

                almacenarImportsYRutas(AST.getHijos().get(0), nombreArchivo);
                almacenarVariablesGlobales(AST.getHijos().get(0), nombreArchivo);
                almacenarFunciones(AST.getHijos().get(0), nombreArchivo);

            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Object buscarArchivo(String argFichero, File argFile) {
        File[] lista = argFile.listFiles();
        if (lista != null) {
            for (File elemento : lista) {
                if (elemento.isDirectory()) {
                    buscarArchivo(argFichero, elemento);
                } else if (argFichero.equalsIgnoreCase(elemento.getName())) {
                    imports.add(elemento.getAbsolutePath());
                    //System.out.println(elemento.getParentFile());
                    return elemento.getAbsolutePath();
                }
            }
        }
        return null;
    }

    public void almacenarVariablesGlobales(Nodo nodo, String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException {
        switch (nodo.getEtiqueta()) {
            case "CUERPO_PRINCIPAL":
                for (Nodo item : nodo.getHijos()) {
                    almacenarVariablesGlobales(item, nombreArchivo);
                }
                break;
            case "DECLARACION_VARIABLES":
                if (nodo.getHijos().get(1).getEtiqueta().equalsIgnoreCase("LISTA_VARIABLES")) {
                    Nodo tipo = nodo.getHijos().get(0);
                    Nodo exp = nodo.getHijos().get(2);
                    DeclaracionVar.recorrerListaVars(tipo, nodo.getHijos().get(1), exp, "ambitoGlobal", nombreArchivo);
                }
                break;
            case "ASIGNACIONES":
                AsignacionVars.asignacionAVariables(nodo, "ambitoGlobal", nombreArchivo);
                break;

            default:
        }
    }

    public void almacenarFunciones(Nodo nodo, String nombreArchivo) {
        switch (nodo.getEtiqueta()) {
            case "CUERPO_PRINCIPAL":
                for (Nodo item : nodo.getHijos()) {
                    almacenarFunciones(item, nombreArchivo);
                }
                break;
            case "FUNCION":
                agregarFunciones(nodo, nombreArchivo);
                break;
            case "METODO":
                agregarFunciones(nodo, nombreArchivo);
                break;
            case "METODO_PRINCIPAL":
                agregarFunciones(nodo, nombreArchivo);
                break;
            default:
        }
    }

    public static String ejecutarMain(String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException {
        String msj = "";

        if (tsFunciones.existeFuncion("vacio_principal")) {
            TablaSimbolo tsMain = new TablaSimbolo("ambitoMain");
            pilaSimbolos.push(tsMain);
            Funcion sentencias = tsFunciones.retornarFuncion("vacio_principal");
            ejecutarSentencias(sentencias.getCuerpo(), msj, "ambitoMain", nombreArchivo);
            tsFunciones = new TablaFunciones();
            System.out.println("Consolita");
            System.out.println(String.valueOf(consola));
        } else {
            System.out.println("El metodo principal no esta declarado aun");
        }
        return consola;
    }

    public static String ejecutarSentencias(Nodo nodo, String msj, String ambito, String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException {
        switch (nodo.getEtiqueta()) {
            case "LISTA_SENTENCIAS":
                switch (nodo.getHijos().size()) {
                    case 1:
                        msj = ejecutarSentencias(nodo.getHijos().get(0), msj, ambito, nombreArchivo);
                        if (msj.contains("retorno") || msj.contains("romper")) {
                            break;
                        } else {
                            return msj;
                        }
                    case 2:
                        msj = ejecutarSentencias(nodo.getHijos().get(0), msj, ambito, nombreArchivo);
                        if (msj.contains("retorno") || msj.contains("romper")) {
                            break;
                        }
                        msj = ejecutarSentencias(nodo.getHijos().get(1), msj, ambito, nombreArchivo);
                        if (msj.contains("retorno") || msj.contains("romper")) {
                            break;
                        } else {
                            return msj;
                        }
                }
                break;

            case "DECLARACION_VARIABLES":
                Nodo tipo = nodo.getHijos().get(0);
                Nodo exp = nodo.getHijos().get(2);
                DeclaracionVar.recorrerListaVars(tipo, nodo.getHijos().get(1), exp, ambito, nombreArchivo);

                return msj;

            case "ASIGNACIONES":
                AsignacionVars.asignacionAVariables(nodo, ambito, nombreArchivo);
                return msj;

            case "MOSTRAR":
                String imprimir = "";
                imprimir = evaluarExpresion(nodo.getHijos().get(0), ambito, nombreArchivo);
                String[] num1 = imprimir.split("@");
                String val = num1[0];
                consola += "> " + val + "\n";
                //System.out.println(consola);
                return consola;

            case "LLAMADA_FUNCION":
                String key = "";
                String paramsEnviados = "";
                Funcion miFuncion = null;
                String paramsEnviadosOrigin = "";

                //Si la funcion tiene parametros
                if (nodo.getHijos().get(2).getHijos().size() != 0) {
                    paramsEnviados = obtenerVarsParam(nodo.getHijos().get(2));
                    String[] params = paramsEnviados.split(",");

                    //Obtengo el tipo de las variables que van como parametros al llamar una funcion
                    List<Simbolo> vars = new ArrayList<Simbolo>();
                    for (String item : params) {
                        String[] TipoParams = item.split("@");
                        if (TipoParams[0].equalsIgnoreCase("identificador")) {
                            vars.add(obtenerTipo(ambito, TipoParams[1]));
                        } else if (TipoParams[0].equalsIgnoreCase("texto")) {
                            Simbolo s = new Simbolo("Texto", "", TipoParams[1], 0, 0, "");
                            vars.add(s);
                        } else if (TipoParams[0].equalsIgnoreCase("Entero")) {
                            Simbolo s = new Simbolo("Entero", "", TipoParams[1], 0, 0, "");
                            vars.add(s);
                        } else if (TipoParams[0].equalsIgnoreCase("Decimal")) {
                            Simbolo s = new Simbolo("Decimal", "", TipoParams[1], 0, 0, "");
                            vars.add(s);
                        } else if (TipoParams[0].equalsIgnoreCase("Booleano")) {
                            Simbolo s = new Simbolo("Booleano", "", TipoParams[1], 0, 0, "");
                            vars.add(s);
                        }

                    }

                    //Generando la llave de los parametros
                    for (Simbolo item : vars) {
                        paramsEnviadosOrigin += item.getTipo().toLowerCase();
                    }

                    key = nodo.getHijos().get(0).getEtiqueta() + "_" + paramsEnviadosOrigin;

                    //Verifico si existe la funcion
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
                        msj = ejecutarSentencias(miFuncion.getCuerpo(), msj, nodo.getHijos().get(0).getEtiqueta(), nombreArchivo);
                        //Quito el ambito de la funcion actual, quedandome solo el main

                        Funcion funActual = tsFunciones.retornarFuncion(key);

                        if (valRetorno.size() > 0) {
                            System.out.println();
                            String retornoFun = valRetorno.get(0).toString();
                            String[] valRet = retornoFun.split("@");
                            String valorRetorno = valRet[0];
                            String tipoRetorno = valRet[1];
                            int linea = Integer.parseInt(valRet[2]);
                            int columna = Integer.parseInt(valRet[3]);

                            //Aqui verifico si el valor de retorno le pertenece al tipo de la funcion
                            if (funActual.getTipo().equalsIgnoreCase("Vacio")) {
                                System.out.println("Error semantico, No se le puede asignar un valor a um metodo vacio" + " linea:" + linea + " columna: " + columna);
                                valRetorno = new ArrayList<>();
                            } else if (funActual.getTipo().equalsIgnoreCase("Entero") && tipoRetorno.equalsIgnoreCase("Entero")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Decimal") && tipoRetorno.equalsIgnoreCase("Decimal")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Texto") && tipoRetorno.equalsIgnoreCase("Texto")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Booleano") && tipoRetorno.equalsIgnoreCase("Booleano")) {
                                System.out.println("Correcto el retorno");
                            } else {
                                valRetorno = new ArrayList<>();
                                System.out.println("Error semantico, el valor de la expresion return es de tipo " + tipoRetorno
                                        + " y la funcion " + funActual.getNombre() + " es de tipo " + funActual.getTipo() + " linea:" + linea + " columna: " + columna);
                            }
                            //Limpio donde eta almacenada el retorno de la funcion
                            //valRetorno = new ArrayList<>();
                        } else {
                            if (msj.equalsIgnoreCase("")) {
                                if (funActual.getTipo().equalsIgnoreCase("Vacio")) {
                                    System.out.println("Funcion de tipo vacio no retorna nada :D... Correcto");
                                } else {
                                    if ((funActual.getTipo().equalsIgnoreCase("Entero") || (funActual.getTipo().equalsIgnoreCase("Decimal")
                                            || (funActual.getTipo().equalsIgnoreCase("booleano") || (funActual.getTipo().equalsIgnoreCase("Texto")) && (msj.equalsIgnoreCase("")))))) {
                                        System.out.println("Error semantico, funcion " + funActual.getNombre() + " no devuelve nada de tipo " + funActual.getTipo() + " en linea " + funActual.getFila());
                                    } else {
                                    }
                                }
                            }
                        }
                        pilaSimbolos.pop();
                        if (valRetorno.size() > 0) {
                            tsVarsFuncion = new TablaSimbolo("");
                            return (String) valRetorno.get(0);
                        } else {
                            return "error";
                        }
                    } else {
                        System.out.println("Funcion o parametros invalidos");
                        return msj;

                    }
                } else {
                    key = nodo.getHijos().get(0).getEtiqueta() + "_" + paramsEnviadosOrigin;

                    if (tsFunciones.existeFuncion(key)) {
                        miFuncion = tsFunciones.retornarFuncion(key);
                        //La cima de la pila sera ahora mi funcion actual
                        TablaSimbolo tsFuncion = new TablaSimbolo(nodo.getHijos().get(0).getEtiqueta(), "");
                        pilaSimbolos.push(tsFuncion);
                        msj = ejecutarSentencias(miFuncion.getCuerpo(), msj, nodo.getHijos().get(0).getEtiqueta(), nombreArchivo);
                        //Quito el ambito de la funcion actual, quedandome solo el main

                        Funcion funActual = tsFunciones.retornarFuncion(key);

                        if (valRetorno.size() > 0) {
                            System.out.println();
                            String retornoFun = valRetorno.get(0).toString();
                            String[] valRet = retornoFun.split("@");
                            String valorRetorno = valRet[0];
                            String tipoRetorno = valRet[1];
                            int linea = Integer.parseInt(valRet[2]);
                            int columna = Integer.parseInt(valRet[3]);

                            //Aqui verifico si el valor de retorno le pertenece al tipo de la funcion
                            if (funActual.getTipo().equalsIgnoreCase("Vacio")) {
                                System.out.println("Error semantico, No se le puede asignar un valor a um metodo vacio" + " linea:" + linea + " columna: " + columna);
                                valRetorno = new ArrayList<>();
                            } else if (funActual.getTipo().equalsIgnoreCase("Entero") && tipoRetorno.equalsIgnoreCase("Entero")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Decimal") && tipoRetorno.equalsIgnoreCase("Decimal")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Texto") && tipoRetorno.equalsIgnoreCase("Texto")) {
                                System.out.println("Correcto el retorno");
                            } else if (funActual.getTipo().equalsIgnoreCase("Booleano") && tipoRetorno.equalsIgnoreCase("Booleano")) {
                                System.out.println("Correcto el retorno");
                            } else {
                                valRetorno = new ArrayList<>();
                                System.out.println("Error semantico, el valor de la expresion return es de tipo " + tipoRetorno
                                        + " y la funcion " + funActual.getNombre() + " es de tipo " + funActual.getTipo() + " linea:" + linea + " columna: " + columna);
                            }
                            //Limpio donde eta almacenada el retorno de la funcion
                            //valRetorno = new ArrayList<>();
                        } else {
                            if (funActual.getTipo().equalsIgnoreCase("Vacio")) {
                                System.out.println("Funcion de tipo vacio no retorna nada :D... Correcto");
                            } else {
                                if (funActual.getTipo().equalsIgnoreCase("Texto")) {
                                    System.out.println("Valor de la funcion tipo Texto dio cadena vacia");
                                }
                            }
                        }

                        if (pilaSimbolos.peek().ambito.equalsIgnoreCase(nodo.getHijos().get(0).getEtiqueta())) {
                            pilaSimbolos.pop();
                        }

                        tsVarsFuncion = new TablaSimbolo("");

                        if (valRetorno.size() > 0) {
                            return (String) valRetorno.get(0);
                        } else {
                            return "error";
                        }

                    } else {
                        return msj;
                    }
                }

            case "ES_VERDADERO":
                //ambito
                String condicion = evaluarExpresion(nodo.getHijos().get(0), "esVerdadero", nombreArchivo);
                String[] condi = condicion.split("@");
                String condEsVerdadera = condi[0];
                String tipoCondIf = condi[1];
                if (tipoCondIf.equalsIgnoreCase("booleano")) {
                    if (condEsVerdadera.equalsIgnoreCase("verdadero") || condEsVerdadera.equalsIgnoreCase("1")) {
                        msj = ejecutarSentencias(nodo.getHijos().get(1), msj, ambito + "@" + "if", nombreArchivo);
                    } else {
                        //Si la condicion es falsa veo si el if contiene un ELSE
                        if (nodo.getHijos().size() == 3) {
                            msj = ejecutarSentencias(nodo.getHijos().get(2).getHijos().get(0), msj, ambito + "@" + "if", nombreArchivo);

                        } else {
                            //System.out.println("no tiene else");
                        }
                    }
                    if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "if")) {
                        pilaSimbolos.pop();
                    }
                } else {
                    System.out.println("Condicion del if invalida en fila: " + nodo.getFila());
                }
                //Limpio este ambito en la pila actuaol
                return msj;

            case "MIENTRAS_QUE":
                //ambito
                String condicionMientrasQ = evaluarExpresion(nodo.getHijos().get(0), "mientrasQue", nombreArchivo);
                String[] condMientras = condicionMientrasQ.split("@");
                String condBoolMQ = condMientras[0];
                String tipoCondMientras = condMientras[1];

                if (tipoCondMientras.equalsIgnoreCase("booleano")) {
                    if (condBoolMQ.equalsIgnoreCase("verdadero") || condBoolMQ.equalsIgnoreCase("1")) {
                        //iteracion del while
                        while (true) {
                            condicionMientrasQ = evaluarExpresion(nodo.getHijos().get(0), "mientrasQue", nombreArchivo);
                            String[] condMientras2 = condicionMientrasQ.split("@");
                            condBoolMQ = condMientras2[0];
                            tipoCondMientras = condMientras2[1];

                            if (condBoolMQ.equalsIgnoreCase("verdadero") || condBoolMQ.equalsIgnoreCase("1")) {
                                msj = ejecutarSentencias(nodo.getHijos().get(1), msj, ambito + "@" + "while", nombreArchivo);
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
                return msj;

            case "HASTA_QUE":
                //ambito
                String condicionHastaQue = evaluarExpresion(nodo.getHijos().get(0), "hastaQue", nombreArchivo);
                if (condicionHastaQue.contains("@")) {
                    String[] condHastaQue = condicionHastaQue.split("@");
                    String condBoolHQ = condHastaQue[0];
                    String tipoCondHQ = condHastaQue[1];

                    if (tipoCondHQ.equalsIgnoreCase("booleano")) {
                        if (condBoolHQ.equalsIgnoreCase("falso") || condBoolHQ.equalsIgnoreCase("0")) {
                            msj = ejecutarSentencias(nodo.getHijos().get(1), msj, ambito + "@" + "whileNeg", nombreArchivo);

                            //iteracion del while
                            while (true) {
                                condBoolHQ = evaluarExpresion(nodo.getHijos().get(0), "hastaQue", nombreArchivo);
                                String[] condHastaQue2 = condBoolHQ.split("@");
                                condBoolHQ = condHastaQue2[0];
                                tipoCondHQ = condHastaQue2[1];

                                if (condBoolHQ.equalsIgnoreCase("falso") || condBoolHQ.equalsIgnoreCase("0")) {
                                    msj = ejecutarSentencias(nodo.getHijos().get(1), msj, ambito + "@" + "whileNeg", nombreArchivo);
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
                } else {
                    System.out.println("Semantico Condicion invalida en hasta_que " + condicionHastaQue);
                }

                return msj;

            case "PARA":
                String tipoP = nodo.getHijos().get(0).getHijos().get(0).getEtiqueta();
                String idP = nodo.getHijos().get(0).getHijos().get(1).getEtiqueta();
                int lineaP = nodo.getHijos().get(0).getHijos().get(1).getFila();
                int colP = nodo.getHijos().get(0).getHijos().get(1).getColumna();
                String valIniP = evaluarExpresion(nodo.getHijos().get(0).getHijos().get(2), ambito, nombreArchivo);
                String inc_Dec = nodo.getHijos().get(2).getHijos().get(1).getEtiqueta();

                String valP = "";
                String tipoValP = "";

                if (valIniP.contains("@")) {
                    String[] condP = valIniP.split("@");
                    valP = condP[0];
                    tipoValP = condP[1];

                    Simbolo simbP = new Simbolo(tipoP, idP, valP, lineaP, colP, ambito + "@para");
                    TablaSimbolo tsPara = new TablaSimbolo(ambito + "@" + "para");
                    tsPara.insertar(idP, simbP);

                    pilaSimbolos.push(tsPara);
                    tsVarsFuncion.insertar(idP, simbP);

                    String condFinP = evaluarExpresion(nodo.getHijos().get(1).getHijos().get(0), ambito, nombreArchivo);

                    if (condFinP.contains("@")) {

                        String[] condPara = condFinP.split("@");
                        String condBoolP = condPara[0];
                        String tipoCondP = condPara[1];

                        if (tipoCondP.equalsIgnoreCase("booleano")) {
                            if (condBoolP.equalsIgnoreCase("verdadero") || condBoolP.equalsIgnoreCase("1")) {
                                msj = ejecutarSentencias(nodo.getHijos().get(3), msj, ambito + "@" + "para", nombreArchivo);

                                //Aqui realizo la iteracion del for
                                while (true) {
                                    condFinP = evaluarExpresion(nodo.getHijos().get(1).getHijos().get(0), ambito, nombreArchivo);
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

                                        msj = ejecutarSentencias(nodo.getHijos().get(3), msj, ambito + "@" + "para", nombreArchivo);

                                    } else {
                                        break;
                                    }
                                }
                            }

                            if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "para")) {
                                //Limpio este ambito en la pila actual
                                pilaSimbolos.pop();
                                //Como la variable del for no se puede utilizar fuera de ella, la elimino del ambito actual
                                tsVarsFuncion.tabla.remove(idP);
                            }
                        } else {
                            System.out.println("Condicion del para no es boolean en fila:");
                        }
                    } else {
                        System.out.println("Error semantico en la condicion para" + condFinP);
                    }
                } else {
                    System.out.println("Error semantico en la condicion inicial para" + valIniP);
                }

                return msj;

            case "RETORNO":
                String valorR = "";
                Nodo n = nodo;
                if (ambito.equalsIgnoreCase("ambitoMain")) {
                    System.out.println("Semantico, no se puede declarar un retorno en metodo principal");
                }
                if (nodo.getHijos().size() == 0) {
                    return "retorno" + "@" + nodo.getFila() + "@" + nodo.getColumna();
                } else {
                    valorR = evaluarExpresion(nodo.getHijos().get(0), ambito, nombreArchivo);
                    valRetorno.add(valorR + "@" + nodo.getFila() + "@" + nodo.getColumna());
                    return "retorno";
                }

            case "CONTINUAR":
                valorR = "";
                n = nodo;
                if (ambito.equalsIgnoreCase("ambitoMain")) {
                    System.out.println("Semantico, no se puede declarar un Continuar en metodo principal");
                }
                return "continuar" + "@" + nodo.getFila() + "@" + nodo.getColumna();

            case "ROMPER":
                valorR = "";
                n = nodo;
                if (ambito.equalsIgnoreCase("ambitoMain")) {
                    System.out.println("Semantico, no se puede declarar un Romper en metodo principal");
                }
                return "romper" + "@" + nodo.getFila() + "@" + nodo.getColumna();

            case "CAMBIAR_A":
                String condicionCambiarA = evaluarExpresion(nodo.getHijos().get(0), "hastaQue", nombreArchivo);
                String valorCA = "";
                String tipoCondCA = "";
                String cuerpo = "";
                if (condicionCambiarA.contains("@")) {
                    String[] condCambiarA = condicionCambiarA.split("@");
                    valorCA = condCambiarA[0];
                    tipoCondCA = condCambiarA[1];

                    if (tipoCondCA.equalsIgnoreCase("Texto") || tipoCondCA.equalsIgnoreCase("Entero") || tipoCondCA.equalsIgnoreCase("Decimal")) {
                        System.out.println("Condicion buena");
                        //Si llega un NO es que los tipos no son del mismo tipo
                        cuerpo = verificarTiposSwitch(nodo.getHijos().get(1), tipoCondCA);
                        if (!cuerpo.equalsIgnoreCase("@No")) {
                            String valorS = verificarCasosSwitch(nodo.getHijos().get(1), tipoCondCA, valorCA);
                            if (valorS.equalsIgnoreCase("@Yes")) {
                                ejecutarSentencias(cuerpoCase.get(0), msj, ambito + "@" + "switch", nombreArchivo);
                            } else if (nodo.getHijos().size() == 3) {
                                ejecutarSentencias(nodo.getHijos().get(2).getHijos().get(0), msj, ambito + "@" + "switch", nombreArchivo);
                            } else {
                                System.out.println("Ninguna opcion es valida, y no contiene sentencia no_cumple");
                            }
                        }

                        if (pilaSimbolos.peek().ambito.equalsIgnoreCase(ambito + "@" + "switch")) {
                            //Limpio este ambito en la pila actual
                            pilaSimbolos.pop();
                            cuerpoCase = new ArrayList<>();
                        }

                    } else {
                        System.out.println("Error semantico condicion invalida en linea:");
                    }

                } else {
                    System.out.println("Error semantico en la condicion " + condicionCambiarA);
                }

                return msj;

            case "DIBUJAR_EXP":
                dibujarEXP(nodo.getHijos().get(0));
                return msj;

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
                        return msj;
                    }
                }
                System.out.println("No se puede generar el ast de la funcion, ya que no existe");
                return msj;
        }
        return msj;
    }
    public static String n = "";

    private static String verificarTiposSwitch(Nodo nodo, String tipo) {
        if (nodo.getTipo().equalsIgnoreCase("NoTerm")) {
            switch (nodo.getHijos().size()) {
                case 1:
                    if (nodo.getEtiqueta().equalsIgnoreCase("LISTA_CASOS")) {
                        n = verificarTiposSwitch(nodo.getHijos().get(0), tipo);
                        if (n.contains("No")) {
                            break;
                        } else {
                            return n;
                        }
                    }

                case 2:
                    if (nodo.getEtiqueta().equalsIgnoreCase("LISTA_CASOS")) {
                        n = verificarTiposSwitch(nodo.getHijos().get(0), tipo);
                        if (n.contains("Yes")) {
                            cuerpoCase.add(nodo.getHijos().get(1));
                        } else if (n.contains("No")) {
                            break;
                        }

                        n = verificarTiposSwitch(nodo.getHijos().get(1), tipo);

                        if (n.contains("No")) {
                            break;
                        } else {
                            return n;
                        }

                    }
                    return n;
            }
        } else {
            String value = nodo.getEtiqueta();
            String type = "";
            try {
                Double numero = Double.parseDouble(value);
                if ((numero % 2) == 0) {
                    type = "Entero";
                } else {
                    type = "Decimal";
                }
            } catch (Exception e) {
                type = "Texto";
            }
            if (!type.equalsIgnoreCase(tipo)) {
                System.out.println("Error semantico, valor debe de ser tipo " + tipo + " y se encontro uno de tipo " + type
                        + " en linea: " + nodo.getFila() + " columna: " + nodo.getColumna());
                return "@No";
            }
        }
        return n;
    }

    private static String verificarCasosSwitch(Nodo nodo, String tipo, String valor) {
        if (nodo.getTipo().equalsIgnoreCase("NoTerm")) {
            switch (nodo.getHijos().size()) {
                case 1:
                    if (nodo.getEtiqueta().equalsIgnoreCase("LISTA_CASOS")) {
                        n = verificarCasosSwitch(nodo.getHijos().get(0), tipo, valor);
                        if (n.contains("Yes")) {
                            break;
                        } else {
                            return n;
                        }
                    }

                case 2:
                    if (nodo.getEtiqueta().equalsIgnoreCase("LISTA_CASOS")) {
                        n = verificarCasosSwitch(nodo.getHijos().get(0), tipo, valor);
                        if (n.contains("Yes")) {
                            cuerpoCase.add(nodo.getHijos().get(1));
                            break;
                        }

                        n = verificarCasosSwitch(nodo.getHijos().get(1), tipo, valor);

                        if (n.contains("Yes")) {
                            cuerpoCase.add(nodo.getHijos().get(1));
                            break;
                        } else if (n.contains("No")) {
                            return n;
                        }

                    }
                    return n;

            }
        } else {
            String value = nodo.getEtiqueta();
            String type = "";
            try {
                Double numero = Double.parseDouble(value);
                if ((numero % 2) == 0) {
                    type = "Entero";
                } else {
                    type = "Decimal";
                }
            } catch (Exception e) {
                type = "Texto";
            }
            if (type.equalsIgnoreCase(tipo)) {

                if (type.equalsIgnoreCase("Texto")) {
                    value = value.replaceAll("\"", "");
                }

                if (value.equalsIgnoreCase(valor) && type.equalsIgnoreCase(tipo)) {
                    return "@Yes";
                } else {
                    return "@nop";
                }
            } else {
                return "@No";
            }
        }
        return n;
    }

    private static void dibujarAST(Nodo raiz) throws FileNotFoundException, UnsupportedEncodingException {
        String archivoTxT = ruta + "\\DibujarAST.txt";
        File file1 = new File(archivoTxT);

        PrintWriter writer = new PrintWriter(file1, "UTF-8");

        writer.println(ControlDot.getDotDibujarAST(raiz));
        writer.close();
        generarGrafica(archivoTxT, "DibujarAST", ruta);
    }

    public static void dibujarEXP(Nodo AST) throws FileNotFoundException, UnsupportedEncodingException {
        String archivoTxT = "C:\\Users\\Hellen\\Desktop\\dot" + "\\DibujarEXP1.txt";
        File file1 = new File(archivoTxT);

        PrintWriter writer = new PrintWriter(file1, "UTF-8");

        writer.println(ControlDot.getDotDibujarEXP(AST));
        writer.close();
        generarGrafica(archivoTxT, "EXP" + imagen.size() + 1, ruta);
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
                        ts = tsGlobal;
                        if (ts.existeSimbolo(id)) {
                            var = ts.retornarSimbolo(id);
                        } else {
                            System.out.println("Error semantico no existe var");
                        }
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
                        String id = nodo.getHijos().get(0).getTipoVar() + "@" + nodo.getHijos().get(0).getEtiqueta();
                        return id;
                    case 2:
                        parametros += obtenerVarsParam(nodo.getHijos().get(0));
                        id = nodo.getHijos().get(1).getTipoVar() + "@" + nodo.getHijos().get(1).getEtiqueta();
                        parametros += "," + id.toLowerCase();
                        break;

                }
                break;
            default:
        }
        return parametros;
    }

    public static String obtenerVarsParamDeFuncion(Nodo nodo) {
        String parametros = "";
        switch (nodo.getEtiqueta()) {
            case "LISTA_PARAMETROS":
                switch (nodo.getHijos().size()) {
                    case 2:
                        String id = "";
                        id = nodo.getHijos().get(0).getTipoVar() + "@" + nodo.getHijos().get(1).getEtiqueta();
                        parametros += id.toLowerCase();
                        return parametros;
                    case 3:
                        parametros += obtenerVarsParamDeFuncion(nodo.getHijos().get(0));
                        id = nodo.getHijos().get(1).getTipoVar() + "@" + nodo.getHijos().get(2).getEtiqueta();
                        parametros += "," + id.toLowerCase();
                        return parametros;
                }
                break;
            default:
        }
        return parametros;
    }

    //Tipo es para ver si esta con globales o locales
    public static String evaluarExpresion(Nodo n, String tipoAmbito, String nombreArchivo) throws FileNotFoundException, UnsupportedEncodingException {

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

                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito, nombreArchivo);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito, nombreArchivo);

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
                    n1 += evaluarExpresion(n.getHijos().get(1), tipoAmbito, nombreArchivo);
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
                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito, nombreArchivo);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito, nombreArchivo);

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

                            n1 += evaluarExpresion(n.getHijos().get(0), tipoAmbito, nombreArchivo);
                            signo += n.getHijos().get(1).getEtiqueta();
                            n3 += evaluarExpresion(n.getHijos().get(2), tipoAmbito, nombreArchivo);

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

                case "NEG":
                    n1 = evaluarExpresion(n.getHijos().get(1), tipoAmbito, nombreArchivo);
                    if (n1.contains("@")) {
                        String[] tipoN = n1.split("@");
                        if (tipoN[1].equalsIgnoreCase("booleano")) {
                            if (tipoN[0].equalsIgnoreCase("verdadero") || tipoN[0].equalsIgnoreCase("1")) {
                                return "falso@" + tipoN[1];
                            } else if (tipoN[0].equalsIgnoreCase("falso") || tipoN[0].equalsIgnoreCase("0")) {
                                return "verdadero@" + tipoN[1];
                            }
                        } else {
                            System.out.println("Semantico, No se puede negar una condicion que no sea de tipo booleano ");
                            return "error";
                        }

                    }
                    System.out.println("es neg");
                    break;

                case "LLAMADA_FUNCION":
                    //ejecutarSentencias(Nodo nodo, String msj, String ambito
                    String valor = ejecutarSentencias(n, "", tipoAmbito, nombreArchivo);
                    int fila = 0;
                    int columna = 0;
                    String valorF = "";
                    String tipoF = "";

                    if (valor.contains("@")) {
                        String[] valores = valor.split("@");
                        valorF = valores[0];
                        tipoF = valores[1];
                        fila = Integer.parseInt(valores[2]);
                        columna = Integer.parseInt(valores[3]);

                        System.out.println("Bitacora El valor de retorno de la funcion " + n.getHijos().get(0).getEtiqueta() + " es: " + valorF
                                + " en fila: " + fila + " columna: " + columna);
                        System.out.println("m");
                        return valorF + "@" + tipoF;

                    } else {
                        if (valor.equalsIgnoreCase("error")) {
                            return "error";
                        } else if (!valorF.equalsIgnoreCase("")) {
                            return valorF + "@" + tipoF;
                        } else {
                            return "" + "@" + "Texto";
                        }
                    }

                default:
                    break;
            }
        } else {
            if (n.getTipoVar().equalsIgnoreCase("Identificador")) {
                String id = n.getEtiqueta();

                if (tipoAmbito.equalsIgnoreCase("ambitoMain")) {
                    if (!pilaSimbolos.empty()) {

                        ts = pilaSimbolos.peek();

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
                        ts = tsVarsFuncion;
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

    public void agregarFunciones(Nodo nodo, String nombreArchivo) {
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
            agregarMetodoPrincipal(nodo, nombreArchivo);
        } else {
            if (!IdesPalabraReservada(id) && nodo.getHijos().size() > 3) {
                if (!tsFunciones.existeFuncion(key)) {

                    String paramsFuncion = obtenerVarsParamDeFuncion(nodo.getHijos().get(2));
                    if (!paramsFuncion.equalsIgnoreCase("")) {
                        String[] params = paramsFuncion.split(",");

                        //Aqui verifico que los id no sean repetidos como (String a, String a)
                        Hashtable<String, String> contVars = new Hashtable<String, String>();
                        boolean idRepetido = false;
                        for (String item : params) {
                            String[] parametro = item.split("@");
                            if (contVars.contains(parametro[1])) {
                                idRepetido = true;
                            } else {
                                contVars.put(parametro[0], parametro[1]);
                            }
                        }

                        //Si no hay ninguna variable repetida agrego la funcion
                        if (idRepetido == false) {
                            cuerpoFuncion = nodo.getHijos().get(3);
                            Funcion miFuncion = new Funcion(id, tipo, fila, columna, cuerpoFuncion, "");
                            agregarParametros(nodo.getHijos().get(2), miFuncion);
                            tsFunciones.insertar(key, miFuncion);
                        } else {
                            listaErrores.add(new Error("Semantico", "Funcion " + tipo + " " + id + " contiene variables repetidas en parametros", nombreArchivo, fila, columna));

                            //System.out.println("Error semantico Funcion " + tipo + " " + id + " contiene variables repetidas en funcion"
                            //        + "fila: " + fila + "Columna: " + columna);
                        }
                    } else {
                        cuerpoFuncion = nodo.getHijos().get(3);
                        Funcion miFuncion = new Funcion(id, tipo, fila, columna, cuerpoFuncion, "");
                        agregarParametros(nodo.getHijos().get(2), miFuncion);
                        tsFunciones.insertar(key, miFuncion);
                    }

                } else {
                    listaErrores.add(new Error("Semantico", "Funcion " + tipo + " " + id + " ya fue declara anteriormente", nombreArchivo, fila, columna));
                }
            } else {
                System.out.println("Error semantico Funcion no puede ser llamada como una palabra reservada");
            }
        }
    }

    public void agregarMetodoPrincipal(Nodo nodo, String nombreArchivo) {
        String tipo = nodo.getHijos().get(0).getEtiqueta();
        String id = nodo.getHijos().get(1).getEtiqueta();
        String key = tipo.toLowerCase() + "_" + id.toLowerCase();
        int fila = nodo.getHijos().get(1).getFila();
        int columna = nodo.getHijos().get(1).getColumna();
        Nodo cuerpoFuncion = nodo.getHijos().get(2);

        //Verificando si el id es una palabra reservada
        if (!tsFunciones.existeFuncion(key)) {
            Funcion miFuncion = new Funcion(id, tipo, fila, columna, cuerpoFuncion, "");
            agregarParametros(nodo.getHijos().get(2), miFuncion);
            tsFunciones.insertar(key, miFuncion);
        } else {
            listaErrores.add(new Error("Semantico", "Metodo principal ya fue declarado anteriormente", nombreArchivo, fila, columna));
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
