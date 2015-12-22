package form;

import com.intellij.ui.components.JBScrollPane;
import entity.Element;
import entity.EspressoAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CustomTestDialog extends JDialog {
    private JPanel contentPane;
    private JButton addButton;
    private JPanel panelActions;
    private JButton buttonOK;
    private JButton buttonCancel;

    private List<Element> elements;
    private List<EspressoAction> espressoActions;

    public CustomTestDialog(List<Element> elements) {
        this.elements = elements;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
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
        espressoActions = new ArrayList<>();
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

    private void showDialog() {
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

    private void addAction(Element element) {
        EspressoAction action = new EspressoAction(element);
        espressoActions.add(action);
        fillActionsList();
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
