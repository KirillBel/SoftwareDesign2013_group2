/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

/**
 *
 * @author Kirill
 */
public class Rect {
    public float left;
    public float right;
    public float top;
    public float bottom;
    
    public Rect()
    {
        left=0;
        right=0;
        top=0;
        bottom=0;
    };
    
    public Rect(float l, float t, float r, float b)
    {
        left=l;
        right=r;
        top=t;
        bottom=b;
    };
    
    public Vec2 getTopLeft()
    {
        return new Vec2(left,top);
    };
    
    public Vec2 getTopRight()
    {
        return new Vec2(right,top);
    };
    
    public Vec2 getBottomLeft()
    {
        return new Vec2(left,bottom);
    };
    
    public Vec2 getBottomRight()
    {
        return new Vec2(right,bottom);
    };
    
    public Vec2 getCenter()
    {
        return new Vec2(left+(right-left)/2,bottom+(top-bottom)/2);
    };
    
    public Vec2 getSize()
    {
        return new Vec2(right-left,top-bottom);
    };
    
    public void setSize(Vec2 size)
    {
        right=left+size.x;
        top=bottom+size.y;
    };
    
    public float getSquare()
    {
        Vec2 size=getSize();
        return size.x*size.y;
    };
    
    public void set(float l, float t, float r, float b)
    {
        left=l;
        right=r;
        top=t;
        bottom=b;
    };
}
