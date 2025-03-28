package com.att.tdp.popcorn_palace.DomainLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

import com.att.tdp.popcorn_palace.DomainLayer.MoviePackage.MovieController;
import com.att.tdp.popcorn_palace.DomainLayer.ShowTimePackage.ShowTimeController;
import com.att.tdp.popcorn_palace.DomainLayer.TicketBookingPackage.BookingController;

@Component
public class PopcornPalace {
    @Autowired
    private MovieController movieController;
    @Autowired
    private ShowTimeController showTimeController;
    @Autowired
    private BookingController bookingController;

    private PopcornPalace() {
    }

    public void cleanup() {
        cleanupMovie();
        cleanupShowtime();
        cleanupTickets();
    }

    public void cleanupShowtime() {
        showTimeController.cleanup();
    }

    public void cleanupMovie() {
        movieController.cleanup();
    }

    public void cleanupTickets() {
        bookingController.cleanup();
    }

    // Movie
    private void isValidMovieDetails(String title, String genre, int duration, double rating, int release_year)
            throws Exception {
        if (title == null || title == "") {
            throw new Exception("title is null");
        }
        if (genre == null || genre == "") {
            throw new Exception("genre is null");
        }
        if (duration < 0) {
            throw new Exception("duration is less than 0");
        }
        if (rating > 5 || rating < 0) {
            throw new Exception("rating is invalid (should be between 0-5)");
        }
        int currentYear = LocalDate.now().getYear();
        if (release_year > currentYear || release_year < 0) {
            throw new Exception("rating is invalid (should be between 0-" + currentYear + ")");
        }
    }

    public String addMovie(String title, String genre, int duration, double rating, int release_year) throws Exception {
        isValidMovieDetails(title, genre, duration, rating, release_year);
        return movieController.addMovie(title, genre, duration, rating, release_year);
    }

    public String updateMovieDetails(String title, String genre, int duration, double rating, int release_year)
            throws Exception {
        isValidMovieDetails(title, genre, duration, rating, release_year);
        List<Integer> showtimes = showTimeController.showtimeForMovie(title);
        if (showtimes != null) {
            String ids = "";
            for (int id : showtimes) {
                ids += id + ", ";
            }
            throw new Exception("cant update movie because showtimes: " + ids + " referenced to this movie");
        }
        return movieController.updateMovieDetails(title, genre, duration, rating, release_year);
    }

    public String deleteMovie(String title) throws Exception {
        if (title == null || title == "") {
            throw new Exception("title is null");
        }
        List<Integer> showtimes = showTimeController.showtimeForMovie(title);
        if (showtimes != null) {
            String ids = "";
            for (int id : showtimes) {
                ids += id + ", ";
            }
            throw new Exception("cant delete movie because showtimes: " + ids + " referenced to this movie");
        }
        return movieController.deleteMovie(title);
    }

    public String fetchAllMovies() {
        return movieController.fetchAllMovies();
    }

    // ShowTime
    private void isValidShowTimeDetails(int id, String movie, String theater, LocalDateTime start_time,
            LocalDateTime end_time,
            double price) throws Exception {
        if (id < 0) {
            throw new Exception("invalid id(less than 0)");
        }
        if (movie == null || movie == "") {
            throw new Exception("movie title is null");
        }
        if (theater == null || theater == "") {
            throw new Exception("theater is null");
        }
        if (start_time == null) {
            throw new Exception("start time is null");
        }
        if (end_time == null) {
            throw new Exception("end time is null");
        }
        if (end_time.isBefore(start_time)) {
            throw new Exception("end time is before start time");
        }
        Duration duration = Duration.between(start_time, end_time);
        int minutes = (int) duration.toMinutes();
        int actualDuration = movieController.getMovieDuration(movie);
        if (actualDuration != minutes) {
            throw new Exception("showtime duration is not equal to movie duration");
        }
        if (price < 0) {
            throw new Exception("invalid price(less than 0)");
        }
    }

    public String addShowTime(int id, String movie, String theater, LocalDateTime start_time, LocalDateTime end_time,
            double price) throws Exception {
        isValidShowTimeDetails(id, movie, theater, start_time, end_time, price);
        return showTimeController.addShowTime(id, movie, theater, start_time, end_time, price);
    }

    public String updateShowtimeDetails(int id, String movie, String theater, LocalDateTime start_time,
            LocalDateTime end_time, double price) throws Exception {
        isValidShowTimeDetails(id, movie, theater, start_time, end_time, price);
        List<Integer> ticketList = bookingController.ticketForShowtime(id);
        if (ticketList != null) {
            String ids = "";
            for (int customerId : ticketList) {
                ids += customerId + ", ";
            }
            throw new Exception("cant update showtime because customers: " + ids + " referenced to this showtime");
        }
        return showTimeController.updateShowtimeDetails(id, movie, theater, start_time, end_time, price);
    }

    public String deleteShowTime(int id) throws Exception {
        if (id < 0) {
            throw new Exception("invalid id(less than 0)");
        }
        List<Integer> ticketList = bookingController.ticketForShowtime(id);
        if (ticketList != null) {
            String ids = "";
            for (int customerId : ticketList) {
                ids += customerId + ", ";
            }
            throw new Exception("cant delete showtime because customers: " + ids + " referenced to this showtime");
        }
        return showTimeController.deleteShowTime(id);
    }

    public String fetchShowtineByID(int id) throws Exception {
        if (id < 0) {
            throw new Exception("invalid id(less than 0)");
        }
        return showTimeController.fetchShowtineByID(id);
    }

    // BookingTickets
    public String bookTicket(int customerID, int showTimeID, int seatNumber) throws Exception {
        if (customerID < 0) {
            throw new Exception("invalid customer ID (less than 0)");
        }
        if (!showTimeController.isShowTimeExits(showTimeID)) {
            throw new Exception("showtime is not exist");
        }
        if (seatNumber < 0) {
            throw new Exception("invalid seat number (less than 0)");
        }
        return bookingController.bookTicket(customerID, showTimeID, seatNumber);
    }
}
