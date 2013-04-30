/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import geometry.Vec2;
import graphview.GraphEdge;
import graphview.GraphNode;
import graphview.GraphScene;
import graphview.GraphUtils;
import graphview.layouts.BaseLayout;
import java.util.ArrayList;

/**
 *
 * @author FlyPig
 */
public class TreeLayoutSimple extends BaseLayout{

    private ArrayList<ArrayList<GraphNode>> trees=new ArrayList<>();
    private ArrayList<ArrayList<GraphNode>> cycles=new ArrayList<>();
    private ArrayList<GraphNode> upNode=new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<GraphNode>>> layers=new ArrayList<>();
    private ArrayList<GraphNode> temp=new ArrayList<>();
    private ArrayList<ArrayList<GraphNode>> temp2=new ArrayList<>();
    private ArrayList<GraphNode> allNodes=new ArrayList<>();
    private ArrayList<ArrayList<Float>> yPosition=new ArrayList<>();
    
    private ArrayList<ArrayList<Object>> xMax=new ArrayList<ArrayList<Object>>();
    private ArrayList<Object> tempMax =new ArrayList<Object>();
    
    private ArrayList<GraphNode> complete=new ArrayList<>();
    
    float DISTANCE = 50;
    
    float graphDistance=-1;    
    
    float yDistance=-1;
    
    float xNow=-1;
    
    float xSize=-DISTANCE;
    
    float maxX=-50;
    
    int treeID=-1;
    int layerID=-1;
    int elementID=-1;
    
    float leftPosition=-1;
    
    @Override
    public void applyLayout(GraphScene scene_) 
    {        
        int size=-1;
        int number=-1;
        scene=scene_;
        trees=GraphUtils.findClusters(scene,null);
        //cycles=GraphUtils.findCycles(scene);
        
        for(int i=0;i<trees.size();i++)
        {
            for(int j=0;j<trees.get(i).size();j++)
            {
                if(size==-1)
                {
                    size=trees.get(i).get(j).getSizeOfNodeEdgesIDArray();
                    number=j;
                }
                if(size<trees.get(i).get(j).getSizeOfNodeEdgesIDArray())
                {
                    size=trees.get(i).get(j).getSizeOfNodeEdgesIDArray();
                    number=j;
                } 
                allNodes.add(trees.get(i).get(j));
            }
            upNode.add(trees.get(i).get(number));
            allNodes.remove(trees.get(i).get(number));
            size=-1;
            number=-1;
        }
        
        doLayer();
        doYposition();
        Vec2 tempPlacement = new Vec2();
        Vec2 tempPlacement2 = new Vec2();
        tempPlacement.set(0, 0);
        GraphNode node;
        GraphNode node2;
        float tempo=-1;
        node=null;
      
        
        //для всех деревьев(графов)
        //i - номер графа
        for(int i=0;i<upNode.size();i++)
        {
            //самый правый элемент в последнем уровне помещается в координаты (0;0)
            if(layers.size()>0)
            {
                if(layers.get(0).size()>0)
                {
                    node=layers
                            .get(layers.size()-1)//уровень
                            .get(0)//дерево
                            .get(layers.get(layers.size()-1).get(0).size()-1);//последний элемент
                    node.getAspect().setPosition(new Vec2(0, 0));
                    leftPosition=node.getAspect().getPosition().x;
                    complete.add(node);
                }
            }

            //пока верхний элемент не станет готовым
            while(!complete.contains(upNode.get(i)))
            {
                int position=-1;
                if(node!=null)
                {
                    if(!complete.contains(node))
                    {
                        for(int j=node.getSizeOfNodeEdgesIDArray()-1;j>-1;j--)
                        {
                            node2=node.getLinkedItem(scene, j);
                            position=elementPosition(node, node2);
                            if(complete.contains(node2) || position==1 || position==0)
                            {
                                if(j==0)
                                {
                                    Vec2 ccc=newNodePosition(node);
                                    node.getAspect().setPosition(ccc);
                                    complete.add(node);
                                    node=getUpElement(node);
                                    break;
                                }
                                continue;
                            }
                            else
                            {
                                node=node2;
                                break;
                            }
                        }
                    }
                    else
                    {
                        node=getUpElement(node);
                    }
                    
                }
                else 
                {
                    break;
                }                
            }
        }
        
    }
    
