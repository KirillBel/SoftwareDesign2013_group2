/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author IvanKhozyainov
 */
public class ImageShape extends NodeAspect {
    
    Image img1 = null;
    public ImageShape(Rect rect,String imagePath)
    {
        setRectangle(rect);
        img1 = Toolkit.getDefaultToolkit().getImage(imagePath);
        this.aspectType=eNodeAspectType.IMAGE;
    };   
    public ImageShape(Rect rect,Image img)
    {
        setRectangle(rect);
        img1 = img;
        this.aspectType=eNodeAspectType.IMAGE;
    };
    public ImageShape(float posX,float posY, float sizeX, float sizeY,String imagePath)
    {
        setRectangle(new Rect(posX,posY,posX+sizeX,posY+sizeY));
        img1 = Toolkit.getDefaultToolkit().getImage(imagePath);
        this.aspectType=eNodeAspectType.IMAGE;
    };
    
    public ImageShape(float posX,float posY, float sizeX, float sizeY,Image img)
    {
        setRectangle(new Rect(posX,posY,posX+sizeX,posY+sizeY));
        img1 = img;
        this.aspectType=eNodeAspectType.IMAGE;
    };    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace=getGlobalRectangle();
  
        g.drawImage(img1, (int)globalPlace.left, (int)globalPlace.top,(int)globalPlace.getSize().x, (int)globalPlace.getSize().y,null); 
        super.draw(g);
    }
    //some useless stuff in this class (i think)
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
        Rect r=new Rect(0,0,getRectangle().getSize().x,getRectangle().getSize().y);
        return r.getReduced(containerOffset);
    }
}

