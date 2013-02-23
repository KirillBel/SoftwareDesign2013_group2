/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author Kirill
 */
public class TextShape extends BoxShape{
    public String text=null;

    public TextShape(String text_)
    {
        super(0,0,0,0);
        text=text_;
    };
    
    @Override
    public void draw(Graphics2D g) {
        Rect bounds=Rect.fromRectangle2D(g.getFont().getStringBounds(text, g.getFontRenderContext()));
        Vec2 newSize=getSize();
        newSize.x=Math.max(newSize.x, bounds.getSize().x);
        newSize.y=Math.max(newSize.y, bounds.getSize().y);
        setSize(newSize);
        if(parent!=null) parent.updateContainer();
        
        Rect globalPlace=getGlobalPlacement();
        g.setColor(color);
        g.drawString(text, globalPlace.left, globalPlace.bottom-bounds.bottom);
        
        if(bSelected)
        {
            Stroke oldStroke=g.getStroke();
            BasicStroke stroke=new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{9}, 0);
            g.setStroke(stroke);
            g.drawRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
            g.setStroke(oldStroke);
            
        }
        else g.drawRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
    }
}
