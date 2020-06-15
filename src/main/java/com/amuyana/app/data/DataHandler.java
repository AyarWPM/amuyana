package com.amuyana.app.data;

import com.amuyana.app.data.tod.*;
import com.amuyana.app.data.tod.containers.*;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class DataHandler implements DataInterface {
    // LISTS
    private ObservableList<User> listUser;
    private ObservableList<LogicSystem> listLogicSystem;
    private ObservableList<FccHasLogicSystem> listFccHasLogicSystem;
    private ObservableList<Fcc> listFcc;
    private ObservableList<Tod> listTod;
    private ObservableList<Container0> listContainer0s;
    private ObservableList<Container1> listContainer1s;
    private ObservableList<Container2> listContainer2s;
    private ObservableList<Container0In1> listContainer0In1s;
    private ObservableList<Container0In2> listContainer0In2s;
    private ObservableList<ConjunctionHasContainer1> listConjunctionHasContainer1s; //
    private ObservableList<CClassHasContainer1> listcClassHasContainer1s; //
    private ObservableList<CClass> listCClass;
    private ObservableList<CClassHasInclusion> listCClassHasInclusion; //
    private ObservableList<Conjunction> listConjunction;
    private ObservableList<ConjunctionHasInclusion> listConjunctionHasInclusions; //
    private ObservableList<Element> listElements;
    private ObservableList<Dynamism> listDynamisms; //
    private ObservableList<Inclusion> listInclusions; //
    private ObservableList<Syllogism> listSyllogisms;
    private ObservableList<SyllogismHasTod> listSyllogismHasTod;
    private ObservableList<InclusionHasSyllogism> listInclusionHasSyllogisms;
    private ObservableList<Register> listRegister;
    private ObservableList<RegisterHasSyllogism> listRegisterHasSyllogism;


    private DataConnection dataConnection;
    private static NodeInterface nodeInterface;

    public DataHandler() {
        this.dataConnection = new DataConnection();
    }

    private void initLists() {
        this.listContainer1s = FXCollections.observableArrayList();
        this.listContainer2s = FXCollections.observableArrayList();
        this.listContainer0In1s = FXCollections.observableArrayList();
        this.listContainer0In2s = FXCollections.observableArrayList();
        this.listConjunctionHasContainer1s = FXCollections.observableArrayList();
        this.listcClassHasContainer1s = FXCollections.observableArrayList();
        this.listConjunction = FXCollections.observableArrayList();
        this.listConjunctionHasInclusions = FXCollections.observableArrayList();
        this.listLogicSystem = FXCollections.observableArrayList();
        this.listFcc = FXCollections.observableArrayList();
        this.listFccHasLogicSystem = FXCollections.observableArrayList();
        this.listTod = FXCollections.observableArrayList();
        this.listElements = FXCollections.observableArrayList();
        this.listDynamisms = FXCollections.observableArrayList();
        this.listUser = FXCollections.observableArrayList();
        this.listCClass = FXCollections.observableArrayList();
        this.listCClassHasInclusion = FXCollections.observableArrayList();
        this.listInclusions = FXCollections.observableArrayList();
        this.listSyllogisms = FXCollections.observableArrayList();
        this.listSyllogismHasTod = FXCollections.observableArrayList();
        this.listInclusionHasSyllogisms = FXCollections.observableArrayList();
        this.listContainer0s = FXCollections.observableArrayList();
        this.listRegister = FXCollections.observableArrayList();
        this.listRegisterHasSyllogism = FXCollections.observableArrayList();
    }

    /**
     * For prototype there's only one notationType of user, however users might need permissions.
     * @param url host address or name of the MYSQL database
     * @param username username for accessing the MYSQL database
     * @param password password of user with username
     */
    public void setDataConnectionValues(String url, String username, String password) {
        DataConnection.setValues(url,username,password);
    }

    @Override
    public DataConnection getDataConnection() {
        return dataConnection;
    }

    @Override
    public boolean testConnection() {
        boolean connection = false;
        if (this.dataConnection.connect()) {
            connection = true;
        }
        return connection;
    }

    @Deprecated
    @Override
    public void reinitializeDatabase() {
        Path path = Paths.get("./src/main/resources/mysql/reinitializeScript.txt");
        File file = new File(path.toUri());

        FileInputStream fileInputStream;
        StringBuilder str = new StringBuilder();

        try {
            fileInputStream = new FileInputStream(file);

            int content;
            while ((content = fileInputStream.read()) != -1) {
                // convert to char and display it
                str.append((char) content);
            }

            Statement statement = this.dataConnection.getConnection().createStatement();
            statement.executeUpdate(str.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        initLists();
        LogicSystem.loadList(dataConnection.getConnection(), this.listLogicSystem);
        Fcc.loadList(dataConnection.getConnection(), this.listFcc);
        FccHasLogicSystem.loadList(this.dataConnection.getConnection(),this.listFccHasLogicSystem,this.listFcc,this.listLogicSystem);
        Container0.loadList(this.dataConnection.getConnection(),this.listContainer0s);
        Container1.loadList(this.dataConnection.getConnection(), this.listContainer1s,this.listContainer0s);
        Container2.loadList(this.dataConnection.getConnection(),this.listContainer2s,this.listFcc,listContainer1s);
        Container0In1.loadList(this.dataConnection.getConnection(),this.listContainer0In1s,this.listContainer0s,this.listContainer1s);
        Container0In2.loadList(this.dataConnection.getConnection(),this.listContainer0In2s,this.listContainer0s,this.listContainer2s);
        ConjunctionHasContainer1.loadList(this.dataConnection.getConnection(),this.listConjunctionHasContainer1s,
                this.listConjunction,this.listContainer1s);
        CClassHasContainer1.loadList(this.dataConnection.getConnection(),this.listcClassHasContainer1s,this.listCClass,this.listContainer1s);
        Conjunction.loadList(this.dataConnection.getConnection(),this.listConjunction);
        ConjunctionHasInclusion.loadList(this.dataConnection.getConnection(),this.listConjunctionHasInclusions,this.listConjunction,this.listInclusions);
        Tod.loadList(this.dataConnection.getConnection(),this.listTod,this.listLogicSystem,this.listContainer0s);
        Element.loadList(this.dataConnection.getConnection(),this.listElements,this.listFcc);
        Dynamism.loadList(this.dataConnection.getConnection(),this.listDynamisms,this.listFcc);
        Inclusion.loadList(this.dataConnection.getConnection(),this.listInclusions,this.listDynamisms,this.listTod);
        CClass.loadList(this.dataConnection.getConnection(),this.listCClass);
        CClassHasInclusion.loadList(this.dataConnection.getConnection(),this.listCClassHasInclusion,this.listCClass,this.listInclusions);
        Syllogism.loadList(this.dataConnection.getConnection(),this.listSyllogisms);
        InclusionHasSyllogism.loadList(this.dataConnection.getConnection(),this.listInclusionHasSyllogisms,this.listInclusions,this.listSyllogisms);
        SyllogismHasTod.loadList(this.dataConnection.getConnection(),listSyllogismHasTod,listSyllogisms,listTod);
        Register.loadList(this.dataConnection.getConnection(), listRegister, listDynamisms);
        RegisterHasSyllogism.loadList(this.dataConnection.getConnection(), listRegisterHasSyllogism,listRegister,listSyllogisms);

//        Dialectic.loadList(this.dataConnection.getConnection(), listDialectic);
//        Space.loadList(this.dataConnection.getConnection(), listSpace);
//        Time.loadList(this.dataConnection.getConnection(), listTime);
//        Quantum.loadList(this.dataConnection.getConnection(), listQuantum);
    }

    // GETTERS OF LISTVIEWS

    @Override
    public ObservableList<LogicSystem> getListLogicSystem() {
        return listLogicSystem;
    }

    @Override
    public ObservableList<Fcc> getListFcc() {
        return listFcc;
    }

    @Override
    public ObservableList<FccHasLogicSystem> getListFccHasLogicSystem() {
        return listFccHasLogicSystem;
    }

    @Override
    public ObservableList<Tod> getListTod() {
        return listTod;
    }

    @Override
    public ObservableList<Element> getListElements() {
        return listElements;
    }

    @Override
    public ObservableList<Dynamism> getListDynamisms() {
        return listDynamisms;
    }

    public ObservableList<User> getListUser() {
        return listUser;
    }

    @Override
    public ObservableList<CClass> getListCClass() {
        return listCClass;
    }

    @Override
    public ObservableList<CClassHasInclusion> getListCClassHasInclusion() {
        return listCClassHasInclusion;
    }

    @Override
    public ObservableList<Inclusion> getListInclusions() {
        return listInclusions;
    }

    @Override
    public ObservableList<Inclusion> getInclusions(Tod tod) {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getTod().getIdTod() == tod.getIdTod()) {
                inclusions.addAll(inclusion);
            }
        }
        return inclusions;
    }

    public ObservableList<SyllogismHasTod> getListSyllogismHasTod() {
        return listSyllogismHasTod;
    }

    public ObservableList<Syllogism> getListSyllogisms() {
        return listSyllogisms;
    }

    @Override
    public ObservableList<Syllogism> getSyllogisms(Tod tod) {
        ObservableList<Syllogism> listSyllogisms = FXCollections.observableArrayList();
        for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
            if (syllogismHasTod.getTod().equals(tod)) {
                listSyllogisms.add(syllogismHasTod.getSyllogism());
            }
        }
        return listSyllogisms;
    }

    @Override
    public List<Inclusion> getInclusions(Syllogism syllogism) {
        List<Inclusion> listInclusions = new ArrayList<>();
        for (InclusionHasSyllogism inclusionHasSyllogism : listInclusionHasSyllogisms) {
            if (inclusionHasSyllogism.getSyllogism().equals(syllogism)) {
                listInclusions.add(inclusionHasSyllogism.getInclusion());
            }
        }
        return listInclusions;
    }

    public ObservableList<InclusionHasSyllogism> getListInclusionHasSyllogisms() {
        return listInclusionHasSyllogisms;
    }

    public ObservableList<Tod> getTods(LogicSystem logicSystem) {
        ObservableList<Tod> todsOf = FXCollections.observableArrayList();
        for (Tod tod : getListTod()) {
            if (tod.getLogicSystem().equals(logicSystem)) {
                todsOf.add(tod);
            }
        }
        return  todsOf;
    }

    @Override
    public ObservableList<Fcc> getFccs(LogicSystem logicSystem) {
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Fcc fcc : getListFcc()) {
            for (FccHasLogicSystem fccHasLogicSystem : getListFccHasLogicSystem()) {
                if (fcc.equals(fccHasLogicSystem.getFcc())) {
                    if (logicSystem.equals(fccHasLogicSystem.getLogicSystem())) {
                        if (!fccs.contains(fcc)) {
                            fccs.add(fcc);
                        }
                    }
                }
            }
        }
        return fccs;
    }

    @Override
    public ObservableList<Container1> getContainer1s(Container0 container0) {
        ObservableList<Container1> container1s = FXCollections.observableArrayList();
        for (Container1 container1 : listContainer1s) {
            if (container1.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                container1s.addAll(container1);
            }
        }
        return container1s;
    }

    @Override
    public ObservableList<Container1> getListContainer1s() {
        return listContainer1s;
    }

    @Override
    public ObservableList<Container2> getListContainer2s() {
        return listContainer2s;
    }

    @Override
    public ObservableList<Container2> getContainer2s(Container1 container1) {
        ObservableList<Container2> container2s = FXCollections.observableArrayList();
        for (Container2 container2 : listContainer2s) {
            if (container2.getContainer1().getIdContainer1()==container1.getIdContainer1()) {
                container2s.add(container2);
            }
        }
        return container2s;
    }

    @Override
    public Container0 getSideContainer0(Container2 container2, boolean side) {
        Container0 container0 = null;
        for (Container0In2 container0In2 : listContainer0In2s) {
            if (container2.equals(container0In2.getContainer2())) {
                if (container0In2.isSide() == side) {
                    container0=container0In2.getContainer0();
                }
            }
        }
        return container0;
    }

    @Override
    public Container0 getSideContainer0(Container1 container1, boolean side) {
        Container0 container0 = null;

        for (Container0In1 container0In1 : listContainer0In1s) {
            if (container1.getIdContainer1()==container0In1.getContainer1().getIdContainer1()) {
                if (container0In1.isSide() == side) {
                    container0=container0In1.getContainer0();
                }
            }
        }
        return container0;
    }

    @Override
    public ObservableList<Fcc> getFccs(Container1 container1) {
        // First get fccs from the center
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Container2 container2 : getContainer2s(container1)) {
            // First add fccs from the center
            fccs.addAll(container2.getFcc());
            // from the left container0 of container2
            fccs.addAll(getFccs(getSideContainer0(container2, false)));
            // from the right container0 of container2
            fccs.addAll(getFccs(getSideContainer0(container2, true)));
        }
        fccs.addAll(getFccs(getSideContainer0(container1, false)));
        fccs.addAll(getFccs(getSideContainer0(container1, true)));

        return fccs;
    }

    @Override
    public ObservableList<Fcc> getFccs(Container0 container0) {
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Container1 container1 : getContainer1s(container0)) {
            fccs.addAll(getFccs(container1));
        }
        return fccs;
    }

    @Override
    public ObservableList<Fcc> getFccs(Tod tod) {
        return getFccs(tod.getContainer0());
    }

    @Override
    public LogicSystem newLogicSystem(String label, String description) {
        LogicSystem logicSystem = new LogicSystem(0,label,description,Timestamp.valueOf(LocalDateTime.now()));
        int resultLS = logicSystem.saveData(dataConnection.getConnection());
        logicSystem.setIdLogicSystem(LogicSystem.currentAutoIncrement);

        if (resultLS == 1){
            listLogicSystem.add(logicSystem);
        }
        return logicSystem;
    }

    @Override
    public void delete(LogicSystem logicSystem) {
        DataConnection dataConnection = getDataConnection();
        // First delete fccHasLogicSystem
        ObservableList<FccHasLogicSystem> tempFccHasLogicSystemToDelete = FXCollections.observableArrayList();
        for (FccHasLogicSystem fccHasLogicSystem : getListFccHasLogicSystem()) {
            if (fccHasLogicSystem.getLogicSystem().equals(logicSystem)) {
                if (fccHasLogicSystem.deleteData(dataConnection.getConnection()) == 1) {
                    tempFccHasLogicSystemToDelete.add(fccHasLogicSystem);
                }
            }
        }
        listFccHasLogicSystem.removeAll(tempFccHasLogicSystemToDelete);

        ObservableList<Tod> tempTodToRemove = FXCollections.observableArrayList();
        for (Tod tod : getListTod()) {
            if (tod.getLogicSystem().equals(logicSystem)) {
                if (tod.deleteData(dataConnection.getConnection()) == 1) {
                    tempTodToRemove.add(tod);
                }
            }
        }
        listTod.removeAll(tempTodToRemove);

        if (logicSystem.deleteData(dataConnection.getConnection()) == 1){
            getListLogicSystem().remove(logicSystem);
        }
    }

    @Override
    public void update(LogicSystem logicSystem) {
        logicSystem.updateData(dataConnection.getConnection());
    }

    @Override
    public void update(Tod tod) {
        tod.updateData(dataConnection.getConnection());
    }

    @Override
    public boolean connect() {
        return dataConnection.connect();
/*        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
            }
        });
        new Thread(sleeper).start();*/
    }

    @Override
    public void disconnect() {
        try {
            dataConnection.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LogicSystem getLogicSystem(Fcc fcc) {
        for (LogicSystem logicSystem : getListLogicSystem()) {
            for (FccHasLogicSystem fccHasLogicSystem : getListFccHasLogicSystem()) {
                if (fccHasLogicSystem.getFcc().equals(fcc)) {
                    if (fccHasLogicSystem.getLogicSystem().equals(logicSystem)) {
                        return logicSystem;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void setNodeInterface(NodeInterface nodeInterface) {
        DataHandler.nodeInterface = nodeInterface;
    }

    /**
     * Assumes there is an inclusion. Use method isInclusion() before.
     * @param ascendantDynamism Ascendant dynamism
     * @param descendantDynamism Descendant dynamism
     * @param tod The Table of deductions
     * @return The inclusion (if any) that has parameters as its definition, null otherwise
     */
    @Override
    public Inclusion getInclusion(Dynamism ascendantDynamism, Dynamism descendantDynamism, Tod tod) {
        for (Inclusion inclusion1 : getInclusions(tod)) {
            if (inclusion1.getGeneral().equals(ascendantDynamism)) {
                if (inclusion1.getParticular().equals(descendantDynamism)) {
                    return inclusion1;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param listInclusions List is in the correct order, i.e. particular notion at left and general notion at the right
     * @return True if there's a syllogism with the same inclusions, false otherwise
     */
    @Override
    public boolean isSyllogism(List<Inclusion> listInclusions, Tod tod) {
        ObservableList<List<Inclusion>> listOfListsOfInclusions  = FXCollections.observableArrayList();

        // create list of lists
        for (Syllogism syllogism1 : listSyllogisms) {
            List<Inclusion> tempListInclusions = new ArrayList<>();
            for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
                if (syllogismHasTod.getTod().equals(tod) && syllogismHasTod.getSyllogism().equals(syllogism1)) {
                    for (InclusionHasSyllogism inclusionHasSyllogism : listInclusionHasSyllogisms) {
                        if (inclusionHasSyllogism.getSyllogism().equals(syllogism1)) {
                            tempListInclusions.add(inclusionHasSyllogism.getInclusion());
                        }
                    }

                }
            }
            /*Collections.sort(tempListInclusions);
            Collections.sort(tempListInclusions,new Inclusion());*/
// this function orders inclusion lists
            Comparator<Inclusion> comparator = (inclusion1, inclusion2) -> {
                int order1=0;
                int order2=0;
                for (InclusionHasSyllogism inclusionHasSyllogism : NodeHandler.getDataInterface().getListInclusionHasSyllogisms()) {
                    if (inclusionHasSyllogism.getSyllogism().equals(syllogism1)) {
                        if (inclusionHasSyllogism.getInclusion().equals(inclusion1)) {
                            order1 = inclusionHasSyllogism.getInclusionOrder();
                        }
                        if (inclusionHasSyllogism.getInclusion().equals(inclusion2)) {
                            order2 = inclusionHasSyllogism.getInclusionOrder();
                        }
                    }
                }

                return order1-order2;

            };
            tempListInclusions.sort(comparator);
            // add tempList to listOfLists
            listOfListsOfInclusions.add(tempListInclusions);
            if(listInclusions.equals(tempListInclusions)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Syllogism getSyllogism(List<Inclusion> listInclusions, Tod tod) {
        Syllogism syllogism = null;

        // create list of lists
        ObservableList<List<Inclusion>> listOfListsOfInclusions  = FXCollections.observableArrayList();

        for (Syllogism syllogism1 : listSyllogisms) {
            List<Inclusion> tempListInclusions = new ArrayList<>();
            for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
                if (syllogismHasTod.getTod().equals(tod) && syllogismHasTod.getSyllogism().equals(syllogism1)) {
                    for (InclusionHasSyllogism inclusionHasSyllogism : listInclusionHasSyllogisms) {
                        if (inclusionHasSyllogism.getSyllogism().equals(syllogism1)) {
                            tempListInclusions.add(inclusionHasSyllogism.getInclusion());
                        }
                    }

                }
            }
            /*Collections.sort(tempListInclusions);
            Collections.sort(tempListInclusions,new Inclusion());*/

            Comparator<Inclusion> comparator = (inclusion1, inclusion2) -> {
                int order1=0;
                int order2=0;
                for (InclusionHasSyllogism inclusionHasSyllogism : NodeHandler.getDataInterface().getListInclusionHasSyllogisms()) {
                    if (inclusionHasSyllogism.getSyllogism().equals(syllogism1)) {
                        if (inclusionHasSyllogism.getInclusion().equals(inclusion1)) {
                            order1 = inclusionHasSyllogism.getInclusionOrder();
                        }
                        if (inclusionHasSyllogism.getInclusion().equals(inclusion2)) {
                            order2 = inclusionHasSyllogism.getInclusionOrder();
                        }
                    }
                }

                return order1-order2;

            };
            tempListInclusions.sort(comparator);
            // add tempList to listOfLists
            listOfListsOfInclusions.add(tempListInclusions);
            if(listInclusions.equals(tempListInclusions)) return syllogism1;
        }
        return syllogism;
    }

    @Override
    public void update(Syllogism syllogism) {
        syllogism.updateData(dataConnection.getConnection());
    }

    @Override
    public Syllogism newSyllogism(String label, List<Inclusion> listInclusions, Tod tod) {
        Syllogism syllogism = new Syllogism(0,label);
        SyllogismHasTod syllogismHasTod = new SyllogismHasTod(syllogism,tod);

        if (syllogism.saveData(dataConnection.getConnection()) == 1) {
            syllogism.setIdSyllogism(Syllogism.currentAutoIncrement);
            syllogism.setLabel(label);
            listSyllogisms.add(syllogism);
        }
        syllogism.updateData(dataConnection.getConnection());

        if (syllogismHasTod.saveData(dataConnection.getConnection()) == 1) {
            listSyllogismHasTod.add(syllogismHasTod);
        }

        // inclusionHasSyllogism
        int order = 0;
        for (Inclusion inclusion : listInclusions) {
            InclusionHasSyllogism inclusionHasSyllogism = new InclusionHasSyllogism(inclusion,syllogism,order);
            if (inclusionHasSyllogism.saveData(dataConnection.getConnection()) == 1) {
                this.listInclusionHasSyllogisms.add(inclusionHasSyllogism);
                order++;
            }
        }
        return syllogism;
    }

    public static NodeInterface getNodeInterface() {
        return DataHandler.nodeInterface;
    }

    // FCC
    @Override
    public Fcc newFcc(LogicSystem logicSystem) {
        // first fcc
        Fcc fcc = new Fcc(0, "","");
        int resultFcc = fcc.saveData(dataConnection.getConnection());
        fcc.setIdFcc(Fcc.currentAutoIncrement);
        fcc.setName("New FCC "+Fcc.currentAutoIncrement);
        if (resultFcc==1) {
            listFcc.add(fcc);
        }
        // second FccHasLogicSystem
        FccHasLogicSystem fccHasLogicSystem = new FccHasLogicSystem(fcc,logicSystem);
        if (fccHasLogicSystem.saveData(dataConnection.getConnection()) == 1){
            getListFccHasLogicSystem().add(fccHasLogicSystem);
        }
        // Elements
        Element e0 = new Element(0, "e" + fcc.getIdFcc(), 0, fcc);
        Element e1 = new Element(0, "e" + fcc.getIdFcc(), 1, fcc);

        int resultE0 = e0.saveData(dataConnection.getConnection());
        e0.setIdElement(Element.currentAutoIncrement);
        int resultE1 = e1.saveData(dataConnection.getConnection());
        e1.setIdElement(Element.currentAutoIncrement);

        if (resultE0 == 1 && resultE1 == 1){
            getListElements().addAll(e0,e1);
        }
        // Dynamisms
        Dynamism dynamism0 = new Dynamism(0, 0, "Positive orientation of "+fcc.getName(), "", fcc);
        int resultC0 = dynamism0.saveData(dataConnection.getConnection());
        dynamism0.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism1 = new Dynamism(0, 1, "Negative orientation of "+fcc.getName(), "", fcc);
        int resultC1 = dynamism1.saveData(dataConnection.getConnection());
        dynamism1.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism2 = new Dynamism(0, 2, "Symmetric orientation of "+fcc.getName(), "", fcc);
        int resultC2 = dynamism2.saveData(dataConnection.getConnection());
        dynamism2.setIdDynamism(Dynamism.currentAutoIncrement);

        if(resultC0==1 && resultC1 == 1 && resultC2 == 1){
            getListDynamisms().addAll(dynamism0,dynamism1,dynamism2);
        }
        return fcc;
    }

    @Override
    public Fcc duplicateFcc(Fcc fcc, int i, LogicSystem logicSystem) {
        // first fcc
        Fcc newFcc = new Fcc(0, fcc.getName() + " " + i,fcc.getDescription());
        int resultFcc = newFcc.saveData(dataConnection.getConnection());
        newFcc.setIdFcc(Fcc.currentAutoIncrement);
        if (resultFcc==1) {
            listFcc.add(newFcc);
        }
        // second FccHasLogicSystem
        FccHasLogicSystem fccHasLogicSystem = new FccHasLogicSystem(newFcc,logicSystem);
        if (fccHasLogicSystem.saveData(dataConnection.getConnection()) == 1){
            getListFccHasLogicSystem().add(fccHasLogicSystem);
        }
        // Elements
        Element e0 = new Element(0, getElement(fcc,0).getSymbol()+i, 0, newFcc);
        Element e1 = new Element(0, getElement(fcc,1).getSymbol()+i, 1, newFcc);

        int resultE0 = e0.saveData(dataConnection.getConnection());
        e0.setIdElement(Element.currentAutoIncrement);
        int resultE1 = e1.saveData(dataConnection.getConnection());
        e1.setIdElement(Element.currentAutoIncrement);

        if (resultE0 == 1 && resultE1 == 1){
            getListElements().addAll(e0,e1);
        }
        // Dynamisms
        Dynamism dynamism0 = new Dynamism(0, 0, getDynamism(fcc,0).getProposition(),getDynamism(fcc,0).getDescription(),newFcc);
        int resultC0 = dynamism0.saveData(dataConnection.getConnection());
        dynamism0.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism1 = new Dynamism(0, 1, getDynamism(fcc,1).getProposition(),getDynamism(fcc,1).getDescription(),newFcc);
        int resultC1 = dynamism1.saveData(dataConnection.getConnection());
        dynamism1.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism2 = new Dynamism(0, 2, getDynamism(fcc,2).getProposition(),getDynamism(fcc,2).getDescription(),newFcc);
        int resultC2 = dynamism2.saveData(dataConnection.getConnection());
        dynamism2.setIdDynamism(Dynamism.currentAutoIncrement);

        if(resultC0==1 && resultC1 == 1 && resultC2 == 1){
            getListDynamisms().addAll(dynamism0,dynamism1,dynamism2);
        }
        return newFcc;
    }

    @Override
    public void update(Fcc fcc) {
        fcc.updateData(dataConnection.getConnection());
        getElement(fcc,0).updateData(dataConnection.getConnection());
        getElement(fcc,1).updateData(dataConnection.getConnection());
        getDynamism(fcc,0).updateData(dataConnection.getConnection());
        getDynamism(fcc,1).updateData(dataConnection.getConnection());
        getDynamism(fcc,2).updateData(dataConnection.getConnection());
    }

    @Override
    public void update(Element element) {
        element.updateData(dataConnection.getConnection());
    }

    @Override
    public void update(Dynamism dynamism) {
        dynamism.updateData(dataConnection.getConnection());
    }


    // Delete methods

    @Override
    public void delete(Tod tod) {
        // delete Syllogisms
        ObservableList<Syllogism> syllogismsToDelete = FXCollections.observableArrayList();
        for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
            if (syllogismHasTod.getTod().equals(tod)) {
                syllogismsToDelete.add(syllogismHasTod.getSyllogism());
            }
        }
        for (Syllogism syllogism : syllogismsToDelete) {
            delete(syllogism);
        }
        // delete Inclusions
        ObservableList<Inclusion> inclusionsToDelete = FXCollections.observableArrayList();
        for (Inclusion inclusion : listInclusions) {
            if (inclusion.getTod().getIdTod() == tod.getIdTod()) {
                inclusionsToDelete.addAll(inclusion);
            }
        }
        for (Inclusion inclusion : inclusionsToDelete) {
            delete(inclusion);
        }
        DataConnection dataConnection = getDataConnection();
        int result = tod.deleteData(dataConnection.getConnection());
        if (result == 1) {
            this.listTod.remove(tod);
        }
        deleteAll(tod.getContainer0());
    }

    @Override
    public void delete(Syllogism syllogism) {
        ObservableList<SyllogismHasTod> syllogismHasTodsToDelete = FXCollections.observableArrayList();
        for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
            if (syllogismHasTod.getSyllogism().equals(syllogism)) {
                syllogismHasTodsToDelete.add(syllogismHasTod);
            }
        }
        for (SyllogismHasTod syllogismHasTod : syllogismHasTodsToDelete) {
            if (syllogismHasTod.deleteData(getDataConnection().getConnection()) == 1) {
                listSyllogismHasTod.remove(syllogismHasTod);
            }
        }
        //inclusion-syllogism
        ObservableList<InclusionHasSyllogism> inclusionHasSyllogismToDelete = FXCollections.observableArrayList();
        for (InclusionHasSyllogism inclusionHasSyllogism : listInclusionHasSyllogisms) {
            if (inclusionHasSyllogism.getSyllogism().equals(syllogism)) {
                inclusionHasSyllogismToDelete.add(inclusionHasSyllogism);
            }
        }
        for (InclusionHasSyllogism inclusionHasSyllogism : inclusionHasSyllogismToDelete) {
            if (inclusionHasSyllogism.deleteData(getDataConnection().getConnection())==1) {
                listInclusionHasSyllogisms.remove(inclusionHasSyllogism);
            }
        }

        int result = syllogism.deleteData(getDataConnection().getConnection());
        if (result == 1) {
            this.listSyllogisms.remove(syllogism);
        }
    }

    @Override
    public ObservableList<Syllogism> getListSyllogisms(Tod tod) {
        ObservableList<Syllogism> listSyllogisms = FXCollections.observableArrayList();
        for (SyllogismHasTod syllogismHasTod : getListSyllogismHasTod()) {
            if (syllogismHasTod.getTod().equals(tod)) {
                listSyllogisms.add(syllogismHasTod.getSyllogism());
            }
        }
        return listSyllogisms;
    }

    @Override
    public ObservableList<Register> getRegisters(Syllogism syllogism) {
        ObservableList<Register> registers = FXCollections.observableArrayList();
        for (RegisterHasSyllogism registerHasSyllogism : listRegisterHasSyllogism) {
            if (registerHasSyllogism.getSyllogism().equals(syllogism)) {
                registers.add(registerHasSyllogism.getRegister());
            }
        }
        return registers;
    }

    @Override
    public Register newRegister(Syllogism syllogism, LocalDateTime startTimeValue, LocalDateTime endTimeValue) {
        Dynamism dynamism = getInclusions(syllogism).get(0).getParticular();
        Register register = new Register(0,dynamism,Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(startTimeValue),Timestamp.valueOf(endTimeValue));
        if (register.saveData(getDataConnection().getConnection())==1) {
            register.setIdRegister(Register.getCurrentAutoIncrement());
            listRegister.add(register);
        }
        RegisterHasSyllogism registerHasSyllogism = new RegisterHasSyllogism(register, syllogism);
        if (registerHasSyllogism.saveData(getDataConnection().getConnection()) == 1) {
            listRegisterHasSyllogism.add(registerHasSyllogism);
        }

        return register;
    }

    @Override
    public void delete(Register register) {
        register.deleteData(dataConnection.getConnection());
    }

    @Override
    public Tod getTod(Syllogism syllogism) {
        Tod tod = null;
        for (SyllogismHasTod syllogismHasTod : listSyllogismHasTod) {
            if (syllogismHasTod.getSyllogism().equals(syllogism)) {
                tod = syllogismHasTod.getTod();
            }
        }
        return tod;
    }

    @Override
    public ObservableList<Register> getListRegisters() {
        return listRegister;
    }

    private void deleteAll(Container0 container0) {
        ObservableList<Container0In1> container0In1sToDelete = FXCollections.observableArrayList();
        ObservableList<Container0In2> container0In2sToDelete = FXCollections.observableArrayList();
        ObservableList<Container1> container1sToDelete = FXCollections.observableArrayList();
        ObservableList<Container2> container2sToDelete = FXCollections.observableArrayList();
        ObservableList<Container0> container0sToDeleteAll = FXCollections.observableArrayList();

        for (Container1 container1 : listContainer1s) {
            if (container1.getContainer0().getIdContainer0() == container0.getIdContainer0()) {

                // left right of each subbranch
                for (Container0In2 container0In2 : listContainer0In2s) {
                    if (container0In2.getContainer2().getContainer1().getIdContainer1() == container1.getIdContainer1()) {
                        container2sToDelete.addAll(container0In2.getContainer2());
                        container0In2sToDelete.addAll(container0In2);
                        container0sToDeleteAll.addAll(container0In2.getContainer0());
                    }
                }

                for (Container0In2 container0In2 : container0In2sToDelete) {
                    delete(container0In2);
                }

                for (Container2 container2 : container2sToDelete) {
                    delete(container2);
                }

                // remove left right container0s, then left right container0s of each subbranch
                for (Container0In1 container0In1 : listContainer0In1s) {
                    if (container0In1.getContainer1().getIdContainer1() == container1.getIdContainer1()) {
                        container1sToDelete.addAll(container1);
                        container0In1sToDelete.addAll(container0In1);
                        container0sToDeleteAll.addAll(container0In1.getContainer0());
                    }
                }
            }
        }

        for (Container0In1 container0In1 : container0In1sToDelete) {
            delete(container0In1);
        }
        for (Container0 container01 : container0sToDeleteAll) {
            deleteAll(container01);
        }
        for (Container1 container1 : container1sToDelete) {
            delete(container1);
        }
        delete(container0);
    }

    private void delete(Container0In2 container0In2) {
        if (container0In2.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0In2s.remove(container0In2);
        }
    }

    private void delete(Container0In1 container0In1) {
        if (container0In1.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0In1s.remove(container0In1);
        }
    }

    @Override
    public void delete(Container0 container0) {
        if (container0.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0s.remove(container0);
        }
    }

    @Override
    public void delete(Container1 container1) {
        if (container1.deleteData(dataConnection.getConnection()) == 1) {
            listContainer1s.remove(container1);
        }
    }

    @Override
    public void delete(Container2 container2) {
        if (container2.deleteData(dataConnection.getConnection()) == 1) {
            listContainer2s.remove(container2);
        }
    }

    @Override
    public void delete(Container0 container0, Container1 container1) {
        Container0In1 container0In1ToRemove=null;
        for (Container0In1 container0In1 : listContainer0In1s) {
            if (container0In1.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                if (container0In1.getContainer1().getIdContainer1()==container1.getIdContainer1()) {
                    if (container0In1.deleteData(dataConnection.getConnection()) == 1) {
                        container0In1ToRemove=container0In1;
                    }
                }
            }
        }
        listContainer0In1s.remove(container0In1ToRemove);
    }

    @Override
    public void delete(Container0 container0, Container2 container2) {
        Container0In2 container0In2ToRemove = null;
        for (Container0In2 container0In2 : listContainer0In2s) {
            if (container0In2.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                if (container0In2.getContainer2().getIdContainer2()==container2.getIdContainer2()) {
                    if (container0In2.deleteData(dataConnection.getConnection()) == 1) {
                        container0In2ToRemove=container0In2;
                    }
                }
            }
        }
        listContainer0In2s.remove(container0In2ToRemove);
    }

    @Override
    public Tod newTod(String label, LogicSystem logicSystem) {
        // Container0
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
        }
        // Tod
        Tod tod = new Tod(0,label, logicSystem,container0);
        if (tod.saveData(dataConnection.getConnection()) == 1) {
            tod.setIdTod(Tod.currentAutoIncrement);
            getListTod().add(tod);
        }
        return tod;
    }

    @Override
    public Container0 newContainer0(Container1 container1, boolean side) {
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
            listContainer0s.add(container0);
        }
        Container0In1 container0In1 = new Container0In1(container0, container1, side);
        listContainer0In1s.add(container0In1);
        container0In1.saveData(dataConnection.getConnection());
        return container0;
    }

    @Override
    public Container0 newContainer0(Container2 container2, boolean side) {
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
        }
        Container0In2 container0In2 = new Container0In2(container0, container2, side);
        listContainer0In2s.add(container0In2);
        container0In2.saveData(dataConnection.getConnection());
        return container0;
    }

    // Fcc... fcc is using varargs, there can be zero or any positive amount of fccs
    @Override
    public Container1 newContainer1(Container0 container0) {
        Container1 container1 = new Container1(0,container0,0);
        if (container1.saveData(dataConnection.getConnection()) == 1) {
            container1.setIdContainer1(Container1.currentAutoIncrement);
            container1.setBranchOrder(Container1.currentAutoIncrement);
            listContainer1s.add(container1);
        }
        container1.updateData(dataConnection.getConnection());
        return container1;
    }

    @Override
    public void update(Container1 container1) {
        container1.updateData(dataConnection.getConnection());
    }

    @Override
    public Container2 newContainer2(Fcc fcc, Container1 container1) {
        Container2 container2 = new Container2(0,fcc,container1,0);
        if (container2.saveData(dataConnection.getConnection()) == 1) {
            container2.setIdContainer2(Container2.currentAutoIncrement);
            container2.setSubBranchOrder(Container2.currentAutoIncrement);
            listContainer2s.add(container2);
        }
        return container2;
    }

    @Override
    public void update(Container2 container2) {
        container2.updateData(dataConnection.getConnection());
    }

    @Override
    public void addContainer0in2(Container0 container0, Container2 container2, boolean side) {
        Container0In2 container0In2 = new Container0In2(container0,container2,side);
        if (container0In2.saveData(dataConnection.getConnection()) == 1) {
            listContainer0In2s.add(container0In2);
        }
    }

    @Override
    public void addContainer0in1(Container0 container0, Container1 container1, boolean side) {
        Container0In1 container0In1 = new Container0In1(container0,container1,side);
        if (container0In1.saveData(dataConnection.getConnection()) == 1) {
            listContainer0In1s.add(container0In1);
        }
    }

    @Override
    public Inclusion newInclusion(Dynamism particularDynamism, Dynamism generalDynamism, Tod tod) {
        Inclusion inclusion = new Inclusion(0, particularDynamism, generalDynamism, tod);
        if (inclusion.saveData(dataConnection.getConnection()) == 1) {
            listInclusions.add(inclusion);
        }
        return inclusion;
    }

    @Override
    public void delete(Inclusion inclusion) {
        DataConnection dataConnection = getDataConnection();
        int result = inclusion.deleteData(dataConnection.getConnection());
        if (result == 1) {
            this.listInclusions.remove(inclusion);
        }
    }


    /*
             _____                       _                        _   _
            /  __ \                     | |                      | | | |
            | /  \/ ___  _ __ ___  _ __ | | _____  __   __ _  ___| |_| |_ ___ _ __ ___
            | |    / _ \| '_ ` _ \| '_ \| |/ _ \ \/ /  / _` |/ _ \ __| __/ _ \ '__/ __|
            | \__/\ (_) | | | | | | |_) | |  __/>  <  | (_| |  __/ |_| ||  __/ |  \__ \
             \____/\___/|_| |_| |_| .__/|_|\___/_/\_\  \__, |\___|\__|\__\___|_|  |___/
                                  | |                   __/ |
                                  |_|                  |___/
     */
    @Override
    public Element getElement(Fcc fcc, int polarity){
        for(Element e: this.listElements){
            if(e.getFcc().equals(fcc)){
                if(e.getPolarity()==polarity){
                    return e;
                }
            }
        }
        try {
            throw new Exception("Could not find any Element for the Fcc.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Element getElement(Dynamism dynamism, int polarity) {
        for (Element element : this.listElements) {
            if (element.getFcc().equals(dynamism.getFcc())) {
                if (polarity==element.getPolarity()) {
                    return element;
                }
            }
        }
        try {
            throw new Exception("Could not find any Element for the Fcc.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Dynamism getDynamism(Fcc fcc, int orientation){
        for(Dynamism d:this.listDynamisms){
            if(d.getFcc().equals(fcc)){
                if(d.getOrientation()==orientation){
                    return d;
                }
            }
        }
        return null;
    }

    /**
     * Get the list of FCCs that belong to a logic system
     * @param ls the Logic System
     * @return List of fcc
     */
    public ArrayList<Fcc> fccOf(LogicSystem ls){
        ArrayList<Fcc> list = new ArrayList<>();
        for(FccHasLogicSystem fhls:this.listFccHasLogicSystem){
            if(fhls.getLogicSystem().equals(ls)){
                list.add(fhls.getFcc());
            }
        }
        return list;
    }

    @Override
    @Deprecated
    public boolean descendsFrom(Fcc fcc, Dynamism dynamism) {
        for(Inclusion i:this.listInclusions){
            if (i.getParticular().equals(getDynamism(fcc, 0)) ||
                    i.getParticular().equals(getDynamism(fcc, 1)) ||
                    i.getParticular().equals(getDynamism(fcc, 2))) {
                if (i.getGeneral().equals(dynamism)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInclusion(Dynamism descendantDynamism, Dynamism ascendantDynamism, Tod tod) {
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getTod().equals(tod)) {
                if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                    if (inclusion.getParticular().getIdDynamism()==descendantDynamism.getIdDynamism()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInclusion(Fcc descendantFcc, Dynamism ascendantDynamism) {
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                if (descendantFcc.getIdFcc()==inclusion.getParticular().getFcc().getIdFcc()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInclusion(Tod tod,Fcc descendantFcc, Fcc ascendantFcc) {
        Dynamism positiveDescendant = getDynamism(descendantFcc,0);
        Dynamism negativeDescendant = getDynamism(descendantFcc,1);
        Dynamism symmetricDescendant = getDynamism(descendantFcc,2);
        Dynamism positiveAscendant = getDynamism(ascendantFcc,0);
        Dynamism negativeAscendant = getDynamism(ascendantFcc,1);
        Dynamism symmetricAscendant = getDynamism(ascendantFcc,2);
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getTod().equals(tod)) {
                if (inclusion.getGeneral().equals(positiveAscendant) | inclusion.getGeneral().equals(negativeAscendant) | inclusion.getGeneral().equals(symmetricAscendant)) {
                    if (inclusion.getParticular().equals(positiveDescendant) | inclusion.getParticular().equals(negativeDescendant) | inclusion.getParticular().equals(symmetricDescendant)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ObservableList<Inclusion> getInclusions(Tod tod, Fcc descendantFcc, Fcc ascendantFcc) {
        ObservableList<Inclusion> listInclusions = FXCollections.observableArrayList();
        Dynamism positiveDescendant = getDynamism(descendantFcc,0);
        Dynamism negativeDescendant = getDynamism(descendantFcc,1);
        Dynamism symmetricDescendant = getDynamism(descendantFcc,2);
        Dynamism positiveAscendant = getDynamism(ascendantFcc,0);
        Dynamism negativeAscendant = getDynamism(ascendantFcc,1);
        Dynamism symmetricAscendant = getDynamism(ascendantFcc,2);
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getTod().equals(tod)) {
                if (inclusion.getGeneral().equals(positiveAscendant) | inclusion.getGeneral().equals(negativeAscendant) | inclusion.getGeneral().equals(symmetricAscendant)) {
                    if (inclusion.getParticular().equals(positiveDescendant) | inclusion.getParticular().equals(negativeDescendant) | inclusion.getParticular().equals(symmetricDescendant)) {
                        listInclusions.add(inclusion);
                    }
                }
            }
        }
        return listInclusions;
    }


}
