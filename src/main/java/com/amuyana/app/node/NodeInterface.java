package com.amuyana.app.node;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Log;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.menu.TopMenuBar;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;

public interface NodeInterface {
    TopMenuBar getTopMenuBar();

    Node getRootNode();
    LogicSystem getLogicSystem();
    void exitAmuyana();
    void openConnectionTab();

    void openLogicSystemTab();
    void addToLogicSystemMenu(LogicSystem logicSystem);
    void load(LogicSystem logicSystem);
    void edit(LogicSystem logicSystem);
    void delete(LogicSystem logicSystem);

    void newTodTab();
    void open(Tod tod);
    void delete(Tod tod);
    void duplicate(Tod tod);

    void openAboutWindow();

    void openDebug1();
}
