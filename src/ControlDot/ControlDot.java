/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlDot;

import fuentes.Nodo;

/**
 *
 * @author Hellen
 */
public class ControlDot {

    private static int contador;
    private static String grafo;

    public static String getDot(Nodo raiz) {
        grafo = "digraph G{";
        grafo += "nodo0[label=\"" + raiz.getEtiqueta() + "\"];\n";
        contador = 1;
        recorrerAST("nodo0", raiz);
        grafo += "}";

        return grafo;
    }

    private static void recorrerAST(String padre, Nodo raiz) {
        for (Nodo hijo : raiz.getHijos()) {
            if (hijo != null) {
                if (hijo.getEtiqueta().contains("\"")) {
                    String nombreHijo = "nodo" + contador;
                    String etiqueta = hijo.getEtiqueta().replaceAll("\"", "");
                    grafo += nombreHijo + "[label=\"" + "\\\"" + etiqueta + "\\\"" + "\"];\n";
                    grafo += padre + "->" + nombreHijo + ";\n";
                    contador++;
                    recorrerAST(nombreHijo, hijo);
                } else {
                    String nombreHijo = "nodo" + contador;
                    grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
                    grafo += padre + "->" + nombreHijo + ";\n";
                    contador++;
                    recorrerAST(nombreHijo, hijo);
                }
            }

        }
    }

    public static String getDotDibujarEXP(Nodo raiz) {
        String nombreHijo = "";
        grafo = "digraph G{";
        grafo += "node [shape=record];\n";
        String etiqueta = "";
        if (raiz.getHijos().size() == 3) {
            etiqueta = raiz.getEtiqueta() + "|\\" + raiz.getHijos().get(1).getEtiqueta();
        }
        grafo += "nodo0[label=\"" + etiqueta + "\"];\n";
        contador = 1;
        recorrerASTDibEXP("nodo0", raiz);
        grafo += "}";

        return grafo;
    }

    private static void recorrerASTDibEXP(String padre, Nodo raiz) {
        String nombreHijo = "";
        for (Nodo hijo : raiz.getHijos()) {
            if (hijo != null) {
                if (hijo.getEtiqueta().equalsIgnoreCase("EXPR_ARIT") || hijo.getEtiqueta().equalsIgnoreCase("EXPR_REL")
                        || hijo.getEtiqueta().equalsIgnoreCase("EXPR_LOGICA")) {
                    nombreHijo = "nodo" + contador;
                    grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "|\\" + hijo.getHijos().get(1).getEtiqueta() + "" + "\"];\n";
                    grafo += padre + "->" + nombreHijo + ";\n";
                    contador++;
                } else if (hijo.getEtiqueta().equalsIgnoreCase("LLAMADA_FUNCION")) {
                    nombreHijo = "nodo" + contador;
                    grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "|" + hijo.getHijos().get(0).getEtiqueta() + "" + "\"];\n";
                    grafo += padre + "->" + nombreHijo + ";\n";
                    contador++;
                } else if (hijo.getEtiqueta().equalsIgnoreCase("+") || hijo.getEtiqueta().equalsIgnoreCase("*")
                        || hijo.getEtiqueta().equalsIgnoreCase("/") || hijo.getEtiqueta().equalsIgnoreCase("^")
                        || hijo.getEtiqueta().equalsIgnoreCase("%") || hijo.getEtiqueta().equalsIgnoreCase(">")
                        || hijo.getEtiqueta().equalsIgnoreCase("<") || hijo.getEtiqueta().equalsIgnoreCase(">=")
                        || hijo.getEtiqueta().equalsIgnoreCase("<=") || hijo.getEtiqueta().equalsIgnoreCase("!=") || hijo.getEtiqueta().equalsIgnoreCase("<=") || hijo.getEtiqueta().equalsIgnoreCase("!=")
                        || hijo.getEtiqueta().equalsIgnoreCase("||") || hijo.getEtiqueta().equalsIgnoreCase("&&")
                        || hijo.getEtiqueta().equalsIgnoreCase("!") || hijo.getTipoVar().equalsIgnoreCase("ID_LLAMADA_FUN")
                        || hijo.getEtiqueta().equalsIgnoreCase("==") || hijo.getEtiqueta().equalsIgnoreCase("LLAMADA_FUNCION")
                        || hijo.getEtiqueta().equalsIgnoreCase("LISTA_PARAM")) {
                } else {
                    if (hijo.getTipoVar().equalsIgnoreCase("Texto")) {
                        nombreHijo = "nodo" + contador;
                        grafo += nombreHijo + "[label=\"" + "cadena" + " |" + "\\\"" + hijo.getEtiqueta() + "\\\"" + "\"];\n";
                        grafo += padre + "->" + nombreHijo + ";\n";
                        contador++;
                    } else {
                        nombreHijo = "nodo" + contador;
                        grafo += nombreHijo + "[label=\"" + hijo.getTipoVar() + " | " + hijo.getEtiqueta() + "\"];\n";
                        grafo += padre + "->" + nombreHijo + ";\n";
                        contador++;
                    }

                }
                recorrerASTDibEXP(nombreHijo, hijo);
            }
        }
    }

