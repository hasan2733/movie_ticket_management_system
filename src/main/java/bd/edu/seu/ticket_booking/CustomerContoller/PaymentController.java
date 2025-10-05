package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentBooking;
import bd.edu.seu.ticket_booking.Utitlity.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.List;
import java.util.UUID;


public class PaymentController implements Initializable {

    @FXML private Label movieLabel;
    @FXML private Label showTimeLabel;
    @FXML private Label seatsLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private TextField paymentAmountField;
    @FXML private TextField couponField;

    private double totalAmount;
    private boolean paymentProcessed = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieLabel.setText(CurrentBooking.getMovieTitle());
        showTimeLabel.setText(CurrentBooking.getShowDate() + " " + CurrentBooking.getShowTime());
        String seats = String.join(", ", CurrentBooking.getSelectedSeats());
        seatsLabel.setText(seats);
        totalAmount = CurrentBooking.getTotalPrice();
        totalAmountLabel.setText(String.format("৳%.2f", totalAmount));
    }

    @FXML
    private void applyCoupon() {
        String code = couponField.getText().trim();
        if (code.isEmpty()) {
            showAlert("Error", "Please enter a coupon code.");
            return;
        }
        try  {
            Connection connection = DBConnection.getConnection();
            String sql = "select type, value from discounts where code = ? and expiry_date >= CURDATE()";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                double value = rs.getDouble("value");
                double discount = "FIXED".equalsIgnoreCase(type)
                        ? value
                        : (totalAmount * value) / 100.0;

                if (discount < 0) discount = 0;
                totalAmount = Math.max(0, totalAmount - discount);
                CurrentBooking.setTotalPrice(totalAmount);
                CurrentBooking.setCouponCode(code);

                totalAmountLabel.setText(String.format("৳%.2f (Coupon Applied)", totalAmount));
                showAlert("Success", "Coupon applied successfully! Discount: ৳" + discount);
            } else {
                showAlert("Invalid", "Coupon code is invalid or expired.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to apply coupon: " + e.getMessage());
        }
    }

    @FXML
    private void processPayment() {
        try {
            double paidAmount = Double.parseDouble(paymentAmountField.getText());
            if (paidAmount >= totalAmount) {
                paymentStatusLabel.setText("Payment Successful!");
                paymentStatusLabel.setStyle("-fx-text-fill: #27ae60;");
                paymentProcessed = true;
                savePaymentToDatabase(paidAmount);
                showAlert("Success", "Payment processed successfully! You can now generate your ticket.");
            } else {
                paymentStatusLabel.setText("Insufficient Amount!");
                paymentStatusLabel.setStyle("-fx-text-fill: #e74c3c;");
                paymentProcessed = false;
                showAlert("Error", "Please pay the full amount of ৳" + String.format("%.2f", totalAmount));
            }
        } catch (NumberFormatException e) {
            paymentStatusLabel.setText("Invalid Amount!");
            paymentStatusLabel.setStyle("-fx-text-fill: #e74c3c;");
            paymentProcessed = false;
            showAlert("Error", "Please enter a valid payment amount.");
        }
    }


    private void savePaymentToDatabase(double paidAmount) {
        String transactionId = UUID.randomUUID().toString();

        try  {
            Connection connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            int customerId = CurrentUser.isLoggedIn() ? CurrentUser.getId() : 9999;
            int movieId = CurrentBooking.getMovieId() > 0 ? CurrentBooking.getMovieId() : 9999;
            int showtimeId = CurrentBooking.getShowtimeId() > 0 ? CurrentBooking.getShowtimeId() : 9999;
            double finalAmount = totalAmount > 0 ? totalAmount : 0.0;

            String bookingSql = "insert into bookings (customer_id, movie_id, showtime_id, total_price, booking_date, payment_status) " +
                    "values (?, ?, ?, ?, NOW(), 'PAID')";
            int bookingId;
            PreparedStatement bookingStmt = connection.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS);
            bookingStmt.setInt(1, customerId);
            bookingStmt.setInt(2, movieId);
            bookingStmt.setInt(3, showtimeId);
            bookingStmt.setDouble(4, finalAmount);

            if (bookingStmt.executeUpdate() == 0) {
                throw new SQLException("Booking insert failed");
            }

            ResultSet keys = bookingStmt.getGeneratedKeys();
            if (!keys.next()) throw new SQLException("No booking ID returned");
            bookingId = keys.getInt(1);
            CurrentBooking.setBookingId(bookingId);

            List<String> seats = CurrentBooking.getSelectedSeats();
            if (seats != null && !seats.isEmpty()) {
                String seatSql = "insert into booking_seats (booking_id, seat_number) values (?, ?)";
                PreparedStatement seatStmt = connection.prepareStatement(seatSql);
                for (String seat : seats) {
                    seatStmt.setInt(1, bookingId);
                    seatStmt.setString(2, seat != null && !seat.isEmpty() ? seat : "nai");
                    seatStmt.addBatch();
                }
                seatStmt.executeBatch();
            }


            String paymentSql = "insert into payments (booking_id, amount, payment_method, transaction_id, payment_date) " +
                    "values (?, ?, ?, ?, NOW())";
            PreparedStatement paymentStmt = connection.prepareStatement(paymentSql);
            paymentStmt.setInt(1, bookingId);
            paymentStmt.setDouble(2, paidAmount > 0 ? paidAmount : 0.0);
            paymentStmt.setString(3, "Cash");
            paymentStmt.setString(4, transactionId != null ? transactionId : "nai");
            paymentStmt.executeUpdate();

            connection.commit();
            CurrentBooking.setPaymentMethod("Cash");
            CurrentBooking.setTransactionId(transactionId);
            saveToBookingHistory(bookingId, paidAmount);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save payment: " + e.getMessage());
        }
    }

    @FXML
    private void generateTicket() {
        if (!paymentProcessed) {
            showAlert("Error", "Please process payment first before generating ticket.");
            return;
        }
        HelloApplication.changeScene("ticketConfirm.fxml");
    }

    @FXML
    private void backToBooking() {
        HelloApplication.changeScene("booking.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void saveToBookingHistory(int bookingId, double paidAmount) {
        String sql = "insert into booking_history (booking_id, customer_id, movie_title, showtime_date, " +
                "seat_numbers, total_price, payment_method, booking_date, payment_status) " +
                "values (?, ?, ?, ?, ?, ?, ?, NOW(), 'PAID')";

        try  {
            Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, bookingId);
            stmt.setInt(2, CurrentUser.getId());
            stmt.setString(3, CurrentBooking.getMovieTitle());
            stmt.setDate(4, Date.valueOf(CurrentBooking.getShowDate()));
            stmt.setString(5, String.join(", ", CurrentBooking.getSelectedSeats()));
            stmt.setDouble(6, paidAmount);
            stmt.setString(7, "Cash");

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(" Note: Failed to save booking history - " + e.getMessage());
        }
    }
}

