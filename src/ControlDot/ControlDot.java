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

    private static void recorrerAST(String padre, Nodo hijos) {
            for (Nodo hijo : hijos.getHijos()) {
                    String nombreHijo = "nodo" + contador;
                    grafo += nombreHijo + "[label=\"" + hijo.getEtiqueta()+ "\"];\n";
                    grafo += padre + "->" + nombreHijo + ";\n";
                    contador++;
                recorrerAST(nombreHijo, hijo);
            }
    }
}
