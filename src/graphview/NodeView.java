/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Kirill
 */
public abstract class NodeView extends ViewAspect {
    
    protected Rect placement=new Rect();
    
    public Vec2 getPosition() {
        return placement.getTopLeft();
    };
    public void setPosition(Vec2 pt){
        placement.setPosition(pt);
    }
    public Vec2 getSize(){
        return placement.getSize();
    }
    public void setSize(Vec2 pt){
        placement.setSize(pt);
    }
    public Rect getPlacement(){
        return placement;
    }
    public void setPlacement(Rect rect){
        placement=rect;
    }
    
    public boolean onMouseDrag(Vec2 location, Vec2 delta){
        placement.move(delta);
        return true;
    };
}