    public GraphNode getUpElement(GraphNode node)
    {
        int lay;
        ArrayList<GraphNode> tempResult=new ArrayList<>();
        GraphNode res=null;
        GraphNode temp=null;
        getParam(node);
        lay=layerID;
        if(layerID!=0)
        {
            for(int i=0;i<node.getSizeOfNodeEdgesIDArray();i++)
            {
                temp=node.getLinkedItem(scene, i);
                getParam(temp);
                if(lay>layerID)
                {
                    tempResult.add(temp);
                }
            }
            getParam(node);
            for(int i=layerID-1;i>-1;i--)
            {
                for(int j=layers.get(i).get(treeID).size()-1;j>-1;j--)
                {
                    temp=layers.get(i).get(treeID).get(j);
                    if(tempResult.contains(temp) && !complete.contains(temp))
                    {
                        return temp;
                    }
                }
            }                              
        }
        return res;
    }
    
        public GraphNode getRightElement(GraphNode node)
    {
        int lay;
        ArrayList<GraphNode> tempResult=new ArrayList<>();
        GraphNode res=null;
        GraphNode temp=null;
        getParam(node);
        lay=layerID;
        
        for(int i=0;i<node.getSizeOfNodeEdgesIDArray();i++)
        {
            temp=node.getLinkedItem(scene, i);
            getParam(temp);
            if(lay<layerID)
            {
                tempResult.add(temp);
            }
        }
        getParam(node);
        if(layerID+1<layers.size())
        {
            for(int i=layerID+1;i<layers.size();i++)
            {
                for(int j=layers.get(i).get(treeID).size();j>-1;j--)
                {
                    temp=layers.get(i).get(treeID).get(i);
                    if(tempResult.contains(temp))
                    {
                        return temp;
                    }
                }
            }
        }
                                      
        
        return res;
    }
    
    
    
    public void getParam(GraphNode node)
    {
        for(int i=0;i<layers.size();i++)
        {
            for(int j=0;j<layers.get(i).size();j++)
            {
                for(int k=0;k<layers.get(i).get(j).size();k++)
                {
                    if(layers.get(i).get(j).get(k).equals(node))
                    {
                        treeID=j;
                        elementID=k;
                        layerID=i;
                        k=layers.get(i).get(j).size()-1;
                        j=layers.get(i).size()-1;
                        i=layers.size()-1;                        
                    }
                }
            }
        }
    }
    
