package bd.edu.seu.ticket_booking.Utitlity;

import java.time.LocalDate;
import java.util.List;

public class CurrentBooking {
    private static int movieId;
    private static String movieTitle;
    private static LocalDate showDate;
    private static String showTime;
    private static List<String> selectedSeats;
    private static double totalPrice;
    private static int showtimeId;
    private static int bookingId;
    private static String transactionId;
    private static String couponCode;
    private static String paymentMethod;


    public static int getMovieId() { return movieId; }
    public static void setMovieId(int id) { movieId = id; }

    public static String getMovieTitle() { return movieTitle; }
    public static void setMovieTitle(String title) { movieTitle = title; }

    public static LocalDate getShowDate() { return showDate; }
    public static void setShowDate(LocalDate date) { showDate = date; }

    public static String getShowTime() { return showTime; }
    public static void setShowTime(String time) { showTime = time; }

    public static List<String> getSelectedSeats() { return selectedSeats; }
    public static void setSelectedSeats(List<String> seats) { selectedSeats = seats; }

    public static double getTotalPrice() { return totalPrice; }
    public static void setTotalPrice(double price) { totalPrice = price; }

    public static int getShowtimeId() { return showtimeId; }
    public static void setShowtimeId(int id) { showtimeId = id; }

    public static int getBookingId() { return bookingId; }
    public static void setBookingId(int id) { bookingId = id; }

    public static String getTransactionId() { return transactionId; }
    public static void setTransactionId(String id) { transactionId = id; }

    public static String getCouponCode() { return couponCode; }
    public static void setCouponCode(String code) { couponCode = code; }

    public static String getPaymentMethod() { return paymentMethod; }
    public static void setPaymentMethod(String method) { paymentMethod = method; }
}
