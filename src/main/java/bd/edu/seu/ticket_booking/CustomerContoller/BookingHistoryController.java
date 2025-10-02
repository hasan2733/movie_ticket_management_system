package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.BookingRecord;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookingHistoryController implements Initializable {

    @FXML private TableView<BookingRecord> bookingTable;
    @FXML private TableColumn<BookingRecord, Number> idCol;
    @FXML private TableColumn<BookingRecord, String> movieCol;
    @FXML private TableColumn<BookingRecord, String> dateCol;
    @FXML private TableColumn<BookingRecord, String> timeCol;
    @FXML private TableColumn<BookingRecord, String> seatsCol;
    @FXML private TableColumn<BookingRecord, Number> priceCol;
    @FXML private TableColumn<BookingRecord, String> statusCol;

    @FXML private Label totalBookingsLabel;
    @FXML private Label totalSpentLabel;
    @FXML private TextField searchField;
    @FXML private Button viewButton;
    @FXML private Button cancelButton;

    private ObservableList<BookingRecord> bookingHistoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadBookingHistory();
        viewButton.setDisable(true);
        cancelButton.setDisable(true);
        bookingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean hasSelection = newSel != null;
            viewButton.setDisable(!hasSelection);
            cancelButton.setDisable(!hasSelection || !newSel.canCancel());
        });
    }

    private void setupTable() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        idCol.setCellValueFactory(c->new SimpleIntegerProperty(c.getValue().getBookingId()));
        movieCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getMovieTitle()));
        dateCol.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getBookingDate().format(dateFormatter)));
        timeCol.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getBookingDate().format(timeFormatter)));
        seatsCol.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getSeats()));
        priceCol.setCellValueFactory(c->new SimpleDoubleProperty(c.getValue().getPrice()));
        statusCol.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getStatus()));
    }

    private void loadBookingHistory() {
        bookingHistoryList.clear();

        String sql = """
            select 
                bh.booking_id, bh.movie_title, bh.showtime_date, bh.seat_numbers,
                bh.total_price, bh.payment_method, bh.booking_date, bh.payment_status
            from booking_history bh
            where bh.customer_id = ?
            order by bh.booking_date desc 
            limit 20
            """;

        try  {
            Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, CurrentUser.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String showTime = "18:00";
                BookingRecord record = new BookingRecord(
                        rs.getInt("booking_id"),
                        rs.getString("movie_title"),
                        rs.getDate("showtime_date").toString(),
                        showTime,
                        rs.getString("seat_numbers"),
                        rs.getDouble("total_price"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("booking_date").toLocalDateTime(),
                        rs.getString("payment_status")
                );
                bookingHistoryList.add(record);
            }

            bookingTable.setItems(bookingHistoryList);
            updateStatistics();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load booking history: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            bookingTable.setItems(bookingHistoryList);
            updateStatistics();
            return;
        }

        ObservableList<BookingRecord> filteredList = FXCollections.observableArrayList(bookingHistoryList.stream().filter(b-> String.valueOf(b.getBookingId()).startsWith(searchText) ||
                b.getMovieTitle().toLowerCase().startsWith(searchText)|| b.getSeats().toLowerCase().startsWith(searchText)||
                b.getShowDate().startsWith(searchText)|| b.getShowTime().startsWith(searchText) || b.getSeats().toLowerCase().startsWith(searchText)||
                String.valueOf(b.getPrice()).startsWith(searchText)).toList());


        bookingTable.setItems(filteredList);
        updateStatistics();
    }

    @FXML
    private void handleRefresh() {
        loadBookingHistory();
        searchField.clear();
        showAlert("Refreshed", "Booking history updated successfully!");
    }

    @FXML
    private void handleBackToDashboard() {
        HelloApplication.changeScene("customerDashboard.fxml");
    }

    @FXML
    private void handleView() {
        BookingRecord booking = bookingTable.getSelectionModel().getSelectedItem();
        if (booking == null) return;

        String details = String.format("""
             Movie: %s
             Show Date: %s
             Show Time: %s
             Seats: %s
             Amount: ৳%.2f
             Payment Method: %s
             Status: %s
             Booking ID: %d
             Booking Date: %s
            """,
                booking.getMovieTitle(),
                booking.getShowDate(),
                booking.getShowTime(),
                booking.getSeats(),
                booking.getPrice(),
                booking.getPaymentMethod(),
                booking.getStatus(),
                booking.getBookingId(),
                booking.getFormattedBookingDate()
        );

        showAlert("Booking Details", details);
    }

    @FXML
    private void handleCancel() {
        BookingRecord booking = bookingTable.getSelectionModel().getSelectedItem();
        if (booking == null) return;
        if (!booking.canCancel()) {
            showAlert("Not Allowed", "This booking can no longer be cancelled (past 24 hours or already cancelled).");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel this booking?\n\n" +
                        " Movie: " + booking.getMovieTitle() + "\n" +
                        " Seats: " + booking.getSeats() + "\n" +
                        " Amount: ৳" + booking.getPrice() + "\n" +
                        " Show Date: " + booking.getShowDate() + " at " + booking.getShowTime() + "\n\n" +
                        "This action cannot be undone.",
                ButtonType.YES, ButtonType.NO);

        if (confirmation.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try  {
                Connection connection = DBConnection.getConnection();
                connection.setAutoCommit(false);
                String updateBookingSql = "update bookings set payment_status = 'CANCELLED' where id = ?";
                PreparedStatement stmt = connection.prepareStatement(updateBookingSql);
                stmt.setInt(1, booking.getBookingId());
                stmt.executeUpdate();
                String updateHistorySql = "update booking_history set payment_status = 'CANCELLED' where booking_id = ?";
                stmt = connection.prepareStatement(updateHistorySql);
                stmt.setInt(1, booking.getBookingId());
                stmt.executeUpdate();
                connection.commit();
                loadBookingHistory();
                showAlert("Success", "Booking cancelled successfully!\nRefund will be processed within 3-5 business days.");

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to cancel booking: " + e.getMessage());
            }
        }
    }



    private void updateStatistics() {
        int totalBookings = bookingTable.getItems().size();
        double totalSpent = bookingTable.getItems().stream()
                .filter(booking -> "PAID".equalsIgnoreCase(booking.getStatus()))
                .mapToDouble(b->b.getPrice())
                .sum();

        totalBookingsLabel.setText(String.valueOf(totalBookings));
        totalSpentLabel.setText(String.format("৳%.0f", totalSpent));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
