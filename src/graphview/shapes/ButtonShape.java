/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.shapes;

import geometry.Rect;
import graphevents.ShapeMouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import javax.swing.Icon;

/**
 *
 * @author Kirill
 */

interface ButtonShapeListener {
    public void onButtonDown();
    public void onButtonUp();
}

public class ButtonShape extends BoxShape{
    
    String text=null;
    Image image=null;
    boolean buttonPressed=false;
    public boolean bEnableBackground=true;
    
    ArrayList<ButtonShapeListener> listeners=new ArrayList<ButtonShapeListener>();
    
    public ButtonShape(Rect place, String txt)
    {
        super(place);
        setColor(Color.gray);
        text=txt;
    };
    
    public ButtonShape(Rect place, Image img)
    {
        super(place);
        setColor(Color.gray);
        image=img;
    };
    
    public void addListener(ButtonShapeListener list)
    {
        listeners.add(list);
    };
    
    @Override
    public void draw(Graphics2D g) {
        Rect globalPlace=getGlobalRectangle();
        Shape prevClip=g.getClip();
        g.setClip((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        
        if(bEnableBackground)
        {
            g.setColor(Color.BLACK);
            g.drawRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
            g.setColor(color.getProp());
            g.fillRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        };
        
        if(text!=null) g.drawString(text, globalPlace.left, globalPlace.top);
        if(image!=null) g.drawImage(image, (int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y,null);
        if(buttonPressed) 
        {
            g.setColor(new Color(0,0,0,50));
            g.fillRect((int)globalPlace.left, (int)globalPlace.top, (int)globalPlace.getSize().x, (int)globalPlace.getSize().y);
        }
        
        
        g.setClip(prevClip);
    }
    
    public void notifyButton(boolean bPressed)
    {
        for(int i=0;i<listeners.size();i++)
        {
            if(bPressed) listeners.get(i).onButtonDown();
            else listeners.get(i).onButtonUp();
        };
    };
    
    @Override
    public Rect getContainRect() {
        Rect r=new Rect(0,0,getRectangle().getSize().x,getRectangle().getSize().y);
        return r.getReduced(containerOffset);
    }
    
    @Override
    public boolean onMousePress(ShapeMouseEvent evt){
        buttonPressed=true;
        notifyButton(buttonPressed);
        return true;
    }
    
    @Override
    public boolean onMouseRelease(ShapeMouseEvent evt){
        buttonPressed=false;
        notifyButton(buttonPressed);
        return true;
    }
    
    public boolean onMouseOut(ShapeMouseEvent evt){
        if(buttonPressed==true)
        {
            buttonPressed=false;
            notifyButton(buttonPressed);
        };
        
        return super.onMouseOut(evt);
    }
}
