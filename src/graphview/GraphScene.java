/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class GraphScene {
    
    public GraphScene(){
        bUpdateMe=true;
    }
    
    public void add(ViewAspect aspect){
        nodes.add(aspect);
    }
    
    public void draw(Graphics2D g){
        g.setTransform(transform);

        for(int i=0;i<nodes.size();i++)
        {
            ((ViewAspect)nodes.get(i)).draw(g);
        };
    }
    
    public Point toScreen(Point pt)
    {
        return (Point)transform.transform(pt, null);
    };
    
    public void onMouseDrag(int nbutton, Point location, Point delta) {
        if(nbutton==1)
        {
//            for(int i=0;i<nodes.size();i++)
//            {
//                if(((ViewAspect)nodes.get(i)).getBoundingRect().contains(toScreen(location)))
//                {
//                    ((ViewAspect)nodes.get(i)).onMouseDrag(toScreen(location),delta);
//                };
//            };
        }
        else if(nbutton==3)
        {
            setOffset(new Point.Float(getOffset().x+delta.x,getOffset().y+delta.y));
            bUpdateMe=true;
        }
    };
    
    public void onMouseWheel(int scrollRotation){
        
        if(scrollRotation>0) {
            transform.scale(0.8, 0.8);
            bUpdateMe=true;
        }
        else if(scrollRotation<0) {
            transform.scale(1.2, 1.2);
            bUpdateMe=true;
        }
    }
    
    public Point.Float getScale(){
        return new Point.Float((float)transform.getScaleX(),(float)transform.getScaleY());
    }
    
    public Point.Float getOffset(){
        return new Point.Float((float)transform.getTranslateX(),(float)transform.getTranslateY());
    }
    
    public void setScale(Point.Float pt){
        transform.setToScale(pt.x, pt.y);
    }
    
    public void setOffset(Point.Float pt){
        transform.setToTranslation(pt.x, pt.y);
    }
    
    public boolean bUpdateMe;
    public Font font=new Font("Arial",Font.PLAIN,20);
    ArrayList nodes=new ArrayList();
    AffineTransform transform=new AffineTransform();
}
