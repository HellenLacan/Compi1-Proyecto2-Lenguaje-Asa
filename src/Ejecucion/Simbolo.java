/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

/**
 *
 * @author Hellen
 */
public class Simbolo {
    /*En esta clase almacenaremos los distintos valores de nuestras variables o simbolos del lenguaje*/
    private String nombre;
    private String valor;
    private String tipo;
    private String tipoObjeto; //Si es una matriz,objeto, variable primitiva, etc...
    private int linea;
    private int columna;
    private String ambito;

    public Simbolo(String tipo, String nombre, String valor, int linea, int columna, String ambito) {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.ambito = ambito;
        this.tipoObjeto = "";
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}
