/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

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
public class LineShape extends EdgeAspect {
    
    public LineShape(NodeAspect portA, NodeAspect portB)
    {
        super();
        portNodeA=portA;
        portNodeB=portB;
        bMoveable=false;
        bResizeable=false;
        bHaveGrip=false;
        this.aspectType=eEdgeAspectType.SIMPLE_LINE;
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
