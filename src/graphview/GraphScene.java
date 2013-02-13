/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class GraphScene {
    
    public GraphScene(){
        scale.x=1;
        scale.y=1;
        bUpdateMe=true;
    }
    
    public void add(ViewAspect aspect){
        nodes.add(aspect);
    }
    
    public void draw(Graphics2D g){
        
        g.translate(offset.x, offset.y);
        g.scale(scale.x, scale.y);
        
        for(int i=0;i<nodes.size();i++)
        {
            ((ViewAspect)nodes.get(i)).draw(g);
        };
    }
    
    public void onMouseDrag(int nbutton, Point location, Point delta) {
        if(nbutton==0)
        {
            offset.x+=delta.x;
            offset.y+=delta.y;
            bUpdateMe=true;
        };
    };
    
    public void onMouseWheel(int scrollRotation){
        
        if(scrollRotation>0) {
            scale.x*=0.8;
            scale.y*=0.8;
            bUpdateMe=true;
        }
        else if(scrollRotation<0) {
            scale.x*=1.2;
            scale.y*=1.2;
            bUpdateMe=true;
        }
    }
    
    public boolean bUpdateMe;
    public Font font=new Font("Arial",Font.PLAIN,20);
    Point.Float scale=new Point.Float();
    Point.Float offset=new Point.Float();
    ArrayList nodes=new ArrayList();
}
