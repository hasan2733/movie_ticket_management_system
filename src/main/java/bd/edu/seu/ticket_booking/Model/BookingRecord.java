package bd.edu.seu.ticket_booking.Model;

import java.time.LocalDateTime;

public class BookingRecord {
    private final int bookingId;
    private final String movieTitle;
    private final String showDate;
    private final String showTime;
    private final String seats;
    private final double price;
    private final String paymentMethod;
    private final LocalDateTime bookingDate;
    private final String status;

    public BookingRecord(int bookingId, String movieTitle, String showDate, String showTime,
                         String seats, double price, String paymentMethod,
                         LocalDateTime bookingDate, String status) {
        this.bookingId = bookingId;
        this.movieTitle = movieTitle;
        this.showDate = showDate;
        this.showTime = showTime;
        this.seats = seats;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public String getMovieTitle() { return movieTitle; }
    public String getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public String getSeats() { return seats; }
    public double getPrice() { return price; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public String getStatus() { return status; }


    public boolean canCancel() {
        if (!"PAID".equalsIgnoreCase(status)) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        long hoursSinceBooking = java.time.temporal.ChronoUnit.HOURS.between(bookingDate, now);
        LocalDateTime showDateTime = LocalDateTime.parse(showDate + "T" + showTime + ":00");
        boolean showtimeInFuture = showDateTime.isAfter(now);

        return hoursSinceBooking < 24 && showtimeInFuture;
    }

    public String getFormattedBookingDate() {
        return bookingDate.toString().replace("T", " ");
    }
}