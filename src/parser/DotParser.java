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
                        System.out.println("DIGRAPH");
                        tk.nextToken();
                        if(tk.ttype==tk.TT_WORD) {
                            tk.nextToken();
                            cluster(tk);
                        }
                        break;
                    case "graph":
                        System.out.println("GRAPH");
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
        
        GraphNode node=graphMain.createCustomNode(graphMain.createTextBox(new Vec2(0,0),name,Color.yellow));
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
            System.out.println("Вершина: "+list.get(node1.getID()).nameNode+" (ID = "+list.get(node1.getID()).IDNode+")");
            tk.nextToken();
            
            Direction dir;
            if(tk.ttype=='-')
            {
                tk.nextToken();
                if(tk.ttype=='>'){
                    dir=Direction.IN;
                    tk.nextToken();
                }
                else if(tk.ttype=='-'){
                    dir=Direction.BIDIR;
                    tk.nextToken();
                }
                else return;
            }
            else if(tk.ttype=='<')
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

                System.out.println("ID ребра = "+edge.getID()+" Из вершины "+node1.getID()+" в вершину "+node2.getID());
                tk.nextToken();
                if(tk.ttype=='['){
                    tk.pushBack();
                    System.out.println("Call optionlist!");
                    tk.nextToken();
                    //optionlist(tk);
                }
            }
        }
    }
  
}