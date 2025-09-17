package com.example.motel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.motel.AuthController.userid;

public class Guest {

    @FXML
    private Label loginInfo;
    @FXML
    private Label mailInfo;
    @FXML
    private Label telInfo;

    @FXML
    private Button back;

    static ResultSet rss = null;
    Connection conn = null;
    PreparedStatement pst = null;

    @FXML
    void clickBack () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Guest.class.getResource("auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 320);
        Stage stage = new Stage();
        stage.setTitle("Motel - Авторизация");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        Stage stage1 = (Stage) back.getScene().getWindow();
        stage1.close();
    }


    @FXML
    void initialize() throws SQLException {

        conn = Connect.ConnectionDB();
        String sql = "SELECT * FROM guests WHERE id=?";
        assert conn != null;
        pst = conn.prepareStatement(sql);

        try {
            pst.setInt(1, userid);
            rss = pst.executeQuery();

            if (rss.next()) {
                loginInfo.setText(rss.getString(2));
                mailInfo.setText(rss.getString(4));
                telInfo.setText(rss.getString(5));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        loginInfo.setTooltip(new Tooltip(loginInfo.getText()));
        mailInfo.setTooltip(new Tooltip(mailInfo.getText()));
        telInfo.setTooltip(new Tooltip(telInfo.getText()));

    }
}
