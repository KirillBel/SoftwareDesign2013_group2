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
        edgesArray=null;
        nodesArray=null;
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
        if(nodesArray.size()>from && nodesArray.size()>to)
        {
            if(nodesArray.get(from)==null)
            {
                System.err.printf("Вершина с ID: %d не существует", from);
            }
            else if(nodesArray.get(to)==null)
            {
                System.err.printf("Вершина с ID: %d не существует", to);
            }
            else
            {
                countEdges++;
                GraphEdge edge=new GraphEdge(nodesArray.size(), from, to, dir);
                edgesArray.add(edge.getID(), edge); 
                nodesArray.get(from).addEdge(edge.getID());
                nodesArray.get(to).addEdge(edge.getID());
            }
        }
        else
        {
            System.err.println("Одной или обеих вершин не существует");
        }        
    }
    
    public void deleteNode(int ID)
    {
        if(nodesArray.size()<=ID)
        {
            System.err.printf("Вершины с ID: %d не существует", ID);
        }
        else if(nodesArray.get(ID)==null)
        {
            System.out.printf("Вершина с ID: %d уже удалена", ID);
        }
        else
        {
            ArrayList<Integer> edgesID=nodesArray.get(ID).getNodeEdgesIDArray();
            for(int i=0; i<edgesID.size(); i++)
            {
                countEdges--;
                edgesArray.set(edgesID.get(i), null);
            }
            
            countNodes--;
            edgesArray.set(ID, null);
        }
    }
    
    public void deleteEdge(int ID)
    {
        if(edgesArray.size()<=ID)
        {
            System.err.printf("Ребра с ID: %d не существует", ID);
        }
        else if(edgesArray.get(ID)==null)
        {
            System.out.printf("Ребро с ID: %d уже удалено", ID);
        }
        else
        {
            countEdges--;
            edgesArray.set(ID, null);
        }
    }
    
}
