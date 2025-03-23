package com.att.tdp.popcorn_palace.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.DTO.ShowTimeDTO;
import com.att.tdp.popcorn_palace.ServiceLayer.Response;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@RestController
@RequestMapping("/api/showtime")
public class ShowTimeRequests {
    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping
    public ResponseEntity<String> addShowTime(@RequestBody ShowTimeDTO showtimeRequest) {
        Response response = serviceFactory.addShowTime(
                showtimeRequest.getMovie(),
                showtimeRequest.getTheater(),
                showtimeRequest.getStart_time(),
                showtimeRequest.getEnd_time(),
                showtimeRequest.getPrice());
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    // ShowTimeDTO contains showtime id so there is no need to add showtime id as PathVariable in PUT Request
    @PutMapping("/update")
    public ResponseEntity<String> updateShowtimeDetails(@RequestBody ShowTimeDTO showtimeRequest) {
        Response response = serviceFactory.updateShowtimeDetails(showtimeRequest.getId(), showtimeRequest.getMovie(),
                showtimeRequest.getTheater(),
                showtimeRequest.getStart_time(),
                showtimeRequest.getEnd_time(),
                showtimeRequest.getPrice());
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowTime(@PathVariable("id") int id) {
        Response response = serviceFactory.deleteShowTime(id);
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> fetchShowtineByID(@PathVariable("id") int id) {
        Response response = serviceFactory.fetchShowtineByID(id);
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }
}
