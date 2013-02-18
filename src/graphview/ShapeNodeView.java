/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Kirill
 */
public class ShapeNodeView extends NodeView {
    
    public ShapeNodeView()
    {
    };
    
    public ShapeNodeView(Rect rect)
    {
        placement=rect;
    };
    
    public ShapeNodeView(int posX,int posY, int sizeX, int sizeY)
    {
        placement=new Rect(posX,posY,posX+sizeX,posY+sizeY);
    };
    
    public Rect getBoundingRect(){
        return placement;
    }
    public void draw(Graphics2D g)
    {
        g.setColor(color);
        g.fillRect((int)placement.left, (int)placement.top, (int)placement.getSize().x, (int)placement.getSize().y);
        g.setColor(Color.black);
        g.drawRect((int)placement.left, (int)placement.top, (int)placement.getSize().x, (int)placement.getSize().y);
        
        g.drawString(label, (int)placement.left, (int)placement.top);
    };
    public void update()
    {
    };
}
