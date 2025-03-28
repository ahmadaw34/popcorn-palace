package com.att.tdp.popcorn_palace.DomainLayer.TicketBookingPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BookingController {
    private Map<Integer, List<Ticket>> tickets; // <customerID,List of <showtimeID,seatNumber>>

    private BookingController() {
        this.tickets = new HashMap<>();
    }

    public void cleanup() {
        this.tickets = new HashMap<>();
    }

    public String bookTicket(int customerID, int showTimeID, int seatNumber) throws Exception {
        for (Map.Entry<Integer, List<Ticket>> ticket : tickets.entrySet()) {
            List<Ticket> customerTickets = ticket.getValue();
            for (Ticket t : customerTickets) {
                if (t.getSeatNumber() == seatNumber && t.getShowTimeID() == showTimeID) {
                    throw new Exception("seat " + seatNumber + " for the showtime " + showTimeID + " already booked");
                }
            }
        }
        if (!tickets.containsKey(customerID)) {
            tickets.put(customerID, new ArrayList<>());
        }
        List<Ticket> customerTickets = tickets.get(customerID);
        customerTickets.add(new Ticket(showTimeID, seatNumber));
        return "ticket booked successfully";
    }

    public List<Integer> ticketForShowtime(int showtimeId) {
        List<Integer> ticketForShowtime = new ArrayList<>();
        for (int customerId : tickets.keySet()) {
            for (Ticket ticket : tickets.get(customerId)) {
                if (ticket.getShowTimeID() == showtimeId) {
                    ticketForShowtime.add(customerId);
                }
            }
        }
        if (ticketForShowtime.size() > 0) {
            return ticketForShowtime;
        } else {
            return null;
        }
    }
}
