/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import ControlDot.ControlDot;
import ControlDot.generarGrafica;
import static ControlDot.generarGrafica.generarGrafica;
import Ejecucion.EjecucionLenguajeAsa;
import EjecucionExpresiones.ExpresionAritmetica;
import Ejecucion.TablaFunciones;
import Ejecucion.TablaSimbolo;
import fuentes.Nodo;
import fuentes.parser;
import fuentes.scanner;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Element;

/**
 *
 * @author Hellen
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VentanaPrincipal
     */
    String a = "Ale";
    double b = 20;

    private JPanel panel1;
    private Object textbox = null;
    JTextArea jta;
    Pestania[] pestania = new Pestania[25];
    ImagenG[][] imagen = new ImagenG[10][10];

    public VentanaPrincipal() throws IOException {
        initComponents();
        addTabsInPanel();

        File files = new File("C:\\Users\\Hellen\\Desktop");

        //buscarArchivo("lexiquin.jflex", files);
        Image img = ImageIO.read(getClass().getResource("open.png"));
        ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemOpen.setIcon((iconoEscala));

        Image imgNew = ImageIO.read(getClass().getResource("new.png"));
        ImageIcon iconoEscalaNew = new ImageIcon(imgNew.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemNew.setIcon((iconoEscalaNew));

        Image imgClosedTab = ImageIO.read(getClass().getResource("closedTab.png"));
        ImageIcon iconoEscalaClosedTab = new ImageIcon(imgClosedTab.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemCloseTab.setIcon((iconoEscalaClosedTab));

        Image imgAnalizar = ImageIO.read(getClass().getResource("analizar.png"));
        ImageIcon iconoEscalaAnalizar = new ImageIcon(imgAnalizar.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuEjecutar.setIcon((iconoEscalaAnalizar));

        Image imgReportes = ImageIO.read(getClass().getResource("reporte.png"));
        ImageIcon iconoEscalaReportes = new ImageIcon(imgReportes.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemReportes.setIcon((iconoEscalaReportes));

        Image imgSave = ImageIO.read(getClass().getResource("save.png"));
        ImageIcon iconoEscalaSave = new ImageIcon(imgSave.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemSave.setIcon((iconoEscalaSave));

        Image imgSaveAs = ImageIO.read(getClass().getResource("saveAs.png"));
        ImageIcon iconoEscalaSaveAs = new ImageIcon(imgSaveAs.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
        jMenuItemSaveAs.setIcon((iconoEscalaSaveAs));

        System.out.println("m" + a);
        File file = new File("C:\\Users\\Hellen\\Documents\\Cursos\\COMPI1_VACAS_DIC_2018\\Proyecto2_201325674\\src\\archivoPrueba.asa");

        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get("C:\\Users\\Hellen\\Documents\\Cursos\\COMPI1_VACAS_DIC_2018\\Proyecto2_201325674\\src\\archivoPrueba.asa")));
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String texto = content;
            System.out.println("Inicia analisis...");

            if (!"".equals(texto)) {
                scanner scan = new scanner(new BufferedReader(new StringReader(texto)));
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

                TablaFunciones a = EjecucionLenguajeAsa.tsFunciones;

                EjecucionLenguajeAsa leng = new EjecucionLenguajeAsa();
                leng.almacenarImportsYRutas(AST.getHijos().get(0), "archivoOriginal.asa");

                leng.almacenarVariablesGlobales(AST.getHijos().get(0), "archivoOriginal.asa");
                leng.almacenarFunciones(AST.getHijos().get(0), "archivoOriginal.asa");

                System.out.println("**************************Variables en tabla de simbolos global :D***********************************");
                EjecucionLenguajeAsa.tsGlobal.mostrarTS();

                System.out.println("**************************Funciones en tabla de Funciones global :D***********************************");
                EjecucionLenguajeAsa.tsFunciones.mostrarTS();

                if (EjecucionLenguajeAsa.tsFunciones.existeFuncion("vacio_principal")) {
                    System.out.println("El metodo principal si esta");
                    txtConsola.setText(EjecucionLenguajeAsa.ejecutarMain("archivoOriginal.asa"));
                    //TablaSimbolo t = EjecucionLenguajeAsa.pilaSimbolos.peek();
                } else {
                    System.out.println("El metodo principal no esta declarado aun");
                }

                //mostrarImagenes();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }        // TODO add your handling code here:
    }

    public void limpiarVariables() {
        EjecucionLenguajeAsa.pilaSimbolos = new Stack<>();
        EjecucionLenguajeAsa.pilaSimbolosAux = new Stack<>();
        EjecucionLenguajeAsa.tsGlobal = new TablaSimbolo("ambitoGlobal");  //Tabla de simbolos global donde iran variables globales, asignaciones, funciones, metodos y main
        EjecucionLenguajeAsa.tsFunciones = new TablaFunciones(); //Tabla para almacenar funciones, metodos y principal
        EjecucionLenguajeAsa.tsVarsFuncion = new TablaSimbolo("FuncActual"); //Tabla para almacenar funciones, metodos y principal
        EjecucionLenguajeAsa.consola = "";
        EjecucionLenguajeAsa.valRetorno = new ArrayList<Object>();
        EjecucionLenguajeAsa.cuerpoCase = new ArrayList<Nodo>();
        EjecucionLenguajeAsa.ruta = "";
        EjecucionLenguajeAsa.imagen = new ArrayList<String>();
        EjecucionLenguajeAsa.imports = new ArrayList<String>();
        EjecucionLenguajeAsa.listaErrores = new ArrayList<Ejecucion.Error>();
    }

    public void addTabsInPanel() {
        pestania[jTabbedPane1.getSelectedIndex() + 1] = new Pestania(jTabbedPane1);
    }

    public void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        int retval = fileChooser.showSaveDialog(jMenuItemSaveAs);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".asa")) {
                file = new File(file.getParentFile(), file.getName() + ".asa");
            }
            try {
                pestania[jTabbedPane1.getSelectedIndex()].getJtextArea().write(new OutputStreamWriter(new FileOutputStream(file),
                        "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String path) {
        File file = new File(path);

        if (!file.getName().toLowerCase().endsWith(".asa")) {
            file = new File(file.getParentFile(), file.getName() + ".asa");
        }
        try {
            pestania[jTabbedPane1.getSelectedIndex()].getJtextArea().write(new OutputStreamWriter(new FileOutputStream(file),
                    "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFile() throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "ASA & ASA Files", "ASA", "asa");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
            pestania[jTabbedPane1.getSelectedIndex()].setRuta(chooser.getSelectedFile().getAbsolutePath());
            String content = new String(Files.readAllBytes(Paths.get(chooser.getSelectedFile().getAbsolutePath())));
            pestania[jTabbedPane1.getSelectedIndex()].getJtextArea().setText(content);
            JLabel name = new JLabel(chooser.getSelectedFile().getName());
            jTabbedPane1.setTitleAt(jTabbedPane1.getSelectedIndex(), chooser.getSelectedFile().getName());
        }
    }

    public void cerrarPestania() {
        if (jTabbedPane1.getSelectedIndex() != -1) {
            jTabbedPane1.remove(jTabbedPane1.getSelectedIndex());
        } else {
            System.out.println("Ya no hay mas pestañas abiertas");
        }
    }

    public String crearReporteErrores() {
        String html = "<h1><Center>Reporte de Errores</center></h1>";
        html += "<table style=\"margin: 0px auto;\" border=\"1\">"
                + "<tr>"
                + "<th>Tipo</th>"
                + "<th>Descripcion</th>"
                + "<th>Archivo</th>"
                + "<th>Linea</th>"
                + "<th>Columna</th>"
                + "</tr>";
        for (Ejecucion.Error item : EjecucionLenguajeAsa.listaErrores) {
            html += "<tr>"
                    + "<td>" + item.getTipo() + "</td>"
                    + "<td>" + item.getDescripcion() + "</td> "
                    + "<td>" + item.getArchivo() + "</td>"
                    + "<td>" + item.getFila() + "</td>"
                    + "<td>" + item.getColumna() + "</td>"
                    + "</tr>";
        }
        html += "</table>";
        return html;
    }

    public void mostrarImagenes() {
        ImagenG imagenxD[][] = new ImagenG[10][10];

        String sDirectorio = EjecucionLenguajeAsa.ruta;
        File f = new File(sDirectorio);

        if (f.exists()) {
            File[] ficheros = f.listFiles();
            for (int x = 0; x < ficheros.length; x++) {
                System.out.println(ficheros[x].getName());

                ImageIcon img = new ImageIcon(sDirectorio + "\\" + ficheros[x].getName());
                ImageIcon iconoEscala = new ImageIcon(img.getImage().getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH));
                jMenuItemOpen.setIcon((iconoEscala));

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        imagenxD[i][j] = new ImagenG();
                        //imagen[i][j].setIcon(iconoEscala);
                        imagen[i][j].setBounds(i * 20, j * 20, i * 20, i * 20);
                        jPanelImage.add(imagen[i][j]);
                    }
                }

            }
        }
    }

    public void buscarArchivo(String argFichero, File argFile) {
        File[] lista = argFile.listFiles();
        if (lista != null) {
            for (File elemento : lista) {
                if (elemento.isDirectory()) {
                    buscarArchivo(argFichero, elemento);
                } else if (argFichero.equalsIgnoreCase(elemento.getName())) {
                    System.out.println(elemento.getParentFile());
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txtConsola = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelImage = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuEjecutar = new javax.swing.JMenu();
        jMenuItemCloseTab = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemReportes = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtConsola.setColumns(20);
        txtConsola.setRows(5);
        jScrollPane2.setViewportView(txtConsola);

        javax.swing.GroupLayout jPanelImageLayout = new javax.swing.GroupLayout(jPanelImage);
        jPanelImage.setLayout(jPanelImageLayout);
        jPanelImageLayout.setHorizontalGroup(
            jPanelImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanelImageLayout.setVerticalGroup(
            jPanelImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        jMenu2.setText("Archivo");

        jMenuItemNew.setText("Nuevo");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemNew);

        jMenuItemOpen.setText("Abrir");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemOpen);

        jMenuItemSave.setText("Guardar");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSave);

        jMenuItemSaveAs.setText("Guardar Como");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSaveAs);

        jMenuBar1.add(jMenu2);

        jMenuEjecutar.setText("Herramientas");

        jMenuItemCloseTab.setText("Cerrar pestaña");
        jMenuItemCloseTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseTabActionPerformed(evt);
            }
        });
        jMenuEjecutar.add(jMenuItemCloseTab);

        jMenuItem1.setText("Ejecutar archivo");
        jMenuEjecutar.add(jMenuItem1);

        jMenuBar1.add(jMenuEjecutar);

        jMenu1.setText("Reportes");

        jMenuItemReportes.setText("Reporte Errores");
        jMenuItemReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportesActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemReportes);

        jMenuItem3.setText("Abrir Album");
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                .addGap(588, 588, 588)
                .addComponent(jPanelImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanelImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        addTabsInPanel();
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        try {
            openFile();
            // TODO add your handling code here:

        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        save(pestania[jTabbedPane1.getSelectedIndex()].getRuta());
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
        saveAs();
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuItemCloseTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseTabActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Desea guardar Cambios?");
        if (dialogResult == JOptionPane.YES_OPTION) {
            if (pestania[jTabbedPane1.getSelectedIndex()].getRuta() != null) {
                save(pestania[jTabbedPane1.getSelectedIndex()].getRuta());
            } else {
                saveAs();
            }
        } else {
            cerrarPestania();
        }

        cerrarPestania();
    }//GEN-LAST:event_jMenuItemCloseTabActionPerformed

    private void jMenuItemReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportesActionPerformed
        File file = new File(EjecucionLenguajeAsa.ruta);

        if (!file.getName().toLowerCase().endsWith(".html")) {
            file = new File(file.getParentFile(), file.getName() + ".html");
        }
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(crearReporteErrores());
            writer.close();
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemReportesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VentanaPrincipal().setVisible(true);

                } catch (IOException ex) {
                    Logger.getLogger(VentanaPrincipal.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEjecutar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemCloseTab;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemReportes;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JPanel jPanelImage;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea txtConsola;
    // End of variables declaration//GEN-END:variables
}
