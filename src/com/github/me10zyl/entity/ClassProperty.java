package com.github.me10zyl.entity;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

/**
 * Created by ZyL on 2017/1/23.
 */
public class ClassProperty {
    private String name;

    private boolean enable;

    private PsiField psiField;

    private PsiClass psiClass;

    private boolean isPlaceHolder;

    public boolean isPlaceHolder() {
        return isPlaceHolder;
    }

    public void setPlaceHolder(boolean placeHolder) {
        isPlaceHolder = placeHolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUpperFirst(){
        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
    }

    public ClassProperty(String name) {
        this.name = name;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public void setPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
