package bd.edu.seu.ticket_booking.CustomerContoller;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Discount;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShowCouponController implements Initializable {

    @FXML
    private TableColumn<Discount, String> ExpiryDateC;

    @FXML
    private TableColumn<Discount, String> codeC;

    @FXML
    private TableView<Discount> tableView;

    @FXML
    private TableColumn<Discount, String> typeC;

    @FXML
    private TableColumn<Discount, Number> valueC;

    ObservableList<Discount> discountList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getCode()));
        ExpiryDateC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getExpiryDate()));
        typeC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getType()));
        valueC.setCellValueFactory(c->new SimpleDoubleProperty(c.getValue().getValue()));
        loadDiscounts();
        tableView.setItems(discountList);
    }

    private void loadDiscounts() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "select * from discounts";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            discountList.clear();
            while (rs.next()) {
                Discount discount = new Discount(
                        rs.getString("code"),
                        rs.getString("type"),
                        rs.getDouble("value"),
                        rs.getString("applicable_to"),
                        rs.getString("expiry_date")
                );
                discountList.add(discount);
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load discounts: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void backEvent()
    {
        HelloApplication.changeScene("payment.fxml");
    }
}