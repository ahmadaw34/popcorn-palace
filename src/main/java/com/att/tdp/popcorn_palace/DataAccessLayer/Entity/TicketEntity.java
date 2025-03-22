package com.att.tdp.popcorn_palace.DataAccessLayer.Entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="Tickets")
public class TicketEntity implements java.io.Serializable {

    @Id
    @Column(name="customerID")
    private int customerID;

    // @Column(name="customerTickets")
    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ElementCollection
    @CollectionTable(name = "ticket_details", joinColumns = @JoinColumn(name = "customer_id"))
    private List<TicketInfo> customerTickets; 

    public List<TicketInfo> getCustomerTickets() {
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

    
}
