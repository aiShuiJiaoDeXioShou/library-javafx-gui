package lh.wordtree.component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.component.editor.WTLangCodeArea;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.file.FileService;
import lh.wordtree.service.file.MenuFileServiceImpl;
import lh.wordtree.service.language.LanguageConstructorService;
import lh.wordtree.service.language.LanguageConstructorServiceImpl;
import lh.wordtree.service.task.TaskService;
import lh.wordtree.task.ITask;
import lh.wordtree.ui.controls.WTFxInputAlert;
import lh.wordtree.ui.utils.SvgUtils;
import lh.wordtree.views.core.FileTreeView;
import lh.wordtree.views.core.TabMenuBarView;
import org.fxmisc.richtext.CodeArea;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CpFileMenu extends TreeItem<Label> {
    private final File file;
    private final FileService fileService = FileTreeView.newInstance().fileService;
    private final Label label;

    private final TabMenuBarView tabMenuBar = TabMenuBarView.newInstance();

    public CpFileMenu(File file) {
        this.file = file;
        label = new Label();
        addIcon(label, file);
        label.setId(file.getPath());
        label.setText(file.getName());
        this.setValue(label);
        label.setMinWidth(230);
        label.setMinHeight(30);
        this.setAction(this);
    }

    private void addIcon(Object labeled, File file) {
        var imageView = new ImageView();
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        if (file.isFile()) {
            var extendName = WTFileUtils.lastName(file);
            if ("".equals(extendName)) {
                imageView.setImage(SvgUtils.imageFromSvg(ClassLoaderUtils.load("static/icon/default_file.svg")));
                if (labeled instanceof Tab tab) {
                    tab.setGraphic(imageView);
                } else if (labeled instanceof Label label) {
                    label.setGraphic(imageView);
                }
            } else {
                imageView.setImage(SvgUtils.imageFromSvg(ClassLoaderUtils.load("static/icon/" + extendName + ".svg")));
                if (labeled instanceof Tab tab) {
                    tab.setGraphic(imageView);
                } else if (labeled instanceof Label label) {
                    label.setGraphic(imageView);
                }
            }
        } else {
            imageView.setImage(SvgUtils.imageFromSvg(ClassLoaderUtils.load("static/icon/default_folder.svg")));
            if (labeled instanceof Tab tab) {
                tab.setGraphic(imageView);
            } else if (labeled instanceof Label label) {
                label.setGraphic(imageView);
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param fileTree
     */
    private void setAction(@NotNull TreeItem<Label> fileTree) {
        if (file.isFile()) {
            label.setOnMouseClicked(event -> {
                // ??????????????????????????????
                if (event.getButton().name().equals(MouseButton.SECONDARY.name()))
                    fileMenuAddContextMenu(fileTree);
                // ????????????
                if (event.getClickCount() >= 2)
                    this.menuDoubleClick(fileTree);
                // ??????????????????????????????
                if (event.getButton().name().equals(MouseButton.SECONDARY.name()))
                    fileMenuAddContextMenu(fileTree);
            });
            return;
        }
        label.setOnMouseClicked(event -> {
            // ??????????????????????????????
            if (event.getButton().name().equals(MouseButton.SECONDARY.name()))
                fileMenuAddContextMenu(fileTree);
        });
        // ????????????????????????????????????????????????
        var nullTree = new TreeItem<Label>();
        this.getChildren().add(nullTree);
        this.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.getChildren().remove(nullTree);
                // ????????????????????????,???????????????????????????
                if (Objects.requireNonNull(file.listFiles()).length <= this.getChildren().size()) return;
                // ????????????????????????????????????
                for (File f : Arrays.stream(file.listFiles()).sorted((o1, o2) -> {
                    if (o1.isDirectory() == o2.isDirectory()) {
                        return o1.compareTo(o2);
                    } else {
                        if (o1.isDirectory()) {
                            return -1;
                        } else return 0;
                    }
                }).toList()) {
                    var menu = new CpFileMenu(f);
                    this.getChildren().add(menu);
                }
            } else {
                // ??????????????????100?????????????????????
                if (this.getChildren().size() > 100) return;
                this.getChildren().clear();
                this.getChildren().add(nullTree);
            }
        });
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param fileTree
     */
    private void fileMenuAddContextMenu(@NotNull TreeItem<Label> fileTree) {
        var target = fileTree.getValue();
        var contextMenu = new ContextMenu();

        var cp = new MenuItem("??????");
        cp.setOnAction(e -> fileService.copy(target.getId()));

        var cv = new MenuItem("??????");
        cv.setOnAction(e -> {
            var file = new File(target.getId());
            // ?????????????????????????????????
            var paste = fileService.paste(target.getId());
            // ????????????????????????????????????
            paste.forEach(f -> {
                var tree = new MenuFileServiceImpl(f).getTree();
                if (file.isDirectory())
                    fileTree.getChildren().add(tree);
                else fileTree.getParent().getChildren().add(tree);
            });
        });

        var del = new MenuItem("??????");
        menuDelFunction(fileTree, fileService, target, del);

        var newFile = new MenuItem("????????????");
        newFile.setOnAction(e -> {
            newFileOrFolder(fileTree, fileService, target, e, 0);
        });

        var newFolder = new MenuItem("???????????????");
        newFolder.setOnAction(e -> {
            newFileOrFolder(fileTree, fileService, target, e, 1);
        });

        var openFileDir = new MenuItem("????????????????????????");
        openFileDir.setOnAction(e -> {
            fileService.openFileDir(target.getId());
        });

        var rename = new MenuItem("?????????");
        rename(fileTree, fileService, target, rename);

        var upload = new MenuItem("????????????");
        upload.setOnAction(e -> {
            FileTreeView.newInstance().toggleFile(FileTreeView.newInstance().nowFile);
        });

        contextMenu.getItems().addAll(cp, cv, del, openFileDir, newFile, newFolder, rename, upload);
        contextMenu.show(target, Side.BOTTOM, 0, 0);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param fileTree
     */
    private void menuDoubleClick(TreeItem<Label> fileTree) {
        ObservableList<Tab> tabs = tabMenuBar.getTabs();
        FilteredList<Tab> filtered = tabs.filtered(tab -> tab.getId().equals(fileTree.getValue().getId()));
        if (filtered.size() > 0) {
            Tab tab = filtered.get(0);
            tabMenuBar.getSelectionModel().select(tab);
        } else {
            this.addTab();
        }
    }

    /**
     * ???????????????
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param e
     */
    private void newFileOrFolder(TreeItem<Label> fileTree, FileService fileService, @NotNull Label target, ActionEvent e, int type) {
        var bool = new File(target.getId()).isFile();
        TreeItem<Label> parent;
        if (bool) {
            parent = fileTree.getParent();
        } else {
            parent = fileTree;
        }

        var alert = new WTFxInputAlert("??????", alert1 -> {
            var textField = alert1.textField;
            var idStr = parent.getValue().getId() + "/" + textField.getText();
            var b = type == 0 ? fileService.createFile(idStr) : fileService.createFolder(idStr);
            if (b) {
                var treeItem = new CpFileMenu(new File(idStr));
                parent.getChildren().add(treeItem);
            }
        });
        var textField = alert.textField;
        // ?????????????????????
        Platform.runLater(textField::requestFocus);
        alert.show();
    }

    /**
     * ?????????????????????????????????????????????tab??????
     */
    private void addTab() {
        FactoryBeanService.nowFile.set(file);
        LanguageConstructorService lsc = new LanguageConstructorServiceImpl(file);
        ThreadUtil.execAsync(() -> TaskService.INSTANCE.start(ITask.TOGGLE_FILE));
        Node build = lsc.build();
        var tab = new CpTab(tabMenuBar);
        {
            tab.textProperty().bind(this.label.textProperty());
            tab.idProperty().bind(this.label.idProperty());
        }
        FactoryBeanService.nowWorkSpace.set(build);
        if (build instanceof WTWriterEditor code) {
            addCode(tab, code);
            tab.setContent(code);
            FactoryBeanService.nowCodeArea.set(code);
        } else if (build instanceof WebView webView) {
            tab.setContent(webView);
        } else if (build instanceof SplitPane box) {
            if (box.getItems().get(0) instanceof CodeArea code) {
                addCode(tab, code);
            }
            tab.setContent(box);
        } else {
            tab.setContent(build);
        }
        tabMenuBar.getTabs().add(tab);
        tabMenuBar.getSelectionModel().select(tab);
        tab.getContent().requestFocus();
    }

    private void addCode(Tab tab, CodeArea code) {
        tab.setGraphic(new Text(""));
        tab.setOnCloseRequest(event -> {
            var target = (Tab) event.getSource();
            var graphic = (Text) target.getGraphic();
            var iconText = graphic.getText();
            if (iconText.equals("*")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("????????????????????????????????????");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    alert.close();
                } else {
                    event.consume();
                    return;
                }
            }
            System.gc();
            tabMenuBar.getTabs().remove(tab);
        });
        String context = FileUtil.readString(this.label.getId(), StandardCharsets.UTF_8);
        if (StrUtil.isBlank(context.trim())) code.appendText("\s\s\s\s" + context);
        if (!(code instanceof WTLangCodeArea)) code.appendText(context);
        // ????????????????????????????????????tab?????????ui??????
        code.textProperty().addListener((observable, oldValue, newValue) -> {
            tab.setGraphic(new Text("*"));
        });
    }

    /**
     * ?????????
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param rename
     */
    private void rename(TreeItem<Label> fileTree, FileService fileService, Label target, @NotNull MenuItem rename) {
        rename.setOnAction(e -> {
            var oldId = target.getId();
            var alert = new WTFxInputAlert("????????????????????????", alert1 -> {
                var textField = alert1.textField;
                var str = fileService.rename(target.getId(), textField.getText());
                if (str != null) {
                    target.textProperty().setValue(textField.getText());
                    target.idProperty().setValue(str);
                }
                reaname0(fileTree, oldId, str);
            });
            var textField = alert.textField;
            textField.setText(target.getText());
            // ???????????????????????????????????????
            Platform.runLater(() -> {
                textField.requestFocus();
                textField.selectRange(0, target.getText().length());
            });
            alert.show();
        });
    }

    /**
     * ?????????????????????
     *
     * @param fileTree
     * @param oldId
     * @param str
     */
    private void reaname0(@NotNull TreeItem<Label> fileTree, String oldId, String str) {
        // ??????????????????????????????????????????????????????????????????????????????
        if (fileTree.getChildren().size() > 0) {
            fileTree.getChildren().forEach(f -> {
                if (f.getChildren().size() > 0) {
                    reaname0(f, oldId, str);
                } else {
                    var t = f.getValue();
                    var id = t.getId();
                    assert str != null;
                    var replace = id.replace(oldId, str);
                    t.idProperty().setValue(replace);
                }
            });
        }
    }

    /**
     * ????????????
     *
     * @param fileTree
     * @param fileService
     * @param target
     * @param del
     */
    private void menuDelFunction(TreeItem<Label> fileTree, FileService fileService, Label target, @NotNull MenuItem del) {
        del.setOnAction(e -> {
            // ????????????????????????
            ThreadUtil.execAsync(() -> {
                var bool = fileService.del(target.getId());
                if (bool) {
                    var parent = fileTree.getParent();
                    Platform.runLater(() -> {
                        // ????????????
                        if (parent != null) {
                            fileTree.getParent().getChildren().remove(fileTree);
                        } else CpMessage.sendError("??????: ??????????????????????????????!");
                    });
                } else {
                    CpMessage.sendWarning("??????????????????");
                }
            });
        });
    }

}
