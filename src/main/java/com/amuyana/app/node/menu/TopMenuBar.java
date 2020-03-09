package com.amuyana.app.node.menu;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class TopMenuBar extends MenuBar {
    private NodeInterface nodeInterface;
    private DataInterface dataInterface;

    private Menu fileMenu;
    private Menu logicSystemMenu;
    private Menu todMenu;
    private Menu dialecticsMenu;
    private Menu statisticsMenu;
    private Menu tutorialsMenu;
    private Menu helpMenu;

    private ObservableList<LogicSystemMenu> logicSystemMenus;
    private ObservableList<TodMenu> todMenus;

    public TopMenuBar(NodeInterface nodeInterface) {
        this.nodeInterface = nodeInterface;
        this.dataInterface = NodeHandler.getDataInterface();
        initialize();
        setInitialValues();
    }

    private void setInitialValues() {
        this.logicSystemMenu.setDisable(true);
        this.todMenu.setDisable(true);
        //this.dialecticsMenu.setDisable(true);
        //this.statisticsMenu.setDisable(true);
    }

    private void initialize() {
        this.fileMenu = new Menu("Amuyaña");
        this.logicSystemMenu = new Menu("Logic System");
        this.todMenu = new Menu("Table of Deductions");
        initializeFileMenu();
        initializeLogicSystemMenu();
        initializeTodMenu();
        //initializeDialecticsMenu();
        //initializeStatisticsMenu();
        initializeTutorialsMenu();
        initializeHelpMenu();
        getMenus().addAll(fileMenu,logicSystemMenu,todMenu, helpMenu);
        //getMenus().addAll(fileMenu,logicSystemMenu,todMenu,dialecticsMenu,statisticsMenu,tutorialsMenu,helpMenu);
    }

    public void logicSystemIsLoaded(boolean isLoaded) {
        if (isLoaded) {
            todMenu.setDisable(false);
            //dialecticsMenu.setDisable(false);
            //statisticsMenu.setDisable(false);
        } else {
            todMenu.setDisable(true);
            //dialecticsMenu.setDisable(true);
            //statisticsMenu.setDisable(true);
        }
    }

    private void initializeFileMenu() {
        MenuItem connexionMenuItem = new MenuItem("Connexion");
        connexionMenuItem.setOnAction(actionEvent -> openConnectionTab());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> nodeInterface.exitAmuyana());
        fileMenu.getItems().addAll(connexionMenuItem,exitMenuItem);
    }

    private void initializeLogicSystemMenu() {
        logicSystemMenus = FXCollections.observableArrayList();
        MenuItem newLogicSystemMenuItem = new MenuItem("New");
        newLogicSystemMenuItem.setOnAction(actionEvent -> nodeInterface.openLogicSystemTab());
        logicSystemMenu.getItems().addAll(newLogicSystemMenuItem,new SeparatorMenuItem());
    }

    private void initializeTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        MenuItem newTodMenuItem = new MenuItem("New");
        newTodMenuItem.setOnAction(actionEvent -> nodeInterface.newTodTab());
        MenuItem editFccsMenuItem = new MenuItem("FCCs viewer");
        editFccsMenuItem.setOnAction(actionEvent -> nodeInterface.openFccTableTab());

        this.todMenu.getItems().addAll(newTodMenuItem,editFccsMenuItem,new SeparatorMenuItem());
    }

    private void initializeDialecticsMenu() {
        this.dialecticsMenu = new Menu("Dialectics");
    }

    private void initializeStatisticsMenu() {
        this.statisticsMenu = new Menu("Statistics");
    }

    private void initializeTutorialsMenu() {
        this.tutorialsMenu = new Menu("Tutorials");
    }

    private void initializeHelpMenu() {
        this.helpMenu = new Menu("Help");
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(actionEvent -> {
            nodeInterface.openAboutWindow();
        });
        this.helpMenu.getItems().addAll(aboutMenuItem);
    }

    private void openConnectionTab() {
        nodeInterface.openConnectionTab();
    }

    // LOGIC SYSTEM
    public void removeLogicSystemMenu(LogicSystem logicSystem) {
        LogicSystemMenu toRemove = null;
        for (LogicSystemMenu logicSystemMenu : this.logicSystemMenus) {
            if (logicSystem.equals(logicSystemMenu.getLogicSystem())) {
                toRemove=logicSystemMenu;
            }
        }
        this.logicSystemMenu.getItems().remove(toRemove);
        this.logicSystemMenus.remove(toRemove);
    }

    public void addMenu(LogicSystem logicSystem) {
        LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
        logicSystemMenu.textProperty().bind(logicSystem.labelProperty());
        this.logicSystemMenu.getItems().add(logicSystemMenu);
        this.logicSystemMenus.add(logicSystemMenu);
    }

    public void addMenu(Tod tod) {
        TodMenu todMenu = new TodMenu(tod, nodeInterface);
        todMenu.textProperty().bind(tod.labelProperty());
        this.todMenu.getItems().add(todMenu);
        this.todMenus.add(todMenu);
    }

    public void updateTodMenu() {
        clearTodMenu();
        fillTodMenu();
    }

    private void clearTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        ObservableList<TodMenu> tempToRemove = FXCollections.observableArrayList();
        for (MenuItem menuItem : this.todMenu.getItems()) {
            if (menuItem.getClass().equals(TodMenu.class)) {
                tempToRemove.add((TodMenu)menuItem);
            }
        }
        todMenu.getItems().removeAll(tempToRemove);
    }

    private void fillTodMenu() {
        for (Tod tod : dataInterface.getTods(nodeInterface.getLogicSystem())) {
            TodMenu todMenu = new TodMenu(tod, nodeInterface);
            todMenu.textProperty().bind(tod.labelProperty());
            this.todMenus.add(todMenu);
        }
        for (TodMenu todMenu : todMenus) {
            this.todMenu.getItems().add(todMenu);
        }
    }

    private void clearLogicSystemMenu() {
        this.logicSystemMenus = FXCollections.observableArrayList();
        ObservableList<LogicSystemMenu> tempToRemove = FXCollections.observableArrayList();
        for (MenuItem menuItem : this.logicSystemMenu.getItems()) {
            if (menuItem.getClass().equals(LogicSystemMenu.class)) {
                tempToRemove.add((LogicSystemMenu) menuItem);
            }
        }
        this.logicSystemMenu.getItems().removeAll(tempToRemove);
    }

    private void fillLogicSystemMenu() {
        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
            this.logicSystemMenu.getItems().add(logicSystemMenu);
        }
    }

    public void resetMenus() {
        // Logic System
        this.logicSystemMenu.setDisable(false);
        clearLogicSystemMenu();
        fillLogicSystemMenu();
        // Tod
        todMenu.setDisable(true);
    }
}
