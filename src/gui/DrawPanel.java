/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import geometry.Vec2;
import graphview.GraphScene;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Kirill
 */
public class DrawPanel extends javax.swing.JPanel {

    /**
     * Creates new form DrawPanel
     */
    public DrawPanel(GraphScene scene_) {
        scene=scene_;
        initComponents();
    }
    
    Point mousePos=new Point();
    Point mouseDelta=new Point();
    int buttonPressed=0;
    int buttonReleased=0;
    
    private GraphScene scene;
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        scene.draw(g2d);
    }
    
    void updateMousePos(Point pos)
    {
        pos.x-=this.getLocationOnScreen().x;
        pos.y-=this.getLocationOnScreen().y;
        
        mouseDelta.x=pos.x-mousePos.x;
        mouseDelta.y=pos.y-mousePos.y;
        mousePos=pos;
    };
    
    void updateScene(boolean forceUpdate)
    {
        if(scene.isNeedUpdate() || forceUpdate) {
            updateUI();
            scene.setUpdate(false);
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        updateMousePos(evt.getLocationOnScreen());
       
        scene.onMouseDrag(buttonPressed,Vec2.fromPoint(mousePos),Vec2.fromPoint(mouseDelta));
        updateScene(false);
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        updateMousePos(evt.getLocationOnScreen());
        scene.onMouseMove(Vec2.fromPoint(mousePos),Vec2.fromPoint(mouseDelta));
        updateScene(false);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        scene.onMouseWheel(evt.getWheelRotation());
        updateScene(false);
    }//GEN-LAST:event_formMouseWheelMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        buttonPressed=evt.getButton();
        buttonReleased=0;
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        buttonPressed=0;
        buttonReleased=evt.getButton();
    }//GEN-LAST:event_formMouseReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        scene.onResize(new Vec2(this.getSize().width,this.getSize().height));
        updateScene(false);
    }//GEN-LAST:event_formComponentResized

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        updateMousePos(evt.getLocationOnScreen());
        scene.onMouseClick(buttonReleased, Vec2.fromPoint(mousePos));
        updateScene(false);
    }//GEN-LAST:event_formMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
