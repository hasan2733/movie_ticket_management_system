package bd.edu.seu.ticket_booking.Model;

public class Review {
    private int id;
    private String title;
    private double ratings;
    private String comment;

    public Review(int id, String title, double ratings, String comment) {
        this.id = id;
        this.title = title;
        this.ratings = ratings;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
