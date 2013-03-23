/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.graphview;

import graphview.GraphEdge;
import graphview.GraphScene;
import graphview.shapes.EdgeAspect;
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

     @Test
    public void testSimpleCreateEdge() {
        try
        {
            System.out.println("SimpleCreateEdge");
            
            Random r = new Random();
                  
            int expID=0;
            int expFrom = 0;               
            int expTo = 0;
            
            int tempRandom=0;
            
            GraphEdge edge=null;    
            GraphEdge edge2=null;
            
            
            EdgeAspect.eEdgeAspectType aspectType= EdgeAspect.eEdgeAspectType.SIMPLE_LINE;
            EdgeAspect shape=GraphScene.createEdgeShape(aspectType);
                                            
            for(int i=0; i<20; i++)
            {
                expID=r.nextInt(100);
                expFrom=r.nextInt(50);
                expTo=r.nextInt(50);
                
                try {
                    edge = new GraphEdge(expID, expFrom, expTo, shape);
                    edge2 = new GraphEdge(expID+1, expFrom+1, expTo+1, aspectType);
                } catch (Exception e) {
                    fail("Create Edge error:" + e.getMessage());
                }            
                       
                assertEquals(edge.getID(), expID);
                assertEquals(edge2.getID(), expID+1);
                
                assertEquals(edge.getFromID(), expFrom);
                assertEquals(edge.getToID(), expTo);     
                
                assertEquals(edge2.getFromID(), expFrom+1);
                assertEquals(edge2.getToID(), expTo+1);
                                
                assertEquals(edge.getAspect(), shape);
                assertEquals(edge2.getAspect().getAspectType(), aspectType);
                
                edge=null;                 
            
            }   
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testSimpleGetParam() {
        try
        {
            System.out.println("SimpleGetParam");
            GraphEdge edge=null; 
            int expResult = 0;               
            int result = 0;            
  
            boolean resultBDirectional=false;
                
            EdgeAspect shape=GraphScene.createEdgeShape(EdgeAspect.eEdgeAspectType.SIMPLE_LINE);
            
            try {
                edge = new GraphEdge(999, 123, 321, shape);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
            
            expResult = 999;
            result=edge.getID();
            assertEquals(expResult, result);
            
            expResult = 123;
            result=edge.getFromID();
            assertEquals(expResult, result);
            
            expResult = 321;
            result=edge.getToID();
            assertEquals(expResult, result);
                              
            resultBDirectional=edge.isDirectional();
            assertFalse(resultBDirectional);
            
            assertEquals(shape, edge.getAspect());
            
            edge=null;
            
            try {
                edge = new GraphEdge(0,1,2,shape);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
            
            expResult = 0;
            result=edge.getID();
            assertEquals(expResult, result);
            
            expResult = 1;
            result=edge.getFromID();
            assertEquals(expResult, result);
            
            expResult = 2;
            result=edge.getToID();
            assertEquals(expResult, result);
            
            resultBDirectional=edge.isDirectional();
            assertFalse(resultBDirectional);
            
            assertEquals(shape, edge.getAspect());
            
            edge=null;
        }
        catch (Exception ex) {
            fail("test error");
        }
    }    
     
}
