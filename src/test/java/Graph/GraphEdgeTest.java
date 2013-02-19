/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.Graph;

import graph.GraphEdge;
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
             
            int resultID=0;
            int resultFrom = 0;               
            int resultTo = 0;
            
            int expID=0;
            int expFrom = 0;               
            int expTo = 0;
            
            GraphEdge.Direction expDirection = null;
            GraphEdge.Direction resultDirect=null;
            GraphEdge edge=null;    
                        
            for(int i=0; i<20; i++)
            {
                expID=r.nextInt(100);
                try {
                    edge = new GraphEdge(expID);
                } catch (Exception e) {
                    fail("Create Edge error:" + e.getMessage());
                }            
            
                resultID = edge.getID();
                
                assertEquals(expID, resultID);
                
                resultDirect=edge.getDirection();
                assertNull(resultDirect);
                
                resultFrom=edge.getFromID();
                resultTo=edge.getToID();
                
                assertEquals(resultFrom, -1);
                assertEquals(resultTo, -1);             
                
                edge=null;                 
            
            }      
            
            for(int i=0; i<20; i++)
            {
                expID=r.nextInt(100);
                expFrom=r.nextInt(50);
                expTo=r.nextInt(50);
                try {
                    edge = new GraphEdge(expID, expFrom, expTo, GraphEdge.Direction.IN);
                } catch (Exception e) {
                    fail("Create Edge error:" + e.getMessage());
                }            
            
                resultID = edge.getID();
                
                assertEquals(expID, resultID);
                
                resultDirect=edge.getDirection();
                expDirection=GraphEdge.Direction.IN;
                assertEquals(resultDirect, expDirection);
                
                resultFrom=edge.getFromID();
                resultTo=edge.getToID();
                
                assertEquals(resultFrom, expFrom);
                assertEquals(resultTo, expTo);             
                
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
            GraphEdge.Direction expDirection = null;
            GraphEdge.Direction resultDirect=null;
            try {
                edge = new GraphEdge(0);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
            
            expResult = 0;
            result=edge.getID();
            assertEquals(expResult, result);
            
            expResult = -1;
            result=edge.getFromID();
            assertEquals(expResult, result);
            
            expResult = -1;
            result=edge.getToID();
            assertEquals(expResult, result);
            
            expDirection=null;
            resultDirect=edge.getDirection();
            assertEquals(expDirection, resultDirect);
            
            edge=null;
            
            try {
                edge = new GraphEdge(999, 123, 321, GraphEdge.Direction.BIDIR);
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
            
            expDirection=GraphEdge.Direction.BIDIR;
            resultDirect=edge.getDirection();
            assertEquals(expDirection, resultDirect);
            
            edge=null;
            
            try {
                edge = new GraphEdge(0,1,2,GraphEdge.Direction.IN);
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
            
            expDirection=GraphEdge.Direction.IN;
            resultDirect=edge.getDirection();
            assertEquals(expDirection, resultDirect);
            
            edge=null;
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testSimpleSetParam() {
        try
        {
            System.out.println("SimpleCreateEdge");
            
            Random r = new Random();
            
            int temp=0;
            int resultFrom = 0;               
            int resultTo = 0;
            
            int expFrom = 0;               
            int expTo = 0;
            
            GraphEdge.Direction expDirection = null;
            GraphEdge.Direction resultDirect=null;
            GraphEdge edge=null;    
            
            
            try {
                edge = new GraphEdge(0);
            } catch (Exception e) {
                fail("Create Edge error:" + e.getMessage());
            }
            
            for(int i=0;i<20;i++)
            {
                expTo=r.nextInt(100);
                expFrom=r.nextInt(100);
                temp=r.nextInt(2);
                if(temp==0)
                {
                    expDirection=GraphEdge.Direction.IN;
                }
                else if(temp==1)
                {
                    expDirection=GraphEdge.Direction.OUT;
                }
                else
                {
                    expDirection=GraphEdge.Direction.BIDIR;
                }
                
                edge.setFrom(expFrom);
                edge.setTo(expTo);
                edge.setDirection(expDirection);
                
                resultFrom=edge.getFromID();
                resultTo=edge.getToID();
                resultDirect=edge.getDirection();
                
                assertEquals(expFrom, resultFrom);
                assertEquals(expTo, resultTo);
                assertEquals(expDirection, resultDirect);
            }
            
            
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    @Test
    public void testSimpleEquals() {
        try
        {
            System.out.println("SimpleEquals");
            Random r=new Random();
            int tempID=0;
            int tempFrom=0;
            int tempTo=0;
            int temp=0;
            GraphEdge.Direction tempDirection=null;
            boolean result=false;
            GraphEdge edge0=null;
            GraphEdge edge1=null;
            
            for(int i=0; i<20;i++)
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
                
                tempID=r.nextInt(100);
                tempFrom=r.nextInt(100);
                tempTo=r.nextInt(100);
                
                edge0=new GraphEdge(tempID, tempFrom, tempTo, tempDirection);
                edge1=new GraphEdge(tempID, tempFrom, tempTo, tempDirection);
                
                result=edge0.equals(edge1);
                
                assertTrue(result);
                
                edge0=null;
                edge1=null;
                
            }            
            
            for(int i=0; i<20;i++)
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
                
                tempID=r.nextInt(100);
                tempFrom=r.nextInt(100);
                tempTo=r.nextInt(100);
                
                edge0=new GraphEdge(tempID, tempFrom+1, tempTo, tempDirection);
                edge1=new GraphEdge(tempID+1, tempFrom, tempTo+1, tempDirection);
                
                result=edge0.equals(edge1);
                
                assertFalse(result);
                
                edge0=null;
                edge1=null;
                
            }                 
        
        }
        catch (Exception ex) {
            fail("test error");
        }
    }
    
    
    
    
    

}
