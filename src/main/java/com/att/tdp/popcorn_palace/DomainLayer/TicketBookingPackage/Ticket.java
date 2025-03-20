package com.att.tdp.popcorn_palace.DomainLayer.TicketBookingPackage;

public class Ticket {
    private int customerID;
    private int showTimeID;
    private int seatNumber;

    public Ticket(int customerID, int showTimeID, int seatNumber) {
        this.customerID = customerID;
        this.showTimeID = showTimeID;
        this.seatNumber = seatNumber;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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
