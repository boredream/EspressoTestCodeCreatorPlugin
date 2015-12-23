package action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import entity.Element;
import entity.EspressoAction;
import entity.EspressoAssertion;
import form.CustomTestDialog;
import listener.ICancelListener;
import listener.IConfirmListener;
import utils.ProjectHelper;
import utils.Utils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EspressoTestCreatorAction extends BaseGenerateAction implements IConfirmListener {

    private CustomTestDialog dialog;
    private Project project;
    private Editor editor;
    private PsiFile file;
    private PsiFile layout;

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
        project = event.getData(PlatformDataKeys.PROJECT);
        editor = event.getData(PlatformDataKeys.EDITOR);

        actionPerformedImpl(project, editor);
    }

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        file = PsiUtilBase.getPsiFileInEditor(editor, project);
        layout = Utils.getLayoutFileFromCaret(editor, file);

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
        dialog = new CustomTestDialog(elements, this);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    public void onConfirm(List<EspressoAction> actions, List<EspressoAssertion> assertions) {
        // 先创建测试基础路径
        String androidTestRootPath = "app/src/androidTest/java";
        // 代码基础路径
        String fileRootPath = "app/src/main/java";
        String fileParentPath = file.getVirtualFile().getParent().getPath();

        // 将代码基础路径替换为测试基础路径,创建测试路径中对应的文件测试类
        String testFilePath = androidTestRootPath + fileParentPath.split(fileRootPath)[1];
        try {
            ProjectHelper.createFileWithGeneratedCode("public class Test {}", project, testFilePath, "Test.java");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (actions.size() > 0 || assertions.size() > 0) { // generate injections
            if (layout == null) {
                return;
            }
//            new LayoutCreator(file, getTargetClass(editor, file), "Generate Injections", elements, layout.getName()).execute();

        } else {
            Utils.showInfoNotification(project, "No actions and assertions");
        }
    }

}
