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
public abstract class BaseShape  extends ShapeEvents{
    public Color color=Color.black;
    private Vec2 localCoord=new Vec2();
    private Vec2 globalCoord=new Vec2();
    private Vec2 shapeSize=new Vec2();
    protected boolean bMoveable=true;
    protected boolean bEquilateral=false;
    protected boolean bResizeable=true;
    protected BaseShape parent=null;
    protected boolean bSelected=false;
    protected int childIndex=-1;
    protected boolean bVisible=true;
    protected boolean bMouseIn=false;
    
    int containerMode=CONTAIN_DEFAULT;
    public float containerOffset=7;
    public static final int CONTAIN_DEFAULT = 0;
    public static final int CONTAIN_CHILDS_TO_NODE = 1;
    public static final int CONTAIN_NODE_TO_CHILDS = 2;
    
    protected ArrayList<BaseShape> childs=new ArrayList<BaseShape>();
    protected ArrayList<Integer> selectedChilds=new ArrayList<Integer>();
    protected ArrayList<Integer> zbuffer=new ArrayList<Integer>();
    
    
    /////////////////////MOVEMENT///////////////////////////////////////
    
    public void updateGlobalCoord()
    {
        if(parent==null) globalCoord.set(localCoord);
        else globalCoord.set(parent.globalCoord.plus(localCoord));
        updateChildsCoord();
    };
    
    public void updateChildsCoord()
    {
        for(int i=0;i<childs.size();i++)
        {
            childs.get(i).updateGlobalCoord();
        };
    };
    
    public void setPosition(Vec2 coord)
    {
        if(!bMoveable) return;
        localCoord.set(onMove(localCoord,coord));
        updateGlobalCoord();
    };
    
    public void move(Vec2 v)
    {
        setPosition(localCoord.plus(v));
    };
    
    public void setCenterPosition(Vec2 coord)
    {
        Rect r=getRectangle();
        r.setCenterPosition(coord);
        setRectangle(r);
    };
    
    public Vec2 getPosition()
    {
        return localCoord;
    };
    
    public Vec2 getGlobalPosition()
    {
        return globalCoord;
    };
    
    public void setSize(Vec2 size)
    {
        if(!bResizeable) return;
        shapeSize.set(onResize(shapeSize,size));
    };
    
    public Vec2 getSize()
    {
        return shapeSize;
    };
    
    public void setRectangle(Rect r)
    {
        setPosition(r.getTopLeft());
        setSize(r.getSize());
    }
    
    public Rect getRectangle()
    {
        return new Rect(localCoord.x,localCoord.y,
                localCoord.x+shapeSize.x,localCoord.y+shapeSize.y);
    };
    
    public Rect getGlobalRectangle()
    {
        return new Rect(globalCoord.x,globalCoord.y,
                globalCoord.x+shapeSize.x,globalCoord.y+shapeSize.y);
    };
    
    protected Vec2 onResize(Vec2 oldSize, Vec2 newSize){
        
        if(bEquilateral)
        {
            if(newSize.x>newSize.y)
                newSize=new Vec2(newSize.x,newSize.x);
            else 
                newSize=new Vec2(newSize.y,newSize.y);
        };
        
        return newSize;
    }
    
    protected Vec2 onMove(Vec2 oldCoord, Vec2 newCoord){
        return newCoord;
    }
    
    public Vec2 toLocal(Vec2 global)
    {
        return global.minus(globalCoord);
    };
    
    public Rect toLocal(Rect global)
    {
        Rect r=new Rect(global);
        r.setPosition(global.getTopLeft().minus(globalCoord));
        return r;
    };
    
    public Vec2 toGlobal(Vec2 local)
    {
        return local.plus(globalCoord);
    };
    
    public Rect toGlobal(Rect local)
    {
        Rect r=new Rect(local);
        r.setPosition(local.getTopLeft().plus(globalCoord));
        return r;
    };
    
    public Rect getBoundingRect()
    {
        Rect r=getRectangle();
        r.add(this.getChildsRect());
        return r;
    };
    
    /////////////////////////END MOVEMENT//////////////////////////////////
    
    ///////////////////////////CHILDS///////////////////////////////////////
    public int testChildIntersect(Vec2 pt)
    {
        for(int i=zbuffer.size()-1;i>=0;i--)
        {
            if(childs.get(zbuffer.get(i)).isIntersects(pt)) return zbuffer.get(i);
        }
        return -1;
    };
    
