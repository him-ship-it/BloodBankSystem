package dao;

import db.DBConnection;
import model.DashboardStats;

import java.sql.*;
import java.util.*;

public class DashboardDAO {

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        try (Connection con = DBConnection.getConnection()) {

            // Total donors
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM donor");
            if (rs.next()) stats.totalDonors = rs.getInt(1);

            // Total recipients
            rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM recipient");
            if (rs.next()) stats.totalRecipients = rs.getInt(1);

            // Total blood units
            rs = con.createStatement().executeQuery("SELECT SUM(units_available) FROM blood_inventory");
            if (rs.next()) stats.totalBloodUnits = rs.getInt(1);

            // Total transactions
            rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM transaction_log");
            if (rs.next()) stats.totalTransactions = rs.getInt(1);

            // Organ donors
            rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM donor WHERE is_organ_donor = true");
            if (rs.next()) stats.organDonors = rs.getInt(1);

            // Critical recipients
            rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM recipient WHERE urgency_level IN ('CRITICAL','HIGH')");
            if (rs.next()) stats.criticalRecipients = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public Map<String, Integer> getBloodInventory() {
        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT blood_group, units_available FROM blood_inventory ORDER BY blood_group");
            while (rs.next()) map.put(rs.getString("blood_group"), rs.getInt("units_available"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public List<Object[]> getAllDonors() {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT donor_id, name, age, blood_group, contact, city, is_organ_donor, registered_at FROM donor ORDER BY donor_id DESC");
            while (rs.next()) list.add(new Object[]{
                    rs.getInt("donor_id"), rs.getString("name"), rs.getInt("age"),
                    rs.getString("blood_group"), rs.getString("contact"),
                    rs.getString("city"), rs.getBoolean("is_organ_donor") ? "Yes" : "No",
                    rs.getString("registered_at")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> getAllRecipients() {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT recipient_id, name, age, blood_group, contact, hospital_id, organ_needed, urgency_level, registered_at FROM recipient ORDER BY recipient_id DESC");
            while (rs.next()) list.add(new Object[]{
                    rs.getInt("recipient_id"), rs.getString("name"), rs.getInt("age"),
                    rs.getString("blood_group"), rs.getString("contact"),
                    rs.getInt("hospital_id"), rs.getString("organ_needed"),
                    rs.getString("urgency_level"), rs.getString("registered_at")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> getAllTransactions() {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT log_id, transaction_type, donor_id, recipient_id, hospital_id, blood_group, organ_type, units, transaction_date, notes FROM transaction_log ORDER BY log_id DESC");
            while (rs.next()) list.add(new Object[]{
                    rs.getInt("log_id"), rs.getString("transaction_type"),
                    rs.getInt("donor_id"), rs.getInt("recipient_id"),
                    rs.getInt("hospital_id"), rs.getString("blood_group"),
                    rs.getString("organ_type") != null ? rs.getString("organ_type") : "N/A",
                    rs.getInt("units"), rs.getString("transaction_date"),
                    rs.getString("notes")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> getAllOrgans() {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT organ_id, organ_type, donor_id, blood_group, status, hospital_id, harvested_at FROM organ_inventory ORDER BY organ_id DESC");
            while (rs.next()) list.add(new Object[]{
                    rs.getInt("organ_id"), rs.getString("organ_type"),
                    rs.getInt("donor_id"), rs.getString("blood_group"),
                    rs.getString("status"), rs.getInt("hospital_id"),
                    rs.getString("harvested_at")
            });
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // --- INSERT ---
    public boolean insertDonor(String name, int age, String bg, String contact, String city, boolean isOrgan) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO donor (name, age, blood_group, contact, city, is_organ_donor) VALUES (?,?,?,?,?,?)");
            ps.setString(1, name); ps.setInt(2, age); ps.setString(3, bg);
            ps.setString(4, contact); ps.setString(5, city); ps.setBoolean(6, isOrgan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean insertRecipient(String name, int age, String bg, String contact, int hospId, String organ, String urgency) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO recipient (name, age, blood_group, contact, hospital_id, organ_needed, urgency_level) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, name); ps.setInt(2, age); ps.setString(3, bg);
            ps.setString(4, contact); ps.setInt(5, hospId);
            ps.setString(6, organ); ps.setString(7, urgency);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean addBloodUnits(String bg, int units) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE blood_inventory SET units_available = units_available + ? WHERE blood_group = ?");
            ps.setInt(1, units); ps.setString(2, bg);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deductBloodUnits(String bg, int units) {
        try (Connection con = DBConnection.getConnection()) {
            // Check stock
            PreparedStatement check = con.prepareStatement(
                    "SELECT units_available FROM blood_inventory WHERE blood_group = ?");
            check.setString(1, bg);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) >= units) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE blood_inventory SET units_available = units_available - ? WHERE blood_group = ?");
                ps.setInt(1, units); ps.setString(2, bg);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean performTransfusion(int donorId, int recipId, int hospId, String bg, int units) {
        try (Connection con = DBConnection.getConnection()) {
            // Deduct blood
            PreparedStatement deduct = con.prepareStatement(
                    "UPDATE blood_inventory SET units_available = units_available - ? WHERE blood_group = ? AND units_available >= ?");
            deduct.setInt(1, units); deduct.setString(2, bg); deduct.setInt(3, units);
            int rows = deduct.executeUpdate();
            if (rows == 0) return false;
            // Log
            PreparedStatement log = con.prepareStatement(
                    "INSERT INTO transaction_log (transaction_type, donor_id, recipient_id, hospital_id, blood_group, units, notes) VALUES (?,?,?,?,?,?,?)");
            log.setString(1, "BLOOD_TRANSFUSION"); log.setInt(2, donorId); log.setInt(3, recipId);
            log.setInt(4, hospId); log.setString(5, bg); log.setInt(6, units);
            log.setString(7, "Blood transfusion completed");
            return log.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // --- DELETE ---
    public boolean deleteDonor(int id) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM donor WHERE donor_id = ?");
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteRecipient(int id) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM recipient WHERE recipient_id = ?");
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteTransaction(int id) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM transaction_log WHERE log_id = ?");
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}