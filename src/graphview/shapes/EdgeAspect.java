/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import graphview.shapes.BaseShape;
import geometry.Vec2;
import graphview.GraphEdge;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import property.IconStringArray;

/**
 *
 * @author Kirill
 */


public abstract class EdgeAspect extends BaseShape{
    
    public enum eEdgeAspectType
    {
        SIMPLE_LINE
    };

    public enum eLineStyle
    {
        SOLID,
        DOT,
        DASH,
        DASHDOT
    };

    ColorProperty color=null;
    StringProperty hint=null;
    StringProperty label=null;
    IntProperty width=null;
    IconStringProperty lineStyle=null;
    
    GraphEdge graphParent;
    eEdgeAspectType aspectType;
    public boolean bDirectional;
    
    NodeAspect portNodeA;
    NodeAspect portNodeB;
    protected ArrayList<NodeAspect> points=new ArrayList<NodeAspect>(); 
    
    public EdgeAspect()
    {
        color=propCreate("Color", Color.BLACK);
        hint=propCreate("Hint", "none");  
        label=propCreate("Label", "none");
        width=propCreate("Width", 1);
        
        IconStringArray ls=new IconStringArray();
        ls.add(eLineStyle.SOLID, "Solid", new ImageIcon("res/images/lines/line_solid.png"));
        ls.add(eLineStyle.DOT, "Dot", new ImageIcon("res/images/lines/line_dot.png"));
        ls.add(eLineStyle.DASH, "Dash", new ImageIcon("res/images/lines/line_dash.png"));
        ls.add(eLineStyle.DASHDOT, "Dash-Dot", new ImageIcon("res/images/lines/line_dash_dot.png"));
        
        lineStyle=propCreate("Line style",ls);
        highlightWidth=7;
    };
    
    protected BasicStroke getLineStroke()
    {
        float strokeArr[]=null;
        float mult=width.getProp();
        
        switch(getLineStyle())
        {
            case DOT: strokeArr=new float[]{ 1.0f, 3.0f }; break;
            case DASH: strokeArr=new float[]{ 5.0f, 5.0f }; break;
            case DASHDOT: strokeArr=new float[] { 5.0f, 3.0f, 1.0f, 3.0f }; break;
            case SOLID: break;
        };
        
        if(strokeArr!=null)
            for(int i=0;i<strokeArr.length;i++) strokeArr[i]*=mult;
        
        return new BasicStroke(width.getProp(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f,strokeArr,0.0f);
    };
    
    protected BasicStroke createStroke(int width)
    {
        return new BasicStroke(width,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    };
    
    @Override
    public Rect getBoundingRect()
    {
        Rect r=this.getChildsRect();
        Vec2 pA=getPortPointA();
        Vec2 pB=getPortPointB();
        
        if(portNodeA!=null)
        {
            if(r.getSquare()==0) r.set(pA.x,pA.y,pA.x,pA.y);
            else r.add(pA);
        };
        
        if(portNodeB!=null)
        {
            if(r.getSquare()==0) r.set(pB.x,pB.y,pB.x,pB.y);
            else r.add(pB);
        };
        return r;
    };
    
    
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
    
    public void setPortA(NodeAspect shape){
        portNodeA=shape;
        update();
    }
    public void setPortB(NodeAspect shape){
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
    public void removeChild(int index)
    {
        for(int i=0;i<points.size();i++)
        {
            if(points.get(i)==childs.get(index))
            {
                points.remove(i);
            };
        };
        super.removeChild(index);
    };
    
    public eEdgeAspectType getAspectType()
    {
        return aspectType;
    }
    
    @Override
    public eShapeAspect getShapeAspect()
    {
        return eShapeAspect.EDGE;
    }
    
    public String getLabel()
    {        
        return label.getProp();
    }
    
    public void setLabel(String str)
    {
        label.setProp(str);
    }
    
    public Color getColor()
    {        
        return color.getProp();
    }
    
    public void setColor(Color val)
    {
        color.setProp(val);
    }
    
    public eLineStyle getLineStyle()
    {        
        return (eLineStyle)lineStyle.getProp().id;
    }
    
    public void setLineStyle(eLineStyle val)
    {
        lineStyle.setProp(val);
    }
}
