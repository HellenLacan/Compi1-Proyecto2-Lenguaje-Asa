/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlDot;

import java.io.File;

/**
 *
 * @author Hellen
 */
public class generarGrafica {
    
    public static void generarGrafica(String Archivo, String img, String ruta) {
        try {

            String dotPath = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";

            String fileInputPath = Archivo;
            String fileOutputPath = ruta + "\\"+img + ".jpg";

            File file = new File(fileOutputPath);

            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
            }

            String tParam = "-Tjpg";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();

            rt.exec(cmd);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }
}
