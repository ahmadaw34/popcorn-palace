package com.att.tdp.popcorn_palace.DataAccessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.MovieEntity;
import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.ShowTimeEntity;
import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.TicketEntity;
import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.TicketInfo;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.ShowTimeRepository;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.TicketsRepository;
import com.att.tdp.popcorn_palace.DomainLayer.PopcornPalace;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DataController {
    public static final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    @Autowired
    private PopcornPalace popcornPalace;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private TicketsRepository ticketsRepository;

    public MovieRepository getMovieRepository() {
        return movieRepository;
    }

    public ShowTimeRepository getShowTimeRepository() {
        return showTimeRepository;
    }

    public TicketsRepository getTicketsRepository() {
        return ticketsRepository;
    }

    @Transactional
    public void deleteData() {
        ticketsRepository.deleteAll();
        showTimeRepository.deleteAll();
        movieRepository.deleteAll();
        popcornPalace.cleanup();
    }

    @PostConstruct
    public void loadData() throws Exception {
        loadMovies();
        loadShowtime();
        loadTickets();
    }

    public void loadMovies() throws Exception{
        List<MovieEntity> movies = movieRepository.findAll();
        for (MovieEntity movieEntity : movies) {
            popcornPalace.addMovie(
                    movieEntity.getTitle(),
                    movieEntity.getGenre(),
                    movieEntity.getDuration(),
                    movieEntity.getRating(),
                    movieEntity.getReleaseYear());
        }
    }

    public void loadShowtime() throws Exception{
        List<ShowTimeEntity> showTimeEntities=showTimeRepository.findAll();
        for(ShowTimeEntity showTime : showTimeEntities){
            popcornPalace.addShowTime(
                showTime.getId(),
                showTime.getMovie().getTitle(), 
                showTime.getTheater(),
                showTime.getStart_time(),
                showTime.getEnd_time(),
                showTime.getPrice());
        }
    }

    public void loadTickets() throws Exception{
        List<TicketEntity> ticketEntities=ticketsRepository.findAll();
        for(TicketEntity ticket : ticketEntities){
            for(TicketInfo ticketInfo : ticket.getCustomerTickets()){
                popcornPalace.bookTicket(
                    ticket.getCustomerID(),
                    ticketInfo.getShowTimeID().getId(),
                    ticketInfo.getSeatNumber());
            }
        }
    }

    // movies repository
    public void addMovie(String title, String genre, int duration, double rating, int release_year) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(title);
        movieEntity.setGenre(genre);
        movieEntity.setDuration(duration);
        movieEntity.setRating(rating);
        movieEntity.setReleaseYear(release_year);
        movieRepository.save(movieEntity);
        LOGGER.info("movie entity added to database");
    }

    public void updateMovieDetails(String title, String genre, int duration, double rating, int release_year) {
        MovieEntity movieEntity = movieRepository.findById(title).get();
        movieEntity.setGenre(genre);
        movieEntity.setDuration(duration);
        movieEntity.setRating(rating);
        movieEntity.setReleaseYear(release_year);
        movieRepository.save(movieEntity);
        LOGGER.info("movie " + title + " details updated successfully in database");
    }

    public void deleteMovie(String title) {
        movieRepository.deleteById(title);
        LOGGER.info("movie " + title + " deleted successfully from database");
    }

    // showtime repository
    public int getNewShowTimeId() {
        if (showTimeRepository.count() == 0)
            return -1;
        return showTimeRepository.findAll().getLast().getId() + 1;
    }

    public int addShowTime(String movie, String theater, LocalDateTime start_time, LocalDateTime end_time,
            double price) {
        ShowTimeEntity showTimeEntity = new ShowTimeEntity();
        MovieEntity referencedMovie = movieRepository.findById(movie).get();
        showTimeEntity.setMovie(referencedMovie);
        showTimeEntity.setTheater(theater);
        showTimeEntity.setStart_time(start_time);
        showTimeEntity.setEnd_time(end_time);
        showTimeEntity.setPrice(price);
        showTimeRepository.save(showTimeEntity);
        LOGGER.info("showtime entity added to database");
        return showTimeEntity.getId();
    }

    public void updateShowtimeDetails(int id, String movie, String theater, LocalDateTime start_time,
            LocalDateTime end_time, double price) {
        ShowTimeEntity showTimeEntity = showTimeRepository.findById(id).get();
        MovieEntity referencedMovie = movieRepository.findById(movie).get();
        showTimeEntity.setMovie(referencedMovie);
        showTimeEntity.setTheater(theater);
        showTimeEntity.setStart_time(start_time);
        showTimeEntity.setEnd_time(end_time);
        showTimeEntity.setPrice(price);
        showTimeRepository.save(showTimeEntity);
        LOGGER.info("showtime's details with id="+id+" updated successfully in database");
    }

    public void deleteShowTime(int id){
        showTimeRepository.deleteById(id);
        LOGGER.info("showtime with id="+id+" deleted successfully from database");
    }

    // tickets repository
    public void bookTicket(int customerID, int showTimeID, int seatNumber){
        ShowTimeEntity referencedShowtime=showTimeRepository.findById(showTimeID).get();
        if(!ticketsRepository.existsById(customerID)){
            TicketEntity ticketEntity=new TicketEntity();
            ticketEntity.setCustomerID(customerID);
            TicketInfo ticketInfo=new TicketInfo();
            ticketInfo.setShowTimeID(referencedShowtime);
            ticketInfo.setSeatNumber(seatNumber);
            ticketEntity.addTicket(ticketInfo);
            ticketsRepository.save(ticketEntity);
        }
        else{
            TicketEntity ticketEntity=ticketsRepository.findById(customerID).get();
            TicketInfo ticketInfo=new TicketInfo();
            ticketInfo.setShowTimeID(referencedShowtime);
            ticketInfo.setSeatNumber(seatNumber);
            ticketEntity.addTicket(ticketInfo);
            ticketsRepository.save(ticketEntity);
        }
        LOGGER.info("ticket added to database");
    }
}
