package com.example.motel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegController {

    @FXML
    private Button back;

    @FXML
    private Button reg;

    @FXML
    private TextField loginText;

    @FXML
    private TextField mailText;

    @FXML
    private TextField passText;

    @FXML
    private TextField telText;

    Connection conn = null;
    PreparedStatement pst = null;

    @FXML
    void clickReg(ActionEvent event) throws SQLException, IOException {
        conn = Connect.ConnectionDB();
        if (!loginText.getText().isEmpty() &&
                !passText.getText().isEmpty() &&
                !mailText.getText().isEmpty() &&
                !telText.getText().isEmpty()) {
            String sql = "INSERT INTO guests(login, password, email, phone_number, role) VALUES (?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            try {
                pst.setString(1,loginText.getText());
                pst.setString(2,passText.getText());
                pst.setString(3,mailText.getText());
                pst.setString(4,telText.getText());
                pst.setString(5,"Гость");

                pst.execute();

                JOptionPane.showMessageDialog(null, "Произошла регистрация!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(RegController.class.getResource("auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 230, 300);
            Stage stage = new Stage();
            stage.setTitle("Окно Авторизации");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

            Stage stage1 = (Stage) reg.getScene().getWindow();
            stage1.close();
        } else {
            JOptionPane.showMessageDialog(null,"Заполните поля!");
        }
    }

    @FXML
    void clickBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegController.class.getResource("auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 230, 300);
        Stage stage = new Stage();
        stage.setTitle("Окно Авторизации");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        Stage stage1 = (Stage) back.getScene().getWindow();
        stage1.close();
    }
}
