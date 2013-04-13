/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author FlyPig
 */
    public class ButtonsPanel extends JPanel {
//        public final JButton but1=new JButton()
//        {
//
//            
//        };
//        //JButton but1=new JButton();
//        ImageIcon icon1 =createImageIcon("res/icons/cancel.png",
//                                 "a pretty but meaningless splat");
        //new ImageIcon("res/icons/cancel.png"); 
//        but1.setIcon(icon1);
//        this.setSize(16, 16);
        Icon icon1=new ImageIcon("res/icons/add.png");
        Icon icon2=new ImageIcon("res/icons/cancel.png");
        public final List<JButton> buttons = Arrays.asList(new JButton(icon1), new JButton(icon2));
        public ButtonsPanel() {
            super();
            setOpaque(true);
            for(JButton b: buttons) {
                b.setFocusable(false);
                //b.setBackground(new Color(0,0,0,0));
                //b.setBorder(null);
                //b.setForeground(new Color(0,0,0,0));
                //b.setMargin(new Insets(0, 0, 0, 0));
                //b.setFocusPainted(false);    
                //b.setOpaque(false);
                b.setPreferredSize(new Dimension(20, 20));
                add(b);
            }
        }
}



