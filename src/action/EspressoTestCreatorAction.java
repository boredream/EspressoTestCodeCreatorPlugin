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
import com.intellij.psi.PsiElement;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

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
        if (actions.size() == 0 && assertions.size() == 0) { // generate injections
            Utils.showInfoNotification(project, "No actions and assertions");
            return;
        }

        // 先创建测试基础路径
        String androidTestRootPath = "app/src/androidTest/java";
        // 代码基础路径
        String fileRootPath = "app/src/main/java";
        String fileParentPath = file.getVirtualFile().getParent().getPath();

        // 将代码基础路径替换为测试基础路径,创建测试路径中对应的文件测试类
        String testFilePath = androidTestRootPath + fileParentPath.split(fileRootPath)[1];
        String activityContent = file.getText();
        String activityClassName = Utils.getFileNameWithoutSuffix(file);
        String testClassName = activityClassName + "Test";

        StringBuilder sb = new StringBuilder();

        // package
        String packageStr = file.getFirstChild().getText();
        sb.append(Utils.formatSingleLine(0, packageStr));
        sb.append("\n");

        // class
        sb.append(Utils.formatSingleLine(0, "@org.junit.runner.RunWith(AndroidJUnit4.class)"));
        sb.append(Utils.formatSingleLine(0, "public class " + testClassName + " {"));
        sb.append("\n");
        sb.append(Utils.formatSingleLine(1, "@Rule"));
        sb.append(Utils.formatSingleLine(1, "public ActivityTestRule<" + activityClassName +
                "> mActivityRule = new ActivityTestRule<>(" + activityClassName + ".class, true, false);"));
        sb.append("\n");

        sb.append(Utils.formatSingleLine(1, "@Test"));
        sb.append(Utils.formatSingleLine(1, "public void test() {"));
        sb.append(Utils.formatSingleLine(2, "Intent intent = new Intent();"));
        // 判断页面初始化时是否有getExtra,如果有需要在测试代码中putExtra
        //　userId = getIntent().getLongExtra("userId", 0);
        String getExtraRegex = ".get([\\w]+)Extra\\(\"([\\w_]+)\"";
        Pattern getExtraPattern = Pattern.compile(getExtraRegex);
        Matcher getExtraMatcher = getExtraPattern.matcher(activityContent);
        if (getExtraMatcher.find()) {
            // Intent intent = new Intent();
            // intent.putExtra("userId", 1016l);
            // mActivityRule.launchActivity(intent);

            sb.append(Utils.formatSingleLine(2, "// 待测试页面需要Extra数据如下"));
            String type = getExtraMatcher.group(1);
            String key = getExtraMatcher.group(2);
            sb.append(Utils.formatSingleLine(2, "intent.putExtra(\"" + key + "\", 添加" + type + "类型的值);"));
        }
        sb.append(Utils.formatSingleLine(2, "mActivityRule.launchActivity(intent);"));
        sb.append("\n");

        sb.append(Utils.formatSingleLine(2, "// actions"));
        // 用onView定位控件,并执行动作
        for (EspressoAction ea : actions) {
            String action = "";
            if (ea.getActionName().equals(EspressoAction.TYPE_TYPE_TEXT)) {
                action = ".perform(typeText(\"" + ea.getTypeText() + "\"), closeSoftKeyboard())";
            } else if (ea.getActionName().equals(EspressoAction.TYPE_CLICK)) {
                action = ".perform(click())";
            } else if (ea.getActionName().equals(EspressoAction.TYPE_DOUBLE_CLICK)) {
                action = ".perform(doubleClick())";
            } else if (ea.getActionName().equals(EspressoAction.TYPE_LONG_CLICK)) {
                action = ".perform(longClick())";
            }
            sb.append(Utils.formatSingleLine(2, "onView(withId(" + ea.getTargetElement().getFullID() + "))" + action + ";"));
        }
        sb.append("\n");

        // 断言结果
        sb.append(Utils.formatSingleLine(2, "// assertions"));
        for (EspressoAssertion ea : assertions) {
            String assertion = "";
            if (ea.getAssertionName().equals(EspressoAssertion.TYPE_IS_DISPLAYED)) {
                assertion = appendIsNotAssertionCode(ea, "isDisplayed()");
            } else if (ea.getAssertionName().equals(EspressoAssertion.TYPE_IS_CHECKED)) {
                assertion = appendIsNotAssertionCode(ea, "isChecked()");
            } else if (ea.getAssertionName().equals(EspressoAssertion.TYPE_IS_SELECTED)) {
                assertion = appendIsNotAssertionCode(ea, "isSelected()");
            } else if (ea.getAssertionName().equals(EspressoAssertion.TYPE_WITH_TEXT)) {
                assertion = "withText(\"" + ea.getAssertionText() + "\")";
            }
            assertion = ".check(matches(" + appendIsNotAssertionCode(ea, assertion) + "));";

            sb.append(Utils.formatSingleLine(2, "onView(withId(" + ea.getTargetElement().getFullID() + "))"));
            if(ea.getTargetElement().name.equals("Toast")) {
                String inRoot = ".inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))";
                sb.append(Utils.formatSingleLine(3, inRoot));
            }
            sb.append(Utils.formatSingleLine(3, assertion));
        }

        sb.append(Utils.formatSingleLine(1, "}"));
        sb.append(Utils.formatSingleLine(0, "}"));

        // create
        ProjectHelper.createFileWithGeneratedCode(sb.toString(), project, testFilePath, testClassName + ".java");
    }

    private String appendIsNotAssertionCode(EspressoAssertion ea, String oldAssertion) {
        if (ea.isNot()) {
            return "not(" + oldAssertion + ")";
        } else {
            return oldAssertion;
        }
    }

}
