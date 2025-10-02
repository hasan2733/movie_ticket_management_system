package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerLoginController {

    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void backEvent(ActionEvent event) {
    HelloApplication.changeScene("loginSelection.fxml");
    }

    @FXML
    public void loginEvent(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password!");
            return;
        }

        try {
            Connection connection = DBConnection.getConnection();
            String sql = "select * from customers where email = ? and password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                CurrentUser.setUser(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("phone"),
                        "customer"
                );
                showAlert("Success", "Login successful!");
                HelloApplication.changeScene("customerDashboard.fxml");
            } else {
                showAlert("Error", "Invalid email or password!");
            }

            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error connecting to database: " + e.getMessage());
        }
    }

    @FXML
    public void registerNewCustomerEvent(ActionEvent event) {
    HelloApplication.changeScene("customerRegistration.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
