/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel.tables;

import graphview.GraphEdge;
import gui.structurePanel.ButtonsEditor;
import gui.structurePanel.ButtonsRenderer;
import gui.structurePanel.StructurePanel;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author FlyPig
 */
public class edgesTable extends JTable
{
    private StructurePanel myPanel=null;
    
    private DefaultTableModel myModel=null;
    private DefaultTableModel oldModel=null;
    private DefaultTableModel newModel=null;
    
    private String strNodeID="Node ID";
    private String strEdgeID="Edge ID";
    private String strLabel="Label";
    private String strFrom="From";
    private String strTo="To";
    private String strBdirectional="Bdirectional";
    private String strNull="";
    
    private String strNODE="NODE";
    private String strEDGE="EDGE";
    
    private int columNumber=-1;
    
    private Point editingEdgeCell=new Point();
    
    private JComboBox comboBoxNodesId = new JComboBox();
    private JComboBox comboBoxDir = new JComboBox();
    
    private boolean dragEdgeComplete = false;
    
    private int oldEdgeColumnValue=-2;
    private int oldEdgeColumnNewValue=-2;
    private int edgeColumnValue=-1;
    private int edgeColumnNewValue=-1;
    
    private Point cellEdgeToEdit=new Point(-1,-1);
    
    private JPopupMenu getJPopupMenuEdgeIndividual = null;
    private JPopupMenu getJPopupMenuEdgeMany = null;
    
    private JMenuItem jMenuItemDelete = null;
    private JMenuItem jMenuItemToAnotherTable = null;
    private JMenuItem jMenuItemToScene = null;
    
    public edgesTable(StructurePanel panel)
    {
        myModel = new DefaultTableModel();
        oldModel = new DefaultTableModel();
        myModel.addColumn(strEdgeID);
        oldModel.addColumn(strEdgeID);
        myModel.addColumn(strFrom);
        oldModel.addColumn(strFrom);
        myModel.addColumn(strTo); 
        oldModel.addColumn(strTo); 
        myModel.addColumn(strBdirectional);
        oldModel.addColumn(strBdirectional);
        myModel.addColumn(strLabel);
        oldModel.addColumn(strLabel);
        
        this.setModel(myModel);
        myPanel=panel;
    }
    
    public void addAll()
    {
        addChangeColumnListener();
        addSelectListener();
        addSceneSynchro();
        addButtonAndSorter();
    }
    
