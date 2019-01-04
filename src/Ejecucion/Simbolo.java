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
    private String linea;
    private String columna;
    private String visibilidad;
    private String ambito;

    public Simbolo(String nombre, String valor, String tipo, String linea, String columna, String ambito) {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.ambito = ambito;
        this.tipoObjeto = "";
        this.visibilidad = "";
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

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}
