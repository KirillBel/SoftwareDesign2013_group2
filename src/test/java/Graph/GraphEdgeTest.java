/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import Graph.GraphEdge;
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
public class GraphEdgeTest {
    
    public GraphEdgeTest() {
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
     * Test of getID method, of class GraphEdge.
     */
    @Test
    public void testGetIDandGetDirection() {
        try
        {
        GraphEdge edge=null;    
        System.out.println("getIDandGetDirection");
        try {
                edge = new GraphEdge(0, 2, 3, GraphEdge.Direction.IN);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
        
        int expResult = 0;        
        int result = edge.getID();
        assertEquals(expResult, result);
        GraphEdge.Direction direct= GraphEdge.Direction.IN;
        GraphEdge.Direction resultDirect=edge.getDirection();
        assertEquals(direct, resultDirect);
        
        edge=null; 
        try {
                edge = new GraphEdge(0, 1, 2, GraphEdge.Direction.OUT);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
        
        expResult = 0;
        result = edge.getID();
        assertEquals(expResult, result);
        direct= GraphEdge.Direction.OUT;
        resultDirect=edge.getDirection();
        assertEquals(direct, resultDirect);
        
        edge=null; 
        try {
                edge = new GraphEdge(123, 1, 2, GraphEdge.Direction.OUT);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
        
        expResult = 123;
        result = edge.getID();
        assertEquals(expResult, result);
        direct= GraphEdge.Direction.OUT;
        resultDirect=edge.getDirection();
        assertEquals(direct, resultDirect);
        
        edge=null; 
        try {
                edge = new GraphEdge(21, 2, 3, GraphEdge.Direction.BIDIR);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
        
        expResult = 21;
        result = edge.getID();
        assertEquals(expResult, result);
        direct= GraphEdge.Direction.BIDIR;
        resultDirect=edge.getDirection();
        assertEquals(direct, resultDirect);
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }

}
