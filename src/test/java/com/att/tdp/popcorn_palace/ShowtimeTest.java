package com.att.tdp.popcorn_palace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

import com.att.tdp.popcorn_palace.DTO.ShowTimeDTO;
import com.att.tdp.popcorn_palace.DataAccessLayer.DataController;
import com.att.tdp.popcorn_palace.ServiceLayer.Response;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShowtimeTest {

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
	void givenValidInput_whenAddShowTime_returnOK() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/showtime",
				HttpMethod.POST,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(dataController.getShowTimeRepository().findAll().size(),1);
	}

    @Test
	void givenNullMovie_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie(null);
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie title is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullTheater_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater(null);
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("theater is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullStart_Time_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(null);
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("start time is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullEnd_Time_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(null);
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("end time is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenEnd_TimeBeforeStart_Time_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 12, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("end time is before start time", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenDurationNotEqualToMovieDuration_whenAddShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 18, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime duration is not equal to movie duration", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenInvalidPrice_whenAddShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(-50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("invalid price(less than 0)", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNotExistedMovie_whenAddShowTime_returnBadRequest() {
		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie with this title is not exist", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenOverlapsShowTimes_whenAddShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 16, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 18, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("cant add showtime: it overlaps", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenValidInput_whenUpdateShowtimeDetails_returnOK() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theaterrrr");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(100);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/showtime/update",
				HttpMethod.PUT,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "showtime's details updated successfully");
		assertEquals(dataController.getShowTimeRepository().findById(showTimeId).get().getTheater(), "theaterrrr");
	}

    @Test
	void givenNullMovie_whenUpdateShowtimeDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie(null);
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("movie title is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullTheater_whenUpdateShowtimeDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater(null);
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("theater is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullStart_Time_whenUpdateShowtimeDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(null);
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("start time is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNullEnd_Time_whenUpdateShowtimeDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(null);
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("end time is null", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenEnd_TimeBeforeStart_Time_whenUpdateShowtimeDetails_returnBadRequest() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 12, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("end time is before start time", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenDurationNotEqualToMovieDuration_whenUpdateShowtimeDetails_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 18, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime duration is not equal to movie duration", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenInvalidPrice_whenUpdateShowtimeDetails_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(-50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("invalid price(less than 0)", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenShowtimeReferencedToTicket_whenUpdateShowtimeDetails_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);
        serviceFactory.bookTicket(123456789, showTimeId, 77);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("cant update showtime because customers: 123456789,  referenced to this showtime", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNotExistedShowtime_whenUpdateShowtimeDetails_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId*10);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 17, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime with id "+showTimeId*10+" is not exist", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenOverlapsShowtime_whenUpdateShowtimeDetails_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		ShowTimeDTO showTimeDTO=new ShowTimeDTO();
        showTimeDTO.setId(showTimeId);
        showTimeDTO.setMovie("movie");
        showTimeDTO.setTheater("theater");
        showTimeDTO.setStart_time(LocalDateTime.of(2025, 5, 1, 19, 0));
        showTimeDTO.setEnd_time(LocalDateTime.of(2025, 5, 1, 21, 0));
        showTimeDTO.setPrice(50);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(showTimeDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/update",
					HttpMethod.PUT,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("cant add showtime: it overlaps", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenValidInput_deleteShowTime_returnOK() {
		serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/showtime/{id}",
				HttpMethod.DELETE,
				entity,
				String.class,
				showTimeId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "showtime deleted successfully");
		assertEquals(dataController.getShowTimeRepository().findAll().size(), 0);
	}

    @Test
	void givenNotExistedShowtime_whenDeleteShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>( headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/{id}",
					HttpMethod.DELETE,
					entity,
					String.class,
                    showTimeId*10);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime with this id is not exist", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenInvalidId_whenDeleteShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>( headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/{id}",
					HttpMethod.DELETE,
					entity,
					String.class,
                    showTimeId*-1);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("invalid id(less than 0)", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenShowtimeReferencedToTicket_whenDeleteShowTime_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);
        serviceFactory.bookTicket(123456789, showTimeId, 77);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>( headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/{id}",
					HttpMethod.DELETE,
					entity,
					String.class,
                    showTimeId);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("cant delete showtime because customers: 123456789,  referenced to this showtime", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenValidInput_whenFetchById_returnOK() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/showtime/{id}",
				HttpMethod.GET,
				entity,
				String.class,
                showTimeId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

    @Test
	void givenNotExistedShowtime_whenFetchById_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<ShowTimeDTO> entity = new HttpEntity<>( headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/showtime/{id}",
					HttpMethod.GET,
					entity,
					String.class,
                    showTimeId*10);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime with this id is not exist", e.getResponseBodyAsString());
		}
	}

    
}
