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
public class BoxShape extends BaseShape{

    public BoxShape(Rect rect)
    {
        setLocalPlacement(rect);
    };
    
    public BoxShape(float posX,float posY, float sizeX, float sizeY)
    {
        setLocalPlacement(new Rect(posX,posY,posX+sizeX,posY+sizeY));
    };
    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace=getGlobalPlacement();
        g.setColor(color);
        g.fillRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        g.setColor(Color.black);
        
        if(bSelected)
        {
            Stroke oldStroke=g.getStroke();
            BasicStroke stroke=new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{9}, 0);
            g.setStroke(stroke);
            g.drawRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
            g.setStroke(oldStroke);
            
        }
        else g.drawRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        
        super.draw(g);
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        if(bUnbodied) return true;
        return getLocalPlacement().pointIn(pt);
    }
    
    @Override
    public boolean isIntersects(Rect r) {
        if(bUnbodied) return true;
        return Intersect.rectangle_rectangle(getLocalPlacement(), r)==Intersect.INCLUSION;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        ArrayList<Vec2> array=new ArrayList();

        if(Intersect.lineseg_rect(from, getGlobalPlacement().getCenter(), getGlobalPlacement(), array)==Intersect.EXCLUSION)
        {
            System.err.printf("Ошибка при расчете пересечения\n");
            return from;
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
