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

enum eShapeType{
    BASE,
    NODE,
    EDGE
};
public abstract class BaseShape {
    public String hint="hint";
    public String label="label";
    public Color color;
    public boolean bSelected=false;
    protected eShapeType shapeType=eShapeType.BASE;
    
    public abstract Rect getBoundingRect();
    
    public eShapeType getType(){
        return shapeType;
    }
    
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
    
    public abstract boolean isIntersects(Vec2 pt);
    
    public void onMouseClick(int button, Vec2 pt){
    }
    public void onMouseDrag(int button, Vec2 location, Vec2 delta){
    };
    
    public void addChild(NodeShape shape)
    {
        for(int i=0;i<childs.size();i++)
        {
            if(childs.get(i)==shape) return;
        };
        childs.add(shape);
    };
    
    public Rect getChildsRect()
    {
        Rect r=new Rect();
        for(int i=0;i<childs.size();i++)
        {
            r.add(childs.get(i).getBoundingRect());
        };
        return r;
    };
    
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
    
    protected ArrayList<NodeShape> childs=new ArrayList<NodeShape>();
}