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
        return new Vec2(left+(right-left)/2,top+(bottom-top)/2);
    };
    
    public Vec2 getSize()
    {
        return new Vec2(right-left,bottom-top);
    };
    
    public void setSize(Vec2 size)
    {
        right=left+size.x;
        bottom=top+size.y;
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
    
    public void setPosition(Vec2 v)
    {
        Vec2 size=getSize();
        left=v.x;
        right=left+size.x;
        top=v.y;
        bottom=top+size.y;
    }
    
    public void setCenterPosition(Vec2 v)
    {
        Vec2 size=getSize();
        left=v.x-size.x/2;
        right=left+size.x/2;
        top=v.y-size.y/2;
        bottom=top+size.y/2;
    }
    
    public void move(Vec2 v)
    {
        left+=v.x;
        right+=v.x;
        top+=v.y;
        bottom+=v.y;
    };
    
    public boolean pointIn(Vec2 v)
    {
        if((v.x>=left) && (v.x<=right) && (v.y<=bottom) && (v.y>=top)) return true;
        return false;
    };
}
