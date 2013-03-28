/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphview.layouts;

import graphview.GraphScene;

/**
 *
 * @author Kirill
 */
public class HierarchicalLayout extends BaseLayout{

    @Override
    public void applyLayout(GraphScene scene_) {
        scene=scene_;
        
        boolean r=calculateCyclicMetric(0);
    }
    
}
