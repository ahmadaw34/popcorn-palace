package com.att.tdp.popcorn_palace.DomainLayer.MoviePackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MovieController {
    private Map<String, Movie> movies; // <title,Movie>
    // private static MovieController instance;

    // public static MovieController getInstance() {
    //     if (instance == null) {
    //         instance = new MovieController();
    //     }
    //     return instance;
    // }

    private MovieController() {
        this.movies = new HashMap<>();
    }

    public String addMovie(String title, String genre, int duration, double rating, int release_year) throws Exception {
        if (movies.containsKey(title)) {
            throw new Exception("movie with this title already exists");
        }
        Movie movie = new Movie(title, genre, duration, rating, release_year);
        movies.put(title, movie);
        return "movie added successfully";
    }

    public String updateMovieDetails(String title, String genre, int duration, double rating, int release_year)
            throws Exception {
        if (!movies.containsKey(title)) {
            throw new Exception("movie with this title is not exists");
        }
        Movie movie = movies.get(title);
        movie.setGenre(genre);
        movie.setDuration(duration);
        movie.setRating(rating);
        movie.setRelease_year(release_year);
        return "movie's details updated successfully";
    }

    public String deleteMovie(String title) throws Exception {
        if (!movies.containsKey(title)) {
            throw new Exception("movie with this title is not exists");
        }
        movies.remove(title);
        return "movie deleted successfully";
    }

    public String fetchAllMovies() {
        List<Movie> movieList = new ArrayList<>(movies.values());
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieList);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public int getMovieDuration(String title){
        return movies.get(title).getDuration();
    }

}
