/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import graphview.shapes.BaseShape;
import graphview.shapes.LineShape;
import graphview.GraphScene;
import graphview.shapes.BoxShape;
import com.javadocking.dock.LineDock;
import com.javadocking.dock.Position;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import com.javadocking.event.DockingEvent;
import com.javadocking.event.DockingListener;
import geometry.Vec2;
import graphview.shapes.EllipseShape;
import graphview.shapes.ImageShape;
import graphview.shapes.NodeAspect;
import graphview.shapes.TextShape;
import graphview.shapes.TringleShape;
import gui.structurePanel.StructurePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import property.PropertyPanel;

/**
 *
 * @author Kirill
 */
public class MainPanel extends DockablePanel{
    
    private Action closeAction;
    private Action restoreAction;
    
    GraphScene scene;
    Dockable[] buttonDockables;
    
    StructurePanel structure;
    OverviewPanel overview;
    
    Dockable sceneDock;
    Dockable structureDock;
    Dockable objectPropertiesDock;
    Dockable overviewDock;
    
    ShapeCreatePanel shapeCreatePanel=null;
    Dockable shapeCreateDock=null;
    
    JFrame mainFrame;
     
    public MainPanel(JFrame frame,GraphScene scene_)
    {
        super(frame);
        scene=scene_;
        mainFrame=frame;
        initUI();
        initScene();
    }
    
    public void initUI()
    {
        shapeCreatePanel=new ShapeCreatePanel(scene);
        shapeCreateDock = new DefaultDockable("Create shape", shapeCreatePanel, "Shapes", null, DockingMode.ALL - DockingMode.FLOAT);
        shapeCreateDock=addActions(shapeCreateDock);
        rightTabbedDock.addDockable(shapeCreateDock, new Position(1));
        
        
        structure=new StructurePanel(scene, mainFrame, this);
        structureDock = new DefaultDockable("Structure", structure, "Structure", null, DockingMode.ALL - DockingMode.FLOAT);
        structureDock=addActions(structureDock);
        centerTabbedDock.addDockable(structureDock, new Position(1));
        

        sceneDock = new DefaultDockable("Scene", scene, "Scene", null, DockingMode.ALL - DockingMode.FLOAT);
        sceneDock=addActions(sceneDock);
        centerTabbedDock.addDockable(sceneDock, new Position(0));
        
 
        Dimension texpPanelSize= new Dimension(100, 100) ;
        JTextArea textPanel1 = new JTextArea();
        textPanel1.setMinimumSize(texpPanelSize);
        textPanel1.setSize(texpPanelSize);
        JScrollPane pane = new JScrollPane(textPanel1);
       //pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Dockable textPanelDock = new DefaultDockable("Text", pane, "Text", null, DockingMode.ALL - DockingMode.FLOAT);
        textPanelDock=addActions(textPanelDock);
        botLeftTabbedDock.addDockable(textPanelDock, new Position(1));
        
        objectPropertiesDock = new DefaultDockable("Object Properties", scene.objectProperties, "Object Properties", null, DockingMode.ALL - DockingMode.FLOAT);
        objectPropertiesDock=addActions(objectPropertiesDock);
        rightTabbedDock.addDockable(objectPropertiesDock, new Position(1));
        
        overview=new OverviewPanel();
        overviewDock = new DefaultDockable("Overview", overview, "Overview", null, DockingMode.ALL - DockingMode.FLOAT);
        overviewDock=addActions(overviewDock);
        topLeftTabbedDock.addDockable(overviewDock, new Position(1));
        
        
        
        buttonDockables = new Dockable[6];
        buttonDockables[0]  = createButtonDockable("ButtonDockableAdd",              "Add",               new ImageIcon("res/icons/add.png"),               "Add!");
	buttonDockables[1]  = createButtonDockable("ButtonDockableAccept",           "Accept",            new ImageIcon("res/icons/accept.png"),            "Accept!");
	buttonDockables[2]  = createButtonDockable("ButtonDockableCancel",           "Cancel",            new ImageIcon("res/icons/cancel.png"),            "Cancel!");
        buttonDockables[3]  = createButtonDockable("ButtonDockableAdd",              "Add",               new ImageIcon("res/icons/add.png"),               "Add!");
	buttonDockables[4]  = createButtonDockable("ButtonDockableAccept",           "Accept",            new ImageIcon("res/icons/accept.png"),            "Accept!");
	buttonDockables[5]  = createButtonDockable("ButtonDockableCancel",           "Cancel",            new ImageIcon("res/icons/cancel.png"),            "Cancel!");
        
        LineDock toolBarDock1 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        toolBarDock1.addDockable(buttonDockables[0], new Position(0));
	toolBarDock1.addDockable(buttonDockables[1], new Position(1));
	toolBarDock1.addDockable(buttonDockables[2], new Position(2));

        LineDock mainToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        mainToolBar.addDockable(buttonDockables[3], new Position(0));
        
        LineDock nodeToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
	nodeToolBar.addDockable(buttonDockables[4], new Position(1));
	        
        LineDock edgeToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
      	edgeToolBar.addDockable(buttonDockables[5], new Position(2));
        
        compositeToolBarDockTop.addChildDock(toolBarDock1, new Position(0));
        compositeToolBarDockTop.addChildDock(mainToolBar, new Position(1));
        compositeToolBarDockTop.addChildDock(nodeToolBar, new Position(2));
        compositeToolBarDockTop.addChildDock(edgeToolBar, new Position(3));
        
    };
    
