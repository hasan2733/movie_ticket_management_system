package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Utitlity.CurrentBooking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketController implements Initializable {

    @FXML private Label movieLabel;
    @FXML private Label showTimeLabel;
    @FXML private Label seatsLabel;
    @FXML private Label bookingIdLabel;
    @FXML private Label transactionIdLabel;
    @FXML private Label amountLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private ImageView qrCodeImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieLabel.setText(CurrentBooking.getMovieTitle());
        showTimeLabel.setText(CurrentBooking.getShowDate() + " " + CurrentBooking.getShowTime());
        seatsLabel.setText(String.join(", ", CurrentBooking.getSelectedSeats()));
        bookingIdLabel.setText("AH" + CurrentBooking.getBookingId());
        transactionIdLabel.setText(CurrentBooking.getTransactionId());
        amountLabel.setText("BDT" + String.format("%.2f", CurrentBooking.getTotalPrice()));
        paymentMethodLabel.setText("BKash");

        String qrData = "Movie: " + CurrentBooking.getMovieTitle() +
                "\nDate & Time: " + CurrentBooking.getShowDate() + " " + CurrentBooking.getShowTime() +
                "\nSeats: " + String.join(", ", CurrentBooking.getSelectedSeats()) +
                "\nBooking ID: BKG" + CurrentBooking.getBookingId() +
                "\nTransaction ID: " + CurrentBooking.getTransactionId() +
                "\nAmount Paid: BDT" + String.format("%.2f", CurrentBooking.getTotalPrice()) +
                "\nPayment Method: BKash";

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 250, 250);

            BufferedImage bufferedImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 250; x++) {
                for (int y = 0; y < 250; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            qrCodeImageView.setImage(fxImage);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMovies() {
        HelloApplication.changeScene("viewMovies.fxml");
    }
}
