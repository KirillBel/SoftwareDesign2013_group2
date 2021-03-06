package graphview;

import graphview.shapes.NodeAspect;
import graphview.shapes.NodeAspect.eNodeAspectType;
import java.util.ArrayList;

/**
 * Ксласс для представления вершины графа
 * Представляет собой уникальный ID вершины и список ID ребер взаимодействующих с этой вершиной 
 * @author FlyPig
 */
public class GraphNode {
    
    private int nodeID;
    NodeAspect nodeAspect=null;
    private ArrayList<Integer> nodeEdgesIDArray=new ArrayList<Integer>();
    public Object userData=null;
   
    /**
     * Конструктор для создания новой вершины графа
     * @param ID - Уникалиный ID вершины
     */
    public GraphNode(int ID, eNodeAspectType shapeType, eNodeAspectType containmentType)
    {
        this.nodeID=ID;
        nodeAspect=GraphScene.createNodeShape(shapeType, containmentType);
    }
    
    public GraphNode(int ID, NodeAspect shape)
    {
        this.nodeID=ID;
        nodeAspect=shape;
    }
    
    /**
     * Функция добавления ID связанного с вершиной ребра в список nodeEdgesIDArray
     * @param ID - ID ребра для добавления
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @return Возвращает ID ребра, если успешно создано, -1 - добавление неудачно
     */
    public int addEdge(int edgeID)
    {
        try
        {                      
            nodeEdgesIDArray.add(edgeID);
            return edgeID;
        }    
        catch(Exception ex)
        {
            System.err.printf("Ошибка при добавлении ребра с ID: %d в список ребер вершины %d \n",edgeID, this.nodeID);
            return -1;
        }
    }
    
    /**
     * Функция для получения ID данной вершины
     * @return Возвращает ID данной вершины
     */
    public int getID()
    {
        int ID=this.nodeID;
        return ID;
    }
    
    /**
     * Функция для получения одного из ID ребей, связанных с данной вершиной
     * @param indexID - индекс элемента в списке nodeEdgesIDArray
     * @return Возвращает ID ребра, связанного с данной вершиной и находящегося по нужному индексу
     */
    public int getElementOfNodeEdgesIDArray(int indexID)
    {
        int edgeID=nodeEdgesIDArray.get(indexID);
        return edgeID;
    }
    
    public GraphNode getLinkedItem(GraphScene scene, int index) 
    {
        int edgeId=getElementOfNodeEdgesIDArray(index);
        
        int nodeID = scene.getEdge(edgeId).getFromID() == getID() ? 
                scene.getEdge(edgeId).getToID() : scene.getEdge(edgeId).getFromID();
        
        return scene.getNode(nodeID);
    };
    
    /**
     * Функция для сравнения двух элементов класса GraphNode на равенство
     * @param node - Вершина, которую требуется сравнить с данной
     * @return Возвращает true, если вершины равны, false - вершины не равны
     */
    public boolean equals(GraphNode node)
    {
        if(this.nodeID==node.getID() && this.nodeAspect==node.getAspect())
        {
            for(int i=0; i<this.nodeEdgesIDArray.size();i++)
            {
                if(this.nodeEdgesIDArray.get(i)!=node.getElementOfNodeEdgesIDArray(i))
                {
                    return false;
                }  
            }
            if(this.nodeAspect!=node.getAspect())
            {
                return false;
            }
            return true;
        }
        else
        {
            return false;
        }   
    }
    
    /**
     * Функция удаления ID связанного с вершиной ребра из списока nodeEdgesIDArray
     * @param ID - ID ребра для удаления
     * @exception IndexBoundException возникает при попытке изменить элемент с несуществующим индексом
     * @exception NullPointerException возникает при попытке изменить что-либо в неинициализированном ArrayList
     * @return Возвращает ID ребра, если успешно создано, -1 - добавление неудачно
     */
    public boolean deleteEdgeFromArray(int ID)
    {
        int index=nodeEdgesIDArray.indexOf(ID);
        if(index==-1)
        {
            return true;
        }
        else
        {
            try
            {      

                nodeEdgesIDArray.remove(index);
                return true;
            }    
            catch(Exception ex)
            {
                System.err.printf("Ошибка при удалении ребра с ID: %d из списка ребер вершины %d \n",ID, this.nodeID);
                return false;
            }   
        }
    }
    
    /**
     * Функция для получения размера списка ребер, связанных с данной вершиной
     * @return Возвращает размера списка ребер, связанных с данной вершиной
     */
    public int getSizeOfNodeEdgesIDArray()
    {
        return this.nodeEdgesIDArray.size();
    }
     
     /**
     * Функция для получения фигуры ребра
     * @return  shape - Фигура ребра
     */
     public NodeAspect getAspect()
     {
         return this.nodeAspect;
     }
}
