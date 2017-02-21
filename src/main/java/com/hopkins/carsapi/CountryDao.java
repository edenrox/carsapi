package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {
    private static final String TABLE_NAME = "country";
    
    public int getCount() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        try (Connection conn = DatabaseConnectionProvider.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public List<Country> getAll() {
        List<Country> list = new ArrayList<>();
        String sql = "SELECT code, name"
                + " FROM " + TABLE_NAME
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(buildCountry(rs));
            }
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public Country getByCode(String countryCode) {
        String sql = "SELECT code, name"
                + " FROM " + TABLE_NAME
                + " WHERE code = ?"
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, countryCode);
            try (ResultSet rs = st.executeQuery()) {
                Country country = null;
                if (rs.next()) {
                    country = buildCountry(rs);
                }
                return country;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    private Country buildCountry(ResultSet rs) throws SQLException {
        String countryCode = rs.getString("code");
        String name = rs.getString("name");
        return new Country(countryCode, name);
    }
}
