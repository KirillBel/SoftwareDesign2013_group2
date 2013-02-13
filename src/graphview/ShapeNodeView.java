/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

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
    
    public ShapeNodeView(Rectangle rect)
    {
        placement=rect;
    };
    
    public ShapeNodeView(int posX,int posY, int sizeX, int sizeY)
    {
        placement=new Rectangle(posX,posY,sizeX,sizeY);
    };
    
    public Rectangle getBoundingRect(){
        return placement;
    }
    public void draw(Graphics2D g)
    {
        g.setColor(color);
        g.fillRect(placement.x, placement.y, placement.width, placement.height);
        g.setColor(Color.black);
        g.drawRect(placement.x, placement.y, placement.width, placement.height);
        
        g.drawString(label, placement.x, placement.y);
    };
    public void update()
    {
    };
}
