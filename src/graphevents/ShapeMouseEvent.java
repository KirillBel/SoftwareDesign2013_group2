/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphevents;

import geometry.Vec2;

/**
 *
 * @author Kirill
 */
public class ShapeMouseEvent extends BaseEvent{
    private int subType=0;
    int intersectChild=-1;
    int mouseButton=0;
    Vec2 mousePos;
    Vec2 mouseDelta;
    
    public static final int MOVE=1;
    public static final int DRAG=2;
    public static final int CLICK=3;
    public static final int PRESS=4;
    public static final int RELEASE=5;
    
    public ShapeMouseEvent(int subType_, int button, Vec2 pos, Vec2 delta)
    {
        super(BaseEvent.EVENT_TYPE_MOUSE);
        subType=subType_;
        mouseButton=button;
        mousePos=pos;
        mouseDelta=delta;
    };
    
    public ShapeMouseEvent(ShapeMouseEvent evt)
    {
        super(BaseEvent.EVENT_TYPE_MOUSE);
        subType=evt.subType;
        mouseButton=evt.mouseButton;
        mousePos=evt.mousePos;
        mouseDelta=evt.mouseDelta;
    };
    
    
    public static ShapeMouseEvent createMouseMove(Vec2 pos, Vec2 delta)
    {
        return new ShapeMouseEvent(MOVE, 0, pos, delta);
    };
    
    public static ShapeMouseEvent createMouseDrag(int button, Vec2 pos, Vec2 delta)
    {
        return new ShapeMouseEvent(DRAG, button, pos, delta);
    };
    
    public static ShapeMouseEvent createMousePress(int button, Vec2 pos)
    {
        return new ShapeMouseEvent(PRESS, button, pos, null);
    };
    
    public static ShapeMouseEvent createMouseRelease(int button, Vec2 pos)
    {
        return new ShapeMouseEvent(RELEASE, button, pos, null);
    };
    
    public static ShapeMouseEvent createMouseClick(int button, Vec2 pos)
    {
        return new ShapeMouseEvent(CLICK, button, pos, null);
    };
    
    public void setIntersectedChild(int index)
    {
        intersectChild=index;
    };
    
    public int getIntersectedChild()
    {
        return intersectChild;
    };
    
    public void toClientRectangle(Vec2 clientPlacement)
    {
        mousePos=mousePos.minus(clientPlacement);
    };
    
    public int getSubtype(){
        return subType;
    }
    
    public int getButton(){
        return mouseButton;
    }
    
    public Vec2 getPosition(){
        return mousePos;
    }
    
    public Vec2 getDelta(){
        return mouseDelta;
    }
}
