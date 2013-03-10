/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import graphview.BaseShape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Kirill
 */
public class PropertyPanel extends PropertySheetPanel{
    
    BaseShape selectedShape=null;
    
    
    public PropertyPanel()
    {
        setMode( PropertySheet.VIEW_AS_FLAT_LIST );
        setDescriptionVisible( true );
    };
    
    public void fromList(PropertyList list)
    {
        setProperties(list.toArray());
    };
    
    public void fromShape(BaseShape shape)
    {
        selectedShape=shape;
        fromList(selectedShape.getProperties());
    };
    
    public void clearProperties()
    {
        setProperties(new Property[0]);
    };
    
    public void clearShape()
    {
        selectedShape=null;
        clearProperties();
    };
    
    public BaseShape getShape()
    {
        return selectedShape;
    };
}
