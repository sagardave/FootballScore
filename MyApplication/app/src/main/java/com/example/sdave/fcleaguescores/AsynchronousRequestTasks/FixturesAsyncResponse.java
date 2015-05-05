package com.example.sdave.fcleaguescores.AsynchronousRequestTasks;

import com.example.sdave.fcleaguescores.Objects.Fixture;

import java.util.ArrayList;

/**
 * Created by Sagar on 4/26/2015.
 */
public interface FixturesAsyncResponse {
    void processFinish(ArrayList<Fixture> fixturesList);
}
