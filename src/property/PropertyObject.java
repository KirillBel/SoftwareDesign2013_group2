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
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class PropertyObject {
     private ArrayList<DefaultProperty> properties=new ArrayList<DefaultProperty>();
     
     public StringProperty propCreate(String name,String value)
     {
         StringProperty prop=new StringProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     public ColorProperty propCreate(String name,Color value)
     {
         ColorProperty prop=new ColorProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     public Vec2Property propCreate(String name,Vec2 value)
     {
         Vec2Property prop=new Vec2Property(name,value);
         properties.add(prop);
         return prop;
     };
     
     public RectProperty propCreate(String name,Rect value)
     {
         RectProperty prop=new RectProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     public FontProperty propCreate(String name,Font value)
     {
         FontProperty prop=new FontProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     
     public IntProperty propCreate(String name,int value)
     {
         IntProperty prop=new IntProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     public FloatProperty propCreate(String name,float value)
     {
         FloatProperty prop=new FloatProperty(name,value);
         properties.add(prop);
         return prop;
     };
     
     ////////////////////////////////////////////////////////
     
    public int propGetCount()
    {
        return properties.size();
    };

    public Property[] propToArray()
    {
        Property prop[]=new Property[properties.size()];
        for(int i=0;i<properties.size();i++)
        {
            prop[i]=properties.get(i);
        };
        return prop;
    };
    
    public void onPropertyChange(PropertyChangeEvent pce)
    {
        //BaseProperty prop=(BaseProperty)pce.getSource();
        //prop.setValue(pce.getNewValue());
    };
     
    public void updateProperties(boolean bUpdateToProp)
    {
        
    }
     ////////////////////////////////////////////////////////
     public static class BaseProperty extends DefaultProperty{
         protected PropertyObject parent=null;
         protected BaseProperty subPropParent=null;
     }
     
     public static class ColorProperty extends BaseProperty{
        public ColorProperty(String name,Color col)
        {
            setName(name);
            setDisplayName(name);
            setType(Color.class);
            setValue(col);
            setCategory("root");
        }
        
        public Color getProp()
        {
            return (Color)getValue();
        };
        
        public void setProp(Color c)
        {
            setValue(c);
        };
    }
    
    public static class Vec2Property extends BaseProperty{
        FloatProperty sub1=new FloatProperty("x", 0);
        FloatProperty sub2=new FloatProperty("y", 0);
        boolean bIgnoreListener=false;
        public Vec2Property(String name,Vec2 val)
        {
            setName(name);
            setDisplayName(name);
            setType(Vec2.class);
            setValue(new Vec2(val));
            setCategory("root");
            
            sub1.setValue(val.x);
            sub2.setValue(val.y);
            sub1.subPropParent=this;
            sub2.subPropParent=this;
            
            addSubProperty(sub1);
            addSubProperty(sub2);
            
            
            addPropertyChangeListener(listener);
        }
        
        PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    if(bIgnoreListener) return;
                    BaseProperty prop=(BaseProperty)pce.getSource();
                    bIgnoreListener=true;
                    if(prop.subPropParent!=null)
                    {
                        updateSubProperties(false);
                    }
                    else updateSubProperties(true);
                    bIgnoreListener=false;
                }
            };
        
        public void updateSubProperties(boolean bToSubProp)
        {
            if(bToSubProp)
            {
                if(getProp().x!=sub1.getProp()) sub1.setValue(getProp().x);
                if(getProp().y!=sub2.getProp()) sub2.setValue(getProp().y);
            }
            else
            {
                Vec2 v=getProp();
                
                v.x=(float)sub1.getValue();
                v.y=(float)sub2.getValue();
                setProp(v);
            };
        };
        
        public Vec2 getProp()
        {
            return (Vec2)getValue();
        };
        
        public void setProp(Vec2 val)
        {
            setValue(new Vec2(val));
        };
    }
    
    public static class RectProperty extends BaseProperty{
        FloatProperty sub1=new FloatProperty("left", 0);
        FloatProperty sub2=new FloatProperty("right", 0);
        FloatProperty sub3=new FloatProperty("top", 0);
        FloatProperty sub4=new FloatProperty("bottom", 0);
        public RectProperty(String name, Rect val)
        {
            setName(name);
            setDisplayName(name);
            setType(Vec2.class);
            setValue(new Rect(val));
            setCategory("root");
            
            sub1.setValue(val.left);
            sub2.setValue(val.right);
            sub3.setValue(val.top);
            sub4.setValue(val.bottom);
            sub1.subPropParent=this;
            sub2.subPropParent=this;
            sub3.subPropParent=this;
            sub4.subPropParent=this;
            
            addSubProperty(sub1);
            addSubProperty(sub2);
            addSubProperty(sub3);
            addSubProperty(sub4);
        }
        
        public Rect getProp()
        {
            return (Rect)getValue();
        };
        
        public void setProp(Rect val)
        {
            setValue(val);
        };
    }
    
    public static class FontProperty extends BaseProperty{
        public FontProperty(String name, Font val)
        {
            setName(name);
            setDisplayName(name);
            setType(Font.class);
            setValue(val);
            setCategory("root");
        }
        
        public Font getProp()
        {
            return (Font)getValue();
        };
        
        public void setProp(Font val)
        {
            setValue(val);
        };
    }
    
    public static class StringProperty extends BaseProperty{
        public StringProperty(String name, String val)
        {
            setName(name);
            setDisplayName(name);
            setType(String.class);
            setValue(val);
            setCategory("root");
        }
        
        public String getProp()
        {
            return (String)getValue();
        };
        
        public void setProp(String val)
        {
            setValue(val);
        };
    }
    
    public static class IntProperty extends BaseProperty{
        public IntProperty(String name, int val)
        {
            setName(name);
            setDisplayName(name);
            setType(Integer.class);
            setValue(val);
            setCategory("root");
        }
        
        public int getProp()
        {
            return (Integer)getValue();
        };
        
        public void setProp(int val)
        {
            setValue(val);
        };
    }
    
    public static class FloatProperty extends BaseProperty{
        public FloatProperty(String name,float val)
        {
            setName(name);
            setDisplayName(name);
            setType(Float.class);
            setValue(val);
            setCategory("root");
        }
        
        public float getProp()
        {
            return (Float)getValue();
        };
        
        public void setProp(float val)
        {
            setValue(val);
        };
    }
}
