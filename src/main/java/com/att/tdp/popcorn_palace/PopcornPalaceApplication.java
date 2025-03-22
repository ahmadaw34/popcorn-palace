package com.att.tdp.popcorn_palace;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.att.tdp.popcorn_palace.DTO.MovieDTO;
import com.att.tdp.popcorn_palace.DTO.ShowTimeDTO;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@SpringBootApplication
public class PopcornPalaceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PopcornPalaceApplication.class, args);
        // ServiceFactory serviceFactory = context.getBean(ServiceFactory.class);
        // System.out.println("************************************************************************************************************************************************************");
        // serviceFactory.addMovie("title", "genre", 0, 0, 0);
        // serviceFactory.fetchAllMovies();
        // serviceFactory.deleteMovie("title");
        // serviceFactory.fetchAllMovies();
        // serviceFactory.addMovie("title", "genre", 0, 0, 0);
        // serviceFactory.fetchAllMovies();
        // serviceFactory.updateMovieDetails("title", "genreeeeeeeeeeeeeeee", 50, 0, 0);
        // serviceFactory.fetchAllMovies();
        // serviceFactory.addShowTime(0, "title", "kfar kana", 1, 4, 2024, 5, 0, 1, 4,
        // 2024, 5, 50, 0);
        // serviceFactory.addShowTime(1, "title", "kfar kana", 1, 4, 2024,6,30 , 1, 4,
        // 2024, 7, 20, 0);
        // serviceFactory.bookTicket(0, 0, 0);
        // serviceFactory.bookTicket(1, 0, 0);
        // serviceFactory.fetchShowtineByID(0);
        // serviceFactory.fetchShowtineByID(5);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/movies";

        MovieDTO movieRequest = new MovieDTO();
        movieRequest.setTitle("The Matrix");
        movieRequest.setGenre("Sci-Fi");
        movieRequest.setDuration(50);
        movieRequest.setRating(4);
        movieRequest.setReleaseYear(1999);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("Response from POST: " + response);
        } catch (Exception e) {
            System.out.println("Response from POST: " + e.getMessage());
        }

        String showtimeurl = "http://localhost:8080/api/showtime";
        ShowTimeDTO showTimeRequest = new ShowTimeDTO();
        showTimeRequest.setId(0);
        showTimeRequest.setMovie("The Matrix");
        showTimeRequest.setTheater("theater");
        showTimeRequest.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 30));
        showTimeRequest.setEnd_time(LocalDateTime.of(2025, 5, 1, 17, 50));
        showTimeRequest.setPrice(100);

        HttpEntity<ShowTimeDTO> showtimeentity = new HttpEntity<>(showTimeRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(showtimeurl, HttpMethod.POST, showtimeentity, String.class);

            System.out.println("Response from POST: " + response);
        } catch (Exception e) {
            System.out.println("Response from POST: " + e.getMessage());
        }

        // MovieDTO movieRequest2 = new MovieDTO();
        // movieRequest2.setTitle("The Matrix");
        // movieRequest2.setGenre("Sci-Fiiiiiiiiiiiiiiiiiiiiiiiiii");
        // movieRequest2.setDuration(136);
        // movieRequest2.setRating(4);
        // movieRequest2.setReleaseYear(1999);

        // HttpEntity<MovieDTO> entity2 = new HttpEntity<>(movieRequest2, headers);

        // try {
        // ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.PUT,
        // entity2, String.class,
        // "The Matrix");

        // System.out.println("Response from PUT: " + response2);
        // } catch (Exception e) {
        // System.out.println("Response from PUT: " + e.getMessage());

        // }

        // HttpEntity<String> entity3 = new HttpEntity<>(headers);
        // String url2 = "http://localhost:8080/api/movies/{title}";

        // try {
        // ResponseEntity<String> response3 = restTemplate.exchange(url2,
        // HttpMethod.DELETE, entity3, String.class,
        // "The Matrix");

        // System.out.println("Response from DELETE: " + response3);
        // } catch (Exception e) {
        // System.out.println("Response from DELETE: " + e.getMessage());

        // }

        // try {
        // ResponseEntity<String> response4 = restTemplate.exchange(url, HttpMethod.GET,
        // entity3, String.class);

        // System.out.println("Response from GET: " + response4);
        // } catch (Exception e) {
        // System.out.println("Response from GET: " + e.getMessage());

        // }

    }

}
