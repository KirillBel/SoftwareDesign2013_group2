/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import graph.GraphEdge;
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
public class GraphNodeTest {
    
    public GraphNodeTest() {
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
    public void testSimpleCreateNode() {
        
        try
        {
            System.out.println("SimpleCreateNode");
        
            Random r=new Random();
            
            int resultID=0;
            int expID=0;
            
            
            GraphNode node=null;
            
            for(int i=0; i<20; i++)
            {
                expID=r.nextInt(100);
                 
                try {
                     node = new GraphNode(expID);
                 } catch (Exception e) {
                     fail("Create Node error:" + e.getMessage());
                 }
                
                resultID=node.getID();
                
                assertEquals(expID, resultID);
            }  
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
     @Test
    public void testSimpleAddAndDeleteEdge() {
        
        try
        {
           System.out.println("SimpleAddAndDeleteEdge");
            
           Random r=new Random();
           
           GraphNode node=null;
           
           boolean result=false;
           
           int edgeID=0;
           int temp=0;
           
           ArrayList<Integer> nodeEdgesIDArray=new ArrayList();
           System.out.println("SimpleCreateNode");
           try {
               node = new GraphNode(0);
           } catch (Exception e) {
               fail("Create Node error:" + e.getMessage());
           }
           
           for(int i=0; i<20; i++)
           {
               do
               {
                   edgeID=r.nextInt(1000);
               }
               while(nodeEdgesIDArray.contains(edgeID)==true);
               node.addEdge(edgeID);
               nodeEdgesIDArray.add(edgeID);
               assertEquals(nodeEdgesIDArray.size(), node.getSizeOfNodeEdgesIDArray());
               
           }       
           
           while(nodeEdgesIDArray.size()!=0)
           {
               temp=r.nextInt(nodeEdgesIDArray.size());
               edgeID=nodeEdgesIDArray.get(temp);
               result=node.deleteEdgeFromArray(edgeID);
               nodeEdgesIDArray.remove(temp);
               assertEquals(nodeEdgesIDArray.size(), node.getSizeOfNodeEdgesIDArray());
               assertTrue(result);             
           } 
                    
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
   

    @Test
    public void testSimpleGetElementAndSize() {
        try
        {
            System.out.println("SimpleGetElementAndSize");
            
            Random r=new Random();
            
            int edgeID=0;
            GraphNode node=null;
            
            try {
                node = new GraphNode(0);
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
            
            for(int i=0; i<20; i++)
            {
                edgeID=r.nextInt(100);
                node.addEdge(edgeID);
                assertEquals(node.getSizeOfNodeEdgesIDArray(), i+1);
                assertEquals(node.getElementOfNodeEdgesIDArray(i), edgeID);
            }
            
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testSimpleEqual() {
        try
        {
            System.out.println("SimpleEqual");
            
            Random r=new Random();
            
            int edgeID=0;
            int nodeID;
            boolean result=false;
            GraphNode node0=null;
            GraphNode node1=null;
                       
            for(int i=0; i<20; i++)
            {
                try {
                    nodeID=r.nextInt(100);
                    node0 = new GraphNode(nodeID);
                    node1 = new GraphNode(nodeID);
                    
                } catch (Exception e) {
                    fail("Create Node error:" + e.getMessage());
                }
                
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                
                result=node0.equals(node1);
                assertTrue(result);
                
                node0=null;
                node1=null;
                                
            }
            
            for(int i=0; i<20; i++)
            {
                try {
                    nodeID=r.nextInt(100);
                    node0 = new GraphNode(nodeID);
                    node1 = new GraphNode(nodeID+1);
                    
                } catch (Exception e) {
                    fail("Create Node error:" + e.getMessage());
                }
                
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID+1);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID+1);
                node1.addEdge(edgeID);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                edgeID=r.nextInt(100);
                node0.addEdge(edgeID);
                node1.addEdge(edgeID);
                
                result=node0.equals(node1);
                assertFalse(result);
                
                node0=null;
                node1=null;
                                
            }
                  
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
}
