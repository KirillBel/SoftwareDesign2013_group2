/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import graphview.GraphEdge;
import graphview.GraphNode;
import graphview.GraphScene;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public abstract class BaseLayout {
    GraphScene scene=null;
    
    public abstract void applyLayout(GraphScene scene_);
    
    protected ArrayList<Integer> recurseTestNodeCyclic(ArrayList<Integer> list, GraphNode base, GraphNode prev, GraphNode next, int pathLen)
    {
        int id=-1;
        
        pathLen++;
        next.userData=new Integer(pathLen);
        if(base==next && pathLen!=0) return list;
        boolean bFirst=true;
        
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i)==next.getID()) return null;
        };
        
        list.add(next.getID());
        for(int i=0;i<next.getSizeOfNodeEdgesIDArray();i++)
        {
            GraphEdge edge=scene.getEdge(next.getElementOfNodeEdgesIDArray(i));
            if(next.getID()==edge.getFromID()) id=edge.getToID();
            else id=edge.getFromID();
            
            if(scene.getNode(id)==prev) continue;
            
            ArrayList<Integer> nodeArr=new ArrayList<Integer>(list);
            
            ArrayList<Integer> ret=recurseTestNodeCyclic(nodeArr,base,next, scene.getNode(id),pathLen);
            if(ret!=null) 
            {
                return ret;
            }
        };
        return null;
    };
    
    protected boolean calculateCyclicMetric(int nodeID)
    {
        GraphNode Node = scene.getNode(nodeID);
        int id=-1;
        
        
        ArrayList<Integer> nodeArr=new ArrayList<Integer>();
        nodeArr=recurseTestNodeCyclic(nodeArr,Node,Node,Node,-1);
        boolean ret=(nodeArr!=null);
        
        System.out.println(String.format("\n\n\nCycled: %s", Boolean.valueOf(ret)));
        if (ret == true)
        {
            System.out.println("\nCycle nodes:");
            for(int i=0;i<nodeArr.size();i++)
            {
                System.out.println(String.format("%s",scene.getNode(nodeArr.get(i)).getAspect().getLabel()));
            }
        }
        
        return ret;
    };
}
