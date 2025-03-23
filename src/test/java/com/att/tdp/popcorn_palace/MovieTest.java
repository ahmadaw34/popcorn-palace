package com.att.tdp.popcorn_palace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.att.tdp.popcorn_palace.DTO.MovieDTO;
import com.att.tdp.popcorn_palace.DataAccessLayer.DataController;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieTest {

	@Autowired
	DataController dataController;
	@Autowired
	ServiceFactory serviceFactory;

	@LocalServerPort
	private int port = 8080;

	@BeforeEach
	void setup() {
		dataController.deleteData();
	}

	@Test
	void givenValidInput_whenAddMovie_returnOK() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie");
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/movies",
				HttpMethod.POST,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "movie added successfully");
	}

	@Test
	void givenNullTitle_whenAddMovie_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle(null); 
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("title is null", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenNullGenre_whenAddMovie_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre(null);
		movieRequest.setDuration(120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("genre is null", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidDuration_whenAddMovie_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(-120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("duration is less than 0", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidRating_whenAddMovie_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(120);
		movieRequest.setRating(10);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("rating is invalid (should be between 0-5)", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidReleaseYear_whenAddMovie_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2050);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			int currentYear = LocalDate.now().getYear();
			assertEquals("rating is invalid (should be between 0-" + currentYear + ")", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenExistedMovie_whenAddMovie_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("Sci-Fi");
		movieRequest.setDuration(120);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie with this title already exists", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenValidInput_whenUpdateMovieDetails_returnOK() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);

		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/movies",
				HttpMethod.PUT,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "movie's details updated successfully");
	}

	@Test
	void givenNullTitle_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle(null); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("title is null", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenNullGenre_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre(null);
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("genre is null", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidDuration_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(-200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("duration is less than 0", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidRating_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(10);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("rating is invalid (should be between 0-5)", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenInvalidReleaseYear_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(-5);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			int currentYear = LocalDate.now().getYear();
			assertEquals("rating is invalid (should be between 0-" + currentYear + ")", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenMovieReferencedToShowTime_whenUpdateMovieDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		serviceFactory.addShowTime("movie", "theater",LocalDateTime.of(2025, 5, 1, 17, 0),LocalDateTime.of(2025, 5, 1, 19, 0),50);

		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}
	}

	@Test
	void givenNotExistMovie_whenUpdateMovieDetails_returnBadRequest() {
		MovieDTO movieRequest = new MovieDTO();
		movieRequest.setTitle("movie"); 
		movieRequest.setGenre("updated Sci-Fi");
		movieRequest.setDuration(200);
		movieRequest.setRating(5);
		movieRequest.setReleaseYear(2005);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(movieRequest, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie with this title is not exist", e.getResponseBodyAsString());
		}
	}

	@Test
	void givenValidInput_whenDeleteMovie_returnOK() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/movies/{title}",
				HttpMethod.DELETE,
				entity,
				String.class,
				"movie");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "movie deleted successfully");
	}

	@Test
	void givenMovieReferencedToShowTime_whenDeleteMovie_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
		serviceFactory.addShowTime("movie", "theater",LocalDateTime.of(2025, 5, 1, 17, 0),LocalDateTime.of(2025, 5, 1, 19, 0),50);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies/{title}",
					HttpMethod.DELETE,
					entity,
					String.class,
					"movie");

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}
	}

	@Test
	void givenNotExistedMovie_whenDeleteMovie_returnBadRequest() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/movies/{title}",
					HttpMethod.DELETE,
					entity,
					String.class,
					"movie");

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie with this title is not exist", e.getResponseBodyAsString());
		}
	}

	@Test
	void whenFetchAllMovies_returnOK() {
		serviceFactory.addMovie("movie1", "Sci-Fi", 120, 5, 1999);
		serviceFactory.addMovie("movie2", "Sci-Fi", 120, 5, 1999);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<MovieDTO> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/movies",
				HttpMethod.GET,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
