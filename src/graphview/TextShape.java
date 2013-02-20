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
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kirill
 */
public class TextShape extends NodeShape{

    public TextShape(String text)
    {
        label=text;
    };
   
    
    @Override
    public Rect getBoundingRect() {
        return getPlacement();
    }

    @Override
    public void draw(Graphics2D g) {
        Rect bounds=Rect.fromRectangle2D(g.getFont().getStringBounds(label, g.getFontRenderContext()));
        Vec2 newSize=getSize();
        newSize.x=Math.max(newSize.x, bounds.getSize().x);
        newSize.y=Math.max(newSize.y, bounds.getSize().y);
        setSize(newSize);
        if(parent!=null) parent.update();
        
        Rect globalPlace=getPlacement();
        g.drawString(label, globalPlace.left, globalPlace.bottom-bounds.bottom);
        
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

    @Override
    public void update() {
        
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return getPlacement().pointIn(pt);
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
