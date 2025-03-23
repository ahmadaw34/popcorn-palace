package com.att.tdp.popcorn_palace.DataAccessLayer.Entity;

import jakarta.persistence.*;

@Embeddable
public class TicketInfo implements java.io.Serializable {

    @ManyToOne
    @JoinColumn(name="showTimeID",referencedColumnName = "id")
    private ShowTimeEntity showTimeID;
    
    @Column(name="seatNumber")
    private int seatNumber;

    public ShowTimeEntity getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(ShowTimeEntity showTimeID) {
        this.showTimeID = showTimeID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
