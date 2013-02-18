/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Vec2;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Kirill
 */
public abstract class  EdgeView extends ViewAspect {
    public abstract Vec2 getPoint(int index);
    public abstract void setPoint(Vec2 pt, int index);
    public abstract void insertPoint(Vec2 pt, int index);
    public abstract void removePoint(Vec2 pt, int index);
}
