/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

import java.util.ArrayList;

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
        if (((((r1.left >= r2.left) && (r1.left <= r2.right))
            || ((r1.right >= r2.left) && (r1.right <= r2.right))))
            && (((r1.top >= r2.top) && (r1.top <= r2.bottom)) 
            || ((r1.bottom >= r2.top) && (r1.bottom <= r2.bottom))
            || ((r1.top <= r2.top) && (r1.bottom >= r2.bottom))))
        return INCLUSION;

        if (((((r2.left >= r1.left) && (r2.left <= r1.right))
            || ((r2.right >= r1.left) && (r2.right <= r1.right))))
            && (((r2.top >= r1.top) && (r2.top <= r1.bottom)) 
            || ((r2.bottom >= r1.top) && (r2.bottom <= r1.bottom))
            || ((r2.top <= r1.top) && (r2.bottom >= r1.bottom))))
        return INCLUSION;
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
    
    public static int lineseg_point(Vec2 lineA1, Vec2 lineA2, float lineWidth, Vec2 point)
    {
        float distance=distanceToSegment(lineA1,lineA2,point);
        if(distance<=lineWidth) return INCLUSION;
        return EXCLUSION;
    };
    
    public static float distanceToSegment(Vec2 lineA1, Vec2 lineA2, Vec2 point) 
    {

	final float xDelta = lineA2.x - lineA1.x;
	final float yDelta = lineA2.y - lineA1.y;

	if ((xDelta == 0) && (yDelta == 0)) {
	    return lineA1.getDistance(point);
	}

	final float u = ((point.x - lineA1.x) * xDelta + (point.y - lineA1.y) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

	final Vec2 closestPoint;
	if (u < 0) {
	    closestPoint = lineA1;
	} else if (u > 1) {
	    closestPoint = lineA2;
	} else {
	    closestPoint = new Vec2(lineA1.x + u * xDelta, lineA1.y + u * yDelta);
	}

	return closestPoint.getDistance(point);
    }
    
    public static int lineseg_lineseg(Vec2 lineA1,Vec2 lineA2, Vec2 lineB1,Vec2 lineB2, Vec2 outPoint)
    {
        float Epsilon = (float)0.0000001f;

        Vec2 delta = lineB1.minus(lineA1);

        Vec2 dirA = lineA2.minus(lineA1);
        Vec2 dirB = lineB2.minus(lineB1);

        float det = dirA.x * dirB.y - dirA.y * dirB.x;
        float detA = delta.x * dirB.y - delta.y * dirB.x;
        float detB = delta.x * dirA.y - delta.y * dirA.x;

        float absDet = Math.abs(det);

        if (absDet >= Epsilon)
        {
            float invDet = (float)1.0 / det;

            float a = detA * invDet;
            float b = detB * invDet;
            //outA = a;
            //outB = b;
            outPoint=Vec2.lerp(lineA1, lineA2, a);

            if ((a > (float)1.0) || (a < (float)0.0) || (b > (float)1.0) || (b < (float)0.0))
                    return EXCLUSION;
        }
        else
        {
            //outA = outB = (F)0.5;
            outPoint=Vec2.lerp(lineA1, lineA2, 0.5f);

            return EXCLUSION;
        }
        
        return INCLUSION;
    };

    public static int lineseg_lineseg2(Vec2 lineA1,Vec2 lineA2, Vec2 lineB1,Vec2 lineB2, Vec2 outPoint)
    {
        outPoint.set(0, 0);

        Vec2 b = lineA2.minus(lineA1);
        Vec2 d = lineB2.minus(lineB1);
        float bDotDPerp = b.x * d.y - b.y * d.x;

        // if b dot d == 0, it means the lines are parallel so have infinite intersection points
        if (bDotDPerp == 0)
            return EXCLUSION;

        Vec2 c = lineB1.minus(lineA1);
        float t = (c.x * d.y - c.y * d.x) / bDotDPerp;
        if (t < 0 || t > 1)
            return EXCLUSION;

        float u = (c.x * b.y - c.y * b.x) / bDotDPerp;
        if (u < 0 || u > 1)
            return EXCLUSION;

        outPoint.set(lineA1.plus(b.multiply(t)));

        return INCLUSION;
    }
    
    public static int lineseg_rect(Vec2 lineA1,Vec2 lineA2, Rect r, ArrayList<Vec2> outArray)
    {
        Vec2 out = new Vec2();
        
        for(int i=0;i<3;i++)
        {
            if(lineseg_lineseg2(lineA1,lineA2,r.getVertex(i),r.getVertex(i+1),out)==INCLUSION)
            {
                outArray.add(new Vec2(out));
            };
        };
        
        if(lineseg_lineseg2(lineA1,lineA2,r.getVertex(3),r.getVertex(0),out)==INCLUSION)
        {
            outArray.add(new Vec2(out));
        };
        
        if(outArray.size()!=0)
        {
            return INCLUSION;
        };
        return EXCLUSION;
    };
}
