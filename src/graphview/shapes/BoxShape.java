/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

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
public class BoxShape extends NodeAspect{

    public BoxShape(Rect rect)
    {
        setRectangle(rect);
        this.aspectType=eNodeAspectType.BOX;
    };
    
    public BoxShape(float posX,float posY, float sizeX, float sizeY)
    {
        setRectangle(new Rect(posX,posY,posX+sizeX,posY+sizeY));
        this.aspectType=eNodeAspectType.BOX;
    };
    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace=getGlobalRectangle();
        g.setColor(color);
        g.fillRoundRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y,5,5);
        g.setColor(Color.black);
        
        g.drawRoundRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y,5,5);
        
        super.draw(g);
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return getGlobalRectangle().pointIn(pt);
    }
    
    @Override
    public boolean isIntersects(Rect r) {
        return Intersect.rectangle_rectangle(getGlobalRectangle(), r)==Intersect.INCLUSION;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        ArrayList<Vec2> array=new ArrayList();

        if(Intersect.lineseg_rect(from, getGlobalRectangle().getCenter(), getGlobalRectangle(), array)==Intersect.EXCLUSION)
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

    @Override
    public Rect getContainRect() {
        return getGlobalRectangle().getReduced(5);
    }
}
