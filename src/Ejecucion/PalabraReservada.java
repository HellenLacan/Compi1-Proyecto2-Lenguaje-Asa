/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

/**
 * Esta clase es utilizada para luego verificar si un identificador es una palabra reservada, si lo es
 * lo toma como un error semantico.
 * @author Hellen
 */
public class PalabraReservada {
    public static enum Reservada {
        Public,
        Principal,
        Importar,
        Definir,
        Decimal,
        Booleano,
        Texto,
        Entero,
        Vacio,
        Verdadero,
        falso,
        principal,
        Retorno,
        Es_Verdadero,
        Es_falso,
        Cambiar_A,
        Valor,
        No_cumple,
        Para,
        Hasta_Que,
        Mientras_que,
        Romper,
        Continuar,
        Mostrar,
        DibujarAST,
        DibujarEXP,
        DibujarTS,
        asa
    }
}
