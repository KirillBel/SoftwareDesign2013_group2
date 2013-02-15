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
    private int countEdge;
    private ArrayList<Integer> nodeEdgesIDArray;
   
    public GraphNode(int ID)
    {
        this.nodeID=ID;
        this.countEdge=-1;
        nodeEdgesIDArray.clear();
    }
    
    public void addEdge(int edgeID)
    {
        countEdge++;
        nodeEdgesIDArray.add(edgeID);
        
    }
    
}
