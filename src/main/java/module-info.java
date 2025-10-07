module bd.edu.seu.ticket_booking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.google.zxing;
    requires javafx.swing;
    requires mysql.connector.j;


    opens bd.edu.seu.ticket_booking to javafx.fxml;
    exports bd.edu.seu.ticket_booking;
    exports bd.edu.seu.ticket_booking.AdminController;
    opens bd.edu.seu.ticket_booking.AdminController to javafx.fxml;
    exports bd.edu.seu.ticket_booking.CustomerContoller;
    opens bd.edu.seu.ticket_booking.CustomerContoller to javafx.fxml;
    exports bd.edu.seu.ticket_booking.Utitlity;
    opens bd.edu.seu.ticket_booking.Utitlity to javafx.fxml;
    exports bd.edu.seu.ticket_booking.Model;
    opens bd.edu.seu.ticket_booking.Model to javafx.fxml;
    exports bd.edu.seu.ticket_booking.DB;
    opens bd.edu.seu.ticket_booking.DB to javafx.fxml;
    exports bd.edu.seu.ticket_booking.Login;
    opens bd.edu.seu.ticket_booking.Login to javafx.fxml;
}