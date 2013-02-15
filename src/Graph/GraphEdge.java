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
    
    //private static enum Direction {FOWARD, BACKWARD, BILATERAL};
    private int edgeID;
    private int fromID;
    private int toID;
    private Direction direct;    
                
    public GraphEdge(int ID, int from, int to, Direction dir)
    {
        this.toID=ID;
        this.fromID=from;
        this.toID=to;
        this.direct=dir;
    }   
    
}
