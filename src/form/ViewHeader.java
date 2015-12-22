package form;

import javax.swing.*;
import java.awt.*;

public class ViewHeader extends JPanel {

    private JLabel labelSelected;
    private JLabel labelViewType;
    private JLabel labelViewName;

    public ViewHeader() {
        labelSelected = new JLabel("Selected");
        labelSelected.setPreferredSize(new Dimension(90, 26));
        labelSelected.setFont(new Font(labelSelected.getFont().getFontName(), Font.BOLD, labelSelected.getFont().getSize()));

        labelViewType = new JLabel("ViewType");
        labelViewType.setPreferredSize(new Dimension(100, 26));
        labelViewType.setFont(new Font(labelViewType.getFont().getFontName(), Font.BOLD, labelViewType.getFont().getSize()));

        labelViewName = new JLabel("ViewName");
        labelViewName.setPreferredSize(new Dimension(100, 26));
        labelViewName.setFont(new Font(labelViewName.getFont().getFontName(), Font.BOLD, labelViewName.getFont().getSize()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(labelSelected);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewType);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewName);
        add(Box.createHorizontalGlue());
    }
}
