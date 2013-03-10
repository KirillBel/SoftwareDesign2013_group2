/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class PropertyList {
    public ArrayList<DefaultProperty> properties=new ArrayList<DefaultProperty>();
    
    public void add(DefaultProperty prop)
    {
        for(int i=0;i<properties.size();i++)
        {
            if(properties.get(i)==prop) return;
            if(properties.get(i).getName().equalsIgnoreCase(prop.getName()))
                    return;
        };
        
        properties.add(prop);
    };
    
    public DefaultProperty get(String name)
    {
        for(int i=0;i<properties.size();i++)
        {
            if(properties.get(i).getName().equalsIgnoreCase(name))
                return properties.get(i);
        }
        return null;
    };
    
    public Color getColor(String name)
    {
        return (Color)((ColorProperty)get(name)).getValue();
    };
    
    public Vec2 getVec2(String name)
    {
        return (Vec2)((Vec2Property)get(name)).getValue();
    };
    
    public Rect getRect(String name)
    {
        return (Rect)((RectProperty)get(name)).getValue();
    };
    
    public int getInt(String name)
    {
        return (Integer)((IntProperty)get(name)).getValue();
    };
    
    public float getFloat(String name)
    {
        return (Float)((FloatProperty)get(name)).getValue();
    };
    
    public void setValue(String name, Color value)
    {
        DefaultProperty prop=get(name);
        if(prop==null) return;
        if(!prop.getClass().getSimpleName().equalsIgnoreCase("ColorProperty")) return;
        get(name).setValue(value);
    };
    
    public void setValue(String name, Vec2 value)
    {
        DefaultProperty prop=get(name);
        if(prop==null) return;
        if(!prop.getClass().getSimpleName().equalsIgnoreCase("Vec2Property")) return;
        get(name).setValue(new Vec2(value));
    };
    
    public void setValue(String name, Rect value)
    {
        DefaultProperty prop=get(name);
        if(prop==null) return;
        if(!prop.getClass().getSimpleName().equalsIgnoreCase("RectProperty")) return;
        get(name).setValue(new Rect(value));
    };
    
    public void setValue(String name, int value)
    {
        DefaultProperty prop=get(name);
        if(prop==null) return;
        if(!prop.getClass().getSimpleName().equalsIgnoreCase("IntegerProperty")) return;
        get(name).setValue(value);
    };
    
    public void setValue(String name, float value)
    {
        DefaultProperty prop=get(name);
        if(prop==null) return;
        if(!prop.getClass().getSimpleName().equalsIgnoreCase("FloatProperty")) return;
        get(name).setValue(value);
    };
    
    public int getPropertyCount()
    {
        return properties.size();
    };
    
    public Property[] toArray()
    {
        Property prop[]=new Property[properties.size()];
        for(int i=0;i<properties.size();i++)
        {
            prop[i]=properties.get(i);
        };
        return prop;
    };
    
    public static class ColorProperty extends DefaultProperty{
        public ColorProperty(String name,String description, Color col)
        {
            setName(name);
            setDisplayName(name);
            setShortDescription(description);
            setType(Color.class);
            setValue(col);
            setCategory("root");
        }
    }
    
    public static class Vec2Property extends DefaultProperty{
        public Vec2Property(String name,String description, Vec2 val)
        {
            setName(name);
            setDisplayName(name);
            setShortDescription(description);
            setType(Vec2.class);
            setValue(new Vec2(val));
            setCategory("root");
            
            addSubProperty(new FloatProperty("x", "x", val.x));
            addSubProperty(new FloatProperty("y", "y", val.y));
            
            PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    DefaultProperty prop = (DefaultProperty) pce.getSource();
                    
                    if(prop.getName()==getName())
                    {
                        getSubProperties()[0].setValue(getVal().x);
                        getSubProperties()[1].setValue(getVal().x);
                    };
                    
                    if(prop.getName().equalsIgnoreCase("x"))
                        getVal().x=(float)prop.getValue();
                    if(prop.getName().equalsIgnoreCase("y"))
                        getVal().y=(float)prop.getValue();
                }
            };
            addPropertyChangeListener(listener);
        }
        
        Vec2 getVal()
        {
            return (Vec2)getValue();
        };
    }
    
    public static class RectProperty extends DefaultProperty{
        public RectProperty(String name,String description, Rect val)
        {
            setName(name);
            setDisplayName(name);
            setShortDescription(description);
            setType(Vec2.class);
            setValue(new Rect(val));
            setCategory("root");
            
            addSubProperty(new FloatProperty("left", "left", val.left));
            addSubProperty(new FloatProperty("right", "right", val.right));
            addSubProperty(new FloatProperty("top", "top", val.top));
            addSubProperty(new FloatProperty("bottom", "bottom", val.bottom));
            
            PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    FloatProperty prop = (FloatProperty) pce.getSource();
                    if(prop.getName().equalsIgnoreCase("left"))
                        getVal().left=prop.getVal();
                    if(prop.getName().equalsIgnoreCase("right"))
                        getVal().right=prop.getVal();
                    if(prop.getName().equalsIgnoreCase("top"))
                        getVal().top=prop.getVal();
                    if(prop.getName().equalsIgnoreCase("bottom"))
                        getVal().bottom=prop.getVal();
                }
            };
            addPropertyChangeListener(listener);
        }
        
        Rect getVal()
        {
            return (Rect)getValue();
        };
    }
    
    public static class IntProperty extends DefaultProperty{
        public IntProperty(String name,String description, int val)
        {
            setName(name);
            setDisplayName(name);
            setShortDescription(description);
            setType(Integer.class);
            setValue(val);
            setCategory("root");
        }
    }
    
    public static class FloatProperty extends DefaultProperty{
        public FloatProperty(String name,String description, float val)
        {
            setName(name);
            setDisplayName(name);
            setShortDescription(description);
            setType(Float.class);
            setValue(val);
            setCategory("root");
        }
        
        float getVal()
        {
            return (Float)getValue();
        };
    }
}


