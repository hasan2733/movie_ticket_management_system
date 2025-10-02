package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminDashboardController {

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

}
