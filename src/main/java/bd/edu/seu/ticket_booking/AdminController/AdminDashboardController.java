package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    public Label adminLabel;

    @FXML
    public void ManageDiscountEvent(ActionEvent event) {
    HelloApplication.changeScene("manageDiscountAndCoupon.fxml");
    }

    @FXML
    public void logoutEvent(ActionEvent event) {
    HelloApplication.changeScene("adminLogin.fxml");
    }

    @FXML
    public void manageMoviesEvent(ActionEvent event) {
    HelloApplication.changeScene("manageMovies.fxml");
    }

    @FXML
    public void viewReportsEvent(ActionEvent event) {
    HelloApplication.changeScene("revenueReport.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminLabel.setText(CurrentUser.getName());
    }

    @FXML
    public void complainEvent()
    {
        HelloApplication.changeScene("viewComplain.fxml");
    }
    @FXML
    public void showBookings()
    {
        HelloApplication.changeScene("adminBookingView.fxml");
    }
}
