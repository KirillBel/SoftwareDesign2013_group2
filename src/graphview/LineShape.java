/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Kirill
 */
public class LineShape extends BaseShape {

    protected BaseShape portNodeA=null;
    protected BaseShape portNodeB=null;
    
    public LineShape(BaseShape portA, BaseShape portB)
    {
        portNodeA=portA;
        portNodeB=portB;
        bMoveable=false;
    };
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        if(bSelected) g.setColor(Color.red);
        
        Vec2 portA=null;
        Vec2 portB=null;
        if(childs.size()==0)
        {
            if(portNodeA==null || portNodeB==null) return;
            
            portA=portNodeA.getPortPoint(portNodeB.getGlobalPlacement().getCenter());
            portB=portNodeB.getPortPoint(portNodeA.getGlobalPlacement().getCenter());
            
            if(portA==null || portB==null) return;
            g.fillOval((int)portA.x-3, (int)portA.y-3, 6, 6);
            g.fillOval((int)portB.x-3, (int)portB.y-3, 6, 6);
            g.drawLine((int)portA.x, (int)portA.y, (int)portB.x, (int)portB.y);
        }
        else
        {
            Vec2 point=null;
            Vec2 point1=null;
            if(portNodeA!=null)
            {
                point=childs.get(0).getGlobalPlacement().getCenter();
                portA=portNodeA.getPortPoint(point);
                if(portA==null) return;
                g.fillOval((int)portA.x-3, (int)portA.y-3, 6, 6);
                g.drawLine((int)portA.x, (int)portA.y, (int)point.x, (int)point.y);
            };
            
            if(portNodeB!=null)
            {
                point=childs.get(childs.size()-1).getGlobalPlacement().getCenter();
                portB=portNodeB.getPortPoint(point);
                if(portB==null) return;
                g.fillOval((int)portB.x-3, (int)portB.y-3, 6, 6);
                g.drawLine((int)portB.x, (int)portB.y, (int)point.x, (int)point.y);
            };
            
            for(int i=0;i<childs.size()-1;i++)
            {
                point=childs.get(i).getGlobalPlacement().getCenter();
                point1=childs.get(i+1).getGlobalPlacement().getCenter();
                g.drawLine((int)point.x, (int)point.y, (int)point1.x, (int)point1.y);
            };
        };
        
        super.draw(g);
    }
    
    
    public Vec2 getPoint(int index){
        return getChild(index).getGlobalPosition();
    }
    public void setPoint(Vec2 pt, int index){
        getChild(index).setGlobalPosition(pt);
        update();
    };
    
    public void setPortA(BaseShape shape){
        portNodeA=shape;
        update();
    }
    public void setPortB(BaseShape shape){
        portNodeB=shape;
        update();
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return testChildIntersect(pt)!=-1;
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
