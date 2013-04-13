/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel.tables;


import gui.structurePanel.ButtonsEditor;
import gui.structurePanel.ButtonsRenderer;
import gui.structurePanel.StructurePanel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author FlyPig
 */
public class nodesTable extends JTable {
    
    private Point cellNodeToEdit= new Point(-1, -1);
    
    private String strNodeID="Node ID";
    private String strEdgeID="Edge ID";
    private String strLabel="Label";
    private String strFrom="From";
    private String strTo="To";
    private String strBdirectional="Bdirectional";
    private String strNull="";
    
    private String strNODE="NODE";
    private String strEDGE="EDGE";

    
    private DefaultTableModel myModel=null;
    private DefaultTableModel oldModel=null;
    private DefaultTableModel newModel=null;
    private StructurePanel myPanel=null;
    
    private int columNumber=-1;
    
    private JPopupMenu getJPopupMenuNodeIndividual = null;
    private JPopupMenu getJPopupMenuNodeMany = null;
    
    private JMenuItem jMenuItemDelete = null;
    private JMenuItem jMenuItemToAnotherTable = null;
    private JMenuItem jMenuItemToScene = null;
    
    private boolean dragNodeComplete = false;
    
    private int oldNodeColumnValue=-2;
    private int oldNodeColumnNewValue=-2;
    private int nodeColumnValue=-1;
    private int nodeColumnNewValue=-1;
    
    public nodesTable(StructurePanel panel)
    {
        myModel = new DefaultTableModel();
        myModel.addColumn(strNodeID);
        myModel.addColumn(strLabel);
        myModel.addColumn("");
        
        oldModel = new DefaultTableModel();
        oldModel.addColumn(strNodeID);
        oldModel.addColumn(strLabel);
        oldModel.addColumn("");
        
        this.setModel(myModel);
        myPanel=panel;
    }
    
    public void addAll()
    {
        addSelectListener();
        addChangeColumListener();
        addSceneSynchro();
        addButtonAndSorter();
        
    }
    
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        if (
                this.getModel().getColumnName(column).equals(strNodeID) ||
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
    
    public void addSelectListener()
    {
        this.addMouseListener(new MouseAdapter() 
        {           
            @Override
            
            public void mouseClicked(MouseEvent e) 
            {               
                changeSelectionFromClick(e);
            }
        });
    }
    
    private void changeSelectionFromClick(MouseEvent e)
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
                if(column==(int)cellNodeToEdit.getX() && 
                        row==(int)cellNodeToEdit.getY() && 
                        column==columNumber)
                {
                    cellNodeToEdit.x=0;
                    cellNodeToEdit.y=0;
                    myPanel.startTextEdit();
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
            int nodeID;
            for(int i=0; i<selectedRowsCount; i++)
            {
                columNumber=myPanel.getColumNum(this, strNodeID);
                nodeID=(int) this.getModel().getValueAt(selectedRows[i], columNumber);
                myPanel.getScene().removeNode(nodeID);                         
            }
            myPanel.updateTables();
        }                     
    }   
    
    private JMenuItem goToAnotherTable() 
    {
        
        jMenuItemToAnotherTable = null;
        
        if(jMenuItemToAnotherTable==null)
        {
            jMenuItemToAnotherTable = new JMenuItem("Показать связанные ребра");
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
            myPanel.updateSelect(strEDGE);
            int selectedRows[]=this.getSelectedRows();  
            int nodeID=-1;
            int edgeID=-1;
            for(int i=0; i<selectedRowsCount; i++)
            {
                columNumber=myPanel.getColumNum(this, strNodeID);
                nodeID=(int) this.getModel().getValueAt(selectedRows[i], columNumber);
                for(int j=0; j<myPanel.getScene().getNode(nodeID).getSizeOfNodeEdgesIDArray();j++)
                {
                    edgeID=myPanel.getScene().getNode(nodeID).getElementOfNodeEdgesIDArray(j);
                    myPanel.getEdgesTable().addSelectInterval(edgeID);
                }                           
            }
            myPanel.changeTable();
        }
    }

