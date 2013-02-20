/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

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
    
    public Rect(Rect r)
    {
        left=r.left;
        right=r.right;
        top=r.top;
        bottom=r.bottom;
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
    
    public void set(Rect r)
    {
        left=r.left;
        right=r.right;
        top=r.top;
        bottom=r.bottom;
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
    
    public Rect getMoved(Vec2 v)
    {
        Rect r=new Rect(this);
        r.move(v);
        return r;
    };
    
    public boolean pointIn(Vec2 v)
    {
        if((v.x>=left) && (v.x<=right) && (v.y<=bottom) && (v.y>=top)) return true;
        return false;
    };
    
    public void add(Rect r)
    {
        if(left>r.left) left=r.left;
        if(top>r.top) top=r.top;
        if(right<r.right) right=r.right;
        if(bottom<r.bottom) bottom=r.bottom;
    };
    
    public void add(Vec2 v)
    {
        if(left>v.x) left=v.x;
        if(top>v.y) top=v.y;
        if(right<v.x) right=v.x;
        if(bottom<v.y) bottom=v.y;
    };
    
    public static Rect fromRectangle2D(Rectangle2D rect)
    {
        return new Rect((float)rect.getX(),(float)rect.getY(),(float)(rect.getX()+rect.getWidth()),(float)(rect.getY()+rect.getHeight()));
    };
    
    public void reduce(float val)
    {
        left+=val;
        right-=val;
        top+=val;
        bottom-=val;
    };
    
    public void increase(float val)
    {
        left-=val;
        right+=val;
        top-=val;
        bottom+=val;
    };
    
    public Rect getReduced(float val)
    {
        Rect r=new Rect(this);
        r.reduce(val);
        return r;
    };
    
    public Rect getIncreased(float val)
    {
        Rect r=new Rect(this);
        r.increase(val);
        return r;
    };
            
    public Vec2 getVertex(int index)
    {
        switch(index)
        {
            case 0: return getTopLeft();
            case 1: return getTopRight();
            case 2: return getBottomRight();
            case 3: return getBottomLeft();
        };
        return null;
    };
}
