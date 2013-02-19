/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class BoxShape extends NodeShape{

    public BoxShape(Rect rect)
    {
        placement=rect;
    };
    
    public BoxShape(int posX,int posY, int sizeX, int sizeY)
    {
        placement=new Rect(posX,posY,posX+sizeX,posY+sizeY);
    };
    
    @Override
    public Rect getBoundingRect() {
        return getPlacement();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int)placement.left, (int)placement.top, (int)placement.getSize().x, (int)placement.getSize().y);
        g.setColor(Color.black);
        
        if(bSelected)
        {
            Stroke oldStroke=g.getStroke();
            BasicStroke stroke=new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{9}, 0);
            g.setStroke(stroke);
            g.drawRect((int)placement.left, (int)placement.top, (int)placement.getSize().x, (int)placement.getSize().y);
            g.setStroke(oldStroke);
            
        }
        else g.drawRect((int)placement.left, (int)placement.top, (int)placement.getSize().x, (int)placement.getSize().y);
        
        super.draw(g);
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return placement.pointIn(pt);
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        ArrayList<Vec2> array=new ArrayList();
        
        if(Intersect.line_rect(from, getPlacement().getCenter(), placement, array)==Intersect.EXCLUSION)
        {
            System.err.printf("Ошибка при расчете пересечения\n");
            return null;
        };
        
        float minDist=array.get(0).getDistance(from);
        int minIndex=0;
        for(int i=0;i<array.size();i++)
        {
            float dist=array.get(i).getDistance(from);
            if(dist<minDist) minIndex=i;
        };
        
        return array.get(minIndex);
    }
}
