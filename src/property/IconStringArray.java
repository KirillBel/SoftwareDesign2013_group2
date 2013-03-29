/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.Icon;

/**
 *
 * @author Kirill
 */
public class IconStringArray {
    
    public static class IconStringElem
    {
        public IconStringElem(Object id, String name, Icon icon, IconStringArray parent)
        {
            this.id=id;
            this.name=name;
            this.icon=icon;
            this.parent=parent;
        };
        
        Object id=null;
        String name=null;
        Icon icon=null;
        IconStringArray parent=null;
    };
    
    public void add(Object id, String name, Icon icon)
    {
        add(new IconStringElem(id,name,icon,this));
    };
    
    public void add(IconStringElem elem)
    {
        if(find(elem.id)!=-1) return;
        elem.parent=this;
        array.add(elem);
    };
    
    int find(Object id)
    {
        for(int i=0;i<array.size();i++)
        {
            if(array.get(i).id==id) return i;
        };
        return -1;
    };
    
    public int size() {
        return array.size();
    };
    
    public IconStringElem get(int ind) {
        return array.get(ind);
    };
    
    public String[] getStrings()
    {
        String strArray[]=new String[array.size()];
        for(int i=0;i<array.size();i++)
        {
            if(array.get(i).name==null) strArray[i]="";
            else strArray[i]=array.get(i).name;
        };
        return strArray;
    };
    
    public Icon[] getIcons()
    {
        Icon icArray[]=new Icon[array.size()];
        for(int i=0;i<array.size();i++)
        {
            if(array.get(i).icon==null) icArray[i]=null;
            else icArray[i]=array.get(i).icon;
        };
        return icArray;
    };
    
    public IconStringElem getSelected()
    {
        if(selectedItem<0) return null;
        if(selectedItem>=array.size()) return null;
        
        return array.get(selectedItem);
    };
    
    public void setSelected(int index)
    {
        if(index<0) return;
        if(index>=array.size()) return;
        
        selectedItem=index;
    };
    
    public Integer getSelectedIndex()
    {
        return selectedItem;
    };
    
    public void showSelectionDial()
    {
        IconStringSelectDial dial=new IconStringSelectDial(this,null,true);
        
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        dial.setLocation(
                (int)d.getWidth()/2-
                dial.getWidth()/2,
                (int)d.getHeight()/2-
                dial.getHeight()/2 
                );
        
        dial.setVisible(true);
        
        if(!dial.bOk) return;
        
        int ind=dial.getSelectedElemIndex();
        setSelected(ind);
    };
    
    private ArrayList<IconStringElem> array=new ArrayList<IconStringElem>();
    private int selectedItem=0;
}