    public int elementPosition(GraphNode main, GraphNode node)
    {
        int lay;
        getParam(main);
        lay=layerID;
        getParam(node);
        if(lay>layerID)
        {
            return 1;
        }
        else if(lay<layerID)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    
    public Vec2 newNodePosition(GraphNode node)
    {
        GraphNode temp;
        float x;
        float y;
        if(rootElement(node))
        {
            getParam(node);
            x=leftPosition-DISTANCE-node.getAspect().getSize().x;
            y=yPosition.get(treeID).get(layerID);
            leftPosition=x;
            return new Vec2(x,y);
        }
        else
        {
            float width=-50;
            float lx=-1;
            for(int i=0;i<node.getSizeOfNodeEdgesIDArray();i++)
            {
                temp=node.getLinkedItem(scene, i);
                if(elementPosition(node, temp)==-1)
                {
                    if(lx==-1)
                    {
                        lx=temp.getAspect().getPosition().x;
                    }
                    if(lx>temp.getAspect().getPosition().x)
                    {
                        lx=temp.getAspect().getPosition().x;
                    }
                    float qqqq=temp.getAspect().getSize().x;
                    width=width+temp.getAspect().getSize().x+DISTANCE;
                }
            }
            float tmp=maxLeft(node);
            getParam(node);
            
            x=lx+width/2-node.getAspect().getSize().x/2;
            if(tmp!=-1)
            {
                if(x>tmp-DISTANCE-node.getAspect().getSize().x)
                {
                    x=tmp-DISTANCE-node.getAspect().getSize().x;
                }
            }
            
            y=yPosition.get(treeID).get(layerID);
            if(x<leftPosition)
            {
                leftPosition=x;
            }
            return new Vec2(x,y);
        }
        
    }
    
    
    public float maxLeft(GraphNode node)
    {
        getParam(node);
        GraphNode tmp;
        float res=-1;
        for (int i = 0; i < layers.get(layerID).get(treeID).size(); i++) 
        {
            tmp=layers.get(layerID).get(treeID).get(i);
            if(complete.contains(tmp))
            {
                if(res==-1)
                {
                    res=layers.get(layerID).get(treeID).get(i).getAspect().getPosition().x;
                }
                if(res>layers.get(layerID).get(treeID).get(i).getAspect().getPosition().x)
                {
                    res=layers.get(layerID).get(treeID).get(i).getAspect().getPosition().x;
                }
            }        
            
        }
        return res;
    }
    
   
        public boolean rootElement(GraphNode node)
        {

            GraphNode temp;
            for(int i=0;i<node.getSizeOfNodeEdgesIDArray();i++)
            {
                temp=node.getLinkedItem(scene, i);
                if(elementPosition(node, temp)==-1)
                {
                    return false;
                }
            }
            
            return true;
        }
    
    public void doLayer()
    {
        GraphNode node;
        GraphNode node2;        
        GraphEdge edge;
        int i=0;
        for(int k=0;k<upNode.size();k++)
        {
            temp.add(upNode.get(k));
            temp2.add(temp);
            maxX=0;
            maxX=upNode.get(k).getAspect().getSize().x;
            tempMax.add(maxX);
            temp=new ArrayList<>();
        }
        xMax.add(tempMax);
        tempMax=new ArrayList<>();
        layers.add(temp2);
        temp2=new ArrayList<>();
        maxX=-DISTANCE;
        
        while(allNodes.size()!=0)
        {
            for(int j=0;j<layers.get(i).size();j++)
            {
                for(int l=0;l<layers.get(i).get(j).size();l++)
                {
                    node=layers.get(i).get(j).get(l);
                    for(int z=0;z<node.getSizeOfNodeEdgesIDArray();z++)
                    {
                        node2=node.getLinkedItem(scene, z);
                        if(allNodes.contains(node2))
                        {
                            temp.add(node2);
                            maxX=maxX+node2.getAspect().getSize().x+DISTANCE;
                            allNodes.remove(node2);
                        }                         
                    }
                }    
                
                temp2.add(temp);
                tempMax.add(maxX);
                maxX=-DISTANCE;
                temp=new ArrayList<>();                
            }
            layers.add(temp2);
            temp2=new ArrayList<>();
            xMax.add(tempMax);
            tempMax=new ArrayList<>();
            i++;
        }
    }
    
    public float getMaxX(int num)
    {
        float max=0;
        for(int h=0;h<xMax.size();h++)
        {
            if((float)(xMax.get(h).get(num))>max)
            {
                max=(float)(xMax.get(h).get(num));
            }
        }
        return max;
        
    }

    @Override
    public void onApplyLayout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void doYposition() {
        float temp=-1;
        float tempSize=-1;
        yPosition.clear();    
        ArrayList<Float> temppos=new ArrayList<>();
        for(int q=0;q<layers.size();q++)
        {
            temppos.add(0.0f);
        }
        if(layers.size()>0 && layers.get(0).size()>0)
        {
            
            for(int k=0;k<layers.get(0).size();k++)
            {
                for(int i=layers.size()-1;i>-1;i--)
                {
                    if(i==layers.size()-1)
                    {
                        temppos.set(i,0.0f);
                    }
                    else
                    {
                        
                        for (int j = 0; j < layers.get(i).get(k).size(); j++) 
                        {
                            if(temp==-1)
                            {
                                temp=layers.get(i).get(k).get(j).getAspect().getSize().y;
                            }
                            if(temp<layers.get(i).get(k).get(j).getAspect().getSize().y)
                            {
                                temp=layers.get(i).get(k).get(j).getAspect().getSize().y;
                            }
                          
                            
                        }
                        temppos.set(i,-temp+temppos.get(i+1)-DISTANCE);
                        temp=-1;
                    }                                                           
                }
            yPosition.add(temppos);
            temppos=new ArrayList<>();
            for(int q=0;q<layers.size();q++)
            {
                temppos.add(0.0f);
            }
            }
        }
        
    }
    
}
