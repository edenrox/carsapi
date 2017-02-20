package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModelDao {
    private static final String TABLE_NAME = "model";
    
    private final Connection conn;
        
    public ModelDao() {
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
    
    public Model getById(int modelId) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, makeId, name, type"
                            + " FROM " + TABLE_NAME
                            + " WHERE id = ?");
            st.setInt(1, modelId);
            ResultSet rs = st.executeQuery();
            Model model = null;
            if (rs.next()) {
                model = buildModel(rs);
            }
            rs.close();
            st.close();
            return model;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Model> getByMake(int makeId) {
        List<Model> list = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, makeId, name, type"
                            + " FROM " + TABLE_NAME
                            + " WHERE makeId = ?"
                            + " ORDER BY name ASC");
            st.setInt(1, makeId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(buildModel(rs));
            }
            rs.close();
            st.close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Model> getByType(ModelType type) {
        List<Model> list = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, makeId, name, type"
                            + " FROM " + TABLE_NAME
                            + " WHERE type = ?"
                            + " ORDER BY name ASC");
            st.setInt(1, type.ordinal());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(buildModel(rs));
            }
            rs.close();
            st.close();
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public final Model insert(int makeId, String name, ModelType type) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME
                            + " (makeId, name, type)"
                            + " VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, makeId);
            st.setString(2, name);
            st.setInt(3, type.ordinal());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            Model model = null;
            if (rs.next()) {
                int modelId = rs.getInt(1);
                model = new Model(modelId, makeId, name, type);
            }
            rs.close();
            st.close();
            return model;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public boolean delete(int modelId) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM " + TABLE_NAME
                            + " WHERE id = ?");
            st.setInt(1, modelId);
            int numRows = st.executeUpdate();
            st.close();
            return numRows > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    private Model buildModel(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int makeId = rs.getInt("makeId");
        String name = rs.getString("name");
        int typeIndex = rs.getInt("type");
        ModelType type = ModelType.values()[typeIndex];
        return new Model(id, makeId, name, type);
    }
}
