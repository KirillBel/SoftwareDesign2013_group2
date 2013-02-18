/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
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
    
    public abstract Rect getBoundingRect();
    public abstract void draw(Graphics2D g);
    public abstract void update();
    
    public boolean onMouseClick(Vec2 pt){
        return false;
    };
    public boolean onMouseDrag(Vec2 location, Vec2 delta){
        return false;
    };
    
}
