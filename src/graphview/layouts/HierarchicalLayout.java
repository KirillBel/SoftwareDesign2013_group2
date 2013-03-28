/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import graphview.GraphNode;
import graphview.GraphScene;
import java.util.ArrayList;

/**
 *
 * @author Kirill
 */
public class HierarchicalLayout extends BaseLayout{
    //int nodeID;
    int nodeCount;

    @Override
    public void applyLayout(GraphScene scene_) {
        scene=scene_;
        int id=-1;
        boolean r=calculateCyclicMetric(0);
        //My code
        nodeCount = scene.getSizeNodeArray();
        //GraphNode Node = scene.getNode(nodeID);
        
        //В maxArr будет записан максимальный путь
        ArrayList<Integer> maxArr = new ArrayList<Integer>();
        ArrayList<Integer> nodeArr=new ArrayList<Integer>();
        //maxArr = null;
        //nodeArr=recurseTestNodeCyclic(nodeArr,Node,Node,Node,-1);
        
        
        System.out.println("Test of the node finder\n");
        
        //Если найден хотя бы один цикл - начинаем перебирать все вершины на предмет обнаружения циклов
        for(int i = 0; i<nodeCount; i++)
        {
            r=calculateCyclicMetric(i);
            if(r == false) continue;
            GraphNode Node = scene.getNode(i);
            nodeArr.clear();
            nodeArr=recurseTestNodeCyclic(nodeArr,Node,Node,Node,-1);
            for(int j=0;j<nodeArr.size();j++)
            {
                        System.out.println(String.format("nodeArr %s",scene.getNode(nodeArr.get(j)).getAspect().getLabel()));
            };
            //сравниваем количество вершин в полученной цепи. Если больше того, что в maxArr - перезаписываем его
            //если нет - не перезаписываем
            if(nodeArr.size() > maxArr.size())
            {
                maxArr = new ArrayList<>(nodeArr);
                for(int k=0;k<maxArr.size();k++)
                {
                        System.out.println(String.format("maxArr %s",scene.getNode(maxArr.get(k)).getAspect().getLabel()));
                };
                //ArrayList<String> list1 = new ArrayList<String>(list0);
            }
        }
       
        //End (My code)
        
    }
    
}
