package com.example.sdave.fcleaguescores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sdave.fcleaguescores.Objects.League;
import com.example.sdave.fcleaguescores.Objects.Player;
import com.example.sdave.fcleaguescores.Objects.Fixture;
import com.example.sdave.fcleaguescores.Objects.LeagueTableRow;
import com.example.sdave.fcleaguescores.Objects.Team;

import java.util.ArrayList;

/**
 * Created by sdave on 4/15/2015.
 */
public class DBAdapter{

    SQLiteDatabase db;
    static final String dbName="footballLeaguesData";
    private final Context context;

    public static final int DATABASE_VERSION = 1;

    static final String teamsTable="Teams";
    static final String colTeamID="TeamID";
    static final String colTeamName="TeamName";
    static final String colTeamMarketValue="TeamMarketValue";
    static final String colTeamShortName="TeamShortName";
    static final String colImageURL="ImageURL";
    static final String colTeamLeague="TeamLeague";
    static final String colTeamCode="TeamCode";
    static final String colPlayersURL="TeaPlayersURL";

    static final String playersTable="Players";
    static final String colPlayerID="PlayerID";
    static final String colPlayerName="PlayerName";
    static final String colPlayerPosition="PlayerPosition";
    static final String colPlayerJerseyNumber="PlayerJerseyNumber";
    static final String colPlayerDateOfBirth="PlayerDateOfBirth";
    static final String colPlayerNationality="PlayerNationality";
    static final String colPlayerMarketValue="PlayerMarketValue";
    static final String colPlayerContractUntil="PlayerContractUntil";
    static final String colPlayerTeam="PlayerTeam";

    static final String FixturesTable="Fxtures";
    static final String colFixturesID="FixturesID";
    static final String colFixturestatus="Fixturestatus";
    static final String colFixturematchDay="FixturematchDay";
    static final String colFixtureHomeTeamName="FixtureHomeTeamName";
    static final String colFixtureAwayTeamName="FixtureAwayTeamName";
    static final String colFixtureAwayTeamGoals="FixtureAwayTeamNameGoals";
    static final String colFixturHomeTeamGoals="FixtureHomeTeamNameGoals";
    static final String colFixtureDate="Fixturedate";
    static final String colFixtureLeague="FixtureLeague";
    static final String colFixturHomeTeamUrl="FixtureHomeTeamUrl";
    static final String colFixturAwayTeamUrl="FixtureAwayTeamUrl";

    static final String leaguesTable="Leagues";
    static final String colLeagueID="LeagueID";
    static final String colLeagueName="LeagueName";
    static final String colLeagueCaption="Caption";
    static final String colLeagueYear="Leagueyear";
    static final String colNumOfTeams="NumOfTeams";
    static final String colNumOfGames="NumOfGames";
    static final String colLastUpdated="LastUpdated";
    static final String colTeamsUrl="TeamsURL";
    static final String colFixturesUrl = "FixturesUrl";
    static final String colLeagueTableUrl = "LeagueTableUrl";


    static final String leagueTableRowTable="LeagueTableRows";
    static final String colLeagueTableRowID="LeagueTableRowID";
    static final String colLeagueRowTeamName="LeagueRowTeamName";
    static final String colLeagueRowPosition="LeagueRowPosition";
    static final String colLeagueRowPlayedGames="LeagueRowPlayedGames";
    static final String colLeagueRowPoints="LeagueRowPoints";
    static final String colLeagueRowGoals="LeagueRowGoals";
    static final String colLeagueRowGoalsAgainst="LeagueRowGoalsAgainst";
    static final String colLeagueRowGoalDifference="LeagueRowGoalDifference";
    static final String colLeagueRowLeagueName="LeagueRowLeagueName";
    static final String colLeagueRowTeamUrl="LeagueRowTeamUrl";

    private DatabaseHelper myDBHelper;

    static final String colPlayerLink="PlayerLink";

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public void AddTeam(ArrayList<Team> teamArray)
    {
        for(int i = 0; i < teamArray.size(); i++){
            Team team = teamArray.get(i);

            ContentValues cv = new ContentValues();

            cv.put(colTeamName, team.getName());
            cv.put(colTeamShortName, team.getShortName());
            cv.put(colTeamMarketValue, team.getMarketValue());
            cv.put(colImageURL, team.getImageUrl());
            cv.put(colTeamLeague, team.getLeague());
            cv.put(colPlayersURL, team.getPlayersURL());
            cv.put(colTeamCode, team.getCode());

            db.insert(teamsTable, colTeamName, cv);
        }

    }

