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

public class Registration {
    private int id;
    private int eventId;
    private int participantId;
    private Date registrationDate;
    private String status; // "Registered": đã đăng ký,
                            // "Canceled": đã hủy, 
                            // "Attended": đã tham dự

    public Registration(int eventId, int participantId, Date registrationDate, String status) {
        this.eventId = eventId;
        this.participantId = participantId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}

