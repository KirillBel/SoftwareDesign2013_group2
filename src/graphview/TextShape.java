/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Graphics2D;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kirill
 */
public class TextShape extends NodeShape{

    @Override
    public Rect getBoundingRect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g) {
    }

    @Override
    public void update() {
        
    }

    @Override
    public boolean isIntersects(Vec2 pt) {
        return placement.pointIn(pt);
    }

    @Override
    public Vec2 getPortPoint(Vec2 from) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
