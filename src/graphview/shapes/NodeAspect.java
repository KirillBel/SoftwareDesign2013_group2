/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import graphview.shapes.BaseShape;
import geometry.Rect;
import graphview.GraphNode;
import java.awt.Color;
import property.PropertyList;

/**
 *
 * @author Kirill
 */


public abstract class NodeAspect extends BaseShape{

    public enum eContainerType
    {
        DEFAULT,
        RESIZE_PARENT_TO_CHILDS,
        RESIZE_CHILDS_TO_PARENT,
    };

    public enum eNodeAspectType
    {
        BOX,
        ELLIPSE,
        TEXT,
        IMAGE
    };


    public Color color;
    public String hint;
    public String label;
    GraphNode graphParent;
    eNodeAspectType aspectType;
    eContainerType containerType;
    
    
    public NodeAspect()
    {
        properties.add(new PropertyList.ColorProperty("Color", "Shape color", color));
        properties.add(new PropertyList.StringProperty("Label", "Node label", label));
        properties.add(new PropertyList.StringProperty("Hint", "Node hint", hint));
    };
    
    public void updateProperties(boolean bUpdateToProp)
    {
        if(bUpdateToProp)
        {
            properties.setValue("Color", color);
            properties.setValue("Label", label);
            properties.setValue("Hint", hint);
        }
        else
        {
            color=properties.getColor("Color");
            label=properties.getString("Label");
            hint=properties.getString("Hint");
        };
        super.updateProperties(bUpdateToProp);
    };
    
    public abstract Rect getContainRect();
}
