package bd.edu.seu.ticket_booking.Login;

import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginSelectionController {

    @FXML
    public void CustomerEvent(ActionEvent event) {
     HelloApplication.changeScene("customerLogin.fxml");
    }

    @FXML
    public void adminEvent(ActionEvent event) {
    HelloApplication.changeScene("adminLogin.fxml");
    }

}
