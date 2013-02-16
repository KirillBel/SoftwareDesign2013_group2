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
        nodeEdgesIDArray=new ArrayList();
        nodeEdgesIDArray.clear();
    }
    
    public void addEdge(int edgeID)
    {
        if(nodeEdgesIDArray.indexOf(edgeID)!=-1)
        {
            System.err.printf("Ребро с ID: %d уже взаимодействует с вершиной с ID: %d", edgeID, this.nodeID);
        }
        else
        {
            if(nodeEdgesIDArray.size()<edgeID)
            {
                for(int i=nodeEdgesIDArray.size();nodeEdgesIDArray.size()<edgeID;i++)
                {
                    nodeEdgesIDArray.add(i, null);
                }
            }            
            nodeEdgesIDArray.add(edgeID,edgeID);
        }        
    }
    
    public int getID()
    {
        return this.nodeID;
    }
    
    public ArrayList getNodeEdgesIDArray()
    {
        return this.nodeEdgesIDArray;
    }
    
    public boolean equals(GraphNode node)
    {
        if(this.nodeID==node.getID() && this.nodeEdgesIDArray.equals(node.getNodeEdgesIDArray()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void deleteEdgeFromArray(int ID)
    {
        nodeEdgesIDArray.set(ID, null);
    }
    
}
