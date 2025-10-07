package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import com.mysql.cj.xdevapi.Warning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddComplainController implements Initializable {

    @FXML
    private ComboBox<String> complainComboBox;

    @FXML
    private TextArea textArea;

    @FXML
    private Label userLabel;

    @FXML
    public void addComplain(ActionEvent event) {
        String type = complainComboBox.getValue();
        String message = textArea.getText().trim();

        if (type == null || type.isEmpty()) {
            showAlert("Warnings", "Please select a complaint type.");
            return;
        }

        if (message.isEmpty()) {
            showAlert("Warnings", "Please write your complaint message.");
            return;
        }

        String username = CurrentUser.getName();
        String sql = "insert into complaints (username, complaint_type, message) values (?, ?, ?)";
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();
            showAlert("Success","Complain successfully submitted");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("customerDashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> obs = FXCollections.observableArrayList();
        obs.add("System Related");
        obs.add("Booking Related");
        obs.add("Payment Related");
        obs.add("Others");
        complainComboBox.setItems(obs);
        userLabel.setText(CurrentUser.getName());
        complainComboBox.getSelectionModel().selectFirst();
        complainComboBox.setStyle("-fx-text-fill: white");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
