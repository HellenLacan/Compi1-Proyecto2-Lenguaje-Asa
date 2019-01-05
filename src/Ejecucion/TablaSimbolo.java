/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import java.util.Hashtable;

/**
 *
 * @author Hellen
 */
public class TablaSimbolo {
    public String nivel;
    public String tipo;
    Hashtable<String,Simbolo> tabla = new Hashtable<String,Simbolo>();
    
    public TablaSimbolo(){
            
    }
    
    public TablaSimbolo(String nivel, String tipo){
            
    }
    
    public void insertar(String nombre, Simbolo simbolo){
    }
    
    public Simbolo retornarSimbolo(String nombre){
        return null;
    }
    
    public Boolean existeSimbolo(String nombre){
        return false;
    }

}
