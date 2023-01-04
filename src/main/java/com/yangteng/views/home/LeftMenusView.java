package com.yangteng.views.home;

import com.yangteng.component.Menu;
import com.yangteng.views.library.LibraryView;
import com.yangteng.views.setting.SettingView;
import com.yangteng.views.tools.ToolsView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LeftMenusView extends VBox {

    public static final LeftMenusView INTER = new LeftMenusView();
    public static List<Map<Menu, Pane>> router;
    private Integer onMenuClickIndex = -1;
    public LeftMenusView() {
        this.setPrefWidth(200);
        this.setStyle("-fx-background-color: #34495e;");
        this.setPadding(new Insets(15));
        var index = new Menu(this, "首页", "icon/home.png");
        var lib = new Menu(this, "图书馆", "icon/lib.png");
        var tools = new Menu(this, "工具箱", "icon/tools.png");
        var setting = new Menu(this, "设置", "icon/setting.png");
        router = Arrays.asList(
                Map.of(index, IndexView.INTER),
                Map.of(lib, LibraryView.INTER),
                Map.of(tools, ToolsView.INTER),
                Map.of(setting, SettingView.INTER)
        );

        rotation(router);
    }

    private void rotation(List<Map<Menu, Pane>> views) {
        for (int i =0; i < views.size(); i++) {
            Map<Menu, Pane> view = views.get(i);
            int finalI = i;
            view.forEach((menu, pane) -> {
                menu.setOnMouseClicked(mouseEvent -> {
                    ObservableList<Node> children = RightShowViwe.INTER.getChildren();
                    if (children.size() > 0) {
                        children.remove(0);
                        children.add(pane);
                    } else {
                        children.add(pane);
                    }

                   menu.setBorder(new Border(new BorderStroke(Paint.valueOf("#3498db"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 4))));
                    if (onMenuClickIndex != -1){
                        Map<Menu, Pane> oldView = views.get(onMenuClickIndex);
                        oldView.forEach((oldMenu, oldPane) -> oldMenu.setBorder(Border.EMPTY));
                    }
                    onMenuClickIndex = finalI;
                });
            });
        }
    }

}