    public void addChangeColumListener()
    {
        this.getTableHeader().addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseReleased(MouseEvent e) 
            {
                changeModel();
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
                newModel(e);
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });                       
    }
    
    public void changeModel()
    {
        if (dragNodeComplete) 
        {
            if(newModel!=null)
            {
                if(newModel.getColumnCount()>0)
                {
                    myModel=newModel;
                }
                this.setModel(myModel);
                myPanel.updateTables();
            }            
        }
        dragNodeComplete = false;
    }
    
    public void newModel(TableColumnModelEvent e)
    {
        dragNodeComplete = true;
        nodeColumnValue = e.getFromIndex();  
        nodeColumnNewValue = e.getToIndex();
        if(
                e.getFromIndex()!=e.getToIndex() &&
                nodeColumnValue!=oldNodeColumnValue &&
                nodeColumnNewValue!=oldNodeColumnNewValue
                )
        {                     
            oldNodeColumnValue=nodeColumnValue;
            oldNodeColumnNewValue=nodeColumnNewValue;
            newModel = new DefaultTableModel();
            for(int i=0;i<this.getColumnCount();i++)
            {
                if(i!=oldNodeColumnValue && i!=oldNodeColumnNewValue)
                {
                    newModel.addColumn(oldModel.getColumnName(i));
                }
                else if(i==oldNodeColumnValue)
                {
                    newModel.addColumn(oldModel.getColumnName(oldNodeColumnNewValue));
                }
                else if(i==oldNodeColumnNewValue)
                {
                    newModel.addColumn(oldModel.getColumnName(oldNodeColumnValue));
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
            columNumber=myPanel.getColumNum(this, strNodeID);              
            id=(int)this.getModel().getValueAt(selectedRow[j], columNumber);             
            if( myPanel.getScene().getNode(id)!=null)
            {
                myPanel.getScene().setSceneSelected( myPanel.getScene().getNode(id).getAspect(), true, false);
            }                                   
        }
        myPanel.getScene().updateUI();
    }
    
    public void addButtonAndSorter()
    {
         TableRowSorter sorter=new TableRowSorter(this.getModel()); 
        this.setRowSorter(sorter);
        
        this.setRowHeight(30);
        
        columNumber=myPanel.getColumNum(this, strNull);
        TableColumn column = this.getColumnModel().getColumn(columNumber);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(this, myPanel));        
    }
    
    public void updateNodes()
    {
        while(myModel.getRowCount()>0)
        {
            myModel.removeRow(0);
        }
        this.setModel(myModel);
        for(int i=0;i<myPanel.getScene().getSizeNodeArray();i++)
        {
            if(myPanel.getScene().getNode(i)!=null)
            {
                Object[] obj=new Object[this.getColumnCount()];
                for(int j=0;j<this.getColumnCount();j++)
                {
                    if(this.getModel().getColumnName(j).equals(strNodeID))
                    {
                        obj[j]=myPanel.getScene().getNode(i).getID();
                    }
                    else if(this.getModel().getColumnName(j).equals(strLabel))
                    {
                        obj[j]=myPanel.getScene().getNode(i).getAspect().getLabel();
                    }   
                    else if(this.getModel().getColumnName(j).equals(strNull))
                    {
                        obj[j]="";
                    }
                }             
                
                myModel.addRow(obj);
                this.setModel(myModel);                
            }
        }
    }  
    
        void addSelectInterval(int nodeIDFrom, int nodeIDTo) 
        {
            int nodeID=-1;
            nodeID=nodeIDFrom;                            
            for(int z=0;z<this.getRowCount();z++)
            {
                columNumber=myPanel.getColumNum(this, strNodeID);
                if(nodeID==(int)this.getModel().getValueAt(z, columNumber))
                {
                    this.addRowSelectionInterval(z, z);
                    break;
                }
            } 
            nodeID=nodeIDTo;                           
            for(int z=0;z<this.getRowCount();z++)
            {
                columNumber=myPanel.getColumNum(this, strNodeID);
                if(nodeID==(int)this.getModel().getValueAt(z, columNumber))
                {
                    this.addRowSelectionInterval(z, z);
                    break;
                }
            }
            
        }
}
