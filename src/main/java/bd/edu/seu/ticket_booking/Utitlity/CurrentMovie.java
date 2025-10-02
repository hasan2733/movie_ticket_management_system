package bd.edu.seu.ticket_booking.Utitlity;

public class CurrentMovie {
    private static int id;
    private static String title;

    public static int getId() {
        return id;
    }

    public static void setMovie(int movieId, String movieTitle) {
        id = movieId;
        title = movieTitle;
    }

    public static String getTitle() {
        return title;
    }
}