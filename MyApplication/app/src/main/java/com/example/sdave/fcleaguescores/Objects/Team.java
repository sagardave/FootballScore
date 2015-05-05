package com.example.sdave.fcleaguescores.Objects;

/**
 * Created by sdave on 4/14/2015.
 */
public class Team {
    int id;
    private String name;
    private String shortName;
    private String marketValue;
    private String imageUrl;
    private String code;
    private String playersURL;
    private String league;

    public Team() {

    }

    public int getID()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public String getShortName()
    {
        return this.shortName;
    }
    public String getImageUrl()
    {
        return this.imageUrl;
    }
    public String getMarketValue()
    {
        return this.marketValue;
    }
    public String getPlayersURL()
    {
        return this.playersURL;
    }
    public String getLeague()
    {
        return this.league;
    }
    public String getCode()
    {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPlayersURL(String playersURL) {
        this.playersURL = playersURL;
    }

    public void setLeague(String league) {
        this.league = league;
    }

}
