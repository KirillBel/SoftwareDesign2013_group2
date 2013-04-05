/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

/**
 *
 * @author Kirill
 */
public class ShapeTools {
    public static Rect getTextBounds(String txt,Font font, FontRenderContext frc)
    {
        float x=0;
        float y=0;
        int lines=0;
        Rect bounds=null;
        
        for (String line : txt.split("\n"))
        {
            bounds=Rect.fromRectangle2D(font.getStringBounds(line, frc));
            x=Math.max(x, bounds.getSize().x);
            y+=bounds.getSize().y;
            lines++;
        }
        
        return new Rect(0,0,x,y);
    };
    
    public static void drawText(float x, float y, Graphics2D g, String txt,Font font, FontRenderContext frc)
    {
        for (String line : txt.split("\n"))
        {
            Rect bounds=Rect.fromRectangle2D(font.getStringBounds(line, frc));
            g.drawString(line, x, y += bounds.getSize().y);
        }
    };
}
