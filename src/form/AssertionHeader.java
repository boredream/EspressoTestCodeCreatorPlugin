package form;

import javax.swing.*;
import java.awt.*;

public class AssertionHeader extends JPanel {

    private JLabel labelViewName;
    private JLabel labelIsNot;
    private JLabel labelAssertion;
    private JLabel labelAssertionText;
    private JLabel labelOperation;

    public AssertionHeader() {
        labelViewName = new JLabel("ViewName");
        labelViewName.setPreferredSize(new Dimension(100, 26));
        labelViewName.setFont(new Font(labelViewName.getFont().getFontName(), Font.BOLD, labelViewName.getFont().getSize()));

        labelIsNot = new JLabel("IsNot");
        labelIsNot.setPreferredSize(new Dimension(50, 26));
        labelIsNot.setFont(new Font(labelIsNot.getFont().getFontName(), Font.BOLD, labelIsNot.getFont().getSize()));

        labelAssertion = new JLabel("Assertions");
        labelAssertion.setPreferredSize(new Dimension(100, 26));
        labelAssertion.setFont(new Font(labelAssertion.getFont().getFontName(), Font.BOLD, labelAssertion.getFont().getSize()));

        labelAssertionText = new JLabel("AssertionText");
        labelAssertionText.setPreferredSize(new Dimension(150, 26));
        labelAssertionText.setFont(new Font(labelAssertionText.getFont().getFontName(), Font.BOLD, labelAssertionText.getFont().getSize()));

        labelOperation = new JLabel("Remove");
        labelOperation.setPreferredSize(new Dimension(100, 26));
        labelOperation.setFont(new Font(labelOperation.getFont().getFontName(), Font.BOLD, labelOperation.getFont().getSize()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(labelViewName);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelIsNot);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelAssertion);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelAssertionText);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelOperation);
        add(Box.createHorizontalGlue());
    }
}
