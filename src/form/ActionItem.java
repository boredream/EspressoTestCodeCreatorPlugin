package form;

import com.intellij.openapi.ui.ComboBox;
import entity.EspressoAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ActionItem extends JPanel {

    private EspressoAction action;
    // ui
    private JLabel labelSeq;
    private JLabel labelViewName;
    private ComboBox cbAction;
    private JTextField tfTypeText;
    private JButton btnOperation;

    public void setRemoveListener(ActionListener removeListener) {
        btnOperation.addActionListener(removeListener);
    }

    /**
     * action item
     * @param seq start with 1
     * @param action
     */
    public ActionItem(int seq, final EspressoAction action) {
        this.action = action;

        labelSeq = new JLabel(String.valueOf(seq));
        labelSeq.setPreferredSize(new Dimension(50, 26));

        labelViewName = new JLabel(action.getTargetElement().fieldName);
        labelViewName.setPreferredSize(new Dimension(100, 26));

        String[] types = EspressoAction.actions.toArray(new String[EspressoAction.actions.size()]);
        cbAction = new ComboBox(types);
        cbAction.setPreferredSize(new Dimension(100, 26));
        int selectedIndex = -1;
        for (int i = 0; i < types.length; i++) {
            if (action.getActionName().equals(types[i])) {
                selectedIndex = i;
                break;
            }
        }
        if(selectedIndex != -1) {
            cbAction.setSelectedIndex(selectedIndex);
        }
        cbAction.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String actionName = (String) e.getItem();
                    tfTypeText.setEnabled(actionName.equals(EspressoAction.TYPE_TYPE_TEXT));

                    action.setActionName(actionName);
                }
            }
        });

        tfTypeText = new JTextField(action.getTypeText());
        tfTypeText.setPreferredSize(new Dimension(150, 26));
        tfTypeText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action.setTypeText(tfTypeText.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action.setTypeText(tfTypeText.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                action.setTypeText(tfTypeText.getText());
            }
        });

        btnOperation = new JButton("Remove");
        btnOperation.setPreferredSize(new Dimension(100, 26));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(labelSeq);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(labelViewName);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(cbAction);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(tfTypeText);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(btnOperation);
        add(Box.createHorizontalGlue());

        checkActionState();
    }

    private void checkActionState() {
        String selectedAction = (String) cbAction.getSelectedItem();
        tfTypeText.setEnabled(selectedAction.equals(EspressoAction.TYPE_TYPE_TEXT));
    }
}
