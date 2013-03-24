/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import graphview.shapes.BaseShape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Kirill
 */
public class PropertyPanel extends PropertySheetPanel{
    
    PropertyObject selectedObject=null;
    
    
    public PropertyPanel()
    {
        setMode( PropertySheet.VIEW_AS_FLAT_LIST );
        setDescriptionVisible( true );
    };
    
    public void fromPropObject(PropertyObject obj)
    {
        selectedObject=obj;
        setProperties(obj.propToArray());
    };
    
    public void clearProperties()
    {
        setProperties(new Property[0]);
    };
    
    public void clearShape()
    {
        selectedObject=null;
        clearProperties();
    };
    
    public PropertyObject getPropObject()
    {
        return selectedObject;
    };
}
