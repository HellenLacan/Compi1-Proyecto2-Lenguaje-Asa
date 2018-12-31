/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2_201325674;

import fuentes.parser;
import fuentes.scanner;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 *
 * @author Hellen
 */
public class Proyecto2_201325674 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      try {
            String texto="Definir";
            System.out.println("Inicia analisis...");
            scanner scan = new scanner(new BufferedReader( new StringReader(texto)));
            parser parser = new parser(scan);
            parser.parse();
            System.out.println("Finaliza analisis...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
