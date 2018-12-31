/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuentes;

import java.util.ArrayList;

/**
 *
 * @author Hellen
 */
public class Clase {

    private String nombre="";
    private int no_lineas=0;
    private ArrayList<Variable> arrayVariables = new ArrayList<Variable>();

    public Clase() {
    }
        
    public Clase(String nombre,ArrayList<Variable> arrayVariables) {
        this.nombre=nombre;
        this.arrayVariables=arrayVariables;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNo_lineas() {
        return no_lineas;
    }

    public void setNo_lineas(int no_lineas) {
        this.no_lineas = no_lineas;
    }

    public ArrayList<Variable> getArrayVariables() {
        return arrayVariables;
    }

    public void setArrayVariables(ArrayList<Variable> arrayVariables) {
        this.arrayVariables = arrayVariables;
    }
    
    
    
}
