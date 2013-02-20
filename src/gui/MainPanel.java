/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.javadocking.dock.LineDock;
import com.javadocking.dock.Position;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import geometry.Vec2;
import graphview.*;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Kirill
 */
public class MainPanel extends DockablePanel{
    
    GraphScene scene;
    Dockable[] buttonDockables;
    
    public MainPanel(JFrame frame)
    {
        super(frame);
        initScene();
    }
    
    @Override
    public void initUI()
    {
        scene=new GraphScene();
        DrawPanel panel=new DrawPanel(scene);
        Dockable dockable1 = new DefaultDockable("Scene", panel, "Scene", null, DockingMode.ALL - DockingMode.FLOAT);
        dockable1=addActions(dockable1);
        rightDoc.addDockable(dockable1, new Position(1));
        
        JTextArea textPanel1 = new JTextArea();
        Dockable dockable2 = new DefaultDockable("Text", textPanel1, "Text", null, DockingMode.ALL - DockingMode.FLOAT);
        dockable2=addActions(dockable2);
        leftDoc.addDockable(dockable2, new Position(1));
        
        buttonDockables = new Dockable[3];
        buttonDockables[0]  = createButtonDockable("ButtonDockableAdd",              "Add",               new ImageIcon("res/icons/add.png"),               "Add!");
	buttonDockables[1]  = createButtonDockable("ButtonDockableAccept",           "Accept",            new ImageIcon("res/icons/accept.png"),            "Accept!");
	buttonDockables[2]  = createButtonDockable("ButtonDockableCancel",           "Cancel",            new ImageIcon("res/icons/cancel.png"),            "Cancel!");
        
        LineDock toolBarDock1 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        toolBarDock1.addDockable(buttonDockables[0], new Position(0));
	toolBarDock1.addDockable(buttonDockables[1], new Position(1));
	toolBarDock1.addDockable(buttonDockables[2], new Position(2));
        
        compositeToolBarDockTop.addChildDock(toolBarDock1, new Position(0));
    };
    
    public void initScene()
    {
        NodeShape shape=new BoxShape(10,10,200,300);
        shape.color=Color.yellow;
        
        NodeShape shape2=new BoxShape(0,0,100,100);
        shape2.color=Color.red;
        shape2.setPosition(new Vec2(300,10));
        
        TextShape text=new TextShape("12345\n67890");
        shape.addChild(text);
        shape.setContainerMode(NodeShape.CONTAIN_NODE_TO_CHILDS);
        
        LineShape line = new LineShape(shape,shape2);
        scene.add(shape);
        scene.add(shape2);
        scene.add(line);
        //scene.add(text);
    };
}
