package com.att.tdp.popcorn_palace.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.DTO.MovieDTO;
import com.att.tdp.popcorn_palace.ServiceLayer.Response;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@RestController
@RequestMapping("/api/movies")
public class MovieRequests {
    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping
    public ResponseEntity<String> addMovie(@RequestBody MovieDTO movieRequest) {
        Response response = serviceFactory.addMovie(
                movieRequest.getTitle(),
                movieRequest.getGenre(),
                movieRequest.getDuration(),
                movieRequest.getRating(),
                movieRequest.getReleaseYear());
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateMovieDetails(@RequestBody MovieDTO movie) {

        Response response = serviceFactory.updateMovieDetails(movie.getTitle(), movie.getGenre(), movie.getDuration(),
                movie.getRating(), movie.getReleaseYear());
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteMovie(@PathVariable("title") String title) {

        Response response = serviceFactory.deleteMovie(title);
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> fetchAllMovies() {
        Response response = serviceFactory.fetchAllMovies();
        if (response.isError()) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }
}
