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
        else if(c.equals(c.blue)) return Color.orange;
        else if(c.equals(c.orange)) return Color.red;
        else if(c.equals(c.red)) return Color.cyan;
        else if(c.equals(c.cyan)) return Color.magenta;
        else if(c.equals(c.magenta)) return Color.yellow;
        else if(c.equals(c.yellow)) return Color.PINK;
        
        return new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
    };
    
    public static ArrayList<ArrayList<GraphNode>> findClusters(GraphScene scene,ArrayList<GraphNode> from)
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
        
        if(from!=null)
        {
            for(int i=0;i<clusters.size();i++)
            {
                boolean bFound=false;
                for(int j=0;j<from.size();j++)
                {
                    if(clusters.get(i).contains(from.get(j)))
                    {
                        bFound=true;
                    }
                };
                
                if(!bFound)
                {
                    clusters.remove(i);
                    i--;
                };
            }
        };
        
        return clusters;
    };
    
    public static ArrayList<ArrayList<GraphNode>> findCycles(GraphScene scene,ArrayList<GraphNode> from)
    {
        ArrayList<ArrayList<GraphNode>> clusters=findClusters(scene,from);
        ArrayList<ArrayList<GraphNode>> cycles=new ArrayList<ArrayList<GraphNode>>();
        for(int i=0;i<clusters.size();i++)
        {
            if(from==null)
            {
                for(int j=0;j<clusters.get(i).size();j++)
                {
                    ArrayList<ArrayList<GraphNode>> cyclesTmp=findCycles(scene, clusters.get(i), clusters.get(i).get(j));
                    cycles.addAll(cyclesTmp);
                };
            }
            else
            {
                for(int j=0;j<from.size();j++)
                {
                    ArrayList<ArrayList<GraphNode>> cyclesTmp=findCycles(scene, clusters.get(i), from.get(j));
                    cycles.addAll(cyclesTmp);
                };
            };
        };
        
        removeEqualCycles(cycles);
        
        return cycles;
    }
    
    public static ArrayList<ArrayList<GraphNode>> reqFindCycles(GraphScene scene, ArrayList<GraphNode> cluster, GraphNode from, GraphNode current, ArrayList<GraphNode> passed)
    {
        ArrayList<ArrayList<GraphNode>> cycles=new ArrayList<ArrayList<GraphNode>>();
        if(passed==null) passed=new ArrayList<GraphNode>();
        GraphNode prev=null;
        if(passed.size()!=0) prev=passed.get(passed.size()-1);
        passed.add(current);
        
        for(int i=0;i<current.getSizeOfNodeEdgesIDArray();i++)
        {
            GraphNode node=current.getLinkedItem(scene, i);
            if((node==from) && (node!=prev))
            {
                cycles.add(passed);
            };
            if(passed.contains(node)) continue;
            
            ArrayList<GraphNode> newPassed=new ArrayList<GraphNode>(passed);
            ArrayList<ArrayList<GraphNode>> found=reqFindCycles(scene,cluster,from,node,newPassed);
            
            for(int j=0;j<found.size();j++)
            {
                cycles.add(found.get(j));
            };
        };
        return cycles;
    };
    
    public static ArrayList<ArrayList<GraphNode>> findCycles(GraphScene scene, ArrayList<GraphNode> cluster,GraphNode from)
    {    
        ArrayList<ArrayList<GraphNode>> cycles=reqFindCycles(scene, cluster, from, from, null);
        removeEqualCycles(cycles);
        return cycles;
    };
    
    public static void removeEqualCycles(ArrayList<ArrayList<GraphNode>> cycles)
    {
        for(int i=0;i<cycles.size();i++)
        {
            for(int j=i+1;j<cycles.size();j++)
            {
                //if(i==j) continue;
                if(compareByData(cycles.get(i),cycles.get(j)))
                {
                    cycles.remove(j);
                    j--;
                };
            };
        };
    };
    
    public static void removeEqualNodes(ArrayList<GraphNode> nodes)
    {
        for(int i=0;i<nodes.size();i++)
        {
            for(int j=i+1;j<nodes.size();j++)
            {
                if(nodes.get(j)==nodes.get(i)) nodes.remove(j);
            };
        };
    };
    
    public static boolean compareByData(ArrayList<GraphNode> c1,ArrayList<GraphNode> c2)
    {
        for(int i=0;i<c1.size();i++)
        {
            if(!c2.contains(c1.get(i))) return false;
        };
        return true;
    };
    
    public static ArrayList<ArrayList<Integer>> getClusterEdges(GraphScene scene, ArrayList<GraphNode> cluster)
    {
        ArrayList<ArrayList<Integer>> edges=new ArrayList<ArrayList<Integer>>();
        for(int i=0;i<cluster.size();i++) edges.add(new ArrayList<Integer>());
        
        for(int i=0;i<cluster.size();i++)
        {
            for(int j=0;j<cluster.get(i).getSizeOfNodeEdgesIDArray();j++)
            {
                GraphNode node=cluster.get(i).getLinkedItem(scene, j);
                for(int z=i+1;z<cluster.size();z++)
                {
                    if(cluster.get(z)==node)
                    {
                        edges.get(z).add(i);
                        edges.get(i).add(z);
                    };
                };
            };
        };
        return edges;
    };
    
    public static ArrayList<GraphNode> getNeighbours(GraphScene scene,ArrayList<GraphNode> cluster)
    {
        ArrayList<GraphNode> neit=new ArrayList<GraphNode>();
        for(int i=0;i<cluster.size();i++)
        {
            for(int j=0;j<cluster.get(i).getSizeOfNodeEdgesIDArray();j++)
            {
                if(!cluster.contains(cluster.get(i).getLinkedItem(scene, j)))
                {
                    neit.add(cluster.get(i).getLinkedItem(scene, j));
                };
            };
        };
        removeEqualNodes(neit);
        return neit;
    };
}
