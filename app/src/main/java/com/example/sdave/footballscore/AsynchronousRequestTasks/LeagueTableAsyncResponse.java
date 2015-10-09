package com.example.sdave.footballscore.AsynchronousRequestTasks;

import com.example.sdave.footballscore.Objects.LeagueTableRow;

import java.util.ArrayList;

/**
 * Created by Sagar on 4/26/2015.
 */
public interface LeagueTableAsyncResponse {
    void processFinish(ArrayList<LeagueTableRow> fixturesList);
}
