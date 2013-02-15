/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author FlyPig
 */
public class GraphData {
    
    private int countElement;
    private int lastID;
    private ArrayList<GraphNode> nodesArray;
    private ArrayList<GraphEdge> edgesArray;
    
    public GraphData()
    {
        this.lastID=-1;
        this.countElement=0;
        this.edgesArray.clear();
        this.nodesArray.clear();
    }
    
    public void createNode()
    {
        lastID++;
        countElement++;
        GraphNode node=new GraphNode(lastID);
        nodesArray.add(lastID, node);        
    }
    
    public void createEdge(int from, int to, Direction dir)
    {
        lastID++;
        countElement++;
        GraphEdge edge=new GraphEdge(lastID, from, to, dir);
        edgesArray.add(lastID, edge); 
        nodesArray.get(from).addEdge(lastID);
        nodesArray.get(to).addEdge(lastID);
        
    }
}