    public void initBox()
    {
        comboBoxDir.addItem("True");
        comboBoxDir.addItem("False");
        comboBoxDir.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) 
            {
                addBoxAct(event);
            }  
        });
    }
    
    private void addBoxAct(ItemEvent event)
    {
        if (event.getStateChange() == ItemEvent.SELECTED) {                    
            Object item = event.getItem();
            int row=-1;
            int col=-1;
            row=(int) editingEdgeCell.getX();
            col=(int) editingEdgeCell.getY();
            if(!((String)this.getModel().getValueAt(row,col)).equals((String)item))
            {
                columNumber=myPanel.getColumNum(this, strEdgeID);                                               
                int edgeId=(Integer)this.getModel().getValueAt(row, columNumber);
                boolean dir=false;

                if(((String)item).equals("True"))
                {
                    dir=true;
                }
                else
                {
                    dir=false;
                }
                myPanel.getScene().getEdge(edgeId).setDirection(dir);
            }                 
        }
    }
    
    @Override
    public TableCellEditor getCellEditor(int row, int column)
    {        
        editingEdgeCell.x=row;
        editingEdgeCell.y=column;
        int edgeId=-1;
        DefaultCellEditor dce;
        if (this.getModel().getColumnName(column).equals(strFrom) )
        {
            columNumber=myPanel.getColumNum(this, strEdgeID);
            edgeId=(Integer)this.getModel().getValueAt(row, columNumber);
            comboBoxNodesId.setSelectedItem(
                    Integer.toString(myPanel.getScene().getEdge(edgeId).getFromID())+
                    " - "+
                    myPanel.getScene().getEdge(edgeId).getAspect().getLabel()
                    );                    
            this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
            dce = new DefaultCellEditor( comboBoxNodesId );                    
            return dce;
        }
        else if(this.getModel().getColumnName(column).equals(strTo))
        {
            columNumber=myPanel.getColumNum(this, strEdgeID);
            edgeId=(Integer)this.getModel().getValueAt(row, columNumber);
            comboBoxNodesId.setSelectedItem(
                    Integer.toString(myPanel.getScene().getEdge(edgeId).getToID())+
                    " - "+
                    myPanel.getScene().getEdge(edgeId).getAspect().getLabel()
                    );
            this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
            dce = new DefaultCellEditor( comboBoxNodesId );  
            return dce; 
        }
        else if(this.getModel().getColumnName(column).equals(strBdirectional))
        {
            columNumber=myPanel.getColumNum(this, strEdgeID);
            edgeId=(Integer)this.getModel().getValueAt(row, columNumber);
            if(myPanel.getScene().getEdge(edgeId).isDirectional())
            {
                comboBoxDir.setSelectedItem("True");
            }
            else
            {
                comboBoxDir.setSelectedItem("False");
            }
            this.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(comboBoxDir));
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
        if (
                this.getModel().getColumnName(column).equals(strEdgeID) ||
                this.getModel().getColumnName(column).equals(strLabel)
                )
        {
            return false;
        }
        else
        {
            return true;
        }
    }      
    
    public void addChangeColumnListener()
    {
        this.getTableHeader().addMouseListener(new MouseAdapter() 
        {
            
            @Override
            public void mouseReleased(MouseEvent e) 
            {
                changeModel(e);
            }
        });
        this.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                dragColumn(e);                            
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });        
        
    }
    
    public void changeModel(MouseEvent e)
    {
        if (dragEdgeComplete) {
            if(newModel!=null)
            {
                if(newModel.getColumnCount()==myModel.getColumnCount())
                {
                    myModel=newModel;
                } 
                this.setModel(myModel);
                myPanel.updateTables();
            }
        }
        dragEdgeComplete = false;
    }
    
    public void dragColumn(TableColumnModelEvent e)
    {
        dragEdgeComplete = true;
        edgeColumnValue = e.getFromIndex();  
        edgeColumnNewValue = e.getToIndex();
        if(
                e.getFromIndex()!=e.getToIndex() &&
                edgeColumnValue!=oldEdgeColumnValue &&
                edgeColumnNewValue!=oldEdgeColumnNewValue
                )
        {                     
            oldEdgeColumnValue=edgeColumnValue;
            oldEdgeColumnNewValue=edgeColumnNewValue;
            newModel = new DefaultTableModel();
            for(int i=0;i<this.getColumnCount();i++)
            {
                if(i!=oldEdgeColumnValue && i!=oldEdgeColumnNewValue)
                {
                    newModel.addColumn(oldModel.getColumnName(i));
                }
                else if(i==oldEdgeColumnValue)
                {
                    newModel.addColumn(oldModel.getColumnName(oldEdgeColumnNewValue));
                }
                else if(i==oldEdgeColumnNewValue)
                {
                    newModel.addColumn(oldModel.getColumnName(oldEdgeColumnValue));
                }
            }
            oldModel=null;
            oldModel=new DefaultTableModel();
            for(int i=0;i<newModel.getColumnCount();i++)
            {                        
               oldModel.addColumn(newModel.getColumnName(i));
            }                  
        }   
    }
    
    public void addSelectListener()
    {
        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            { 
                clickSelect(e);
            }
                
        });
    }
    
    public void clickSelect(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e)) 
        {
            Point p = e.getPoint();
            int column = this.columnAtPoint(p);
            int row = this.rowAtPoint(p);
            if(column!=-1 && row!=-1) 
            { 
                this.changeSelection(row, column, true, true);                                  
            }  


            if(this.getSelectedColumnCount()==1)
            {
                columNumber=myPanel.getColumNum(this, strLabel);
                if(column== (int)cellEdgeToEdit.getX() && row== (int)cellEdgeToEdit.getY() && column==columNumber )
                {
                    cellEdgeToEdit.x=0;
                    cellEdgeToEdit.y=0;
                    myPanel.startTextEdit();
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
            int column = this.columnAtPoint(p);
            int row = this.rowAtPoint(p);
            boolean select=false;
            if(this.getSelectedRowCount()==0)
            {
                this.setColumnSelectionInterval(column, column);
                this.setRowSelectionInterval(row, row);
            }
            else
            {
                int[] selectedRows=this.getSelectedRows();
                for(int i=0; i<selectedRows.length; i++)
                {
                    if(selectedRows[i]==row)                            
                    {
                        select=true;
                    }
                }
                if(select==false)
                {
                    this.clearSelection();
                    this.setColumnSelectionInterval(column, column);
                    this.setRowSelectionInterval(row, row);
                }                        
            }         
                JPopupMenu jPopupMenu = getJPopupMenu();
                jPopupMenu.show(this, e.getX(), e.getY());                                    
        }               
    }
    
    private JPopupMenu getJPopupMenu() 
    {
        if(this.getSelectedRowCount()==1)
        {
            if (getJPopupMenuEdgeIndividual == null)
            {
                getJPopupMenuEdgeIndividual = new JPopupMenu();
                getJPopupMenuEdgeIndividual.add(getJMenuItemDelete());
                getJPopupMenuEdgeIndividual.add(goToAnotherTable());
//                    getJPopupMenuNodeIndividual.add(goToScene());
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
//                    getJPopupMenuNodeMany.add(goToScene());
            }
            return getJPopupMenuEdgeMany;
        }          
    }
    
    private JMenuItem getJMenuItemDelete() 
    {
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
        selectedRowsCount=this.getSelectedRowCount();
        if(selectedRowsCount!=0)                    
        {
            int selectedRows[]=this.getSelectedRows();  
            int edgeID;
            for(int i=0; i<selectedRowsCount; i++)
            {
                columNumber=myPanel.getColumNum(this, strEdgeID);
                edgeID=(int) this.getModel().getValueAt(selectedRows[i], columNumber);
                myPanel.getScene().removeEdge(edgeID);                         
            }
            myPanel.updateTables();
        }                     
    }   
    
    private JMenuItem goToAnotherTable() 
    {
        
        jMenuItemToAnotherTable = null;
        
        if(jMenuItemToAnotherTable==null)
        {
            jMenuItemToAnotherTable = new JMenuItem("Показать связанные вершины");
        }               
     
        jMenuItemToAnotherTable.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                changeTable();                                                          
            }
        });            
        return jMenuItemToAnotherTable;
    }
    
    public void changeTable()
    {
        int selectedRowsCount=-1;                                                 
        selectedRowsCount=this.getSelectedRowCount();
        if(selectedRowsCount!=0)                    
        {
            myPanel.updateSelect(strNODE);   
            int selectedRows[]=this.getSelectedRows();  
            int nodeIDFrom=-1;
            int nodeIDTo=-1;
            for(int i=0; i<selectedRowsCount; i++)
            {
                columNumber=myPanel.getColumNum(this, strFrom);
                String[] str=((String) this.getModel().getValueAt(selectedRows[i], columNumber)).split(" ");
                nodeIDFrom=Integer.valueOf(str[0]);                                          
                columNumber=myPanel.getColumNum(this, strTo);
                str=((String) this.getModel().getValueAt(selectedRows[i], columNumber)).split(" ");
                nodeIDTo=Integer.valueOf(str[0]);     
                myPanel.getNodesTable().addSelectInterval(nodeIDFrom,nodeIDTo);
                
            }
            myPanel.changeTable();
        }
    }
    
    public void addSceneSynchro()
    {
        ListSelectionModel selModel = this.getSelectionModel();
        selModel.addListSelectionListener(new ListSelectionListener() {               
               public void valueChanged(ListSelectionEvent e) 
               {
                   sceneSynchro();
               }               
          });
    }
    
    public void sceneSynchro()
    {
        int id=-1;
        myPanel.getScene().clearAllSelection();
        int[] selectedRow=this.getSelectedRows();
        for(int j=0;j<selectedRow.length;j++)
        {
            columNumber=myPanel.getColumNum(this, strEdgeID);              
            id=(int)this.getModel().getValueAt(selectedRow[j], columNumber);             
            if( myPanel.getScene().getEdge(id)!=null)
            {
                myPanel.getScene().setSceneSelected( myPanel.getScene().getEdge(id).getAspect(), true, false);
            }                                   
        }
        myPanel.getScene().updateUI();
    }
    
    public void addButtonAndSorter()
    {
        TableRowSorter sorter=new TableRowSorter(this.getModel()); 
        this.setRowSorter(sorter);
        
//        this.setRowHeight(30);
//        
//        columNumber=myPanel.getColumNum(this, strNull);
//        TableColumn column = this.getColumnModel().getColumn(columNumber);
//        column.setCellRenderer(new ButtonsRenderer());
//        column.setCellEditor(new ButtonsEditor(this, myPanel));        
    }                   
    
    void addSelectInterval(int edgeID) 
    {
        for(int z=0;z<this.getRowCount();z++)
        {
            columNumber=myPanel.getColumNum(this, strEdgeID);
            if(edgeID==(int)this.getModel().getValueAt(z, columNumber))
            {
                this.addRowSelectionInterval(z, z);
                break;
            }
        }
    }
    
    public void updateComboBox()
    {
        comboBoxNodesId=new JComboBox();
        for(int j=0; j<myPanel.getScene().getSizeNodeArray();j++)
        {
            if(myPanel.getScene().getNode(j)!=null)
            {
                comboBoxNodesId.addItem(String.valueOf(
                        myPanel.getScene().getNode(j).getID())+
                        " - "+
                        myPanel.getScene().getNode(j).getAspect().getLabel()
                        );
            }                          
        }
        if(comboBoxNodesId.getItemCount()<1)
        {
            myPanel.enableButton(true);
        }
        else
        {
            myPanel.enableButton(false);
        }
    }
    
    public void updateEdges()
    {

        updateComboBox();
        String dir="";        
        while(myModel.getRowCount()>0)
        {
            myModel.removeRow(0);
        }        
        
        comboBoxNodesId.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) 
            {
                addComboListener(event);
            }  
        });                                
        
        this.setModel(myModel);
        for(int i=0;i<myPanel.getScene().getSizeEdgeArray();i++)
        {
            GraphEdge edge=myPanel.getScene().getEdge(i);
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


                Object[] obj=new Object[this.getColumnCount()];
                for(int j=0;j<this.getColumnCount();j++)
                {
                    if(this.getModel().getColumnName(j).equals(strEdgeID))
                    {
                        obj[j]=edge.getID();
                    }
                    else if(this.getModel().getColumnName(j).equals(strFrom))
                    {
                        obj[j]=String.valueOf(edge.getFromID())+
                                " - "+
                                myPanel.getScene().getNode(edge.getFromID()
                                ).getAspect().getLabel();
                    } 
                    else if(this.getModel().getColumnName(j).equals(strTo))
                    {
                        obj[j]=String.valueOf(edge.getToID())+
                                " - "+
                                myPanel.getScene().getNode(edge.getToID()
                                ).getAspect().getLabel();
                    } 
                    else if(this.getModel().getColumnName(j).equals(strBdirectional))
                    {
                        obj[j]=dir;
                    } 
                    else if(this.getModel().getColumnName(j).equals(strLabel))
                    {
                        obj[j]=edge.getAspect().getLabel();
                    }                            
                }

                myModel.addRow(obj);
                
                }     
            
                this.setModel(myModel);  
                
        }
    } 
    
    public void addComboListener(ItemEvent event)
    {
        if (event.getStateChange() == ItemEvent.SELECTED) 
            {                    
                Object item = event.getItem();
                int row=-1;
                int col=-1;
                row=(int) editingEdgeCell.getX();
                col=(int) editingEdgeCell.getY();
                if(!String.valueOf(this.getModel().getValueAt(row,col)).equals((String)item))
                {            

                    columNumber=myPanel.getColumNum(this, strEdgeID);
                    int edgeId=(Integer)this.getModel().getValueAt(row, columNumber);                        
                    String[] str=((String)item).split(" ");

                    if(this.getModel().getColumnName(col).equals(strFrom))
                    {
                        myPanel.getScene().setFromEdge(edgeId, Integer.valueOf(str[0]));
                    }
                    else if(this.getModel().getColumnName(col).equals(strTo))
                    {
                        myPanel.getScene().setToEdge(edgeId, Integer.valueOf(str[0]));
                    }                                                    
                }    
            }
    }
    
    public JComboBox getCombo()
    {
        return comboBoxNodesId;
    }
    
}

