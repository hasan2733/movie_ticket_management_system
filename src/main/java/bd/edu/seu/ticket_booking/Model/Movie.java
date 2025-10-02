package bd.edu.seu.ticket_booking.Model;
public class Movie {
    private int Id;
    private String title;
    private String genre;
    private double duration;
    private double ratings;
    private String trailerUrl;
    private String imagePath;

    public Movie(int id, String title, String genre, double duration, double ratings, String trailerUrl, String imagePath) {
        Id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.ratings = ratings;
        this.trailerUrl = trailerUrl;
        this.imagePath = imagePath;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}