package com.example.motel;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.HAND;

public class AuthController {
    @FXML
    private Label linkReg;

    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passText;
    @FXML
    private Button Join;

    public static int userid;

    static ResultSet rss = null;
    Connection conn = null;
    PreparedStatement pst = null;

    public void clickJoin() throws SQLException {
        conn = Connect.ConnectionDB();
        String sql = "SELECT * FROM guests WHERE login=? AND password=?";
        assert conn != null;
        pst = conn.prepareStatement(sql);

        try {
            pst.setString(1,loginText.getText());
            pst.setString(2,passText.getText());
            rss = pst.executeQuery();

            if (rss.next()) {
                String pass = rss.getString(3);

                userid = rss.getInt(1);


                String Role = rss.getString(6);

                if ("Гость".equals(Role)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Guest.class.getResource("guest.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    Stage stage = new Stage();
                    stage.setTitle("Motel - Ваш профиль");
                    stage.setScene(scene);
                    stage.show();
                    stage.setResizable(false);

                    Stage stage1 = (Stage) Join.getScene().getWindow();
                    stage1.close();
                } else if ("Админ".equals(Role)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Guest.class.getResource("admin.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 940, 520);
                    Stage stage = new Stage();
                    stage.setTitle("Motel - Админ меню");
                    stage.setScene(scene);
                    stage.show();
//                    stage.setResizable(false);

                    stage.setMinWidth(960);
                    stage.setMinHeight(560);

                    Stage stage1 = (Stage) Join.getScene().getWindow();
                    stage1.close();
                }
            } else if (loginText.getText().isEmpty() && passText.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Введите поля.");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка в строке 'Логин' или 'Пароль'!");
                passText.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void clickLinkReg () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegController.class.getResource("reg.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 350);
        Stage stage = new Stage();
        stage.setTitle("Окно Регистрации");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        Stage stage1 = (Stage) linkReg.getScene().getWindow();
        stage1.close();
    }


    @FXML
    void clickExit () {System.exit(0);}

    @FXML
    void initialize() {

        linkReg.setOnMouseEntered(event -> {
            linkReg.setCursor(HAND);
            linkReg.setStyle("-fx-text-fill: #f54b64");
        });

        linkReg.setOnMouseExited(event -> {
            linkReg.setCursor(DEFAULT);
            linkReg.setStyle("-fx-text-fill: rgb(255, 255, 255)");
        });

    }
}