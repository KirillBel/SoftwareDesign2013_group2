/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphevents;

import java.awt.event.MouseEvent;

/**
 *
 * @author Kirill
 */
public class ShapeEvents {
    protected boolean bReceiveMouseClick=true;
    protected boolean bReceiveMouseDrag=true;
    protected boolean bReceiveMousePress=true;
    protected boolean bReceiveMouseRelease=true;
    protected boolean bReceiveMouseMove=true;
    
    public boolean isReceivedMouseClick() {
        return bReceiveMouseClick;
    }
    
    public boolean isReceivedMouseDrag() {
        return bReceiveMouseDrag;
    }
    
    public boolean isReceivedMousePress() {
        return bReceiveMousePress;
    }
    
    public boolean isReceivedMouseRelease() {
        return bReceiveMouseRelease;
    }
    
    public boolean isReceivedMouseMove() {
        return bReceiveMouseMove;
    }
    
    
    protected boolean testEvent(BaseEvent evt)
    {
        if(evt.getType()==BaseEvent.EVENT_TYPE_MOUSE)
        {
            ShapeMouseEvent mEvt=(ShapeMouseEvent)evt;
            if(mEvt.getSubtype()==ShapeMouseEvent.CLICK && bReceiveMouseClick) 
                return true;
            if(mEvt.getSubtype()==ShapeMouseEvent.DRAG && bReceiveMouseDrag) 
                return true;
            if(mEvt.getSubtype()==ShapeMouseEvent.MOVE && bReceiveMouseMove) 
                return true;
            if(mEvt.getSubtype()==ShapeMouseEvent.PRESS && bReceiveMousePress) 
                return true;
            if(mEvt.getSubtype()==ShapeMouseEvent.RELEASE && bReceiveMouseRelease) 
                return true;
        };
        return false;
    }
    
    
    public boolean processEvent(BaseEvent evt)
    {
        switch(evt.getType())
        {
            case BaseEvent.EVENT_TYPE_MOUSE: 
                return processMouseEvent((ShapeMouseEvent)evt);
        };
        return false;
    };
    
    protected boolean processMouseEvent(ShapeMouseEvent evt)
    {
        boolean bRet=false;
        switch(evt.getSubtype())
        {
            case ShapeMouseEvent.CLICK:     bRet=onMouseClick(evt); break;
            case ShapeMouseEvent.DRAG:      bRet=onMouseDrag(evt); break;
            case ShapeMouseEvent.MOVE:      bRet=onMouseMove(evt); break;
            case ShapeMouseEvent.PRESS:     bRet=onMousePress(evt); break;
            case ShapeMouseEvent.RELEASE:   bRet=onMouseRelease(evt); break;
            default: return false;
        };
        return bRet;
    };
    
    public boolean onMouseClick(ShapeMouseEvent evt){
        return false;
    }
    
    public boolean onMousePress(ShapeMouseEvent evt){
        return false;
    }
    
    public boolean onMouseRelease(ShapeMouseEvent evt){
        return false;
    }
    
    public boolean onMouseDrag(ShapeMouseEvent evt){
        return false;
    }
   
    public boolean onMouseMove(ShapeMouseEvent evt){
        return false;
    }
    
    public boolean onMouseIn(ShapeMouseEvent evt){
        return false;
    }
    
    public boolean onMouseOut(ShapeMouseEvent evt){
        return false;
    }
}
