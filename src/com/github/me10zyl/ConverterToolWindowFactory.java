package com.github.me10zyl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by ZyL on 2017/1/22.
 */
public class ConverterToolWindowFactory implements ToolWindowFactory {
    private GUI gui;

    public ConverterToolWindowFactory() {
             gui = new GUI();
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(gui, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public GUI getGui() {
        return gui;
    }
}
