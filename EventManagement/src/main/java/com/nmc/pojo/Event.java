/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nmc.pojo;

/**
 *
 * @author FPTSHOP
 */
import java.util.Date;

public class Event {
    private int id;
    private String name;
    private String description;
    private int venueId;
    private Date startTime;
    private Date endTime;
    private int organizerId;
    private int maxGuests;
    private String status; // "Pending Approval" : Chờ phê duyệt,
                            // "Approved" : đã phê duyệt, 
                            // "Ongoing" : đang diễn ra, 
                            // "Completed" : Hoàn thành,
                            // "Canceled" : đã hủy

    public Event() {
    }
    

    public Event(String name, String description, int venueId, Date startTime, Date endTime, int organizerId, int maxGuests, String status) {
        this.name = name;
        this.description = description;
        this.venueId = venueId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizerId = organizerId;
        this.maxGuests = maxGuests;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}

