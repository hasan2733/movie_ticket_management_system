package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.fxml.FXML;

public class AboutController {
    @FXML
    public void backEvent()
    {
        HelloApplication.changeScene("customerDashboard.fxml");
    }
}
