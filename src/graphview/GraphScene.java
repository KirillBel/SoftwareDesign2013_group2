/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview;

import geometry.Intersect;
import geometry.Rect;
import geometry.Vec2;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    boolean bNeedUpdate=true;
    Font font=new Font("Arial",Font.PLAIN,20);
    ArrayList<BaseShape> shapes=new ArrayList();
    ArrayList<BaseShape> selectedShapes=new ArrayList();
    MouseState mouseState=new MouseState();
    int sceneMode=0;
    Rect selectionRect=new Rect();
    
    public static final int SCENE_MODE_NONE = 0;
    public static final int SCENE_MODE_SELECT = 1;
    public static final int SCENE_MODE_DRAG_SELECTED = 2;
    public static final int SCENE_MODE_OFFSET = 3;
    
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
    
    void updateMousePos(Vec2 pos)
    {
        pos.x-=this.getLocationOnScreen().x;
        pos.y-=this.getLocationOnScreen().y;
        
        mouseState.screenDelta=pos.minus(mouseState.screenPos);
        mouseState.screenPos=pos;
        
        mouseState.scenePos=fromScreen(mouseState.screenPos);
        mouseState.sceneDelta=mouseState.screenDelta.divide(scale);
    };
    
    void updateScene(boolean forceUpdate)
    {
        if(isNeedUpdate() || forceUpdate) {
            updateUI();
            setUpdate(false);
        }
    };
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {                                  
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
       
        //onMouseDrag(mouseState);
        processSceneMode();
    }                                 

    private void formMouseMoved(java.awt.event.MouseEvent evt) {                                
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
        processSceneMode();
    }                               

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {                                     
        if(evt.getWheelRotation()>0) {
            scale=scale.multiply(0.8f);
        }
        else if(evt.getWheelRotation()<0) {
            scale=scale.multiply(1.2f);
        }
        updateScene(true);
    }                                    

    private void formMousePressed(java.awt.event.MouseEvent evt) {                                  
        mouseState.setBtn(evt.getButton(), 1);
        
        BaseShape shape=testIntersect(mouseState.scenePos);
        
        if(shape==null)
        {
            if(mouseState.MouseBtnL==1)
                setSceneMode(SCENE_MODE_SELECT);
            else if(mouseState.MouseBtnR==1)
                setSceneMode(SCENE_MODE_OFFSET);
        }
        else if(shape!=null && mouseState.MouseBtnL==1)
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i)==shape)
                {
                    setSceneMode(SCENE_MODE_DRAG_SELECTED);
                    break;
                };
            };
            
        };
    }                                 

    private void formMouseReleased(java.awt.event.MouseEvent evt) {                                   
        mouseState.setBtn(evt.getButton(), 0);
        if(sceneMode==SCENE_MODE_SELECT && mouseState.LastBtn==1) endSceneMode();
        else if(sceneMode==SCENE_MODE_DRAG_SELECTED && mouseState.LastBtn==1) endSceneMode();
        else if(sceneMode==SCENE_MODE_OFFSET && mouseState.LastBtn==3) endSceneMode();
    }                                  

    private void formComponentResized(java.awt.event.ComponentEvent evt) {              
        frameSize=new Vec2(this.getSize().width,this.getSize().height);
        updateScene(true);
    }                                     

    private void formMouseClicked(java.awt.event.MouseEvent evt) {                                  
        updateMousePos(Vec2.fromPoint(evt.getLocationOnScreen()));
        onMouseClick(mouseState);
        updateScene(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        draw(g2d);
    }
    
    public void setSceneMode(int nMode)
    {
        endSceneMode();
        sceneMode=nMode;
        if(sceneMode==SCENE_MODE_SELECT)
        {
            clearSelection();
            selectionRect.set(mouseState.scenePos.x,mouseState.scenePos.y,mouseState.scenePos.x,mouseState.scenePos.y);
            updateScene(true);
        };
    };
    
    public void endSceneMode()
    {
        sceneMode=0;
        updateScene(true);
    };
    
    public void processSceneMode()
    {
        if(sceneMode==SCENE_MODE_SELECT)
        {
            selectionRect.right=mouseState.scenePos.x;
            selectionRect.bottom=mouseState.scenePos.y;
            
            clearSelection();
            for(int i=0;i<shapes.size();i++)
            {
                if(Intersect.rectangle_rectangle(selectionRect.getConvertedToStd(), shapes.get(i).getGlobalPlacement())==Intersect.INCLUSION)
                {
                    setSelected(i,true);
                };
            }
            updateScene(true);
        }
        else if(sceneMode==SCENE_MODE_DRAG_SELECTED)
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                selectedShapes.get(i).move(mouseState.sceneDelta);
                updateScene(true);
            }
        }
        else if(sceneMode==SCENE_MODE_OFFSET)
        {
            offset=offset.plus(mouseState.screenDelta);
            updateScene(true);
        };
    };
    
    public void add(BaseShape shape){
        for(int i=0;i<shapes.size();i++)
        {
            if(shapes.get(i)==shape) return;
        };
        shapes.add(shape);
    }
    
    public void draw(Graphics2D g){
        g.translate(offset.x, offset.y);
        g.scale(scale.x, scale.y);
        
        g.setFont(font);

        drawGrid(g,new Vec2(100,100));
        for(int i=0;i<shapes.size();i++)
        {
            shapes.get(i).draw(g);
        };
        
        if(sceneMode==SCENE_MODE_SELECT)
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
    
    public boolean isNeedUpdate() {
        return bNeedUpdate;
    }
    
    public void setUpdate(boolean val) {
        bNeedUpdate=val;
    }
    
    public BaseShape testIntersect(Vec2 pt)
    {
        for(int i=shapes.size()-1;i>=0;i--)
        {
            if(shapes.get(i).isIntersects(pt))
            {
                return shapes.get(i);
            };
        };
        return null;
    };
    
    public void onMouseClick(MouseState state) {
        if(state.LastBtn==1)
        {
            boolean bIntersects=false;
            for(int i=shapes.size()-1;i>=0;i--)
            {
                if(shapes.get(i).isIntersects(state.scenePos))
                {
                    setSelected(i, !shapes.get(i).bSelected);
                    bIntersects=true;
                    break;
                };
            };
            if(!bIntersects)
            {
                clearSelection();
            };
            setUpdate(true);
        }
    }
    
    public void setSelected(int Index, boolean bSelected)
    {
        if(bSelected)
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i)==shapes.get(Index)) return;
            };
            selectedShapes.add(shapes.get(Index));
            shapes.get(Index).bSelected=true;
        }
        else
        {
            for(int i=0;i<selectedShapes.size();i++)
            {
                if(selectedShapes.get(i)==shapes.get(Index))
                {
                    selectedShapes.remove(i);
                };
            };
            shapes.get(Index).bSelected=false;
        };
    };
    
    public void clearSelection()
    {
        for(int i=0;i<selectedShapes.size();i++)
        {
            selectedShapes.get(i).bSelected=false;
        };
        selectedShapes.clear();
    };
    
    public void removeAllShapes()
    {
        shapes.clear();
        selectedShapes.clear();
        updateScene(true);
    };
}
