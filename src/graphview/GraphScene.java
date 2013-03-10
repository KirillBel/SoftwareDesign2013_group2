/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Rect;
import geometry.Vec2;
import graphevents.ShapeMouseEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import property.PropertyPanel;

/**
 *
 * @author Kirill
 */

class MouseState{
    public Vec2 screenPos=new Vec2();
    public Vec2 screenDelta=new Vec2();
    public Vec2 scenePos=new Vec2();
    public Vec2 sceneDelta=new Vec2();
    public int MouseBtnL=0;
    public int MouseBtnR=0;
    public int MouseBtnW=0;
    public int LastBtn=0;
    
    public int getBtn(int nButton)
    {
        switch(nButton)
        {
            case 1: return MouseBtnL;
            case 2: return MouseBtnW;
            case 3: return MouseBtnR;
        };
        return 0;
    };
    
    public void setBtn(int nButton, int val)
    {
        switch(nButton)
        {
            case 1: MouseBtnL=val; break;
            case 2: MouseBtnW=val; break;
            case 3: MouseBtnR=val; break;
        };
        LastBtn=nButton;
    };
}

public class GraphScene extends javax.swing.JPanel{
    Vec2 frameSize=new Vec2(1,1);
    Vec2 offset=new Vec2();
    Vec2 scale=new Vec2(1,1);
    Font font=new Font("Arial",Font.PLAIN,20);
    BaseShape root=new RootShape();
    MouseState mouseState=new MouseState();
    Rect selectionRect=new Rect();
    
    int sceneMode=0;
    public static final int SCENE_MODE_NONE = 0;
    public static final int SCENE_MODE_RECTANGLE_SELECT = 1;
    public static final int SCENE_MODE_DRAG_SELECTED = 2;
    public static final int SCENE_MODE_OFFSET = 3;
    public static final int SCENE_MODE_DRAG_GRIP=4;
    
    public PropertyPanel objectProperties=new PropertyPanel();
    
    BaseShape dragTarget=null;
    int selectedGrip=0;
    
    public GraphScene() {
        initComponents();
    }

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
        
