
package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import static extras.tod.FccContainer.FccType.NORMAL;
import static extras.tod.FormulaContainer.Styles.SIMPLE;

public class FccContainer extends TitledPane {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;
    private final FormulaContainer positiveFormula;
    private final FormulaContainer negativeFormula;
    private final FormulaContainer symmetricFormula;

    private FccType type;

    private ContextMenu menu;

    enum FccType{NORMAL, MIRROR}

    public static void setControllers(AppController appController, TodController todController) {
        FccContainer.appController = appController;
        FccContainer.todController = todController;
    }

    public void setType(FccType type){
        this.type = type;
    }

    public FccContainer(Fcc fcc){
        this.fcc = fcc;

        this.positiveFormula = new FormulaContainer();
        this.negativeFormula = new FormulaContainer();
        this.symmetricFormula = new FormulaContainer();

        this.positiveFormula.setDynamism(appController.dynamismOf(0, fcc));
        this.negativeFormula.setDynamism(appController.dynamismOf(1, fcc));
        this.symmetricFormula.setDynamism(appController.dynamismOf(2, fcc));

        this.setText(fcc.getLabel());

        setStyle();
    }

    private void setMenu(FccType type){
        this.menu = new ContextMenu();

        MenuItem muimTurnToFront = new MenuItem("Turn to this");
        
        muimTurnToFront.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MultiContainer multiContainer = (MultiContainer)getParent().getParent();
                AnalogyContainer analogyContainer = (AnalogyContainer)multiContainer.getParent();
                analogyContainer.turnToFront(multiContainer);
            }
        });

        switch(type){
            case NORMAL:{
                Menu deployMenu = new Menu("Deploy");
                CheckMenuItem inclusion = new CheckMenuItem("Deploy inclusions");
                CheckMenuItem positiveDeductions= new CheckMenuItem("Deploy positive deductions");
                CheckMenuItem negativeDeductions= new CheckMenuItem("Deploy negative deductions");
                CheckMenuItem symmetricDeductions = new CheckMenuItem("Deploy symmetric deductions");

                deployMenu.getItems().addAll(inclusion, positiveDeductions, negativeDeductions, symmetricDeductions);
                this.menu.getItems().addAll(muimTurnToFront,deployMenu);

                break;
            }
            case MIRROR:{
                MenuItem drawHere = new MenuItem("Draw here");
                this.menu.getItems().addAll(muimTurnToFront,drawHere);
                break;
            }
            default: break;
        }

        this.setContextMenu(menu);
    }

    private void setStyle(){
        this.setCollapsible(false);
    }

    void deploy(){

        if(this.type==NORMAL){
            VBox vBox = new VBox();
            vBox.getChildren().addAll(this.positiveFormula,this.negativeFormula,this.symmetricFormula);
            vBox.setSpacing(3);
            this.setContent(vBox);
            this.positiveFormula.write(SIMPLE);
            this.negativeFormula.write(SIMPLE);
            this.symmetricFormula.write(SIMPLE);

        } else if(this.type==FccType.MIRROR){
            this.setContent(new Label("The FCC has been\ndrawn somewhere else..."));
        }

        setMenu(this.type);
    }
}
