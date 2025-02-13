package vttp.batch5.paf.movies.models;

import java.util.UUID;

public class Movie {
    private String imdb_id;
    private float vote_average;
    private int vote_count;
    private String release_date;
    private Integer revenue;
    private Integer budget;
    private int runtime;

    public Movie() {
        this.imdb_id = UUID.randomUUID().toString().substring(0, 16);
    }

    public String getImdb_id() {
        return imdb_id;
    }
    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }
    public float getVote_average() {
        return vote_average;
    }
    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
    public int getVote_count() {
        return vote_count;
    }
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Integer getRevenue() {
        return revenue;
    }
    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }
    public Integer getBudget() {
        return budget;
    }
    public void setBudget(Integer budget) {
        this.budget = budget;
    }
    public int getRuntime() {
        return runtime;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    
}
