package lh.wordtree.component;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jfxtras.styles.jmetro.MDL2IconFont;
import lh.wordtree.comm.entity.fun.OrdinaryFunction;
import lh.wordtree.service.factory.FactoryBeanService;

public class CpHide extends VBox {
    private OrdinaryFunction func;
    private Button show;

    public CpHide() {
        var borderHover = this;
        {
            borderHover.setPrefHeight(10);
            borderHover.prefHeightProperty().bind(FactoryBeanService.heigth);
        }

        show = new Button();
        {
            show.setVisible(false);
            show.setGraphic(new MDL2IconFont("\uE761"));
        }
        borderHover.getChildren().add(show);
        borderHover.hoverProperty().addListener((observable, oldValue, newValue) -> show.setVisible(observable.getValue()));
    }

    public void setFunc(OrdinaryFunction func) {
        this.func = func;
        if (func != null) {
            show.setOnMouseClicked(e -> func.apply());
        }
    }
}
