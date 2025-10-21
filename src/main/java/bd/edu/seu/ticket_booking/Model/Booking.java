package bd.edu.seu.ticket_booking.Model;

public class Booking {
    private int bookingId;
    private int customerId;
    private String customerName;
    private String movieTitle;
    private String seatNumbers;
    private double totalPrice;
    private String bookingDate;
    private String paymentStatus;

    public Booking(int bookingId, int customerId, String customerName, String movieTitle, String seatNumbers, double totalPrice, String bookingDate, String paymentStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.movieTitle = movieTitle;
        this.seatNumbers = seatNumbers;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.paymentStatus = paymentStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
