package dao;

import db.DBConnection;
import model.Recipient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipientDAO {

    public boolean addRecipient(Recipient r) throws SQLException {
        String sql = "INSERT INTO recipient (name, age, blood_group, contact, hospital_id, organ_needed, urgency_level) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getName());
            ps.setInt(2, r.getAge());
            ps.setString(3, r.getBloodGroup());
            ps.setString(4, r.getContact());
            ps.setInt(5, r.getHospitalId());
            ps.setString(6, r.getOrganNeeded());
            ps.setString(7, r.getUrgencyLevel());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Recipient> getAllRecipients() throws SQLException {
        List<Recipient> list = new ArrayList<>();
        String sql = "SELECT * FROM recipient";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Recipient r = new Recipient();
                r.setRecipientId(rs.getInt("recipient_id"));
                r.setName(rs.getString("name"));
                r.setAge(rs.getInt("age"));
                r.setBloodGroup(rs.getString("blood_group"));
                r.setContact(rs.getString("contact"));
                r.setHospitalId(rs.getInt("hospital_id"));
                r.setOrganNeeded(rs.getString("organ_needed"));
                r.setUrgencyLevel(rs.getString("urgency_level"));
                list.add(r);
            }
        }
        return list;
    }
}