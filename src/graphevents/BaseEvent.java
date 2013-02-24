/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphevents;

/**
 *
 * @author Kirill
 */
public class BaseEvent {
    private int eventType=0;
    public static final int EVENT_TYPE_MOUSE=1;
    public static final int EVENT_TYPE_KEYBOARD=2;
    
    public BaseEvent(int type)
    {
        eventType=type;
    };
    
    public int getType() {
        return eventType;
    }
    
    
}


