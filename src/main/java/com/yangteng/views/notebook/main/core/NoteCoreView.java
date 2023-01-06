package com.yangteng.views.notebook.main.core;

import javafx.scene.layout.BorderPane;

public class NoteCoreView {
    public static final BorderPane INSTANCE = new BorderPane();

    static {
        INSTANCE.setTop(NoteBookMenuView.INSTANCE);
        INSTANCE.setCenter(TabMenuBarView.INSTANCE);
        var menus = LeftNoteBookFileTreeView.INSTANCE;{
            INSTANCE.setLeft(menus);
        }

    }
}
