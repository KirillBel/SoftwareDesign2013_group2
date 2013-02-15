/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;

/**
 *
 * @author FlyPig
 */
public class GraphNode {
    
    private int nodeID;
    private ArrayList<Integer> nodeEdgesIDArray;
   
    public GraphNode(int ID)
    {
        this.nodeID=ID;
        nodeEdgesIDArray.clear();
    }
    
    public void addEdge(int edgeID)
    {
        nodeEdgesIDArray.add(edgeID);        
    }
    
    public int getID()
    {
        return this.nodeID;
    }
    
}
