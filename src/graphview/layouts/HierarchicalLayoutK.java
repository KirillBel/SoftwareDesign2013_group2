/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import geometry.Rect;
import geometry.Vec2;
import graphview.GraphNode;
import graphview.GraphScene;
import graphview.GraphUtils;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class HierarchicalLayoutK extends BaseLayout{

    public static final int ELEM_OFFSET = 10;
    public static final int COLOMN_OFFSET = 10;
    
    @Override
    public void onApplyLayout() {
        ArrayList<ArrayList<GraphNode>> clusters=GraphUtils.findClusters(scene,null);
        
        Rect offset=new Rect();
        for(int i=0;i<clusters.size();i++)
        {
            System.out.println(String.format("\nCluster %d:",i));
            calculateCluster(clusters.get(i));
            
            Rect currRect=new Rect(clusters.get(i).get(0).getAspect().getGlobalRectangle());
            for(int j=0;j<clusters.get(i).size();j++)
            {
                currRect.add(clusters.get(i).get(j).getAspect().getGlobalRectangle());
            };
            currRect.increase(30);
            
            System.out.println(String.format("[%.2f, %.2f, %.2f, %.2f]\tx=%.2f, y=%.2f",currRect.left,currRect.right,currRect.top,currRect.bottom,currRect.getSize().x,currRect.getSize().y));
            Vec2 mov=new Vec2(-(currRect.left-offset.right),-(currRect.top));
            for(int j=0;j<clusters.get(i).size();j++)
            {
                clusters.get(i).get(j).getAspect().move(mov);
            };
            currRect.move(mov);
            System.out.println(String.format("mov: %.2f, %.2f",mov.x,mov.y));
            offset=currRect;
        }   
        
    };
    
    private class TreeElem
    {
        TreeElem(TreeElem parent,Vec2 size, int nRow,int nCol,ArrayList<Integer> prevColInRow, int index_)
        {
            this.size=size;
            this.parent=parent;
            currentRow=nRow;
            currentCol=nCol;
            index=index_;
            colomnInRow.addAll(prevColInRow);
            parUpdMaxColDepth(currentRow);
        };
        
        ArrayList<Integer> getCurrColArray()
        {
            ArrayList<Integer> arr=new ArrayList<Integer>();
            for(int i=0;i<colomnInRow.size();i++)
            {
                arr.add(new Integer(colomnInRow.get(i)));
            };
            arr.add(currentCol);
            return arr;
        };
        
        public void setPrevColInRowArr(ArrayList<Integer> arr)
        {
            colomnInRow.clear();
            colomnInRow.addAll(arr);
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setPrevColInRowArr(getCurrColArray());
            };
        };
        
        public void updateChilds()
        {
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).setPrevColInRowArr(getCurrColArray());
            };
        };
        
        public TreeElem addChild(int index,Vec2 sz)
        {
            TreeElem el=new TreeElem(this,sz, currentRow+1,childs.size(),getCurrColArray(),index);
            childs.add(el);
            return el;
        };
        
        public void setCol(int nCol)
        {
            currentCol=nCol;
            updateChilds();
        };
        
        public void swapChilds(int i1, int i2)
        {
            TreeElem tmp=childs.get(i1);
            childs.set(i1, childs.get(i2));
            childs.set(i2, tmp);
            
            childs.get(i1).setCol(i1);
            childs.get(i2).setCol(i2);
        };
        
        public void calculateOptimalPlacement()
        {
//            if(links.size()==0) 
//            {
//                for(int i=0;i<childs.size();i++)
//                    childs.get(i).calculate();
//                return;
//            }
            
            for(int i=0;i<childs.size();i++)
            {
                if(childs.get(i).links.size()==0)
                {
                    childs.get(i).calculateOptimalPlacement();
                    //childs.get(i).calculate();
                    continue;
                };
                
                int maxDiff=childs.get(i).getAllChildsSumLinksDist();
                //int maxDiff=getAllChildsSumLinksDist();
                int maxI=i;
                for(int j=0;j<childs.size();j++)
                {
                    if(i==j) continue;
                    swapChilds(i, j);
                    int diff=childs.get(j).getAllChildsSumLinksDist();
                    //int diff=getAllChildsSumLinksDist();
                    if(diff<maxDiff)
                    {
                        maxI=j;
                        maxDiff=diff;
                    };
                    swapChilds(i, j);
                };
                swapChilds(i, maxI);
                childs.get(i).calculateOptimalPlacement();
            };
            
//            for(int i=0;i<childs.size();i++)
//                    childs.get(i).calculateOptimalPlacement();
        };
        
        int getAllChildsSumLinksDist()
        {
            int d=0;
            for(int i=0;i<childs.size();i++)
            {
                d+=childs.get(i).getSumLinksDist();
            };
            return d;
        };
        
        int getSumLinksDist()
        {
            int d=0;
            for(int i=0;i<links.size();i++)
            {
                d+=getColDistance(links.get(i));
            };
            return d;
        };
        
        int getColDistance(TreeElem el)
        {
            int d=0;
            int colStart=(currentCol>el.getCurrColArray().get(currentRow)) ? 
                    currentCol-1 : currentCol+1;
            int colEnd=(el.getCurrColArray().get(currentRow)>currentCol) ? 
                    el.getCurrColArray().get(currentRow)-1 : el.getCurrColArray().get(currentRow)+1;
            
            int minInd=Math.min(colStart, colEnd);
            int maxInd=Math.max(colStart, colEnd);
            
            for(int i=minInd;i<=maxInd;i++)
            {
                if(parent.childs.get(i).maxRow>maxRow) d++;
                //if(parent.childs.get(i).colomnInRow.size()>colomnInRow.size()) d++;
            };
            
            int dist=Math.abs(currentCol-el.getCurrColArray().get(currentRow));

            
            return dist;
        };
        
        public TreeElem findIndex(int ind)
        {
            if(ind==index) return this;
            for(int i=0;i<childs.size();i++)
            {
                TreeElem ret=childs.get(i).findIndex(ind);
                if(ret!=null) return ret;
            };
            return null;
        };
        
        public boolean isExists(int ind)
        {
            return findIndex(ind) != null;
        };
        
        public void calcRowSizes(float[] rowSizes)
        {
            if(rowSizes[currentRow]<size.y)
            {
                rowSizes[currentRow]=size.y;
            };
            for(int i=0;i<childs.size();i++)
                childs.get(i).calcRowSizes(rowSizes);
        };
        
        public void placeNodes(float[] rowSizes, ArrayList<GraphNode> cluster)
        {
            if(childs.size()==0) return;
            Rect currRect=cluster.get(index).getAspect().getGlobalRectangle();
            
            float topStart=currRect.top+rowSizes[currentRow];
            float leftStart=currRect.getCenter().x+colSize.x;
            Vec2 childPos=new Vec2();
            
            for(int i=0;i<childs.size();i++)
            {
                childPos.set(leftStart+Math.abs(childs.get(i).colSize.x),topStart);
                childPos.x-=childs.get(i).size.x/2;
                childPos.x+=ELEM_OFFSET/2;
                cluster.get(childs.get(i).index).getAspect().setPosition(childPos);
                leftStart+=childs.get(i).colSize.y-childs.get(i).colSize.x;
            };
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).placeNodes(rowSizes,cluster);
            };
        };
        
        public void calcColSize()
        {
            float w=0;
            float c=0;
            colSize.set(0,0);
            
            if(currentRow==0)
            {
                int t=0;
                t++;
            };
            
            for(int i=0;i<childs.size();i++)
            {
                childs.get(i).calcColSize();
                
                float sze=(childs.get(i).colSize.y-childs.get(i).colSize.x);
                if(i==childs.size()/2)
                {
                    if(childs.size()%2==0)
                    {
                        c=w;
                    }
                    else
                    {
                        c=w+sze/2;
                    };
                }
                w+=sze;
            };
            
            colSize.x=-(c);
            colSize.y=(w-c);
            if(Math.abs(colSize.x)<size.x/2)
                colSize.x=-size.x/2;
            if(colSize.y<size.x/2)
                colSize.y=size.x/2;
            colSize.x-=ELEM_OFFSET/2;
            colSize.y+=ELEM_OFFSET/2;
            System.out.println(String.format("Col [%d,%d]: l = %.2f, r = %.2f  [%.2f]",currentRow,currentCol,colSize.x,colSize.y,colSize.y-colSize.x));
        };
        
        void parUpdMaxColDepth(int childRow)
        {
            if(maxRow<childRow) maxRow=childRow;
            if(parent!=null) 
                    parent.parUpdMaxColDepth(maxRow);
        };
        
        int maxRow=0;
        
        public int currentRow=-1;
        public int currentCol=-1;
        public ArrayList<Integer> colomnInRow=new ArrayList<Integer>();
        public ArrayList<TreeElem> links=new ArrayList<TreeElem>();
        public ArrayList<TreeElem> childs=new ArrayList<TreeElem>();
        public int index=-1;
        Vec2 size=new Vec2();
        Vec2 colSize=new Vec2();
        TreeElem parent=null;
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
        
        int mostEdgedIndex=0;
        int maxEdges=0;
        for(int i=0;i<edges.size();i++)
        {
            if(maxEdges<edges.get(i).size())
            {
                maxEdges=edges.get(i).size();
                mostEdgedIndex=i;
            };
        } 
        
        ArrayList<Integer> ranks=calculateRanks(cluster,edges,mostEdgedIndex);
        
        System.out.println("\nRanks:");
        for(int i=0;i<cluster.size();i++)
        {
            System.out.println(String.format("%s:\t%d", cluster.get(i).getAspect().getLabel(),ranks.get(i)));
        };
        
        int rowCount=0;
        int rootIndex=-1;
        for(int i=0;i<ranks.size();i++)
        {
            rowCount=Math.max(rowCount, ranks.get(i));
            if(ranks.get(i)==0) rootIndex=i;
        };
        rowCount++;
        
        TreeElem root=new TreeElem(null,cluster.get(rootIndex).getAspect().getSize(),0,0,new ArrayList<Integer>(),rootIndex);
        reqCalcTree(root,root,cluster,edges,ranks);
        root.calculateOptimalPlacement();
        
        
        float[] rowSizes=new float[rowCount];
        for(int i=0;i<rowSizes.length;i++)
            rowSizes[i]=0;
        root.calcRowSizes(rowSizes);
        for(int i=0;i<rowSizes.length;i++)
            rowSizes[i]*=3;
        
        root.calcColSize();
        root.placeNodes(rowSizes, cluster);
    };
    
    public void reqCalcTree(TreeElem root, TreeElem curr, ArrayList<GraphNode> cluster,ArrayList<ArrayList<Integer>> edges, ArrayList<Integer> ranks)
    {
        int currIndex=curr.index;
        
        for(int i=0;i<edges.get(currIndex).size();i++)
        {
            int nextIndex=edges.get(currIndex).get(i);
            
            TreeElem ell=root.findIndex(nextIndex);
            if((ranks.get(nextIndex)>=curr.currentRow) && (ell!=null))
            {
                curr.links.add(ell);
            }
            else if(ranks.get(nextIndex)==curr.currentRow+1)
            {
                TreeElem el=curr.addChild(nextIndex,cluster.get(nextIndex).getAspect().getSize());
                reqCalcTree(root,el,cluster,edges,ranks);
            };
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
