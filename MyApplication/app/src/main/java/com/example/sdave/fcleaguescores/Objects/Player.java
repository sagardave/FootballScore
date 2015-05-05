package com.example.sdave.fcleaguescores.Objects;

/**
 * Created by sdave on 4/16/2015.
 */
public class Player {
    int id;
    String name;
    String position;
    int jerseyNumber;
    String dateOfBirth;
    String nationality;
    String contractUntil;
    String marketValue;
    String team;

    public Player(String name, String nationality, String position, int jerseyNumber, String dateOfBirth, String contractUntil, String marketValue, String team) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.contractUntil = contractUntil;
        this.marketValue = marketValue;
        this.team = team;

    }

    public int getID()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public int getJerseyNumber()
    {
        return this.jerseyNumber;
    }
    public String getPosition()
    {
        return this.position;
    }
    public String getDateOfBirth()
    {
        return this.dateOfBirth;
    }
    public String getContractUntil()
    {
        return this.contractUntil;
    }
    public String getMarketValue()
    {
        return this.marketValue;
    }
    public String getNationality()
    {
        return this.nationality;
    }
    public String getTeam()
    {
        return this.team;
    }



}
