package com.example.motel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.HAND;

public class Admin {
    @FXML
    private VBox vbox;
    @FXML
    private ScrollPane scrollGuests;
    @FXML
    private ScrollPane scrollRooms;
    @FXML
    private HBox hbox;

    @FXML
    private Button back;
    @FXML
    private Button add;
    @FXML
    private TextField loginText, passText, mailText, telText;
    @FXML
    private TextField roomNum;
    @FXML
    private TextField search;


    String idSafe = "";

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

    @FXML
    void guestTable() throws SQLException {
        ResultSet rss = Connect.getAllGuest();

        VBox guestVBox = new VBox();


        scrollGuests.setStyle("-fx-background: #222a35; -fx-background-color: transparent;");
        scrollGuests.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        scrollGuests.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        guestVBox.setAlignment(Pos.CENTER);
        guestVBox.setSpacing(20);
        guestVBox.setPadding(new Insets(10, 10, 10, 10));
        VBox.setVgrow(scrollGuests, Priority.ALWAYS);
        HBox.setHgrow(hbox, Priority.ALWAYS);

        try {
            while (rss.next()) {
                int id = rss.getInt("id");
                String login = rss.getString("login");
                String pass = rss.getString("password");
                String mail = rss.getString("email");
                String telephone = rss.getString("phone_number");
                String role = rss.getString("role");


                if (role.equals("Гость")) {
                    VBox container = new VBox();
                    container.setStyle("-fx-border-color: white; -fx-border-radius: 10px;");

                    Label itemGuestID = new Label("ID: " + id);
                    itemGuestID.setTextFill(Color.valueOf("#f54b64"));
                    itemGuestID.setStyle("-fx-text-fill: #f54b64; -fx-font-family: DaMiOne; -fx-font-size: 15px");
                    itemGuestID.setPadding(new Insets(10, 30, 0,30));

                    Label itemGuest = new Label("Логин: " + login + "\nПароль: " + pass + "\nПочта: " + mail + "\nТел.: " + telephone);
                    itemGuest.setStyle("-fx-text-fill: white; -fx-font-family: Times New Roman; -fx-font-size: 15px;");
                    itemGuest.setPadding(new Insets(0, 30, 10,30));

                    container.getChildren().addAll(itemGuestID, itemGuest);
                    guestVBox.getChildren().addAll(container);

                    container.setOnMouseClicked(event -> {
                        // Извлекаем логин из метки itemGuest
                        idSafe = itemGuestID.getText().replace("ID: ", "");
                        String guestLogin = itemGuest.getText().split("\n")[0].replace("Логин: ", "");
                        String guestPass = itemGuest.getText().split("\n")[1].replace("Пароль: ", "");
                        String guestMail = itemGuest.getText().split("\n")[2].replace("Почта: ", "");
                        String guestTel = itemGuest.getText().split("\n")[3].replace("Тел.: ", "");
                        // Устанавливаем логин в текстовое поле
                        loginText.setText(guestLogin);
                        passText.setText(guestPass);
                        mailText.setText(guestMail);
                        telText.setText(guestTel);
                    });

                    container.setOnMouseEntered(event -> {
                        container.setCursor(HAND);
                        container.setStyle("-fx-border-color: #f54b64; -fx-border-radius: 10px;");
                    });

                    container.setOnMouseExited(event -> {
                        container.setCursor(DEFAULT);
                        container.setStyle("-fx-border-color: white; -fx-border-radius: 10px;");
                    });
                }

                scrollGuests.setContent(guestVBox);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void roomsTable() throws SQLException {
        ResultSet rss = Connect.getAllRooms();

        VBox roomsVBox = new VBox();


        scrollRooms.setStyle("-fx-background: #222a35; -fx-background-color: transparent;");
        scrollRooms.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        scrollRooms.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        roomsVBox.setAlignment(Pos.CENTER);
        roomsVBox.setSpacing(20);
        roomsVBox.setPadding(new Insets(10, 10, 10, 10));
        VBox.setVgrow(scrollRooms, Priority.ALWAYS);
        HBox.setHgrow(hbox, Priority.ALWAYS);

        try {
            while (rss.next()) {
                int id = rss.getInt("id");
                String room_number = rss.getString("room_number");
                String room_type = rss.getString("room_type");
                String price = rss.getString("price");
                String status = rss.getString("status");

                status = switch (status) {
                    case "available" -> "Доступно";
                    case "booked" -> "Забронировано";
                    case "maintenance" -> "Тех. обслуживание";
                    default -> status;
                };

                VBox container = new VBox();
                container.setStyle("-fx-border-color: white; -fx-border-radius: 10px;");

                Label itemRoomID = new Label("ID: " + id);
                itemRoomID.setTextFill(Color.valueOf("#f54b64"));
                itemRoomID.setStyle("-fx-text-fill: #f54b64; -fx-font-family: DaMiOne; -fx-font-size: 15px");
                itemRoomID.setPadding(new Insets(10, 30, 0,30));

                Label itemRoom = new Label("Номер: " + room_number + "\nТип: " + room_type + "\nЦена: " + price + "\nСтатус: " + status);
                itemRoom.setStyle("-fx-text-fill: white; -fx-font-family: Times New Roman; -fx-font-size: 15px;");
                itemRoom.setPadding(new Insets(0, 40, 10,30));

                container.getChildren().addAll(itemRoomID, itemRoom);
                roomsVBox.getChildren().addAll(container);


                container.setOnMouseClicked(event -> {
                    // Извлекаем логин из метки itemRoom
                    String room = itemRoom.getText().split("\n")[0].replace("Номер: ", "");
                    // Устанавливаем логин в текстовое поле
                    roomNum.setText(room);
                });

                container.setOnMouseEntered(event -> {
                    container.setCursor(HAND);
                    container.setStyle("-fx-border-color: #f54b64; -fx-border-radius: 10px;");
                });

                container.setOnMouseExited(event -> {
                    container.setCursor(DEFAULT);
                    container.setStyle("-fx-border-color: white; -fx-border-radius: 10px;");
                });

                scrollRooms.setContent(roomsVBox);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }



    @FXML
    void clickDelete() throws SQLException {
        ResultSet rss = Connect.getAllGuest();
        Connection conn = Connect.ConnectionDB();

        String sql = "DELETE FROM guests WHERE id=" + idSafe;

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.execute();

        JOptionPane.showMessageDialog(null, "Пользователь удален");

        loginText.setText("");
        passText.setText("");
        mailText.setText("");
        telText.setText("");

        guestTable();
    }

    @FXML
    void clickChange () throws SQLException {
        ResultSet rss = Connect.getAllGuest();
        Connection conn = Connect.ConnectionDB();

        String sql = "UPDATE guests SET login=?, password=?, email=?, phone_number=? WHERE id=" + idSafe;

        try {
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, loginText.getText());
            pst.setString(2, passText.getText());
            pst.setString(3, mailText.getText());
            pst.setString(4, telText.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Произовшло изменение");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void clickAdd () throws SQLException {
        ResultSet rss = Connect.getAllGuest();

        String sql = "INSERT INTO guests(login, password, email, phone_number, role) VALUES (?,?,?,?,?)";
        Connection conn = Connect.ConnectionDB();

        try {

            PreparedStatement pst = conn.prepareStatement(sql);

            if (rss.next()) {
                String login = rss.getString("login");
                String mail = rss.getString("email");
                String telephone = rss.getString("phone_number");


                if (loginText.getText().isEmpty() ||
                        passText.getText().isEmpty() ||
                        mailText.getText().isEmpty() ||
                        telText.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Поля пусты, нечего добовлять.");
                } else if (loginText.getText().equals(login) ||
                        mailText.getText().equals(mail) ||
                        telText.getText().equals(telephone)) {
                    JOptionPane.showMessageDialog(null, "Такой логин или почта или телефон уже существует.");
                } else {
                    pst.setString(1, loginText.getText());
                    pst.setString(2, passText.getText());
                    pst.setString(3, mailText.getText());
                    pst.setString(4, telText.getText());
                    pst.setString(5, "Гость");
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Добавлен новый пользователь.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }


    @FXML
    private void filterGuests(String query) {
        VBox guestVBox = (VBox) scrollGuests.getContent();
        for (Object node : guestVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox container = (VBox) node;
                boolean matchFound = false;

                for (Object childNode : container.getChildren()) {
                    if (childNode instanceof Label) {
                        Label label = (Label) childNode;
                        if (label.getText().toLowerCase().contains(query.toLowerCase())) {
                            matchFound = true;
                            break;
                        }
                    }
                }
                container.setVisible(matchFound);
            }
        }
    }

    @FXML
    private void printGuestTable() {
        // Получаем содержимое scrollGuests
        VBox guestVBox = (VBox) scrollGuests.getContent();

        // Создаем PrinterJob для печати
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            // Устанавливаем корневой узел для печати
            boolean printed = job.printPage(guestVBox);

            if (printed) {
                // Завершаем процесс печати
                job.endJob();
            } else {
                System.out.println("Печать не удалась.");
            }
        } else {
            System.out.println("Не удалось создать PrinterJob.");
        }
    }

    @FXML
    void exportDatabase() {
        String downloadsPath = System.getProperty("user.home") + File.separator + "Загрузки" + File.separator + "database_export";
        File exportDir = new File(downloadsPath);

        // Проверяем существование папки для экспорта и создаем её, если необходимо
        if (!exportDir.exists()) {
            if (!exportDir.mkdirs()) {
                System.out.println("Не удалось создать папку для экспорта.");
                return;
            }
        } else if (!exportDir.isDirectory()) {
            System.out.println("Путь для экспорта не является каталогом.");
            return;
        }

        try (Connection conn = Connect.ConnectionDB()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                exportTableToCSV(conn, tableName, downloadsPath);
            }

            System.out.println("База данных успешно экспортирована в " + downloadsPath);

        } catch (SQLException e) {
            System.out.println("Ошибка при экспорте базы данных: " + e.getMessage());
        }
    }

    void exportTableToCSV(Connection conn, String tableName, String exportPath) {
        String selectSQL = "SELECT * FROM " + tableName;
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery();
             FileWriter fileWriter = new FileWriter(exportPath + File.separator + tableName + ".csv")) {

            // Получаем метаданные результата для заголовков
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Записываем заголовки в CSV
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(metaData.getColumnName(i));
                if (i < columnCount) fileWriter.append(",");
            }
            fileWriter.append("\n");

            // Записываем данные в CSV
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fileWriter.append(rs.getString(i));
                    if (i < columnCount) fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

            System.out.println("Таблица " + tableName + " успешно экспортирована.");

        } catch (SQLException | IOException e) {
            System.out.println("Ошибка при экспорте таблицы " + tableName + ": " + e.getMessage());
        }
    }

    @FXML
    void exportGuests () {
        String downloadsPath = System.getProperty("user.home") + File.separator + "Загрузки";
        File downloadsDir = new File(downloadsPath);

        // Проверяем существование папки "Загрузки" и создаем её, если необходимо
        if (!downloadsDir.exists()) {
            if (!downloadsDir.mkdirs()) {
                System.out.println("Не удалось создать папку 'Загрузки'.");
                return;
            }
        } else if (!downloadsDir.isDirectory()) {
            System.out.println("Путь 'Загрузки' не является каталогом.");
            return;
        }

        // Полный путь к файлу CSV
        String csvFilePath = downloadsPath + File.separator + "guests.csv";
        String selectSQL = "SELECT id, login, email, phone_number, role FROM guests";

        try (Connection conn = Connect.ConnectionDB();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery();
             FileWriter fileWriter = new FileWriter(csvFilePath)) {

            // Записываем заголовки в CSV
            fileWriter.append("ID,Login,Email,Phone Number,Role\n");

            // Записываем данные в CSV
            while (rs.next()) {
                int id = rs.getInt("id");
                String login = rs.getString("login");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");

                fileWriter.append(String.format("%d,%s,%s,%s,%s\n", id, login, email, phoneNumber, role));
            }

            System.out.println("Данные успешно экспортированы в " + csvFilePath);

        } catch (SQLException | IOException e) {
            System.out.println("Ошибка при экспорте данных: " + e.getMessage());
        }
    }

    @FXML
    void initialize() throws SQLException {
        guestTable();
        roomsTable();

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGuests(newValue);
        });
    }
}
