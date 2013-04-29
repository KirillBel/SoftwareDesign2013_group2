/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import graphview.GraphNode;
import graphview.GraphScene;
import graphview.GraphUtils;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class HierarchicalLayoutK extends BaseLayout{

    @Override
    public void onApplyLayout() {
        ArrayList<ArrayList<GraphNode>> clusters=GraphUtils.findClusters(scene,null);
        
        for(int i=0;i<clusters.size();i++)
        {
            System.out.println(String.format("\nCluster %d:",i));
            calculateCluster(clusters.get(i));
        }   
        
    };
    
    public void calculateCluster(ArrayList<GraphNode> cluster)
    {
        ArrayList<ArrayList<Integer>> edges=GraphUtils.getClusterEdges(scene,cluster);
        System.out.println("Edges:");
        for(int i=0;i<cluster.size();i++)
        {
            System.out.print(String.format("%s -> [", cluster.get(i).getAspect().getLabel()));
            for(int j=0;j<edges.get(i).size();j++)
            {
                System.out.print(String.format(" %s ", cluster.get(edges.get(i).get(j)).getAspect().getLabel()));
            };
            System.out.println("]");
        };
        
        ArrayList<Integer> ranks=calculateRanks(cluster,edges,0);
        
        System.out.println("\nRanks:");
        for(int i=0;i<cluster.size();i++)
        {
            System.out.println(String.format("%s:\t%d", cluster.get(i).getAspect().getLabel(),ranks.get(i)));
        };
    };
    
    
    public ArrayList<Integer> calculateRanks(ArrayList<GraphNode> cluster,ArrayList<ArrayList<Integer>> edges, int fromIndex)
    {
        ArrayList<Integer> ranks=new ArrayList<Integer>();
        for(int i=0;i<cluster.size();i++) ranks.add(-1);
        
        reqCalcRanks(cluster,edges,ranks,fromIndex,-1);
        
        return ranks;
    };
    
    public void reqCalcRanks(ArrayList<GraphNode> cluster,ArrayList<ArrayList<Integer>> edges, ArrayList<Integer> ranks, int currIndex, int prevRank)
    {
        String curr=cluster.get(currIndex).getAspect().getLabel();
        int currRank=prevRank+1;
        
        if((ranks.get(currIndex)<=currRank) && (ranks.get(currIndex)!=-1)) 
            return;
        
        ranks.set(currIndex,currRank);
        
        for(int i=0;i<edges.get(currIndex).size();i++)
        {
            if(edges.get(currIndex).get(i)==currIndex) continue;
            reqCalcRanks(cluster,edges,ranks,edges.get(currIndex).get(i),currRank);
        };
    };
}
