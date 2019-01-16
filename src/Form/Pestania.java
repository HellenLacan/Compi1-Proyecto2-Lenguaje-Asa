/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

/**
 *
 * @author Hellen
 */
public class Pestania extends  JTabbedPane{

    private JPanel panel1;
    private String ruta;
    private JTextArea jtextArea;

    public Pestania(JTabbedPane jTabbedPane1) {
        //Creamos el panel y lo añadimos a las pestañas
        JPanel panel1 = new JPanel();
        panel1.add(addLineNumberToText());
        int num = jTabbedPane1.getTabCount() + 1;
        jTabbedPane1.addTab("Nuevo", panel1);
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        panel1.setLayout(null);
        panel1.setOpaque(false);  // make transparent background

    }

    public JTextArea getJtextArea() {
        return jtextArea;
    }

    public void setJtextArea(JTextArea jtextArea) {
        this.jtextArea = jtextArea;
    }

    public JScrollPane addLineNumberToText() {
        final JScrollPane scrollPane = new JScrollPane(this.panel1,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 0, 585, 425);

        jtextArea = new JTextArea();
        JTextArea lines = new JTextArea("1");

        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);

        jtextArea.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = jtextArea.getDocument().getLength();
                Element root = jtextArea.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

        });

        scrollPane.getViewport().add(jtextArea);
        scrollPane.setRowHeaderView(lines);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
