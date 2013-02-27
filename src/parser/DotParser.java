package parser;

import geometry.Vec2;
import graph.GraphData;
import graph.GraphEdge;
import graph.GraphEdge.Direction;
import graph.GraphNode;
import graphview.GraphMain;
import graphview.LineShape;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * Класс осуществляющий синтаксический анализ файла формата .dot
 * @author BoytsevAndrey
 */
public class DotParser 
{
    
    private Reader m_input;
    private GraphMain graphMain = null;
    private ArrayList<NodeID> list = new ArrayList<NodeID>();
    private ArrayList<GraphEdge> listEdge = new ArrayList<GraphEdge>();
    private ArrayList<GraphNode> listNode = new ArrayList<GraphNode>();
    private boolean direction = true;
    
    /**
     * Конструктор для создания нового парсера
     * @param input - Входной файловый поток
     */
    public DotParser(Reader input,GraphMain graphMain_)
    {
        m_input = input;
        graphMain=graphMain_;
    }
    
    /**
     * Метод для запуска парсера
     * @return Возвращает объект GraphData
     */
    public boolean parse() 
    {
        StreamTokenizer tk = new StreamTokenizer(new BufferedReader(m_input));
        setSyntax(tk);
    
        return graph(tk);
    }
    
    /**
     * Метод для устанавки синтаксиса StreamTokenizer
     * @param tk - поток StreamTokenizer
     */
    protected void setSyntax(StreamTokenizer tk) 
    {
        tk.resetSyntax();
        tk.eolIsSignificant(false);
        tk.slashStarComments(true);
        tk.slashSlashComments(true);
        tk.whitespaceChars(0,' ');
        tk.wordChars(' '+1,'\u00ff');
        tk.ordinaryChar('[');
        tk.ordinaryChar(']');
        tk.ordinaryChar('{');
        tk.ordinaryChar('}');
        tk.ordinaryChar('-');
        tk.ordinaryChar('>');
        tk.ordinaryChar('/');
        tk.ordinaryChar('*');
        tk.ordinaryChar(',');
        tk.quoteChar('"');
        tk.whitespaceChars(';',';');
        tk.ordinaryChar('=');
    }
    
    /**
     * Метод, определяющий направленность графа
     * @param tk - поток StreamTokenizer
     */
    protected boolean graph(StreamTokenizer tk) 
    {
        try {
            tk.nextToken();
      
            if(tk.ttype==tk.TT_WORD) {
                switch(tk.sval){
                    case "digraph":
                        direction = true;
                        System.out.println("DIGRAPH");
                        tk.nextToken();
                        if(tk.ttype==tk.TT_WORD) {
                            tk.nextToken();
                            cluster(tk);
                        }
                        else System.err.println("Ошибка. Некорректно имя ориентированного графа\n");
                        break;
                    case "graph":
                        direction = false;
                        System.out.println("GRAPH");
                        tk.nextToken();
                        if(tk.ttype==tk.TT_WORD) {
                            tk.nextToken();
                            cluster(tk);
                        }
                        else System.err.println("Ошибка. Некорректное имя графа\n");
                        break;
                    default:
                        System.err.println("Ошибка. Ожидается 'graph' или 'digraph' в строке "+tk.lineno());
                        return false;
                }
            }
            else {System.err.println("Ошибка. Ожидается 'graph' или 'digraph' в строке "+tk.lineno()); return false;};
        }
        catch(Exception ex) { ex.printStackTrace(); return false;}
        return true;
    }
    
    /**
     * Метод, определяющий наличие подграфов
     * @param tk - поток StreamTokenizer
     */
    protected boolean cluster(StreamTokenizer tk) throws IOException 
    {
        if(tk.ttype=='{') {
            tk.nextToken();
            while(tk.ttype!='}'){
                if(tk.ttype!=tk.TT_EOF){
                    if(tk.ttype==tk.TT_WORD) {
                        if(tk.sval.equalsIgnoreCase("subgraph")){
                            //subgraph(tk);
                        }
                        else{
                            listEdge.removeAll(listEdge);
                            System.out.println("Call edge!");
                            edge(tk);
                        }
                    }
                    else tk.nextToken();
                }
                else {System.err.println("Ошибка. Отсутствует '}' в конце файла"); return false;}
            }
        }
        else {
            System.err.println("Error at line "+tk.lineno()+" ignoring token "+tk.sval);
            return false;
        }
        System.out.println("End of graph!"); 
        return true;
    }
    
