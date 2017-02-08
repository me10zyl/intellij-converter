package com.github.me10zyl.util;

import com.github.me10zyl.ConverterToolWindowFactory;
import com.github.me10zyl.GUI;
import com.github.me10zyl.entity.ClassProperty;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZyL on 2017/1/23.
 */
public class ActionUtil {

    public static String TOOL_WINDOW_ID = "Converter";

    public static void left(AnActionEvent event){
        updateToolWindow(event, true);
    }

    public static void right(AnActionEvent event){
        updateToolWindow(event, false);
    }

    private static void updateToolWindow(AnActionEvent event, boolean left){
        PsiFile psi = event.getData(LangDataKeys.PSI_FILE);
        List<PsiClass> classes = PsiTreeUtil.getChildrenOfTypeAsList(psi, PsiClass.class);
        PsiClass clazz = classes.get(0);
        PsiField[] allFields = clazz.getAllFields();
        ConverterToolWindowFactory factory = new ConverterToolWindowFactory();
        ToolWindowManager twm = ToolWindowManager.getInstance(event.getProject());
        boolean isRegisted = false;
        for(String s : twm.getToolWindowIds()){
            if(TOOL_WINDOW_ID.equals(s)){
                isRegisted = true;
                break;
            }
        }
        ToolWindow tw = null;
        if(isRegisted) {
            tw = ToolWindowManager.getInstance(event.getProject()).getToolWindow(TOOL_WINDOW_ID);
        }else{
            tw = twm.registerToolWindow(TOOL_WINDOW_ID, true, ToolWindowAnchor.RIGHT, event.getProject(), true);
            factory.createToolWindowContent(event.getProject(), tw);
        }
        JComponent component = tw.getContentManager().getContent(0).getComponent();
        GUI gui = (GUI)component;
        List<ClassProperty> props;
        List<ClassProperty> anotherProps;
        if(left){
            props = gui.getProps1();
            anotherProps = gui.getProps2();
        }else{
            props = gui.getProps2();
            anotherProps = gui.getProps1();
        }
        clearNullProp(anotherProps);
        props.clear();


        for(PsiField p : allFields){
            ClassProperty classProp = new ClassProperty(p.getName());
            classProp.setPsiField(p);
            classProp.setPsiClass(clazz);
            classProp.setEnable(true);
            classProp.setPlaceHolder(false);
            props.add(classProp);
        }

        int diff = Math.abs(anotherProps.size() - props.size());
        if(anotherProps.size() > props.size()){
            for(int i = 0; (i < diff) && (props.size() > 0);i++){
                ClassProperty classProp = newNullProp();
                props.add(classProp);
            }
        }else{
            for(int i = 0; (i< diff) && (anotherProps.size() > 0);i++){
                ClassProperty classProp = newNullProp();
                anotherProps.add(classProp);
            }
        }

        String title = "";
        String leftName = "?";
        String rightName = "?";
        if(!gui.getProps1().isEmpty()){
            leftName = gui.getProps1().get(0).getPsiClass().getName();
        }
        if(!gui.getProps2().isEmpty()){
            rightName = gui.getProps2().get(0).getPsiClass().getName();
        }
        title = "(" + leftName + " → " + rightName + ")";
        tw.setTitle(title);
        gui.updateData();
        tw.show(null);
    }

    private static void  clearNullProp(List<ClassProperty> list){
        List<ClassProperty> deleting = new ArrayList<>();
        for(ClassProperty c : list){
            if(c.isPlaceHolder()){
                deleting.add(c);
            }
        }
        for(ClassProperty delete : deleting){
            list.remove(delete);
        }
    }

    @NotNull
    private static ClassProperty newNullProp() {
        ClassProperty classProp = new ClassProperty("*");
        classProp.setPsiField(null);
        classProp.setPsiClass(null);
        classProp.setEnable(true);
        classProp.setPlaceHolder(true);
        return classProp;
    }
}
