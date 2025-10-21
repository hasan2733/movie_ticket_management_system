package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginController {

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField userField;

    @FXML
    public void backEvent(ActionEvent event) {
    HelloApplication.changeScene("loginSelection.fxml");
    }

    @FXML
    public void loginEvent(ActionEvent event) {
        String username = userField.getText().trim();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password are required!");
            return;
        }

        try {
            if (validateAdminCredentials(username, password)) {
                setCurrentAdmin(username);
                showAlert("Success", "Login successful!");
                HelloApplication.changeScene("adminDashboard.fxml");
            } else {
                showAlert("Error", "Invalid username or password!");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    @FXML
    public void resgisterNewAdminEvent(ActionEvent event) {
    HelloApplication.changeScene("adminRegistration.fxml");
    }
    private boolean validateAdminCredentials(String username, String password) throws SQLException {
        String sql = "select * from admins where username = ? and password = ?";

        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void setCurrentAdmin(String username) throws SQLException {
        String sql = "select id, name, username, email from admins where username = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CurrentUser.setUser(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        "admin"
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
