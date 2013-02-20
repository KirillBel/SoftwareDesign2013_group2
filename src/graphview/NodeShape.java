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

    int containerMode=CONTAIN_NONE;
    public static final int CONTAIN_CHILDS_TO_NODE = 0;
    public static final int CONTAIN_NODE_TO_CHILDS = 1;
    public static final int CONTAIN_NONE = 2;
    
    public NodeShape()
    {
        shapeType=eShapeType.NODE;
    };
    
    protected Rect placement=new Rect();
    
    public Vec2 getLocalPosition() {
        return placement.getTopLeft();
    };
    public void setLocalPosition(Vec2 pt){
        placement.setPosition(pt);
    }
    
    public Vec2 getPosition() {
        return toGlobal(placement.getTopLeft());
    };
    public void setPosition(Vec2 pt){
        placement.setPosition(toLocal(pt));
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
    public Rect getLocalPlacement(){
        return placement;
    }
    public void setLocalPlacement(Rect rect){
        placement=rect;
    }
    public Rect getPlacement(){
        return toGlobal(placement);
    }
    public void setPlacement(Rect rect){
        placement=toLocal(rect);
    }
    
    public void setContainerMode(int mode)
    {
        containerMode=mode;
        updateContainer();
    };
    
    public void updateContainer()
    {
        if(containerMode==CONTAIN_CHILDS_TO_NODE)
        {
            Rect r=new Rect(0,0,placement.getSize().x,placement.getSize().y);
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setLocalPlacement(r.getReduced(5));
            };
        }
        else if(containerMode==CONTAIN_NODE_TO_CHILDS)
        {
            Rect r=new Rect(placement.left,placement.top,placement.left,placement.top);
            r.add(toLocal(getChildsRect()));
            r.right+=5;
            r.bottom+=5;
            for(int i=0;i<childs.size();i++)
            {
                if(childs.get(i).placement.left<5) {
                    childs.get(i).placement.move(new Vec2(5,0));
                }
                if(childs.get(i).placement.top<5) {
                    childs.get(i).placement.move(new Vec2(0,5));
                }
            };
            placement.set(r);
        };
    };
    
    @Override
    public void update()
    {
        updateContainer();
        super.update();
    };
    
    public abstract Vec2 getPortPoint(Vec2 from);
    
    public Vec2 getGlobalOffset()
    {
        if(parent==null) return getLocalPosition();
        return getLocalPosition().plus(parent.getGlobalOffset());
    };
    
    @Override
    public Vec2 toGlobal(Vec2 v)
    {
        if(parent==null) return v;
        return v.plus(parent.getGlobalOffset());
    };
    
    @Override
    public Rect toGlobal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset());
    };
    
    @Override
    public Vec2 toLocal(Vec2 v)
    {
        if(parent==null) return v;
        return v.minus(parent.getGlobalOffset());
    };
    
    @Override
    public Rect toLocal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset().multiply(-1));
    };
    
    public boolean onGripDrag(int nGrip, Vec2 location, Vec2 delta){
        return false;
    };
}
