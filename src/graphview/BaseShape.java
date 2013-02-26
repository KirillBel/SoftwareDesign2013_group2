/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import graphevents.BaseEvent;
import graphevents.ShapeEvents;
import graphevents.ShapeMouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public abstract class BaseShape extends ShapeEvents{
    public Color color=Color.black;
    Rect placement=new Rect();
    protected boolean bMoveable=true;
    protected BaseShape parent=null;
    protected boolean bSelected=false;
    protected boolean bFocused=false;
    protected boolean bUnbodied=false;
    private boolean bVisible=true;
    protected int childIndex=-1;
    
    int containerMode=CONTAIN_NONE;
    public static final int CONTAIN_NONE = 0;
    public static final int CONTAIN_CHILDS_TO_NODE = 1;
    public static final int CONTAIN_NODE_TO_CHILDS = 2;
    
    protected ArrayList<BaseShape> childs=new ArrayList<BaseShape>();
    protected ArrayList<Integer> selectedChilds=new ArrayList<Integer>();
    protected ArrayList<Integer> zbuffer=new ArrayList<Integer>();
 
    
    public Vec2 getLocalPosition() {
        return placement.getTopLeft();
    };
    public void setLocalPosition(Vec2 pt){
        if(!bMoveable) return;
        placement.setPosition(pt);
    }
    
    public Vec2 getGlobalPosition() {
        if(parent==null) return placement.getTopLeft();
        return parent.toGlobal(placement.getTopLeft());
    };
    public void setGlobalPosition(Vec2 pt){
        if(!bMoveable) return;
        if(parent!=null) placement.setPosition(parent.toLocal(pt));
        placement.setPosition(pt);
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
        if(parent==null) return placement;
        return parent.toGlobal(placement);
    }
    public void setGlobalPlacement(Rect rect){
        if(!bMoveable) return;
        if(parent!=null) placement=parent.toLocal(rect);
        else placement=rect;
    }
    
    public Vec2 getGlobalOffset()
    {
        //if(!bChildsRelativeCoord) return new Vec2();
        if(parent==null) return getLocalPosition();
        return getLocalPosition().plus(parent.getGlobalOffset());
    };
    
    public Vec2 toGlobal(Vec2 v)
    {
        if(parent==null) return v;
        return v.plus(parent.getGlobalOffset()).plus(getLocalPosition());
    };
    
    public Rect toGlobal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset()).getMoved(getLocalPosition());
    };

    public Vec2 toLocal(Vec2 v)
    {
        if(parent==null) return v;
        return v.minus(parent.getGlobalOffset()).minus(getLocalPosition());
    };
    
    public Rect toLocal(Rect r)
    {
        if(parent==null) return r;
        return r.getMoved(parent.getGlobalOffset().plus(getLocalPosition()).multiply(-1));
    };
    
    public BaseShape testIntersect(Vec2 pt){
        if(isIntersects(pt))
        {
            BaseShape shape=null;
            for(int i=zbuffer.size()-1;i>=0;i--)
            {
                shape=childs.get(zbuffer.get(i)).testIntersect(pt.minus(childs.get(i).getLocalPosition()));
                if(shape!=null) return shape;
            }
            return this;
        };
        return null;
    };
    
    public int testChildIntersect(Vec2 pt)
    {
        for(int i=zbuffer.size()-1;i>=0;i--)
        {
            if(childs.get(zbuffer.get(i)).isIntersects(pt)) return zbuffer.get(i);
        }
        return -1;
    };
    
    public int testChildMouseEvent(Vec2 pt, BaseEvent evt)
    {
        for(int i=zbuffer.size()-1;i>=0;i--)
        {
            if(childs.get(zbuffer.get(i)).isIntersects(pt)) 
            {
                if(childs.get(zbuffer.get(i)).testEvent(evt))
                {
                    return zbuffer.get(i);
                };
            }
        }
        return -1;
    };
    
    public void addChild(BaseShape shape)
    {
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i)==null) continue;
            if(childs.get(i)==shape) return;
        };
        
        if(shape.parent!=null)
        {
            shape.parent.removeChild(shape.childIndex);
        };
        
        childs.add(shape);
        shape.parent=this;
        shape.childIndex=childs.size()-1;
        addToZBuffer(shape.childIndex);
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
        for(int i=0;i<zbuffer.size();i++)
        {
            if(zbuffer.get(i)==index)
            {
                zbuffer.remove(i);
                break;
            };
        };
        
        for(int i=0;i<selectedChilds.size();i++)
        {
            if(selectedChilds.get(i)==index)
            {
                selectedChilds.remove(i);
                break;
            };
        };
        
        childs.set(index,null);
        update();
    }
    
    public void removeAllChilds()
    {
        zbuffer.clear();
        clearSelection();
        childs.clear();
    };
    
    public void setVisible(boolean bVal)
    {
        if(bVal==bVisible) return;
        
        if(bVal==true && parent!=null)
        {
            parent.addToZBuffer(childIndex);
        }
        else if(bVal==false && parent!=null)
        {
            parent.removeFromZBuffer(childIndex);
        };
    };
    
    protected void addToZBuffer(int index)
    {
        for(int i=0;i<zbuffer.size();i++)
        {
            if(zbuffer.get(i)==index) return;
        };
        zbuffer.add(index);
    };
    
    protected void removeFromZBuffer(int index)
    {
        for(int i=0;i<zbuffer.size();i++)
        {
            if(zbuffer.get(i)==index)
            {
                zbuffer.remove(i);
                return;
            };
        };
    };
    
    protected void addToSelected(int index)
    {
        for(int i=0;i<selectedChilds.size();i++)
        {
            if(selectedChilds.get(i)==index) return;
        };
        selectedChilds.add(index);
        bringToTop(index);
    };
    
    protected void removeFromSelected(int index)
    {
        for(int i=0;i<selectedChilds.size();i++)
        {
            if(selectedChilds.get(i)==index)
            {
                selectedChilds.remove(i);
                return;
            };
        };
    };
    
    public void bringToTop(int index)
    {
        for(int i=0;i<zbuffer.size();i++)
        {
            if(zbuffer.get(i)==index)
            {
                zbuffer.remove(i);
                zbuffer.add(index);
                return;
            };
        };
    };
    
    public void setSelected(boolean bVal)
    {
        if(parent!=null)
        {
            if(bVal)
            {
                parent.addToSelected(childIndex);
            }
            else
            {
                parent.removeFromSelected(childIndex);
            };
        }
        bSelected=bVal;
    };
    
    public void clearSelection()
    {
        for(int i=0;i<selectedChilds.size();i++)
        {
            childs.get(selectedChilds.get(i)).bSelected=false;
        };
        selectedChilds.clear();
        
        update();
    };
    
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
            Rect r=getChildsRect();
            r.right+=5;
            r.bottom+=5;
            r.left=placement.left;
            r.top=placement.top;
            for(int i=0;i<childs.size();i++)
            {
                if(childs.get(i).placement.left<5) {
                    childs.get(i).placement.setPosition(new Vec2(5,childs.get(i).placement.top));
                }
                if(childs.get(i).placement.top<5) {
                    childs.get(i).placement.setPosition(new Vec2(childs.get(i).placement.left,5));
                }
            };
            placement.set(r);
        };
    };
    
    public void update()
    {
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i)!=null) 
                childs.get(i).update();
        };
        updateContainer();
    };
    
    public void draw(Graphics2D g)
    {
        if(bMouseIn && !bUnbodied)
        {
            g.setColor(Color.green);
            Rect r=getGlobalPlacement().getReduced(10);
            g.fillRect((int)r.left, (int)r.top, (int)r.getSize().x, (int)r.getSize().y);
        };
        
        //g.setColor(Color.blue);
        //g.fillRect((int)debugMouseMove.x-5, (int)debugMouseMove.y-5, 10, 10);
        for(int i=0;i<zbuffer.size();i++)
        {
            if(childs.get(zbuffer.get(i))!=null) 
                childs.get(zbuffer.get(i)).draw(g);
        };
    };
    
    public Rect getBoundingRect() {
        return getGlobalPlacement();
    }
    
    
    
    @Override
    protected boolean processMouseEvent(ShapeMouseEvent evt)
    {
        boolean bRet=false;
        int numChild=testChildMouseEvent(evt.getPosition(),evt);
        evt.setIntersectedChild(numChild);
        
        bRet=super.processMouseEvent(evt);
        
        if(bRet) return true;
    
        if(numChild!=-1) 
        {
            evt.toClientRectangle(childs.get(numChild).getLocalPosition());
            childs.get(numChild).processEvent(evt);
        }
        for(int j=0;j<childs.size();j++)
        {
            if(j!=numChild && childs.get(j).bUnbodied==true) 
            {
                childs.get(j).processEvent(new ShapeMouseEvent(evt));
            };
        };
        return true;
    };
    
    @Override
    public boolean onMouseClick(ShapeMouseEvent evt){
        if(evt.getButton()==1 && evt.getIntersectedChild()==-1)
        {
            boolean bSel=bSelected;
            clearSelection();
            setSelected(!bSel);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onMousePress(ShapeMouseEvent evt){
        
        if(evt.getIntersectedChild()!=-1 && 
                evt.getButton()==1 && 
                childs.get(evt.getIntersectedChild()).bSelected) 
        {
            nMode=1;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onMouseRelease(ShapeMouseEvent evt){
        nMode=0;
        return false;
    }
    
    @Override
    public boolean onMouseDrag(ShapeMouseEvent evt){
        if(nMode==1)
        {
            for(int j=0;j<selectedChilds.size();j++)
            {
                childs.get(selectedChilds.get(j)).move(evt.getDelta());
            };
            return true;
        }
        return false;
    }
   
    @Override
    public boolean onMouseMove(ShapeMouseEvent evt){
        debugMouseMove=toGlobal(evt.getPosition());
        int numChild=evt.getIntersectedChild();
        if(numChild==-1)
        {
            if(prevMoveIntersect!=-1)
            {
                childs.get(prevMoveIntersect).onMouseOut(evt);
                prevMoveIntersect=-1;
            };
            return false;
        }
        
        if(prevMoveIntersect!=-1 && prevMoveIntersect!=numChild)
        {
            childs.get(prevMoveIntersect).onMouseOut(evt);
            prevMoveIntersect=-1;
        };
        childs.get(numChild).onMouseIn(evt);
        prevMoveIntersect=numChild;
        return false;
    }
    
    @Override
    public boolean onMouseIn(ShapeMouseEvent evt){
        bMouseIn=true;
        return false;
    }
    
    @Override
    public boolean onMouseOut(ShapeMouseEvent evt){
        bMouseIn=false;
        //if(!bUnbodied) nMode=0;
        return false;
    }
  
    int nMode=0;
    int prevMoveIntersect=-1;
    Vec2 debugMouseMove=new Vec2();
    boolean bMouseIn=false;
    
    
    public abstract boolean isIntersects(Vec2 pt);
    public abstract boolean isIntersects(Rect r);
    public abstract Vec2 getPortPoint(Vec2 from);
}
