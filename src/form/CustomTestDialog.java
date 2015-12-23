package form;

import com.intellij.ui.components.JBScrollPane;
import entity.Element;
import entity.EspressoAction;
import entity.EspressoAssertion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CustomTestDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnAddAction;
    private JPanel panelActions;
    private JButton btnAddAssertion;
    private JPanel panelAssertions;
    private JButton buttonOK;
    private JButton buttonCancel;

    private List<Element> elements;
    private List<EspressoAction> espressoActions = new ArrayList<>();
    private List<EspressoAssertion> espressoAssertions = new ArrayList<>();

    public CustomTestDialog(final List<Element> elements) {
        this.elements = elements;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        btnAddAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViews4ActionDialog(elements);
            }
        });

        btnAddAssertion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViews4AssertionDialog(elements);
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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

        initActions();
    }

    private void initActions() {
        // 填充数据
        for (Element element : elements) {
            EspressoAction action = new EspressoAction(element);
            espressoActions.add(action);
        }

        fillActionsList();
    }

    private void fillActionsList() {
        panelActions.removeAll();
        panelActions.add(new ActionHeader(), BorderLayout.NORTH);

        JPanel viewListPanel = new JPanel();
        viewListPanel.setLayout(new BoxLayout(viewListPanel, BoxLayout.PAGE_AXIS));
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        int seq = 1;
        for (int i = 0; i < espressoActions.size(); i++) {
            final EspressoAction action = espressoActions.get(i);
            String actionName = action.getActionName();
            if (actionName.equals(EspressoAction.TYPE_NONE)) {
                continue;
            }

            final ActionItem actionItem = new ActionItem(seq ++, action);
            actionItem.setRemoveListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    espressoActions.remove(action);
                    fillActionsList();
                }
            });
            if (i > 0) {
                viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            viewListPanel.add(actionItem);
        }
        viewListPanel.add(Box.createVerticalGlue());
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(viewListPanel);
        panelActions.add(scrollPane, BorderLayout.CENTER);

        pack();
    }

    private void fillAssertionsList() {
        panelAssertions.removeAll();
        panelAssertions.add(new AssertionHeader(), BorderLayout.NORTH);

        JPanel viewListPanel = new JPanel();
        viewListPanel.setLayout(new BoxLayout(viewListPanel, BoxLayout.PAGE_AXIS));
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        for (int i = 0; i < espressoAssertions.size(); i++) {
            final EspressoAssertion assertion = espressoAssertions.get(i);
            final AssertionItem assertionItem = new AssertionItem(assertion);
            assertionItem.setRemoveListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    espressoAssertions.remove(assertion);
                    fillAssertionsList();
                }
            });
            if (i > 0) {
                viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            viewListPanel.add(assertionItem);
        }
        viewListPanel.add(Box.createVerticalGlue());
        viewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(viewListPanel);
        panelAssertions.add(scrollPane, BorderLayout.CENTER);

        pack();
    }

    private void showViews4ActionDialog(List<Element> elements) {
        ViewListDialog dialog = new ViewListDialog(elements,
                new ViewListDialog.OnViewSelectedListener() {
            @Override
            public void onViewSelected(Element element) {
                addAction(element);
            }
        });
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showViews4AssertionDialog(List<Element> elements) {
        List<Element> element4assertion = new ArrayList<>();
        Element element = new Element("Toast", "@android:id/message");
        element.fieldName = "Toast";
        element4assertion.add(element);
        element4assertion.addAll(elements);

        ViewListDialog dialog = new ViewListDialog(element4assertion,
                new ViewListDialog.OnViewSelectedListener() {
            @Override
            public void onViewSelected(Element element) {
                addAssertion(element);
            }
        });
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void addAction(Element element) {
        EspressoAction action = new EspressoAction(element);
        espressoActions.add(action);
        fillActionsList();
    }

    private void addAssertion(Element element) {
        EspressoAssertion assertion = new EspressoAssertion(element);
        espressoAssertions.add(assertion);
        fillAssertionsList();
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
