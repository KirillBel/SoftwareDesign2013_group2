/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import Graph.GraphEdge;
import Graph.GraphNode;
import java.util.ArrayList;
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

    /**
     * Test of addEdge method, of class GraphNode.
     */
    @Test
    public void testAddEdge() {
        
        try
        {
            GraphNode node=null;
            GraphEdge edge=null;
            ArrayList<Integer> nodeEdgesIDArray=new ArrayList();
            System.out.println("addEdge");
            try {
                node = new GraphNode(0);
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
            
            node.addEdge(0);
            node.addEdge(0);
            node.addEdge(1);
            node.addEdge(2);
            node.addEdge(123); 
            nodeEdgesIDArray=node.getNodeEdgesIDArray();
            assertEquals(0, nodeEdgesIDArray.indexOf(0));
            assertEquals(1, nodeEdgesIDArray.indexOf(1));
            assertEquals(2, nodeEdgesIDArray.indexOf(2));
            assertEquals(123, nodeEdgesIDArray.indexOf(123));
            assertEquals(-1, nodeEdgesIDArray.indexOf(3));
            assertEquals(-1, nodeEdgesIDArray.indexOf(4));
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }


    /**
     * Test of getID method, of class GraphNode.
     */
    @Test
    public void testGetID() {
        
        System.out.println("getID");
        try
        {
            GraphNode node=null;  
            System.out.println("getIDandGetDirection");
            try {
                node = new GraphNode(0);
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
        int expResult = 0;        
        int result = node.getID();
        assertEquals(expResult, result);
          
        node=null; 
        try {
            node = new GraphNode(123);
        } catch (Exception e) {
            fail("Create Node error:" + e.getMessage());
        }
        
        expResult = 123;
        result = node.getID();
        assertEquals(expResult, result);
        
        node=null; 
        try {
            node = new GraphNode(21);
        } catch (Exception e) {
            fail("Create Node error:" + e.getMessage());
        }
        
        expResult = 21;
        result = node.getID();
        assertEquals(expResult, result);
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }   

    /**
     * Test of getNodeEdgesIDArray method, of class GraphNode.
     */
    @Test
    public void testGetNodeEdgesIDArray() {
        System.out.println("getNodeEdgesIDArray");
        try
        {
            GraphNode node=null;  
            System.out.println("getIDandGetDirection");
            try {
                node = new GraphNode(0);
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
            ArrayList<Integer> nodeEdgesIDArray=new ArrayList();
            ArrayList<Integer> resNodeEdgesIDArray=new ArrayList();
            node.addEdge(0);
            node.addEdge(1);
            node.addEdge(2);
            node.addEdge(123); 
            nodeEdgesIDArray.add(0, 0);
            nodeEdgesIDArray.add(1, 1);
            nodeEdgesIDArray.add(2, 2);
            for(int i=nodeEdgesIDArray.size();nodeEdgesIDArray.size()<123;i++)
            {
                nodeEdgesIDArray.add(i, null);
            }
            nodeEdgesIDArray.add(123, 123);
            resNodeEdgesIDArray=node.getNodeEdgesIDArray();
            assertEquals(nodeEdgesIDArray, resNodeEdgesIDArray);
            node.addEdge(21); 
            nodeEdgesIDArray.add(21, 21);
            resNodeEdgesIDArray=node.getNodeEdgesIDArray();
            assertEquals(nodeEdgesIDArray, resNodeEdgesIDArray);
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
}
