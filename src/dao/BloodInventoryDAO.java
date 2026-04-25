package dao;

import db.DBConnection;
import model.BloodInventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodInventoryDAO {

    public boolean addUnits(String bloodGroup, int units) throws SQLException {
        String sql = "UPDATE blood_inventory SET units_available = units_available + ? WHERE blood_group = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, units);
            ps.setString(2, bloodGroup);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deductUnits(String bloodGroup, int units) throws SQLException {
        String checkSql = "SELECT units_available FROM blood_inventory WHERE blood_group = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(checkSql)) {
            ps.setString(1, bloodGroup);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("units_available") >= units) {
                String deductSql = "UPDATE blood_inventory SET units_available = units_available - ? WHERE blood_group = ?";
                PreparedStatement ps2 = con.prepareStatement(deductSql);
                ps2.setInt(1, units);
                ps2.setString(2, bloodGroup);
                return ps2.executeUpdate() > 0;
            }
        }
        System.out.println("Insufficient blood units for group: " + bloodGroup);
        return false;
    }

    public List<BloodInventory> getAllInventory() throws SQLException {
        List<BloodInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_inventory";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                BloodInventory bi = new BloodInventory();
                bi.setInventoryId(rs.getInt("inventory_id"));
                bi.setBloodGroup(rs.getString("blood_group"));
                bi.setUnitsAvailable(rs.getInt("units_available"));
                list.add(bi);
            }
        }
        return list;
    }
}