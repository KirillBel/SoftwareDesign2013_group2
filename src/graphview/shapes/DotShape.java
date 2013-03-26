/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Kirill
 */
public class DotShape extends NodeAspect{

    public DotShape(Vec2 pos, float size)
    {
        Rect r=new Rect(pos.x-size,pos.y-size,pos.x+size,pos.y+size);
        setRectangle(r);
        
        bHaveGrip=false;
        bResizeable=false;
    };
    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace=getGlobalRectangle();
        
        
        if(bSelected) 
        {
            g.setColor(Color.red);
            Rect r=globalPlace.getIncreased(globalPlace.getSize().x/4);
            g.fillOval((int)r.left, (int)r.top, (int)r.getSize().x, (int)r.getSize().y);
        };
        g.setColor(color.getProp());
        g.fillOval((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        
        super.draw(g);
    }
    
    @Override
    public boolean isIntersects(Vec2 pt) {
        return getGlobalRectangle().pointIn(pt);
    }
    
    @Override
    public boolean isIntersects(Rect r) {
        return Intersect.rectangle_rectangle(getGlobalRectangle(), r)==Intersect.INCLUSION;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        return getGlobalRectangle().getCenter();
    }

    @Override
    public Rect getContainRect() {
        return getGlobalRectangle();
    }
    
}
