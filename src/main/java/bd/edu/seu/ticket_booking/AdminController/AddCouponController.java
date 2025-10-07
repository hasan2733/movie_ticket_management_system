package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddCouponController implements Initializable {

    @FXML
    public ChoiceBox<String> applicableField;

    @FXML
    public TextField codeField;

    @FXML
    public DatePicker expirayField;

    @FXML
    public ChoiceBox<String> typeField;

    @FXML
    public TextField valueField;

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("manageDiscountAndCoupon.fxml");
    }

    @FXML
    public void saveEvent(ActionEvent event) {
        String code = codeField.getText().trim();
        String type = typeField.getValue();
        String applicable = applicableField.getValue();
        String expiryDate = expirayField.getValue() != null ? expirayField.getValue().toString() : null;
        String valueText = valueField.getText().trim();

        if (code.isEmpty() || type == null || applicable == null || expiryDate == null || valueText.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            double value = Double.parseDouble(valueText);

            try {
                Connection connection = DBConnection.getConnection();
                String sql = "insert into discounts values (?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, code);
                ps.setString(2, type);
                ps.setDouble(3, value);
                ps.setString(4, applicable);
                ps.setString(5, expiryDate);

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Success", "Discount/Coupon added successfully!");
                    HelloApplication.changeScene("manageDiscountAndCoupon.fxml");
                } else {
                    showAlert("Error", "Failed to add discount.");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Value must be a valid number.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String>obs1 = FXCollections.observableArrayList();
        ObservableList<String>obs2 = FXCollections.observableArrayList();
        obs1.add("FIXED");
        obs1.add("PERCENTAGE");
        typeField.setItems(obs1);
        obs2.add("ALL");
        obs2.add("SPECIFIC_MOVIE");
        obs2.add("NEW_CUSTOMER");
        applicableField.setItems(obs2);
        applicableField.setOnAction(Event->{
            String selected = applicableField.getValue();
            if (selected == null)
            {
                return;
            }
            if(selected.equals("SPECIFIC_MOVIE") || selected.equals("NEW_CUSTOMER"))
            {
                applicableField.getSelectionModel().clearSelection();
                showAlert("Alert","This selection now off , please select 'ALL'");
            }
        });
        expirayField.setDayCellFactory(d-> new DateCell(){
            @Override
            public void updateItem(LocalDate date,boolean empty){
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        typeField.getSelectionModel().selectFirst();
        applicableField.getSelectionModel().selectFirst();
        expirayField.setValue(LocalDate.now());
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
