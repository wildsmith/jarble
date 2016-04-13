package com.wildsmith.jarble.ui.jars;

import com.wildsmith.jarble.provider.jar.JarTableModel;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModelListener;

public interface JarViewRecyclerModelListener extends DynamicRecyclerModelListener {

    void onJarClicked(JarTableModel recyclerModel);
}