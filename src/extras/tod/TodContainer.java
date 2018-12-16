package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class TodContainer extends Group {

    private static AppController appController;
    private static TodController todController;
    private Fcc initialFcc;

    private LevelContainer mainLevelContainer;

    public TodContainer() {
        setStyle();
    }

    public TodContainer(Fcc initialFcc) {
        this.initialFcc = initialFcc;
        setStyle();

        ArrayList<Analogy> listAnalogy = appController.getDataInterface().getListAnalogyForInitial(initialFcc);

        this.mainLevelContainer = new LevelContainer(appController.getDataInterface().getListAnalogies(initialFcc).get(0));

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

//                prefWidth(2000);
//                minWidth(2000);
            }
        });
    }

    public static void setControllers(AppController appController, TodController todController) {
        TodContainer.appController = appController;
        TodContainer.todController = todController;
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6))));
    }

    public LevelContainer getMainLevelContainer() {
        return mainLevelContainer;
    }

    public void deploy(Analogy initialAnalogy) {
        addRules();
        this.mainLevelContainer = new LevelContainer(initialAnalogy);
        this.getChildren().setAll(this.mainLevelContainer);
        this.mainLevelContainer.deploy();
    }

    private void addRules() {
        Line xAxis = new Line(0,0,400,0);
        Line yAxis = new Line(0,0,0,400);

        xAxis.setStrokeWidth(2);
        yAxis.setStrokeWidth(2);

        this.getChildren().addAll(xAxis,yAxis);

        for(int i=0;i<=400;i+=10){
            Line markX = new Line();
            Line markY = new Line();

            markX.setStartX(i);
            markX.setEndX(i);
            markX.setStartY(-3);
            markX.setEndY(3);

            markY.setStartX(-3);
            markY.setEndX(3);
            markY.setStartY(i);
            markY.setEndY(i);

            markX.setStrokeWidth(1);

            if(i==50||i==100||i==150||i==200||i==250||i==300||i==350||i==400){
                markX.setStrokeWidth(2);
                markX.setStartY(-6);
                markX.setEndY(10);
                markY.setStrokeWidth(2);
                markY.setStartX(-6);
                markY.setEndX(10);
            }

            this.getChildren().addAll(markX,markY);
        }
    }

    private void addBorder(){
        //Line top = new Line()
    }

    public ObservableList<FccContainer> getFccContainers(ObservableList<FccContainer> listFccContainers,LevelContainer levelContainer) {
        ObservableList<FccContainer> tempListFccContainers = FXCollections.observableArrayList();
        tempListFccContainers.addAll(listFccContainers);

        for (AnalogyContainer analogyContainer : levelContainer.getAnalogyContainers()) {
            tempListFccContainers.addAll(analogyContainer.getFccContainers());
            for (MultiContainer multiContainer : analogyContainer.getMultiContainers()) {
                if (multiContainer.isAntecedentDeployed()) {
                    tempListFccContainers.addAll(getFccContainers(tempListFccContainers,multiContainer.getAntecedentsLevelContainer()));
                }
                if (multiContainer.isDescendantDeployed()) {
                    tempListFccContainers.addAll(getFccContainers(tempListFccContainers,multiContainer.getDescendantsLevelContainers()));
                }
            }
        }
        return tempListFccContainers;
    }
    
    public ObservableList<AnalogyContainer> getAnalogyContainers(ObservableList<AnalogyContainer> listAnalogyContainers, LevelContainer levelContainer){
        ObservableList<AnalogyContainer> tempListAnalogyContainers = FXCollections.observableArrayList();
        
        for(AnalogyContainer analogyContainer : levelContainer.getAnalogyContainers()){
            tempListAnalogyContainers.add(analogyContainer);
            
            for(MultiContainer multiContainer:analogyContainer.getMultiContainers()){
                if(multiContainer.isAntecedentDeployed()){
                    tempListAnalogyContainers.addAll(getAnalogyContainers(tempListAnalogyContainers,multiContainer.getAntecedentsLevelContainer()));
                }
                
                // TODO
                // if deductions are deployed...
                
            }
        }
        return tempListAnalogyContainers;
    }

    public ObservableList<LevelContainer> getLevelContainers(ObservableList<LevelContainer> listLevelContainer, LevelContainer levelContainer) {
        ObservableList<LevelContainer> tempListLevelContainers = FXCollections.observableArrayList();

        tempListLevelContainers.addAll(listLevelContainer);
        for (AnalogyContainer analogyContainer : levelContainer.getAnalogyContainers()) {

            for (MultiContainer multiContainer : analogyContainer.getMultiContainers()) {
                // for the Antecedent level and Descendant level
                tempListLevelContainers.addAll(multiContainer.getAntecedentsLevelContainer());

                tempListLevelContainers.addAll(multiContainer.getDescendantsLevelContainers());

            }
        }

        return  tempListLevelContainers;
    }

}
