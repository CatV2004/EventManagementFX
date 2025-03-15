package com.nmc.services;

import com.nmc.configs.JdbcUtils;
import com.nmc.pojo.Organizer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganizerService {

    public List<Organizer> getAllOrganizers() throws SQLException {
        List<Organizer> organizers = new ArrayList<>();
        String query = "SELECT * FROM organizers";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                organizers.add(mapOrganizer(rs));
            }
        }
        return organizers;
    }

    public boolean addOrganizer(Organizer organizer) throws SQLException {
        String query = "INSERT INTO organizers (name, contact_person, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, organizer.getName());
            stmt.setString(2, organizer.getContactPerson());
            stmt.setString(3, organizer.getEmail());
            stmt.setString(4, organizer.getPhone());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        organizer.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;
        }
    }

    public List<Organizer> getOrganizersByKeyword(String keyword) throws SQLException {
        List<Organizer> organizers = new ArrayList<>();
        String query = "SELECT * FROM organizers WHERE name LIKE ? OR contact_person LIKE ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    organizers.add(mapOrganizer(rs));
                }
            }
        }
        return organizers;
    }

    public Optional<Organizer> getOrganizerById(int organizerId) throws SQLException {
        String sql = "SELECT * FROM organizers WHERE id = ?";
        
        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, organizerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOrganizer(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Organizer> getOrganizerByName(String organizerName) throws SQLException {
        String sql = "SELECT * FROM organizers WHERE name = ?";
        
        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, organizerName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOrganizer(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean updateOrganizer(Organizer organizer) throws SQLException {
        String query = "UPDATE organizers SET name = ?, contact_person = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, organizer.getName());
            stmt.setString(2, organizer.getContactPerson());
            stmt.setString(3, organizer.getEmail());
            stmt.setString(4, organizer.getPhone());
            stmt.setInt(5, organizer.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteOrganizer(int organizerId) throws SQLException {
        String query = "DELETE FROM organizers WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, organizerId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Organizer mapOrganizer(ResultSet rs) throws SQLException {
        Organizer organizer = new Organizer();
        organizer.setId(rs.getInt("id"));
        organizer.setName(rs.getString("name"));
        organizer.setContactPerson(rs.getString("contact_person"));
        organizer.setEmail(rs.getString("email"));
        organizer.setPhone(rs.getString("phone"));
        return organizer;
    }
}
