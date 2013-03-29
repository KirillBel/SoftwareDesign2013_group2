/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Kirill
 */
public class IconStringPropertyEditor  extends AbstractPropertyEditor  {
    private IconStringCellRenderer label;
    private JButton button;
    private IconStringArray.IconStringElem arrElem;
    
    public IconStringPropertyEditor() {
        editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
        ((JPanel)editor).add("*", label = new IconStringCellRenderer());
        label.setOpaque(false);
        ((JPanel)editor).add(button = ComponentFactory.Helper.getFactory()
          .createMiniButton());
        button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            selectValue();
          }
        });
    }
    
    public Object getValue() {
        return arrElem;
    }

    public void setValue(Object value) {
        if(value==null) return;
        if(value instanceof IconStringArray)
        {
            if(((IconStringArray)value).size()==0) return;
            arrElem=((IconStringArray)value).get(0);
        }
        else if(value instanceof IconStringArray.IconStringElem)
        {
            arrElem = (IconStringArray.IconStringElem)value;
        };
        
        label.setValue(arrElem);
    }
    
    public void selectValue()
    {
        if(arrElem==null) return;
        if(arrElem.parent==null) return;
        
        arrElem.parent.showSelectionDial();
        IconStringArray.IconStringElem oldValue=arrElem;
        IconStringArray.IconStringElem newValue=arrElem.parent.getSelected();
        arrElem = newValue;
        
        firePropertyChange(oldValue, newValue);
    };
}
