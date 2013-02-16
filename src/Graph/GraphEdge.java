/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

/**
 *
 * @author FlyPig
 */
public class GraphEdge {
    
    public static enum Direction {IN, OUT, BIDIR};
    private int edgeID;
    private int fromID;
    private int toID;
    private Direction direct;    
                
    public GraphEdge(int ID, int from, int to, Direction dir)
    {
        this.edgeID=ID;
        this.fromID=from;
        this.toID=to;
        this.direct=dir;
    }   
    
    public int getID()
    {
        return this.edgeID;
    }
    
    public Direction getDirection()
    {
        return this.direct;
    }
    
    public int getFromID()
    {
        return this.fromID;
    }
    
    public int getToID()
    {
        return this.toID;
    }
    
    public boolean equals(GraphEdge edge)
    {
        if(this.edgeID==edge.getID() && this.fromID==edge.getFromID() && this.toID==edge.toID && this.direct==edge.getDirection())
        {
            return  true;
        }
        else
        {
            return false;
        }
    }
}
