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
    private final Connection conn;
        
    public MakeDao() {
        conn = DatabaseConnectionProvider.connect();
    }
    
    public int getCount() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) FROM " + TABLE_NAME);
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            st.close();
            return count;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Make> getAll() {
        List<Make> list = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, name, countryCode"
                            + " FROM " + TABLE_NAME
                            + " ORDER BY name ASC");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(buildMake(rs));
            }
            rs.close();
            st.close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Make> getByCountryCode(String countryCode) {
        List<Make> list = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, name, countryCode"
                            + " FROM " + TABLE_NAME
                            + " WHERE countryCode = ?"
                            + " ORDER BY name ASC");
            st.setString(1, countryCode);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(buildMake(rs));
            }
            rs.close();
            st.close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public Make getById(int id) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, name, countryCode"
                            + " FROM " + TABLE_NAME
                            + " WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            Make make = null;
            if (rs.next()) {
                make = buildMake(rs);
            }
            rs.close();
            st.close();
            return make;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public boolean delete(int makeId) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM " + TABLE_NAME
                            + " WHERE id = ?");
            st.setInt(1, makeId);
            int numRows = st.executeUpdate();
            st.close();
            return numRows > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public final Make insert(String name, String countryCode) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME
                            + " (name, countryCode)"
                            + " VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, name);
            st.setString(2, countryCode);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            Make make = null;
            if (rs.next()) {
                int makeId = rs.getInt(1);
                make = new Make(makeId, name, countryCode);
            }
            rs.close();
            st.close();
            return make;
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
