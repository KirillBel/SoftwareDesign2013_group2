/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kirill
 */
public class GraphScene {
    Vec2 frameSize=new Vec2(1,1);
    Vec2 offset=new Vec2();
    Vec2 scale=new Vec2(1,1);
    boolean bNeedUpdate=true;
    Font font=new Font("Arial",Font.PLAIN,20);
    ArrayList<BaseShape> shapes=new ArrayList();
    ArrayList<BaseShape> selectedShapes=new ArrayList();
    
    public void add(BaseShape shape){
        shapes.add(shape);
    }
    
    public void draw(Graphics2D g){
        g.translate(offset.x, offset.y);
        g.scale(scale.x, scale.y);
        
        g.setFont(font);

        drawGrid(g,new Vec2(100,100));
        for(int i=0;i<shapes.size();i++)
        {
            shapes.get(i).draw(g);
        };
    }
    
    public void drawGrid(Graphics2D g, Vec2 gridSize)
    {
        Rect frameRect=fromScreen(new Rect(0,0,frameSize.x,frameSize.y));
        if((frameRect.getSize().x/gridSize.x)>50) return;
        if((frameRect.getSize().y/gridSize.y)>50) return;
        
        float startX=frameRect.left-frameRect.left%gridSize.x;
        float startY=frameRect.top-frameRect.top%gridSize.y;
        
        
        g.setColor(Color.lightGray);
        
        Stroke oldStroke=g.getStroke();
        BasicStroke stroke=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{9}, 0);
        g.setStroke(stroke);
        
        for(float i=startX;i<=frameRect.right;i+=gridSize.x)
        {
            g.drawLine((int)i, (int)frameRect.top, (int)i, (int)frameRect.bottom);
        };
        
        for(float i=startY;i<=frameRect.bottom;i+=gridSize.y)
        {
            g.drawLine((int)frameRect.left, (int)i, (int)frameRect.right, (int)i);
        };
        g.setStroke(oldStroke);
    };
    
    public Vec2 fromScreen(Vec2 pt)
    {
        AffineTransform tr=new AffineTransform();
        tr.translate(offset.x, offset.y);
        tr.scale(scale.x, scale.y);
        
        Point.Float pt1=new Point.Float();
        try {
            tr.inverseTransform(pt.toPoint(), pt1);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GraphScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vec2(pt1.x,pt1.y);
    };
    
    public Rect fromScreen(Rect r)
    {
        Vec2 topLeft=fromScreen(r.getTopLeft());
        Vec2 bottomRight=fromScreen(r.getBottomRight());
        return new Rect(topLeft.x,topLeft.y,bottomRight.x,bottomRight.y);
    }
    
    public Vec2 toScreen(Vec2 pt)
    {
        AffineTransform tr=new AffineTransform();
        tr.translate(offset.x, offset.y);
        tr.scale(scale.x, scale.y);
        
        Point.Float pt1=new Point.Float();
        tr.transform(pt.toPoint(), pt1);
        return new Vec2(pt1.x,pt1.y);
    };
    
    public Rect toScreen(Rect r)
    {
        Vec2 topLeft=toScreen(r.getTopLeft());
        Vec2 bottomRight=toScreen(r.getBottomRight());
        return new Rect(topLeft.x,topLeft.y,bottomRight.x,bottomRight.y);
    }
    
    public boolean isNeedUpdate() {
        return bNeedUpdate;
    }
    
    public void setUpdate(boolean val) {
        bNeedUpdate=val;
    }
    
    public void onMouseMove(Vec2 location, Vec2 delta) {
    }
    
    public void onMouseDown(int nbutton, Vec2 location) {
    }
    
    public void onMouseUp(int nbutton, Vec2 location) {
    }
    
    public void onMouseClick(int nbutton, Vec2 location) {
        if(nbutton==1)
        {
            boolean bIntersects=false;
            for(int i=shapes.size()-1;i>=0;i--)
            {
                if(shapes.get(i).isIntersects(fromScreen(location)))
                {
                    setSelected(i, !shapes.get(i).bSelected);
                    bIntersects=true;
                    break;
                };
            };
            if(!bIntersects)
            {
                clearSelection();
            };
            setUpdate(true);
        }
    }
    
    public void onMouseDrag(int nbutton, Vec2 location, Vec2 delta) {
        if(nbutton==1)
        {
            boolean bIntersects=false;
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i).isIntersects(fromScreen(location)))
                {
                    bIntersects=true;
                    break;
                };
            };
            
            if(bIntersects)
            {
                for(int i=0;i<selectedShapes.size();i++)
                {
                    selectedShapes.get(i).move(delta);
                }
            };
            
            setUpdate(true);
        }
        else if(nbutton==3)
        {
            offset=offset.plus(delta);
            setUpdate(true);
        }
    };
    
    public void onResize(Vec2 size)
    {
        frameSize=size;
    }
    
    public void onMouseWheel(int scrollRotation){
        
        if(scrollRotation>0) {
            scale=scale.multiply(0.8f);
            setUpdate(true);
        }
        else if(scrollRotation<0) {
            scale=scale.multiply(1.2f);
            setUpdate(true);
        }
    }
    
    public void setSelected(int Index, boolean bSelected)
    {
        if(bSelected)
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i)==shapes.get(Index)) return;
            };
            selectedShapes.add(shapes.get(Index));
            shapes.get(Index).bSelected=true;
        }
        else
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i)==shapes.get(Index))
                {
                    selectedShapes.remove(i);
                };
            };
            shapes.get(Index).bSelected=false;
        };
    };
    
    public void clearSelection()
    {
        for(int i=0;i<selectedShapes.size();i++)
        {
            selectedShapes.get(i).bSelected=false;
        };
        selectedShapes.clear();
    };
}