    /**
     * Метод для проверки списка на повторение вершины
     * @param name - имя вершины
     * @return Возвращает объект GraphNode 
     */
    protected GraphNode getNode(String name)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).nameNode.equalsIgnoreCase(name)) {
                System.out.println("Повтор вершины "+list.get(i).nameNode);
                return graphMain.getGraphData().getElementOfNodesArray(list.get(i).IDNode);
            }
        }
        
        GraphNode node=graphMain.createCustomNode(graphMain.createTextEllipse(new Vec2(0,0),name,Color.yellow));
        NodeID nodeID=new NodeID(name, node.getID());
        list.add(nodeID);
        return node;
    }
    
    /**
     * Метод для определения вершин и ребер
     * @param tk - поток StreamTokenizer
     */
    protected void edge(StreamTokenizer tk) throws IOException
    {
        if(tk.ttype==tk.TT_WORD) {
            GraphNode node1 = getNode(tk.sval);
            listNode.add(node1);
            System.out.println("Вершина: "+list.get(node1.getID()).nameNode+" (ID = "+list.get(node1.getID()).IDNode+")");
            tk.nextToken();
            
            if(tk.ttype=='['){
              optionlist(tk);
              listEdge.removeAll(listEdge);
              listNode.removeAll(listNode);
              tk.nextToken();
            }
            
            if(tk.ttype==','){
                tk.nextToken();
            }
            
            Direction dir;
            if(tk.ttype=='-')
            {
                tk.nextToken();
                if(tk.ttype=='>' && direction==true){
                    dir=Direction.IN;
                    tk.nextToken();
                }
                else if(tk.ttype=='-' && direction==false){
                    dir=Direction.BIDIR;
                    tk.nextToken();
                }
                else {
                    System.err.println("Ошибка. Некорректно задано ребро\n");
                    return;
                }
            }
            else if(tk.ttype=='<' && direction==true)
            {
                tk.nextToken();
                if(tk.ttype=='-'){
                    dir=Direction.OUT;
                    tk.nextToken();
                }
                else return;
            }
            else return;
                
            if(tk.ttype==tk.TT_WORD){
                GraphNode node2 = getNode(tk.sval);
                System.out.println("Вершина: "+list.get(node2.getID()).nameNode+" (ID = "+list.get(node2.getID()).IDNode+")");
                
                GraphEdge edge = graphMain.createCustomEdge(node1.getID(), node2.getID(), dir, new LineShape(null,null));
                listEdge.add(edge);
                System.out.println("ID ребра = "+edge.getID()+" Из вершины "+node1.getID()+" в вершину "+node2.getID());
                edge(tk);
            }
        }
    }
    
    /**
     * Метод для разбора опций вершин и ребер
     * @param tk - поток StreamTokenizer
     */
    protected void optionlist(StreamTokenizer tk) throws IOException
  {
      if(tk.ttype=='[') {
          tk.nextToken();
          while(tk.ttype!=']'){
              property(tk);
              tk.nextToken();
              if(tk.ttype==','){
                  property(tk);
              }
              
          }
          
      }
      System.out.println("End options!");
  }
 
  /**
     * Метод для реализации опций вершин и ребер
     * @param tk - поток StreamTokenizer
     */  
  protected void property(StreamTokenizer tk) throws IOException
  {
      LineShape b = new LineShape(null, null);
      if(tk.ttype==tk.TT_WORD){
          System.out.println("Option: "+tk.sval);
          switch(tk.sval){
                    case "color":
                        tk.nextToken();
                        if(tk.ttype=='='){
                            tk.nextToken();
                            if(tk.ttype==tk.TT_WORD){
                                System.out.println( "is "+tk.sval);
                                switch(tk.sval){
                                    case "black":
                                        if(listEdge.size()==0) setColorNode(Color.black);
                                        else setColorEdge(Color.black);
                                        break;
                                    case "blue":
                                        if(listEdge.size()==0) setColorNode(Color.blue);
                                        else setColorEdge(Color.blue);
                                        break;
                                    case "cyan":
                                        if(listEdge.size()==0) setColorNode(Color.cyan);
                                        else setColorEdge(Color.cyan);
                                        break;
                                    case "darkGray":
                                        if(listEdge.size()==0) setColorNode(Color.darkGray);
                                        else setColorEdge(Color.darkGray);
                                        break;
                                    case "gray":
                                        if(listEdge.size()==0) setColorNode(Color.gray);
                                        else setColorEdge(Color.gray);
                                        break;
                                    case "green":
                                        if(listEdge.size()==0) setColorNode(Color.green);
                                        else setColorEdge(Color.green);
                                        break;
                                    case "lightGray":
                                        if(listEdge.size()==0) setColorNode(Color.lightGray);
                                        else setColorEdge(Color.lightGray);
                                        break;
                                    case "margenta":
                                        if(listEdge.size()==0) setColorNode(Color.magenta);
                                        else setColorEdge(Color.magenta);
                                        break;
                                    case "orange":
                                        if(listEdge.size()==0) setColorNode(Color.orange);
                                        else setColorEdge(Color.orange);
                                        break;  
                                    case "pink":
                                        if(listEdge.size()==0) setColorNode(Color.pink);
                                        else setColorEdge(Color.pink);
                                        break;
                                    case "red":
                                        if(listEdge.size()==0) setColorNode(Color.red);
                                        else setColorEdge(Color.red);
                                        break;
                                    case "white":
                                        if(listEdge.size()==0) setColorNode(Color.white);
                                        else setColorEdge(Color.white);
                                        break;
                                    case "yellow":
                                        if(listEdge.size()==0) setColorNode(Color.yellow);
                                        else setColorEdge(Color.yellow);
                                        break;    
                                    default:
                                        System.err.println("Ошибка. Цвет "+tk.sval+" недопустим");
                                        return;
                                }
                            }
                            
                        }
                        break;
                    case "label":
                        tk.nextToken();
                        if(tk.ttype=='='){
                            tk.nextToken();
                        }
                        break;
                    case "shape":
                        tk.nextToken();
                        if(tk.ttype=='='){
                            tk.nextToken();
                        }
                        break;
                    case "style":
                        tk.nextToken();
                        if(tk.ttype=='='){
                            tk.nextToken();
                        }
                        break;
                    default:
                        System.err.println("Ошибка. Опции не существует");
                        return;
          }
          
                }
  }
  
  /**
     * Метод для задания цвета ребер
     * @param color - цвет ребра
     */
  protected void setColorEdge(Color color)
  {
      for(int i=0; i<listEdge.size();i++){
          System.out.println("OPTION for edge "+listEdge.get(i).getID()); 
          listEdge.get(i).getShape().color=color;
      }
      return;
  }
  
  /**
     * Метод для задания цвета вершин
     * @param color - цвет ребра
     */
  protected void setColorNode (Color color)
  {
      for(int i=0; i<listNode.size();i++){
          System.out.println("OPT for node "+listNode.get(i).getID()); 
          listNode.get(i).getShape().color = color;
      }
      return;
      
  }
  
  protected void subgraph(StreamTokenizer tk) throws IOException
  {
      tk.nextToken();
      if(tk.ttype==tk.TT_WORD){
          System.out.println("Name subgraph: "+tk.sval);
          tk.nextToken();
          cluster(tk);
      }
      else{
          System.err.println("Ожидается имя подграфа в строке "+tk.lineno());
          return;
      } 
  }
}