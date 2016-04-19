package com.wildsmith.jarble.ui.jars.weekly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarViewRecyclerModel;

class WeeklyJarRecyclerModel extends JarViewRecyclerModel {

    public WeeklyJarRecyclerModel(JarTableModel jarTableModel) {
        super(jarTableModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.weekly_jar_layout;
    }
}