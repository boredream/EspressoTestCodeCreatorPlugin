package form;

import com.intellij.ui.components.JBScrollPane;
import entity.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.List;

public class ViewListDialog extends JDialog {

    private List<Element> elements;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel paneViews;
    private ButtonGroup group;

    public interface OnViewSelectedListener {
        void onViewSelected(Element element);
    }

    public ViewListDialog(final List<Element> elements, final OnViewSelectedListener onViewSelectedListener) {
        this.elements = elements;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Enumeration<AbstractButton> buttons = group.getElements();
                for (int i = 0; buttons.hasMoreElements(); i++) {
                    AbstractButton button = buttons.nextElement();

                    if (button.isSelected()) {
                        onViewSelectedListener.onViewSelected(elements.get(i));
                        break;
                    }
                }

                dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initViewList();
    }

    private void initViewList() {
        paneViews.removeAll();
        paneViews.add(new ViewHeader(), BorderLayout.NORTH);

        JPanel viewListPanel = new JPanel();
        viewListPanel.setLayout(new BoxLayout(viewListPanel, BoxLayout.PAGE_AXIS));
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        group = new ButtonGroup();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            ViewItem viewItem = new ViewItem(element);
            viewItem.addRadioButton(group);
            viewListPanel.add(viewItem);
        }
        viewListPanel.add(Box.createVerticalGlue());
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(viewListPanel);
        paneViews.add(scrollPane, BorderLayout.CENTER);

        paneViews.updateUI();
    }

    private void onCancel() {
        dispose();
    }

}
