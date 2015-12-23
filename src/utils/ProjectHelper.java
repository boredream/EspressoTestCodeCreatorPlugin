package utils;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.velocity.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class ProjectHelper {

    public static VirtualFile createFileWithGeneratedCode(String fileCode, Project project, String folderPath, String fileName) throws IOException {
        // 生成路径
        VirtualFile folder = createFolderIfNotExist(project, folderPath);
        // 生成文件
        VirtualFile createdFile = folder.findOrCreateChildData(project, fileName);
        // 将代码写入文件
        return setFileContent(project, createdFile, fileCode);
    }

    public static VirtualFile setFileContent(Project project, VirtualFile createdFile, String code) throws IOException {
        createdFile.setBinaryContent(code.getBytes());
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

    public static List<String> getSourceRootPathList(Project project, AnActionEvent event) {
        List<String> sourceRoots = Lists.newArrayList();
        String projectPath = StringUtils.normalizePath(project.getBasePath());
        for (VirtualFile virtualFile : getModuleRootManager(event).getSourceRoots(false)) {
            sourceRoots.add(StringUtils.normalizePath(virtualFile.getPath()).replace(projectPath, ""));
        }
        return sourceRoots;
    }

    private static ModuleRootManager getModuleRootManager(AnActionEvent event) {
        return ModuleRootManager.getInstance(event.getData(LangDataKeys.MODULE));
    }
}