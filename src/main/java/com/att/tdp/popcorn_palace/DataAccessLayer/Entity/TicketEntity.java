package com.att.tdp.popcorn_palace.DataAccessLayer.Entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Tickets")
public class TicketEntity implements java.io.Serializable {

    @Id
    @Column(name = "customerID")
    private int customerID;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_details", joinColumns = @JoinColumn(name = "customerID"))
    private List<TicketInfo> customerTickets;

    public List<TicketInfo> getCustomerTickets() {
        if (customerTickets == null) {
            customerTickets = new ArrayList<>();
        }
        return customerTickets;
    }

    public void setCustomerTickets(List<TicketInfo> customerTickets) {
        this.customerTickets = customerTickets;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void addTicket(TicketInfo ticketInfo) {
        if (customerTickets == null) {
            customerTickets = new ArrayList<>();
        }
        customerTickets.add(ticketInfo);
    }

}
