package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDashboardController implements Initializable {

    @FXML
    public Label emailLabel;

    @FXML
    public Label nameLabel;

    @FXML
    public Label phoneLabel;

    @FXML
    public void aboutEvent(ActionEvent event) {
        HelloApplication.changeScene("about.fxml");
    }

    @FXML
    public void bookingHistory(ActionEvent event) {
        HelloApplication.changeScene("bookingHistory.fxml");
    }

    @FXML
    public void logoutEvent(ActionEvent event) {
        CurrentUser.clear();
        HelloApplication.changeScene("customerLogin.fxml");
    }

    @FXML
    public void viewMoviesEvent(ActionEvent event) {
    HelloApplication.changeScene("viewMovies.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (CurrentUser.isLoggedIn()) {
            nameLabel.setText(CurrentUser.getName());
            emailLabel.setText(CurrentUser.getEmail());
            phoneLabel.setText(CurrentUser.getPhone() != null ? CurrentUser.getPhone() : "Not available");
        }
    }
}
