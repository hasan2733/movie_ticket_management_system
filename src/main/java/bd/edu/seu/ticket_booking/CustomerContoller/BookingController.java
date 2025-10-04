package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.Utitlity.CurrentBooking;
import bd.edu.seu.ticket_booking.Utitlity.CurrentMovie;
import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class BookingController implements Initializable {

    @FXML
    private Label selectedMovieLabel;
    @FXML
    private DatePicker showDatePicker;
    @FXML
    private ComboBox<String> showTimeComboBox;
    @FXML
    private ComboBox<Integer> seatCountComboBox;
    @FXML
    private GridPane seatGridPane;
    @FXML
    private Label selectedSeatsLabel;
    @FXML
    private Label totalPriceLabel;

    private String selectedMovie;
    private int movieId;
    private double ticketPrice = 300.0;

    private final Set<String> selectedSeats = new HashSet<>();
    private final Set<String> bookedSeats = new HashSet<>();

    private final Map<String, Integer> showTimeToIdMap = new HashMap<>();
    private int showtimeId = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedMovie = CurrentMovie.getTitle();
        movieId = CurrentMovie.getId();
        selectedMovieLabel.setText(selectedMovie != null ? selectedMovie : "Unknown Movie");


        ObservableList<Integer> seatCounts = FXCollections.observableArrayList(1,2,3,4,5,6,7,8);
        seatCountComboBox.setItems(seatCounts);
        seatCountComboBox.getSelectionModel().selectFirst();

        showDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });


        showDatePicker.setValue(LocalDate.now());


        showDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadShowtimesForDate());

        showTimeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadBookedSeats());

        seatCountComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            while (selectedSeats.size() > newVal) {
                Iterator<String> it = selectedSeats.iterator();
                if (it.hasNext()) it.next();
                it.remove();
            }
            updateSelectedSeatsLabel();
            calculateTotalPrice();
            generateSeatLayout();
        });

        loadShowtimesForDate();
    }

    private void loadShowtimesForDate() {
        showTimeToIdMap.clear();
        showTimeComboBox.getItems().clear();
        showtimeId = 0;
        bookedSeats.clear();
        generateSeatLayout();

        LocalDate selectedDate = showDatePicker.getValue();
        if (selectedDate == null || movieId <= 0)
            return;

        try  {
            Connection conn = DBConnection.getConnection();
            String sql = "select id, show_time from showtimes where movie_id = ? and show_date = ? order by show_time";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, movieId);
                ps.setDate(2, java.sql.Date.valueOf(selectedDate));
                ResultSet rs = ps.executeQuery();
                List<String> displayTimes = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String dbShowTime = rs.getString("show_time");
                    String display = formatShowTime(dbShowTime);
                    if (!showTimeToIdMap.containsKey(display)) {
                        showTimeToIdMap.put(display, id);
                        displayTimes.add(display);
                    }
                }
                if (!displayTimes.isEmpty()) {
                    showTimeComboBox.setItems(FXCollections.observableArrayList(displayTimes));
                    showTimeComboBox.getSelectionModel().selectFirst();
                    loadBookedSeats();
                } else {
                    showTimeComboBox.getItems().clear();
                    showtimeId = 0;
                    bookedSeats.clear();
                    generateSeatLayout();
                }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load showtimes: " + e.getMessage());
        }
    }

    private String formatShowTime(String dbTime) {
        if (dbTime == null) return "";
        dbTime = dbTime.trim();
        try {
            LocalTime t = LocalTime.parse(dbTime);
            return t.format(DateTimeFormatter.ofPattern("h:mm a")).toLowerCase();
        } catch (DateTimeParseException ignored) {}
        try {
            DateTimeFormatter in = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
            LocalTime t = LocalTime.parse(dbTime.toUpperCase(), in);
            return t.format(DateTimeFormatter.ofPattern("h:mm a")).toLowerCase();
        } catch (DateTimeParseException ignored) {}

        return dbTime;
    }

    private void loadBookedSeats() {
        bookedSeats.clear();
        selectedSeats.clear();
        updateSelectedSeatsLabel();
        calculateTotalPrice();

        String selectedShowTimeDisplay = showTimeComboBox.getValue();
        LocalDate selectedDate = showDatePicker.getValue();
        if (selectedDate == null || selectedShowTimeDisplay == null) {
            generateSeatLayout();
            return;
        }

        Integer stId = showTimeToIdMap.get(selectedShowTimeDisplay);
        if (stId == null) {
            try  {
                Connection conn = DBConnection.getConnection();
                String sql = "select id from showtimes where movie_id = ? and show_date = ? LIMIT 1";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, movieId);
                ps.setDate(2, java.sql.Date.valueOf(selectedDate));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stId = rs.getInt("id");
                }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (stId == null) {
            generateSeatLayout();
            return;
        }

        showtimeId = stId;

        try  {
            Connection conn = DBConnection.getConnection();
            String sql = "select bs.seat_number from bookings b join booking_seats bs on b.id = bs.booking_id where b.showtime_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String seat = rs.getString("seat_number");
                if (seat != null && !seat.isEmpty()) bookedSeats.add(seat);
            }
        }
        catch (SQLException e)
        {
            showAlert("Error", "Failed to load booked seats: " + e.getMessage());
        }

        generateSeatLayout();
    }

    private void generateSeatLayout() {
        seatGridPane.getChildren().clear();

        int rows = 5, cols = 8;
        char rowChar = 'A';

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String seatNumber = rowChar + String.valueOf(col + 1);

                Button seatButton = new Button(seatNumber);
                seatButton.setPrefSize(36, 28);
                seatButton.setStyle("-fx-font-size: 11px;");

                if (bookedSeats.contains(seatNumber)) {
                    seatButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
                    seatButton.setDisable(true);
                } else if (selectedSeats.contains(seatNumber)) {

                    seatButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                } else {

                    seatButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: black;");
                }

                seatButton.setOnAction(e -> {
                    if (bookedSeats.contains(seatNumber)) {
                        generateSeatLayout();
                        showAlert("Info", "Sorry — that seat was just booked by someone else.");
                        return;
                    }
                    toggleSeatSelection(seatNumber);
                });

                seatGridPane.add(seatButton, col, row);
            }
            rowChar++;
        }
    }

    private void toggleSeatSelection(String seatNumber) {
        int maxSeats = seatCountComboBox.getValue() != null ? seatCountComboBox.getValue() : 1;

        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
        } else {
            if (selectedSeats.size() >= maxSeats) {
                showAlert("Info", "You can only select " + maxSeats + " seats.");
                return;
            }
            selectedSeats.add(seatNumber);
        }
        generateSeatLayout();
        updateSelectedSeatsLabel();
        calculateTotalPrice();
    }

    private void updateSelectedSeatsLabel() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsLabel.setText("Selected Seats: None");
        } else {
            List<String> sorted = new ArrayList<>(selectedSeats);
            Collections.sort(sorted);
            selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", sorted));
        }
    }

    private void calculateTotalPrice() {
        double total = selectedSeats.size() * ticketPrice;
        totalPriceLabel.setText(String.format("Total Price: %.2f", total));
    }

    @FXML
    private void proceedToPayment() {
        if (showDatePicker.getValue() == null) {
            showAlert("Error", "Please select a show date.");
            return;
        }
        if (showTimeComboBox.getValue() == null) {
            showAlert("Error", "Please select a show time.");
            return;
        }
        if (selectedSeats.isEmpty()) {
            showAlert("Error", "Please select at least one seat.");
            return;
        }


        for (String seat : selectedSeats) {
            if (bookedSeats.contains(seat)) {
                showAlert("Error", "One or more selected seats are no longer available. Please re-select.");
                loadBookedSeats();
                return;
            }
        }

        CurrentBooking.setMovieId(movieId);
        CurrentBooking.setMovieTitle(selectedMovie);
        CurrentBooking.setShowDate(showDatePicker.getValue());
        CurrentBooking.setShowTime(showTimeComboBox.getValue());
        CurrentBooking.setSelectedSeats(new ArrayList<>(selectedSeats));
        CurrentBooking.setTotalPrice(selectedSeats.size() * ticketPrice);
        CurrentBooking.setShowtimeId(showtimeId);

        HelloApplication.changeScene("payment.fxml");
    }

    @FXML
    private void backToMovies() {
        HelloApplication.changeScene("viewMovies.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
