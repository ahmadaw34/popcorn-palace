package com.att.tdp.popcorn_palace.DomainLayer.ShowTimePackage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ShowTimeController {
    private Map<Integer, ShowTime> showTimes;
    // private static ShowTimeController instance;

    // public static ShowTimeController getInstance() {
    //     if (instance == null) {
    //         instance = new ShowTimeController();
    //     }
    //     return instance;
    // }

    private ShowTimeController() {
        this.showTimes = new HashMap<>();
    }

    public void cleanup(){
        this.showTimes = new HashMap<>();
    }

    private static boolean hasOverlap(List<ShowTime> showtimes, ShowTime newShowtime) {
        for (ShowTime existing : showtimes) {
            if (newShowtime.getStart_time().isBefore(existing.getEnd_time()) &&
                newShowtime.getEnd_time().isAfter(existing.getStart_time()) &&
                newShowtime.getTheater().equals(existing.getTheater())) {
                return true;
            }
        }
        return false;
    }

    public String addShowTime(int id, String movie, String theater, LocalDateTime start_time, LocalDateTime end_time,
            double price) throws Exception {
        if (showTimes.containsKey(id)) {
            throw new Exception("showtime with id "+id+" already exists");
        }
        List<ShowTime> showTimesList=new ArrayList<>(showTimes.values());
        ShowTime showTime = new ShowTime(id, movie, theater, start_time, end_time, price);
        if(hasOverlap(showTimesList, showTime)){
            throw new Exception("cant add showtime: it overlaps");
        }
        showTimes.put(id, showTime);
        return "showtime "+id+" added successfully";
    }

    public String updateShowtimeDetails(int id, String movie, String theater, LocalDateTime start_time,
            LocalDateTime end_time, double price) throws Exception {
        if (!showTimes.containsKey(id)) {
            throw new Exception("showtime with id "+id+" is not exist");
        }
        List<ShowTime> showTimesList=new ArrayList<>(showTimes.values());
        ShowTime updatedShowTime = new ShowTime(id, movie, theater, start_time, end_time, price);
        if(hasOverlap(showTimesList, updatedShowTime)){
            throw new Exception("cant add showtime: it overlaps");
        }
        ShowTime showTime=showTimes.get(id);
        showTime.setMovie(movie);
        showTime.setTheater(theater);
        showTime.setStart_time(start_time);
        showTime.setEnd_time(end_time);
        showTime.setPrice(price);
        return "showtime's details updated successfully";
    }

    public String deleteShowTime(int id) throws Exception{
        if (!showTimes.containsKey(id)) {
            throw new Exception("showtime with this id is not exist");
        }
        showTimes.remove(id);
        return "showtime deleted successfully";
    }

    public String fetchShowtineByID(int id) throws Exception{
        if (!showTimes.containsKey(id)) {
            throw new Exception("showtime with this id is not exist");
        }
        ShowTime showTime=showTimes.get(id);
        return showTime.toString();
    }

    public boolean isShowTimeExits(int id){
        return showTimes.containsKey(id);
    }

    public List<Integer> showtimeForMovie(String movie){
        List<Integer> showtimesForMovie=new ArrayList<>();
        for(int id : showTimes.keySet()){
            if(showTimes.get(id).getMovie().equals(movie)){
                showtimesForMovie.add(id);
            }
        }
        if(showtimesForMovie.size()!=0){
            return showtimesForMovie;
        }
        else{
            return null;
        }
    }
}
