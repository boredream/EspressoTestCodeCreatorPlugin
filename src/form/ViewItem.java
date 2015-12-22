package form;

import com.intellij.openapi.ui.ComboBox;
import entity.Element;
import entity.EspressoAction;
import org.jdesktop.swingx.JXRadioGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ViewItem extends JPanel {

    private Element element;
    // ui
    private JRadioButton rbSelected;
    private JLabel labelViewType;
    private JLabel labelViewName;

    public void addRadioButton(ButtonGroup group) {
        group.add(rbSelected);
    }

    /**
     * element item
     * @param element
     */
    public ViewItem(Element element) {
        this.element = element;

        rbSelected = new JRadioButton();
        rbSelected.setPreferredSize(new Dimension(80, 26));

        labelViewType = new JLabel(element.name);
        labelViewType.setPreferredSize(new Dimension(100, 26));

        labelViewName = new JLabel(element.fieldName);
        labelViewName.setPreferredSize(new Dimension(100, 26));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(rbSelected);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewName);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewType);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewName);
        add(Box.createHorizontalGlue());
    }
}
