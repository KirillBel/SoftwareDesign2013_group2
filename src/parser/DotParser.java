package parser;

import geometry.Vec2;
import graphview.GraphEdge;
import graphview.GraphNode;
import graphview.GraphScene;
import graphview.shapes.EdgeAspect;
import graphview.shapes.EdgeAspect.eEdgeAspectType;
import graphview.shapes.LineShape;
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

class DotBase
{
    ArrayList<DotOption> options=new ArrayList<DotOption>();
    
    public void setOption(String name, String value)
    {
        DotOption op=getOption(name);
        if(op==null)
            options.add(new DotOption(name,value));
        else
            op.value=value;
    };
    
    public DotOption getOption(String name)
    {
        for(int i=0;i<options.size();i++)
        {
            if(options.get(i).name.equalsIgnoreCase(name)) return options.get(i);
        };
        return null;
    };
    
    public void copyFrom(DotBase n)
    {
        for(int i=0;i<n.options.size();i++)
        {
            this.setOption(n.options.get(i).name, n.options.get(i).value);
        };
    };
};

class DotNode extends DotBase
{
    String name=null;
    int id;
}

class DotEdge extends DotBase
{
    String from=null;
    String to=null;
}

class DotOption
{
    String name;
    String value;
    
    public DotOption(String n, String v)
    {
        name=n;
        value=v;
    };
};

public class DotParser 
{
    
    private Reader m_input;
    private GraphScene scene = null;
    
    private ArrayList<DotNode> listNodeOpt = new ArrayList<DotNode>();
    private ArrayList<DotEdge> listEdgeOpt = new ArrayList<DotEdge>();
    
    private ArrayList<DotNode> ListN = new ArrayList<DotNode>();
    private ArrayList<DotEdge> ListE = new ArrayList<DotEdge>();
    
    private DotBase optNode = new DotBase();
    private DotBase optEdge = new DotBase();
    
    private int flag_opt = 0;
    private int flag = 0;
    
    private boolean direction = true;
    
    /**
     * Конструктор для создания нового парсера
     * @param input - Входной файловый поток
     */
    public DotParser(Reader input,GraphScene scene_)
    {
        m_input = input;
        scene=scene_;
        //example();
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
        if(tk.ttype=='{') 
        {
            tk.nextToken();
            while(tk.ttype!='}')
            {
                if(tk.ttype!=tk.TT_EOF)
                {
                    if(tk.ttype==tk.TT_WORD) 
                    {
                        if(tk.sval.equalsIgnoreCase("subgraph"))
                        {
                            //subgraph(tk);
                        }
                        else
                        {
                            //listEdge.removeAll(listEdge);
                            System.out.println("Call edge!");
                            edge(tk);
                        }
                    }
                    else tk.nextToken();
                }
                else 
                {
                    System.err.println("Ошибка. Отсутствует '}' в конце файла"); 
                    return false;
                }
            } 
        }
        else 
        {
            System.err.println("Error at line "+tk.lineno()+" ignoring token "+tk.sval);
            return false;
        }
        System.out.println("End of graph!"); 
        createGraph();
        return true;
    }
    
    /**
     * Метод для проверки списка на повторение вершины
     * @param name - имя вершины
     * @return Возвращает объект GraphNode 
     */
    protected DotNode getNode(String name)
    {
        for(int i=0;i<ListN.size();i++)
        {
            if(ListN.get(i).name.equalsIgnoreCase(name)) 
            {
                return ListN.get(i);
            }
        }
        
        DotNode node = new DotNode();
        node.name = name;
        ListN.add(node);
        
        System.out.println("Список вершин: ");
        for(int j=0;j<ListN.size();j++)
        {
            System.out.println(ListN.get(j).name);
        }
        return node;
    }
    
