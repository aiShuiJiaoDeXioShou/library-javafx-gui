package com.yangteng.library.views.notebook.main.core;

import javafx.scene.control.TabPane;

public class TabMenuBarView extends TabPane {

    public static final TabMenuBarView INSTANCE = new TabMenuBarView();

    public TabMenuBarView() {
        this.setPrefSize(NoteCoreView.WIDTH * 0.66, NoteCoreView.HEIGHT);
    }

}
