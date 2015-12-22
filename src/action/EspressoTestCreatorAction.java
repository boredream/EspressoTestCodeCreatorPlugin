package action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import entity.Element;
import form.CustomTestDialog;
import listener.ICancelListener;
import listener.IConfirmListener;
import utils.Utils;

import javax.swing.*;
import java.util.ArrayList;

public class EspressoTestCreatorAction extends BaseGenerateAction implements ICancelListener, IConfirmListener {

    private CustomTestDialog dialog;

    @SuppressWarnings("unused")
    public EspressoTestCreatorAction() {
        super(null);
    }

    @SuppressWarnings("unused")
    public EspressoTestCreatorAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        actionPerformedImpl(project, editor);
    }

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        if (layout == null) {
            Utils.showErrorNotification(project, "No layout found");
            return; // no layout found
        }

        ArrayList<Element> elements = Utils.getIDsFromLayout(layout);
        if (!elements.isEmpty()) {
            showDialog(elements);
        } else {
            Utils.showErrorNotification(project, "No IDs found in layout");
        }
    }

    private void showDialog(ArrayList<Element> elements) {
        dialog = new CustomTestDialog(elements);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    public void onCancel() {
        closeDialog();
    }

    @Override
    public void onConfirm(Project project, Editor editor, ArrayList<Element> elements, String mPrefix) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        closeDialog();

        // count selected elements
        int cnt = 0;
        for (Element element : elements) {
            if (element.used) {
                cnt++;
            }
        }

        if (cnt > 0) { // generate injections
            if (layout == null) {
                return;
            }
//            new LayoutCreator(file, getTargetClass(editor, file), "Generate Injections", elements, layout.getName()).execute();

            if (cnt == 1) {
                Utils.showInfoNotification(project, "One injection added to " + file.getName());
            } else {
                Utils.showInfoNotification(project, String.valueOf(cnt) + " injections added to " + file.getName());
            }
        } else { // just notify user about no element selected
            Utils.showInfoNotification(project, "No injection was selected");
        }
    }

    protected void closeDialog() {
        if (dialog == null) {
            return;
        }

        dialog.setVisible(false);
        dialog.dispose();
    }
}
