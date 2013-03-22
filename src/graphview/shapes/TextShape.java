/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import property.PropertyList;

/**
 *
 * @author Kirill
 */
public class TextShape extends BoxShape{
    FontRenderContext frc=new FontRenderContext(null, true,true);
    Rect bounds=null;

    public TextShape(String text_)
    {
        super(0,0,10,10);
        
        getProperties(false).add(new PropertyList.FontProperty("Font", "Shape font", 
                new Font("Arial",Font.PLAIN,20)));
        getProperties(false).add(new PropertyList.StringProperty("Text", "Shape text", 
                new String(text_)));
       
        
        updateTextBounds();
        
        
        //bMoveable=false;
        //bResizeable=false;
        bReceiveMouseDrag=false;
        bReceiveMousePress=false;
        //bReceiveMouseClick=false;
        
        this.aspectType=eNodeAspectType.TEXT;
    };
    
    public void updateTextBounds()
    {
        bounds=Rect.fromRectangle2D(properties.getFont("Font").
                getStringBounds(properties.getString("Text"), frc));
        Vec2 newSize=getSize();
        newSize.x=Math.max(newSize.x, bounds.getSize().x);
        newSize.y=Math.max(newSize.y, bounds.getSize().y);
        setSize(newSize);
        if(parent!=null) parent.updateContainer();
    };
    
    @Override
     public void updateProperties(boolean bUpdateToProp)
     {
         if(bUpdateToProp==false) 
         {
             updateTextBounds();
         }
         super.updateProperties(bUpdateToProp);
     };
    
    @Override
    public void draw(Graphics2D g) {
        g.setFont(properties.getFont("Font"));
        
        
        Rect globalPlace=getGlobalRectangle();
        g.setColor(color);
        g.drawString(properties.getString("Text"), globalPlace.left, globalPlace.bottom-bounds.bottom);
        
        if(bSelected) drawGrip(g);
    }
    
    @Override
    public Rect getContainRect() {
        return getGlobalRectangle();
    }
}
