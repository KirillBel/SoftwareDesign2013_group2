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
        graphScene.updateScene(true);
        return node;
    };
    
    public GraphEdge createCustomEdge(int fromID, int toID, Direction dir, LineShape shape)
    {
        GraphEdge edge=graphData.createEdge(fromID, toID, dir);
        edge.setShape(shape);
        graphScene.add(shape);
        shape.setPortA(graphData.getElementOfNodesArray(fromID).getShape());
        shape.setPortB(graphData.getElementOfNodesArray(toID).getShape());
        graphScene.updateScene(true);
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
        graphScene.updateScene(true);
        return b;
    }
    
    public void syncronize()
    {
    };
}
