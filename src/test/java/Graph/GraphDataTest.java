/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import graph.GraphData;
import graph.GraphEdge;
import graph.GraphEdge.Direction;
import graph.GraphNode;
import java.util.ArrayList;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author FlyPig
 */
public class GraphDataTest {
    
    public GraphDataTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   
    @Test    
    public void testCreareNode() {
        
        try
        {
            System.out.println("CreareNode");
            
            GraphData graph=null;
            GraphNode node=null;
            int countNodes=0;
            int expNodesCount=0;     
            boolean result=false;
            ArrayList<GraphNode> expNodes=new ArrayList();

            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
        
            for(int i=0; i<20; i++)
            {
                graph.createNode();
                
                expNodesCount=i+1;               
                countNodes=graph.getCountNodes();
                assertEquals(expNodesCount, countNodes);
                
                node=new GraphNode(i);
                expNodes.add(i, node);
                
                for(int j=0; j<expNodes.size();j++ )
                {
                    result=graph.getElementOfNodesArray(j).equals(expNodes.get(j));
                    assertTrue(result);
                } 
                
                node=null;
                
            }   
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testCreateEdge() {
        
        try
        {
            System.out.println("CreateEdge");
            
            Random r=new Random();
            GraphData graph=null;
            GraphEdge edge=null;
            int tempFrom=0;
            int tempTo=0;
            int temp=0;
            int countEdges=0;
            boolean result=false;
            GraphEdge.Direction tempDirection=null;
            ArrayList<GraphEdge> expEdges=new ArrayList();
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
        
            for(int i=0;i<20;i++)
            {
                graph.createNode();
            }
             
            for(int i=0; i<20; i++)
            {
                temp=r.nextInt(2);
                if(temp==0)
                {
                    tempDirection=GraphEdge.Direction.IN;
                }
                else if(temp==1)
                {
                    tempDirection=GraphEdge.Direction.OUT;
                }
                else
                {
                    tempDirection=GraphEdge.Direction.BIDIR;
                }
                
                tempFrom=r.nextInt(graph.getCountNodes());
                tempTo=r.nextInt(graph.getCountNodes());
                                
                graph.createEdge(tempFrom, tempTo, tempDirection);
                
                countEdges=graph.getCountEdges();
                assertEquals(i+1, countEdges);
                
                edge=new GraphEdge(i, tempFrom, tempTo, tempDirection);
                expEdges.add(edge);
                
                for(int j=0; j<expEdges.size();j++ )
                {
                    result=graph.getElementOfEdgesArray(j).equals(expEdges.get(j));
                    assertTrue(result);
                }                 
            }  
        }        
        catch (Exception ex) {
            fail("test error");
        }
    }

    
    @Test
    public void testDeleteNode() {
        
        try
        {
            System.out.println("DeleteNode");
            
            GraphData graph=null;
            boolean result=false;
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
            
            for(int i=0; i<20; i++)
            {
                graph.createNode(); 
            }
            
            for(int i=0; i<20;i++)
            {
                result=graph.deleteNode(i);
                assertTrue(result);
                assertNull(graph.getElementOfNodesArray(i));
            }
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

    
    @Test
    public void testSimpleDeleteEdge() {
        
        try
        {
            System.out.println("SimpleDeleteEdge");
            
            Random r=new Random();
            GraphData graph=null;
            int tempFrom=0;
            int tempTo=0;
            int temp=0;
            boolean result=false;
            GraphEdge.Direction tempDirection=null;
            
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
        
             for(int i=0;i<20;i++)
            {
                graph.createNode();
            }
             
            for(int i=0; i<20; i++)
            {
                temp=r.nextInt(2);
                if(temp==0)
                {
                    tempDirection=GraphEdge.Direction.IN;
                }
                else if(temp==1)
                {
                    tempDirection=GraphEdge.Direction.OUT;
                }
                else
                {
                    tempDirection=GraphEdge.Direction.BIDIR;
                }
                
                tempFrom=r.nextInt(graph.getCountNodes());
                tempTo=r.nextInt(graph.getCountNodes());
                                
                graph.createEdge(tempFrom, tempTo, tempDirection);
                            
            }  
            
            for(int i=0; i<20;i++)
            {
                result=graph.deleteEdge(i);
                assertTrue(result);
                assertNull(graph.getElementOfEdgesArray(i));
            }  
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testDeleteEdgeWithdependencies() {
        
        try
        {
            System.out.println("DeleteEdgeWithdependencies");
            
            Random r=new Random();
            GraphData graph=null;
            GraphEdge edge=null;
            GraphNode node=null;
            int tempFrom=0;
            int tempTo=0;
            int temp=0;
            boolean result=false;
            GraphEdge.Direction tempDirection=null;
            
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
        
             for(int i=0;i<20;i++)
            {
                graph.createNode();
            }
             
            for(int i=0; i<20; i++)
            {
                temp=r.nextInt(2);
                if(temp==0)
                {
                    tempDirection=GraphEdge.Direction.IN;
                }
                else if(temp==1)
                {
                    tempDirection=GraphEdge.Direction.OUT;
                }
                else
                {
                    tempDirection=GraphEdge.Direction.BIDIR;
                }
                
                tempFrom=r.nextInt(graph.getCountNodes());
                tempTo=r.nextInt(graph.getCountNodes());
                                
                graph.createEdge(tempFrom, tempTo, tempDirection);
                            
            }  
            
            for(int i=0; i<20;i++)
            {
                result=false;
                edge=graph.getElementOfEdgesArray(i);
                tempFrom=edge.getFromID();
                tempTo=edge.getToID();
                node=graph.getElementOfNodesArray(tempFrom);
                for(int j=0; j<node.getSizeOfNodeEdgesIDArray();j++)
                {
                    if(node.getElementOfNodeEdgesIDArray(j)==edge.getID())
                    {
                        result=true;
                        break;
                    }
                }
                assertTrue(result);
                result=false;
                node=graph.getElementOfNodesArray(tempTo);
                for(int j=0; j<node.getSizeOfNodeEdgesIDArray();j++)
                {
                    if(node.getElementOfNodeEdgesIDArray(j)==edge.getID())
                    {
                        result=true;
                        break;
                    }
                }
                assertTrue(result);
                result=false;
                result=graph.deleteEdge(i);
                assertTrue(result);
                assertNull(graph.getElementOfEdgesArray(i));
                result=false;
                node=graph.getElementOfNodesArray(tempFrom);
                for(int j=0; j<node.getSizeOfNodeEdgesIDArray();j++)
                {
                    if(node.getElementOfNodeEdgesIDArray(j)==edge.getID())
                    {
                        result=true;
                        break;
                    }
                }
                assertFalse(result);
                result=false;
                node=graph.getElementOfNodesArray(tempTo);
                for(int j=0; j<node.getSizeOfNodeEdgesIDArray();j++)
                {
                    if(node.getElementOfNodeEdgesIDArray(j)==edge.getID())
                    {
                        result=true;
                        break;
                    }
                }
                assertFalse(result);               
                                
            }  
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testCreateEdgeFail() {
        
        try
        {
            System.out.println("CreateEdgeFail");
            
            Random r=new Random();
            GraphData graph=null;
            GraphEdge edge=null;
            int tempFrom=0;
            int tempTo=0;
            int temp=0;
            int countEdges=0;
            GraphEdge.Direction tempDirection=null;
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }
             
            for(int i=0; i<20; i++)
            {
                temp=r.nextInt(2);
                if(temp==0)
                {
                    tempDirection=GraphEdge.Direction.IN;
                }
                else if(temp==1)
                {
                    tempDirection=GraphEdge.Direction.OUT;
                }
                else
                {
                    tempDirection=GraphEdge.Direction.BIDIR;
                }
                
                tempFrom=r.nextInt(100);
                tempTo=r.nextInt(100);
                                
                edge=graph.createEdge(tempFrom, tempTo, tempDirection);
                
                countEdges=graph.getCountEdges();
                assertEquals(0, countEdges);
                
                assertNull(edge);    
                edge=null;
            }  
        }        
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testDeleteNodeFail() {
        
        try
        {
            System.out.println("DeleteNodeFail");
            
            GraphData graph=null;
            boolean result=false;
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }

            for(int i=0; i<20;i++)
            {
                result=graph.deleteNode(i);
                assertFalse(result);
            }
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

    
    @Test
    public void testSimpleDeleteEdgeFail() {
        
        try
        {
            System.out.println("SimpleDeleteEdgeFail");
            
            Random r=new Random();
            GraphData graph=null; 
            boolean result=false;

            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create graph error:" + e.getMessage());
            }                   
            
            for(int i=0; i<20;i++)
            {
                result=graph.deleteEdge(i);
                assertFalse(result);
            }  
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
  
    
}
