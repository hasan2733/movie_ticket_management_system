package bd.edu.seu.ticket_booking.Utitlity;

public class CurrentUser {
    private static int id;
    private static String name;
    private static String email;
    private static String phone;
    private static String type;

    public static void setUser(int userId, String userName, String userEmail, String userType) {
        id = userId;
        name = userName;
        email = userEmail;
        type = userType;
    }

    public static void setUser(int userId, String userName, String userEmail, String userPhone, String userType) {
        id = userId;
        name = userName;
        email = userEmail;
        phone = userPhone;
        type = userType;
    }

    public static void clear() {
        id = 0;
        name = null;
        email = null;
        phone = null;
        type = null;
    }


    public static int getId() { return id; }
    public static String getName() { return name; }
    public static String getEmail() { return email; }
    public static String getPhone() { return phone; }
    public static String getType() { return type; }

    public static boolean isAdmin() { return "admin".equals(type); }
    public static boolean isCustomer() { return "customer".equals(type); }
    public static boolean isLoggedIn() { return id > 0; }
}