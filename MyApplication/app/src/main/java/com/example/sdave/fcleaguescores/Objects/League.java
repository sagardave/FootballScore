package com.example.sdave.fcleaguescores.Objects;

import java.io.Serializable;

/**
 * Created by sdave on 4/22/2015.
 */
public class League implements Serializable {



    String name;
    String caption;
    int year;
    int numOfTeams;
    int numOfGame;
    String lastUpdated;
    String teamsUrl;
    private String fixturesUrl;
    private String leagueTableUrl;

    public League() {

    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public int getYear() {
        return year;
    }

    public int getNumOfTeams() {
        return numOfTeams;
    }

    public int getNumOfGame() {
        return numOfGame;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getTeamsUrl() {
        return teamsUrl;
    }

    public String getFixturesUrl() {
        return fixturesUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setNumOfTeams(int numOfTeams) {
        this.numOfTeams = numOfTeams;
    }

    public void setNumOfGame(int numOfGame) {
        this.numOfGame = numOfGame;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public void setFixturesUrl(String fixturesUrl) {
        this.fixturesUrl = fixturesUrl;
    }

    public String getLeagueTableUrl() {
        return leagueTableUrl;
    }

    public void setLeagueTableUrl(String leagueTableUrl) {
        this.leagueTableUrl = leagueTableUrl;
    }
}
