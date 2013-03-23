/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import graphview.shapes.BaseShape;
import geometry.Rect;
import geometry.Vec2;
import graphview.GraphNode;
import graphview.GraphScene;
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
    int labelNode=-1;
    GraphNode graphParent;
    eNodeAspectType aspectType;
    eContainerType containerType;
    public float containerOffset=7;
    
    
    public NodeAspect()
    {
        properties.add(new PropertyList.ColorProperty("Color", "Shape color", color));
        properties.add(new PropertyList.StringProperty("Hint", "Node hint", hint));
    };
    
    public void updateProperties(boolean bUpdateToProp)
    {
        if(bUpdateToProp)
        {
            properties.setValue("Color", color);
            properties.setValue("Hint", hint);
        }
        else
        {
            color=properties.getColor("Color");
            hint=properties.getString("Hint");
        };
        super.updateProperties(bUpdateToProp);
    };
    
    public void createLabel(String str)
    {
        removeAllChilds();
        labelNode=addChild(GraphScene.createNodeShape(eNodeAspectType.TEXT));
        setLabel(str);
    };
    
    public String getLabel()
    {
        if(labelNode==-1) return "";
        return childs.get(labelNode).getProperties(true).getString("Text");
    };
    
    public void setLabel(String txt)
    {
        if(labelNode==-1) return;
        
        childs.get(labelNode).getProperties(true).setValue("Text", txt);
        childs.get(labelNode).update();
    };
    
    /////////////////////////////CONTAINER////////////////////////////////
    
    public void setContainmentObject(eNodeAspectType type)
    {
        removeAllChilds();
        addChild(GraphScene.createNodeShape(type));
        update();
    };
    
    public void setContainerMode(eContainerType mode)
    {
        containerType=mode;
        updateContainer();
    };
    
    public eContainerType getContainerMode()
    {
        return containerType;
    };
    
    public eNodeAspectType getAspectType()
    {
        return aspectType;
    }
    
    public void updateContainer()
    {
        if(containerType==eContainerType.RESIZE_CHILDS_TO_PARENT)
        {
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setRectangle(getContainRect());
            };
        }
        else if(containerType==eContainerType.RESIZE_PARENT_TO_CHILDS)
        {
            Rect r=getChildsRect();
            setSize(r.getSize().plus(containerOffset));
            
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).bMoveable=true;
                childs.get(i).setCenterPosition(getSize().divide(2));
                childs.get(i).bMoveable=false;
            };
        }
    };
    
    public void fitToChilds(boolean bEquilateral)
    {
        Rect r=getChildsRect();
        Vec2 size=r.getSize();
        if(bEquilateral)
        {
            if(size.x>size.y) size.y=size.x;
            else size.x=size.y; 
        };
        
        setSize(size.plus(containerOffset*2));
    }
    
    public abstract Rect getContainRect();
    ///////////////////////////////END CONTAINER/////////////////////////////
    
    public void update()
    {
        updateContainer();
        super.update();
    };   
}
