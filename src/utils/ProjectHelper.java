package utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import entity.Element;
import org.apache.velocity.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class ProjectHelper {

    public static void createFileWithGeneratedCode(final String fileCode, final Project project, final String folderPath, final String fileName) {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    // 生成路径
                    final VirtualFile folder = createFolderIfNotExist(project, folderPath);
                    // 生成文件
                    VirtualFile createdFile = folder.findOrCreateChildData(project, fileName);
                    // 将代码写入文件
                    setFileContent(project, createdFile, fileCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static VirtualFile setFileContent(Project project, VirtualFile createdFile, String code) throws IOException {
        Charset charset = createdFile.getCharset();
        createdFile.setBinaryContent(code.getBytes(charset));
        FileEditorManager.getInstance(project).openFile(createdFile, true);
        return createdFile;
    }

    private static VirtualFile createFolderIfNotExist(Project project, String folder) throws IOException {
        VirtualFile directory = project.getBaseDir();
        String[] folders = folder.split("/");
        for (String childFolder : folders) {
            VirtualFile childDirectory = directory.findChild(childFolder);
            if (childDirectory != null && childDirectory.isDirectory()) {
                directory = childDirectory;
            } else {
                directory = directory.createChildDirectory(project, childFolder);
            }
        }
        return directory;
    }

    public static VirtualFile getAndroidManifest(Project project) {
        VirtualFile directory = project.getBaseDir();
        String path = "app/src/main/AndroidManifest.xml";
        String[] folders = path.split("/");
        for (String childFolder : folders) {
            VirtualFile childDirectory = directory.findChild(childFolder);
            if (childDirectory != null && childDirectory.isDirectory()) {
                directory = childDirectory;
            } else {
                directory = childDirectory;
                break;
            }
        }
        return directory;
    }

    public static String getComPackageName(Project project) {
        VirtualFile androidManifest = getAndroidManifest(project);
        PsiFile file = PsiManager.getInstance(project).findFile(androidManifest);
        PsiElement rootElement = file.getFirstChild();
        for (PsiElement element : rootElement.getChildren()) {
            if (element instanceof XmlTag) {
                XmlTag tag = (XmlTag) element;
                XmlAttribute attr = tag.getAttribute("package");
                if(attr != null) {
                    return attr.getValue();
                }
            }
        }
        return null;
    }
}