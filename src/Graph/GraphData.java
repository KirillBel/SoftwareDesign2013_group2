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
public class GraphData {
    
    private int countNodes;
    private int countEdges;
    private ArrayList<GraphNode> nodesArray;
    private ArrayList<GraphEdge> edgesArray;
    
    public GraphData()
    {
        this.countNodes=0;
        this.countEdges=0;
        this.edgesArray.clear();
        this.nodesArray.clear();
    }
    
    public void createNode()
    {
        countNodes++;
        GraphNode node=new GraphNode(nodesArray.size());
        nodesArray.add(node.getID(), node);        
    }
    
    public void createEdge(int from, int to, GraphEdge.Direction dir)
    {
        countEdges++;
        GraphEdge edge=new GraphEdge(nodesArray.size(), from, to, dir);
        edgesArray.add(edge.getID(), edge); 
        nodesArray.get(from).addEdge(edge.getID());
        nodesArray.get(to).addEdge(edge.getID());
        
    }
}
