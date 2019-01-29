package com.amuyana.app.controllers;

import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.Message;
import com.amuyana.app.node.content.LogicSystemContentTab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.amuyana.app.data.*;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.data.tod.containers.Container2;
import com.amuyana.app.data.tod.containers.Tod;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LogicSystemController implements Initializable {

    // COMPONENTES GUI
    private NodeInterface nodeInterface;

    @FXML private TextField labelTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private Label idLabel;
    @FXML private Label creationDateLabel;

    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button deleteButton;

    private LogicSystem logicSystem;

    private boolean isLogicSystemNew;
    private LogicSystemContentTab logicSystemTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.isLogicSystemNew=true;
    }

    public void setTab(LogicSystemContentTab logicSystemContentTab) {
        this.logicSystemTab = logicSystemContentTab;
    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface=nodeInterface;
    }

    public boolean isLogicSystemNew() {
        return isLogicSystemNew;
    }

    public void setLogicSystemNew(boolean logicSystemNew) {
        this.isLogicSystemNew = logicSystemNew;
        if (logicSystemNew) {
            loadButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            loadButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public void fillData(LogicSystem logicSystem){
        setLogicSystemNew(false);
        this.logicSystem = logicSystem;
        labelTextField.setText(logicSystem.getLabel());
        descriptionTextArea.setText(logicSystem.getDescription());
        idLabel.setText(Integer.toString(logicSystem.getIdLogicSystem()));
        creationDateLabel.setText(logicSystem.getCreationDate().toString());
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }
    /**
     * Method for both when a logic system exists and does not exist yet...
     */
    @FXML
    public void save(){
        //first check that all fields are filled
        if(labelTextField.getText().isEmpty()|descriptionTextArea.getText().isEmpty()){
            Message.completeAllFieldsAlert();
            return;
        }
        // Second, Is new logic system or not?
        // If the logic system is new
        if (this.isLogicSystemNew) {
            saveData();
        }
        // If the logic system exists already
        else {
            updateData();
        }
    }

    private void saveData() {
        // LogicSystem
        this.logicSystem = MainBorderPane.getDataInterface().newLogicSystem(labelTextField.getText(),descriptionTextArea.getText());

        logicSystemTab.textProperty().bind(logicSystem.labelProperty());
        idLabel.setText(String.valueOf(logicSystem.getIdLogicSystem()));
        creationDateLabel.setText(String.valueOf(logicSystem.getCreationDate()));

        this.isLogicSystemNew=false;
        loadButton.setDisable(false);
        deleteButton.setDisable(false);
        nodeInterface.addToLogicSystemMenu(logicSystem);

        Message.createdLogicSystemAlert();
    }

    private void updateData() {
        logicSystem.labelProperty().setValue(labelTextField.getText());
        logicSystem.descriptionProperty().setValue(descriptionTextArea.getText());
        logicSystemTab.textProperty().bind(logicSystem.labelProperty());
        MainBorderPane.getDataInterface().update(logicSystem);
        Message.updatedLogicSystemAlert();
    }

    @FXML
    void load() {
        nodeInterface.load(logicSystem);
    }
    
    @FXML
    public void delete(){
        nodeInterface.delete(logicSystem);
    }

    private void showCompleteFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Logic System");
        alert.setHeaderText(null);
        alert.setContentText("Please complete all fields.");
        alert.showAndWait();
    }
}