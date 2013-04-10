/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;

/**
 *
 * @author Kirill
 */
public class TextShape extends BoxShape{
    FontRenderContext frc=new FontRenderContext(null, true,false);
    Rect bounds=null;
    boolean bFitText=false;
    ButtonShape button=null;
    
    FontProperty fontProp=null;
    StringProperty textProp=null;
    ColorProperty background=null;
    
    public TextShape(String text_)
    {
        super(0,0,10,10);
        
        textProp=propCreate("Text", new String(text_));
        fontProp=propCreate("Font", new Font("Arial",Font.PLAIN,15)); 
        background=propCreate("Background", (Color)null);
        setColor(Color.black);
        
        updateTextBounds();

        //bMoveable=false;
        //bResizeable=false;
        bReceiveMouseDrag=false;
        //bReceiveMousePress=false;
        //bReceiveMouseClick=false;
        
        this.aspectType=eNodeAspectType.TEXT;
        
        button=new ButtonShape(new Rect(0,0,15,15),ShapeTools.getImage("res/icons/plus.png"));
        button.bEnableBackground=false;
        button.setVisible(false);
        button.addListener(new ButtonShapeListener() {
            @Override
            public void onButtonDown() {
                fitParentToText();
            }

            @Override
            public void onButtonUp() {
            }
        });
        addChild(button);
    };
    
    public void fitParentToText()
    {
        if(parent.isNode()) 
        {
            for(int i=0;i<5;i++) //ACHTUNG
                ((NodeAspect)parent).fitChildRect(ShapeTools.getTextBounds(textProp.getProp(),fontProp.getProp(), frc));
        };
    };
    
    public void updateProperties(boolean bUpdateToProp)
    {
        if(!bUpdateToProp) updateTextBounds();
        super.updateProperties(bUpdateToProp);
    };
    
    public String getText()
    {
        return new String(textProp.getProp());
    };
    
    public void setText(String txt)
    {
        textProp.setProp(txt);
        updateTextBounds();
    };
    
    public void updateTextBounds()
    {
        if(getParentContainerType()==eContainerType.RESIZE_CHILDS_TO_PARENT)
        {
            bFitText=true;
            if(ShapeTools.isTextPlaced(getGlobalRectangle(), textProp.getProp(),fontProp.getProp(), frc,bounds))
                button.setVisible(false);
            else 
                button.setVisible(true);
        }
        else 
        {
            bounds=ShapeTools.getTextBounds(textProp.getProp(), fontProp.getProp(), frc);
            bFitText=false;
        } 
        
        setSize(bounds.getSize());
        if(parent!=null) ((NodeAspect) parent).updateContainer();
    };
    
    @Override
    public void update()
    {
        updateTextBounds();
        super.update();
    };
    
    protected Vec2 onResize(Vec2 oldSize, Vec2 newSize){
        if(button!=null) button.setRectangle(new Rect(newSize.x-15,newSize.y-15,newSize.x,newSize.y));
        return super.onResize(oldSize, newSize);
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setFont(fontProp.getProp());
        
        Rect globalPlace=getGlobalRectangle();
        
        if(background.getProp()!=null)
        {
            g.setColor(background.getProp());
            g.fillRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        }
        else if(parent!=null && parent.isNode())
        {
            g.setColor(new Color(255,255,255,200));
            g.fillRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        };
        
        g.setColor(color.getProp());
        if(bFitText) ShapeTools.drawText(globalPlace, g, textProp.getProp(),fontProp.getProp(), frc);
        else ShapeTools.drawText(globalPlace.left, globalPlace.top, g, textProp.getProp(),fontProp.getProp(), frc);
        
        if(bSelected) drawGrip(g);
        
        for(int i=0;i<zbuffer.size();i++)
        {
            if(childs.get(zbuffer.get(i))!=null) 
                childs.get(zbuffer.get(i)).draw(g);
        };
    }
    
    @Override
    public Rect getContainRect() {
        Rect r=new Rect(0,0,getRectangle().getSize().x,getRectangle().getSize().y);
        return r.getReduced(containerOffset);
    }
    
    public void setBackgroundColor(Color c)
    {
        background.setProp(c);
    };
}
