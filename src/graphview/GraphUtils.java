/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class GraphUtils {
    
    public static Color nextColor(Color c)
    {
        if(c==null) return Color.green;
        else if(c.equals(c.green)) return Color.blue;
        else if(c.equals(c.blue)) return Color.red;
        else if(c.equals(c.red)) return Color.orange;
        else if(c.equals(c.orange)) return Color.yellow;
        else if(c.equals(c.yellow)) return Color.magenta;
        else if(c.equals(c.magenta)) return Color.cyan;
        else if(c.equals(c.cyan)) return Color.PINK;
        
        return new Color((float)Math.random()*255.f,(float)Math.random()*255.f,(float)Math.random()*255.f);
    };
    
    public static ArrayList<ArrayList<GraphNode>> findClusters(GraphScene scene)
    {
        ArrayList<ArrayList<GraphNode>> clusters=new ArrayList<ArrayList<GraphNode>>();
        
        ArrayList<GraphNode> cluster = new ArrayList<GraphNode>();
        ArrayList<Integer> clusterHelper=new ArrayList<Integer>();
        ArrayList<GraphNode> nodes=new ArrayList<GraphNode>();
        
        for(int i=0;i<scene.getSizeNodeArray();i++)
        {
            if(scene.getNode(i)!=null)
            {
                nodes.add(scene.getNode(i));
            };
        };
        
        if(nodes.size()==0) return clusters;
        
        while(nodes.size()!=0)
        {
            cluster.add(nodes.get(0));
            nodes.remove(0);
            clusterHelper.add(0);
            
            int numAdded=1;
            while(numAdded!=0)
            {
                numAdded=0;

                for(int i=0;i<cluster.size();i++)
                {
                    if(clusterHelper.get(i)!=0) continue;
                    for(int j=0;j<cluster.get(i).getSizeOfNodeEdgesIDArray();j++)
                    {
                        GraphNode node=cluster.get(i).getLinkedItem(scene,j);
                        if(!cluster.contains(node)) 
                        {
                            cluster.add(node);
                            nodes.remove(node);
                            clusterHelper.add(0);
                            numAdded++;
                        };
                    }
                    clusterHelper.set(i, 1);
                };
            };
            
            clusters.add(cluster);
            cluster=new ArrayList<GraphNode>();
            clusterHelper.clear();
        };
        
        return clusters;
    };
}
