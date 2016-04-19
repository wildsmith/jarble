package com.wildsmith.jarble.ui.jars;

import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelListener;

public interface JarViewRecyclerModelListener extends DynamicRecyclerModelListener {

    void onJarClicked(JarTableModel recyclerModel);
}