package com.att.tdp.popcorn_palace.DTO;

import java.time.LocalDateTime;

public class ShowTimeDTO {
    private int id;
    private String movie;
    private String theater;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private double price;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMovie() {
        return movie;
    }
    public void setMovie(String movie) {
        this.movie = movie;
    }
    public String getTheater() {
        return theater;
    }
    public void setTheater(String theater) {
        this.theater = theater;
    }
    public LocalDateTime getStart_time() {
        return start_time;
    }
    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }
    public LocalDateTime getEnd_time() {
        return end_time;
    }
    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
