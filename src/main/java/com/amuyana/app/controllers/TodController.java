package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.containers.Tod;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.FccEditorTab;
import com.amuyana.app.node.content.RightPanelTab;
import com.amuyana.app.node.content.TodContentTab;
import com.amuyana.app.node.tod.Tree;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;

public class TodController implements Initializable {
    @FXML private SplitPane splitPane;
    @FXML private Group canvas;
    @FXML private Slider scaleSlider;

    @FXML private ScrollPane treeScrollPane;

    @FXML private Label todNameLabel;
    @FXML private Label logicSystemNameLabel;
    @FXML private TextField todNameTextField;
    @FXML private ListView<Fcc> fccsInTodListView;
    @FXML private ScrollPane leftPanel;
    @FXML private Button toggleTodNameButton;

    @FXML private Button toggleLeftPanelButton;
    @FXML private TabPane rightTabPane;

    private NodeInterface nodeInterface;
    private DataInterface dataInterface;
    private Tod tod;

    private TodContentTab todContentTab;
    private Tree tree;
    private BooleanProperty editName;
    private BooleanProperty leftPanelOpen;
    private BooleanProperty rightPanelOpen;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dataInterface = MainBorderPane.getDataInterface();
        editName = new SimpleBooleanProperty();
        leftPanelOpen = new SimpleBooleanProperty(true);
        rightPanelOpen = new SimpleBooleanProperty(true);

