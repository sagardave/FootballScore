package com.example.sdave.fcleaguescores.Objects;

import java.util.Date;

/**
 * Created by sdave on 4/22/2015.
 */
public class Fixture {

    int id;

    String date;
    String status;
    int matchDay;
    String homeTeamName;
    String awayTeamName;
    int goalsHomeTeam;
    int goalsAwayTeam;
    String league;
    String homeTeamNameUrl;
    String awayTeamNameUrl;



    public Fixture() {
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getGoalsAwayTeam()
    {
        return this.goalsAwayTeam;
    }

    public int getGoalsHomeTeam()
    {
        return this.goalsHomeTeam;
    }

    public String getLeague() {
        return league;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }


    public void setGoalsHomeTeam(int goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    public void setGoalsAwayTeam(int goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHomeTeamNameUrl() {
        return homeTeamNameUrl;
    }

    public void setHomeTeamNameUrl(String homeTeamNameUrl) {
        this.homeTeamNameUrl = homeTeamNameUrl;
    }

    public String getAwayTeamNameUrl() {
        return awayTeamNameUrl;
    }

    public void setAwayTeamNameUrl(String awayTeamNameUrl) {
        this.awayTeamNameUrl = awayTeamNameUrl;
    }
}
