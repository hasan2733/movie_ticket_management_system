package bd.edu.seu.ticket_booking.Model;

public class MovieRevenue {
    private String movieTitle;
    private int ticketSold;
    private double revenue;

    public MovieRevenue(String movieTitle, int ticketSold, double revenue) {
        this.movieTitle = movieTitle;
        this.ticketSold = ticketSold;
        this.revenue = revenue;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getTicketSold() {
        return ticketSold;
    }

    public void setTicketSold(int ticketSold) {
        this.ticketSold = ticketSold;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
