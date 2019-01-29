package com.amuyana.app.data.tod.containers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.sql.*;

public class Container1 {
    private IntegerProperty idContainer1;
    private Container0 container0;
    public static int currentAutoIncrement;

    public Container1(int idContainer1, Container0 container0) {
        this.idContainer1 = new SimpleIntegerProperty(idContainer1);
        this.container0 = container0;
    }


    //Metodos atributo: idContainer1
    public int getIdContainer1() {
        return idContainer1.get();
    }
    public void setIdContainer1(int idContainer1) {
        this.idContainer1 = new SimpleIntegerProperty(idContainer1);
    }
    public IntegerProperty IdContainer1Property() {
        return idContainer1;
    }

    // Methods attribute: idContainer0
    public Container0 getContainer0() {
        return container0;
    }

    public void setContainer0(Container0 container0) {
        this.container0 = container0;
    }

    /*
                      __    ____  __
          /\/\  _   _/ _\  /___ \/ /
         /    \| | | \ \  //  / / /
        / /\/\ \ |_| |\ \/ \_/ / /___
        \/    \/\__, \__/\___,_\____/
                |___/

     */
    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_container_1 (id_container_1, id_container_0) "
                + "VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, this.getIdContainer1());
            statement.setInt(2, this.container0.getIdContainer0());
            int result = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Container1.currentAutoIncrement = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Not used a priori, we will just delete relations, maybe...
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_container_1 SET container_0 = ? WHERE id_container_1 = ? and id_container_0 = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setInt(1, idContainer1.get()); // change these
            instruccion.setInt(2, container0.getIdContainer0()); // change these

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_container_1 "+
                            "WHERE id_container_1 = ? and id_container_0 = ?"
            );
            instruccion.setInt(1, this.idContainer1.get());
            instruccion.setInt(2, container0.getIdContainer0());

            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void loadList(Connection connection,
                                ObservableList<Container1> container1s,
                                ObservableList<Container0> container0s){
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                    "SELECT id_container_1, id_container_0 "
                            + "FROM amuyana.tbl_container_1"
            );

            while(result.next()){
                for (Container0 container0 : container0s) {
                    if (container0.getIdContainer0()==result.getInt("id_container_0")) {
                        container1s.add(new Container1(result.getInt("id_container_1"), container0));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        //this.setSpacing(20);
        //this.setAlignment(Pos.CENTER);
        //this.setPickOnBounds(true);
    }


}