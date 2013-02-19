/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public abstract class NodeShape extends BaseShape {

    public NodeShape()
    {
        shapeType=eShapeType.NODE;
    };
    
    protected Rect placement=new Rect();
    
    public Vec2 getPosition() {
        return placement.getTopLeft();
    };
    public void setPosition(Vec2 pt){
        placement.setPosition(pt);
    }
    public void move(Vec2 pt){
        placement.move(pt);
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
    
    public abstract Vec2 getPortPoint(Vec2 from);
    
    public boolean onGripDrag(int nGrip, Vec2 location, Vec2 delta){
        return false;
    };
}