    public BaseShape getIntersectedChild(Vec2 pt){
        BaseShape shape=null;
        for(int i=zbuffer.size()-1;i>=0;i--)
        {
            if(!childs.get(zbuffer.get(i)).isIntersects(pt)) continue;
            shape=childs.get(zbuffer.get(i)).getIntersectedChild(pt);
            if(shape!=null) return shape;
        }
        if(isIntersects(pt))
            return this;
        return null;
    };
    
    public void addChild(BaseShape shape)
    {
        if(shape==null) return;
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i)==shape) return;
        };
        
        if(shape.parent!=null)
        {
            shape.parent.removeChild(shape.childIndex);
        };
        
        childs.add(shape);
        shape.parent=this;
        shape.updateGlobalCoord();
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
    
    //////////////////////////END CHILDS///////////////////////////////
    
    //////////////////////SELECTION_VISIBILITY/////////////////////////
    
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
    
    ////////////////////////END SELECTION_VISIBILITY//////////////////////
    
    /////////////////////////////CONTAINER////////////////////////////////
    
    public void setContainerMode(int mode)
    {
        containerMode=mode;
        updateContainer();
    };
    
    public void updateContainer()
    {
        if(containerMode==CONTAIN_CHILDS_TO_NODE)
        {
            Rect r=new Rect(0,0,getSize().x,getSize().y);
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setRectangle(r.getReduced(containerOffset));
            };
        }
        else if(containerMode==CONTAIN_NODE_TO_CHILDS)
        {
            Rect r=getChildsRect();
            setSize(r.getSize().plus(containerOffset));
            
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setCenterPosition(getSize().divide(2));
            };
        };
    };
    
    ///////////////////////////////END CONTAINER/////////////////////////////
    
    /////////////////////////////EVENTS/////////////////////////////////////
    public int testChildMouseEvent(Vec2 pt, BaseEvent evt)
    {
        for(int i=zbuffer.size()-1;i>=0;i--)
        {
            if(childs.get(zbuffer.get(i)).isIntersects(pt) &&
                    childs.get(zbuffer.get(i)).testEvent(evt)) 
            {
                return zbuffer.get(i);
            }
        }
        return -1;
    };
    
    public void dragSelected(Vec2 delta)
    {
        nMode=2;
        for(int j=0;j<selectedChilds.size();j++)
        {
            childs.get(selectedChilds.get(j)).move(delta);
        };
    };
    
    @Override
    protected boolean processMouseEvent(ShapeMouseEvent evt)
    {
        int numChild=testChildMouseEvent(evt.getPosition(),evt);

        if(numChild!=-1) 
        {
            if(!childs.get(numChild).processEvent(evt))
                return super.processMouseEvent(evt);
        }
        else
        {
            return super.processMouseEvent(evt);
        };

        return false;
    };
    
    @Override
    public boolean onMouseClick(ShapeMouseEvent evt){
        if(evt.getButton()==1)
        {
            boolean bSel=bSelected;
            clearSelection();
            setSelected(!bSel);
            return true;
        }
        return false;
    }
    
    int nMode=0;
    @Override
    public boolean onMousePress(ShapeMouseEvent evt){
        if(evt.getButton()==1 && bSelected) 
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
//        if(nMode==1)
//        {
//            if(parent!=null)
//                parent.dragSelected(evt.getDelta());
//        }
//        else if(nMode==2)
//        {
//            dragSelected(evt.getDelta());
//        };
        
        return true;
    }
   
    Vec2 debugMouseMove=new Vec2();
    @Override
    public boolean onMouseMove(ShapeMouseEvent evt){
        debugMouseMove=toGlobal(evt.getPosition());
        
        if(!bMouseIn)
        {
            onMouseIn(evt);
        };
        return true;
    }
    
    @Override
    public boolean onMouseIn(ShapeMouseEvent evt){
        bMouseIn=true;
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i).bMouseIn)
                childs.get(i).onMouseOut(evt);
        };
        if(parent!=null && parent.bMouseIn)
            parent.bMouseIn=false;
        return true;
    }
    
    @Override
    public boolean onMouseOut(ShapeMouseEvent evt){
        bMouseIn=false;
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i).bMouseIn)
                childs.get(i).onMouseOut(evt);
        };
        if(parent!=null && parent.bMouseIn)
            parent.bMouseIn=false;
        return true;
    }
    /////////////////////////END EVENTS/////////////////////////////////////
    
    
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
        if(bMouseIn)
        {
            g.setColor(Color.green);
            Rect r=getGlobalRectangle().getReduced(10);
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
    
    
    public abstract boolean isIntersects(Vec2 pt);
    public abstract boolean isIntersects(Rect r);
    public abstract Vec2 getPortPoint(Vec2 from);
}
