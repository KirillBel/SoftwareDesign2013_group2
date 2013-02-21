package graph;

import graphview.LineShape;

/**
 * Ксласс для представления ребра графа
 * Представляет собой уникальный ID ребра, ID вершины из который идёт ребро, ID вершины в которую идёт ребро и направление ребра 
 * @author FlyPig
 */
public class GraphEdge {
    
    public static enum Direction {IN, OUT, BIDIR};
    private int edgeID;
    private int fromID;
    private int toID;
    private Direction direct;    
    private LineShape edgeShape=null;
      
    /**
     * Конструктор для создания нового ребра графа без связи с вершинами
     * @param ID - Уникалиный ID ребра
     */
    public GraphEdge(int ID)
    {
        this.edgeID=ID;
        this.fromID=-1;
        this.toID=-1;
        this.direct=null;
        edgeShape=null;
    }    
    
    /**
     * Конструктор для создания нового ребра графа
     * @param ID - Уникалиный ID ребра
     * @param from - ID вершины из которой выходит ребро
     * @param to - ID вершины в которую попадает ребро
     * @param dir - Направление ребра
     */
    public GraphEdge(int ID, int from, int to, Direction dir)
    {
        this.edgeID=ID;
        this.fromID=from;
        this.toID=to;
        this.direct=dir;
        edgeShape=null;
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
    public Direction getDirection()
    {
        Direction direct=this.direct;
        return direct;
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
        if(this.edgeID==edge.getID() && this.fromID==edge.getFromID() && this.toID==edge.toID && this.direct==edge.getDirection() && this.edgeShape==edge.getShape())
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
    public void setFrom(int newFromID)
    {
        this.fromID=newFromID;
    }
    
    /**
     * Функция для задания нового ID вершины в которую попадает ребро
     * @param newToID - Новый ID вершины в которую попадает ребро
     */
    public void setTo(int newToID)
    {
        this.toID=newToID;
    }
    
    /**
     * Функция для задания нового направления ребра
     * @param newDirection - Новое направление ребра
     */
     public void setDirection(Direction newDirection)
     {
         this.direct=newDirection;
     }
     
     /**
     * Функция для задания фигуры ребра
     * @param shape - Фигура ребра
     */
     public void setShape(LineShape shape)
     {
         this.edgeShape=shape;
     }
     
     /**
     * Функция для получения фигуры ребра
     * @return  shape - Фигура ребра
     */
     public LineShape getShape()
     {
         LineShape shape=this.edgeShape;
         return shape;
     }

}
