/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.javadocking.DockingManager;
import com.javadocking.dock.*;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.DockFactory;
import com.javadocking.dock.factory.SingleDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;

import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
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

        // Create the content components.
        JTextArea textPanel1 = new JTextArea();
        JTextArea textPanel2 = new JTextArea();

        // Create the dockables around the content components.
        Dockable dockable1 = new DefaultDockable("Window1", textPanel1, "Window 1", null, DockingMode.ALL - DockingMode.FLOAT);
        Dockable dockable2 = new DefaultDockable("Window2", textPanel2, "Window 2", null, DockingMode.ALL - DockingMode.FLOAT);

        dockable1 = addActions(dockable1);
	dockable2 = addActions(dockable2);
        
        add(rootDock);

        //DockFactory leafChildDockFactory = new SingleDockFactory();
        //rootDock.setChildDockFactory(leafChildDockFactory);
        //rootDock.getCompositeChildDockFactory().setChildDockFactory(leafChildDockFactory);
                
        
        leftDock.addDockable(dockable1, new Position(Position.LEFT));
	rightDoc.addDockable(dockable2, new Position(Position.RIGHT)); 
        
        rootDock.addChildDock(leftDock, new Position(Position.LEFT));
        rootDock.addChildDock(rightDoc, new Position(Position.RIGHT));
        rootDock.setDividerLocation(200);
        
        BorderDock borderDock = new BorderDock(new ToolBarDockFactory());
        borderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
        borderDock.setCenterComponent(rootDock);
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
		
		// Minimize dockables.
		//minimizer.visualizeDockable(dockable5);
		//minimizer.visualizeDockable(dockable6);
		//minimizer.visualizeDockable(dockable7);
		//minimizer.visualizeDockable(dockable8);
                
        dockModel.addRootDock("dock", rootDock, frame);

    }
    
    public Dockable addActions(Dockable dockable)
    {

            Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), new int[0]);
            wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), DockableState.statesAll());
            return wrapper;

    }
    
    public Dockable decorateDockable(Dockable dockable)
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

    private class MessageAction extends AbstractAction
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
    
    TabDock leftDock = new TabDock();
    TabDock rightDoc = new TabDock();
    SplitDock rootDock = new SplitDock();
}
