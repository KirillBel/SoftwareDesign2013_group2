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
        edgesArray=new ArrayList();
        nodesArray=new ArrayList();
        this.edgesArray.clear();
        this.nodesArray.clear();
    }
    
    public void createNode()
    {       
        countNodes++;
        GraphNode node=new GraphNode(nodesArray.size());
        if(nodesArray.size()<node.getID())
        {
            for(int i=nodesArray.size();nodesArray.size()<node.getID();i++)
            {
                nodesArray.add(i, null);
            }
        }
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
                GraphEdge edge=new GraphEdge(edgesArray.size(), from, to, dir);
                if(edgesArray.size()<edge.getID())
                {
                    for(int i=edgesArray.size();edgesArray.size()<edge.getID();i++)
                    {
                        edgesArray.add(i, null);
                    }
                }
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
                if(edgesID.get(i)!=null)
                {
                    countEdges--;
                    if(edgesArray.get(i).getFromID()!=ID)
                    {
                        nodesArray.get(edgesArray.get(i).getFromID()).deleteEdgeFromArray(i);
                    }

                    if(edgesArray.get(i).getToID()!=ID)
                    {
                        nodesArray.get(edgesArray.get(i).getToID()).deleteEdgeFromArray(i);
                    }  
                    edgesArray.set(edgesID.get(i), null);
                }
            }
            
            countNodes--;
            nodesArray.set(ID, null);
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
    
    public ArrayList getNodesArray()
    {
        return this.nodesArray;
    }
    
    public ArrayList getEdgesArray()
    {
        return this.edgesArray;
    }
    
    public int getCountNodes()
    {
        return this.countNodes;
    }
    
    public int getCountEdges()
    {
        return this.countEdges;
    }
    
}
