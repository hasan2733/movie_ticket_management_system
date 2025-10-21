package bd.edu.seu.ticket_booking.AdminController;

import bd.edu.seu.ticket_booking.DB.DBConnection;
import bd.edu.seu.ticket_booking.HelloApplication;
import bd.edu.seu.ticket_booking.Model.MovieRevenue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.text.NumberFormat;
import java.util.Locale;

public class RevenueReportController implements Initializable {

    @FXML private BarChart<String, Number> revenueBarChart;
    @FXML private CategoryAxis movieAxis;
    @FXML private NumberAxis revenueAxis;
    @FXML private Label revenueLabel;

    @FXML
    public void backEvent(ActionEvent event) {
        HelloApplication.changeScene("adminDashboard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        revenueBarChart.setCategoryGap(40);
        movieAxis.setTickLabelRotation(0);
        movieAxis.setTickLabelFont(Font.font(12));
        revenueAxis.setTickLabelFont(Font.font(12));

        loadRevenueData();
    }

    private void loadRevenueData() {
        ObservableList<XYChart.Data<String, Number>> chartData = FXCollections.observableArrayList();
        double totalRevenue = 0;

        String sql = "select m.title, sum(p.amount) as total_revenue " +
                "from movies m " +
                "join bookings b on m.id = b.movie_id " +
                "join payments p on b.id = p.booking_id " +
                "group by m.title";

        try  {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                double revenue = rs.getDouble("total_revenue");
                totalRevenue += revenue;
                chartData.add(new XYChart.Data<>(title, revenue));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(chartData);

        revenueBarChart.getData().clear();
        revenueBarChart.getData().add(series);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        revenueLabel.setText(nf.format(totalRevenue) + " BDT");
    }
}
