/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public abstract class BaseShape {
    public Color color;
    public boolean bSelected=false;
    protected BaseShape parent=null;
    private Rect placement=new Rect();
    protected boolean bMoveable=true;
    protected boolean bChildsRelativeCoord=true;
    
    int containerMode=CONTAIN_NONE;
    public static final int CONTAIN_NONE = 0;
    public static final int CONTAIN_CHILDS_TO_NODE = 1;
    public static final int CONTAIN_NODE_TO_CHILDS = 2;
 
    protected ArrayList<BaseShape> childs=new ArrayList<BaseShape>();
    
    public Vec2 getLocalPosition() {
        return placement.getTopLeft();
    };
    public void setLocalPosition(Vec2 pt){
        if(!bMoveable) return;
        placement.setPosition(pt);
    }
    
    public Vec2 getGlobalPosition() {
        return toGlobal(placement.getTopLeft());
    };
    public void setGlobalPosition(Vec2 pt){
        if(!bMoveable) return;
        placement.setPosition(toLocal(pt));
    }
    
    public void move(Vec2 pt){
        if(!bMoveable) return;
        placement.move(pt);
    }
    public Vec2 getSize(){
        return placement.getSize();
    }
    public void setSize(Vec2 pt){
        if(!bMoveable) return;
        placement.setSize(pt);
    }
    public Rect getLocalPlacement(){
        return placement;
    }
    public void setLocalPlacement(Rect rect){
        if(!bMoveable) return;
        placement=rect;
    }
    public Rect getGlobalPlacement(){
        return toGlobal(placement);
    }
    public void setGlobalPlacement(Rect rect){
        if(!bMoveable) return;
        placement=toLocal(rect);
    }
    
    public void setContainerMode(int mode)
    {
        containerMode=mode;
        updateContainer();
    };
    
    public Vec2 getGlobalOffset()
    {
        if(!bChildsRelativeCoord) return new Vec2();
        if(parent==null) return getLocalPosition();
        return getLocalPosition().plus(parent.getGlobalOffset());
    };
    
    public Vec2 toGlobal(Vec2 v)
    {
        if(parent==null) return v;
        return v.plus(parent.getGlobalOffset());
    };
    
    public Rect toGlobal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset());
    };

    public Vec2 toLocal(Vec2 v)
    {
        if(parent==null) return v;
        return v.minus(parent.getGlobalOffset());
    };
    
    public Rect toLocal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset().multiply(-1));
    };
    
    public BaseShape testIntersect(Vec2 pt){
        if(isIntersects(pt))
        {
            BaseShape shape=null;
            for(int i=0;i<childs.size();i++)
            {
                shape=childs.get(i).testIntersect(pt);
                if(shape!=null) return shape;
            }
            return this;
        };
        return null;
    };
    
    public void addChild(BaseShape shape)
    {
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i)==shape) return;
        };
        childs.add(shape);
        shape.parent=this;
        update();
    };
    
    public Rect getChildsRect()
    {
        Rect r=new Rect();
        for(int i=0;i<childs.size();i++)
        {
            if(i==0) r.set(childs.get(i).getBoundingRect());
            else r.add(childs.get(i).getBoundingRect());
        };
        return r;
    };
    
    public int getNumChilds(){
        return childs.size();
    };
    
    public BaseShape getChild(int index){
        return childs.get(index);
    };
    
    public void removeChild(int index){
        childs.remove(index);
        update();
    }
    
    public void update()
    {
        for(int i=0;i<childs.size();i++)
        {
            childs.get(i).update();
        };
    };
    
    public void draw(Graphics2D g)
    {
        for(int i=0;i<childs.size();i++)
        {
            childs.get(i).draw(g);
        };
    };
    
    public Rect getBoundingRect() {
        return getGlobalPlacement();
    }
    
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
    
    public abstract boolean isIntersects(Vec2 pt);
    public abstract Vec2 getPortPoint(Vec2 from);
}
