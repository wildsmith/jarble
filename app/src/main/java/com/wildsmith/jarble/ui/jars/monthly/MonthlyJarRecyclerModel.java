package com.wildsmith.jarble.ui.jars.monthly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarViewRecyclerModel;

class MonthlyJarRecyclerModel extends JarViewRecyclerModel {

    public MonthlyJarRecyclerModel(JarTableModel jarTableModel) {
        super(jarTableModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.monthly_jar_layout;
    }
}