    protected DotEdge getEdge(String from, String to)
    {
        for(int i=0;i<ListE.size();i++)
        {
            if(ListE.get(i).from.equalsIgnoreCase(from) && ListE.get(i).to.equalsIgnoreCase(to)) 
            {
                return ListE.get(i);
            }
        }
        
        DotEdge edge = new DotEdge();
        edge.from = from;
        edge.to = to;
        ListE.add(edge);
        
        System.out.println("Список ребер: ");
        for(int j=0;j<ListE.size();j++)
        {
            System.out.println(ListE.get(j).from+" to "+ListE.get(j).to);
        }
        return edge;
    }
    /**
     * Метод для определения вершин и ребер
     * @param tk - поток StreamTokenizer
     */
    protected void edge(StreamTokenizer tk) throws IOException
    {
        if(tk.ttype==tk.TT_WORD) 
        {
            if(tk.sval.equalsIgnoreCase("node"))
            {
                flag_opt = 1;
                optNode.options.clear();
                listNodeOpt.clear();
                tk.nextToken();
                if(tk.ttype=='[')
                {
                    optionlist(tk);
                    tk.nextToken();
                }
                return;
            }
            
            else if(tk.sval.equalsIgnoreCase("edge"))
            {
                flag_opt = 2;
                optEdge.options.clear();
                listEdgeOpt.clear();
                tk.nextToken();
                if(tk.ttype=='[')
                {
                    optionlist(tk);
                    tk.nextToken();
                }
                return;                
            }
            
            else
            {
                flag_opt = 0;
                flag=0;
                DotNode node1 = getNode(tk.sval);
                System.out.println("Вершина: "+node1.name);
                //scene.createTextCircleNode(node1.name, Color.yellow);
            
                listNodeOpt.add(node1);
                tk.nextToken();
                           
            
            if(tk.ttype=='[')
            {
              optionlist(tk);
              listEdgeOpt.removeAll(listEdgeOpt);
              listNodeOpt.removeAll(listNodeOpt);
              tk.nextToken();
            }
            
            if(tk.ttype==',')
            {
                flag = 1;
                tk.nextToken();
            }
            
            boolean dir;
            if(tk.ttype=='-')
            {
                tk.nextToken();
                if(tk.ttype=='>' && direction==true){
                    dir=true;
                    tk.nextToken();
                }
                else if(tk.ttype=='-' && direction==false){
                    dir=false;
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
                    dir=true;
                    tk.nextToken();
                }
                else return;
            }
            else
            {
                if(optNode.options.size()!=0 && flag==0)
                {
                    setOptionForNode();
                    flag=0;
                    listNodeOpt.clear();
                }
                else if(optNode.options.size()==0 && flag==0) listNodeOpt.clear();
                
                if(optEdge.options.size()!=0)
                {
                    setOptionForEdge();
                    listEdgeOpt.clear();
                }
                return;
            }
                
            if(tk.ttype==tk.TT_WORD)
            {
		DotNode node2 = getNode(tk.sval);
                System.out.println("Вершина: "+node2.name);
                          
                DotEdge edge = getEdge(node1.name, node2.name);
                listEdgeOpt.add(edge);
                edge(tk);
            }
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
        String name_opt;
        String val_opt;
        
        if(tk.ttype==tk.TT_WORD && flag_opt == 0)
        {
          name_opt = tk.sval;
          tk.nextToken();
          if(tk.ttype=='=')
          {
              tk.nextToken();
              if(tk.ttype==tk.TT_WORD)
              {
                  val_opt = tk.sval;
                  DotOption opt = new DotOption(name_opt, val_opt);
                  
                  if(listEdgeOpt.size()==0)
                  {
                      for(int i=0; i<listNodeOpt.size();i++)
                      {
                          System.out.println("OPTION for node "+listNodeOpt.get(i).name+" :"); 
                          for(int j=0; j<ListN.size(); j++)
                          {
                              if(ListN.get(j).name == listNodeOpt.get(i).name)
                              {
                                  ListN.get(j).options.add(opt);
                                  for(int n=0; n<ListN.get(j).options.size(); n++)
                                  {
                                      System.out.println(ListN.get(j).options.get(n).name+" = "+ListN.get(j).options.get(n).value);
                                  }
                              }
                          }
                      }
                  }
                  else
                  {
                      for(int i=0; i<listEdgeOpt.size();i++)
                      {
                          System.out.println("OPTION for edge from < "+listEdgeOpt.get(i).from+" > to <"+listEdgeOpt.get(i).to+" > :"); 
                          for(int j=0; j<ListE.size(); j++)
                          {
                              if(ListE.get(j).from == listEdgeOpt.get(i).from && ListE.get(j).to == listEdgeOpt.get(i).to)
                              {
                                  ListE.get(j).options.add(opt);
                                  for(int n=0; n<ListE.get(j).options.size(); n++)
                                  {
                                      System.out.println("Edge from < "+ListE.get(j).from+" > to < "+ListE.get(j).to+" > Option: "+ListE.get(j).options.get(n).name+" = "+ListE.get(j).options.get(n).value);
                                  }

                              }
                          }
                      }
                  }    
              }    
          }
          else
          {
              System.err.println("Ошибка. Ожидается < = > в строке "+tk.lineno());
              return;
          }
          return;
        }
        
        else if(tk.ttype==tk.TT_WORD && flag_opt == 1)
        {
            name_opt = tk.sval;
            tk.nextToken();
            if(tk.ttype=='=')
            {
                tk.nextToken();
                if(tk.ttype==tk.TT_WORD)
                {
                    val_opt = tk.sval;
                    DotOption optAll = new DotOption(name_opt, val_opt);
                    optNode.options.add(optAll);
                    System.out.println("Список опций для вершин:");
                    for(int n=0; n<optNode.options.size(); n++)
                    {
                        System.out.println(optNode.options.get(n).name+" = "+optNode.options.get(n).value);
                    }
                }
            }
            return;
        }
        
        else if(tk.ttype==tk.TT_WORD && flag_opt == 2)
        {
            name_opt = tk.sval;
            tk.nextToken();
            if(tk.ttype=='=')
            {
                tk.nextToken();
                if(tk.ttype==tk.TT_WORD)
                {
                    val_opt = tk.sval;
                    DotOption optAll = new DotOption(name_opt, val_opt);
                    optEdge.options.add(optAll);
                    System.out.println("Список опций для ребер:");
                    for(int n=0; n<optEdge.options.size(); n++)
                    {
                        System.out.println(optEdge.options.get(n).name+" = "+optEdge.options.get(n).value);
                    }
                }
            }
            return;
        }               
        
        else
        {
            //System.err.println("Ошибка.\n");
            return;
        }
    }
    
  protected void setOptionForNode()
  {
      for(int i=0; i<listNodeOpt.size();i++)
      {
          for(int j=0; j<ListN.size(); j++)
          {
              if(ListN.get(j).name == listNodeOpt.get(i).name)
              {
                  for(int n=0; n<optNode.options.size(); n++)
                  {
                      ListN.get(j).options.add(optNode.options.get(n));
                      System.out.println("Node: "+ListN.get(j).name+" Option: "+ListN.get(j).options.get(n).name+" = "+ListN.get(j).options.get(n).value);
                  }
              }
          }
      }    
  }
  protected void setOptionForEdge()
  {
      for(int i=0; i<listEdgeOpt.size();i++)
      {
          for(int j=0; j<ListE.size(); j++)
          {
              if(ListE.get(j).from == listEdgeOpt.get(i).from && ListE.get(j).to == listEdgeOpt.get(i).to)
              {
                  for(int n=0; n<optEdge.options.size(); n++)
                  {
                      ListE.get(j).options.add(optEdge.options.get(n));
                      System.out.println("Edge from < "+ListE.get(j).from+" > to < "+ListE.get(j).to+" > Option: "+ListE.get(j).options.get(n).name+" = "+ListE.get(j).options.get(n).value);
                  }
              }
          }
      }    
  }
  protected void createGraph()
  {
      for(int i=0; i<ListN.size(); i++)
      {
          if(ListN.get(i).options.size()==0)
          {
              GraphNode node = scene.createTextCircleNode(ListN.get(i).name, Color.yellow);
              ListN.get(i).id = node.getID();              
          }
          else
          {
              GraphNode node = scene.createTextCircleNode(ListN.get(i).name, Color.yellow);
              for(int j=0; j<ListN.get(i).options.size(); j++)
              {
                  switch(ListN.get(i).options.get(j).name){
                    case "color":
                        Color col = OptionColor(ListN.get(i).options.get(j).value);
                        node.getAspect().setColor(col);
                        ListN.get(i).id = node.getID();
                        break;
                    case "label":
                        System.out.println("LABEL");
                        break;
                    case "shape":
                        System.out.println("SHAPE");
                        break;
                    case "style":
                        System.out.println("STYLE");
                        break;
                    default:
                        System.err.println("Ошибка. Неправильная опция");
                        return;
                  }      
              }    
          }    
      }      
      for(int j=0; j<ListE.size(); j++)
      {
          for(int n=0; n<ListN.size(); n++)
          {
              if(ListE.get(j).from.equalsIgnoreCase(ListN.get(n).name))
              {
                  int from = ListN.get(n).id;
                  for(int i=0; i<ListN.size(); i++)
                  {
                      if(ListE.get(j).to.equalsIgnoreCase(ListN.get(i).name))
                      {
                          int to = ListN.get(i).id;
                          if(ListE.get(j).options.size()==0)
                          {
                              GraphEdge edge = scene.createEdge(from, to, EdgeAspect.eEdgeAspectType.SIMPLE_LINE);                              
                          }
                          else
                          {
                              GraphEdge edge = scene.createEdge(from, to, EdgeAspect.eEdgeAspectType.SIMPLE_LINE);
                              for(int c=0; c<ListE.get(j).options.size(); c++)
                              {
                                  switch(ListE.get(j).options.get(c).name)
                                  {
                                      case "color":
                                        Color col = OptionColor(ListE.get(j).options.get(c).value);
                                        edge.getAspect().setColor(col);
                                        break;
                                      case "label":
                                          System.out.println("LABEL");
                                          break;
                                      case "shape":
                                          System.out.println("SHAPE");
                                          break;
                                      case "style":
                                          System.out.println("STYLE");
                                          break;
                                      default:
                                          System.err.println("Ошибка. Неправильная опция");
                                          return;
                                  }
                              }
                          }
                          
                      }
                  }    
              }    
          }                 
      }
      return;
  }
  
  protected Color OptionColor(String value)
  {
      Color col = Color.cyan;
      switch(value){
            case "black":
                col= Color.black;
                break;
            case "blue":
                col= Color.blue;                
                break;
            case "cyan":
                col= Color.cyan;
                break;
            case "darkGray":
                col= Color.darkGray;
                break;
            case "gray":
                col= Color.gray;
                break;
            case "green":
                col= Color.green;
                break;
            case "lightGray":
                col= Color.lightGray;
                break;
            case "margenta":
                col= Color.magenta;
                break;
            case "orange":
                col= Color.orange;
                break;  
            case "pink":
                col= Color.pink;
                break;
            case "red":
                col= Color.red;
                break;
            case "white":
                col= Color.white;
                break;
            case "yellow":
                col= Color.yellow;
                break;    
            default:
                System.err.println("Ошибка. Цвет недопустим");
                //return col;
      }
      return col;      
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