    public void initScene()
    {
        NodeAspect shape=new BoxShape(10,10,200,300);
        shape.setColor(Color.yellow);
        
        NodeAspect shape3=new BoxShape(15,15,200,200);
        shape3.setColor(Color.red);
        //shape.addChild(shape3);
        NodeAspect shapetringle = new TringleShape(10,10,100,100,"right");
        // 4 types - top, bottom, right, left.
        shapetringle.setColor(Color.blue);
        
        
        NodeAspect imageShape=new ImageShape(0,0,100,100,"res/images/default.png");
        shape3.addChild(imageShape);
        shape3.setContainerMode(NodeAspect.eContainerType.RESIZE_CHILDS_TO_PARENT);
        
        NodeAspect shape2=new BoxShape(0,0,100,100);
        shape2.setColor(Color.red);
        shape2.setPosition(new Vec2(300,10));

        
        NodeAspect dot=new EllipseShape(0,0,5,5);
        dot.move(new Vec2(200,-100));
        dot.setColor(Color.black);
        
        TextShape text=new TextShape("12345\n67890");
        shape.addChild(text);
        shape.setContainerMode(NodeAspect.eContainerType.RESIZE_PARENT_TO_CHILDS);
        //shape.setContainerMode(BaseShape.CONTAIN_CHILDS_TO_NODE);
        
        NodeAspect ellipse=new EllipseShape(new Vec2(0,0),100);
        ellipse.setPosition(new Vec2(40,100));
        ellipse.setColor(Color.CYAN);
        NodeAspect text2=new TextShape("QWERty");
        ellipse.addChild(text2);
        ellipse.setContainerMode(NodeAspect.eContainerType.RESIZE_PARENT_TO_CHILDS);
        
        NodeAspect ellipse2=new EllipseShape(new Vec2(40,40),100);
        ellipse2.setPosition(new Vec2(100,100));
        ellipse2.setColor(Color.PINK);
        //ellipse2.bDebugDrawBBox=true;
        
        LineShape line = new LineShape(shape,shape2);
        line.insertPoint(new Vec2(200,-100),0);
        
        LineShape line2 = new LineShape(shape,ellipse);
        
        LineShape line3 = new LineShape(shape2,shapetringle);
        
        scene.addShape(shape);
        scene.addShape(shape2);
        //graphMain.getGraphScene().addShape(imageShape);
        scene.addShape(line);
        scene.addShape(ellipse);
        scene.addShape(ellipse2);
        scene.addShape(line2);
        scene.addShape(shape3);
        scene.addShape(shapetringle);
        scene.addShape(line3);
        
        scene.addShape(scene.createNodeShape(NodeAspect.eNodeAspectType.IMAGE));
        //graphMain.getGraphScene().addShape(line3);
        //graphMain.getGraphScene().add(text);
    };
    
    public void hideDock(String str)
    {
        
        
        switch (str) {
            case "Scene":
                closeAction = new DefaultDockableStateAction(sceneDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(sceneDock, DockableState.NORMAL);
                if (scene.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "ObjectProperties":
                closeAction = new DefaultDockableStateAction(objectPropertiesDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(objectPropertiesDock, DockableState.NORMAL);
                if (scene.objectProperties.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "Structure":
                closeAction = new DefaultDockableStateAction(structureDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(structureDock, DockableState.NORMAL);
                if (structure.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "Overview":
                closeAction = new DefaultDockableStateAction(overviewDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(overviewDock, DockableState.NORMAL);
                if (overview.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
                
        }                      
    }
    
//    public void selectStructure()
//    {
//        if(structureDock.getDock().getClass()==centerTabbedDock.getClass()) {
//            ((TabDock)structureDock.getDock()).setSelectedDockable(structureDock);
//        }
//    }
//    public void selectScene()
//    {
//        if(sceneDock.getDock().getClass()==centerTabbedDock.getClass()) {
//            ((TabDock)sceneDock.getDock()).setSelectedDockable(sceneDock);
//        }
//    }
}
