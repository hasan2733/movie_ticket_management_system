package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Booking;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminBookingViewController implements Initializable {

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, String> colBookingDate;

    @FXML
    private TableColumn<Booking, Number> colBookingId;

    @FXML
    private TableColumn<Booking, String> colCustomerName;

    @FXML
    private TableColumn<Booking, String> colMovieTitle;

    @FXML
    private TableColumn<Booking, String> colPaymentStatus;

    @FXML
    private TableColumn<Booking, String> colSeatNumbers;

    @FXML
    private TableColumn<Booking, Number> colTotalPrice;

    @FXML
    void backButton(ActionEvent event) {
        HelloApplication.changeScene("adminDashboard.fxml");
    }
    ObservableList<Booking> bookingList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTotalPrice.setCellValueFactory(c->new SimpleDoubleProperty(c.getValue().getTotalPrice()));
        colSeatNumbers.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getSeatNumbers()));
        colBookingDate.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getBookingDate()));
        colPaymentStatus.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getPaymentStatus()));
        colBookingId.setCellValueFactory(c->new SimpleIntegerProperty(c.getValue().getBookingId()));
        colCustomerName.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getCustomerName()));
        colMovieTitle.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getMovieTitle()));

        bookingTable.setItems(bookingList);


        loadBookings();

    }
    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(content);
        a.showAndWait();
    }

    public void loadBookings(){
        bookingList.clear();

        String sql = "select b.id, b.customer_id, b.movie_id, b.showtime_id, b.total_price, b.booking_date, b.payment_status, " +
                "c.name as customer_name " +
                "from bookings b " +
                "join customers c on b.customer_id = c.id " +
                "order by b.booking_date desc ";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int bookingId = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                int movieId = rs.getInt("movie_id");
                double totalPrice = rs.getDouble("total_price");
                String bookingDate = rs.getString("booking_date");
                String paymentStatus = rs.getString("payment_status");
                String customerName = rs.getString("customer_name");
                String movieTitle = resolveMovieTitle(conn, movieId);

                String seatNumbers = resolveSeatNumbers(conn, bookingId);

                Booking b = new Booking(
                        bookingId,
                        customerId,
                        customerName,
                        movieTitle,
                        seatNumbers,
                        totalPrice,
                        bookingDate,
                        paymentStatus
                );
                bookingList.add(b);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private String resolveMovieTitle(Connection conn, int movieId)
    {
        String t = null;
        if (movieId > 0) {
            String q1 = "select title from movies where id = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(q1);
                ps.setInt(1, movieId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    t = rs.getString("title");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    private String resolveSeatNumbers(Connection conn, int bookingId)
    {
        String seats = null;
        String q = "select GROUP_CONCAT(seat_number order by id SEPARATOR ', ') as seats from booking_seats where booking_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                seats = rs.getString("seats");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return seats;
    }


    @FXML
    public void cancleBooking(ActionEvent event) {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a booking to cancel.");
            return;
        }

        if ("CANCELLED".equalsIgnoreCase(selected.getPaymentStatus())) {
            showAlert("Already Cancelled", "This booking is already cancelled.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setContentText("Are you sure you want to cancel booking ID " + selected.getBookingId() + " ?");
        if (confirm.showAndWait().filter(b -> b == ButtonType.OK).isPresent())
        {
            String q = "update bookings set payment_status = 'CANCELLED' where id = ?";
            String deletePayment = "delete from payments where booking_id = ?";
            String updateHistory = "update booking_history set payment_status = 'CANCELLED' where booking_id = ?";
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(q);
                PreparedStatement ps2 = conn.prepareStatement(deletePayment);
                PreparedStatement ps3 = conn.prepareStatement(updateHistory);
                ps.setInt(1, selected.getBookingId());
                int updated = ps.executeUpdate();
                ps2.setInt(1, selected.getBookingId());
                ps2.executeUpdate();
                ps3.setInt(1, selected.getBookingId());
                ps3.executeUpdate();
                if (updated > 0) {
                    showAlert("Success", "Booking cancelled successfully.");
                    loadBookings();
                } else {
                    showAlert("No Change", "Booking not cancelled (maybe already cancelled).");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void rebook(ActionEvent event) {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a booking to rebook.");
            return;
        }

        if (!"CANCELLED".equalsIgnoreCase(selected.getPaymentStatus())) {
            showAlert("Invalid Operation", "Only cancelled bookings can be rebooked.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Rebook");
        confirm.setContentText("Rebook booking ID " + selected.getBookingId() + "?");

        if (confirm.showAndWait().filter(b -> b == ButtonType.OK).isPresent()) {
            String updateBooking = "update bookings set payment_status = 'PAID' where id = ?";
            String updateHistory = "update booking_history set payment_status = 'PAID' where booking_id = ?";
            String updatePayments = "insert into payments (booking_id, amount, payment_method, transaction_id, payment_date) values (?, ?, ?, ?, NOW())";
            try  {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps1 = conn.prepareStatement(updateBooking);
                PreparedStatement ps2 = conn.prepareStatement(updateHistory);
                PreparedStatement ps3 = conn.prepareStatement(updatePayments);
                ps1.setInt(1, selected.getBookingId());
                ps2.setInt(1, selected.getBookingId());
                ps3.setInt(1,selected.getBookingId());
                ps3.setDouble(2,selected.getTotalPrice());
                ps3.setString(3,"Cash");
                ps3.setString(4,java.util.UUID.randomUUID().toString());

                int updated = ps1.executeUpdate();
                ps2.executeUpdate();
                ps3.executeUpdate();

                if (updated > 0) {
                    showAlert("Success", "Booking rebooked successfully.");
                    loadBookings();
                } else {
                    showAlert("Failed", "Unable to rebook booking.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Failed to rebook booking.");
            }
        }
    }

    @FXML
    public void refresEvent(ActionEvent event) {
        loadBookings();
    }

    @FXML
    public void viewCustomer(ActionEvent event) {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a booking to view customer details.");
            return;
        }

        int customerId = selected.getCustomerId();
        String q = "select id, name, email, phone from customers where id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                StringBuilder info = new StringBuilder();
                info.append("ID: ").append(rs.getInt("id")).append("\n");
                info.append("Name: ").append(rs.getString("name")).append("\n");
                info.append("Email: ").append(rs.getString("email")).append("\n");
                info.append("Phone: ").append(rs.getString("phone")).append("\n");
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Customer Details");
                a.setContentText(info.toString());
                a.showAndWait();
            }
            else
            {
                showAlert("Not Found", "Customer not found in database.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}


