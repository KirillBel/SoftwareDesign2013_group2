/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

/**
 *
 * @author Kirill
 */


public class Intersect {
    
    public static final int EXCLUSION = 0;
    public static final int INCLUSION = 1;
    public static final int OVERLAP = 2;
    
    public static int rectangle_point(Rect r, Vec2 v)
    {
        if((v.x>=r.left) && (v.x<=r.right) && (v.y<=r.bottom) && (v.y>=r.top)) return INCLUSION;
        return EXCLUSION;
    }
    
    public static int rectangle_rectangle(Rect r1, Rect r2)
    {
        if(((r2.left>=r1.left) && (r2.left<=r1.right)) || ((r2.right>=r1.left) && (r2.right<=r1.right))){
            if(((r2.top>=r1.top) && (r2.top<=r1.bottom)) || ((r2.bottom>=r1.top) && (r2.right<=r1.bottom))){
                return INCLUSION;
            }
        }
        return EXCLUSION;
    }
    
    public static float line_point_distance(Vec2 lineA, Vec2 lineB, Vec2 point)
    {
        float normalLength = lineA.getDistance(lineB);
        return (float)Math.abs((point.x - lineA.x) * (lineB.y - lineA.y) - (point.y - lineA.y) * (lineB.x - lineA.x)) / normalLength;
    }
    
    public static int line_point(Vec2 lineA, Vec2 lineB, float lineWidth, Vec2 point)
    {
        float distance=line_point_distance(lineA,lineB,point);
        if(distance<=lineWidth) return INCLUSION;
        return EXCLUSION;
    }
}
