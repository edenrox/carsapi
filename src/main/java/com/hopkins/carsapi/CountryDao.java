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
    
    private final Connection conn;
        
    public CountryDao() {
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
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public List<Country> getAll() {
        List<Country> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT code, name"
                            + " FROM " + TABLE_NAME
                            + " ORDER BY name ASC");
            while (rs.next()) {
                list.add(buildCountry(rs));
            }
            st.close();
            rs.close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public Country getByCode(String countryCode) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT code, name"
                            + " FROM " + TABLE_NAME
                            + " WHERE code = ?"
                            + " ORDER BY name ASC");
            st.setString(1, countryCode);
            ResultSet rs = st.executeQuery();
            Country country = null;
            if (rs.next()) {
                country = buildCountry(rs);
            }
            st.close();
            rs.close();
            return country;
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
