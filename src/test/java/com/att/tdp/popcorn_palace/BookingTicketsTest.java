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

import com.att.tdp.popcorn_palace.DTO.TicketDTO;
import com.att.tdp.popcorn_palace.DataAccessLayer.DataController;
import com.att.tdp.popcorn_palace.ServiceLayer.Response;
import com.att.tdp.popcorn_palace.ServiceLayer.ServiceFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingTicketsTest {
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
	void givenValidInput_BookTicket_returnOK() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		TicketDTO ticketDTO=new TicketDTO();
        ticketDTO.setCustomerID(123456789);
        ticketDTO.setShowTimeID(showTimeId);
        ticketDTO.setSeatNumber(77);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<TicketDTO> entity = new HttpEntity<>(ticketDTO, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/api/booking",
				HttpMethod.POST,
				entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

    @Test
	void givenInvalidCustomerId_whenBook_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		TicketDTO ticketDTO=new TicketDTO();
        ticketDTO.setCustomerID(-123456789);
        ticketDTO.setShowTimeID(showTimeId);
        ticketDTO.setSeatNumber(77);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<TicketDTO> entity = new HttpEntity<>(ticketDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/booking",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("invalid customer ID (less than 0)", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenNotExistedShowtime_whenBook_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		TicketDTO ticketDTO=new TicketDTO();
        ticketDTO.setCustomerID(123456789);
        ticketDTO.setShowTimeID(showTimeId*10);
        ticketDTO.setSeatNumber(77);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<TicketDTO> entity = new HttpEntity<>(ticketDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/booking",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("showtime is not exist", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenInvalidSeatNumber_whenBook_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);

		TicketDTO ticketDTO=new TicketDTO();
        ticketDTO.setCustomerID(123456789);
        ticketDTO.setShowTimeID(showTimeId);
        ticketDTO.setSeatNumber(-77);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<TicketDTO> entity = new HttpEntity<>(ticketDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/booking",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("invalid seat number (less than 0)", e.getResponseBodyAsString());
		}
	}

    @Test
	void givenBookedSeat_whenBook_returnBadRequest() {
        serviceFactory.addMovie("movie", "Sci-Fi", 120, 5, 1999);
        Response serviceResponse=serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 17, 0), LocalDateTime.of(2025, 5, 1, 19, 0), 50);
        serviceFactory.addShowTime("movie","theater", LocalDateTime.of(2025, 5, 1, 20, 0), LocalDateTime.of(2025, 5, 1, 22, 0), 50);
        String showtimeId=serviceResponse.getMessage().split(" ")[1];
        int showTimeId=Integer.parseInt(showtimeId);
        serviceFactory.bookTicket(123456, showTimeId, 77);

		TicketDTO ticketDTO=new TicketDTO();
        ticketDTO.setCustomerID(123456789);
        ticketDTO.setShowTimeID(showTimeId);
        ticketDTO.setSeatNumber(77);
        

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<TicketDTO> entity = new HttpEntity<>(ticketDTO, headers);

		try {
			restTemplate.exchange(
					"http://localhost:" + port + "/api/booking",
					HttpMethod.POST,
					entity,
					String.class);

			fail("Expected BAD_REQUEST");
		} catch (HttpClientErrorException.BadRequest e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
			assertEquals("seat 77 for the showtime " + showTimeId + " already booked", e.getResponseBodyAsString());
		}
	}
    

}
