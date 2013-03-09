/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import graphevents.ShapeMouseEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class LineShape extends BaseShape {

    protected BaseShape portNodeA=null;
    protected BaseShape portNodeB=null;
    protected ArrayList<BaseShape> points=new ArrayList<BaseShape>(); 
    
    public LineShape(BaseShape portA, BaseShape portB)
    {
        portNodeA=portA;
        portNodeB=portB;
        bMoveable=false;
        bResizeable=false;
        bHaveGrip=false;
    };
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        if(bSelected) g.setColor(Color.BLACK);
        
        Vec2 point=null;
        Vec2 point1=null;
        for(int i=0;i<getNumPointsWithPort()-1;i++)
        {
            point=getPointWithPort(i);
            point1=getPointWithPort(i+1);
            
            if(bSelected)
            {
                Color c=g.getColor();
                g.setColor(Color.ORANGE);
                Stroke oldStroke=g.getStroke();
                BasicStroke stroke=new BasicStroke(4);
                g.setStroke(stroke);
                g.drawLine((int)point.x, (int)point.y, (int)point1.x, (int)point1.y);
                g.setStroke(oldStroke);
                g.setColor(c);
            };
            
            g.drawLine((int)point.x, (int)point.y, (int)point1.x, (int)point1.y);
            
            
            if(i==0)
            {
                g.fillOval((int)point.x-3, (int)point.y-3, 6, 6);
            }
            if(i==getNumPointsWithPort()-2)
            {
                g.fillOval((int)point1.x-3, (int)point1.y-3, 6, 6);
            };
        };
        
        super.draw(g);
        return;
        
        
    }
    
    public void insertPoint(Vec2 pt, int index)
    {
        DotShape shape=new DotShape(pt,5);
        points.add(index, shape);
        addChild(shape);
    };
    
    public Vec2 getPoint(int index){
        return points.get(index).getGlobalPosition();
    }
    public void setPoint(Vec2 pt, int index){
        points.get(index).setPosition(pt);
        update();
    };
    
    public int getNumPoints()
    {
        return points.size();
    };
    
    public Vec2 getPointWithPort(int index){
        int offset=0;
        if(portNodeA!=null) offset=1;
        if(index==0) 
        {
            if(portNodeA!=null) return getPortPointA();
        };
        
        if(index==(getNumPointsWithPort()-1)) 
        {
            if(portNodeB!=null) return getPortPointB();
        };
        
        return points.get(index-offset).getGlobalRectangle().getCenter();
    }
    
    public int getNumPointsWithPort()
    {
        int count=points.size();
        if(portNodeA!=null) count++;
        if(portNodeB!=null) count++;
        return count;
    };
    
    public void setPortA(BaseShape shape){
        portNodeA=shape;
        update();
    }
    public void setPortB(BaseShape shape){
        portNodeB=shape;
        update();
    }
    
    public Vec2 getPortPointA(){
        Vec2 point;
        Vec2 portA;
        
        if(getNumPoints()==0) 
        {
            if(portNodeB!=null)
                point=portNodeB.getGlobalRectangle().getCenter();
            else 
                return new Vec2();
        }
        else
            point=points.get(0).getGlobalRectangle().getCenter();
        
        if(portNodeA!=null)
            portA=portNodeA.getPortPoint(point);
        else
            portA=point;
        return portA;
    }
    public Vec2 getPortPointB(){
        Vec2 point;
        Vec2 portB;
        
        if(getNumPoints()==0) 
        {
            if(portNodeA!=null)
                point=portNodeA.getGlobalRectangle().getCenter();
            else 
                return new Vec2();
        }
        else
            point=points.get(points.size()-1).getGlobalRectangle().getCenter();
        
        if(portNodeB!=null)
            portB=portNodeB.getPortPoint(point);
        else
            portB=point;
        return portB;
    }
    
    @Override
    public boolean onMouseClick(ShapeMouseEvent evt){
        if(evt.getButton()==1 && 
                evt.getIntersectedChild()==-1 && 
                isIntersects(evt.getPosition()))
        {
            boolean bSel=bSelected;
            clearSelection();
            setSelected(!bSel);
            return true;
        }
        return false;
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        if(testChildIntersect(pt)!=-1) return true;
        
        Vec2 point=null;
        Vec2 point1=null;
        for(int i=0;i<getNumPointsWithPort()-1;i++)
        {
            point=getPointWithPort(i);
            point1=getPointWithPort(i+1);
            
            if(Intersect.lineseg_point(point, point1, 5,pt)==Intersect.INCLUSION) return true;
        };
        
        return false;
       
    }
    
    @Override
    public boolean isIntersects(Rect r) {
        return false;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        return null;
    }
}
