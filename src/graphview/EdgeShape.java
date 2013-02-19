/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Vec2;

/**
 *
 * @author Kirill
 */
public abstract class EdgeShape extends BaseShape{
    public EdgeShape()
    {
        shapeType=eShapeType.EDGE;
    };
    
    public int getNumPoints(){
        return childs.size();
    };
    public Vec2 getPoint(int index){
        return ((NodeShape)childs.get(index)).getPosition();
    }
    public void setPoint(Vec2 pt, int index){
        ((NodeShape)childs.get(index)).setPosition(pt);
        update();
    };
    public abstract void insertPoint(Vec2 pt, int index);
    public void removePoint(int index){
        childs.remove(index);
        update();
    }
    
    public void setPortA(NodeShape shape){
        portNodeA=shape;
        update();
    }
    public void setPortB(NodeShape shape){
        portNodeB=shape;
        update();
    }
    
    protected NodeShape portNodeA=null;
    protected NodeShape portNodeB=null;
}
