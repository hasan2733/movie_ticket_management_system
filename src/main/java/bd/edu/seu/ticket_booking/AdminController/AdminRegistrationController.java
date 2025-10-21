package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;


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

    private String generatedOtp;

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

        String enteredOpt = opt.getText().trim();
        if(generatedOtp == null)
        {
            showAlert("Error", "Please send and verify your OTP first!");
            return;
        }
        if(!enteredOpt.equals(generatedOtp))
        {
            showAlert("Error", "Invalid OTP! Please check your email again.");
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

    @FXML
    public TextField opt;
    @FXML
    public void sendEmailEvent()
    {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email first!");
            return;
        }

        if (!emailValidation(email)) {
            showAlert("Error", "Invalid email address!");
            return;
        }

        generatedOtp = String.valueOf(new Random().nextInt(900000) + 100000);


        String subject = "Your OTP for Movie Ticket Management System Registration";
        String body = "Dear Admin,\n\nYour OTP is: " + generatedOtp +
                "\n\nPlease use this code to complete your registration.\n\nThank you,\nMovie Ticket Management System";

        try {
            sendEmail(email, subject, body);
            showAlert("Success", "OTP sent successfully to " + email);
        } catch (Exception e) {
            showAlert("Error", "Failed to send OTP: " + e.getMessage());
        }
    }

    public void sendEmail(String toMail,String subject,String message)
    {
        final String fromEmail = "musfiqr856@gmail.com";
        final String fromPassword = "ifki wjyb twyu crzn";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to send email: " + e.getMessage());
        }
    }

}
