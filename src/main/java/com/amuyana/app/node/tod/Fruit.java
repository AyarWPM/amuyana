package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.FruitController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.amuyana.app.data.Fcc;

import java.io.IOException;
import java.util.BitSet;

public class Fruit extends VBox {
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();
    private final Fcc fcc;
    private SubBranch subBranch;
    private FruitController fruitController;

    // for loading an existing Tree or
    // for loading a new fcc created
    public Fruit(SubBranch subBranch) {
        this.subBranch = subBranch;
        this.fcc = subBranch.getContainer2().getFcc();
    }

    // For loading a new Tree
    public Fruit(SubBranch subBranch, Fcc fcc){
        this.subBranch = subBranch;
        this.fcc = fcc;
    }

    public void loadFruitSource() {
        getTree().addFruit(this);
        HBox hBox = new HBox(new Group(loadFruit()));
        hBox.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(hBox);
    }
    /**
     *
     * @param fcc The fcc tested descendant of this.fruit
     * @return True if fruit in parameter is descendant of fruit calling the method
     */
    public boolean isDescendant(Fcc fcc) {
        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            Dynamism particular = inclusion.getParticular();
            Dynamism general = inclusion.getGeneral();
            if (fcc.getIdFcc()==particular.getFcc().getIdFcc()) {
                if (this.getFcc().getIdFcc()==general.getFcc().getIdFcc()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Fruit getThis() {
        return this;
    }

    public Fcc getFcc(){
        return this.fcc;
    }

    public Tree getTree() {
        return getTrunk().getTree();
    }

    public Trunk getTrunk() {
        return getBranch().getTrunk();
    }

    /**
     *
     * @return The subBranch (container2) this fruit is in.
     */
    public SubBranch getSubBranch() {
        return this.subBranch;
    }

    /**
     * Get the Branch this Fruit is in, ie where the this.subBranch is
     * @return
     */
    Branch getBranch() {
        return subBranch.getBranch();
    }

    private Node loadFruit() {
        Node fruit=null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FRUIT.getUrl()));
        try {
            fruit = fxmlLoader.load();
            this.fruitController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fruitController.initialize(getThis());
        return fruit;
    }

    public FruitController getFruitController() {
        return fruitController;
    }

    public ObservableList<Fruit> getDescendantFruits() {
        ObservableList<Fruit> descendantFruits = FXCollections.observableArrayList();
        // first child nodes of subBranch (and in later versions of branch) where this.fruit is
        for (Branch rightTrunkBranch: getSubBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                // If that other fruit's fcc is a descendant
                if (isDescendant(subBranch.getFruit().getFcc())) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }

        // second check if it is in a left trunk of another fruit that descends from this.fruit
        for (Fruit fruit1 : getTree().getObservableFruits()) {
            for (Branch leftTrunkBranch: fruit1.getSubBranch().getLeftTrunk().getBranches()) {
                for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                    if(this.equals(fruit1)) continue;
                    if (isDescendant(subBranch.getFruit().getFcc())) {
                        descendantFruits.addAll(subBranch.getFruit());
                    }
                }
            }
        }
        return descendantFruits;
    }

    public ObservableList<Fruit> getAscendantFruits() {
        ObservableList<Fruit> ascendantFruits = FXCollections.observableArrayList();
        // 1. check left Trunk of subBranch
        for (Branch leftTrunkBranch : getSubBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                if (subBranch.getFruit().isDescendant(getFcc())) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }

        // 2. check if it is in a right Trunk of another fruit and if isDescendant
        for (Fruit fruit1 : getTree().getObservableFruits()) {
            for (Branch rightTrunkBranch : fruit1.getSubBranch().getRightTrunk().getBranches()) {
                for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                    if(this.equals(fruit1)) continue;
                    if (subBranch.getFruit().isDescendant(getFcc())) {
                        ascendantFruits.addAll(subBranch.getFruit());
                    }
                }
            }
        }
        return ascendantFruits;
    }
}