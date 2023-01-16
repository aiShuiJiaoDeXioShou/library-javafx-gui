package com.yangteng.library.views.notebook.main.root;

import com.yangteng.library.component.WTNoteLeftButtonItem;
import com.yangteng.library.views.notebook.main.bookrack.BookRackView;
import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import com.yangteng.library.views.notebook.main.plugin.PluginView;
import com.yangteng.library.views.notebook.main.setting.SettingView;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class NoteLeftButtonBarView extends HBox {

    public static final NoteLeftButtonBarView INSTANCE = new NoteLeftButtonBarView();
    public ListView<WTNoteLeftButtonItem> listView;

    public NoteLeftButtonBarView() {
        this.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 0))));
        WTNoteLeftButtonItem fliesItem, writeItem, setting, plugins;
        fliesItem = new WTNoteLeftButtonItem("\uE7F4", "书籍管理");
        writeItem = new WTNoteLeftButtonItem("\uE70F", "写作");
        plugins = new WTNoteLeftButtonItem("\uE74C", "插件");
        setting = new WTNoteLeftButtonItem("\uE713", "设置");
        listView = new ListView<>();
        {
            listView.getItems().addAll(fliesItem, writeItem, plugins, setting);
            listView.getSelectionModel().select(1);
            listView.setPrefWidth(45);
            listView.setPadding(new Insets(10));
        }
        this.getChildren().addAll(listView);
        this.controller();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "书籍管理" -> {
                    NoteBookRootView.INSTANCE.setCenter(BookRackView.INSTANCE);
                    BookRackView.INSTANCE.update();
                }
                case "写作" -> NoteBookRootView.INSTANCE.setCenter(NoteCoreView.INSTANCE);
                case "插件" -> NoteBookRootView.INSTANCE.setCenter(new PluginView());
                case "设置" -> NoteBookRootView.INSTANCE.setCenter(new SettingView());
            }
        });
    }

}
