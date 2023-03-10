package lh.wordtree.views.root;

import javafx.scene.layout.BorderPane;
import lh.wordtree.views.core.NoteCoreView;

public class NoteBookRootView extends BorderPane {
    public NoteBookRootView() {
        this.setLeft(NoteLeftButtonBarView.newInstance());
        this.setCenter(NoteCoreView.newInstance());
    }

    public static NoteBookRootView newInstance() {
        return NoteBookRootViewHolder.instance;
    }

    private static class NoteBookRootViewHolder {
        public static NoteBookRootView instance = new NoteBookRootView();
    }
}
