package form;

import com.intellij.openapi.ui.ComboBox;
import entity.EspressoAssertion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AssertionItem extends JPanel {

    // ui
    private JLabel labelViewName;
    private JCheckBox cbIsNot;
    private ComboBox cbAssertion;
    private JTextField tfAssertionText;
    private JButton btnOperation;

    public void setRemoveListener(ActionListener removeListener) {
        btnOperation.addActionListener(removeListener);
    }

    /**
     * assertion item
     * @param assertion
     */
    public AssertionItem(final EspressoAssertion assertion) {
        labelViewName = new JLabel(assertion.getTargetElement().fieldName);
        labelViewName.setPreferredSize(new Dimension(100, 26));

        cbIsNot = new JCheckBox();
        cbIsNot.setPreferredSize(new Dimension(50, 26));

        String[] types = EspressoAssertion.assertions.toArray(new String[EspressoAssertion.assertions.size()]);
        cbAssertion = new ComboBox(types);
        cbAssertion.setPreferredSize(new Dimension(100, 26));
        int selectedIndex = -1;
        for (int i = 0; i < types.length; i++) {
            if (assertion.getAssertionName().equals(types[i])) {
                selectedIndex = i;
                break;
            }
        }
        if(selectedIndex != -1) {
            cbAssertion.setSelectedIndex(selectedIndex);
        }
        cbAssertion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String assertionName = (String) e.getItem();
                    tfAssertionText.setEnabled(assertionName.equals(EspressoAssertion.TYPE_WITH_TEXT));

                    assertion.setAssertionName(assertionName);
                }
            }
        });

        tfAssertionText = new JTextField(assertion.getAssertionText());
        tfAssertionText.setPreferredSize(new Dimension(150, 26));
        tfAssertionText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                assertion.setAssertionText(tfAssertionText.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                assertion.setAssertionText(tfAssertionText.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                assertion.setAssertionText(tfAssertionText.getText());
            }
        });

        btnOperation = new JButton("Remove");
        btnOperation.setPreferredSize(new Dimension(100, 26));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(labelViewName);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(cbIsNot);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(cbAssertion);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(tfAssertionText);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(btnOperation);
        add(Box.createHorizontalGlue());

        checkActionState();
    }

    private void checkActionState() {
        String assertionName = (String) cbAssertion.getSelectedItem();
        tfAssertionText.setEnabled(assertionName.equals(EspressoAssertion.TYPE_WITH_TEXT));
    }
}
