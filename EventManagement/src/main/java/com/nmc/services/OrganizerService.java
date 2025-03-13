/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.services;

/**
 *
 * @author FPTSHOP
 */
import com.nmc.configs.JdbcUtils;
import com.nmc.pojo.Organizer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizerService {
    public List<Organizer> getAllOrganizers() throws SQLException {
        List<Organizer> organizers = new ArrayList<>();
        String query = "SELECT * FROM organizers";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Organizer organizer = new Organizer();
                organizer.setId(rs.getInt("id"));
                organizer.setName(rs.getString("name"));
                organizer.setContactPerson(rs.getString("contact_person"));
                organizer.setEmail(rs.getString("email"));
                organizer.setPhone(rs.getString("phone"));

                organizers.add(organizer);
            }
        }

        return organizers;
    }

    public boolean addOrganizer(Organizer organizer) throws SQLException {
        String query = "INSERT INTO organizers (name, contact_person, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, organizer.getName());
            stmt.setString(2, organizer.getContactPerson());
            stmt.setString(3, organizer.getEmail());
            stmt.setString(4, organizer.getPhone());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Organizer> getOrganizersByKeyword(String keyword) throws SQLException {
        List<Organizer> organizers = new ArrayList<>();
        String query = "SELECT * FROM organizers WHERE name LIKE ? OR contact_person LIKE ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Organizer organizer = new Organizer();
                    organizer.setId(rs.getInt("id"));
                    organizer.setName(rs.getString("name"));
                    organizer.setContactPerson(rs.getString("contact_person"));
                    organizer.setEmail(rs.getString("email"));
                    organizer.setPhone(rs.getString("phone"));
                    organizers.add(organizer);
                }
            }
        }
        return organizers;
    }

}
