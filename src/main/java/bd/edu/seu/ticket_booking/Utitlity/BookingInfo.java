package bd.edu.seu.ticket_booking.Utitlity;

import java.util.List;

public class BookingInfo {
    private static BookingInfo instance;
    private List<String> selectedSeats;
    private String showTime;
    private double totalPrice;

    private int bookingId = -1;
    private int movieId = -1;
    private String movieTitle;

    private BookingInfo() {}

    public static BookingInfo getInstance() {
        if (instance == null) {
            instance = new BookingInfo();
        }
        return instance;
    }


    public List<String> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<String> selectedSeats) { this.selectedSeats = selectedSeats; }

    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }


    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
}
