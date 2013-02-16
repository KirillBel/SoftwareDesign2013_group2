/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import Graph.GraphData;
import Graph.GraphEdge;
import Graph.GraphEdge.Direction;
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

    /**
     * Test of createNode method, of class GraphData.
     */
    @Test
    public void testCreateNodeAndNodeCountAndGetNodesArray() {
        System.out.println("createNodeAndNodeCountAndGetNodesArray");
        try
        {
            GraphData graph=null;
            GraphNode node=null;
            ArrayList<GraphNode> nodes=new ArrayList();
            ArrayList<GraphNode> expNodes=new ArrayList();
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
            graph.createNode();
            
            int expNodesCount=1;
            int countNodes=0;
            countNodes=graph.getCountNodes();
            assertEquals(expNodesCount, countNodes);
            
            
            nodes=graph.getNodesArray();
            node=new GraphNode(0);
            expNodes.add(node);
            for(int i=0; i<nodes.size();i++)
            {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
            }
            
            graph.createNode();
            
            expNodesCount=2;
            countNodes=graph.getCountNodes();
            assertEquals(expNodesCount, countNodes);
            
            
            nodes=graph.getNodesArray();
            node=new GraphNode(1);
            expNodes.add(node);
            for(int i=0; i<nodes.size();i++)
            {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
            }
            
            graph.createNode();
            
            expNodesCount=3;
            countNodes=graph.getCountNodes();
            assertEquals(expNodesCount, countNodes);
            
            
            nodes=graph.getNodesArray();
            node=new GraphNode(2);
            expNodes.add(node);
            for(int i=0; i<nodes.size();i++)
            {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
            } 
            
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

    /**
     * Test of createEdge method, of class GraphData.
     */
    @Test
    public void testCreateEdgeAndEdgeCountAndGetEdgesArray() {
        System.out.println("CreateEdgeAndEdgeCountAndGetEdgesArray");
        try
        {
            GraphData graph=null;
            GraphEdge edge=null;
            ArrayList<GraphEdge> edges=new ArrayList();
            ArrayList<GraphEdge> expEdges=new ArrayList();
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            
            graph.createEdge(0, 1, Direction.IN);
                        
            int expCountEdges=1;
            int countEdges=0;
            countEdges=graph.getCountEdges();
            assertEquals(expCountEdges, countEdges);
            
            
            edges=graph.getEdgesArray();
            edge=new GraphEdge(0, 0, 1, Direction.IN);
            expEdges.add(edge);
            
            for(int i=0; i<edges.size();i++)
            {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
            }
            
            
            graph.createEdge(2, 3, Direction.OUT);
            
            expCountEdges=2;
            countEdges=graph.getCountEdges();
            assertEquals(expCountEdges, countEdges);
            
            
            edges=graph.getEdgesArray();
            edge=new GraphEdge(1, 2, 3, Direction.OUT);
            expEdges.add(edge);
            
            for(int i=0; i<edges.size();i++)
            {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
            }
            
            graph.createEdge(4, 5, Direction.BIDIR);
            
            expCountEdges=3;
            countEdges=graph.getCountEdges();
            assertEquals(expCountEdges, countEdges);
            
            
            edges=graph.getEdgesArray();
            edge=new GraphEdge(2, 4, 5, Direction.BIDIR);
            expEdges.add(edge);
            
            for(int i=0; i<edges.size();i++)
            {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
            } 
            
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

    /**
     * Test of deleteNode method, of class GraphData.
     */
    @Test
    public void testDeleteNode() {
        System.out.println("deleteNode");
        try
        {
            GraphData graph=null;
            GraphNode node=null;
            ArrayList<GraphNode> nodes=new ArrayList();
            ArrayList<GraphNode> expNodes=new ArrayList();
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
            graph.createNode(); 
            node=new GraphNode(0);
            expNodes.add(node);
            
            graph.createNode();            
            node=new GraphNode(1);
            expNodes.add(node);
                        
            graph.createNode();            
            node=new GraphNode(2);
            expNodes.add(node);
            
            nodes=graph.getNodesArray();
            
            for(int i=0; i<nodes.size();i++)
            {
                if(nodes.get(i)!=null)
                {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
                }
            }
                        
            graph.deleteNode(1);
            nodes=graph.getNodesArray();
            expNodes.set(1,null);
            for(int i=0; i<nodes.size();i++)
            {
                if(nodes.get(i)!=null)
                {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
                }
            }
            
            graph.deleteNode(0);
            nodes=graph.getNodesArray();
            expNodes.set(0,null);
            for(int i=0; i<nodes.size();i++)
            {
                if(nodes.get(i)!=null)
                {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
                }
            }
            
            graph.deleteNode(2);
            nodes=graph.getNodesArray();
            expNodes.set(2,null);
            for(int i=0; i<nodes.size();i++)
            {
                if(nodes.get(i)!=null)
                {
                assertTrue(nodes.get(i).equals(expNodes.get(i)));
                }
            }
            
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

    /**
     * Test of deleteEdge method, of class GraphData.
     */
    @Test
    public void testDeleteEdge() {
        System.out.println("deleteEdge");
        try
        {
            GraphData graph=null;
            GraphEdge edge=null;
            ArrayList<GraphEdge> edges=new ArrayList();
            ArrayList<GraphEdge> expEdges=new ArrayList();
            System.out.println("getIDandGetDirection");
            try {
                graph = new GraphData();
            } catch (Exception e) {
                fail("Create Node error:" + e.getMessage());
            }
        
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            graph.createNode();
            
            graph.createEdge(0, 1, Direction.IN);
            edge=new GraphEdge(0, 0, 1, Direction.IN);
            expEdges.add(edge);
            
            graph.createEdge(2, 3, Direction.OUT);            
            edge=new GraphEdge(1, 2, 3, Direction.OUT);
            expEdges.add(edge);
                        
            graph.createEdge(4, 5, Direction.BIDIR);
            edge=new GraphEdge(2, 4, 5, Direction.BIDIR);
            expEdges.add(edge);
           
            edges=graph.getEdgesArray();
            for(int i=0; i<edges.size();i++)
            {
                if(edges.get(i)!=null)
                {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
                }
            }
            
            graph.deleteEdge(1);
            edges=graph.getEdgesArray();
            expEdges.set(1,null);
            for(int i=0; i<edges.size();i++)
            {
                if(edges.get(i)!=null)
                {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
                }
            }
            
            graph.deleteEdge(2);
            edges=graph.getEdgesArray();
            expEdges.set(2,null);
            for(int i=0; i<edges.size();i++)
            {
                if(edges.get(i)!=null)
                {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
                }
            }
            
            graph.deleteEdge(0);
            edges=graph.getEdgesArray();
            expEdges.set(0,null);
            for(int i=0; i<edges.size();i++)
            {
                if(edges.get(i)!=null)
                {
                assertTrue(edges.get(i).equals(expEdges.get(i)));
                }
            }
            
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
}
