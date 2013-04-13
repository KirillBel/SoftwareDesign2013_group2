/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel;

import graphview.GraphEdge;
import graphview.GraphNode;
import graphview.GraphScene;
import graphview.shapes.EdgeAspect;
import graphview.shapes.NodeAspect;
import gui.MainPanel;
import gui.structurePanel.tables.edgesTable;
import gui.structurePanel.tables.nodesTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author FlyPig
 */
public class StructurePanel extends javax.swing.JPanel {

    private GraphScene scene;
    public DefaultTableModel modelNode;
    public DefaultTableModel modelEdge;
    
    public nodesTable tableNode;
    public edgesTable tableEdge;
    
    public TablePropertiesFrame propertiesFrame;
    
    private JFrame mainFrame;

    private Point editingNodeCell=new Point();
    private Point editingEdgeCell=new Point();

    private nodeCreateDialog nodeCreate=null;
    private edgeCreateDialog edgeCreate=null;
      
    private int columNumber=-1;
     
    private String strNodeID="Node ID";
    private String strEdgeID="Edge ID";
    
    private String strNODE="NODE";
    private String strEDGE="EDGE";

    private MainPanel myPanel=null;
    private  textDialog myTextDialog=null;
           
        
    /**
     * Creates new form StructurePanel
     */
    public StructurePanel( GraphScene scene_, JFrame parent, MainPanel panel_) {
        scene=scene_;      
        mainFrame=parent;
        myPanel=panel_;
        propertiesFrame=new TablePropertiesFrame(parent, true);
        initComponents();
        initCreate();
        initTable();     

    }

    public void initTable()
    {       
        jButton1.setSelected(true);
        jButton5.setEnabled(false);
        tableNode=new nodesTable(this);
        tableNode.addAll();
        tableEdge=new edgesTable(this);
        tableEdge.addAll();
        jScrollPane1.setViewportView(tableNode);
        tableNode.updateNodes();        
        tableEdge.initBox();
               
    } 
    
    private void initCreate()
    {
        nodeCreate=new nodeCreateDialog(mainFrame, true);
        edgeCreate=new edgeCreateDialog(mainFrame, true);
        myTextDialog=new textDialog(mainFrame, true, this);
    }   
                      
    public void startTextEdit()
    {
        myTextDialog.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                myTextDialog.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                myTextDialog.getHeight()/2 
                );
        int row=-1;
        int col=-1;
        String data="";
        if(jButton1.isSelected())
        {
            row=tableNode.getSelectedRow();
            col=tableNode.getSelectedColumn();
            editingNodeCell.x=row;
            editingNodeCell.y=col;
            data=(String)tableNode.getModel().getValueAt(row, col);
        }
        else if(jButton2.isSelected())
        {
            row=tableEdge.getSelectedRow();
            col=tableEdge.getSelectedColumn();
            editingEdgeCell.x=row;
            editingEdgeCell.y=col;
            data=(String)tableEdge.getModel().getValueAt(row, col);
                       
        }     
        
        myTextDialog.setText(data); 
        myTextDialog.setVisible(true);   
        
    }
    
 
    public int getColumNum(JTable table, String str)
    {
        int result =0;
        if(table.getModel().getColumnCount()!=0)
        {
            while(!table.getModel().getColumnName(result).equals(str))
            {
                result++;
            }
        }
        return result;
    }  
    
    
    
    public void updateTables()
    {
        tableNode.updateNodes();
        tableEdge.updateEdges();
    }        
   
    
    
          

    
