package bd.edu.seu.ticket_booking.Model;

public class Complaint {
    private int id;
    private String username;
    private String complaintType;
    private String message;

    public Complaint(int id, String username, String complaintType, String message) {
        this.id = id;
        this.username = username;
        this.complaintType = complaintType;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
