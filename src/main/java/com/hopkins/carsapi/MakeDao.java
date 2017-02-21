package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MakeDao {
    private static final String TABLE_NAME = "make";
    
    public int getCount() {
        String sql = "SELECT COUNT(*)"
                + " FROM " + TABLE_NAME;
        try (Connection conn = DatabaseConnectionProvider.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Make> getAll() {
        List<Make> list = new ArrayList<>();
        String sql = "SELECT id, name, countryCode"
                + " FROM " + TABLE_NAME
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(buildMake(rs));
            }
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Make> getByCountryCode(String countryCode) {
        List<Make> list = new ArrayList<>();
        String sql = "SELECT id, name, countryCode"
                + " FROM " + TABLE_NAME
                + " WHERE countryCode = ?"
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, countryCode);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(buildMake(rs));
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public Make getById(int id) {
        String sql = "SELECT id, name, countryCode"
                + " FROM " + TABLE_NAME
                + " WHERE id = ?";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                Make make = null;
                if (rs.next()) {
                    make = buildMake(rs);
                }
                return make;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public boolean delete(int makeId) {
        String sql = "DELETE FROM " + TABLE_NAME
                + " WHERE id = ?";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, makeId);
            int numRows = st.executeUpdate();
            return numRows > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public final Make insert(String name, String countryCode) {
        String sql = "INSERT INTO " + TABLE_NAME
                + " (name, countryCode)"
                + " VALUES (?, ?)";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, name);
            st.setString(2, countryCode);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                Make make = null;
                if (rs.next()) {
                    int makeId = rs.getInt(1);
                    make = new Make(makeId, name, countryCode);
                }
                return make;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    private Make buildMake(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String countryCode = rs.getString("countryCode");
        return new Make(id, name, countryCode);
    }
}
