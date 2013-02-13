/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Kirill
 */
public abstract class  EdgeView extends ViewAspect {
    public abstract Point getPoint(int index);
    public abstract void setPoint(Point2D pt, int index);
    public abstract void insertPoint(Point2D pt, int index);
    public abstract void removePoint(Point2D pt, int index);
}
