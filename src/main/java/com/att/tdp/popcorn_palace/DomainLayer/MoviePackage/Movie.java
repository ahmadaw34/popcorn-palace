package com.att.tdp.popcorn_palace.DomainLayer.MoviePackage;

public class Movie {
    private String title;
    private String genre;
    private int duration;
    private double rating;
    private int release_year;

    public Movie(String title, String genre, int duration, double rating, int release_year) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.release_year = release_year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

}
