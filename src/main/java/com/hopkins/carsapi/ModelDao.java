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
    
    public int getCount() {
        String sql = "SELECT COUNT(*)"
                + " FROM " + TABLE_NAME;
        try (Connection conn = DatabaseConnectionProvider.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public Model getById(int modelId) {
        String sql = "SELECT id, makeId, name, type"
                + " FROM " + TABLE_NAME
                + " WHERE id = ?";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, modelId);
            try (ResultSet rs = st.executeQuery()) {
                Model model = null;
                if (rs.next()) {
                    model = buildModel(rs);
                }
                return model;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Model> getByMake(int makeId) {
        List<Model> list = new ArrayList<>();
        String sql = "SELECT id, makeId, name, type"
                + " FROM " + TABLE_NAME
                + " WHERE makeId = ?"
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, makeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(buildModel(rs));
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public List<Model> getByType(ModelType type) {
        List<Model> list = new ArrayList<>();
        String sql = "SELECT id, makeId, name, type"
                + " FROM " + TABLE_NAME
                + " WHERE type = ?"
                + " ORDER BY name ASC";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, type.ordinal());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(buildModel(rs));
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading from MySQL", ex);
        }
    }
    
    public final Model insert(int makeId, String name, ModelType type) {
        String sql = "INSERT INTO " + TABLE_NAME
                + " (makeId, name, type)"
                + " VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, makeId);
            st.setString(2, name);
            st.setInt(3, type.ordinal());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                Model model = null;
                if (rs.next()) {
                    int modelId = rs.getInt(1);
                    model = new Model(modelId, makeId, name, type);
                }
                return model;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error loading count from MySQL", ex);
        }
    }
    
    public boolean delete(int modelId) {
        String sql = "DELETE FROM " + TABLE_NAME
                + " WHERE id = ?";
        try (Connection conn = DatabaseConnectionProvider.connect();
            PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, modelId);
            int numRows = st.executeUpdate();
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
