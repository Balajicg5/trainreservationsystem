package com.trainreservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/train_reservation_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "cg5@";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public static List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT * FROM stations ORDER BY station_name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stations.add(new Station(rs.getString("station_id"), rs.getString("station_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }
    public static Station getStationById(String stationId) {
        String sql = "SELECT * FROM stations WHERE station_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Station(rs.getString("station_id"), rs.getString("station_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     public static List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String trainId = rs.getString("train_id");
                List<Station> route = getRouteForTrain(trainId);
                int totalSeats = rs.getInt("total_seats");
                int bookedSeats = getBookedSeatsCount(trainId);
                Train train = new Train(trainId, rs.getString("train_name"), route, totalSeats);
                train.setAvailableSeats(totalSeats - bookedSeats);
                trains.add(train);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }

    public static List<Station> getRouteForTrain(String trainId) {
        List<Station> route = new ArrayList<>();
        String sql = "SELECT s.* FROM routes r JOIN stations s ON r.station_id = s.station_id WHERE r.train_id = ? ORDER BY r.sequence_number";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trainId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                route.add(new Station(rs.getString("station_id"), rs.getString("station_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return route;
    }

    public static int getBookedSeatsCount(String trainId) {
        String sql = "SELECT SUM(num_seats) AS total_booked FROM tickets WHERE train_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trainId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_booked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Ticket getTicketById(String ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                final String trainIdFromResult = rs.getString("train_id");
                Train train = getAllTrains().stream().filter(t -> t.getTrainId().equals(trainIdFromResult)).findFirst().orElse(null);
                Station source = getStationById(rs.getString("source_station_id"));
                Station dest = getStationById(rs.getString("destination_station_id"));
                return new Ticket(rs.getString("ticket_id"), rs.getString("passenger_name"), train, source, dest, rs.getInt("num_seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String bookTicket(String passengerName, String trainId, String sourceId, String destId, int numSeats) {
        String ticketId = "TKT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sql = "INSERT INTO tickets (ticket_id, passenger_name, train_id, source_station_id, destination_station_id, num_seats) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticketId);
            pstmt.setString(2, passengerName);
            pstmt.setString(3, trainId);
            pstmt.setString(4, sourceId);
            pstmt.setString(5, destId);
            pstmt.setInt(6, numSeats);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return ticketId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean cancelTicket(String ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticketId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addToWaitlist(String passengerName, String trainId, int numSeats) {
        String sql = "INSERT INTO waitlist (passenger_name, train_id, num_seats) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, passengerName);
            pstmt.setString(2, trainId);
            pstmt.setInt(3, numSeats);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNextFromWaitlist(String trainId, int availableSeats) {
        String sql = "SELECT * FROM waitlist WHERE train_id = ? AND num_seats <= ? ORDER BY waitlist_id LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trainId);
            pstmt.setInt(2, availableSeats);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String passengerName = rs.getString("passenger_name");
                // In a real app, you would also book a ticket for them and remove them from the waitlist.
                return passengerName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}