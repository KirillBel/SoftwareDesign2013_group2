package graphview;

import graphview.shapes.EdgeAspect;
import graphview.shapes.EdgeAspect.eEdgeAspectType;
import graphview.shapes.LineShape;
import graphview.shapes.NodeAspect;

/**
 * Ксласс для представления ребра графа
 * Представляет собой уникальный ID ребра, ID вершины из который идёт ребро, ID вершины в которую идёт ребро и направление ребра 
 * @author FlyPig
 */
public class GraphEdge {
    
    private int edgeID=-1;
    private int fromID=-1;
    private int toID=-1;
    private boolean bDirectional=false;
    private EdgeAspect edgeAspect=null;
    public Object userData=null;  
    
    /**
     * Конструктор для создания нового ребра графа
     * @param ID - Уникалиный ID ребра
     * @param from - ID вершины из которой выходит ребро
     * @param to - ID вершины в которую попадает ребро
     * @param direction - Направление ребра
     */
    public GraphEdge(int ID, int from, int to,eEdgeAspectType shapeType)
    {
        this.edgeID=ID;
        this.fromID=from;
        this.toID=to;
        edgeAspect=GraphScene.createEdgeShape(shapeType);
    }   
    
    public GraphEdge(int ID, int from, int to,EdgeAspect shape)
    {
        this.edgeID=ID;
        this.fromID=from;
        this.toID=to;
        edgeAspect=shape;
    }
    
    /**
     * Функция для получения ID данного ребра
     * @return Возвращает ID данного ребра
     */
    public int getID()
    {
        int ID=this.edgeID;
        return ID;
    }
    
    /**
     * Функция для получения направления данного ребра
     * @return Возвращает направление данного ребра
     */
    public boolean isDirectional()
    {
        return bDirectional;
    }
    
    /**
     * Функция для получения ID вершины из которой выходит ребро
     * @return Возвращает ID вершины из которой выходит ребро
     */
    public int getFromID()
    {
        int ID=this.fromID;
        return ID;
    }
    
    /**
     * Функция для получения ID вершины в которую попадает ребро
     * @return Возвращает ID вершины в которую попадает ребро
     */
    public int getToID()
    {
        int ID=this.toID;
        return ID;
    }
    
     /**
     * Функция для сравнения двух элементов класса GraphEdge на равенство
     * @param edge - Ребро, которое требуется сравнить с данным
     * @return Возвращает true, если ребра равны, false - ребра не равны
     */
    public boolean equals(GraphEdge edge)
    {
        if(this.edgeID==edge.getID() &&
                this.fromID==edge.getFromID() &&
                this.toID==edge.toID &&
                this.bDirectional==edge.isDirectional() &&
                this.edgeAspect==edge.getAspect()
                )
        {
            return  true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Функция для задания нового ID вершины из которой выходит ребро
     * @param newFromID - Новый ID вершины из которой выходит ребро
     */
    public boolean setFrom(int newFromID, GraphScene scene)
    {        
        if(this==scene.getEdge(this.edgeID))
        {
            if(scene.getSizeNodeArray()>newFromID )
            {
                if(scene.getNode(newFromID)!=null)
                {
                    this.fromID=newFromID;
                    return true;
                }
            }              
        }
        return false;
    }
    
    /**
     * Функция для задания нового ID вершины в которую попадает ребро
     * @param newToID - Новый ID вершины в которую попадает ребро
     */
    public boolean setTo(int newToID, GraphScene scene)
    {
        if(this==scene.getEdge(this.edgeID))
        {
            if(scene.getSizeNodeArray()>newToID )
            {
                if(scene.getNode(newToID)!=null)
                {
                    this.toID=newToID;
                    return true;
                }
            }              
        }
        return false;
    }
    
    /**
     * Функция для задания нового направления ребра
     * @param newDirection - Новое направление ребра
     */
     public void setDirection(boolean newDirection)
     {
         this.bDirectional=newDirection;
     }
     
     /**
     * Функция для получения фигуры ребра
     * @return  shape - Фигура ребра
     */
     public EdgeAspect getAspect()
     {
         return this.edgeAspect;
     }
     
     public void syncronize(GraphScene scene)
     {
         NodeAspect n1=scene.getNode(fromID).getAspect();
         NodeAspect n2=scene.getNode(toID).getAspect();
         edgeAspect.setPortA(n1);
         edgeAspect.setPortB(n2);
     }
}
