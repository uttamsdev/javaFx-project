package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.utils.CommonTask;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddContactController extends DBConnection implements Initializable {
    public Button backButton;
    public TextField tfNumber;
    public TextField tfName;
    public TextField tfEmailAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void OnBackPress(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 700, 450));
        stage.show();
    }

    public void storeContact(ActionEvent actionEvent) {
        try{
            Connection connection = getConnections();
            if(!connection.isClosed()) {

                String number = tfNumber.getText();
                String name = tfName.getText();
                String email = tfEmailAddress.getText();

                if(number.isEmpty()) {

                    CommonTask.showAlert(Alert.AlertType.ERROR,"Field required","Number field can't be empty");
                   /* Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Field Required");
                    alert.setContentText("Number field can't be empty");
                    alert.show();*/
                }

                if(name.isEmpty()) {
                    CommonTask.showAlert(Alert.AlertType.ERROR,"Field required","name field can't be empty");
                }

                if(email.isEmpty()) {
                    CommonTask.showAlert(Alert.AlertType.ERROR,"Field required","email field can't be empty");
                }

                //data store
                //sql query
                String sql = "INSERT INTO contact(name,number,email) VALUES(?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,number);
                preparedStatement.setString(3,email);
               boolean execute =  preparedStatement.execute();
               CommonTask.showAlert(Alert.AlertType.CONFIRMATION,"Data operation","Data successfully stored");
               cleanTextField();
            }

        }catch (Exception ex) {
           CommonTask.showAlert(Alert.AlertType.ERROR,"Data operation failed","SQL query not executed");
        }
    }

    private void cleanTextField() {
        tfName.setText("");
        tfNumber.setText("");
        tfEmailAddress.setText("");
    }
}
