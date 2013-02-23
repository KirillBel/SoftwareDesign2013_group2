/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.javadocking.DockingManager;
import com.javadocking.dock.*;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.CompositeToolBarDockFactory;
import com.javadocking.dock.factory.DockFactory;
import com.javadocking.dock.factory.LeafDockFactory;
import com.javadocking.dock.factory.SingleDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.util.ToolBarButton;

import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.drag.DragListener;
import com.javadocking.model.FloatDockModel;
import com.javadocking.visualizer.DockingMinimizer;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.SingleMaximizer;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author Kirill
 */
public class DockablePanel extends JPanel {
    public DockablePanel(JFrame frame)
    {
        super(new BorderLayout());

        // Create the dock model for the docks.
        FloatDockModel dockModel = new FloatDockModel();
        dockModel.addOwner("frame0", frame);

        // Give the dock model to the docking manager.
        DockingManager.setDockModel(dockModel);

        rootDock.addChildDock(leftDoc, new Position(Position.LEFT));
        rootDock.addChildDock(rightDoc, new Position(Position.RIGHT));
        rootDock.setDividerLocation(200);

        BorderDock toolBarBorderDock = new BorderDock(new CompositeToolBarDockFactory(), rootDock);
		toolBarBorderDock.setMode(BorderDock.MODE_TOOL_BAR);
       
        toolBarBorderDock.setDock(compositeToolBarDockTop, Position.TOP);
        toolBarBorderDock.setDock(compositeToolBarDockLeft, Position.LEFT);
        
        BorderDock borderDock = new BorderDock(new ToolBarDockFactory());
        borderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
        borderDock.setCenterComponent(toolBarBorderDock);
        BorderDocker borderDocker = new BorderDocker();
        borderDocker.setBorderDock(borderDock);
        DockingMinimizer minimizer = new DockingMinimizer(borderDocker);
              
        // Create an externalizer.
	FloatExternalizer externalizer = new FloatExternalizer(frame);
	dockModel.addVisualizer("externalizer", externalizer, frame);
                
        // Create a maximizer and add it to the dock model.
	SingleMaximizer maximizePanel = new SingleMaximizer(borderDock);
	dockModel.addVisualizer("maximizePanel", maximizePanel, frame);
                
        dockModel.addVisualizer("minimizePanel", minimizer, frame);
	dockModel.addRootDock("minimizerBorderDock", borderDock, frame);
        // Add the 3 root docks to the dock model.
        
        // Add the border dock of the minimizer to this panel.
	this.add(maximizePanel, BorderLayout.CENTER);
        dockModel.addRootDock("dock", toolBarBorderDock, frame);
    }
    
    protected Dockable addActions(Dockable dockable)
    {
            Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), new int[0]);
            wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), DockableState.statesAll());
            return wrapper;
    }
    
    protected Dockable decorateDockable(Dockable dockable)
    {
            // Add an icon and a description for the tooltip.
            if (dockable instanceof DefaultDockable)
            {
                    Icon icon = new ImageIcon("res/images/text12.gif");
                    ((DefaultDockable)dockable).setIcon(icon);
                    ((DefaultDockable)dockable).setDescription("Small window");
            }

            // Decorate the dockable with a close action.		
            dockable = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), DockableState.statesClosed());

            // Decorate the dockable other actions.
            MessageAction helloAction = new MessageAction(this, "Hello", new ImageIcon("res/images/hello12.gif"), "Hello world!");
            MessageAction cautionAction = new MessageAction(this, "Caution", new ImageIcon("res/images/caution12.gif"), "Be Careful!");
            Action[][] actions = new Action[1][];
            actions[0] = new Action[2];
            actions[0][0] = helloAction;
            actions[0][1] = cautionAction;
            dockable = new ActionDockable(dockable, actions);

            return dockable;

    }
    
    protected Dockable createButtonDockable(String id, String title, Icon icon, String message)
    {
            // Create the action.
            MessageAction action = new MessageAction(this, title, icon, message);

            // Create the button.
            ToolBarButton button = new ToolBarButton(action);

            // Create the dockable with the button as component.
            ButtonDockable buttonDockable = new ButtonDockable(id, button);

            // Add a dragger to the individual dockable.
            createDockableDragger(buttonDockable);

            return buttonDockable;
    }
    
    protected void createDockableDragger(Dockable dockable)
    {
            // Create the dragger for the dockable.
            DragListener dragListener = DockingManager.getDockableDragListenerFactory().createDragListener(dockable);
            dockable.getContent().addMouseListener(dragListener);
            dockable.getContent().addMouseMotionListener(dragListener);
    }

    protected class MessageAction extends AbstractAction
    {

            private Component parentComponent;
            private String message;
            private String name;

            public MessageAction(Component parentComponent, String name, Icon icon, String message)
            {
                    super(name, icon);
                    this.message = message;
                    this.name = name;
                    this.parentComponent = parentComponent;
            }

            public void actionPerformed(ActionEvent actionEvent)
            {
                    JOptionPane.showMessageDialog(parentComponent,
                                    message, name, JOptionPane.INFORMATION_MESSAGE);
            }

    }
    
    TabDock leftDoc = new TabDock();
    TabDock rightDoc = new TabDock();
    SplitDock rootDock = new SplitDock();
    CompositeLineDock compositeToolBarDockTop = new CompositeLineDock(CompositeLineDock.ORIENTATION_HORIZONTAL, false,
                    new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
    CompositeLineDock compositeToolBarDockLeft = new CompositeLineDock(CompositeLineDock.ORIENTATION_VERTICAL, false,
                    new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
}
