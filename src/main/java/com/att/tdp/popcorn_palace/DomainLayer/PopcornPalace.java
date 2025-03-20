package com.att.tdp.popcorn_palace.DomainLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

import com.att.tdp.popcorn_palace.DomainLayer.MoviePackage.MovieController;
import com.att.tdp.popcorn_palace.DomainLayer.ShowTimePackage.ShowTimeController;
// import com.att.tdp.popcorn_palace.DomainLayer.TicketBookingPackage.BookingController;

public class PopcornPalace {
    private MovieController movieController;
    private ShowTimeController showTimeController;
    // private BookingController bookingController;
    private static PopcornPalace instance;

    public static PopcornPalace getInstance() {
        if (instance == null) {
            instance = new PopcornPalace();
        }
        return instance;
    }

    private PopcornPalace() {
        movieController = MovieController.getInstance();
        showTimeController = ShowTimeController.getInstance();
        // bookingController = BookingController.getInstance();
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
        return movieController.updateMovieDetails(title, genre, duration, rating, release_year);
    }

    public String deleteMovie(String title) throws Exception {
        if (title == null || title == "") {
            throw new Exception("title is null");
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
        int minutes = (int) duration.toMinutes() % 60;
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
        return showTimeController.updateShowtimeDetails(id, movie, theater, start_time, end_time, price);
    }

    public String deleteShowTime(int id) throws Exception {
        if (id < 0) {
            throw new Exception("invalid id(less than 0)");
        }
        return showTimeController.deleteShowTime(id);
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
        return bookTicket(customerID, showTimeID, seatNumber);
    }
}
