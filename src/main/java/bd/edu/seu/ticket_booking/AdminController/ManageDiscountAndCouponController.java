package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.Model.Discount;
import bd.edu.seu.ticket_booking.HelloApplication;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageDiscountAndCouponController implements Initializable {

    @FXML public Button addNewDiscountEvent;
    @FXML public Button backEvent;
    @FXML public Button deleteDiscountButton;

    @FXML public TableView<Discount> discountTable;
    @FXML public TableColumn<Discount, String> codeC;
    @FXML public TableColumn<Discount, String> typeC;
    @FXML public TableColumn<Discount, Number> valueC;
    @FXML public TableColumn<Discount, String> applicableC;
    @FXML public TableColumn<Discount, String> expiryDateC;

    private final ObservableList<Discount> discountList = FXCollections.observableArrayList();

    @FXML
    public void addNewDiscountEvent(ActionEvent event) {
        HelloApplication.changeScene("addCoupon.fxml");
    }

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("adminDashboard.fxml");
    }

    @FXML
    public void deleteDiscountEvent(ActionEvent event) {
        Discount selected = discountTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a discount to delete.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM discounts WHERE code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, selected.getCode());
            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                discountList.remove(selected);
                showAlert("Success", "Discount deleted successfully!");
            } else {
                showAlert("Error", "Failed to delete discount!");
            }

        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
        }
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getCode()));
        typeC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getType()));
        valueC.setCellValueFactory(c-> new SimpleDoubleProperty(c.getValue().getValue()));
        applicableC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getApplicableTo()));
        expiryDateC.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getExpiryDate()));

        discountTable.setItems(discountList);

        loadDiscounts();
    }
}
