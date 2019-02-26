package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * Equivalent to tod in data package: this.todController.getTod();
 */
public class Tree extends Group {
    private DataInterface dataInterface;
    private DoubleProperty viewScale;
    // General
    private TodController todController;

    // Layout
    private Trunk mainTrunk; //Container0
    private DoubleProperty maxLevel;

    //private ObservableList<Fruit> fruits;
    private ObservableList<Tie> ties;

    private Group linesGroup;

    // constructor to loadExistingTree an existing Tree
    public Tree(TodController todController) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
        bindScale();
    }

    public Tree(TodController todController, Conjunction conjunction) {

    }
    public Tree(TodController todController, CClass cClass) {

    }

    private void initializeAndBind() {
        this.dataInterface = MainBorderPane.getDataInterface();
        this.linesGroup = new Group();

        this.ties = FXCollections.observableArrayList();

        this.viewScale = new SimpleDoubleProperty();
        this.maxLevel = new SimpleDoubleProperty();

        Slider slider = todController.getScaleSlider();
        viewScale.bind(slider.valueProperty());
    }

    public void loadExistingTree() {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().setAll(this.linesGroup, this.mainTrunk);
        this.mainTrunk.loadBranches();
    }

    public void loadNewTree() {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().setAll(this.linesGroup, this.mainTrunk);
        Fcc newFcc = MainBorderPane.getDataInterface().newFcc(todController.getTod().getLogicSystem());
        this.mainTrunk.loadNewBranch(newFcc);
        todController.openFccEditor(newFcc);
    }

    public void loadNewTreeFromExistingFcc(Fcc fcc) {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().setAll(this.linesGroup, this.mainTrunk);
        this.mainTrunk.loadNewBranch(fcc);
        todController.openFccEditor(fcc);
    }

    private void bindScale() {
        scaleXProperty().bind(viewScale);
        scaleYProperty().bind(viewScale);
    }

    void updateMaxLevel(double level) {
        if (level > maxLevel.getValue()) {
            setMaxLevel(level);
        }
    }

    public void clearTies() {
        ties.clear();
        linesGroup.getChildren().clear();
        getChildren().setAll(linesGroup,mainTrunk);
    }

    public void addTie(Tie tie) {
        System.out.println("Tree.addTie ("+tie+")");
        ties.add(tie);
        for (Line line : tie.getLines()) {
            linesGroup.getChildren().addAll(line);
        }
    }

    public void remove(Tie tie) {
        ties.remove(tie);
        linesGroup.getChildren().removeAll(tie.getLines());
    }

    public ObservableList<Fruit> getObservableFruits() {
        ObservableList<Fruit> fruits = FXCollections.observableArrayList();
        for (Branch branch : mainTrunk.getBranches()) {
            fruits.addAll(branch.getFruits());
        }
        return fruits;
    }

    public TodController getTodController() {
        return this.todController;
    }

    public double getViewScale() {
        return viewScale.get();
    }

    public DoubleProperty viewScaleProperty() {
        return viewScale;
    }

    public void setViewScale(double viewScale) {
        this.viewScale.set(viewScale);
    }

    public Trunk getMainTrunk() {
        return this.mainTrunk;
    }

    public double getMaxLevel() {
        return maxLevel.get();
    }

    public DoubleProperty maxLevelProperty() {
        return maxLevel;
    }

    private void setMaxLevel(double maxLevel) {
        this.maxLevel.set(maxLevel);
    }

    public ObservableList<Tie> getTies() {
        return this.ties;
    }

    // removes the fruit in both the fruits field and observableList of fruits in tree nodes
    // this is activated by an inclusions listener in Tie, we can invoke it again by removing all inclusions to child nodes

    public ObservableList<Fruit> remove(ObservableList<Fruit> fruitsToRemove, Fruit fruit) {
        fruitsToRemove.addAll(fruit);
        // removing inclusions to fruits in subBranches of Branches in left and right trunks of fruit's subBranch
        ObservableList<Branch> leftBranches  = fruit.getSubBranch().getLeftTrunk().getBranches();
        ObservableList<Branch> rightBranches  = fruit.getSubBranch().getRightTrunk().getBranches();

        Dynamism thisPositiveDynamism = dataInterface.getDynamism(fruit.getFcc(),0);
        Dynamism thisNegativeDynamism = dataInterface.getDynamism(fruit.getFcc(),1);
        Dynamism thisSymmetricDynamism = dataInterface.getDynamism(fruit.getFcc(),2);

        // Branches in leftTrunk
        if (!leftBranches.isEmpty()) {
            for (Branch branch1 : leftBranches) {
                for (SubBranch subBranch1 : branch1.getSubBranches()) {
                    remove(fruitsToRemove,subBranch1.getFruit());

                    // Delete inclusions
                    ObservableList<Inclusion> inclusionsToRemove = FXCollections.observableArrayList();
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        Dynamism particular = inclusion.getParticular();
                        Dynamism general = inclusion.getGeneral();

                        Dynamism positiveAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),0);
                        Dynamism negativeAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),1);
                        Dynamism symmetricAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),2);

                        if (particular.getIdDynamism() == thisPositiveDynamism.getIdDynamism()||
                                particular.getIdDynamism() == thisNegativeDynamism.getIdDynamism()||
                                particular.getIdDynamism() == thisSymmetricDynamism.getIdDynamism()) {
                            if (general.getIdDynamism() == positiveAscendant.getIdDynamism() ||
                                    general.getIdDynamism() == negativeAscendant.getIdDynamism() ||
                                    general.getIdDynamism() == symmetricAscendant.getIdDynamism()) {
                                inclusionsToRemove.addAll(inclusion);
                            }
                        }
                    }
                    for (Inclusion inclusion1 : inclusionsToRemove) {
                        dataInterface.delete(inclusion1);
                    }

                    // Remove tie
                    Tie tieToRemove=null;
                    boolean tieBoolean = false;
                    for (Tie tie : getTies()) {
                        if (tie.getAscendantFruit().equals(subBranch1.getFruit()) &&
                                tie.getDescendantFruit().equals(fruit)) {
                            tieToRemove=tie;
                            tieBoolean = true;
                        }
                    }
                    if(tieBoolean) remove(tieToRemove);
                }
            }
        }
        // Branches in rightTrunk : fruit (in param) is ascendant and subBranch1.getFruit() is descendant
        if (!rightBranches.isEmpty()) {
            for (Branch branch1 : rightBranches) {
                for (SubBranch subBranch1 : branch1.getSubBranches()) {
                    remove(fruitsToRemove,subBranch1.getFruit());

                    // Delete inclusions
                    ObservableList<Inclusion> inclusionsToRemove = FXCollections.observableArrayList();
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        Dynamism particular = inclusion.getParticular();
                        Dynamism general = inclusion.getGeneral();

                        Dynamism positiveDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),0);
                        Dynamism negativeDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),1);
                        Dynamism symmetricDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),2);

                        if (general.getIdDynamism() == thisPositiveDynamism.getIdDynamism()||
                                general.getIdDynamism() == thisNegativeDynamism.getIdDynamism()||
                                general.getIdDynamism() == thisSymmetricDynamism.getIdDynamism()) {
                            if (particular.getIdDynamism() == positiveDescendant.getIdDynamism() ||
                                    particular.getIdDynamism() == negativeDescendant.getIdDynamism() ||
                                    particular.getIdDynamism() == symmetricDescendant.getIdDynamism()) {
                                inclusionsToRemove.addAll(inclusion);
                            }
                        }
                    }
                    for (Inclusion inclusion1 : inclusionsToRemove) {
                        dataInterface.delete(inclusion1);
                    }

                    // Remove tie
                    Tie tieToRemove=null;
                    boolean tieBoolean = false;
                    for (Tie tie : getTies()) {
                        if (tie.getAscendantFruit().equals(fruit) &&
                                tie.getDescendantFruit().equals(subBranch1.getFruit())) {
                            tieToRemove=tie;
                            tieBoolean = true;
                        }
                    }
                    if(tieBoolean) remove(tieToRemove);
                }
            }
        }
        fruit.getTrunk().remove(fruit.getSubBranch());
        return fruitsToRemove;
    }

    @Deprecated
    public void buildLines() {
        for (Tie tie : getTies()) {
                tie.buildLines();
        }
        getChildren().setAll(this.linesGroup, this.mainTrunk);
    }

    public void update() {
        clearTies();
        updateOrientationTies();
        checkFruitsRemoval();
        updateFruitsMenus();
        buildTies();
        //todController.getScaleSlider().setValue(1);
    }

    public void updateOrientationTies() {
        for (Tie tie : getTies()) {
            tie.setOrientations();
        }
    }

    public void checkFruitsRemoval() {
    // As the changes are produced one by one we'll find a tie with all orientations false (and no more than 1)
        Tie tieToRemove = null;
        boolean tieBoolean = false;
        Fruit fruitToRemove = null;
        for (Tie tie : ties) {
            if (!tie.getPositiveOrientation() && !tie.getNegativeOrientation() && !tie.getSymmetricOrientation()) {
                tieToRemove=tie;
                tieBoolean = true;
                if (tie.getAscendantFruit().isChild(tie.getDescendantFruit())) {
                    //Remove descendantFruit as it is child
                    fruitToRemove=tie.getDescendantFruit();
                } else if (tie.getDescendantFruit().isChild(tie.getAscendantFruit())) {
                    //Remove ascendantFruit as it is child
                    fruitToRemove=tie.getAscendantFruit();
                }
            }
        }
        if (tieBoolean) {
            remove(FXCollections.observableArrayList(),fruitToRemove);
            remove(tieToRemove);
        }
    }

    public void updateFruitsMenus() {
        for (Fruit fruit : getObservableFruits()) {
            fruit.getFruitController().buildMenus();
        }
    }

    public void buildTies() {
        ties.clear();
        linesGroup.getChildren().clear();
        for (Fruit fruit : getObservableFruits()) {
            fruit.getFruitController().buildTies();
        }
    }
}