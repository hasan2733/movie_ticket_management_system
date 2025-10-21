package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Discount;
import bd.edu.seu.ticket_booking.Model.Movie;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageMoviesController implements Initializable {

    @FXML
    public TableColumn<Movie, Number> durationC;

    @FXML
    public TableColumn<Movie, String> genreC;

    @FXML
    public TableView<Movie>  movieTable;

    @FXML
    public TableColumn<Movie, Number> ratingC;

    @FXML
    public TableColumn<Movie, String> titleC;

    @FXML
    public TextField searchField;

     ObservableList<Movie> movies = FXCollections.observableArrayList();
     private FilteredList<Movie> filteredMovies;

    @FXML
    public void addNewMovieEvent(ActionEvent event) {
     HelloApplication.changeScene("addMovie.fxml");
    }

    @FXML
    public void backEvent(ActionEvent event) {
     HelloApplication.changeScene("adminDashboard.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTitle()));
        genreC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getGenre()));
        durationC.setCellValueFactory(c-> new SimpleDoubleProperty(c.getValue().getDuration()));
        ratingC.setCellValueFactory(c-> new SimpleDoubleProperty(c.getValue().getRatings()));

        loadMovies();
        movieTable.setItems(movies);
    }

    private void loadMovies() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "select * from movies";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            movies.clear();
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
                movies.add(movie);
            }


        } catch (SQLException e) {
            showAlert("Error", "Failed to load movies: " + e.getMessage());
        }
    }


    @FXML
    void searchEvent(KeyEvent event) {
     String text = searchField.getText().toLowerCase();
     List<Movie> filtered = movies.stream().filter(m->m.getTitle().toLowerCase().startsWith(text) ||
             m.getGenre().toLowerCase().startsWith(text) || String.valueOf(m.getRatings()).startsWith(text)
     || String.valueOf(m.getDuration()).startsWith(text)).toList();

     movieTable.setItems(FXCollections.observableArrayList(filtered));

    }

    @FXML
    private void deleteSelectedMovie() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
            showAlert("Error", "Please select a movie to delete.");
            return;
        }

        try  {
            Connection connection = DBConnection.getConnection();
            String sql = "delete from movies where id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, selectedMovie.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                movies.remove(selectedMovie);
                showAlert("Success", "Movie and all related data deleted successfully.");
            } else {
                showAlert("Error", "Movie could not be deleted.");
            }

        } catch (SQLException e) {
            showAlert("Error", "Failed to delete movie: " + e.getMessage());
        }
    }
}
