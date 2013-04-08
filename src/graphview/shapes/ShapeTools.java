/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import geometry.Vec2;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
        int ln=0;
        for (String line : txt.split("\n"))
        {
            Rect bounds=Rect.fromRectangle2D(font.getStringBounds(line, frc));
            
            if(ln==0) y+=bounds.getSize().y*0.75;
            else y += bounds.getSize().y;
            
            g.drawString(line, x, y);
            ln++;
        }
    };
    
    public static void drawText(Rect bounds, Graphics2D g, String txt,Font font, FontRenderContext frc)
    {
        Rectangle prevClip=g.getClipBounds();
        g.setClip((int)bounds.left, (int)bounds.top, (int)bounds.getSize().x, (int)bounds.getSize().y);
        Vec2 current=new Vec2();
        int ln=0;
        for (String line : txt.split("\n"))
        {
            Rect lineBounds=Rect.fromRectangle2D(font.getStringBounds(line, frc));
            if(ln==0) current.y+=lineBounds.getSize().y*0.75;
            else current.y += lineBounds.getSize().y;
            
            current.x=0;
            
            for (String word : line.split(" "))
            {
                word+=" ";
                Rect wordBounds=Rect.fromRectangle2D(font.getStringBounds(word, frc));
                if((current.x+wordBounds.getSize().x)>bounds.getSize().x && current.x!=0)
                {
                    current.y+=wordBounds.getSize().y;
                    current.x=0;
                };
                g.drawString(word, current.x+bounds.left, current.y+bounds.top);
                current.x+=wordBounds.getSize().x;
            }
            ln++;
        }
        g.setClip((int)prevClip.x, (int)prevClip.y, (int)prevClip.width, (int)prevClip.height);
    };
}
