package dao;

import db.DBConnection;
import model.TransactionLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogDAO {

    public boolean addLog(TransactionLog log) throws SQLException {
        String sql = "INSERT INTO transaction_log (transaction_type, donor_id, recipient_id, hospital_id, blood_group, organ_type, units, notes) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, log.getTransactionType());
            ps.setInt(2, log.getDonorId());
            ps.setInt(3, log.getRecipientId());
            ps.setInt(4, log.getHospitalId());
            ps.setString(5, log.getBloodGroup());
            ps.setString(6, log.getOrganType());
            ps.setInt(7, log.getUnits());
            ps.setString(8, log.getNotes());
            return ps.executeUpdate() > 0;
        }
    }

    public List<TransactionLog> getAllLogs() throws SQLException {
        List<TransactionLog> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction_log ORDER BY transaction_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                TransactionLog log = new TransactionLog();
                log.setLogId(rs.getInt("log_id"));
                log.setTransactionType(rs.getString("transaction_type"));
                log.setDonorId(rs.getInt("donor_id"));
                log.setRecipientId(rs.getInt("recipient_id"));
                log.setHospitalId(rs.getInt("hospital_id"));
                log.setBloodGroup(rs.getString("blood_group"));
                log.setOrganType(rs.getString("organ_type"));
                log.setUnits(rs.getInt("units"));
                log.setNotes(rs.getString("notes"));
                list.add(log);
            }
        }
        return list;
    }
}