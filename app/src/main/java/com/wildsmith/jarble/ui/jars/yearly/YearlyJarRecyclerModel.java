package com.wildsmith.jarble.ui.jars.yearly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarViewRecyclerModel;

public class YearlyJarRecyclerModel extends JarViewRecyclerModel {

    public YearlyJarRecyclerModel(JarTableModel jarTableModel) {
        super(jarTableModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.yearly_jar_layout;
    }
}
