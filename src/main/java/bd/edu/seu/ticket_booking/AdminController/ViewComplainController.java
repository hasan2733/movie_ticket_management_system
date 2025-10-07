package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.Complaint;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewComplainController implements Initializable {

    @FXML
    private TableColumn<Complaint, String> commentsC;

    @FXML
    private TextArea datailsComments;

    @FXML
    private TableColumn<Complaint, String> nameC;

    @FXML
    private Label nameLabel;

    @FXML
    private TableView<Complaint> tableView;

    @FXML
    private TableColumn<Complaint, String> typeC;

    @FXML
    private Label typeOfComplainLabel;

    @FXML
    public TextField searchField;

    ObservableList<Complaint> complaintsList = FXCollections.observableArrayList();

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("adminDashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    nameC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getUsername()));
    typeC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getComplaintType()));
    commentsC.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getMessage()));

    loadComplaints();
    tableView.getSelectionModel().selectedItemProperty().addListener((obs,old,val)->{
        if(val!=null)
        {
            nameLabel.setText(val.getUsername());
            typeOfComplainLabel.setText(val.getComplaintType());
            datailsComments.setText(val.getMessage());
            datailsComments.setDisable(true);
        }
    });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void loadComplaints()
    {
        complaintsList.clear();
        String sql = "select * from complaints";

        try  {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                complaintsList.add(new Complaint(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("complaint_type"),
                        rs.getString("message")
                ));
            }
            tableView.setItems(complaintsList);
            tableView.setStyle("-fx-text-fill: white;");


            if (!complaintsList.isEmpty()) {
                tableView.getSelectionModel().selectFirst();
                Complaint first = tableView.getSelectionModel().getSelectedItem();
                if (first != null) {
                    nameLabel.setText(first.getUsername());
                    typeOfComplainLabel.setText(first.getComplaintType());
                    datailsComments.setText(first.getMessage());
                    datailsComments.setDisable(true);
                    nameLabel.setStyle("-fx-text-fill: red");
                    typeOfComplainLabel.setStyle("-fx-text-fill: red");
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            showAlert("Alert", "Database Error: " + e.getMessage());
        }
    }
    @FXML
    void searchEvent(KeyEvent event) {
    String text = searchField.getText().toLowerCase();
    List<Complaint> obs = complaintsList.stream().filter(c->c.getComplaintType().toLowerCase().startsWith(text)
    || c.getUsername().toLowerCase().startsWith(text)|| c.getMessage().toLowerCase().startsWith(text)).toList();

    tableView.setItems(FXCollections.observableArrayList(obs));
    }
}
