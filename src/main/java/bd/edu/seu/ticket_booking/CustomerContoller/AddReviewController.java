package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import bd.edu.seu.ticket_booking.Utitlity.CurrentMovie;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddReviewController implements Initializable {

    @FXML
    private TextArea commentBox;

    @FXML
    private ImageView moviePoster;

    @FXML
    private ChoiceBox<Double> ratings;

    @FXML
    private Label titleLabel;

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("movieReview.fxml");
    }

    @FXML
    public void saveButton(ActionEvent event) {
        Double rating = ratings.getValue();
        String comment = commentBox.getText();

        if (rating == null || comment.isEmpty()) {
            showAlert("Error", "Please select a rating and write a comment!");
            return;
        }

        try  {
            Connection connection = DBConnection.getConnection();
            String sql = "insert into movie_reviews (movie_id, user_id, rating, comment) values (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, CurrentMovie.getId());
            ps.setInt(2, CurrentUser.getId());
            ps.setDouble(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();

            showAlert("Success", "Review added successfully!");
            HelloApplication.changeScene("movieReview.fxml");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add review: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Double> obs = FXCollections.observableArrayList();
        obs.add(1.0);
        obs.add(2.0);
        obs.add(3.0);
        obs.add(4.0);
        obs.add(5.0);
        ratings.setItems(obs);
        titleLabel.setText(ViewMoviesController.myMovie);
        String imagePath = ViewMoviesController.myimagePath;

        if(imagePath!= null && !imagePath.isEmpty())
        {
            File file = new File(imagePath);
            if(file.exists())
            {
                moviePoster.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
