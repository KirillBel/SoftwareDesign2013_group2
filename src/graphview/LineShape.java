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
public class LineShape extends EdgeShape {

    public LineShape(NodeShape portA, NodeShape portB )
    {
        portNodeA=portA;
        portNodeB=portB;
    };
     
    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        
        Vec2 portA=null;
        Vec2 portB=null;
        if(childs.size()==0)
        {
            if(portNodeA==null || portNodeB==null) return;
            
            portA=portNodeA.getPortPoint(portNodeB.getPlacement().getCenter());
            portB=portNodeB.getPortPoint(portNodeA.getPlacement().getCenter());
            
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
                point=childs.get(0).getPlacement().getCenter();
                portA=portNodeA.getPortPoint(point);
                if(portA==null) return;
                g.setColor(Color.gray);
                g.fillOval((int)portA.x-5, (int)portA.y-5, (int)portA.x+5, (int)portA.y+5);
                g.setColor(color);
                g.drawLine((int)portA.x, (int)portA.y, (int)point.x, (int)point.y);
            };
            
            if(portNodeB!=null)
            {
                point=childs.get(childs.size()-1).getPlacement().getCenter();
                portB=portNodeB.getPortPoint(point);
                if(portB==null) return;
                g.setColor(Color.gray);
                g.fillOval((int)portB.x-5, (int)portB.y-5, (int)portB.x+5, (int)portB.y+5);
                g.setColor(color);
                g.drawLine((int)portB.x, (int)portB.y, (int)point.x, (int)point.y);
            };
            
            for(int i=0;i<childs.size()-1;i++)
            {
                point=childs.get(i).getPlacement().getCenter();
                point1=childs.get(i+1).getPlacement().getCenter();
                g.drawLine((int)point.x, (int)point.y, (int)point1.x, (int)point1.y);
            };
        };
    }
    
    @Override
    public void insertPoint(Vec2 pt, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rect getBoundingRect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return false;
    }
    
}
