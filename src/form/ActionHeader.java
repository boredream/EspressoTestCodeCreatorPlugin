package form;

import javax.swing.*;
import java.awt.*;

public class ActionHeader extends JPanel {

    private JLabel labelSeq;
    private JLabel labelViewName;
    private JLabel labelAction;
    private JLabel labelTypeText;
    private JLabel labelOperation;

    public ActionHeader() {
        labelSeq = new JLabel("Seq");
        labelSeq.setPreferredSize(new Dimension(50, 26));
        labelSeq.setFont(new Font(labelSeq.getFont().getFontName(), Font.BOLD, labelSeq.getFont().getSize()));

        labelViewName = new JLabel("ViewName");
        labelViewName.setPreferredSize(new Dimension(100, 26));
        labelViewName.setFont(new Font(labelViewName.getFont().getFontName(), Font.BOLD, labelViewName.getFont().getSize()));

        labelAction = new JLabel("Actions");
        labelAction.setPreferredSize(new Dimension(100, 26));
        labelAction.setFont(new Font(labelAction.getFont().getFontName(), Font.BOLD, labelAction.getFont().getSize()));

        labelTypeText = new JLabel("TypeText");
        labelTypeText.setPreferredSize(new Dimension(150, 26));
        labelTypeText.setFont(new Font(labelTypeText.getFont().getFontName(), Font.BOLD, labelTypeText.getFont().getSize()));

        labelOperation = new JLabel("Remove");
        labelOperation.setPreferredSize(new Dimension(100, 26));
        labelOperation.setFont(new Font(labelOperation.getFont().getFontName(), Font.BOLD, labelOperation.getFont().getSize()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(labelSeq);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewName);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelAction);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelTypeText);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelOperation);
        add(Box.createHorizontalGlue());
    }
}
