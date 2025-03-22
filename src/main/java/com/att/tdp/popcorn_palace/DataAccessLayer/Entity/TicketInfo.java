package com.att.tdp.popcorn_palace.DataAccessLayer.Entity;

import jakarta.persistence.*;

@Embeddable
public class TicketInfo implements java.io.Serializable {

    @JoinColumn(name="showTimeID",referencedColumnName = "id")
    private int showTimeID;
    
    @Column(name="seatNumber")
    private int seatNumber;

    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
