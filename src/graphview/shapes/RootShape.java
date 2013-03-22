/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Graphics2D;

/**
 *
 * @author Kirill
 */
public class RootShape extends BaseShape{

    public RootShape()
    {
        bHaveGrip=false;
        bResizeable=false;
        bVisible=false;
    };
    
    @Override
    public boolean isIntersects(Vec2 pt) {
        return false;
    }

    @Override
    public boolean isIntersects(Rect r) {
        return false;
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        return new Vec2();
    }
    
}
