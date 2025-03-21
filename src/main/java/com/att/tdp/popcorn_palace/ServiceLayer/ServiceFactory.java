package com.att.tdp.popcorn_palace.ServiceLayer;

import com.att.tdp.popcorn_palace.DomainLayer.PopcornPalace;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFactory {
    public static final Logger LOGGER = Logger.getLogger(ServiceFactory.class.getName());
    @Autowired
    private PopcornPalace popcornPalace;
    // private static SreviceFactory instance;

    // public static SreviceFactory getInstance() {
    //     if (instance == null) {
    //         instance = new SreviceFactory();
    //     }
    //     return instance;
    // }

    // private SreviceFactory() {
    //     popcornPalace = PopcornPalace.getInstance();
    // }

    // movie service
    public Response addMovie(String title, String genre, int duration, double rating, int release_year) {
        try {
            String result = popcornPalace.addMovie(title, genre, duration, rating, release_year);
            LOGGER.info(result);
            return new Response(result, false);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new Response(e.getMessage(),true);
        }
    }

    public Response updateMovieDetails(String title, String genre, int duration, double rating, int release_year) {
        try {
            String result = popcornPalace.updateMovieDetails(title, genre, duration, rating, release_year);
            LOGGER.info(result);
            return new Response(result, false);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new Response(e.getMessage(), true);
        }
    }

    public Response deleteMovie(String title) {
        try {
            String result = popcornPalace.deleteMovie(title);
            LOGGER.info(result);
            return new Response(result, false);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new Response(e.getMessage(), true);
        }
    }

    public Response fetchAllMovies() {
        try {
            String result = popcornPalace.fetchAllMovies();
            LOGGER.info(result);
            return new Response(result, false);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new Response(e.getMessage(), true);
        }
    }

    // showtime service
    public void addShowTime(int id, String movie, String theater, int startDay,int startMonth,int startYear,int startHour,int startMinute,int endDay,int endMonth,int endYear,int endHour,int endMinute,
            double price) {
        try {
            LocalDateTime start_time=LocalDateTime.of(startYear,startMonth,startDay,startHour,startMinute);
            LocalDateTime end_time=LocalDateTime.of(endYear,endMonth,endDay,endHour,endMinute);
            String result = popcornPalace.addShowTime(id, movie, theater, start_time, end_time, price);
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void updateShowtimeDetails(int id, String movie, String theater, int startDay,int startMonth,int startYear,int startHour,int startMinute,int endDay,int endMonth,int endYear,int endHour,int endMinute,
    double price) {
        try {
            LocalDateTime start_time=LocalDateTime.of(startYear,startMonth,startDay,startHour,startMinute);
            LocalDateTime end_time=LocalDateTime.of(endYear,endMonth,endDay,endHour,endMinute);
            String result=popcornPalace.updateShowtimeDetails(id, movie, theater, start_time, end_time, price);
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void deleteShowTime(int id){
        try {
            String result=popcornPalace.deleteShowTime(id);
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void fetchShowtineByID(int id) {
        try {
            String result=popcornPalace.fetchShowtineByID(id);
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    // booking tickets service
    public void bookTicket(int customerID, int showTimeID, int seatNumber){
        try {
            String result=popcornPalace.bookTicket(customerID, showTimeID, seatNumber);
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

}
