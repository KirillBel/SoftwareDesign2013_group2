/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import javax.swing.JPanel;

/**
 *
 * @author FlyPig
 */
public class OverviewPanel extends JPanel{
    
    public OverviewPanel(){
        initComponents();
    }
    
    private void initComponents() {
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
    
}
