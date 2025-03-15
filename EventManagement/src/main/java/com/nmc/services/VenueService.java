package com.nmc.services;

import com.nmc.configs.JdbcUtils;
import com.nmc.pojo.Venue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VenueService {

    public Optional<Venue> getVenueById(int venueId) throws SQLException {
        String sql = "SELECT * FROM venues WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venueId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVenue(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Venue> getVenueByName(String name) throws SQLException {
        String sql = "SELECT * FROM venues WHERE name = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVenue(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Venue> getAllVenues() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String query = "SELECT * FROM venues";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                venues.add(mapVenue(rs));
            }
        }
        return venues;
    }

    public boolean addVenue(Venue venue) throws SQLException {
        String query = "INSERT INTO venues (name, location, capacity) VALUES (?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getCapacity());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venue.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;
        }
    }

    public boolean updateVenue(Venue venue) throws SQLException {
        String query = "UPDATE venues SET name = ?, location = ?, capacity = ? WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getCapacity());
            stmt.setInt(4, venue.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteVenue(int venueId) throws SQLException {
        String query = "DELETE FROM venues WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, venueId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Venue> getVenuesByKeyword(String keyword) throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String query = "SELECT * FROM venues WHERE name LIKE ? OR location LIKE ?";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    venues.add(mapVenue(rs));
                }
            }
        }
        return venues;
    }

    private Venue mapVenue(ResultSet rs) throws SQLException {
        Venue venue = new Venue();
        venue.setId(rs.getInt("id"));
        venue.setName(rs.getString("name"));
        venue.setLocation(rs.getString("location"));
        venue.setCapacity(rs.getInt("capacity"));
        return venue;
    }
}
