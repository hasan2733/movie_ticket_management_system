package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;

public class AddMovieController {

    @FXML
    public TextField durationField;

    @FXML
    public TextField genreField;

    @FXML
    public ImageView posterImage;

    @FXML
    public TextField ratingsField;

    @FXML
    public TextField titleField;

    @FXML
    public TextField urlField;

    private String imagePath;

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("manageMovies.fxml");
    }

    @FXML
    public void saveEvent(ActionEvent event) {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String durationStr = durationField.getText().trim();
        String ratingsStr = ratingsField.getText().trim();
        String trailerUrl = urlField.getText().trim();

        if (title.isEmpty() || genre.isEmpty() || durationStr.isEmpty() || ratingsStr.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        try {
            double duration = Double.parseDouble(durationStr);
            double ratings = Double.parseDouble(ratingsStr);

            if (duration <= 0 || ratings < 0 || ratings > 10) {
                showAlert("Error", "Duration must be positive and ratings must be between 0-10!");
                return;
            }

            Connection connection = DBConnection.getConnection();

            String sql = "insert into movies (title, genre, duration, ratings, trailer_url, image_path) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setString(2, genre);
            statement.setDouble(3, duration);
            statement.setDouble(4, ratings);
            statement.setString(5, trailerUrl.isEmpty() ? null : trailerUrl);
            statement.setString(6, imagePath != null ? imagePath : null);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                int movieId = -1;
                if (generatedKeys.next()) {
                    movieId = generatedKeys.getInt(1);
                }

                String[] times = {"10:00 am", "01:00 pm", "04:00 pm", "07:00 pm"};
                String insertShowtimeSql = "insert into showtimes (movie_id, show_time, show_date) values (?, ?, ?)";
                PreparedStatement showtimeStmt = connection.prepareStatement(insertShowtimeSql);

                LocalDate today = LocalDate.now();
                for (int i = 0; i < 30; i++) {
                    LocalDate showDate = today.plusDays(i);
                    for (String time : times) {
                        showtimeStmt.setInt(1, movieId);
                        showtimeStmt.setString(2, time);
                        showtimeStmt.setDate(3, Date.valueOf(showDate));
                        showtimeStmt.addBatch();
                    }
                }
                showtimeStmt.executeBatch();
                showtimeStmt.close();

                showAlert("Success", "Movie and showtimes added successfully!");
                backEvent(null);
            } else {
                showAlert("Error", "Failed to add movie!");
            }

            statement.close();
            connection.close();
        } catch (NumberFormatException e) {
            showAlert("Error", "Duration and ratings must be valid numbers!");
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    @FXML
    public void uploadEvent(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Movie Poster");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) posterImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                Path imageDir = Paths.get("movie_posters");
                if (!Files.exists(imageDir)) {
                    Files.createDirectories(imageDir);
                }

                String fileName = System.currentTimeMillis() + "_" + file.getName();
                Path destination = imageDir.resolve(fileName);
                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                imagePath = "movie_posters/" + fileName;

                Image image = new Image(file.toURI().toString());
                posterImage.setImage(image);

                showAlert("Success", "Image uploaded successfully!");
            } catch (IOException e) {
                showAlert("Error", "Failed to upload image: " + e.getMessage());
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
}
