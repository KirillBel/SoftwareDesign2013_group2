/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Kirill
 */
public class Vec2 {
    public float x;
    public float y;
    
    public Vec2()
    {
        x=0;
        y=0;
    };
    
    public Vec2(float X, float Y)
    {
        x=X;
        y=Y;
    };
    
    public Vec2(Vec2 v)
    {
        x=v.x;
        y=v.y;
    };
    
    public void set(float X, float Y)
    {
        x=X;
        y=Y;
    };
    
    public void set(Vec2 v)
    {
        x=v.x;
        y=v.y;
    };
    
    public boolean equals(Vec2 v)
    {
        if(x==v.x && y==v.y) return true;
        return false;
    };
    
    public Vec2 plus(float f)
    {
        return new Vec2(this.x+f,this.y+f);
    };
    
    public Vec2 plus(Vec2 v)
    {
        return new Vec2(this.x+v.x,this.y+v.y);
    };
    
    public Vec2 minus(float f)
    {
        return new Vec2(this.x-f,this.y-f);
    };
    
    public Vec2 minus(Vec2 v)
    {
        return new Vec2(this.x-v.x,this.y-v.y);
    };
    
    public Vec2 multiply(float f)
    {
        return new Vec2(this.x*f,this.y*f);
    };
    
    public Vec2 multiply(Vec2 v)
    {
        return new Vec2(this.x*v.x,this.y*v.y);
    };
    
    public Vec2 divide(float f)
    {
        return new Vec2(this.x/f,this.y/f);
    };
    
    public Vec2 divide(Vec2 v)
    {
        return new Vec2(this.x/v.x,this.y/v.y);
    };
    
    public float dot(Vec2 v)
    {
        return x*v.x + y*v.y;
    };
    
    public float cross(Vec2 v)
    {
        return x*v.y - y*v.x;
    };
    
    public Vec2 getDistance2D(Vec2 v)
    {
        return new Vec2(v.x-x,v.y-y);
    };
    
    public float getDistance(Vec2 v)
    {
        return getDistance2D(v).getLength();
    }
    
    public void normalize()
    {
        float len=1.0f/getLength();
        x *= len; y *= len; 
    };
    
    public Vec2 getNormalized()
    {
        Vec2 v=new Vec2(this);
        v.normalize();
        return v;
    };
    
    public float getLength()
    {
        return (float)Math.sqrt(x*x+y*y);
    };
    
    public float getLength2()
    {
        return (float)x*x+y*y;
    };
    
    public void setLength(float val)
    {
        float mul=val/getLength();
        x *= mul; y *= mul; 
    };
    
    public Vec2 getWithLength(float val)
    {
        Vec2 v=new Vec2(this);
        v.setLength(val);
        return v;
    };
    
    public void rotate(float angle)
    {
        float nX=x,nY=y;
        x=(float)(nX*Math.cos(angle)-nY*Math.sin(angle));
        y=(float)(nX*Math.sin(angle)+nY*Math.cos(angle));
    };
    
    public Vec2 getRotated(float angle)
    {
        Vec2 v=new Vec2(this);
        v.rotate(angle);
        return v;
    };
    
    public Point.Float toPoint()
    {
        return new Point.Float(x,y);
    };
    
    public static Vec2 fromPoint(Point pt)
    {
        return new Vec2(pt.x,pt.y);
    };
    
    public static Vec2 lerp(Vec2 A, Vec2 B, float c)
    {
        return new Vec2(A.x+(B.x-A.x)*c,A.y+(B.y-A.y)*c);
    };
    
    @Override
    public String toString()
    {
        return String.format("[%G; %G]", x,y);
    };
}
