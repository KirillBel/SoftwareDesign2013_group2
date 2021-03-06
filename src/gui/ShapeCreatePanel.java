/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.PercentLayout;
import graphview.GraphScene;
import graphview.shapes.BaseShape;
import graphview.shapes.NodeAspect;
import graphview.shapes.TextShape;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;

/**
 *
 * @author Kirill
 */
public class ShapeCreatePanel extends javax.swing.JPanel {

    /**
     * Creates new form ShapeCreatePanel
     */
    JOutlookBar bar=null;
    GraphScene scene=null;
    
    
    public ShapeCreatePanel(GraphScene scene_) {
        initComponents();
        
        scene=scene_;
        
        bar=new JOutlookBar();
        this.add(bar);
        bar.setSize(200, 500);
        
        createSimpleShapesTab();
        createExtendedShapesTab();
    }
    
    JPanel addTab(JOutlookBar tabs, String title) 
    {
        JPanel panel = new JPanel();
        //panel.setLayout(new PercentLayout(PercentLayout.VERTICAL, 0));
        //panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.setOpaque(false);

        //JScrollPane scroll = tabs.makeScrollPane(panel);
        //scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tabs.addTab("", panel);

        // this to test the UI gets notified of changes
        int index = tabs.indexOfComponent(panel);
        tabs.setTitleAt(index, title);
        tabs.setToolTipTextAt(index, title + " Tooltip");
        return panel;
    };
    
    public JButton createButton(JPanel panel, String txt, NodeAspect shape)
    {
        JButton button = new JButton(txt);
        try {
        button.setUI((ButtonUI)Class.forName(
            (String)UIManager.get("OutlookButtonUI")).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image img=scene.drawToImage(60,60,shape);
        button.setIcon(new ImageIcon(img));
        panel.add(button);  
        return button;
    };
    
    public void createSimpleShapesTab()
    {
        JPanel panel=addTab(bar, "Simple shapes");
        
        createButton(panel, "Box", scene.createNodeShape(NodeAspect.eNodeAspectType.BOX));
        createButton(panel, "Ellipse", scene.createNodeShape(NodeAspect.eNodeAspectType.ELLIPSE));
        
        TextShape node=(TextShape)scene.createNodeShape(NodeAspect.eNodeAspectType.TEXT);
        node.setText("Text");
        createButton(panel, "Text", node);
        
        createButton(panel, "Image", scene.createNodeShape(NodeAspect.eNodeAspectType.IMAGE));
    };
    
    public void createExtendedShapesTab()
    {
        JPanel panel=addTab(bar, "Extended shapes");
        
        createButton(panel, "Text box", scene.createNodeShape(NodeAspect.eNodeAspectType.BOX,NodeAspect.eNodeAspectType.TEXT));
        createButton(panel, "Text ellipse", scene.createNodeShape(NodeAspect.eNodeAspectType.ELLIPSE,NodeAspect.eNodeAspectType.TEXT));
        
        createButton(panel, "Image box", scene.createNodeShape(NodeAspect.eNodeAspectType.BOX,NodeAspect.eNodeAspectType.IMAGE));
        createButton(panel, "Image ellipse", scene.createNodeShape(NodeAspect.eNodeAspectType.ELLIPSE,NodeAspect.eNodeAspectType.IMAGE));
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 347, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        bar.setSize(getSize());
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
