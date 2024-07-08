package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addContact(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addcontact.fxml"));
        Main.stage.setTitle("Add Contact Page");
        Main.stage.setScene(new Scene(root, 700, 450));
        Main.stage.show();
    }

    public void viewContact(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("viewcontact.fxml"));
        stage.setTitle("Contact Application");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }
}
