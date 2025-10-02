package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class AdminRegistrationController {

    @FXML
    public PasswordField confirmPasswordField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField nameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField usernameField;

    @FXML
    public void backTologinEvent(ActionEvent event) {
       HelloApplication.changeScene("adminLogin.fxml");
    }

    @FXML
    public void registerEvent(ActionEvent event) {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();


        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }
        if(!emailValidation(email) )
        {
            showAlert("Error","Invalid Email");
            return;
        }
        if(password.length()<4 )
        {
            showAlert("Error","Password must be at leat 4 character");
            return;
        }
        if(!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*"))
        {
            showAlert("Error","Password must contain both upper and lower case letter");
            return;
        }
        if (name.length()<3)
        {
            showAlert("Error","Invalid name");
            return;
        }

        try {
            Connection connection = DBConnection.getConnection();
            String sql = "insert into admins (name,username,email,password) values (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, email);
            statement.setString(4, password);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Admin registered successfully!");
                backTologinEvent(null);
            } else {
                showAlert("Error", "Failed to register admin!");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                showAlert("Error", "Username or email already exists!");
            } else {
                showAlert("Error", "Database error: " + e.getMessage());
            }
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean emailValidation(String email)
    {
        int atPos = email.indexOf('@');
        int lastAtPos = email.lastIndexOf('@');
        if(atPos <=0 || atPos!=lastAtPos)
        {
            return false;
        }
        String local = email.substring(0,atPos);
        String domain = email.substring(atPos+1);

        if(local.isEmpty())
        {
            return false;
        }
        int dotPos = domain.lastIndexOf(".");

        if (dotPos<=0 || domain.length()-1 == dotPos)
        {
            return false;
        }
        String s = domain.substring(dotPos+1);
        if (s.length()<2)
        {
            return false;
        }
        return true;
    }

}
