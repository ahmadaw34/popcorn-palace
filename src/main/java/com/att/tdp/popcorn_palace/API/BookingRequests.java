package com.att.tdp.popcorn_palace.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.DTO.TicketDTO;
import com.att.tdp.popcorn_palace.ServiceLayer.Response;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@RestController
@RequestMapping("/api/booking")
public class BookingRequests {
    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping
    public ResponseEntity<String> bookTicket(@RequestBody TicketDTO ticketRequest) {
        Response response = serviceFactory.bookTicket(
                ticketRequest.getCustomerID(),
                ticketRequest.getShowTimeID(),
                ticketRequest.getSeatNumber());
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }
}
