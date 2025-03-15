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

public class Payment {
    private int id;
    private int registrationId;
    private double amount;
    private String status; // "Paid": Tiền thanh toán,
                            // "Refunded": Đã hoàn lại
    private Date paymentDate;

    public Payment(int registrationId, double amount, String status, Date paymentDate) {
        this.registrationId = registrationId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    
}

