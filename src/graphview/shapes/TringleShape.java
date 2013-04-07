
package graphview.shapes;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
/**
 *
 * @author Ivan Khozyainov
 */
public class TringleShape extends NodeAspect {

    Vec2 p1 = new Vec2();
    Vec2 p2 = new Vec2();
    Vec2 p3 = new Vec2();
    int myType= 0;
    int myCon= 0;

    public TringleShape(Vec2 position, float radius,int con)
    {
        Rect r=new Rect(position.x-radius,position.y-radius,position.x+radius,position.y+radius);
        setRectangle(r);
        myCon=con;
        this.aspectType=eNodeAspectType.TRINGLE;
        if (con <90){
            myType=1;
        }else if(con <180){
            myType=2;
        }else if(con <270){
            myType=3;
        }else if(con <360){
            myType=4;
        }       
    };
    
    public TringleShape(float posX, float posY, float sizeX, float sizeY, String type) {
        setRectangle(new Rect(posX, posY, posX + sizeX, posY + sizeY));
        this.aspectType = NodeAspect.eNodeAspectType.TRINGLE;
        switch (type) {
            case "top":
                myType = 1;
                break;
            case "bottom":
                myType = 2;
                break;
            case "left":
                myType = 3;
                break;
            case "right":
                myType = 4;
                break;
        }
    }
    ;
    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace = getGlobalRectangle();
        if (myType == 1) {
            p3.x = (int) globalPlace.left;
            p3.y = (int) globalPlace.top;
            p2.x = (int) globalPlace.right;
            p2.y = (int) globalPlace.top;
            p1.x = (int) globalPlace.left+ (int) globalPlace.getSize().x / 2;
            p1.y = (int) globalPlace.bottom;
        }
        if (myType == 2) {
            p1.x = (int) globalPlace.left + (int) globalPlace.getSize().x / 2;
            p1.y = (int) globalPlace.top;
            p2.x = (int) globalPlace.right;
            p2.y = (int) globalPlace.bottom;
            p3.x = (int) globalPlace.left;
            p3.y = (int) globalPlace.bottom;
        }
        if (myType == 3) {
            p3.x = (int) globalPlace.left;
            p3.y = (int) globalPlace.top;
            p1.x = (int) globalPlace.right;
            p1.y = (int) globalPlace.bottom - (int) globalPlace.getSize().y / 2;
            p2.x = (int) globalPlace.left;
            p2.y = (int) globalPlace.bottom;
        }
        if (myType == 4) {
            p1.x = (int) globalPlace.left;
            p1.y = (int) globalPlace.bottom - (int) globalPlace.getSize().y / 2;
            p2.x = (int) globalPlace.right;
            p2.y = (int) globalPlace.top;
            p3.x = (int) globalPlace.right;
            p3.y = (int) globalPlace.bottom;
        }
        
        if(myCon == 0){
        int[] xs = {(int) p1.x, (int) p2.x, (int) p3.x};
        int[] ys = {(int) p1.y, (int) p2.y, (int) p3.y};
        Polygon triangle = new Polygon(xs, ys, xs.length);
        g.setColor(color.getProp());
        g.fillPolygon(triangle);
        g.setColor(Color.black);
        g.drawPolygon(triangle);

        super.draw(g);
        }else{
            
        }
        
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return getGlobalRectangle().pointIn(pt);
    }

    @Override
    public boolean isIntersects(Rect r) {
        return Intersect.rectangle_rectangle(getGlobalRectangle(), r) == Intersect.INCLUSION;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        Vec2 v1 = new Vec2();
        Vec2 v2 = new Vec2();
        Vec2 v3 = new Vec2();
        Vec2 v4 = new Vec2();
        //Intersect.line_ellipsecenter(from, getGlobalRectangle(), v1, v2);
        Intersect.line_tringlecenter(from, getGlobalRectangle(), p1, p2, v1);
        Intersect.line_tringlecenter(from, getGlobalRectangle(), p2, p3, v2);
        Intersect.line_tringlecenter(from, getGlobalRectangle(), p1, p3, v3);
        float hz = p1.x - p3.x;
        float hz2 = (p1.y - p3.y) / 2;
        float gip = (float) Math.sqrt(hz * hz + hz2 * hz2);
        // find border        
        if (from.getDistance(p1) <= from.getDistance(p3) && from.getDistance(p2) <= from.getDistance(p3)) {
            if(getGlobalRectangle().pointIn(v1)){
               v4 = v1;
            }else if(from.getDistance(p1) <= from.getDistance(p2)) {
                v4=p1;
            }else{
                v4=p2;
            }
        }
        if (from.getDistance(p2) <= from.getDistance(p1) && from.getDistance(p3) <= from.getDistance(p1)) {
            if(getGlobalRectangle().pointIn(v2)){
            v4 = v2;
            }else if(from.getDistance(p2) <= from.getDistance(p3)) {
                v4=p2;
            }else{
                v4=p3;
            }
        }
        if (from.getDistance(p1) <= from.getDistance(p2) && from.getDistance(p3) <= from.getDistance(p2)) {
            if(getGlobalRectangle().pointIn(v3)){
            v4 = v3;
            }else if(from.getDistance(p1) <= from.getDistance(p3)) {
                v4=p1;
            }else{
                v4=p3;
            }
        }
        return v4;
    }

    @Override
    public Rect getContainRect() {
        Vec2 center=getRectangle().getSize().divide(2);
        float temp = (getGlobalRectangle().getSize().x * getGlobalRectangle().getSize().y)/(getGlobalRectangle().getSize().x+getGlobalRectangle().getSize().y);
     
        Rect r=new Rect(center.x-temp/2,center.y-temp/2,center.x+temp/2,center.y+temp/2);
        return r;
    }
}
