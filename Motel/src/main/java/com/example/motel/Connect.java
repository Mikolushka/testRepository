package com.example.motel;

import javax.swing.*;
import java.sql.*;

public class Connect {
    public static Connection ConnectionDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.0.188/test_base","isp17", "12345678");
            return conn;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static ResultSet getAllGuest() throws SQLException {
        String query = "SELECT * FROM guests";
        Connection conn1 = ConnectionDB();
        PreparedStatement pst1 = conn1.prepareStatement(query);
        return pst1.executeQuery();
    }

    public static ResultSet getAllRooms() throws SQLException {
        String query = "SELECT * FROM rooms";
        Connection conn1 = ConnectionDB();
        PreparedStatement pst1 = conn1.prepareStatement(query);
        return pst1.executeQuery();
    }

//    public static ResultSet getAllBook() throws SQLException {
//        String query = "INSERT INTO bookings (guest_id, room_id, check_in, check_out, booking_date) VALUES (?,?,?,?,?)";
//        Connection conn1 = ConnectionDB();
//        PreparedStatement pst1 = conn1.prepareStatement(query);
//        return pst1.executeQuery();
//    }
}
