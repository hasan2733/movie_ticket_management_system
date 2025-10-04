package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Movie;
import bd.edu.seu.ticket_booking.Utitlity.CurrentMovie;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewMoviesController implements Initializable {

    @FXML
    public TableColumn<Movie, Number> DurationC;

    @FXML
    public TableColumn<Movie, Number> ImdbC;

    @FXML
    public Label durationField;

    @FXML
    public TableColumn<Movie, String> genreC;

    @FXML
    public Label genreField;

    @FXML
    public Label movieTItleField;

    @FXML
    public TableView<Movie> movieTable;

    @FXML
    public ImageView posterImage;

    @FXML
    public Label ratingField;

    @FXML
    public TextField searchField;

    @FXML
    public TableColumn<Movie, String> titleC;
    public static String myimagePath;
    public static String myMovie;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private Movie selectedMovie;

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("customerDashboard.fxml");
    }

    @FXML
    public void bookEvent(ActionEvent event) {
        if (selectedMovie != null) {
            CurrentMovie.setMovie(selectedMovie.getId(), selectedMovie.getTitle());
            HelloApplication.changeScene("booking.fxml");
        } else {
            showAlert("Error", "Please select a movie to book!");
        }
    }

    @FXML
    public void trailerEvent(ActionEvent event) {
        if (selectedMovie != null && selectedMovie.getTrailerUrl() != null)
        {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(selectedMovie.getTrailerUrl()));
            }
            catch (Exception e)
            {
                showAlert("Error", "Could not open trailer: " + e.getMessage());
            }
        }
        else
        {
            showAlert("Info", "No trailer available for this movie.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getTitle()));
        genreC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getGenre()));
        DurationC.setCellValueFactory(c-> new SimpleDoubleProperty(c.getValue().getDuration()));
        ImdbC.setCellValueFactory(c->new SimpleDoubleProperty(c.getValue().getRatings()));

        loadMovies();

        movieTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, val) -> {
                    if (val != null) {
                        selectedMovie = val;
                        showMovieDetails(val);
                    }
                }
        );

        if (!movieList.isEmpty()) {
            movieTable.getSelectionModel().selectFirst();
        }
    }

    private void loadMovies() {
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "select * from movies";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            movieList.clear();
            while (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getDouble("duration"),
                        resultSet.getDouble("ratings"),
                        resultSet.getString("trailer_url"),
                        resultSet.getString("image_path")
                );
                movieList.add(movie);
            }

            movieTable.setItems(movieList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load movies: " + e.getMessage());
        }
    }


    @FXML
    public void searchEvent(KeyEvent event) {
        String text = searchField.getText().toLowerCase();
        List<Movie>filtered = movieList.stream().filter(m->m.getTitle().toLowerCase().startsWith(text)
        ||m.getGenre().toLowerCase().startsWith(text) || String.valueOf(m.getRatings()).startsWith(text)).toList();
        movieTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void reviewEvent(ActionEvent event)
    {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            CurrentMovie.setMovie(selectedMovie.getId(), selectedMovie.getTitle());
            HelloApplication.changeScene("movieReview.fxml");
        } else {
            showAlert("Error", "Please select a movie first!");
        }
    }

    private void showMovieDetails(Movie movie) {
        movieTItleField.setText(movie.getTitle());
        myMovie = movie.getTitle();
        genreField.setText(movie.getGenre());
        durationField.setText(movie.getDuration() + " minutes");
        ratingField.setText(String.valueOf(movie.getRatings()));

        String imagePath = movie.getImagePath();
        myimagePath = imagePath;
        if (imagePath == null || imagePath.isEmpty()) {
            posterImage.setImage(null);
            return;
        }

        File file = new File(imagePath);
        if (file.exists()) {
            posterImage.setImage(new Image(file.toURI().toString()));
        }
        else {
            posterImage.setImage(null);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
