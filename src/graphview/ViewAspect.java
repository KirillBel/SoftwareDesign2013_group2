/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Kirill
 */

public abstract class ViewAspect {
 
    public String hint="hint";
    public String label="label";
    public Color color;
    
    public abstract Rectangle getBoundingRect();
    public abstract void draw(Graphics2D g);
    public abstract void update();
    
    public boolean onMouseClick(Point pt){
        return false;
    };
    public boolean onMouseDrag(Point pt){
        return false;
    };
    
}
