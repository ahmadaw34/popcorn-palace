package com.att.tdp.popcorn_palace.DomainLayer.TicketBookingPackage;

public class Ticket {
    private int showTimeID;
    private int seatNumber;

    public Ticket(int showTimeID, int seatNumber) {
        this.showTimeID = showTimeID;
        this.seatNumber = seatNumber;
    }

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
