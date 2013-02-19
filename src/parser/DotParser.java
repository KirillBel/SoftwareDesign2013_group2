package parser;

import graph.GraphData;
import graph.GraphEdge;
import graph.GraphEdge.Direction;
import graph.GraphNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * Класс осуществляющий синтаксический анализ файла формата .dot
 * @author BoytsevAndrey
 */
public class DotParser {
    
    private Reader m_input;
    private GraphData graphData = new GraphData();
    private ArrayList<NodeID> list = new ArrayList<NodeID>();
    
    /**
     * Конструктор для создания нового парсера
     * @param input - Входной файловый поток
     */
    public DotParser(Reader input)
    {
        m_input = input;
    }
    
    /**
     * Функция для запуска парсера
     * @return Возвращает объект GraphData
     */
    public GraphData parse() 
    {
        StreamTokenizer tk = new StreamTokenizer(new BufferedReader(m_input));
        setSyntax(tk);
    
        graph(tk);
    
        return graphData;
    }
    
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
    
    protected void graph(StreamTokenizer tk) 
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
                }
            }
            else System.err.println("Ошибка. Ожидается 'graph' или 'digraph' в строке "+tk.lineno());
        }
        catch(Exception ex) { ex.printStackTrace(); }
    }
    
    protected void cluster(StreamTokenizer tk) throws IOException 
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
                }
                else {System.err.println("Ошибка. Отсутствует '}' в конце файла"); break;}
            }
        }
        else {
            System.err.println("Error at line "+tk.lineno()+" ignoring token "+tk.sval);
            return;
        }
        System.out.println("End of graph!");    
    }
    
    protected GraphNode getNode(String name)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).nameNode.equalsIgnoreCase(name)) {
                System.out.println("Повтор вершины "+list.get(i).nameNode);
                return graphData.getElementOfNodesArray(list.get(i).IDNode);
            }
        }
        
        GraphNode node=graphData.createNode();
        NodeID nodeID=new NodeID(name, node.getID());
        list.add(nodeID);
        return node;
    }
    
    protected void edge(StreamTokenizer tk) throws IOException
    {
        if(tk.ttype==tk.TT_WORD) {
            GraphNode node1 = getNode(tk.sval);
            System.out.println("Вершина: "+list.get(node1.getID()).nameNode+" (ID = "+list.get(node1.getID()).IDNode+")");
            tk.nextToken();
            if(tk.ttype=='-'){
                tk.nextToken();
                if(tk.ttype=='>'){
                    tk.nextToken();
                    if(tk.ttype==tk.TT_WORD){
                        GraphNode node2 = getNode(tk.sval);
                        System.out.println("Вершина: "+list.get(node2.getID()).nameNode+" (ID = "+list.get(node2.getID()).IDNode+")");
                        GraphEdge edge = graphData.createEdge(node1.getID(), node2.getID(), Direction.IN);
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
    }
  
}