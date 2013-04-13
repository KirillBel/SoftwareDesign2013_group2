/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel;

import gui.MainPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author FlyPig
 */
public class textDialog extends JDialog{
    
    Dimension texpPanelSize= new Dimension(300, 300);
    private JTextArea textField=new JTextArea();
    private StructurePanel myStPanel=null;
    private JFrame mainFrame;
//    
//    private String strNode="NODE";
//    private String strEdge="EDGE";
    
    

    public textDialog(JFrame parent, boolean modal, StructurePanel stPanel)
    {
        super(parent, modal);
        mainFrame=parent;
        myStPanel=stPanel;
        initComponent();
    }
    
    public void initComponent()
    {
        this.setSize((int)texpPanelSize.getWidth(), (int)texpPanelSize.getHeight()+100 );   
        textField.setMinimumSize(texpPanelSize);
        textField.setSize(texpPanelSize);       
        Font myFront=textField.getFont();
        Font newFront=new Font(myFront.getName(), myFront.getStyle(),myFront.getSize()+5 );
        textField.setFont(newFront);
        JScrollPane textScrollPanel = new JScrollPane(textField);
        textScrollPanel.setSize(texpPanelSize);
        JPanel textPanel=new JPanel();
        textPanel.add(textScrollPanel);
        textPanel.setSize((int)texpPanelSize.getWidth(), (int)texpPanelSize.getHeight()-200 );  
        textPanel.setLayout(new BorderLayout());
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                myStPanel.setLabel(textField.getText()); 
                closeDialog();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });
        textPanel.add(enter,BorderLayout.PAGE_START);
        textPanel.add(cancel,BorderLayout.PAGE_END);
 
        
        this.setLayout(new BorderLayout());
        this.add(textScrollPanel);
        this.add(textPanel,BorderLayout.AFTER_LAST_LINE);
    }
    
    private void closeDialog()
    {
        textField.setText("");
        this.setVisible(false);
    }
    
    public void setText(String text)
    {
        textField.setText(text);
    }
    //textDialog=new JDialog(mainFrame,"Text", true);  
    
    
}
