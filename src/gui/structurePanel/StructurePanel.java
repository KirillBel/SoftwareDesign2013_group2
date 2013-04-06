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
    
    public JTable tableNode;
    public JTable tableEdge;
    
    public TablePropertiesFrame propertiesFrame;
    
    private JFrame mainFrame;
    
    private JPopupMenu getJPopupMenuNodeIndividual = null;
    private JPopupMenu getJPopupMenuNodeMany = null;
    private JPopupMenu getJPopupMenuEdgeIndividual = null;
    private JPopupMenu getJPopupMenuEdgeMany = null;

    private JMenuItem jMenuItemDelete = null;
    private JMenuItem jMenuItemToAnotherTable = null;
    private JMenuItem jMenuItemToScene = null;


       
    private JDialog textDialog=null;
    
    private Point editingNodeCell=new Point();
    private Point editingEdgeCell=new Point();
    
    private JTextArea textField=new JTextArea();
    
    private Point cellNodeToEdit=new Point();
    private Point cellEdgeToEdit=new Point();
    
    private nodeCreateDialog nodeCreate=null;
    private edgeCreateDialog edgeCreate=null;
      
    private JComboBox comboBoxNodesId = new JComboBox();
    private JComboBox comboBoxDir = new JComboBox();
    
    private boolean dragEdgeComplete = false;
     
    private int oldEdgeColumnValue=-2;
    private int oldEdgeColumnNewValue=-2;
    private int edgeColumnValue=-1;
    private int edgeColumnNewValue=-1;
     
    private  DefaultTableModel newModelEdge= new DefaultTableModel();
    private DefaultTableModel oldNewModelEdge= new DefaultTableModel();
     
    private boolean dragNodeComplete = false;
     
    private int oldNodeColumnValue=-2;
    private int oldNodeColumnNewValue=-2;
    private int nodeColumnValue=-1;
    private int nodeColumnNewValue=-1;
     
    private DefaultTableModel newModelNode= new DefaultTableModel();
    private DefaultTableModel oldNewModelNode= new DefaultTableModel();
    
    private int columNumber=-1;
     
    private String strNodeID="Node ID";
    private String strEdgeID="Edge ID";
    private String strLabel="Label";
    private String strFrom="From";
    private String strTo="To";
    private String strBdirectional="Bdirectional";
    private String strNull="";
    
    private int[] selectedRow;
    private int[] prewSelectedRow;
    
    private MainPanel myPanel=null;
           
        
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
        initTextDialog();
    }

    public void initTable()
    {       
        jButton1.setSelected(true);
        jButton5.setEnabled(false);
        tableNode=createNodeTable();
        tableEdge=createEdgeTable();
        jScrollPane1.setViewportView(tableNode);
        updateNodes();        
        initBox();
               
    } 
    
    private void initCreate()
    {
        nodeCreate=new nodeCreateDialog(mainFrame, true);
        edgeCreate=new edgeCreateDialog(mainFrame, true);
    }
    
    private void initBox()
    {
        comboBoxDir.addItem("True");
        comboBoxDir.addItem("False");
        comboBoxDir.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {                    
                    Object item = event.getItem();
                    int row=-1;
                    int col=-1;
                    row=(int) editingEdgeCell.getX();
                    col=(int) editingEdgeCell.getY();
                    if(!((String)tableEdge.getModel().getValueAt(row,col)).equals((String)item))
                    {
                        columNumber=getColumNum(tableEdge, strEdgeID);                                               
                        int edgeId=(Integer)tableEdge.getModel().getValueAt(row, columNumber);
                        boolean dir=false;
    
                        if(((String)item).equals("True"))
                        {
                            dir=true;
                        }
                        else
                        {
                            dir=false;
                        }
                        scene.getEdge(edgeId).setDirection(dir);
                    }                 
                }
            }  
        });
    }
        
    
    private void initTextDialog()
    {   
        Dimension texpPanelSize= new Dimension(300, 300) ;
        textDialog=new JDialog(mainFrame,"Text", true);
        
        textDialog.setSize((int)texpPanelSize.getWidth(), (int)texpPanelSize.getHeight()+100 );   
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
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jButton1.isSelected())
                {
                    String label=textField.getText();
                    columNumber=getColumNum(tableNode, strNodeID);
                    int ID=(int)tableNode.getModel().getValueAt((int)editingNodeCell.getX(), columNumber);
                    scene.getNode(ID).getAspect().setLabel(label);            
                    textDialog.setVisible(false);
                    updateTables();
                }
                else
                {
                    String label=textField.getText();
                    columNumber=getColumNum(tableEdge, strEdgeID);                 
                    int ID=(int)tableEdge.getModel().getValueAt((int)editingEdgeCell.getX(), columNumber);
                    scene.getEdge(ID).getAspect().setLabel(label);            
                    textDialog.setVisible(false);
                    updateTables();
                }
               
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                textDialog.setVisible(false);
            }
        });
        textPanel.add(enter,BorderLayout.PAGE_START);
        textPanel.add(cancel,BorderLayout.PAGE_END);
 
        
        textDialog.setLayout(new BorderLayout());
        textDialog.add(textScrollPanel);
        textDialog.add(textPanel,BorderLayout.AFTER_LAST_LINE);
    }    
    
    public void startEdit()
    {
        textDialog.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                textDialog.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                textDialog.getHeight()/2 
                );
        int row;
        int col;
        if(jButton1.isSelected())
        {
            row=tableNode.getSelectedRow();
            col=tableNode.getSelectedColumn();
            editingNodeCell.x=row;
            editingNodeCell.y=col;
            String data=(String)tableNode.getModel().getValueAt(row, col);
            textField.setText(data);

            textDialog.setVisible(true);
        }
        else
        {
            row=tableEdge.getSelectedRow();
            col=tableEdge.getSelectedColumn();
            editingEdgeCell.x=row;
            editingEdgeCell.y=col;
            String data=(String)tableEdge.getModel().getValueAt(row, col);
            textField.setText(data);

            textDialog.setVisible(true);
        }      
        
    }
    
 
    private  int getColumNum(JTable table, String str)
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
    
    private JTable createNodeTable()
    {
        modelNode = new DefaultTableModel();
        modelNode.addColumn(strNodeID);
        modelNode.addColumn(strLabel);
        modelNode.addColumn("");
//        modelNode.addColumn("Scene");
        oldNewModelNode.addColumn(strNodeID);
        oldNewModelNode.addColumn(strLabel);       
       
        final JTable table = new JTable(modelNode)
        {           
            @Override
            public boolean isCellEditable(int row, int column) {
                if (this.getModel().getColumnName(column).equals(strNodeID) ||
                        this.getModel().getColumnName(column).equals(strLabel))
                {
                    return false;                    
                }
                else
                {
                    return true;
                }
            }       
        };
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                                                   
                if (SwingUtilities.isLeftMouseButton(e)) 
                {
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    if(column!=-1 && row!=-1) 
                    { 
                        table.changeSelection(row, column, true, true);
                        selectedRow=table.getSelectedRows();
                    }    

                    if(table.getSelectedColumnCount()==1)
                    {
                        columNumber=getColumNum(table, strLabel);
                        if(column==(int)cellNodeToEdit.getX() && 
                                row==(int)cellNodeToEdit.getY() && 
                                column==columNumber)
                        {
                            cellNodeToEdit.x=0;
                            cellNodeToEdit.y=0;
                            startEdit();
                        }
                        else
                        {
                            cellNodeToEdit.x=column;
                            cellNodeToEdit.y=row;
                        }
                    }                                       
                }                
                else if (SwingUtilities.isRightMouseButton(e))
                {      
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    boolean select=false;
                    if(table.getSelectedRowCount()==0)
                    {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);                      
                    }
                    else
                    {
                        int[] selectedRows=table.getSelectedRows();
                        for(int i=0; i<selectedRows.length; i++)
                        {
                            if(selectedRows[i]==row)                            
                            {
                                select=true;
                            }
                        }
                        if(select==false)
                        {         
                            table.clearSelection();
                            table.setColumnSelectionInterval(column, column);
                            table.setRowSelectionInterval(row, row);
                        }                        
                    }
                    JPopupMenu jPopupMenu = getJPopupMenu();
                    jPopupMenu.show(table, e.getX(), e.getY());
                }               
            }
        });
        
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragNodeComplete) {
                    if(newModelNode!=null)
                    {
                        if(newModelNode.getColumnCount()>0)
                        {
                            modelNode=newModelNode;
                        }
                        table.setModel(modelNode);
                        updateTables();
                    }

                }
                dragNodeComplete = false;
            }
        });
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                dragNodeComplete = true;
                nodeColumnValue = e.getFromIndex();  
                nodeColumnNewValue = e.getToIndex();
                if(
                        e.getFromIndex()!=e.getToIndex() &&
                        nodeColumnValue!=oldNodeColumnValue &&
                        nodeColumnNewValue!=oldNodeColumnNewValue)
                {                     
                    oldNodeColumnValue=nodeColumnValue;
                    oldNodeColumnNewValue=nodeColumnNewValue;
                    newModelNode = new DefaultTableModel();
                    for(int i=0;i<table.getColumnCount();i++)
                    {
                        if(i!=oldNodeColumnValue && i!=oldNodeColumnNewValue)
                        {
                            newModelNode.addColumn(oldNewModelNode.getColumnName(i));
                        }
                        else if(i==oldNodeColumnValue)
                        {
                            newModelNode.addColumn(oldNewModelNode.getColumnName(oldNodeColumnNewValue));
                        }
                        else if(i==oldNodeColumnNewValue)
                        {
                            newModelNode.addColumn(oldNewModelNode.getColumnName(oldNodeColumnValue));
                        }
                    }
                    oldNewModelNode=null;
                    oldNewModelNode=new DefaultTableModel();
                    for(int i=0;i<table.getColumnCount();i++)
                    {                        
                       oldNewModelNode.addColumn(newModelNode.getColumnName(i));
                    }                                      
                }                
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });        
        
        ListSelectionModel selModel = table.getSelectionModel();
        selModel.addListSelectionListener(new ListSelectionListener() {               
               public void valueChanged(ListSelectionEvent e) 
               {
                   selectAspect(table, strNodeID);
               }               
          });

        TableRowSorter sorter=new TableRowSorter(table.getModel()); 
        table.setRowSorter(sorter);
        
        table.setRowHeight(30);
        
        columNumber=getColumNum(table, strNull);
        TableColumn column = table.getColumnModel().getColumn(columNumber);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(table));

        
        
        return table;
    }
    
    private JTable createEdgeTable()
    {
        modelEdge = new DefaultTableModel();
        modelEdge.addColumn(strEdgeID);
        oldNewModelEdge.addColumn(strEdgeID);
        modelEdge.addColumn(strFrom);
        oldNewModelEdge.addColumn(strFrom);
        modelEdge.addColumn(strTo); 
        oldNewModelEdge.addColumn(strTo); 
        modelEdge.addColumn(strBdirectional);
        oldNewModelEdge.addColumn(strBdirectional);
        modelEdge.addColumn(strLabel);
        oldNewModelEdge.addColumn(strLabel);
         
        final JTable table = new JTable(modelEdge)
        {
            @Override
            public TableCellEditor getCellEditor(int row, int column)
            {        
                editingEdgeCell.x=row;
                editingEdgeCell.y=column;
                int edgeId=-1;
                DefaultCellEditor dce;
                if (this.getModel().getColumnName(column).equals(strFrom) )
                {
                    columNumber=getColumNum(tableEdge, strEdgeID);
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, columNumber);
                    comboBoxNodesId.setSelectedItem(
                            Integer.toString(scene.getEdge(edgeId).getFromID())+
                            " - "+
                            scene.getEdge(edgeId).getAspect().getLabel()
                            );                    
                    tableEdge.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
                    dce = new DefaultCellEditor( comboBoxNodesId );                    
                    return dce;
                }
                else if(this.getModel().getColumnName(column).equals(strTo))
                {
                    columNumber=getColumNum(tableEdge, strEdgeID);
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, columNumber);
                    comboBoxNodesId.setSelectedItem(
                            Integer.toString(scene.getEdge(edgeId).getToID())+
                            " - "+
                            scene.getEdge(edgeId).getAspect().getLabel()
                            );
                    tableEdge.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
                    dce = new DefaultCellEditor( comboBoxNodesId );  
                    return dce; 
                }
                else if(this.getModel().getColumnName(column).equals(strBdirectional))
                {
                    columNumber=getColumNum(tableEdge, strEdgeID);
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, columNumber);
                    if(scene.getEdge(edgeId).isDirectional())
                    {
                        comboBoxDir.setSelectedItem("True");
                    }
                    else
                    {
                        comboBoxDir.setSelectedItem("False");
                    }
                    tableEdge.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxDir));
                    dce = new DefaultCellEditor( comboBoxDir );  
                    return dce;
                }
                else
                {
                    return super.getCellEditor(row, column);
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                if (this.getModel().getColumnName(column).equals(strEdgeID) || this.getModel().getColumnName(column).equals(strLabel))
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }         
        };        

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragEdgeComplete) {
                    if(newModelEdge!=null)
                    {
                        if(newModelEdge.getColumnCount()==modelEdge.getColumnCount())
                        {
                            modelEdge=newModelEdge;
                        } 
                        table.setModel(modelEdge);
                        updateTables();
                    }
                }
                dragEdgeComplete = false;
            }
        });
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                dragEdgeComplete = true;
                edgeColumnValue = e.getFromIndex();  
                edgeColumnNewValue = e.getToIndex();
                if(e.getFromIndex()!=e.getToIndex() && edgeColumnValue!=oldEdgeColumnValue && edgeColumnNewValue!=oldEdgeColumnNewValue)
                {                     
                    oldEdgeColumnValue=edgeColumnValue;
                    oldEdgeColumnNewValue=edgeColumnNewValue;
                    newModelEdge = new DefaultTableModel();
                    for(int i=0;i<table.getColumnCount();i++)
                    {
                        if(i!=oldEdgeColumnValue && i!=oldEdgeColumnNewValue)
                        {
                            newModelEdge.addColumn(oldNewModelEdge.getColumnName(i));
                        }
                        else if(i==oldEdgeColumnValue)
                        {
                            newModelEdge.addColumn(oldNewModelEdge.getColumnName(oldEdgeColumnNewValue));
                        }
                        else if(i==oldEdgeColumnNewValue)
                        {
                            newModelEdge.addColumn(oldNewModelEdge.getColumnName(oldEdgeColumnValue));
                        }
                    }
                    oldNewModelEdge=null;
                    oldNewModelEdge=new DefaultTableModel();
                    for(int i=0;i<table.getColumnCount();i++)
                    {                        
                       oldNewModelEdge.addColumn(newModelEdge.getColumnName(i));
                    }                  
                }                                
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });
        
            
       
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                                                   
                if (SwingUtilities.isLeftMouseButton(e)) 
                {
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    if(column!=-1 && row!=-1) 
                    { 
                        table.changeSelection(row, column, true, true);                                  
                    }  
                    
                   
                    if(table.getSelectedColumnCount()==1)
                    {
                        columNumber=getColumNum(table, strLabel);
                        if(column== (int)cellEdgeToEdit.getX() && row== (int)cellEdgeToEdit.getY() && column==columNumber )
                        {
                            cellEdgeToEdit.x=0;
                            cellEdgeToEdit.y=0;
                            startEdit();
                        }
                        else
                        {
                            cellEdgeToEdit.x=column;
                            cellEdgeToEdit.y=row;
                        }
                    }
                    
                    
                }                
                else if (SwingUtilities.isRightMouseButton(e))
                {      
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    boolean select=false;
                    if(table.getSelectedRowCount()==0)
                    {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    else
                    {
                        int[] selectedRows=table.getSelectedRows();
                        for(int i=0; i<selectedRows.length; i++)
                        {
                            if(selectedRows[i]==row)                            
                            {
                                select=true;
                            }
                        }
                        if(select==false)
                        {
                            table.clearSelection();
                            table.setColumnSelectionInterval(column, column);
                            table.setRowSelectionInterval(row, row);
                        }                        
                    }         
                        JPopupMenu jPopupMenu = getJPopupMenu();
                        jPopupMenu.show(table, e.getX(), e.getY());                                    
                }               
            }
        });
        
        ListSelectionModel selModel = table.getSelectionModel();
        selModel.addListSelectionListener(new ListSelectionListener() {               
               public void valueChanged(ListSelectionEvent e) 
               {
                   selectAspect(table, strEdgeID);
               }               
          });
        
        TableRowSorter sorter=new TableRowSorter(table.getModel()); 
        table.setRowSorter(sorter);
  
        return table;
    }
    
    public void updateTables()
    {
        updateNodes();
        updateEdges();
    }        
   
    public void updateNodes()
    {
        while(modelNode.getRowCount()>0)
        {
            modelNode.removeRow(0);
        }
        tableNode.setModel(modelNode);
        for(int i=0;i<scene.getSizeNodeArray();i++)
        {
            if(scene.getNode(i)!=null)
            {
                Object[] obj=new Object[tableNode.getColumnCount()];
                for(int j=0;j<tableNode.getColumnCount();j++)
                {
                    if(tableNode.getModel().getColumnName(j).equals(strNodeID))
                    {
                        obj[j]=scene.getNode(i).getID();
                    }
                    else if(tableNode.getModel().getColumnName(j).equals(strLabel))
                    {
                        obj[j]=scene.getNode(i).getAspect().getLabel();
                    }   
                    else if(tableNode.getModel().getColumnName(j).equals(strNull))
                    {
                        obj[j]="";
                    }
                }             
                
                modelNode.addRow(obj);
                tableNode.setModel(modelNode);                
            }
        }
    }           
    
    public void updateEdges()
    {

        updateComboBox();
        String dir="";        
        while(modelEdge.getRowCount()>0)
        {
            modelEdge.removeRow(0);
        }        
        
        comboBoxNodesId.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {                    
                    Object item = event.getItem();
                    int row=-1;
                    int col=-1;
                    row=(int) editingEdgeCell.getX();
                    col=(int) editingEdgeCell.getY();
                    if(!String.valueOf(tableEdge.getModel().getValueAt(row,col)).equals((String)item))
                    {            

                        columNumber=getColumNum(tableEdge, strEdgeID);
                        int edgeId=(Integer)tableEdge.getModel().getValueAt(row, columNumber);                        
                        String[] str=((String)item).split(" ");
                            
                        if(tableEdge.getModel().getColumnName(col).equals(strFrom))
                        {
                            scene.setFromEdge(edgeId, Integer.valueOf(str[0]));
                        }
                        else if(tableEdge.getModel().getColumnName(col).equals(strTo))
                        {
                            scene.setToEdge(edgeId, Integer.valueOf(str[0]));
                        }                                                    
                    }    
                }
            }  
        });
        
        
        
        tableEdge.setModel(modelEdge);
        for(int i=0;i<scene.getSizeEdgeArray();i++)
        {
            GraphEdge edge=scene.getEdge(i);
            if(edge!=null)
            {  
                
                if(edge.isDirectional())
                {
                    dir="True";
                }
                else
                {
                    dir="False";
                }


                Object[] obj=new Object[tableEdge.getColumnCount()];
                for(int j=0;j<tableEdge.getColumnCount();j++)
                {
                    if(tableEdge.getModel().getColumnName(j).equals(strEdgeID))
                    {
                        obj[j]=edge.getID();
                    }
                    else if(tableEdge.getModel().getColumnName(j).equals(strFrom))
                    {
                        obj[j]=String.valueOf(edge.getFromID())+
                                " - "+
                                scene.getNode(edge.getFromID()
                                ).getAspect().getLabel();
                    } 
                    else if(tableEdge.getModel().getColumnName(j).equals(strTo))
                    {
                        obj[j]=String.valueOf(edge.getToID())+
                                " - "+
                                scene.getNode(edge.getToID()
                                ).getAspect().getLabel();
                    } 
                    else if(tableEdge.getModel().getColumnName(j).equals(strBdirectional))
                    {
                        obj[j]=dir;
                    } 
                    else if(tableEdge.getModel().getColumnName(j).equals(strLabel))
                    {
                        obj[j]=edge.getAspect().getLabel();
                    }                            
                }

                modelEdge.addRow(obj);
                
                }     
            
                tableEdge.setModel(modelEdge);  
                
        }
    }        
    
    private void updateComboBox()
    {
        comboBoxNodesId=new JComboBox();
        for(int j=0; j<scene.getSizeNodeArray();j++)
        {
            if(scene.getNode(j)!=null)
            {
                comboBoxNodesId.addItem(String.valueOf(
                        scene.getNode(j).getID())+
                        " - "+
                        scene.getNode(j).getAspect().getLabel()
                        );
            }                          
        }
        if(comboBoxNodesId.getItemCount()<1)
        {
            jButton5.setEnabled(false);
        }
        else
        {
            jButton5.setEnabled(true);
        }
    }    
    
    private JPopupMenu getJPopupMenu() 
    {
        if(jButton1.isSelected())
        {
            if(tableNode.getSelectedRowCount()==1)
            {
                if (getJPopupMenuNodeIndividual == null)
                {
                    getJPopupMenuNodeIndividual = new JPopupMenu();
                    getJPopupMenuNodeIndividual.add(getJMenuItemDelete());
                    getJPopupMenuNodeIndividual.add(goToAnotherTable());
//                    getJPopupMenuNodeIndividual.add(goToScene());
                }
                return getJPopupMenuNodeIndividual;
            }
            else
            {
                if (getJPopupMenuNodeMany == null)
                {
                    getJPopupMenuNodeMany = new JPopupMenu();
                    getJPopupMenuNodeMany.add(getJMenuItemDelete());
                    getJPopupMenuNodeMany.add(goToAnotherTable());
//                    getJPopupMenuNodeMany.add(goToScene());
                }
                return getJPopupMenuNodeMany;
            }            
        }
        else
        {
           if(tableEdge.getSelectedRowCount()==1)
            {
                if (getJPopupMenuEdgeIndividual == null)
                {
                    getJPopupMenuEdgeIndividual = new JPopupMenu();
                    getJPopupMenuEdgeIndividual.add(getJMenuItemDelete());
                    getJPopupMenuEdgeIndividual.add(goToAnotherTable());
//                    getJPopupMenuEdgeIndividual.add(goToScene());
                }
                return getJPopupMenuEdgeIndividual;
            }
            else
            {
                if (getJPopupMenuEdgeMany == null)
                {
                    getJPopupMenuEdgeMany = new JPopupMenu();
                    getJPopupMenuEdgeMany.add(getJMenuItemDelete());
                    getJPopupMenuEdgeMany.add(goToAnotherTable());
//                    getJPopupMenuEdgeMany.add(goToScene());
                }
                return getJPopupMenuEdgeMany;
            } 
        }
    }

    private JMenuItem getJMenuItemDelete() {
        if (jMenuItemDelete == null) {
            jMenuItemDelete = new JMenuItem("Удалить");
        }
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            @Override            
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteTableRow();
            }
        });            
        return jMenuItemDelete;
    }   
    
    public void deleteTableRow()
    {
        int selectedRowsCount=-1;
            if(jButton1.isSelected())
            {
            selectedRowsCount=tableNode.getSelectedRowCount();
            if(selectedRowsCount!=0)                    
            {
                int selectedRows[]=tableNode.getSelectedRows();  
                int nodeID;
                for(int i=0; i<selectedRowsCount; i++)
                {
                    columNumber=getColumNum(tableNode, strNodeID);
                    nodeID=(int) tableNode.getModel().getValueAt(selectedRows[i], columNumber);
                    scene.removeNode(nodeID);                            
                }
                updateTables();

            }  
            }
            else
            {
                selectedRowsCount=tableEdge.getSelectedRowCount();
                if(selectedRowsCount!=0)                    
                {
                    int selectedRows[]=tableEdge.getSelectedRows();  
                    int edgeID;
                    for(int i=0; i<selectedRowsCount; i++)
                    {
                        columNumber=getColumNum(tableEdge, strEdgeID);
                        edgeID=(int) tableEdge.getModel().getValueAt(selectedRows[i], columNumber);
                        scene.removeEdge(edgeID);                            
                    }
                    updateTables();
                }
            }
    }
   
    
    private JMenuItem goToAnotherTable() {
        
        jMenuItemToAnotherTable = null;
        
        if(jButton1.isSelected())
        {
            jMenuItemToAnotherTable = new JMenuItem("Показать связанные ребра");
        }
        else
        {
             jMenuItemToAnotherTable = new JMenuItem("Показать связанные вершины");
        }
            
     
        jMenuItemToAnotherTable.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int selectedRowsCount=-1;
                if(jButton1.isSelected())
                {                                     
                    selectedRowsCount=tableNode.getSelectedRowCount();
                    if(selectedRowsCount!=0)                    
                    {
                        tableEdge.clearSelection();   
                        int selectedRows[]=tableNode.getSelectedRows();  
                        int nodeID=-1;
                        int edgeID=-1;
                        for(int i=0; i<selectedRowsCount; i++)
                        {
                            columNumber=getColumNum(tableNode, strNodeID);
                            nodeID=(int) tableNode.getModel().getValueAt(selectedRows[i], columNumber);
                            for(int j=0; j<scene.getNode(nodeID).getSizeOfNodeEdgesIDArray();j++)
                            {
                                edgeID=scene.getNode(nodeID).getElementOfNodeEdgesIDArray(j);
                                for(int z=0;z<tableEdge.getRowCount();z++)
                                {
                                    columNumber=getColumNum(tableEdge, strEdgeID);
                                    if(edgeID==(int)tableEdge.getModel().getValueAt(z, columNumber))
                                    {
                                        tableEdge.addRowSelectionInterval(z, z);
                                        break;
                                    }
                                }
                            }                           
                        }
                        changeTable();
                    }
                }
                else
                {
                    selectedRowsCount=tableEdge.getSelectedRowCount();
                    if(selectedRowsCount!=0)                    
                    {
                        tableNode.clearSelection();   
                        int selectedRows[]=tableEdge.getSelectedRows();  
                        int nodeID=-1;
                        int edgeID=-1;
                        for(int i=0; i<selectedRowsCount; i++)
                        {
                            columNumber=getColumNum(tableEdge, strFrom);
                            String[] str=((String) tableEdge.getModel().getValueAt(selectedRows[i], columNumber)).split(" ");
                            nodeID=Integer.valueOf(str[0]);                            
                            for(int z=0;z<tableNode.getRowCount();z++)
                            {
                                columNumber=getColumNum(tableNode, strNodeID);
                                if(nodeID==(int)tableNode.getModel().getValueAt(z, columNumber))
                                {
                                    tableNode.addRowSelectionInterval(z, z);
                                    break;
                                }
                            } 
                            columNumber=getColumNum(tableEdge, strTo);
                            str=((String) tableEdge.getModel().getValueAt(selectedRows[i], columNumber)).split(" ");
                            nodeID=Integer.valueOf(str[0]);                           
                            for(int z=0;z<tableNode.getRowCount();z++)
                            {
                                columNumber=getColumNum(tableNode, strNodeID);
                                if(nodeID==(int)tableNode.getModel().getValueAt(z, columNumber))
                                {
                                    tableNode.addRowSelectionInterval(z, z);
                                    break;
                                }
                            }
                        }
                        changeTable();
                    }
                }
                               
            }
        });            
        return jMenuItemToAnotherTable;
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
     
     public void selectAspect(JTable table, String str)
     {
         int id=-1;
         scene.clearAllSelection();
         selectedRow=table.getSelectedRows();
         for(int j=0;j<selectedRow.length;j++)
         {
             columNumber=getColumNum(table, str);                                        
             id=(int)table.getModel().getValueAt(selectedRow[j], columNumber);  
             if(str.equals(strNodeID))
             {
                 if(scene.getNode(id)!=null)
                 {
                     scene.setSceneSelected(scene.getNode(id).getAspect(), true, false);
                 }
             }
             else
             {
                 if(scene.getEdge(id)!=null)
                 {
                     scene.setSceneSelected(scene.getEdge(id).getAspect(), true, false);
                 }
             }             
             
         }
         scene.updateUI();
     }
    
    
    public void updateSelect()
    {
        tableNode.clearSelection();
        tableEdge.clearSelection();        
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
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class ButtonsPanel extends JPanel {
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

    class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {
    public ButtonsRenderer() {
        super();
        setName("Table.cellRenderer");
    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setBackground(isSelected?table.getSelectionBackground():table.getBackground());
        return this;
    }
}

    class ButtonsEditor extends ButtonsPanel implements TableCellEditor {
    public ButtonsEditor(final JTable table) {
        super();
    
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ButtonModel m = ((JButton)e.getSource()).getModel();
                if(m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
                    setBackground(table.getBackground());
                }
            }
        };        
        buttons.get(0).addMouseListener(ml);
        buttons.get(1).addMouseListener(ml);
        //<----

        buttons.get(0).addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                
            }
        });

        buttons.get(1).addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());                
                fireEditingStopped();
                deleteTableRow();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                fireEditingStopped();
            }
        });
    }
    @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.setBackground(table.getSelectionBackground());
        return this;
    }
    @Override public Object getCellEditorValue() {
        return "";
    }

    transient protected ChangeEvent changeEvent = null;

    @Override public boolean isCellEditable(EventObject e) {
        return true;
    } 
    @Override public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    @Override public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }
    @Override public void  cancelCellEditing() {
        fireEditingCanceled();
    }
    @Override public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }
    @Override public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }
    public CellEditorListener[] getCellEditorListeners() {
        return listenerList.getListeners(CellEditorListener.class);
    }
    protected void fireEditingStopped() {

        Object[] listeners = listenerList.getListenerList();

        for(int i = listeners.length-2; i>=0; i-=2) {
            if(listeners[i]==CellEditorListener.class) {
                // Lazily create the event:
                if(changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
            }
        }
    }
    protected void fireEditingCanceled() {

        Object[] listeners = listenerList.getListenerList();

        for(int i = listeners.length-2; i>=0; i-=2) {
            if(listeners[i]==CellEditorListener.class) {
                // Lazily create the event:
                if(changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
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
        updateNodes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jScrollPane1.setViewportView(tableEdge);
        jButton2.setSelected(true);
        jButton1.setSelected(false);
        updateEdges();
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
            updateNodes();
        }
        nodeCreate.clearData();
        updateComboBox();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
                    Icon icon1=new ImageIcon("res/icons/add.png");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        updateComboBox();
        edgeCreate.setComboBox(comboBoxNodesId);
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
        updateEdges();
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