//     private JMenuItem goToScene() {         
//         
//         if (jMenuItemToScene == null)    
//         {
//             jMenuItemToScene = new JMenuItem("Показать на сцене");
//         }
//         
//         jMenuItemToScene.addActionListener(new java.awt.event.ActionListener() {
//             @Override            
//             public void actionPerformed(java.awt.event.ActionEvent e) {
//                 scene.clearAllSelection();
//                 if(jButton1.isSelected())
//                 {
//                     selectAspect(tableNode, strNodeID);
//                 }
//                 else
//                 {
//                     selectAspect(tableEdge, strEdgeID);
//                 }
//                 myPanel.selectScene();
//             }
//        });            
//        return jMenuItemToScene;
//     }
     

    
    
    public void updateSelect(String mode)
    {
        if(mode.equals(strNODE))
        {
            tableNode.clearSelection();
        }
        else if(mode.equals(strEDGE))
        {
            tableEdge.clearSelection();
        }
        else
        {
            tableNode.clearSelection();
            tableEdge.clearSelection();
        }
                
    }
    
    public void changeTable()
    {
        if(jButton1.isSelected())
        {
            jScrollPane1.setViewportView(tableEdge);   
            jButton1.setSelected(false);
            jButton2.setSelected(true);            
        }
        else
        {
            jScrollPane1.setViewportView(tableNode);   
            jButton1.setSelected(true);
            jButton2.setSelected(false);
        }
    }

    
    public void setLabel(String myLabel)
    {
        int ID=-1;
        if(jButton1.isSelected())
        {
            columNumber=getColumNum(tableNode, strNodeID);
            ID=(int)tableNode.getModel().getValueAt((int)editingNodeCell.getX(), columNumber);
            scene.getNode(ID).getAspect().setLabel(myLabel);            
        }
        else if(jButton2.isSelected())
        {
            columNumber=getColumNum(tableEdge, strEdgeID);                 
            ID=(int)tableEdge.getModel().getValueAt((int)editingEdgeCell.getX(), columNumber);
            scene.getEdge(ID).getAspect().setLabel(myLabel);         
        }
        updateTables();
    }
    
    public GraphScene getScene()
    {
        return scene;
    }

    public edgesTable getEdgesTable() 
    {
        return tableEdge;
    }
    
    public nodesTable getNodesTable() 
    {
        return tableNode;
    }

    public void deleteRow()
    {
        if(jButton1.isSelected())
        {
            tableNode.deleteTableRow();
        }
        else if(jButton2.isSelected())
        {
            tableEdge.deleteTableRow();
        }
    }
    
    public void enableButton(boolean res)
    {
        if(res)
        {
            jButton5.setEnabled(false);
        }
        else
        {
            jButton5.setEnabled(true);
        }
    }

              
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


      /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setMaximumSize(new java.awt.Dimension(9999, 9999));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton1.setText("Узлы");
        jButton1.setFocusable(false);
        jButton1.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Рёбра");
        jButton2.setFocusable(false);
        jButton2.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Конфигурация");
        jButton3.setFocusable(false);
        jButton3.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Добавить узел");
        jButton4.setFocusable(false);
        jButton4.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Добавить ребро");
        jButton5.setFocusable(false);
        jButton5.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setText("Поиск/Замена");
        jButton8.setFocusable(false);
        jButton8.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addGap(83, 83, 83)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jScrollPane1.setViewportView(tableNode);        
        jButton1.setSelected(true);
        jButton2.setSelected(false);
        tableNode.updateNodes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jScrollPane1.setViewportView(tableEdge);
        jButton2.setSelected(true);
        jButton1.setSelected(false);
        tableEdge.updateEdges();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        propertiesFrame.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                propertiesFrame.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                propertiesFrame.getHeight()/2 
                );  
        propertiesFrame.setVisible(true);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         
        nodeCreate.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                nodeCreate.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                nodeCreate.getHeight()/2 
                ); 
        nodeCreate.setVisible(true);
        if(nodeCreate.getNice())
        {
            String s = nodeCreate.getMyText();
            GraphNode node=scene.createNode(NodeAspect.eNodeAspectType.BOX);
            node.getAspect().createLabel(s);
            node.getAspect().setContainerMode(NodeAspect.eContainerType.RESIZE_PARENT_TO_CHILDS);
            tableNode.updateNodes();
        }
        nodeCreate.clearData();
        tableEdge.updateComboBox();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
                    Icon icon1=new ImageIcon("res/icons/add.png");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        tableEdge.updateComboBox();
        edgeCreate.setComboBox(tableEdge.getCombo());
        edgeCreate.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                edgeCreate.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                edgeCreate.getHeight()/2 
                );  
        edgeCreate.setVisible(true);
        if(edgeCreate.getNice())
        {
            scene.createEdge(Integer.valueOf(edgeCreate.getFrom()),
                    Integer.valueOf(edgeCreate.getTo()),
                    EdgeAspect.eEdgeAspectType.SIMPLE_LINE);
            if(edgeCreate.getCheckBox())
            {
                scene.getEdge(scene.getSizeEdgeArray()-1).setDirection(true);                        
            }
            scene.getEdge(scene.getSizeEdgeArray()-1).getAspect().setLabel(edgeCreate.getMyText());
        }
        edgeCreate.clearData();
        tableEdge.updateEdges();
    }//GEN-LAST:event_jButton5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables


}
