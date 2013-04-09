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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kirill
 */
public class ShapeTools {
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
    
    public static boolean isTextPlaced(Rect bounds, String txt,Font font, FontRenderContext frc, Rect cont)
    {
        Rect contain=new Rect();
        String formatted=formatTextToBounds(bounds, txt, font, frc,contain);
        if(cont!=null) cont.set(contain);
        if(bounds.getSize().x<contain.getSize().x) return false;
        if(bounds.getSize().y<contain.getSize().y) return false;
        return true;
    };
    
    public static Rect getTextBounds(String txt,Font font, FontRenderContext frc)
    {
        Vec2 current=new Vec2();
        int ln=0;
        for (String line : txt.split("\n"))
        {
            Rect lineBounds=Rect.fromRectangle2D(font.getStringBounds(line, frc));
            if(ln==0) current.y+=lineBounds.getSize().y*0.75;
            else current.y += lineBounds.getSize().y;
            
            current.x=Math.max(current.x,lineBounds.getSize().x);
            
            ln++;
        }
        return new Rect(0,0,current.x+10,current.y+10);
    };
    
    public static String formatTextToBounds(Rect bounds, String txt,Font font, FontRenderContext frc, Rect containRect)
    {
        Vec2 current=new Vec2();
        String output="";
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
                    output+="\n";
                };
                output+=word;
                current.x+=wordBounds.getSize().x;
            }
            output+="\n";
            ln++;
        }
        if(containRect!=null) containRect.set(0,0,current.x,current.y);
        return output;
    };
    
    public static void drawText(Rect bounds, Graphics2D g, String txt,Font font, FontRenderContext frc)
    {
        Shape prevClip=g.getClip();
        g.setClip((int)bounds.left, (int)bounds.top, (int)bounds.getSize().x, (int)bounds.getSize().y);
        
        String formatted=formatTextToBounds(bounds, txt, font, frc,null);
        drawText(bounds.left,bounds.top, g, formatted, font, frc);
        
        g.setClip(prevClip);
    };
    
    public static Image getImage(String path)
    {
        Image img1=null;
        try {
            img1 = ImageIO.read(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(ImageShape.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img1;
    };
}
