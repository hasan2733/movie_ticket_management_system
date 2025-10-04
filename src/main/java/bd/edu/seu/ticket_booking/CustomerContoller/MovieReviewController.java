package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Review;
import bd.edu.seu.ticket_booking.Utitlity.CurrentMovie;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MovieReviewController implements Initializable {

    @FXML
    private TableColumn<Review, String> comentC;

    @FXML
    private TextArea commentField;

    @FXML
    private TableColumn<Review, Number> ratingsC;

    @FXML
    private Label ratingsLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Review> tableview;

    @FXML
    private TableColumn<Review, String> titleC;

    @FXML
    private Label titleLabel;

    @FXML
    public void backButton(ActionEvent event) {
        HelloApplication.changeScene("viewMovies.fxml");
    }
    private ObservableList<Review> reviewList = FXCollections.observableArrayList();
    @FXML
    public void reviewEvent(ActionEvent event) {
    HelloApplication.changeScene("addReview.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleC.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        ratingsC.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getRatings()));
        comentC.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getComment()));

        loadReviews();

        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                titleLabel.setText(newSel.getTitle());
                ratingsLabel.setText(String.valueOf(newSel.getRatings()));
                commentField.setText(newSel.getComment());
                commentField.setDisable(true);
            }
        });

    }

    private void loadReviews() {
        try {
            Connection connection = DBConnection.getConnection();

            String sql = """
                select r.id, m.title, r.rating, r.comment
                from movie_reviews r
                join movies m on r.movie_id = m.id
                where m.id = ?
                order by r.id desc
            """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, CurrentMovie.getId());
            ResultSet rs = ps.executeQuery();


            reviewList.clear();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("rating"),
                        rs.getString("comment")
                );
                reviewList.add(review);
            }
            tableview.setItems(reviewList);
            if (!reviewList.isEmpty()) {
                tableview.getSelectionModel().selectFirst();
                Review first = tableview.getSelectionModel().getSelectedItem();
                if (first != null) {
                    titleLabel.setText(first.getTitle());
                    ratingsLabel.setText(String.valueOf(first.getRatings()));
                    commentField.setText(first.getComment());
                    commentField.setDisable(true);
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load reviews: " + e.getMessage());
        }
    }
    @FXML
    public void searchEvent(KeyEvent even) {
        String text = searchField.getText().toLowerCase();
        List<Review>  filtered = reviewList.stream()
                .filter(r -> r.getTitle().toLowerCase().contains(text))
                .toList();
        tableview.setItems(FXCollections.observableArrayList(filtered));
    }
}
