package com.wildsmith.jarble.ui.jars;

import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;

public abstract class JarViewRecyclerModel implements DynamicRecyclerModel {

    private JarTableModel jarTableModel;

    public JarViewRecyclerModel(JarTableModel jarTableModel) {
        this.jarTableModel = jarTableModel;
    }

    public JarTableModel getJarTableModel() {
        return jarTableModel;
    }

}