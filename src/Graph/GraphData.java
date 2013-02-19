package Graph;

import Graph.GraphEdge.Direction;
import java.util.ArrayList;

/**
 * Класс для представления графа
 * Представляет собой списки вершин и ребер графа, а также их количество
 * @author FlyPig
 */
public class GraphData {
    
    private int countNodes;
    private int countEdges;
    private ArrayList<GraphNode> nodesArray;
    private ArrayList<GraphEdge> edgesArray;
    
    /**
     * Конструктор для создания нового графа
     */
    public GraphData()
    {
        this.countNodes=0;
        this.countEdges=0;
        edgesArray=new ArrayList();
        nodesArray=new ArrayList();
        this.edgesArray.clear();
        this.nodesArray.clear();
    }
    
    /**
     * Функция для создания новой вершины графа
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @return Возвращает созданную вершину, если успешно, null - при ошибке
     */
    public GraphNode createNode()
    {  
        try
        {  
            GraphNode node=new GraphNode(nodesArray.size());
            nodesArray.add(node.getID(), node); 
            countNodes++;
            return node;
        }    
        catch(Exception ex)
        {
            System.err.println("Ошибка при создании новой вершины графа");
            return null;
        }
    }
    
    /**
     * Функция для создания нового ребра графа
     * @param from - ID вершины из которой выходит ребро
     * @param to - ID вершины в которую попадает ребро
     * @param dir - Направление ребра
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @return Возвращает созданное ребро, если успешно, null - при ошибке
     */
    public GraphEdge createEdge(int from, int to, GraphEdge.Direction dir)
    {
        int result=0;
        try
        {         
            if(nodesArray.size()>from && nodesArray.size()>to)
            {
                if(nodesArray.get(from)==null)
                {
                    System.err.printf("Вершина с ID: %d не существует \n", from);
                    return null;
                }
                else if(nodesArray.get(to)==null)
                {
                    System.err.printf("Вершина с ID: %d не существует \n", to);
                    return null;
                }
                else
                {                    
                    GraphEdge edge=new GraphEdge(edgesArray.size(), from, to, dir);
                    result=nodesArray.get(from).addEdge(edge.getID());
                    if(result==-1)
                    {
                        System.err.printf("При добавлении ребра с ID: %d в список ребер вершины с ID: %d возникла ошибка \n", edge.getID(), from);
                        return null;
                    }
                    
                    result=nodesArray.get(to).addEdge(edge.getID());
                    if(result==-1)
                    {
                        System.err.printf("При добавлении ребра с ID: %d в список ребер вершины с ID: %d возникла ошибка \n", edge.getID(), to);
                        return null;
                    }
                    
                    edgesArray.add(edge.getID(), edge);                    
                    countEdges++;
                    return edge;
                }
            }
            else
            {
                System.err.println("Одной или обеих вершин не существует");
                return null;
            }    
        }
        catch(Exception ex)
        {
            System.err.println("Ошибка при создании нового ребра графа");
            return null;
        }
    }
    
    /**
     * Функция для удаления вершины графа и ребер, связанных с ней
     * @param ID - ID вершины для удаления
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @return Возвращает true, если вершина и ребра связанные с ней успешно удалены , false - при ошибке
     */
    public boolean deleteNode(int ID)
    {
        try
        { 
            if(nodesArray.size()<=ID)
            {
                System.err.printf("Вершины с ID: %d не существует \n", ID);
                return false;
            }
            else if(nodesArray.get(ID)==null)
            {
                System.out.printf("Вершина с ID: %d уже удалена \n", ID);
                return true;
            }
            else
            {
                int size=nodesArray.get(ID).getSizeOfNodeEdgesIDArray();
                int edgeID=0;
                boolean delResult=false;
                for(int i=0; i<size; i++)
                {
                    edgeID=nodesArray.get(ID).getElementOfNodeEdgesIDArray(i);
                    if(edgeID!=-1)
                    {  
                        this.deleteEdge(edgeID);
                    }
                } 
                nodesArray.set(ID, null);
                countNodes--;
                return true;
            }            
        }
        catch(Exception ex)
        {
            System.err.println("Ошибка при удалении вершины графа");
            return false;
        }
    }
    
    /**
     * Функция для удаления ребра графа
     * @param ID - ID ребра для удаления
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @return Возвращает true, если ребро успешно удалено , false - при ошибке
     */
    public boolean deleteEdge(int ID)
    {
        try
        {
            if(edgesArray.size()<=ID)
            {
                System.err.printf("Ребра с ID: %d не существует \n", ID);
                return false;
            }
            else if(edgesArray.get(ID)==null)
            {
                System.out.printf("Ребро с ID: %d уже удалено \n", ID);
                return true;
            }
            else
            {
                boolean delResult;
                delResult=nodesArray.get(edgesArray.get(ID).getFromID()).deleteEdgeFromArray(ID);
                if(delResult!=true)
                {
                    System.err.println("Ошибка при удалении ребра");
                    return false;
                }
                delResult=nodesArray.get(edgesArray.get(ID).getToID()).deleteEdgeFromArray(ID);
                if(delResult!=true)
                {
                    System.err.println("Ошибка при удалении ребра");
                    return false;
                }           
                edgesArray.set(ID, null);
                countEdges--;
                return true;            
            }
        }
        catch(Exception ex)
        {
            System.err.println("Ошибка при удалении ребра графа");
            return false;
        }
        
    }
    
    /**
     * Функция для получения вершины графа с нужным индексом
     * @param index - index вершины в nodesArray
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправиьным индексом индексом
     * @return Возвращает нужную вершину, если успешно, null - при ошибке
     */
    public GraphNode getElementOfNodesArray(int index)
    {
        try
        {
            GraphNode node=this.nodesArray.get(index);
            return node;
        }
        catch(Exception ex)
        {
            System.err.println("Ошибка при получении вершины");
            return null;
        }        
    }
    
    /**
     * Функция для получения ребра графа с нужным индексом
     * @param index - index вершины в edgesArray
     * @exception NullPointerException возникает при попытке добавить что-либо в неинициализированный ArrayList
     * @exception IndexBoundException возникает при попытке добавить элемент с неправильным индексом
     * @return Возвращает нужное ребро, если успешно, null - при ошибке
     */
    public GraphEdge getElementOfEdgesArray(int index)
    {       
        try
        {
            GraphEdge edge=this.edgesArray.get(index);
            return edge;
        }
        catch(Exception ex)
        {
            System.err.println("Ошибка при получении ребра");
            return null;
        } 
    }
   
    /**
     * Функция для получения количества вершин графа
     * @return Возвращает количество вершин графа
     */
    public int getCountNodes()
    {
        int counter=this.countNodes;
        return counter;
    }
    
    /**
     * Функция для получения количества ребер графа
     * @return Возвращает количество ребер графа
     */
    public int getCountEdges()
    {
        int counter=this.countEdges;
        return counter;
    }

    
}
