package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.entity.Contact;
import sample.utils.CommonTask;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewContactController extends DBConnection implements Initializable {

    public TableView<Contact> tableView;
    public TableColumn<Contact,String> tableColName;
    public TableColumn<Contact,String> tableColNumber;
    public TableColumn<Contact,String> tableColEmail;
    public TableColumn tableColAction;
    public TableColumn tableColID;

    private ArrayList<Contact> contacts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contacts = new ArrayList<Contact>();

        tableColName.setCellValueFactory(new PropertyValueFactory<Contact,String>("name"));
        tableColNumber.setCellValueFactory(new PropertyValueFactory<Contact,String>("number"));
        tableColEmail.setCellValueFactory(new PropertyValueFactory<Contact,String>("email"));
        tableColID.setCellValueFactory(new PropertyValueFactory<Contact,Integer>("id"));
        tableColAction.setCellValueFactory(new PropertyValueFactory<>(""));

     showTableInformation();

        Callback<TableColumn<Contact,String>, TableCell<Contact,String>> cellCallback = new Callback<TableColumn<Contact, String>, TableCell<Contact, String>>() {
            @Override
            public TableCell<Contact, String> call(TableColumn<Contact, String> param) {
                TableCell<Contact,String> cell = new TableCell<Contact,String>() {
                    final Button editButton = new Button("Update");
                    final Button deleteButton = new Button("Delete");
                    final HBox hBox = new HBox(editButton,deleteButton);

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setGraphic(null);
                            setText(null);
                        } else {

                            editButton.setOnAction(event -> {
                                Stage stage = new Stage();
                                Parent root = null;
                                try {
                                    root = FXMLLoader.load(getClass().getResource("update.fxml"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage.setTitle("Update Contact");
                                stage.setScene(new Scene(root, 800, 600));
                                stage.show();
                                Contact contact = getTableView().getItems().get(getIndex());
                                updateContact(contact);

                            });

                            deleteButton.setOnAction(event -> {
                                Contact contact = getTableView().getItems().get(getIndex());
                                deleteContact(contact);
                            });
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        tableColAction.setCellFactory(cellCallback);
    }

    private void deleteContact(Contact contact) {
        Connection connection = getConnections();
        try {
            if(!connection.isClosed()) {
               String sql = "DELETE FROM contact where number=?";
               PreparedStatement statement = connection.prepareStatement(sql);
               statement.setString(1,contact.getNumber());
               statement.execute();

                CommonTask.showAlert(Alert.AlertType.CONFIRMATION,"Delete operation","User "+contact.getName()+" is deleted from database");
                tableView.getItems().remove(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeConnections();
        }
    }

    private void updateContact(Contact contact) {
        //DB connection

        Connection connection = getConnections();
        try {
            if(!connection.isClosed()) {
                String  sql = "UPDATE contact SET name=?, number=?, email=? where _id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
              //  statement.setString(1,name);
               // statement.setString(2,number);
               // statement.setString(3, email);
                statement.setInt(4,contact.getId());
                statement.executeUpdate();
                showTableInformation();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            closeConnections();
        }

    }

    private void showTableInformation() {

        // db connection
        Connection connection = getConnections();
        try {
            if(!connection.isClosed()){
                String sql = "SELECT * FROM contact";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt("_id");
                    String name = resultSet.getString("name");
                    String number = resultSet.getString("number");
                    String emailAddress = resultSet.getString("email");

                    Contact contact = new Contact(id,name,number,emailAddress);

                    contacts.add(contact);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnections();
        }

        // show data into table view
        tableView.getItems().setAll(contacts);
    }
}
