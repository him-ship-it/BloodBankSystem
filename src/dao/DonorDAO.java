package dao;

import db.DBConnection;
import model.Donor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    public boolean addDonor(Donor d) throws SQLException {
        String sql = "INSERT INTO donor (name, age, blood_group, contact, city, is_organ_donor) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setInt(2, d.getAge());
            ps.setString(3, d.getBloodGroup());
            ps.setString(4, d.getContact());
            ps.setString(5, d.getCity());
            ps.setBoolean(6, d.isOrganDonor());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Donor> getAllDonors() throws SQLException {
        List<Donor> list = new ArrayList<>();
        String sql = "SELECT * FROM donor";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Donor d = new Donor();
                d.setDonorId(rs.getInt("donor_id"));
                d.setName(rs.getString("name"));
                d.setAge(rs.getInt("age"));
                d.setBloodGroup(rs.getString("blood_group"));
                d.setContact(rs.getString("contact"));
                d.setCity(rs.getString("city"));
                d.setOrganDonor(rs.getBoolean("is_organ_donor"));
                list.add(d);
            }
        }
        return list;
    }

    public List<Donor> getDonorsByBloodGroup(String bg) throws SQLException {
        List<Donor> list = new ArrayList<>();
        String sql = "SELECT * FROM donor WHERE blood_group = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bg);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Donor d = new Donor();
                d.setDonorId(rs.getInt("donor_id"));
                d.setName(rs.getString("name"));
                d.setAge(rs.getInt("age"));
                d.setBloodGroup(rs.getString("blood_group"));
                d.setContact(rs.getString("contact"));
                d.setCity(rs.getString("city"));
                d.setOrganDonor(rs.getBoolean("is_organ_donor"));
                list.add(d);
            }
        }
        return list;
    }

    public boolean deleteDonor(int id) throws SQLException {
        String sql = "DELETE FROM donor WHERE donor_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}