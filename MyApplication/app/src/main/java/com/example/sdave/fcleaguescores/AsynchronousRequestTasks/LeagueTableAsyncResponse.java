package com.example.sdave.fcleaguescores.AsynchronousRequestTasks;

import com.example.sdave.fcleaguescores.Objects.LeagueTableRow;

import java.util.ArrayList;

/**
 * Created by Sagar on 4/26/2015.
 */
public interface LeagueTableAsyncResponse {
    void processFinish(ArrayList<LeagueTableRow> fixturesList);
}
