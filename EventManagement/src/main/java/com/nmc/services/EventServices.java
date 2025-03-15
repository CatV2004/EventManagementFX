/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.services;

import com.nmc.configs.JdbcUtils;
import com.nmc.pojo.Event;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class EventServices {

    public List<Event> getEventsByTimeRange(Timestamp startTime, Timestamp endTime) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "{ CALL GetEventsByTimeRange(?, ?) }";

        try (Connection conn = JdbcUtils.getConn(); CallableStatement stmt = conn.prepareCall(sql)) {

            // Nếu tham số là null, đặt kiểu NULL tương ứng
            if (startTime != null) {
                stmt.setTimestamp(1, startTime);
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            if (endTime != null) {
                stmt.setTimestamp(2, endTime);
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event();
                    event.setId(rs.getInt("id"));
                    event.setName(rs.getString("name"));
                    event.setDescription(rs.getString("description"));
                    event.setVenueId(rs.getInt("venue_id"));
                    event.setStartTime(rs.getTimestamp("start_time"));
                    event.setEndTime(rs.getTimestamp("end_time"));
                    event.setOrganizerId(rs.getInt("organizer_id"));
                    event.setMaxGuests(rs.getInt("max_guests"));
                    event.setStatus(rs.getString("status"));
                    events.add(event);
                }
            }
        }
        return events;
    }

    public List<Event> getEventsByKeyword(String keyword) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE name LIKE ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%"); // Tìm kiếm theo tên sự kiện

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event();
                    event.setId(rs.getInt("id"));
                    event.setName(rs.getString("name"));
                    event.setVenueId(rs.getInt("venue_id"));
                    event.setStartTime(rs.getTimestamp("start_time"));
                    event.setEndTime(rs.getTimestamp("end_time"));
                    event.setStatus(rs.getString("status"));
                    events.add(event);
                }
            }
        }
        return events;
    }

    public boolean addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (name, description, venue_id, start_time, end_time, organizer_id, max_guests) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setInt(3, event.getVenueId());
            stmt.setTimestamp(4, new Timestamp(event.getStartTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(event.getEndTime().getTime()));
            stmt.setInt(6, event.getOrganizerId());
            stmt.setInt(7, event.getMaxGuests());

            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật sự kiện
    public boolean updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET name = ?, description = ?, venue_id = ?, start_time = ?, end_time = ?, organizer_id = ?, max_guests = ? WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setInt(3, event.getVenueId());
            stmt.setTimestamp(4, new Timestamp(event.getStartTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(event.getEndTime().getTime()));
            stmt.setInt(6, event.getOrganizerId());
            stmt.setInt(7, event.getMaxGuests());
            stmt.setInt(8, event.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa sự kiện
    public boolean deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            return stmt.executeUpdate() > 0;
        }
    }

}