        objectProperties.addPropertySheetChangeListener(listener);

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
    }


    ////////////////////MESSAGES//////////////////////////////////////////////
    void updateMousePos(Vec2 pos)
    {
        pos.x-=this.getLocationOnScreen().x;
        pos.y-=this.getLocationOnScreen().y;

        mouseState.screenDelta=pos.minus(mouseState.screenPos);
        mouseState.screenPos=pos;

        mouseState.scenePos=fromScreen(mouseState.screenPos);
        mouseState.sceneDelta=mouseState.screenDelta.divide(scale);
    };

    PropertyChangeListener listener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if(objectProperties.getShape()!=null)
            {
                objectProperties.getShape().updateProperties(false);
                updateScene();
            };
        }
    };
    
    
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {                                  
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
        root.processEvent(ShapeMouseEvent.createMouseDrag(mouseState.LastBtn, mouseState.scenePos, mouseState.sceneDelta));
        processSceneMode();
        updateScene();
    }                                 

    private void formMouseMoved(java.awt.event.MouseEvent evt) {                                
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
        root.processEvent(ShapeMouseEvent.createMouseMove(mouseState.scenePos, mouseState.sceneDelta));
        processSceneMode();
        updateScene();
    }                                                                   

    private void formMousePressed(java.awt.event.MouseEvent evt) {                                  
        mouseState.setBtn(evt.getButton(), 1);
        
        BaseShape shape=root.getIntersectedChild(mouseState.scenePos);
        dragTarget=null;
        
        if(shape!=null)
        {
            if(shape.bSelected)
            {
                dragTarget=shape;
                selectedGrip=dragTarget.isGripIntersect(mouseState.scenePos);
                if(selectedGrip==0) setSceneMode(SCENE_MODE_DRAG_SELECTED);
                else setSceneMode(SCENE_MODE_DRAG_GRIP);
            }
            root.processEvent(ShapeMouseEvent.createMousePress(mouseState.LastBtn, mouseState.scenePos));
        }
        else
        {
            if(mouseState.MouseBtnL==1)
                setSceneMode(SCENE_MODE_RECTANGLE_SELECT);
            else if(mouseState.MouseBtnR==1)
                setSceneMode(SCENE_MODE_OFFSET);
        };
        
        
        processSceneMode();
        updateScene();
    }                                 

    private void formMouseReleased(java.awt.event.MouseEvent evt) {                                   
        mouseState.setBtn(evt.getButton(), 0);
        dragTarget=null;
        
        if(sceneMode==SCENE_MODE_DRAG_GRIP) endSceneMode();
        if(sceneMode==SCENE_MODE_DRAG_SELECTED) endSceneMode();
        if(sceneMode==SCENE_MODE_OFFSET) endSceneMode();
        if(sceneMode==SCENE_MODE_RECTANGLE_SELECT) endSceneMode();
        
        root.processEvent(ShapeMouseEvent.createMouseRelease(mouseState.LastBtn, mouseState.scenePos));
        processSceneMode();
        updateScene();
    }                                  

    private void formMouseClicked(java.awt.event.MouseEvent evt) {                                  
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
        
        BaseShape shape=root.getIntersectedChild(mouseState.scenePos);
        if(shape!=null)
            objectProperties.fromShape(shape);
        else 
            objectProperties.clearShape();
        
        root.clearAllSelection();
        root.processEvent(ShapeMouseEvent.createMouseClick(mouseState.LastBtn, mouseState.scenePos));
        updateScene();
    }
    
    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {                                     
        if(evt.getWheelRotation()>0) {
            scale=scale.multiply(0.8f);
        }
        else if(evt.getWheelRotation()<0) {
            scale=scale.multiply(1.2f);
        }
        updateScene();
    }
    
    private void formComponentResized(java.awt.event.ComponentEvent evt) {              
        frameSize=new Vec2(this.getSize().width,this.getSize().height);
        updateScene();
    }                                     


    /////////////////END MESSAGES//////////////////////////////////////////////
    
    ////////////////////SCENE MODE/////////////////////////////////////////////
    public void setSceneMode(int nMode)
    {
        endSceneMode();
        sceneMode=nMode;
        
        if(sceneMode==SCENE_MODE_RECTANGLE_SELECT)
        {
            root.clearAllSelection();
            objectProperties.clearShape();
            selectionRect.set(mouseState.scenePos.x,mouseState.scenePos.y,mouseState.scenePos.x,mouseState.scenePos.y);
            updateScene();
        };
    };
    
    public void endSceneMode()
    {
        sceneMode=0;
        updateScene();
    };
    
    public void processSceneMode()
    {
        if(sceneMode==SCENE_MODE_RECTANGLE_SELECT)
        {
            selectionRect.right=mouseState.scenePos.x;
            selectionRect.bottom=mouseState.scenePos.y;
            
            root.clearSelection();
            for(int i=0;i<root.getNumChilds();i++)
            {
                if(root.getChild(i)==null) continue;

                if(root.getChild(i).isIntersects(selectionRect.getConvertedToStd()))
                {
                    root.getChild(i).setSelected(true);
                };
            }
        }
        else if(sceneMode==SCENE_MODE_OFFSET)
        {
            offset=offset.plus(mouseState.screenDelta);
        }
        else if(sceneMode==SCENE_MODE_DRAG_SELECTED)
        {
            if(dragTarget==null) 
            {
                setSceneMode(SCENE_MODE_NONE);
                return;
            }
            
            if(dragTarget.parent==null)
                dragTarget.move(mouseState.sceneDelta);
            else
            {
                for(int i=0;i<dragTarget.parent.getNumChilds();i++)
                {
                    if(dragTarget.parent.getChild(i)==null) continue;
                    if(dragTarget.parent.getChild(i).bSelected)
                    {
                        dragTarget.parent.getChild(i).move(mouseState.sceneDelta);
                    };
                };
            };
        }
        else if(sceneMode==SCENE_MODE_DRAG_GRIP)
        {
            if(dragTarget==null || selectedGrip==0) 
            {
                setSceneMode(SCENE_MODE_NONE);
                return;
            }
            
            if(dragTarget.parent==null)
                dragTarget.move(mouseState.sceneDelta);
            else
            {
                for(int i=0;i<dragTarget.parent.getNumChilds();i++)
                {
                    if(dragTarget.parent.getChild(i)==null) continue;
                    if(dragTarget.parent.getChild(i).bSelected)
                    {
                        dragTarget.parent.getChild(i).onGripDragged(selectedGrip,mouseState.sceneDelta);
                    };
                };
            };
        }
    };
    ///////////////////END SCENE MODE//////////////////////////////////////////
    
    /////////////////////////DRAW//////////////////////////////////////////////
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        draw(g2d);
    }
    
    public void draw(Graphics2D g){
        g.translate(offset.x, offset.y);
        g.scale(scale.x, scale.y);
        
        g.setFont(font);

        drawGrid(g,new Vec2(100,100));
        root.draw(g);
        
        if(sceneMode==SCENE_MODE_RECTANGLE_SELECT)
        {
            g.setColor(Color.orange);
            Rect r=selectionRect.getConvertedToStd();
            g.drawRect((int)r.left, (int)r.top, (int)r.getSize().x, (int)r.getSize().y);
        }
    }
    
    public void drawGrid(Graphics2D g, Vec2 gridSize)
    {
        Rect frameRect=fromScreen(new Rect(0,0,frameSize.x,frameSize.y));
        if((frameRect.getSize().x/gridSize.x)>50) return;
        if((frameRect.getSize().y/gridSize.y)>50) return;
        
        float startX=frameRect.left-frameRect.left%gridSize.x;
        float startY=frameRect.top-frameRect.top%gridSize.y;
        
        
        g.setColor(Color.lightGray);
        
        Stroke oldStroke=g.getStroke();
        BasicStroke stroke=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{9}, 0);
        g.setStroke(stroke);
        
        for(float i=startX;i<=frameRect.right;i+=gridSize.x)
        {
            g.drawLine((int)i, (int)frameRect.top, (int)i, (int)frameRect.bottom);
        };
        
        for(float i=startY;i<=frameRect.bottom;i+=gridSize.y)
        {
            g.drawLine((int)frameRect.left, (int)i, (int)frameRect.right, (int)i);
        };
        g.setStroke(oldStroke);
    };
    
    void updateScene()
    {
        updateUI();
    };
    //////////////////////END DRAW/////////////////////////////////////////////
    
    /////////////////UNIT CONVERSION///////////////////////////////////////////
    public Vec2 fromScreen(Vec2 pt)
    {
        AffineTransform tr=new AffineTransform();
        tr.translate(offset.x, offset.y);
        tr.scale(scale.x, scale.y);
        
        Point.Float pt1=new Point.Float();
        try {
            tr.inverseTransform(pt.toPoint(), pt1);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GraphScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vec2(pt1.x,pt1.y);
    };
    
    public Rect fromScreen(Rect r)
    {
        Vec2 topLeft=fromScreen(r.getTopLeft());
        Vec2 bottomRight=fromScreen(r.getBottomRight());
        return new Rect(topLeft.x,topLeft.y,bottomRight.x,bottomRight.y);
    }
    
    public Vec2 toScreen(Vec2 pt)
    {
        AffineTransform tr=new AffineTransform();
        tr.translate(offset.x, offset.y);
        tr.scale(scale.x, scale.y);
        
        Point.Float pt1=new Point.Float();
        tr.transform(pt.toPoint(), pt1);
        return new Vec2(pt1.x,pt1.y);
    };
    
    public Rect toScreen(Rect r)
    {
        Vec2 topLeft=toScreen(r.getTopLeft());
        Vec2 bottomRight=toScreen(r.getBottomRight());
        return new Rect(topLeft.x,topLeft.y,bottomRight.x,bottomRight.y);
    }
    /////////////END UNIT CONVERSION///////////////////////////////////////////
    
    /////////////////////SHAPES////////////////////////////////////////////////
    public void addShape(BaseShape shape){
        root.addChild(shape);
    }
    
    public void removeAllShapes()
    {
        root.removeAllChilds();
    };
    //////////////////END SHAPES///////////////////////////////////////////////
}
