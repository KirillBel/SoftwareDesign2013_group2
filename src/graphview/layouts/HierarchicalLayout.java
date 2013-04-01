/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import geometry.Vec2;
import graphview.GraphNode;
import graphview.GraphScene;
import java.util.ArrayList;
import graphview.GraphNode;

/**
 *
 * @author Kirill
 */
public class HierarchicalLayout extends BaseLayout{
    //int nodeID;
    
    class PlaceNode
    {
        //Данные нода
        public GraphNode NodeData;
        //Указывает на то, что нод уже был установлен моим установщиком. Фактически,
        //это защита нода от перетаскивания
        public boolean isPlaced = false;
        //Тестовая переменная показывает справа или слева находится нод
        public boolean isRightHanded = true;
        //public Vec2 place;
    };

    @Override
    public void applyLayout(GraphScene scene_) 
    {
        scene=scene_;
        PlaceNode placeNode = new PlaceNode();
        //GraphNode placeNode = null;
        int nodeCount;
        int maxArrsize;
        int id=-1;
        boolean r=calculateCyclicMetric(0);
        
        Vec2 tempPlacement = new Vec2();
        Vec2 MaxDemencions = new Vec2();
        Vec2 DemencionsOfNode = new Vec2();
        nodeCount = scene.getSizeNodeArray();
  
        //В maxArr будет записан максимальный путь
        ArrayList<Integer> maxArr = new ArrayList<Integer>();
        ArrayList<Integer> nodeArr=new ArrayList<Integer>();
        
        
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
            }
        }
        System.out.println("\n Final maxArray: \n");
        for(int k=0;k<maxArr.size();k++)
        {
                System.out.println(String.format("maxArr %s",scene.getNode(maxArr.get(k)).getAspect().getLabel()));
        };
        
        //Начало процесса расстановки
        
        //Рассчёт максимальных размеров нода
        int NodeCount = scene.getCountNodes();
        for (int i = 0; i<NodeCount; i++)
        {
            placeNode.NodeData = scene.getNode(i);
            DemencionsOfNode = placeNode.NodeData.getAspect().getGlobalRectangle().getSize();
            MaxDemencions.x = Math.max(MaxDemencions.x, DemencionsOfNode.x);
            MaxDemencions.y = Math.max(MaxDemencions.y, DemencionsOfNode.y);
        }
        maxArrsize = maxArr.size();
        //Расстановка самого длинного пути по вертикали
        for(int i =0; i<maxArrsize; i++)
        {
            placeNode.NodeData = scene.getNode(maxArr.get(i));
            tempPlacement.y = MaxDemencions.y*2*(i);
            placeNode.NodeData.getAspect().setPosition(tempPlacement);
            placeNode.isPlaced = true;
        }
        //scene.updateUI();
        
        
    }
    
}
