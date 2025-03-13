/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.services;

import com.nmc.configs.JdbcUtils;
import com.nmc.pojo.Venue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class VenueService {

    public static Venue getVenueById(int venueId) throws SQLException {
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT * FROM venues WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, venueId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Venue(rs.getString("name"), rs.getString("location"), rs.getInt("capacity"));
        }

        return null; // Nếu không tìm thấy
    }

    public List<Venue> getAllVenues() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String query = "SELECT * FROM venues";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venue venue = new Venue();
                venue.setId(rs.getInt("id"));
                venue.setName(rs.getString("name"));
                venue.setLocation(rs.getString("location"));
                venue.setCapacity(rs.getInt("capacity"));

                venues.add(venue);
            }
        }

        return venues;
    }
    
    
    public boolean addVenue(Venue venue) throws SQLException {
        String query = "INSERT INTO venues (name, location, capacity) VALUES (?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getCapacity());

            return stmt.executeUpdate() > 0;
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
                    Venue venue = new Venue();
                    venue.setId(rs.getInt("id"));
                    venue.setName(rs.getString("name"));
                    venue.setLocation(rs.getString("location"));
                    venue.setCapacity(rs.getInt("capacity"));
                    venues.add(venue);
                }
            }
        }
        return venues;
    }
}
