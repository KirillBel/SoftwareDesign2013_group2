/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import javax.swing.Icon;

/**
 *
 * @author Kirill
 */
public class IconStringCellRenderer extends DefaultCellRenderer{
    
    @Override
    protected Icon convertToIcon(Object value) 
    {
        if (value == null) { return null; }
        
        if(value instanceof IconStringArray)
        {
            if(((IconStringArray)value).size()==0) return null;
            return ((IconStringArray)value).get(0).icon;
        }
        else if(value instanceof IconStringArray.IconStringElem)
        {
            return ((IconStringArray.IconStringElem)value).icon;
        };
        
        return null;
    }
    
    @Override
    protected String convertToString(Object value) 
    {
        if (value == null) { return null; }
        if(!(value instanceof IconStringArray.IconStringElem)) return null;
        
        if(value instanceof IconStringArray)
        {
            if(((IconStringArray)value).size()==0) return null;
            return ((IconStringArray)value).get(0).name;
        }
        else if(value instanceof IconStringArray.IconStringElem)
        {
            return ((IconStringArray.IconStringElem)value).name;
        };
        
        return value.toString();
    }
}