    public Cursor getTeamsByLeague(String code)
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+teamsTable + " WHERE " + colTeamLeague + "=" + "'" + code + "'" + " ORDER BY " + colTeamName + " ASC",null);
            if (cur.moveToFirst()) {
                return cur;
            }
            else{
                return null;
            }
        }
        catch(SQLiteException e){
            return null;
        }

    }

    public Cursor getTeamByName(String name)
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+teamsTable + " WHERE " + colTeamName + "=" + "'" + name + "'",null);
            if (cur.moveToFirst()) {
                return cur;
            }
            else{
                return null;
            }
        }
        catch(SQLiteException e){
            return null;
        }

    }

    public String getTeamUrlByName(String name)
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+teamsTable + " WHERE " + colTeamName + "=" + "'" + name + "'",null);
            if (cur.moveToFirst()) {
                return cur.getString(7);
            }
            else{
                return null;
            }
        }
        catch(SQLiteException e){
            return null;
        }

    }

    Cursor getAllTeams()
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cur= db.rawQuery("SELECT * FROM "+teamsTable + " ORDER BY " + colTeamName + " ASC",null);
        return cur;

    }

    public void AddLeague(ArrayList<League> leagueArray)
    {
        for(int i = 0; i < leagueArray.size(); i++){
            League league = leagueArray.get(i);

            ContentValues cv = new ContentValues();

            cv.put(colLeagueName, league.getName());
            cv.put(colLeagueCaption, league.getCaption());
            cv.put(colLeagueYear, league.getYear());
            cv.put(colNumOfTeams, league.getNumOfTeams());
            cv.put(colNumOfGames, league.getNumOfGame());
            cv.put(colLastUpdated, league.getLastUpdated());
            cv.put(colTeamsUrl, league.getTeamsUrl());
            cv.put(colFixturesUrl, league.getFixturesUrl());
            cv.put(colLeagueTableUrl, league.getLeagueTableUrl());

            db.insert(leaguesTable, colTeamName, cv);
        }

    }

    public ArrayList<League> getAllLeagues()
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cursor= db.rawQuery("SELECT * FROM " + leaguesTable + " ORDER BY " + colLeagueName + " ASC", null);
        ArrayList<League> leaguesList = new ArrayList<>();

        if(cursor.moveToFirst()){
            //process data
            do{
                League league = new League();
                league.setName(cursor.getString(1));
                league.setCaption(cursor.getString(2));
                league.setYear(cursor.getInt(3));
                league.setNumOfTeams(cursor.getInt(4));
                league.setNumOfGame(cursor.getInt(5));
                league.setLastUpdated(cursor.getString(6));
                league.setTeamsUrl(cursor.getString(7));
                league.setFixturesUrl(cursor.getString(8));
                league.setLeagueTableUrl(cursor.getString(9));

                leaguesList.add(league);

            }while(cursor.moveToNext());

            cursor.close();
        }
        return leaguesList;
    }

    public Cursor getFixtures(String leagueName) {
        return null;
    }

    public void AddPlayer(Player player)
    {
        ContentValues cv = new ContentValues();

        cv.put(colPlayerName, player.getName());
        cv.put(colPlayerPosition, player.getPosition());
        cv.put(colPlayerJerseyNumber, player.getJerseyNumber());
        cv.put(colPlayerDateOfBirth, player.getDateOfBirth());
        cv.put(colPlayerNationality, player.getNationality());
        cv.put(colPlayerMarketValue, player.getMarketValue());
        cv.put(colPlayerContractUntil, player.getContractUntil());
        cv.put(colPlayerTeam, player.getTeam());

        db.insert(playersTable, colTeamName, cv);
    }

    Cursor getPlayersByTeam(String teamName)
    {
        //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
        Cursor cur= db.rawQuery("SELECT * FROM "+playersTable + " WHERE " + colPlayerTeam + "=" + teamName + "ORDER BY " + colPlayerName + " ASC",null);
        return cur;

    }


    public void AddFixture(Fixture fixture)
    {
        ContentValues cv = new ContentValues();

        cv.put(colFixturestatus, fixture.getStatus());
        cv.put(colFixturematchDay, fixture.getMatchDay());
        cv.put(colFixtureHomeTeamName, fixture.getHomeTeamName());
        cv.put(colFixtureAwayTeamName, fixture.getAwayTeamName());
        cv.put(colFixtureAwayTeamGoals, fixture.getGoalsAwayTeam());
        cv.put(colFixturHomeTeamGoals, fixture.getGoalsHomeTeam());
        cv.put(colFixtureDate, fixture.getDate());
        cv.put(colFixtureLeague, fixture.getLeague());
        cv.put(colFixturHomeTeamUrl, fixture.getHomeTeamNameUrl());
        cv.put(colFixturAwayTeamUrl, fixture.getAwayTeamNameUrl());

        db.insert(FixturesTable, colFixturestatus, cv);

    }

    public void updateFixture(Fixture fixture, String id)
    {
        ContentValues cv = new ContentValues();

        cv.put(colFixturestatus, fixture.getStatus());
        cv.put(colFixturematchDay, fixture.getMatchDay());
        cv.put(colFixtureHomeTeamName, fixture.getHomeTeamName());
        cv.put(colFixtureAwayTeamName, fixture.getAwayTeamName());
        cv.put(colFixtureAwayTeamGoals, fixture.getGoalsAwayTeam());
        cv.put(colFixturHomeTeamGoals, fixture.getGoalsHomeTeam());
        cv.put(colFixtureDate, fixture.getDate());
        cv.put(colFixtureLeague, fixture.getLeague());
        cv.put(colFixturHomeTeamUrl, fixture.getHomeTeamNameUrl());
        cv.put(colFixturAwayTeamUrl, fixture.getAwayTeamNameUrl());

        db.update(FixturesTable, cv, colFixturesID+ "=" + id, null);
    }

    public Cursor getFixturesByLeague(String league)
    {
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+FixturesTable + " WHERE " + colFixtureLeague + "=" + "'" + league + "'",null);
            if (cur.moveToFirst()) {
                return cur;
            }
            else{
                return null;
            }
        }
        catch(SQLiteException e){
            return null;
        }
    }

    public int getFixturesIdByDateAndTeamNames(String date, String hometeam, String awayTeam)
    {
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+FixturesTable + " WHERE " + colFixtureDate + "=" + "'" + date + "' and " +
                    colFixtureHomeTeamName + "=" + "'" + hometeam + "' and " +
                    colFixtureAwayTeamName + "=" + "'" + awayTeam + "'" ,null);
            if (cur.moveToFirst()) {
                return cur.getInt(0);
            }
            else{
                return -1;
            }
        }
        catch(SQLiteException e){
            return -1;
        }
    }

    public void AddLeagueTableRow(LeagueTableRow leagueTableRow)
    {
        ContentValues cv = new ContentValues();

        cv.put(colLeagueRowTeamName, leagueTableRow.getTeamName());
        cv.put(colLeagueRowPosition, leagueTableRow.getPosition());
        cv.put(colLeagueRowPlayedGames, leagueTableRow.getPlayedGames());
        cv.put(colLeagueRowPoints, leagueTableRow.getPoints());
        cv.put(colLeagueRowGoals, leagueTableRow.getGoals());
        cv.put(colLeagueRowGoalsAgainst, leagueTableRow.getGoalsAgainst());
        cv.put(colLeagueRowGoalDifference, leagueTableRow.getGoalDifference());
        cv.put(colLeagueRowLeagueName, leagueTableRow.getLeagueName());

        db.insert(leagueTableRowTable, colFixturestatus, cv);

    }

    public void updateLeagueTableRow(LeagueTableRow leagueTableRow, String id)
    {
        ContentValues cv = new ContentValues();

        cv.put(colLeagueRowTeamName, leagueTableRow.getTeamName());
        cv.put(colLeagueRowPosition, leagueTableRow.getPosition());
        cv.put(colLeagueRowPlayedGames, leagueTableRow.getPlayedGames());
        cv.put(colLeagueRowPoints, leagueTableRow.getPoints());
        cv.put(colLeagueRowGoals, leagueTableRow.getGoals());
        cv.put(colLeagueRowGoalsAgainst, leagueTableRow.getGoalsAgainst());
        cv.put(colLeagueRowGoalDifference, leagueTableRow.getGoalDifference());
        cv.put(colLeagueRowLeagueName, leagueTableRow.getLeagueName());

        db.update(leagueTableRowTable, cv, colLeagueTableRowID+ "=" + id, null);
    }

    public Cursor getLeagueTableRowByLeague(String league)
    {
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+leagueTableRowTable + " WHERE " + colLeagueRowLeagueName + "=" + "'" + league + "'" + "ORDER BY " + colLeagueRowPosition + " ASC",null);
            if (cur.moveToFirst()) {
                return cur;
            }
            else{
                return null;
            }
        }
        catch(SQLiteException e){
            return null;
        }
    }

    public int getLeagueTableRowId(String teamName)
    {
        Cursor cur;
        try{
            cur= db.rawQuery("SELECT * FROM "+leagueTableRowTable + " WHERE " + colLeagueRowTeamName + "=" + "'" + teamName + "'" ,null);
            if (cur.moveToFirst()) {
                return cur.getInt(0);
            }
            else{
                return -1;
            }
        }
        catch(SQLiteException e){
            return -1;
        }
    }

    public void removeAll()
    {
        // Delete all rows of a table
        db.delete(leaguesTable, null, null);
        db.delete(teamsTable, null, null);
        db.delete(playersTable, null, null);
        db.delete(FixturesTable, null, null);
        db.delete(leagueTableRowTable, null, null);


    }

    public boolean isTeamInDataBase(String fieldValue) {
        String Query = "Select * from " + teamsTable + " where " + colTeamName + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            return false;
        }
        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, dbName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE "+teamsTable+" ("+colTeamID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    colTeamName+" TEXT NOT NULL, "+
                    colTeamLeague+" TEXT NOT NULL, "+
                    colTeamMarketValue+" TEXT, "+
                    colTeamShortName+" TEXT, " +
                    colPlayersURL+" TEXT NOT NULL, " +
                    colTeamCode+" TEXT, " +
                    colImageURL + " TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE "+playersTable+" ("+colPlayerID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    colPlayerName+ " TEXT NOT NULL, " +
                    colPlayerPosition + " TEXT NOT NULL, " +
                    colPlayerJerseyNumber+ " INTEGER NOT NULL, " +
                    colPlayerDateOfBirth+ " TEXT NOT NULL, " +
                    colPlayerNationality+ " TEXT NOT NULL, " +
                    colPlayerMarketValue+ " TEXT NOT NULL, " +
                    colPlayerContractUntil+ " TEXT NOT NULL " +
                    ");");

            db.execSQL("CREATE TABLE "+leaguesTable+" ("+colLeagueID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    colLeagueName+ " TEXT NOT NULL, " +
                    colLeagueCaption + " TEXT NOT NULL, " +
                    colLeagueYear+ " INTEGER NOT NULL, " +
                    colNumOfTeams+ " INTEGER NOT NULL, " +
                    colNumOfGames+ " INTEGER NOT NULL, " +
                    colLastUpdated+ " TEXT NOT NULL, " +
                    colTeamsUrl+ " TEXT NOT NULL, " +
                    colFixturesUrl+ " TEXT NOT NULL, " +
                    colLeagueTableUrl+ " TEXT NOT NULL " +
                    ");");

            db.execSQL("CREATE TABLE "+FixturesTable+" ("+colFixturesID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    colFixturestatus+ " TEXT NOT NULL, " +
                    colFixturematchDay + " INTEGER NOT NULL, " +
                    colFixtureHomeTeamName+ " TEXT NOT NULL, " +
                    colFixtureAwayTeamName+ " TEXT NOT NULL, " +
                    colFixtureAwayTeamGoals+ " INTEGER NOT NULL, " +
                    colFixturHomeTeamGoals+ " INTEGER NOT NULL, " +
                    colFixtureDate+ " TEXT NOT NULL, " +
                    colFixtureLeague+ " TEXT NOT NULL, " +
                    colFixturHomeTeamUrl+ " TEXT NOT NULL, " +
                    colFixturAwayTeamUrl+ " TEXT NOT NULL " +
                    ");");

            db.execSQL("CREATE TABLE "+leagueTableRowTable+" ("+colLeagueTableRowID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    colLeagueRowTeamName+ " TEXT NOT NULL, " +
                    colLeagueRowPosition + " INTEGER NOT NULL, " +
                    colLeagueRowPlayedGames+ " INTEGER NOT NULL, " +
                    colLeagueRowPoints+ " INTEGER NOT NULL, " +
                    colLeagueRowGoals+ " INTEGER NOT NULL, " +
                    colLeagueRowGoalsAgainst+ " INTEGER NOT NULL, " +
                    colLeagueRowGoalDifference+ " INTEGER NOT NULL, " +
                    colLeagueRowLeagueName+ " TEXT NOT NULL, " +
                    colLeagueRowTeamUrl+ " TEXT " +
                    ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+teamsTable);
            db.execSQL("DROP TABLE IF EXISTS "+playersTable);
            db.execSQL("DROP TABLE IF EXISTS "+FixturesTable);
            db.execSQL("DROP TABLE IF EXISTS "+leagueTableRowTable);
        }
    }
}
