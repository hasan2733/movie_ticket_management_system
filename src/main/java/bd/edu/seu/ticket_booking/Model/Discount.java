package bd.edu.seu.ticket_booking.Model;

public class Discount {
    private String code;
    private String type;
    private double value;
    private String applicableTo;
    private String expiryDate;

    public Discount(String code, String type, double value, String applicableTo, String expiryDate) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.applicableTo = applicableTo;
        this.expiryDate = expiryDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
