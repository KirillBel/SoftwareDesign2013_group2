/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Kirill
 */
public abstract class NodeView extends ViewAspect {
    
    protected Rectangle placement=new Rectangle();
    
    public Point2D getPosition() {
        return new Point(placement.x,placement.y);
    };
    public void setPosition(Point pt){
        placement.x=(int)pt.getX();
        placement.y=(int)pt.getY();
    }
    public Point2D getSize(){
        return new Point(placement.width,placement.height);
    }
    public void setSize(Point pt){
        placement.width=(int)pt.getX();
        placement.height=(int)pt.getY();
    }
    public Rectangle2D getPlacement(){
        return placement;
    }
    public void setPlacement(Rectangle rect){
        placement=rect;
    }
    
    public boolean onMouseDrag(Point location, Point delta){
        
        placement.x+=delta.x;
        placement.y+=delta.y;
        return true;
    };
}
