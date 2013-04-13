/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel;

import graphview.GraphScene;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author FlyPig
 */
    public class ButtonsEditor extends ButtonsPanel implements TableCellEditor
    {
        private  StructurePanel myPanel=null;
        public ButtonsEditor(final JTable table, StructurePanel panel_)
        {
            super();
            myPanel=panel_;
    
            MouseListener ml = new MouseAdapter() 
            {
                public void mousePressed(MouseEvent e) 
                {
                    ButtonModel m = ((JButton)e.getSource()).getModel();
                    if(m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) 
                    {
                        setBackground(table.getBackground());
                    }
                }
            };        
            buttons.get(0).addMouseListener(ml);
            buttons.get(1).addMouseListener(ml);
            //<----

            buttons.get(0).addActionListener(new ActionListener() 
            {
                @Override public void actionPerformed(ActionEvent e) 
                {
                    fireEditingStopped();

                }
        });

        buttons.get(1).addActionListener(new ActionListener() 
        {
            @Override public void actionPerformed(ActionEvent e) 
            {
                int row = table.convertRowIndexToModel(table.getEditingRow());                
                fireEditingStopped();
                myPanel.deleteRow();
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
