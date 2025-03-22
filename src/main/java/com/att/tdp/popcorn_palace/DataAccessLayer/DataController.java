package com.att.tdp.popcorn_palace.DataAccessLayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.MovieEntity;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.ShowTimeRepository;
import com.att.tdp.popcorn_palace.DataAccessLayer.Repository.TicketsRepository;
import com.att.tdp.popcorn_palace.DomainLayer.PopcornPalace;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

import java.util.List;
import java.util.logging.Logger;


@Service
public class DataController {
    public static final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private TicketsRepository ticketsRepository;
    @Autowired
    private ServiceFactory serviceFactory;

    public void clearAll(){
        movieRepository.deleteAll();
        showTimeRepository.deleteAll();
        ticketsRepository.deleteAll();
    }

    public void loadData() throws Exception{
        List<MovieEntity> movies=movieRepository.findAll();
        for(MovieEntity movieEntity : movies){
            serviceFactory.getPopcornPalace().addMovie(
                movieEntity.getTitle(),
                movieEntity.getGenre(),
                movieEntity.getDuration(),
                movieEntity.getRating(),
                movieEntity.getReleaseYear());
        }
        
    }

    public void addMovie(String title, String genre, int duration, double rating, int release_year){
        MovieEntity movieEntity=new MovieEntity();
        movieEntity.setTitle(title);
        movieEntity.setGenre(genre);
        movieEntity.setDuration(duration);
        movieEntity.setRating(rating);
        movieEntity.setReleaseYear(release_year);
        movieRepository.save(movieEntity);
        LOGGER.info("movie entity added to database");
    }

    public void updateMovieDetails(String title, String genre, int duration, double rating, int release_year){
        MovieEntity movieEntity=movieRepository.findById(title).get();
        movieEntity.setGenre(genre);
        movieEntity.setDuration(duration);
        movieEntity.setRating(rating);
        movieEntity.setReleaseYear(release_year);
        movieRepository.save(movieEntity);
        LOGGER.info("movie "+ title +" details updated successfully in database");
    }

    public void deleteMovie(String title){
        movieRepository.deleteById(title);
        LOGGER.info("movie "+ title +" deleted successfully from database");
    }

}
