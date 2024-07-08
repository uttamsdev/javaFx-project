package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.entity.Contact;
import sample.utils.CommonTask;
import sample.utils.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateController extends DBConnection implements Initializable {
    public TextField EmailX;
    public TextField NumberX;
    public TextField nameX;

    public void updateContactField(ActionEvent actionEvent) {
        String name = nameX.getText();
        String number = NumberX.getText();
        String email = EmailX.getText();

        Connection connection = getConnections();
        try {
            if(!connection.isClosed()) {
                String  sql = "UPDATE contact SET name=?, number=?, email=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,name);
                statement.setString(2,number);
                statement.setString(3, email);
                //statement.setInt(4,);
                statement.executeUpdate();
                CommonTask.showAlert(Alert.AlertType.CONFIRMATION,"Update Operation","Update Query successfully executed");

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



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
