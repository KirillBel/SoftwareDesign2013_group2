/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import graph.*;
import graph.GraphEdge.Direction;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.DotParser;

/**
 *
 * @author Kirill
 */
public class GraphMain {
    private GraphData graphData=new GraphData();
    private GraphScene graphScene=new GraphScene();
    
    public GraphData getGraphData(){
        return graphData;
    }
    
    public GraphScene getGraphScene(){
        return graphScene;
    }
    
    public GraphNode createCustomNode(BaseShape shape)
    {
        GraphNode node=graphData.createNode();
        node.setShape(shape);
        graphScene.add(shape);
        graphScene.updateScene();
        return node;
    };
    
    public GraphEdge createCustomEdge(int fromID, int toID, Direction dir, LineShape shape)
    {
        GraphEdge edge=graphData.createEdge(fromID, toID, dir);
        edge.setShape(shape);
        graphScene.add(shape);
        shape.setPortA(graphData.getElementOfNodesArray(fromID).getShape());
        shape.setPortB(graphData.getElementOfNodesArray(toID).getShape());
        graphScene.updateScene();
        return edge;
    };
    
    public BoxShape createTextBox(Vec2 pos, String txt, Color color)
    {
        BoxShape shape=new BoxShape(pos.x,pos.y,5,5);
        shape.color=color;
        shape.setContainerMode(BaseShape.CONTAIN_NODE_TO_CHILDS);
        shape.addChild(new TextShape(txt));
        graphScene.add(shape);
        return shape;
    };
    
    public void removeAll()
    {
        graphData.removeAll();
        graphScene.removeAllShapes();
    };
    
    public boolean loadDot(String filename){
        removeAll();
        Reader stream;
        try {
            stream = new FileReader(filename);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphMain.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        DotParser parser=new DotParser(stream,this);
        boolean b=parser.parse();
        graphScene.updateScene();
        return b;
    }
    
    public void syncronize()
    {
    };
    
       public void applySimpleLayout()
    {
        GraphNode Node = null;
        int NodesPerLineCounter = 1;
        int NodesPerColumnCounter = 1;
        Vec2 DemencionsOfNode = new Vec2();
        Vec2 MaxDemencions = new Vec2();
        Vec2 tempPlacement = new Vec2();
        BaseShape NodeShape = null;
        int NodeCount = graphData.getCountNodes();
        double tempNodesPerLine = Math.sqrt(NodeCount);
        int NodesPerLine = (int) Math.round(tempNodesPerLine);
        for (int i = 0; i<NodeCount; i++)
        {
            Node = graphData.getElementOfNodesArray(i);
            DemencionsOfNode = Node.getShape().getGlobalPlacement().getSize();
            MaxDemencions.x = Math.max(MaxDemencions.x, DemencionsOfNode.x);
            MaxDemencions.y = Math.max(MaxDemencions.y, DemencionsOfNode.y);
        }
        for (int i = 0; i<NodeCount; i++)
        {
            Node = graphData.getElementOfNodesArray(i);
            tempPlacement.x = MaxDemencions.x*2*NodesPerLineCounter;
            tempPlacement.y = MaxDemencions.y*2*NodesPerColumnCounter;
            Node.getShape().setGlobalPosition(tempPlacement);
            if (NodesPerLineCounter == NodesPerLine)
            {
                NodesPerLineCounter = 1;
                NodesPerColumnCounter++;
            }
            else
            {
                NodesPerLineCounter++;
            }
            
        }
        graphScene.updateScene();
    };
}