    public static String getDotDibujarAST(Nodo raiz) {
        grafo = "digraph G{";
        grafo += "rankdir=LR;\n";
        grafo += "node [shape=record];\n";
        grafo += "nodo0[label=\"" + raiz.getEtiqueta() + "\"];\n";
        contador = 1;
        dibujarAST("nodo0", raiz);
        grafo += "}";

        return grafo;
    }

    private static void dibujarAST(String padre, Nodo raiz) {
        String nombreHijo = "";
        for (Nodo hijo : raiz.getHijos()) {
            if (hijo != null) {
                if (hijo.getTipo().equalsIgnoreCase("NoTerm")) {
                    if (hijo.getEtiqueta().equalsIgnoreCase("RETORNO")) {
                        nombreHijo = "nodo" + contador;
                        grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
                        grafo += padre + "->" + nombreHijo + ";\n";
                        contador++;

                        if (hijo.getHijos().size() != 0) {
                            nombreHijo = "nodo" + contador;
                            grafo += nombreHijo + "[label=\"" + "EXPRESION" + "\"];\n";
                            grafo += "nodo" + (contador - 1) + "->" + nombreHijo + ";\n";
                            contador++;
                        }
                    } else if (hijo.getEtiqueta().equalsIgnoreCase("EXPR") || hijo.getEtiqueta().equalsIgnoreCase("EXPR_REL")) {
                        nombreHijo = "nodo" + contador;
                        grafo += nombreHijo + "[label=\"" + "EXPRESION" + "\"];\n";
                        grafo += padre + "->" + nombreHijo + ";\n";
                        contador++;
                    } else {
                        nombreHijo = "nodo" + contador;
                        grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
                        grafo += padre + "->" + nombreHijo + ";\n";
                        contador++;
                        dibujarAST(nombreHijo, hijo);
                    }

                }
            }
        }
    }
}

//    private static void dibujarAST(String padre, Nodo raiz) {
//        String nombreHijo = "";
//        for (Nodo hijo : raiz.getHijos()) {
//            if (hijo != null) {
//                if (hijo.getTipo().equalsIgnoreCase("NoTerm")) {
//                    if (hijo.getEtiqueta().equalsIgnoreCase("RETORNO")) {
//                        nombreHijo = "nodo" + contador;
//                        grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
//                        grafo += padre + "->" + nombreHijo + ";\n";
//                        contador++;
//
//                        if (hijo.getHijos().size() != 0) {
//                            nombreHijo = "nodo" + contador;
//                            grafo += nombreHijo + "[label=\"" + "EXPR" + "\"];\n";
//                            grafo += "nodo" + (contador - 1) + "->" + nombreHijo + ";\n";
//                            contador++;
//                        }
//                    } else if (hijo.getEtiqueta().equalsIgnoreCase("EXPR")) {
//                        nombreHijo = "nodo" + contador;
//                        grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
//                        grafo += padre + "->" + nombreHijo + ";\n";
//                        contador++;
//                    } else {
//                        nombreHijo = "nodo" + contador;
//                        grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta() + "\"];\n";
//                        grafo += padre + "->" + nombreHijo + ";\n";
//                        contador++;
//                        dibujarAST(nombreHijo, hijo);
//                    }
//                }
//            }
//        }
//    }
