package com.yangteng.views.main.home;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class HomeScene extends Scene {
    public final static HBox center = new HBox();
    static {
        var leftMenus = LeftMenusView.INTER;
        var rightShowView = RightShowViwe.INTER;
        center.getChildren().addAll(leftMenus, rightShowView);
        center.setPrefHeight(700);
    }

    public final static HomeScene INTER =  new HomeScene(center);

    public HomeScene(Parent parent) {
        super(parent);
        this.getStylesheets().add(Objects.requireNonNull(HomeScene.class.getResource("light.css")).toExternalForm());
    }
}