        toggleRightTabPane();
        manageEvents();
    }

    public void setTab(TodContentTab todContentTab) {
        this.todContentTab = todContentTab;
    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface=nodeInterface;
    }

    public void newTod(LogicSystem logicSystem) {
        // get the maximum id of tables of deductions and add 1
        int currentValue=0;
        for (Tod tod : dataInterface.getTods(logicSystem)) {
            if (currentValue < tod.getIdTod()) {
                currentValue = tod.getIdTod();
            }
        }
        currentValue+=1;

        this.tod = this.dataInterface.newTod("New Table of deductions " + currentValue,logicSystem);

        this.canvas.getChildren().setAll(loadFccSelector());
        bindProperties();
    }

    public void openTod(Tod tod) {
        this.tod = tod;
        bindProperties();

        if (dataInterface.getFccs(tod).isEmpty()) {
            Node node = loadFccSelector();
            this.canvas.getChildren().setAll(node);
        } else if (!dataInterface.getFccs(tod).isEmpty()) {
            showTree();
        }
    }

    private void bindProperties() {
        todNameLabel.textProperty().bind(todNameTextField.textProperty());
        todNameLabel.visibleProperty().bind(editName.not());
        todNameLabel.managedProperty().bind(editName.not());

        todNameTextField.textProperty().bindBidirectional(tod.labelProperty());
        todNameTextField.visibleProperty().bind(editName);
        todNameTextField.managedProperty().bind(editName);
    }

    public void fillData() {
        logicSystemNameLabel.textProperty().bind(nodeInterface.getLogicSystem().labelProperty());
        fccsInTodListView.setItems(dataInterface.getFccs(this.tod));

    }

    private void manageEvents() {
        rightTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    if (change.getList().size() == 1) {
                        setRightPanelOpen(false);
                    }
                }
            }
        });

        treeScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            event.consume();
            manageScrollEvent(event);
        });

    }

    private void manageScrollEvent(ScrollEvent scrollEvent) {
        if(scrollEvent.getDeltaY()==0) return;
        if(scrollEvent.isShiftDown()) return;

        int factor = (int) (scrollEvent.getDeltaY()/scrollEvent.getMultiplierY()); // 40/40 = +/-1
        if (factor == 1) {
            scaleSlider.setValue(scaleSlider.getValue()+1);
        } else if (factor == -1) {
            scaleSlider.setValue(scaleSlider.getValue()-1);
        }
        double deltaX = scrollEvent.getX()/treeScrollPane.getWidth()-0.5;
        double deltaY = scrollEvent.getY()/treeScrollPane.getHeight()-0.5;

        treeScrollPane.setHvalue((treeScrollPane.getHvalue()+1)*0.3+treeScrollPane.getHvalue()*deltaX*50);
        treeScrollPane.setVvalue((treeScrollPane.getVvalue()+1)*0.3+treeScrollPane.getVvalue()*deltaY*40);
    }

    @FXML
    private void toggleTodName() {
        if (isEditName()) {
            // Save
            setEditName(false);
            dataInterface.update(tod);
            toggleTodNameButton.setText("Edit");
        } else {
            setEditName(true);
            toggleTodNameButton.setText("Save");

        }
    }

    @FXML
    private void toggleLeftPanel() {
        if (getLeftPanelOpen()) {
            setLeftPanelOpen(false);
            toggleLeftPanelButton.setText("\u2192");
            splitPane.getItems().remove(leftPanel);
        } else {
            setLeftPanelOpen(true);
            toggleLeftPanelButton.setText("\u2190");
            splitPane.getItems().add(0, leftPanel);
        }
    }

    @FXML
    private void editTod() {

    }

    private TodController getThisTodController() {
        return this;
    }

    private void toggleRightTabPane() {
        if (getRightPanelOpen()) {
            setRightPanelOpen(false);
            splitPane.getItems().remove(rightTabPane);
        } else {
            splitPane.getItems().add(rightTabPane);
            setRightPanelOpen(true);
        }
    }

    void closeTab(RightPanelTab rightPanelTab) {
        rightTabPane.getTabs().remove(rightPanelTab);
        if (rightTabPane.getTabs().isEmpty()) {
            toggleRightTabPane();
        }
    }


    EventHandler<ActionEvent> openFccEditorEventHandler(Fcc fcc) {
        return actionEvent -> openFccEditor(fcc);
    }

    public void openFccEditor(Fcc fcc) {
        for (Tab tab : rightTabPane.getTabs()) {
            if (tab.getClass().equals(FccEditorTab.class)) {
                FccEditorTab fccEditorTab = (FccEditorTab)tab;
                if(fccEditorTab.getFccEditorController().getFcc().equals(fcc)) {
                    rightTabPane.getSelectionModel().select(fccEditorTab);
                    return;
                }
            }
        }
        toggleRightTabPane();
        FccEditorTab fccEditorTab =new FccEditorTab(getThisTodController(),nodeInterface,fcc);
        rightTabPane.getTabs().add(fccEditorTab);
        rightTabPane.getSelectionModel().select(fccEditorTab);
    }

    private void showScaleSlider() {
        scaleSlider.setMin(1);
        scaleSlider.maxProperty().bind(tree.maxLevelProperty());
    }

    private Node loadFccSelector() {
        Node node = null;
        FccSelectorController fccSelectorController=null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FCCSELECTOR.getUrl()));
        try {
            node = fxmlLoader.load();
            fccSelectorController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(fccSelectorController).initialize(this.nodeInterface,this.todContentTab);
        return node;
    }

    public ScrollPane getTreeScrollPane() {
        return treeScrollPane;
    }


    public Tod getTod() {
        return this.tod;
    }

    public Tree getTree() {
        return this.tree;
    }

    public Slider getScaleSlider() {
        return this.scaleSlider;
    }

    /*
                  _____
                 |_   _| __ ___  ___
                   | || '__/ _ \/ _ \
                   | || | |  __/  __/
                   |_||_|  \___|\___|

         */
    private void showTree() {
        this.tree = new Tree(this);
        this.tree.loadExistingTree();
        canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        showScaleSlider();
    }


    void showNewTree() {
        this.tree = new Tree(this);
        this.tree.loadNewTree();
        canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        showScaleSlider();
    }


    void showNewTree(Fcc fcc) {
        this.tree = new Tree(this);
        this.tree.loadNewTreeFromExistingFcc(fcc);
        canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        showScaleSlider();
    }

    void showNewTree(Conjunction conjunction) {

    }

    void showNewTree(CClass cClass) {

    }

    Group getCanvas() {
        return this.canvas;
    }

    private boolean isEditName() {
        return editName.get();
    }

    public BooleanProperty editNameProperty() {
        return editName;
    }

    private void setEditName(boolean editName) {
        this.editName.set(editName);
    }

    private boolean getLeftPanelOpen() {
        return leftPanelOpen.get();
    }

    public BooleanProperty leftPanelOpenProperty() {
        return leftPanelOpen;
    }

    private void setLeftPanelOpen(boolean leftPanelOpen) {
        this.leftPanelOpen.set(leftPanelOpen);
    }

    private boolean getRightPanelOpen() {
        return rightPanelOpen.get();
    }

    public BooleanProperty rightPanelOpenProperty() {
        return rightPanelOpen;
    }

    private void setRightPanelOpen(boolean rightPanelOpen) {
        this.rightPanelOpen.set(rightPanelOpen);
    }
}
