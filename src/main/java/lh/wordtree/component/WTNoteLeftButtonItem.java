package lh.wordtree.component;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class WTNoteLeftButtonItem extends Label {
    public WTNoteLeftButtonItem(String icon, String tooltipText) {
        super(icon);
        this.getStyleClass().addAll("icon-font", "mdl2-assets", "note-left-item");
        this.setId(tooltipText);
        var tooltip = new Tooltip(tooltipText);
        Tooltip.install(this, tooltip);
    }
}
